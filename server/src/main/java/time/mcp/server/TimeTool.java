package time.mcp.server;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CreateMessageResult;
import io.modelcontextprotocol.spec.McpSchema.ModelPreferences;
import io.modelcontextprotocol.spec.McpSchema.SamplingMessage;


@Service
public class TimeTool {

    @McpTool(name = "get_time", description = "Get the current time in a specified location")

    public String getCurrentTime(McpSyncServerExchange exchange,
                                 @McpToolParam(description = "Country or city") String location) {

        String timezone = null;

        // Check if client supports sampling
        if (exchange.getClientCapabilities().sampling() != null) 
        {

            // Create the sampling message
            SamplingMessage samplingMessage = new SamplingMessage(McpSchema.Role.USER,
            new McpSchema.TextContent("Convert this location: "+location+" to a valid IANA timezone"));

            // Build the sampling request
            var samplingRequest = McpSchema.CreateMessageRequest.builder()
                    .systemPrompt("You are an assistant that converts cities/countries into valid IANA timezones.")
                    .messages(List.of(samplingMessage))
                    .modelPreferences(ModelPreferences.builder()

                        .build())
                    .build();

            try 
            {
                CreateMessageResult samplingResponse = exchange.createMessage(samplingRequest);
                timezone = ((McpSchema.TextContent) samplingResponse.content()).text();
            }

            catch (Exception e) 
            {
                return "❌ Sampling failed for location: " + location + " (" + e.getMessage() + ")";
            }
        }

        if (timezone == null || timezone.isBlank() || timezone.equalsIgnoreCase("null")) {
            return "❌ Unknown or invalid location: " + location;
        }

        try 
        {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timezone));
            return "🕒 Time in " + location + " (" + timezone + "): " + now.toString();
        } 
        catch (Exception e) 
        {
            return "❌ Failed to use timezone: " + timezone + " (resolved from: " + location + ")";
        }
    }
}


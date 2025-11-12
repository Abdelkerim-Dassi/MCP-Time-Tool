package time.mcp.client;

import org.springaicommunity.mcp.annotation.McpSampling;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.spec.McpSchema.CreateMessageRequest;
import io.modelcontextprotocol.spec.McpSchema.CreateMessageResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import jakarta.annotation.PostConstruct;




@Component
public class TimeZoneSampling {

    private final ChatClient chatClient;

    public TimeZoneSampling(@Lazy ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @McpSampling(clients = "mcp-server-time")
    public CreateMessageResult samplingHandler(CreateMessageRequest llmRequest) {

        String userPrompt = ((TextContent) llmRequest.messages().get(0).content()).text();

        String llmResponse = chatClient
                .prompt()
                .system(llmRequest.systemPrompt())
                .user(userPrompt)
                .options(OpenAiChatOptions.builder().maxTokens(70).build())

                .call()
                .content();

        return CreateMessageResult.builder()
                .content(new TextContent(llmResponse))
                .build();
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ TimeZoneSampling bean created successfully!");
    }

}



package time.mcp.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient groqChatClient(OpenAiChatModel chatModel,
           ToolCallbackProvider tools) {


        return ChatClient.builder(chatModel)
                .defaultToolCallbacks(tools)
                .build();
    }
}
    

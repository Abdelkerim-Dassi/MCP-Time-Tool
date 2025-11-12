package time.mcp.client;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

 
@RestController
@RequestMapping("/api/timezone")


public class ClientController {

    private final ChatClient chatClient;


       public ClientController(@Lazy ChatClient chatClient) {
        this.chatClient = chatClient;
    }

     @PostMapping("/prompt")
    public String sendPrompt(@RequestBody String request) {

        // Call the ChatClient with user prompt
        return chatClient.prompt()
                .user(request)
                .call()
                .content();
    }
}

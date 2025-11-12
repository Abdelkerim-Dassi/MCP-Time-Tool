package time.mcp.client;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.ai.mcp.client.enabled=false",
        "spring.ai.tool-callback.enabled=false"
})
class ClientApplicationTests {}

package blog.integration;

import blog.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = Application.class, // 找到 spring 的启动类
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT // 为 spring 指定随机端口，以免端口占用
)
@TestPropertySource(locations = "classpath:test.properties") // 用于测试的配置，只是改了下数据库端口
public class MyIntegrationTest {
    @Inject
    Environment environment; // 拿到 spring 应用启动的随机端口号

    @Test
    void notLoggedInByDefault() throws IOException, InterruptedException {
        System.out.println("------------- 喵喵喵 -----------");
//        String port = environment.getProperty("local.server.port");
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:" + port + "/auth"))
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response.statusCode());
//        System.out.println(response.body());
//
//        Assertions.assertEquals(200, response.statusCode());
//        Assertions.assertTrue(response.body().contains("用户没有登录"));
    }

    // TODO: 暂时不用 Java11，还是继续用 Java8 更兼容其他工具一些。
    // TODO: To add more integration tests such as register, login, and logout.
}

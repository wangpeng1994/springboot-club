package blog.integration;

import blog.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

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
    void indexHtmlIsAccessible() {
        System.out.println("------------- 喵喵喵 -----------");
        System.out.println(environment.getProperty("local.server.port"));
    }

}

// TODO: 使用 java11 自带的http client

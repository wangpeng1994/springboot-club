package blog;

import org.junit.jupiter.api.Test; // Junit 内置的测试引擎

public class SmokeTest {
    @Test
    public void test() {
        System.out.println("我在测试");
//        throw new RuntimeException("运行时报错");
    }
}

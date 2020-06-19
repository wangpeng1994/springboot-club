package blog.controller;

import blog.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc; // spring test 提供的假的 MVC

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        // 只测试一个 AuthController
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
    }

    /**
     * 在 JVM 某个地方，测试框架自动创建了测试实例，每个 @Test 测试条目所在的实例都是独立的
     * 同理 @BeforeEach 中的 each，也暗示了是每一个 @Test 测试用例之前都要做的事情
     */

    @Test
    void returnNotLoginByDefault() throws Exception {
        ResultActions resultActions = mvc.perform(get("/auth"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录")));
    }

    @Test
    void testLogin() throws Exception {
        // 未登录时，/auth接口返回未登录状态
        ResultActions resultActions = mvc.perform(get("/auth"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录")));

        // 使用 /auth/login 登录
        Map<String, String> usernamePassword = new HashMap<>(); // 写入Map使用Jackson转为JSON
        usernamePassword.put("username", "myUsername");
        usernamePassword.put("password", "myPassword");

        Mockito.when(userService.loadUserByUsername("myUsername"))
                .thenReturn(new User("myUsername", bCryptPasswordEncoder.encode("myPassword"), Collections.emptyList()));
        Mockito.when(userService.getUserByUsername("myUsername"))
                .thenReturn(new blog.entity.User(1, "myUsername", bCryptPasswordEncoder.encode("myPassword")));

        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");

        MvcResult mvcResult = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(usernamePassword)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录成功")))
                .andReturn();

        // 这里的单元测试不会实际走鉴权种cookie那一套，也是个 mockSession
        // session 代表了一段时间内的会话
        HttpSession session = mvcResult.getRequest().getSession();

        // 登录后再次请求 /auth 显示登录的用户信息
        mvc.perform(get("/auth").session((MockHttpSession) session).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("myUsername"));
                });
    }
}

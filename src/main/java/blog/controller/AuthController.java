package blog.controller;

import blog.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class AuthController {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        return new Result("ok", "用户没有登录", false);
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        // 让 spring 来鉴权

        // （假装）从数据库中尝试拿到该 username 的真正信息，结合传入的 password 生成鉴权 token
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return new Result("fail", "用户不存在", false);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );

        try {
            // 拿着 token 开始鉴权，如果密码错误会抛 BadCredentialsException 异常
            authenticationManager.authenticate(token);
            // 鉴权成功后会把凭证信息存回原 token 中
            SecurityContextHolder.getContext().setAuthentication(token);
            User loggedInUser = new User(1, "张三");
            Result result = new Result("ok", "登录成功", true, loggedInUser);
            System.out.println(result);
            return result;
        } catch (BadCredentialsException e) {
            // 密码错误，鉴权失败
            return new Result("fail", "密码不正确", false);
        }
    }

    private static class Result {
        private String status;
        private String msg;
        private Boolean isLogin;
        Object data;

        public Result(String status, String msg, Boolean isLogin) {
            this(status, msg, isLogin, null);
        }

        public Result(String status, String msg, Boolean isLogin, User data) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }

        public Boolean isIsLogin() {
            return isLogin;
        }

        public Object getData() {
            return data;
        }
    }
}
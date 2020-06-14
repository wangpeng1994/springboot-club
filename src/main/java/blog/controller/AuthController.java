package blog.controller;

import blog.entity.Result;
import blog.entity.User;
import blog.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Object register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        if (username == null || password == null) {
            return Result.failure("用户名或密码为空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return Result.failure("用户名格式不正确");
        }
        if (password.length() < 6 || password.length() > 16) {
            return Result.failure("密码格式不正确");
        }
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return Result.failure("用户名已存在");
        }

        // TODO 注册成功后自动登录，待改进
        UserDetails userDetails = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(token);

        return new Result("ok", "注册成功", false, userService.getUserByUsername(username));
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        // 如果没登录，或鉴权不通过，拿到的 username 是 "anonymousUser"
        // 若通过了鉴权，则能拿到当前的 username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser = userService.getUserByUsername(username);

        if (loggedInUser == null) {
            return new Result("ok", "用户没有登录", false);
        }
        return new Result("ok", null, true, loggedInUser);
    }



    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        // 让 spring 来鉴权
        // 从数据库中尝试拿到该用户信息，结合传入的 password 生成鉴权 token
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failure("用户不存在");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );

        try {
            // 拿着 token 开始鉴权，如果密码错误会抛 BadCredentialsException 异常
            authenticationManager.authenticate(token);
            // 鉴权成功后，更新当前用户的认证信息，保存在一个地方（内存中），会设置 set-cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok", "登录成功", true, userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            // 密码错误，鉴权失败
            return Result.failure("密码不正确");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(username);

        if (loggedInUser == null) {
            return Result.failure("用户尚未登录");
        }
        SecurityContextHolder.clearContext();
        return new Result("ok", "注销成功", false);
    }
}

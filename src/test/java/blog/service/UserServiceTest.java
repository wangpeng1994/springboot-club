package blog.service;

import blog.entity.User;
import blog.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock // Mockito 基于继承进行Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks // UserService 是真实的代码实现，其所依赖的东西需要注入Mock
    UserService userService;

    @Test
    public void testSave() {
        // 调用 userService，验证 userService 将请求转发给了 userMapper

        // mock 出来的加密器太假了，需要进一步 mock 加密器中的方法
        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");

        // 调用方法
        userService.save("myUsername", "myPassword");

        // 验证刚才的调用转发给了 mockMapper.insertuser
        // 这里是期望，看刚才的调用是否符合这里的期望
        Mockito.verify(mockMapper).insertUser("myUsername", "myEncodedPassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUsername("myUsername");
        Mockito.verify(mockMapper).findUserByUsername("myUsername");
    }

    @Test
    public void throwExceptionWhenUserNotfound() {
        Mockito.when(mockMapper.findUserByUsername("myUsername")).thenReturn(null);

        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("myUsername"));
    }

    @Test void returnUserDetailsWhenUserFound() {
        Mockito.when(mockMapper.findUserByUsername("myUsername"))
                .thenReturn(new User(1, "myUsername", "myEncodedPassword"));

        UserDetails userDetails = userService.loadUserByUsername("myUsername");

        Assertions.assertEquals("myUsername", userDetails.getUsername());
        Assertions.assertEquals("myEncodedPassword", userDetails.getPassword());
    }

}
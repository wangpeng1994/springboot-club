package blog.service;

import blog.entity.User;
import blog.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserService {
    private UserMapper userMapper;

    @Inject
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserById(Integer id) {
        return userMapper.findUserById(id);
    }
}
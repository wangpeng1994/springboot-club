package blog.mapper;

import blog.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Insert("insert into user (username, encrypted_password, created_at, updated_at) " +
            "values (#{username}, #{encryptedPassword}, now(), now())") // TODO 其实有点问题
    void insertUser(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);
}

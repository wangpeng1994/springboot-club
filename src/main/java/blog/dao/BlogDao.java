package blog.dao;

import blog.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);
        return sqlSession.selectList("BlogMapper.selectBlog", params);
    }

    public int count(Integer userId) {
        return sqlSession.selectOne("BlogMapper.countBlog", userId);
    }
}

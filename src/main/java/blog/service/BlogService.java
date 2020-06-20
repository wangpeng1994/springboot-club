package blog.service;

import blog.dao.BlogDao;
import blog.entity.Blog;
import blog.entity.BlogResult;
import blog.entity.Result;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private BlogDao blogDao;
    private UserService userService;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService) {
        this.blogDao = blogDao;
        this.userService = userService;
    }

    public Result getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);
            int count = blogDao.count(userId);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1; // 总的页数
            return BlogResult.newBlogs(blogs, count, page, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("系统异常");
        }
    }
}

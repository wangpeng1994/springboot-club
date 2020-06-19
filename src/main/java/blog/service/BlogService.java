package blog.service;

import blog.dao.BlogDao;
import blog.entity.Blog;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        return blogDao.getBlogs(page, pageSize, userId);
    }
}

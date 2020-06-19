package blog.service;

import blog.dao.BlogDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    BlogDao blogDao;

    @InjectMocks
    BlogService blogService;

    @Test
    public void getBlogsFromDb() {
        blogService.getBlogs(1, 1, null);

        // 验证上面的调用是否调用了 blogDao 的方法
        Mockito.verify(blogDao).getBlogs(1, 1, null);
    }
}

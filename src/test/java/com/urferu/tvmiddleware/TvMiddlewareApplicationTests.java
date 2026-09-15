package com.urferu.tvmiddleware;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.urferu.tvmiddleware.repository.CommentRepository;
import com.urferu.tvmiddleware.repository.ShowCacheRepository;

@SpringBootTest
class TvMiddlewareApplicationTests {

    @MockitoBean
    private ShowCacheRepository showCacheRepository;

    @MockitoBean
    private CommentRepository commentRepository;

    @Test
    void contextLoads() {
    }
}

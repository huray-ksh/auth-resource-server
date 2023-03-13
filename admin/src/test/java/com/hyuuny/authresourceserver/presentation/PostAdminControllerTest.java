package com.hyuuny.authresourceserver.presentation;

import com.hyuuny.authresourceserver.application.PostService;
import com.hyuuny.authresourceserver.application.dto.PostDto;
import com.hyuuny.authresourceserver.common.BaseIntegrationTests;
import com.hyuuny.authresourceserver.domain.repository.PostRepository;
import com.hyuuny.authresourceserver.infrastructure.create.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import static com.hyuuny.authresourceserver.Fixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class PostAdminControllerTest extends BaseIntegrationTests {

    private final String REQUEST_PATH = "/admin/posts";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private RedisRepository repository;

    @AfterEach
    void tearDown() {
        deleteUsers();
        postRepository.deleteAll();

        log.info("PostAdminControllerTest.cache.clear()");
        repository.clear();
    }

    @DisplayName("관리자는 게시물 등록 가능")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void createPostByAdmin() throws Exception {
        PostDto.Create create = aPost().build();

        ResultActions resultActions = this.mockMvc.perform(post(REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(create))
                )
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions
                .andExpect(jsonPath("code").value("20100"))
                .andExpect(jsonPath("message").value("CREATED"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").value(create.getTitle()))
                .andExpect(jsonPath("$.data.content").value(create.getContent()))
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists())
        ;
    }

    @DisplayName("매니저가 게시물 등록하면 인가 예외 발생")
    @WithUserDetails(value = MANAGER_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void createPostByManagerEx() throws Exception {
        PostDto.Create create = aPost().build();

        mockMvc.perform(post(REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(create))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("관리자는 게시물 상세조회 가능")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void getPostByAdmin() throws Exception {
        PostDto.Create create = aPost().build();
        PostDto.Response savedPost = postService.createPost(create);

        ResultActions resultActions = this.mockMvc.perform(get(REQUEST_PATH + "/{id}", savedPost.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("code").value("20000"))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").value(create.getTitle()))
                .andExpect(jsonPath("$.data.content").value(create.getContent()))
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists())
        ;
    }

    @DisplayName("매니저는 게시물 상세조회 가능")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void getPostByManager() throws Exception {
        PostDto.Create create = aPost().build();
        PostDto.Response savedPost = postService.createPost(create);

        ResultActions resultActions = this.mockMvc.perform(get(REQUEST_PATH + "/{id}", savedPost.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(MANAGER_EMAIL, MANAGER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("code").value("20000"))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").value(create.getTitle()))
                .andExpect(jsonPath("$.data.content").value(create.getContent()))
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists())
        ;
    }

    @DisplayName("일반 회원이 게시물 상세 조회하면 인가 예외 발생")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void getPostByUser() throws Exception {
        PostDto.Create create = aPost().build();
        PostDto.Response savedPost = postService.createPost(create);

        this.mockMvc.perform(get(REQUEST_PATH + "/{id}", savedPost.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(USER_EMAIL, USER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("게시물 삭제는 관리자만 가능")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void deletePost() throws Exception {
        PostDto.Create create = aPost().build();
        PostDto.Response savedPost = postService.createPost(create);

        ResultActions resultActions = this.mockMvc.perform(delete(REQUEST_PATH + "/{id}", savedPost.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("code").value("20000"))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("$.data").value(savedPost.getId()))
        ;
    }

    @DisplayName("매니저가 게시물 삭제하면 인가 예외 발생")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void deletePostByManagerEx() throws Exception {
        PostDto.Create create = aPost().build();
        PostDto.Response savedPost = postService.createPost(create);

        this.mockMvc.perform(delete(REQUEST_PATH + "/{id}", savedPost.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(MANAGER_EMAIL, MANAGER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
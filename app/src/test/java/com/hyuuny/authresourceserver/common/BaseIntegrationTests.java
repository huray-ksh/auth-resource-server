package com.hyuuny.authresourceserver.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyuuny.authresourceserver.application.dto.AuthenticationDto;
import com.hyuuny.authresourceserver.config.TestConfig;
import com.hyuuny.authresourceserver.domain.repository.UserRepository;
import com.hyuuny.authresourceserver.infrastructure.create.RedisRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static com.hyuuny.authresourceserver.Fixture.ADMIN_EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@Import(TestConfig.class)
@MockMvcCustomConfig
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.config.location="
                + "classpath:application-test.yaml"
)
public abstract class BaseIntegrationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected RedisRepository redisRepository;

    protected String getBearerToken(final String username, final String password) {
        AuthenticationDto.LoginDto loginDto = AuthenticationDto.LoginDto.builder()
                .username(username)
                .password(password)
                .build();
        try {
            return getBearerToken(loginDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getBearerToken(AuthenticationDto.LoginDto loginDto) throws Exception {
        return "Bearer " + getAccessToken(loginDto);
    }

    protected String getAccessToken(AuthenticationDto.LoginDto loginDto) throws Exception {
        ResultActions perform = this.mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(loginDto)));

        String responseBody = perform.andReturn().getResponse().getContentAsString();
        AuthenticationDto.Data data = objectMapper.readValue(responseBody, AuthenticationDto.Data.class);
        return data.getData().getAccessToken().getAccessToken();
    }

    public void deleteUsers() {
        userRepository.findAll().stream()
                .filter(user -> !Objects.equals(user.getUserAccount().getUsername(), ADMIN_EMAIL))
                .forEach(user -> userRepository.deleteById(user.getId()));
    }

}

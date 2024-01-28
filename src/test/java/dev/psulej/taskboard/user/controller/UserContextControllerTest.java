package dev.psulej.taskboard.user.controller;

import dev.psulej.taskboard.config.TestConfiguration;
import dev.psulej.taskboard.security.TokenProvider;
import dev.psulej.taskboard.user.api.ApplicationTheme;
import dev.psulej.taskboard.user.api.UserContext;
import dev.psulej.taskboard.user.service.UserContextService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserContextController.class)
@Import(TestConfiguration.class)
@WithMockUser
class UserContextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    private UserContextService userContextService;

    @Test
    void when_GetLoggedUserContext_shouldReturn200StatusCode_andUserContext() throws Exception {
        // given
        UserContext userContext = UserContext.builder()
                .login("johndoe")
                .id(UUID.fromString("fca6845c-92d2-4e8f-98c7-88d2832f0311"))
                .email("johndoe@localhost.com")
                .name("John Doe")
                .imageId(UUID.fromString("c9370954-eab1-4e99-9e97-6dc845c1b433"))
                .applicationTheme(new ApplicationTheme("dark"))
                .build();

        when(userContextService.getUserLoggedUserContext()).thenReturn(userContext);

        // when
        this.mockMvc.perform(get("/user-context"))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                    "id" : "fca6845c-92d2-4e8f-98c7-88d2832f0311",
                                    "login": "johndoe",
                                    "email" : "johndoe@localhost.com",
                                    "name": "John Doe",
                                    "imageId" : "c9370954-eab1-4e99-9e97-6dc845c1b433",
                                    "applicationTheme" : "dark"
                                }
                                """
                ));
    }

    @Test
    @WithAnonymousUser
    void when_UnauthorizedGetLoggedUserContext_shouldReturn403StatusCode() throws Exception {
        // given
        UserContext userContext = UserContext.builder()
                .login("johndoe")
                .id(UUID.fromString("fca6845c-92d2-4e8f-98c7-88d2832f0311"))
                .email("johndoe@localhost.com")
                .name("John Doe")
                .imageId(UUID.fromString("c9370954-eab1-4e99-9e97-6dc845c1b433"))
                .applicationTheme(new ApplicationTheme("dark"))
                .build();

        when(userContextService.getUserLoggedUserContext()).thenReturn(userContext);

        // when
        this.mockMvc.perform(get("/user-context"))
                .andDo(print())

                // then
                .andExpect(status().isUnauthorized());
    }
}
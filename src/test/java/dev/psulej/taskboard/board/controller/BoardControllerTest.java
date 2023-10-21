package dev.psulej.taskboard.board.controller;

import dev.psulej.taskboard.board.api.Board;
import dev.psulej.taskboard.board.service.BoardService;
import dev.psulej.taskboard.config.TestConfiguration;
import dev.psulej.taskboard.security.TokenProvider;
import dev.psulej.taskboard.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@Import(TestConfiguration.class)
class BoardControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    BoardService boardService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Get board test")
    void shouldReturnSuccessResponse() throws Exception {
        // given
        UUID boardId = UUID.fromString("4a000f56-6c84-4e6c-8601-e34419c202d1");
        Board board = Board.builder()
                .id(boardId)
//                .users(
//                        User.builder()
//                                .build(),
//                )

                .build();

        when(boardService.getBoard(boardId)).thenReturn(board);
        // when
        this.mockMvc.perform(get("/boards/" + boardId))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                    "id": "4a000f56-6c84-4e6c-8601-e34419c202d1"
                                }
                                """));
    }
}
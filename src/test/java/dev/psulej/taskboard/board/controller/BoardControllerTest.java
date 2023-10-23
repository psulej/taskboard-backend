package dev.psulej.taskboard.board.controller;

import dev.psulej.taskboard.board.api.*;
import dev.psulej.taskboard.board.domain.TaskPriority;
import dev.psulej.taskboard.board.service.BoardService;
import dev.psulej.taskboard.config.TestConfiguration;
import dev.psulej.taskboard.security.TokenProvider;
import dev.psulej.taskboard.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void shouldReturnGetBoardSuccessResponse() throws Exception {
        // given
        UUID boardId = UUID.fromString("4a000f56-6c84-4e6c-8601-e34419c202d1");

        User firstBoardUser = User.builder()
                .id(UUID.fromString("29653bcb-a57e-4ac8-a87b-c5219ba1f5cf"))
                .login("testUser")
                .name("Johnny")
                .avatarColor("#B80000")
                .imageId(UUID.fromString("fe1b8a0c-67fe-49e9-a98f-1555e702711e"))
                .build();

        User secondBoardUser = User.builder()
                .id(UUID.fromString("108a70de-d64e-4b3a-a0cf-cc6190daba6e"))
                .login("testUser2")
                .name("Susan")
                .avatarColor("#B80000")
                .imageId(UUID.fromString("4a000f56-6c84-4e6c-8601-e34419c202d1"))
                .build();

        Board board = Board.builder()
                .id(boardId)
                .users(
                        List.of(firstBoardUser,
                                secondBoardUser)
                )
                .columns(
                        List.of(
                                Column.builder()
                                        .id(UUID.fromString("82f852ca-7101-11ee-b962-0242ac120002"))
                                        .name("testColumn")
                                        .tasks(List.of(
                                                Task.builder()
                                                        .id(UUID.fromString("13fe8fb2-7104-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle")
                                                        .description("testTaskDescription")
                                                        .assignedUser(firstBoardUser)
                                                        .priority(TaskPriority.NORMAL)
                                                        .build(),
                                                Task.builder()
                                                        .id(UUID.fromString("1e1fbf98-7104-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle2")
                                                        .description("testTaskDescription2")
                                                        .assignedUser(secondBoardUser)
                                                        .priority(TaskPriority.HIGH)
                                                        .build())).build(),
                                Column.builder()
                                        .id(UUID.fromString("f335d812-7103-11ee-b962-0242ac120002"))
                                        .name("testColumn2")
                                        .tasks(List.of(
                                                Task.builder()
                                                        .id(UUID.fromString("eba6baf0-7106-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle3").description("testTaskDescription3")
                                                        .assignedUser(null).priority(TaskPriority.LOW).build(),
                                                Task.builder()
                                                        .id(UUID.fromString("efce1b28-7106-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle4").description("testTaskDescription4")
                                                        .assignedUser(secondBoardUser)
                                                        .priority(null)
                                                        .build()))
                                        .build()))
                .build();


        when(boardService.getBoard(boardId)).thenReturn(board);
        // when
        this.mockMvc.perform(get("/boards/" + boardId))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(content().json("""
                        {
                            "id" : "4a000f56-6c84-4e6c-8601-e34419c202d1",
                            "users" : 
                                [
                                    {
                                        "id" : "29653bcb-a57e-4ac8-a87b-c5219ba1f5cf",
                                        "login" : "testUser",
                                        "name" : "Johnny",
                                        "avatarColor" : "#B80000",
                                        "imageId" : "fe1b8a0c-67fe-49e9-a98f-1555e702711e"
                                    },
                                    {
                                        "id" : "108a70de-d64e-4b3a-a0cf-cc6190daba6e",
                                        "login" : "testUser2",
                                        "name" : "Susan",
                                        "avatarColor" : "#B80000",
                                        "imageId" : "4a000f56-6c84-4e6c-8601-e34419c202d1"
                                    }
                                ],
                            "columns" :
                                [
                                    {
                                        "id" : "82f852ca-7101-11ee-b962-0242ac120002",
                                        "name" : "testColumn",
                                        "tasks" : 
                                        [
                                            {
                                                "id" : "13fe8fb2-7104-11ee-b962-0242ac120002",
                                                "title" : "testTaskTitle",
                                                "description" : "testTaskDescription",
                                                "assignedUser" : 
                                                 {
                                                    "id" : "29653bcb-a57e-4ac8-a87b-c5219ba1f5cf",
                                                    "login" : "testUser",
                                                    "name" : "Johnny",
                                                    "avatarColor" : "#B80000",
                                                    "imageId" : "fe1b8a0c-67fe-49e9-a98f-1555e702711e"
                                                },
                                                "priority" : "NORMAL"
                                            },
                                                                                                {
                                                "id" : "1e1fbf98-7104-11ee-b962-0242ac120002",
                                                "title" : "testTaskTitle2",
                                                "description" : "testTaskDescription2",
                                                "assignedUser" : 
                                                 {
                                                    "id" : "108a70de-d64e-4b3a-a0cf-cc6190daba6e",
                                                    "login" : "testUser2",
                                                    "name" : "Susan",
                                                    "avatarColor" : "#B80000",
                                                    "imageId" : "4a000f56-6c84-4e6c-8601-e34419c202d1"
                                                },
                                                "priority" : "HIGH"
                                            }
                                        ]
                                    },
                                    {
                                        "id" : "f335d812-7103-11ee-b962-0242ac120002",
                                        "name" : "testColumn2",
                                        "tasks" : 
                                        [
                                            {
                                                "id" : "eba6baf0-7106-11ee-b962-0242ac120002",
                                                "title" : "testTaskTitle3",
                                                "description" : "testTaskDescription3",
                                                "assignedUser" : null,
                                                "priority" : "LOW"
                                            },
                                                                                                {
                                                "id" : "efce1b28-7106-11ee-b962-0242ac120002",
                                                "title" : "testTaskTitle4",
                                                "description" : "testTaskDescription4",
                                                "assignedUser" : 
                                                 {
                                                    "id" : "108a70de-d64e-4b3a-a0cf-cc6190daba6e",
                                                    "login" : "testUser2",
                                                    "name" : "Susan",
                                                    "avatarColor" : "#B80000",
                                                    "imageId" : "4a000f56-6c84-4e6c-8601-e34419c202d1"
                                                },
                                                "priority" : null
                                            }
                                        ]
                                    }
                                    
                                ]
                        }
                        """));
    }

    @Test
    @DisplayName("Add board test")
    void shouldReturnAddBoardSuccessResponse() throws Exception {

        // given
        UUID boardId = UUID.fromString("b4a5fdd3-b467-4c61-81b4-83a243d001e2");
        String boardName = "testowyBoard";


        Board board = Board.builder()
                .id(boardId)
                .name(boardName)
                .users(List.of(
                        User.builder()
                                .id(UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"))
                                .login("testUser123")
                                .name("Mr")
                                .avatarColor("#B80000")
                                .build()))
                .columns(
                        List.of())
                .build();

        when(boardService.addBoard(new CreateBoard(boardName))).thenReturn(board);

        // when
        this.mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + boardName + "\"}"))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("""
                                   {
                                     "id": "b4a5fdd3-b467-4c61-81b4-83a243d001e2",
                                     "name": "testowyBoard",
                                     "users": [
                                       {
                                         "id": "461c84d0-2233-433b-9784-4bf32cd81d6e",
                                         "login": "testUser123",
                                         "name": "Mr",
                                         "imageId": null,
                                         "avatarColor": "#B80000"
                                       }
                                     ],
                                     "columns": []
                                   }
                                """));

    }

    @Test
    @DisplayName("Edit board test")
    void shouldEditBoardReturnSuccess() throws Exception {
        // given
        UUID boardId = UUID.fromString("2b31501a-0e74-4515-b1e6-e5a4c8518924");
        String boardName = "testowyBoard";


        Board board = Board.builder()
                .id(boardId)
                .name(boardName)
                .users(List.of(
                        User.builder()
                                .id(UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"))
                                .login("testUser123")
                                .name("Mr")
                                .avatarColor("#B80000")
                                .build()))
                .columns(
                        List.of())
                .build();


        when(boardService.editBoard(boardId, new UpdateBoard(boardName, List.of(
                UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"),
                UUID.fromString("99b65bfa-7120-11ee-b962-0242ac120002")

        )))).thenReturn(board);

        // when
        this.mockMvc.perform(put("/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "testowyBoard",
                                    "userIds": ["461c84d0-2233-433b-9784-4bf32cd81d6e", "99b65bfa-7120-11ee-b962-0242ac120002"]
                                }
                                """)
                )

                // then
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("""
                                   {
                                     "id": "2b31501a-0e74-4515-b1e6-e5a4c8518924",
                                     "name": "testowyBoard",
                                     "users": [
                                       {
                                         "id": "461c84d0-2233-433b-9784-4bf32cd81d6e",
                                         "login": "testUser123",
                                         "name": "Mr",
                                         "avatarColor": "#B80000"
                                       }
                                     ],
                                     "columns": []
                                   }
                                """));
    }

    @Test
    @DisplayName("Delete board test")
    void shouldDeleteBoardReturnSuccess() throws Exception {
        // given
        UUID boardId = UUID.fromString("3eed8d1c-7128-11ee-b962-0242ac120002");

        Mockito.doNothing().when(boardService).deleteBoard(boardId);

        // when
        this.mockMvc.perform(delete("/boards/3eed8d1c-7128-11ee-b962-0242ac120002"))
                // then
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(boardService, Mockito.times(1)).deleteBoard(boardId);
    }

    @Test
    @DisplayName("Get boards test")
    void shouldGetBoardReturnSuccess() throws Exception {

        // given
        List<AvailableBoard> availableBoards = List.of(
                new AvailableBoard(UUID.fromString("e769f2a6-712c-11ee-b962-0242ac120002"), "testBoard1"),
                new AvailableBoard(UUID.fromString("06fa66aa-712d-11ee-b962-0242ac120002"), "testBoard2"),
                new AvailableBoard(UUID.fromString("1104c2bc-712d-11ee-b962-0242ac120002"), "testBoard3")
        );

        when(boardService.getAvailableBoards()).thenReturn(
                availableBoards
        );

        // when
        this.mockMvc.perform(get("/boards"))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        """
                                [
                                    {
                                        "id" :  "e769f2a6-712c-11ee-b962-0242ac120002",
                                        "name" : "testBoard1"
                                    },
                                    {
                                        "id" :  "06fa66aa-712d-11ee-b962-0242ac120002",
                                        "name" : "testBoard2"
                                    },
                                    {
                                        "id" :  "1104c2bc-712d-11ee-b962-0242ac120002",
                                        "name" : "testBoard3"
                                    }
                                ]
                                """
                ));
    }

    @Test
    @DisplayName("Get assignable users for board test")
    void shouldGetAssignableUserReturnSuccess() throws Exception {
        // given
        UUID boardId = UUID.fromString("33e34bae-712e-11ee-b962-0242ac120002");
        String loginPhrase = "test";
        List<UUID> excludedUserIds = List.of(
                UUID.fromString("5736a48a-7132-11ee-b962-0242ac120002"),
                UUID.fromString("0a71105c-7139-11ee-b962-0242ac120002")
        );

        List<User> users = List.of(
                User.builder()
                        .id(UUID.fromString("23a91c24-7132-11ee-b962-0242ac120002"))
                        .login("testUser1")
                        .name("Johnny")
                        .avatarColor("#B80000")
                        .imageId(null)
                        .build(),
                User.builder()
                        .id(UUID.fromString("3ba2bb96-7132-11ee-b962-0242ac120002"))
                        .login("testUser2")
                        .name("Marry")
                        .avatarColor("#B80000")
                        .imageId(null)
                        .build()
        );
        when(boardService.getAssignableUsers(boardId, loginPhrase, excludedUserIds))
                .thenReturn(users);

        // when
        this.mockMvc.perform(get("/boards/33e34bae-712e-11ee-b962-0242ac120002/assignable-users")
                        .queryParam("loginPhrase", loginPhrase)
                        .queryParam("excludedUserIds", excludedUserIds.stream().map(UUID::toString).collect(Collectors.joining(",")))
                )
                // then
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                                """
                                        [
                                            {
                                                "id" :  "23a91c24-7132-11ee-b962-0242ac120002",
                                                "login" : "testUser1",
                                                "name" : "Johnny",
                                                "imageId" : null,
                                                "avatarColor" : "#B80000"
                                            },
                                            {
                                                "id" :  "3ba2bb96-7132-11ee-b962-0242ac120002",
                                                "login" : "testUser2",
                                                "name" : "Marry",
                                                "imageId" : null,
                                                "avatarColor" : "#B80000"
                                            }
                                        ]
                                        """
                        )
                );
    }
}
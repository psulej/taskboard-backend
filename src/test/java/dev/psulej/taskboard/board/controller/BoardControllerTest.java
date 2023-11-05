//package dev.psulej.taskboard.board.controller;
//
//import dev.psulej.taskboard.board.api.*;
//import dev.psulej.taskboard.board.domain.BoardUserRole;
//import dev.psulej.taskboard.board.domain.TaskPriority;
//import dev.psulej.taskboard.board.service.BoardService;
//import dev.psulej.taskboard.config.TestConfiguration;
//import dev.psulej.taskboard.security.TokenProvider;
//import dev.psulej.taskboard.board.api.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(BoardController.class)
//@Import(TestConfiguration.class)
//class BoardControllerTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    BoardService boardService;
//
//    @MockBean
//    TokenProvider tokenProvider;
//
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    void whenGetBoard_shouldReturn200StatusCode_andReturnBoard() throws Exception {
//        // given
//        UUID boardId = UUID.fromString("15d2f9d0-81b4-4ecb-bd08-530b6f223984");
//
//        User user1 = User.builder()
//                .id(UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"))
//                .login("asmith")
//                .name("Anna")
//                .imageId("dd672f4b-ab70-4976-9d2d-db1f404bea87")
//                .avatarColor("#fad0c3")
//                .build();
//
//        User user2 = User.builder()
//                .id(UUID.fromString("512501e0-f76f-4924-9c6f-e60b1c42b098"))
//                .login("johndoe")
//                .name("johndoe")
//                .imageId(null)
//                .avatarColor("#004DCF")
//                .build();
//
//        Board board = Board.builder()
//                .id(boardId)
//                .name("Test board")
//                .users(
//                        List.of(
//                                new BoardUser(user1, BoardUserRole.BOARD_ADMINISTRATOR.BOARD_ADMINISTRATOR, null),
//                                new BoardUser(user2, Role.BOARD_USER, null)
//                        )
//                )
//                .columns(
//                        List.of(
//                                Column.builder()
//                                        .id(UUID.fromString("82c8bfb6-f63f-4c68-9be0-93b7e66e12ff"))
//                                        .name("Test column 1")
//                                        .tasks(List.of(
//                                                Task.builder()
//                                                        .id(UUID.fromString("7e2e7e9b-affb-4aba-be3d-87ed51cd3302"))
//                                                        .title("testTask1")
//                                                        .description("testDescription1")
//                                                        .assignedUser(user2)
//                                                        .priority(TaskPriority.HIGH)
//                                                        .build(),
//                                                Task.builder()
//                                                        .id(UUID.fromString("083009b6-4076-485f-8e7d-ef9cd02b3828"))
//                                                        .title("testTask2")
//                                                        .description("testDescription2")
//                                                        .assignedUser(user1)
//                                                        .priority(TaskPriority.NORMAL)
//                                                        .build()
//                                        ))
//                                        .build(),
//                                Column.builder()
//                                        .id(UUID.fromString("2ee7affd-65ce-48b5-b50f-9ea7e831deab"))
//                                        .name("Test column 2")
//                                        .tasks(List.of(
//                                                Task.builder()
//                                                        .id(UUID.fromString("6353831c-9f0f-4936-b0a7-89b14b7e20cc"))
//                                                        .title("testTask3")
//                                                        .description("testDescription3")
//                                                        .assignedUser(user2)
//                                                        .priority(TaskPriority.LOW)
//                                                        .build(),
//                                                Task.builder()
//                                                        .id(UUID.fromString("4125c944-b5da-4301-a3f1-d3e3052bc034"))
//                                                        .title("testTask5")
//                                                        .description("testDescription5")
//                                                        .assignedUser(null)
//                                                        .priority(TaskPriority.NORMAL)
//                                                        .build()
//                                        ))
//                                        .build(),
//                                Column.builder()
//                                        .id(UUID.fromString("1da1f76b-7c3a-4e95-b4fe-59e71c6c7318"))
//                                        .name("Test column 3")
//                                        .tasks(List.of(
//                                                Task.builder()
//                                                        .id(UUID.fromString("13bb74d2-2269-4d18-86d2-15fb5eced977"))
//                                                        .title("testTask4")
//                                                        .description("testDescription4")
//                                                        .assignedUser(user1)
//                                                        .priority(TaskPriority.LOW)
//                                                        .build()
//                                        ))
//                                        .build()
//                        )
//                )
//                .build();
//
//        when(boardService.getBoard(boardId)).thenReturn(board);
//
//        // when
//        this.mockMvc.perform(get("/boards/" + boardId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(content().json("""
//                        {
//                            "id": "15d2f9d0-81b4-4ecb-bd08-530b6f223984",
//                            "name": "Test board",
//                            "users": [
//                                {
//                                    "user": {
//                                        "id": "461c84d0-2233-433b-9784-4bf32cd81d6e",
//                                        "login": "asmith",
//                                        "name": "Anna",
//                                        "imageId": "dd672f4b-ab70-4976-9d2d-db1f404bea87",
//                                        "avatarColor": "#fad0c3"
//                                    },
//                                    "role": "BOARD_ADMINISTRATOR",
//                                    "joinedAt": null
//                                },
//                                {
//                                    "user": {
//                                        "id": "512501e0-f76f-4924-9c6f-e60b1c42b098",
//                                        "login": "johndoe",
//                                        "name": "johndoe",
//                                        "imageId": null,
//                                        "avatarColor": "#004DCF"
//                                    },
//                                    "role": "BOARD_USER",
//                                    "joinedAt": null
//                                }
//                            ],
//                            "columns": [
//                                {
//                                    "id": "82c8bfb6-f63f-4c68-9be0-93b7e66e12ff",
//                                    "name": "Test column 1",
//                                    "tasks": [
//                                        {
//                                            "id": "7e2e7e9b-affb-4aba-be3d-87ed51cd3302",
//                                            "title": "testTask1",
//                                            "description": "testDescription1",
//                                            "assignedUser": {
//                                                "id": "512501e0-f76f-4924-9c6f-e60b1c42b098",
//                                                "login": "johndoe",
//                                                "name": "johndoe",
//                                                "imageId": null,
//                                                "avatarColor": "#004DCF"
//                                            },
//                                            "priority": "HIGH"
//                                        },
//                                        {
//                                            "id": "083009b6-4076-485f-8e7d-ef9cd02b3828",
//                                            "title": "testTask2",
//                                            "description": "testDescription2",
//                                            "assignedUser": {
//                                                "id": "461c84d0-2233-433b-9784-4bf32cd81d6e",
//                                                "login": "asmith",
//                                                "name": "Anna",
//                                                "imageId": "dd672f4b-ab70-4976-9d2d-db1f404bea87",
//                                                "avatarColor": "#fad0c3"
//                                            },
//                                            "priority": "NORMAL"
//                                        }
//                                    ]
//                                },
//                                {
//                                    "id": "2ee7affd-65ce-48b5-b50f-9ea7e831deab",
//                                    "name": "Test column 2",
//                                    "tasks": [
//                                        {
//                                            "id": "6353831c-9f0f-4936-b0a7-89b14b7e20cc",
//                                            "title": "testTask3",
//                                            "description": "testDescription3",
//                                            "assignedUser": {
//                                                "id": "512501e0-f76f-4924-9c6f-e60b1c42b098",
//                                                "login": "johndoe",
//                                                "name": "johndoe",
//                                                "imageId": null,
//                                                "avatarColor": "#004DCF"
//                                            },
//                                            "priority": "LOW"
//                                        },
//                                        {
//                                            "id": "4125c944-b5da-4301-a3f1-d3e3052bc034",
//                                            "title": "testTask5",
//                                            "description": "testDescription5",
//                                            "assignedUser": null,
//                                            "priority": "NORMAL"
//                                        }
//                                    ]
//                                },
//                                {
//                                    "id": "1da1f76b-7c3a-4e95-b4fe-59e71c6c7318",
//                                    "name": "Test column 3",
//                                    "tasks": [
//                                        {
//                                            "id": "13bb74d2-2269-4d18-86d2-15fb5eced977",
//                                            "title": "testTask4",
//                                            "description": "testDescription4",
//                                            "assignedUser": {
//                                                "id": "461c84d0-2233-433b-9784-4bf32cd81d6e",
//                                                "login": "asmith",
//                                                "name": "Anna",
//                                                "imageId": "dd672f4b-ab70-4976-9d2d-db1f404bea87",
//                                                "avatarColor": "#fad0c3"
//                                            },
//                                            "priority": "LOW"
//                                        }
//                                    ]
//                                }
//                            ]
//                        }
//                        """));
//    }
//
//
//    @Test
//    void when_AddBoard_shouldReturn200StatusCode_andNewBoard() throws Exception {
//        // given
//
//        UUID boardId = UUID.fromString("b4a5fdd3-b467-4c61-81b4-83a243d001e2");
//        String boardName = "testowyBoard";
//
//        Board board = Board.builder()
//                .id(boardId)
//                .name(boardName)
//                .users(List.of(
//                        User.builder()
//                                .id(UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"))
//                                .login("testUser123")
//                                .name("Mr")
//                                .avatarColor("#B80000")
//                                .build()))
//                .columns(
//                        List.of())
//                .build();
//
//        when(boardService.addBoard(new CreateBoard(boardName))).thenReturn(board);
//
//        // when
//        this.mockMvc.perform(post("/boards")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"" + boardName + "\"}"))
//
//                // then
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content()
//                        .json("""
//                                   {
//                                     "id": "b4a5fdd3-b467-4c61-81b4-83a243d001e2",
//                                     "name": "testowyBoard",
//                                     "users": [
//                                       {
//                                         "id": "461c84d0-2233-433b-9784-4bf32cd81d6e",
//                                         "login": "testUser123",
//                                         "name": "Mr",
//                                         "imageId": null,
//                                         "avatarColor": "#B80000"
//                                       }
//                                     ],
//                                     "columns": []
//                                   }
//                                """));
//
//    }
//
//    @Test
//    void when_EditBoard_shouldReturn200StatusCode_andEditedBoard() throws Exception {
//        // given
//        UUID boardId = UUID.fromString("2b31501a-0e74-4515-b1e6-e5a4c8518924");
//        String boardName = "testowyBoard";
//
//        Board board = Board.builder()
//                .id(boardId)
//                .name(boardName)
//                .users(List.of(
//                        User.builder()
//                                .id(UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"))
//                                .login("testUser123")
//                                .name("Mr")
//                                .avatarColor("#B80000")
//                                .build()))
//                .columns(
//                        List.of())
//                .build();
//
//
//        when(boardService.editBoard(boardId, new UpdateBoard(boardName, List.of(
//                UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"),
//                UUID.fromString("99b65bfa-7120-11ee-b962-0242ac120002")
//
//        )))).thenReturn(board);
//
//        // when
//        this.mockMvc.perform(put("/boards/" + boardId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                                {
//                                    "name": "testowyBoard",
//                                    "userIds": ["461c84d0-2233-433b-9784-4bf32cd81d6e", "99b65bfa-7120-11ee-b962-0242ac120002"]
//                                }
//                                """)
//                )
//
//                // then
//                .andDo(print())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .json("""
//                                   {
//                                     "id": "2b31501a-0e74-4515-b1e6-e5a4c8518924",
//                                     "name": "testowyBoard",
//                                     "users": [
//                                       {
//                                         "id": "461c84d0-2233-433b-9784-4bf32cd81d6e",
//                                         "login": "testUser123",
//                                         "name": "Mr",
//                                         "avatarColor": "#B80000"
//                                       }
//                                     ],
//                                     "columns": []
//                                   }
//                                """));
//    }
//
//    @Test
//    void when_DeleteBoard_shouldReturn200StatusCode() throws Exception {
//        // given
//        UUID boardId = UUID.fromString("3eed8d1c-7128-11ee-b962-0242ac120002");
//
//        Mockito.doNothing().when(boardService).deleteBoard(boardId);
//
//        // when
//        this.mockMvc.perform(delete("/boards/3eed8d1c-7128-11ee-b962-0242ac120002"))
//                // then
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        Mockito.verify(boardService).deleteBoard(boardId);
//    }
//
//    @Test
//    void when_GetBoards_shouldReturn200StatusCode_andReturnBoardList() throws Exception {
//        // given
//        List<AvailableBoard> availableBoards = List.of(
//                new AvailableBoard(UUID.fromString("e769f2a6-712c-11ee-b962-0242ac120002"), "testBoard1"),
//                new AvailableBoard(UUID.fromString("06fa66aa-712d-11ee-b962-0242ac120002"), "testBoard2"),
//                new AvailableBoard(UUID.fromString("1104c2bc-712d-11ee-b962-0242ac120002"), "testBoard3")
//        );
//
//        when(boardService.getAvailableBoards()).thenReturn(
//                availableBoards
//        );
//
//        // when
//        this.mockMvc
//                .perform(get("/boards"))
//                // then
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(
//                        """
//                                [
//                                    {
//                                        "id" :  "e769f2a6-712c-11ee-b962-0242ac120002",
//                                        "name" : "testBoard1"
//                                    },
//                                    {
//                                        "id" :  "06fa66aa-712d-11ee-b962-0242ac120002",
//                                        "name" : "testBoard2"
//                                    },
//                                    {
//                                        "id" :  "1104c2bc-712d-11ee-b962-0242ac120002",
//                                        "name" : "testBoard3"
//                                    }
//                                ]
//                                """
//                ));
//    }
//
//    @Test
//    void when_GetAssignableUsers_shouldReturn200StatusCode_andReturnUserList() throws Exception {
//        // given
//        UUID boardId = UUID.fromString("33e34bae-712e-11ee-b962-0242ac120002");
//        String loginPhrase = "test";
//        List<UUID> excludedUserIds = List.of(
//                UUID.fromString("5736a48a-7132-11ee-b962-0242ac120002"),
//                UUID.fromString("0a71105c-7139-11ee-b962-0242ac120002")
//        );
//
//        List<User> users = List.of(
//                User.builder()
//                        .id(UUID.fromString("23a91c24-7132-11ee-b962-0242ac120002"))
//                        .login("testUser1")
//                        .name("Johnny")
//                        .avatarColor("#B80000")
//                        .imageId(null)
//                        .build(),
//                User.builder()
//                        .id(UUID.fromString("3ba2bb96-7132-11ee-b962-0242ac120002"))
//                        .login("testUser2")
//                        .name("Marry")
//                        .avatarColor("#B80000")
//                        .imageId(null)
//                        .build()
//        );
//        when(boardService.getAssignableUsers(boardId, loginPhrase, excludedUserIds))
//                .thenReturn(users);
//
//        // when
//        this.mockMvc.perform(get("/boards/33e34bae-712e-11ee-b962-0242ac120002/assignable-users")
//                        .queryParam("loginPhrase", loginPhrase)
//                        .queryParam("excludedUserIds", excludedUserIds.stream().map(UUID::toString).collect(Collectors.joining(",")))
//                )
//                // then
//                .andDo(print())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(
//                                """
//                                        [
//                                            {
//                                                "id" :  "23a91c24-7132-11ee-b962-0242ac120002",
//                                                "login" : "testUser1",
//                                                "name" : "Johnny",
//                                                "imageId" : null,
//                                                "avatarColor" : "#B80000"
//                                            },
//                                            {
//                                                "id" :  "3ba2bb96-7132-11ee-b962-0242ac120002",
//                                                "login" : "testUser2",
//                                                "name" : "Marry",
//                                                "imageId" : null,
//                                                "avatarColor" : "#B80000"
//                                            }
//                                        ]
//                                        """
//                        )
//                );
//    }
//}
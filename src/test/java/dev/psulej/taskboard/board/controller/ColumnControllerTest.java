package dev.psulej.taskboard.board.controller;

import dev.psulej.taskboard.board.api.*;
import dev.psulej.taskboard.board.domain.TaskPriority;
import dev.psulej.taskboard.board.service.BoardService;
import dev.psulej.taskboard.board.service.ColumnService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ColumnController.class)
@Import(TestConfiguration.class)
class ColumnControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    BoardService boardService;

    @MockBean
    ColumnService columnService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Add column test")
    void whenAddColumn_shouldReturn200StatusCode_andNewColumn() throws Exception {
        // given
        UUID boardId = UUID.fromString("d60219b8-020b-47d0-aa81-5f2365d16bdd");
        String columnName = "testColumn";

        User boardUser = User.builder()
                .id(UUID.fromString("29653bcb-a57e-4ac8-a87b-c5219ba1f5cf"))
                .login("testUser")
                .name("Johnny")
                .avatarColor("#B80000")
                .imageId(UUID.fromString("fe1b8a0c-67fe-49e9-a98f-1555e702711e"))
                .build();

        Column column = Column.builder()
                .id(UUID.fromString("fe611628-71b4-11ee-b962-0242ac120002"))
                .name(columnName)
                .tasks(List.of(
                                Task.builder()
                                        .id(UUID.fromString("0bfcff72-71b5-11ee-b962-0242ac120002"))
                                        .title("testTaskTitle")
                                        .description("testTaskDescription")
                                        .assignedUser(null)
                                        .priority(TaskPriority.LOW)
                                        .build(),
                                Task.builder()
                                        .id(UUID.fromString("122cba90-71b5-11ee-b962-0242ac120002"))
                                        .title("testTaskTitle2")
                                        .description("testTaskDescription2")
                                        .assignedUser(boardUser)
                                        .priority(null)
                                        .build()
                        )
                )
                .build();

        when(columnService.addColumn(
                boardId,
                new CreateColumn("testColumn"))
        ).thenReturn(column);
        // when
        this.mockMvc.perform(post("/boards/d60219b8-020b-47d0-aa81-5f2365d16bdd/columns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + columnName + "\"}"))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                                """
                                        {
                                        "id":"fe611628-71b4-11ee-b962-0242ac120002",
                                        "name":"testColumn",
                                        "tasks":
                                            [
                                                  {
                                                      "id" : "0bfcff72-71b5-11ee-b962-0242ac120002",
                                                      "title" : "testTaskTitle",
                                                      "description" : "testTaskDescription",
                                                      "assignedUser" : null,
                                                      "priority" : "LOW"
                                                   },
                                                   {
                                                      "id" : "122cba90-71b5-11ee-b962-0242ac120002",
                                                      "title" : "testTaskTitle2",
                                                      "description" : "testTaskDescription2",
                                                      "assignedUser" : 
                                                          {
                                                              "id" : "29653bcb-a57e-4ac8-a87b-c5219ba1f5cf",
                                                              "login" : "testUser",
                                                              "name" : "Johnny",
                                                              "avatarColor" : "#B80000",
                                                              "imageId" : "fe1b8a0c-67fe-49e9-a98f-1555e702711e"
                                                          }
                                                      ,
                                                      "priority" : null
                                                  }
                                            ]
                                        }
                                        """
                        )
                );
    }

    @Test
    @DisplayName("Edit column test")
    void whenEditColumn_shouldReturn200StatusCode_andEditedColumn() throws Exception {
        // given
        UUID boardId = UUID.fromString("1a908648-71bb-11ee-b962-0242ac120002");
        UUID columnId = UUID.fromString("5aeb9302-71e0-11ee-b962-0242ac120002");
        String columnName = "testColumn";

        User boardUser = User.builder()
                .id(UUID.fromString("29653bcb-a57e-4ac8-a87b-c5219ba1f5cf"))
                .login("testUser")
                .name("Johnny")
                .avatarColor("#B80000")
                .imageId(UUID.fromString("fe1b8a0c-67fe-49e9-a98f-1555e702711e"))
                .build();

        Column column = Column.builder()
                .id(UUID.fromString("fe611628-71b4-11ee-b962-0242ac120002"))
                .name(columnName)
                .tasks(List.of(
                                Task.builder()
                                        .id(UUID.fromString("d895ceac-71e1-11ee-b962-0242ac120002"))
                                        .title("testTaskTitle")
                                        .description("testTaskDescription")
                                        .assignedUser(null)
                                        .priority(TaskPriority.LOW)
                                        .build(),
                                Task.builder()
                                        .id(UUID.fromString("e5bf097c-71e1-11ee-b962-0242ac120002"))
                                        .title("testTaskTitle2")
                                        .description("testTaskDescription2")
                                        .assignedUser(boardUser)
                                        .priority(null)
                                        .build()
                        )
                )
                .build();

        when(columnService.editColumn(
                boardId,
                columnId,
                new UpdateColumn(columnName))
        ).thenReturn(column);

        // when
        this.mockMvc
                .perform(put("/boards/1a908648-71bb-11ee-b962-0242ac120002/columns/5aeb9302-71e0-11ee-b962-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + columnName + "\"}"))

                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        """
                                {
                                "id":"fe611628-71b4-11ee-b962-0242ac120002",
                                "name":"testColumn",
                                "tasks":
                                    [
                                          {
                                              "id" : "d895ceac-71e1-11ee-b962-0242ac120002",
                                              "title" : "testTaskTitle",
                                              "description" : "testTaskDescription",
                                              "assignedUser" : null,
                                              "priority" : "LOW"
                                           },
                                           {
                                              "id" : "e5bf097c-71e1-11ee-b962-0242ac120002",
                                              "title" : "testTaskTitle2",
                                              "description" : "testTaskDescription2",
                                              "assignedUser" : 
                                                  {
                                                      "id" : "29653bcb-a57e-4ac8-a87b-c5219ba1f5cf",
                                                      "login" : "testUser",
                                                      "name" : "Johnny",
                                                      "avatarColor" : "#B80000",
                                                      "imageId" : "fe1b8a0c-67fe-49e9-a98f-1555e702711e"
                                                  }
                                              ,
                                              "priority" : null
                                          }
                                    ]
                                }
                                """
                ));
    }

    @Test
    @DisplayName("Delete column test")
    void whenDeleteColumn_shouldReturn200StatusCode() throws Exception {
        // given
        UUID boardId = UUID.fromString("35830810-71e6-11ee-b962-0242ac120002");
        UUID columnId = UUID.fromString("3b7e6386-71e6-11ee-b962-0242ac120002");

        Mockito.doNothing().when(columnService).deleteColumn(boardId, columnId);

        // when
        this.mockMvc.perform(delete("/boards/35830810-71e6-11ee-b962-0242ac120002/columns/3b7e6386-71e6-11ee-b962-0242ac120002"))
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update columns test")
    void whenUpdateColumns_shouldReturn200StatusCode() throws Exception {
        // given
        UUID boardId = UUID.fromString("35830810-71e6-11ee-b962-0242ac120002");
        UUID columnId = UUID.fromString("0dd456c8-71f2-11ee-b962-0242ac120002");
        UUID secondColumnId = UUID.fromString("2bedadd4-71f3-11ee-b962-0242ac120002");

        List<UpdateColumnTasks> updatedColumns = List.of(
                new UpdateColumnTasks(columnId, List.of(
                        UUID.fromString("b2a77282-71ef-11ee-b962-0242ac120002"),
                        UUID.fromString("b9b326c0-71ef-11ee-b962-0242ac120002")
                )),
                new UpdateColumnTasks(secondColumnId, List.of(
                        UUID.fromString("bdda1150-71ef-11ee-b962-0242ac120002"),
                        UUID.fromString("b2a774b2-71ef-11ee-b962-0242ac120002")
                ))
        );
        Mockito.doNothing().when(columnService).updateColumns(
                columnId,
                updatedColumns
        );

        mockMvc.perform(put("/boards/35830810-71e6-11ee-b962-0242ac120002/columns")
                        // then
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    [
                                        {
                                            "columnId": "0dd456c8-71f2-11ee-b962-0242ac120002",
                                            "taskIds": [
                                                "b2a77282-71ef-11ee-b962-0242ac120002",
                                                "b9b326c0-71ef-11ee-b962-0242ac120002"
                                            ]
                                        },
                                        {
                                            "columnId": "2bedadd4-71f3-11ee-b962-0242ac120002",
                                            "taskIds": [
                                                "bdda1150-71ef-11ee-b962-0242ac120002",
                                                "b2a774b2-71ef-11ee-b962-0242ac120002"
                                            ]
                                        }
                                    ]
                                """
                        )
                )
                // then
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(columnService).updateColumns(boardId, updatedColumns);
    }
}
package dev.psulej.taskboard.board.controller;

import dev.psulej.taskboard.board.api.CreateTask;
import dev.psulej.taskboard.board.api.Task;
import dev.psulej.taskboard.board.api.UpdateTask;
import dev.psulej.taskboard.board.domain.TaskPriority;
import dev.psulej.taskboard.board.service.TaskService;
import dev.psulej.taskboard.config.TestConfiguration;
import dev.psulej.taskboard.security.TokenProvider;
import dev.psulej.taskboard.board.api.User;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(TestConfiguration.class)
class TaskControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    TaskService taskService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void whenAddTask_shouldReturn200StatusCode_andNewTask() throws Exception {
        // given

        UUID boardId = UUID.fromString("5623603c-7386-11ee-b962-0242ac120002");
        UUID columnId = UUID.fromString("71bf4932-7386-11ee-b962-0242ac120002");

        User assignedUser = User.builder()
                .id(UUID.fromString("feab66d8-7385-11ee-b962-0242ac120002"))
                .login("testUser")
                .name("Johnny")
                .avatarColor("#B80000")
                .imageId(UUID.fromString("042ff4d4-7386-11ee-b962-0242ac120002"))
                .build();

        Task task = Task.builder()
                .id(UUID.fromString("0b912e46-7386-11ee-b962-0242ac120002"))
                .title("taskTitle")
                .description("taskDescription")
                .priority(TaskPriority.LOW)
                .assignedUser(assignedUser)
                .build();

        when(taskService.addTask(boardId, columnId,
                new CreateTask("taskTitle", "taskDescription", assignedUser.id(),TaskPriority.LOW)))
                .thenReturn(task);

        // when
        this.mockMvc.perform(post("/boards/5623603c-7386-11ee-b962-0242ac120002/columns/71bf4932-7386-11ee-b962-0242ac120002/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                                {
                                                    "title" : "taskTitle",
                                                    "description" : "taskDescription",
                                                    "assignedUserId" : "feab66d8-7385-11ee-b962-0242ac120002",
                                                    "priority" : "LOW"                   
                                                }
                                        """
                        ))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        """
                                {
                                  "id" : "0b912e46-7386-11ee-b962-0242ac120002",
                                  "title" : "taskTitle",
                                  "description" : "taskDescription",
                                  "priority" : "LOW",
                                  "assignedUser" : 
                                  {
                                      "id" : "feab66d8-7385-11ee-b962-0242ac120002",
                                      "login" : "testUser",
                                      "name" : "Johnny",
                                      "avatarColor" : "#B80000",
                                      "imageId" : "042ff4d4-7386-11ee-b962-0242ac120002"
                                  }
                                  
                                }
                                """
                ));
    }

    @Test
    void whenEditTask_shouldReturn200StatusCode_andEditedTask() throws Exception {
        // given

        UUID boardId = UUID.fromString("7d4230a4-738a-11ee-b962-0242ac120002");
        UUID columnId = UUID.fromString("801fd5f6-738a-11ee-b962-0242ac120002");
        UUID taskId = UUID.fromString("825d0456-738a-11ee-b962-0242ac120002");

        UUID assignedUserId = UUID.fromString("825d0456-738a-11ee-b962-0242ac120002");

        User assignedUser = User.builder()
                .id(UUID.fromString("86f1e20c-738a-11ee-b962-0242ac120002"))
                .login("testUser")
                .name("Johnny")
                .avatarColor("#B80000")
                .imageId(UUID.fromString("8e374a66-738a-11ee-b962-0242ac120002"))
                .build();

        Task task = Task.builder()
                .id(UUID.fromString("92587764-738a-11ee-b962-0242ac120002"))
                .title("taskTitle")
                .description("taskDescription")
                .priority(TaskPriority.LOW)
                .assignedUser(assignedUser)
                .build();

        when(taskService.editTask(boardId, columnId, taskId,
                new UpdateTask("taskTitle", "taskDescription", assignedUserId, TaskPriority.LOW)))
                .thenReturn(task);

        this.mockMvc.perform(put("/boards/7d4230a4-738a-11ee-b962-0242ac120002/columns/801fd5f6-738a-11ee-b962-0242ac120002/tasks/825d0456-738a-11ee-b962-0242ac120002")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                        {
                                            "title" : "taskTitle",
                                            "description" : "taskDescription",
                                            "priority" : "LOW",
                                            "assignedUserId" : "825d0456-738a-11ee-b962-0242ac120002"                   
                                        }
                                """
                ))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        """
                                {
                                  "id" : "92587764-738a-11ee-b962-0242ac120002",
                                  "title" : "taskTitle",
                                  "description" : "taskDescription",
                                  "priority" : "LOW",
                                  "assignedUser" : 
                                  {
                                      "id" : "86f1e20c-738a-11ee-b962-0242ac120002",
                                      "login" : "testUser",
                                      "name" : "Johnny",
                                      "avatarColor" : "#B80000",
                                      "imageId" : "8e374a66-738a-11ee-b962-0242ac120002"
                                  }
                                  
                                }
                                """
                ));
    }

    @Test
    void whenDeleteTask_shouldReturn200StatusCode() throws Exception {
        UUID boardId = UUID.fromString("7d4230a4-738a-11ee-b962-0242ac120002");
        UUID columnId = UUID.fromString("801fd5f6-738a-11ee-b962-0242ac120002");
        UUID taskId = UUID.fromString("825d0456-738a-11ee-b962-0242ac120002");

        Mockito.doNothing().when(taskService).deleteTask(boardId,columnId,taskId);
        // when
        this.mockMvc.perform(delete("/boards/7d4230a4-738a-11ee-b962-0242ac120002/columns/801fd5f6-738a-11ee-b962-0242ac120002/tasks/825d0456-738a-11ee-b962-0242ac120002"))
                // then
                .andDo(print())
                .andExpect(status().isOk());
    }
}
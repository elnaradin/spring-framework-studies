package com.example.tasktracker;

import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.TaskStatus;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "User1", roles = "MANAGER")
@AutoConfigureWebTestClient
public class AbstractTest {
    protected static String FIRST_USER_ID = UUID.randomUUID().toString();
    protected static String SECOND_USER_ID = UUID.randomUUID().toString();
    protected static String FIRST_TASK_ID = UUID.randomUUID().toString();
    protected static String SECOND_TASK_ID = UUID.randomUUID().toString();
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected TaskRepository taskRepository;
    @Autowired
    protected UserRepository userRepository;
    protected List<User> userList;
    protected List<Task> taskList;
    @Autowired
    protected ObjectMapper objectMapper;

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0.8"));

    static {
        mongoDBContainer.start();
    }

    @BeforeEach
    void setUp() {
        User user1 = User
                .builder()
                .id(FIRST_USER_ID)
                .email("random@email1")
                .userName("User1")
                .password("pass")
                .roles(Set.of(RoleType.ROLE_USER))
                .build();
        User user2 = User
                .builder()
                .id(SECOND_USER_ID)
                .email("random@email2")
                .userName("User2")
                .password("pass")
                .roles(Set.of(RoleType.ROLE_USER))
                .build();
        userList = userRepository.saveAll(List.of(user1, user2)).collectList().block();
        Task task1 = Task
                .builder()
                .id(FIRST_TASK_ID)
                .name("Task1")
                .authorId(FIRST_USER_ID)
                .assigneeId(SECOND_USER_ID)
                .status(TaskStatus.TODO)
                .description("Description1")
                .build();
        Task task2 = Task
                .builder()
                .id(SECOND_TASK_ID)
                .name("Task2")
                .authorId(SECOND_USER_ID)
                .assigneeId(FIRST_USER_ID)
                .status(TaskStatus.IN_PROGRESS)
                .description("Description2")
                .build();
        taskList = taskRepository.saveAll(List.of(task1, task2)).collectList().block();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll().block();
        taskRepository.deleteAll().block();
    }
}

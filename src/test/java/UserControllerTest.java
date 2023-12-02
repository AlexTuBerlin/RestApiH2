import com.example.restapih2.RestApiH2Application;
import com.example.restapih2.model.User;
import com.example.restapih2.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@SpringBootTest(classes = RestApiH2Application.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    long userIdJohn = 1L;
    long userIdJane = 2L;

    @BeforeEach
    public void setUp() {
        userRepository.save(new User("John", "Doe", "john@example.com"));
        userRepository.save(new User("Jane", "Doe", "jane@example.com"));
    }
    @AfterEach
    public void reset() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetAllUsersWithName() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?name=John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetUserById() throws Exception {

        long userId = getIdByName("John");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"));
    }

    @Test
    public void testDeleteUserById() throws Exception {

        long userId = getIdByName("John");
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", getIdByName("John"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(Matchers.not(userId)));
    }

    @Test
    public void testDeleteAllUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    public void testUpdateUser() throws Exception {

        long userId = getIdByName("John");
        User updatedUserData = new User("Updated", "User", "updated@example.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUserData)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated"));

    }

    private long getIdByName(String name) {
        return userRepository.findByName(name).get(0).getId();
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
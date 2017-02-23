package com.example.bankingapp.unit.controller;

import com.example.bankingapp.BankingApplication;
import com.example.bankingapp.data.dto.CreateUserDto;
import com.example.bankingapp.data.dto.UpdateUserDto;
import com.example.bankingapp.unit.controller.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.data.repository.UserRepository;
import com.example.bankingapp.unit.TestUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Artis on 2/3/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankingApplication.class)
@WebAppConfiguration
public class UserControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AppUser testUser = TestUtil.getUser();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
        objectMapper = new ObjectMapper();
        userRepository.save(testUser);
    }

    @Test
    @WithMockCustomUser
    public void testGetUser() throws Exception {
        String basicToken = "Basic " + new String(Base64.encodeBase64((testUser.getEmail()+":"+testUser.getPassword()).getBytes()));
        mockMvc.perform(get("/user")
                .header("Authorization", basicToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andDo(document("{class-name}/{method-name}",
                        requestHeaders(
                                headerWithName("Authorization").description("HTTP basic authentication. " +
                                        "With successful authentication x-auth-token will be exposed in response headers for further use.")
                        ),
                        responseFields(
                                fieldWithPath("name").description("User's name"),
                                fieldWithPath("email").description("User's email")
                        ),
                        responseHeaders(
                                headerWithName("x-auth-token").description(UUID.randomUUID()).optional()
                        )
                ));
    }

    @Test
    public void testCreateUser() throws Exception {
        CreateUserDto userDto = new CreateUserDto();
        userDto.setEmail("exampleuser@user.com");
        userDto.setName("User");
        userDto.setPassword("strongpassword");

        mockMvc.perform(post("/user")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andDo(document("{class-name}/{method-name}",
                        requestFields(
                                fieldWithPath("name").description("User's name"),
                                fieldWithPath("email").description("User's email"),
                                fieldWithPath("password").description("User's password")
                        ),
                        responseFields(
                                fieldWithPath("name").description("User's name"),
                                fieldWithPath("email").description("User's email")
                        )
                ));
    }


    @Test
    @WithMockCustomUser
    public void testUpdateUser() throws Exception {

        UpdateUserDto userDto = new UpdateUserDto();
        userDto.setName("Updated User");
        userDto.setEmail("user@example.com");

        mockMvc.perform(put("/user")
                .content(objectMapper.writeValueAsString(userDto))
                .header("x-auth-token", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        requestHeaders(
                            headerWithName("x-auth-token").description("Authentication token")
                        ),
                        requestFields(
                                fieldWithPath("name").description("User's name"),
                                fieldWithPath("email").description("User's email")
                        )
                ));
    }
}

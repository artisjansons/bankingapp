package com.example.bankingapp.unit.controller;

import com.example.bankingapp.BankingApplication;
import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.data.repository.AccountRepository;
import com.example.bankingapp.unit.TestUtil;
import com.example.bankingapp.unit.controller.security.WithMockCustomUser;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.data.repository.UserRepository;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
public class UserAccountControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;
    private AppUser testUser = TestUtil.getUser();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;
    

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
        userRepository.save(testUser);
    }

    @Test
    @WithMockCustomUser
    public void testCreateAccount() throws Exception {
        mockMvc.perform(post("/user/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header("x-auth-token", UUID.randomUUID()))
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        requestHeaders(
                                headerWithName("x-auth-token").description("Authentication token")
                        ),
                        responseFields(
                                fieldWithPath("accountNumber").description("Account number"),
                                fieldWithPath("created").description("Date and time when account is created"),
                                fieldWithPath("balance").description("Current balance of account"),
                                fieldWithPath("_links").ignored()
                        ))
                );
    }

    @Test
    @WithMockCustomUser
    public void testGetAccount() throws Exception {
        Account account = TestUtil.getAccount(testUser);
        accountRepository.save(account);

        mockMvc.perform(get("/user/account/{id}", account.getId())
                .header("x-auth-token", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is(account.getAccountNumber())))
                .andDo(document("{class-name}/{method-name}",
                        requestHeaders(
                                headerWithName("x-auth-token").description("Authentication token")
                        ),
                        pathParameters(
                                parameterWithName("id").description("Account ID")
                        ),
                        responseFields(
                                fieldWithPath("accountNumber").description("Account number"),
                                fieldWithPath("created").description("Date and time when account is created"),
                                fieldWithPath("balance").description("Current balance of account")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    public void testGetNonExistingAccount() throws Exception {
        mockMvc.perform(get("/user/account/{id}", 0L))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser
    public void testListAccounts() throws Exception {
        Account account = TestUtil.getAccount(testUser);
        accountRepository.save(account);

        mockMvc.perform(get("/user/account")
                .header("x-auth-token", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].length()", greaterThan(0)))
                .andExpect(jsonPath("$.[0].accountNumber", is(account.getAccountNumber())))
                .andDo(document("{class-name}/{method-name}",
                        requestHeaders(
                                headerWithName("x-auth-token").description("Authentication token")
                        ),
                        responseFields(
                                fieldWithPath("[].accountNumber").description("Account number"),
                                fieldWithPath("[].created").description("Date and time when account is created"),
                                fieldWithPath("[].balance").description("Current balance of account"),
                                fieldWithPath("[].links").ignored()
                        )
                ));
    }
}

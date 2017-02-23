package com.example.bankingapp.unit.controller;

import com.example.bankingapp.data.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bankingapp.BankingApplication;
import com.example.bankingapp.data.dto.WithdrawDto;
import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.data.repository.UserRepository;
import com.example.bankingapp.unit.TestUtil;
import com.example.bankingapp.unit.controller.security.WithMockCustomUser;
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

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Artis on 2/3/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankingApplication.class)
@WebAppConfiguration
public class UserAccountWithdrawControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;
    private AppUser testUser;
    private Account account;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();

        objectMapper = new ObjectMapper();
        testUser = TestUtil.getUser();
        account = TestUtil.getAccount(testUser);
        account.setBalance(new BigDecimal(100));

        userRepository.save(testUser);
        accountRepository.save(account);
    }

    @Test
    @WithMockCustomUser
    public void testWithdraw() throws Exception {

        WithdrawDto withdrawDto = new WithdrawDto();
        withdrawDto.setAmount(new BigDecimal(20));

        mockMvc.perform(post("/user/account/{accountId}/withdraw", account.getId())
                .content(objectMapper.writeValueAsString(withdrawDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        pathParameters(
                                parameterWithName("accountId").description("Account ID")
                        ),
                        requestHeaders(
                                headerWithName("x-auth-token").description("Authentication token")
                        ),
                        requestFields(
                                fieldWithPath("amount").description("Amount of withdraw")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    public void testWithdrawWithExceededAmount() throws Exception {

        WithdrawDto withdrawDto = new WithdrawDto();
        withdrawDto.setAmount(new BigDecimal(120));

        mockMvc.perform(post("/user/account/{accountId}/withdraw", account.getId())
                .content(objectMapper.writeValueAsString(withdrawDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser
    public void testWithdrawWithZeroAmount() throws Exception {

        WithdrawDto withdrawDto = new WithdrawDto();
        withdrawDto.setAmount(BigDecimal.ZERO);

        mockMvc.perform(post("/user/account/{accountId}/withdraw", account.getId())
                .content(objectMapper.writeValueAsString(withdrawDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

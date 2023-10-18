package com.zerobase.mission2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.mission2.dto.AccountDto;
import com.zerobase.mission2.dto.CancelBalance;
import com.zerobase.mission2.dto.TransactionDto;
import com.zerobase.mission2.dto.UseBalance;
import com.zerobase.mission2.service.TransactionService;
import com.zerobase.mission2.type.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.zerobase.mission2.type.TransactionResultType.*;
import static com.zerobase.mission2.type.TransactionType.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void successUseBalance() throws Exception {
        // given
        given(transactionService.useBalance(anyLong(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1000000000")
                        .transactedAt(LocalDateTime.now())
                        .amount(12345L)
                        .transactionId("transactionId")
                        .transactionResultType(S)
                        .build()
                );
        // when
        
        // then
        mockMvc.perform(post("/transaction/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new UseBalance.Request(1L, "2000000000", 3000L)
                ))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1000000000"))
                .andExpect(jsonPath("$.transactionResult").value("S"))
                .andExpect(jsonPath("$.transactionId").value("transactionId"))
                .andExpect(jsonPath("$.amount").value(12345));

    }

    @Test
    void successCancelBalance() throws Exception {
        // given
        given(transactionService.cancelBalance(anyString(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1000000000")
                        .transactedAt(LocalDateTime.now())
                        .amount(12345L)
                        .transactionId("transactionIdForCancel")
                        .transactionResultType(S)
                        .build()
                );
        // when

        // then
        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CancelBalance.Request("transactionId", "2000000000", 3000L)
                        ))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1000000000"))
                .andExpect(jsonPath("$.transactionResult").value("S"))
                .andExpect(jsonPath("$.transactionId").value("transactionIdForCancel"))
                .andExpect(jsonPath("$.amount").value(12345));

    }

    @Test
    void successGetQueryTransaction() throws Exception {
        // given
        given(transactionService.queryTransaction(anyString()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1000000000")
                        .transactionType(USE)
                        .transactedAt(LocalDateTime.now())
                        .amount(12345L)
                        .transactionId("transactionIdForCancel")
                        .transactionResultType(S)
                        .build());
        // when
        // then
        mockMvc.perform(get("/transaction/12345"))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber").value("1000000000"))
                .andExpect(jsonPath("$.transactionType").value("USE"))
                .andExpect(jsonPath("$.amount").value(12345))
                .andExpect(jsonPath("$.transactionId").value("transactionIdForCancel"))
                .andExpect(jsonPath("$.transactionResult").value("S"));
    }
}
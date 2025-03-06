package org.qianli.bank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.qianli.bank.model.Transaction;
import org.qianli.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setType(Transaction.Type.DEPOSIT);
        testTransaction.setDescription("Test transaction");
    }

    @Test
    void createTransaction_shouldReturnCreated() throws Exception {
        given(transactionService.createTransaction(any(Transaction.class)))
            .willReturn(testTransaction);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.0,\"type\":\"DEPOSIT\",\"description\":\"Test transaction\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.amount").value(100.0))
            .andExpect(jsonPath("$.type").value("DEPOSIT"))
            .andExpect(jsonPath("$.description").value("Test transaction"));
    }

    @Test
    void getTransaction_shouldReturnTransaction() throws Exception {
        given(transactionService.getTransactionById(testTransaction.getId()))
            .willReturn(Optional.of(testTransaction));

        mockMvc.perform(get("/api/transactions/" + testTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testTransaction.getId()))
            .andExpect(jsonPath("$.amount").value(100.0))
            .andExpect(jsonPath("$.type").value("DEPOSIT"))
            .andExpect(jsonPath("$.description").value("Test transaction"));
    }

    @Test
    void getAllTransactions_shouldReturnTransactionList() throws Exception {
        given(transactionService.getAllTransactions(0, 10, "DEPOSIT", "12345"))
            .willReturn(Arrays.asList(testTransaction));

        mockMvc.perform(get("/api/transactions?page=0&size=10&type=DEPOSIT&accountId=12345"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(testTransaction.getId()))
            .andExpect(jsonPath("$[0].amount").value(100.0))
            .andExpect(jsonPath("$[0].type").value("DEPOSIT"))
            .andExpect(jsonPath("$[0].description").value("Test transaction"));
    }

    @Test
    void updateTransaction_shouldReturnUpdatedTransaction() throws Exception {
        given(transactionService.updateTransaction(any(Long.class), any(Transaction.class)))
            .willReturn(testTransaction);

        mockMvc.perform(put("/api/transactions/" + testTransaction.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":200.0,\"type\":\"WITHDRAWAL\",\"description\":\"Updated transaction\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testTransaction.getId()))
            .andExpect(jsonPath("$.amount").value(100.0))
            .andExpect(jsonPath("$.type").value("DEPOSIT"))
            .andExpect(jsonPath("$.description").value("Test transaction"));
    }

    @Test
    void deleteTransaction_shouldReturnNoContent() throws Exception {
        doNothing().when(transactionService).deleteTransaction(testTransaction.getId());

        mockMvc.perform(delete("/api/transactions/" + testTransaction.getId()))
            .andExpect(status().isNoContent());
    }
}

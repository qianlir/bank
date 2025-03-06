package org.qianli.bank.controller;

import org.qianli.bank.model.Account;
import org.qianli.bank.model.ErrorResponse;
import org.qianli.bank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Get all accounts
     * 
     * @return List of all accounts
     */
    // 使用@GetMapping注解标记该方法为处理GET请求的方法
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        logger.info("Fetching all accounts");
        try {
            List<Account> accounts = accountService.getAllAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching accounts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    /**
     * Get account by ID
     * 
     * @param id Account ID
     * @return Account details
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccountByAccountNumber(@PathVariable String accountNumber) {
        logger.info("Fetching account with accountNumber: {}", accountNumber);
        try {
            return accountService.getAccountByAccountNumber(accountNumber)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            logger.error("Error fetching account: {}", e.getMessage());
            ErrorResponse error = new ErrorResponse("Failed to fetch account", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

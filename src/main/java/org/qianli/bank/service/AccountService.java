package org.qianli.bank.service;

import org.qianli.bank.model.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    /**
     * 获取所有账户信息的方法。
     * 账户暂不实现分页功能，一次性返回所有账户信息。
     * 
     * @return 返回一个包含所有账户对象的列表。
     */
    List<Account> getAllAccounts();
    Optional<Account> getAccountByAccountNumber(String accountNumber);
}

package org.qianli.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Transaction {

    public enum Type {
        DEPOSIT,    // 存款
        WITHDRAWAL, // 取款
        TRANSFER,    // 转账
        //TODO 以下待讨论
        TRANSFER_IN,    // 转入
        TRANSFER_OUT,    // 转出
    }

    public Transaction() {
    }

    public Transaction(BigDecimal amount, String description, LocalDateTime timestamp) {
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }

    private Long id;
    
    @NotBlank(message = "Type is required")
    private Type type;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;
    
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("date")
    private LocalDateTime timestamp;

    private String fromAccountNumber; // 转出账户
    private String toAccountNumber;   // 转入账户
    //仅可为1或0，1表示该记录被更新过，0表示该记录未被更新过
    private String modifyFlg="0"; // 删除标志,一条记录被更新和删除过一次之后，不可再次更新

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", fromAccountNumber='" + fromAccountNumber + '\'' +
                ", toAccountNumber='" + toAccountNumber + '\'' +
                '}';
    }

  
}

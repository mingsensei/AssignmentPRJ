package org.example.rf.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "payment")
public class Payment {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến Order
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "order_id",nullable = true)
    private Order order;

    @Setter
    @Getter
    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;
    @Setter
    @Column(name = "transaction_no", length = 100)
    private String transactionNo;

    @Setter
    @Column(name = "bank_code", length = 50)
    private String bankCode;

    @Setter
    @Column(name = "pay_date")
    private LocalDateTime payDate;

    @Getter
    @Setter
    @Column(name = "status", length = 20)
    private String status;

    @Setter
    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Payment() {}

    // Getters và Setters

    public String getTransactionNo() {
        return transactionNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public LocalDateTime getPayDate() {
        return payDate;
    }

    public String getFormattedPayDate() {
        if (payDate == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return payDate.format(formatter);
    }
}

package org.example.rf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")  // vì table tên orders, tránh trùng với từ khóa SQL
@Builder
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // liên kết với User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "order_type", nullable = false, length = 50)
    private String orderType; // Ví dụ: "COURSE_PURCHASE" hoặc "PLAN_PURCHASE"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    public Order() {}

    // Getters và Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    // Trong file model Order.java

    public java.util.Date getCreatedAtAsDate() {
        // THÊM DÒNG KIỂM TRA NÀY
        if (this.createdAt == null) {
            return null; // Nếu ngày tạo là null, trả về null. JSP sẽ bỏ qua và không hiển thị gì.
        }
        // Chỉ chuyển đổi nếu createdAt có giá trị
        return java.sql.Timestamp.valueOf(this.createdAt);
    }

}

package org.example.rf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;

@Entity
@Table(name = "plans")
@Builder
@AllArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "max_test_attempts", nullable = false)
    private Integer maxTestAttempts;

    @Column(name = "max_posts")
    private Integer maxPosts; // null = không giới hạn

    // === THÊM CÁC TRƯỜDEỂ BÁN HÀNG ===
    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Plan() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getMaxTestAttempts() { return maxTestAttempts; }
    public void setMaxTestAttempts(Integer maxTestAttempts) { this.maxTestAttempts = maxTestAttempts; }
    public Integer getMaxPosts() { return maxPosts; }
    public void setMaxPosts(Integer maxPosts) { this.maxPosts = maxPosts; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
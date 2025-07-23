package org.example.rf.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;

@Entity
@Table(name = "plans")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
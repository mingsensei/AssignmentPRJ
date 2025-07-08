package org.example.rf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscriptions")
@Builder
@AllArgsConstructor
public class UserSubscription {

    // Getters and Setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Setter
    @Getter
    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Setter
    @Getter
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Setter
    @Getter
    @Column(name = "end_date")
    private LocalDate endDate;

    @Setter
    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Setter
    @Getter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;


    public UserSubscription() {}


}

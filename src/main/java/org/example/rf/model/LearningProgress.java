package org.example.rf.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(name = "LearningProgress")
@Table(name = "learning_progress")
public class LearningProgress {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến người học
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Liên kết đến bài học (lesson)
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false, unique = true)
    private Lesson lesson;


    // Trạng thái đã học chưa
    @Getter
    @Setter
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    // Thời gian hoàn thành (nếu có)
    @Getter
    @Setter
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Ngày tạo bản ghi
    @Getter
    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public LearningProgress() {}

    public String getFormattedCompletedAt() {
        if (completedAt == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return completedAt.format(formatter);
    }
}

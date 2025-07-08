package org.example.rf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "chapter")
@Builder
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "order_index")
    private Integer orderIndex;

    public Chapter() {}

    public void setId(Long id) { this.id = id; }

    public void setTitle(String title) { this.title = title; }

    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}

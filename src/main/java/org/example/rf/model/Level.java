package org.example.rf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "level")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "level")
    private Integer level;

    public Level() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getChapterId() { return chapterId; }
    public void setChapterId(Long chapterId) { this.chapterId = chapterId; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
}

package org.example.rf.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "level")
public class Level {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "student_id")
    private Long studentId;

    @Setter
    @Getter
    @Column(name = "chapter_id")
    private Long chapterId;
    @Getter
    @Setter
    @Column(name = "level")
    private int level;

    public Level() {}

}

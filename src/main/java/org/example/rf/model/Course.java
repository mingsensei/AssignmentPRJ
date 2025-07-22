package org.example.rf.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "category_id")
    private Long categoryId;

}

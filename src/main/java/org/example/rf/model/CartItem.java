package org.example.rf.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ ManyToOne tới Cart
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Quan hệ ManyToOne tới Course
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    public CartItem() {}

    // Getters và Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}

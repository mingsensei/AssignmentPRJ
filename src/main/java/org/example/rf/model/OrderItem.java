package org.example.rf.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Liên kết đến Course
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    public OrderItem() {}

    // Getters và Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

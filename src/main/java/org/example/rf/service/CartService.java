package org.example.rf.service;

import org.example.rf.dao.CartDAO;
import org.example.rf.model.Cart;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class CartService {

    private final EntityManager em;
    private final CartDAO cartDAO;

    public CartService() {
        this.em = JPAUtil.getEntityManager();  // Tạo EntityManager 1 lần
        this.cartDAO = new CartDAO(em);        // Tạo DAO 1 lần dùng chung
    }

    // Tạo giỏ hàng mới
    public void createCart(Cart cart) {
        cartDAO.create(cart);
    }

    // Tìm giỏ hàng theo ID
    public Cart getCartById(Long id) {
        return cartDAO.findById(id);
    }

    // Cập nhật giỏ hàng
    public void updateCart(Cart cart) {
        cartDAO.update(cart);
    }

    // Xóa giỏ hàng theo ID
    public void deleteCart(Long id) {
        cartDAO.delete(id);
    }

    // Lấy danh sách tất cả giỏ hàng
    public List<Cart> getAllCarts() {
        return cartDAO.findAll();
    }

    // Đóng EntityManager khi không cần nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}

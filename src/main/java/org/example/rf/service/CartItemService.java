package org.example.rf.service;

import org.example.rf.dao.CartItemDAO;
import org.example.rf.model.CartItem;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.util.List;

public class CartItemService {

    private final EntityManager em;
    private final CartItemDAO cartItemDAO;

    public CartItemService() {
        this.em = JPAUtil.getEntityManager();         // Chỉ tạo 1 lần
        this.cartItemDAO = new CartItemDAO(em);       // Chỉ tạo 1 lần
    }

    // Thêm CartItem mới
    public void createCartItem(CartItem cartItem) {
        cartItemDAO.create(cartItem);
    }

    // Tìm CartItem theo ID
    public CartItem getCartItemById(Long id) {
        return cartItemDAO.findById(id);
    }

    // Cập nhật CartItem
    public void updateCartItem(CartItem cartItem) {
        cartItemDAO.update(cartItem);
    }

    // Xóa CartItem theo ID
    public void deleteCartItem(Long id) {
        cartItemDAO.delete(id);
    }

    // Lấy danh sách tất cả CartItem
    public List<CartItem> getAllCartItems() {
        return cartItemDAO.findAll();
    }

    // Đóng EntityManager khi không cần nữa
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}

package org.example.rf.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory("your-persistence-unit");
        } catch (Exception e) {
            System.err.println("Initial EntityManagerFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    // Lấy EntityManager mới từ EntityManagerFactory
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Đóng EntityManagerFactory khi ứng dụng shutdown
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

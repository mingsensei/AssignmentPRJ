package org.example.rf.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static EntityManagerFactory emf;
    private static volatile boolean initialized = false;

    private static synchronized void initializeIfNeeded() {
        if (!initialized) {
            buildEntityManagerFactory();
        }
    }

    private static void buildEntityManagerFactory() {
        try {
            System.out.println("Initializing EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory("your-persistence-unit");
            initialized = true;
            System.out.println("EntityManagerFactory initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing EntityManagerFactory: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void initializeConnectionPool() {
        if (!initialized) {
            buildEntityManagerFactory();
            testConnection();
        }
    }

    private static void testConnection() {
        EntityManager em = null;
        try {
            System.out.println("Testing connection to initialize pool...");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.getTransaction().commit();
            System.out.println("Connection test successful - Connection pool is ready!");
        } catch (Exception e) {
            System.err.println("Error testing connection: " + e.getMessage());
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static EntityManager getEntityManager() {
        initializeIfNeeded();
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            System.out.println("Closing EntityManagerFactory...");
            emf.close();
            initialized = false;
            System.out.println("EntityManagerFactory closed!");
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
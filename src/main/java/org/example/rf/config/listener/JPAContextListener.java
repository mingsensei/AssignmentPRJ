package org.example.rf.config.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.rf.util.JPAUtil;

@WebListener
public class JPAContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Server starting - Initializing JPA Connection Pool...");
        try {
            JPAUtil.initializeConnectionPool();
            System.out.println("JPA Connection Pool initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing JPA Connection Pool: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Server shutting down - Closing JPA Connection Pool...");
        try {
            JPAUtil.close();
            System.out.println("JPA Connection Pool closed successfully!");
        } catch (Exception e) {
            System.err.println("Error closing JPA Connection Pool: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
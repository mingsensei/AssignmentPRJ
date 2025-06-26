package org.example.rf.service;

import org.example.rf.dao.TestAttemptDAO;
import org.example.rf.model.TestAttempt;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class TestAttemptService {

    private final EntityManager em;
    private final TestAttemptDAO testAttemptDAO;

    public TestAttemptService() {
        this.em = JPAUtil.getEntityManager();
        this.testAttemptDAO = new TestAttemptDAO(em);
    }

    public void createTestAttempt(TestAttempt attempt) {
        testAttemptDAO.create(attempt);
    }

    public TestAttempt getTestAttemptById(Long id) {
        return testAttemptDAO.findById(id);
    }

    public void updateTestAttempt(TestAttempt attempt) {
        testAttemptDAO.update(attempt);
    }

    public void deleteTestAttempt(Long id) {
        testAttemptDAO.delete(id);
    }

    public List<TestAttempt> getAllTestAttempts() {
        return testAttemptDAO.findAll();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}

package org.example.rf.service;

import org.example.rf.dao.PlanDAO;
import org.example.rf.model.Plan;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class PlanService {

    private final EntityManager em;
    private final PlanDAO planDAO;

    public PlanService() {
        this.em = JPAUtil.getEntityManager();
        this.planDAO = new PlanDAO(em);
    }

    public void createPlan(Plan plan) {
        planDAO.create(plan);
    }

    public Plan getPlanById(Long id) {
        return planDAO.findById(id);
    }

    public void updatePlan(Plan plan) {
        planDAO.update(plan);
    }

    public void deletePlan(Long id) {
        planDAO.delete(id);
    }

    public List<Plan> getAllPlans() {
        return planDAO.findAll();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}

package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import org.example.rf.model.Plan;

public class PlanDAO extends GenericDAO<Plan, Long> {

    public PlanDAO(EntityManager entityManager) {
        super(entityManager, Plan.class);
    }

    public Plan findByName(String name) {
        return entityManager.createQuery(
                        "SELECT p FROM Plan p WHERE p.name = :name", Plan.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}

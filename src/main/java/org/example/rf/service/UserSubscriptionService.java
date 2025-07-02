package org.example.rf.service;

import org.example.rf.dao.UserSubscriptionDAO;
import org.example.rf.model.Plan;
import org.example.rf.model.UserSubscription;
import org.example.rf.util.JPAUtil;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserSubscriptionService {

    private final EntityManager em;
    private final UserSubscriptionDAO userSubscriptionDAO;
    private final PlanService planService;

    public UserSubscriptionService() {
        this.em = JPAUtil.getEntityManager();
        this.userSubscriptionDAO = new UserSubscriptionDAO(em);
        this.planService = new PlanService();
    }

    public void createSubscription(UserSubscription subscription) {
        userSubscriptionDAO.create(subscription);
    }

    public UserSubscription getSubscriptionById(Long id) {
        return userSubscriptionDAO.findById(id);
    }

    public void updateSubscription(UserSubscription subscription) {
        userSubscriptionDAO.update(subscription);
    }

    public void deleteSubscription(Long id) {
        userSubscriptionDAO.delete(id);
    }

    public List<UserSubscription> getAllSubscriptions() {
        return userSubscriptionDAO.findAll();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    public UserSubscription findActiveSub(Long userId) {
        List<UserSubscription> activeSubs = getAllSubscriptions().stream()
                .filter(sub -> sub.getUserId().equals(userId) &&
                        (sub.getEndDate() == null || sub.getEndDate().isAfter(LocalDate.now())))
                .collect(Collectors.toList());

        if (activeSubs.isEmpty()) {
            return null;
        }

        // Ưu tiên theo plan
        return activeSubs.stream()
                .max(Comparator.comparingInt(sub -> {
                    Plan plan = planService.getPlanById(sub.getPlanId());
                    switch (plan.getName()) {
                        case "Ultimate": return 3;
                        case "Personal": return 2;
                        case "Free": return 1;
                        default: return 0;
                    }
                }))
                .orElse(null);
    }

}

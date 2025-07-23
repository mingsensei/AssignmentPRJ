package org.example.rf.dao;

import org.example.rf.dto.QuestionRequestPython;
import org.example.rf.model.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.util.JPAUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionDAO {

    private final EntityManager entityManager;

    public QuestionDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Question question) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(question);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Question findById(Long id) {
        return entityManager.find(Question.class, id);
    }

    public void update(Question question) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(question);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Question question = entityManager.find(Question.class, id);
            if (question != null) {
                entityManager.remove(question);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Question> findAll() {
        TypedQuery<Question> query = entityManager.createQuery("SELECT q FROM Question q", Question.class);
        return query.getResultList();
    }

    public List<Question> findByChapterId(Long chapterId) {
        TypedQuery<Question> query = entityManager.createQuery(
                "SELECT q FROM Question q WHERE q.chapterId = :chapterId", Question.class);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }

    public List<Question> findByDifficulty(Integer difficulty) {
        TypedQuery<Question> query = entityManager.createQuery(
                "SELECT q FROM Question q WHERE q.difficulty = :difficulty", Question.class);
        query.setParameter("difficulty", difficulty);
        return query.getResultList();
    }

    public List<Question> findAllByChapterIdAndDifficulty(Long chapterId, Integer difficulty) {
        TypedQuery<Question> query = entityManager.createQuery(
                "SELECT q FROM Question q WHERE q.chapterId = :chapterId AND q.difficulty = :difficulty",
                Question.class);
        query.setParameter("chapterId", chapterId);
        query.setParameter("difficulty", difficulty);
        return query.getResultList();
    }

    public List<Integer> findAllDifficulties() {
        TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT DISTINCT q.difficulty FROM Question q ORDER BY q.difficulty ASC", Integer.class);
        return query.getResultList();
    }

    public List<QuestionRequestPython> findAnsweredQuestionData(Long examId) {
        String sql = """
        SELECT q.correct_option, eq.student_answer, q.difficulty
        FROM exam_question eq
        JOIN question q ON eq.question_id = q.id
        WHERE eq.exam_id = :examId
          AND eq.student_answer IS NOT NULL
    """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("examId", examId)
                .getResultList();

        List<QuestionRequestPython> dtos = new ArrayList<>();
        for (Object[] row : results) {
            String correctAnswer = String.valueOf(row[0]);
            String studentAnswer = String.valueOf(row[1]);
            Integer difficulty = (Integer) row[2];

            dtos.add(QuestionRequestPython.builder()
                    .correctAnswer(correctAnswer)
                    .studentAnswer(studentAnswer)
                    .difficulty(difficulty)
                    .build());
        }

        return dtos;
    }
    public List<Question> findAllQuestionsByExamId(Long examId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Sử dụng JPQL để truy vấn các Question dựa trên ID của Exam liên quan
            TypedQuery<Question> query = em.createQuery(
                    "SELECT q FROM Question q WHERE q.exam.id = :examId",
                    Question.class
            );
            query.setParameter("examId", examId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public List<Question> findNewQuestions(Long chapterId, int difficulty, List<Long> excludedIds, int limit) {
        if (excludedIds == null || excludedIds.isEmpty()) {
            excludedIds = Collections.singletonList(-1L);
        }

        TypedQuery<Question> query = entityManager.createQuery(
                "SELECT q FROM Question q WHERE q.chapterId = :chapterId AND q.difficulty = :difficulty AND q.id NOT IN :excludedIds",
                Question.class
        );
        query.setParameter("chapterId", chapterId);
        query.setParameter("difficulty", difficulty);
        query.setParameter("excludedIds", excludedIds);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<QuestionRequestPython> findAllQuestionDataForExam(Long examId) {
        // 1. Loại bỏ điều kiện "AND eq.student_answer IS NOT NULL"
        String sql = """
    SELECT q.correct_option, eq.student_answer, q.difficulty
    FROM exam_question eq
    JOIN question q ON eq.question_id = q.id
    WHERE eq.exam_id = :examId
    """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("examId", examId)
                .getResultList();

        List<QuestionRequestPython> dtos = new ArrayList<>();
        for (Object[] row : results) {
            String correctAnswer = String.valueOf(row[0]);

            // 2. Xử lý trường hợp student_answer có thể là NULL
            // Nếu row[1] là null, studentAnswer sẽ là null.
            // Nếu không, chuyển nó thành String.
            String studentAnswer = (row[1] != null) ? String.valueOf(row[1]) : null;

            Integer difficulty = (Integer) row[2];

            dtos.add(QuestionRequestPython.builder()
                    .correctAnswer(correctAnswer)
                    .studentAnswer(studentAnswer)
                    .difficulty(difficulty)
                    .build());
        }

        return dtos;
    }
}

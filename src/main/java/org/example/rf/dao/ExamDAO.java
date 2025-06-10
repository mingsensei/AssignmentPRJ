package org.example.rf.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.rf.dto.AnswerCheckDTO;
import org.example.rf.model.Exam;

import java.util.ArrayList;
import java.util.List;

public class ExamDAO {

    private final EntityManager entityManager;

    public ExamDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tạo mới Exam
    public void create(Exam exam) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(exam);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Tìm Exam theo id
    public Exam findById(Long id) {
        return entityManager.find(Exam.class, id);
    }

    // Cập nhật Exam
    public void update(Exam exam) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(exam);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Xóa Exam theo id
    public void delete(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Exam exam = entityManager.find(Exam.class, id);
            if (exam != null) {
                entityManager.remove(exam);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả Exam
    public List<Exam> findAll() {
        TypedQuery<Exam> query = entityManager.createQuery("SELECT e FROM Exam e", Exam.class);
        return query.getResultList();
    }

    // Tìm Exam theo studentId
    public List<Exam> findByStudentId(Long studentId) {
        TypedQuery<Exam> query = entityManager.createQuery(
                "SELECT e FROM Exam e WHERE e.student.id = :studentId", Exam.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    // Tìm Exam theo chapterId
    public List<Exam> findByChapterId(Long chapterId) {
        TypedQuery<Exam> query = entityManager.createQuery(
                "SELECT e FROM Exam e WHERE e.chapter.id = :chapterId", Exam.class);
        query.setParameter("chapterId", chapterId);
        return query.getResultList();
    }


    public List<AnswerCheckDTO> findAnswerChecks(Long examId) {
        String sql = """
        SELECT eq.student_answer, aiq.correct_option
        FROM exam e
        JOIN exam_question eq ON e.id = eq.exam_id
        JOIN ai_question aiq ON eq.ai_question_id = aiq.id
        WHERE e.id = :examId
          AND eq.student_answer IS NOT NULL
    """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("examId", examId)
                .getResultList();

        List<AnswerCheckDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            String studentAnswer = String.valueOf(row[0]);
            String correctAnswer = String.valueOf(row[1]);

            dtos.add(new AnswerCheckDTO(studentAnswer, correctAnswer));
        }

        return dtos;
    }




}

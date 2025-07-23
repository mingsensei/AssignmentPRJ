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
            if (tx.isActive()) {
                tx.rollback();
            }
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
            if (tx.isActive()) {
                tx.rollback();
            }
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
            if (tx.isActive()) {
                tx.rollback();
            }
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
                "SELECT e FROM Exam e WHERE e.studentId = :studentId", Exam.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    // Tìm Exam theo chapterId
    public List<Exam> findByChapterId(Long chapterId) {
        TypedQuery<Exam> query = entityManager.createQuery(
                "SELECT e FROM Exam e WHERE e.chapterId = :chapterId", Exam.class);
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

    public List<QuestionResult> getExamQuestions(long examId) {
        String sql = """
        SELECT 
            eq.question_order,
            COALESCE(q.content, ai.content),
            COALESCE(q.option_a, ai.option_a),
            COALESCE(q.option_b, ai.option_b),
            COALESCE(q.option_c, ai.option_c),
            COALESCE(q.option_d, ai.option_d),
            COALESCE(q.correct_option, ai.correct_option),
            eq.student_answer,
            CASE WHEN ai.id IS NOT NULL THEN 1 ELSE 0 END AS is_ai
        FROM exam_question eq
        LEFT JOIN question q ON eq.question_id = q.id
        LEFT JOIN ai_question ai ON eq.ai_question_id = ai.id
        WHERE eq.exam_id = :examId
        ORDER BY eq.question_order
    """;

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("examId", examId)
                .getResultList();

        List<QuestionResult> result = new ArrayList<>();

        for (Object[] row : rows) {
            QuestionResult qr = new QuestionResult();
            qr.setOrder(((Number) row[0]).intValue());
            qr.setContent((String) row[1]);
            qr.setOptionA((String) row[2]);
            qr.setOptionB((String) row[3]);
            qr.setOptionC((String) row[4]);
            qr.setOptionD((String) row[5]);

            qr.setCorrectOption(row[6] != null ? String.valueOf(row[6].toString().charAt(0)) : null);
            qr.setStudentAnswer(row[7] != null ? String.valueOf(row[7].toString().charAt(0)) : null);

            Number isAiNum = (Number) row[8];
            qr.setAIQuestion(isAiNum != null && isAiNum.intValue() == 1);

            result.add(qr);
        }

        return result;
    }


}

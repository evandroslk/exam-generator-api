package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.devdojo.examgenerator.persistence.model.Question;

public interface QuestionRepository extends CustomPagingAndSortingRepository<Question, Long> {

	@Query("select q from Question q where q.course.id = ?1 and q.title like %?2% and q.professor = ?#{principal.professor}"
			+ " and q.enabled = true")
	List<Question> listQuestionsByCourseAndTitle(long courseId, String title);
	
	@Query("update Question q set q.enabled = false where q.course.id = ?1 and q.professor = ?#{principal.professor}"
			+ " and q.enabled = true")
	@Modifying
	void deleteAllQuestionsRelatedToCourse(long coursId);

	@Query("select q from Question q where q.course.id = ?1 and q.id not in " +
            "(select qa.question.id from QuestionAssignment qa where qa.assignment.id = ?2 and qa.professor = ?#{principal.professor} and qa.enabled = true) " +
            "and q.professor = ?#{principal.professor} and q.enabled = true")
	@Transactional
	List<Question> listQuestionsByCourseNotAssociatedWithAnAssignment(long courseId, long assignmentId);
	
}

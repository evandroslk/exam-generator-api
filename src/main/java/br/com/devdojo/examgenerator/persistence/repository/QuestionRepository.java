package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.devdojo.examgenerator.persistence.model.Question;

public interface QuestionRepository extends CustomPagingAndSortingRepository<Question, Long> {

	@Query("select q from Question q where q.course.id = ?1 and q.title like %?2% and q.professor = ?#{principal.professor}"
			+ " and q.enabled = true")
	List<Question> listQuestionsByCourseAndTitle(long courseId, String title);
	
	@Query("update Question q set q.enabled = false where q.course.id = ?1")
	@Modifying
	void deleteAllQuestionsRelatedToCourse(long coursId);
	
}

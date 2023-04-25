package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;

public interface ChoiceRepository extends CustomPagingAndSortingRepository<Choice, Long> {
	
	@Query("select c From Choice c where c.question.id = ?1 and c.professor = ?#{principal.professor}"
			+ " and c.enabled = true")
	List<Choice> listChoicesByQuestionId(long questionId);
	
	@Query("update Choice c set c.correctAnswer = false where c <> ?1 and c.question = ?2"
			+ " and c.professor = ?#{principal.professor} and c.enabled = true")
	@Modifying
	void updateAllOtherChoicesCorrectAnswerToFalse(Choice choice, Question question);
	
	@Query("update Choice c set c.enabled = false where c.question.id = ?1 and c.professor = ?#{principal.professor}"
			+ " and c.enabled = true")
	@Modifying
	void deleteAllChoicesRelatedToQuestion(long questionId);
	
	@Query("update Choice c set c.enabled = false where c.question.id in (select q.id from Question q"
			+ " where q.course.id = ?1) and c.professor = ?#{principal.professor}"
			+ " and c.enabled = true")
	@Modifying
	void deleteAllChoicesRelatedToCourse(long courseId);

}

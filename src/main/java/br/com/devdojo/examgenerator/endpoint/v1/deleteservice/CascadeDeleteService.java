package br.com.devdojo.examgenerator.endpoint.v1.deleteservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.devdojo.examgenerator.persistence.repository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionAssigmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;

@Service
public class CascadeDeleteService {

	private final QuestionRepository questionRepository;
	private final ChoiceRepository choiceRepository;
	private final CourseRepository courseRepository;
	private final AssignmentRepository assignmentRepository;
	private final QuestionAssigmentRepository questionAssigmentRepository;

	@Autowired
	public CascadeDeleteService(QuestionRepository questionRepository, ChoiceRepository choiceRepository,
			CourseRepository courseRepository, AssignmentRepository assignmentRepository,
			QuestionAssigmentRepository questionAssigmentRepository) {
		this.questionRepository = questionRepository;
		this.choiceRepository = choiceRepository;
		this.courseRepository = courseRepository;
		this.assignmentRepository = assignmentRepository;
		this.questionAssigmentRepository = questionAssigmentRepository;
	}

	public void deleteCourseAndAllRelatedEntities(long courseId) {
		courseRepository.delete(courseId);
		questionRepository.deleteAllQuestionsRelatedToCourse(courseId);
		choiceRepository.deleteAllChoicesRelatedToCourse(courseId);
		assignmentRepository.deleteAllAssignmentsRelatedToCourse(courseId);
		questionAssigmentRepository.deleteAllQuestionAssignmentsRelatedToCourse(courseId);
	}

	public void deleteQuestionAndAllRelatedEntities(long questionId) {
		questionRepository.delete(questionId);
		choiceRepository.deleteAllChoicesRelatedToQuestion(questionId);
		questionAssigmentRepository.deleteAllQuestionAssignmentsRelatedToQuestion(questionId);
	}
	
	public void deleteAssignmentAndAllRelatedEntities(long assignmentId) {
		assignmentRepository.delete(assignmentId);
		questionAssigmentRepository.deleteAllQuestionAssignmentsRelatedToAssignment(assignmentId);
	}

}

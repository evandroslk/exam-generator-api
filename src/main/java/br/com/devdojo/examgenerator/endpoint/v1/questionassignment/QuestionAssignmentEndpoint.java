package br.com.devdojo.examgenerator.endpoint.v1.questionassignment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionAssigmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/professor/course/assignment/questionassignment")
public class QuestionAssignmentEndpoint {

	private final QuestionRepository questionRepository;
	private final QuestionAssigmentRepository questionAssigmentRepository;
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService service;
	private final CascadeDeleteService cascadeDeleteService;

	@Autowired
	public QuestionAssignmentEndpoint(QuestionRepository questionRepository, EndpointUtil endpointUtil, 
			GenericService service, CourseRepository courseRepository,
			CascadeDeleteService cascadeDeleteService,
			QuestionAssigmentRepository questionAssigmentRepository) {
		this.questionRepository = questionRepository;
		this.endpointUtil = endpointUtil;
		this.service = service;
		this.courseRepository = courseRepository;
		this.cascadeDeleteService = cascadeDeleteService;
		this.questionAssigmentRepository = questionAssigmentRepository;
	}

	@ApiOperation(value = "Return valid questions for that course (valid questions are"
			+ " questions with at least two choices and one of the choices is correct"
			+ " and is not already associated with that assignment)",
			response = Question[].class)
	@GetMapping(path = "{courseId}/{assignmentId}")
	public ResponseEntity<?> getQuestionById(@PathVariable long courseId,
				@PathVariable long assignmentId) {
		List<Question> questions = questionRepository.listQuestionsByCourseNotAssociatedWithAnAssignment(courseId, assignmentId);
		List<Question> validQuestions = questions.stream().filter(question -> hasMoreThanOneChoice(question) 
				&& hasOnlyOneCorrectAnswer(question))
			.collect(Collectors.toList());
		return endpointUtil.returnObjectOrNotFound(validQuestions);
	}

	private boolean hasOnlyOneCorrectAnswer(Question question) {
		return question.getChoices().stream().filter(Choice::isCorrectAnswer).count() == 1;
	}

	private boolean hasMoreThanOneChoice(Question question) {
		return question.getChoices().size() > 1;
	}

}

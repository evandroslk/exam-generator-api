package br.com.devdojo.examgenerator.endpoint.v1.question;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("v1/professor/course/question")
public class QuestionEndpoint {

	private final QuestionRepository questionRepository;
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService service;

	@Autowired
	public QuestionEndpoint(QuestionRepository questionRepository, EndpointUtil endpointUtil, 
			GenericService service, CourseRepository courseRepository) {
		this.questionRepository = questionRepository;
		this.endpointUtil = endpointUtil;
		this.service = service;
		this.courseRepository = courseRepository;
	}

	@ApiOperation(value = "Return a question based on it's id", response = Question.class)
	@GetMapping(path = "{id}")
	public ResponseEntity<?> getQuestionById(@PathVariable long id) {
		return endpointUtil.returnObjectOrNotFound(questionRepository.findOne(id));
	}

	@ApiOperation(value = "Return a list of questions related to course", response = Question.class)
	@GetMapping(path = "/list/{courseId}")
	public ResponseEntity<?> listQuestions(
			@ApiParam("Course id") @PathVariable long courseId,
			@ApiParam("Question title") @RequestParam(value = "title", defaultValue = "") String title) {
		return new ResponseEntity<>(questionRepository.listQuestionsByCourseAndTitle(courseId, title), 
				HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a specific course and return 200 OK with no body")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		service.throwResourceNotFoundIfDoesNotExist(id, questionRepository, "Question not found");
		questionRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(value = "Update question and return 200 OK with no body")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Question question) {
		service.throwResourceNotFoundIfDoesNotExist(question, questionRepository, "Question not found");
		questionRepository.save(question);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(value = "Create a question and return the question created")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Question question) {
		service.throwResourceNotFoundIfDoesNotExist(question.getCourse(), courseRepository, "Course not found");
		question.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(questionRepository.save(question), HttpStatus.OK);
	}

}

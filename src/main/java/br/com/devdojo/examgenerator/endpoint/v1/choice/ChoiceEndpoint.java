package br.com.devdojo.examgenerator.endpoint.v1.choice;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.repository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("v1/professor/course/question/choice")
public class ChoiceEndpoint {

	private final QuestionRepository questionRepository;
	private final ChoiceRepository choiceRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService service;

	@Autowired
	public ChoiceEndpoint(QuestionRepository questionRepository, ChoiceRepository choiceRepository,
			EndpointUtil endpointUtil, GenericService service) {
		this.questionRepository = questionRepository;
		this.choiceRepository = choiceRepository;
		this.endpointUtil = endpointUtil;
		this.service = service;
	}
	
	@ApiOperation(value = "Return a choice based on it's id", response = Choice.class)
	@GetMapping(path = "{id}")
	public ResponseEntity<?> getChoiceById(@PathVariable long id) {
		return endpointUtil.returnObjectOrNotFound(choiceRepository.findOne(id));
	}
	
	@ApiOperation(value = "Return a list of choices related to questionId", response = Choice[].class)
	@GetMapping(path = "/list/{questionId}")
	public ResponseEntity<?> listChoicesByQuestionId(@ApiParam("Question id") @PathVariable long questionId) {
		return new ResponseEntity<>(choiceRepository.listChoicesByQuestionId(questionId), 
				HttpStatus.OK);
	}
	
	@ApiOperation(value = "Create a choice and return the choice created",
			notes = "If this choice's correctAnswer is true all other choices' correctAnswer "
					+ "related to this question will be updated to false")
	@PostMapping
	@Transactional
	public ResponseEntity<?> create(@Valid @RequestBody Choice choice) {
		validateChoicesQuestion(choice);
		choice.setProfessor(endpointUtil.extractProfessorFromToken());
		Choice savedChoice = choiceRepository.save(choice);
		updateChangingOtherChoicesCorrectAnswerToFalse(choice);
		return new ResponseEntity<>(savedChoice, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update a choice and return 200 with no body",
			notes = "If this choice's correctAnswer is true all other choices' correctAnswer "
					+ "related to this question will be updated to false")
	@PutMapping
	@Transactional
	public ResponseEntity<?> update(@Valid @RequestBody Choice choice) {
		validateChoicesQuestion(choice);
		updateChangingOtherChoicesCorrectAnswerToFalse(choice);
		choiceRepository.save(choice);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete a specific choice and return 200 OK with no body")
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		service.throwResourceNotFoundIfDoesNotExist(id, choiceRepository, "Choise not found");
		choiceRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void validateChoicesQuestion(@Valid @RequestBody Choice choice) {
		service.throwResourceNotFoundIfDoesNotExist(choice.getQuestion(), questionRepository, 
				"The question related to this choice was not found");
	}
	
	private void updateChangingOtherChoicesCorrectAnswerToFalse(Choice choice) {
		if (choice.isCorrectAnswer()) {
			choiceRepository.updateAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion());
		}
	}

}

package br.com.devdojo.examgenerator.endpoint.v1.assignment;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Assignment;
import br.com.devdojo.examgenerator.persistence.repository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("v1/professor/course/assignment")
public class AssignmentEndpoint {

	private final AssignmentRepository assignmentRepository;
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService service;
	private final CascadeDeleteService cascadeDeleteService;

	@Autowired
	public AssignmentEndpoint(AssignmentRepository assignmentRepository, EndpointUtil endpointUtil, 
			GenericService service, CourseRepository courseRepository,
			CascadeDeleteService cascadeDeleteService) {
		this.assignmentRepository = assignmentRepository;
		this.endpointUtil = endpointUtil;
		this.service = service;
		this.courseRepository = courseRepository;
		this.cascadeDeleteService = cascadeDeleteService;
	}

	@ApiOperation(value = "Return an assignment based on it's id", response = Assignment.class)
	@GetMapping(path = "{id}")
	public ResponseEntity<?> getAssignmentById(@PathVariable long id) {
		return endpointUtil.returnObjectOrNotFound(assignmentRepository.findOne(id));
	}

	@ApiOperation(value = "Return a list of assignments related to course", response = Assignment[].class)
	@GetMapping(path = "/list/{courseId}")
	public ResponseEntity<?> listAssignments(
			@ApiParam("Course id") @PathVariable long courseId,
			@ApiParam("Assigment title") @RequestParam(value = "title", defaultValue = "") String title) {
		return new ResponseEntity<>(assignmentRepository.listAssignmentsByCourseAndTitle(courseId, title), 
				HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a specific assigment and "
			+ "return 200 OK with no body")
	@DeleteMapping(path = "{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable long id) {
		validateAssignmentExistenceOnDB(id);
		cascadeDeleteService.deleteAssignmentAndAllRelatedEntities(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void validateAssignmentExistenceOnDB(long id) {
		service.throwResourceNotFoundIfDoesNotExist(id, assignmentRepository, "Assignment not found");
	}

	@ApiOperation(value = "Update assigment and return 200 OK with no body")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Assignment assignment) {
		validateAssignmentExistenceOnDB(assignment.getId());
		assignmentRepository.save(assignment);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(value = "Create a assigment and return the assigment created")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Assignment assignment) {
		service.throwResourceNotFoundIfDoesNotExist(assignment.getCourse(), courseRepository,
				"Course not found");
		assignment.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(assignmentRepository.save(assignment), HttpStatus.OK);
	}

}

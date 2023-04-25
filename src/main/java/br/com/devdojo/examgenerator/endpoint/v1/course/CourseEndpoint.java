package br.com.devdojo.examgenerator.endpoint.v1.course;

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
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("v1/professor/course")
public class CourseEndpoint {

	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService service;
	private final CascadeDeleteService cascadeDeleteService;

	@Autowired
	public CourseEndpoint(CourseRepository courseRepository, EndpointUtil endpointUtil, 
			GenericService service, CascadeDeleteService cascadeDeleteService) {
		this.courseRepository = courseRepository;
		this.endpointUtil = endpointUtil;
		this.service = service;
		this.cascadeDeleteService = cascadeDeleteService;
	}

	@ApiOperation(value = "Return a course based on it's id", response = Course.class)
	@GetMapping(path = "{id}")
	public ResponseEntity<?> getCourseById(@PathVariable long id) {
		return endpointUtil.returnObjectOrNotFound(courseRepository.findOne(id));
	}

	@ApiOperation(value = "Return a list of courses related to professor", response = Course.class)
	@GetMapping(path = "list")
	public ResponseEntity<?> listCourses(
			@ApiParam("Course name") @RequestParam(value = "name", defaultValue = "") String name) {
		return new ResponseEntity<>(courseRepository.listCoursesByName(name), HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a specific course and all related questions and choices and"
			+ " return 200 OK with no body")
	@DeleteMapping(path = "{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable long id) {
		validateCourseExistenceOnDB(id);
		cascadeDeleteService.deleteCourseAndAllRelatedEntities(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void validateCourseExistenceOnDB(long id) {
		service.throwResourceNotFoundIfDoesNotExist(id, courseRepository, "Course not found");
	}

	@ApiOperation(value = "Update a course and return 200 OK with no body")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Course course) {
		validateCourseExistenceOnDB(course.getId());
		courseRepository.save(course);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(value = "Create a course and return the course created")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Course course) {
		course.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
	}

}

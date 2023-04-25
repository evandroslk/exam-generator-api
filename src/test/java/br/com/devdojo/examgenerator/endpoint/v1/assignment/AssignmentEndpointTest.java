package br.com.devdojo.examgenerator.endpoint.v1.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.course.CourseEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Assignment;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.ProfessorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AssignmentEndpointTest {

	@MockBean
	private AssignmentRepository assignmentRepository;

	@MockBean
	private ProfessorRepository professorRepository;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private HttpEntity<Void> professorHeader;
	private HttpEntity<Void> wrongHeader;

	private Assignment assignment = mockAssignment();

	public static Assignment mockAssignment() {
		Assignment assignment = new Assignment();
		assignment.setId(1L);
		assignment.setTitle("The Java Exam from Hell");
		assignment.setCourse(CourseEndpointTest.mockCourse());
		assignment.setProfessor(ProfessorEndpointTest.mockProfessor());
		return assignment;
	}

	@Before
	public void configProcessorHeader() {
		String body = "{\"username\":\"evandro\",\"password\":\"devdojo\"}";
		HttpHeaders headers = testRestTemplate.postForEntity("/login", body, String.class).getHeaders();
		this.professorHeader = new HttpEntity<>(headers);
	}

	@Before
	public void configWrongHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "11111");
		this.wrongHeader = new HttpEntity<>(headers);
	}

	@Before
	public void setup() {
		BDDMockito.when(assignmentRepository.findOne(assignment.getId())).thenReturn(assignment);
		BDDMockito.when(assignmentRepository.listAssignmentsByCourseAndTitle(assignment.getCourse().getId(), ""))
				.thenReturn(Collections.singletonList(assignment));
		BDDMockito
				.when(assignmentRepository.listAssignmentsByCourseAndTitle(assignment.getCourse().getId(), "The Java?"))
				.thenReturn(Collections.singletonList(assignment));
	}

	@Test
	public void getAssignmentByIdWhenTokenIsWrongShouldReturn403() throws Exception {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/1", HttpMethod.GET,
				wrongHeader, String.class);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}

	@Test
	public void listAssignmentsByCourseAndTitleWhenTokenIsWrongShouldReturn403() throws Exception {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/list/1/?title=",
				HttpMethod.GET, wrongHeader, String.class);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}

	@Test
	public void listAllAssignmnetsByCourseAndTitleWhenTitleDoesNotExistsShouldReturnEmptyList() throws Exception {
		ResponseEntity<List<Assignment>> exchange = testRestTemplate.exchange(
				"/v1/professor/course/assignment/list/1/?title=xaxa", HttpMethod.GET, professorHeader,
				new ParameterizedTypeReference<List<Assignment>>() {
				});
		assertThat(exchange.getBody()).isEmpty();
	}

	@Test
	public void listAllAssignmentsByCourseWhenTitleExistsShouldReturn200() throws Exception {
		ResponseEntity<String> exchange = testRestTemplate.exchange(
				"/v1/professor/course/assignment/list/1/?title=The Java", HttpMethod.GET, professorHeader,
				String.class);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void getAssignmentByIdWithoutIdShouldReturn400() throws Exception {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/", HttpMethod.GET,
				professorHeader, String.class);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
	}

	@Test
	public void getAssignmentByIdWhenAssignmentIdDoesNotExistsShouldReturn404() throws Exception {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/-1",
				HttpMethod.GET, professorHeader, String.class);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
	}

	@Test
	public void getAssignmentByIdWhenAssignmentIdExistsShouldReturn200() throws Exception {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/1", HttpMethod.GET,
				professorHeader, String.class);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void deleteAssignmentWhenIdExistsShouldReturn200() throws Exception {
		long id = 1L;
		BDDMockito.doNothing().when(assignmentRepository).delete(id);
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/{id}",
				HttpMethod.DELETE, professorHeader, String.class, id);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void deleteAssignmentWhenIdDoesNotExistsShouldReturn404() throws Exception {
		long id = -1L;
		BDDMockito.doNothing().when(assignmentRepository).delete(id);
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/{id}",
				HttpMethod.DELETE, professorHeader, String.class, id);
		assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
	}

	@Test
	public void createAssignmentWhenTitleIsNullShouldReturn400() throws Exception {
		Assignment assignment = assignmentRepository.findOne(1L);
		assignment.setTitle(null);
		assertThat(createAssignment(assignment).getStatusCodeValue()).isEqualTo(400);

	}

	@Test
	public void createAssignmentWhenEverythingIsRightShouldReturn200() throws Exception {
		Assignment assignment = assignmentRepository.findOne(1L);
		assignment.setId(null);
		assertThat(createAssignment(assignment).getStatusCodeValue()).isEqualTo(200);

	}

	@Test
	public void createAssignmentWhenCourseDoesNotExistsShouldReturn404() throws Exception {
		Assignment assignment = assignmentRepository.findOne(1L);
		assignment.setCourse(new Course());
		assertThat(createAssignment(assignment).getStatusCodeValue()).isEqualTo(404);
	}

	private ResponseEntity<String> createAssignment(Assignment assignment) {
		BDDMockito.when(assignmentRepository.save(assignment)).thenReturn(assignment);
		return testRestTemplate.exchange("/v1/professor/course/assignment/", HttpMethod.POST,
				new HttpEntity<>(assignment, professorHeader.getHeaders()), String.class);
	}

}

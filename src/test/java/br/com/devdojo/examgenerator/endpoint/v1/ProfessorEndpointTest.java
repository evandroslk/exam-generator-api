package br.com.devdojo.examgenerator.endpoint.v1;

import br.com.devdojo.examgenerator.persistence.model.Professor;

public class ProfessorEndpointTest {

	public static Professor mockProfessor() {
		Professor professor = new Professor();
		professor.setId(1L);
		professor.setName("Will");
		professor.setEmail("will@something.com");
		return professor;	
	}

}

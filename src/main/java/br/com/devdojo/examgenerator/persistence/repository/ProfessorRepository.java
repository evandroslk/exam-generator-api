package br.com.devdojo.examgenerator.persistence.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.devdojo.examgenerator.persistence.model.Professor;

public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long> {
	Professor findByEmail(String email);
}

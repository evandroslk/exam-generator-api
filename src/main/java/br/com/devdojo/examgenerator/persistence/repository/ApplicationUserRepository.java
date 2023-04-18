package br.com.devdojo.examgenerator.persistence.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {

	ApplicationUser findByUsername(String username);
	
}

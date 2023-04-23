package br.com.devdojo.examgenerator.endpoint.v1.genericservice;

import org.springframework.stereotype.Service;

import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.AbstractEntity;
import br.com.devdojo.examgenerator.persistence.repository.CustomPagingAndSortingRepository;

@Service
public class GenericService {

	public <T extends AbstractEntity, ID extends Long> void throwResourceNotFoundIfDoesNotExist(T t,
			CustomPagingAndSortingRepository<T, ID> repository, String msg) {
		if (t == null || t.getId() == null || repository.findOne(t.getId()) == null) {
			throw new ResourceNotFoundException(msg);
		}
	}

	public <T extends AbstractEntity, ID extends Long> void throwResourceNotFoundIfDoesNotExist(long id,
			CustomPagingAndSortingRepository<T, ID> repository, String msg) {
		if (id == 0 || repository.findOne(id) == null) {
			throw new ResourceNotFoundException(msg);
		}
	}

}

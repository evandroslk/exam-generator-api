package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.devdojo.examgenerator.persistence.model.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

	@Query("select c from Course c where c.id = ?1 and c.professor = ?#{principal.professor}")
	Course findOne(Long id);
	
	@Query("select c from Course c where c = ?1 and c.professor = ?#{principal.professor}")
	Course findOne(Course course);
	
	@Query("select c from Course c where c.name like %?1% and c.professor = ?#{principal.professor}")
	List<Course> listCourses(String name);

}

package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Course extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "The field name cannot be empty")
	@ApiModelProperty(notes = "The name of the course")
	private String name;

	@ManyToOne(optional = false)
	private Professor professor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

}

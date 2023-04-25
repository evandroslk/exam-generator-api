package br.com.devdojo.examgenerator.persistence.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Assignment extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "The field title cannot be empty")
	@ApiModelProperty(notes = "The title of the assignment")
	private String title;

	private LocalDateTime createdAt = LocalDateTime.now();

	@ManyToOne(optional = false)
	private Course course;

	@ManyToOne(optional = false)
	private Professor professor;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

}

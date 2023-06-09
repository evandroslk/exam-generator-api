package br.com.devdojo.examgenerator.persistence.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Question extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "The field title cannot be empty")
	@ApiModelProperty(notes = "The title of the question")
	private String title;

	@ManyToOne(optional = false)
	private Course course;

	@ManyToOne(optional = false)
	private Professor professor;

	@OneToMany(mappedBy = "question")
	@JsonIgnore
	private List<Choice> choices;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

}

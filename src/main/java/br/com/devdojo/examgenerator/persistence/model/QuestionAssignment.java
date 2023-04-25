package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Questions associated with assigments, the sum of all grades for"
		+ " one assigment must be equal to 100 in order to be used by the students")
public class QuestionAssignment extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "The field assignment cannot be empty")
	@ApiModelProperty(notes = "The assignment that the question belongs to")
	@ManyToOne
	private Assignment assignment;

	@NotEmpty(message = "The field question cannot be empty")
	@ApiModelProperty(notes = "The question for associated with that assigment")
	@ManyToOne
	private Question question;

	@ManyToOne(optional = false)
	private Professor professor;

	private Double grade;

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

}

package model.entities;

public class Student extends Pessoa {
	private static final long serialVersionUID = 1L;
	
	private String cpf;
	
	private Course course;
	
	public Student() {	
		super();
	}

	public Student(Integer id, String name, String cpf, Course course) {
		super(id, name);
		this.cpf = cpf;
		this.course = course;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return course.getId() +" - "+course.getName() +" - "+ course.getSchool();
	}	
}

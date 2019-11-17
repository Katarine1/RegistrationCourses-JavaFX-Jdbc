package model.entities;

public class Teacher extends Pessoa {
	private static final long serialVersionUID = 1L;
	
	private String registration;
	
	private Course course;
	
	public Teacher() {
		super();
	}

	public Teacher(Integer id, String name, String registration, Course course) {
		super(id, name);
		this.registration = registration;
		this.course = course;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
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

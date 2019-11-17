package model.dao;

import java.util.List;

import model.entities.Course;
import model.entities.Student;

public interface StudentDao {

	void insert(Student student);
	void update(Student student);
	void deleteById(Integer id);
	Student findById(Integer id);
	List<Student> findAll();
	List<Student> findByCourse(Course course);
}

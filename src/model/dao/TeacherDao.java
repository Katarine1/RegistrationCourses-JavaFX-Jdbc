package model.dao;

import java.util.List;

import model.entities.Course;
import model.entities.Teacher;

public interface TeacherDao {

	void insert(Teacher teacher);
	void update(Teacher teacher);
	void deleteById(Integer id);
	Teacher findById(Integer id);
	List<Teacher> findAll();
	List<Teacher> findByCourse(Course course);
}

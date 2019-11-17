package model.dao;

import java.util.List;

import model.entities.Course;

public interface CourseDao {

	void insert(Course course);
	void update(Course course);
	void deleteById(Integer id);
	Course findById(Integer id);
	List<Course> findAll();
}

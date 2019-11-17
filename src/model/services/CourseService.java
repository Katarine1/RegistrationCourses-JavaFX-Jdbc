package model.services;

import java.util.List;

import model.dao.CourseDao;
import model.dao.DaoFactory;
import model.entities.Course;

public class CourseService {

	private CourseDao dao = DaoFactory.createCourseDao();
	
	public List<Course> findAll(){
		return dao.findAll();
	}
		
	public void save(Course course) {
		if(course.getId() == null) {
			dao.insert(course);
		}
	}
	
	public void update(Course course) {
		if(course.getId() != null) {
			dao.update(course);
		}
	}
	
	public void findById(Course course) {
		dao.findById(course.getId());
	}
			
	public void remove(Course course) {
		dao.deleteById(course.getId());
	}
}

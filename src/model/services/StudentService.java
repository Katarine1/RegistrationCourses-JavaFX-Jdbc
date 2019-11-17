package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.StudentDao;
import model.entities.Student;

public class StudentService {

	private StudentDao dao = DaoFactory.createStudentDao();
	
	public List<Student> findAll(){
		return dao.findAll();
	}	
	
	public void save(Student student) {
		if(student.getId() == null) {
			dao.insert(student);
		}
	}
	
	public void update(Student student) {
		if(student.getId() != null) {
			dao.update(student);
		}
	}
		
	public void remove(Student student) {
		dao.deleteById(student.getId());
	}
}

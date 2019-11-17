package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TeacherDao;
import model.entities.Teacher;

public class TeacherService {

	private TeacherDao dao = DaoFactory.createTeacherDao();
	
	public List<Teacher> findAll(){
		return dao.findAll();
	}
			
	public void save(Teacher teacher) {
		if(teacher.getId() == null) {
			dao.insert(teacher);
		}
	}
	
	public void update(Teacher teacher) {
		if(teacher.getId() != null) {
			dao.update(teacher);
		}
	}
	
	public void remove(Teacher teacher) {
		dao.deleteById(teacher.getId());
	}
}

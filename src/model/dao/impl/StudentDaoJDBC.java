package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.StudentDao;
import model.entities.Course;
import model.entities.Student;

public class StudentDaoJDBC implements StudentDao {
	
	private static Connection conn;
	
	private PreparedStatement st = null;
	private ResultSet rs = null;
	
	public StudentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Student student) {
		try {
			String sql = "INSERT INTO student (name, cpf, courseId) VALUE (?, ?, ?)";
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, student.getName());
			st.setString(2, student.getCpf());
			st.setInt(3, student.getCourse().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					student.setId(id);
				}
				DB.closeResultSet(rs);
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Student student) {
		try {
			String sql = "UPDATE student SET name= ?, cpf= ?, courseId= ? WHERE id = ?";
			st = conn.prepareStatement(sql);
			
			st.setString(1, student.getName());
			st.setString(2, student.getCpf());
			st.setInt(3, student.getCourse().getId());
			st.setInt(4, student.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		try {
			String sql = "DELETE FROM student WHERE id = ?";
			st = conn.prepareStatement(sql);
			
			st.setInt(1, id);
			
			st.executeUpdate();			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Student findById(Integer id) {
		try {
			String sql = 
					"SELECT student.*, course.name as CourseName "
					+ "FROM student INNER JOIN course "
					+ "ON student.courseId = course.id "
					+ "WHERE student.id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if(rs.next()) {
				Course course = instantiateCourse();
				Student student = instantiateStudent(course);
				return student;
			}
			return null;			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Student> findAll() {
		try {
			String sql = 
			"SELECT student.*, course.name as CourseName, course.school as CourseSchool "
			+ "FROM student INNER JOIN course "
			+ "ON student.courseId = course.id "
			+ "ORDER BY name";
			
			st = conn.prepareStatement(sql);
			
			rs = st.executeQuery();
			
			List<Student> list = new ArrayList<>();
			Map<Integer, Course> map = new HashMap<>();
			
			while(rs.next()) {
				Course course = map.get(rs.getInt("courseId"));
				
				if(course ==null) {
					course = instantiateCourse();
					map.put(rs.getInt("courseId"), course);
				}
				
				Student student = instantiateStudent(course);
				list.add(student);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Student> findByCourse(Course course) {
		try {
			String sql = 
					"SELECT student.*, course.name as CourseName, course.school as CourseSchool "
					+ "FROM student INNER JOIN course "
					+ "ON student.courseId = course.id "
					+ "WHERE courseId = ? "
					+ "ORDER BY name";
			st = conn.prepareStatement(sql);
			st.setInt(1, course.getId());
			
			rs = st.executeQuery();
			
			List<Student> list = new ArrayList<>();
			Map<Integer, Course> map = new HashMap<>();
			
			while(rs.next()) {
				course = map.get(rs.getInt("courseId"));
				if(course == null) {
					course = instantiateCourse();
					map.put(rs.getInt("courseId"), course);
				}
				
				Student student = instantiateStudent(course);
				list.add(student);				
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Student instantiateStudent(Course course) throws SQLException {
		Student student = new Student();
		student.setId(rs.getInt("id"));
		student.setName(rs.getString("name"));
		student.setCpf(rs.getString("cpf"));
		student.setCourse(course);
		return student;
	}
	
	private Course instantiateCourse() throws SQLException {
		Course course = new Course();
		course.setId(rs.getInt("courseId"));
		course.setName(rs.getString("CourseName"));
		course.setSchool(rs.getString("CourseSchool"));
		return course;
	}
}

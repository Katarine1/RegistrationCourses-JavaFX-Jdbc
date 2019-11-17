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
import model.dao.TeacherDao;
import model.entities.Course;
import model.entities.Teacher;

public class TeacherDaoJDBC implements TeacherDao {
	
	private static Connection conn;
	
	private PreparedStatement st = null;
	private ResultSet rs = null;
	
	public TeacherDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Teacher teacher) {
		try {
			String sql = "INSERT INTO teacher (name, registration, courseId) VALUE (?, ?, ?)";
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, teacher.getName());
			st.setString(2, teacher.getRegistration());
			st.setInt(3, teacher.getCourse().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					teacher.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
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
	public void update(Teacher teacher) {
		try {
			String sql = "UPDATE teacher SET name= ?, registration= ?, courseId= ? WHERE id= ?";
			st = conn.prepareStatement(sql);
			
			st.setString(1, teacher.getName());
			st.setString(2, teacher.getRegistration());
			st.setInt(3, teacher.getCourse().getId());
			st.setInt(4, teacher.getId());
			
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
			String sql = "DELETE FROM teacher WHERE id = ?";
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
	public Teacher findById(Integer id) {
		try {
			String sql = 
					"SELECT teacher.*, course.name as CourseName "
					+ "FROM teacher INNER JOIN course "
					+ "ON teacher.courseId = course.id "
					+ "WHERE teacher.id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if(rs.next()) {
				Course course = instantiateCourse();
				Teacher teacher = instatiateTeacher(course);
				return teacher;
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
	public List<Teacher> findAll() {
		try {
			String sql = 
					"SELECT teacher.*, course.name as CourseName, course.school as CourseSchool "
					+ "FROM teacher INNER JOIN course "
					+ "ON teacher.courseId = course.id "
					+ "ORDER BY name";
			st =  conn.prepareStatement(sql);
			
			rs = st.executeQuery();
			
			List<Teacher> list = new ArrayList<>();
			Map<Integer, Course> map = new HashMap<>();
			
			while(rs.next()) {
				Course course = map.get(rs.getInt("courseId"));
				
				if(course == null) {
					course = instantiateCourse();
					map.put(rs.getInt("courseId"), course);
				}
				
				Teacher teacher = instatiateTeacher(course);
				list.add(teacher);
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
	public List<Teacher> findByCourse(Course course) {
		try {
			String sql = 
					"SELECT teacher.*, course.name as CourseName, course.school as CourseSchool "
					+ "FROM teacher INNER JOIN course "
					+ "ON teacher.courseId = course.id "
					+ "WHERE courseId = ? "
					+ "ORDER BY name";
			st = conn.prepareStatement(sql);
			st.setInt(1, course.getId());
			
			rs = st.executeQuery();
			
			List<Teacher> list = new ArrayList<>();
			Map<Integer, Course> map = new HashMap<>();
			
			while(rs.next()) {
				course = map.get(rs.getObject("courseId"));
				if(course == null) {
					course = instantiateCourse();
					map.put(rs.getInt("courseId"), course);
				}
				
				Teacher teacher = instatiateTeacher(course);
				list.add(teacher);
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
	
	private Teacher instatiateTeacher(Course course) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setId(rs.getInt("id"));
		teacher.setName(rs.getString("name"));
		teacher.setRegistration(rs.getString("registration"));
		teacher.setCourse(course);
		return teacher;
	}
	
	private Course instantiateCourse() throws SQLException {
		Course course = new Course();
		course.setId(rs.getInt("courseId"));
		course.setName(rs.getString("CourseName"));
		course.setSchool(rs.getString("CourseSchool"));
		return course;
	}
}

package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.CourseDao;
import model.entities.Course;

public class CourseDaoJDBC implements CourseDao {
	
	private static Connection conn;
	
	private PreparedStatement st = null;
	private ResultSet rs = null;
	
	public CourseDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Course course) {
		try {
			String sql = "INSERT INTO course (name, school) VALUE (?, ?)";
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
							
			st.setString(1, course.getName());
			st.setString(2, course.getSchool());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					course.setId(id);
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
	public void update(Course course) {		
		try {
			String sql = "UPDATE course SET name = ?, school = ? WHERE id = ?";
			st = conn.prepareStatement(sql);
			
			st.setString(1, course.getName());
			st.setString(2, course.getSchool());
			st.setInt(3, course.getId());
			
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
			String sql = "DELETE FROM course WHERE id= ?";
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
	public Course findById(Integer id) {
		try {
			String sql = "SELECT * FROM course WHERE id= ?";
			st = conn.prepareStatement(sql);
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if(rs.next()) {
				Course course = new Course();
				course.setId(rs.getInt("id"));
				course.setName(rs.getString("name"));
				course.setSchool(rs.getString("school"));
				return course;
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
	public List<Course> findAll() {
		try {
			String sql = "SELECT * FROM course ORDER BY name";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			List<Course> list = new ArrayList<>();
			
			while(rs.next()) {
				Course course = new Course();
				course.setId(rs.getInt("id"));
				course.setName(rs.getString("name"));
				course.setSchool(rs.getString("school"));
				list.add(course);				
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
}

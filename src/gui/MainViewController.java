package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.entities.Course;
import model.entities.Student;
import model.entities.Teacher;
import model.services.CourseService;
import model.services.StudentService;
import model.services.TeacherService;

public class MainViewController implements Initializable {

	public static Stage  parentStage;
	
	@FXML
	private MenuItem menuItemCourse;
	
	@FXML
	private MenuItem menuItemStudent;
	
	@FXML
	private MenuItem menuItemTeacher;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemAboutAction(ActionEvent event) {
		loadDialog("/gui/About.fxml", "About", x -> {});
	}
	
	@FXML
	public void onMenuItemCourseAction(ActionEvent event) {		
		loadDialog("/gui/CourseList.fxml", "Course",(CourseListController controller) -> {
			Course course = new Course();
			controller.setCourse(course);
			controller.setCourseService(new CourseService());			
		});		
	}
	
	@FXML
	public void onMenuItemStudentAction(ActionEvent event) {
		loadDialog("/gui/StudentList.fxml", "Student", (StudentListController controller) -> {
			Student student = new Student();
			controller.setStudent(student);
			controller.setStudentService(new StudentService());
			
			Course course = new Course();
			controller.setCourse(course);
			controller.setCourseService(new CourseService());
		});
	}
	
	@FXML
	public void onMenuItemTeacherAction(ActionEvent event) {
		loadDialog("/gui/TeacherList.fxml", "Teacher", (TeacherListController controller) -> {
			Teacher teacher = new Teacher();
			controller.setTeacher(teacher);
			controller.setTeacherService(new TeacherService());
			
			Course course = new Course();
			controller.setCourse(course);
			controller.setCourseService(new CourseService());
		});
	}
	
	private synchronized <T> void loadDialog(String file, String title, Consumer<T> initializingAction) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(file));
			Scene scene = new Scene(parent);
			parentStage = new Stage();			
			parentStage.setTitle(title);
			//parentStage.getIcons().add(new Image("/img/img.jpg"));
			parentStage.setResizable(false);
			parentStage.setScene(scene);			
			parentStage.show();			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub		
	}	
}

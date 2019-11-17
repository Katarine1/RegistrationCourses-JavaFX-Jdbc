package gui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Course;
import model.entities.Student;
import model.entities.Teacher;
import model.services.CourseService;
import model.services.TeacherService;

public class TeacherListController implements Initializable, DataChangeListener {

	private TeacherService teacherService;
	private CourseService courseService;
	private Course course;
	private Teacher teacher;
	
	int id = 0;
	String name = null, registration = null;
	
	@FXML
	private TableView<Teacher> tableViewTeacher;

	@FXML
	private TableColumn<Teacher, Integer> tableColumnIdTeacher;

	@FXML
	private TableColumn<Teacher, String> tableColumnNameTeacher;

	@FXML
	private TableColumn<Teacher, String> tableColumnRegistrationTeacher;
	
	@FXML
	private TableColumn<Teacher, String> tableColumnCourse;

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;

	@FXML
	private TextField textFieldRegistration;

	@FXML
	private ComboBox<Teacher> comboBoxCourse;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnDelete;
	
	ObservableList<Teacher> obsListTeacher;

	Optional<ButtonType> result;
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		teacherService = new TeacherService();
		if (teacherService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			name = textFieldName.getText().toString();
			registration = textFieldRegistration.getText().toString();
			
			teacher = new Teacher(null, name, registration, comboBoxCourse.getSelectionModel().getSelectedItem().getCourse());
			teacherService.save(teacher);
			Alerts.showAlert("Save", null, "Saved successfully!", AlertType.INFORMATION);
			initializeNodesTeacher();
			clean();
		} catch (DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtnUpdateAction(ActionEvent event) {
		teacherService = new TeacherService();	
		if (teacherService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			id = Integer.parseInt(textFieldId.getText().toString());
			name = textFieldName.getText().toString();
			registration = textFieldRegistration.getText().toString();
			
			teacher = new Teacher(id, name, registration, comboBoxCourse.getSelectionModel().getSelectedItem().getCourse());
			teacherService.update(teacher);
			initializeNodesTeacher();
			Alerts.showAlert("Update", null, "Update successfully!", AlertType.INFORMATION);
			
			clean();
		} catch (DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtnDeleteAction(ActionEvent event) {
		teacherService = new TeacherService();
		if (teacherService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			id = Integer.parseInt(textFieldId.getText().toString());
			name = textFieldName.getText().toString();
			registration = textFieldRegistration.getText().toString();
			
			Alert alert = new Alert(AlertType.CONFIRMATION);		
			alert.setTitle("Delete");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to delete the selected data?");
			result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				teacher = new Teacher();
				teacher.setId(id);
				teacherService.remove(teacher);
				Alerts.showAlert("Delete", null, "Delete successfully!", AlertType.INFORMATION);
				initializeNodesTeacher();
				clean();
			} 
			else if (result.get() == ButtonType.CANCEL) {
				//exit alert
			}
		} catch (DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}	

	private void clean() {
		textFieldId.setText("");
		textFieldName.setText("");
		textFieldRegistration.setText("");
	}
	
	private void initializeNodesTeacher() {
		tableColumnIdTeacher.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNameTeacher.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnRegistrationTeacher.setCellValueFactory(new PropertyValueFactory<>("registration"));
		tableColumnCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
		
		updateTableViewTeacher();
	}

	public void updateTableViewTeacher() {
		teacherService = new TeacherService();
		if (teacherService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Teacher> list = teacherService.findAll();
		obsListTeacher = FXCollections.observableArrayList(list);
		tableViewTeacher.setItems(obsListTeacher);
		comboBoxCourse.setItems(obsListTeacher);
	}
	
	@Override
	public void onDataChanged() {
		//
	}

	@Override
	public void onDataChangedEntity() {
		updateTableViewTeacher();	
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodesTeacher();
	}

	public void setTeacherService(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}

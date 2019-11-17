package gui;

import java.net.URL;
import java.util.ArrayList;
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
import model.services.CourseService;
import model.services.StudentService;

public class StudentListController implements Initializable, DataChangeListener {

	private StudentService studentService;
	private CourseService courseService;
	private Course course;
	private Student student;
	
	int id = 0;
	String name = null, cpf = null;

	@FXML
	private TableView<Student> tableViewStudent;
	
	@FXML
	private TableColumn<Student, Integer> tableColumnIdStudent;

	@FXML
	private TableColumn<Student, String> tableColumnNameStudent;

	@FXML
	private TableColumn<Student, String> tableColumnCPFStudent;
	
	@FXML
	private TableColumn<Student, String> tableColumnNameCourse;
	
	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;

	@FXML
	private TextField textFieldCPF;

	@FXML
	private ComboBox<Student> comboBoxCourse;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnDelete;
	
	ObservableList<Student> obsListStudent;
		
	Optional<ButtonType> result;
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		studentService = new StudentService();	
		if (studentService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			name = textFieldName.getText().toString();
			cpf = textFieldCPF.getText().toString();
			
			student = new Student(null, name, cpf, comboBoxCourse.getSelectionModel().getSelectedItem().getCourse());
			studentService.save(student);
			Alerts.showAlert("Save", null, "Saved successfully!", AlertType.INFORMATION);
			initializeNodesStudent();
			clean();
		} catch (DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtnUpdateAction(ActionEvent event) {
		studentService = new StudentService();	
		if (studentService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			id = Integer.parseInt(textFieldId.getText().toString());
			name = textFieldName.getText().toString();
			cpf = textFieldCPF.getText().toString();
			
			student = new Student(id, name, cpf, comboBoxCourse.getSelectionModel().getSelectedItem().getCourse());
			studentService.update(student);
			initializeNodesStudent();
			Alerts.showAlert("Update", null, "Update successfully!", AlertType.INFORMATION);
			clean();
		} catch (DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtnDeleteAction(ActionEvent event) {
		studentService = new StudentService();	
		if (studentService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			id = Integer.parseInt(textFieldId.getText().toString());
			name = textFieldName.getText().toString();
			cpf = textFieldCPF.getText().toString();
			
			Alert alert = new Alert(AlertType.CONFIRMATION);		
			alert.setTitle("Delete");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to delete the selected data?");
			result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				student = new Student();
				student.setId(id);
				studentService.remove(student);
				Alerts.showAlert("Delete", null, "Delete successfully!", AlertType.INFORMATION);
				initializeNodesStudent();
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
		textFieldCPF.setText("");
	}

	private void initializeNodesStudent() {
		student = new Student();
		tableColumnIdStudent.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNameStudent.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnCPFStudent.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnNameCourse.setCellValueFactory(new PropertyValueFactory<>("course"));		
		updateTableViewStudent();
	}	

	public void updateTableViewStudent() {
		studentService = new StudentService();
		if (studentService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Student> list = studentService.findAll();
		obsListStudent = FXCollections.observableArrayList(list);
				
		tableViewStudent.getItems().setAll(obsListStudent);		
		comboBoxCourse.setItems(obsListStudent);
	}	

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public void onDataChanged() {
		//
	}

	@Override
	public void onDataChangedEntity() {
		updateTableViewStudent();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodesStudent();
	}
}

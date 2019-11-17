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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Course;
import model.services.CourseService;

public class CourseListController implements Initializable, DataChangeListener {
	
	private CourseService service;
	private Course course;
					
	ObservableList<Course> obsList;
	
	int id = 0;
	String name = null, school = null;
	
	@FXML
	private TextField textFieldId;
	
	@FXML
	private TextField textFieldName;
	
	@FXML
	private TextField textFieldSchool;
	
	@FXML
	private Button btnSave;	
		
	@FXML
	private Button btnUpdate;
	
	@FXML
	private Button btnDelete;	
	
	@FXML
	private TableView<Course> tableViewCourse;
	
	@FXML
	private TableColumn<Course, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Course, String> tableColumnName;
	
	@FXML
	private TableColumn<Course, String> tableColumnSchool;
	
	Optional<ButtonType> result;
			
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		service = new CourseService();		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}	
		try {
			name = textFieldName.getText().toString();
			school = textFieldSchool.getText().toString();
			
			course = new Course(null,name, school);
			service.save(course);
			Alerts.showAlert("Save", null, "Saved successfully!", AlertType.INFORMATION);
			initializeNodes();
			clean();
		}
		catch(DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}		
	}	
	
	@FXML
	public void onBtnUpdateAction(ActionEvent event) {
		service = new CourseService();
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}		
		try {
			id = Integer.parseInt(textFieldId.getText().toString());
			name = textFieldName.getText().toString();
			school = textFieldSchool.getText().toString();
			
			course = new Course(id, name, school);						
			service.update(course);	
			Alerts.showAlert("Update", null, "Update successfully!", AlertType.INFORMATION);
			initializeNodes();
			clean();
		}
		catch(DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}		
	}
	
	@FXML
	public void onBtnDeleteAction(ActionEvent event) {
		service = new CourseService();
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			id = Integer.parseInt(textFieldId.getText().toString());
			name = textFieldName.getText().toString();
			school = textFieldSchool.getText().toString();
			
			Alert alert = new Alert(AlertType.CONFIRMATION);		
			alert.setTitle("Delete");
			alert.setHeaderText(null);
			alert.setContentText("Do you want to delete the selected data?");
			result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				course = new Course();
				course.setId(id);
				service.remove(course);
				Alerts.showAlert("Delete", null, "Delete successfully!", AlertType.INFORMATION);
				clean();
				initializeNodes();
			}
			else if (result.get() == ButtonType.CANCEL) {
				//exit alert
			}
		}
		catch(DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}	
	}	
	
	private void clean() {
		textFieldId.setText("");
		textFieldName.setText("");
		textFieldSchool.setText("");
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnSchool.setCellValueFactory(new PropertyValueFactory<>("school"));
				
		updateTableView();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();		
	}
		
	public void updateTableView() {
		service = new CourseService();
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Course> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);		
		tableViewCourse.setItems(obsList);
	}
	
	public void setCourseService(CourseService service) {
		this.service = service;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public void onDataChangedEntity() {
		// TODO Auto-generated method stub		
	}	
}

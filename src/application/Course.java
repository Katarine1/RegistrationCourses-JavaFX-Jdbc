package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Course extends Application {

	@Override
	public void start(Stage stage) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/gui/CourseList.fxml"));
			Scene scene = new Scene(parent);
			stage = new Stage(StageStyle.DECORATED);
			stage.setScene(scene);			
			stage.setTitle("Course");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

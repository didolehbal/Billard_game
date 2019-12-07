import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
public class Menu extends Application {
	BorderPane root;
	Scene scene;
	Rectangle bill;
	public  void start(Stage window)throws Exception {
		root = new BorderPane();
		scene = new Scene(root);
		bill = new Rectangle(600,300);
		bill.setId("billboard");
		root.setCenter(bill);
		scene.getStylesheets().add("style.css");
		window.setScene(scene);
		window.show();
	}
	public static void main(String args []) {
		  Application.launch(args);
	  }
}

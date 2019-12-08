import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class Menu extends Application {
	Pane root;
	Scene scene;

	Background background = new Background(new BackgroundImage(new Image("table.jpeg"),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT));

		Circle circle1=  new Circle(300,100,20);

	public  void start(Stage window)throws Exception {
		root = new Pane();
		root.setBackground(background);
		scene = new Scene(root);
		root.getChildren().add(circle1);
		scene.getStylesheets().add("style.css");
		window.setWidth(500);
		window.setHeight(500);
		window.setScene(scene);
		window.show();
	}
	public static void main(String args []) {
		  Application.launch(args);
	  }

}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu extends Application {

	private BorderPane container;
	private Button btnPvP= new Button();
	private Button btnPvC;
	private Label lblWelcome;

	public static void main(String args []) {

		Application.launch(args);
	}
	public void initUILayout(){
		container = new BorderPane();
		btnPvP = new Button("Player vs Player");
		btnPvC = new Button("Player vs Co");
		lblWelcome = new Label("Welcome to Billard Game");
		HBox row = new HBox();
		row.getChildren().addAll(btnPvP,btnPvC);
		VBox column = new VBox();
		column.getChildren().addAll(lblWelcome,row);
		container.setCenter(column);
	}
	public void initUIStyle(){
		container.getStyleClass().add("container");


		btnPvP.getStyleClass().addAll("btn","pvp");
		btnPvC.getStyleClass().addAll("btn","pvc");

	}
	@Override
	public void start(Stage stage) throws Exception {
		initUILayout();
		initUIStyle();
		Scene s = new Scene(container,1000,800);
		s.getStylesheets().add("assets/styles/menu.css");

		stage.setScene(s);
		stage.show();
	}
}
/*import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class Menu extends Application {
	StackPane root;
	Scene scene;

	ImageView imgV;
	ImageView imgBallWhite;
	public  void start(Stage window)throws Exception {
		root = new StackPane();
		scene = new Scene(root,1000,800);
		
		//bill = new Rectangle(600,300);
		//bill.setId("billboard");
		HBox container = new HBox();
		imgV = new ImageView(new Image("assets/game-3.png"));

		//imgV.setX(150);
		//imgV.setY(150);
		imgBallWhite = new ImageView(new Image("assets/ball_white.png"));
		imgBallWhite.setFitWidth(20);
		imgBallWhite.setPreserveRatio(true);
		imgBallWhite.setX(200);
		imgBallWhite.setY(200);
		imgBallWhite.toFront();
		imgV.toBack();
		RotateTransition rotateTransition = new RotateTransition();
		rotateTransition.setDuration(Duration.millis(50));
		rotateTransition.setNode(imgBallWhite);
		//Setting the angle of the rotation
		rotateTransition.setByAngle(90);

		//Setting the cycle count for the transition
		rotateTransition.setCycleCount(50);

		//Setting auto reverse value to false
		rotateTransition.setAutoReverse(false);

		//Playing the animation


		root.getChildren().addAll(imgV,imgBallWhite);
		EventHandler<MouseEvent> eventHandler =
				new EventHandler<javafx.scene.input.MouseEvent>() {

					@Override
					public void handle(javafx.scene.input.MouseEvent e) {
						rotateTransition.play();
					}
				};
		root.addEventHandler(MouseEvent.MOUSE_CLICKED,eventHandler);
		scene.getStylesheets().add("assets/styles/style.css");
		window.setScene(scene);
		window.show();
	}
	public void animateBall(){
		imgBallWhite.setStyle("-fx-rotate: 90;");
	}
	public static void main(String args []) {
		  Application.launch(args);
	  }
}
*/
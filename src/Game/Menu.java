package Game;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.scene.layout.*;


public class Menu {

	private BorderPane container;
	private Button btnOffline;
	private Button btnOnline;
	private Label lblWelcome;
	private HBox row;
	private VBox column;
	final private double WIDTH=800,HEIGHT=500;
	public Menu(){
		initUILayout();
		initUIStyle();
		initEvents();
	}
	public void initUILayout(){

		container = new BorderPane();
		Image image = new Image("assets/images/billard.jpg");

		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);

		Background background = new Background(new BackgroundImage(image,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				bSize));
		container.setBackground(background);
		container.setPrefSize(WIDTH, HEIGHT);
		btnOffline = new Button("Play Offline");
		btnOnline = new Button("Play Online");
		lblWelcome = new Label("Welcome to Billard Game");

		row = new HBox();
		row.setAlignment(Pos.CENTER);
		row.getChildren().addAll(btnOffline, btnOnline);

		column = new VBox();
		column.setAlignment(Pos.CENTER);
		column.getChildren().addAll(lblWelcome,row);

		container.setCenter(column);
	}
	public void initUIStyle(){

		container.getStyleClass().add("container");
		row.getStyleClass().add("buttons-container");
		column.getStyleClass().add("form-container");
		btnOffline.getStyleClass().addAll("btn","pvp");
		btnOnline.getStyleClass().addAll("btn","pvc");
		lblWelcome.getStyleClass().add("lblWecome");
	}
	public void initEvents(){
		btnOffline.setOnMouseClicked(e -> {
			Main.activate(Main.OFFLINE);
		});
		btnOnline.setOnMouseClicked(e ->{
			Main.activate(Main.ONLINE);
		});
	}

	public BorderPane getContainer() {
		return container;
	}


}

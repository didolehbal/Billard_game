import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.scene.layout.*;


public class Menu {

	private BorderPane container;
	private Button btnPvP;
	private Button btnPvC;
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
		btnPvP = new Button("Player vs Player");
		btnPvC = new Button("Player vs Co");
		lblWelcome = new Label("Welcome to Billard Game");

		row = new HBox();
		row.setAlignment(Pos.CENTER);
		row.getChildren().addAll(btnPvP,btnPvC);

		column = new VBox();
		column.setAlignment(Pos.CENTER);
		column.getChildren().addAll(lblWelcome,row);

		container.setCenter(column);
	}
	public void initUIStyle(){

		container.getStyleClass().add("container");
		row.getStyleClass().add("buttons-container");
		column.getStyleClass().add("form-container");
		btnPvP.getStyleClass().addAll("btn","pvp");
		btnPvC.getStyleClass().addAll("btn","pvc");
		lblWelcome.getStyleClass().add("lblWecome");
	}
	public void initEvents(){
		btnPvP.setOnMouseClicked(e -> {
			Main.activate("Game");
		});
	}

	public BorderPane getContainer() {
		return container;
	}


}

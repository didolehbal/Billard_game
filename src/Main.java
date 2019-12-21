import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {
    public static Scene s;

    protected static void activate(String name){
        System.out.println(name + " rendering...");
            switch (name){
                case "Menu" : s.setRoot(new Menu().getContainer());
                case "Game" : s.setRoot(new Game().getContainer());
            }
    }

    public static void main(String args []) {

        Application.launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        s = new Scene(new Menu().getContainer(),1000,800);
        s.getStylesheets().add("assets/styles/menu.css");
        stage.setScene(s);
        stage.show();
    }
}

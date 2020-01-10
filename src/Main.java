import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Scene s;
    public static Stage st;
    protected static void activate(String name){
        System.out.println(name + " rendering...");
            switch (name){
                case "Menu" :
                    s.setRoot(new Menu().getContainer());
                    st.sizeToScene(); //resize the stage to fit the scene
                    break;
                case "Game" :
                    s.setRoot(new Game().getGameContainer());
                    st.setHeight(Game.WINDOW_HEIGHT);
                    st.setWidth(Game.WINDOW_WIDTH);
                    st.sizeToScene();
                    st.setResizable(false);
                    break;
            }
    }

    public static void main(String args []) {

        Application.launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        st = stage; // set stage
        s = new Scene(new Menu().getContainer());
        s.getStylesheets().add("assets/styles/menu.css");
        s.getStylesheets().add("assets/styles/header.css");
        stage.setScene(s);
        stage.show();
    }
}

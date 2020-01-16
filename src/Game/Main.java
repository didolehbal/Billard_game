package Game;

import Client.GameOnline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static Scene s;
    public static Stage st;
    public static final String MENU = "MENU";
    public static final String ONLINE = "ONLINE";
    public static final String OFFLINE = "OFFLINE";
    public static void activate(String name){
        System.out.println(name + " rendering...");
            switch (name){
                case MENU:
                    s.setRoot(new Menu().getContainer());
                    st.sizeToScene();
                    st.setResizable(false);
                    break;
                case OFFLINE :
                    s.setRoot(new Game().getGameContainer());
                    st.setHeight(Game.WINDOW_HEIGHT);
                    st.setWidth(Game.WINDOW_WIDTH);
                    st.sizeToScene();
                    st.setResizable(false);
                    break;
                case ONLINE :
                    Pane p = new GameOnline().getGameContainer();
                    if(p == null) break;
                    s.setRoot(p);
                    st.setHeight(GameOnline.WINDOW_HEIGHT);
                    st.setWidth(GameOnline.WINDOW_WIDTH);
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
        s.getStylesheets().add("assets/styles/winBox.css");
        stage.setScene(s);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("AppClosed !");
    }
}

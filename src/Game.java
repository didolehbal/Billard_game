import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class Game {
    Pane container;
    ImageView imgV;
    ImageView bWhite;
    public Game(){
        container = new Pane();
    }
    public void move(Node n,int duration, int x, int y){
        //Creating Translate Transition
        TranslateTransition translateTransition = new TranslateTransition();

        //Setting the duration of the transition
        translateTransition.setDuration(Duration.millis(duration));

        //Setting the node for the transition
        translateTransition.setNode(n);
        //Setting the value of the transition along the x axis.
        translateTransition.setByX(x);
        translateTransition.setByY(y);

        //Setting the cycle count for the transition
        translateTransition.setCycleCount(1);

        //Setting auto reverse value to false
        translateTransition.setAutoReverse(false);

        //Playing the animation
        translateTransition.play();
    }
    public void moveMe(Event e){
        //Creating Translate Transition
        TranslateTransition translateTransition = new TranslateTransition();

        //Setting the duration of the transition
        translateTransition.setDuration(Duration.millis(2000));

        //Setting the node for the transition
        translateTransition.setNode(bWhite);
        //Setting the value of the transition along the x axis.
        translateTransition.setByX(300);
        translateTransition.setByY(100);

        //Setting the cycle count for the transition
        translateTransition.setCycleCount(1);

        //Setting auto reverse value to false
        translateTransition.setAutoReverse(false);

        //Playing the animation
        translateTransition.play();
    }
    public Pane getContainer(){
        imgV = new ImageView(new Image("assets/images/game-3.png"));

        bWhite = new ImageView(new Image("assets/images/ball_white.png"));
        bWhite.setFitWidth(20);
        bWhite.setPreserveRatio(true);
        bWhite.setX(600);
        bWhite.setY(200);

        container.setOnMouseClicked(e -> moveMe(e));
        imgV.toBack();

        container.getChildren().addAll(imgV, bWhite);
        return container;
    }

}

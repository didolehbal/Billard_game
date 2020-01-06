import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


public class Game {
    /*
    * corners are 50px width
    * */
    Pane container;
    ImageView imgV;
    BallsManager ballManager;
    ConstrolStick stick;
    static double WIDTH = 852 , HEIGHT = 426;  //according to wiki  2.84 meters by 1.42 meters ratio : 2.84:1.42 => 852 x 426
    Dimensions dimensions = new Dimensions(852,426);
    PVector mousePosition  = new PVector(0,0);
    boolean isgameOver = false;
    static long MAX_POWER = 2000;
    boolean isMousePressed = true;
    AtomicLong MousePressedTime = new AtomicLong();

    public Game(){
        container = new Pane();
        ballManager = new BallsManager(container);
        stick = new ConstrolStick(container);
    }



    public void updateMousePosition(MouseEvent e) {
        mousePosition = new PVector(e.getX(),e.getY());
        stick.RotateStick(e.getX(),e.getY());
    }
    public void manageMouseclick(){
        container.setOnMousePressed(e->{
            MousePressedTime.set(System.currentTimeMillis());
            isMousePressed = true;

        });
        container.setOnMouseReleased(e->{
            isMousePressed = false;
            long pressed = System.currentTimeMillis() - MousePressedTime.get();
            if(pressed > MAX_POWER)
                pressed = MAX_POWER;
            ballManager.MouseClick(mousePosition, pressed);

        });
    }
    public Pane getContainer(){
        imgV = new ImageView(new Image("assets/images/game-3.png"));
      //  container.setOnMouseClicked(e -> moveMe(e));
        manageMouseclick();
        container.setOnMouseMoved(e -> updateMousePosition(e));
        imgV.toBack();
        imgV.setFitWidth(dimensions.width);
        imgV.setFitHeight(dimensions.height);
        container.getChildren().add(imgV);

        ballManager.drawBalls();
        stick.drawStick();
        Ball white = ballManager.balls[15];
        stick.white = white;
        startTheGame();

        return container;
    }

    private void startTheGame(){
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.030),                // 60 FPS
                new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                        if(!isgameOver) {
                            stick.update(isMousePressed,mousePosition,MousePressedTime.get());
                            // update ball
                            ballManager.checkEdges();
                            ballManager.checkCollisions();
                            if(stick.value == 0)
                                ballManager.updateBallsPositions();

                        }
                        // else show msg end
                    }
                });

        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

    }
    class Dimensions {
        public double width;
        public double height;
        Dimensions(double width,double height){
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "Dimensions{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

}

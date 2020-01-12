import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicLong;


public class Game {
    BorderPane root;
    Pane gameContainer;
    ImageView imgV;
    BallsManager ballManager;
    ControlStick stick;
    static final double TABLE_WIDTH = 852 , TABLE_HEIGHT = 485;  //according to wiki  2.84 meters by 1.42 meters ratio : 2.84:1.42 => 852 x 426
    static final double HEAD_WIDTH=852,HEAD_HEIGHT=75;
    static final double WINDOW_WIDTH = Math.max(TABLE_WIDTH , HEAD_WIDTH), WINDOW_HEIGHT = TABLE_HEIGHT + HEAD_HEIGHT;
    PVector mousePosition  = new PVector(0,0);
    boolean isGameOver = true;
    static final long MAX_POWER = 2000;
    boolean isMousePressed = false;
    AtomicLong MousePressedTime = new AtomicLong();
    PlayerManager playerManager;
    Circle holes[] = new Circle[6];

    public Game(){
        root = new BorderPane();
        gameContainer = new Pane();
        ballManager = new BallsManager(gameContainer);
        stick = new ControlStick(gameContainer);
        playerManager = new PlayerManager();
    }

    public void updateMousePosition() {

        gameContainer.setOnMouseMoved(e -> {
            mousePosition = new PVector(e.getX(),e.getY());
            stick.RotateStick(e.getX(),e.getY());
        });

    }
    public void manageKeysPress(){
        gameContainer.setOnKeyPressed(e ->{
            System.out.println("pressed : "+ e.getCharacter());
        });
    }
    public void manageMouseclick(){
        gameContainer.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> System.out.println("Pressed: " + event.getCode()));

        gameContainer.setOnMouseClicked(e->{
            System.out.println(mousePosition);
            if(e.getButton() == MouseButton.SECONDARY){ // cancel shooting
                isMousePressed = false;
                stick.translationValue = 0;
            }
        });
        gameContainer.setOnMousePressed(e->{
            if(e.getButton() == MouseButton.PRIMARY){ // prepare shooting
                MousePressedTime.set(System.currentTimeMillis());
                isMousePressed = true;
            }


        });
        gameContainer.setOnMouseReleased(e->{
            if(isMousePressed && e.getButton() == MouseButton.PRIMARY){ // shoot
                isMousePressed = false;
                long pressed = System.currentTimeMillis() - MousePressedTime.get();
                if(pressed > MAX_POWER)
                    pressed = MAX_POWER;
                ballManager.fireWhiteBall(mousePosition, pressed);
            }
        });
    }
    public HBox getHeader(){
        HBox head = new HBox();
        head.getStyleClass().add("header-container");

        Label name1 = new Label("Player 1");
        name1.getStyleClass().add("player-name");

        Label name2 = new Label("Player 2");
        name2.getStyleClass().add("player-name");


        VBox player1 = new VBox();
        player1.getStyleClass().add("player-container");
        player1.getChildren().add(name1);
         HBox player1Balls = PlayerManager.players.get(0).playerBallsView;
        player1Balls.getStyleClass().add("balls-container");
        for(Ball b : playerManager.players.get(0).balls){
            player1Balls.getChildren().add(b.sphere);
        }
        player1.getChildren().add(player1Balls);

        VBox player2 = new VBox();
        player2.getChildren().add(name2);
        player2.getStyleClass().add("player-container");
        HBox player2Balls = PlayerManager.players.get(1).playerBallsView;
        player2Balls.getStyleClass().add("balls-container");
        for(Ball b : playerManager.players.get(1).balls){
            player2Balls.getChildren().add(b.sphere);
        }
        player2.getChildren().add(player2Balls);

        head.getChildren().addAll(player1,player2);
        head.setMinSize(HEAD_WIDTH,HEAD_HEIGHT);
        return head;
    }
    public Pane getGameContainer(){
        imgV = new ImageView("assets/images/game-3.png");

        imgV.setFitWidth(TABLE_WIDTH);
        imgV.setFitHeight(TABLE_HEIGHT);
        //imgV.setPreserveRatio(true);
        imgV.setSmooth(true);
        imgV.setCache(true);
        gameContainer.getChildren().add(imgV);
        gameContainer.setPrefSize(TABLE_WIDTH,TABLE_HEIGHT);
        ballManager.drawBalls();
        Ball white = ballManager.balls[15];
        stick.white = white;
        stick.drawStick();
        startTheGame();
        manageMouseclick();
        manageKeysPress();
        updateMousePosition();
        root.setCenter(gameContainer);

        root.setTop(getHeader());
        return root;
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
                        if(!isGameOver) {
                            stick.render(isMousePressed,mousePosition,MousePressedTime.get());
                            // update ball
                            ballManager.handleHoleCollisions();
                            ballManager.handleWallCollisions();
                            ballManager.handleBallsCollisions();
                            ballManager.handleWhiteBallFall();
                            ballManager.handleEdgesCollisions();
                            ballManager.handlePlayerTurnUI();
                            if(stick.translationValue == 0)
                                ballManager.updateBallsPositions();

                        }
                        // else show msg end
                    }
                });

        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();
        isGameOver = false;
    }
}

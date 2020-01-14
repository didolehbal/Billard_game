import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicLong;


public class Game {
    BallsManager ballManager;
    PlayerManager playerManager;
    ControlStick stick;



    static final double TABLE_WIDTH = 852 , TABLE_HEIGHT = 485;  //according to wiki  2.84 meters by 1.42 meters ratio : 2.84:1.42 => 852 x 426
    static final double HEAD_WIDTH=852,HEAD_HEIGHT=75;
    static final double WINDOW_WIDTH = Math.max(TABLE_WIDTH , HEAD_WIDTH), WINDOW_HEIGHT = TABLE_HEIGHT + HEAD_HEIGHT;
    static final long MAX_POWER = 2000;

    BorderPane root;
    Pane gameContainer;
    ImageView imgV;
    Button goback;

    static boolean isGameOver = false;
    static Player Winner = null;

    boolean isMousePressed = false;
    AtomicLong MousePressedTime = new AtomicLong();
    Circle holes[] = new Circle[6];
    PVector mousePosition  = new PVector(0,0);

    public Game(){
        root = new BorderPane();
        gameContainer = new Pane();
        ballManager = new BallsManager(gameContainer);
        stick = new ControlStick(gameContainer);
        playerManager = new PlayerManager();
    }



    public void manageEvents(){
        gameContainer.setOnMouseMoved(e -> {
            mousePosition = new PVector(e.getX(),e.getY());
            stick.RotateStick(e.getX(),e.getY());
        });
        gameContainer.setOnScroll(e-> {
                    isGameOver=true;
                    Winner=PlayerManager.getPlayerWithTurn();
        });
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
        HBox player1Balls = new HBox();
        player1Balls.getStyleClass().add("balls-container");
        for(Ball b : playerManager.players.get(0).balls){
            player1Balls.getChildren().add(b.sphere);
        }
        PlayerManager.players.get(0).playerBallsView= player1Balls;
        player1.getChildren().add(player1Balls);

        VBox player2 = new VBox();
        player2.getChildren().add(name2);
        player2.getStyleClass().add("player-container");
        HBox player2Balls = new HBox();
        player2Balls.getStyleClass().add("balls-container");
        for(Ball b : playerManager.players.get(1).balls){
            player2Balls.getChildren().add(b.sphere);
        }
        PlayerManager.players.get(1).playerBallsView = player2Balls;
        player2.getChildren().add(player2Balls);

        head.getChildren().addAll(player1,player2);
        head.setMinSize(HEAD_WIDTH,HEAD_HEIGHT);
        return head;
    }
    public Pane getWinContainer(){
        if(Winner != null ){
            VBox vb = new VBox();
            vb.getStyleClass().add("box-win");

            Label lbl = new Label(Winner.name + " Has Won !");
            lbl.getStyleClass().add("lbl-win");

             goback = new Button("Go to Menu");
            goback.getStyleClass().add("btn");
            goback.setOnMouseClicked(e->{
                System.out.println("click");
                Main.activate("Menu");
            });
            vb.getChildren().addAll(lbl,goback);
            return vb;
        }
        return null;
    }
    public Pane getGameContainer(){
        imgV = new ImageView("assets/images/game-3.png");

        isGameOver=false;
        Winner=null;

        imgV.setFitWidth(TABLE_WIDTH);
        imgV.setFitHeight(TABLE_HEIGHT);
        imgV.setSmooth(true);
        imgV.setCache(true);
        gameContainer.getChildren().add(imgV);
        gameContainer.setPrefSize(TABLE_WIDTH,TABLE_HEIGHT);
        ballManager.drawBalls();
        Ball white = ballManager.balls[15];
        stick.white = white;
        stick.drawStick();
        startTheGame();
        manageEvents();
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
                        else {
                            gameLoop.stop();
                            stick.hide();
                            root.setTop(null);
                            root.setCenter(getWinContainer());
                        }
                        // else show msg end
                    }
                });

        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();
    }
}

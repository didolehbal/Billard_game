package Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicLong;


public class Game {
    BallsManager ballManager;
    PlayerManager playerManager;
    ControlStick stick;



    public static final double TABLE_WIDTH = 852 , TABLE_HEIGHT = 485;  //according to wiki  2.84 meters by 1.42 meters ratio : 2.84:1.42 => 852 x 426
    public static final double HEAD_WIDTH=852,HEAD_HEIGHT=75;
    public static final double WINDOW_WIDTH = Math.max(TABLE_WIDTH , HEAD_WIDTH), WINDOW_HEIGHT = TABLE_HEIGHT + HEAD_HEIGHT;
    public static final long MAX_POWER = 2000;

    protected BorderPane root;
    public StackPane stackpane;
    protected Pane gameContainer;
    ImageView imgV;
    Pane pauseView;
    Button goback;
    static boolean isGameOver = false;
    static Player Winner = null;
    boolean isMousePressed = false;
    AtomicLong MousePressedTime = new AtomicLong();
    PVector mousePosition  = new PVector(0,0);
    Timeline gameLoop;
    boolean isGamePaused = false;
    Circle holes[] = new Circle[6];

    protected Label names[] = new Label[2];
    public Game(){
        root = new BorderPane();
        stackpane = new StackPane();
        gameContainer = new Pane();
        ballManager = new BallsManager(gameContainer,holes);
        stick = new ControlStick(gameContainer);
        playerManager = new PlayerManager();
        pauseView = getPauseContainer();
    }

    public void initHoles(){
        holes[0] = new Circle(44,45,20);
        holes[1] = new Circle(428,33,20);
        holes[2] = new Circle(813,45,20);
        holes[3] = new Circle(44,440,20);
        holes[4] = new Circle(428,450,20);
        holes[5] = new Circle(813,440,20);
        for(Circle hole : holes){
            if(hole == null) continue;
           // hole.setFill(Color.RED);// to preview the hole
            hole.setOpacity(0);
            gameContainer.getChildren().add(hole);
        }
    }

    private void endGame(){
        Ball black = null;
        for(Ball b : ballManager.balls){
            if(b.ballid > 7)
                b.addBallToPlayerScore();
            if(b.ballid == 7)
                black = b;
        }
        if(black != null)
            black.addBallToPlayerScore();
    }
    protected  void updateMousePosition(double x, double y){
        mousePosition = new PVector(x,y);
        stick.RotateStick(x,y);
    }
    protected  void prepareShooting(){
            MousePressedTime.set(System.currentTimeMillis());
            isMousePressed = true;
    }
    protected long startShooting(){
        long pressed = System.currentTimeMillis() - MousePressedTime.get();
        shootBall(pressed);
        return pressed;
    }

    public void handleWhiteBallFall(){
        Ball white = ballManager.balls[15];
        if(white.isHidden()){

            gameContainer.getChildren().add(white.sphere);
            gameContainer.setStyle("-fx-cursor: move");

            white.setX(BallsManager.INITIAL_WHITE_POS.x);
            white.setY(BallsManager.INITIAL_WHITE_POS.y);
            white.power = 0;
            white.velocity = new PVector(0,0);
            white.setHidden( false);
            white.isFalling = false;
            white.isHandled = true;
        }
        if(white.isHandled){
            if(mousePosition.x > 61 && mousePosition.x < TABLE_WIDTH -61 && mousePosition.y >61 && mousePosition.y < TABLE_HEIGHT-61){ // check if collision with ball walls
                boolean noObstacles=true;
                for(Ball b2 : ballManager.balls){ //check if collision with a ball
                    if(b2 == null || white.ballid == b2.ballid || b2.isHidden()) continue;

                    double xDist = b2.getX() - mousePosition.x;
                    double yDist = b2.getY() - mousePosition.y;
                    double distance = Math.sqrt(xDist * xDist + yDist * yDist) ;
                    if (distance <= 20 ) {
                        noObstacles=false;
                    }
                }
                if(noObstacles){
                    white.setX(mousePosition.x);
                    white.setY(mousePosition.y);
                }
            }
        }
    }
    protected void shootBall(long power){
        isMousePressed = false;
        if(power > MAX_POWER)
            power = MAX_POWER;
        ballManager.fireWhiteBall(mousePosition, power);
    }
    protected void cancelShooting(){
        isMousePressed = false;
        stick.translationValue = 0;
    }
    protected void quit(){
        isGameOver= true;
        Main.activate(Main.MENU);

    }
    public void setWhitePosition(){
        Ball white = ballManager.balls[15];
        if(white.isHandled){
            white.isHandled = false;
            gameContainer.setStyle("-fx-cursor: hand");
        }
    }
    public void manageEvents(){
        gameContainer.setFocusTraversable(true);
        gameContainer.setOnKeyPressed(e-> {
                if (e.getCode().equals(KeyCode.P)) {
                    toggleGamePauseStop();
                }
                else if(e.getCode().equals(KeyCode.ESCAPE))
                {
                   quit();
                }
        });
        gameContainer.setOnMouseMoved(e -> {
            updateMousePosition( e.getX(),e.getY());
        });
        gameContainer.setOnMouseClicked(e->{
            System.out.println(mousePosition);
            if(e.getButton() == MouseButton.SECONDARY){ // cancel shooting
                cancelShooting();
            }
            if(e.getButton() == MouseButton.PRIMARY){ // put white ball on board
                setWhitePosition();
            }
        });
        gameContainer.setOnMousePressed(e->{
            if(e.getButton() == MouseButton.PRIMARY){ // prepare shooting
                Ball white = ballManager.balls[15];
                if(!white.isHandled)
                    prepareShooting();
            }
        });
        gameContainer.setOnMouseReleased(e->{
            if(isMousePressed && e.getButton() == MouseButton.PRIMARY){ // shoot
                Ball white = ballManager.balls[15];
                if(!white.isHandled)
                    startShooting();
            }
        });


    }
    public HBox getHeader(){
        HBox head = new HBox();
        head.getStyleClass().add("header-container");

        names[0] = new Label("first Player");
        names[0].getStyleClass().add("player-name");

        names[1] = new Label("seconde Player");
        names[1].getStyleClass().add("player-name");


        VBox player1 = new VBox();
        player1.getStyleClass().add("player-container");
        player1.getChildren().add(names[0]);
        HBox player1Balls = new HBox();
        player1Balls.getStyleClass().add("balls-container");
        for(Ball b : playerManager.players.get(0).balls){
            player1Balls.getChildren().add(b.sphere);
        }
        PlayerManager.players.get(0).playerBallsView= player1Balls;
        player1.getChildren().add(player1Balls);

        VBox player2 = new VBox();
        player2.getChildren().add(names[1]);
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
                Main.activate(Main.MENU);
            });
            vb.getChildren().addAll(lbl,goback);
            return vb;
        }
        return null;
    }
    public Pane getPauseContainer(){
            VBox vb = new VBox();
            vb.getStyleClass().add("box-pause");

            Label lbl = new Label("Game Paused !");
            lbl.getStyleClass().add("lbl-win");

            vb.getChildren().addAll(lbl);
            return vb;
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
        gameContainer.setStyle("-fx-cursor: hand");
        ballManager.drawBalls();
        Ball white = ballManager.balls[15];
        stick.white = white;
        stick.drawStick();
        initHoles();
        startTheGame();
        manageEvents();
        stackpane.getChildren().add(gameContainer);
        root.setCenter(stackpane);

        root.setTop(getHeader());
        return root;
    }

    public void toggleGamePauseStop(){
        if(!isGamePaused){
            gameLoop.pause();
            gameContainer.setStyle("-fx-cursor: wait");
            isGamePaused=true;
            stackpane.getChildren().add(pauseView);
        }
        else{
            gameLoop.play();
            gameContainer.setStyle("-fx-cursor: hand");
            isGamePaused=false;
            stackpane.getChildren().remove(pauseView);
        }
    }
    private void startTheGame(){
        gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.030),                // 60 FPS
                new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                         if(!isGameOver) {
                            stick.render(isMousePressed,mousePosition,MousePressedTime.get());
                            ballManager.handleHoleCollisions();
                            ballManager.handleWallCollisions();
                            ballManager.handleBallsCollisions();
                            handleWhiteBallFall();
                            ballManager.handleEdgesCollisions();
                            playerManager.handlePlayerTurnUI();
                            if(stick.translationValue == 0)
                                ballManager.updateBallsPositions();
                        }
                        else {
                            gameLoop.stop();
                            stick.hide();
                            Pane  p = getWinContainer();
                            if(p != null)
                                stackpane.getChildren().add(p);
                        }
                    }
                });

        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();
    }

}

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;




public class Game {
    /*
    * corners are 50px width
    * */
    Pane container;
    ImageView imgV;
    Ball white;
    Dimensions dimensions = new Dimensions(852,426); //according to wiki  2.84 meters by 1.42 meters ratio : 2.84:1.42 => 852 x 426
    Position mousePosition  = new Position(0,0);
    public Game(){
        container = new Pane();
        white = new Ball(100,150);
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
        translateTransition.setNode(white.getImage());
        //Setting the value of the transition along the x axis.
        double to = getF(mousePosition,white.pos,50);



        //Setting the cycle count for the transition
        translateTransition.setCycleCount(1);

        //Setting auto reverse value to false
        translateTransition.setAutoReverse(false);

        //Playing the animation
        translateTransition.play();
    }
    public double getF(Position one, Position two, double distance ){
        //distance dayrha ha 3la axis de x
        System.out.println(one + "and " + two);
        System.out.println("x:"+(two.x+distance)+" y:"+ (one.y + ((two.y - one.y)/(two.x - one.x))*(two.x+distance - one.x)));
        return  one.y + ((two.y - one.y)/(two.x - one.x))*(distance - one.x);
    }
    public void updateMousePosition(MouseEvent e) {
        mousePosition = new Position(e.getX(),e.getY());
    }
    public Pane getContainer(){
        imgV = new ImageView(new Image("assets/images/game-3.png"));
        container.setOnMouseClicked(e -> moveMe(e));
        container.setOnMouseMoved(e -> updateMousePosition(e));
        imgV.toBack();
        imgV.setFitWidth(dimensions.width);
        imgV.setFitHeight(dimensions.height);
        container.getChildren().addAll(imgV, white.getImage());
        return container;
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
class Position {
    public double x;
    public double y;
    Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
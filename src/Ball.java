import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    Position pos;
    ImageView img;
    Ball(double x, double y){
        pos  = new Position(x,y);
        draw();
        img.setX(x);
        img.setY(y);

    }
    private void draw(){
        img = new ImageView(new Image("assets/images/ball_white.png"));
        img.setFitWidth(20);
        img.setPreserveRatio(true);
    }
    public ImageView getImage(){
        return img;
    }
}

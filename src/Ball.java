import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import static javafx.scene.paint.Color.WHITE;

public class Ball {
    public  Sphere sphere =new Sphere(10,40);
    PVector velocity = new PVector(0,0);
    double power = 0;
    private boolean isHidden = false;
    int ballid = -1;
    boolean isFalling = false;
    Ball( double x, double y, String imageName, Color color, BallsManager ballsManager) {
        sphere.setLayoutX(x);
        sphere.setLayoutY(y);
        sphere.setRadius(10);

        Image myimage = new Image(imageName);
        PhongMaterial material = new PhongMaterial();
        material.setSpecularColor(WHITE);
        if (color == Color.BLACK) {
            material.setDiffuseColor(color);
        }

        material.setDiffuseMap(myimage);
        material.setSpecularPower(30);
        sphere.setMaterial(material);

        DropShadow d=new DropShadow();
        d.setSpread(40);
        d.setOffsetX(10);
        d.setOffsetY(10);
        sphere.setEffect(d);
    }
    Ball(){
        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        //greyMaterial.setSpecularColor(Color.GREY);
        sphere.setMaterial(greyMaterial);

    }
    public void hideWithFadeEffect(Pane container){
       if(isHidden) return;
        isFalling = true;
        if(power > 10) power = 10 / this.velocity.magnitude();
        sphere.setOpacity(sphere.getOpacity()*0.90);
        PhongMaterial material  = (PhongMaterial)sphere.getMaterial();
        material.setDiffuseColor(new Color(sphere.getOpacity(),sphere.getOpacity(),sphere.getOpacity(),sphere.getOpacity()));
       if(sphere.getOpacity() <= 0.1){
            container.getChildren().remove(sphere);
            isHidden=true;
            material.setDiffuseColor(new Color(1,1,1,1));
            sphere.setOpacity(1);
            addBallToPlayerScore();
       }
    }

    void addBallToPlayerScore(){
        if(ballid == 15) {
            PlayerManager.switchturn();
            return;
        }
        if (ballid >= 0 && ballid <= 6) {
            Player firstPlayer  = PlayerManager.players.get(0);
            Player.pushBall(firstPlayer,this);
            if(PlayerManager.getPlayerWithTurn() != firstPlayer)
                PlayerManager.switchturn();
        }
        else if (ballid >= 8 && ballid <= 14 ){
            Player secondPlayer = PlayerManager.players.get(1);
            Player.pushBall(secondPlayer,this);
            if(PlayerManager.getPlayerWithTurn() != secondPlayer)
                PlayerManager.switchturn();
        }
    }
    public boolean isHidden(){
        return isHidden;
    }
    public void show(Pane container){
        setX(BallsManager.INITIAL_WHITE_POS.x);
        setY(BallsManager.INITIAL_WHITE_POS.y);
        power = 0;
        velocity = new PVector(0,0);
        container.getChildren().add(sphere);
        isHidden = false;
        isFalling = false;
    }
    public void setX(double x) {
        sphere.setLayoutX(x);
    }
    public void setY(double y) {
        sphere.setLayoutY(y);
    }
    public boolean move(){
        double mag = velocity.magnitude();
            if(power > 1 && power > mag ){
                if(mag < 1 ) mag = velocity.normalize();
                double acc = 0.01 * power;
                if(acc < 1 ) acc = 1;
                power -= mag *acc;
                setX(getX() + velocity.x*acc );
                setY(getY() + velocity.y*acc );
                spin();
                return true;
            }
            else{
                power = 0;
                velocity.x=0;
                velocity.y=0;
                return false;
            }

    }
    public double getX(){
        return  sphere.getLayoutX();
    }
    public double getY(){
        return sphere.getLayoutY();
    }

    @Override
    public String toString() {
        return "Ball{" +
                "id= "+ballid +
                "velocity=" + velocity +
                "Position= ("+ getX()+","+getY() +")"+
                "power= "+power+
                '}';
    }
    private void spin() {
            Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
            Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
            rx.setAngle(Math.toDegrees(velocity.x / 10));
            ry.setAngle(-Math.toDegrees(velocity.y / 10));
            sphere.getTransforms().addAll(rx, ry);
    }
}

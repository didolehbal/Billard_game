import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import static javafx.scene.paint.Color.WHITE;

public class Ball {
    public  Sphere sphere =new Sphere(10,40);
    PVector velocity = new PVector(0,0);
    double power = 0;
    int ballid;

    Ball( double x, double y, String imageName, Color color) {
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

    public void setX(double x) {
        sphere.setLayoutX(x);
    }
    public void setY(double y) {
        sphere.setLayoutY(y);
    }
    public void move(){
        double mag = velocity.magnitude();
            if(power > 1 && power > mag ){
                if(mag < 1 ) mag = velocity.normalize();
                double acc = 0.01 * power;
                if(acc < 1 ) acc = 1;
                power -= mag *acc;
                setX(getX() + velocity.x*acc );
                setY(getY() + velocity.y*acc );
                spin();
            }
            else{
                power = 0;
                velocity.x=0;
                velocity.y=0;
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

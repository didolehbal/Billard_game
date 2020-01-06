import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import static javafx.scene.paint.Color.WHITE;

/**
 * Created by Sadiqur Rahman on 5/19/2016.
 */
public class Ball {
    public  Sphere sphere =new Sphere(10,40);

    private double angle;
    private double a=10;
    PVector velocity = new PVector(0,0);
    double power = 0;
    double mass =1;
    int ballid;

    Ball(){

    }

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

       /* if(power < 2){
            velocity.x = 0;
            velocity.y = 0;
            power = 0;
        }
        else {
            double mag = velocity.magnitude();
            System.out.print(mag+"  ");
            if(mag <0.7 || mag > 1.3){
                System.out.println("BEFOR: " + this );
                mag = velocity.normalize();
                power += mag;
                System.out.println( this);

            }
            /*double mag =  velocity.normalize();
            System.out.println(this + " mag="+mag);
            power += mag;*/
            /*double acc = power *  0.01;
            if(acc < 1) acc =1;*/
            /*double acc= power * 0.01;
            if(acc < 1) acc = 1;
            if(velocity.magnitude() < 1 )
                velocity.normalize();
            power -= mag ;
            setX(getX() + velocity.x );
            setY(getY() + velocity.y );
            spin();
        }
        */
        double mag = velocity.magnitude();
            if(power > 1 && power > mag ){
                power -= mag ;
                setX(getX() + velocity.x );
                setY(getY() + velocity.y );
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

    public void setAngle(double angle) {
        this.angle = angle;
    }





    public void setA(double a) {
        this.a = a;
    }

    public double getA() {
        return a;
    }


    public void setBallid(int ballid) {
        this.ballid = ballid;
    }

    public int getBallid() {
        return ballid;
    }
    private void spin() {
            Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
            Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
            rx.setAngle(Math.toDegrees(velocity.x / 10));
            ry.setAngle(-Math.toDegrees(velocity.y / 10));
            sphere.getTransforms().addAll(rx, ry);
    }
}

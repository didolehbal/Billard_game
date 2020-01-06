import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;


/**
 * Created by Sadiqur Rahman on 5/22/2016.
 */

public class ConstrolStick {
    Polygon stick;

    double lineEnlarge=250;
    double angle;
    double a=0;
    double b=180;
    double x=15;
    Pane root;
    public double Initial_v;
    double value=0;
    boolean flagAngle=true;
    //CreateScrollbar scrollbar;
    Circle sc;
    public Rectangle line;
    boolean isHidden = false;
    Ball white;

   public double lineangle;

   public ConstrolStick(){
    }

   public ConstrolStick(Pane root)
    {
        this.root=root;

        //scrollbar=new CreateScrollbar(root);
        //sc=scrollbar.getSc();

       // line=new Polygon(0,0,15,0,15,1.5,lineEnlarge,1.5,lineEnlarge,-1.5,15,-1.5,15,0);
        line=new Rectangle();
        line.setWidth(1.8);
        line.setHeight(lineEnlarge);
        line.setFill(Color.AQUA);

        DropShadow dropShadow=new DropShadow();
       // d.setColor(Color.ALICEBLUE);
        line.setEffect(dropShadow);
        line.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS));


    }


    void update(){
        if(white.velocity.magnitude() > 0){ // ball is moving
            hide();
        }
        else {
            show();
            InitializeStick(white.getX(),white.getY());
            MoveStick(white.getX(),white.getY());
        }
    }
    void drawStick(){
        if(stick != null)
            root.getChildren().remove(stick);

        stick=new Polygon(0,0,0+x,0,0+x,-2.2,25+x,-3,26+x,-3,200+x,-4,202+x,-4.1,260+x,-5,260+x,5,202+x,4.1,
                202+x, -4.1,200+x,-4,200+x,4,26+x,3,26+x,-3,25+x,-3,25+x,3,0+x,2.2,0+x,0);
        Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
        LinearGradient lg = new LinearGradient(.5,0,.5,.7,true, CycleMethod.REFLECT,stops);
        stick.setFill(lg);
        DropShadow d=new DropShadow();
        stick.setEffect(d);
        root.getChildren().addAll(stick);
    }

    void InitializeStick(double middleX, double middleY ){
        stick.setLayoutX(middleX);
        stick.setLayoutY(middleY);
        line.setLayoutX(middleX);
        line.setLayoutY(middleY);
    }

    public void RotateStick(double ex, double ey){

        if(flagAngle==true) {


                angle = Math.toDegrees(Math.atan2((ey- white.getY()), (ex - white.getX())));
                if (angle < 0) {
                    angle += 360;
                }
                lineangle=angle+180;
                lineangle=lineangle%360;
                stick.getTransforms().addAll(new Rotate(angle - a, Rotate.Z_AXIS));
                line.getTransforms().addAll(new Rotate(lineangle-b,Rotate.Z_AXIS));
                a = angle;
                b=lineangle;
        }
    }

    public void  hide(){
       if(!isHidden){
            root.getChildren().remove(stick);
            isHidden = true;
       }
    }
    public void show(){
        if(isHidden){
            root.getChildren().add(stick);
            isHidden= false;
        }
    }
    public  void MoveStick(double pivotx,double pivoty) {
            stick.setLayoutX(pivotx +value * Math.cos(Math.toRadians(angle)));
            stick.setLayoutY(pivoty + value * Math.sin(Math.toRadians(angle)));
    }

    public void setInitial_v(double initial_v) {
        Initial_v = initial_v;
    }

    public double getValue() {
        return value;
    }

    public Polygon getStick() {
        return stick;
    }

    public double getAngle() {
        return angle;
    }


    public void setInitial_v(){
        sc.setOnMouseDragged(event -> {
            if((event.getSceneY()-250)<200 && (event.getSceneY()-250)>0) {
                sc.setLayoutY(event.getSceneY());
                value = event.getSceneY() - 250;
                value=value*.5;
                //System.out.println(value);
            }
        });
        sc.setOnMouseReleased(event -> {
            Initial_v=value/7.5;
             value=0;
            sc.setLayoutY(250);
        });
    }


    public void setValue(double value) {
        this.value = value;
    }

    public void setRotateStick(Double ang){
        lineangle=(angle+180)%360;
        stick.getTransforms().addAll(new Rotate(angle - a, Rotate.Z_AXIS));
        line.getTransforms().addAll(new Rotate(lineangle- b, Rotate.Z_AXIS));
        a = angle;
        b=lineangle;
    }

    public void setMoveStick(double pivotx,double pivoty){
        stick.setLayoutX(pivotx +value * Math.cos(Math.toRadians(angle)));
        stick.setLayoutY(pivoty + value * Math.sin(Math.toRadians(angle)));
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }


    public double getInitial_v() {
        return Initial_v;
    }
}


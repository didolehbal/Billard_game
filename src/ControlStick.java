import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class ControlStick {
    Polygon stick;
    double lineEnlarge=250;
    double angle;
    double a=0;
    double b=180;
    double x=15;
    Pane root;
    double value=0;
    public Rectangle line;
    boolean isHidden = false;
    Ball white;
   public double lineangle;

   public ControlStick(Pane root)
    {
        this.root=root;
        line=new Rectangle();
        line.setWidth(1.8);
        line.setHeight(lineEnlarge);
        line.setFill(Color.AQUA);

        DropShadow dropShadow=new DropShadow();
        line.setEffect(dropShadow);
        line.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS));
    }


    void update(boolean isMousePressed, PVector mousePos, long timePressed){
        timePressed =  System.currentTimeMillis() - timePressed;
        if(timePressed > Game.MAX_POWER)
            timePressed = Game.MAX_POWER;
       if(isMousePressed){
           if(value < timePressed/20){
               value +=2;
           }
       }else{
           if(value > 0)
            value -=10;
           if(value < 0)
               value = 0 ;
       }
        PVector p = PVector.subs(new PVector(white.getX(),white.getY()),mousePos);
        MoveStick(p.x,p.y);

        if(white.velocity.magnitude() > 0 && value == 0){ // ball is moving
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
        root.getChildren().addAll(stick,line);
    }

    void InitializeStick(double middleX, double middleY ){
        stick.setLayoutX(middleX);
        stick.setLayoutY(middleY);
        line.setLayoutX(middleX);
        line.setLayoutY(middleY);
    }

    public void RotateStick(double ex, double ey){
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

    public void  hide(){
       if(!isHidden){
            root.getChildren().remove(stick);
            root.getChildren().remove(line);
            isHidden = true;
       }
    }
    public void show(){
        if(isHidden){
            root.getChildren().addAll(stick,line);
            isHidden= false;
        }
    }
    public  void MoveStick(double pivotx,double pivoty) {
            stick.setLayoutX(pivotx +value * Math.cos(Math.toRadians(angle)));
            stick.setLayoutY(pivoty + value * Math.sin(Math.toRadians(angle)));
    }

}


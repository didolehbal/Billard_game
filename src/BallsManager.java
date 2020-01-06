import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

public class BallsManager {
    Ball [] balls = new Ball[16];
    PVector pos;
    Pane container;
    PVector acceleration;
    BallsManager(Pane container){
        this.container = container;
        Ball white = new Ball(150,Game.HEIGHT/2 , "assets/images/cueball.png", Color.ANTIQUEWHITE);
        Ball test = new Ball(350,Game.HEIGHT/2 , "assets/images/cueball.png", Color.ANTIQUEWHITE);
        String[] imageName ={
                "assets/images/one.png","assets/images/two.png","assets/images/three.png","assets/images/four.png",
                "assets/images/five.png","assets/images/six.png","assets/images/seven.png","assets/images/eight.png",
                "assets/images/nine.png","assets/images/ten.png","assets/images/eleven.png","assets/images/twelve.png",
                "assets/images/thirteen.png","assets/images/fourteen.png","assets/images/fifteen.png","assets/images/cueball.png"
        };
        int c = 0;
        double r = 21;
        double variable;
                   /*1st 8 solidball including 8ball,last 8 stripball including cue ball   */

        for (int i = 1; i <= 5; i++) {
            int v = 0;
            for (int j = 1; j <= i; j++) {
                if (c == 0) {
                    variable = 2;
                } else variable = 0;

                if (c < 7) {
                    if (i % 2 == 0) {
                        balls[c] = new Ball( Game.WIDTH/2 + r * i, Game.HEIGHT/2  + variable + 10 + r * v, imageName[c], Color.BROWN);
                    } else {
                        balls[c] = new Ball( Game.WIDTH/2  + r * i, Game.HEIGHT/2  + variable + r * v, imageName[c], Color.BROWN);
                    }
                } else if (c == 7) {
                    if (i % 2 == 0) {
                        balls[c] = new Ball( Game.WIDTH/2  + r * i, Game.HEIGHT/2  + variable + 10 + r * v, imageName[c], Color.BLACK);
                    } else {
                        balls[c] = new Ball(Game.WIDTH/2  + r * i, Game.HEIGHT/2  + variable + r * v, imageName[c], Color.BLACK);
                    }
                } else if (c < 15) {
                    if (i % 2 == 0) {
                        balls[c] = new Ball( Game.WIDTH/2 + r * i, Game.HEIGHT/2  + variable + 10 + r * v, imageName[c], Color.DARKBLUE);
                    } else {
                        balls[c] = new Ball( Game.WIDTH/2 + r * i, Game.HEIGHT/2  + variable + r * v, imageName[c], Color.DARKBLUE);
                    }
                }

                if (v < 0) {
                    v = (v / (-1));
                } else {
                    v = (v + 1) / (-1);
                }
                c++;
            }
        }

        balls[15] = white;
        int i=0;
        for(Ball b : balls){
            b.ballid = ++i;
        }
        //balls[14] = test;
    }

    void MouseClick (PVector mousePos,long pressed){
        Ball white = balls[15];
        if(white.velocity.magnitude() > 0 ) // ball already moving
            return;
        white.power=pressed;
        white.velocity = PVector.subs(new PVector(white.getX(),white.getY()),mousePos);
        white.velocity.normalize(5);
    }
    public void drawBalls(){
        for(Ball b : balls){
            if(b != null){
                container.getChildren().add(b.sphere);
            }
        }
    }
    public void updateBallsPositions() {
        for(Ball b : balls){
            if(b != null)
                b.move();
        }

    }
    public void checkEdges() {
        for(Ball b : balls){
            if(b != null){
                    if(b.getX() > 800 || b.getX() < 50){
                        b.velocity.x = - b.velocity.x;
                    }
                    if(b.getY() > 360 || b.getY() < 50){
                        b.velocity.y = - b.velocity.y;
                    }
            }
        }
    }
    private PVector  rotate(PVector velocity, double angle) {
    return new PVector(velocity.x * Math.cos(angle) - velocity.y * Math.sin(angle),velocity.x * Math.sin(angle) + velocity.y * Math.cos(angle));
    }

    public void checkCollisions() {
        for(Ball b1 : balls){
                if(b1 != null)
                for(Ball b2 : balls){
                    if(b2 == null || b1.ballid == b2.ballid) continue;

                    double xDist = b2.getX() - b1.getX();
                    double yDist = b2.getY() - b1.getY();
                    double distance = Math.sqrt(xDist * xDist + yDist * yDist) ;

                    if (distance <= 20 ) {
                        double xVelocityDiff = b1.velocity.x - b2.velocity.x;
                        double yVelocityDiff = b1.velocity.y - b2.velocity.y;


                        // Prevent accidental overlap of particles
                        if (xVelocityDiff * xDist + yVelocityDiff * yDist >= 0) {

                            // Grab angle between the two colliding particles
                            double angle = -Math.atan2(b2.getY() - b1.getY(), b2.getX() - b1.getX());

                            // Store mass in var for better readability in collision equation
                            double m1 = 1;
                            double m2 = 1;

                            // Velocity before equation
                            PVector u1 = rotate(b1.velocity, angle);
                            PVector u2 = rotate(b2.velocity, angle);

                            // Velocity after 1d collision equation
                            PVector v1 = new PVector(u1.x * (m1 - m2) / (m1 + m2) + u2.x * 2 * m2 / (m1 + m2), u1.y);
                            PVector v2 = new PVector(u2.x * (m1 - m2) / (m1 + m2) + u1.x * 2 * m2 / (m1 + m2), u2.y);

                            // Final velocity after rotating axis back to original location
                            PVector vFinal1 = rotate(v1, -angle);
                            PVector vFinal2 = rotate(v2, -angle);
                            double power = (b1.power + b2.power) /2;
                            b1.power = power;
                            b2.power = power;
                            // Swap particle velocities for realistic bounce effect
                            b1.velocity.x = vFinal1.x;
                            b1.velocity.y = vFinal1.y;

                            b2.velocity.x = vFinal2.x;
                            b2.velocity.y = vFinal2.y;
                        }

                    }
                }
        }
    }

}

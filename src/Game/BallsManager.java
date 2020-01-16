package Game;

import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.File;

public class BallsManager {
    Ball [] balls = new Ball[16];
    Pane container;
    private  AudioClip hitSideTableSound;
    private  AudioClip clackingSound;
    private  AudioClip ballFallingSound;
    static boolean areballsMoving = false;
    static boolean hasScored = false;
    final static PVector INITIAL_WHITE_POS = new PVector(Game.TABLE_WIDTH/2 - 200, Game.TABLE_HEIGHT/2);
    Circle[] holes;
     //public final static PVector INITIAL_WHITE_POS = new PVector( 795, 65);
    BallsManager(Pane container, Circle[] holes){
        this.holes = holes;
        this.container = container;
        Ball white = new Ball(INITIAL_WHITE_POS.x,INITIAL_WHITE_POS.y , "assets/images/cueball.png", Color.ANTIQUEWHITE);
        String[] imageName ={
                "assets/images/one.png","assets/images/two.png","assets/images/three.png","assets/images/four.png",
                "assets/images/five.png","assets/images/six.png","assets/images/seven.png","assets/images/8ball.png",
                "assets/images/nine.png","assets/images/ten.png","assets/images/eleven.png","assets/images/twelve.png",
                "assets/images/thirteen.png","assets/images/fourteen.png","assets/images/fifteen.png","assets/images/cueball.png"
        };
        int c = 0;
        double r = 21;
        double variable;
        for (int i = 1; i <= 5; i++) {
            int v = 0;
            for (int j = 1; j <= i; j++) {
                if (c == 0) {
                    variable = 2;
                } else variable = 0;

                if (c < 7) {
                    if (i % 2 == 0) {
                        balls[c] = new Ball( Game.TABLE_WIDTH /2 + r * i, Game.TABLE_HEIGHT /2  + variable + 10 + r * v, imageName[c], Color.BROWN);
                    } else {
                        balls[c] = new Ball( Game.TABLE_WIDTH /2  + r * i, Game.TABLE_HEIGHT /2  + variable + r * v, imageName[c], Color.BROWN);
                    }
                } else if (c == 7) {
                    if (i % 2 == 0) {
                        balls[c] = new Ball( Game.TABLE_WIDTH /2  + r * i, Game.TABLE_HEIGHT /2  + variable + 10 + r * v, imageName[c], Color.WHITE);
                    } else {
                        balls[c] = new Ball(Game.TABLE_WIDTH /2  + r * i, Game.TABLE_HEIGHT /2  + variable + r * v, imageName[c], Color.WHITE);
                    }
                } else if (c < 15) {
                    if (i % 2 == 0) {
                        balls[c] = new Ball( Game.TABLE_WIDTH /2 + r * i, Game.TABLE_HEIGHT /2  + variable + 10 + r * v, imageName[c], Color.DARKBLUE);
                    } else {
                        balls[c] = new Ball( Game.TABLE_WIDTH /2 + r * i, Game.TABLE_HEIGHT /2  + variable + r * v, imageName[c], Color.DARKBLUE);
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
            b.ballid = i++;
        }
        initSounds();
    }

    void fireWhiteBall(PVector mousePos, long pressed){
        hasScored = false;
        Ball white = balls[15];
        if(white.power > 0 || white.isHandled ) // ball already moving
            return;
        white.power=pressed;
        white.velocity = PVector.subs(new PVector(white.getX(),white.getY()),mousePos);
        System.out.println(mousePos);
        System.out.println(white.velocity);
        white.velocity.normalize();
    }
    public void drawBalls(){
        for(Ball b : balls){
            if(b != null){
                container.getChildren().add(b.sphere);
            }
        }
    }

    public void updateBallsPositions() {
        boolean areballsMoving = false;
        for(Ball b : balls)
            if(b != null)
                areballsMoving = areballsMoving |  b.move(); // || wont let b.move() execute !!
        this.areballsMoving = areballsMoving;
    }
    public void handleHoleCollisions() {
        for(Ball b : balls){
            if(b.isHidden() || b.isHandled) continue;
            for(Circle hole : holes){
                if(hole == null) continue;
                double distance = Math.sqrt(Math.pow(b.getX() - hole.getCenterX(), 2) + Math.pow(b.getY() - hole.getCenterY(), 2));
                if(distance < 18){
                    if(!b.isFalling){  // to play the sound only when falling starts
                        playBallFallingSound();
                        b.velocity = PVector.subs(new PVector(hole.getCenterX(),hole.getCenterY()),new PVector(b.getX(),b.getY())); // directing ball toward center of the hole
                        b.velocity.normalize();
                    }
                    b.hideWithFadeEffect(container);
                    hasScored= true;
                }
            }
        }
    }
    public void handleWallCollisions() {

      for (Ball b : balls) {

            if(b.isHidden()) continue;


            if (b.getX() >= 75 && b.getX() < 405) {
                if ((b.getY() - 50 <= 11)) { // 11 = radius + 1px
                    playBallHitWallSound();
                    b.velocity.y = - b.velocity.y;
                    b.setY(b.getY() + 2);// in case its inside the wall
                }
            }

          if (b.getX() >= 455 && b.getX() <= 780) {
              if ((b.getY() - 50 <= 11)) { // 11 = radius + 1px
                  playBallHitWallSound();
                  b.velocity.y = - b.velocity.y;
                  b.setY(b.getY() + 2);// in case its inside the wall
              }
          }

          if (b.getX() >= 75 && b.getX() < 405) {
              if (433 - b.getY() <= 11) { // 11 = radius + 1px
                  playBallHitWallSound();
                  b.velocity.y = - b.velocity.y;
                  b.setY(b.getY() - 2);// in case its inside the wall
              }
          }

          if (b.getX() >= 455 && b.getX() <= 780) {
              if (433 - b.getY() <= 11){ // 11 = radius + 1px
                  playBallHitWallSound();
                  b.velocity.y = - b.velocity.y;
                  b.setY(b.getY() - 2);// in case its inside the wall
              }
          }
          if (b.getY() >= 70 && b.getY() <= 433) {
              if ((b.getX() - 50 <= 11)) { // 11 = radius + 1px
                  playBallHitWallSound();
                  b.velocity.x = - b.velocity.x;
                  b.setX(b.getX() + 2);// in case its inside the wall
              }
          }
          if (b.getY() >= 70 && b.getY() <= 433) {
              if (805 - b.getX() <= 11){ // 11 = radius + 1px
                  playBallHitWallSound();
                  b.velocity.x = - b.velocity.x;
                  b.setX(b.getX() - 2);// in case its inside the wall
              }
          }
        }
    }
    public void handleEdgesCollisions() {

        for (Ball b : balls) {
            if(b.isHidden()) continue;

            if (b.getX() >= 28 && b.getX() < 51) {
                if (b.getY() - 80 <= 11 ) {
                    System.out.println("edge 1");
                    double fi = Math.atan2( 55 - 80,  28 - 50) * 180 / Math.PI;
                    b.velocity = getInclinedHitVect(b.velocity,fi);
                }
            }

            if (b.getX() >= 52 && b.getX() < 75) {
                if (b.getY() - 50 <= 11 ) {
                    System.out.println("edge 2");
                    double fi = Math.atan2( 45 - 55,  52 - 75) * 180 / Math.PI;
                    b.velocity = getInclinedHitVect(b.velocity,fi);
                }
            }


            if (b.getX() >= 404 && b.getX() < 415) {
                if (b.getY() - 55 <= 11 ) {
                    System.out.println("edge 3");
                    double fi = Math.atan2( 20 - 55 , 415 - 405 ) * 180 / Math.PI;
                    System.out.println(fi);
                    b.velocity = getInclinedHitVect(b.velocity,fi);
                }
            }
            if (b.getX() > 440 && b.getX() < 455) {
                if (b.getY() - 55 <= 11 ) {
                    System.out.println("edge 4");
                    double fi = Math.atan2( 37 - 55,   448 - 455) * 180 / Math.PI;
                    b.velocity = getInclinedHitVect(b.velocity,fi);
                }
            }

            if (b.getX() > 780 && b.getX() < 797) {
                if (b.getY() - 45 <= 11 ) {
                    System.out.println("edge 5");
                    double fi = Math.atan2( 39 - 55 , 797 - 780 ) * 180 / Math.PI;
                    b.velocity = getInclinedHitVect(b.velocity,fi);
                }
            }
            if (b.getX() > 805 && b.getX() < 825) {
                if (b.getY() - 80 <= 11 ) {
                    System.out.println("edge 6");
                    double fi = Math.atan2( 63 - 80,   825 - 805) * 180 / Math.PI;
                    b.velocity = getInclinedHitVect(b.velocity,fi);
                }
            }

            /*
            ///1///


            //2//
            if (ball[i].sphere.getLayoutX() > 475 && ball[i].sphere.getLayoutY() > 135 && ball[i].sphere.getLayoutY() < 162) {
                if (isTouch(-15, -10, 8625, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    // handleCollisionsInWall(303.6900675-20,ball[i]);
                    handleCollisionsInWallMinus(33.7, ball[i]);

                }
            }

            //3//
            if (ball[i].sphere.getLayoutX() < 525 && ball[i].sphere.getLayoutY() > 135 && ball[i].sphere.getLayoutY() < 162) {
                if (isTouch(15, -10, -6375, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallPlus(33.7, ball[i]);
                }
            }

            //4//
            if (ball[i].sphere.getLayoutX() > 875 && ball[i].sphere.getLayoutX() < 902) {
                if (isTouch(-15, -20, 16125, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallMinus(90 - 36.86, ball[i]);
                }
            }

            //5//
            if (ball[i].sphere.getLayoutX() >= 98 && ball[i].sphere.getLayoutX() < 125) {
                if (isTouch(15, 20, -12875, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallMinus(90 - 36.86, ball[i]);
                }
            }

            //6//
            if (ball[i].sphere.getLayoutX() > 474 && ball[i].sphere.getLayoutY() > 540 && ball[i].sphere.getLayoutY() <= 565) {
                if (isTouch(15, -10, -1625, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallPlus(33.7, ball[i]);
                }
            }

            //7//
            if (ball[i].sphere.getLayoutX() < 525 && ball[i].sphere.getLayoutY() < 565 && ball[i].sphere.getLayoutY() >= 540) {
                if (isTouch(15, 10, -13375, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallMinus(33.7, ball[i]);
                }
            }
            //8//
            if (ball[i].sphere.getLayoutX() > 875 && ball[i].sphere.getLayoutX() < 902) {
                if (isTouch(15, -20, -2125, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallPlus(90 - 36.86, ball[i]);
                }
            }

            //9//
            if (ball[i].sphere.getLayoutY() > 149 && ball[i].sphere.getLayoutY() < 175) {
                if (isTouch(20, -15, 625, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallMinus1(90 - 36.86, ball[i]);
                }
            }

            //10//
            if (ball[i].sphere.getLayoutY() > 525 && ball[i].sphere.getLayoutY() < 551) {
                if (isTouch(20, 15, -9875, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallPlus1(90 - 36, ball[i]);
                }
            }

            //11//
            if (ball[i].sphere.getLayoutY() > 149 && ball[i].sphere.getLayoutY() < 175) {
                if (isTouch(20, 15, -20625, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallPlus1(90 - 36, ball[i]);
                }
            }

            //12//
            if (ball[i].sphere.getLayoutY() > 525 && ball[i].sphere.getLayoutY() < 551) {
                if (isTouch(20, -15, -10125, ball[i].sphere.getLayoutX(), ball[i].sphere.getLayoutY())) {
                    handleCollisionsInWallMinus1(90 - 36.86, ball[i]);
                }
            }*/
        }
    }
    public void handleBallsCollisions() {
        for(Ball b1 : balls){
            if(b1 != null || !b1.isHidden())
                for(Ball b2 : balls){
                    if(b2 == null || b1.ballid == b2.ballid || b2.isHidden()) continue;

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
                            if(power>0)
                                playClackingSound();
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


    private PVector getInclinedHitVect(PVector v, double fi){
        double nx = -Math.sin(fi);
        double ny = Math.cos(fi);
        double dot = v.x * nx + v.y * ny;
        double vnewx = v.x - 2 * dot * nx;
        double vnewy = v.y - 2 * dot * ny;
        return new PVector(vnewx, vnewy);
    }
    private PVector rotate(PVector velocity, double angle) {
        return new PVector(velocity.x * Math.cos(angle) - velocity.y * Math.sin(angle),velocity.x * Math.sin(angle) + velocity.y * Math.cos(angle));
    }

    private void initSounds(){
        String hitfile = "src/assets/sounds/HitSideTable.mp3";
        hitSideTableSound = new AudioClip(new File(hitfile).toURI().toString());

        String clackFile = "src/assets/sounds/BallsClacking.mp3";
        clackingSound = new AudioClip(new File(clackFile).toURI().toString());
        String fallFile = "src/assets/sounds/BallFallPocket.mp3";
        ballFallingSound = new AudioClip(new File(fallFile).toURI().toString());
    }
    private void playBallHitWallSound(){
        hitSideTableSound.play();
    }
    private void playClackingSound(){
            clackingSound.play();
    }
    private void playBallFallingSound(){
        ballFallingSound.play();
    }

}

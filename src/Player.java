import javafx.scene.layout.HBox;

import java.util.LinkedList;
import java.util.Queue;

class Player {
   String name;
   Queue<Ball> balls = new LinkedList<>();
   boolean hasTurn = false;
   boolean hasWon = false;
   HBox playerBallsView;

   Player(String name){
       this.name=name;
       for(int i=0; i<7 ; i++) balls.add(new Ball());
       playerBallsView = new HBox();
   }
   boolean hasPocketedAllHisBalls(){
       for(Ball b : balls)
           if(b.ballid == -1)
               return false;
       return true;
   }

   static void pushBall(Player p ,Ball b){
       if(b.ballid == 16) return; // white ball
       Ball bRemoved = p.balls.remove();
       p.balls.add(b);
       p.playerBallsView.getChildren().remove(bRemoved.sphere);
       p.playerBallsView.getChildren().add(b.sphere);
   }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
               // ", balls=" + balls +
                ", hasTurn=" + hasTurn +
                ", hasWon=" + hasWon +
                '}';
    }
}

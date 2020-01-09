import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PlayerManager{
    static ArrayList<Player> players = new ArrayList<Player>();
    PlayerManager(){
        Player p = new Player("Player 1");
        p.hasTurn = true;
        players.add(p);
        players.add(new Player("Player 2"));
    }
    static Player getPlayerWithTurn(){
        for(Player p : players){
            if(p.hasTurn)
                return p;
        }
        return null;
    }
    static void switchturn(){
        System.out.println("done");
        players.get(0).hasTurn = players.get(1).hasTurn;
        players.get(1).hasTurn = !players.get(0).hasTurn;
    }
}
 class Player {
    String name;
    Queue<Ball> balls = new LinkedList<>();
    boolean hasTurn = false;
    HBox playerBallsView;

    Player(String name){
        this.name=name;
        for(int i=0; i<7 ; i++) balls.add(new Ball());
        playerBallsView = new HBox();
    }
    static void pushBall(Player p ,Ball b){
        if(b.ballid == 16) return; // white ball
        Ball bRemoved = p.balls.remove();
        p.balls.add(b);
        p.playerBallsView.getChildren().remove(bRemoved.sphere);
        p.playerBallsView.getChildren().add(b.sphere);
    }
}

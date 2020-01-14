import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PlayerManager{
    static ArrayList<Player> players ;
    PlayerManager(){
        players = new ArrayList<Player>();
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
        players.get(0).hasTurn = players.get(1).hasTurn;
        players.get(1).hasTurn = !players.get(0).hasTurn;
    }
    public void handlePlayerTurnUI(){
        for(Player p : PlayerManager.players){
            if(p.hasTurn){
                p.playerBallsView.setStyle("-fx-border-width: 2px");
            }
            else{

                p.playerBallsView.setStyle("-fx-border-width: 0px");
            }
        }
    }

}

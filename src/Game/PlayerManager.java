package Game;

import java.util.ArrayList;

public class PlayerManager{
   public static ArrayList<Player> players ;
    PlayerManager(){
        players = new ArrayList<Player>();
        Player p = new Player("first Player");
        p.hasTurn = true;
        players.add(p);
        players.add(new Player("seconde Player"));
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

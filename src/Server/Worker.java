package Server;

import Shared.PlayerSocket;
import Shared.Payload;

import java.net.Socket;

public class Worker implements Runnable {
    PlayerSocket[] players = new PlayerSocket[2];
    boolean isGameOver = false;
    public Worker(Socket[] splayers){
        players[0] = new PlayerSocket(splayers[0]);
        players[0].id =0;

        players[1] = new PlayerSocket(splayers[1]);
        players[1].id = 1;
    }

    void startGame(){
        for(PlayerSocket p : players){
            try{
                Payload req = new Payload(Payload.START);
                req.body = p.id;
                p.output().writeObject(req);
                p.output().flush();
            }catch(Exception ex){

            }

        }
    }
    void handleRequest(Payload req, PlayerSocket owner){
        for(PlayerSocket p : players){
            if(p.id == owner.id) continue;
            try{
                p.output().writeObject(req);
                p.output().flush();
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }
    @Override
    public void run() {
        startGame();
        for(PlayerSocket p : players){
            AsyncPlayerListener a = new AsyncPlayerListener(p,this);
            a.start();
        }
    }
}

class AsyncPlayerListener extends Thread{
    PlayerSocket player;
    Worker worker;
    AsyncPlayerListener(PlayerSocket player, Worker worker){
        this.player= player;
        this.worker = worker;
    }
    public void run(){
        while (true){
                try{
                    Payload req = (Payload) player.input().readObject();
                    worker.handleRequest(req,player);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

        }

    }
}

package Client;

import Game.PVector;
import Shared.PlayerSocket;
import Shared.Payload;
import javafx.application.Platform;


public class AsyncListener implements Runnable {
    GameOnline game;
    PlayerSocket player;
    AsyncListener(GameOnline game, PlayerSocket player){
        this.player = player;
        this.game = game;
    }

    @Override
    public void run() {
        try{
            Payload req= (Payload) player.input().readObject();
            if(req.code.equals(Payload.START)) {
                onRecvGameStart(req);
            }
            while(true){
                req= (Payload) player.input().readObject();
                switch(req.code){
                    case Payload.PAUSE:
                        onRecvGamePause(); break;
                    case Payload.MOUSE_POS:
                        onRecvMousePos(req); break;
                    case Payload.PREPARE_SHOOTING:
                        onRecvMousePressed(); break;
                    case Payload.START_SHOOTING:
                        onRecvMouseReleased(req); break;
                    case Payload.CANCEL_SHOOTING:
                        onRecvCancelShooting(); break;
                    case Payload.SET_OPNNENT_NAME:
                        onRecvOponnentName(req);break;
                    case Payload.QUIT:
                        onRecvQuit();break;
                    case Payload.SET_NEW_WHITE_POSITION:
                        onRecvSetWhitePos();break;
                }

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    void onRecvSetWhitePos(){
        Platform.runLater(()->{
            game.workerSetWhitePos();
        });
    }
    void onRecvQuit(){
        Platform.runLater(()->{
            game.workerQuit();
        });
    }
    void onRecvOponnentName(Payload req){
        String opName = (String)req.body;
        Platform.runLater(()->{
            game.setOponnentname(opName);
        });
    }
    void onRecvMousePos(Payload req){
        PVector pos = (PVector) req.body;
        Platform.runLater(()->{
            game.workerUpdateMousePosition(pos.x,pos.y);
        });
    }
    void onRecvGameStart(Payload req){
        System.out.println("game Starting...");
        player.id = (Integer) req.body;
        player.hasTurn = player.id == 0;
        Platform.runLater(
                () -> {
                    game.startGame();
                }
        );
    }
void onRecvCancelShooting(){
        Platform.runLater(()->{
            game.workerCancelShooting();
        });
}
    void onRecvGamePause(){
        Platform.runLater(
                () -> {
                    game.workerToggleGamePause();
                }
        );
    }
    void onRecvMousePressed(){
        Platform.runLater(()->{
            game.workeronMousePressed();
        });
    }
    void onRecvMouseReleased(Payload req){
        final long power = (long)req.body;
        Platform.runLater(()->{
            game.workeronMouseReleased(power);
        });
    }
}

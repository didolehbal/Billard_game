package Client;

import Game.*;
import Shared.PlayerSocket;
import Shared.Payload;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.Socket;
import java.util.Optional;

public class GameOnline extends Game {
    final String IP = "localhost";
    final int PORT = 8666;
    PlayerSocket player;
    public Pane waitView;
    String PlayerName="";

    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }

    public GameOnline(){
        super();
        waitView = getWaitingContainer();

    }
    void connectToServer(){
        try{
            Socket client = new Socket(IP,PORT);
            System.out.println("Connected to server ! with name +"+ getPlayerName());
            player = new PlayerSocket(client);
            player.setName(getPlayerName());
            sendMyNameToOponnent();
            new Thread(new AsyncListener(this,player)).start();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public  void workerSetWhitePos(){
        super.setWhitePosition();
    }
    public void setWhitePosition(){
        if(!player.hasTurn){
            return;
        }
        try {
            Payload req = new Payload(Payload.SET_NEW_WHITE_POSITION);
            req.body = getPlayerName();
            player.output().writeObject(req);
            player.output().flush();
            super.setWhitePosition();
        }catch (Exception ex){

        }
    }
    public void sendMyNameToOponnent(){
        try {
            Payload req = new Payload(Payload.SET_OPNNENT_NAME);
            req.body = getPlayerName();
            player.output().writeObject(req);
            player.output().flush();
            System.out.println("my name sent");
        }catch (Exception ex){

        }
    }
    void workerQuit(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Ended");
        alert.setContentText("Your Oponnent have Quitted !");
        alert.show();
        super.quit();
    }
    @Override
    public void quit(){
        try{
            Payload req = new Payload(Payload.QUIT);
            player.output().writeObject(req);
            player.output().flush();
            System.out.println("Quitted !");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game Ended");
            alert.setContentText("You have Quitted !");
            alert.show();

            super.quit();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void toggleGamePauseStop() {
        try{
            Payload req = new Payload(Payload.PAUSE);
            player.output().writeObject(req);
            player.output().flush();
            System.out.println("send Pause");
            super.toggleGamePauseStop();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    protected  void workeronMousePressed(){
        super.prepareShooting();
    }
    protected  void workeronMouseReleased(long power){
        player.hasTurn=true;
        super.shootBall(power);
    }
    void setOponnentname(String opName){
        int id = player.id ==0 ? 1: 0;
        PlayerManager.players.get(id).setName(opName);
        names[id].setText(opName);
    }
    public void workerCancelShooting(){
        super.cancelShooting();
    }
    protected  void cancelShooting(){
        if(!player.hasTurn){
            return;
        }
        try{
            Payload req = new Payload(Payload.CANCEL_SHOOTING);
            player.output().writeObject(req);
            player.output().flush();
            super.cancelShooting();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    protected  void prepareShooting(){
        if(!player.hasTurn){
            return;
        }
        try{
            Payload req = new Payload(Payload.PREPARE_SHOOTING);
            player.output().writeObject(req);
            player.output().flush();
            super.prepareShooting();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    protected long startShooting(){
        if(!player.hasTurn){
            return 0;
        }
        try{
            long power = super.startShooting();
            Payload req = new Payload(Payload.START_SHOOTING);
            req.body = power;
            player.output().writeObject(req);
            player.output().flush();
            player.hasTurn=false;
            return power;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public void workerUpdateMousePosition(double x, double y){
        super.updateMousePosition(x,y);
    }
    protected  void updateMousePosition(double x, double y){
        if(!player.hasTurn){
            return;
        }
        try{
            Payload req = new Payload(Payload.MOUSE_POS);
            req.body = new PVector(x,y);
            player.output().writeObject(req);
            player.output().flush();
            super.updateMousePosition(x,y);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void workerToggleGamePause(){
        System.out.println("received Pause");
        super.toggleGamePauseStop();
    }
    void startGame(){
        super.names[player.id ==0 ? 1 : 0].setText("Other");
        super.names[player.id ].setText(getPlayerName()+" (You)");
        PlayerManager.players.get(player.id).setName(getPlayerName());
        super.stackpane.getChildren().remove(waitView);
        super.toggleGamePauseStop();
    }
    Pane getWaitingContainer(){
        VBox vb = new VBox();
        vb.getStyleClass().add("queue-box");
        Label lb = new Label("Waitng for another Player to join ...");
        lb.getStyleClass().add("lbl-win");
        vb.getChildren().add(lb);
        return vb;
    }
    public void nameInputDialog(){
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Online");
        dialog.setHeaderText("Enter your name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
           setPlayerName(name);
        });

    }
    public Pane getGameContainer(){
        nameInputDialog();
        if(getPlayerName().trim().equals("")){
            System.out.println("you need to provide a name !");
            return null;
        }
        Pane p = super.getGameContainer();
        super.toggleGamePauseStop();
        super.stackpane.getChildren().add(waitView);
        connectToServer();
        return p;
    }

}



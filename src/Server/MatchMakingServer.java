package Server;

import java.net.ServerSocket;
import java.net.Socket;

public class MatchMakingServer {
    private final int PORT = 8666;
    ServerSocket ss;
    MatchMakingServer(){
    }
    Socket listen() throws Exception{
        if(ss == null)
            ss = new ServerSocket(PORT);
       return ss.accept();
    }
    public static void main(String[] args) {
        MatchMakingServer server = new MatchMakingServer();
        try {
            while(true){
                System.out.println("New Matchmaking !");
                Socket splayers[] = new Socket[2];
                splayers[0] = server.listen();
                System.out.println("First Player Connected !");
                splayers[1] = server.listen();
                System.out.println("Seconde Player Connected !");

                System.out.println("Game Starting!...");
                new Thread( new Worker(splayers)).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

package Shared;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerSocket {
    public Socket getClient() {
        return client;
    }

    Socket client;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        public int id;
        private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasTurn = false;
       public PlayerSocket(Socket client){
            this.client = client;
        }
        public ObjectInputStream input()throws Exception{
            if(in == null){
                in = new ObjectInputStream(client.getInputStream());
            }
            return in;
        }
        public ObjectOutputStream output() throws Exception{
            if(out == null){
                out = new ObjectOutputStream(client.getOutputStream());
            }
            return out;
        }
}

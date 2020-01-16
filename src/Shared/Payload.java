package Shared;

import java.io.Serializable;

public class Payload implements Serializable {
    public static final String PAUSE = "PAUSE";
    public static final String START = "START";
    public static final String MOUSE_POS = "MOUSE_POS";
    public static final String PREPARE_SHOOTING = "PREPARE_SHOOTING";
    public static final String START_SHOOTING = "START_SHOOTING";
    public static final String CANCEL_SHOOTING = "CANCEL_SHOOTING";
    public static final String SET_OPNNENT_NAME = "SET_OPNNENT_NAME";
    public static final String SET_NEW_WHITE_POSITION = "SET_NEW_WHITE_POSITION";
    public static final String QUIT = "QUIT";
    public String code;
    public Object body;
    public Object body2;
    public Payload(String code){
        this.code = code;
    }

}

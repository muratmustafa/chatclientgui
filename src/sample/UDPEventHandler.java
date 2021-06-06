package sample;

import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.Hashtable;

public class UDPEventHandler implements UDPModel.UDPEvent, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String TAG = "UDPEventHandler";


    public UDPEventHandler(){

    }

    @Override
    public void onMessageReceived(String message) {
        System.out.println("Received Message: " + message.trim());

    }
}

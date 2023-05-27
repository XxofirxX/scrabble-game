package model.host;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Guest_Host {
    public final InputStream inFromGuestInputSteam;
    public final OutputStream outToGuestOutputStream;
    public final Scanner inFromGuest;
    public final PrintWriter outToGuest;

    public Guest_Host(InputStream inFromGuest, OutputStream outToGuest) {
        this.inFromGuest = new Scanner(new BufferedInputStream(inFromGuest));
        this.outToGuest = new PrintWriter(outToGuest);
        this.outToGuestOutputStream = outToGuest;
        this.inFromGuestInputSteam = inFromGuest;
    }
    public void sendToGuest(String message){
        outToGuest.println(message);
    }
    public String getFromGuest(){
        return inFromGuest.next();
    }
    public void close(){
        sendToGuest("Close Connections");
        inFromGuest.close();
        outToGuest.close();
    }
}

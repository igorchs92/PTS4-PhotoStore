/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.user;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Databasemanager;
import shared.Log;
import shared.SocketConnection;
import shared.user.UserCall;

/**
 *
 * @author Igor
 */
public class UserServerRunnable implements Observer, Runnable {

    private SocketConnection socket;
    private Databasemanager dbm;

    public UserServerRunnable(SocketConnection socket) {
        this.socket = socket;
        dbm= new Databasemanager();
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.info("Oberservable");
    }

    @Override
    public void run(){
        try {
            while (!socket.isClosed()) {
                UserCall call = (UserCall) socket.readObject();
                switch (call) {
                    case test:
                        testConnection();
                        break;
                    case register:
                        Log.info("register");
                        String uname = (String) socket.readObject();
                        String pword = (String) socket.readObject();
                        try {
                            dbm.registreren(uname, pword, "", "", "", "");
                        } catch (SQLException ex) {
                            Logger.getLogger(UserServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                        System.out.println("U: " + uname + " PW:" + pword);
                        break;
                    case login: {
                        Log.info("login");
                        
                        break;
                    }
                    case logout: {

                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Log.exception(ex);
        } catch (IOException ex) {
            Log.info("User client disconnected: {0}", socket.getInetAddress());
        }
    }

    public void testConnection() {
        try {
            boolean receive = (boolean) socket.readObject();
            Log.info("Message received: {0}", receive);
            boolean send = true;
            socket.writeObject(send);
            Log.info("Message sent: {0}", send);
        } catch (ClassNotFoundException ex) {
            Log.exception(ex);
        } catch (IOException ex) {
            Log.info("User client disconnected: {0}", socket.getInetAddress());
        }
    }
}

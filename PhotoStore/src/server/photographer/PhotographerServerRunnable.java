/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.photographer;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import shared.SocketConnection;
import shared.photographer.PhotographerCall;

/**
 *
 * @author Igor
 */
public class PhotographerServerRunnable extends SocketConnection implements Observer, Runnable {

    public PhotographerServerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void update(Observable o, Object arg) {
        LOG.log(Level.INFO, "Oberservable");
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                PhotographerCall call = (PhotographerCall) readObject();
                switch (call) {
                    case test: {
                        testConnection();
                        break;
                    }
                    case login: {

                        break;
                    }
                    case logout: {

                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.INFO, "User client disconnected: {0}", socket.getInetAddress());
        }
    }

    public void testConnection() {
        try {
            while (!socket.isClosed()) {
                boolean receive = (boolean) readObject();
                LOG.log(Level.INFO, "Message received: {0}", receive);
                boolean send = true;
                writeObject(send);
                LOG.log(Level.INFO, "Message sent: {0}", send);
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.INFO, "Photographer client disconnected: {0}", socket.getInetAddress());
        }
    }
}

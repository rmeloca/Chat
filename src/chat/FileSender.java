/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

/**
 *
 * @author romulo
 */
public class FileSender implements Runnable {

    private final PeerConnection peerConnection;
    private final String filename;

    public FileSender(PeerConnection peerConnection, String filename) {
        this.peerConnection = peerConnection;
        this.filename = filename;
    }

    @Override
    public void run() {
        peerConnection.sendFile(filename);
    }

}

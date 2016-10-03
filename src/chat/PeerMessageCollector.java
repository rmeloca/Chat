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
public class PeerMessageCollector implements Runnable {

    private final PeerConnection peerConnection;

    public PeerMessageCollector(PeerConnection peerConnection) {
        this.peerConnection = peerConnection;
    }

    @Override
    public void run() {
        while (true) {            
            Message retrieveMessage = this.peerConnection.retrieveMessage();
            System.out.println(retrieveMessage.toString());
        }
    }

}

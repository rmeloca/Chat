/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.multicast;

import chat.Buffer;
import chat.Client;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author romulo
 */
public class MulticastInternalTalker extends MulticastTalker {

    private final Buffer sharedBuffer;

    public MulticastInternalTalker(MulticastSocket multicastSocket, InetAddress endereco, Client cliente, Buffer sharedBuffer) {
        super(multicastSocket, endereco, cliente);
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                sendMessage(sharedBuffer.poll());
            }
        } catch (InterruptedException ex) {
        }
    }

}

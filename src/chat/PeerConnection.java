/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author romulo
 */
public class PeerConnection {

    private final int listenToPort;
    private final InetAddress talkToHost;
    private final int talkToPort;
    private final DatagramSocket listenerDatagramSocket;
    private final DatagramSocket talkerDatagramSocket;

    public PeerConnection(int listenToPort, InetAddress talkToHost, int talkToPort) {
        this.listenToPort = listenToPort;
        this.talkToHost = talkToHost;
        this.talkToPort = talkToPort;

        DatagramSocket listenerDatagramSocket = null;
        DatagramSocket talkerDatagramSocket = null;
        try {
            listenerDatagramSocket = new DatagramSocket(this.listenToPort);
            talkerDatagramSocket = new DatagramSocket();
            talkerDatagramSocket.connect(talkToHost, talkToPort);
        } catch (SocketException ex) {
            Logger.getLogger(PeerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.listenerDatagramSocket = listenerDatagramSocket;
        this.talkerDatagramSocket = talkerDatagramSocket;
    }

    public InetAddress getHostAddress() {
        return talkToHost;
    }

    public synchronized void disconnect() {
        this.listenerDatagramSocket.close();
        this.talkerDatagramSocket.close();
    }

    public synchronized final void sendMessage(Message message) {
        byte[] bytesMessage = message.toString().getBytes();
        DatagramPacket messageOut;
        try {
            messageOut = new DatagramPacket(bytesMessage, bytesMessage.length, this.talkToHost, this.talkToPort);
            this.talkerDatagramSocket.send(messageOut);
        } catch (SocketException ex) {
            System.err.println("Erro na comunicacao");
        } catch (IOException ex) {
            System.err.println("Erro ao enviar mensagem" + ex.getMessage());
        }
    }

    public synchronized final Message retrieveMessage() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            this.listenerDatagramSocket.receive(messageIn);
            String messageStr = new String(messageIn.getData());
            return new Message(messageStr);
        } catch (IOException ex) {
            Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

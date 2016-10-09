/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
    private final Client client;

    public PeerConnection(int listenToPort, InetAddress talkToHost, int talkToPort, Client client) {
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
        this.client = client;
    }

    public int getListenToPort() {
        return listenToPort;
    }

    public InetAddress getHostAddress() {
        return talkToHost;
    }

    public Client getClient() {
        return client;
    }

    public void disconnect() {
        this.listenerDatagramSocket.close();
        this.talkerDatagramSocket.close();
    }

    public final void sendMessage(Message message) {
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

    private String ls(String folder) {
        InputStream inputStream = getClass().getResourceAsStream(folder);
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Iterator<String> iterator = bufferedReader.lines().iterator();
        StringBuilder arquivos = new StringBuilder();
        arquivos.append("[");
        while (true) {
            arquivos.append(iterator.next());
            if (iterator.hasNext()) {
                arquivos.append(", ");
            } else {
                break;
            }
        }
        arquivos.append("]");
        return arquivos.toString();
    }

    public final Message retrieveMessage() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            this.listenerDatagramSocket.receive(messageIn);
            String messageStr = new String(messageIn.getData());
            Message message = new Message(messageStr);
            switch (message.getType()) {
                case LISTFILES:
                    String arquivos = ls("");
                    sendMessage(new Message(MessageType.FILES, arquivos));
                    break;
            }
            return message;
        } catch (IOException ex) {
            Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author romulo
 */
public class Group {

    private final Client self;
    private final InetAddress ip;
    private final int port;
    private final MulticastSocket multicastSocket;
    private final List<Client> online;

    public Group(InetAddress ip, int port, Client self) {
        MulticastSocket multicastSocket = null;
        try {
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(ip);
        } catch (IOException ex) {
            System.err.println("Erro ao iniciar chat");
            if (multicastSocket != null) {
                multicastSocket.close();
            }
        }
        this.ip = ip;
        this.port = port;
        this.online = new ArrayList<>();
        this.multicastSocket = multicastSocket;
        this.self = self;
        sendMessage(new Message(MessageType.JOIN, this.self));
    }

    public final void sendMessage(Message mensagem) {
        byte[] bytesMessage = mensagem.toString().getBytes();
        DatagramPacket messageOut;
        try {
            messageOut = new DatagramPacket(bytesMessage, bytesMessage.length, this.ip, this.port);
            multicastSocket.send(messageOut);
        } catch (SocketException ex) {
            System.err.println("Erro na comunicacao");
        } catch (IOException ex) {
            System.err.println("Erro ao enviar mensagem" + ex.getMessage());
        }

    }

}

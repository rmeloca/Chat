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
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author romulo
 */
public class Group extends Observable {

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

    public Client getSelf() {
        return self;
    }

    public List<Client> getOnline() {
        return online;
    }

    public void leaveGroup() {
        try {
            sendMessage(new Message(MessageType.LEAVE, this.self));
            this.multicastSocket.leaveGroup(ip);
        } catch (IOException ex) {
            Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.multicastSocket.close();
        }
    }

    public final Message retrieveMessage() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            this.multicastSocket.receive(messageIn);
            String messageStr = new String(messageIn.getData());
            Message message = new Message(messageStr);

            if (message.getType().equals(MessageType.JOIN)) {
                InetAddress newClientAddress = messageIn.getAddress();
                Client newClient = message.getSender();
                newClient.setIp(newClientAddress);
                this.self.addKnownHost(newClientAddress, newClient);
                addOnline(newClient);
                sendMessage(new Message(MessageType.JOINACK, this.self));
            } else if (message.getType().equals(MessageType.LEAVE)) {
                removeOnline(new Client(message.getSender().getNickname()));
            }

            return message;
        } catch (IOException ex) {
            Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void addOnline(Client client) {
        this.online.add(client);
        setChanged();
        notifyObservers();
    }

    private void removeOnline(Client client) {
        this.online.remove(client);
        setChanged();
        notifyObservers();
    }

    public final void sendMessage(Message message) {
        byte[] bytesMessage = message.toString().getBytes();
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

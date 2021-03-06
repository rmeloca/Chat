/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.multicast;

import chat.Client;
import chat.Message;
import chat.MessageType;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 *
 * @author romulo
 */
public class MulticastListener implements Runnable {

    private final MulticastSocket multicastSocket;
    private final Client cliente;
    private final InetAddress endereco;
    private byte[] buffer;

    public MulticastListener(MulticastSocket multicastSocket, InetAddress endereco, Client cliente) {
        this.cliente = cliente;
        this.multicastSocket = multicastSocket;
        this.endereco = endereco;
    }

    @Override
    public void run() {
        String mensagemStr;
        Message mensagem;
        do {
            try {
                this.buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(messageIn);
                mensagemStr = new String(messageIn.getData());
                System.out.println("Recebido:" + mensagemStr);
                mensagem = new Message(mensagemStr);
                if (mensagem.getType().equals(MessageType.JOIN)) {
                    sendMessage(new Message(MessageType.JOINACK, cliente));
                }
                if (mensagem.getType().equals(MessageType.LEAVE)) {
                    if (this.cliente.equals(mensagem.getSender())) {
                        break;
                    }
                }
            } catch (IOException ex) {
                System.err.println("Erro ao receber mensagem");
            }
        } while (true);
    }

    private void sendMessage(Message mensagem) {
        byte[] bytesMessage = mensagem.toString().getBytes();
        DatagramPacket messageOut;
        try {
            messageOut = new DatagramPacket(bytesMessage, bytesMessage.length, this.endereco, multicastSocket.getLocalPort());
            multicastSocket.send(messageOut);
        } catch (SocketException ex) {
            System.err.println("Erro na comunicacao");
        } catch (IOException ex) {
            System.err.println("Erro ao enviar mensagem" + ex.getMessage());
        }

    }

}

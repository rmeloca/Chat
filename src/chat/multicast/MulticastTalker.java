/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.multicast;

import chat.Cliente;
import chat.Mensagem;
import chat.TipoMensagem;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

/**
 *
 * @author romulo
 */
public class MulticastTalker implements Runnable {

    private final MulticastSocket multicastSocket;
    private final Cliente cliente;
    private final InetAddress endereco;

    public MulticastTalker(MulticastSocket multicastSocket, InetAddress endereco, Cliente cliente) {
        this.multicastSocket = multicastSocket;
        this.cliente = cliente;
        this.endereco = endereco;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        sendMessage(new Mensagem(TipoMensagem.JOIN, cliente));
        do {
            System.out.println("Mensagem");
            String mensagem = scanner.nextLine();
            if (mensagem.contains("EXIT")) {
                break;
            }
            sendMessage(new Mensagem(TipoMensagem.MSG, cliente, mensagem));
        } while (true);
        sendMessage(new Mensagem(TipoMensagem.LEAVE, cliente));
    }

    protected void sendMessage(Mensagem mensagem) {
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

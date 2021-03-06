package chat.udp;

import chat.Client;
import chat.Message;
import chat.MessageType;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author romulo
 */
public class UDPTalker implements Runnable {

    private final DatagramSocket datagramSocket;
    private byte[] buffer;
    private final Client cliente;

    public UDPTalker(DatagramSocket datagramSocket, Client cliente) {
        this.datagramSocket = datagramSocket;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Digite a mensagem: ");
            String input = scanner.nextLine();
            if (input.contains("EXIT")) {
                break;
            }
            sendMessage(new Message(MessageType.MSGIDV, cliente, input));
        } while (true);
    }

    protected void sendMessage(Message mensagem) {
        try {
            buffer = mensagem.toString().getBytes();
            DatagramPacket request = new DatagramPacket(buffer, mensagem.toString().length(), datagramSocket.getInetAddress(), datagramSocket.getPort());
            datagramSocket.send(request);
        } catch (IOException ex) {
            System.err.println("Erro ao criar pacote");
        }
    }

}

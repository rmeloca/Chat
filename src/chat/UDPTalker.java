package chat;


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

    public UDPTalker(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Digite a mensagem: ");
                String input = scanner.nextLine();
                buffer = input.getBytes();
                DatagramPacket request = new DatagramPacket(buffer, input.length(), datagramSocket.getInetAddress(), datagramSocket.getPort());
                datagramSocket.send(request);
            } catch (IOException ex) {
                System.err.println("Erro ao criar pacote");
            }
        }
    }

}

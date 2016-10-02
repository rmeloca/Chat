package chat.udp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import chat.Message;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author romulo
 */
public class UDPListener implements Runnable {

    private final DatagramSocket datagramSocket;
    private final byte[] buffer;

    public UDPListener(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
        this.buffer = new byte[1000];
    }

    @Override
    public void run() {
        System.out.println("Aguardando mensagens...");
        while (true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            try {
                datagramSocket.receive(request);
                Message mensagem = new Message(new String(request.getData()));
                System.out.println("Cliente: " + mensagem.toString());
            } catch (IOException ex) {
                System.err.println("Erro ao ler pacote");
            }
        }
    }

}

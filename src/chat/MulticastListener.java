/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 *
 * @author romulo
 */
public class MulticastListener implements Runnable {

    private final MulticastSocket multicastSocket;
    private byte[] buffer;
    private final Cliente cliente;

    MulticastListener(MulticastSocket multicastSocket, Cliente cliente) {
        this.cliente = cliente;
        this.multicastSocket = multicastSocket;
    }

    @Override
    public void run() {
        String mensagem = "";
        do {
            try {
                this.buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(messageIn);
                mensagem = new String(messageIn.getData());
                System.out.println("Recebido:" + mensagem);
                //enviar joinack
                //buffer compartilhado
            } catch (IOException ex) {
                System.err.println("Erro ao receber mensagem");
            }
        } while (!(mensagem.contains(TipoMensagem.LEAVE.name()) && mensagem.contains(cliente.getApelido())));
    }

}

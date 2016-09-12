/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 *
 * @author romulo
 */
public class Cliente {

//    implementar um chat usandop comunicação em grupo
//    -> JOIN   --> JOIN    [APELIDO]
//    -> MSG    --> MSG     [APELIDO] "texto"
//    -> LEAVE  --> LEAVE   [APELIDO]
//    ID GRUPO 225.1.2.3 porta 6789
    private String apelido;

    public Cliente(String apelido) {
        this.apelido = apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getApelido() {
        return apelido;
    }

    public void joinGroup(String ip, int porta) {
        MulticastSocket multicastSocket = null;
        try {
            InetAddress group = InetAddress.getByName(ip);
            multicastSocket = new MulticastSocket(porta);
            multicastSocket.joinGroup(group);

            MulticastListener multicastListener = new MulticastListener(multicastSocket, this);
            Thread listenerThread = new Thread(multicastListener);

            MulticastTalker multicastTalker = new MulticastTalker(multicastSocket, this);
            Thread talkerThread = new Thread(multicastTalker);

            listenerThread.start();
            talkerThread.start();

            listenerThread.join();
            talkerThread.join();

            try {
                multicastSocket.leaveGroup(group);
            } catch (IOException ex) {
                System.err.println("Erro ao sair do chat");
            }

        } catch (IOException ex) {
            System.err.println("Erro ao iniciar chat");
        } catch (InterruptedException ex) {
            System.err.println("Erro fluxo");
        } finally {
            if (multicastSocket != null) {
                multicastSocket.close(); //fecha o socket
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Apelido");
        String apelido = scanner.nextLine();

        Cliente cliente = new Cliente(apelido);

        System.out.println("IP");
        String ip;
        ip = scanner.next();
//        ip = "225.1.2.3";

        System.out.println("Porta");
        int porta;
        porta = scanner.nextInt();
//        porta = 6789;

        cliente.joinGroup(ip, porta);
    }
}

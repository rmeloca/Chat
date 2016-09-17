/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.multicast.MulticastTalker;
import chat.multicast.MulticastInternalTalker;
import chat.multicast.MulticastListener;
import chat.udp.UDPTalker;
import chat.udp.UDPListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

//Implementar um serviço de chat que possibilite:
//- envio de mensagens para um grupo de pessoas (MulticastSocket) - GRUPO 225.1.2.3
//- envio de mensagens individuais para as pessoas ativas (DatagramSocket) - receber na porta 6799
//- compartilhamento e download de arquivos (Socket -- TCP) 
//- interface de interação (GUI ou CLI)
//
//- protocolo textual:
//   -- JOIN [apelido] 
//   * junta-se ao grupo de conversação 
//   -- JOINACK [apelido] 
//   * resposta ao JOIN para possibilitar a manutenção da lista de usuários ativos
//   -- MSG [apelido] "texto"
//   * mensagem enviada a todos os membros do grupo pelo IP 225.1.2.3 e porta 6789 
//   -- MSGIDV FROM [apelido] TO [apelido] "texto" 
//   * mensagem enviada a um membro do grupo para ser recebida na porta 6799
//   -- LISTFILES [apelido] 
//   * solicitação de listagem de arquivos para um usuário 
//   -- FILES [arq1, arq2, arqN] 
//   * resposta para o LISTFILES
//   -- DOWNFILE [apelido] filename 
//   * solicita arquivo do servidor. 
//   -- DOWNINFO [filename, size, IP, PORTA] 
//   * resposta com informações sobre o arquivo e conexão TCP. 
//   -- LEAVE [apelido]
//   * deixa o grupo de conversação
/**
 *
 * @author romulo
 */
public class Cliente {

    private String apelido;
    private InetAddress ip;

    public Cliente(String apelido) {
        this.apelido = apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getApelido() {
        return apelido;
    }

    public void connectToPeer(int listenToPort, String talkToHost, int talkToPort) {
        DatagramSocket listenerDatagramSocket = null;
        DatagramSocket talkerDatagramSocket = null;
        try {
            InetAddress inetAddres = InetAddress.getByName(talkToHost);

            listenerDatagramSocket = new DatagramSocket(listenToPort);
            talkerDatagramSocket = new DatagramSocket();

            talkerDatagramSocket.connect(inetAddres, talkToPort);

            UDPListener udpListener = new UDPListener(listenerDatagramSocket);
            UDPTalker udpTalker = new UDPTalker(talkerDatagramSocket);

            Thread listener = new Thread(udpListener);
            Thread talker = new Thread(udpTalker);

            talker.start();
            listener.start();

            talker.join();
            listener.join();

        } catch (SocketException | UnknownHostException | InterruptedException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (listenerDatagramSocket != null) {
                listenerDatagramSocket.close(); //fecha o socket
            }
            if (talkerDatagramSocket != null) {
                talkerDatagramSocket.close(); //fecha o socket
            }
        }
    }

    public void joinGroup(String ip, int porta) {
        MulticastSocket multicastSocket = null;
        try {
            InetAddress group = InetAddress.getByName(ip);
            multicastSocket = new MulticastSocket(porta);
            multicastSocket.joinGroup(group);

            MulticastListener multicastListener = new MulticastListener(multicastSocket, group, this);
            Thread listenerThread = new Thread(multicastListener);

            MulticastTalker multicastTalker = new MulticastTalker(multicastSocket, group, this);
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

//        System.out.println("IP");
        String ip;
//        ip = scanner.next();
        ip = "225.1.2.3";

//        System.out.println("Porta");
        int porta;
//        porta = scanner.nextInt();
        porta = 6789;

        cliente.joinGroup(ip, porta);
    }
}

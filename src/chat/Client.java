/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.multicast.MulticastTalker;
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
import java.util.logging.Level;
import java.util.logging.Logger;

//Implementar um serviço de chat que possibilite:
//- envio de mensagens para um grupo de pessoas (MulticastSocket) - GRUPO 225.1.2.3
//- envio de mensagens individuais para as pessoas ativas (DatagramSocket) - receber na porta 6799
//- compartilhamento e download de arquivos (Socket -- TCP) 
//- interface de interação (GUI ou CLI)
//
//- protocolo textual:
// +  -- JOIN [nickname] 
//   * junta-se ao grupo de conversação 
// +  -- JOINACK [nickname] 
//   * resposta ao JOIN para possibilitar a manutenção da lista de usuários ativos
// +  -- MSG [nickname] "texto"
//   * mensagem enviada a todos os membros do grupo pelo IP 225.1.2.3 e porta 6789 
//   -- MSGIDV FROM [nickname] TO [nickname] "texto" 
//   * mensagem enviada a um membro do grupo para ser recebida na porta 6799
//   -- LISTFILES [nickname] 
//   * solicitação de listagem de arquivos para um usuário 
//   -- FILES [arq1, arq2, arqN] 
//   * resposta para o LISTFILES
//   -- DOWNFILE [nickname] filename 
//   * solicita arquivo do servidor. 
//   -- DOWNINFO [filename, size, IP, PORTA] 
//   * resposta com informações sobre o arquivo e conexão TCP. 
// +  -- LEAVE [nickname]
//   * deixa o grupo de conversação
/**
 *
 * @author romulo
 */
public class Client {

    private String nickname;
    private InetAddress ip;
    private Group group;

    public Client(String apelido) {
        this.nickname = apelido;
        this.group = null;
        this.ip = null;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
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
            UDPTalker udpTalker = new UDPTalker(talkerDatagramSocket, this);

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

    public void leaveGroup(String ip, int porta) {
        MulticastSocket multicastSocket = null;
        try {
            InetAddress group = InetAddress.getByName(ip);
            multicastSocket = new MulticastSocket(porta);
            multicastSocket.leaveGroup(group);
        } catch (IOException ex) {
            System.err.println("Erro ao sair do chat" + ex);
        } finally {
            if (multicastSocket != null) {
                multicastSocket.close(); //fecha o socket
            }
        }
    }

    public void joinGroup(InetAddress ip, int porta) {
        this.group = new Group(ip, porta, this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Client)) {
            return false;
        }
        Client cliente = (Client) obj;
        return this.nickname.equals(cliente.nickname);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Apelido");
        String apelido = scanner.next();

        Client cliente = new Client(apelido);

//        System.out.println("IP");
        String ip;
//        ip = scanner.next();
        ip = "225.1.2.3";

//        System.out.println("Porta");
        int porta;
//        porta = scanner.nextInt();
        porta = 6789;

        try {
            cliente.joinGroup(InetAddress.getByName(ip), porta);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessageToGroup(String text) {

    }

}

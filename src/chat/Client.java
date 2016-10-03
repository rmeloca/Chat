/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
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
    private final PriorityQueue<Integer> listenToPortQueue;
    private final Map<InetAddress, PeerConnection> connections;
    private final Map<InetAddress, Client> knownHosts;

    public Client(String apelido) {
        this.nickname = apelido;
        this.group = null;
        this.connections = new HashMap<>();
        this.knownHosts = new HashMap<>();

        this.listenToPortQueue = new PriorityQueue<>();
        for (int i = 30000; i < 30500; i++) {
            this.listenToPortQueue.add(i);
        }

        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ip = ip;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public void addKnownHost(InetAddress ip, Client client) {
        this.knownHosts.put(ip, client);
    }

    public void leaveGroup() {
        this.group.leaveGroup();
        this.group = null;
    }

    public void joinGroup(String ip, int port) {
        try {
            this.group = new Group(InetAddress.getByName(ip), port, this);
            Thread thread = new Thread(new GroupMessageCollector(group));
            thread.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessageToGroup(String text) {
        this.group.sendMessage(new Message(MessageType.MSG, text));
    }

    public void connectToPeer(String ip, int port) {
        int listenToPort = this.listenToPortQueue.poll();
        try {
            InetAddress addressToTalk = InetAddress.getByName(ip);
            PeerConnection peerConnection = new PeerConnection(listenToPort, addressToTalk, port, this.knownHosts.get(addressToTalk));
            this.connections.put(addressToTalk, peerConnection);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnectFromPeer(String ip) {
        try {
            InetAddress addressToTalk = InetAddress.getByName(ip);
            PeerConnection disconnectedPeer = this.connections.remove(addressToTalk);
            disconnectedPeer.disconnect();
            this.listenToPortQueue.add(disconnectedPeer.getListenToPort());
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessageToPeer(String ip, String text) {
        try {
            InetAddress addressToTalk = InetAddress.getByName(ip);
            PeerConnection peerConnection = this.connections.get(addressToTalk);
            peerConnection.sendMessage(new Message(MessageType.MSGIDV, this, peerConnection.getClient(), text));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        cliente.joinGroup(ip, porta);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final Map<InetAddress, Client> knownHosts;
    private PeerController peerController;
    private Thread peerMessageCollectorThread;
    private Thread groupMessageCollectorThread;

    public Client(String nickname) {
        this.nickname = nickname;
        this.group = null;
        this.knownHosts = new HashMap<>();

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

    public InetAddress getIp() {
        return ip;
    }

    public Group getGroup() {
        return group;
    }

    public void addKnownHost(InetAddress ip, Client client) {
        this.knownHosts.put(ip, client);
    }

    public InetAddress findIpByNickname(String nickname) {
        for (Client client : knownHosts.values()) {
            if (client.getNickname().equals(nickname)) {
                return client.ip;
            }
        }
        return null;
    }

    public List<String> getOnlineNicknames() {
        if (group != null) {
            List<String> onlineNicknames = new ArrayList<>();
            List<Client> online = this.group.getOnline();
            for (Client client : online) {
                onlineNicknames.add(client.nickname);
            }
            return onlineNicknames;
        }
        return null;
    }

    public void leaveGroup() {
        this.peerMessageCollectorThread.interrupt();
        this.groupMessageCollectorThread.interrupt();

        this.peerController.disconnect();
        this.group.leaveGroup();

        this.peerController = null;
        this.group = null;
    }

    public void joinGroup(InetAddress ip, int port) {
        this.group = new Group(ip, port, this);
        this.groupMessageCollectorThread = new Thread(new GroupMessageCollector(this.group));
        this.groupMessageCollectorThread.start();

        this.peerController = new PeerController(this);
        this.peerMessageCollectorThread = new Thread(new PeerMessageCollector(this.peerController));
        this.peerMessageCollectorThread.start();
    }

    public void sendMessageToGroup(String text) {
        this.group.sendMessage(new Message(MessageType.MSG, this, text));
    }

    public void sendMessageToPeer(InetAddress ip, String text) {
        Client addressee = this.knownHosts.get(ip);
        if (text.equals("ls")) {
            this.peerController.sendMessage(new Message(MessageType.LISTFILES, this, addressee));
        } else if (text.startsWith("wget")) {
            this.peerController.sendMessage(new Message(MessageType.DOWNFILE, this, addressee, text.split(" ")[1]));
        } else {
            this.peerController.sendMessage(new Message(MessageType.MSGIDV, this, addressee, text));
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

        String ip;
        ip = "225.1.2.3";

        int porta;
        porta = 6789;

        try {
            cliente.joinGroup(InetAddress.getByName(ip), porta);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

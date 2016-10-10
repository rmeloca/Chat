/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

/**
 *
 * @author romulo
 */
public class Message {

    private final MessageType type;
    private Client sender;
    private Client addressee;
    private String content;

    public Message(String parseInput) {
        this.content = "";
        String[] split = parseInput.split(" ");
        this.type = MessageType.valueOf(split[0]);
        int init = 0;
        switch (type) {
            case MSG:
            case DOWNFILE:
                init = 2;
            case JOIN:
            case JOINACK:
            case LEAVE:
            case LISTFILES:
                this.sender = new Client(split[1].replace("[", "").replace("]", "").trim());
                break;
            case FILES:
            case DOWNINFO:
                init = 1;
                break;
            case MSGIDV:
                this.sender = new Client(split[2].replace("[", "").replace("]", "").trim());
                this.addressee = new Client(split[4].replace("[", "").replace("]", "").trim());
                init = 5;
                break;
        }
        for (int i = init; i < split.length; i++) {
            this.content += split[i];
            this.content += " ";
        }
    }

    public Message(MessageType tipo, Client remetente) {
        this.content = "";
        this.type = tipo;
        this.sender = remetente;
    }

    public Message(MessageType tipo, Client remetente, String conteudo) {
        this.content = "";
        this.type = tipo;
        this.sender = remetente;
        this.content = conteudo;
    }

    public Message(MessageType tipo, String conteudo) {
        this.content = "";
        this.type = tipo;
        this.content = conteudo;
    }

    public Message(MessageType tipo, Client remetente, Client destinatario, String conteudo) {
        this.content = "";
        this.type = tipo;
        this.sender = remetente;
        this.addressee = destinatario;
        this.content = conteudo;
    }

    public MessageType getType() {
        return type;
    }

    public Client getSender() {
        return sender;
    }

    public Client getAddressee() {
        return addressee;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        switch (type) {
            case JOIN:
            case JOINACK:
            case LEAVE:
            case LISTFILES:
                return type.name() + " " + "[" + sender.getNickname() + "]";
            case MSG:
            case DOWNFILE:
                return type.name() + " " + "[" + sender.getNickname() + "]" + " " + content;
            case FILES:
            case DOWNINFO:
                return type.name() + " " + content;
            case MSGIDV:
                return type.name() + " FROM " + "[" + sender.getNickname() + "]" + " TO " + "[" + addressee.getNickname() + "]" + " " + content;
            default:
                return null;
        }
    }

}

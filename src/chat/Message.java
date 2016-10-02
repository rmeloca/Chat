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

    private final MessageType tipo;
    private Client remetente;
    private Client destinatario;
    private String conteudo;

    public Message(String parseInput) {
        String[] split = parseInput.split(" ");
        this.tipo = MessageType.valueOf(split[0]);
        switch (tipo) {
            case MSG:
            case DOWNFILE:
                this.conteudo = split[2];
            case JOIN:
            case JOINACK:
            case LEAVE:
            case LISTFILES:
                this.remetente = new Client(split[1].replace("[", "").replace("]", "").trim());
                break;
            case FILES:
            case DOWNINFO:
                this.conteudo = split[1];
                break;
            case MSGIDV:
                this.remetente = new Client(split[2].replace("[", "").replace("]", "").trim());
                this.destinatario = new Client(split[4].replace("[", "").replace("]", "").trim());
                this.conteudo = split[5];
                break;
        }
    }

    public Message(MessageType tipo, Client remetente) {
        this.tipo = tipo;
        this.remetente = remetente;
    }

    public Message(MessageType tipo, Client remetente, String conteudo) {
        this.tipo = tipo;
        this.remetente = remetente;
        this.conteudo = conteudo;
    }

    public Message(MessageType tipo, String conteudo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
    }

    public Message(MessageType tipo, Client remetente, Client destinatario, String conteudo) {
        this.tipo = tipo;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.conteudo = conteudo;
    }

    public MessageType getTipo() {
        return tipo;
    }

    public Client getRemetente() {
        return remetente;
    }

    @Override
    public String toString() {
        switch (tipo) {
            case JOIN:
            case JOINACK:
            case LEAVE:
            case LISTFILES:
                return tipo.name() + " " + "[" + remetente.getNickname() + "]";
            case MSG:
            case DOWNFILE:
                return tipo.name() + " " + "[" + remetente.getNickname() + "]" + " " + conteudo;
            case FILES:
            case DOWNINFO:
                return tipo.name() + " " + conteudo;
            case MSGIDV:
                return tipo.name() + " FROM " + "[" + remetente.getNickname() + "]" + " TO " + "[" + destinatario.getNickname() + "]" + " " + conteudo;
            default:
                return null;
        }
    }

}

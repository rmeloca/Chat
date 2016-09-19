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
public class Mensagem {

    private final TipoMensagem tipo;
    private Cliente remetente;
    private Cliente destinatario;
    private String conteudo;

    public Mensagem(String parseInput) {
        String[] split = parseInput.split(" ");
        this.tipo = TipoMensagem.valueOf(split[0]);
        switch (tipo) {
            case MSG:
            case DOWNFILE:
                this.conteudo = split[2];
            case JOIN:
            case JOINACK:
            case LEAVE:
            case LISTFILES:
                this.remetente = new Cliente(split[1].substring(1, split[1].length() - 1));
                break;
            case FILES:
            case DOWNINFO:
                this.conteudo = split[1];
                break;
            case MSGIDV:
                this.remetente = new Cliente(split[2].substring(1, split[2].length() - 1));
                this.destinatario = new Cliente(split[4].substring(1, split[4].length() - 1));
                this.conteudo = split[5];
                break;
        }
    }

    public Mensagem(TipoMensagem tipo, Cliente remetente) {
        this.tipo = tipo;
        this.remetente = remetente;
    }

    public Mensagem(TipoMensagem tipo, Cliente remetente, String conteudo) {
        this.tipo = tipo;
        this.remetente = remetente;
        this.conteudo = conteudo;
    }

    public Mensagem(TipoMensagem tipo, String conteudo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
    }

    public Mensagem(TipoMensagem tipo, Cliente remetente, Cliente destinatario, String conteudo) {
        this.tipo = tipo;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.conteudo = conteudo;
    }

    public TipoMensagem getTipo() {
        return tipo;
    }

    public Cliente getRemetente() {
        return remetente;
    }

    @Override
    public String toString() {
        switch (tipo) {
            case JOIN:
            case JOINACK:
            case LEAVE:
            case LISTFILES:
                return tipo.name() + " " + "[" + remetente.getApelido() + "]";
            case MSG:
            case DOWNFILE:
                return tipo.name() + " " + "[" + remetente.getApelido() + "]" + " " + conteudo;
            case FILES:
            case DOWNINFO:
                return tipo.name() + " " + conteudo;
            case MSGIDV:
                return tipo.name() + " FROM " + "[" + remetente.getApelido() + "]" + " TO " + "[" + destinatario.getApelido() + "]" + " " + conteudo;
            default:
                return null;
        }
    }

}

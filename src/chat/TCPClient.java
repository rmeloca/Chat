package chat;

/**
 * * TCPClient: Cliente para conexao TCP Descricao: Envia uma informacao ao *
 * servidor e finaliza a conexao
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class TCPClient {

    public static void main(String args[]) {
        /* args[0]: ip do servidor */
        Socket socket;
        try {
            int serverPort = 7896;
            /* especifica a porta */
            socket = new Socket(args[0], serverPort);
            /* conecta com o servidor */
 /* cria objetos de leitura e escrita */
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            String mensagem;
            Scanner scanner = new Scanner(System.in);
            Request request;
            do {
                System.out.print("Digite: ");
                mensagem = scanner.nextLine();
                request = new Request(mensagem);// envia uma string para o servidor         
                outputStream.flush();
                outputStream.writeObject(request);
                System.out.println("Informacao enviada.");
                Response response = (Response) inputStream.readObject();
                System.out.println("Resposta: " + response.getMensagem());
            } while (!request.isExit());
            inputStream.close();
            outputStream.close();
            socket.close();   //finaliza a conexao     
        } catch (UnknownHostException e) {
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("leitura:" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("classn:" + ex.getMessage());
        } //catch  
    } //main

    private static class Request {

        public Request() {
        }

        private Request(String mensagem) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private boolean isExit() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class Response {

        public Response() {
        }

        private String getMensagem() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
} //class

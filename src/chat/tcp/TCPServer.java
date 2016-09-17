package chat.tcp;

/**
 * * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma *
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.net.*;
import java.io.*;

public class TCPServer {

    public static void main(String args[]) {
        try {
            int serverPort = 7896; // porta do servidor           
            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                System.out.println("Servidor aguardando conexao ...");
                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();
                System.out.println("Cliente conectado ... Criando thread ...");
                /* cria um thread para atender a conexao */
                WorkerThread workerThread = new WorkerThread(clientSocket);
                workerThread.start();
            } //while      
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch   
    } //main

    private static class WorkerThread {

        public WorkerThread(Socket clientSocket) {
        }

        private void start() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
} //class

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author romulo
 */
public class PeerController {

    private final DatagramSocket listenerDatagramSocket;
    private final DatagramSocket talkerDatagramSocket;
    private final Client self;

    public PeerController(Client self) {
        DatagramSocket listenerDatagramSocket = null;
        DatagramSocket talkerDatagramSocket = null;
        try {
            listenerDatagramSocket = new DatagramSocket(30000);
            talkerDatagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.listenerDatagramSocket = listenerDatagramSocket;
        this.talkerDatagramSocket = talkerDatagramSocket;
        this.self = self;
    }

    public void disconnect() {
        this.listenerDatagramSocket.close();
        this.talkerDatagramSocket.close();
    }

    public final void sendMessage(Message message) {
        talkerDatagramSocket.connect(message.getAddressee().getIp(), 6799);
        byte[] bytesMessage = message.toString().getBytes();
        DatagramPacket messageOut;
        try {
            messageOut = new DatagramPacket(bytesMessage, bytesMessage.length, message.getAddressee().getIp(), 31000);
            this.talkerDatagramSocket.send(messageOut);
        } catch (SocketException ex) {
            System.err.println("Erro na comunicacao");
        } catch (IOException ex) {
            System.err.println("Erro ao enviar mensagem" + ex.getMessage());
        }
    }

    public final Message retrieveMessage() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            this.listenerDatagramSocket.receive(messageIn);
            String messageStr = new String(messageIn.getData());
            Message message = new Message(messageStr);
            switch (message.getType()) {
                case LISTFILES:
                    String arquivos = ls("");
                    sendMessage(new Message(MessageType.FILES, arquivos));
                    break;
                case DOWNFILE:
                    String info = getFileInfo(message.getContent());
                    sendMessage(new Message(MessageType.DOWNINFO, info));
                    Thread sendFileThread = new Thread(new FileSender(this, message.getContent()));
                    sendFileThread.start();
                    break;
                case DOWNINFO:
                    String[] split = message.getContent().replace("[", "").replace("]", "").replace(" ", "").split(",");
                    Thread wgetThread = new Thread(new FileDownloader(this, InetAddress.getByName(split[2]), Integer.valueOf(split[3]), split[0], Integer.valueOf(split[1])));
                    wgetThread.start();
                    break;
            }
            return message;
        } catch (IOException ex) {
            Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String ls(String folder) {
        InputStream inputStream = getClass().getResourceAsStream(folder);
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Iterator<String> iterator = bufferedReader.lines().iterator();
        StringBuilder arquivos = new StringBuilder();
        arquivos.append("[");
        while (true) {
            arquivos.append(iterator.next());
            if (iterator.hasNext()) {
                arquivos.append(", ");
            } else {
                break;
            }
        }
        arquivos.append("]");
        return arquivos.toString();
    }

    private String getFileInfo(String filename) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(filename.trim());
            StringBuilder arquivos = new StringBuilder();
            arquivos.append("[");
            arquivos.append(filename);
            arquivos.append(", ");
            arquivos.append(inputStream.available());
            arquivos.append(", ");
            arquivos.append(this.self.getIp().getHostAddress());
            arquivos.append(", ");
            arquivos.append(20000);
            arquivos.append("]");
            return arquivos.toString();
        } catch (IOException ex) {
            Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    protected void sendFile(String filename) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        InputStream inputStream = null;
        try {
            int serverPort = 20000;
            serverSocket = new ServerSocket(serverPort);
            clientSocket = serverSocket.accept();
            inputStream = getClass().getResourceAsStream(filename);
            while (true) {
                int read = inputStream.read();
                if (read == -1) {
                    break;
                }
                clientSocket.getOutputStream().write(read);
            }
        } catch (IOException e) {
            System.out.println("Socket Error: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void wget(InetAddress ip, int port, String filename, int size) {
        Socket clientSocket = null;
        FileOutputStream download = null;
        InputStream inputStream = null;
        try {
            clientSocket = new Socket(ip, port);
            inputStream = clientSocket.getInputStream();

            download = new FileOutputStream(filename);
            while (true) {
                int read = inputStream.read();
                if (read == -1) {
                    break;
                }
                download.write(read);
            }
        } catch (IOException ex) {
            Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (download != null) {
                try {
                    download.close();
                } catch (IOException ex) {
                    Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(PeerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}

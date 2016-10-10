/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.net.InetAddress;

/**
 *
 * @author romulo
 */
public class FileDownloader implements Runnable {

    private final PeerConnection peerConnection;
    private final InetAddress ip;
    private final int port;
    private final String filename;
    private final int size;

    public FileDownloader(PeerConnection peerConnection, InetAddress ip, int port, String filename, int size) {
        this.peerConnection = peerConnection;
        this.ip = ip;
        this.port = port;
        this.filename = filename;
        this.size = size;
    }

    @Override
    public void run() {
        peerConnection.wget(ip, port, filename, size);
    }

}

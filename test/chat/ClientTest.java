/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author romulo
 */
public class ClientTest {

    public ClientTest() {
    }

    @Test
    public void testSomeMethod() {
        try {
            InetAddress address1 = InetAddress.getByName("192.168.2.1");
            InetAddress address2 = InetAddress.getByName("192.168.2.1");
            assertEquals(address2, address1);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testtMethod() {
        InputStream inputStream = getClass().getResourceAsStream("Buffer.class");

        try {
            Client romulo = new Client("romulo");
            Client bianca = new Client("bianca");

            InetAddress biancaIp = InetAddress.getByName("192.168.25.4");
            InetAddress romuloIp = InetAddress.getByName("192.168.25.4");

            romulo.addKnownHost(biancaIp, bianca);
//            romulo.connectToPeer(biancaIp, 10000);

//            romulo.sendMessageToPeer(biancaIp, "Ola");
//            romulo.sendMessageToPeer(biancaIp, "Ola");
//            romulo.sendMessageToPeer(biancaIp, "ls");
            romulo.sendMessageToPeer(biancaIp, "wget Buffer.class");
//            
//            bianca.addKnownHost(romuloIp, romulo);
//            bianca.connectToPeer(romuloIp, 10001);
//            
//            bianca.sendMessageToPeer(romuloIp, "Ola");
//            bianca.sendMessageToPeer(romuloIp, "ls");

        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

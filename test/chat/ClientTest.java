/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

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
    public void testtMethod() {
        try {
            Client romulo = new Client("romulo");
            Client bianca = new Client("bianca");
            
            InetAddress romuloIp = InetAddress.getByName("192.168.25.4");
            romulo.addKnownHost(romuloIp, bianca);
            romulo.connectToPeer(romuloIp, 10000);
            romulo.sendMessageToPeer(romuloIp, "Ola");
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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

}

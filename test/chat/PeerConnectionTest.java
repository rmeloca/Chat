/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author romulo
 */
public class PeerConnectionTest {

    public PeerConnectionTest() {
    }

    @Test
    public void testSomeMethod() {
        String filename = "/data/devprconf.jpg";
//        InputStream inputStream = getClass().getResourceAsStream(filename);
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            while (true) {
                int read;
                read = fileInputStream.read();
                if (read == -1) {
                    break;
                }
                System.out.println(read);
            }
        } catch (IOException ex) {
            Logger.getLogger(PeerConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

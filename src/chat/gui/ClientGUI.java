/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.gui;

import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author romulo
 */
public class ClientGUI extends JFrame {

    public ClientGUI() throws HeadlessException {
        super.setTitle("Batalha Terrestre");

        super.setLayout(null);
        JButton btnJogar = new JButton("Jogar");
        btnJogar.setBounds(590, 580, 100, 30);
        super.add(btnJogar);

        super.setResizable(false);
        super.setSize(700, 700);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}

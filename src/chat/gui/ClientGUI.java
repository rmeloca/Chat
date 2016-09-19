/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author romulo
 */
public class ClientGUI extends JFrame {

    public ClientGUI() throws HeadlessException {
        super.setTitle("Chat");

        super.setLayout(new BorderLayout());

        JButton btnJogar = new JButton("Jogar");
        btnJogar.setBounds(590, 580, 100, 30);

        JPanel northPanel = new JPanel(new GridLayout(1, 4));
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JPanel southPanel = new JPanel(new GridLayout(1, 2));

        super.add(northPanel, BorderLayout.NORTH);
        super.add(centerPanel, BorderLayout.CENTER);
        super.add(southPanel, BorderLayout.SOUTH);

        JTextField apelidoTextField = new JTextField("Apelido");
        JTextField grupoTextField = new JTextField("225.1.2.3");
        JTextField portaTextField = new JTextField("6789");
        
        JButton joinButton = new JButton("Join");
        JButton leaveButton = new JButton("Leave");
        leaveButton.setEnabled(false);

        JPanel painelJoinLeave = new JPanel(new GridLayout(2, 1));
        painelJoinLeave.add(joinButton);
        painelJoinLeave.add(leaveButton);

        northPanel.add(apelidoTextField);
        northPanel.add(grupoTextField);
        northPanel.add(portaTextField);
        northPanel.add(painelJoinLeave);

        JTextArea inboxTextArea = new JTextArea();
        inboxTextArea.setText("Jogar asd sg da");
        inboxTextArea.setEnabled(false);

        JTextArea outboxTextArea = new JTextArea();

        JList<String> onlineJList = new JList<>();
        Vector<String> clientes = new Vector<>();
        clientes.add("romulo1");
        clientes.add("romulo2");
        clientes.add("romulo3");
        clientes.add("romulo4");
        onlineJList.setListData(clientes);

        JRadioButton messageToAll = new JRadioButton("Todos");
        messageToAll.setSelected(true);
        JRadioButton messageToPeer = new JRadioButton("Individual");
        
        ButtonGroup messageTo = new ButtonGroup();
        messageTo.add(messageToAll);
        messageTo.add(messageToPeer);

        JPanel painelMessageTo = new JPanel(new GridLayout(1,2));
        painelMessageTo.add(messageToAll);
        painelMessageTo.add(messageToPeer);
        
        JPanel painelOnline = new JPanel(new GridLayout(2, 1));
        painelOnline.add(painelMessageTo);
        painelOnline.add(onlineJList);

        centerPanel.add(inboxTextArea);
        centerPanel.add(painelOnline);

        southPanel.add(outboxTextArea);
        southPanel.add(new JButton("Enviar"));

        super.setResizable(false);
        super.setSize(700, 700);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}

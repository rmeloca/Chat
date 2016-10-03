/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.gui;

import chat.Client;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
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

    private JTextField nicknameTextField;
    private JTextField groupTextField;
    private JTextField portTextField;
    private Client client;
    private JButton joinButton;
    private JButton leaveButton;
    private JButton sendButton;
    JTextArea outboxTextArea;
    JTextArea inboxTextArea;

    public ClientGUI() throws HeadlessException {
        super.setTitle("Chat");

        super.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new GridLayout(1, 4));
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JPanel southPanel = new JPanel(new GridLayout(1, 2));

        super.add(northPanel, BorderLayout.NORTH);
        super.add(centerPanel, BorderLayout.CENTER);
        super.add(southPanel, BorderLayout.SOUTH);

        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                inboxTextArea.append(String.valueOf((char) b));
                inboxTextArea.setCaretPosition(inboxTextArea.getDocument().getLength());
            }
        });

        System.setOut(printStream);

        nicknameTextField = new JTextField("Apelido");
        groupTextField = new JTextField("225.1.2.3");
        portTextField = new JTextField("6789");

        joinButton = new JButton("Join");
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinButton.setEnabled(false);
                leaveButton.setEnabled(true);
                nicknameTextField.setEnabled(false);
                portTextField.setEnabled(false);
                groupTextField.setEnabled(false);
                sendButton.setEnabled(true);
                client = new Client(nicknameTextField.getText());
                String ip = groupTextField.getText();
                int port = Integer.valueOf(portTextField.getText());
                client.joinGroup(ip, port);
            }
        });

        leaveButton = new JButton("Leave");
        leaveButton.setEnabled(false);
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leaveButton.setEnabled(false);
                joinButton.setEnabled(true);
                nicknameTextField.setEnabled(true);
                portTextField.setEnabled(true);
                groupTextField.setEnabled(true);
                sendButton.setEnabled(false);
                String ip = groupTextField.getText();
                int port = Integer.valueOf(portTextField.getText());
                client.leaveGroup();
            }
        });

        JPanel painelJoinLeave = new JPanel(new GridLayout(2, 1));
        painelJoinLeave.add(joinButton);
        painelJoinLeave.add(leaveButton);

        northPanel.add(nicknameTextField);
        northPanel.add(groupTextField);
        northPanel.add(portTextField);
        northPanel.add(painelJoinLeave);

        inboxTextArea = new JTextArea();
        inboxTextArea.setText("Jogar asd sg da");
        inboxTextArea.setEnabled(false);

        JList<String> onlineJList = new JList<>();
        if (client != null) {
            List<String> online = this.client.getOnlineNicknames();
            if (online != null) {
                onlineJList.setListData((String[]) online.toArray());
            }
        }

        JRadioButton messageToAll = new JRadioButton("Todos");
        messageToAll.setSelected(true);
        JRadioButton messageToPeer = new JRadioButton("Individual");

        ButtonGroup messageTo = new ButtonGroup();
        messageTo.add(messageToAll);
        messageTo.add(messageToPeer);

        JPanel painelMessageTo = new JPanel(new GridLayout(1, 2));
        painelMessageTo.add(messageToAll);
        painelMessageTo.add(messageToPeer);

        JPanel painelOnline = new JPanel(new GridLayout(2, 1));
        painelOnline.add(painelMessageTo);
        painelOnline.add(onlineJList);

        centerPanel.add(inboxTextArea);
        centerPanel.add(painelOnline);

        outboxTextArea = new JTextArea("your message");
        sendButton = new JButton("Enviar");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessageToGroup(outboxTextArea.getText());
            }
        });

        southPanel.add(outboxTextArea);
        southPanel.add(sendButton);

        super.setResizable(false);
        super.setSize(700, 700);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}

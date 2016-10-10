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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author romulo
 */
public class ClientGUI extends JFrame implements Observer {

    private JTextField nicknameTextField;
    private JTextField groupTextField;
    private JTextField portTextField;
    private Client client;
    private JButton joinButton;
    private JButton leaveButton;
    private JButton sendButton;
    private JTextArea outboxTextArea;
    private JTextArea inboxTextArea;
    private JList<String> onlineJList;
    private JRadioButton messageToAll;
    private JRadioButton messageToPeer;

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
                client = new Client(nicknameTextField.getText());
                String ip = groupTextField.getText();
                int port = Integer.valueOf(portTextField.getText());
                try {
                    client.joinGroup(InetAddress.getByName(ip), port);
                    client.getGroup().addObserver(ClientGUI.this);
                    blockInterface();
                } catch (UnknownHostException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Ip Incorreto");
                }
            }
        });

        leaveButton = new JButton("Leave");
        leaveButton.setEnabled(false);
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                releaseInterface();
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
        inboxTextArea.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(inboxTextArea);

        onlineJList = new JList<>();

        messageToAll = new JRadioButton("Todos");
        messageToAll.setSelected(true);
        messageToPeer = new JRadioButton("Individual");

        ButtonGroup messageTo = new ButtonGroup();
        messageTo.add(messageToAll);
        messageTo.add(messageToPeer);

        JPanel painelMessageTo = new JPanel(new GridLayout(1, 2));
        painelMessageTo.add(messageToAll);
        painelMessageTo.add(messageToPeer);

        JPanel painelOnline = new JPanel(new GridLayout(2, 1));
        painelOnline.add(painelMessageTo);
        painelOnline.add(onlineJList);

        centerPanel.add(scrollPane);
        centerPanel.add(painelOnline);

        outboxTextArea = new JTextArea("your message");
        sendButton = new JButton("Enviar");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (messageToAll.isSelected()) {
                    client.sendMessageToGroup(outboxTextArea.getText());
                } else if (messageToPeer.isSelected()) {
                    String selectedNickname = onlineJList.getSelectedValue();
                    InetAddress findIpByNickname = client.findIpByNickname(selectedNickname);
                    client.sendMessageToPeer(findIpByNickname, outboxTextArea.getText());
                }
            }
        });

        southPanel.add(outboxTextArea);
        southPanel.add(sendButton);

        super.setResizable(false);
        super.setSize(700, 700);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    private void releaseInterface(boolean release) {
        leaveButton.setEnabled(!release);
        joinButton.setEnabled(release);
        nicknameTextField.setEnabled(release);
        portTextField.setEnabled(release);
        groupTextField.setEnabled(release);
        sendButton.setEnabled(!release);
    }

    private void releaseInterface() {
        releaseInterface(true);
    }

    private void blockInterface() {
        releaseInterface(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (client != null) {
            List<String> online = this.client.getOnlineNicknames();
            if (online != null) {
                onlineJList.setListData(online.toArray(new String[online.size()]));
                onlineJList.setSelectedIndex(0);
            } else {
                onlineJList.setListData(new String[0]);
            }
        }
    }

    public static void main(String[] args) {
        new ClientGUI();
    }

}

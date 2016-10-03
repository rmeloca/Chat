/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

/**
 *
 * @author romulo
 */
public class GroupMessageCollector implements Runnable {

    private final Group group;

    public GroupMessageCollector(Group group) {
        this.group = group;
    }

    @Override
    public void run() {
        while (true) {
            Message retrieveMessage = group.retrieveMessage();
            if (retrieveMessage.getTipo().equals(MessageType.LEAVE) && retrieveMessage.getRemetente().equals(group.getSelf())) {
                break;
            }
            System.out.println(retrieveMessage.toString());
        }
    }

}

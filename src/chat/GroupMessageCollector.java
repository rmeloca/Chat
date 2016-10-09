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
            System.out.println(retrieveMessage.toString());
            if (retrieveMessage.getType().equals(MessageType.LEAVE) && retrieveMessage.getSender().equals(group.getSelf())) {
                break;
            }
        }
    }

}

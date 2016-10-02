/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.util.PriorityQueue;

/**
 *
 * @author romulo
 */
public class Buffer {

    private final PriorityQueue<Message> queue;

    public Buffer() {
        this.queue = new PriorityQueue<>();
    }

    public synchronized void add(Message mensagem) {
        queue.add(mensagem);
        notifyAll();
    }

    public synchronized Message poll() throws InterruptedException {
        wait();
        return queue.poll();
    }

}

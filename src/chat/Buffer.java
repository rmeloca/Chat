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

    private final PriorityQueue<Mensagem> queue;

    public Buffer() {
        this.queue = new PriorityQueue<>();
    }

    public synchronized void add(Mensagem mensagem) {
        queue.add(mensagem);
        notifyAll();
    }

    public synchronized Mensagem poll() throws InterruptedException {
        wait();
        return queue.poll();
    }

}

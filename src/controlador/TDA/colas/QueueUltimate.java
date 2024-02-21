/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.TDA.colas;

import controlador.TDA.listas.Exception.EmptyException;
import controlador.TDA.pilas.FullStackException;

/**
 *
 * @author santi
 */
public class QueueUltimate<E> {

    private Queue<E> tail;

    public QueueUltimate(Integer length) {
        this.tail = new Queue(length);
    }

    public void queue(E info) throws EmptyException, FullStackException {
        tail.queue(info);
    }

    public E dequeue() throws EmptyException {
        return tail.dequue();
    }

    public Integer length() {
        return tail.getLength();
    }

    public Boolean isFull() {
        return tail.isFull();
    }

    public void print() {
        System.out.println("QUEUE");
        System.out.println(tail.toString());
        System.out.println("");
    }

    public boolean isEmpty() {
        return tail.getLength() == 0;
    }
}

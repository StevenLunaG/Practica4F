/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.TDA.colas;

import controlador.TDA.pilas.*;
import controlador.TDA.listas.DynamicList;
import controlador.TDA.listas.Exception.EmptyException;

/**
 *
 * @author santi
 */
class Queue<E> extends DynamicList<E>{
    //revisar como hacer una pila infinita
    private Integer cima;

    public Queue(Integer tope) {
        this.cima = tope;
    }
    
    public Boolean isFull(){
        return getLength().intValue() >= cima.intValue();
    }
    
    public void queue(E info)throws EmptyException, FullStackException{
        if (isFull()) {
            throw new FullStackException("Queue Full");
        }else{
            add(info);
        }
    }
    
    public E dequue() throws EmptyException{
        E info = extractFirst();
        return info;
    }
}

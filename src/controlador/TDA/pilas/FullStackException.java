/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package controlador.TDA.pilas;

/**
 *
 * @author santi
 */
public class FullStackException extends Exception{

    /**
     * Creates a new instance of <code>FullStackException</code> without detail
     * message.
     */
    public FullStackException() {
    }

    /**
     * Constructs an instance of <code>FullStackException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FullStackException(String msg) {
        super(msg);
    }
}

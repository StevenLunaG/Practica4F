/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package controlador.TDA.listas.Exception;

/**
 *
 * @author sebastian
 */
public class EmptyException extends Exception {

    /**
     * Creates a new instance of <code>EmptyException</code> without detail
     * message.
     */
    public EmptyException() {
    }

    /**
     * Constructs an instance of <code>EmptyException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public EmptyException(String msg) {
        super(msg);
    }
}

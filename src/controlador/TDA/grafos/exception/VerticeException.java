/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.TDA.grafos.exception;

/**
 *
 * @author Stiven Jimenez
 */
public class VerticeException extends Exception{

    public VerticeException(String msg) {
        super(msg);
    }

    public VerticeException() {
        super("Vertice fuera de rango");
    }
}

package controlador.TDA.grafos.exception;

public class LabelEdgeException extends Exception{

    public LabelEdgeException(String msg) {
        super(msg);
    }

    public LabelEdgeException() {
        super("Grafo no etiquetado completamente");
    }
}

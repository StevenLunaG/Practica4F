package controlador.TDA.grafos;

import controlador.TDA.listas.DynamicList;

public abstract class Grafo {

    // G = {v, e}
    public abstract Integer num_vertice();

    public abstract Integer num_aristas();

    //v1 --- v2
    public abstract Boolean existe_arista(Integer v1, Integer v2) throws Exception;

    public abstract Double peso_arista(Integer v1, Integer v2) throws Exception;

    public abstract void insertar_arista(Integer v1, Integer v2, Double peso)throws Exception;

    public abstract void insertar_arista(Integer v1, Integer v2)throws Exception;

    public abstract DynamicList<Adyacencia> adyacentes(Integer v1);
    //v1 --- nro de adyacentes?
    //G = 20V
    //MA = 20 X 20 ---> grafos grandes

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder("GRAFO").append("\n");
        try {
            for (int i = 1; i <= num_vertice(); i++) {
                grafo.append("V").append(i+"\n");
                DynamicList<Adyacencia> list = adyacentes(i);
                for (int j = 0; j < list.getLength(); j++) {
                    Adyacencia a = list.getInfo(j);
                    grafo.append(" ^--Adyacente ").append(a.getDestino()).append(" (Peso ").append(a.getPeso()).append(")\n");
                }
            }
        } catch (Exception e) {
        }
        return grafo.toString();
    }

    public DynamicList<Adyacencia>[] getListaAdyacencias() {
        return new DynamicList[0];
    }
}

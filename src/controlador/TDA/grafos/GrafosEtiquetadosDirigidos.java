package controlador.TDA.grafos;

import controlador.TDA.colas.QueueUltimate;
import controlador.TDA.grafos.exception.LabelEdgeException;
import controlador.TDA.grafos.exception.VerticeException;
import controlador.TDA.listas.DynamicList;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Random;
import modelo.SubEstacion;
import vista.utiles.UtilesVistaSub;

public class GrafosEtiquetadosDirigidos<E> extends GrafoDirigido {

    private Double[][] distancias;
    protected E[] labels;
    protected HashMap<E, Integer> dicVertices;
    private Class<E> clazz;

    public GrafosEtiquetadosDirigidos(Integer num_vertices, Class clazz) {
        super(num_vertices);
        this.clazz = clazz;
        labels = (E[]) Array.newInstance(clazz, num_vertices + 1);
        dicVertices = new HashMap<>(num_vertices);
    }

    //Metodo que permite recatar el nro de vertice asociado a la etiqueta
    public Integer getVerticeE(E label) throws Exception {
        Integer aux = dicVertices.get(label);
        if (aux != null) {
            return aux;
        } else {
            throw new VerticeException("No se encuentra ese vertice asociado a esa etiqueta");
        }
    }

    public E getLabelE(Integer v) throws Exception {
        if (v <= num_vertice()) {
            return labels[v];
        } else {
            throw new VerticeException("No se encuentra ese vertice");
        }
    }

    public Boolean isEdge(E o, E d) throws Exception {
        if (isAllLabelsGraph()) {
            return existe_arista(getVerticeE(o), getVerticeE(d));
        } else {
            throw new LabelEdgeException();
        }
    }

    public void insertEdgeE(E o, E d, Double weight) throws Exception {
        if (isAllLabelsGraph()) {
            insertar_arista(getVerticeE(o), getVerticeE(d), weight);
        } else {
            throw new LabelEdgeException();
        }
    }

    public void insertEdgeE(E o, E d) throws Exception {
        if (isAllLabelsGraph()) {
            insertar_arista(getVerticeE(o), getVerticeE(d), Double.NaN);
        } else {
            throw new LabelEdgeException();
        }
    }

    public DynamicList<Adyacencia> adjacents(E label) throws Exception {
        if (isAllLabelsGraph()) {
            return adyacentes(getVerticeE(label));
        } else {
            throw new LabelEdgeException();
        }
    }

    //Metodo principal que permite etiquetar grafos
    public void labelVertice(Integer v, E label) {
        labels[v] = label;
        dicVertices.put(label, v);
    }

    public Boolean isAllLabelsGraph() throws Exception {
        Boolean band = true;
        for (int i = 1; i < labels.length; i++) {
            if (labels[i] == null) {
                band = false;
                break;
            }
        }
        return band;
    }

    public void coneccionAleatoria() throws Exception {
        Random rand = new Random();
        boolean[][] conexiones = new boolean[num_vertice() + 1][num_vertice() + 1];

        for (int i = 1; i <= num_vertice(); i++) {
            int numConexiones = rand.nextInt(2) + 2;
            int conexionesRealizadas = 0;

            while (conexionesRealizadas < numConexiones) {
                int nodoDestino = rand.nextInt(num_vertice()) + 1;

                if (nodoDestino != i && !conexiones[i][nodoDestino]) {
                    // Coneccion entre nodo actual con nodo destino
                    Double dist = UtilesVistaSub.calcularDistanciaSub((SubEstacion) getLabelE(i), (SubEstacion) getLabelE(nodoDestino));
                    insertar_arista(i, nodoDestino, dist);

                    // Marcar la conexión realizada
                    conexiones[i][nodoDestino] = true;
                    conexiones[nodoDestino][i] = true;
                    conexionesRealizadas++;
                }
            }
        }
    }

    public String encontrarCaminoMasCorto(int origen, int destino) throws Exception {
        if (distancias[origen][destino] == Double.POSITIVE_INFINITY) {
            return "No hay ruta entre:\n  " + getLabelE(origen) + "  y  " + getLabelE(destino);
        }

        StringBuilder rutaMasCorta = new StringBuilder();
        rutaMasCorta.append("CAMINO MAS CORTO \n  Desde: ").append(getLabelE(origen)).append("\n  Hasta: ").append(getLabelE(destino)).append(":\n\n");

        int nodoActual = origen;
        double sumaPesos = 0.0;
        rutaMasCorta.append(getLabelE(nodoActual));

        while (nodoActual != destino) {
            int siguienteNodo = encontrarVecinoMasCercano(nodoActual, destino);
            if (siguienteNodo == -1) {
                break; // No hay vecinos o no se encontró un camino, salir del bucle
            }

            sumaPesos += distancias[nodoActual][siguienteNodo];
            rutaMasCorta.append(" -> ").append(getLabelE(siguienteNodo));
            nodoActual = siguienteNodo;
        }

        rutaMasCorta.append("\n\n Suma de los pesos: ").append(sumaPesos);

        return rutaMasCorta.toString();
    }

    private int encontrarVecinoMasCercano(int nodoActual, int destino) throws Exception {
        int vecinoMasCercano = -1;
        double distanciaMasCorta = Double.POSITIVE_INFINITY;

        for (int k = 1; k <= num_vertice(); k++) {
            if (nodoActual != k && existe_arista(nodoActual, k) && distancias[k][destino] < distanciaMasCorta) {
                vecinoMasCercano = k;
                distanciaMasCorta = distancias[k][destino];
            }
        }

        return vecinoMasCercano;
    }

    // Recorrido por Anchura
    public boolean bfs(Integer verticeInicial) throws Exception {
        boolean[] visitados = new boolean[num_vertice()];
        auxBfs(verticeInicial, visitados);
        // Verificar si todos los nodos fueron visitados
        for (boolean visitado : visitados) {
            if (!visitado) {
                return false;
            }
        }
        return true;
    }

    private void auxBfs(Integer verticeInicial, boolean[] visitados) throws Exception {
        QueueUltimate<Integer> cola = new QueueUltimate<>(num_vertice());
        visitados[verticeInicial - 1] = true;
        cola.queue(verticeInicial);
        while (!cola.isEmpty()) {
            Integer verticeActual = cola.dequeue();
            System.out.println("Visitando nodo: " + verticeActual);
            DynamicList<Adyacencia> adyacentes = adyacentes(verticeActual);
            for (int i = 0; i < adyacentes.getLength(); i++) {
                Adyacencia adyacente = adyacentes.getInfo(i);
                Integer verticeAdyacente = adyacente.getDestino();
                if (!visitados[verticeAdyacente - 1]) {
                    visitados[verticeAdyacente - 1] = true;
                    cola.queue(verticeAdyacente);
                }
            }
        }
    }

    // Recorrido por Profundidad
    public boolean dfs(Integer verticeInicial) throws Exception {
        boolean[] visitados = new boolean[num_vertice()];
        auxDfs(verticeInicial, visitados);
        // Verificar si todos los nodos fueron visitados
        for (boolean visitado : visitados) {
            if (!visitado) {
                return false;
            }
        }
        return true;
    }

    private void auxDfs(Integer verticeActual, boolean[] visitados) throws Exception {
        visitados[verticeActual - 1] = true;
        System.out.println("Visitando nodo: " + verticeActual);
        DynamicList<Adyacencia> adyacentes = adyacentes(verticeActual);
        for (int i = 0; i < adyacentes.getLength(); i++) {
            Adyacencia adyacente = adyacentes.getInfo(i);
            Integer verticeAdyacente = adyacente.getDestino();
            if (!visitados[verticeAdyacente - 1]) {
                auxDfs(verticeAdyacente, visitados);
            }
        }
    }

    //Algoritmo de Floyd
    public String aplicarAlgoritmoFloydConEtiquetas() throws Exception {
        StringBuilder resultado = new StringBuilder();
        distancias = new Double[num_vertice() + 1][num_vertice() + 1];
        for (int i = 1; i <= num_vertice(); i++) {
            for (int j = 1; j <= num_vertice(); j++) {
                if (i == j) {
                    distancias[i][j] = 0.0;
                } else if (existe_arista(i, j)) {
                    distancias[i][j] = peso_arista(i, j);
                } else {
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        resultado.append("Matriz de distancias con etiquetas despues de aplicar Floyd:\n\n");

        //TABLA
        // Columnas
        resultado.append(String.format("%-40s", ""));
        for (int i = 1; i <= num_vertice(); i++) {
            resultado.append(String.format("%-30s", getLabelE(i)));
        }
        resultado.append("\n");

        for (int i = 1; i <= num_vertice(); i++) {
            // Filas
            resultado.append(String.format("%-40s", getLabelE(i)));

            for (int j = 1; j <= num_vertice(); j++) {

                // Valores
                if (distancias[i][j] == Double.POSITIVE_INFINITY) {
                    resultado.append(String.format("%-30s", "-/-"));
                } else {
                    resultado.append(String.format("%-30.2f", distancias[i][j]));
                }
            }
            resultado.append("\n");
        }
        return resultado.toString();
    }

    //Algoritmo de Bellman-Ford
    public String aplicarAlgoritmoBellmanFord(int nodoOrigen) throws Exception {
        inicializarMatrizDistancias(nodoOrigen);
        StringBuilder resultado = new StringBuilder();
        for (int i = 1; i <= num_vertice() - 1; i++) {
            for (int u = 1; u <= num_vertice(); u++) {
                for (int v = 1; v <= num_vertice(); v++) {
                    if (existe_arista(u, v)) {
                        double pesoUV = peso_arista(u, v);

                        if (distancias[nodoOrigen][u] + pesoUV < distancias[nodoOrigen][v]) {
                            distancias[nodoOrigen][v] = distancias[nodoOrigen][u] + pesoUV;
                        }
                    }
                }
            }
        }
        for (int u = 1; u <= num_vertice(); u++) {
            for (int v = 1; v <= num_vertice(); v++) {
                if (existe_arista(u, v)) {
                    double pesoUV = peso_arista(u, v);

                    if (distancias[nodoOrigen][u] + pesoUV < distancias[nodoOrigen][v]) {
                        throw new Exception("Hay ciclo negativo");
                    }
                }
            }
        }

        resultado.append("Matriz de distancias con etiquetas despues de aplicar Bellman-Ford:\n\n");
        resultado.append(construirRepresentacionMatrizDistancias());

        return resultado.toString();
    }

    private void inicializarMatrizDistancias(int nodoOrigen) throws Exception {
        distancias = new Double[num_vertice() + 1][num_vertice() + 1];
        for (int i = 1; i <= num_vertice(); i++) {
            for (int j = 1; j <= num_vertice(); j++) {
                if (i == j) {
                    distancias[i][j] = 0.0;
                } else if (existe_arista(i, j)) {
                    distancias[i][j] = peso_arista(i, j);
                } else {
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        for (int i = 1; i <= num_vertice(); i++) {
            if (i != nodoOrigen) {
                distancias[nodoOrigen][i] = Double.POSITIVE_INFINITY;
            }
        }
    }

    private String construirRepresentacionMatrizDistancias() throws Exception {
        StringBuilder representacion = new StringBuilder();

        // TABLA
        //Titulos
        representacion.append(String.format("%-40s", ""));
        for (int i = 1; i <= num_vertice(); i++) {
            representacion.append(String.format("%-30s", getLabelE(i)));
        }
        representacion.append("\n");

        //Valores
        for (int i = 1; i <= num_vertice(); i++) {
            representacion.append(String.format("%-40s", getLabelE(i)));
            for (int j = 1; j <= num_vertice(); j++) {
                if (distancias[i][j] == Double.POSITIVE_INFINITY) {
                    representacion.append(String.format("%-30s", "-/-"));
                } else {
                    representacion.append(String.format("%-30.2f", distancias[i][j]));
                }
            }
            representacion.append("\n");
        }

        return representacion.toString();
    }

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder("GRAFO").append("\n");
        try {
            for (int i = 1; i <= num_vertice(); i++) {
                grafo.append("[").append(i).append("] = ").append(getLabelE(i)).append("\n");
                DynamicList<Adyacencia> list = adyacentes(i);
                for (int j = 0; j < list.getLength(); j++) {
                    Adyacencia a = list.getInfo(j);
                    grafo.append(" ^--Adyacente [").append(a.getDestino()).append("] = ").append(getLabelE(a.getDestino())).append(" (Peso: ").append(a.getPeso()).append(")\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
        return grafo.toString();
    }

//    public static void main(String[] args) {
//        try {
//            GrafosEtiquetadosNoDirigidos<String> ged = new GrafosEtiquetadosNoDirigidos(6, String.class);
//            ged.labelVertice(1, "Estefania");
//            ged.labelVertice(2, "Luna");
//            ged.labelVertice(3, "Jimenez");
//            ged.labelVertice(4, "Criollo");
//            ged.labelVertice(5, "Maritza");
//            ged.labelVertice(6, "Nivelo");
//            ged.insertEdgeE("Estefania", "Jimenez", 50.0);
//            System.out.println(ged.toString());
//        } catch (Exception e) {
//            System.out.println("Error main" + e);
//        }
//    }
}

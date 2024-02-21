package controlador.TDA.grafos;

import controlador.TDA.listas.DynamicList;
import java.io.FileWriter;

public class PaintGraph {

    String URL = "d3/grafo.js";

    public void update(Grafo graph) throws Exception {
        String nodes = "";
        String edges = "";
        String paint = "";
        
        nodes += "var nodes = new vis.DataSet([\n";
        for (int i = 1; i <= graph.num_vertice(); i++) {
            nodes += "{id: " + i + ", label: \"Node " + i + "\"},\n";
        }
        nodes += "]);\n\n";

        edges += "var edges = new vis.DataSet([\n";
        for (int i = 1; i <= graph.num_vertice(); i++) {
            DynamicList<Adyacencia> links = graph.adyacentes(i);
            for (int j = 0; j < links.getLength(); j++) {
                Adyacencia ady = links.getInfo(j);
                edges += "{from: " + i + ", to: " + ady.getDestino() + ", label: \"" + ady.getPeso() + "\"},\n";
            }
        }
        edges += "]);\n\n";

        paint += nodes + edges
                +"var container = document.getElementById(\"mynetwork\");\n"
                + "      var data = {\n"
                + "        nodes: nodes,\n"
                + "        edges: edges,\n"
                + "      };\n"
                + "      var options = {};\n"
                + "      var network = new vis.Network(container, data, options);";

        FileWriter load = new FileWriter(URL);
        load.write(paint);
        load.close();
    }
    
        public void update(Grafo graph, GrafosEtiquetadosDirigidos grp) throws Exception {
        String nodes = "";
        String edges = "";
        String paint = "";
        
        nodes += "var nodes = new vis.DataSet([\n";
        for (int i = 1; i <= graph.num_vertice(); i++) {
            nodes += "{id: " + i + ", label: \"" + grp.getLabelE(i) + "\"},\n";
        }
        nodes += "]);\n\n";

        edges += "var edges = new vis.DataSet([\n";
        for (int i = 1; i <= graph.num_vertice(); i++) {
            DynamicList<Adyacencia> links = graph.adyacentes(i);
            for (int j = 0; j < links.getLength(); j++) {
                Adyacencia ady = links.getInfo(j);
                edges += "{from: " + i + ", to: " + ady.getDestino() + ", label: \"" + ady.getPeso() + "\"},\n";
            }
        }
        edges += "]);\n\n";

        paint += nodes + edges
                +"var container = document.getElementById(\"mynetwork\");\n"
                + "      var data = {\n"
                + "        nodes: nodes,\n"
                + "        edges: edges,\n"
                + "      };\n"
                + "      var options = {};\n"
                + "      var network = new vis.Network(container, data, options);";

        FileWriter load = new FileWriter(URL);
        load.write(paint);
        load.close();
    }

}

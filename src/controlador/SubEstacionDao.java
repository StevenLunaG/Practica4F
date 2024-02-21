package controlador;

import controlador.TDA.grafos.GrafosEtiquetadosNoDirigidos;
import controlador.TDA.listas.DynamicList;
import controlador.dao.DaoImplement;
import java.io.FileReader;
import java.io.FileWriter;
import modelo.SubEstacion;

public class SubEstacionDao extends DaoImplement<SubEstacion>{

    private DynamicList<SubEstacion> lista = new DynamicList<>();
    private SubEstacion subEstacion;
    private GrafosEtiquetadosNoDirigidos<SubEstacion> grafo;

    public SubEstacionDao() {
        super(SubEstacion.class);
    }

    public GrafosEtiquetadosNoDirigidos<SubEstacion> getGrafo() throws Exception {
        if (grafo == null) {
            DynamicList<SubEstacion> list = getLista();
            if (!list.isEmpty()) {
                grafo = new GrafosEtiquetadosNoDirigidos<>(list.getLength(), SubEstacion.class);
                for (int i = 0; i < list.getLength(); i++) {
                    grafo.labelVertice((i + 1), list.getInfo(i));
                }
            }
        }
        return grafo;
    }

    public void setGrafo(GrafosEtiquetadosNoDirigidos<SubEstacion> grafo) {
        this.grafo = grafo;
    }

    public DynamicList<SubEstacion> getLista() {
        if (lista.isEmpty()) {
            lista = all();
        }
        return lista;
    }

    public void setLista(DynamicList<SubEstacion> lista) {
        this.lista = lista;
    }

    public SubEstacion getSubEstacion() {
        if (subEstacion == null) {
            subEstacion = new SubEstacion();
        }
        return subEstacion;
    }

    public void loadGraph() throws Exception {
        grafo = (GrafosEtiquetadosNoDirigidos<SubEstacion>) getConection().fromXML(new FileReader("files/grafo.json"));

        lista.reset();
        for (int i = 1; i <= grafo.num_vertice(); i++) {
            lista.add(grafo.getLabelE(i));
        }
    }

    public void setSubEstacion(SubEstacion subEstacion) {
        this.subEstacion = subEstacion;
    }

    public void guardarGrafo() throws Exception {
        getConection().toXML(grafo, new FileWriter("files/grafo.json"));
    }

    public Boolean persist() {
        subEstacion.setId(all().getLength());
        return persist(subEstacion);
    }

}

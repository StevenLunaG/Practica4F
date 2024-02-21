package vista.tabla;

import controlador.TDA.grafos.GrafosEtiquetadosDirigidos;
import controlador.utiles.Utiles;
import javax.swing.table.AbstractTableModel;
import modelo.SubEstacion;

public class ModeloTablaGraph extends AbstractTableModel {

    private GrafosEtiquetadosDirigidos<SubEstacion> grafo;

    @Override
    public int getRowCount() {
        return grafo.num_vertice();
    }

    @Override
    public int getColumnCount() {
        return grafo.num_vertice() + 1;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "SUBESTACION";
        } else {
            try {
                return grafo.getLabelE(column).toString();
            } catch (Exception e) {
                return "";
            }
        }
    }

    public GrafosEtiquetadosDirigidos<SubEstacion> getGrafo() {
        return grafo;
    }

    public void setGrafo(GrafosEtiquetadosDirigidos<SubEstacion> grafo) {
        this.grafo = grafo;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        try {
            if (i1 == 0) {
                return grafo.getLabelE(i + 1).toString();
            } else {
                SubEstacion o = grafo.getLabelE(i + 1);
                SubEstacion d = grafo.getLabelE(i1);
                if (grafo.isEdge(o, d)) {
                    return Utiles.redondear(grafo.peso_arista(i + 1, i1)).toString();
                } else {
                    return "--";
                }
            }
        } catch (Exception e) {
            return "";
        }
    }
}

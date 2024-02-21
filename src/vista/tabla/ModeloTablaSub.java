/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.tabla;

import controlador.TDA.listas.DynamicList;
import javax.swing.table.AbstractTableModel;
import modelo.SubEstacion;

public class ModeloTablaSub extends AbstractTableModel {
    private DynamicList<SubEstacion> subEstaciones = new DynamicList<>();

    public DynamicList<SubEstacion> getSubEstaciones() {
        return subEstaciones;
    }

    public void setSubEstaciones(DynamicList<SubEstacion> subEstaciones) {
        this.subEstaciones = subEstaciones;
    }

    @Override
    public int getRowCount() {
        return subEstaciones.getLength();
    }

    @Override
    public int getColumnCount() {
         return 3;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        SubEstacion s = null;
        try {
            s = subEstaciones.getInfo(i);
        } catch (Exception ex) {
        }
        switch (i1) {
            case 0: return (s != null) ? s.getId() : "";
            case 1: return (s != null) ? s.getGeoPosicion().getLongitud(): "";        
            case 2: return (s != null) ? s.getGeoPosicion().getLatitud(): ""; 
                
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID";
            case 1: return "LONGITUD";
            case 2: return "LATITUD";
            default:
                return null;
        }
    }
}
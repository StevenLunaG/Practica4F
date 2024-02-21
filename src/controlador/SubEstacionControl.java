package controlador;

import controlador.TDA.listas.DynamicList;
import modelo.SubEstacion;

public class SubEstacionControl {

    private SubEstacion subEstacion = new SubEstacion();
    private DynamicList<SubEstacion> subEstaciones;

    public SubEstacionControl(SubEstacion subEstacion) {
        this.subEstacion = subEstacion;
    }

    public SubEstacionControl() {
        this.subEstaciones = new DynamicList<>();
    }

    public Boolean guardar() {
        try {
            getSubEstacion().setId(getSubEstaciones().getLength());
            getSubEstaciones().add(getSubEstacion());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public SubEstacion getSubEstacion() {
        if (subEstacion == null) {
            subEstacion = new SubEstacion();
        }
        return subEstacion;
    }

    public void setSubEstacion(SubEstacion subEstacion) {
        this.subEstacion = subEstacion;
    }

    public DynamicList<SubEstacion> getSubEstaciones() {
        return subEstaciones;
    }

    public void setSubEstaciones(DynamicList<SubEstacion> subEstaciones) {
        this.subEstaciones = subEstaciones;
    }

}

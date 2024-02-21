package modelo;

import controlador.TDA.listas.DynamicList;

public class SubEstacion {
    private Integer id;
    private Coordenada geoPosicion;
    private String[] fotos;

    public SubEstacion() {
    }

    public SubEstacion(Integer id, Coordenada geoPosicion, String[] fotos) {
        this.id = id;
        this.geoPosicion = geoPosicion;
        this.fotos = fotos;
    }

    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coordenada getGeoPosicion() {
        return geoPosicion;
    }

    public void setGeoPosicion(Coordenada geoPosicion) {
        this.geoPosicion = geoPosicion;
    }

    public String[] getFotos() {
        return fotos;
    }

    public void setFotos(String[] fotos) {
        this.fotos = fotos;
    }    
    @Override
    public String toString() {
        return "[" + id + "]" + " " + String.format("%.4f", geoPosicion.getLongitud()) + " | " + String.format("%.4f", geoPosicion.getLatitud());
    }
    
}

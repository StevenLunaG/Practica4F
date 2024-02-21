package controlador.TDA.grafos;
import controlador.TDA.grafos.exception.VerticeException;

public class GrafoNoDirigido extends GrafoDirigido {

    public GrafoNoDirigido(Integer num_vertices) {
        super(num_vertices);
    }

 @Override
    public void insertar_arista(Integer v1, Integer v2, Double peso) throws Exception{
        if (v1.intValue() <= num_vertice() && v2.intValue() <= num_vertice()) {
            if (!existe_arista(v1, v2)) {
                setNum_aristas(num_aristas()+1);
                getListaAdyacencias()[v1].add(new Adyacencia(v2, peso));
                getListaAdyacencias()[v2].add(new Adyacencia(v1, peso));
            }
        }else
            throw new VerticeException();
    }    

     public static void main(String[] args) throws VerticeException {
        Grafo f = new GrafoNoDirigido(6);
        System.out.println(f);
        try {
            f.insertar_arista(1, 3, 50.0);
            System.out.println(f);
            
            PaintGraph p =new PaintGraph();
            p.update(f);
            
        } catch (Exception e) {
            throw new VerticeException();
        }
    }
}


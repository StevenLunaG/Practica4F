package vista.utiles;

import controlador.SubEstacionDao;
import controlador.TDA.listas.DynamicList;
import controlador.utiles.Utiles;
import javax.swing.JComboBox;
import modelo.SubEstacion;

public class UtilesVistaSub {

    public static void cargarComboSub(JComboBox cbx) throws Exception {
        DynamicList<SubEstacion> list = new SubEstacionDao().getLista();
        cbx.removeAllItems();
        for (int i = 0; i < list.getLength(); i++) {
            cbx.addItem(list.getInfo(i));
        }
    }

    public static Double calcularDistanciaSub(SubEstacion o, SubEstacion d) {
        Double dist = Utiles.coordGpsToKm(o.getGeoPosicion().getLatitud(), o.getGeoPosicion().getLongitud(), d.getGeoPosicion().getLatitud(), d.getGeoPosicion().getLongitud());
        return dist;
    }
}

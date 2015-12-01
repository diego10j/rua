package pkg_contabilidad;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_forma_pago extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();

    public pre_forma_pago() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("con_cabece_forma_pago", "ide_cncfp", 1);
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("con_deta_forma_pago", "ide_cndfp", 2);
        tab_tabla2.setCampoForanea("ide_cncfp");
        tab_tabla2.dibujar();

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel2, "50%", "H");

        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        } else {
            tab_tabla.insertar();
        }
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        tab_tabla2.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla.seleccionarFila(evt);
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
}

package pkg_sri;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_tipo_sustento_tributario extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();

    public pre_tipo_sustento_tributario() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("sri_tipo_sustento_tributario", "ide_srtst", 1);
        tab_tabla.setCampoOrden("alterno_srtst");
        tab_tabla.getColumna("nombre_srtst").setControl("AreaTexto");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}

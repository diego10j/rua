package pkg_cuentas_x_cobrar;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_guia extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();

    public pre_guia() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("cxc_guia", "ide_ccgui", 1);
        tab_tabla.getColumna("ide_cctgi").setCombo("cxc_tipo_guia", "ide_cctgi", "nombre_cctgi", "");
        tab_tabla.getColumna("ide_cccfa").setLectura(true);
        tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO' AND es_cliente_geper=TRUE AND ide_empr=" + utilitario.getVariable("ide_empr"));
        tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("fecha_emision_ccgui").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("fecha_emision_ccgui").setLectura(true);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
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

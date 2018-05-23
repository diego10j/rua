package pkg_cuentas_x_cobrar;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_datos_factura_cxc extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    private Tabla tab_tabla1 = new Tabla();

    public pre_datos_factura_cxc() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("con_tipo_document", "ide_cntdo", 2);
        tab_tabla1.getColumna("ide_cntdo").setVisible(false);
        tab_tabla1.getColumna("aplic_tribu_cntdo").setVisible(false);
        tab_tabla1.setRows(15);
        tab_tabla1.onSelect("seleccionarTipoDocumento");
        tab_tabla1.setLectura(true);
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("cxc_datos_fac", "ide_ccdaf", 1);
        tab_tabla.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
        tab_tabla.getColumna("activo_ccdaf").setValorDefecto("true");
        tab_tabla.setCondicionSucursal(true);
        tab_tabla.setCondicion("ide_cntdo is null ");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel, "40%", "H");
        agregarComponente(div_division);

    }

    public void seleccionarTipoDocumento(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla.setCondicion("ide_cntdo=" + tab_tabla1.getValorSeleccionado());
        tab_tabla.ejecutarSql();
    }

    @Override
    public void insertar() {
        if (tab_tabla1.getValorSeleccionado() != null) {
            tab_tabla.insertar();
            tab_tabla.setValor("ide_cntdo", tab_tabla1.getValorSeleccionado());
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            utilitario.getConexion().guardarPantalla();
        }
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

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

}

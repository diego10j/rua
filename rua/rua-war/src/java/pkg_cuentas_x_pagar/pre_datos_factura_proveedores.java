/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_cuentas_x_pagar;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

public class pre_datos_factura_proveedores extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private AutoCompletar aut_filtro_proveedor = new AutoCompletar();
    private Boton bot_clean = new Boton();
    private String proveedor_actual = "-1";

    public pre_datos_factura_proveedores() {



        aut_filtro_proveedor.setId("aut_filtro_proveedor");
        aut_filtro_proveedor.setAutoCompletar("select ide_geper,identificac_geper,nom_geper from gen_persona where es_proveedo_geper is TRUE");
        aut_filtro_proveedor.setMetodoChange("filtrar_por_proveedor");

        bar_botones.agregarComponente(new Etiqueta("Proveedor: "));
        bar_botones.agregarComponente(aut_filtro_proveedor);

        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarComponente(bot_clean);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("cxp_datos_factura", "ide_cpdaf", 1);

        tab_tabla1.setCondicion("ide_cpdaf=-1");
        tab_tabla1.setCampoOrden("ide_cpdaf desc");
        tab_tabla1.getColumna("ide_geper").setVisible(false);

        tab_tabla1.dibujar();
        PanelTabla pat_pane1 = new PanelTabla();
        pat_pane1.setPanelTabla(tab_tabla1);
        pat_pane1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_pane1.getMenuTabla().getItem_insertar().setRendered(false);
        div_division.setId("div_division");
        div_division.dividir1(pat_pane1);
        agregarComponente(div_division);


    }

    public void filtrar_por_proveedor(SelectEvent evt) {

        aut_filtro_proveedor.onSelect(evt);
        proveedor_actual = aut_filtro_proveedor.getValor();
        if (aut_filtro_proveedor.getValor() != null) {


            tab_tabla1.setCondicion("ide_geper=" + proveedor_actual);
            tab_tabla1.ejecutarSql();
            if (tab_tabla1.getTotalFilas() == 0) {
                utilitario.agregarMensajeInfo("Atencion ", "No Existe Facturas del Proveedor Seleccionado");
            }

        } else {
            utilitario.agregarMensajeInfo("Atencion ", "No Ha Seleccionado ningun Proveedor");
        }
    }

    public void limpiar() {
        aut_filtro_proveedor.setValue(null);
        // tab_tabla1.setCondicion("ide_geper=" + proveedor_actual);
        tab_tabla1.limpiar();
        utilitario.addUpdate("aut_filtro_proveedor,tab_tabla1");

    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        if(tab_tabla1.isFilaModificada()){
            tab_tabla1.guardar();
            utilitario.getConexion().guardarPantalla();
        }
        
        
    }

    @Override
    public void eliminar() {
    }

    public AutoCompletar getAut_filtro_proveedor() {
        return aut_filtro_proveedor;
    }

    public void setAut_filtro_proveedor(AutoCompletar aut_filtro_proveedor) {
        this.aut_filtro_proveedor = aut_filtro_proveedor;
    }

    public Boton getBot_clean() {
        return bot_clean;
    }

    public void setBot_clean(Boton bot_clean) {
        this.bot_clean = bot_clean;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Division getDiv_division() {
        return div_division;
    }

    public void setDiv_division(Division div_division) {
        this.div_division = div_division;
    }
}

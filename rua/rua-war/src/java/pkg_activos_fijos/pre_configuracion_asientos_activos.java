/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_activos_fijos;


import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.List;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_configuracion_asientos_activos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();

    public pre_configuracion_asientos_activos() {

        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null) {

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("con_det_conf_asie", "ide_cndca", 1);
            tab_tabla1.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0));
            tab_tabla1.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla1.getColumna("ide_acttr").setCombo("act_tipo_transaccion", "ide_acttr", "nombre_acttr", "");
            tab_tabla1.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "");
            tab_tabla1.getColumna("ide_inarti").setAutoCompletar();
            tab_tabla1.getColumna("ide_cnvca").setVisible(false);
            tab_tabla1.getColumna("ide_intti").setVisible(false);
            tab_tabla1.getColumna("ide_cncim").setVisible(false);
            tab_tabla1.getColumna("ide_cnpim").setVisible(false);            
            tab_tabla1.getColumna("credi_tribu_cndca").setVisible(false);
            tab_tabla1.getColumna("alter_tribu_cndca").setVisible(false);
            tab_tabla1.getColumna("ide_geper").setVisible(false);            
            tab_tabla1.setCondicion("ide_cndca=-1");
            tab_tabla1.setCondicionSucursal(false);
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            div_division.setId("div_division");
            div_division.dividir1(pat_panel1);

            agregarComponente(div_division);
            cargarCabecera();

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }
    
    public boolean validar() {
        if (tab_tabla1.getValor("ide_inarti") == null || tab_tabla1.getValor("ide_inarti").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe seleccionar un articulo");
            return false;
        }
        if (tab_tabla1.getValor("ide_cndpc") == null || tab_tabla1.getValor("ide_cndpc").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe seleccionar una cuenta contable");
            return false;
        }
        if (tab_tabla1.getValor("ide_acttr") == null || tab_tabla1.getValor("ide_acttr").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe seleccionar un tipo de transacción");
            return false;
        }
        return true;
    }
    
String ide_cnvca="";
    public void cargarCabecera() {
        TablaGenerica tab_vigencia = utilitario.consultar("SELECT * FROM con_vig_conf_asie WHERE ide_cncca=" + utilitario.getVariable("p_con_inve_gasto_activo"));
        if (tab_vigencia.getTotalFilas() >= 0) {
            ide_cnvca=tab_vigencia.getValor(0, "ide_cnvca");
            tab_tabla1.setCondicion("ide_cnvca=" + tab_vigencia.getValor(0, "ide_cnvca") + " and ide_acttr is not null ");
            tab_tabla1.ejecutarSql();
        }
    }

    @Override
    public void insertar() {        
        utilitario.getTablaisFocus().insertar();
        tab_tabla1.setValor("ide_cnvca", ide_cnvca);
    }

    @Override
    public void guardar() {
        if (validar()) {
            tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
        }        
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
}

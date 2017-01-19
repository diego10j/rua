/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioRetenciones;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_modificar_retencion extends Pantalla {

    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);
    @EJB
    private final ServicioRetenciones ser_retencion = (ServicioRetenciones) utilitario.instanciarEJB(ServicioRetenciones.class);
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);

    private AutoCompletar aut_proveedor = new AutoCompletar();
    private SeleccionTabla sel_factura = new SeleccionTabla();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private String ide_cpcfa = null;

    public pre_modificar_retencion() {
        bar_botones.quitarBotonsNavegacion();

        aut_proveedor.setId("aut_proveedor");
        aut_proveedor.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_proveedor.setAutocompletarContenido();
        aut_proveedor.setMetodoChange("abrirRetenciones");
        aut_proveedor.setSize(70);

        bar_botones.agregarComponente(new Etiqueta("PROVEEDOR"));
        bar_botones.agregarComponente(aut_proveedor);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("con_cabece_retenc", "ide_cncre", 1);
        tab_tabla1.setCondicion("ide_cncre=-1");
        tab_tabla1.getColumna("ide_cnere").setCombo("con_estado_retenc", "ide_cnere", "nombre_cnere", "");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.getColumna("numero_cncre").setEstilo("font-size: 16px;font-weight: bold");
        tab_tabla1.getColumna("autorizacion_cncre").setEstilo("font-size: 16px;font-weight: bold");
        tab_tabla1.getColumna("autorizacion_cncre").setEstilo("font-size: 16px;font-weight: bold");
        tab_tabla1.getColumna("es_venta_cncre").setVisible(false);
        tab_tabla1.getColumna("numero_cncre").setNombreVisual("NUMERO DE RETENCION");
        tab_tabla1.getColumna("autorizacion_cncre").setNombreVisual("NUMERO DE AUTORIZACION");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("TRANSACCION");
        tab_tabla1.getColumna("fecha_emisi_cncre").setNombreVisual("FECHA DE EMISION");
        tab_tabla1.getColumna("observacion_cncre").setNombreVisual("OBSERVACION");
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("con_detall_retenc", "ide_cndre", 2);
        tab_tabla2.setCampoForanea("ide_cncre");

        tab_tabla2.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
        tab_tabla2.getColumna("ide_cncim").setAutoCompletar();
        tab_tabla2.getColumna("porcentaje_cndre").setMetodoChange("calclularValorRetencion");
        tab_tabla2.getColumna("base_cndre").setMetodoChange("calclularValorRetencion");

        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().getItem_insertar().setRendered(true);
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
        agregarComponente(div_division);

        sel_factura.setId("sel_factura");
        sel_factura.setTitle("RETENCIONES DEL PROVEEDOR");
        sel_factura.setHeight("50%");
        sel_factura.setWidth("65%");
        sel_factura.setRadio();
        sel_factura.setSeleccionTabla(ser_retencion.getSqlRetencionesProveedor("-1"), "ide_cncre");
        sel_factura.getTab_seleccion().getColumna("NUMERO").setFiltroContenido();
        sel_factura.getTab_seleccion().getColumna("ide_cpcfa").setVisible(false);

        sel_factura.getBot_aceptar().setMetodo("aceptarRetencion");
        agregarComponente(sel_factura);

    }

    /**
     * Multiplica la base imponible por el porcentaje de retención
     *
     * @param evt
     */
    public void calclularValorRetencion(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        double dou_val_ret = 0;
        try {
            dou_val_ret = (Double.parseDouble(tab_tabla2.getValor("base_cndre")) * Double.parseDouble(tab_tabla2.getValor("porcentaje_cndre"))) / 100;
        } catch (Exception e) {
        }
        tab_tabla2.setValor("valor_cndre", utilitario.getFormatoNumero(dou_val_ret));
        utilitario.addUpdateTabla(tab_tabla2, "VALOR_CNDRE", "");

    }

    public void limpiar() {
        aut_proveedor.limpiar();
        tab_tabla1.limpiar();
        tab_tabla2.limpiar();
        utilitario.addUpdate("div_division");
    }

    public void aceptarRetencion() {
        if (sel_factura.getValorSeleccionado() != null) {
            sel_factura.cerrar();
            tab_tabla1.setCondicion("ide_cncre=" + sel_factura.getValorSeleccionado());
            ide_cpcfa = sel_factura.getTab_seleccion().getValor("ide_cpcfa");
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Comprobante de Retención", "");
        }
    }

    public void abrirRetenciones(SelectEvent evt) {
        aut_proveedor.onSelect(evt);
        if (aut_proveedor.getValor() != null) {
            sel_factura.setSql(ser_retencion.getSqlRetencionesProveedor(aut_proveedor.getValor()));
            sel_factura.getTab_seleccion().ejecutarSql();
            if (sel_factura.getTab_seleccion().isEmpty() == false) {
                sel_factura.setTitle("COMPROBANTES DE RETENCIÓN - " + aut_proveedor.getValorArreglo(2));
                sel_factura.dibujar();
            } else {
                tab_tabla1.limpiar();
                tab_tabla2.limpiar();
                utilitario.agregarMensaje("El proveedor seleccionado no tiene Comprobantes de Retención", "");
            }
        }
    }

    @Override
    public void insertar() {
        if (tab_tabla1.isEmpty() == false) {
            tab_tabla2.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                ser_cuentas_cxp.generarModificarTransaccionRetencion(ide_cpcfa, tab_tabla2.getSumaColumna("valor_cndre"));
                utilitario.getConexion().guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isEmpty() == false) {
            tab_tabla2.eliminar();
        }
    }

    public boolean validarComprobanteRetencion() {

        if (tab_tabla1.getValor("numero_cncre") == null || tab_tabla1.getValor("numero_cncre").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Comprobante", "Debe ingresar el número de retención");
            return false;
        }

        if (tab_tabla1.getValor("autorizacion_cncre") == null || tab_tabla1.getValor("autorizacion_cncre").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Comprobante", "Debe ingresar el número de autorización");
            return false;
        }

        try {
            if (Integer.parseInt(tab_tabla1.getValor("numero_cncre").substring(6, tab_tabla1.getValor("numero_cncre").length())) != 0) {
                List sql_num_com = utilitario.getConexion().consultar("select 1 from con_cabece_retenc where autorizacion_cncre='" + tab_tabla1.getValor("autorizacion_cncre") + "' and numero_cncre='" + tab_tabla1.getValor("numero_cncre") + "'");
                if (sql_num_com.size() > 0) {
                    utilitario.agregarMensajeError("Error al guardar el Comprobante", "Debe ingresar número de retención ya existe");
                    return false;
                }
            }
        } catch (Exception e) {
            utilitario.agregarMensajeError("Error al guardar el Comprobante", "El número de retención no es válido");
            return false;
        }

        if (tab_tabla2.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe ingresar Detalles al comprobante de retención");
            return false;
        } else {
            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                if (tab_tabla2.getValor(i, "ide_cncim") == null || tab_tabla2.getValor("ide_cncim").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe seleccionar un Impuesto en los Detalles de la retención");
                    return false;
                }
                if (tab_tabla2.getValor(i, "porcentaje_cndre") == null || tab_tabla2.getValor("porcentaje_cndre").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe ingresar el porcentaje de retención en los Detalles de la retención");
                    return false;
                }
                if (tab_tabla2.getValor(i, "base_cndre") == null || tab_tabla2.getValor("base_cndre").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe ingresar la Base Imponible en los Detalles de la retención ");
                    return false;
                }
            }
        }

        return true;
    }

    public AutoCompletar getAut_proveedor() {
        return aut_proveedor;
    }

    public void setAut_proveedor(AutoCompletar aut_proveedor) {
        this.aut_proveedor = aut_proveedor;
    }

    public SeleccionTabla getSel_factura() {
        return sel_factura;
    }

    public void setSel_factura(SeleccionTabla sel_factura) {
        this.sel_factura = sel_factura;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

}

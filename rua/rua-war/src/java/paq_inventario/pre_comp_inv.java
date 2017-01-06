/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_comp_inv extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private SeleccionArbol sel_arbol = new SeleccionArbol();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private final Texto tex_num_transaccion = new Texto();
    private final Boton bot_buscar_transaccion = new Boton();

    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);

    public pre_comp_inv() {
        //Recuperar el plan de cuentas activo

        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarReporte();

        tex_num_transaccion.setId("tex_num_transaccion");
        tex_num_transaccion.setSoloEnteros();
        bot_buscar_transaccion.setTitle("Buscar");
        bot_buscar_transaccion.setIcon("ui-icon-search");
        bot_buscar_transaccion.setMetodo("buscarTransaccion");
        bar_botones.agregarComponente(new Etiqueta("NUM. COMPROBANTE DE INVENTARIO: "));
        bar_botones.agregarComponente(tex_num_transaccion);
        bar_botones.agregarBoton(bot_buscar_transaccion);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_cab_comp_inve", "ide_incci", 1);
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");

        tab_tabla1.getColumna("ide_usua").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_geper").setRequerida(true);
        tab_tabla1.getColumna("ide_inepi").setCombo("inv_est_prev_inve", "ide_inepi", "nombre_inepi", "");
        tab_tabla1.getColumna("ide_intti").setCombo("select ide_intti,nombre_intti,nombre_intci from inv_tip_tran_inve a\n"
                + "inner join inv_tip_comp_inve b on a.ide_intci=b.ide_intci\n"
                + "order by nombre_intci desc, nombre_intti");
        //tab_tabla1.getColumna("ide_intti").setMetodoChange("cambiaTipoTransaccion");
        tab_tabla1.getColumna("ide_intti").setRequerida(true);
        tab_tabla1.getColumna("ide_inbod").setCombo("inv_bodega", "ide_inbod", "nombre_inbod", "nivel_inbod='HIJO'");
        tab_tabla1.getColumna("ide_inbod").setRequerida(true);
        tab_tabla1.getColumna("ide_inepi").setValorDefecto(utilitario.getVariable("p_inv_estado_normal"));
        tab_tabla1.getColumna("fecha_trans_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_siste_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("hora_sistem_incci").setValorDefecto(utilitario.getHoraActual());
        tab_tabla1.getColumna("numero_incci").setLectura(true);
        tab_tabla1.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
        tab_tabla1.getColumna("observacion_incci").setRequerida(true);
        tab_tabla1.getColumna("observacion_incci").setControl("AreaTexto");
        tab_tabla1.getColumna("sis_ide_usua").setVisible(false);
        tab_tabla1.getColumna("fecha_siste_incci").setVisible(false);
        tab_tabla1.getColumna("hora_sistem_incci").setVisible(false);
        tab_tabla1.getColumna("fec_cam_est_incci").setVisible(false);
        tab_tabla1.getColumna("fecha_efect_incci").setVisible(false);
        tab_tabla1.getColumna("ide_cnccc").setLink();
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.setCondicion("ide_incci=-1");
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("inv_det_comp_inve", "ide_indci", 2);
        tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "");
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("cantidad1_indci").setVisible(false);
        tab_tabla2.getColumna("ide_inarti").setMetodoChange("cargarPrecio");
        tab_tabla2.getColumna("cantidad_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla2.getColumna("precio_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla2.getColumna("cantidad_indci").setRequerida(true);
        tab_tabla2.getColumna("precio_indci").setRequerida(true);
//        tab_tabla2.getColumna("ide_inarti").setRequerida(true);
        tab_tabla2.getColumna("valor_indci").setRequerida(true);
        tab_tabla2.getColumna("valor_indci").setEtiqueta();
        tab_tabla2.getColumna("valor_indci").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("referencia_indci").setVisible(false);
        tab_tabla2.getColumna("referencia1_indci").setVisible(false);
        tab_tabla2.setRows(10);
        tab_tabla2.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "numero_cpcfa", "ide_cpcfa=-1");
        tab_tabla2.getColumna("ide_cpcfa").setLectura(true);
        tab_tabla2.getColumna("ide_cccfa").setCombo("cxc_cabece_factura", "ide_cccfa", "secuencial_cccfa", "ide_cccfa=-1");
        tab_tabla2.getColumna("ide_cccfa").setLectura(true);
        tab_tabla2.getColumna("precio_promedio_indci").setVisible(false);

        // tab_tabla2.getColumna("ide_cpdfa").setVisible(false);
        // tab_tabla2.getColumna("ide_cndcc").setVisible(false); 
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.dividir2(pat_panel1, pat_panel2, "40%", "H");
        agregarComponente(div_division);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.setMultiple(false);
        agregarComponente(sec_rango_reporte);
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        rep_reporte.setId("rep_reporte");
        agregarComponente(rep_reporte);
        sel_arbol.setId("sel_arbol");
        sel_arbol.setSeleccionArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");
        sel_arbol.getArb_seleccion().setCondicion("ide_inarti=" + utilitario.getVariable("p_inv_articulo_bien"));
        sel_arbol.getArb_seleccion().setOptimiza(true);
        agregarComponente(sel_arbol);
        sel_arbol.getBot_aceptar().setMetodo("aceptarReporte");

        sel_tab.setId("sel_tab");
        sel_tab.setSeleccionTabla("SELECT ide_inbod,nombre_inbod FROM inv_bodega WHERE nivel_inbod='HIJO' AND ide_empr=" + utilitario.getVariable("ide_empr"), "ide_inbod");
        agregarComponente(sel_tab);
        sel_tab.getBot_aceptar().setMetodo("aceptarReporte");

    }

    public void buscarTransaccion() {
        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {
            tab_tabla1.setCondicion("ide_incci=" + tex_num_transaccion.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            tab_tabla2.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "numero_cpcfa", "ide_cpcfa=" + tab_tabla2.getValor("ide_cpcfa"));
            tab_tabla2.getColumna("ide_cccfa").setCombo("cxc_cabece_factura", "ide_cccfa", "secuencial_cccfa", "ide_cccfa=" + tab_tabla2.getValor("ide_cccfa"));
            utilitario.addUpdate("tab_tabla1,tab_tabla2");
        }
    }

    public void calcularTotalDetalles(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularDetalles();
    }

    public void cargarPrecio(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        List<Double> lisSaldos = ser_producto.getSaldoPromedioProductoBodega(tab_tabla2.getValor("ide_inarti"), utilitario.getFechaActual(), tab_tabla1.getValor("ide_inbod"));
        double dou_precioi = lisSaldos.get(1);
        tab_tabla2.setValor("precio_indci", utilitario.getFormatoNumero(dou_precioi));
        utilitario.addUpdateTabla(tab_tabla2, "precio_indci", "");
        double dou_existencia = ser_producto.getCantidadProductoBodega(tab_tabla2.getValor("ide_inarti"), tab_tabla1.getValor("ide_inbod"));
        if (dou_existencia <= 0) {
            utilitario.agregarMensajeError("No hay existencia de " + tab_tabla2.getValorArreglo("ide_inarti", 1) + " en Bodega", "");
        }
    }

    private void calcularDetalles() {
        double dou_cantidad = 0;
        double dou_precio = 0;
        double dou_valor = 0;
        try {
            dou_cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_indci"));
            dou_precio = Double.parseDouble(tab_tabla2.getValor("precio_indci"));
        } catch (Exception e) {
            dou_cantidad = 0;
            dou_precio = 0;
        }
        double dou_existencia = ser_producto.getCantidadProductoBodega(tab_tabla2.getValor("ide_inarti"), tab_tabla1.getValor("ide_inbod"));
        if (dou_cantidad > dou_existencia) {
            utilitario.agregarMensajeError("La cantidad ingresada es mayor a la existencia en Inventario", "Existencia actual de " + tab_tabla2.getValorArreglo("ide_inarti", 1) + " es :" + utilitario.getFormatoNumero(dou_existencia));
        }

        dou_valor = dou_cantidad * dou_precio;
        tab_tabla2.setValor("valor_indci", utilitario.getFormatoNumero(dou_valor));
        tab_tabla2.sumarColumnas();
        utilitario.addUpdateTabla(tab_tabla2, "valor_indci", "");
    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
            tab_tabla2.sumarColumnas();
        }
    }

    @Override
    public void guardar() {
        if (validar()) {
            if (tab_tabla1.isFilaInsertada()) {
                tab_tabla1.setValor("numero_incci", ser_inventario.getSecuencialComprobanteInventario(String.valueOf(tab_tabla1.getValor("ide_inbod"))));
            }
            if (tab_tabla1.guardar()) {
                if (tab_tabla2.guardar()) {
                    utilitario.getConexion().guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
        }
        if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
            tab_tabla2.sumarColumnas();
        }
    }

    public boolean validar() {
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar un Beneficiario");
            return false;
        }
        if (tab_tabla1.getValor("ide_intti") == null || tab_tabla1.getValor("ide_intti").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe seleccionar un tipo de transacción");
            return false;
        }
        if (tab_tabla1.getValor("fecha_trans_incci") == null || tab_tabla1.getValor("fecha_trans_incci").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe ingresar la fecha de la transacción");
            return false;
        }
        if (tab_tabla1.getValor("observacion_incci") == null || tab_tabla1.getValor("observacion_incci").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar la observación");
            return false;
        }
        if (tab_tabla2.getTotalFilas() == 0) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe insertar información en el detalle");
            return false;
        }
//        if (tab_tabla2.getValor("ide_inarti") == null || tab_tabla2.getValor("ide_inarti").isEmpty()) {
//            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar un articulo");
//            return false;
//        }
//        if (tab_tabla2.getValor("cantidad_indci") == null || tab_tabla2.getValor("cantidad_indci").isEmpty()) {
//            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la cantidad");
//            return false;
//        }
//        if (tab_tabla2.getValor("precio_indci") == null || tab_tabla2.getValor("precio_indci").isEmpty()) {
//            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el precio");
//            return false;
//        }
        return true;
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    private Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Comprobante de Inventario")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_incci", Long.parseLong(tab_tabla1.getValor("ide_incci")));
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sef_formato");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Saldos de Productos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else if (sel_tab.isVisible()) {
                sel_tab.cerrar();
                parametro.put("ide_inbod", sel_tab.getSeleccionados());//lista sel
                System.out.println("seleccion..de tabla..." + sel_tab.getSeleccionados());
                sel_arbol.dibujar();
                //sec_rango_reporte.dibujar();
                utilitario.addUpdate("sel_tab,sel_arbol");
            } else if (sel_arbol.isVisible()) {
                parametro.put("ide_inarti", sel_arbol.getSeleccionados());
                System.out.println("seleccion..de arbol..." + sel_arbol.getSeleccionados());
                sel_arbol.cerrar();
                sec_rango_reporte.setMultiple(false);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("sel_arbol,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_trans_incci", sec_rango_reporte.getFecha1());
                sec_rango_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("sef_formato,sec_rango_reporte");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Kardex")) {
            System.out.println("Si entra al kardex");
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else if (sel_tab.isVisible()) {
                sel_tab.cerrar();
                parametro.put("ide_inbod", sel_tab.getSeleccionados());
                System.out.println("seleccion..de tabla..." + sel_tab.getSeleccionados());
                sel_arbol.dibujar();
                //sec_rango_reporte.dibujar();
                utilitario.addUpdate("sel_tab,sel_arbol");
            } else if (sel_arbol.isVisible()) {
                parametro.put("ide_inarti", sel_arbol.getSeleccionados());
                System.out.println("seleccion..de arbol..." + sel_arbol.getSeleccionados());
                sel_arbol.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("sel_arbol,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("sef_formato,sec_rango_reporte");
            }
        }
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

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public SeleccionArbol getSel_arbol() {
        return sel_arbol;
    }

    public void setSel_arbol(SeleccionArbol sel_arbol) {
        this.sel_arbol = sel_arbol;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
    }

}

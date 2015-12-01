/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import pkg_contabilidad.VistaAsiento;
import pkg_contabilidad.cls_cab_comp_cont;
import pkg_contabilidad.cls_contabilidad;
import pkg_contabilidad.cls_det_comp_cont;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_comprobante_inv extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Division div_division = new Division();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Boton bot_ver_saldo = new Boton();
    private Dialogo dia_saldo_producto = new Dialogo();
    private Combo com_bodega = new Combo();
    private AutoCompletar aut_pruducto = new AutoCompletar();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private SeleccionArbol sel_arbol = new SeleccionArbol();
    private String p_con_tipo_comprobante_diario = utilitario.getVariable("p_con_tipo_comprobante_diario");
    private String p_est_com_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private Dialogo dia_comprobante_contabilidad = new Dialogo();
    private String p_modulo = "9";
    private Etiqueta eti_total_detalle = new Etiqueta();
    private double dou_total_detalle = 0;
    private String str_inv_estado_normal = utilitario.getVariable("p_inv_estado_normal");
    private cls_contabilidad conta = new cls_contabilidad();
    private cls_cab_comp_cont cab_com_con;
    private List<cls_det_comp_cont> lista_detalles = new ArrayList();
    private cls_inventario in = new cls_inventario();
    private VistaAsiento via_asiento = new VistaAsiento();
    private String p_bienes = utilitario.getVariable("p_inv_articulo_bien");
    private AutoCompletar aut_num_factura = new AutoCompletar();
    private AutoCompletar aut_serie_factura = new AutoCompletar();
    private Boton bot_clean = new Boton();

    public pre_comprobante_inv() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {

            bar_botones.agregarReporte();
            bar_botones.getBot_inicio().setMetodo("inicio");
            bar_botones.getBot_fin().setMetodo("fin");
            bar_botones.getBot_siguiente().setMetodo("siguiente");
            bar_botones.getBot_atras().setMetodo("atras");
            bar_botones.agregarComponente(new Etiqueta("Bodega:"));
            bar_botones.agregarComponente(com_bodega);
            bar_botones.agregarComponente(bot_ver_saldo);
            bar_botones.agregarCalendario();
            bot_ver_saldo.setValue("Ver Saldo");
            bot_ver_saldo.setMetodo("dibujar_panel_saldo");

            aut_num_factura.setId("aut_num_factura");
            aut_num_factura.setAutoCompletar("select cf.ide_cpcfa,cf.ide_cnccc,gp.identificac_geper,gp.nom_geper,cf.numero_cpcfa from cxp_cabece_factur cf "
                    + "left join gen_persona gp on cf.ide_geper=gp.ide_geper "
                    + "where cf.ide_empr=" + utilitario.getVariable("ide_empr") + " and cf.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            aut_num_factura.setMetodoChange("buscarfacturaproveedor");
            bar_botones.agregarComponente(new Etiqueta("Factura Proveedor:"));
            bar_botones.agregarComponente(aut_num_factura);

            aut_serie_factura.setId("aut_serie_factura");
            aut_serie_factura.setAutoCompletar("select cf.ide_cccfa,cf.ide_cnccc,gp.identificac_geper,gp.nom_geper,cf.secuencial_cccfa from cxc_cabece_factura cf "
                    + "left join gen_persona gp on  cf.ide_geper=gp.ide_geper "
                    + " where cf.ide_empr=" + utilitario.getVariable("ide_empr") + " and cf.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            aut_serie_factura.setMetodoChange("buscarfactura");
            bar_botones.agregarComponente(new Etiqueta("Serie Factura:"));
            bar_botones.agregarComponente(aut_serie_factura);

            bot_clean.setIcon("ui-icon-cancel");
            bot_clean.setTitle("Limpiar");
            bot_clean.setMetodo("limpiar");
            bar_botones.agregarComponente(bot_clean);

            com_bodega.setId("com_bodega");
            com_bodega.setCombo("select ide_inbod,nombre_inbod from inv_bodega where nivel_inbod='HIJO' and ide_empr=" + utilitario.getVariable("ide_empr"));
            com_bodega.setMetodo("cargar_comprobantes_inv");

            sec_rango_reporte.setId("sec_rango_reporte");
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");

            sel_tab.setId("sel_tab");
            sel_tab.setSeleccionTabla("SELECT ide_inbod,nombre_inbod FROM inv_bodega WHERE nivel_inbod='HIJO' AND ide_empr=" + utilitario.getVariable("ide_empr"), "ide_inbod");
            agregarComponente(sel_tab);
            sel_tab.getBot_aceptar().setMetodo("aceptarReporte");

            sel_arbol.setId("sel_arbol");
            sel_arbol.setSeleccionArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");
            sel_arbol.getArb_seleccion().setCondicion("ide_inarti=" + utilitario.getVariable("p_inv_articulo_bien"));
            agregarComponente(sel_arbol);
            sel_arbol.getBot_aceptar().setMetodo("aceptarReporte");

            sec_rango_reporte.setId("sec_rango_reporte");
            sec_rango_reporte.setMultiple(false);
            agregarComponente(sec_rango_reporte);

            sec_calendario.setId("sec_calendario");
            //por defecto friltra un mes
            sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -30));
            sec_calendario.setFecha2(utilitario.getDate());
            agregarComponente(sec_calendario);
            sec_calendario.getBot_aceptar().setMetodo("aceptarRango");

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("inv_cab_comp_inve", "ide_incci", 1);
            tab_tabla1.onSelect("seleccionar_tabla1");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_geper").setRequerida(true);
            tab_tabla1.getColumna("ide_inepi").setCombo("inv_est_prev_inve", "ide_inepi", "nombre_inepi", "");
            tab_tabla1.getColumna("ide_intti").setCombo("inv_tip_tran_inve", "ide_intti", "nombre_intti", "");
            tab_tabla1.getColumna("ide_intti").setMetodoChange("cambiaTipoTransaccion");
            tab_tabla1.getColumna("ide_intti").setRequerida(true);
            tab_tabla1.getColumna("ide_inbod").setCombo("inv_bodega", "ide_inbod", "nombre_inbod", "nivel_inbod='HIJO'");
            tab_tabla1.getColumna("ide_inepi").setValorDefecto(str_inv_estado_normal);
            tab_tabla1.getColumna("fecha_trans_incci").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_siste_incci").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_sistem_incci").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("observacion_incci").setRequerida(true);
            tab_tabla1.getColumna("hora_sistem_incci").setLectura(true);
            tab_tabla1.getColumna("ide_inbod").setVisible(false);
            tab_tabla1.getColumna("sis_ide_usua").setVisible(false);
            tab_tabla1.getColumna("fecha_siste_incci").setVisible(false);
            tab_tabla1.getColumna("hora_sistem_incci").setVisible(false);
            tab_tabla1.getColumna("fec_cam_est_incci").setVisible(false);
            tab_tabla1.getColumna("fecha_efect_incci").setVisible(false);

////            tab_tabla1.setRows(10);
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(4);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.setCondicion("ide_inbod=-1");
            tab_tabla1.getColumna("numero_incci").setMascara("9999999999");
            tab_tabla1.setRecuperarLectura(true);
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("inv_det_comp_inve", "ide_indci", 2);
            tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "");
            tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
            tab_tabla2.getColumna("cantidad1_indci").setVisible(false);
            tab_tabla2.getColumna("ide_inarti").setMetodoChange("cargarPrecioUnitarioArticulo");
            tab_tabla2.getColumna("cantidad_indci").setMetodoChange("calcularTotalDetalles");
            tab_tabla2.getColumna("precio_indci").setMetodoChange("calcularTotalDetalles");
            tab_tabla2.getColumna("cantidad_indci").setRequerida(true);
            tab_tabla2.getColumna("precio_indci").setRequerida(true);
            tab_tabla2.getColumna("ide_inarti").setRequerida(true);
            tab_tabla2.getColumna("valor_indci").setRequerida(true);
            tab_tabla2.getColumna("valor_indci").setEtiqueta();
            tab_tabla2.getColumna("valor_indci").setEstilo("font-size:13px;font-weight: bold;");
//            tab_tabla2.getColumna("cantidad_indci").setMetodoChange("calcularTotalDetalles");
//            tab_tabla2.getColumna("precio_indci").setMetodoChange("calcularTotalDetalles");
            tab_tabla2.getColumna("referencia_indci").setVisible(false);
            tab_tabla2.getColumna("referencia1_indci").setVisible(false);
            tab_tabla2.setRows(10);
            tab_tabla2.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "numero_cpcfa", "");
            tab_tabla2.getColumna("ide_cpcfa").setNombreVisual("NUMERO FACTURA");
            tab_tabla2.getColumna("ide_cpcfa").setLectura(true);
            tab_tabla2.getColumna("ide_cccfa").setCombo("cxc_cabece_factura", "ide_cccfa", "secuencial_cccfa", "");
            tab_tabla2.getColumna("ide_cccfa").setNombreVisual("SECUENCIA FACTURA");
            tab_tabla2.getColumna("ide_cccfa").setLectura(true);
            tab_tabla2.getColumna("precio_promedio_indci").setLectura(true);

            // tab_tabla2.getColumna("ide_cpdfa").setVisible(false);
            // tab_tabla2.getColumna("ide_cndcc").setVisible(false);
            tab_tabla2.setCampoForanea("ide_incci");
            tab_tabla2.setRecuperarLectura(true);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            Division div_aux = new Division();
            div_aux.setFooter(pat_panel2, eti_total_detalle, "90%");

            div_division.setId("div_division");
            div_division.dividir2(pat_panel1, div_aux, "50%", "H");

            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sef_formato.setId("sef_formato");

            tab_tabla3.setId("tab_tabla3");
            cls_inventario inv = new cls_inventario();
            tab_tabla3.setSql(inv.obtener_saldo("1", "-1", "2012-11-15"));
            tab_tabla3.setCampoPrimaria("ide_inarti");
            tab_tabla3.setLectura(true);
            tab_tabla3.dibujar();

            via_asiento.setId("via_asiento");
            via_asiento.getBot_aceptar().setMetodo("aceptarVistaAsiento");
            via_asiento.getBot_cancelar().setMetodo("cancelarDialogo");
            agregarComponente(via_asiento);

            aut_pruducto.setId("aut_pruducto");
            aut_pruducto.setAutoCompletar("select ide_inarti,nombre_inarti from inv_articulo where nivel_inarti='HIJO' AND ide_empr=" + utilitario.getVariable("ide_empr"));
            aut_pruducto.setMetodoChange("filtrar_saldo_producto");

            Grid grid_autocom = new Grid();
            grid_autocom.setColumns(2);
            grid_autocom.getChildren().add(new Etiqueta("Producto "));
            grid_autocom.getChildren().add(aut_pruducto);

            Grid grid_saldo = new Grid();
            grid_saldo.getChildren().add(grid_autocom);
            grid_saldo.getChildren().add(tab_tabla3);

            dia_saldo_producto.setId("dia_saldo_producto");
            dia_saldo_producto.setWidth("35%");
            dia_saldo_producto.setHeight("40%");
            dia_saldo_producto.setHeader("Tabla Saldo de Producto");
            dia_saldo_producto.getBot_aceptar().setRendered(false);
            dia_saldo_producto.setDialogo(grid_saldo);
            grid_saldo.setStyle("width:" + (dia_saldo_producto.getAnchoPanel() - 5) + "px;height:" + dia_saldo_producto.getAltoPanel() + "px;overflow: auto;display: block;");

            eti_total_detalle.setId("eti_total_detalle");
            eti_total_detalle.setValue("TOTAL: 0");
            eti_total_detalle.setStyle("font-size: 14px;font-weight: bold");

            agregarComponente(div_division);
            agregarComponente(sef_formato);
            agregarComponente(rep_reporte);
            agregarComponente(dia_saldo_producto);
            agregarComponente(dia_comprobante_contabilidad);

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void buscarfacturaproveedor(SelectEvent evt) {
        aut_num_factura.onSelect(evt);
        if (aut_serie_factura.getValue() != null) {
            aut_serie_factura.setValue(null);
            utilitario.addUpdate("aut_serie_factura");
        }
        TablaGenerica tab_ide_incci = utilitario.consultar("SELECT dci.ide_incci,cci.ide_inbod FROM inv_det_comp_inve dci "
                + "LEFT JOIN inv_cab_comp_inve cci ON dci.ide_incci=cci.ide_incci "
                + " WHERE ide_cpcfa=" + aut_num_factura.getValor());
        if (tab_ide_incci.getTotalFilas() > 0) {
            if (tab_ide_incci.getValor(0, "ide_incci") != null && !tab_ide_incci.getValor(0, "ide_incci").isEmpty()) {
                tab_tabla1.setCondicion("ide_incci=" + tab_ide_incci.getValor(0, "ide_incci"));
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
                com_bodega.setValue(tab_ide_incci.getValor(0, "ide_inbod"));
                utilitario.addUpdate("tab_tabla1,tab_tabla2,com_bodega");
            } else {
                utilitario.agregarMensajeInfo("No existe el numero de factura", "Debe seleccionar un número correcto");
            }
        } else {
            tab_tabla1.setCondicion("ide_incci=-1");
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            com_bodega.setValue(null);
            utilitario.addUpdate("tab_tabla1,tab_tabla2,com_bodega");
            utilitario.agregarMensajeInfo("No existe el numero de factura", "Debe seleccionar un número correcto");
        }
    }

    public void buscarfactura(SelectEvent evt) {
        aut_serie_factura.onSelect(evt);
        if (aut_num_factura.getValue() != null) {
            aut_num_factura.setValue(null);
            utilitario.addUpdate("aut_num_factura");
        }
        TablaGenerica tab_ide_incci = utilitario.consultar("SELECT dci.ide_incci,cci.ide_inbod FROM inv_det_comp_inve dci "
                + "LEFT JOIN inv_cab_comp_inve cci ON dci.ide_incci=cci.ide_incci "
                + " WHERE ide_cccfa=" + aut_serie_factura.getValor());
        if (tab_ide_incci.getTotalFilas() > 0) {
            if (tab_ide_incci.getValor(0, "ide_incci") != null && !tab_ide_incci.getValor(0, "ide_incci").isEmpty()) {
                tab_tabla1.setCondicion("ide_incci=" + tab_ide_incci.getValor(0, "ide_incci"));
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
                com_bodega.setValue(tab_ide_incci.getValor(0, "ide_inbod"));
                utilitario.addUpdate("tab_tabla1,tab_tabla2,com_bodega");
            } else {
                utilitario.agregarMensajeInfo("No existe el numero de factura", "Debe seleccionar un número correcto");
            }
        } else {
            tab_tabla1.setCondicion("ide_incci=-1");
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            com_bodega.setValue(null);
            utilitario.addUpdate("tab_tabla1,tab_tabla2,com_bodega");
            utilitario.agregarMensajeInfo("No existe el numero de factura", "Debe seleccionar un número correcto");
        }
    }

    public void cambiaTipoTransaccion() {
        System.out.print("camniar combo transaccion");
        tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_incci"));

    }

    public void cargar_comprobantes_inv() {
        if (com_bodega.getValue() != null) {
            tab_tabla1.setCondicion("fecha_trans_incci between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and ide_inbod=" + com_bodega.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            calcularTotalDetalleFinal();
            if (tab_tabla1.getTotalFilas() <= 0) {
                utilitario.agregarMensajeInfo("Comprobantes de Inventario", "La bodega seleccionada no tiene comprobantes");
            }
        } else {
            tab_tabla1.limpiar();
        }
    }

    public void aceptarRango() {
        if (sec_calendario.isFechasValidas()) {
            sec_calendario.cerrar();
            tab_tabla1.setCondicion("fecha_trans_incci between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and ide_inbod=" + com_bodega.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            utilitario.addUpdate("sec_calendario,tab_tabla1,tab_tabla2");
            calcularTotalDetalleFinal();
            utilitario.addUpdate("eti_total_detalle");
        } else {
            utilitario.agregarMensajeInfo("Fechas no válidas", "");
        }
    }

    @Override
    public void abrirRangoFecha() {
        if (com_bodega.getValue() != null) {
            sec_calendario.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo de comprobante", "");
        }
    }

    public void cargarPrecioUnitarioArticulo() {
        cls_inventario inv = new cls_inventario();
        String str_bodega = com_bodega.getValue().toString();
        String ide_intci = inv.getParametroTablaTipoTransaccion(tab_tabla1.getValor("ide_intti"), "ide_intci");
        String signo = inv.getParametroTablaTipoComprobante(ide_intci, "signo_intci");
        if (signo.equals("-1")) {
            String precio_unitario = inv.getPrecioPromedioTransaccionNegativa(tab_tabla2.getValor("ide_inarti"), str_bodega);
            if (precio_unitario != null) {
                double dou_valor_promedio = Double.parseDouble(precio_unitario);
                tab_tabla2.setValor("precio_indci", utilitario.getFormatoNumero(dou_valor_promedio, 3) + "");
                tab_tabla2.setValor("precio_promedio_indci", utilitario.getFormatoNumero(dou_valor_promedio, 3) + "");
                utilitario.addUpdateTabla(tab_tabla2, "precio_indci,precio_promedio_indci", "");
            }
        } else {
            try {
                String precio_pro = inv.getPrecioPromedioTransaccionPositiva(tab_tabla2.getValor("ide_inarti"), com_bodega.getValue() + "", Double.parseDouble(tab_tabla2.getValor("valor_indci")), Double.parseDouble(tab_tabla2.getValor("cantidad_indci")));
                double dou_valor_promedio = Double.parseDouble(precio_pro);
                tab_tabla2.setValor("precio_promedio_indci", utilitario.getFormatoNumero(dou_valor_promedio, 3) + "");
                utilitario.addUpdateTabla(tab_tabla2, "precio_promedio_indci", "");
            } catch (Exception e) {
            }
        }
    }

    public void calcularTotalDetalles(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotalDetalles1();
        cargarPrecioUnitarioArticulo();
    }

    public void calcularTotalDetalles1() {
        double dou_cantidad = 0;
        double dou_precio = 0;
        double dou_valor = 0;
        try {
            dou_cantidad = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_indci"));
            dou_precio = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_indci"));
        } catch (Exception e) {
            dou_cantidad = 0;
            dou_precio = 0;
        }
        dou_valor = dou_cantidad * dou_precio;
        tab_tabla2.setValor(tab_tabla2.getFilaActual(), "valor_indci", utilitario.getFormatoNumero(dou_valor, 2));
        utilitario.addUpdateTabla(tab_tabla2, "valor_indci", "");
        calcularTotalDetalleFinal();
        validarIngresoCantidad();
        cargarPrecioUnitarioArticulo();

    }

    public void calcularTotalDetalleFinal() {
        eti_total_detalle.setValue("TOTAL: " + utilitario.getFormatoNumero(tab_tabla2.getSumaColumna("valor_indci"), 2));
        System.out.println("calcular detalles " + utilitario.getFormatoNumero(tab_tabla2.getSumaColumna("valor_indci"), 2));
        utilitario.addUpdate("eti_total_detalle");
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
        if (tab_tabla2.getValor("ide_inarti") == null || tab_tabla2.getValor("ide_inarti").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar un articulo");
            return false;
        }
        if (tab_tabla2.getValor("cantidad_indci") == null || tab_tabla2.getValor("cantidad_indci").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la cantidad");
            return false;
        }
        if (tab_tabla2.getValor("precio_indci") == null || tab_tabla2.getValor("precio_indci").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el precio");
            return false;
        }
        return true;
    }

    public void validarIngresoCantidad() {
        cls_inventario inv = new cls_inventario();
        tab_tabla3.setSql(inv.obtener_saldo(com_bodega.getValue().toString(), tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti"), utilitario.getFechaActual()));
        tab_tabla3.ejecutarSql();
        try {
            String str_saldo_arti = tab_tabla3.getValor("saldo");
            double dou_saldo_arti = Double.parseDouble(str_saldo_arti);
            String str_cantidad = tab_tabla2.getValor("cantidad_indci");
            double dou_cantidad = Double.parseDouble(str_cantidad);
            if (dou_cantidad > dou_saldo_arti) {
                utilitario.agregarMensajeError("Error Cantidad", "Sobrepasa el saldo actual");
            }
        } catch (Exception e) {
        }
    }

    public void filtrar_saldo_producto(SelectEvent evt) {
        aut_pruducto.onSelect(evt);
        String producto = aut_pruducto.getValor();
        cls_inventario inv = new cls_inventario();
        System.out.println("valor promedio " + inv.obtener_valor_promedio(com_bodega.getValue() + "", producto));
        tab_tabla3.setSql(inv.obtener_saldo(com_bodega.getValue() + "", producto, utilitario.getFechaActual()));
        tab_tabla3.ejecutarSql();
    }

    public void dibujar_panel_saldo() {
        aut_pruducto.setValor(tab_tabla2.getValor("ide_inarti"));
        cls_inventario inv = new cls_inventario();
        if (com_bodega.getValue() != null) {
            tab_tabla3.setSql(inv.obtener_saldo(com_bodega.getValue() + "", tab_tabla2.getValor("ide_inarti"), utilitario.getFechaActual()));
            tab_tabla3.ejecutarSql();
            dia_saldo_producto.dibujar();
            utilitario.addUpdate("dia_saldo_producto");
        } else {
            utilitario.agregarMensajeInfo("Comprobantes de Inventario", "Debe seleccionar una bodega");
        }
    }

    public List getListaSignos() {
        //pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
        List lista = new ArrayList();
        Object fila1[] = {
            "0", "+"
        };
        Object fila2[] = {
            "1", "-"
        };
        lista.add(fila1);
        lista.add(fila2);
        return lista;
    }

    @Override
    public void insertar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.insertar();
        } else if (tab_tabla1.isFocus()) {
            if (com_bodega.getValue() != null) {
                tab_tabla1.insertar();
                tab_tabla1.setValor("ide_inbod", com_bodega.getValue().toString());
            } else {
                utilitario.agregarMensajeError("Bodega", "Debe seleccionar una bodega");
            }
        } else if (tab_tabla2.isFocus()) {
            if (tab_tabla1.isFilaInsertada()) {
                cls_inventario inv = new cls_inventario();
                String ide_intci = inv.getParametroTablaTipoTransaccion(tab_tabla1.getValor("ide_intti"), "ide_intci");
                String signo = inv.getParametroTablaTipoComprobante(ide_intci, "signo_intci");
                if (signo.equals("-1")) {
                    tab_tabla2.getColumna("precio_indci").setLectura(true);
                } else {
                    tab_tabla2.getColumna("precio_indci").setLectura(false);
                }
                tab_tabla2.insertar();
            }
        } else {
            utilitario.getTablaisFocus().insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.isFilaInsertada()) {
            if (validar()) {
                List lis_haceAsiento = utilitario.getConexion().consultar("SELECT hace_asient_intti FROM inv_tip_tran_inve WHERE ide_intti=" + tab_tabla1.getValor("ide_intti"));
                if (lis_haceAsiento != null && !lis_haceAsiento.isEmpty() && lis_haceAsiento.get(0) != null) {
                    if (lis_haceAsiento.get(0).toString().equalsIgnoreCase("true")) {
                        System.out.println("resultado: " + lis_haceAsiento.get(0).toString());
                        System.out.println("resultado de la lista de hace asiento: " + lis_haceAsiento.get(0).toString());
                        generarAsientoInventario();
                    } else {
                        System.out.println("resultado de la lista de hace asiento: " + lis_haceAsiento.get(0).toString());
                        System.out.println("guarda sin asiento");
                        cls_inventario in = new cls_inventario();
                        tab_tabla1.setValor("numero_incci", in.generarSecuencialComprobanteInventario(tab_tabla1.getValor("ide_intti")));
                        System.out.println("Numero de secuencia: " + in.generarSecuencialComprobanteInventario(tab_tabla1.getValor("ide_intti")));
                        if (tab_tabla1.getValor("numero_incci") == null || tab_tabla1.getValor("numero_incci").isEmpty()) {
                            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar el numero de la transacción");

                        } else {
                            tab_tabla1.guardar();
                            tab_tabla2.guardar();
                            utilitario.getConexion().guardarPantalla();
                        }

                    }
                }
            }

        } else {
            utilitario.agregarMensajeInfo("No se puede Guardar", "Debe Insertar un nuevo registro");
        }
    }

    public void limpiar() {
        if (aut_num_factura.getValue() != null) {
            aut_num_factura.setValue(null);
            tab_tabla1.limpiar();
            tab_tabla2.limpiar();
            com_bodega.setValue(null);
            utilitario.addUpdate("aut_num_factura,tab_tabla1,tab_tabla2,com_bodega");
        } else if (aut_serie_factura.getValue() != null) {
            aut_serie_factura.setValue(null);
            tab_tabla1.limpiar();
            tab_tabla2.limpiar();
            com_bodega.setValue(null);
            utilitario.addUpdate("aut_serie_factura,tab_tabla1,tab_tabla2,com_bodega");
        }
    }

    public String obtener_tipo_articulo(String ide_arti) {
        String ide_art = ide_arti;
        List inv_ide_arti = utilitario.getConexion().consultar("select inv_ide_inarti from inv_articulo where ide_inarti=" + ide_art);
        if (inv_ide_arti.get(0) != null) {
            do {
                ide_art = inv_ide_arti.get(0).toString();
                inv_ide_arti = utilitario.getConexion().consultar("select inv_ide_inarti from inv_articulo where ide_inarti=" + ide_art);
            } while (inv_ide_arti.get(0) != null);
        }
        return ide_art;
    }

    public boolean es_bien1(String ide_inarti) {
        String art = obtener_tipo_articulo(ide_inarti);
        if (art.equals(p_bienes)) {
            return true;
        } else {
            return false;
        }
    }

    public void generarAsientoInventario() {
        conta.limpiar();
        if (com_bodega.getValue().toString() != null) {
            cab_com_con = new cls_cab_comp_cont(utilitario.getVariable("p_con_tipo_comprobante_egreso"), utilitario.getVariable("p_con_estado_comprobante_normal"), "1", tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_trans_incci"), tab_tabla1.getValor("observacion_incci"));
            lista_detalles.clear();

            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                if (es_bien1(tab_tabla2.getValor(i, "ide_inarti"))) {
                    // COSTO EN VENTAS --- DEBE
                    String ide_cuenta_cos_ven = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_tabla2.getValor(i, "ide_inarti"));
                    double valor = Double.parseDouble(tab_tabla2.getValor(i, "valor_indci"));
                    if (ide_cuenta_cos_ven != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_cos_ven, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                    } else {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                    }
                    String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("COSTO EN VENTAS", tab_tabla2.getValor(i, "ide_inarti"));
                    // INVENTARIO GASTO ACTIVO --- HABER
                    if (ide_cuenta_inv_gas_act != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                    } else {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                    }
                }
            }

            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }

    public void aceptarVistaAsiento() {
        if (via_asiento.validarComprobante()) {//siempre if cudra el comp cont
            cab_com_con.setObservacion_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            lista_detalles.clear();
            for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            String ide_cnccc = conta.generarAsientoContable(cab_com_con);
            if (ide_cnccc != null) {
                tab_tabla1.setValor("ide_cnccc", ide_cnccc);
                System.out.println("Numero secuencial: " + in.generarSecuencialComprobanteInventario(utilitario.getVariable("p_inv_tipo_transaccion_consumo")));

                tab_tabla1.setValor("numero_incci", in.generarSecuencialComprobanteInventario(tab_tabla1.getValor("ide_intti")));  /// calcular numero de comprobante de inventario
                tab_tabla1.guardar();
                tab_tabla2.guardar();
                via_asiento.cerrar();
                utilitario.getConexion().guardarPantalla();
                utilitario.addUpdate("via_asiento,tab_tabla1,tab_tabla2");
            }
        }

    }

    public void cancelarDialogo() {
        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        }
        //cancela todo lo que haya tenido hasta ese momento
        // utilitario.getConexion().rollback();*-********
        utilitario.getConexion().getSqlPantalla().clear();
    }

    @Override
    public void eliminar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.eliminar();
        } else {
            utilitario.getTablaisFocus().eliminar();
        }
    }

    public void inicio() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_incci=" + tab_tabla1.getValor("ide_incci"));
            tab_tabla2.ejecutarSql();
            calcularTotalDetalleFinal();
        }
    }

    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_incci=" + tab_tabla1.getValor("ide_incci"));
            tab_tabla2.ejecutarSql();
            calcularTotalDetalleFinal();
        }
    }

    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_incci=" + tab_tabla1.getValor("ide_incci"));
            tab_tabla2.ejecutarSql();
            calcularTotalDetalleFinal();
        }
    }

    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_incci=" + tab_tabla1.getValor("ide_incci"));
            tab_tabla2.ejecutarSql();
            calcularTotalDetalleFinal();
        }
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra   
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista
        if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sef_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sef_formato");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Facturas de Compras")) {
            if (rep_reporte.isVisible()) {
                List sql_cab_fac = utilitario.getConexion().consultar("SELECT ide_cpcfa FROM cxp_cabece_factur WHERE ide_cnccc=" + tab_tabla1.getValor("ide_cnccc"));
                if (sql_cab_fac != null && !sql_cab_fac.isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cpcfa", Long.parseLong(sql_cab_fac.get(0).toString()));
                    sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sef_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sef_formato");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene cabecera de factura");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante de Inventario")) {
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
            } else {
                if (sel_tab.isVisible()) {
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
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Kardex")) {
            System.out.println("Si entra al kardex");
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else {
                if (sel_tab.isVisible()) {
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

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
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

    public Dialogo getDia_saldo_producto() {
        return dia_saldo_producto;
    }

    public void setDia_saldo_producto(Dialogo dia_saldo_producto) {
        this.dia_saldo_producto = dia_saldo_producto;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public AutoCompletar getAut_pruducto() {
        return aut_pruducto;
    }

    public void setAut_pruducto(AutoCompletar aut_pruducto) {
        this.aut_pruducto = aut_pruducto;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
    }

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public SeleccionArbol getSel_arbol() {
        return sel_arbol;
    }

    public void setSel_arbol(SeleccionArbol sel_arbol) {
        this.sel_arbol = sel_arbol;
    }

    public VistaAsiento getVia_asiento() {
        return via_asiento;
    }

    public void setVia_asiento(VistaAsiento via_asiento) {
        this.via_asiento = via_asiento;
    }

    public AutoCompletar getAut_num_factura() {
        return aut_num_factura;
    }

    public void setAut_num_factura(AutoCompletar aut_num_factura) {
        this.aut_num_factura = aut_num_factura;
    }

    public AutoCompletar getAut_serie_factura() {
        return aut_serie_factura;
    }

    public void setAut_serie_factura(AutoCompletar aut_serie_factura) {
        this.aut_serie_factura = aut_serie_factura;
    }
}

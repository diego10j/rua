/*
 * Esta clase permite extraer las facturas para el registro de los inventarios que se encuentran registrados 
 * solo la factura de compra
 */
package paq_inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
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
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import servicios.sistema.ServicioSistema;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_comp_inv_entrega extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_bodega = new Tabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private SeleccionArbol sel_arbol = new SeleccionArbol();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private final Texto tex_num_transaccion = new Texto();
    private final Texto tex_nomb_transaccion = new Texto();
    private final Boton bot_buscar_transaccion = new Boton();
    private final Boton bot_buscar_transacciones = new Boton();
    private SeleccionTabla sel_empleado = new SeleccionTabla();
    private SeleccionTabla sel_departamento = new SeleccionTabla();
    private SeleccionTabla sel_cabece_venta = new SeleccionTabla();
    private SeleccionTabla sel_detalle_venta = new SeleccionTabla();
    private Confirmar con_confirma = new Confirmar();
    private Confirmar con_anular = new Confirmar();
    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();
    private Dialog dia_mensaje_fac = new Dialog();

    String factura = "";
    String valor_orden = "";

    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private final ServicioProduccion ser_produccion = (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class);
    @EJB
    private final ServiciosAdquisiones ser_persona = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private final ServicioSistema ser_sistema = (ServicioSistema) utilitario.instanciarEJB(ServicioSistema.class);

    public pre_comp_inv_entrega() {

        bar_botones.agregarReporte();
        tex_nomb_transaccion.setId("tex_nomb_transaccion");
        //bar_botones.agregarComponente(tex_nomb_transaccion);

        TablaGenerica tab_responsable = ser_sistema.getUsuario(utilitario.getVariable("IDE_USUA"));
        String ide_gtemp = tab_responsable.getValor("ide_gtemp");

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_cab_comp_inve", "ide_incci", 1);
        tab_tabla1.setCampoOrden("ide_incci desc");
        tab_tabla1.setCondicion("ide_intti=" + utilitario.getVariable("p_inv_tipo_transaccion_consumo"));
        tab_tabla1.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");

        tab_tabla1.getColumna("referencia_incci").setVisible(false);
        tab_tabla1.getColumna("referencia_incci").setUnico(true);
        tab_tabla1.getColumna("ide_usua").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_inepi").setCombo("inv_est_prev_inve", "ide_inepi", "nombre_inepi", "");
        tab_tabla1.getColumna("ide_intti").setCombo("select ide_intti, nombre_intti, nombre_intci from inv_tip_tran_inve a\n"
                + "left join inv_tip_comp_inve b on a.ide_intci=b.ide_intci\n"
                + "order by nombre_intci desc, nombre_intti");
        tab_tabla1.getColumna("ide_intti").setRequerida(true);
        tab_tabla1.getColumna("ide_intti").setValorDefecto(utilitario.getVariable("p_inv_tipo_transaccion_consumo"));
        tab_tabla1.getColumna("ide_intti").setLectura(true);
        tab_tabla1.getColumna("ide_inbod").setVisible(false);
        tab_tabla1.getColumna("ide_inepi").setValorDefecto(utilitario.getVariable("p_inv_estado_normal"));
        tab_tabla1.getColumna("fecha_trans_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_siste_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("hora_sistem_incci").setValorDefecto(utilitario.getHoraActual());
        tab_tabla1.getColumna("numero_incci").setEtiqueta();
        tab_tabla1.getColumna("numero_incci").setEstilo("font-size:12px;font-weight: bold;color:red");
        //tab_tabla1.getColumna("numero_incci").setLectura(true);
        tab_tabla1.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
        tab_tabla1.getColumna("observacion_incci").setRequerida(true);
        tab_tabla1.getColumna("observacion_incci").setControl("AreaTexto");
        tab_tabla1.getColumna("sis_ide_usua").setVisible(false);
        tab_tabla1.getColumna("fecha_siste_incci").setVisible(false);
        tab_tabla1.getColumna("hora_sistem_incci").setVisible(false);
        tab_tabla1.getColumna("fec_cam_est_incci").setVisible(false);
        tab_tabla1.getColumna("fecha_efect_incci").setVisible(false);
        //tab_tabla1.getColumna("ide_indod").setVisible(false);
        //tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("ide_cnccc").setVisible(false);
        //tab_tabla1.getColumna("gth_ide_gtemp").setVisible(false);
        tab_tabla1.getColumna("gth_ide_gtemp2").setVisible(false);
        tab_tabla1.getColumna("gth_ide_gtemp3").setVisible(false);
        tab_tabla1.getColumna("maquina_incci").setVisible(false);
        tab_tabla1.getColumna("codigo_documento_incci").setVisible(false);
        tab_tabla1.getColumna("referencia_incci").setNombreVisual("NUMERO FACTURA");
        tab_tabla1.getColumna("referencia_incci").setLectura(true);
        tab_tabla1.getColumna("ide_gtemp").setCombo(ser_persona.getDatosEmpleado());
        tab_tabla1.getColumna("gth_ide_gtemp").setCombo(ser_persona.getDatosEmpleado());
        tab_tabla1.getColumna("gth_ide_gtemp2").setCombo(ser_persona.getDatosEmpleado());
        tab_tabla1.getColumna("gth_ide_gtemp3").setCombo(ser_persona.getDatosEmpleado());
        tab_tabla1.getColumna("ide_inbod").setValorDefecto(utilitario.getVariable("p_inv_bodega_defecto"));
        tab_tabla1.getColumna("ide_gtemp").setAutoCompletar();
        tab_tabla1.getColumna("gth_ide_gtemp").setAutoCompletar();
        tab_tabla1.getColumna("gth_ide_gtemp2").setAutoCompletar();
        tab_tabla1.getColumna("gth_ide_gtemp3").setAutoCompletar();
        tab_tabla1.getColumna("ide_gtemp").setNombreVisual("RESPONSABLE DE REGISTRO");
        tab_tabla1.getColumna("gth_ide_gtemp").setNombreVisual("ENTREGA A");
        tab_tabla1.getColumna("ide_gtemp").setValorDefecto(ide_gtemp);
        tab_tabla1.getColumna("ide_incci").setNombreVisual("CODIGO");
        tab_tabla1.getColumna("ide_inepi").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("ide_inepi").setLectura(true);
        tab_tabla1.getColumna("ide_geper").setNombreVisual("CLIENTE");
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("observacion_incci").setNombreVisual("OBSERVACION");
        //tab_tabla1.getColumna("fecha_trans_incci").setLectura(true);
        tab_tabla1.getColumna("fecha_trans_incci").setNombreVisual("FECHA TRANSACCION");
        tab_tabla1.getColumna("ide_intti").setNombreVisual("TIPO TRANSACCIÓN");
        tab_tabla1.getColumna("ide_incci").setOrden(0);
        tab_tabla1.getColumna("numero_incci").setOrden(1);
        tab_tabla1.getColumna("ide_inepi").setOrden(2);
        tab_tabla1.getColumna("ide_intti").setOrden(3);
        tab_tabla1.getColumna("fecha_trans_incci").setOrden(4);
        tab_tabla1.getColumna("ide_georg").setOrden(5);
        tab_tabla1.getColumna("ide_gtemp").setOrden(6);
        tab_tabla1.getColumna("gth_ide_gtemp").setOrden(7);
        tab_tabla1.getColumna("observacion_incci").setOrden(8);
        tab_tabla1.setCampoOrden("ide_incci desc");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.agregarRelacion(tab_tabla2);
        //tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("inv_det_comp_inve", "ide_indci", 2);
        tab_tabla2.getColumna("ide_inarti").setCombo(ser_inventario.getInventarioGrupo(utilitario.getVariable("p_inv_grupo_entrega_productos")));
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("cantidad1_indci").setVisible(false);
        tab_tabla2.getColumna("ide_inarti").setMetodoChange("cargarPrecio");
        tab_tabla2.getColumna("cantidad_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla2.getColumna("cantidad_indci").setValorDefecto("0");
        tab_tabla2.getColumna("cantidad_indci").setRequerida(true);
        tab_tabla2.getColumna("precio_indci").setValorDefecto("0");
        tab_tabla2.getColumna("valor_indci").setValorDefecto("0");
        tab_tabla2.getColumna("valor_indci").setEtiqueta();
        tab_tabla2.getColumna("precio_indci").setVisible(false);
        tab_tabla2.getColumna("valor_indci").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("referencia_indci").setVisible(false);
        tab_tabla2.getColumna("referencia1_indci").setVisible(false);
        tab_tabla2.getColumna("valor_indci").setVisible(false);
        tab_tabla2.getColumna("precio_promedio_indci").setVisible(false);
        tab_tabla2.getColumna("ide_cccfa").setVisible(false);
        tab_tabla2.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla2.getColumna("ide_indci").setNombreVisual("CODIGO");
        tab_tabla2.getColumna("ide_inarti").setNombreVisual("ARTICULO");
        tab_tabla2.getColumna("cantidad_indci").setNombreVisual("CANTIDAD");
        tab_tabla2.getColumna("precio_indci").setNombreVisual("PRECIO");
        tab_tabla2.getColumna("valor_indci").setNombreVisual("TOTAL");
        tab_tabla2.getColumna("secuencial_indci").setNombreVisual("SECUENCIAL");
        tab_tabla2.getColumna("secuencial_indci").setVisible(false);
        tab_tabla2.getColumna("observacion_indci").setNombreVisual("OBSERVACIÓN");
        tab_tabla2.getColumna("ide_inarti").setAncho(-1);
        tab_tabla2.getColumna("ide_inarti").setLongitud(-1);
        tab_tabla2.getColumna("ide_ccdfa").setVisible(false);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
        agregarComponente(div_division);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.setMultiple(false);
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sec_rango_reporte);
        sef_formato.setId("sef_formato");

        agregarComponente(sef_formato);
        rep_reporte.setId("rep_reporte");
        agregarComponente(rep_reporte);

        sel_arbol.setId("sel_arbol");
        sel_arbol.setSeleccionArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");
        sel_arbol.getArb_seleccion().setOptimiza(true);
        agregarComponente(sel_arbol);
        sel_arbol.getBot_aceptar().setMetodo("aceptarReporte");

        sel_tab.setId("sel_tab");
        sel_tab.setSeleccionTabla("SELECT ide_inbod,nombre_inbod FROM inv_bodega WHERE nivel_inbod='HIJO' AND ide_empr=" + utilitario.getVariable("ide_empr"), "ide_inbod");
        agregarComponente(sel_tab);
        sel_tab.getBot_aceptar().setMetodo("aceptarReporte");

        sel_empleado.setId("sel_empleado");
        sel_empleado.setSeleccionTabla(ser_inventario.getSqlComboEmpleados(), "ide_geper");
        agregarComponente(sel_empleado);
        sel_empleado.getBot_aceptar().setMetodo("aceptarReporte");

        sel_departamento.setId("sel_departamento");
        sel_departamento.setSeleccionTabla(ser_inventario.getSqlComboOrganigrama(), "ide_georg");
        agregarComponente(sel_departamento);
        sel_departamento.getBot_aceptar().setMetodo("aceptarReporte");

        Boton bot_busca_solici = new Boton();
        bot_busca_solici.setValue("BUSCAR FACTURA");
        bot_busca_solici.setIcon("ui-icon-search");
        bot_busca_solici.setMetodo("dibujaSolicitud");
        //bar_botones.agregarBoton(bot_busca_solici);

        Boton bot_aprobar_ingreso = new Boton();
        bot_aprobar_ingreso.setValue("APROBAR INGRESO");
        bot_aprobar_ingreso.setIcon("ui-icon-search");
        bot_aprobar_ingreso.setMetodo("aprobarIngreso");
        bar_botones.agregarBoton(bot_aprobar_ingreso);

        Boton bot_anular_ingreso = new Boton();
        bot_anular_ingreso.setValue("ANULAR");
        bot_anular_ingreso.setIcon("ui-icon-cancel");
        bot_anular_ingreso.setMetodo("anularComprobante");
        bar_botones.agregarBoton(bot_anular_ingreso);
        
        Boton bot_imprimir = new Boton();
        bot_imprimir.setValue("IMPRIMIR");
        bot_imprimir.setIcon("ui-icon-print");
        bot_imprimir.setMetodo("generarPDFnota");
        bar_botones.agregarBoton(bot_imprimir);

        sel_cabece_venta.setId("sel_cabece_venta");
        sel_cabece_venta.setTitle("SELECCIONE UNA FACTURA");
        sel_cabece_venta.setSeleccionTabla(ser_adquisiciones.getFacturasVentas("ide_cccfa=-1"), "ide_cccfa");
        sel_cabece_venta.setWidth("50%");
        sel_cabece_venta.setHeight("70%");
        sel_cabece_venta.setRadio();
        sel_cabece_venta.getBot_aceptar().setMetodo("aceptarSolicitud");
        agregarComponente(sel_cabece_venta);

        sel_detalle_venta.setId("sel_detalle_venta");
        sel_detalle_venta.setTitle("SELECCIONA EL DETALLE DE LA FACTURA");
        sel_detalle_venta.setSeleccionTabla(ser_adquisiciones.getdetalleFacturaVenta("ide_ccdfa=-1"), "ide_ccdfa");
        sel_detalle_venta.setWidth("50%");
        sel_detalle_venta.setHeight("70%");
        sel_detalle_venta.getBot_aceptar().setMetodo("generarCabecera");
        agregarComponente(sel_detalle_venta);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro que desea aprobar el siguiente Ingreso/Egreso de inventarios");
        con_confirma.setTitle("APROBAR INGRESO/EGRESO INVENTARIO");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        vipdf_comprobante.setId("vipdf_comprobante");
        vipdf_comprobante.setTitle("COMPROBANTE INVENTARIO");
        agregarComponente(vipdf_comprobante);
        bloquearModificacion();

        dia_mensaje_fac.setId("dia_mensaje_fac");
        dia_mensaje_fac.setWidgetVar("dia_mensaje_fac");
        dia_mensaje_fac.setAppendToBody(true);
        dia_mensaje_fac.setWidth("500");
        dia_mensaje_fac.setHeight("60");
        dia_mensaje_fac.setShowEffect("fade");
        dia_mensaje_fac.setClosable(true);
        dia_mensaje_fac.setResizable(false);
        //agregarComponente(dia_mensaje_fac);
        utilitario.getPantalla().getChildren().add(dia_mensaje_fac);
        
        con_anular.setId("con_anular");
        con_anular.setMessage("Está seguro que desea anular y enviar a recalcular el siguiente Ingreso/Egreso de inventarios");
        con_anular.setTitle("ANULAR INGRESO/EGRESO INVENTARIO");
        con_anular.getBot_aceptar().setValue("Si");
        con_anular.getBot_cancelar().setValue("No");
        agregarComponente(con_anular);
    }

    /**
     * anular el comprobante y realiza el recalculo del kardex
     */
    public void anularComprobante() {
        TablaGenerica tab_consulta = utilitario.consultar(" select * from inv_det_comp_inve where ide_incci=" + tab_tabla1.getValor("ide_incci") + " ");
        if (tab_tabla1.getValor("ide_inepi").equals(utilitario.getVariable("p_inv_estado_anulado"))) {
            utilitario.agregarMensajeInfo("Información", "El comprobante de invetario ya esta anulado");
            return;
        }
        if (tab_tabla1.isFilaInsertada()) {
            utilitario.agregarMensajeInfo("Información", "Primero debe guardar el registro que esta trabajando");
            return;
        }
        if (tab_tabla2.isFilaInsertada()) {
            utilitario.agregarMensajeInfo("Información", "Primero debe guardar el registro que esta trabajando");
            return;
        }
        if (tab_consulta.getTotalFilas() <= 0) {
            utilitario.agregarMensajeInfo("Información", "Debe insertar productos ");
            return;
        }
        con_anular.getBot_aceptar().setMetodo("confirmarAnular");
        utilitario.addUpdate("con_anular");
        con_anular.dibujar();
    }

    public void confirmarAnular() {
        String cod = tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_incci");
        String fecha = tab_tabla1.getValor(tab_tabla1.getFilaActual(), "fecha_trans_incci");
        TablaGenerica tab_extraer_anio = utilitario.consultar(ser_inventario.getExtraerAnio(fecha));
        TablaGenerica tab_anio = utilitario.consultar(ser_inventario.getInventarioAnio(tab_extraer_anio.getValor("anio")));
        if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_inepi").equals(utilitario.getVariable("p_inv_estado_aprobado"))) {
            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                //System.out.println("Imprimo >> " + cod + " " + fecha + " " + tab_anio.getValor("ide_geani"));
                //System.err.println("articulo >> " + tab_tabla2.getValor("ide_inarti"));
                ser_inventario.getRecalcularInventario("1", tab_anio.getValor("ide_geani"), tab_tabla2.getValor("ide_inarti"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR"));
            }
            utilitario.getConexion().ejecutarSql("update inv_cab_comp_inve set ide_inepi =" + utilitario.getVariable("p_inv_estado_anulado") + " where ide_incci=" + cod);
            con_anular.cerrar();
            tab_tabla1.actualizar();
            utilitario.agregarMensaje("Se anulo correctamente", "");
            return;
        }

        utilitario.getConexion().ejecutarSql("update inv_cab_comp_inve set ide_inepi =" + utilitario.getVariable("p_inv_estado_anulado") + " where ide_incci=" + cod);
        con_anular.cerrar();
        tab_tabla1.actualizar();
        utilitario.agregarMensaje("Se anulo correctamente", "");
    }
    
    public void bloquearModificacion() {
        //BLOQUEOS
        if (tab_tabla1.getTotalFilas() > 0) {
            if (tab_tabla1.getValor("ide_inepi").equals(utilitario.getVariable("p_inv_estado_aprobado")) || tab_tabla1.getValor("ide_inepi").equals(utilitario.getVariable("p_inv_estado_aprobado"))) {
                for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                    tab_tabla2.getFilas().get(i).setLectura(true);
                }
                tab_tabla1.getFilaSeleccionada().setLectura(true);
                utilitario.addUpdate("tab_tabla2,tab_tabla1");
            } else {
                tab_tabla1.setLectura(false);
                tab_tabla2.setLectura(false);
                utilitario.addUpdate("tab_tabla2,tab_tabla1");
            }
        }
    }

    public void generarPDFnota() {
        if (tab_tabla1.isFilaInsertada()) {
            utilitario.agregarMensajeInfo("No se puede imprimir", "Primero debe guardar el registro que esta trabajando");
            return;
        } else if (tab_tabla2.isFilaInsertada()) {
            utilitario.agregarMensajeInfo("No se puede imprimir", "Primero debe guardar el registro que esta trabajando");
            return;
        } else if (tab_tabla1.getTotalFilas() > 0) {
            String reporte = "";
            Map parametros = new HashMap();
            parametros.put("ide_incci", Integer.parseInt(tab_tabla1.getValor("ide_incci")));
            reporte = "rep_inventario/rep_comprob_entrega_consumo.jasper";
            String nom_rep = "Comprobante";
            vipdf_comprobante.setVisualizarPDF(reporte, parametros);
            vipdf_comprobante.dibujar();
            utilitario.addUpdate("vipdf_comprobante");
        } else {
            utilitario.agregarMensajeInfo("No se puede imprimir", "Primero registre una factura");
            return;
        }
    }

    public void registrarInventario() {
        TablaGenerica tab_fecha = utilitario.consultar(ser_inventario.getExtraerAnio(tab_tabla1.getValor("fecha_trans_incci")));
        //TablaGenerica tab_anio = utilitario.consultar(ser_inventario.getInventarioAnio(tab_fecha.getValor("anio")));
        /**
         * parametro aplica bodega True permite el registrar el ingreso y
         * egresos pos sucursales y empresas es decir varias bodegas False
         * permite regitrar por defecto en una solo bodega
         */
        //if (utilitario.getVariable("p_varias_bodegas").equals("true")) { comento Jhon para evitar duplicidad en el kardex
        ser_inventario.getRegistrarInventario("1", tab_tabla1.getValor("ide_incci"), tab_tabla1.getValor("ide_intti"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR"), tab_fecha.getValor("anio"));
        /*} else {
            ser_inventario.getRegistrarInventario("0", tab_tabla1.getValor("ide_incci"), tab_tabla1.getValor("ide_intti"), "0", "0", tab_fecha.getValor("anio"));
        }*/
        con_confirma.cerrar();
        tab_tabla1.actualizar();
        utilitario.agregarMensaje("Se aprobo correctamente", "");
        bloquearModificacion();
    }

    public void aprobarIngreso() {
        TablaGenerica tab_consulta = utilitario.consultar(" select * from inv_det_comp_inve where ide_incci=" + tab_tabla1.getValor("ide_incci") + " ");
        if (tab_tabla1.getValor("ide_inepi").equals(utilitario.getVariable("p_inv_estado_aprobado"))) {
            utilitario.agregarMensajeInfo("Información", "El comprobante de invetario ya esta aprobada");
        } else if (tab_tabla1.isFilaInsertada()) {
            utilitario.agregarMensajeInfo("Información", "Primero debe guardar el registro que esta trabajando");
            return;
        } else if (tab_tabla2.isFilaInsertada()) {
            utilitario.agregarMensajeInfo("Información", "Primero debe guardar el registro que esta trabajando");
            return;
        } else if (tab_consulta.getTotalFilas() > 0) {
            con_confirma.getBot_aceptar().setMetodo("registrarInventario");
            utilitario.addUpdate("con_confirma");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Información", "Debe insertar productos ");
            return;
        }
    }

    public void dibujaSolicitud() {
        sel_cabece_venta.getTab_seleccion().setSql(ser_adquisiciones.getFacturasVentas("secuencial_cccfa like '%" + tex_nomb_transaccion.getValue().toString() + "%'"));
        //sel_cabece_venta.getTab_seleccion().imprimirSql();
        sel_cabece_venta.getTab_seleccion().ejecutarSql();
        sel_cabece_venta.dibujar();
    }

    public void aceptarSolicitud() {
        if (sel_cabece_venta.getValorSeleccionado() != null) {
            factura = sel_cabece_venta.getValorSeleccionado();
            sel_cabece_venta.cerrar();
            sel_detalle_venta.getTab_seleccion().setSql(ser_adquisiciones.getdetalleFacturaVenta("ide_cccfa=" + factura));
            sel_detalle_venta.getTab_seleccion().ejecutarSql();
            sel_detalle_venta.dibujar();
        } else {
            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Seleccione al menos un registro");
        }
    }

    public void generarCabecera() {

        String selec_productos = sel_detalle_venta.getSeleccionados();
        if (selec_productos.equals("null") || selec_productos.isEmpty()) {
            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Seleccione al menos un registro");
        } else {
            TablaGenerica tab_fact_cabera = utilitario.consultar(ser_adquisiciones.getFacturasVentas("ide_cccfa=" + factura));

            TablaGenerica tab_responsable = ser_sistema.getUsuario(utilitario.getVariable("IDE_USUA"));
            String ide_gtemp = tab_responsable.getValor("ide_gtemp");
            //TablaGenerica tab_anio = utilitario.consultar(ser_inventario.getExtraerAnio(utilitario.getFechaActual()));

            for (int i = 0; i < tab_fact_cabera.getTotalFilas(); i++) {
                if (tab_tabla1.isFilaInsertada() == false) {
                    tab_tabla1.insertar();
                }
                tab_tabla1.setValor("ide_geper", tab_fact_cabera.getValor(i, "ide_geper"));
                tab_tabla1.setValor("ide_intti", utilitario.getVariable("p_inv_tipo_transaccion_venta"));
                tab_tabla1.setValor("ide_inbod", utilitario.getVariable("p_inv_bodega_defecto"));
                tab_tabla1.setValor("ide_gtemp", ide_gtemp);
                tab_tabla1.setValor("referencia_incci", tab_fact_cabera.getValor(i, "secuencial_cccfa"));
                //tab_tabla1.setValor("numero_incci", ser_inventario.getSecuencialCompInventario(utilitario.getVariable("p_inv_tipo_transaccion_venta"), tab_anio.getValor("anio")));
            }
            // tab_tabla1.guardar();
            // guardarPantalla();
            sel_detalle_venta.cerrar();
            utilitario.addUpdate("tab_tabla1");
            generaDetalle();
        }
    }

    public void generaDetalle() {
        String selec_productos = sel_detalle_venta.getSeleccionados();
        TablaGenerica tab_detalle_fac = utilitario.consultar(ser_adquisiciones.getdetalleFacturaVenta("ide_ccdfa in (" + selec_productos + ")"));
        System.out.println("factura" + factura);
        for (int i = 0; i < tab_detalle_fac.getTotalFilas(); i++) {
            tab_tabla2.insertar();
            tab_tabla2.setValor("ide_incci", tab_tabla1.getValor("ide_incci"));
            tab_tabla2.setValor("ide_inarti", tab_detalle_fac.getValor(i, "ide_inarti"));
            tab_tabla2.setValor("cantidad_indci", tab_detalle_fac.getValor(i, "cantidad_ccdfa"));
            tab_tabla2.setValor("precio_indci", tab_detalle_fac.getValor(i, "precio_ccdfa"));
            tab_tabla2.setValor("valor_indci", tab_detalle_fac.getValor(i, "subtotal"));
            tab_tabla2.setValor("ide_ccdfa", tab_detalle_fac.getValor(i, "ide_ccdfa"));
        }
        utilitario.addUpdate("tab_tabla2");
    }

    public void calcularTotalDetalles(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularDetalle();
    }

    public void cargarPrecio(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        verificarStock();
        calcularDetalle();
    }

    private void calcularDetalle() {
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        try {
            cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_indci"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_tabla2.getValor("precio_indci"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_tabla2.setValor("valor_indci", utilitario.getFormatoNumero(total, 2));

        utilitario.addUpdate("tab_tabla2");
    }

    private void verificarStock() {
        double cantidad = 0;
        try {
            cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_indci"));
        } catch (Exception e) {
            cantidad = 0;
        }
        //System.out.println("entre averifuicar stock ");
        if (utilitario.getVariable("p_aplica_stock").equals("true")) {
            //DFJ
            //Valida existencia en stock 
            //double saldo = ser_inventario.getSaldoProducto(null, tab_deta_factura.getValor("ide_inarti"));
            //extraer año
            TablaGenerica tab_fecha = utilitario.consultar(ser_inventario.getExtraerAnio(tab_tabla1.getValor("fecha_trans_incci")));
            TablaGenerica tab_anio = utilitario.consultar(ser_inventario.getInventarioAnio(tab_fecha.getValor("anio")));
//System.out.println("entre averifuicar stockyyyyy ");
            if (utilitario.getVariable("p_varias_bodegas").equals("true")) {
                System.out.println("entre averifuicar stockhhhhh ");
                double saldo = ser_inventario.getStockArticulo("1", tab_tabla2.getValor("ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR"));
                if (saldo <= 0) {
                    utilitario.agregarMensajeInfo("No existe stock en inventario", tab_tabla2.getValorArreglo("ide_inarti", 1) + " stock = " + saldo);
                } else if (cantidad > saldo) {
                    // System.out.println("entre averifuicar stock bbbbb");
                    dia_mensaje_fac.getChildren().clear();
                    dia_mensaje_fac.setHeader("La cantidad ingresada es mayor al stock en inventario".toUpperCase());
                    Grid g = new Grid();
                    g.setWidth("100%");
                    g.getChildren().add(new Etiqueta("<div align='center'> <strong>" + tab_tabla2.getValorArreglo("ide_inarti", 1) + "</strong> stock = " + saldo + " </div>"));
                    dia_mensaje_fac.getChildren().add(g);
                    utilitario.addUpdate("dia_mensaje_fac");
                    utilitario.ejecutarJavaScript("dia_mensaje_fac.show();");
                }
            } else {
                //System.out.println("entre averifuicar stock ggggggg");
                TablaGenerica tab_cantida = utilitario.consultar("select * from gen_anio where nom_geani in (extract(year from cast('" + tab_tabla1.getValor("fecha_trans_incci") + "' as date))||'')");
                //tab_cantida.imprimirSql();
                TablaGenerica tab_valor = utilitario.consultar(ser_inventario.getBodtArticulo("0", tab_tabla2.getValor("ide_inarti"), tab_cantida.getValor("ide_geani"), "0", "0"));
                //tab_valor.imprimirSql();
                if (tab_valor.getTotalFilas() > 0) {
                    tab_tabla2.setValor("precio_indci", tab_valor.getValor("costo_actual_boart"));
                    utilitario.addUpdate("tab_tabla2");
                } else {
                    utilitario.agregarMensajeError("EL articulo seleccionado", "Verificar si se encuentra en el inventario inicial y tiene valor de compra");
                    return;
                }

            }
        }
    }

    /**
     * metodo para el boton Inicio del navegador de paginas, muestra el primer
     * registro de la tabla
     *
     */
    @Override
    public void inicio() {
        // TODO Auto-generated method stub
        super.inicio();
        bloquearModificacion();
    }

    /**
     * metodo para el boton Fin del navegador de paginas, muestra el ultimo
     * registro de la tabla
     *
     */
    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
        bloquearModificacion();
    }

    /**
     * metodo para el boton Siguiente del navegador de paginas, muestra un
     * registro posterior del registro actual de la tabla
     *
     */
    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
        bloquearModificacion();
    }

    /**
     * metodo para el boton Atras del navegador de paginas, muestra un registro
     * anterior del registro actual de la tabla
     *
     */
    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
        bloquearModificacion();
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
                //tab_tabla1.setValor("numero_incci", ser_inventario.getSecuencialComprobanteInventario(String.valueOf(tab_tabla1.getValor("ide_inbod"))));
                TablaGenerica tab_anio = utilitario.consultar(ser_inventario.getExtraerAnio(utilitario.getFechaActual()));
                tab_tabla1.setValor("numero_incci", ser_inventario.getSecuencialCompInventario(utilitario.getVariable("p_inv_tipo_transaccion_consumo"), tab_anio.getValor("anio")));
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

        if (tab_tabla1.getValor("ide_intti") == null || tab_tabla1.getValor("ide_intti").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe seleccionar un tipo de transacción");
            return false;
        }
        if (tab_tabla1.getValor("fecha_trans_incci") == null || tab_tabla1.getValor("fecha_trans_incci").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe ingresar la fecha de la transacción");
            return false;
        }
        if (tab_tabla1.getValor("gth_ide_gtemp") == null || tab_tabla1.getValor("gth_ide_gtemp").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar el empleado que recibe");
            return false;
        }
        if (tab_tabla1.getValor("observacion_incci") == null || tab_tabla1.getValor("observacion_incci").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar la observación");
            return false;
        }
        if (tab_tabla1.getValor("ide_georg") == null || tab_tabla1.getValor("ide_georg").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar el destino");
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
                System.out.println("seleccion..de arbol..." + sel_arbol.getSeleccionados());
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
                // System.out.println("seleccion..de arbol..." + sel_arbol.getSeleccionados());
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
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else if (sel_tab.isVisible()) {
                sel_tab.cerrar();
                parametro.put("ide_inbod", sel_tab.getSeleccionados());
                //  System.out.println("seleccion..de tabla..." + sel_tab.getSeleccionados());
                sel_arbol.dibujar();
                //sec_rango_reporte.dibujar();
                utilitario.addUpdate("sel_tab,sel_arbol");
            } else if (sel_arbol.isVisible()) {

                parametro.put("ide_inarti", sel_arbol.getSeleccionados());
                // System.out.println("seleccion..de arbol..." + sel_arbol.getSeleccionados());
                sel_arbol.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("sel_arbol,sec_rango_reporte");
            }/* else if (sec_rango_reporte.isVisible()) {

                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                // System.out.println("seleccion..de arbol..." + sel_arbol.getSeleccionados());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("sef_formato,sec_rango_reporte");
            }*/
        } else if (rep_reporte.getReporteSelecionado().equals("Consumos por Departamento")) {
            // System.out.println("seleccion..de arbol... entre" + sel_arbol.getSeleccionados());
            if (rep_reporte.isVisible()) {

                parametro = new HashMap();
                //  System.out.println("seleccion..de arbol...ingre" + sel_arbol.getSeleccionados());
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
            } else if (sec_rango_reporte.isVisible()) {

                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                // System.out.println("seleccion..de arbol...ing" + sel_arbol.getSeleccionados());
                sec_rango_reporte.cerrar();

                parametro.put("informe_para", "Jimmy Massa");
                //  System.out.println("seleccion..de ide_geper..." + sel_empleado.getSeleccionados());
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
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

    public SeleccionTabla getSel_empleado() {
        return sel_empleado;
    }

    public void setSel_empleado(SeleccionTabla sel_empleado) {
        this.sel_empleado = sel_empleado;
    }

    public SeleccionTabla getSel_departamento() {
        return sel_departamento;
    }

    public void setSel_departamento(SeleccionTabla sel_departamento) {
        this.sel_departamento = sel_departamento;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public SeleccionTabla getSel_cabece_venta() {
        return sel_cabece_venta;
    }

    public void setSel_cabece_venta(SeleccionTabla sel_cabece_venta) {
        this.sel_cabece_venta = sel_cabece_venta;
    }

    public SeleccionTabla getSel_detalle_venta() {
        return sel_detalle_venta;
    }

    public void setSel_detalle_venta(SeleccionTabla sel_detalle_venta) {
        this.sel_detalle_venta = sel_detalle_venta;
    }

}

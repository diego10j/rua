/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import paq_inventario.*;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
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
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_comp_inv_rmp extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
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
    private SeleccionTabla sel_tab_solicitud = new SeleccionTabla();
    private SeleccionTabla sel_detalle_compra = new SeleccionTabla();
    private SeleccionTabla sel_cabecera_orden_prod = new SeleccionTabla();
    private SeleccionTabla sel_detalle_orden_prod = new SeleccionTabla();
    private Confirmar con_confirma = new Confirmar();

    String solicitud = "";
    String valor_orden = "";
    private VisualizarPDF vipdf_r_m_p = new VisualizarPDF();

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

    public pre_comp_inv_rmp() {
        //Recuperar el plan de cuentas activo

        //bar_botones.quitarBotonsNavegacion();
        //bar_botones.agregarReporte();
        //Busqueda del num de articulo
        /*tex_num_transaccion.setId("tex_num_transaccion");
        tex_num_transaccion.setSoloEnteros();
        bot_buscar_transaccion.setTitle("Buscar");
        bot_buscar_transaccion.setIcon("ui-icon-search");
        bot_buscar_transaccion.setMetodo("buscarTransaccion");
        bot_buscar_transaccion.setValue("BUSCAR POR NUMERO TRANSACCION");
        bar_botones.agregarComponente(tex_num_transaccion);
        bar_botones.agregarBoton(bot_buscar_transaccion);
        
        //Busqueda del nombre de articulo
        tex_nomb_transaccion.setId("tex_nomb_transaccion");
        bot_buscar_transacciones.setTitle("Buscar");
        bot_buscar_transacciones.setIcon("ui-icon-search");
        bot_buscar_transacciones.setMetodo("buscarnombTransaccion");
        bot_buscar_transacciones.setValue("BUSCAR POR DETALLE TRANSACCION");
        bar_botones.agregarComponente(tex_nomb_transaccion);
       
        
        bar_botones.agregarBoton(bot_buscar_transacciones);
      
         */
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_cab_comp_inve", "ide_incci", 1);
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        //tab_tabla1.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
        tab_tabla1.getColumna("referencia_incci").setVisible(false);
        tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("referencia_incci").setUnico(true);
        tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("ide_usua").setVisible(false);
        //tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_geper").setRequerida(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("ide_inepi").setCombo("inv_est_prev_inve", "ide_inepi", "nombre_inepi", "");
        tab_tabla1.getColumna("ide_intti").setCombo("select ide_intti, nombre_intti, nombre_intci from inv_tip_tran_inve a\n"
                + "inner join inv_tip_comp_inve b on a.ide_intci=b.ide_intci\n"
                + "order by nombre_intci desc, nombre_intti");
        //tab_tabla1.getColumna("ide_intti").setMetodoChange("cambiaTipoTransaccion");
        tab_tabla1.getColumna("ide_gtemp").setCombo(ser_persona.getDatosEmpleado());
        tab_tabla1.getColumna("gth_ide_gtemp").setCombo(ser_persona.getDatosEmpleado());
        tab_tabla1.getColumna("gth_ide_gtemp").setValorDefecto(utilitario.getVariable("p_prod_entregado_requerimiento"));
        tab_tabla1.getColumna("gth_ide_gtemp2").setCombo(ser_persona.getDatosEmpleado());
        tab_tabla1.getColumna("ide_intti").setRequerida(true);
        tab_tabla1.getColumna("ide_inbod").setCombo("inv_bodega", "ide_inbod", "nombre_inbod", "nivel_inbod='HIJO'");
        tab_tabla1.getColumna("ide_inbod").setVisible(false);
        tab_tabla1.getColumna("ide_inepi").setValorDefecto(utilitario.getVariable("p_inv_estado_normal"));
        tab_tabla1.getColumna("fecha_trans_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_siste_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("hora_sistem_incci").setValorDefecto(utilitario.getHoraActual());
        tab_tabla1.getColumna("numero_incci").setLectura(true);
        tab_tabla1.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
        tab_tabla1.getColumna("observacion_incci").setRequerida(false);
        tab_tabla1.getColumna("observacion_incci").setControl("AreaTexto");
        tab_tabla1.getColumna("sis_ide_usua").setVisible(false);
        tab_tabla1.getColumna("fecha_siste_incci").setVisible(false);
        tab_tabla1.getColumna("hora_sistem_incci").setVisible(false);
        tab_tabla1.getColumna("fec_cam_est_incci").setVisible(false);
        tab_tabla1.getColumna("fecha_efect_incci").setVisible(false);
        tab_tabla1.getColumna("REFERENCIA_INCCI").setVisible(false);
        tab_tabla1.getColumna("GTH_IDE_GTEMP3").setVisible(false);
        tab_tabla1.getColumna("MAQUINA_INCCI").setVisible(false);
        tab_tabla1.getColumna("valor_factura_incci").setVisible(false);
        tab_tabla1.getColumna("IDE_GTEMP").setNombreVisual("SOLICITADO POR");
        tab_tabla1.getColumna("IDE_GTEMP").setAutoCompletar();
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setNombreVisual("ENTREGADO POR");
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setAutoCompletar();
        tab_tabla1.getColumna("GTH_IDE_GTEMP2").setNombreVisual("RECIBIDO POR");
        tab_tabla1.getColumna("GTH_IDE_GTEMP2").setAutoCompletar();
        tab_tabla1.getColumna("CODIGO_DOCUMENTO_INCCI").setNombreVisual("PRODUCCIÓN DE");
        tab_tabla1.getColumna("CODIGO_DOCUMENTO2_INCCI").setNombreVisual("RP-02 N°");
        tab_tabla1.getColumna("CODIGO_DOCUMENTO2_INCCI").setValorDefecto("N/A");
        tab_tabla1.getColumna("IDE_INTTI").setNombreVisual("DOCUMENTO");
        tab_tabla1.getColumna("IDE_INTTI").setAutoCompletar();

        tab_tabla1.getColumna("ide_intti").setLectura(true);
        tab_tabla1.getColumna("ide_cnccc").setLink();
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.setCondicion("ide_intti= " + utilitario.getVariable("p_prod_requerimiento_materia_prima"));
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("inv_det_comp_inve", "ide_indci", 2);
        //tab_tabla2.setCondicion("ide_incci=-1");
        //tab_tabla2.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaArticulos());
        tab_tabla2.getColumna("ide_prcol").setCombo(ser_produccion.getColor());
        tab_tabla2.getColumna("ide_prcol").setAncho(-1);
        tab_tabla2.getColumna("ide_prcol").setLongitud(-1);
        tab_tabla2.getColumna("ide_inarti").setCombo(ser_inventario.getInventarioGrupo(utilitario.getVariable("p_prod_grupo_requerimiento")));
        tab_tabla2.getColumna("ide_inarti").setAncho(-1);
        tab_tabla2.getColumna("ide_inarti").setLongitud(-1);
        //tab_tabla2.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaProductos());
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("cantidad1_indci").setVisible(false);
        tab_tabla2.getColumna("ide_inarti").setMetodoChange("cargarPrecio");
        tab_tabla2.getColumna("cantidad_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla2.getColumna("precio_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla2.getColumna("cantidad_indci").setRequerida(true);
        tab_tabla2.getColumna("cantidad_indci").setFormatoNumero(3);
        tab_tabla2.getColumna("cantidad1_indci").setFormatoNumero(3);
        tab_tabla2.getColumna("precio_indci").setRequerida(false);
        tab_tabla2.getColumna("precio_indci").setVisible(false);//identificar nombre de campo cambio

//        tab_tabla2.getColumna("ide_inarti").setRequerida(true);
        tab_tabla2.getColumna("valor_indci").setRequerida(false);
        //tab_tabla2.getColumna("valor_indci").setEtiqueta();
        //tab_tabla2.getColumna("valor_indci").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("valor_indci").setVisible(false);
        tab_tabla2.getColumna("referencia_indci").setVisible(false);
        tab_tabla2.getColumna("referencia1_indci").setVisible(false);
        tab_tabla2.getColumna("secuencial_indci").setVisible(false);
        tab_tabla2.getColumna("observacion_indci").setVisible(false);
        tab_tabla2.setRows(10);
////        tab_tabla2.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "numero_cpcfa", "ide_cpcfa=-1");
////        tab_tabla2.getColumna("ide_cpcfa").setLectura(true);
////        tab_tabla2.getColumna("ide_cccfa").setCombo("cxc_cabece_factura", "ide_cccfa", "secuencial_cccfa", "ide_cccfa=-1");
////        tab_tabla2.getColumna("ide_cccfa").setLectura(true);
        tab_tabla2.getColumna("precio_promedio_indci").setVisible(false);

        tab_tabla2.getColumna("ide_cccfa").setVisible(false);
        tab_tabla2.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla2.getColumna("ide_inuni").setCombo(ser_produccion.getUnidad());
        tab_tabla2.getColumna("ide_inuni").setAncho(-1);
        tab_tabla2.getColumna("ide_inuni").setLongitud(-1);
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
        sel_arbol.getArb_seleccion().setCondicion("ide_inarti=" + utilitario.getVariable("p_inv_articulo_bien"));
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

        /*Boton bot_busca_solici = new Boton();
        bot_busca_solici.setValue("BUSCAR SOLICITUD MATERIAL");
        bot_busca_solici.setIcon("ui-icon-search");
        bot_busca_solici.setMetodo("dibujaSolicitud");
        bar_botones.agregarBoton(bot_busca_solici);  */
        sel_tab_solicitud.setId("sel_cabece_compra");
        sel_tab_solicitud.setTitle("SELECCIONE UNA SOLICITUD DE MATERIAL");
        sel_tab_solicitud.setSeleccionTabla(ser_produccion.getSolicitudMaterial("1", ""), "ide_prsol");
        sel_tab_solicitud.setWidth("80%");
        sel_tab_solicitud.setHeight("70%");
        sel_tab_solicitud.setRadio();
        sel_tab_solicitud.getBot_aceptar().setMetodo("aceptarSolicitud");
        agregarComponente(sel_tab_solicitud);

        /*  sel_detalle_compra.setId("sel_detalle_compra");
        sel_detalle_compra.setTitle("SELECCIONA EL DETALLE DE LA FACTURA");
        sel_detalle_compra.setSeleccionTabla(ser_adquisiciones.getdetalleFacturaCompra("1", ""), "ide_cpdfa");
        sel_detalle_compra.setWidth("80%");
        sel_detalle_compra.setHeight("70%");
        //sel_tab_detalle_compra.setRadio();
        sel_detalle_compra.getBot_aceptar().setMetodo("generarCabecera");
        agregarComponente(sel_detalle_compra);
        
        Boton bot_busca_orden = new Boton();
        bot_busca_orden.setValue("BUSCAR ORDEN DE PRODUCIÓN");
        bot_busca_orden.setIcon("ui-icon-search");
        bot_busca_orden.setMetodo("dibujaCabeceraOrden");
        //bar_botones.agregarBoton(bot_busca_orden);
        
        sel_cabecera_orden_prod.setId("sel_cabecera_orden_prod");
        sel_cabecera_orden_prod.setTitle("ORDEN DE PRODUCCION");
        sel_cabecera_orden_prod.setSeleccionTabla(ser_produccion.getOrdenPro(), "ide_prorp");
        sel_cabecera_orden_prod.setWidth("80%");
        sel_cabecera_orden_prod.setHeight("70%");
        sel_cabecera_orden_prod.setRadio();
        sel_cabecera_orden_prod.getBot_aceptar().setMetodo("dibujaDetalleOrden");
        agregarComponente(sel_cabecera_orden_prod);
        
        sel_detalle_orden_prod.setId("sel_detalle_orden_prod");
        sel_detalle_orden_prod.setTitle("ORDEN DE PRODUCCION");
        sel_detalle_orden_prod.setSeleccionTabla(ser_produccion.getSqlDetalleOrdenProd("1", "", ""), "ide_prord");
        sel_detalle_orden_prod.setWidth("80%");
        sel_detalle_orden_prod.setHeight("70%");
        sel_detalle_orden_prod.getBot_aceptar().setMetodo("aceptaDetalleOrden");
        agregarComponente(sel_detalle_orden_prod);*/
        vipdf_r_m_p.setId("vipdf_r_m_p");
        vipdf_r_m_p.setTitle("REQUERIMIENTO MATERIA PRIMA");
        agregarComponente(vipdf_r_m_p);

        Boton bot_imprimir_nota = new Boton();
        bot_imprimir_nota.setValue("IMPRIMIR REPORTE");
        bot_imprimir_nota.setIcon("ui-icon-print");
        bot_imprimir_nota.setMetodo("generarPDFnota");
        bar_botones.agregarBoton(bot_imprimir_nota);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro que desea aprobar el siguiente Ingreso/Egreso de inventarios");
        con_confirma.setTitle("APROBAR INGRESO/EGRESO INVENTARIO");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        Boton bot_aprobar_ingreso = new Boton();
        bot_aprobar_ingreso.setValue("APROBAR");
        bot_aprobar_ingreso.setIcon("ui-icon-check");
        bot_aprobar_ingreso.setMetodo("aprobarIngreso");
        bar_botones.agregarBoton(bot_aprobar_ingreso);

    }

    public void aprobarIngreso() {
        TablaGenerica tab_consulta = utilitario.consultar(" select * from inv_det_comp_inve where ide_incci=" + tab_tabla1.getValor("ide_incci") + " ");
        if (tab_tabla1.getValor("ide_inepi").equals(utilitario.getVariable("p_inv_estado_aprobado"))) {
            utilitario.agregarMensajeInfo("Información", "El comprobante de invetario ya esta aprobada");
        } else if (tab_consulta.getTotalFilas() > 0) {
            con_confirma.getBot_aceptar().setMetodo("registrarInventario");
            utilitario.addUpdate("con_confirma");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Información", "Debe insertar productos ");
        }
    }

    public void registrarInventario() {
        TablaGenerica tab_fecha = utilitario.consultar(ser_inventario.getExtraerAnio(tab_tabla1.getValor("fecha_trans_incci")));
        TablaGenerica tab_anio = utilitario.consultar(ser_inventario.getInventarioAnio(tab_fecha.getValor("anio")));
        TablaGenerica tab_detalle = utilitario.consultar(ser_inventario.getDetalleInventario(tab_tabla1.getValor("ide_incci"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
        TablaGenerica tab_transaccion = utilitario.consultar(ser_inventario.getConsultarTipoTransaccion(tab_tabla1.getValor("ide_intti")));
        double costo_actual = 0;
        if (tab_transaccion.getValor("ide_intci").equals(utilitario.getVariable("p_inv_tipo_ingreso"))) {
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                TablaGenerica tab_kardex = utilitario.consultar(ser_inventario.getAplicaKardex(tab_detalle.getValor(i, "ide_inarti")));
                if (tab_kardex.getValor("hace_kardex_inarti").equals("true")) {
                    TablaGenerica tab_articulo = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                    if (tab_articulo.getTotalFilas() > 0) {
                        costo_actual = ser_inventario.getPrecioPonderado(Double.parseDouble(tab_articulo.getValor("stock")), Double.parseDouble(tab_articulo.getValor("costo_actual_boart")), Double.parseDouble(tab_detalle.getValor(i, "cantidad_indci")), Double.parseDouble(tab_detalle.getValor(i, "valor_indci")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarBodegaArticulos(tab_articulo.getValor("costo_actual_boart"), costo_actual, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarIngreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarDetalleStock(tab_arti2.getValor("stock"), costo_actual, tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), tab_tabla1.getValor("ide_incci")));
                    } else {
                        utilitario.getConexion().ejecutarSql(ser_inventario.getInsertarBodegaArticulos(tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR"), tab_detalle.getValor(i, "ide_inarti")));
                        TablaGenerica tab_articulos = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                        costo_actual = ser_inventario.getPrecioPonderado(Double.parseDouble(tab_articulos.getValor("stock")), Double.parseDouble(tab_articulos.getValor("costo_actual_boart")), Double.parseDouble(tab_detalle.getValor(i, "cantidad_indci")), Double.parseDouble(tab_detalle.getValor(i, "valor_indci")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarBodegaArticulos(tab_articulos.getValor("costo_actual_boart"), costo_actual, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarIngreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarDetalleStock(tab_arti2.getValor("stock"), costo_actual, tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), tab_tabla1.getValor("ide_incci")));

                    }
                }
            }
        } else if (tab_transaccion.getValor("ide_intci").equals(utilitario.getVariable("p_inv_tipo_egreso"))) {
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                TablaGenerica tab_kardex = utilitario.consultar(ser_inventario.getAplicaKardex(tab_detalle.getValor(i, "ide_inarti")));
                if (tab_kardex.getValor("hace_kardex_inarti").equals("true")) {
                    TablaGenerica tab_articulo = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                    if (tab_articulo.getTotalFilas() > 0) {
                        //costo_actual = ser_inventario.getPrecioPonderado(Double.parseDouble(tab_articulo.getValor("stock")), Double.parseDouble(tab_articulo.getValor("costo_actual_boart")), Double.parseDouble(tab_detalle.getValor(i, "cantidad_indci")), Double.parseDouble(tab_detalle.getValor(i, "valor_indci")));
                        //utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarBodegaArticulos(tab_articulo.getValor("costo_actual_boart"), costo_actual, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarEgreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarDetalleStock(tab_arti2.getValor("stock"), Double.parseDouble(tab_arti2.getValor("costo_actual_boart")), tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), tab_tabla1.getValor("ide_incci")));

                    } else {
                        utilitario.getConexion().ejecutarSql(ser_inventario.getInsertarBodegaArticulos(tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR"), tab_detalle.getValor(i, "ide_inarti")));
                        TablaGenerica tab_articulos = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                        //costo_actual = ser_inventario.getPrecioPonderado(Double.parseDouble(tab_articulos.getValor("stock")), Double.parseDouble(tab_articulos.getValor("costo_actual_boart")), Double.parseDouble(tab_detalle.getValor(i, "cantidad_indci")), Double.parseDouble(tab_detalle.getValor(i, "valor_indci")));
                        //utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarBodegaArticulos(tab_articulos.getValor("costo_actual_boart"), costo_actual, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarEgreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(ser_inventario.getBodtArticulo(tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarDetalleStock(tab_arti2.getValor("stock"), Double.parseDouble(tab_arti2.getValor("costo_actual_boart")), tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(ser_inventario.getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), tab_tabla1.getValor("ide_incci")));

                    }
                }
            }
        }
        con_confirma.cerrar();
        utilitario.addUpdateTabla(tab_tabla1, "ide_inepi", "");
        utilitario.agregarMensaje("Se aprobo correctamente", "");
    }

    public void generarPDFnota() {
        if (tab_tabla1.getValorSeleccionado() != null) {
            Map parametros = new HashMap();
            parametros.put("pide_requerimiento", Integer.parseInt(tab_tabla1.getValorSeleccionado()));
            parametros.put("pide_version", utilitario.getVariable("p_prod_version_documento"));
            parametros.put("pide_fecha", utilitario.getVariable("p_prod_fecha_documento"));
            //parametros.put("p_usuario", utilitario.getVariable("NICK"));
            vipdf_r_m_p.setVisualizarPDF("rep_produccion/rep_requerimiento_materia_prima.jasper", parametros);
            vipdf_r_m_p.dibujar();
            utilitario.addUpdate("vipdf_r_m_p");
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Nota de Materia Prima", "");
        }
    }

    public void dibujaCabeceraOrden() {
        sel_cabecera_orden_prod.dibujar();
    }

    public void dibujaDetalleOrden() {
        valor_orden = sel_cabecera_orden_prod.getValorSeleccionado();
        sel_cabecera_orden_prod.cerrar();
        sel_detalle_orden_prod.getTab_seleccion().setSql(ser_produccion.getSqlDetalleOrdenProd("2", valor_orden, ""));
        sel_detalle_orden_prod.getTab_seleccion().ejecutarSql();
        sel_detalle_orden_prod.dibujar();
    }

    public void aceptaDetalleOrden() {
        if (tab_tabla1.isFilaInsertada() == false) {
            tab_tabla1.insertar();
        }
        String valor_detalle = sel_detalle_orden_prod.getSeleccionados();
        TablaGenerica tab_detalle_ordenes = utilitario.consultar(ser_produccion.getSqlDetalleOrdenProd("3", valor_orden, valor_detalle));
        for (int i = 0; i < tab_detalle_ordenes.getTotalFilas(); i++) {
            tab_tabla3.insertar();
            tab_tabla3.setValor("ide_prord", tab_detalle_ordenes.getValor(i, "ide_prord"));
        }
        sel_detalle_orden_prod.cerrar();
    }

    public void dibujaSolicitud() {
        if (tab_tabla1.isFilaInsertada() == true) {
            sel_tab_solicitud.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar al menos un valor", "");
        }
    }

    public void aceptarSolicitud() {
        solicitud = sel_tab_solicitud.getValorSeleccionado();
        TablaGenerica tab_solicitud = utilitario.consultar(ser_produccion.getSolicitudMaterial("2", solicitud));
        tab_tabla1.setValor("CODIGO_DOCUMENTO2_INCCI", tab_solicitud.getValor("SECUENCIAL"));
        utilitario.addUpdate("tab_tabla1");
        sel_tab_solicitud.cerrar();
        /*sel_detalle_compra.getTab_seleccion().setSql(ser_adquisiciones.getdetalleFacturaCompra("2", factura));
        sel_detalle_compra.getTab_seleccion().ejecutarSql();   
        sel_detalle_compra.dibujar();*/
    }

    /*  public void generarCabecera(){
         TablaGenerica tab_fact_cabera = utilitario.consultar("select ide_cpcfa, a.ide_geper, nom_geper from cxp_cabece_factur a\n" +
                                                              "left join gen_persona b on a.ide_geper = b.ide_geper \n" +
                                                              "where ide_cpcfa = "+factura+"");
          for (int i=0; i < tab_fact_cabera.getTotalFilas(); i++ ){
              if (tab_tabla1.isFilaInsertada() == false){
                  tab_tabla1.insertar();
              }
              tab_tabla1.setValor("ide_geper",tab_fact_cabera.getValor(i, "ide_geper"));
          }
          // tab_tabla1.guardar();
          // guardarPantalla();
           sel_detalle_compra.cerrar();
	   utilitario.addUpdate("tab_tabla1");
           generaDetalle();
     }*/
 /* public void generaDetalle(){
         String selec_productos = sel_detalle_compra.getSeleccionados();
         TablaGenerica tab_detalle_fac = utilitario.consultar("select a.ide_cpdfa, b.ide_inarti, nombre_inarti, cantidad_cpdfa, precio_cpdfa, valor_cpdfa\n" +
                                                              "from cxp_detall_factur a\n" +
                                                              "left join inv_articulo b on a.ide_inarti = b.ide_inarti \n" +
                                                              "where a.ide_cpdfa in ("+selec_productos+")");
         System.out.println("factura" +factura);
         for (int i=0; i < tab_detalle_fac.getTotalFilas(); i++ ){
             tab_tabla2.insertar();
             tab_tabla2.setValor("ide_incci",tab_tabla1.getValor("ide_incci"));
             tab_tabla2.setValor("ide_inarti",tab_detalle_fac.getValor(i, "ide_inarti"));
             tab_tabla2.setValor("cantidad_indci",tab_detalle_fac.getValor(i, "cantidad_cpdfa"));
             tab_tabla2.setValor("precio_indci",tab_detalle_fac.getValor(i, "precio_cpdfa"));   
             tab_tabla2.setValor("valor_indci",tab_detalle_fac.getValor(i, "valor_cpdfa"));  
             utilitario.getConexion().ejecutarSql("update cxp_detall_factur set recibido_compra_cpdfa = true where ide_cpdfa in ("+selec_productos+")");
         }
         TablaGenerica tab_con_recibido = utilitario.consultar("select ide_cpdfa, recibido_compra_cpdfa \n" +
                                                               "from cxp_detall_factur where ide_cpcfa = "+factura+" \n" +
                                                               "and recibido_compra_cpdfa = false");
         if (tab_con_recibido.getTotalFilas()> 0){
             
         } else {
             utilitario.getConexion().ejecutarSql("update cxp_cabece_factur set recibido_compra_cpcfa = true where ide_cpcfa = "+factura+"");
         }
        // tab_tabla2.guardar();
        // guardarPantalla();
        utilitario.addUpdate("tab_tabla2");
     }*/
    public void buscarTransaccion() {
        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {
            tab_tabla1.setCondicion("ide_incci=" + tex_num_transaccion.getValue() + " and ide_intti =" + utilitario.getVariable("p_prod_requerimiento_materia_prima"));
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            ////tab_tabla2.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "numero_cpcfa", "ide_cpcfa=" + tab_tabla2.getValor("ide_cpcfa"));
            ////tab_tabla2.getColumna("ide_cccfa").setCombo("cxc_cabece_factura", "ide_cccfa", "secuencial_cccfa", "ide_cccfa=" + tab_tabla2.getValor("ide_cccfa"));
            utilitario.addUpdate("tab_tabla1,tab_tabla2");
        }
    }

    public void buscarnombTransaccion() {
        String val_text = tex_nomb_transaccion.getValue().toString();
        if (tex_nomb_transaccion.getValue() != null) {
            tab_tabla1.setCondicion("observacion_incci ilike '%" + tex_nomb_transaccion.getValue() + "%'" + " and ide_intti =" + utilitario.getVariable("p_prod_requerimiento_materia_prima"));
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            utilitario.addUpdate("tab_tabla1,tab_tabla2");
        }
    }

    public void calcularTotalDetalles(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularDetalles();
    }

    public void cargarPrecio(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        try {
            List<Double> lisSaldos = ser_producto.getSaldoPromedioProductoBodega(tab_tabla2.getValor("ide_inarti"), utilitario.getFechaActual(), tab_tabla1.getValor("ide_inbod"));
            double dou_precioi = lisSaldos.get(1);
            tab_tabla2.setValor("precio_indci", utilitario.getFormatoNumero(dou_precioi));
            utilitario.addUpdateTabla(tab_tabla2, "precio_indci", "");
            double dou_existencia = ser_producto.getCantidadProductoBodega(tab_tabla2.getValor("ide_inarti"), tab_tabla1.getValor("ide_inbod"));
            if (dou_existencia <= 0) {
                utilitario.agregarMensajeError("No hay existencia de " + tab_tabla2.getValorArreglo("ide_inarti", 1) + " en Bodega", "");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar precio " + e);
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
            utilitario.agregarMensajeError("La cantidad ingresada es mayor a la existencia en Inventario", "Existencia actual de " + tab_tabla2.getValorArreglo("ide_inarti", 1) + " es :" + utilitario.getFormatoNumero(dou_existencia, 3));
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
            TablaGenerica tab_secuen = utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_mod_requeri_materia_prima")));
            // ser_produccion.getSecuencialNumero(Integer.parseInt(tab_secuen.getValor("longitud_secuencial_gemos")), Integer.parseInt(tab_secuen.getValor("tamano")));
            String tipo_aplica = tab_secuen.getValor("aplica_abreviatura_gemos");
            tab_tabla1.setValor("numero_incci", ser_produccion.getSecuencialNumero(tipo_aplica, Integer.parseInt(tab_secuen.getValor("longitud_secuencial_gemos")), Integer.parseInt(tab_secuen.getValor("tamano"))) + tab_secuen.getValor("nuevo_secuencial"));
            tab_tabla1.setValor("ide_intti", utilitario.getVariable("p_prod_requerimiento_materia_prima"));

        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
            tab_tabla2.sumarColumnas();
        }
    }

    @Override
    public void guardar() {
        //if (validar()) {
        if (tab_tabla1.isFilaInsertada()) {
            utilitario.getConexion().ejecutarSql(ser_produccion.getActualizarSecuencial(utilitario.getVariable("p_prod_num_mod_requeri_materia_prima")));
            // tab_tabla1.setValor("numero_incci", ser_inventario.getSecuencialComprobanteInventario(String.valueOf(tab_tabla1.getValor("ide_inbod"))));
            //tab_tabla1.setValor("numero_incci", );

        }
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                utilitario.getConexion().guardarPantalla();
            }
        }
        // }
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
                utilitario.addUpdate("sel_arbol,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {

                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                // System.out.println("seleccion..de arbol..." + sel_arbol.getSeleccionados());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("sef_formato,sec_rango_reporte");
            }
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
                sel_arbol.dibujar();
                utilitario.addUpdate("sel_arbol,sec_rango_reporte");
            } else if (sel_arbol.isVisible()) {

//                if (sel_arbol.getSeleccionados() == null || sel_arbol.getSeleccionados().isEmpty()) {
//                    return;
//                }
                parametro.put("ide_inarti", sel_arbol.getSeleccionados());
                //System.out.println("seleccion..de ide_inarti...entras" + sel_arbol.getSeleccionados());
                sel_arbol.cerrar();
                sel_departamento.dibujar();
            } else if (sel_departamento.isVisible()) {

//                if (sel_departamento.getSeleccionados() == null || sel_departamento.getSeleccionados().isEmpty()) {
//                    return;
//                }
                parametro.put("ide_georg", sel_departamento.getSeleccionados());
                // System.out.println("seleccion..de ide_georg..." + sel_departamento.getSeleccionados());
                sel_departamento.cerrar();
                sel_empleado.dibujar();
            } else if (sel_empleado.isVisible()) {

//                if (sel_empleado.getSeleccionados() == null || sel_empleado.getSeleccionados().isEmpty()) {
//                    return;
//                }
                parametro.put("ide_geper", sel_empleado.getSeleccionados());
                parametro.put("informe_para", "Jimmy Massa");
                //  System.out.println("seleccion..de ide_geper..." + sel_empleado.getSeleccionados());
                sel_empleado.cerrar();
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

    public SeleccionTabla getSel_detalle_compra() {
        return sel_detalle_compra;
    }

    public void setSel_detalle_compra(SeleccionTabla sel_detalle_compra) {
        this.sel_detalle_compra = sel_detalle_compra;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public SeleccionTabla getSel_cabecera_orden_prod() {
        return sel_cabecera_orden_prod;
    }

    public void setSel_cabecera_orden_prod(SeleccionTabla sel_cabecera_orden_prod) {
        this.sel_cabecera_orden_prod = sel_cabecera_orden_prod;
    }

    public SeleccionTabla getSel_detalle_orden_prod() {
        return sel_detalle_orden_prod;
    }

    public void setSel_detalle_orden_prod(SeleccionTabla sel_detalle_orden_prod) {
        this.sel_detalle_orden_prod = sel_detalle_orden_prod;
    }

    public VisualizarPDF getVipdf_r_m_p() {
        return vipdf_r_m_p;
    }

    public void setVipdf_r_m_p(VisualizarPDF vipdf_r_m_p) {
        this.vipdf_r_m_p = vipdf_r_m_p;
    }

    public Map getParametro() {
        return parametro;
    }

    public void setParametro(Map parametro) {
        this.parametro = parametro;
    }

    public SeleccionTabla getSel_tab_solicitud() {
        return sel_tab_solicitud;
    }

    public void setSel_tab_solicitud(SeleccionTabla sel_tab_solicitud) {
        this.sel_tab_solicitud = sel_tab_solicitud;
    }

}

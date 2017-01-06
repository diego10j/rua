/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import componentes.AsientoContable;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_inventario extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private Tabla tab_tabla;
    private final Combo com_bodega = new Combo();
    private AsientoContable asc_asiento = new AsientoContable();

    private Tabla tab_tabla2;

    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    private AutoCompletar aut_productos;
    private String ide_incci_busca = null;

    public pre_inventario() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonEliminar();

        com_bodega.setId("com_bodega");
        com_bodega.setCombo("select ide_inbod,nombre_inbod from inv_bodega where nivel_inbod='HIJO' and ide_empr=" + utilitario.getVariable("ide_empr"));
        com_bodega.eliminarVacio();
        com_bodega.setMetodo("actualizarMovimientos");
        bar_botones.agregarComponente(new Etiqueta("BODEGA :"));
        bar_botones.agregarComponente(com_bodega);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);
        Boton bot_consultar = new Boton();
        bot_consultar.setMetodo("actualizarMovimientos");
        bot_consultar.setIcon("ui-icon-search");

        bar_botones.agregarBoton(bot_consultar);

        mep_menu.setMenuPanel("INVENTARIO", "22%");
        mep_menu.agregarItem("Lista de Comprobantes", "dibujarComprobantes", "ui-icon-note");//1
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarComprobantesNoConta", "ui-icon-notice");//2
        mep_menu.agregarItem("Kardex", "dibujarKardex", "ui-icon-note");//3
        mep_menu.agregarItem("Saldos Productos", "dibujarSaldos", "ui-icon-note");//4

        agregarComponente(mep_menu);
        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(asc_asiento);

    }

    public void dibujarComprobantes() {
        Grupo gru_grupo = new Grupo();
        if (isBodegaSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(6);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_inventario.getSqlComprobantesInventario(String.valueOf(com_bodega.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("INGRESO").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("INGRESO").alinearDerecha();
            tab_tabla.getColumna("EGRESO").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("EGRESO").alinearDerecha();
            tab_tabla.setCampoPrimaria("cod");
            tab_tabla.getColumna("cod").setVisible(false);
            tab_tabla.getColumna("ide_incci").setNombreVisual("NUM. COMP.");
            tab_tabla.getColumna("ide_incci").setLink();
            tab_tabla.getColumna("ide_incci").setMetodoChange("cargarComprobante");
            tab_tabla.getColumna("ide_incci").alinearCentro();
            tab_tabla.getColumna("ide_inepi").setVisible(false);
            tab_tabla.getColumna("ide_geper").setVisible(false);
            tab_tabla.getColumna("fecha_trans_incci").setNombreVisual("FECHA");
            tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
            tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO.");
            tab_tabla.getColumna("IDE_CNCCC").setLink();
            tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
            tab_tabla.getColumna("IDE_CNCCC").alinearCentro();
            tab_tabla.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
            tab_tabla.getColumna("numero_incci").setFiltroContenido();
            tab_tabla.getColumna("nombre_intti").setNombreVisual("TIPO TRANSACCIÓN");
            tab_tabla.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
            tab_tabla.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
            tab_tabla.setValueExpression("rowStyleClass", "fila.campos[1]  eq '0' ? 'text-red' : fila.campos[4] eq null  ? 'text-green' : null"); //poner variable anulado 0
            tab_tabla.setLectura(true);
            tab_tabla.setRows(25);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);

            if (tab_tabla.isEmpty()) {
                tab_tabla.setEmptyMessage("No existen Comprobantes de Inventario");
            }
        }
        mep_menu.dibujar(1, "LISTADO DE COMPROBANTES DE INVENTARIO", gru_grupo);
    }

    public void dibujarComprobantesNoConta() {
        Grupo gru_grupo = new Grupo();
        if (isBodegaSeleccionado()) {
            Barra bar_menu = new Barra();
            bar_menu.setId("bar_menu");
            bar_menu.limpiar();
            Boton bot_asi = new Boton();
            bot_asi.setValue("Generar Asiento Contable");
            bot_asi.setMetodo("abrirGeneraAsiento");
            bar_menu.agregarComponente(bot_asi);
            bar_menu.agregarSeparador();

            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(6);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_inventario.getSqlComprobantesInventarioNoConta(String.valueOf(com_bodega.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("INGRESO").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("INGRESO").alinearDerecha();
            tab_tabla.getColumna("EGRESO").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("EGRESO").alinearDerecha();
            tab_tabla.setCampoPrimaria("ide_incci");
            tab_tabla.getColumna("ide_incci").setNombreVisual("NUM. COMP.");
            tab_tabla.getColumna("ide_inepi").setVisible(false);
            tab_tabla.getColumna("ide_geper").setVisible(false);
            tab_tabla.getColumna("fecha_trans_incci").setNombreVisual("FECHA");
            tab_tabla.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
            tab_tabla.getColumna("numero_incci").setFiltroContenido();
            tab_tabla.getColumna("nombre_intti").setNombreVisual("TIPO TRANSACCIÓN");
            tab_tabla.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
            tab_tabla.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
            tab_tabla.setLectura(true);
            tab_tabla.setRows(25);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(bar_menu);
            gru_grupo.getChildren().add(pat_panel);

            if (tab_tabla.isEmpty()) {
                tab_tabla.setEmptyMessage("No existen Comprobantes de Inventario para Contabilizar");
            }
        }
        mep_menu.dibujar(2, "COMPROBANTES DE INVENTARIO NO CONTABILIZADOS", gru_grupo);
    }

    public void dibujarKardex() {
        Grupo gru_grupo = new Grupo();

        aut_productos = new AutoCompletar();
        aut_productos.setId("aut_productos");
        aut_productos.setAutoCompletar(ser_producto.getSqlProductosKardexCombo());
        aut_productos.setSize(75);
        aut_productos.setAutocompletarContenido(); // no startWith para la busqueda
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        bar_menu.agregarComponente(new Etiqueta("PRODUCTO :"));
        bar_menu.agregarComponente(aut_productos);

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarMovimientos");
        bot_consultar.setIcon("ui-icon-search");

        bar_menu.agregarComponente(bot_consultar);

        tab_tabla = new Tabla();
        tab_tabla.setNumeroTabla(-1);
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_producto.getSqlKardex("-1", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
        tab_tabla.getColumna("ide_indci").setVisible(false);
        tab_tabla.getColumna("fecha_trans_incci").setNombreVisual("FECHA");
        tab_tabla.getColumna("nom_geper").setNombreVisual("CLIENTE / PROVEEDOR");
        tab_tabla.getColumna("nombre_intti").setNombreVisual("TIPO DE TRANSACCIÓN");
        tab_tabla.getColumna("CANT_INGRESO").setNombreVisual("CANT. INGRESO");
        tab_tabla.getColumna("VUNI_INGRESO").setNombreVisual("V/U INGRESO");
        tab_tabla.getColumna("VTOT_INGRESO").setNombreVisual("TOTAL INGRESO");
        tab_tabla.getColumna("CANT_EGRESO").setNombreVisual("CANT. EGRESO");
        tab_tabla.getColumna("VUNI_EGRESO").setNombreVisual("V/U EGRESO");
        tab_tabla.getColumna("VTOT_EGRESO").setNombreVisual("TOTAL EGRESO");
        tab_tabla.getColumna("CANT_SALDO").setNombreVisual("CANT. SALDO");
        tab_tabla.getColumna("VUNI_SALDO").setNombreVisual("V/U SALDO");
        tab_tabla.getColumna("VTOT_SALDO").setNombreVisual("TOTAL SALDO");

        tab_tabla.getColumna("CANT_INGRESO").alinearDerecha();
        tab_tabla.getColumna("VUNI_INGRESO").alinearDerecha();
        tab_tabla.getColumna("VTOT_INGRESO").alinearDerecha();
        tab_tabla.getColumna("CANT_EGRESO").alinearDerecha();
        tab_tabla.getColumna("VUNI_EGRESO").alinearDerecha();
        tab_tabla.getColumna("VTOT_EGRESO").alinearDerecha();
        tab_tabla.getColumna("CANT_SALDO").alinearDerecha();
        tab_tabla.getColumna("VUNI_SALDO").alinearDerecha();
        tab_tabla.getColumna("VTOT_SALDO").alinearDerecha();

        tab_tabla.getColumna("CANT_INGRESO").setLongitud(20);
        tab_tabla.getColumna("VUNI_INGRESO").setLongitud(20);
        tab_tabla.getColumna("VTOT_INGRESO").setLongitud(20);
        tab_tabla.getColumna("CANT_EGRESO").setLongitud(20);
        tab_tabla.getColumna("VUNI_EGRESO").setLongitud(20);
        tab_tabla.getColumna("VTOT_EGRESO").setLongitud(20);
        tab_tabla.getColumna("CANT_SALDO").setLongitud(20);
        tab_tabla.getColumna("VUNI_SALDO").setLongitud(20);
        tab_tabla.getColumna("VTOT_SALDO").setLongitud(20);

        tab_tabla.getColumna("CANT_SALDO").setEstilo("font-weight: bold;");
        tab_tabla.getColumna("VUNI_SALDO").setEstilo("font-weight: bold;");
        tab_tabla.getColumna("VTOT_SALDO").setEstilo("font-weight: bold;");

        tab_tabla.getColumna("nombre_intti").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.setColumnaSuma("CANT_INGRESO,VTOT_INGRESO,CANT_EGRESO,VTOT_EGRESO,CANT_SALDO,VTOT_SALDO");
        tab_tabla.getColumna("CANT_SALDO").setSuma(false);
        tab_tabla.getColumna("VUNI_SALDO").setSuma(false);
        tab_tabla.getColumna("VTOT_SALDO").setSuma(false);
        tab_tabla.setOrdenar(false);
        tab_tabla.setLectura(true);
        tab_tabla.setScrollable(true);
        tab_tabla.setRows(25);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        gru_grupo.getChildren().add(bar_menu);
        gru_grupo.getChildren().add(pat_panel);
        calculaKardex();
        mep_menu.dibujar(3, "KARDEX - PROMEDIO PONDERADO", gru_grupo);
    }

    public void dibujarComprobanteInv() {
        Grupo gru_grupo = new Grupo();
        tab_tabla = new Tabla();
        tab_tabla2 = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("inv_cab_comp_inve", "ide_incci", 100);
        tab_tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
        tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setRequerida(true);
        tab_tabla.getColumna("ide_inepi").setCombo("inv_est_prev_inve", "ide_inepi", "nombre_inepi", "");
        tab_tabla.getColumna("ide_intti").setCombo("select ide_intti,nombre_intti,nombre_intci from inv_tip_tran_inve a\n"
                + "inner join inv_tip_comp_inve b on a.ide_intci=b.ide_intci\n"
                + "order by nombre_intci desc, nombre_intti");
        tab_tabla.getColumna("ide_inbod").setVisible(false);
        tab_tabla.getColumna("ide_intti").setRequerida(true);
        tab_tabla.getColumna("ide_inepi").setValorDefecto(utilitario.getVariable("p_inv_estado_normal"));
        tab_tabla.getColumna("fecha_trans_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("fecha_siste_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("hora_sistem_incci").setValorDefecto(utilitario.getHoraActual());
        tab_tabla.getColumna("numero_incci").setLectura(true);
        tab_tabla.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
        tab_tabla.getColumna("observacion_incci").setRequerida(true);
        tab_tabla.getColumna("observacion_incci").setControl("AreaTexto");
        tab_tabla.getColumna("sis_ide_usua").setVisible(false);
        tab_tabla.getColumna("fecha_siste_incci").setVisible(false);
        tab_tabla.getColumna("hora_sistem_incci").setVisible(false);
        tab_tabla.getColumna("fec_cam_est_incci").setVisible(false);
        tab_tabla.getColumna("fecha_efect_incci").setVisible(false);
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO.");
        tab_tabla.getColumna("IDE_CNCCC").setLink();
        tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.setRecuperarLectura(true);
        tab_tabla.setMostrarNumeroRegistros(false);
        if (ide_incci_busca == null) {
            tab_tabla.setCondicion("ide_incci=-1");
        } else {
            tab_tabla.setCondicion("ide_incci=" + ide_incci_busca);
        }
        tab_tabla.dibujar();
        if (ide_incci_busca == null) {
            tab_tabla.insertar();
            tab_tabla.setValor("ide_inbod", String.valueOf(com_bodega.getValue()));
        }
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("inv_det_comp_inve", "ide_indci", 200);
        tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "");
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("cantidad1_indci").setVisible(false);
        tab_tabla2.getColumna("ide_inarti").setMetodoChange("cargarPrecio");
        tab_tabla2.getColumna("cantidad_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla2.getColumna("precio_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla2.getColumna("cantidad_indci").setRequerida(true);
        tab_tabla2.getColumna("precio_indci").setRequerida(true);
        tab_tabla2.getColumna("ide_inarti").setRequerida(true);
        tab_tabla2.getColumna("valor_indci").setRequerida(true);
        tab_tabla2.getColumna("valor_indci").setEtiqueta();
        tab_tabla2.getColumna("valor_indci").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("referencia_indci").setVisible(false);
        tab_tabla2.getColumna("referencia1_indci").setVisible(false);
        tab_tabla2.setRows(10);
        tab_tabla2.getColumna("precio_promedio_indci").setVisible(false);
        tab_tabla2.setRecuperarLectura(true);
        if (ide_incci_busca == null) {
            tab_tabla2.getColumna("ide_cpcfa").setVisible(false);
            tab_tabla2.getColumna("ide_cccfa").setVisible(false);
        } else {
            tab_tabla2.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "numero_cpcfa", "ide_cpcfa=-1");
            tab_tabla2.getColumna("ide_cpcfa").setLectura(true);
            tab_tabla2.getColumna("ide_cccfa").setCombo("cxc_cabece_factura", "ide_cccfa", "secuencial_cccfa", "ide_cccfa=-1");
            tab_tabla2.getColumna("ide_cccfa").setLectura(true);
        }
        tab_tabla2.dibujar();
        if (ide_incci_busca == null) {
            tab_tabla2.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "numero_cpcfa", "ide_cpcfa=" + tab_tabla2.getValor("ide_cpcfa"));
            tab_tabla2.getColumna("ide_cccfa").setCombo("cxc_cabece_factura", "ide_cccfa", "secuencial_cccfa", "ide_cccfa=" + tab_tabla2.getValor("ide_cccfa"));
        }
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        if (ide_incci_busca != null) {
            pat_panel2.getMenuTabla().getItem_insertar().setDisabled(true);
            pat_panel2.getMenuTabla().getItem_eliminar().setDisabled(true);
        }
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);
        gru_grupo.getChildren().add(pat_panel1);
        gru_grupo.getChildren().add(pat_panel2);
        mep_menu.dibujar(5, "COMPROBANTE DE INVENTARIO", gru_grupo);
    }

    public void calcularTotalDetalles(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularDetalles();
    }

    public void cargarPrecio(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        List<Double> lisSaldos = ser_producto.getSaldoPromedioProductoBodega(tab_tabla2.getValor("ide_inarti"), utilitario.getFechaActual(), String.valueOf(com_bodega.getValue()));
        double dou_precioi = lisSaldos.get(1);
        tab_tabla2.setValor("precio_indci", utilitario.getFormatoNumero(dou_precioi));
        utilitario.addUpdateTabla(tab_tabla2, "precio_indci", "");
        double dou_existencia = ser_producto.getCantidadProductoBodega(tab_tabla2.getValor("ide_inarti"), String.valueOf(com_bodega.getValue()));
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
        double dou_existencia = ser_producto.getCantidadProductoBodega(tab_tabla2.getValor("ide_inarti"), String.valueOf(com_bodega.getValue()));
        if (dou_cantidad > dou_existencia) {
            utilitario.agregarMensajeError("La cantidad ingresada es mayor a la existencia en Inventario", "Existencia actual de " + tab_tabla2.getValorArreglo("ide_inarti", 1) + " es :" + utilitario.getFormatoNumero(dou_existencia));
        }

        dou_valor = dou_cantidad * dou_precio;
        tab_tabla2.setValor("valor_indci", utilitario.getFormatoNumero(dou_valor));
        tab_tabla2.sumarColumnas();
        utilitario.addUpdateTabla(tab_tabla2, "valor_indci", "");
    }

    /**
     * Calcula los saldos del kardex
     */
    private void calculaKardex() {
        List<Double> lisSaldos = ser_producto.getSaldosInicialesKardex(aut_productos.getValor(), cal_fecha_inicio.getFecha(), "");

        double dou_canti = lisSaldos.get(0);
        double dou_precioi = lisSaldos.get(1);
        double dou_saldoi = lisSaldos.get(2);

        double dou_cantf = dou_canti; //acumula
        double dou_preciof = dou_precioi;//acumula
        double dou_saldof = dou_saldoi;//acumula

        for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
            double dou_cant_fila = 0;
            double dou_precio_fila = 0;
            double dou_saldo_fila = 0;

            if (tab_tabla.getValor(i, "VTOT_INGRESO") != null && tab_tabla.getValor(i, "VTOT_INGRESO").isEmpty() == false) {
                try {
                    dou_cant_fila = Double.parseDouble(tab_tabla.getValor(i, "CANT_INGRESO"));
                    dou_precio_fila = Double.parseDouble(tab_tabla.getValor(i, "VUNI_INGRESO"));
                    dou_saldo_fila = Double.parseDouble(tab_tabla.getValor(i, "VTOT_INGRESO"));
                } catch (Exception e) {
                }
                if (i == 0 && dou_saldof == 0) {
                    dou_cantf += dou_cant_fila;
                    dou_preciof = dou_precio_fila;
                    dou_saldof = dou_cant_fila * dou_precio_fila;
                } else {
                    dou_cantf += dou_cant_fila;
                    dou_saldof += dou_saldo_fila;
                    dou_preciof = dou_saldof / dou_cantf;
                }
            } else if (tab_tabla.getValor(i, "VTOT_EGRESO") != null && tab_tabla.getValor(i, "VTOT_EGRESO").isEmpty() == false) {
                try {
                    dou_cant_fila = Double.parseDouble(tab_tabla.getValor(i, "CANT_EGRESO"));
                    dou_precio_fila = Double.parseDouble(tab_tabla.getValor(i, "VUNI_EGRESO"));
                    dou_saldo_fila = Double.parseDouble(tab_tabla.getValor(i, "VTOT_EGRESO"));
                } catch (Exception e) {
                }
                if (i == 0 && dou_saldof == 0) {
                    dou_cantf -= dou_cant_fila;
                    dou_preciof = dou_precio_fila;
                    dou_saldof = dou_cant_fila * dou_precio_fila;
                } else {
                    dou_cantf -= dou_cant_fila;
                    dou_saldof -= dou_saldo_fila;
                    dou_preciof = dou_saldof / dou_cantf;
                }

            }
            tab_tabla.setValor(i, "CANT_SALDO", utilitario.getFormatoNumero(dou_cantf));
            tab_tabla.setValor(i, "VUNI_SALDO", utilitario.getFormatoNumero(dou_preciof));
            tab_tabla.setValor(i, "VTOT_SALDO", utilitario.getFormatoNumero(dou_saldof));
        }

        if (dou_saldoi != 0) {
            tab_tabla.setLectura(false);
            tab_tabla.insertar();
            tab_tabla.setValor("CANT_SALDO", utilitario.getFormatoNumero(dou_canti));
            tab_tabla.setValor("VUNI_SALDO", utilitario.getFormatoNumero(dou_precioi));
            tab_tabla.setValor("VTOT_SALDO", utilitario.getFormatoNumero(dou_saldoi));
            tab_tabla.setValor("nom_geper", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_tabla.setValor("fecha_trans_incci", cal_fecha_inicio.getFecha());
            tab_tabla.setLectura(true);
        }
        tab_tabla.getColumna("CANT_SALDO").setTotal(dou_cantf);
        tab_tabla.getColumna("VUNI_SALDO").setTotal(dou_preciof);
        tab_tabla.getColumna("VTOT_SALDO").setTotal(dou_saldof);

    }

    public void dibujarSaldos() {
        Grupo gru_grupo = new Grupo();
        if (isBodegaSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(6);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_inventario.getSqlSaldoProductos(String.valueOf(com_bodega.getValue()), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("EXISTENCIA").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("EXISTENCIA").alinearDerecha();
            tab_tabla.getColumna("VALOR").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("VALOR").alinearDerecha();
            tab_tabla.setCampoPrimaria("ide_inarti");
            tab_tabla.getColumna("ide_inarti").setVisible(false);
            tab_tabla.getColumna("ARTICULO").setFiltroContenido();
            tab_tabla.setColumnaSuma("EXISTENCIA,VALOR");
            tab_tabla.setRows(25);
            tab_tabla.setLectura(true);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);
        }
        mep_menu.dibujar(4, "SALDOS DE PRODUCTOS INVENTARIO", gru_grupo);
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla.getFilasSeleccionadas() != null && tab_tabla.getFilasSeleccionadas().length() > 0) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.getTab_cabe_asiento().setValor("ide_geper", tab_tabla.getValor("ide_geper"));
            asc_asiento.getBot_aceptar().setMetodo("guardar");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos una Factura", "");
        }
    }

    public void actualizarMovimientos() {
        if (isBodegaSeleccionado()) {
            if (tab_tabla != null) {
                switch (mep_menu.getOpcion()) {
                    case 1:
                        tab_tabla.setSql(ser_inventario.getSqlComprobantesInventario(String.valueOf(com_bodega.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                        tab_tabla.ejecutarSql();
                        break;
                    case 2:
                        tab_tabla.setSql(ser_inventario.getSqlComprobantesInventarioNoConta(String.valueOf(com_bodega.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                        tab_tabla.ejecutarSql();
                        break;
                    case 3:
                        tab_tabla.setSql(ser_producto.getSqlKardex(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
                        tab_tabla.ejecutarSql();
                        calculaKardex();
                        break;
                    case 4:
                        tab_tabla.setSql(ser_inventario.getSqlSaldoProductos(String.valueOf(com_bodega.getValue()), cal_fecha_fin.getFecha()));
                        tab_tabla.ejecutarSql();
                        break;
                    default:
                        break;
                }
            }
        } else {
            mep_menu.limpiar();
        }
    }

    /**
     * Validacion para que se seleccione un Proveedor del Autocompletar
     *
     * @return
     */
    private boolean isBodegaSeleccionado() {
        if (com_bodega.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una Bodega", "");
            return false;
        }
        return true;
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    public void cargarComprobante(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        ide_incci_busca = lin_ide_cnccc.getValue().toString();
        dibujarComprobanteInv();
    }

    @Override
    public void insertar() {
        if (isBodegaSeleccionado()) {
            if (tab_tabla2 != null) {
                if (ide_incci_busca == null) {
                    if (utilitario.getTablaisFocus() != null && tab_tabla2.isFocus()) {
                        tab_tabla2.insertar();
                        return;
                    }
                }
            }
            ide_incci_busca = null;
            dibujarComprobanteInv();
        }

    }

    @Override
    public void guardar() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                dibujarComprobantes();
            }
        } else {

            if (tab_tabla.isFilaInsertada()) {
                if (validar()) {
                    tab_tabla.setValor("ide_inbod", String.valueOf(com_bodega.getValue()));
                    tab_tabla.setValor("numero_incci", ser_inventario.getSecuencialComprobanteInventario(String.valueOf(com_bodega.getValue())));
                } else {
                    return;
                }
            } else if (tab_tabla.isFilaModificada()) {
                if (!validar()) {
                    return;
                }
            }
            if (tab_tabla.guardar()) {
                String ide_incci = tab_tabla.getValor("ide_incci");
                if (tab_tabla2.guardar()) {
                    if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                        dibujarComprobantes();
                        tab_tabla.setFilaActual(ide_incci);
                        ide_incci_busca = null;
                    }
                }
            }

        }

    }

    public boolean validar() {
        if (tab_tabla.getValor("ide_geper") == null || tab_tabla.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar un Beneficiario");
            return false;
        }
        if (tab_tabla.getValor("ide_intti") == null || tab_tabla.getValor("ide_intti").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe seleccionar un tipo de transacción");
            return false;
        }
        if (tab_tabla.getValor("fecha_trans_incci") == null || tab_tabla.getValor("fecha_trans_incci").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe ingresar la fecha de la transacción");
            return false;
        }
        if (tab_tabla.getValor("observacion_incci") == null || tab_tabla.getValor("observacion_incci").isEmpty()) {
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
        if (com_bodega.getValue() == null) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe seleccionar una  bodega");
            return false;
        }
        return true;
    }

    @Override
    public void eliminar() {
        if (tab_tabla2 != null) {
            if (ide_incci_busca == null) {
                if (utilitario.getTablaisFocus() != null && tab_tabla2.isFocus()) {
                    tab_tabla2.eliminar();
                }
            }
        }
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public AutoCompletar getAut_productos() {
        return aut_productos;
    }

    public void setAut_productos(AutoCompletar aut_productos) {
        this.aut_productos = aut_productos;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelArbol;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.graficos.GraficoCartesiano;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_articulos extends Pantalla {

    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_productos = new AutoCompletar();
    //opcion 1
    private Tabla tab_producto;
    //opcion 2
    private Arbol arb_estructura;// Estructura Gerarquica de Productos
    //Kardex opcion 7
    private Tabla tab_kardex;
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;

    //Movimientos opcion 5
      /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    private AutoCompletar aut_cuentas;
    private Tabla tab_movimientos;
    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    private Tabla tab_asiento_tipo;
    private Tabla tab_detalle_asiento;

    //Opcion 3
    private Tabla tab_clientesFacturas;

    //Opcion 6
    private Tabla tab_proveedoresFacturas;

    //Opcion 8,9
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private Tabla tab_grafico;
    private GraficoCartesiano gca_grafico;
    private Combo com_periodo;

    //Opcion 10
    private Tabla tab_preciosVentas;
    private Tabla tab_preciosCompras;

    public pre_articulos() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarComponente(new Etiqueta("PRODUCTO :"));
        aut_productos.setId("aut_productos");
        aut_productos.setAutoCompletar(ser_producto.getSqlProductosCombo());
        aut_productos.setSize(75);
        aut_productos.setAutocompletarContenido(); // no startWith para la busqueda
        aut_productos.setMetodoChangeRuta("pre_index.clase.seleccionarProducto");
        bar_botones.agregarComponente(aut_productos);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        mep_menu.setMenuPanel("OPCIONES PRODUCTO", "20%");
        mep_menu.agregarItem("Información Producto", "dibujarProducto", "ui-icon-cart");
        mep_menu.agregarItem("Clasificación Productos", "dibujarEstructura", "ui-icon-arrow-4-diag");
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Kardex", "dibujarKardex", "ui-icon-contact");
        mep_menu.agregarItem("Facturas de Ventas", "dibujarVentas", "ui-icon-calculator");
        mep_menu.agregarItem("Facturas de Compras", "dibujarCompras", "ui-icon-calculator");
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Configura Cuenta Contable", "dibujarConfiguraCuenta", "ui-icon-wrench");
        mep_menu.agregarItem("Movimientos Contables", "dibujarMovimientos", "ui-icon-note");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Gráfico de Ventas", "dibujarGraficoVentas", "ui-icon-bookmark");
        mep_menu.agregarItem("Gráfico de Compras", "dibujarGraficoCompras", "ui-icon-bookmark");
        mep_menu.agregarItem("Últimos Precios", "dibujarPrecios", "ui-icon-cart");

        agregarComponente(mep_menu);

    }

    /**
     * Selecciona un Producto del Autocompletar
     *
     * @param evt
     */
    public void seleccionarProducto(SelectEvent evt) {
        aut_productos.onSelect(evt);
        if (aut_productos != null) {
            switch (mep_menu.getOpcion()) {
                case 1:
                    dibujarProducto();
                    break;
                case 2:
                    dibujarKardex();
                    break;
                case 3:
                    dibujarVentas();
                    break;
                case 4:
                    dibujarConfiguraCuenta();
                    break;
                case 5:
                    dibujarMovimientos();
                    break;
                case 6:
                    dibujarCompras();
                    break;
                case 7:
                    dibujarEstructura();
                    break;
                case 8:
                    dibujarGraficoVentas();
                    break;
                case 9:
                    dibujarGraficoCompras();
                    break;
                case 10:
                    dibujarPrecios();
                    break;
                default:
                    dibujarProducto();
            }
        } else {
            limpiar();
        }
    }

    /**
     * Dibuja el formulario de datos del Producto, opcion 1
     */
    public void dibujarProducto() {
        tab_producto = new Tabla();
        tab_producto.setId("tab_producto");
        ser_producto.configurarTablaProducto(tab_producto);
        tab_producto.setTabla("inv_articulo", "ide_inarti", 1);
        tab_producto.setCondicion("ide_inarti=" + aut_productos.getValor());
        tab_producto.setMostrarNumeroRegistros(false);
        tab_producto.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_producto);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(1, "DATOS DEL PRODUCTO", pat_panel);
    }

    /**
     * Arbol de Productos, opcion 7
     */
    public void dibujarEstructura() {
        Grupo gru_grupo = new Grupo();
        arb_estructura = new Arbol();
        arb_estructura.setId("arb_estructura");
        arb_estructura.setArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");
        arb_estructura.setCondicion("ide_inarti!=53"); //no activos fijos
        arb_estructura.setOptimiza(true);
        arb_estructura.dibujar();
        //Selecciona el nodo
        if (aut_productos.getValor() != null) {
            arb_estructura.seleccinarNodo(aut_productos.getValor());
            arb_estructura.getNodoSeleccionado().setExpanded(true);
            arb_estructura.getNodoSeleccionado().getParent().setExpanded(true);
        }
        PanelArbol paa_panel = new PanelArbol();
        paa_panel.setPanelArbol(arb_estructura);
        gru_grupo.getChildren().add(paa_panel);
        mep_menu.dibujar(7, "CLASIFICACIÓN DE PRODUCTOS", gru_grupo);
    }

    /**
     * Kardex, Opcion 2
     */
    public void dibujarKardex() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {

            Fieldset fis_consulta = new Fieldset();
            Grid gri_fechas = new Grid();
            gri_fechas.setColumns(5);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
            cal_fecha_inicio = new Calendario();
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            gri_fechas.getChildren().add(cal_fecha_inicio);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
            cal_fecha_fin = new Calendario();
            cal_fecha_fin.setFechaActual();
            gri_fechas.getChildren().add(cal_fecha_fin);
            fis_consulta.getChildren().add(gri_fechas);

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setMetodo("actualizarKardex");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);

            Separator separar = new Separator();
            fis_consulta.getChildren().add(separar);

            tab_kardex = new Tabla();
            tab_kardex.setNumeroTabla(-1);
            tab_kardex.setId("tab_kardex");
            tab_kardex.setSql(ser_producto.getSqlKardex(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
            tab_kardex.getColumna("ide_indci").setVisible(false);
            tab_kardex.getColumna("fecha_trans_incci").setNombreVisual("FECHA");
            tab_kardex.getColumna("nom_geper").setNombreVisual("CLIENTE / PROVEEDOR");
            tab_kardex.getColumna("nombre_intti").setNombreVisual("TIPO DE TRANSACCIÓN");
            tab_kardex.getColumna("CANT_INGRESO").setNombreVisual("CANT. INGRESO");
            tab_kardex.getColumna("VUNI_INGRESO").setNombreVisual("V/U INGRESO");
            tab_kardex.getColumna("VTOT_INGRESO").setNombreVisual("TOTAL INGRESO");
            tab_kardex.getColumna("CANT_EGRESO").setNombreVisual("CANT. EGRESO");
            tab_kardex.getColumna("VUNI_EGRESO").setNombreVisual("V/U EGRESO");
            tab_kardex.getColumna("VTOT_EGRESO").setNombreVisual("TOTAL EGRESO");
            tab_kardex.getColumna("CANT_SALDO").setNombreVisual("CANT. SALDO");
            tab_kardex.getColumna("VUNI_SALDO").setNombreVisual("V/U SALDO");
            tab_kardex.getColumna("VTOT_SALDO").setNombreVisual("TOTAL SALDO");

            tab_kardex.getColumna("CANT_INGRESO").alinearDerecha();
            tab_kardex.getColumna("VUNI_INGRESO").alinearDerecha();
            tab_kardex.getColumna("VTOT_INGRESO").alinearDerecha();
            tab_kardex.getColumna("CANT_EGRESO").alinearDerecha();
            tab_kardex.getColumna("VUNI_EGRESO").alinearDerecha();
            tab_kardex.getColumna("VTOT_EGRESO").alinearDerecha();
            tab_kardex.getColumna("CANT_SALDO").alinearDerecha();
            tab_kardex.getColumna("VUNI_SALDO").alinearDerecha();
            tab_kardex.getColumna("VTOT_SALDO").alinearDerecha();

            tab_kardex.getColumna("CANT_INGRESO").setLongitud(20);
            tab_kardex.getColumna("VUNI_INGRESO").setLongitud(20);
            tab_kardex.getColumna("VTOT_INGRESO").setLongitud(20);
            tab_kardex.getColumna("CANT_EGRESO").setLongitud(20);
            tab_kardex.getColumna("VUNI_EGRESO").setLongitud(20);
            tab_kardex.getColumna("VTOT_EGRESO").setLongitud(20);
            tab_kardex.getColumna("CANT_SALDO").setLongitud(20);
            tab_kardex.getColumna("VUNI_SALDO").setLongitud(20);
            tab_kardex.getColumna("VTOT_SALDO").setLongitud(20);

            tab_kardex.getColumna("CANT_SALDO").setEstilo("font-weight: bold;");
            tab_kardex.getColumna("VUNI_SALDO").setEstilo("font-weight: bold;");
            tab_kardex.getColumna("VTOT_SALDO").setEstilo("font-weight: bold;");

            tab_kardex.getColumna("nombre_intti").setFiltroContenido();
            tab_kardex.getColumna("nom_geper").setFiltroContenido();
            tab_kardex.setColumnaSuma("CANT_INGRESO,VTOT_INGRESO,CANT_EGRESO,VTOT_EGRESO,CANT_SALDO,VTOT_SALDO");
            tab_kardex.getColumna("CANT_SALDO").setSuma(false);
            tab_kardex.getColumna("VUNI_SALDO").setSuma(false);
            tab_kardex.getColumna("VTOT_SALDO").setSuma(false);
            tab_kardex.setOrdenar(false);
            tab_kardex.setLectura(true);
            tab_kardex.setScrollable(true);
            tab_kardex.setRows(20);
            tab_kardex.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_kardex);
            gru_grupo.getChildren().add(pat_panel);

            //actualizarSaldoxCobrar();
        }
        mep_menu.dibujar(2, "KARDEX", gru_grupo);
        calculaKardex();
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

        for (int i = 0; i < tab_kardex.getTotalFilas(); i++) {
            double dou_cant_fila = 0;
            double dou_precio_fila = 0;
            double dou_saldo_fila = 0;

            if (tab_kardex.getValor(i, "VTOT_INGRESO") != null && tab_kardex.getValor(i, "VTOT_INGRESO").isEmpty() == false) {
                try {
                    dou_cant_fila = Double.parseDouble(tab_kardex.getValor(i, "CANT_INGRESO"));
                    dou_precio_fila = Double.parseDouble(tab_kardex.getValor(i, "VUNI_INGRESO"));
                    dou_saldo_fila = Double.parseDouble(tab_kardex.getValor(i, "VTOT_INGRESO"));
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
            } else if (tab_kardex.getValor(i, "VTOT_EGRESO") != null && tab_kardex.getValor(i, "VTOT_EGRESO").isEmpty() == false) {
                try {
                    dou_cant_fila = Double.parseDouble(tab_kardex.getValor(i, "CANT_EGRESO"));
                    dou_precio_fila = Double.parseDouble(tab_kardex.getValor(i, "VUNI_EGRESO"));
                    dou_saldo_fila = Double.parseDouble(tab_kardex.getValor(i, "VTOT_EGRESO"));
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
            tab_kardex.setValor(i, "CANT_SALDO", utilitario.getFormatoNumero(dou_cantf));
            tab_kardex.setValor(i, "VUNI_SALDO", utilitario.getFormatoNumero(dou_preciof));
            tab_kardex.setValor(i, "VTOT_SALDO", utilitario.getFormatoNumero(dou_saldof));
        }

        if (dou_saldoi != 0) {
            tab_kardex.setLectura(false);
            tab_kardex.insertar();
            tab_kardex.setValor("CANT_SALDO", utilitario.getFormatoNumero(dou_canti));
            tab_kardex.setValor("VUNI_SALDO", utilitario.getFormatoNumero(dou_precioi));
            tab_kardex.setValor("VTOT_SALDO", utilitario.getFormatoNumero(dou_saldoi));
            tab_kardex.setValor("nom_geper", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_kardex.setValor("fecha_trans_incci", cal_fecha_inicio.getFecha());
            tab_kardex.setLectura(true);
        }
        tab_kardex.getColumna("CANT_SALDO").setTotal(dou_cantf);
        tab_kardex.getColumna("VUNI_SALDO").setTotal(dou_preciof);
        tab_kardex.getColumna("VTOT_SALDO").setTotal(dou_saldof);

    }

    public void dibujarMovimientos() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {
            TablaGenerica tab_cuenta = ser_contabilidad.getCuenta(ser_producto.getCuentaProducto(aut_productos.getValor()));
            if (!tab_cuenta.isEmpty()) {
                Fieldset fis_consulta = new Fieldset();
                fis_consulta.getChildren().add(new Etiqueta("<p style='font-size:16px;padding-bottom:5px;'> <strong>" + tab_cuenta.getValor("codig_recur_cndpc") + "</strong> &nbsp; " + tab_cuenta.getValor("nombre_cndpc") + "</p>"));
                Grid gri_fechas = new Grid();
                gri_fechas.setColumns(5);
                gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
                cal_fecha_inicio = new Calendario();
                cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
                gri_fechas.getChildren().add(cal_fecha_inicio);
                gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
                cal_fecha_fin = new Calendario();
                cal_fecha_fin.setFechaActual();
                gri_fechas.getChildren().add(cal_fecha_fin);
                fis_consulta.getChildren().add(gri_fechas);

                Boton bot_consultar = new Boton();
                bot_consultar.setValue("Consultar");
                bot_consultar.setMetodo("actualizarMovimientos");
                bot_consultar.setIcon("ui-icon-search");

                gri_fechas.getChildren().add(bot_consultar);

                Separator separar = new Separator();
                fis_consulta.getChildren().add(separar);

                gru_grupo.getChildren().add(fis_consulta);

                tab_movimientos = new Tabla();
                tab_movimientos.setNumeroTabla(4);
                tab_movimientos.setId("tab_movimientos");
                tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_producto.getCuentaProducto(aut_productos.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                tab_movimientos.setLectura(true);
                tab_movimientos.getColumna("ide_cnccc").setNombreVisual("N. COMP.");
                tab_movimientos.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
                tab_movimientos.getColumna("ide_cnlap").setVisible(false);
                tab_movimientos.getColumna("debe").setLongitud(20);
                tab_movimientos.getColumna("haber").setLongitud(20);
                tab_movimientos.getColumna("saldo").setLongitud(25);
                tab_movimientos.getColumna("debe").alinearDerecha();
                tab_movimientos.getColumna("haber").alinearDerecha();
                tab_movimientos.getColumna("saldo").alinearDerecha();
                tab_movimientos.getColumna("saldo").setEstilo("font-weight: bold;");
                tab_movimientos.getColumna("valor_cndcc").setVisible(false);
                tab_movimientos.getColumna("debe").setSuma(false);
                tab_movimientos.getColumna("haber").setSuma(false);
                tab_movimientos.getColumna("saldo").setSuma(false);
                tab_movimientos.setColumnaSuma("debe,haber,saldo");
                tab_movimientos.setRows(20);
                tab_movimientos.setOrdenar(false);
                tab_movimientos.dibujar();
                PanelTabla pat_panel = new PanelTabla();
                pat_panel.setPanelTabla(tab_movimientos);
                gru_grupo.getChildren().add(pat_panel);
                actualizarSaldosContable();
            } else {
                utilitario.agregarMensajeInfo("No se encontro Cuenta Contable", "El Producto seleccionado no tiene asociado una cuenta contable");
            }
        }
        mep_menu.dibujar(5, "MOVIMIENTOS CONTABLES", gru_grupo);
    }

    public void dibujarConfiguraCuenta() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {
            aut_cuentas = new AutoCompletar();
            aut_cuentas.setId("aut_cuentas");
            aut_cuentas.setAutoCompletar(ser_contabilidad.getSqlCuentas());
            aut_cuentas.setSize(75);
            aut_cuentas.setDisabled(true);
            aut_cuentas.setMetodoChange("seleccionarCuentaContable");
            aut_cuentas.setValor(ser_producto.getCuentaProducto(aut_productos.getValor()));

            Grid gri_contenido = new Grid();
            gri_contenido.setColumns(3);
            gri_contenido.getChildren().add(new Etiqueta("<strong>CUENTA CONTABLE : </strong>"));
            gri_contenido.getChildren().add(aut_cuentas);
            Boton bot_cambia = new Boton();
            bot_cambia.setValue("Cambiar");
            bot_cambia.setIcon("ui-icon-refresh");
            bot_cambia.setMetodo("activaCambiarCuenta");
            gri_contenido.getChildren().add(bot_cambia);
            gru_grupo.getChildren().add(gri_contenido);

            tab_asiento_tipo = new Tabla();
            tab_detalle_asiento = new Tabla();
            tab_asiento_tipo.setId("tab_asiento_tipo");
            tab_asiento_tipo.setSql("SELECT ide_conac,detalle_conac,nom_modu from cont_nombre_asiento_contable a\n"
                    + "inner join sis_modulo b on a.ide_modu=b.ide_modu\n"
                    + "WHERE activo_conac=true order by detalle_conac");
            tab_asiento_tipo.setLectura(true);
            tab_asiento_tipo.setScrollable(true);
            tab_asiento_tipo.setScrollHeight(150);
            tab_asiento_tipo.agregarRelacion(tab_detalle_asiento);
            tab_asiento_tipo.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_asiento_tipo);
            gru_grupo.getChildren().add(pat_panel1);
            gru_grupo.getChildren().add(new Separator());

            tab_detalle_asiento.setId("tab_detalle_asiento");
            tab_detalle_asiento.setTabla("cont_asiento_tipo", "ide_coast", -1);
            tab_detalle_asiento.getColumna("ide_cndpc").setCombo(ser_contabilidad.getSqlCuentas());
            tab_detalle_asiento.getColumna("ide_cndpc").setAutoCompletar();
            tab_detalle_asiento.getColumna("ide_cndpc").setRequerida(true);
            tab_detalle_asiento.getColumna("ide_cnlap").setCombo(ser_comprobante.getSqlLugarAplica());
            tab_detalle_asiento.getColumna("ide_cnlap").setPermitirNullCombo(false);
            tab_detalle_asiento.getColumna("ide_cnlap").setLongitud(10);
            tab_detalle_asiento.getColumna("ide_inarti").setValorDefecto(aut_productos.getValor());
            tab_detalle_asiento.getColumna("ide_inarti").setVisible(false);

            tab_detalle_asiento.getColumna("ide_prcla").setCombo("pre_clasificador", "ide_prcla", "codigo_clasificador_prcla,descripcion_clasificador_prcla", "");
            tab_detalle_asiento.getColumna("ide_prcla").setAutoCompletar();
            tab_detalle_asiento.getColumna("ide_prmop").setCombo("pre_movimiento_presupuestario", "ide_prmop", "detalle_prmop", "");
            tab_detalle_asiento.setCondicion("ide_inarti=" + aut_productos.getValor() + " or ide_inarti is null");
            tab_detalle_asiento.setRecuperarLectura(true);
            tab_detalle_asiento.setCampoOrden("ide_inarti desc");
            tab_detalle_asiento.dibujar();
            for (int i = 0; i < tab_detalle_asiento.getTotalFilas(); i++) {
                if (tab_detalle_asiento.getValor(i, "ide_inarti") != null) {
                    if (tab_detalle_asiento.getValor(i, "ide_inarti").equals(aut_productos.getValor())) {
                        tab_detalle_asiento.getFila(i).setLectura(false);
                        break;
                    }
                }
            }

            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_detalle_asiento);
            gru_grupo.getChildren().add(pat_panel2);

        }
        mep_menu.dibujar(4, "CONFIGURACIÓN DE CUENTA CONTABLE", gru_grupo);
    }

    public void dibujarVentas() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {

            Fieldset fis_consulta = new Fieldset();

            Grid gri_fechas = new Grid();
            gri_fechas.setColumns(5);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
            cal_fecha_inicio = new Calendario();
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            gri_fechas.getChildren().add(cal_fecha_inicio);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
            cal_fecha_fin = new Calendario();
            cal_fecha_fin.setFechaActual();
            gri_fechas.getChildren().add(cal_fecha_fin);
            fis_consulta.getChildren().add(gri_fechas);

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setMetodo("actualizarVentas");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);
            tab_clientesFacturas = new Tabla();
            tab_clientesFacturas.setNumeroTabla(3);
            tab_clientesFacturas.setId("tab_clientesFacturas");
            tab_clientesFacturas.setSql(ser_producto.getSqlVentasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_clientesFacturas.getColumna("ide_ccdfa").setVisible(false);
            tab_clientesFacturas.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
            tab_clientesFacturas.getColumna("serie_ccdaf").setNombreVisual("N. SERIE");
            tab_clientesFacturas.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_clientesFacturas.getColumna("nom_geper").setNombreVisual("CLIENTE");
            tab_clientesFacturas.getColumna("cantidad_ccdfa").setNombreVisual("CANTIDAD");
            tab_clientesFacturas.getColumna("precio_ccdfa").setNombreVisual("PRECIO UNI.");
            tab_clientesFacturas.getColumna("total_ccdfa").setNombreVisual("TOTAL");
            tab_clientesFacturas.getColumna("precio_ccdfa").alinearDerecha();
            tab_clientesFacturas.getColumna("total_ccdfa").alinearDerecha();
            tab_clientesFacturas.getColumna("total_ccdfa").setEstilo("font-weight: bold");
            tab_clientesFacturas.getColumna("nom_geper").setFiltroContenido();
            tab_clientesFacturas.getColumna("secuencial_cccfa").setFiltroContenido();
            tab_clientesFacturas.setLectura(true);
            tab_clientesFacturas.setRows(20);
            tab_clientesFacturas.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_clientesFacturas);
            gru_grupo.getChildren().add(pat_panel);
        }

        mep_menu.dibujar(3, "DETALLE DE VENTAS DEL PRODUCTO", gru_grupo);
    }

    public void dibujarCompras() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {
            Fieldset fis_consulta = new Fieldset();

            Grid gri_fechas = new Grid();
            gri_fechas.setColumns(5);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
            cal_fecha_inicio = new Calendario();
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            gri_fechas.getChildren().add(cal_fecha_inicio);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
            cal_fecha_fin = new Calendario();
            cal_fecha_fin.setFechaActual();
            gri_fechas.getChildren().add(cal_fecha_fin);
            fis_consulta.getChildren().add(gri_fechas);

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setMetodo("actualizarCompras");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);
            tab_proveedoresFacturas = new Tabla();
            tab_proveedoresFacturas.setNumeroTabla(4);
            tab_proveedoresFacturas.setId("tab_proveedoresFacturas");
            tab_proveedoresFacturas.setSql(ser_producto.getSqlComprasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_proveedoresFacturas.getColumna("ide_cpdfa").setVisible(false);
            tab_proveedoresFacturas.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
            tab_proveedoresFacturas.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
            tab_proveedoresFacturas.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
            tab_proveedoresFacturas.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
            tab_proveedoresFacturas.getColumna("precio_cpdfa").setNombreVisual("PRECIO UNI.");
            tab_proveedoresFacturas.getColumna("valor_cpdfa").setNombreVisual("TOTAL");
            tab_proveedoresFacturas.getColumna("precio_cpdfa").alinearDerecha();
            tab_proveedoresFacturas.getColumna("valor_cpdfa").alinearDerecha();
            tab_proveedoresFacturas.getColumna("valor_cpdfa").setEstilo("font-weight: bold");
            tab_proveedoresFacturas.getColumna("nom_geper").setFiltroContenido();
            tab_proveedoresFacturas.getColumna("numero_cpcfa").setFiltroContenido();
            tab_proveedoresFacturas.setLectura(true);
            tab_proveedoresFacturas.setRows(20);
            tab_proveedoresFacturas.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_proveedoresFacturas);
            gru_grupo.getChildren().add(pat_panel);
        }

        mep_menu.dibujar(6, "DETALLE DE COMPRAS DEL PRODUCTO", gru_grupo);
    }

    public void dibujarGraficoVentas() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {
            gca_grafico = new GraficoCartesiano();
            gca_grafico.setId("gca_grafico");

            com_periodo = new Combo();
            com_periodo.setMetodo("actualizarGraficoVentas");
            com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
            com_periodo.eliminarVacio();
            com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

            tab_grafico = new Tabla();
            tab_grafico.setId("tab_grafico");
            tab_grafico.setSql(ser_producto.getSqlTotalVentasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
            tab_grafico.setLectura(true);
            tab_grafico.setColumnaSuma("num_facturas,cantidad,total");
            tab_grafico.getColumna("cantidad").alinearDerecha();
            tab_grafico.getColumna("total").alinearDerecha();
            tab_grafico.getColumna("total").setNombreVisual("VALOR TOTAL");
            tab_grafico.dibujar();

            Grid gri_opciones = new Grid();
            gri_opciones.setColumns(2);
            gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
            gri_opciones.getChildren().add(com_periodo);
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.getChildren().add(gri_opciones);
            pat_panel.setPanelTabla(tab_grafico);

            gca_grafico.setTitulo("VENTAS MENSUALES");
            gca_grafico.agregarSerie(tab_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));

            gru_grupo.getChildren().add(pat_panel);
            gru_grupo.getChildren().add(gca_grafico);
        }
        mep_menu.dibujar(8, "GRAFICO DE VENTAS", gru_grupo);
    }

    public void dibujarGraficoCompras() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {
            gca_grafico = new GraficoCartesiano();
            gca_grafico.setId("gca_grafico");

            com_periodo = new Combo();
            com_periodo.setMetodo("actualizarGraficoCompras");
            com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
            com_periodo.eliminarVacio();
            com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

            tab_grafico = new Tabla();
            tab_grafico.setId("tab_grafico");
            tab_grafico.setSql(ser_producto.getSqlTotalComprasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
            tab_grafico.setLectura(true);
            tab_grafico.setColumnaSuma("num_facturas,cantidad,total");
            tab_grafico.getColumna("cantidad").alinearDerecha();
            tab_grafico.getColumna("total").alinearDerecha();
            tab_grafico.getColumna("total").setNombreVisual("VALOR TOTAL");
            tab_grafico.dibujar();

            Grid gri_opciones = new Grid();
            gri_opciones.setColumns(2);
            gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
            gri_opciones.getChildren().add(com_periodo);
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.getChildren().add(gri_opciones);
            pat_panel.setPanelTabla(tab_grafico);

            gca_grafico.setTitulo("COMPRAS MENSUALES");
            gca_grafico.agregarSerie(tab_grafico, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));

            gru_grupo.getChildren().add(pat_panel);
            gru_grupo.getChildren().add(gca_grafico);
        }
        mep_menu.dibujar(9, "GRAFICO DE COMPRAS", gru_grupo);
    }

    public void dibujarPrecios() {
        Grupo gru_grupo = new Grupo();
        if (isProductoSeleccionado()) {
            Tabulador tab_tabulador = new Tabulador();
            tab_tabulador.setId("tab_tabulador");

            tab_preciosVentas = new Tabla();
            tab_preciosVentas.setId("tab_preciosVentas");
            tab_preciosVentas.setIdCompleto("tab_tabulador:tab_preciosVentas");
            tab_preciosVentas.setSql(ser_producto.getSqlUltimosPreciosVentas(aut_productos.getValor()));
            tab_preciosVentas.getColumna("ide_geper").setVisible(false);
            tab_preciosVentas.setRows(20);
            tab_preciosVentas.getColumna("nom_geper").setNombreVisual("CLIENTE");
            tab_preciosVentas.getColumna("nom_geper").setFiltroContenido();
            tab_preciosVentas.getColumna("nom_geper").setLongitud(200);
            tab_preciosVentas.getColumna("fecha_ultima_venta").setNombreVisual("FECHA ULTIMA VENTA");
            tab_preciosVentas.getColumna("fecha_ultima_venta").setLongitud(80);
            tab_preciosVentas.getColumna("ultima_cantidad").setNombreVisual("CANTIDAD");
            tab_preciosVentas.getColumna("ultima_cantidad").setFormatoNumero(2);
            tab_preciosVentas.getColumna("ultima_cantidad").alinearDerecha();
            tab_preciosVentas.getColumna("ultima_cantidad").setLongitud(100);
            tab_preciosVentas.getColumna("ultima_cantidad").setEstilo("font-size:14px");

            tab_preciosVentas.getColumna("ultimo_precio").setNombreVisual("PRECIO");
            tab_preciosVentas.getColumna("ultimo_precio").setFormatoNumero(2);
            tab_preciosVentas.getColumna("ultimo_precio").alinearDerecha();
            tab_preciosVentas.getColumna("ultimo_precio").setLongitud(100);
            tab_preciosVentas.getColumna("ultimo_precio").setEstilo("font-weight: bold;font-size:14px");

            tab_preciosVentas.getColumna("valor_total").setNombreVisual("VALOR");
            tab_preciosVentas.getColumna("valor_total").setFormatoNumero(2);
            tab_preciosVentas.getColumna("valor_total").alinearDerecha();
            tab_preciosVentas.getColumna("valor_total").setLongitud(100);
            tab_preciosVentas.getColumna("valor_total").setEstilo("font-size:14px");

            tab_preciosVentas.setLectura(true);
            tab_preciosVentas.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_preciosVentas);

            tab_tabulador.agregarTab("VENTAS", pat_panel1);

            tab_preciosCompras = new Tabla();
            tab_preciosCompras.setId("tab_preciosCompras");
            tab_preciosCompras.setIdCompleto("tab_tabulador:tab_preciosCompras");
            tab_preciosCompras.setSql(ser_producto.getSqlUltimosPreciosCompras(aut_productos.getValor()));
            tab_preciosCompras.getColumna("ide_geper").setVisible(false);
            tab_preciosCompras.setRows(20);
            tab_preciosCompras.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
            tab_preciosCompras.getColumna("nom_geper").setFiltroContenido();
            tab_preciosCompras.getColumna("nom_geper").setLongitud(200);
            tab_preciosCompras.getColumna("fecha_ultima_venta").setNombreVisual("FECHA ULTIMA COMPRA");
            tab_preciosCompras.getColumna("fecha_ultima_venta").setLongitud(80);
            tab_preciosCompras.getColumna("ultima_cantidad").setNombreVisual("CANTIDAD");
            tab_preciosCompras.getColumna("ultima_cantidad").setFormatoNumero(2);
            tab_preciosCompras.getColumna("ultima_cantidad").alinearDerecha();
            tab_preciosCompras.getColumna("ultima_cantidad").setLongitud(100);
            tab_preciosCompras.getColumna("ultima_cantidad").setEstilo("font-size:14px");

            tab_preciosCompras.getColumna("ultimo_precio").setNombreVisual("PRECIO");
            tab_preciosCompras.getColumna("ultimo_precio").setFormatoNumero(2);
            tab_preciosCompras.getColumna("ultimo_precio").alinearDerecha();
            tab_preciosCompras.getColumna("ultimo_precio").setLongitud(100);
            tab_preciosCompras.getColumna("ultimo_precio").setEstilo("font-weight: bold;font-size:14px");

            tab_preciosCompras.getColumna("valor_total").setNombreVisual("VALOR");
            tab_preciosCompras.getColumna("valor_total").setFormatoNumero(2);
            tab_preciosCompras.getColumna("valor_total").alinearDerecha();
            tab_preciosCompras.getColumna("valor_total").setLongitud(100);
            tab_preciosCompras.getColumna("valor_total").setEstilo("font-size:14px");

            tab_preciosCompras.setLectura(true);
            tab_preciosCompras.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_preciosCompras);

            tab_tabulador.agregarTab("COMPRAS", pat_panel2);

            gru_grupo.getChildren().add(tab_tabulador);

        }
        mep_menu.dibujar(10, "ÚLTIMOS PRECIOS", gru_grupo);
    }

    public void actualizarGraficoVentas() {
        tab_grafico.setSql(ser_producto.getSqlTotalVentasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
        tab_grafico.ejecutarSql();
        gca_grafico.limpiar();
        gca_grafico.agregarSerie(tab_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
        utilitario.addUpdate("gca_grafico");
    }

    public void actualizarGraficoCompras() {
        tab_grafico.setSql(ser_producto.getSqlTotalComprasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
        tab_grafico.ejecutarSql();
        gca_grafico.limpiar();
        gca_grafico.agregarSerie(tab_grafico, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));
        utilitario.addUpdate("gca_grafico");
    }

    /**
     * Selecciona una nueva cuenta contable, y agrega el SQL para que pueda ser
     * guardado
     *
     * @param evt
     */
    public void seleccionarCuentaContable(SelectEvent evt) {
        aut_cuentas.onSelect(evt);
        if (isProductoSeleccionado()) {
            if (aut_cuentas.getValor() != null) {
                utilitario.getConexion().getSqlPantalla().clear();
                if (ser_producto.isTieneCuentaConfiguradaProducto(aut_productos.getValor()) == false) {
                    //nueva
                    utilitario.getConexion().agregarSqlPantalla(ser_producto.getSqlInsertarCuentaProducto(aut_productos.getValor(), aut_cuentas.getValor()));
                } else {
                    //modifica
                    utilitario.getConexion().agregarSqlPantalla(ser_producto.getSqlActualizarCuentaProducto(aut_productos.getValor(), aut_cuentas.getValor()));
                }
            }
        }

    }

    /**
     * Actualiza compras del producto seleccionado selecionadas
     */
    public void actualizarCompras() {
        tab_proveedoresFacturas.setSql(ser_producto.getSqlComprasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_proveedoresFacturas.ejecutarSql();
        if (tab_proveedoresFacturas.isEmpty()) {
            tab_proveedoresFacturas.setEmptyMessage("No existen Compras en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza ventas del producto seleccionado selecionadas
     */
    public void actualizarVentas() {
        tab_clientesFacturas.setSql(ser_producto.getSqlVentasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_clientesFacturas.ejecutarSql();
        if (tab_clientesFacturas.isEmpty()) {
            tab_clientesFacturas.setEmptyMessage("No existen Ventas en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza el kardex de un producto segun las fechas selecionadas
     */
    public void actualizarKardex() {
        tab_kardex.setSql(ser_producto.getSqlKardex(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
        tab_kardex.ejecutarSql();
        // actualizarSaldoxCobrar();
        calculaKardex();
        utilitario.addUpdate("tex_saldo_final,tex_total_debe,tex_saldo_inicial");
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_producto.getCuentaProducto(aut_productos.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_movimientos.ejecutarSql();
        actualizarSaldosContable();
    }

    /**
     * *
     * Activa el Autocompletar para cambiar la cuenta contable
     */
    public void activaCambiarCuenta() {
        if (isProductoSeleccionado()) {
            aut_cuentas.setDisabled(false);
            utilitario.addUpdate("aut_cuentas");
        }
    }

    /**
     * Actualiza los solados que se visualizan en pantalla
     */
    private void actualizarSaldosContable() {

        double saldo_anterior = ser_contabilidad.getSaldoInicialCuenta(ser_producto.getCuentaProducto(aut_productos.getValor()), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_debe = 0;
        double dou_haber = 0;
        String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");

        for (int i = 0; i < tab_movimientos.getTotalFilas(); i++) {
            if (tab_movimientos.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                tab_movimientos.setValor(i, "debe", utilitario.getFormatoNumero(tab_movimientos.getValor(i, "valor_cndcc")));
                dou_debe += Double.parseDouble(tab_movimientos.getValor(i, "debe"));

            } else {
                tab_movimientos.setValor(i, "haber", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_movimientos.getValor(i, "valor_cndcc")))));
                dou_haber += Double.parseDouble(tab_movimientos.getValor(i, "haber"));
            }
            dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_movimientos.getValor(i, "valor_cndcc"));
            tab_movimientos.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));

            saldo_anterior = dou_saldo_actual;
        }
        if (tab_movimientos.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_movimientos.setEmptyMessage("No existen Movimientos Contables en el rango de fechas seleccionado");
        }

        //INSERTA PRIMERA FILA SALDO INICIAL
        if (dou_saldo_inicial != 0) {
            tab_movimientos.setLectura(false);
            tab_movimientos.insertar();
            tab_movimientos.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_movimientos.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_movimientos.setValor("fecha_trans_cnccc", cal_fecha_inicio.getFecha());
            tab_movimientos.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_movimientos.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_movimientos.getColumna("debe").setTotal(dou_debe);
        tab_movimientos.getColumna("haber").setTotal(dou_haber);
    }

    public void limpiar() {
        aut_productos.limpiar();
        mep_menu.limpiar();
    }

    /**
     * Validacion para que se seleccione un Producto del Autocompletar
     *
     * @return
     */
    private boolean isProductoSeleccionado() {
        if (aut_productos.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Producto", "Seleccione un producto de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    @Override
    public void insertar() {
        aut_productos.limpiar();
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO PRODUCTO
            tab_producto.limpiar();
            tab_producto.insertar();
        } else if (mep_menu.getOpcion() == 4) {
            tab_detalle_asiento.insertar();
        } else {
            dibujarProducto();
            tab_producto.insertar();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO PRODUCTO
            if (true) { //!!!!!!!!******Validar Dtaoss Producto
                tab_producto.guardar();
                if (guardarPantalla().isEmpty()) {
                    //Actualiza el autocompletar
                    aut_productos.actualizar();
                    aut_productos.setSize(75);
                    aut_productos.setValor(tab_producto.getValorSeleccionado());
                    utilitario.addUpdate("aut_productos");
                }
            }
        } else if (mep_menu.getOpcion() == 4) {
            tab_detalle_asiento.guardar();
            //Cambiar Cuenta Contable
            if (guardarPantalla().isEmpty()) {
                aut_cuentas.setDisabled(true);
            }
        } else if (mep_menu.getOpcion() == 7) {
            //Classificacion de Productos
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO PRODUCTO
            tab_producto.eliminar();
        }
    }

    public AutoCompletar getAut_productos() {
        return aut_productos;
    }

    public void setAut_productos(AutoCompletar aut_productos) {
        this.aut_productos = aut_productos;
    }

    public Tabla getTab_producto() {
        return tab_producto;
    }

    public void setTab_producto(Tabla tab_producto) {
        this.tab_producto = tab_producto;
    }

    public Arbol getArb_estructura() {
        return arb_estructura;
    }

    public void setArb_estructura(Arbol arb_estructura) {
        this.arb_estructura = arb_estructura;
    }

    public Tabla getTab_kardex() {
        return tab_kardex;
    }

    public void setTab_kardex(Tabla tab_kardex) {
        this.tab_kardex = tab_kardex;
    }

    public Tabla getTab_movimientos() {
        return tab_movimientos;
    }

    public void setTab_movimientos(Tabla tab_movimientos) {
        this.tab_movimientos = tab_movimientos;
    }

    public AutoCompletar getAut_cuentas() {
        return aut_cuentas;
    }

    public void setAut_cuentas(AutoCompletar aut_cuentas) {
        this.aut_cuentas = aut_cuentas;
    }

    public Tabla getTab_clientesFacturas() {
        return tab_clientesFacturas;
    }

    public void setTab_clientesFacturas(Tabla tab_clientesFacturas) {
        this.tab_clientesFacturas = tab_clientesFacturas;
    }

    public Tabla getTab_proveedoresFacturas() {
        return tab_proveedoresFacturas;
    }

    public void setTab_proveedoresFacturas(Tabla tab_proveedoresFacturas) {
        this.tab_proveedoresFacturas = tab_proveedoresFacturas;
    }

    public Tabla getTab_grafico() {
        return tab_grafico;
    }

    public void setTab_grafico(Tabla tab_grafico) {
        this.tab_grafico = tab_grafico;
    }

    public GraficoCartesiano getGca_grafico() {
        return gca_grafico;
    }

    public void setGca_grafico(GraficoCartesiano gca_grafico) {
        this.gca_grafico = gca_grafico;
    }

    public Tabla getTab_preciosVentas() {
        return tab_preciosVentas;
    }

    public void setTab_preciosVentas(Tabla tab_preciosVentas) {
        this.tab_preciosVentas = tab_preciosVentas;
    }

    public Tabla getTab_preciosCompras() {
        return tab_preciosCompras;
    }

    public void setTab_preciosCompras(Tabla tab_preciosCompras) {
        this.tab_preciosCompras = tab_preciosCompras;
    }

    public Tabla getTab_asiento_tipo() {
        return tab_asiento_tipo;
    }

    public void setTab_asiento_tipo(Tabla tab_asiento_tipo) {
        this.tab_asiento_tipo = tab_asiento_tipo;
    }

    public Tabla getTab_detalle_asiento() {
        return tab_detalle_asiento;
    }

    public void setTab_detalle_asiento(Tabla tab_detalle_asiento) {
        this.tab_detalle_asiento = tab_detalle_asiento;
    }

}

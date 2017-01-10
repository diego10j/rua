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
    private Tabla tab_tabla;
    //opcion 2
    private Arbol arb_estructura;// Estructura Gerarquica de Productos
    //Kardex opcion 7    
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;

    //Movimientos opcion 5
      /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    private AutoCompletar aut_cuentas;

    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    private Tabla tab_asiento_tipo;
    private Tabla tab_detalle_asiento;

    //Opcion 8,9
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private GraficoCartesiano gca_grafico;
    private Combo com_periodo;

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
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        ser_producto.configurarTablaProducto(tab_tabla);
        tab_tabla.setTabla("inv_articulo", "ide_inarti", 1);
        tab_tabla.setCondicion("ide_inarti=" + aut_productos.getValor());
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.getColumna("INV_IDE_INARTI").setRequerida(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
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

            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(-1);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_producto.getSqlKardex(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
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
            tab_tabla.setRows(20);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
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

                tab_tabla = new Tabla();
                tab_tabla.setNumeroTabla(4);
                tab_tabla.setId("tab_tabla");
                tab_tabla.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_producto.getCuentaProducto(aut_productos.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
                tab_tabla.setLectura(true);
                tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. COMP.");
                tab_tabla.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
                tab_tabla.getColumna("ide_cnlap").setVisible(false);
                tab_tabla.getColumna("debe").setLongitud(20);
                tab_tabla.getColumna("haber").setLongitud(20);
                tab_tabla.getColumna("saldo").setLongitud(25);
                tab_tabla.getColumna("debe").alinearDerecha();
                tab_tabla.getColumna("haber").alinearDerecha();
                tab_tabla.getColumna("saldo").alinearDerecha();
                tab_tabla.getColumna("saldo").setEstilo("font-weight: bold;");
                tab_tabla.getColumna("valor_cndcc").setVisible(false);
                tab_tabla.getColumna("debe").setSuma(false);
                tab_tabla.getColumna("haber").setSuma(false);
                tab_tabla.getColumna("saldo").setSuma(false);
                tab_tabla.setColumnaSuma("debe,haber,saldo");
                tab_tabla.setRows(20);
                tab_tabla.setOrdenar(false);
                tab_tabla.dibujar();
                PanelTabla pat_panel = new PanelTabla();
                pat_panel.setPanelTabla(tab_tabla);
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
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(3);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_producto.getSqlVentasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("ide_ccdfa").setVisible(false);
            tab_tabla.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
            tab_tabla.getColumna("serie_ccdaf").setNombreVisual("N. SERIE");
            tab_tabla.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_tabla.getColumna("nom_geper").setNombreVisual("CLIENTE");
            tab_tabla.getColumna("cantidad_ccdfa").setNombreVisual("CANTIDAD");
            tab_tabla.getColumna("precio_ccdfa").setNombreVisual("PRECIO UNI.");
            tab_tabla.getColumna("total_ccdfa").setNombreVisual("TOTAL");
            tab_tabla.getColumna("precio_ccdfa").alinearDerecha();
            tab_tabla.getColumna("total_ccdfa").alinearDerecha();
            tab_tabla.getColumna("total_ccdfa").setEstilo("font-weight: bold");
            tab_tabla.getColumna("nom_geper").setFiltroContenido();
            tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
            tab_tabla.setLectura(true);
            tab_tabla.setRows(20);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
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
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(4);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_producto.getSqlComprasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("ide_cpdfa").setVisible(false);
            tab_tabla.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
            tab_tabla.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
            tab_tabla.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
            tab_tabla.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
            tab_tabla.getColumna("precio_cpdfa").setNombreVisual("PRECIO UNI.");
            tab_tabla.getColumna("valor_cpdfa").setNombreVisual("TOTAL");
            tab_tabla.getColumna("precio_cpdfa").alinearDerecha();
            tab_tabla.getColumna("valor_cpdfa").alinearDerecha();
            tab_tabla.getColumna("valor_cpdfa").setEstilo("font-weight: bold");
            tab_tabla.getColumna("nom_geper").setFiltroContenido();
            tab_tabla.getColumna("numero_cpcfa").setFiltroContenido();
            tab_tabla.setLectura(true);
            tab_tabla.setRows(20);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
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

            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_producto.getSqlTotalVentasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
            tab_tabla.setLectura(true);
            tab_tabla.setColumnaSuma("num_facturas,cantidad,total");
            tab_tabla.getColumna("cantidad").alinearDerecha();
            tab_tabla.getColumna("total").alinearDerecha();
            tab_tabla.getColumna("total").setNombreVisual("VALOR TOTAL");
            tab_tabla.dibujar();

            Grid gri_opciones = new Grid();
            gri_opciones.setColumns(2);
            gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
            gri_opciones.getChildren().add(com_periodo);
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.getChildren().add(gri_opciones);
            pat_panel.setPanelTabla(tab_tabla);

            gca_grafico.setTitulo("VENTAS MENSUALES");
            gca_grafico.agregarSerie(tab_tabla, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));

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

            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_producto.getSqlTotalComprasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
            tab_tabla.setLectura(true);
            tab_tabla.setColumnaSuma("num_facturas,cantidad,total");
            tab_tabla.getColumna("cantidad").alinearDerecha();
            tab_tabla.getColumna("total").alinearDerecha();
            tab_tabla.getColumna("total").setNombreVisual("VALOR TOTAL");
            tab_tabla.dibujar();

            Grid gri_opciones = new Grid();
            gri_opciones.setColumns(2);
            gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
            gri_opciones.getChildren().add(com_periodo);
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.getChildren().add(gri_opciones);
            pat_panel.setPanelTabla(tab_tabla);

            gca_grafico.setTitulo("COMPRAS MENSUALES");
            gca_grafico.agregarSerie(tab_tabla, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));

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

            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setIdCompleto("tab_tabulador:tab_tabla");
            tab_tabla.setSql(ser_producto.getSqlUltimosPreciosVentas(aut_productos.getValor()));
            tab_tabla.getColumna("ide_geper").setVisible(false);
            tab_tabla.setRows(20);
            tab_tabla.getColumna("nom_geper").setNombreVisual("CLIENTE");
            tab_tabla.getColumna("nom_geper").setFiltroContenido();
            tab_tabla.getColumna("nom_geper").setLongitud(200);
            tab_tabla.getColumna("fecha_ultima_venta").setNombreVisual("FECHA ULTIMA VENTA");
            tab_tabla.getColumna("fecha_ultima_venta").setLongitud(80);
            tab_tabla.getColumna("ultima_cantidad").setNombreVisual("CANTIDAD");
            tab_tabla.getColumna("ultima_cantidad").setFormatoNumero(2);
            tab_tabla.getColumna("ultima_cantidad").alinearDerecha();
            tab_tabla.getColumna("ultima_cantidad").setLongitud(100);
            tab_tabla.getColumna("ultima_cantidad").setEstilo("font-size:14px");

            tab_tabla.getColumna("ultimo_precio").setNombreVisual("PRECIO");
            tab_tabla.getColumna("ultimo_precio").setFormatoNumero(2);
            tab_tabla.getColumna("ultimo_precio").alinearDerecha();
            tab_tabla.getColumna("ultimo_precio").setLongitud(100);
            tab_tabla.getColumna("ultimo_precio").setEstilo("font-weight: bold;font-size:14px");

            tab_tabla.getColumna("valor_total").setNombreVisual("VALOR");
            tab_tabla.getColumna("valor_total").setFormatoNumero(2);
            tab_tabla.getColumna("valor_total").alinearDerecha();
            tab_tabla.getColumna("valor_total").setLongitud(100);
            tab_tabla.getColumna("valor_total").setEstilo("font-size:14px");

            tab_tabla.setLectura(true);
            tab_tabla.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla);

            tab_tabulador.agregarTab("VENTAS", pat_panel1);

            tab_asiento_tipo = new Tabla();
            tab_asiento_tipo.setId("tab_asiento_tipo");
            tab_asiento_tipo.setIdCompleto("tab_tabulador:tab_asiento_tipo");
            tab_asiento_tipo.setSql(ser_producto.getSqlUltimosPreciosCompras(aut_productos.getValor()));
            tab_asiento_tipo.getColumna("ide_geper").setVisible(false);
            tab_asiento_tipo.setRows(20);
            tab_asiento_tipo.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
            tab_asiento_tipo.getColumna("nom_geper").setFiltroContenido();
            tab_asiento_tipo.getColumna("nom_geper").setLongitud(200);
            tab_asiento_tipo.getColumna("fecha_ultima_venta").setNombreVisual("FECHA ULTIMA COMPRA");
            tab_asiento_tipo.getColumna("fecha_ultima_venta").setLongitud(80);
            tab_asiento_tipo.getColumna("ultima_cantidad").setNombreVisual("CANTIDAD");
            tab_asiento_tipo.getColumna("ultima_cantidad").setFormatoNumero(2);
            tab_asiento_tipo.getColumna("ultima_cantidad").alinearDerecha();
            tab_asiento_tipo.getColumna("ultima_cantidad").setLongitud(100);
            tab_asiento_tipo.getColumna("ultima_cantidad").setEstilo("font-size:14px");

            tab_asiento_tipo.getColumna("ultimo_precio").setNombreVisual("PRECIO");
            tab_asiento_tipo.getColumna("ultimo_precio").setFormatoNumero(2);
            tab_asiento_tipo.getColumna("ultimo_precio").alinearDerecha();
            tab_asiento_tipo.getColumna("ultimo_precio").setLongitud(100);
            tab_asiento_tipo.getColumna("ultimo_precio").setEstilo("font-weight: bold;font-size:14px");

            tab_asiento_tipo.getColumna("valor_total").setNombreVisual("VALOR");
            tab_asiento_tipo.getColumna("valor_total").setFormatoNumero(2);
            tab_asiento_tipo.getColumna("valor_total").alinearDerecha();
            tab_asiento_tipo.getColumna("valor_total").setLongitud(100);
            tab_asiento_tipo.getColumna("valor_total").setEstilo("font-size:14px");

            tab_asiento_tipo.setLectura(true);
            tab_asiento_tipo.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_asiento_tipo);

            tab_tabulador.agregarTab("COMPRAS", pat_panel2);

            gru_grupo.getChildren().add(tab_tabulador);

        }
        mep_menu.dibujar(10, "ÚLTIMOS PRECIOS", gru_grupo);
    }

    public void actualizarGraficoVentas() {
        tab_tabla.setSql(ser_producto.getSqlTotalVentasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
        tab_tabla.ejecutarSql();
        gca_grafico.limpiar();
        gca_grafico.agregarSerie(tab_tabla, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
        utilitario.addUpdate("gca_grafico");
    }

    public void actualizarGraficoCompras() {
        tab_tabla.setSql(ser_producto.getSqlTotalComprasMensualesProducto(aut_productos.getValor(), String.valueOf(com_periodo.getValue())));
        tab_tabla.ejecutarSql();
        gca_grafico.limpiar();
        gca_grafico.agregarSerie(tab_tabla, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));
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
        tab_tabla.setSql(ser_producto.getSqlComprasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.ejecutarSql();
        if (tab_tabla.isEmpty()) {
            tab_tabla.setEmptyMessage("No existen Compras en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza ventas del producto seleccionado selecionadas
     */
    public void actualizarVentas() {
        tab_tabla.setSql(ser_producto.getSqlVentasProducto(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.ejecutarSql();
        if (tab_tabla.isEmpty()) {
            tab_tabla.setEmptyMessage("No existen Ventas en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza el kardex de un producto segun las fechas selecionadas
     */
    public void actualizarKardex() {
        tab_tabla.setSql(ser_producto.getSqlKardex(aut_productos.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
        tab_tabla.ejecutarSql();
        // actualizarSaldoxCobrar();
        calculaKardex();
        utilitario.addUpdate("tex_saldo_final,tex_total_debe,tex_saldo_inicial");
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_tabla.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_producto.getCuentaProducto(aut_productos.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(),""));
        tab_tabla.ejecutarSql();
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

        for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
            if (tab_tabla.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                tab_tabla.setValor(i, "debe", utilitario.getFormatoNumero(tab_tabla.getValor(i, "valor_cndcc")));
                dou_debe += Double.parseDouble(tab_tabla.getValor(i, "debe"));

            } else {
                tab_tabla.setValor(i, "haber", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_tabla.getValor(i, "valor_cndcc")))));
                dou_haber += Double.parseDouble(tab_tabla.getValor(i, "haber"));
            }
            dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_tabla.getValor(i, "valor_cndcc"));
            tab_tabla.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));

            saldo_anterior = dou_saldo_actual;
        }
        if (tab_tabla.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_tabla.setEmptyMessage("No existen Movimientos Contables en el rango de fechas seleccionado");
        }

        //INSERTA PRIMERA FILA SALDO INICIAL
        if (dou_saldo_inicial != 0) {
            tab_tabla.setLectura(false);
            tab_tabla.insertar();
            tab_tabla.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_tabla.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_tabla.setValor("fecha_trans_cnccc", cal_fecha_inicio.getFecha());
            tab_tabla.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_tabla.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_tabla.getColumna("debe").setTotal(dou_debe);
        tab_tabla.getColumna("haber").setTotal(dou_haber);
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
            tab_tabla.limpiar();
            tab_tabla.insertar();
        } else if (mep_menu.getOpcion() == 4) {
            tab_detalle_asiento.insertar();
        } else {
            dibujarProducto();
            tab_tabla.insertar();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO PRODUCTO
            if (true) { //!!!!!!!!******Validar Datos Producto
                tab_tabla.guardar();
                if (guardarPantalla().isEmpty()) {
                    //Actualiza el autocompletar
                    aut_productos.actualizar();
                    aut_productos.setSize(75);
                    aut_productos.setValor(tab_tabla.getValorSeleccionado());
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
            tab_tabla.eliminar();
        }
    }

    public AutoCompletar getAut_productos() {
        return aut_productos;
    }

    public void setAut_productos(AutoCompletar aut_productos) {
        this.aut_productos = aut_productos;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Arbol getArb_estructura() {
        return arb_estructura;
    }

    public void setArb_estructura(Arbol arb_estructura) {
        this.arb_estructura = arb_estructura;
    }

    public AutoCompletar getAut_cuentas() {
        return aut_cuentas;
    }

    public void setAut_cuentas(AutoCompletar aut_cuentas) {
        this.aut_cuentas = aut_cuentas;
    }

    public GraficoCartesiano getGca_grafico() {
        return gca_grafico;
    }

    public void setGca_grafico(GraficoCartesiano gca_grafico) {
        this.gca_grafico = gca_grafico;
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

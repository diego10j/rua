/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelArbol;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidad;
import servicios.cuentas_x_pagar.ServicioProveedor;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_proveedores extends Pantalla {

    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_proveedor = new AutoCompletar();

    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;

    private Tabla tab_proveedor; //formulario proveedor    
    private Tabla tab_transacciones_cxp; //transacciones proveedor
    private Tabla tab_productos; //transacciones proveedor
    private Tabla tab_facturas; //Facturas pendientes

    private Arbol arb_estructura;// Estructura Gerarquica de proveedores

    /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
    private AutoCompletar aut_cuentas;
    private Tabla tab_movimientos; //movimientos contables
    private Texto tex_saldo_inicial;
    private Texto tex_saldo_final;
    private Texto tex_total_debe;
    private Texto tex_total_haber;

    public pre_proveedores() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarComponente(new Etiqueta("PROVEEDOR :"));

        aut_proveedor.setId("aut_proveedor");
        aut_proveedor.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_proveedor.setSize(75);
        aut_proveedor.setAutocompletarContenido(); // no startWith para la busqueda
        aut_proveedor.setMetodoChange("seleccionarProveedor");
        bar_botones.agregarComponente(aut_proveedor);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        mep_menu.setMenuPanel("OPCIONES PROVEEDOR", "20%");
        mep_menu.agregarItem("Información Proveedor", "dibujarProveedor", "ui-icon-person");
        mep_menu.agregarItem("Clasificación Proveedores", "dibujarEstructura", "ui-icon-arrow-4-diag");
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Transacciones Proveedor", "dibujarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Productos Proveedor", "dibujarProductos", "ui-icon-cart");
        mep_menu.agregarItem("Facturas Por Pagar", "dibujarFacturas", "ui-icon-calculator");
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Configura Cuenta Contable", "dibujarConfiguraCuenta", "ui-icon-wrench");
        mep_menu.agregarItem("Movimientos Contables", "dibujarMovimientos", "ui-icon-note");

        agregarComponente(mep_menu);

    }

    /**
     * Selecciona un Proveedor en el autocompletar
     *
     * @param evt
     */
    public void seleccionarProveedor(SelectEvent evt) {
        aut_proveedor.onSelect(evt);
        if (aut_proveedor != null) {
            switch (mep_menu.getOpcion()) {
                case 1:
                    dibujarProveedor();
                    break;
                case 2:
                    dibujarTransacciones();
                    break;
                case 3:
                    dibujarProductos();
                    break;
                case 4:
                    dibujarConfiguraCuenta();
                    break;
                case 5:
                    dibujarMovimientos();
                    break;
                case 6:
                    dibujarFacturas();
                    break;
                case 7:
                    dibujarEstructura();
                    break;
                default:
                    dibujarProveedor();
            }
        } else {
            limpiar();
        }
    }

    /**
     * Dibuja el formulario de datos del Proveedor, osigna opcion 1
     */
    public void dibujarProveedor() {
        tab_proveedor = new Tabla();
        tab_proveedor.setId("tab_proveedor");
        ser_proveedor.configurarTablaProveedor(tab_proveedor);
        tab_proveedor.setTabla("gen_persona", "ide_geper", 1);
        tab_proveedor.setCondicion("ide_geper=" + aut_proveedor.getValor());
        tab_proveedor.setMostrarNumeroRegistros(false);
        tab_proveedor.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_proveedor);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(1, "DATOS DEL PROVEEDOR", pat_panel);
    }

    public void dibujarTransacciones() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {

            Fieldset fis_consulta = new Fieldset();
            fis_consulta.setLegend("Detalle de la Consulta");

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
            bot_consultar.setMetodo("actualizarTransacciones");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);

            Separator separar = new Separator();
            fis_consulta.getChildren().add(separar);

            Grid gri_saldos = new Grid();
            gri_saldos.setColumns(4);
            gri_saldos.getChildren().add(new Etiqueta("<strong>SALDO INICIAL :</strong>"));
            tex_saldo_inicial = new Texto();
            tex_saldo_inicial.setId("tex_saldo_inicial");
            tex_saldo_inicial.setDisabled(true);
            tex_saldo_inicial.setSize(10);
            tex_saldo_inicial.setStyle("font-size: 13px;font-weight: bold;text-align: right;");
            gri_saldos.getChildren().add(tex_saldo_inicial);
            gri_saldos.getChildren().add(new Etiqueta("TOTAL INGRESOS :"));
            tex_total_debe = new Texto();
            tex_total_debe.setId("tex_total_debe");
            tex_total_debe.setDisabled(true);
            tex_total_debe.setSize(10);
            tex_total_debe.setStyle("font-size: 13px;text-align: right;");
            gri_saldos.getChildren().add(tex_total_debe);
            gri_saldos.getChildren().add(new Etiqueta("<strong>SALDO FINAL :</strong>"));
            tex_saldo_final = new Texto();
            tex_saldo_final.setId("tex_saldo_final");
            tex_saldo_final.setDisabled(true);
            tex_saldo_final.setStyle("font-size: 13px;font-weight: bold;text-align: right;");
            tex_saldo_final.setSize(10);
            gri_saldos.getChildren().add(tex_saldo_final);
            gri_saldos.getChildren().add(new Etiqueta("TOTAL EGRESOS :"));
            tex_total_haber = new Texto();
            tex_total_haber.setId("tex_total_haber");
            tex_total_haber.setDisabled(true);
            tex_total_haber.setStyle("font-size: 13px;text-align: right;");
            tex_total_haber.setSize(10);
            gri_saldos.getChildren().add(tex_total_haber);

            fis_consulta.getChildren().add(gri_saldos);

            tab_transacciones_cxp = new Tabla();
            tab_transacciones_cxp.setNumeroTabla(2);
            tab_transacciones_cxp.setId("tab_transacciones_cxp");
            tab_transacciones_cxp.setSql(ser_proveedor.getSqlTransaccionesProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_transacciones_cxp.setCampoPrimaria("ide_cpdtr");
            tab_transacciones_cxp.getColumna("IDE_TECLB").setVisible(false);
            tab_transacciones_cxp.getColumna("fecha_trans_cpdtr").setNombreVisual("FECHA");
            tab_transacciones_cxp.getColumna("ide_cpdtr").setVisible(false);
            tab_transacciones_cxp.getColumna("numero_pago_cpdtr").setVisible(false);
            tab_transacciones_cxp.getColumna("INGRESOS").alinearDerecha();
            tab_transacciones_cxp.getColumna("EGRESOS").alinearDerecha();
            tab_transacciones_cxp.getColumna("IDE_CNCCC").setFiltro(true);
            tab_transacciones_cxp.getColumna("IDE_CNCCC").setNombreVisual("N. COMP");
            tab_transacciones_cxp.getColumna("TRANSACCION").setFiltroContenido();
            tab_transacciones_cxp.getColumna("docum_relac_cpdtr").setFiltroContenido();
            tab_transacciones_cxp.getColumna("docum_relac_cpdtr").setNombreVisual("N. DOCUMENTO");
            tab_transacciones_cxp.getColumna("INGRESOS").alinearDerecha();
            tab_transacciones_cxp.getColumna("EGRESOS").alinearDerecha();
            tab_transacciones_cxp.getColumna("INGRESOS").setLongitud(20);
            tab_transacciones_cxp.getColumna("EGRESOS").setLongitud(20);
            tab_transacciones_cxp.getColumna("saldo").setLongitud(25);
            tab_transacciones_cxp.getColumna("saldo").alinearDerecha();
            tab_transacciones_cxp.getColumna("saldo").setEstilo("font-weight: bold;");

            tab_transacciones_cxp.setLectura(true);
            tab_transacciones_cxp.setScrollable(true);
            tab_transacciones_cxp.setScrollHeight(300);
            tab_transacciones_cxp.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_transacciones_cxp);
            gru_grupo.getChildren().add(pat_panel);

            actualizarSaldoxPagar();
        }
        mep_menu.dibujar(2, "TRANSACCIONES DEL PROVEEDOR", gru_grupo);
    }

    public void dibujarProductos() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            Fieldset fis_consulta = new Fieldset();
            fis_consulta.setLegend("Detalle de la Consulta");

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
            bot_consultar.setMetodo("actualizarProducots");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);
            tab_productos = new Tabla();
            tab_productos.setNumeroTabla(3);
            tab_productos.setId("tab_productos");
            tab_productos.setSql(ser_proveedor.getSqlProductosProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_productos.getColumna("ide_cpdfa").setVisible(false);

            tab_productos.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
            tab_productos.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
            tab_productos.getColumna("nombre_inarti").setNombreVisual("ARTICULO");
            tab_productos.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
            tab_productos.getColumna("precio_cpdfa").setNombreVisual("PRECIO UNI.");
            tab_productos.getColumna("valor_cpdfa").setNombreVisual("TOTAL");
            tab_productos.getColumna("precio_cpdfa").alinearDerecha();
            tab_productos.getColumna("valor_cpdfa").alinearDerecha();
            tab_productos.getColumna("valor_cpdfa").setEstilo("font-weight: bold");
            tab_productos.getColumna("nombre_inarti").setFiltroContenido();
            tab_productos.getColumna("numero_cpcfa").setFiltroContenido();

            tab_productos.setLectura(true);
            tab_productos.setScrollable(true);
            tab_productos.setScrollHeight(300);
            tab_productos.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_productos);
            gru_grupo.getChildren().add(pat_panel);
        }

        mep_menu.dibujar(3, "PRODUCTOS COMPRADOS POR EL PROVEEDOR", gru_grupo);
    }

    public void dibujarConfiguraCuenta() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            aut_cuentas = new AutoCompletar();
            aut_cuentas.setId("aut_cuentas");
            aut_cuentas.setAutoCompletar(ser_contabilidad.getSqlCuentasPasivos());
            aut_cuentas.setSize(75);
            aut_cuentas.setDisabled(true);
            aut_cuentas.setMetodoChange("seleccionarCuentaContable");
            aut_cuentas.setValor(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()));

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
            gru_grupo.getChildren().add(new Etiqueta("<p style='padding-top:10px;'><strong >NOTA: </strong> La cuenta contable seleccionada se relacionará a los movimientos-transacciones que realice el Proveedor, a partir que se <strong>guarde </strong> el cambio. </p>"));
        }
        mep_menu.dibujar(4, "CONFIGURACIÓN DE CUENTA CONTABLE", gru_grupo);
    }

    public void dibujarMovimientos() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            TablaGenerica tab_cuenta = ser_contabilidad.getCuenta(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()));
            if (!tab_cuenta.isEmpty()) {

                Fieldset fis_consulta = new Fieldset();
                fis_consulta.setLegend("Detalle de la Consulta");
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

                Grid gri_saldos = new Grid();
                gri_saldos.setColumns(4);
                gri_saldos.getChildren().add(new Etiqueta("<strong>SALDO INICIAL :</strong>"));
                tex_saldo_inicial = new Texto();
                tex_saldo_inicial.setId("tex_saldo_inicial");
                tex_saldo_inicial.setDisabled(true);
                tex_saldo_inicial.setSize(10);
                tex_saldo_inicial.setStyle("font-size: 13px;font-weight: bold;text-align: right;");
                gri_saldos.getChildren().add(tex_saldo_inicial);
                gri_saldos.getChildren().add(new Etiqueta("TOTAL DEBE :"));
                tex_total_debe = new Texto();
                tex_total_debe.setId("tex_total_debe");
                tex_total_debe.setDisabled(true);
                tex_total_debe.setSize(10);
                tex_total_debe.setStyle("font-size: 13px;text-align: right;");
                gri_saldos.getChildren().add(tex_total_debe);
                gri_saldos.getChildren().add(new Etiqueta("<strong>SALDO FINAL :</strong>"));
                tex_saldo_final = new Texto();
                tex_saldo_final.setId("tex_saldo_final");
                tex_saldo_final.setDisabled(true);
                tex_saldo_final.setStyle("font-size: 13px;font-weight: bold;text-align: right;");
                tex_saldo_final.setSize(10);
                gri_saldos.getChildren().add(tex_saldo_final);
                gri_saldos.getChildren().add(new Etiqueta("TOTAL HABER :"));
                tex_total_haber = new Texto();
                tex_total_haber.setId("tex_total_haber");
                tex_total_haber.setDisabled(true);
                tex_total_haber.setStyle("font-size: 13px;text-align: right;");
                tex_total_haber.setSize(10);
                gri_saldos.getChildren().add(tex_total_haber);

                fis_consulta.getChildren().add(gri_saldos);

                gru_grupo.getChildren().add(fis_consulta);

                tab_movimientos = new Tabla();
                tab_movimientos.setNumeroTabla(4);
                tab_movimientos.setId("tab_movimientos");
                tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
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
                tab_movimientos.setScrollable(true);
                tab_movimientos.setScrollHeight(250);
                tab_movimientos.dibujar();
                PanelTabla pat_panel = new PanelTabla();
                pat_panel.setPanelTabla(tab_movimientos);
                gru_grupo.getChildren().add(pat_panel);
                actualizarSaldosContable();
            } else {
                utilitario.agregarMensajeInfo("No se encontro Cuenta Contable", "El proveedor seleccionado no tiene asociado una cuenta contable");
            }
        }
        mep_menu.dibujar(5, "MOVIMIENTOS CONTABLES", gru_grupo);
    }

    public void dibujarFacturas() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            tab_facturas = new Tabla();
            tab_facturas.setNumeroTabla(6);
            tab_facturas.setId("tab_facturas");
            tab_facturas.setSql(ser_proveedor.getSqlFacturasPorPagar(aut_proveedor.getValor()));
            tab_facturas.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
            tab_facturas.getColumna("saldo_x_pagar").alinearDerecha();
            tab_facturas.setCampoPrimaria("ide_cpctr");
            tab_facturas.getColumna("ide_cpctr").setVisible(false);
            tab_facturas.getColumna("ide_cpcfa").setVisible(false);
            tab_facturas.getColumna("fecha").setVisible(true);
            tab_facturas.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
            tab_facturas.getColumna("numero_cpcfa").setFiltroContenido();
            tab_facturas.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
            tab_facturas.getColumna("total_cpcfa").setNombreVisual("TOTAL");
            tab_facturas.getColumna("total_cpcfa").setEstilo("font-size: 13px;");
            tab_facturas.getColumna("total_cpcfa").alinearDerecha();
            tab_facturas.setLectura(true);
            tab_facturas.setColumnaSuma("saldo_x_pagar");
            tab_facturas.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_facturas);
            gru_grupo.getChildren().add(pat_panel);

            if (tab_facturas.isEmpty()) {
                tab_facturas.setEmptyMessage("No tiene facturas por pagar al proveedor seleccionado");
            }
        }
        mep_menu.dibujar(6, "FACTURAS POR PAGAR AL PROVEEDOR", gru_grupo);
    }

    public void dibujarEstructura() {
        Grupo gru_grupo = new Grupo();
        arb_estructura = new Arbol();
        arb_estructura.setId("arb_estructura");
        arb_estructura.setArbol("gen_persona", "ide_geper", "nom_geper", "gen_ide_geper");
        arb_estructura.setCondicion("es_proveedo_geper=true");
        arb_estructura.dibujar();
        //Selecciona el nodo
        if (aut_proveedor.getValor() != null) {
            arb_estructura.seleccinarNodo(aut_proveedor.getValor());
            arb_estructura.getNodoSeleccionado().setExpanded(true);
            arb_estructura.getNodoSeleccionado().getParent().setExpanded(true);
        }
        PanelArbol paa_panel = new PanelArbol();
        paa_panel.setPanelArbol(arb_estructura);
        gru_grupo.getChildren().add(paa_panel);
        mep_menu.dibujar(7, "CLASIFICACIÓN DE PROVEEDORES", gru_grupo);
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_movimientos.ejecutarSql();
        actualizarSaldosContable();
    }

    /**
     * Actualiza las transacciones de un Proveedor segun las fechas selecionadas
     */
    public void actualizarTransacciones() {
        tab_transacciones_cxp.setSql(ser_proveedor.getSqlTransaccionesProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_transacciones_cxp.ejecutarSql();
        actualizarSaldoxPagar();
    }

    /**
     * Actualiza los productos comprados del Proveedor segun las fechas
     * selecionadas
     */
    public void actualizarProducots() {
        tab_productos.setSql(ser_proveedor.getSqlProductosProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_productos.ejecutarSql();
        if (tab_productos.isEmpty()) {

            tab_productos.setEmptyMessage("No Productos en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza los solados que se visualizan en pantalla
     */
    private void actualizarSaldosContable() {

        double saldo_anterior = ser_contabilidad.getSaldoInicialCuenta(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_debe = 0;
        double dou_haber = 0;
        String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");

        for (int i = 0; i < tab_movimientos.getTotalFilas(); i++) {
            if (tab_movimientos.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                tab_movimientos.setValor(i, "debe", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_movimientos.getValor(i, "valor_cndcc")))));
                dou_debe += Double.parseDouble(tab_movimientos.getValor(i, "debe"));

            } else {
                tab_movimientos.setValor(i, "haber", utilitario.getFormatoNumero(tab_movimientos.getValor(i, "valor_cndcc")));
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

        tex_total_debe.setValue(utilitario.getFormatoNumero(dou_debe));
        tex_total_haber.setValue(utilitario.getFormatoNumero(dou_haber));

        tex_saldo_inicial.setValue(utilitario.getFormatoNumero(dou_saldo_inicial));
        tex_saldo_final.setValue(utilitario.getFormatoNumero(dou_saldo_actual));

        utilitario.addUpdate("tex_total_debe,tex_total_haber,tex_saldo_inicial,tex_saldo_final");
    }

    /**
     * *
     * Activa el Autocompletar para cambiar la cuenta contable
     */
    public void activaCambiarCuenta() {
        if (isProveedorSeleccionado()) {
            aut_cuentas.setDisabled(false);
            utilitario.addUpdate("aut_cuentas");
        }
    }

    /**
     * Selecciona una nueva cuenta contable, y agrega el SQL para que pueda ser
     * guardado
     *
     * @param evt
     */
    public void seleccionarCuentaContable(SelectEvent evt) {
        aut_cuentas.onSelect(evt);
        if (isProveedorSeleccionado()) {
            if (aut_cuentas.getValor() != null) {
                utilitario.getConexion().getSqlPantalla().clear();
                if (ser_proveedor.isTieneCuentaConfiguradaProveedor(aut_proveedor.getValor()) == false) {
                    //nueva
                    utilitario.getConexion().agregarSqlPantalla(ser_proveedor.getSqlInsertarCuentaProveedor(aut_proveedor.getValor(), aut_cuentas.getValor()));
                } else {
                    //modifica
                    utilitario.getConexion().agregarSqlPantalla(ser_proveedor.getSqlActualizarCuentaProveedor(aut_proveedor.getValor(), aut_cuentas.getValor()));
                }
            }
        }

    }

    /**
     * Validacion para que se seleccione un Proveedor del Autocompletar
     *
     * @return
     */
    private boolean isProveedorSeleccionado() {
        if (aut_proveedor.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Proveedor", "Seleccione un proveedor de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    /**
     * Validaciones para crear o modificar un Proveedor
     *
     * @return
     */
    private boolean validarProveedor() {
        if (tab_proveedor.getValor("ide_getid") != null && tab_proveedor.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
            if (utilitario.validarCedula(tab_proveedor.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de cédula válida");
                return false;
            }
        }
        if (tab_proveedor.getValor("ide_getid") != null && tab_proveedor.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
            if (utilitario.validarRUC(tab_proveedor.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de ruc válido");
                return false;
            }
        }
        if (tab_proveedor.getValor("ide_cntco") == null || tab_proveedor.getValor("ide_cntco").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar el tipo de contribuyente");
            return false;
        }
        //      }
        return true;
    }

    /**
     * Calcula el saldo por cobrar del Proveedor
     *
     * @return
     */
    private void actualizarSaldoxPagar() {
        double saldo_anterior = ser_proveedor.getSaldoInicialProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_ingresos = 0;
        double dou_egresos = 0;

        for (int i = 0; i < tab_transacciones_cxp.getTotalFilas(); i++) {
            if (tab_transacciones_cxp.getValor(i, "ingresos") != null) {
                dou_ingresos += Double.parseDouble(tab_transacciones_cxp.getValor(i, "ingresos"));
                dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_transacciones_cxp.getValor(i, "ingresos"));
            } else {
                dou_egresos += Double.parseDouble(tab_transacciones_cxp.getValor(i, "egresos"));
                dou_saldo_actual = saldo_anterior - Double.parseDouble(tab_transacciones_cxp.getValor(i, "egresos"));
            }

            tab_transacciones_cxp.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
            saldo_anterior = dou_saldo_actual;
        }
        if (tab_transacciones_cxp.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_transacciones_cxp.setEmptyMessage("No existen Transacciones en el rango de fechas seleccionado");
        }
        tex_total_debe.setValue(utilitario.getFormatoNumero(dou_ingresos));
        tex_total_haber.setValue(utilitario.getFormatoNumero(dou_egresos));
        tex_saldo_inicial.setValue(utilitario.getFormatoNumero(dou_saldo_inicial));
        tex_saldo_final.setValue(utilitario.getFormatoNumero(dou_saldo_actual));

        utilitario.addUpdate("tex_total_debe,tex_total_haber,tex_saldo_inicial,tex_saldo_final");

    }

    /**
     * Limpia la pantalla y el autocompletar
     */
    public void limpiar() {
        aut_proveedor.limpiar();
        mep_menu.limpiar();
    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == -1) {
            //PANTALLA LIMPIA
            dibujarProveedor();
        }

        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO Proveedor
            aut_proveedor.limpiar();
            tab_proveedor.insertar();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO Proveedor
            if (validarProveedor()) {
                tab_proveedor.guardar();
                if (guardarPantalla().isEmpty()) {
                    //Actualiza el autocompletar
                    aut_proveedor.actualizar();
                    aut_proveedor.setSize(75);
                    aut_proveedor.setValor(tab_proveedor.getValorSeleccionado());
                    utilitario.addUpdate("aut_proveeedor");
                }
            }
        } else if (mep_menu.getOpcion() == 4) {
            //Cambiar Cuenta Contable
            if (guardarPantalla().isEmpty()) {
                aut_cuentas.setDisabled(true);
            }
        } else if (mep_menu.getOpcion() == 7) {
            //Classificacion de Proveedores
            guardarPantalla();
        }

    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO Proveedor
            tab_proveedor.eliminar();
        }
    }

    public AutoCompletar getAut_proveedor() {
        return aut_proveedor;
    }

    public void setAut_proveedor(AutoCompletar aut_proveedor) {
        this.aut_proveedor = aut_proveedor;
    }

    public Tabla getTab_proveedor() {
        return tab_proveedor;
    }

    public void setTab_proveedor(Tabla tab_proveedor) {
        this.tab_proveedor = tab_proveedor;
    }

    public Tabla getTab_transacciones_cxp() {
        return tab_transacciones_cxp;
    }

    public void setTab_transacciones_cxp(Tabla tab_transacciones_cxp) {
        this.tab_transacciones_cxp = tab_transacciones_cxp;
    }

    public Tabla getTab_productos() {
        return tab_productos;
    }

    public void setTab_productos(Tabla tab_productos) {
        this.tab_productos = tab_productos;
    }

    public AutoCompletar getAut_cuentas() {
        return aut_cuentas;
    }

    public void setAut_cuentas(AutoCompletar aut_cuentas) {
        this.aut_cuentas = aut_cuentas;
    }

    public Tabla getTab_movimientos() {
        return tab_movimientos;
    }

    public void setTab_movimientos(Tabla tab_movimientos) {
        this.tab_movimientos = tab_movimientos;
    }

    public Tabla getTab_facturas() {
        return tab_facturas;
    }

    public void setTab_facturas(Tabla tab_facturas) {
        this.tab_facturas = tab_facturas;
    }

    public Arbol getArb_estructura() {
        return arb_estructura;
    }

    public void setArb_estructura(Arbol arb_estructura) {
        this.arb_estructura = arb_estructura;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.AsientoContable;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MarcaAgua;
import framework.componentes.MenuPanel;
import framework.componentes.PanelArbol;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.graficos.GraficoCartesiano;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.prestamo.ServicioPrestamo;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_clientes extends Pantalla {

    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);

    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_clientes = new AutoCompletar();

    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;

    private Tabla tab_tabla;
    private Arbol arb_estructura;// Estructura Gerarquica de clientes

    /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    private AutoCompletar aut_cuentas;

    private AsientoContable asc_asiento = new AsientoContable();
    @EJB
    private final ServicioPrestamo ser_prestamo = (ServicioPrestamo) utilitario.instanciarEJB(ServicioPrestamo.class);

    /*INFOMRES*/
    private GraficoCartesiano gca_grafico;
    private Combo com_periodo;

    public pre_clientes() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarComponente(new Etiqueta("CLIENTE :"));
        aut_clientes.setId("aut_clientes");
        aut_clientes.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_clientes.setSize(75);
        aut_clientes.setAutocompletarContenido(); // no startWith para la busqueda
        aut_clientes.setMetodoChangeRuta("pre_index.clase.seleccionarCliente");

        bar_botones.agregarComponente(aut_clientes);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        mep_menu.setMenuPanel("OPCIONES CLIENTE", "20%");
        mep_menu.agregarItem("Información Cliente", "dibujarCliente", "ui-icon-person");
        mep_menu.agregarItem("Clasificación Clientes", "dibujarEstructura", "ui-icon-arrow-4-diag");
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Transacciones Cliente", "dibujarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Ingresar Transacción", "dibujarIngresarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Productos Cliente", "dibujarProductos", "ui-icon-cart");
        mep_menu.agregarItem("Facturas Por Cobrar", "dibujarFacturas", "ui-icon-calculator");
        mep_menu.agregarItem("Préstamos - Créditos", "dibujarPrestamos", "ui-icon-calculator");
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Configura Cuenta Contable", "dibujarConfiguraCuenta", "ui-icon-wrench");
        mep_menu.agregarItem("Movimientos Contables", "dibujarMovimientos", "ui-icon-note");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Gráfico de Ventas", "dibujarGrafico", "ui-icon-bookmark");
        mep_menu.agregarItem("Reporte Cuentas por Cobrar", "dibujarReporteCxC", "ui-icon-bookmark");
        mep_menu.agregarItem("Productos Vendidos", "dibujarProductosVendidos", "ui-icon-cart");

        agregarComponente(mep_menu);
        asc_asiento.setId("asc_asiento");
        agregarComponente(asc_asiento);
    }

    /**
     * Selecciona un cliente en el autocompletar
     *
     * @param evt
     */
    public void seleccionarCliente(SelectEvent evt) {
        aut_clientes.onSelect(evt);
        if (aut_clientes != null) {
            switch (mep_menu.getOpcion()) {
                case 1:
                    dibujarCliente();
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
                case 8:
                    dibujarGrafico();
                    break;
                case 9:
                    dibujarProductosVendidos();
                    break;
                case 10:
                    dibujarIngresarTransacciones();
                    break;
                case 11:
                    dibujarPrestamos();
                    break;
                case 12:
                    dibujarReporteCxC();
                    break;
                default:
                    dibujarCliente();
            }
        } else {
            limpiar();
        }
    }

    public void dibujarReporteCxC() {
//12
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_factura.getSqlTransaccionesCxC());
        tab_tabla.setCampoPrimaria("ide_geper");
        tab_tabla.getColumna("cliente").setLongitud(190);
        tab_tabla.getColumna("cliente").setFiltroContenido();
        tab_tabla.getColumna("IDENTIFICACION").setFiltroContenido();
        tab_tabla.getColumna("ide_geper").setVisible(false);
        tab_tabla.setLectura(true);
        tab_tabla.setColumnaSuma("SALDO");
        tab_tabla.setRows(20);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(12, "REPORTE  CUENTAS POR COBRAR", pat_panel);

    }

    public void dibujarPrestamos() {
//11  
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {

            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_prestamo.getSqlPrestamosCliente(aut_clientes.getValor()));
            tab_tabla.getColumna("ide_ipcpr").setVisible(false);
            tab_tabla.getColumna("tipo").setLongitud(10);
            tab_tabla.getColumna("fecha_prestamo_ipcpr").setNombreVisual("FECHA");
            tab_tabla.getColumna("num_prestamo_ipcpr").setNombreVisual("NUM. PRESTAMO");
            tab_tabla.getColumna("monto_ipcpr").setNombreVisual("MONTO");
            tab_tabla.getColumna("monto_ipcpr").alinearDerecha();
            tab_tabla.getColumna("interes_ipcpr").setNombreVisual("% INTERES");
            tab_tabla.getColumna("interes_ipcpr").alinearDerecha();
            tab_tabla.getColumna("num_pagos_ipcpr").setNombreVisual("NUM. PAGOS");
            tab_tabla.getColumna("num_pagos_ipcpr").alinearDerecha();
            tab_tabla.getColumna("pagos").setNombreVisual("PAGADOS");
            tab_tabla.getColumna("saldo").alinearDerecha();
            tab_tabla.getColumna("pagos").alinearDerecha();
            tab_tabla.getColumna("valor_pagado").setNombreVisual("VALOR PAGADO");
            tab_tabla.getColumna("valor_pagado").alinearDerecha();
            tab_tabla.getColumna("capital").alinearDerecha();
            tab_tabla.getColumna("interes").alinearDerecha();
            tab_tabla.getColumna("cuota").alinearDerecha();
            tab_tabla.getColumna("fecha_ultimo_pago").setNombreVisual("FECHA ULT. PAGO");
            tab_tabla.setColumnaSuma("saldo,capital,interes");
            tab_tabla.setOrdenar(false);
            tab_tabla.setNumeroTabla(-1);
            tab_tabla.setRows(20);
            tab_tabla.setLectura(true);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
            gru_grupo.getChildren().add(pat_panel);
        }
        mep_menu.dibujar(11, "PRÉSTAMOS - CRÉDITOS", gru_grupo);

    }

    /**
     * Dibuja el formulario de datos del Cliente, osigna opcion 1
     */
    public void dibujarCliente() {
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        ser_cliente.configurarTablaCliente(tab_tabla);
        tab_tabla.setTabla("gen_persona", "ide_geper", 1);
        tab_tabla.setCondicion("ide_geper=" + aut_clientes.getValor());
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(1, "DATOS DEL CLIENTE", pat_panel);
    }

    public void dibujarIngresarTransacciones() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setTabla("cxc_detall_transa", "ide_ccdtr", 998);
            tab_tabla.setTipoFormulario(true);
            tab_tabla.getGrid().setColumns(2);
            tab_tabla.getColumna("ide_ccdtr").setVisible(false);
            tab_tabla.setMostrarNumeroRegistros(false);
            tab_tabla.getColumna("ide_teclb").setVisible(false);
            tab_tabla.getColumna("ide_ccttr").setCombo("cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr", "");
            tab_tabla.getColumna("ide_ccttr").setNombreVisual("TIPO DE TRANSACCIÓN");
            tab_tabla.getColumna("ide_ccttr").setOrden(1);
            tab_tabla.getColumna("ide_ccttr").setRequerida(true);

            tab_tabla.getColumna("fecha_trans_ccdtr").setValorDefecto(utilitario.getFechaActual());
            tab_tabla.getColumna("fecha_trans_ccdtr").setNombreVisual("FECHA");
            tab_tabla.getColumna("fecha_trans_ccdtr").setOrden(2);
            tab_tabla.getColumna("fecha_trans_ccdtr").setRequerida(true);

            tab_tabla.getColumna("valor_ccdtr").setNombreVisual("VALOR");
            tab_tabla.getColumna("valor_ccdtr").setRequerida(true);
            tab_tabla.getColumna("valor_ccdtr").setOrden(3);

            tab_tabla.getColumna("fecha_venci_ccdtr").setVisible(false);
            tab_tabla.getColumna("numero_pago_ccdtr").setVisible(false);
            tab_tabla.getColumna("numero_pago_ccdtr").setValorDefecto("0");

            tab_tabla.getColumna("docum_relac_ccdtr").setNombreVisual("NUM. DOCUMENTO");
            tab_tabla.getColumna("docum_relac_ccdtr").setOrden(4);

            tab_tabla.getColumna("observacion_ccdtr").setNombreVisual("OBSERVACIÓN");
            tab_tabla.getColumna("observacion_ccdtr").setRequerida(true);
            tab_tabla.getColumna("observacion_ccdtr").setOrden(5);
            tab_tabla.getColumna("observacion_ccdtr").setControl("AreaTexto");

            tab_tabla.getColumna("ide_cccfa").setVisible(false);
            tab_tabla.getColumna("ide_cnccc").setVisible(false);
            tab_tabla.getColumna("ide_ccctr").setVisible(false);
            tab_tabla.getColumna("ide_usua").setVisible(false);
            tab_tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));

            tab_tabla.dibujar();
            tab_tabla.insertar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla);
            pat_panel2.getMenuTabla().setRendered(false);
            gru_grupo.getChildren().add(pat_panel2);
        }
        mep_menu.dibujar(10, "INGRESAR TRANSACCIÓN", gru_grupo);
    }

    public void dibujarTransacciones() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {

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
            bot_consultar.setMetodo("actualizarTransacciones");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);

            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(-1);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.setCampoPrimaria("ide_ccdtr");
            tab_tabla.getColumna("ide_ccdtr").setVisible(false);
            tab_tabla.getColumna("IDE_TECLB").setVisible(false);
            tab_tabla.getColumna("FECHA_TRANS_CCDTR").setNombreVisual("FECHA");
            tab_tabla.getColumna("NUMERO_PAGO_CCDTR").setVisible(false);
            tab_tabla.getColumna("INGRESOS").alinearDerecha();
            tab_tabla.getColumna("EGRESOS").alinearDerecha();
            tab_tabla.getColumna("IDE_CNCCC").setFiltro(true);
            tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO.");
            tab_tabla.getColumna("IDE_CNCCC").setLink();
            tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
            tab_tabla.getColumna("TRANSACCION").setFiltroContenido();
            tab_tabla.getColumna("DOCUM_RELAC_CCDTR").setFiltroContenido();
            tab_tabla.getColumna("DOCUM_RELAC_CCDTR").setNombreVisual("N. DOCUMENTO");
            tab_tabla.getColumna("INGRESOS").alinearDerecha();
            tab_tabla.getColumna("EGRESOS").alinearDerecha();
            tab_tabla.getColumna("INGRESOS").setLongitud(20);
            tab_tabla.getColumna("EGRESOS").setLongitud(20);
            tab_tabla.getColumna("saldo").setLongitud(25);
            tab_tabla.getColumna("saldo").alinearDerecha();
            tab_tabla.getColumna("saldo").setEstilo("font-weight: bold;");
            tab_tabla.setColumnaSuma("INGRESOS,EGRESOS,saldo");
            tab_tabla.getColumna("INGRESOS").setSuma(false);
            tab_tabla.getColumna("EGRESOS").setSuma(false);
            tab_tabla.getColumna("saldo").setSuma(false);
            tab_tabla.setOrdenar(false);
            tab_tabla.setLectura(true);
            tab_tabla.setScrollable(true);
            tab_tabla.setRows(20);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);

            actualizarSaldoxCobrar();
        }
        mep_menu.dibujar(2, "TRANSACCIONES DEL CLIENTE", gru_grupo);
    }

    public void dibujarProductos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {

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
            bot_consultar.setMetodo("actualizarProductos");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(-1);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("ide_ccdfa").setVisible(false);
            tab_tabla.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
            tab_tabla.getColumna("serie_ccdaf").setNombreVisual("N. SERIE");
            tab_tabla.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_tabla.getColumna("nombre_inarti").setNombreVisual("ARTICULO");
            tab_tabla.getColumna("cantidad_ccdfa").setNombreVisual("CANTIDAD");
            tab_tabla.getColumna("precio_ccdfa").setNombreVisual("PRECIO UNI.");
            tab_tabla.getColumna("total_ccdfa").setNombreVisual("TOTAL");
            tab_tabla.getColumna("precio_ccdfa").alinearDerecha();
            tab_tabla.getColumna("total_ccdfa").alinearDerecha();
            tab_tabla.getColumna("total_ccdfa").setEstilo("font-weight: bold");
            tab_tabla.getColumna("nombre_inarti").setFiltroContenido();
            tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();

            tab_tabla.setLectura(true);
            tab_tabla.setRows(20);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);
        }

        mep_menu.dibujar(3, "DETALLE DE PRODUCTOS VENDIDOS AL CLIENTE", gru_grupo);
    }

    public void dibujarConfiguraCuenta() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            aut_cuentas = new AutoCompletar();
            aut_cuentas.setId("aut_cuentas");
            aut_cuentas.setAutoCompletar(ser_contabilidad.getSqlCuentasActivos());
            aut_cuentas.setSize(75);
            aut_cuentas.setDisabled(true);
            aut_cuentas.setMetodoChange("seleccionarCuentaContable");
            aut_cuentas.setValor(ser_cliente.getCuentaCliente(aut_clientes.getValor()));
            aut_cuentas.setAutocompletarContenido();

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
            gru_grupo.getChildren().add(new Etiqueta("<p style='padding-top:10px;'><strong >NOTA: </strong> La cuenta contable seleccionada se relacionará a los movimientos-transacciones que realice el Cliente, a partir que se <strong>guarde </strong> el cambio. </p>"));
        }
        mep_menu.dibujar(4, "CONFIGURACIÓN DE CUENTA CONTABLE", gru_grupo);
    }

    public void dibujarMovimientos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            TablaGenerica tab_cuenta = ser_contabilidad.getCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()));
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

                gru_grupo.getChildren().add(fis_consulta);

                tab_tabla = new Tabla();
                tab_tabla.setNumeroTabla(-1);
                tab_tabla.setId("tab_tabla");
                tab_tabla.setSql(ser_contabilidad.getSqlMovimientosCuentaPersona(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), aut_clientes.getValor()));
                tab_tabla.setLectura(true);
                tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
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
                utilitario.agregarMensajeInfo("No se encontro Cuenta Contable", "El cliente seleccionado no tiene asociado una cuenta contable");
            }
        }
        mep_menu.dibujar(5, "MOVIMIENTOS CONTABLES", gru_grupo);
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

    public void dibujarFacturas() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(-1);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_cliente.getSqlFacturasPorCobrar(aut_clientes.getValor()));
            tab_tabla.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("saldo_x_pagar").alinearDerecha();
            tab_tabla.setCampoPrimaria("ide_ccctr");
            tab_tabla.getColumna("ide_ccctr").setVisible(false);
            tab_tabla.getColumna("fecha").setVisible(true);
            tab_tabla.getColumna("serie_ccdaf").setNombreVisual("SERIE");
            tab_tabla.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
            tab_tabla.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
            tab_tabla.getColumna("total_cccfa").setNombreVisual("TOTAL");
            tab_tabla.getColumna("total_cccfa").setEstilo("font-size: 13px;");
            tab_tabla.getColumna("total_cccfa").alinearDerecha();
            tab_tabla.setRows(20);
            tab_tabla.setLectura(true);
            tab_tabla.setColumnaSuma("saldo_x_pagar");
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);

            if (tab_tabla.isEmpty()) {
                tab_tabla.setEmptyMessage("El cliente no tiene facturas por pagar");
            }
        }
        mep_menu.dibujar(6, "FACTURAS POR COBRAR AL CLIENTE", gru_grupo);
    }

    public void dibujarEstructura() {
        Grupo gru_grupo = new Grupo();
        arb_estructura = new Arbol();
        arb_estructura.setId("arb_estructura");
        arb_estructura.setArbol("gen_persona", "ide_geper", "nom_geper", "gen_ide_geper");
        arb_estructura.setCondicion("es_cliente_geper=true");
        arb_estructura.dibujar();
        //Selecciona el nodo
        if (aut_clientes.getValor() != null) {
            arb_estructura.seleccinarNodo(aut_clientes.getValor());
            arb_estructura.getNodoSeleccionado().setExpanded(true);
            arb_estructura.getNodoSeleccionado().getParent().setExpanded(true);
        }
        PanelArbol paa_panel = new PanelArbol();
        paa_panel.setPanelArbol(arb_estructura);
        gru_grupo.getChildren().add(paa_panel);
        mep_menu.dibujar(7, "CLASIFICACIÓN DE CLIENTES", gru_grupo);
    }

    public void agregarPadre() {
        if (mep_menu.getOpcion() == 7) {
            TablaGenerica tab_padre_cliente = new TablaGenerica();
            tab_padre_cliente.setTabla("gen_persona", "ide_geper");
            tab_padre_cliente.setCondicion("ide_geper=-1");
            tab_padre_cliente.ejecutarSql();
        }
    }

    public void dibujarGrafico() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            gca_grafico = new GraficoCartesiano();
            gca_grafico.setId("gca_grafico");

            com_periodo = new Combo();
            com_periodo.setMetodo("actualizarGrafico");
            com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
            com_periodo.eliminarVacio();
            com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_cliente.getSqlTotalVentasMensualesCliente(aut_clientes.getValor(), String.valueOf(com_periodo.getValue())));
            tab_tabla.setLectura(true);
            tab_tabla.setColumnaSuma("num_facturas,ventas12,ventas0,iva,total");
            tab_tabla.getColumna("num_facturas").alinearDerecha();
            tab_tabla.getColumna("ventas12").alinearDerecha();
            tab_tabla.getColumna("ventas0").alinearDerecha();
            tab_tabla.getColumna("iva").alinearDerecha();
            tab_tabla.getColumna("total").alinearDerecha();
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

    public void dibujarProductosVendidos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_cliente.getSqlProductosVendidos(aut_clientes.getValor()));
            tab_tabla.getColumna("ide_inarti").setVisible(false);
            tab_tabla.setRows(25);
            tab_tabla.getColumna("nombre_inarti").setNombreVisual("PRODUCTO");
            tab_tabla.getColumna("nombre_inarti").setFiltroContenido();
            tab_tabla.getColumna("nombre_inarti").setLongitud(200);
            tab_tabla.getColumna("fecha_ultima_venta").setNombreVisual("FECHA ULTIMA VENTA");
            tab_tabla.getColumna("fecha_ultima_venta").setLongitud(100);
            tab_tabla.getColumna("ultimo_precio").setNombreVisual("PRECIO ULTIMA VENTA");
            tab_tabla.getColumna("ultimo_precio").setFormatoNumero(2);
            tab_tabla.getColumna("ultimo_precio").alinearDerecha();
            tab_tabla.getColumna("ultimo_precio").setLongitud(100);
            tab_tabla.getColumna("ultimo_precio").setEstilo("font-weight: bold;font-size:14px");
            tab_tabla.setLectura(true);
            tab_tabla.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel1);

        }
        mep_menu.dibujar(9, "PRODUCTOS VENDIDOS", gru_grupo);
    }

    public void actualizarGrafico() {
        tab_tabla.setSql(ser_cliente.getSqlTotalVentasMensualesCliente(aut_clientes.getValor(), String.valueOf(com_periodo.getValue())));
        tab_tabla.ejecutarSql();
        gca_grafico.limpiar();
        gca_grafico.agregarSerie(tab_tabla, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
        utilitario.addUpdate("gca_grafico");
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_tabla.setSql(ser_contabilidad.getSqlMovimientosCuentaPersona(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), aut_clientes.getValor()));
        tab_tabla.ejecutarSql();
        actualizarSaldosContable();
    }

    /**
     * Actualiza las transacciones de un cliente segun las fechas selecionadas
     */
    public void actualizarTransacciones() {
        tab_tabla.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.ejecutarSql();
        actualizarSaldoxCobrar();
    }

    /**
     * Actualiza los productos comprados del cliente segun las fechas
     * selecionadas
     */
    public void actualizarProductos() {
        tab_tabla.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.ejecutarSql();
        if (tab_tabla.isEmpty()) {

            tab_tabla.setEmptyMessage("No existen Productos en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza los solados que se visualizan en pantalla
     */
    private void actualizarSaldosContable() {

        double saldo_anterior = ser_contabilidad.getSaldoInicialCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha());
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

    /**
     * *
     * Activa el Autocompletar para cambiar la cuenta contable
     */
    public void activaCambiarCuenta() {
        if (isClienteSeleccionado()) {
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
        if (isClienteSeleccionado()) {
            if (aut_cuentas.getValor() != null) {
                utilitario.getConexion().getSqlPantalla().clear();
                if (ser_cliente.isTieneCuentaConfiguradaCliente(aut_clientes.getValor()) == false) {
                    //nueva
                    utilitario.getConexion().agregarSqlPantalla(ser_cliente.getSqlInsertarCuentaCliente(aut_clientes.getValor(), aut_cuentas.getValor()));
                } else {
                    //modifica
                    utilitario.getConexion().agregarSqlPantalla(ser_cliente.getSqlActualizarCuentaCliente(aut_clientes.getValor(), aut_cuentas.getValor()));
                }
            }
        }

    }

    /**
     * Validacion para que se seleccione un cliente del Autocompletar
     *
     * @return
     */
    private boolean isClienteSeleccionado() {
        if (aut_clientes.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Cliente", "Seleccione un cliente de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    /**
     * Calcula el saldo por cobrar del Cliente
     *
     * @return
     */
    private void actualizarSaldoxCobrar() {
        double saldo_anterior = ser_cliente.getSaldoInicialCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_ingresos = 0;
        double dou_egresos = 0;

        for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
            if (tab_tabla.getValor(i, "ingresos") != null) {
                dou_ingresos += Double.parseDouble(tab_tabla.getValor(i, "ingresos"));
                dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_tabla.getValor(i, "ingresos"));
            } else {
                dou_egresos += Double.parseDouble(tab_tabla.getValor(i, "egresos"));
                dou_saldo_actual = saldo_anterior - Double.parseDouble(tab_tabla.getValor(i, "egresos"));
            }

            tab_tabla.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
            saldo_anterior = dou_saldo_actual;
        }
        if (tab_tabla.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_tabla.setEmptyMessage("No existen Transacciones en el rango de fechas seleccionado");
        }

        //INSERTA PRIMERA FILA SALDO INICIAL
        if (dou_saldo_inicial != 0) {
            tab_tabla.setLectura(false);
            tab_tabla.insertar();
            tab_tabla.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_tabla.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_tabla.setValor("FECHA_TRANS_CCDTR", cal_fecha_inicio.getFecha());
            tab_tabla.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_tabla.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_tabla.getColumna("INGRESOS").setTotal(dou_ingresos);
        tab_tabla.getColumna("EGRESOS").setTotal(dou_egresos);

    }

    /**
     * Limpia la pantalla y el autocompletar
     */
    public void limpiar() {
        aut_clientes.limpiar();
        mep_menu.limpiar();
    }

    @Override
    public void insertar() {

        aut_clientes.limpiar();
        //FORMULARIO CLIENTE
        dibujarCliente();
        tab_tabla.limpiar();
        tab_tabla.insertar();

    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO CLIENTE
            if (ser_cliente.validarCliente(tab_tabla)) {
                tab_tabla.guardar();
                if (guardarPantalla().isEmpty()) {
                    //Actualiza el autocompletar
                    aut_clientes.actualizar();
                    aut_clientes.setSize(75);
                    aut_clientes.setValor(tab_tabla.getValorSeleccionado());
                    utilitario.addUpdate("aut_clientes");
                }
            }
        } else if (mep_menu.getOpcion() == 4) {
            //Cambiar Cuenta Contable
            if (guardarPantalla().isEmpty()) {

                aut_cuentas.setDisabled(true);
            }
        } else if (mep_menu.getOpcion() == 7) {
            //Classificacion de Clientes
            guardarPantalla();
        } else if (mep_menu.getOpcion() == 10) {
            TablaGenerica tab_cab = new TablaGenerica();
            tab_cab.setTabla("cxc_cabece_transa", "ide_ccctr");
            tab_cab.setCondicion("ide_ccctr=-1");
            tab_cab.ejecutarSql();
            tab_cab.insertar();
            tab_cab.setValor("fecha_trans_ccctr", tab_tabla.getValor("fecha_trans_ccdtr"));
            tab_cab.setValor("ide_geper", aut_clientes.getValor());
            tab_cab.setValor("observacion_ccctr", tab_tabla.getValor("observacion_ccdtr"));
            tab_cab.setValor("ide_ccttr", tab_tabla.getValor("ide_ccttr"));
            tab_cab.guardar();
            String ide_ccctr = tab_cab.getValor("ide_ccctr");
            tab_tabla.setValor("fecha_venci_ccdtr", tab_tabla.getValor("fecha_trans_ccdtr"));
            tab_tabla.setValor("ide_ccctr", ide_ccctr);
            tab_tabla.guardar();
            if (guardarPantalla().isEmpty()) {
                dibujarTransacciones();
            }
        }
    }

    @Override
    public void eliminar() {

        //FORMULARIO CLIENTE
        if (tab_tabla.eliminar()) {
            aut_clientes.actualizar();
            aut_clientes.setSize(75);
            utilitario.addUpdate("aut_clientes");
            limpiar();
        }
    }

    @Override
    public void actualizar() {
        super.actualizar();
        if (mep_menu.getOpcion() == 2) {
            actualizarSaldoxCobrar();
        } else if (mep_menu.getOpcion() == 5) {
            actualizarSaldosContable();
        }
    }

    public AutoCompletar getAut_clientes() {
        return aut_clientes;
    }

    public void setAut_clientes(AutoCompletar aut_clientes) {
        this.aut_clientes = aut_clientes;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public AutoCompletar getAut_cuentas() {
        return aut_cuentas;
    }

    public void setAut_cuentas(AutoCompletar aut_cuentas) {
        this.aut_cuentas = aut_cuentas;
    }

    public Arbol getArb_estructura() {
        return arb_estructura;
    }

    public void setArb_estructura(Arbol arb_estructura) {
        this.arb_estructura = arb_estructura;
    }

    public GraficoCartesiano getGca_grafico() {
        return gca_grafico;
    }

    public void setGca_grafico(GraficoCartesiano gca_grafico) {
        this.gca_grafico = gca_grafico;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidad;
import servicios.cuentas_x_cobrar.ServicioCliente;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_clientes extends Pantalla {

    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_clientes = new AutoCompletar();

    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;

    private Tabla tab_cliente; //formulario cliente    
    private Tabla tab_transacciones_cxc; //transacciones cliente
    private Tabla tab_productos; //transacciones cliente
    private Tabla tab_facturas; //Facturas pendientes

    /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
    private AutoCompletar aut_cuentas;
    private Tabla tab_movimientos; //movimientos contables
    private Texto tex_saldo_inicial;
    private Texto tex_saldo_final;
    private Texto tex_total_debe;
    private Texto tex_total_haber;

    public pre_clientes() {
        aut_clientes.setId("aut_clientes");
        aut_clientes.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_clientes.setSize(75);
        aut_clientes.setAutocompletarContenido(); // no startWith para la busqueda
        aut_clientes.setMetodoChange("seleccionarCliente");
        bar_botones.agregarComponente(aut_clientes);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        mep_menu.setMenuPanel("OPCIONES CLIENTE", "20%");
        mep_menu.agregarItem("Información Cliente", "dibujarCliente", "ui-icon-person");
        mep_menu.agregarItem("Transacciones Cliente", "dibujarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Productos Cliente", "dibujarProductos", "ui-icon-cart");
        mep_menu.agregarItem("Facturas Por Cobrar", "dibujarFacturas", "ui-icon-calculator");
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Configura Cuenta Contable", "dibujarConfiguraCuenta", "ui-icon-wrench");
        mep_menu.agregarItem("Movimientos Contables", "dibujarMovimientos", "ui-icon-note");

        agregarComponente(mep_menu);

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
                default:
                    dibujarCliente();
            }
        } else {
            limpiar();
        }
    }

    /**
     * Dibuja el formulario de datos del Cliente, osigna opcion 1
     */
    public void dibujarCliente() {
        tab_cliente = new Tabla();
        tab_cliente.setId("tab_cliente");
        ser_cliente.configurarTablaCliente(tab_cliente);
        tab_cliente.setTabla("gen_persona", "ide_geper", 1);
        tab_cliente.setCondicion("ide_geper=" + aut_clientes.getValor());
        tab_cliente.setMostrarNumeroRegistros(false);
        tab_cliente.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_cliente);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(1, "DATOS DEL CLIENTE", pat_panel);
    }

    public void dibujarTransacciones() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {

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

            tab_transacciones_cxc = new Tabla();
            tab_transacciones_cxc.setNumeroTabla(2);
            tab_transacciones_cxc.setId("tab_transacciones_cxc");
            tab_transacciones_cxc.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_transacciones_cxc.getColumna("IDE_TECLB").setVisible(false);
            tab_transacciones_cxc.getColumna("FECHA_TRANS_CCDTR").setNombreVisual("FECHA");
            tab_transacciones_cxc.getColumna("IDE_CCDTR").setVisible(false);
            tab_transacciones_cxc.getColumna("NUMERO_PAGO_CCDTR").setVisible(false);
            tab_transacciones_cxc.getColumna("INGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("EGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("IDE_CNCCC").setFiltro(true);
            tab_transacciones_cxc.getColumna("IDE_CNCCC").setNombreVisual("N. COMP");
            tab_transacciones_cxc.getColumna("TRANSACCION").setFiltroContenido();
            tab_transacciones_cxc.getColumna("DOCUM_RELAC_CCDTR").setFiltroContenido();
            tab_transacciones_cxc.getColumna("DOCUM_RELAC_CCDTR").setNombreVisual("N. DOCUMENTO");
            tab_transacciones_cxc.getColumna("INGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("EGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("INGRESOS").setLongitud(20);
            tab_transacciones_cxc.getColumna("EGRESOS").setLongitud(20);
            tab_transacciones_cxc.getColumna("saldo").setLongitud(25);
            tab_transacciones_cxc.getColumna("saldo").alinearDerecha();
            tab_transacciones_cxc.getColumna("saldo").setEstilo("font-weight: bold;");

            tab_transacciones_cxc.setLectura(true);
            tab_transacciones_cxc.setScrollable(true);
            tab_transacciones_cxc.setScrollHeight(300);
            tab_transacciones_cxc.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_transacciones_cxc);
            gru_grupo.getChildren().add(pat_panel);

            actualizarSaldoxCobrar();
        }
        mep_menu.dibujar(2, "TRANSACCIONES DEL CLIENTE", gru_grupo);
    }

    public void dibujarProductos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
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
            tab_productos.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_productos.getColumna("ide_ccdfa").setVisible(false);
            tab_productos.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
            tab_productos.getColumna("serie_ccdaf").setNombreVisual("N. SERIE");
            tab_productos.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_productos.getColumna("nombre_inarti").setNombreVisual("ARTICULO");
            tab_productos.getColumna("cantidad_ccdfa").setNombreVisual("CANTIDAD");
            tab_productos.getColumna("precio_ccdfa").setNombreVisual("PRECIO UNI.");
            tab_productos.getColumna("total_ccdfa").setNombreVisual("TOTAL");
            tab_productos.getColumna("precio_ccdfa").alinearDerecha();
            tab_productos.getColumna("total_ccdfa").alinearDerecha();
            tab_productos.getColumna("total_ccdfa").setEstilo("font-weight: bold");

            tab_productos.setLectura(true);
            tab_productos.setScrollable(true);
            tab_productos.setScrollHeight(300);
            tab_productos.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_productos);
            gru_grupo.getChildren().add(pat_panel);
        }

        mep_menu.dibujar(3, "PRODUCTOS COMPRADOS POR EL CLIENTE", gru_grupo);
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

            Grid gri_contenido = new Grid();
            gri_contenido.setColumns(3);
            gri_contenido.getChildren().add(new Etiqueta("CUENTA CONTABLE : "));
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
                tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                tab_movimientos.setLectura(true);
                tab_movimientos.getColumna("ide_cnccc").setVisible(false);
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
                utilitario.agregarMensajeInfo("No se encontro Cuenta Contable", "El cliente seleccionado no tiene asociado una cuenta contable");
            }
        }
        mep_menu.dibujar(5, "MOVIMIENTOS CONTABLES", gru_grupo);
    }

    public void dibujarFacturas() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            tab_facturas = new Tabla();
            tab_facturas.setNumeroTabla(6);
            tab_facturas.setId("tab_facturas");
            tab_facturas.setSql(ser_cliente.getSqlFacturasPorCobrar(aut_clientes.getValor()));
            tab_facturas.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
            tab_facturas.getColumna("saldo_x_pagar").alinearDerecha();
            tab_facturas.setCampoPrimaria("ide_ccctr");
            tab_facturas.getColumna("ide_ccctr").setVisible(false);
            tab_facturas.getColumna("fecha").setVisible(true);
            tab_facturas.getColumna("serie_ccdaf").setNombreVisual("SERIE");
            tab_facturas.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_facturas.getColumna("secuencial_cccfa").setFiltroContenido();
            tab_facturas.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
            tab_facturas.getColumna("total_cccfa").setNombreVisual("TOTAL");
            tab_facturas.getColumna("total_cccfa").setEstilo("font-size: 13px;");
            tab_facturas.getColumna("total_cccfa").alinearDerecha();
            tab_facturas.setLectura(true);
            tab_facturas.setColumnaSuma("saldo_x_pagar");
            tab_facturas.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_facturas);
            gru_grupo.getChildren().add(pat_panel);

            if (tab_facturas.isEmpty()) {
                tab_facturas.setEmptyMessage("El cliente no tiene facturas por pagar");
            }
        }
        mep_menu.dibujar(6, "FACTURAS POR COBRAR AL CLIENTE", gru_grupo);
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_movimientos.ejecutarSql();
        actualizarSaldosContable();
    }

    /**
     * Actualiza las transacciones de un cliente segun las fechas selecionadas
     */
    public void actualizarTransacciones() {
        tab_transacciones_cxc.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_transacciones_cxc.ejecutarSql();
        actualizarSaldoxCobrar();
    }

    /**
     * Actualiza los productos comprados del cliente segun las fechas
     * selecionadas
     */
    public void actualizarProducots() {
        tab_productos.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_productos.ejecutarSql();
        if (tab_productos.isEmpty()) {

            tab_productos.setEmptyMessage("No Productos en el rango de fechas seleccionado");
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
     * Validaciones para crear o modificar un Cliente
     *
     * @return
     */
    private boolean validarCliente() {
        if (tab_cliente.getValor("ide_getid") != null && tab_cliente.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
            if (utilitario.validarCedula(tab_cliente.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de cédula válida");
                return false;
            }
        }
        if (tab_cliente.getValor("ide_getid") != null && tab_cliente.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
            if (utilitario.validarRUC(tab_cliente.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de ruc válido");
                return false;
            }
        }
        if (tab_cliente.getValor("ide_cntco") == null || tab_cliente.getValor("ide_cntco").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar el tipo de contribuyente");
            return false;
        }
        //      }
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

        for (int i = 0; i < tab_transacciones_cxc.getTotalFilas(); i++) {
            if (tab_transacciones_cxc.getValor(i, "ingresos") != null) {
                dou_ingresos += Double.parseDouble(tab_transacciones_cxc.getValor(i, "ingresos"));
                dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_transacciones_cxc.getValor(i, "ingresos"));
            } else {
                dou_egresos += Double.parseDouble(tab_transacciones_cxc.getValor(i, "egresos"));
                dou_saldo_actual = saldo_anterior - Double.parseDouble(tab_transacciones_cxc.getValor(i, "egresos"));
            }

            tab_transacciones_cxc.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
            saldo_anterior = dou_saldo_actual;
        }
        if (tab_transacciones_cxc.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_transacciones_cxc.setEmptyMessage("No existen Transacciones en el rango de fechas seleccionado");
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
        aut_clientes.limpiar();
        mep_menu.limpiar();
    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == -1) {
            //PANTALLA LIMPIA
            dibujarCliente();
        }

        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO CLIENTE
            aut_clientes.limpiar();
            tab_cliente.insertar();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO CLIENTE
            if (validarCliente()) {
                tab_cliente.guardar();
                if (guardarPantalla().isEmpty()) {
                    //Actualiza el autocompletar
                    aut_clientes.actualizar();
                    aut_clientes.setSize(75);
                    aut_clientes.setValor(tab_cliente.getValorSeleccionado());
                    utilitario.addUpdate("aut_clientes");
                }
            }
        } else if (mep_menu.getOpcion() == 4) {
            if (guardarPantalla().isEmpty()) {
                aut_cuentas.setDisabled(true);
            }
        }
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO CLIENTE
            tab_cliente.eliminar();
        }
    }

    public AutoCompletar getAut_clientes() {
        return aut_clientes;
    }

    public void setAut_clientes(AutoCompletar aut_clientes) {
        this.aut_clientes = aut_clientes;
    }

    public Tabla getTab_cliente() {
        return tab_cliente;
    }

    public void setTab_cliente(Tabla tab_cliente) {
        this.tab_cliente = tab_cliente;
    }

    public Tabla getTab_transacciones_cxc() {
        return tab_transacciones_cxc;
    }

    public void setTab_transacciones_cxc(Tabla tab_transacciones_cxc) {
        this.tab_transacciones_cxc = tab_transacciones_cxc;
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

}

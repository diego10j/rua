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
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
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

    /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
    private AutoCompletar aut_cuentas;

    private Tabla tab_movimientos; //movimientos contables

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
        Grupo contenido = new Grupo();

        Grid gri_saldos = new Grid();
        gri_saldos.setColumns(2);
        Etiqueta eti_saldo = new Etiqueta();
        eti_saldo.setEstiloCabecera("border:none;font-size: 14px");
        eti_saldo.setValue("SALDO POR COBRAR : ");
        gri_saldos.getChildren().add(eti_saldo);
        gri_saldos.setTitle(aut_clientes.getValorArreglo(2));

        contenido.getChildren().add(gri_saldos);

        tab_transacciones_cxc = new Tabla();
        tab_transacciones_cxc.setNumeroTabla(2);
        tab_transacciones_cxc.setId("tab_transacciones_cxc");
        tab_transacciones_cxc.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor()));
        tab_transacciones_cxc.setCampoPrimaria("IDE_CCDTR");
        tab_transacciones_cxc.getColumna("IDE_TECLB").setVisible(false);
        tab_transacciones_cxc.getColumna("IDE_CCDTR").setVisible(false);
        tab_transacciones_cxc.getColumna("NUMERO_PAGO_CCDTR").setVisible(false);
        tab_transacciones_cxc.getColumna("INGRESOS").alinearDerecha();
        tab_transacciones_cxc.getColumna("EGRESOS").alinearDerecha();
        tab_transacciones_cxc.getColumna("IDE_CNCCC").setFiltro(true);
        tab_transacciones_cxc.getColumna("DOCUM_RELAC_CCDTR").setFiltroContenido();
        tab_transacciones_cxc.getColumna("INGRESOS").setEstilo("font-weight: bold;");
        tab_transacciones_cxc.getColumna("EGRESOS").setEstilo("font-weight: bold;");
        tab_transacciones_cxc.setLectura(true);
        tab_transacciones_cxc.setScrollable(true);
        tab_transacciones_cxc.setScrollHeight(400);
        tab_transacciones_cxc.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_transacciones_cxc);
        contenido.getChildren().add(pat_panel);

        gri_saldos.getChildren().add(new Etiqueta(" <span style='font-weight: bold; font-size: 17px;'> " + utilitario.getFormatoNumero(calcularSaldoxPagar()) + "</span>  <span class='fa fa-usd'/>"));

        mep_menu.dibujar(2, "TRANSACCIONES DEL CLIENTE", contenido);
    }

    public void dibujarProductos() {
        tab_productos = new Tabla();
        tab_productos.setNumeroTabla(3);
        tab_productos.setId("tab_productos");
        tab_productos.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor()));
        tab_productos.setCampoPrimaria("ide_ccdfa");
        tab_productos.getColumna("ide_ccdfa").setVisible(false);
        tab_productos.setLectura(true);
        tab_productos.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_productos);
        mep_menu.dibujar(3, "PRODUCTOS COMPRADOS POR EL CLIENTE", pat_panel);
    }

    public void dibujarConfiguraCuenta() {
        aut_cuentas = new AutoCompletar();
        aut_cuentas.setId("aut_cuentas");
        aut_cuentas.setAutoCompletar(ser_contabilidad.getSqlCuentasActivos());
        aut_cuentas.setSize(75);
        aut_cuentas.setDisabled(true);
        aut_cuentas.setMetodoChange("seleccionarCuentaContable");
        if (aut_clientes.getValor() != null) {
            aut_cuentas.setValor(ser_cliente.getCuentaCliente(aut_clientes.getValor()));
        }
        Grupo gru_grupo = new Grupo();
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

        mep_menu.dibujar(4, "CONFIGURACIÓN DE CUENTA CONTABLE", gru_grupo);
    }

    public void dibujarMovimientos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            TablaGenerica tab_cuenta = ser_contabilidad.getCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()));
            if (!tab_cuenta.isEmpty()) {
                gru_grupo.getChildren().add(new Etiqueta("<p style='font-size:16px;padding-bottom:5px;'> <strong>" + tab_cuenta.getValor("codig_recur_cndpc") + "</strong> &nbsp; " + tab_cuenta.getValor("nombre_cndpc") + "</p>"));
                Fieldset fis_consulta = new Fieldset();
                fis_consulta.setLegend("Detalle de la Consulta");

                Grid gri_fechas = new Grid();
                gri_fechas.setColumns(5);
                gri_fechas.getChildren().add(new Etiqueta("FECHA DESDE :"));
                cal_fecha_inicio = new Calendario();
                cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
                gri_fechas.getChildren().add(cal_fecha_inicio);
                gri_fechas.getChildren().add(new Etiqueta("FECHA HASTA :"));
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

                tab_movimientos = new Tabla();
                tab_movimientos.setNumeroTabla(4);
                tab_movimientos.setId("tab_movimientos");
                tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                tab_movimientos.setLectura(true); 
                tab_movimientos.setCampoPrimaria("ide_cnccc");
                tab_movimientos.getColumna("ide_cnccc").setVisible(false);
                tab_movimientos.getColumna("ide_cnlap").setVisible(false);
                tab_movimientos.getColumna("debe").setLongitud(30);
                tab_movimientos.getColumna("haber").setLongitud(30);
                tab_movimientos.getColumna("saldo").setLongitud(30);
                
                tab_movimientos.getColumna("debe").alinearDerecha();
                tab_movimientos.getColumna("haber").alinearDerecha();
                tab_movimientos.getColumna("saldo").alinearDerecha();
                tab_movimientos.getColumna("saldo").setEstilo("font-weight: bold;");
                
                tab_movimientos.getColumna("valor_cndcc").setVisible(false);
                tab_movimientos.setScrollRows(15);
                tab_movimientos.dibujar();
                PanelTabla pat_panel = new PanelTabla();
                pat_panel.setPanelTabla(tab_movimientos);
                gru_grupo.getChildren().add(pat_panel);

            } else {
                utilitario.agregarMensajeInfo("No se encontro Cuenta Contable", "El cliente seleccionado no tiene asociado una cuenta contable");
            }
        }
        mep_menu.dibujar(5, "MOVIMIENTOS CONTABLES", gru_grupo);
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_movimientos.ejecutarSql();
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
     * Calcula el saldo por pagar del Cliente
     *
     * @return
     */
    private double calcularSaldoxPagar() {
        double saldo_pos = tab_transacciones_cxc.getSumaColumna("INGRESOS");;
        double saldo_neg = tab_transacciones_cxc.getSumaColumna("EGRESOS");;
        return saldo_pos - saldo_neg;
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

}

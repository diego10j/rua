/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import servicios.cuentas_x_pagar.ServicioCliente;
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

    private Tabla tab_cliente; //formulario cliente    
    private Tabla tab_transacciones_cxc; //transacciones cliente
    private Tabla tab_productos; //transacciones cliente

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
        mep_menu.agregarItem("Datos Cliente", "dibujarCliente", "ui-icon-person");
        mep_menu.agregarItem("Transacciones Cliente", "dibujarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Productos Cliente", "dibujarProductos", "ui-icon-cart");
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
            dibujarCliente();
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
        tab_transacciones_cxc.setId("tab_transacciones_cxc");
        tab_transacciones_cxc.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor()));        
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
        tab_productos.setId("tab_productos");
        tab_productos.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor()));
        tab_productos.getColumna("ide_ccdfa").setVisible(false);
        tab_productos.setLectura(true);
        tab_productos.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_productos);
        mep_menu.dibujar(3, "PRODUCTOS COMPRADOS POR EL CLIENTE", pat_panel);
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

}

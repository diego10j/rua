/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelArbol;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
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
    private Arbol arb_estructura;// Estructura Gerarquica de clientes

    public pre_articulos() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarComponente(new Etiqueta("PRODUCTO :"));
        aut_productos.setId("aut_productos");
        aut_productos.setAutoCompletar(ser_producto.getSqlProductosCombo());
        aut_productos.setSize(75);
        aut_productos.setAutocompletarContenido(); // no startWith para la busqueda
        aut_productos.setMetodoChange("seleccionarProducto");
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
        mep_menu.agregarItem("Kardex", "dibujarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Facturas de Ventas", "dibujarVentas", "ui-icon-calculator");
        mep_menu.agregarItem("Facturas de Compras", "dibujarCompras", "ui-icon-calculator");
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Configura Cuenta Contable", "dibujarConfiguraCuenta", "ui-icon-wrench");
        mep_menu.agregarItem("Movimientos Contables", "dibujarMovimientos", "ui-icon-note");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Gráfico de Ventas", "dibujarGrafico", "ui-icon-bookmark");
        mep_menu.agregarItem("Gráfico de Compras", "dibujarGrafico", "ui-icon-bookmark");
        mep_menu.agregarItem("Últimos Precios", "dibujarProductosVendidos", "ui-icon-cart");

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
                    // dibujarTransacciones();
                    break;
                case 3:
                    // dibujarProductos();
                    break;
                case 4:
                    // dibujarConfiguraCuenta();
                    break;
                case 5:
                    //  dibujarMovimientos();
                    break;
                case 6:
                    //  dibujarFacturas();
                    break;
                case 7:
                    dibujarEstructura();
                    break;
                case 8:
                    //  dibujarGrafico();
                    break;
                case 9:
                    //  dibujarProductosVendidos();
                    break;
                default:
                    dibujarProducto();
            }
        } else {
            limpiar();
        }
    }

    /**
     * Dibuja el formulario de datos del Cliente, osigna opcion 1
     */
    public void dibujarProducto() {
        tab_producto = new Tabla();
        tab_producto.setId("tab_producto");
        ser_producto.configurarTablaProducto(tab_producto);
        tab_producto.setTabla("inv_articulo", "ide_inarti", 1);
        tab_producto.setCondicion("ide_inarti!=53 and ide_inarti=" + aut_productos.getValor()); //no activos fijos
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

    public void limpiar() {
        aut_productos.limpiar();
        mep_menu.limpiar();
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}

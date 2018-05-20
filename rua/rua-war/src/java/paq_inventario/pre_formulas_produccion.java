/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_formulas_produccion extends Pantalla {

    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);

    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();

    public pre_formulas_produccion() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_cab_formula", "ide_incfo", 1);
        tab_tabla1.setHeader("FÓRMULA DE PRODUCCIÓN");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaProductos());
        tab_tabla1.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla1.getColumna("fecha_incfo").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("num_formula_incfo").setMascara("999999999");
        tab_tabla1.getColumna("num_formula_incfo").setEstilo("font-size: 11px;font-weight: bold");
        tab_tabla1.getColumna("num_formula_incfo").setEtiqueta();
        tab_tabla1.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_tabla1.getColumna("ide_inepi").setValorDefecto(utilitario.getVariable("p_inv_estado_normal"));
        tab_tabla1.getColumna("ide_inepi").setCombo("inv_est_prev_inve", "ide_inepi", "nombre_inepi", "");
        tab_tabla1.getColumna("ide_inepi").setLectura(true);
        tab_tabla1.getColumna("concepto_incfo").setRequerida(true);
        tab_tabla1.getColumna("cantidad_incfo").setRequerida(true);
        tab_tabla1.getColumna("total_materia_incfo").setLectura(true);
        tab_tabla1.getColumna("total_produccion_incfo").setLectura(true);
        tab_tabla1.getColumna("total_materia_incfo").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_tabla1.getColumna("total_produccion_incfo").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_tabla1.getColumna("total_servicios_incfo").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_tabla1.getColumna("total_gastos_incfo").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setHeader("DETALLE DE LA FÓRMULA DE PRODUCCIÓN");
        tab_tabla2.setTabla("inv_deta_formula", "ide_indfo", 2);
        tab_tabla2.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaProductos());
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("ide_inarti").setMetodoChange("cambioPrecioCantidad");
        tab_tabla2.getColumna("cantidad_indfo").setMetodoChange("cambioPrecioCantidad");
        tab_tabla2.getColumna("cantidad_indfo").setFormatoNumero(3);
        tab_tabla2.getColumna("costo_indfo").setMetodoChange("cambioPrecioCantidad");
        tab_tabla2.getColumna("total_indfo").setEstilo("font-size:12px;font-weight: bold;");
        tab_tabla2.getColumna("total_indfo").alinearDerecha();
        tab_tabla2.getColumna("total_indfo").setEtiqueta();
        tab_tabla2.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_tabla2.getColumna("ide_inuni").setLongitud(-1);
        tab_tabla2.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla2);

        Division div = new Division();
        div.dividir2(pat_panel, pat_panel1, "50%", "H");
        agregarComponente(div);

    }

    public void cambioPrecioCantidad(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        double dou_precioi = ser_producto.getUltimoPrecioIngresoInventario(tab_tabla2.getValor("ide_inarti"));
        tab_tabla2.setValor("costo_indfo", utilitario.getFormatoNumero(dou_precioi, 4));
        utilitario.addUpdateTabla(tab_tabla2, "costo_indfo", "");

        TablaGenerica tab_producto = ser_producto.getProducto(tab_tabla2.getValor("ide_inarti"));
        if (!tab_producto.isEmpty()) {
            //Carga la Unidad del producto seleccionado
            tab_tabla2.setValor("ide_inuni", tab_producto.getValor("ide_inuni"));
        }
        utilitario.addUpdateTabla(tab_tabla2, "costo_indfo,ide_inuni,total_indfo", "");
        calcularTotalDetalle();
    }

    public void cambioPrecioCantidad(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotalDetalle();
    }

    private void calcularTotalDetalle() {
        double cantidad = 0;
        double precio = 0;
        double total = 0;

        try {
            cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_indfo"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_tabla2.getValor("costo_indfo"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_tabla2.setValor("total_indfo", utilitario.getFormatoNumero(total));

        utilitario.addUpdateTabla(tab_tabla2, "total_indfo", "");
        calcularTotalFactura();
    }

    public void calcularTotalFormula(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotalFactura();
    }

    /**
     * Calcula totales de la factura
     */
    public void calcularTotalFactura() {
        try {
            double total_meteria = 0;

            double total_servicio = 0;
            double total_gasto = 0;

            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                try {
                    total_meteria = Double.parseDouble(tab_tabla2.getValor(i, "total_indfo")) + total_meteria;
                } catch (Exception e) {
                }
            }

            try {
                total_servicio = Double.parseDouble(tab_tabla1.getValor("total_servicios_incfo"));
            } catch (Exception e) {
            }

            try {
                total_gasto = Double.parseDouble(tab_tabla1.getValor("total_gastos_incfo"));
            } catch (Exception e) {
            }

            tab_tabla1.setValor("total_materia_incfo", utilitario.getFormatoNumero(total_meteria));
            tab_tabla1.setValor("total_produccion_incfo", utilitario.getFormatoNumero(total_meteria + total_servicio + total_gasto));

            utilitario.addUpdate("tab_tabla1");
        } catch (Exception e) {
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        if (tab_tabla1.isFocus()) {
            tab_tabla1.setValor("num_formula_incfo", ser_inventario.getSecuencialFormula());
        }
    }

    @Override
    public void guardar() {
        tab_tabla1.setValor("num_formula_incfo", ser_inventario.getSecuencialFormula());
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

}

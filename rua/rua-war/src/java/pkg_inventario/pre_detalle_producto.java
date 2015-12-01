/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

public class pre_detalle_producto extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private AutoCompletar aut_filtro_articulo = new AutoCompletar();
    private Boton bot_clean = new Boton();
    private Etiqueta eti_saldo_cant = new Etiqueta();
    private Etiqueta eti_saldo_valor = new Etiqueta();
    private Etiqueta eti_valor_promedio = new Etiqueta();
    private Texto tex_valor_promedio = new Texto();
    private Texto tex_saldo_cant = new Texto();
    private Texto tex_saldo_valor = new Texto();
    private Grid gri_totales = new Grid();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();

    public pre_detalle_producto() {

        aut_filtro_articulo.setId("aut_filtro_articulo");
        aut_filtro_articulo.setAutoCompletar("SELECT ide_inarti,nombre_inarti from inv_articulo arti "
                + "where hace_kardex_inarti is TRUE and arti.ide_empr=" + utilitario.getVariable("ide_empr") + "");
        aut_filtro_articulo.setMetodoChange("filtrar_articulo");
        bar_botones.agregarComponente(new Etiqueta("Articulo:"));
        bar_botones.agregarComponente(aut_filtro_articulo);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql("SELECT dci.ide_indci,cci.fecha_trans_incci,tci.ide_intci,nombre_intci,tci.signo_intci,tti.ide_intti,nombre_intti,cantidad_indci,precio_indci,valor_indci,precio_promedio_indci,'' as saldo_cant,'' as saldo_valor from "
                + " inv_det_comp_inve dci "
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                + "left join inv_articulo arti on dci.ide_inarti=arti.ide_inarti  "
                + "where dci.ide_inarti=-1 and dci.ide_sucu=" + utilitario.getVariable("ide_sucu") + " and dci.ide_empr=" + utilitario.getVariable("ide_empr") + " and arti.hace_kardex_inarti is true "
                + "ORDER BY cci.fecha_trans_incci,dci.ide_indci asc");

        tab_tabla1.setCampoPrimaria("ide_indci");
        tab_tabla1.getColumna("ide_intci").setVisible(false);
        tab_tabla1.getColumna("ide_intti").setVisible(false);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        div_division.setId("div_division");
        eti_saldo_cant.setValue("Cantidad Actual :");
        eti_saldo_cant.setStyle("font-size: 16px;font-weight: bold");
        eti_saldo_valor.setStyle("font-size: 16px;font-weight: bold");
        tex_saldo_cant.setStyle("font-size: 16px;font-weight: bold");
        eti_saldo_valor.setValue("Saldo Actual: ");
        tex_saldo_valor.setStyle("font-size: 16px;font-weight: bold");
        eti_valor_promedio.setValue("Precio Promedio: ");
        eti_valor_promedio.setStyle("font-size: 16px;font-weight: bold");
        tex_valor_promedio.setStyle("font-size: 16px;font-weight: bold");
        tex_valor_promedio.setDisabled(true);
        tex_saldo_cant.setDisabled(true);
        tex_saldo_valor.setDisabled(true);
        gri_totales.setId("gri_totales");
        gri_totales.setColumns(6);
        gri_totales.getChildren().add(eti_saldo_cant);
        gri_totales.getChildren().add(tex_saldo_cant);
        gri_totales.getChildren().add(eti_saldo_valor);
        gri_totales.getChildren().add(tex_saldo_valor);
        gri_totales.getChildren().add(eti_valor_promedio);
        gri_totales.getChildren().add(tex_valor_promedio);
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarComponente(bot_clean);
        bar_botones.agregarReporte();
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        sel_formato.setId("sel_formato");
        agregarComponente(sel_formato);

        div_division.setFooter(pat_panel1, gri_totales, "90%");
        agregarComponente(div_division);
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    Map parametros = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Comprobante de Contabilidad")) {
            if (rep_reporte.isVisible()) {
                parametros = new HashMap();
                TablaGenerica tab_cab_comp_inv = utilitario.consultar("SELECT * FROM inv_cab_comp_inve WHERE ide_incci in(SELECT ide_incci FROM inv_det_comp_inve WHERE ide_indci=" + tab_tabla1.getValor("ide_indci") + ")");
                if (tab_cab_comp_inv.getTotalFilas() > 0) {
                    if (tab_cab_comp_inv.getValor(0, "ide_cnccc") != null && !tab_cab_comp_inv.getValor(0, "ide_cnccc").isEmpty()) {
                        parametros.put("ide_cnccc", Long.parseLong(tab_cab_comp_inv.getValor(0, "ide_cnccc")));
                        parametros.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                        parametros.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                        sel_formato.setSeleccionFormatoReporte(parametros, rep_reporte.getPath());
                        sel_formato.dibujar();
                        rep_reporte.cerrar();
                        utilitario.addUpdate("sel_formato,rep_reporte");
                    } else {
                        utilitario.agregarMensajeInfo("No se puede generar el Reporte", "La transaccion de comprobante de inventario seleccionado no tiene Asiento Contable ");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede abrir", "La transaccion de comprobante de inventario seleccionado no tiene Asiento Contable ");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante de Inventario")) {
            if (rep_reporte.isVisible()) {
                parametros = new HashMap();
                TablaGenerica tab_cab_comp_inv = utilitario.consultar("SELECT * FROM inv_cab_comp_inve WHERE ide_incci in(SELECT ide_incci FROM inv_det_comp_inve WHERE ide_indci=" + tab_tabla1.getValor("ide_indci") + ")");
                if (tab_cab_comp_inv.getTotalFilas() > 0) {
                    if (tab_cab_comp_inv.getValor(0, "ide_incci") != null && !tab_cab_comp_inv.getValor(0, "ide_incci").isEmpty()) {
                        parametros.put("ide_incci", Long.parseLong(tab_cab_comp_inv.getValor(0, "ide_incci")));
                        sel_formato.setSeleccionFormatoReporte(parametros, rep_reporte.getPath());
                        sel_formato.dibujar();
                        rep_reporte.cerrar();
                        utilitario.addUpdate("sel_formato,rep_reporte");
                    } else {
                        utilitario.agregarMensajeInfo("No se puede generar el Reporte", "La transaccion de comprobante de inventario seleccionado no tiene Asiento Contable ");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede Abrir", "La transaccion del Comprobante de inventario no numero de Transaccion con el Comprobante de Inventario");
                }
            }

        }
    }

    public void filtrar_articulo(SelectEvent evt) {
        System.out.println("entra al metodo change ");
        aut_filtro_articulo.onSelect(evt);

        tab_tabla1.setSql("SELECT dci.ide_indci,cci.fecha_trans_incci,tci.ide_intci,nombre_intci,tci.signo_intci,tti.ide_intti,nombre_intti,cantidad_indci,precio_indci,valor_indci,precio_promedio_indci,'' as saldo_cant,'' as saldo_valor from "
                + " inv_det_comp_inve dci "
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                + "left join inv_articulo arti on dci.ide_inarti=arti.ide_inarti  "
                + "where dci.ide_inarti=" + aut_filtro_articulo.getValor() + " and dci.ide_sucu=" + utilitario.getVariable("ide_sucu") + " and dci.ide_empr=" + utilitario.getVariable("ide_empr") + " and arti.hace_kardex_inarti is true "
                + "ORDER BY cci.fecha_trans_incci,dci.ide_indci asc");
        tab_tabla1.ejecutarSql();
        System.out.println("Errores" + tab_tabla1.getSql());
        utilitario.addUpdate("tab_tabla1");
        double val_ant = 0;
        double cant_ant = 0;

        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            try {
                if (i == 0) {
                    tab_tabla1.setValor(i, "saldo_valor", tab_tabla1.getValor(i, "valor_indci"));
                    val_ant = Double.parseDouble(tab_tabla1.getValor(i, "valor_indci"));
                    cant_ant = Double.parseDouble(tab_tabla1.getValor(i, "cantidad_indci"));

                } else {

                    double val_tran = (Double.parseDouble(tab_tabla1.getValor(i, "valor_indci"))) * (Double.parseDouble(tab_tabla1.getValor(i, "signo_intci")));
                    double total = val_ant + val_tran;
                    val_ant = total;
                    tab_tabla1.setValor(i, "saldo_valor", utilitario.getFormatoNumero(total));
                    double cant_tran = (Double.parseDouble(tab_tabla1.getValor(i, "cantidad_indci"))) * (Double.parseDouble(tab_tabla1.getValor(i, "signo_intci")));
                    double total_cant = cant_ant + cant_tran;
                    cant_ant = total_cant;
                    tab_tabla1.setValor(i, "saldo_cant", utilitario.getFormatoNumero(total_cant));
                }
            } catch (Exception e) {
            }
        }
        if (tab_tabla1.getTotalFilas() == 0) {

            utilitario.agregarMensajeError("Atencion", "No existe informacion de Producto ");
            tex_saldo_cant.limpiar();
            tex_saldo_valor.limpiar();

            utilitario.addUpdate("div_division");
        }

        utilitario.addUpdate("tab_tabla1");
        calcular_totales();

    }

    public void calcular_totales() {

//        try {

        if (tab_tabla1.getTotalFilas() > 0) {
            double total_cantidad = 0;
            double total_valor = 0;
            for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
                try {
                    total_cantidad = Double.parseDouble(tab_tabla1.getValor(i, "signo_intci")) * Double.parseDouble(tab_tabla1.getValor(i, "cantidad_indci")) + total_cantidad;
                } catch (Exception e) {
                    total_cantidad = 0 + total_cantidad;

                }
                try {
                total_valor = Double.parseDouble(tab_tabla1.getValor(i, "signo_intci")) * Double.parseDouble(tab_tabla1.getValor(i, "valor_indci")) + total_valor;
                } catch (Exception e) {
                total_valor = 0 + total_valor;
                }

            }

            tex_saldo_cant.setValue(utilitario.getFormatoNumero(total_cantidad));
            tex_saldo_valor.setValue(utilitario.getFormatoNumero(total_valor));
            tex_valor_promedio.setValue(utilitario.getFormatoNumero(tab_tabla1.getValor(tab_tabla1.getTotalFilas() - 1, "precio_promedio_indci")));
            utilitario.addUpdate("gri_totales");

        }
//        } catch (Exception e) {
//        utilitario.agregarMensajeError(null, null);
//        }


    }

    public void limpiar() {
        aut_filtro_articulo.setValue(null);

        tab_tabla1.limpiar();
        tex_saldo_cant.limpiar();
        tex_saldo_valor.limpiar();
        utilitario.addUpdate("aut_filtro_articulo,tab_tabla1,div_division");

    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public AutoCompletar getAut_filtro_articulo() {
        return aut_filtro_articulo;
    }

    public void setAut_filtro_articulo(AutoCompletar aut_filtro_articulo) {
        this.aut_filtro_articulo = aut_filtro_articulo;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_formato() {
        return sel_formato;
    }

    public void setSel_formato(SeleccionFormatoReporte sel_formato) {
        this.sel_formato = sel_formato;
    }
}

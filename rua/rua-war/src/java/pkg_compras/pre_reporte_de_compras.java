package pkg_compras;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.reportes.ReporteDataSource;
import java.util.HashMap;
import java.util.Map;
import pkg_cuentas_x_pagar.cls_cuentas_x_pagar;
import sistema.aplicacion.Pantalla;

public class pre_reporte_de_compras extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private Etiqueta eti_cuenta = new Etiqueta("REPORTE DE COMPRAS: ");
    private Etiqueta eti_rango_fechas = new Etiqueta("DESDE: 0/0/0       HASTA: 0/0/0 ");
    private Grid gri_titulo = new Grid();
    private Grid gri_totales = new Grid();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Reporte rep_reporte = new Reporte();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    cls_cuentas_x_pagar cxp=new cls_cuentas_x_pagar();
    public pre_reporte_de_compras() {
        bar_botones.agregarCalendario();
        bar_botones.agregarReporte();
        sec_calendario.setId("sec_calendario");
        //por defecto friltra un mes
        sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -31));
        sec_calendario.setFecha2(utilitario.getDate());
        agregarComponente(sec_calendario);
        sec_calendario.getBot_aceptar().setMetodo("aceptarRango");

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(cxp.getSqlReporteCompras(sec_calendario.getFecha1String(), sec_calendario.getFecha2String()));
        tab_tabla1.setCampoPrimaria("ide_cncre");
        tab_tabla1.getColumna("ide_cncre").setVisible(false);
        tab_tabla1.getColumna("ide_cnimp").setVisible(false);
        tab_tabla1.setNumeroTabla(1);
        tab_tabla1.setScrollRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        eti_cuenta.setStyle("font-size: 14px;font-weight: bold;text-align: center");
        eti_rango_fechas.setStyle("font-size: 14px;font-weight: bold;text-align: center");
        gri_titulo.setId("gri_titulo");
        gri_titulo.setColumns(2);
        gri_titulo.setWidth("80%");
        Espacio esp1 = new Espacio("10", "10");
        gri_titulo.getChildren().add(eti_cuenta);
        gri_titulo.getChildren().add(esp1);
        gri_titulo.getChildren().add(eti_rango_fechas);

        div_division.setId("div_division");
        div_division.dividir2(gri_titulo, pat_panel, "15%", "H");

        agregarComponente(div_division);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(false);
        agregarComponente(sec_rango_reporte);

        cargarTitulo();
        llenarTabla();
    }
    Map parametro = new HashMap();
    String fecha_inicio;
    String fecha_fin;
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Reporte de Compras")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                        if (tab_datos.getTotalFilas() > 0) {
                            parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                            parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                            parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                            parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                            parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                            parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));

                        }
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = sec_rango_reporte.getFecha1String();
                        
                        TablaGenerica tab_rep_compras=cxp.getTablaReporteCompras(sec_rango_reporte.getFecha1String(), sec_rango_reporte.getFecha2String());
                        System.out.println("total  "+tab_rep_compras.getTotalFilas());
                        parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                        parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                        parametro.put("total_base_no_objeto_de_iva", "0");
                        parametro.put("total_base_0", "0");
                        parametro.put("total_base_gravada", "0");
                        parametro.put("total_iva", "0");
                        parametro.put("total_retencion_iva", "0");
                        parametro.put("total_base_retencion_ir", "0");
                        parametro.put("total_retencion_ir", "0");
                        ReporteDataSource data = new ReporteDataSource(tab_rep_compras);
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                        sel_rep.dibujar();
                        sec_rango_reporte.cerrar();
                        utilitario.addUpdate("sec_rango_reporte,sel_rep");
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha final");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicial");
                }
            }

        }
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        sec_rango_reporte.getCal_fecha1().setValue(null);
        sec_rango_reporte.getCal_fecha2().setValue(null);
        rep_reporte.dibujar();

    }

    public void aceptarRango() {
        if (sec_calendario.isFechasValidas()) {
            sec_calendario.cerrar();
            tab_tabla1.setSql(cxp.getSqlReporteCompras(sec_calendario.getFecha1String(), sec_calendario.getFecha2String()));
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("sec_calendario,tab_tabla1");
            cargarTitulo();
            llenarTabla();
        } else {
            utilitario.agregarMensajeInfo("Fechas no válidas", "");
        }
    }

    public Tabla llenarTabla() {
        //0 iva
        //1 renta

        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            //iva y renta
            try {
                if (tab_tabla1.getValor(i, "ide_cnimp").equals("0")) {
                    tab_tabla1.setValor(i, "por_ret_renta", "");
                    tab_tabla1.setValor(i, "val_ret_renta", "");
                } else {
                    tab_tabla1.setValor(i, "por_ret_iva", "");
                    tab_tabla1.setValor(i, "val_ret_iva", "");
                }
                if (Double.parseDouble(tab_tabla1.getValor(i, "base_no_objeto_iva_cpcfa")) == 0) {
                    tab_tabla1.setValor(i, "base_no_objeto_iva_cpcfa", "");
                }
                if (Double.parseDouble(tab_tabla1.getValor(i, "base_tarifa0_cpcfa")) == 0) {
                    tab_tabla1.setValor(i, "base_tarifa0_cpcfa", "");
                }
                if (Double.parseDouble(tab_tabla1.getValor(i, "base_grabada_cpcfa")) == 0) {
                    tab_tabla1.setValor(i, "base_grabada_cpcfa", "");
                }
                if (Double.parseDouble(tab_tabla1.getValor(i, "valor_iva_cpcfa")) == 0) {
                    tab_tabla1.setValor(i, "valor_iva_cpcfa", "");
                }
                String ide_cncre = "";
                if (i > 0) {
                    ide_cncre = tab_tabla1.getValor(i - 1, "ide_cncre");
                }
                if (ide_cncre.equals(tab_tabla1.getValor(i, "ide_cncre"))) {
                    tab_tabla1.setValor(i, "identificac_geper", "");
                    tab_tabla1.setValor(i, "numero_cncre", "");
                }

            } catch (Exception e) {
            }
        }
        utilitario.addUpdate("tab_tabla1");
        return tab_tabla1;
    }

   
    
    @Override
    public void abrirRangoFecha() {
        sec_calendario.dibujar();
    }

    public String retornar_mes_letras(int mes) {
        String mes1 = "";
        if (mes == 1) {
            mes1 = "Enero";
        }
        if (mes == 2) {
            mes1 = "Febrero";
        }
        if (mes == 3) {
            mes1 = "Marzo";
        }
        if (mes == 4) {
            mes1 = "Abril";
        }
        if (mes == 5) {
            mes1 = "Mayo";
        }
        if (mes == 6) {
            mes1 = "Junio";
        }
        if (mes == 7) {
            mes1 = "Julio";
        }
        if (mes == 8) {
            mes1 = "Agosto";
        }
        if (mes == 9) {
            mes1 = "Septiembre";
        }
        if (mes == 10) {
            mes1 = "Octubre";
        }
        if (mes == 11) {
            mes1 = "Noviembre";
        }
        if (mes == 12) {
            mes1 = "Diciembre";
        }
        return mes1;
    }

    public String getFormatoFecha(String fecha) {
        String mes = retornar_mes_letras(utilitario.getMes(fecha));
        String dia = utilitario.getDia(fecha) + "";
        String anio = utilitario.getAnio(fecha) + "";
        String fecha_formato = dia + " de " + mes + " del " + anio;
        return fecha_formato;
    }

    public void cargarTitulo() {
        eti_rango_fechas.setValue("Desde: " + getFormatoFecha(sec_calendario.getFecha1String()) + "    ----     Hasta: " + getFormatoFecha(sec_calendario.getFecha2String()));
        utilitario.addUpdate("gri_titulo");
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

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }
}

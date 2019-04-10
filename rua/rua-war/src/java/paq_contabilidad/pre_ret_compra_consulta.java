package paq_contabilidad;

import framework.componentes.Division;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_contabilidad.ejb.ServicioContabilidad;
import sistema.aplicacion.Pantalla;

public class pre_ret_compra_consulta extends Pantalla {

    private SeleccionCalendario sec_rango_fechas = new SeleccionCalendario();
    private Etiqueta eti_rango_fechas = new Etiqueta("DESDE: 0/0/0       HASTA: 0/0/0 ");
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private Tabla repor_ret_compra = new Tabla();

    @EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

    public pre_ret_compra_consulta() {

        
        repor_ret_compra.setId("repor_ret_compra");
        repor_ret_compra.setSql(ser_contabilidad.getConsultaRetencioneCxC("1900-01-01", "1900-01-01"));
        repor_ret_compra.setHeader("PRUEBA REPORTES");
        repor_ret_compra.setLectura(true);
        repor_ret_compra.setRows(20);
        repor_ret_compra.dibujar();

        PanelTabla pat_repor_ret_compra = new PanelTabla();
        pat_repor_ret_compra.setId("pat_repor_ret_compra");
        pat_repor_ret_compra.setPanelTabla(repor_ret_compra);

        Division div_report_ret_compra = new Division();
        div_report_ret_compra.setId("div_report_ret_compra");
        div_report_ret_compra.dividir1(pat_repor_ret_compra);
        agregarComponente(div_report_ret_compra);
        
        Boton bot_abrir = new Boton();
        bot_abrir.setValue("Seleccionar Fechas");
        bot_abrir.setIcon("ui-calendario");
        bot_abrir.setMetodo("abrirRango");
        bar_botones.agregarBoton(bot_abrir);
        bar_botones.getBot_insertar().setRendered(false);
        bar_botones.getBot_eliminar().setRendered(false);
        bar_botones.getBot_guardar().setRendered(false);

        sec_rango_fechas.setId("sec_rango_fecha");
        sec_rango_fechas.getBot_aceptar().setMetodo("aceptarRango");
        sec_rango_fechas.setFechaActual();
        agregarComponente(sec_rango_fechas);

    }

    public void abrirRango() {

        sec_rango_fechas.dibujar();

    }

    public void aceptarRango() {

        if(sec_rango_fechas.isFechasValidas()){
        
            repor_ret_compra.setSql(ser_contabilidad.getConsultaRetencioneCxC(sec_rango_fechas.getFecha1String(), sec_rango_fechas.getFecha2String()));
            repor_ret_compra.ejecutarSql();
            utilitario.addUpdate("repor_ret_compra");
            sec_rango_fechas.cerrar();
            
        }else {
        
            utilitario.agregarMensajeError("Las fechas selecionadas no son válidas", "");
        }
    }

    public SeleccionCalendario getSec_rango_fechas() {

        return sec_rango_fechas;
    }

    public void setSec_rango_fechas(SeleccionCalendario sec_rango_fechas) {

        this.sec_rango_fechas = sec_rango_fechas;
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

    public Tabla getRepor_ret_compra() {
        return repor_ret_compra;
    }

    public void setRepor_ret_compra(Tabla repor_ret_compra) {
        this.repor_ret_compra = repor_ret_compra;
    }

}

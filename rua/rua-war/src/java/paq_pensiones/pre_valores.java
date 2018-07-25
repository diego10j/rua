/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

/**
 *
 * @author ANDRES REDROBAN
 */

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_valores extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private AutoCompletar autAlumno = new AutoCompletar();
    private SeleccionTabla sel_tab_alumno = new SeleccionTabla();
    String alumno = "";
    
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    public pre_valores(){
        
        autAlumno.setId("autAlumno");
        autAlumno.setAutoCompletar(ser_pensiones.getSqlComboAlumnos());
        autAlumno.setSize(75);
        autAlumno.setAutocompletarContenido(); // no startWith para la busqueda
        autAlumno.setMetodoChange("seleccionarAlumno");
        bar_botones.agregarComponente(new Etiqueta("Buscar Alumno : "));
        bar_botones.agregarComponente(autAlumno);
        
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setTabla("rec_valores", "ide_titulo_recval", 1);
        tab_tabla1.setCondicion("ide_titulo_recval = -1");
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("ide_recalp").setCombo("select ide_recalp, c.nom_geani, b.descripcion_repea, b.fecha_inicial_repea, b.fecha_final_repea\n" +
                                                     "from rec_alumno_periodo a\n" +
                                                     "inner join rec_periodo_academico b on a.ide_repea = b.ide_repea\n" +
                                                     "inner join gen_anio c on b.ide_geani = c.ide_geani\n" +
                                                     "order by fecha_inicial_repea");
        tab_tabla1.getColumna("ide_recalp").setAutoCompletar();
        tab_tabla1.getColumna("ide_cocaj").setCombo(ser_adquisiciones.getTipoCaja());
        tab_tabla1.getColumna("gen_ide_geper").setCombo(ser_pensiones.getSqlComboRepresentantes());
        tab_tabla1.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("gen_ide_geper").setNombreVisual("REPRESENTANTE");
        tab_tabla1.getColumna("ide_concepto_recon").setCombo("rec_concepto", "ide_concepto_recon", "des_concepto_recon", "");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(6);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");   //identificador
        tab_tabla2.setTabla("rec_valor_detalle", "ide_valdet_revad", 1);
        tab_tabla2.dibujar();
        PanelTabla pat_tabla2 = new PanelTabla();
        pat_tabla2.setId("pat_tabla2");
        pat_tabla2.setPanelTabla(tab_tabla2);
            
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir2(pat_tabla1, pat_tabla2,"60%","H");
        agregarComponente(div_tabla1);
        
        sel_tab_alumno.setId("sel_tab_alumno");
        sel_tab_alumno.setTitle("SELECCIONE EL PERIODO ACADÉMICO");
        sel_tab_alumno.setSeleccionTabla(ser_pensiones.getPeriodosEstudiantes("-1"),"");
        sel_tab_alumno.setWidth("80%");
        sel_tab_alumno.setHeight("70%");
        sel_tab_alumno.setRadio();
        //sel_tab_alumno.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
        //sel_tab_alumno.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        sel_tab_alumno.getBot_aceptar().setMetodo("aceptarAlumno");
        agregarComponente(sel_tab_alumno);
        
        Boton bot_fac_elec = new Boton();
        bot_fac_elec.setIcon("ui-icon-search");
        bot_fac_elec.setValue("GENERAR FACTURA");
        bot_fac_elec.setMetodo("generarFactura");
        //bot_fac_elec.setMetodo(ser_pensiones.generarFacturaElectronica("-1"));
        agregarComponente(bot_fac_elec);
        bar_botones.agregarBoton(bot_fac_elec);
    }
    public void generarFactura(){
        String maximo = "";
       // String defecto = 0;
        //ser_pensiones.generarFacturaElectronica(alumno);
        TablaGenerica cod_max = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("pen_tmp_lista_fact", "ide_petlf"));
        maximo = cod_max.getValor("maximo");
        String codig_val = tab_tabla1.getValorSeleccionado();
        TablaGenerica tab_datos_temp = utilitario.consultar(ser_pensiones.selectPenTemp(codig_val));
        for (int i=0;i<tab_datos_temp.getTotalFilas();i++){
            utilitario.getConexion().ejecutarSql("INSERT INTO pen_tmp_lista_fact (ide_petlf, codigo_alumno_petlf, nombre_alumno_petlf, paralelo_petlf, subtotal_petlf, rebaja_petlf, total_petlf, cod_factura_petlf, fecha_petlf, concepto_petlf, representante_petlf, cedula_petlf, periodo_lectivo_petlf, correo_petlf, direccion_petlf, telefono_petlf, cedula_alumno_petlf, ide_sucu, ide_empr)\n" +
                                                 "VALUES ("+maximo+", "+tab_datos_temp.getValor(i, "codigo_alumno")+", '"+tab_datos_temp.getValor(i, "nombres_alumno")+"', '"+tab_datos_temp.getValor(i, "paralelo")+"', "+tab_datos_temp.getValor(i, "total")+", "+null+", "+tab_datos_temp.getValor(i, "total")+", "+tab_datos_temp.getValor(i, "codigo_fac")+", '"+tab_datos_temp.getValor(i, "fecha")+"', '"+tab_datos_temp.getValor(i, "concepto")+"', "
                                                         + "'"+tab_datos_temp.getValor(i, "nom_repre")+"', '"+tab_datos_temp.getValor(i, "cedula_repre")+"', '"+tab_datos_temp.getValor(i, "periodo_academico")+"', '"+tab_datos_temp.getValor(i, "correo_repre")+"', '"+tab_datos_temp.getValor(i, "direccion_repre")+"', '"+tab_datos_temp.getValor(i, "telefono_repre")+"', '"+tab_datos_temp.getValor(i, "cedula_alumno")+"', "+tab_datos_temp.getValor(i, "ide_sucu")+", "+tab_datos_temp.getValor(i, "ide_empr")+")");
        }
        utilitario.getConexion().ejecutarSql(ser_pensiones.generarFacturaElectronica(codig_val));
    }
    
    public void aceptarAlumno(){
        String codig_alumno = sel_tab_alumno.getValorSeleccionado();
        System.out.println("Este es el alumno seleccionado "+codig_alumno);
        TablaGenerica tab_dat_alum = utilitario.consultar("select ide_recalp, c.nom_geani, b.descripcion_repea, b.fecha_inicial_repea, b.fecha_final_repea\n" +
                                                          "from rec_alumno_periodo a\n" +
                                                          "inner join rec_periodo_academico b on a.ide_repea = b.ide_repea\n" +
                                                          "inner join gen_anio c on b.ide_geani = c.ide_geani\n" +
                                                          "where a.ide_recalp = "+codig_alumno+"");
        for (int i=0;i<tab_dat_alum.getTotalFilas();i++){
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_geper", autAlumno.getValor());
            tab_tabla1.setValor("ide_recalp",tab_dat_alum.getValor(i, "ide_recalp"));
          //  System.out.println("periodo al "+tab_dat_alum.getValor(i, "ide_recalp"));
        }
        sel_tab_alumno.cerrar();
	utilitario.addUpdate("tab_tabla1");
        
    }
    
    public void seleccionarAlumno(SelectEvent evt) {
        autAlumno.onSelect(evt);
        alumno = autAlumno.getValor();
        if (autAlumno.getValor() != null) {
          /*  tab_tabla1.setSql(ser_pensiones.getFacturasAlumno(autAlumno.getValorArreglo(1)));
            tab_tabla1.ejecutarSql();*/
          tab_tabla1.setCondicion("ide_geper="+autAlumno.getValor());
          tab_tabla1.ejecutarSql();
          utilitario.addUpdate("tab_tabla1");
        } else {
            tab_tabla1.limpiar();
        }
    }
    
    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()){
        if (autAlumno.getValue() == null){
            utilitario.agregarMensajeError("No se puede continuar", "Debe buscar un alumno");
        }else{
        sel_tab_alumno.getTab_seleccion().setSql(ser_pensiones.getPeriodosEstudiantes(alumno));
        sel_tab_alumno.getTab_seleccion().ejecutarSql();
        sel_tab_alumno.dibujar();
            System.out.println("este es el id elumno" +alumno);
                
        }
    }
        else if (tab_tabla2.isFocus()){
            tab_tabla2.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.isFocus()){
            tab_tabla1.guardar();
        }
        else if (tab_tabla2.isFocus()){
            tab_tabla2.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()){
            tab_tabla1.eliminar();
        }
        else if (tab_tabla2.isFocus()){
            tab_tabla2.eliminar();
        }
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


    public ServicioPensiones getSer_pensiones() {
        return ser_pensiones;
    }

    public void setSer_pensiones(ServicioPensiones ser_pensiones) {
        this.ser_pensiones = ser_pensiones;
    }

    public AutoCompletar getAutAlumno() {
        return autAlumno;
    }

    public void setAutAlumno(AutoCompletar autAlumno) {
        this.autAlumno = autAlumno;
    }

    public SeleccionTabla getSel_tab_alumno() {
        return sel_tab_alumno;
    }

    public void setSel_tab_alumno(SeleccionTabla sel_tab_alumno) {
        this.sel_tab_alumno = sel_tab_alumno;
    }


    
}

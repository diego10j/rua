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
import javax.faces.event.AjaxBehaviorEvent;
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
        tab_tabla1.getColumna("IDE_GTEMP").setCombo("gth_empleado", "ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp,primer_nombre_gtemp", "");
        tab_tabla1.getColumna("IDE_GTEMP").setAutoCompletar();
        tab_tabla1.getColumna("IDE_GTEMP").setNombreVisual("REGISTRADORR");
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setCombo("gth_empleado", "ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp,primer_nombre_gtemp", "");
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setAutoCompletar();
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setNombreVisual("RECAUDADOR");
        tab_tabla1.getColumna("ide_concepto_recon").setCombo("rec_concepto", "ide_concepto_recon", "des_concepto_recon", "");
        tab_tabla1.getColumna("ide_concepto_recon").setRequerida(true);
        tab_tabla2.getColumna("ide_recest").setCombo("rec_estados", "ide_recest", "descripcion_recest", "");
        tab_tabla1.getColumna("TOTAL_RECVA ").setEtiqueta();
        tab_tabla1.getColumna("TOTAL_RECVA ").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:blue");//Estilo
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(6);
        tab_tabla1.dibujar();
        tab_tabla1.agregarRelacion(tab_tabla2);
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");   //identificador
        tab_tabla2.setTabla("rec_valor_detalle", "ide_valdet_revad", 2);
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
        sel_tab_alumno.setSeleccionTabla(ser_pensiones.getPeriodosEstudiantes("2"),"");
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
    
    public void cargaDetalle(AjaxBehaviorEvent evt){
        tab_tabla2.modificar(evt);
        String dat_cmb_concep;
        dat_cmb_concep = tab_tabla1.getValor("IDE_CONCEPTO_RECON");
        if (tab_tabla1.getValor("IDE_CONCEPTO_RECON") != null){
        TablaGenerica tab_concepto = utilitario.consultar("select ide_forma_impuesto_refim, b.ide_impuesto_reimp, b.des_impuesto_reimp, b.valor_reimp\n" +
                                                          "from rec_forma_impuesto a\n" +
                                                          "left join rec_impuesto b on a.ide_impuesto_reimp = b.ide_impuesto_reimp\n" +
                                                          "where a.ide_concepto_recon = "+dat_cmb_concep+"");
         for (int i=0;i<tab_concepto.getTotalFilas();i++){
             tab_tabla2.insertar();
             tab_tabla2.setValor("ide_impuesto_reimp", tab_concepto.getValor(i, "ide_impuesto_reimp"));
             tab_tabla2.setValor("detalle_revad", tab_concepto.getValor(i, "des_impuesto_reimp"));
             tab_tabla2.setValor("precio_revad", tab_concepto.getValor(i, "valor_reimp"));
             tab_tabla2.setValor("cantidad_revad", "1");
             calcular();
             tab_tabla2.guardar();
         }
        }
        else {
            utilitario.agregarMensajeError("No se puede guardar", "Debe seleccionar el Concepto");
        }
    }
    public void calcular() {
        //Variables para almacenar y calcular el total del detalle
        double dou_cantidad = 0;
        double dou_precio = 0;
        double dou_total = 0;
        
        try {
            //Obtenemos el valor de la cantidad
            dou_cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_revad"));
        } catch (Exception e) {
        }

        try {
            //Obtenemos el valor
            dou_precio = Double.parseDouble(tab_tabla2.getValor("precio_revad"));
        } catch (Exception e) {
        }
        //Calculamos el total
        dou_total = dou_cantidad * dou_precio;
        //Asignamos el total a la tabla detalle, con 2 decimales
        tab_tabla2.setValor("total_revad", utilitario.getFormatoNumero(dou_total, 2));
        tab_tabla1.setValor("TOTAL_RECVA",""+tab_tabla2.getSumaColumna("total_revad"));
	tab_tabla1.modificar(tab_tabla1.getFilaActual());
	utilitario.addUpdateTabla(tab_tabla1, "TOTAL_RECVA","tab_tabla1");
        utilitario.addUpdateTabla(tab_tabla2, "total_revad", "tab_tabla2");
        utilitario.addUpdate("tab_tabla1");
        utilitario.addUpdate("tab_tabla2");
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
                                                 "VALUES ("+maximo+", "+tab_datos_temp.getValor(i, "codigo_alumno")+", '"+tab_datos_temp.getValor(i, "nombres_alumno")+"', '"+tab_datos_temp.getValor(i, "paralelo")+"', "+tab_datos_temp.getValor(i, "total_revad")+", "+tab_datos_temp.getValor(i, "valor_descuento_revad")+", "+tab_datos_temp.getValor(i, "total")+", "+tab_datos_temp.getValor(i, "codigo_fac")+", '"+tab_datos_temp.getValor(i, "fecha")+"', '"+tab_datos_temp.getValor(i, "concepto")+"', "
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
          tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
          utilitario.addUpdate("tab_tabla1,tab_tabla2");
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
            if (tab_tabla1.isFilaInsertada()){
            
            tab_tabla1.guardar();
            String dat_cmb_concep;
            dat_cmb_concep = tab_tabla1.getValor("IDE_CONCEPTO_RECON");
             TablaGenerica tab_concepto = utilitario.consultar("select ide_forma_impuesto_refim, b.ide_impuesto_reimp, b.des_impuesto_reimp, b.valor_reimp\n" +
                                                          "from rec_forma_impuesto a\n" +
                                                          "left join rec_impuesto b on a.ide_impuesto_reimp = b.ide_impuesto_reimp\n" +
                                                          "where a.ide_concepto_recon = "+dat_cmb_concep+"");
         for (int i=0;i<tab_concepto.getTotalFilas();i++){
             tab_tabla2.insertar();
             tab_tabla2.setValor("ide_titulo_recval", tab_tabla1.getValor("ide_titulo_recval"));
             tab_tabla2.setValor("ide_impuesto_reimp", tab_concepto.getValor(i, "ide_impuesto_reimp"));
             tab_tabla2.setValor("detalle_revad", tab_concepto.getValor(i, "des_impuesto_reimp"));
             tab_tabla2.setValor("precio_revad", tab_concepto.getValor(i, "valor_reimp"));
             tab_tabla2.setValor("cantidad_revad", "1");
             calcular();
             tab_tabla2.guardar();
          }
         
        }
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

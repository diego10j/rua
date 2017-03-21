/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import paq_gestion.*;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import javax.ejb.EJB;
import paq_nomina.ejb.ServicioNomina;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import java.util.Map;


public class pre_asiento_rua extends Pantalla {

    private Combo com_periodo=new Combo();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private SeleccionTabla sel_tab_tipo_nomina = new SeleccionTabla();
    private SeleccionTabla sel_tab_consuta_descuadre = new SeleccionTabla();
    	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
        private Map p_parametros=new HashMap();
    
    	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

    public pre_asiento_rua() {    
        
                bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
        
        com_periodo.setCombo(ser_nomina.getSqlComboPeriodoRol());
	//com_periodo.setMetodo("cambioPeriodo");		 
	bar_botones.agregarComponente(new Etiqueta("Periodo Rol:"));
	bar_botones.agregarComponente(com_periodo);
        
        Boton bot_generar_asiento=new Boton();
	bot_generar_asiento.setMetodo("generarAsiento");
	bot_generar_asiento.setValue("Generar Asiento Contable");
	bot_generar_asiento.setIcon("ui-icon-mail-closed");
	bar_botones.agregarBoton(bot_generar_asiento);

        Boton bot_cerrar_asiento=new Boton();
	bot_cerrar_asiento.setMetodo("generarAsiento");
	bot_cerrar_asiento.setValue("Cerrar Asiento Contable");
	bot_cerrar_asiento.setIcon("ui-icon-mail-closed");
	bar_botones.agregarBoton(bot_cerrar_asiento);

        Boton bot_transferir_asiento=new Boton();
	bot_transferir_asiento.setMetodo("generarAsiento");
	bot_transferir_asiento.setValue("Transferir Asiento Contable");
	bot_transferir_asiento.setIcon("ui-icon-mail-closed");
	bar_botones.agregarBoton(bot_transferir_asiento);

        Boton bot_consulta_descuadre=new Boton();
	bot_consulta_descuadre.setMetodo("consultaDescuadre");
	bot_consulta_descuadre.setValue("Consultar Rol Descuadrado");
	bot_consulta_descuadre.setIcon("ui-icon-mail-closed");
	bar_botones.agregarBoton(bot_consulta_descuadre);
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("NRH_CABECERA_ASIENTO", "IDE_NRCAA", 1);
        tab_tabla.getColumna("ide_gepro").setCombo(ser_nomina.getSqlComboPeriodoRol());
        tab_tabla.getColumna("ide_gepro").setAutoCompletar();
        tab_tabla.getColumna("ide_gepro").setLectura(true);
        tab_tabla.getColumna("estado_nrcaa").setLectura(true);
        tab_tabla.getColumna("traspaso_nrcaa").setLectura(true);
        tab_tabla.getColumna("ide_asiento_rua").setLectura(true);
        tab_tabla.getColumna("estado_nrcaa").setValorDefecto("false");
        tab_tabla.getColumna("traspaso_nrcaa").setValorDefecto("false");
        tab_tabla.setTipoFormulario(true);
	tab_tabla.getGrid().setColumns(6);
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("NRH_DETALLE_ASIENTO", "IDE_NRDEA", 2);
        tab_tabla2.getColumna("ide_gelua").setCombo("con_lugar_aplicac","ide_cnlap","nombre_cnlap","");
        tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc||' '||nombre_cndpc", "");
        tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla2.setColumnaSuma("DEBE_NRDEA,HABER_NRDEA");
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel2, "30%", "H");
        agregarComponente(div_division);
        
        sel_tab_tipo_nomina.setId("sel_tab_tipo_nomina");
	sel_tab_tipo_nomina.setSeleccionTabla("select " +
				"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
				"TEM.DETALLE_GTTEM," +
				"TIC.DETALLE_GTTCO, " +
				"SUC.NOM_SUCU " +
				"from NRH_ROL ROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"where ROL.IDE_GEPRO=-1","IDE_NRROL");


		//sel_tab_tipo_nomina.setRadio();
		gru_pantalla.getChildren().add(sel_tab_tipo_nomina);
		sel_tab_tipo_nomina.getBot_aceptar().setMetodo("aceptarAsiento");
		agregarComponente(sel_tab_tipo_nomina);
                
               sel_tab_consuta_descuadre.setId("sel_tab_consuta_descuadre");
                sel_tab_consuta_descuadre.setSeleccionTabla("select a.ide_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,debe,haber "
                + "from gth_empleado a,( "
                + "select sum(debe) as debe,sum(haber) as haber, ide_geedp from ( "
                + "select ide_gelua,ide_cndpc, sum(round(debe,2)) as debe,sum(round(haber,2)) as haber, ide_geedp from ( "
                + "select a.ide_gelua,a.ide_cndpc,b.ide_nrrub,b.ide_nrder,d.ide_nrrol,d.ide_gepro, "
                + "round((case when a.ide_gelua = 0 then valor_nrdro else 0 end),2) as haber, "
                + "round((case when a.ide_gelua = 1 then valor_nrdro else 0 end),2) as debe,detalle_nrrub,codig_recur_cndpc,nombre_cndpc,ide_geedp "
                + "from nrh_rubro_asiento a,nrh_detalle_rubro b, nrh_detalle_rol c,nrh_rol d,nrh_rubro e,con_det_plan_cuen f "
                + "where a.ide_nrrub = b.ide_nrrub "
                + "and b.ide_nrder = c.ide_nrder "
                + "and c.ide_nrrol = d.ide_nrrol "
                + "and b.ide_nrrub = e.ide_nrrub "
                + "and a.ide_cndpc = f.ide_cndpc "
                + "and ide_gepro =-1 "
                + ") a "
                + "group by ide_gelua,ide_cndpc, ide_geedp "
                + ")a "
                + "group by ide_geedp "
                + ") b, gen_empleados_departamento_par c "
                + "where a.ide_gtemp = c.ide_gtemp and b.ide_geedp = c.ide_geedp "
                + "and debe != haber "
                + "order by apellido_paterno_gtemp","ide_gtemp");

        //sel_tab_tipo_nomina.setRadio();
        gru_pantalla.getChildren().add(sel_tab_consuta_descuadre);
        sel_tab_consuta_descuadre.getBot_aceptar().setRendered(false);
        agregarComponente(sel_tab_consuta_descuadre);
    }
public void consultaDescuadre(){
    if(com_periodo.getValue()!=null){
    sel_tab_consuta_descuadre.getTab_seleccion().setSql("select a.ide_gtemp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,debe,haber "
                + "from gth_empleado a,( "
                + "select sum(debe) as debe,sum(haber) as haber, ide_geedp from ( "
                + "select ide_gelua,ide_cndpc, sum(round(debe,2)) as debe,sum(round(haber,2)) as haber, ide_geedp from ( "
                + "select a.ide_gelua,a.ide_cndpc,b.ide_nrrub,b.ide_nrder,d.ide_nrrol,d.ide_gepro, "
                + "round((case when a.ide_gelua = 0 then valor_nrdro else 0 end),2) as haber, "
                + "round((case when a.ide_gelua = 1 then valor_nrdro else 0 end),2) as debe,detalle_nrrub,codig_recur_cndpc,nombre_cndpc,ide_geedp "
                + "from nrh_rubro_asiento a,nrh_detalle_rubro b, nrh_detalle_rol c,nrh_rol d,nrh_rubro e,con_det_plan_cuen f "
                + "where a.ide_nrrub = b.ide_nrrub "
                + "and b.ide_nrder = c.ide_nrder "
                + "and c.ide_nrrol = d.ide_nrrol "
                + "and b.ide_nrrub = e.ide_nrrub "
                + "and a.ide_cndpc = f.ide_cndpc "
                + "and ide_gepro = "+com_periodo.getValue().toString()
                + ") a "
                + "group by ide_gelua,ide_cndpc, ide_geedp "
                + ")a "
                + "group by ide_geedp "
                + ") b, gen_empleados_departamento_par c "
                + "where a.ide_gtemp = c.ide_gtemp and b.ide_geedp = c.ide_geedp "
                + "and debe != haber "
                + "order by apellido_paterno_gtemp");
			sel_tab_consuta_descuadre.getTab_seleccion().ejecutarSql();
                        sel_tab_consuta_descuadre.dibujar();
    }else {
                      utilitario.agregarMensajeInfo("Seleccione un periodo", "Seleccione El periodo para Continuar con la consulta");
                      }
                      
}
    @Override
    public void insertar() {
         if(tab_tabla.isFocus()){
          tab_tabla.insertar();
           
         }
         else if (tab_tabla2.isFocus()){
             tab_tabla2.insertar();
         }
    }

    @Override
    public void guardar() {
        if(tab_tabla.isFocus()){
        tab_tabla.guardar();
        guardarPantalla();
           
         }
         else if (tab_tabla2.isFocus()){
        tab_tabla2.guardar();
        guardarPantalla();
         }

    }

    @Override
    public void eliminar() {
        if(tab_tabla.isFocus()){

            utilitario.getConexion().ejecutarSql("delete from NRH_DETALLE_ASIENTO where IDE_NRCAA ="+tab_tabla.getValor("IDE_NRCAA"));
            utilitario.getConexion().ejecutarSql("delete from NRH_CABECERA_ASIENTO where IDE_NRCAA ="+tab_tabla.getValor("IDE_NRCAA"));    
            tab_tabla.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
            utilitario.addUpdate("tab_tabla2");
        }
    }
    public void cerrarAsiento(){
                tab_tabla.setValor("ESTADO_NRCAA", "true");
                                        
                        tab_tabla.modificar(tab_tabla.getFilaActual());
                        tab_tabla.guardar();
                        guardarPantalla();
                        utilitario.addUpdate("tab_tabla");
    }
    public void aceptarAsiento (){
        		String str_seleccionado = sel_tab_tipo_nomina.getSeleccionados();
		if (str_seleccionado!=null){
                        TablaGenerica  tab_resultado = utilitario.consultar(ser_nomina.contabilizaNominaRua(str_seleccionado));
			
                        tab_tabla.setValor("ide_gepro", com_periodo.getValue().toString());
                        for(int i=0;i < tab_resultado.getTotalFilas();i++){
                            tab_tabla2.insertar();
                            tab_tabla2.setValor("ide_gelua", tab_resultado.getValor(i, "ide_gelua"));
                            tab_tabla2.setValor("IDE_CNDPC", tab_resultado.getValor(i, "IDE_CNDPC"));
                            tab_tabla2.setValor("DEBE_NRDEA", tab_resultado.getValor(i, "debe"));
                            tab_tabla2.setValor("HABER_NRDEA", tab_resultado.getValor(i, "haber"));
                            tab_tabla2.setValor("IDE_NRCAA", tab_tabla.getValor("IDE_NRCAA"));

                        }
                        sel_tab_tipo_nomina.cerrar();
                        
                        tab_tabla.modificar(tab_tabla.getFilaActual());

                        tab_tabla2.guardar();
                        tab_tabla.guardar();
                        guardarPantalla();
                        tab_tabla2.ejecutarSql();
                        utilitario.addUpdate("tab_tabla2,tab_tabla");
		}
		else {
			utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
		    }
    }
public void generarAsiento(){		
        if(tab_tabla.getValor("IDE_NRCAA") == null){
            		utilitario.agregarMensajeInfo("Ingresar la cabecera del asiento", "Para generar el asiento contable ingrese una cabecera contable");
			return;
        }
        if(tab_tabla.getValor("ESTADO_NRCAA").equals("true")){
            		utilitario.agregarMensajeInfo("Asiento Cerrado", "Para Editar o Agregar Items, El asiento no se debe encontrar cerrado");
			return;            
        }
		//abre el dialogo de leccion de tipo de nomina
		if(com_periodo.getValue()!=null){
			sel_tab_tipo_nomina.setHeader("Tipos de Nomina");
			sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
					"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
					"TEM.DETALLE_GTTEM," +
					"TIC.DETALLE_GTTCO, " +
					"SUC.NOM_SUCU " +
					"from NRH_ROL ROL " +
					"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
					"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
					"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
					"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
					"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
					"where ROL.IDE_GEPRO="+com_periodo.getValue() +"  AND IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada"));
			sel_tab_tipo_nomina.getTab_seleccion().ejecutarSql();
			if(sel_tab_tipo_nomina.getTab_seleccion().isEmpty()==false){
				sel_tab_tipo_nomina.dibujar();
			}
			else{
				utilitario.agregarMensajeInfo("No tiene nominas cerradas en el periodo seleccionado", "Solo se pueden enviar n�minas con estado cerradas");
			}								
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un periodo", "");
		}
	}
        @Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Plantilla Contable")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				//p_parametros.put("IDE_GETIA",1);
				p_parametros.put("titulo","PLANTILLA CONTABLE");
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();					
                        }
			
		}
                else if (rep_reporte.getReporteSelecionado().equals("Asiento Contable")) {
                    if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				p_parametros.put("ide_nrcaa",Integer.parseInt(tab_tabla.getValor("ide_nrcaa")));
				p_parametros.put("titulo","PLANTILLA CONTABLE");
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sef_reporte.dibujar();					
                        }
                }
	}
    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public SeleccionTabla getSel_tab_tipo_nomina() {
        return sel_tab_tipo_nomina;
    }

    public void setSel_tab_tipo_nomina(SeleccionTabla sel_tab_tipo_nomina) {
        this.sel_tab_tipo_nomina = sel_tab_tipo_nomina;
    }

    public SeleccionTabla getSel_tab_consuta_descuadre() {
        return sel_tab_consuta_descuadre;
    }

    public void setSel_tab_consuta_descuadre(SeleccionTabla sel_tab_consuta_descuadre) {
        this.sel_tab_consuta_descuadre = sel_tab_consuta_descuadre;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_reporte() {
        return sef_reporte;
    }

    public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
        this.sef_reporte = sef_reporte;
    }
    
}

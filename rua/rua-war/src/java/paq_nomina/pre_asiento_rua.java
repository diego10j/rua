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


public class pre_asiento_rua extends Pantalla {

    private Combo com_periodo=new Combo();
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private SeleccionTabla sel_tab_tipo_nomina = new SeleccionTabla();
    
    	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

    public pre_asiento_rua() {    
        
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
    
}

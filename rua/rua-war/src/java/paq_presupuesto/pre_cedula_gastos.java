package paq_presupuesto;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;

public class pre_cedula_gastos extends Pantalla{

	private Tabla tab_balance_inicial=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();
	private Radio rad_imprimir= new Radio();


	
	public static String par_tipo_asiento_inicial;

	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);

	public pre_cedula_gastos() {
		par_tipo_asiento_inicial =utilitario.getVariable("p_tipo_asiento_inicial");

		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));		
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		com_nivel_cuenta_inicial.setCombo(ser_presupuesto.getListaGruposNivelPresupuesto());		
		com_nivel_cuenta_inicial.setValue("1");
		com_nivel_cuenta_inicial.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Nivel de Cuenta Inicial"));
		bar_botones.agregarComponente(com_nivel_cuenta_inicial);
		
		com_nivel_cuenta_final.setCombo(ser_presupuesto.getListaGruposNivelPresupuesto());		
		com_nivel_cuenta_final.setValue("1");
		com_nivel_cuenta_final.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione el Nivel de Cuenta Final"));
		bar_botones.agregarComponente(com_nivel_cuenta_final);

			
		// boton limpiar
				Boton bot_limpiar = new Boton();
				bot_limpiar.setIcon("ui-icon-cancel");
				bot_limpiar.setMetodo("limpiar");
				bar_botones.agregarBoton(bot_limpiar);
		
				Grid gri_formulario = new Grid();
		    	gri_formulario.setColumns(2);
		    	
				List listax = new ArrayList();
			       Object fila1x[] = {
			           "0", "COSOLIDADO"
			       };
			       Object fila2x[] = {
			           "1", "POR PROGRAMAS"
			       };
			       
			       listax.add(fila1x);
			       listax.add(fila2x);
			       rad_imprimir.setId("rad_imprimir");
			       rad_imprimir.setRadio(listax);
			       rad_imprimir.setValue(fila2x);
			    gri_formulario.getChildren().add(rad_imprimir);			
				
		tab_balance_inicial.setId("tab_balance_inicial");  
		tab_balance_inicial.setTabla("pre_cedula_gastos", "ide_prcei", 1);	
		tab_balance_inicial.setHeader("CEDULA PRESUPUESTARIA DE GASTOS");
		tab_balance_inicial.setCondicion("ide_geani=-1");
		tab_balance_inicial.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false", "true,false"));
		tab_balance_inicial.getColumna("ide_geani").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_balance_inicial.getColumna("ide_prcla").setAutoCompletar();
		tab_balance_inicial.getColumna("ide_prcla").setFiltroContenido();
		tab_balance_inicial.getColumna("ide_prcla").setLongitud(200);

		tab_balance_inicial.setRows(20);
		tab_balance_inicial.setLectura(true);
		tab_balance_inicial.dibujar();
		PanelTabla pat_balance_inicial=new PanelTabla();
		pat_balance_inicial.setHeader(gri_formulario);

		pat_balance_inicial.setPanelTabla(tab_balance_inicial);
		Division div1 = new Division();
		div1.dividir1(pat_balance_inicial);
		agregarComponente(div1);
		
		Boton bot_actualizar=new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Recalcular Cedula Presupuestaria");
		bot_actualizar.setMetodo("generarCedula");
		bar_botones.agregarBoton(bot_actualizar);
		
	}
	public void generarCedula(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		String sql="update pre_cedula_gastos"
+" set inicial_prcei=0,"
+" acumulado_prcei=0,"
+" certificado_prcei=0,"
+" comprometido_prcei=0,"
+" devengado_prcei=0,"
+" pagado_prcei=0,"
+" saldo_comprometer_prcei=0,"
+" saldo_devengar_prcei=0,"
+" reforma_prcei=0,"
+" saldo_pagado_prcei=0,"
+" codificado_prcei=0;"
+" update pre_cedula_gastos"
+" set inicial_prcei=valor_inicial_pranu,"
+" certificado_prcei=valor_precomprometido_pranu,"
+" comprometido_prcei=valor_eje_comprometido_pranu,"
+" devengado_prcei=valor_devengado_pranu,"
+" pagado_prcei=pagado_pranu,"
+" reforma_prcei=valor_reformado_pranu,"
+" codificado_prcei=valor_codificado_pranu"
+" from ("
+" select a.ide_prcla,sum(valor_reformado_pranu) as valor_reformado_pranu,sum(valor_inicial_pranu) as valor_inicial_pranu,"
+" sum(valor_codificado_pranu) as valor_codificado_pranu,sum(valor_reformado_h_pranu) as valor_reformado_h_pranu,sum(valor_devengado_pranu) as valor_devengado_pranu,"
+" sum(valor_precomprometido_pranu) as valor_precomprometido_pranu,sum(valor_eje_comprometido_pranu) as valor_eje_comprometido_pranu,"
+" sum(valor_recaudado_pranu) as valor_recaudado_pranu,sum(valor_recaudado_efectivo_pranu) as valor_recaudado_efectivo_pranu ,sum(pagado_pranu) as pagado_pranu"
+" from pre_programa a, pre_anual b"
+" where a.ide_prpro = b.ide_prpro group by a.ide_prcla"
+" ) a where a.ide_prcla = pre_cedula_gastos.ide_prcla;"
+" update pre_cedula_gastos"
+" set inicial_prcei=inicial,"
+" certificado_prcei=certificado,"
+" comprometido_prcei=comprometido,"
+" devengado_prcei=devengado,"
+" pagado_prcei=pagado,"
+" reforma_prcei=reformado,"
+" codificado_prcei=codificado"
+" from ("
+" select pre_ide_prcla,"
+" sum(inicial_prcei) as inicial,sum(reforma_prcei) as reformado, sum(codificado_prcei) as codificado,sum(devengado_prcei) as devengado,sum(certificado_prcei) as certificado,"
+" sum (comprometido_prcei)as comprometido,sum(pagado_prcei) as pagado"
+" from pre_clasificador a, pre_cedula_gastos b "
+" where a.ide_prcla = b.ide_prcla and nivel_prcla = 4"
+" group by pre_ide_prcla"
+" ) a where a.pre_ide_prcla = pre_cedula_gastos.ide_prcla;"
+" update pre_cedula_gastos"
+" set inicial_prcei=inicial,"
+" certificado_prcei=certificado,"
+" comprometido_prcei=comprometido,"
+" devengado_prcei=devengado,"
+" pagado_prcei=pagado,"
+" reforma_prcei=reformado,"
+" codificado_prcei=codificado"
+" from ("
+" select pre_ide_prcla,"
+" sum(inicial_prcei) as inicial,sum(reforma_prcei) as reformado, sum(codificado_prcei) as codificado,sum(devengado_prcei) as devengado,sum(certificado_prcei) as certificado,"
+" sum (comprometido_prcei)as comprometido,sum(pagado_prcei) as pagado"
+" from pre_clasificador a, pre_cedula_gastos b "
+" where a.ide_prcla = b.ide_prcla and nivel_prcla = 3"
+" group by pre_ide_prcla"
+" ) a where a.pre_ide_prcla = pre_cedula_gastos.ide_prcla;"
+" update pre_cedula_gastos"
+" set inicial_prcei=inicial,"
+" certificado_prcei=certificado,"
+" comprometido_prcei=comprometido,"
+" devengado_prcei=devengado,"
+" pagado_prcei=pagado,"
+" reforma_prcei=reformado,"
+" codificado_prcei=codificado"
+" from ("
+" select pre_ide_prcla,"
+" sum(inicial_prcei) as inicial,sum(reforma_prcei) as reformado, sum(codificado_prcei) as codificado,sum(devengado_prcei) as devengado,sum(certificado_prcei) as certificado,"
+" sum (comprometido_prcei)as comprometido,sum(pagado_prcei) as pagado"
+" from pre_clasificador a, pre_cedula_gastos b "
+" where a.ide_prcla = b.ide_prcla and nivel_prcla = 2"
+" group by pre_ide_prcla"
+" ) a where a.pre_ide_prcla = pre_cedula_gastos.ide_prcla;"
+" update pre_cedula_gastos"
+" set saldo_comprometer_prcei=certificado_prcei-comprometido_prcei,"
+" saldo_devengar_prcei=comprometido_prcei-devengado_prcei,"
+" saldo_pagado_prcei=devengado_prcei-pagado_prcei;";
		utilitario.getConexion().ejecutarSql(sql);
		tab_balance_inicial.ejecutarSql();
		utilitario.addUpdate("tab_balance_inicial");
	}
		public void seleccionaElAnio (){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		if(com_nivel_cuenta_inicial.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione el Nivel de Cuenta Inicial", "");
			return;			

		}
		if(com_nivel_cuenta_final.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione el Nivel de Cuenta Final", "");
			return;			

		}
		
			
			tab_balance_inicial.setCondicion("ide_geani="+com_anio.getValue()+"  and ide_prcla in (select ide_prcla from pre_clasificador where nivel_prcla between "+com_nivel_cuenta_inicial.getValue()+" and "+com_nivel_cuenta_final.getValue()+" and tipo_prcla = 0 )");
			tab_balance_inicial.ejecutarSql();
			utilitario.addUpdate("tab_balance_inicial");
		
	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_balance_inicial.limpiar();	
		utilitario.addUpdate("tab_balance_inicial");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_balance_inicial.eliminar();
	}

	public Tabla getTab_balance_inicial() {
		return tab_balance_inicial;
	}

	public void setTab_balance_inicial(Tabla tab_balance_inicial) {
		this.tab_balance_inicial = tab_balance_inicial;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}
	
	public Combo getCom_nivel_cuenta_inicial() {
		return com_nivel_cuenta_inicial;
	}
	public void setCom_nivel_cuenta_inicial(Combo com_nivel_cuenta_inicial) {
		this.com_nivel_cuenta_inicial = com_nivel_cuenta_inicial;
	}
	public Combo getCom_nivel_cuenta_final() {
		return com_nivel_cuenta_final;
	}
	public void setCom_nivel_cuenta_final(Combo com_nivel_cuenta_final) {
		this.com_nivel_cuenta_final = com_nivel_cuenta_final;
	}
	public SeleccionTabla getSet_asiento_contable() {
		return set_asiento_contable;
	}
	public void setSet_asiento_contable(SeleccionTabla set_asiento_contable) {
		this.set_asiento_contable = set_asiento_contable;
	}
	public Confirmar getCon_guardar() {
		return con_guardar;
	}
	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	


}

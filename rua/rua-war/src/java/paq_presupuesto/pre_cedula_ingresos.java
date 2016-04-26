package paq_presupuesto;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;

public class pre_cedula_ingresos extends Pantalla{

	private Tabla tab_balance_inicial=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_asiento_contable = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	private Combo com_nivel_cuenta_inicial = new Combo();
	private Combo com_nivel_cuenta_final = new Combo();
	
	public static String par_tipo_asiento_inicial;
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();

	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);

	public pre_cedula_ingresos() {
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
				
		tab_balance_inicial.setId("tab_balance_inicial");  
		tab_balance_inicial.setTabla("pre_cedula_ingreso", "ide_prcing", 1);	
		tab_balance_inicial.setHeader("CEDULA PRESUPUESTARIA DE INGRESOS");
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
		System.out.println("generamos cedula");
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Selecione un Año", "");
			return;			

		}
		String sql="update pre_cedula_ingreso"
+" set inicial_prcing=0,"
+" reformado_prcing=0,"
+" codificado_prcing=0,"
+" devengado_prcing=0,"
+" cobrado_prcing=0,"
+" saldo_devengar_prcing=0,"
+" saldo_cobrar_prcing=0;"
+" update pre_cedula_ingreso"
+" set inicial_prcing=valor_inicial_pranu,"
+" reformado_prcing=valor_reformado_pranu,"
+" codificado_prcing=valor_codificado_pranu,"
+" devengado_prcing=valor_devengado_pranu,"
+" cobrado_prcing=valor_recaudado_pranu"
+" from("
+" select valor_reformado_pranu,valor_inicial_pranu,valor_codificado_pranu,valor_reformado_h_pranu,valor_devengado_pranu,"
+" valor_precomprometido_pranu,valor_eje_comprometido_pranu,valor_recaudado_pranu,valor_recaudado_efectivo_pranu,ide_prcla"
+" from pre_anual a where ide_geani=7"
+" ) a"
+" where a.ide_prcla = pre_cedula_ingreso.ide_prcla;"
+" update pre_cedula_ingreso"
+" set inicial_prcing=inicial,"
+" reformado_prcing=reformado,"
+" codificado_prcing=codificado,"
+" devengado_prcing=devengado,"
+" cobrado_prcing=cobrado"
+" from("
+" select pre_ide_prcla,"
+" sum(inicial_prcing) as inicial,sum(reformado_prcing) as reformado, sum(codificado_prcing) as codificado,sum(devengado_prcing) as devengado,sum(cobrado_prcing) as cobrado"
+" from pre_clasificador a, pre_cedula_ingreso b "
+" where a.ide_prcla = b.ide_prcla and nivel_prcla = 4"
+" group by pre_ide_prcla"
+" ) a"
+" where a.pre_ide_prcla = pre_cedula_ingreso.ide_prcla;"
+" update pre_cedula_ingreso"
+" set inicial_prcing=inicial,"
+" reformado_prcing=reformado,"
+" codificado_prcing=codificado,"
+" devengado_prcing=devengado,"
+" cobrado_prcing=cobrado"
+" from("
+" select pre_ide_prcla,"
+" sum(inicial_prcing) as inicial,sum(reformado_prcing) as reformado, sum(codificado_prcing) as codificado,sum(devengado_prcing) as devengado,sum(cobrado_prcing) as cobrado"
+" from pre_clasificador a, pre_cedula_ingreso b "
+" where a.ide_prcla = b.ide_prcla and nivel_prcla = 3"
+" group by pre_ide_prcla"
+" ) a"
+" where a.pre_ide_prcla = pre_cedula_ingreso.ide_prcla;"
+" update pre_cedula_ingreso"
+" set inicial_prcing=inicial,"
+" reformado_prcing=reformado,"
+" codificado_prcing=codificado,"
+" devengado_prcing=devengado,"
+" cobrado_prcing=cobrado"
+" from("
+" select pre_ide_prcla,"
+" sum(inicial_prcing) as inicial,sum(reformado_prcing) as reformado, sum(codificado_prcing) as codificado,sum(devengado_prcing) as devengado,sum(cobrado_prcing) as cobrado"
+" from pre_clasificador a, pre_cedula_ingreso b "
+" where a.ide_prcla = b.ide_prcla and nivel_prcla = 2"
+" group by pre_ide_prcla"
+" ) a"
+" where a.pre_ide_prcla = pre_cedula_ingreso.ide_prcla;"
+" update pre_cedula_ingreso"
+" set saldo_devengar_prcing=inicial_prcing-devengado_prcing,"
+" saldo_cobrar_prcing=devengado_prcing-cobrado_prcing;";
		
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
		
			
			tab_balance_inicial.setCondicion("ide_geani="+com_anio.getValue()+"  and ide_prcla in (select ide_prcla from pre_clasificador where nivel_prcla between "+com_nivel_cuenta_inicial.getValue()+" and "+com_nivel_cuenta_final.getValue()+" and tipo_prcla = 1 )");
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

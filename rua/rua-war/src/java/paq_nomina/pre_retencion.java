package paq_nomina;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
public class pre_retencion extends Pantalla{
	private Tabla tab_retencion_judicial = new Tabla();
	private Tabla tab_retencion_cargas_familiares = new Tabla();
	private Tabla tab_retencion_rubros_descuento = new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private Tabulador tab_tabulador=new Tabulador();
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	public pre_retencion() {
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true");
		aut_empleado.setMetodoChange("filtrarRetencionesJudicialesEmpleados");
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarComponente(bot_limpiar);
		tab_retencion_judicial.setId("tab_retencion_judicial");
		tab_retencion_judicial.setTipoFormulario(true);
		tab_retencion_judicial.getGrid().setColumns(4);
		tab_retencion_judicial.setTabla("NRH_RETENCION_JUDICIAL","IDE_NRREJ", 1);
		tab_retencion_judicial.setCondicion("IDE_GTEMP=-1");
		tab_retencion_judicial.getColumna("IDE_NRTRE").setCombo("NRH_TIPO_RETENCION","IDE_NRTRE","DETALLE_NRTRE", "");
		tab_retencion_judicial.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION","IDE_GEINS","DETALLE_GEINS", "");
		tab_retencion_judicial.getColumna("IDE_GTTPR").setCombo("GTH_TIPO_PARENTESCO_RELACION","IDE_GTTPR","DETALLE_GTTPR", "");
		tab_retencion_judicial.getColumna("IDE_GEBEN").setCombo("GEN_BENEFICIARIO","IDE_GEBEN","TITULAR_GEBEN", "");
		tab_retencion_judicial.getColumna("IDE_GEEDP").setVisible(false);
		tab_retencion_judicial.getColumna("IDE_GTEMP").setVisible(false);
		tab_retencion_judicial.getColumna("IDE_GEBEN").setAutoCompletar();
		tab_retencion_judicial.getColumna("ACTIVO_NRREJ").setCheck();
		tab_retencion_judicial.getColumna("ACTIVO_NRREJ").setValorDefecto("true");
		tab_retencion_judicial.agregarRelacion(tab_retencion_cargas_familiares);
		tab_retencion_judicial.agregarRelacion(tab_retencion_rubros_descuento);
		tab_retencion_judicial.dibujar();
		PanelTabla pan_panel = new PanelTabla();
		pan_panel.setPanelTabla(tab_retencion_judicial);
		tab_retencion_cargas_familiares.setId("tab_retencion_cargas_familiares");
		tab_retencion_cargas_familiares.setIdCompleto("tab_tabulador:tab_retencion_cargas_familiares");
		tab_retencion_cargas_familiares.setTabla("NRH_RETENCION_CARGA_FAMILIA","IDE_NRRCF", 2);
		tab_retencion_cargas_familiares.getColumna("IDE_GTCAF").setCombo("SELECT IDE_GTCAF, " +
				"APELLIDO_PATERNO_GTCAF || '  ' || " +
				"APELLIDO_MATERNO_GTCAF || '  ' || " +
				"PRIMER_NOMBRE_GTCAF || '  ' || " +
				"SEGUNDO_NOMBRE_GTCAF AS NOMBRES , " +
				"DOCUMENTO_IDENTIDAD_GTCAF " +
				"FROM GTH_CARGAS_FAMILIARES where IDE_GTEMP=-1");
		tab_retencion_cargas_familiares.getColumna("ACTIVO_NRRCF").setCheck();
		tab_retencion_cargas_familiares.getColumna("ACTIVO_NRRCF").setValorDefecto("true");
		tab_retencion_cargas_familiares.setCondicion("IDE_NRRCF=-1");
		tab_retencion_cargas_familiares.dibujar();
		PanelTabla pan_panel1 = new PanelTabla();
		pan_panel1.setPanelTabla(tab_retencion_cargas_familiares);
		tab_retencion_rubros_descuento.setId("tab_retencion_rubros_descuento");
		tab_retencion_rubros_descuento.setIdCompleto("tab_tabulador:tab_retencion_rubros_descuento");
		tab_retencion_rubros_descuento.setTabla("NRH_RETENCION_RUBRO_DESCUENTO","IDE_NRRRD", 3);
		tab_retencion_rubros_descuento.getColumna("IDE_NRRUB").setCombo("NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB","ANTICIPO_NRRUB=TRUE");
		tab_retencion_rubros_descuento.getColumna("IDE_NRRUB").setMetodoChange("seleccionaRubroDetallePago");
		tab_retencion_rubros_descuento.getColumna("PORCENTAJE_NRRRD").setMetodoChange("calculaValorDescuento");
		tab_retencion_rubros_descuento.getColumna("ACTIVO_NRRRD").setCheck();
		tab_retencion_rubros_descuento.getColumna("ACTIVO_NRRRD").setValorDefecto("true");
		tab_retencion_rubros_descuento.setCondicion("IDE_NRRRD=-1");
		tab_retencion_rubros_descuento.dibujar();
		PanelTabla pan_panel2 = new PanelTabla();
		pan_panel2.setPanelTabla(tab_retencion_rubros_descuento);
		tab_tabulador.setId("tab_tabulador");
		tab_tabulador.agregarTab("Cargas Familiares", pan_panel1);
		tab_tabulador.agregarTab("Rubro Descuento",pan_panel2);
		Division div_division = new Division();
		div_division.dividir2(pan_panel, tab_tabulador,"50%", "H");
		agregarComponente(div_division);
	}

	public void calculaValorDescuento(AjaxBehaviorEvent evt){
		tab_retencion_rubros_descuento.modificar(evt);
		double porcentaje_descuento=0;
		double valor_rubro =0;
		double resultado_descuento=0;
		try {
			porcentaje_descuento=Double.parseDouble(tab_retencion_rubros_descuento.getValor("PORCENTAJE_NRRRD"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			valor_rubro=Double.parseDouble(tab_retencion_rubros_descuento.getValor("VALOR_RUBRO_NRRRD"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			resultado_descuento=(porcentaje_descuento*valor_rubro)/100;
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("valor rubro"+valor_rubro);
		System.out.println("porcentaje"+porcentaje_descuento);
		System.out.println("resultado"+resultado_descuento);
		tab_retencion_rubros_descuento.setValor("VALOR_DESCUENTO_NRRRD",""+utilitario.getFormatoNumero(resultado_descuento,2));
		utilitario.addUpdateTabla(tab_retencion_rubros_descuento,"VALOR_DESCUENTO_NRRRD", "");

	}
	public Tabla getTab_retencion_rubros_descuento() {
		return tab_retencion_rubros_descuento;
	}
	public void setTab_retencion_rubros_descuento(
			Tabla tab_retencion_rubros_descuento) {
		this.tab_retencion_rubros_descuento = tab_retencion_rubros_descuento;
	}
	public Tabla getTab_retencion_cargas_familiares() {
		return tab_retencion_cargas_familiares;
	}
	public void setTab_retencion_cargas_familiares(
			Tabla tab_retencion_cargas_familiares) {
		this.tab_retencion_cargas_familiares = tab_retencion_cargas_familiares;
	}
	public void filtrarRetencionesJudicialesEmpleados(SelectEvent evt){
		aut_empleado.onSelect(evt);
		System.out.println("empelado "+aut_empleado.getValor());
		String ide_gtemp=ser_nomina.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GTEMP");
		tab_retencion_judicial.setCondicion("IDE_GTEMP="+ide_gtemp);
		tab_retencion_judicial.ejecutarSql();
		tab_retencion_cargas_familiares.setCondicion("IDE_NRREJ="+tab_retencion_judicial.getValor("IDE_NRREJ"));
		tab_retencion_cargas_familiares.ejecutarSql();
		tab_retencion_rubros_descuento.setCondicion("IDE_NRREJ="+tab_retencion_judicial.getValor("IDE_NRREJ"));
		tab_retencion_rubros_descuento.ejecutarSql();
	}

	public void limpiar() {
		tab_retencion_judicial.limpiar();
		tab_retencion_cargas_familiares.limpiar();
		tab_retencion_rubros_descuento.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado,tab_retencion_judicial,tab_retencion_cargas_familiares,tab_retencion_rubros_descuento");
	}
	public Tabla getTab_retencion_judicial() {
		return tab_retencion_judicial;
	}

	public void setTab_retencion_judicial(Tabla tab_retencion_judicial) {
		this.tab_retencion_judicial = tab_retencion_judicial;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}
	@Override
	public void insertar() {
		if(tab_retencion_judicial.isFocus()){
			if (aut_empleado.getValor()!=null){
				tab_retencion_judicial.insertar();
				String ide_gtemp=ser_nomina.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GTEMP");
				tab_retencion_judicial.setValor("IDE_GTEMP", ide_gtemp);
				tab_retencion_judicial.setValor("IDE_GEEDP", aut_empleado.getValor());
				System.out.println("aha"+ide_gtemp);
			}else
			{
				utilitario.agregarMensajeInfo("DEBE SELECCIONAR UN EMPLEADO", "");
			}
		}
		if(tab_retencion_cargas_familiares.isFocus()){
			if (aut_empleado.getValor()!=null){
				String ide_gtemp=ser_nomina.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GTEMP");
				tab_retencion_cargas_familiares.getColumna("IDE_GTCAF").setCombo("SELECT IDE_GTCAF, " +
						"APELLIDO_PATERNO_GTCAF || '  ' || " +
						"APELLIDO_MATERNO_GTCAF || '  ' || " +
						"PRIMER_NOMBRE_GTCAF || '  ' || " +
						"SEGUNDO_NOMBRE_GTCAF AS NOMBRES , " +
						"DOCUMENTO_IDENTIDAD_GTCAF " +
						"FROM GTH_CARGAS_FAMILIARES where IDE_GTEMP="+ide_gtemp+"");
				System.out.print("nsdbd"+ide_gtemp);
				tab_retencion_cargas_familiares.insertar();
			}
			else
			{
				utilitario.agregarMensajeInfo("DEBE SELECCIONAR UN EMPLEADO", "");
			}
		}
		if(tab_retencion_rubros_descuento.isFocus()){
			if (aut_empleado.getValor()!=null){

				tab_retencion_rubros_descuento.insertar();
			}
			else
			{
				utilitario.agregarMensajeInfo("DEBE SELECCIONAR UN EMPLEADO", "");
			}
		}
	}
	public boolean validarRentencionJudicial(){
		if (tab_retencion_judicial.getValor("IDE_NRTRE")==null  || tab_retencion_judicial.getValor("IDE_NRTRE").isEmpty()) {

			utilitario.agregarMensajeInfo("Debe ingresar un tipo de retencion", "");
			return false;
		}
		if (tab_retencion_judicial.getValor("IDE_GEINS")==null  || tab_retencion_judicial.getValor("IDE_GEINS").isEmpty()) {
			utilitario.agregarMensajeInfo("Debe ingresar una Institucion", "");
			return false;
		}

		return true;
	}

	@Override
	public void guardar() {
		if(tab_retencion_judicial.guardar()){
			if (aut_empleado.getValor()!=null){
				guardarPantalla();
			}

		}
		if(tab_retencion_cargas_familiares.guardar()){
			if (aut_empleado.getValor()!=null){
				guardarPantalla();
			}
		}

		if (aut_empleado.getValor()!=null){
			guardarPantalla();
		}
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}
	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}


}

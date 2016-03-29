package paq_nomina;

import javax.ejb.EJB;

import org.primefaces.event.SelectEvent;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;

public class pre_consulta_valor_rubro extends Pantalla{

	@EJB

	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	private AutoCompletar aut_empleado = new AutoCompletar();
	private Tabla tab_consulta=new Tabla();
	private Combo com_rubros = new Combo();

	private SeleccionCalendario sec_rango=new SeleccionCalendario();
	private Check che_todos_emp=new Check();

	public pre_consulta_valor_rubro(){
		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		aut_empleado.setMetodoChange("cambiaEmpleado");


		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");


		Etiqueta eti_colaborador=new Etiqueta("Empleado:");


		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);


		che_todos_emp.setId("che_todos_emp");
		che_todos_emp.setMetodoChange("aplicarTodosEmpleados");
		
		Etiqueta eti_todos_emp=new Etiqueta("Todos");
		bar_botones.agregarComponente(eti_todos_emp);
		bar_botones.agregarComponente(che_todos_emp);



		// autocompletar empleado
		com_rubros.setId("com_rubros");
		com_rubros.setCombo("select ide_nrrub,detalle_nrrub from nrh_rubro order by detalle_nrrub");
		com_rubros.setMetodo("cambiaRubro");

		Etiqueta eti_rubro=new Etiqueta("Rubro:");
		bar_botones.agregarComponente(eti_rubro);
		bar_botones.agregarComponente(com_rubros);

		// boton consultar
		Boton bot_consultar = new Boton();
		bot_consultar.setIcon("ui-icon-calculator");
		bot_consultar.setMetodo("consultar");
		bot_consultar.setValue("Consultar");
		bot_consultar.setTitle("Consultar");
		bar_botones.agregarBoton(bot_consultar);

		tab_consulta.setId("tab_consulta");
		tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo("-1", "-1", utilitario.getFechaActual(), utilitario.getFechaActual()));
		tab_consulta.setCampoPrimaria("ide_nrrol");
		tab_consulta.setColumnaSuma("valor");
		tab_consulta.setLectura(true);
		tab_consulta.setNumeroTabla(1);
		tab_consulta.dibujar();

		PanelTabla pat_tab=new PanelTabla();
		pat_tab.setPanelTabla(tab_consulta);

		Division div1=new Division();
		div1.dividir1(pat_tab);


		agregarComponente(div1);

		sec_rango.setId("sec_rango");
		sec_rango.setTitle("Seleccione un Rango de Fechas");

		agregarComponente(sec_rango);

	}

	public void aplicarTodosEmpleados(){
		System.out.println("che "+che_todos_emp.getValue());
		
		if (che_todos_emp.getValue()!=null && !che_todos_emp.getValue().toString().isEmpty()
				&& che_todos_emp.getValue().toString().equalsIgnoreCase("true")){
			aut_empleado.setValue(null);
			utilitario.addUpdate("aut_empleado");
			if (!str_fecha1.isEmpty() && !str_fecha2.isEmpty()){
			tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo(com_rubros.getValue()+"",str_fecha1,str_fecha2 ));		
			tab_consulta.ejecutarSql();
			tab_consulta.sumarColumnas();
		
			}else{
			tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo("-1", "-1", utilitario.getFechaActual(), utilitario.getFechaActual()));
			tab_consulta.ejecutarSql();
			tab_consulta.sumarColumnas();
			}
		}else{
			if (aut_empleado.getValue()!=null){
				if (!str_fecha1.isEmpty() && !str_fecha2.isEmpty()){
					String ide_geedp=ser_gestion.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GEEDP");
					tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo(ide_geedp, com_rubros.getValue()+"",str_fecha1,str_fecha2 ));
					tab_consulta.ejecutarSql();
					tab_consulta.sumarColumnas();
					utilitario.addUpdate("tab_consulta,che_todos_emp");
					
					if (tab_consulta.getTotalFilas()==0){
						utilitario.agregarMensajeInfo("No existen transacciones registradas ", "");
					}
					return;
				}
			}
				tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo("-1", "-1", utilitario.getFechaActual(), utilitario.getFechaActual()));
				tab_consulta.ejecutarSql();
				tab_consulta.sumarColumnas();
			
		}
	}
	public void cambiaEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		che_todos_emp.setValue(null);
		utilitario.addUpdate("che_todos_emp");
		if (com_rubros.getValue()!=null){
			if (!str_fecha1.isEmpty() && !str_fecha2.isEmpty()){
				String ide_geedp=ser_gestion.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GEEDP");
				tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo(ide_geedp, com_rubros.getValue()+"",str_fecha1,str_fecha2 ));
				tab_consulta.ejecutarSql();
				tab_consulta.sumarColumnas();
				utilitario.addUpdate("tab_consulta,che_todos_emp");
				
				if (tab_consulta.getTotalFilas()==0){
					utilitario.agregarMensajeInfo("No existen transacciones registradas ", "");
				}
			}
		}
	}

	public void cambiaRubro(){
		if (che_todos_emp.getValue()==null|| che_todos_emp.getValue().toString().isEmpty() 
				|| che_todos_emp.getValue().toString().equalsIgnoreCase("false")){
			if (aut_empleado.getValue()!=null){
				if (!str_fecha1.isEmpty() && !str_fecha2.isEmpty()){
					String ide_geedp=ser_gestion.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GEEDP");
					tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo(ide_geedp, com_rubros.getValue()+"",str_fecha1,str_fecha2 ));
					tab_consulta.ejecutarSql();
					tab_consulta.sumarColumnas();
				}
			}
		}else{
			tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo(com_rubros.getValue()+"",str_fecha1,str_fecha2 ));		
			tab_consulta.ejecutarSql();
			tab_consulta.sumarColumnas();
			
		}
		
       

	}

	public void limpiar(){
		tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo("-1", "-1", utilitario.getFechaActual(), utilitario.getFechaActual()));
		tab_consulta.ejecutarSql();
		tab_consulta.sumarColumnas();
		aut_empleado.setValue(null);
		com_rubros.setValue(null);
		che_todos_emp.setValue(null);
		utilitario.addUpdate("aut_empleado,com_rubros,che_todos_emp");
	}

	String str_fecha1="";
	String str_fecha2="";
	
	public void consultar(){
		if (che_todos_emp.getValue()==null){
			if (aut_empleado.getValor()==null){
				utilitario.agregarMensajeInfo("No se puede consultar", "Debe seleccionar un empleado");
				return;
			}
		}
		if (com_rubros.getValue()==null ){
			utilitario.agregarMensajeInfo("No se puede consultar", "Debe seleccionar un rubro");
			return;
		}


		if (!sec_rango.isVisible()){
			sec_rango.setFecha1(null);
			sec_rango.setFecha2(null);
			str_fecha1="";
			str_fecha2="";
			sec_rango.getBot_aceptar().setMetodo("consultar");
			sec_rango.dibujar();
			utilitario.addUpdate("sec_rango");
		}else{

			if (!sec_rango.isFechasValidas()){
				str_fecha1="";
				str_fecha2="";
				utilitario.agregarMensajeInfo("No se puede consultar", "fechas incorrectas");
				return;
			}



			System.out.println("che "+che_todos_emp.getValue());

			str_fecha1=sec_rango.getFecha1String();
			str_fecha2=sec_rango.getFecha2String();
			if (che_todos_emp.getValue()==null || che_todos_emp.getValue().toString().isEmpty() 
					|| che_todos_emp.getValue().toString().equalsIgnoreCase("false")){
				String ide_geedp=ser_gestion.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GEEDP");
				tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo(ide_geedp, com_rubros.getValue()+"",str_fecha1,str_fecha2 ));	
			}else{
				tab_consulta.setSql(ser_nomina.getSqlConsultaValorRubroPeriodo(com_rubros.getValue()+"",str_fecha1,str_fecha2 ));
			}
			tab_consulta.ejecutarSql();
			tab_consulta.sumarColumnas();
			utilitario.addUpdate("tab_consulta");


			sec_rango.cerrar();
			
			if (tab_consulta.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen transacciones registradas ", "");
			}
		}
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub

	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Tabla getTab_consulta() {
		return tab_consulta;
	}

	public void setTab_consulta(Tabla tab_consulta) {
		this.tab_consulta = tab_consulta;
	}


	public SeleccionCalendario getSec_rango() {
		return sec_rango;
	}


	public void setSec_rango(SeleccionCalendario sec_rango) {
		this.sec_rango = sec_rango;
	}



}

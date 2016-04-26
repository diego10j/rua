package paq_presupuesto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_presupuesto.pre_contratacion;
public class pre_poa_reforma_fuente extends Pantalla {
	
	private Tabla tab_poa_reforma_fuenta =new Tabla();
	private SeleccionTabla set_reforma_fuente = new SeleccionTabla();
	private Combo com_anio=new Combo();
	private Combo com_fuente_financiamiento=new Combo();
	private Etiqueta eti_valor_total = new Etiqueta();
	private Texto txt_num_oficio = new Texto();
	private Dialogo dia_datos_reforma=new Dialogo();
	private Texto txt_num_resolucion_guarda=new Texto();
	private Calendario cal_fecha_reforma=new Calendario();
	private Dialogo dia_busca_resolucion = new Dialogo();
	private Radio rad_imprimir= new Radio();
	private Check che_aprobado=new Check();
	//private pre_contratacion trig_reformas = new pre_contratacion();



	@EJB
	 private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	  @EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_poa_reforma_fuente() {
		
		bar_botones.getBot_insertar().setRendered(false);
		bar_botones.getBot_eliminar().setRendered(false);
		bar_botones.getBot_atras().setRendered(false);
		bar_botones.getBot_fin().setRendered(false);
		bar_botones.getBot_inicio().setRendered(false);
		bar_botones.getBot_siguiente().setRendered(false);
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		
		
		com_fuente_financiamiento.setId("com_fuente_financiamiento");
		com_fuente_financiamiento.setCombo("select ide_prfuf,detalle_prfuf from  pre_fuente_financiamiento order by detalle_prfuf");
		bar_botones.agregarComponente(new Etiqueta("Fuente Financiamiento"));
		bar_botones.agregarComponente(com_fuente_financiamiento);
		
		Boton bot_buscar = new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("mostrarSetReformaFuente");
		bar_botones.agregarBoton(bot_buscar); 
		
		List lista = new ArrayList();
	       Object fila1[] = {
	           "0", "Reforma Techos"
	       };
	       Object fila2[] = {
	           "1", "Reforma por Partida"
	       };
	       
	       lista.add(fila1);
	       lista.add(fila2);
	       rad_imprimir.setId("rad_imprimir");
	       rad_imprimir.setRadio(lista);
	       rad_imprimir.setValue(fila2);
	       rad_imprimir.setMetodoChange("seleccionaOpcion");
	       rad_imprimir.setVertical();
	  //     bar_botones.agregarComponente(rad_imprimir);
		
		Boton bot_buscar_resol = new Boton();
		bot_buscar_resol.setIcon("ui-icon-person");
		bot_buscar_resol.setValue("Buscar Reformada");
		bot_buscar_resol.setMetodo("mostrarDialogoBusca");
	//	bar_botones.agregarBoton(bot_buscar_resol);
		
	
		Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(4);
    	
    	gri_formulario.getChildren().add(rad_imprimir);
    	gri_formulario.getChildren().add(bot_buscar_resol);
    	
    	Etiqueta eti_total = new Etiqueta("TOTAL REFORMADO:");
    	eti_total.setStyle("font-size: 15px;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_total);
       	
       	eti_valor_total.setId("eti_valor_total");
       	eti_valor_total.setValue("0.00");
    	eti_valor_total.setStyle("font-size: 17px;color: red;font-weight: bold;text-aling:left");
       	gri_formulario.getChildren().add(eti_valor_total);

       	//Grid para el dialogo
       	Grid gri_guarda_reforma = new Grid();
       	gri_guarda_reforma.setColumns(2);
       	
       	Etiqueta eti_resolcion_guarda = new Etiqueta("Nro. Resolución: ");
       	gri_guarda_reforma.getChildren().add(eti_resolcion_guarda);
       	txt_num_resolucion_guarda.setId("txt_num_resolucion_guarda");
       	txt_num_resolucion_guarda.setSize(15);
       	gri_guarda_reforma.getChildren().add(txt_num_resolucion_guarda);
       	
       	Etiqueta eti_fecha_guarda = new Etiqueta("Fecha Reforma");
       	gri_guarda_reforma.getChildren().add(eti_fecha_guarda);
       	cal_fecha_reforma.setId("cal_fecha_reforma");
       	cal_fecha_reforma.setFechaActual();
       	gri_guarda_reforma.getChildren().add(cal_fecha_reforma);
       	
       	Etiqueta eti_che_aprobado = new Etiqueta("Aprobado/No Aprobado");
       	gri_guarda_reforma.getChildren().add(eti_che_aprobado);
       	//che_aprobado.setValue("true");
       	
       	gri_guarda_reforma.getChildren().add(che_aprobado);
       	
       	dia_datos_reforma.setId("dia_datos_reforma");
       	dia_datos_reforma.setTitle("Ingrese los Siguientes Datos para Realizar la Reforma");
       	dia_datos_reforma.setWidth("25%");
       	dia_datos_reforma.setHeight("20%");
       	dia_datos_reforma.setDialogo(gri_guarda_reforma);
       	dia_datos_reforma.getBot_aceptar().setMetodo("guardar");
       	
       	// Dialogo para buscar reformas
       	Grid gri_busca_reforma = new Grid();
       	gri_busca_reforma.setColumns(2);
       	
       	Etiqueta eti_resolcion_busca = new Etiqueta("Nro. Resolución: ");
       	gri_busca_reforma.getChildren().add(eti_resolcion_busca);
       	txt_num_oficio.setId("txt_num_oficio");
       	txt_num_oficio.setSize(15);
       	gri_busca_reforma.getChildren().add(txt_num_oficio);
       	
       	dia_busca_resolucion.setId("dia_busca_resolucion");
       	dia_busca_resolucion.setTitle("Ingrese Nro. Resolución Para Buscar la Reforma");
       	dia_busca_resolucion.setWidth("25%");
       	dia_busca_resolucion.setHeight("20%");
       	dia_busca_resolucion.setDialogo(gri_busca_reforma);
       	dia_busca_resolucion.getBot_aceptar().setMetodo("buscaResolucion");
       	
       	agregarComponente(dia_busca_resolucion);
       	
   	
		tab_poa_reforma_fuenta.setId("tab_poa_reforma_fuenta");
		tab_poa_reforma_fuenta.setTabla("pre_poa_reforma_fuente", "ide_prprf", 1);
		tab_poa_reforma_fuenta.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaNombre("select ide_geani from gen_anio"));
		tab_poa_reforma_fuenta.setCondicion("ide_prprf=-1");
		tab_poa_reforma_fuenta.getColumna("ide_prpoa").setAutoCompletar();
		tab_poa_reforma_fuenta.getColumna("ide_prpoa").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setAutoCompletar();
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("saldo_actual_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("activo_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("resolucion_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("fecha_prprf").setLectura(true);
		tab_poa_reforma_fuenta.getColumna("aprobado_prprf").setLectura(true);
		
		tab_poa_reforma_fuenta.getColumna("valor_reformado_prprf").setMetodoChange("calcularReforma");
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setAncho(50);
		tab_poa_reforma_fuenta.getColumna("saldo_actual_prprf").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_poa_reforma_fuenta.getColumna("ide_prprf").setOrden(1);
		tab_poa_reforma_fuenta.getColumna("ide_prfuf").setOrden(2);
		tab_poa_reforma_fuenta.getColumna("saldo_actual_prprf").setOrden(3);


		tab_poa_reforma_fuenta.dibujar();
		PanelTabla pat_reforma = new PanelTabla();
		pat_reforma.setHeader(gri_formulario);
		pat_reforma.setPanelTabla(tab_poa_reforma_fuenta);
		
		Division div_poa_reforma = new Division();
		div_poa_reforma.setId("div_poa_reforma");
		div_poa_reforma.dividir1(pat_reforma);
		
		agregarComponente(div_poa_reforma);
		
		agregarComponente(dia_datos_reforma);

		
		inicializaSetPoaReformar();
		
	}
	public void inicializaSetPoaReformar(){
		set_reforma_fuente.setId("set_reforma_fuente");
		set_reforma_fuente.setTitle("Seleccione una Fuente de Financiamiento para la Reforma");
		set_reforma_fuente.setSeleccionTabla(ser_presupuesto.getPoaSaldosFuenteFinanciamiento("-1","-1","0","1"),"codigo");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setNombreVisual("Fuente Financiamiento");
		set_reforma_fuente.getTab_seleccion().getColumna("valor_asignado").setNombreVisual("Asignación Inicial F.F.");
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_clasificador_prcla").setNombreVisual("Partida Presupuestaria");
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setNombreVisual("Codigo Sub-Actividad");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_subactividad").setNombreVisual("Sub-Actividad");
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setNombreVisual("Nro. Resolución");
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_proyecto").setNombreVisual("PROYECTO");

		set_reforma_fuente.getTab_seleccion().getColumna("detalle_prfuf").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("valor_asignado").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("codigo_subactividad").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_subactividad").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("num_resolucion_prpoa").setFiltro(true);
		set_reforma_fuente.getTab_seleccion().getColumna("detalle_proyecto").setFiltro(true);

		set_reforma_fuente.getTab_seleccion().getColumna("ide_prpoa").setVisible(false);
		set_reforma_fuente.getTab_seleccion().getColumna("ide_prfuf").setVisible(false);
		//set_reforma_fuente.setRadio();
		set_reforma_fuente.getTab_seleccion().ejecutarSql();
		set_reforma_fuente.getBot_aceptar().setMetodo("aceptarPoa");

		agregarComponente(set_reforma_fuente);

	}
	public void calcularReforma(AjaxBehaviorEvent evt) {
		//System.out.println(" entre actualizar ");
		tab_poa_reforma_fuenta.modificar(evt); //Siempre es la primera linea
		double dou_valor_reforma=0;
		double dou_valor_saldo=0;
		double dou_valor_reforma_individual=0;
		double dou_valor_certificado=0;
		double total_ejecutado=0;
		String mensaje="0";
		
		
		
		for (int i = 0; i < tab_poa_reforma_fuenta.getTotalFilas(); i++) {
			TablaGenerica tab_poa_certificado= utilitario.consultar("select ide_prpfe,valor_certificado_prpfe from pre_poa_fuente_ejecucion where ide_prfuf = "+tab_poa_reforma_fuenta.getValor(i,"ide_prfuf")+" and ide_prpoa="+tab_poa_reforma_fuenta.getValor(i,"ide_prpoa"));
			if(tab_poa_certificado.getTotalFilas()>0){
				dou_valor_certificado=Double.parseDouble(tab_poa_certificado.getValor("valor_certificado_prpfe"));
				
				BigDecimal big_valor_certificado=new BigDecimal(dou_valor_certificado);
				big_valor_certificado=big_valor_certificado.setScale(2, RoundingMode.HALF_UP);
				
				dou_valor_certificado=Double.parseDouble(big_valor_certificado+"");
				
			}
			//Obtenemos el valor del saldo
			try {
				dou_valor_saldo =Double.parseDouble(tab_poa_reforma_fuenta.getValor(i,"saldo_actual_prprf"));
				
				BigDecimal big_valor_saldo=new BigDecimal(dou_valor_saldo);
				big_valor_saldo=big_valor_saldo.setScale(2, RoundingMode.HALF_UP);
				
				dou_valor_saldo=Double.parseDouble(big_valor_saldo+"");
				
			} catch (Exception e) {
				System.out.println("Error ingreso valor saldo_actual_prprf "+e);
			}
			
			try {
				//Obtenemos el valor de la cantidad
				dou_valor_reforma +=Double.parseDouble(tab_poa_reforma_fuenta.getValor(i,"valor_reformado_prprf"));
				
				
				BigDecimal big_valor_reforma=new BigDecimal(dou_valor_reforma);
				big_valor_reforma=big_valor_reforma.setScale(2, RoundingMode.HALF_UP);
				
				dou_valor_reforma=Double.parseDouble(big_valor_reforma+"");
				
			} catch (Exception e) {
				System.out.println("Error ingreso valor reformado "+e);
			}
		
			total_ejecutado =dou_valor_saldo-dou_valor_certificado;
			BigDecimal big_total_ejecutado=new BigDecimal(total_ejecutado);
			big_total_ejecutado=big_total_ejecutado.setScale(2, RoundingMode.HALF_UP);
			
			total_ejecutado=Double.parseDouble(big_total_ejecutado+"");
			//System.out.println("valores ejcutados "+total_ejecutado+" dou_valor_saldo "+dou_valor_saldo+" dou_valor_certificado "+dou_valor_certificado);
			double valor_ingresado =Double.parseDouble(tab_poa_reforma_fuenta.getValor(i,"valor_reformado_prprf")); //*(-1);
			
			BigDecimal big_valor_ingresado=new BigDecimal(valor_ingresado);
			big_valor_ingresado=big_valor_ingresado.setScale(2, RoundingMode.HALF_UP);
			
			valor_ingresado=Double.parseDouble(big_valor_ingresado+"");
			
			double valor_ingreso_positivo=Double.parseDouble(tab_poa_reforma_fuenta.getValor(i,"valor_reformado_prprf"));
			//System.out.println("valor_ingreso_positivo "+valor_ingreso_positivo+" valor_ingresado " +valor_ingresado);

			double ejcutado_inicial=0;
			double ejecucion=0;
			double asi_inicial=0;
			
			
			//System.out.println("total_ejecutado "+total_ejecutado+" valor_ingresado " +valor_ingresado);

				/*
			if(total_ejecutado<valor_ingresado){
				System.out.println("intrego ver saldo insuficiente "+total_ejecutado+"   xxx  "+valor_ingresado);
				utilitario.agregarMensajeError("Saldo Insuficiente", "No se puede ejecutar la reforma solo dispone de un saldo incluido el valor certificado de: "+total_ejecutado);
				tab_poa_reforma_fuenta.setValor(i, "valor_reformado_prprf", "0");
				utilitario.addUpdate("tab_poa_reforma_fuenta");
				break;
			}
			*/
       		TablaGenerica tabla1= utilitario.consultar(ser_presupuesto.getInicialFuenteFinanciamiento(tab_poa_reforma_fuenta.getValor(i,"ide_prfuf"), com_anio.getValue().toString()));
       		if(tabla1.getTotalFilas()>0){
       			asi_inicial=Double.parseDouble(tabla1.getValor("valor"));
       			
       			BigDecimal big_asi_inicial=new BigDecimal(asi_inicial);
    			big_asi_inicial=big_asi_inicial.setScale(2, RoundingMode.HALF_UP);
    			
    			asi_inicial=Double.parseDouble(big_asi_inicial+"");
    			
    			TablaGenerica tabla2=utilitario.consultar(ser_presupuesto.getEjecutaFuenteFinanciamiento(tab_poa_reforma_fuenta.getValor(i, "ide_prfuf"), com_anio.getValue().toString(),null));
    			if(tabla2.getTotalFilas()>0){
    				ejcutado_inicial=Double.parseDouble(tabla2.getValor("valor"));
    				BigDecimal big_ejcutado_inicial=new BigDecimal(ejcutado_inicial);
        			big_ejcutado_inicial=big_ejcutado_inicial.setScale(2, RoundingMode.HALF_UP);
    				
        			ejcutado_inicial=Double.parseDouble(big_ejcutado_inicial+"");
        			
    				ejecucion=dou_valor_reforma+ejcutado_inicial;
    				BigDecimal big_ejecucion=new BigDecimal(ejecucion);
        			big_ejecucion=big_ejecucion.setScale(2, RoundingMode.HALF_UP);

    				ejecucion=Double.parseDouble(big_ejecucion+"");
    			//	System.out.println("ejcutado_inicial x "+ejcutado_inicial+" ejecucion x "+ejecucion);

    			}
       		}
			//System.out.println("asi_inicial "+asi_inicial+" ejecucion x "+ejecucion);
			/*
       		if(asi_inicial<ejecucion){
				System.out.println("intrego ver saldo insuficiente "+asi_inicial+"   xxx  "+ejecucion);

       			utilitario.agregarMensajeError("Supera Techo Inicial", "No se puede ejecutar la reforma la fuente de financiamiento posee un Techo M�ximo de: "+asi_inicial);
				tab_poa_reforma_fuenta.setValor(i, "valor_reformado_prprf", "0");
				utilitario.addUpdate("tab_poa_reforma_fuenta");
				break;
       		}
		*/
		} 
		
		
		//System.out.println(" actaulizco el valor "+dou_valor_reforma);
		eti_valor_total.setValue(utilitario.getFormatoNumero(dou_valor_reforma, 3));
		utilitario.addUpdate("eti_valor_total");
	}
	public void mostrarDialogoBusca(){
		dia_busca_resolucion.dibujar();
	}
	public void buscaResolucion(){
		tab_poa_reforma_fuenta.setCondicion("resolucion_prprf='"+txt_num_oficio.getValue()+"'");
		tab_poa_reforma_fuenta.ejecutarSql();
		dia_busca_resolucion.cerrar();
	}
	public void aceptarPoa(){
		String str_seleccionado=set_reforma_fuente.getSeleccionados();
		//System.out.println("seleccionados "+str_seleccionado);
		TablaGenerica tab_busca_poa=utilitario.consultar(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(),com_fuente_financiamiento.getValue().toString(),"1",str_seleccionado)); 
		if(str_seleccionado!=null){
			for(int i=0;i<tab_busca_poa.getTotalFilas();i++){
				//if(tab_busca_poa.getValor(i,"codigo").equals(str_seleccionado)){
				tab_poa_reforma_fuenta.insertar();
				tab_poa_reforma_fuenta.setValor("ide_prpoa", tab_busca_poa.getValor(i, "ide_prpoa"));
				tab_poa_reforma_fuenta.setValor("ide_prfuf", tab_busca_poa.getValor(i, "ide_prfuf"));
				tab_poa_reforma_fuenta.setValor("valor_reformado_prprf", "0");
				tab_poa_reforma_fuenta.setValor("activo_prprf", "true");
				tab_poa_reforma_fuenta.setValor("aprobado_prprf", "false");
				tab_poa_reforma_fuenta.setValor("saldo_actual_prprf", tab_busca_poa.getValor(i, "valor_saldo_fuente"));
				//}
			}
			tab_poa_reforma_fuenta.guardar();
			guardarPantalla();
			//set_reforma_fuente.cerrar();
			utilitario.addUpdate("tab_poa_reforma_fuenta");	
		}

	}
	public void mostrarSetReformaFuente(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		else if(com_fuente_financiamiento.getValue()==null){
			utilitario.agregarMensajeInfo("Fuente de Financiamiento", "Seleccione una fuente de Financiamiento para realizar la reforma");
			return;
		}
		else {
		tab_poa_reforma_fuenta.setCondicion("ide_prprf=-1");	
		tab_poa_reforma_fuenta.ejecutarSql();
		eti_valor_total.setValue("0.00");
		utilitario.addUpdate("tab_poa_reforma_fuenta,eti_valor_total");
		utilitario.getConexion().ejecutarSql("delete from pre_poa_reforma_fuente where aprobado_prprf = false and resolucion_prprf is null;");
		set_reforma_fuente.getTab_seleccion().setSql(ser_presupuesto.getPoaSaldosFuenteFinanciamiento(com_anio.getValue().toString(),com_fuente_financiamiento.getValue().toString(),"0","-1"));
		set_reforma_fuente.getTab_seleccion().ejecutarSql();
		set_reforma_fuente.dibujar();
		}
	}
	public void borrarContadores(String resolucion){
		String sql_emilinar_contadores="delete from sis_bloqueo where tabla_bloq ilike 'pre_poa_reforma';"
				+" delete from pre_poa_reforma where resolucion_prpor='"+resolucion+"';";
		utilitario.getConexion().ejecutarSql(sql_emilinar_contadores);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_poa_reforma_fuenta.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(dia_datos_reforma.isVisible()){
		   // Inserto validando reforma por partida
			if(rad_imprimir.getValue().equals("1") && eti_valor_total.getValue().equals("0")) {
				tab_poa_reforma_fuenta.guardar();
				guardarPantalla();
				borrarContadores(txt_num_resolucion_guarda.getValue().toString());
				
				for(int i=0;i<tab_poa_reforma_fuenta.getTotalFilas();i++){
					String sql_actualiza="update pre_poa_reforma_fuente set fecha_prprf='"+cal_fecha_reforma.getFecha()+"',resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"',aprobado_prprf='"+che_aprobado.getValue()+"' where ide_prprf="+tab_poa_reforma_fuenta.getValor(i, "ide_prprf");
					utilitario.getConexion().ejecutarSql(sql_actualiza);
					//actualizarSaldosReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"), tab_poa_reforma_fuenta.getValor(i, "valor_reformado_prprf"), txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_poa_reforma_fuenta.getValor(i, "saldo_actual_prprf"),che_aprobado.getValue().toString());
					ser_presupuesto.trigActualizaReformaFuente(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));
					ser_presupuesto.trigActualizaReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));
					
				}
				tab_poa_reforma_fuenta.setCondicion("resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"'");
				tab_poa_reforma_fuenta.ejecutarSql();
			}
		   // Inserto validando reforma por techos
			else if(rad_imprimir.getValue().equals("0")){
				
				tab_poa_reforma_fuenta.guardar();
				guardarPantalla();
				borrarContadores(txt_num_resolucion_guarda.getValue().toString());
				
				for(int i=0;i<tab_poa_reforma_fuenta.getTotalFilas();i++){
					String sql_actualiza="update pre_poa_reforma_fuente set fecha_prprf='"+cal_fecha_reforma.getFecha()+"',resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"',aprobado_prprf='"+che_aprobado.getValue()+"' where ide_prprf="+tab_poa_reforma_fuenta.getValor(i, "ide_prprf");
					utilitario.getConexion().ejecutarSql(sql_actualiza);
					//actualizarSaldosReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"), tab_poa_reforma_fuenta.getValor(i, "valor_reformado_prprf"), txt_num_resolucion_guarda.getValue().toString(), cal_fecha_reforma.getFecha(), tab_poa_reforma_fuenta.getValor(i, "saldo_actual_prprf"),che_aprobado.getValue().toString());
					
					//triguers actualiza reformas.
					ser_presupuesto.trigActualizaReformaFuente(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));
					ser_presupuesto.trigActualizaReforma(tab_poa_reforma_fuenta.getValor(i, "ide_prpoa"));

				}
				tab_poa_reforma_fuenta.setCondicion("resolucion_prprf='"+txt_num_resolucion_guarda.getValue().toString()+"'");
				tab_poa_reforma_fuenta.setCampoOrden("ide_prprf desc");
				tab_poa_reforma_fuenta.ejecutarSql();
			}
			else {
				utilitario.agregarMensajeError("No se puede ejecutar Reforma", "La Reforma por Partida debe Generar un Valor 0.00");
				return;
				
			}
			dia_datos_reforma.cerrar();
		}
		else{
			dia_datos_reforma.dibujar();
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_poa_reforma_fuenta() {
		return tab_poa_reforma_fuenta;
	}

	public void setTab_poa_reforma_fuenta(Tabla tab_poa_reforma_fuenta) {
		this.tab_poa_reforma_fuenta = tab_poa_reforma_fuenta;
	}

	public SeleccionTabla getSet_reforma_fuente() {
		return set_reforma_fuente;
	}

	public void setSet_reforma_fuente(SeleccionTabla set_reforma_fuente) {
		this.set_reforma_fuente = set_reforma_fuente;
	}
	public Dialogo getDia_datos_reforma() {
		return dia_datos_reforma;
	}
	public void setDia_datos_reforma(Dialogo dia_datos_reforma) {
		this.dia_datos_reforma = dia_datos_reforma;
	}
	public Dialogo getDia_busca_resolucion() {
		return dia_busca_resolucion;
	}
	public void setDia_busca_resolucion(Dialogo dia_busca_resolucion) {
		this.dia_busca_resolucion = dia_busca_resolucion;
	}



}

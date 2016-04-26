package paq_presupuesto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;
public class pre_liquida_certificacion extends Pantalla {
	
	private Tabla tab_liquida_certificacion=new Tabla();
	private  Tabla tab_detalle=new Tabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
	private SeleccionTabla set_certificacion=new SeleccionTabla();
	private Combo com_anio=new Combo();
	private Radio rad_imprimir= new Radio();
	public static String par_modulosec_certificacion;



	
	 @EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	 @EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
			

	
	public pre_liquida_certificacion (){
		
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		par_modulosec_certificacion=utilitario.getVariable("p_modulo_secuencialcertificacion");

	/////boton buscar poa
		Boton bot_certificacion=new Boton();
		bot_certificacion.setIcon("ui-icon-person");
		bot_certificacion.setValue("Buscar Certificación");
		bot_certificacion.setMetodo("importarCertificacion");
		//bar_botones.agregarBoton(bot_certificacion);
		
		Grid gri_formulario = new Grid();
    	gri_formulario.setColumns(2);
    	
		List listax = new ArrayList();
	       Object fila1x[] = {
	           "0", "PARCIAL"
	       };
	       Object fila2x[] = {
	           "1", "TOTAL"
	       };
	       
	       listax.add(fila1x);
	       listax.add(fila2x);
	       rad_imprimir.setId("rad_imprimir");
	       rad_imprimir.setRadio(listax);
	       rad_imprimir.setValue(fila2x);
	    gri_formulario.getChildren().add(rad_imprimir);
		gri_formulario.getChildren().add(bot_certificacion);
		
		tab_liquida_certificacion.setId("tab_liquida_certificacion");
		tab_liquida_certificacion.setHeader("LIQUIDACION CERTIFICACION");
		tab_liquida_certificacion.setTabla("pre_liquida_certificacion", "ide_prlce", 1);
		tab_liquida_certificacion.getColumna("activo_prlce").setValorDefecto("true");
		tab_liquida_certificacion.getColumna("ide_prcer").setCombo(ser_presupuesto.getCertificacion("true,false"));
		tab_liquida_certificacion.getColumna("ide_prcer").setAutoCompletar();
		tab_liquida_certificacion.getColumna("ide_prcer").setLectura(true);
		  // para contruir los radios
		   List lista = new ArrayList();
	       Object fila1[] = {
	           "1", "TOTAL"
	       };
	       Object fila2[] = {
	           "0", "PARCIAL"
	       };
	       
	       lista.add(fila1);
	       lista.add(fila2);
	       tab_liquida_certificacion.getColumna("total_parcial_prlce").setRadio(lista, "1");
	       tab_liquida_certificacion.getColumna("total_parcial_prlce").setRadioVertical(true);
	       tab_liquida_certificacion.getColumna("total_parcial_prlce").setLectura(true);
	    tab_liquida_certificacion.getColumna("valor_total_prlce").setValorDefecto("0.00");
	    tab_liquida_certificacion.getColumna("valor_total_prlce").setEtiqueta();
	    tab_liquida_certificacion.getColumna("valor_total_prlce").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
	    tab_liquida_certificacion.setTipoFormulario(true);
	    tab_liquida_certificacion.getGrid().setColumns(6);
		tab_liquida_certificacion.agregarRelacion(tab_detalle);
		tab_liquida_certificacion.dibujar();
		PanelTabla pat_liquida =new PanelTabla();
		pat_liquida.setHeader(gri_formulario);
		pat_liquida.setPanelTabla(tab_liquida_certificacion);
		
		///// detalle liquida certificacion
		
		tab_detalle.setId("tab_detalle");
		tab_detalle.setHeader("DETALLE LIQUIDACION CERTIFICACION");
		tab_detalle.setTabla("pre_detalle_liquida_certif", "ide_prdcl", 2);
		tab_detalle.getColumna("activo_prdcl").setValorDefecto("true");
		tab_detalle.getColumna("activo_prdcl").setLectura(true);
		tab_detalle.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_detalle.getColumna("ide_prpoa").setAutoCompletar();
		tab_detalle.getColumna("ide_prpoa").setLectura(true);
		tab_detalle.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_detalle.getColumna("ide_prfuf").setAutoCompletar();
		tab_detalle.getColumna("ide_prfuf").setLectura(true);
		tab_detalle.getColumna("valor_certificado_prdcl").setLectura(true);
		//tab_detalle.getColumna("valor_liquidado_prdcl").setLectura(true);

		tab_detalle.dibujar();
		PanelTabla pat_detalle=new PanelTabla();
		
		pat_detalle.setPanelTabla(tab_detalle);
		
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_liquida, pat_detalle, "30%", "H");
		
		agregarComponente(div_divi);

	/*
	/////boton buscar poa
			Boton bot_buscar=new Boton();
			bot_buscar.setIcon("ui-icon-person");
			bot_buscar.setValue("Buscar POA");
			bot_buscar.setMetodo("importarPoa");
			bar_botones.agregarBoton(bot_buscar);
*/
			set_poa.setId("set_poa");
			set_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","true"),"ie_prpoa");
			set_poa.setTitle("Seleccione Poa");
			set_poa.getTab_seleccion().getColumna("DETALLE_PROGRAMA").setFiltro(true);//pone filtro
			set_poa.getTab_seleccion().getColumna("PROGRAMA").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_PROYECTO").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("PROYECTO").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_PRODUCTO").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("PRODUCTO").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_ACTIVIDAD").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("ACTIVIDAD").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("DETALLE_SUBACTIVIDAD").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("SUBACTIVIDAD").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("CODIGO_SUBACTIVIDAD").setFiltro(true);
			set_poa.getTab_seleccion().getColumna("NUM_RESOLUCION_PRPOA").setFiltro(true);
			set_poa.getBot_aceptar().setMetodo("aceptarPoa");
			agregarComponente(set_poa);


		

			set_certificacion.setId("set_certificacion");
			set_certificacion.setSeleccionTabla(ser_presupuesto.getCertificacion("true,false"),"ide_prcer");
			set_certificacion.setTitle("Seleccione Certificación");
			set_certificacion.getTab_seleccion().getColumna("nro_certificacion_prcer").setFiltro(true);

			set_certificacion.setRadio();
			set_certificacion.getBot_aceptar().setMetodo("aceptarCertificacion");
			agregarComponente(set_certificacion);

		
		
	}
	
////importar poa
	public void importarPoa(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","true"));
		set_poa.getTab_seleccion().ejecutarSql();
		set_poa.dibujar();

	}

	public  void aceptarPoa(){
		String str_seleccionados = set_poa.getSeleccionados();
		

		if (str_seleccionados!=null){
			TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaPoa(str_seleccionados);		
			for(int i=0;i<tab_poa.getTotalFilas();i++){
				tab_detalle.insertar();
				tab_detalle.setValor("ide_prpoa", tab_poa.getValor(i, "ide_prpoa"));
				tab_detalle.setValor("valor_certificado_prdcl", tab_poa.getValor("presupuesto_codificado_prpoa"));
				
			}
			set_poa.cerrar();
			utilitario.addUpdate("tab_detalle");
		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	
	}

	/////certificacion
	public void importarCertificacion(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		set_certificacion.getTab_seleccion().setSql(ser_presupuesto.getCertificacion("true,false"));
		set_certificacion.getTab_seleccion().ejecutarSql();
		set_certificacion.dibujar();

	}
	public  void aceptarCertificacion(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		String str_seleccionado = set_certificacion.getValorSeleccionado();
		
		if (str_seleccionado!=null){

			tab_liquida_certificacion.insertar();
			tab_liquida_certificacion.setValor("ide_prcer", str_seleccionado);
			tab_liquida_certificacion.setValor("total_parcial_prlce", rad_imprimir.getValue().toString());
			tab_liquida_certificacion.setValor("sec_liquidacion_prlce", ser_contabilidad.numeroSecuencial(par_modulosec_certificacion));
			// Primera validacion cuando es una liquidacion total de la certificacion
			if(rad_imprimir.getValue().equals("1")){
			TablaGenerica tab_valida_presupuesto = utilitario.consultar("select * from pre_tramite where ide_prcer="+str_seleccionado);
				if(tab_valida_presupuesto.getTotalFilas()>0){
					utilitario.agregarMensajeError("No se puede realizar una Liquidación Total", "Existen ya ejecutados compromisos con la certificacion seleccionada");
					tab_liquida_certificacion.limpiar();
					return;

				}
				else{
					
					TablaGenerica tab_consulta_poacer = utilitario.consultar("select ide_prpoc,ide_prpoa,ide_prfuf,valor_certificado_prpoc,valor_certificado_prpoc* (-1) as total_liquidado from pre_poa_certificacion  where ide_prcer="+str_seleccionado);
					if(tab_consulta_poacer.getTotalFilas()>0){
						for(int i=0;i<tab_consulta_poacer.getTotalFilas();i++){
							tab_detalle.insertar();
							tab_detalle.setValor("valor_certificado_prdcl", tab_consulta_poacer.getValor(i, "valor_certificado_prpoc"));;
							tab_detalle.setValor("valor_liquidado_prdcl", tab_consulta_poacer.getValor(i, "total_liquidado"));;
							tab_detalle.setValor("saldo_liquidado_prdcl", "0");
							tab_detalle.setValor("activo_prdcl", "true");;
							tab_detalle.setValor("ide_prpoa", tab_consulta_poacer.getValor(i, "ide_prpoa"));;
							tab_detalle.setValor("ide_prfuf", tab_consulta_poacer.getValor(i, "ide_prfuf"));;

						}
						tab_liquida_certificacion.setValor("valor_total_prlce", utilitario.getFormatoNumero(tab_detalle.getSumaColumna("valor_liquidado_prdcl"),2)+"");
					}
					
				
				}
			}
			// Segunda validacion cuando es una liquidacion parcial de la certificacion
			else if(rad_imprimir.getValue().equals("0")){
				TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaCert(str_seleccionado);		
				for(int i=0;i<tab_poa.getTotalFilas();i++){
					tab_detalle.insertar();
					tab_detalle.setValor("ide_prpoa", tab_poa.getValor(i, "ide_prpoa"));
					tab_detalle.setValor("ide_prfuf", tab_poa.getValor(i, "ide_prfuf"));
					tab_detalle.setValor("valor_certificado_prdcl", tab_poa.getValor(i, "saldo_comprometer"));
					TablaGenerica tab_consulta_negativo=utilitario.consultar("select 1 as codigo,(-1)*"+tab_poa.getValor(i, "saldo_comprometer")+" as valor_liquidar");
					
					tab_detalle.setValor("valor_liquidado_prdcl",tab_consulta_negativo.getValor("valor_liquidar"));
					tab_detalle.setValor("saldo_liquidado_prdcl", "0");
					tab_detalle.setValor("activo_prdcl", "true");

				}
				tab_liquida_certificacion.setValor("valor_total_prlce", tab_detalle.getSumaColumna("valor_liquidado_prdcl")+"");

			}
			
		}
			set_certificacion.cerrar();
			utilitario.addUpdate("tab_liquida_certificacion"); 
		}
	
public void calcularTotalLiquidacion(){
	double dou_valo_liquidado=0;
}

	
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(tab_liquida_certificacion.isFocus()){
		utilitario.agregarMensajeInfo("No puede insertar", "Debe buscar una Certificación");
		}
		else if(tab_detalle.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe ingresar un poa");
		}
	}
		
	

	@Override
	public void guardar() {
		if(tab_liquida_certificacion.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_modulosec_certificacion), par_modulosec_certificacion);
		}
		if(tab_liquida_certificacion.getValor("total_parcial_prlce").equals("1")){
			double saldo=0;
			try{
			saldo=tab_detalle.getSumaColumna("saldo_liquidado_prdcl");
			}
			catch (Exception e){
				utilitario.agregarMensaje("No existe valor", "El valor del saldo deb ser valido");
			}
			if (saldo !=0){
				utilitario.agregarMensajeError("No se puede guardar", "Para una Liquidacion Total de la Certificación los Saldos deben ser Cero");
			}
		}
		// TODO Auto-generated method stub
		if(tab_liquida_certificacion.guardar()){
			if(tab_detalle.guardar()){
				guardarPantalla();
				
			}
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
		
	}


	public Tabla getTab_liquida_certificacion() {
		return tab_liquida_certificacion;
	}


	public void setTab_liquida_certificacion(Tabla tab_liquida_certificacion) {
		this.tab_liquida_certificacion = tab_liquida_certificacion;
	}


	public Tabla getTab_detalle() {
		return tab_detalle;
	}


	public void setTab_detalle(Tabla tab_detalle) {
		this.tab_detalle = tab_detalle;
	}

	public SeleccionTabla getSet_poa() {
		return set_poa;
	}

	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}

	public SeleccionTabla getSet_certificacion() {
		return set_certificacion;
	}

	public void setSet_certificacion(SeleccionTabla set_certificacion) {
		this.set_certificacion = set_certificacion;
	}

}

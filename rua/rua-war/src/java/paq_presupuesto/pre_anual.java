package paq_presupuesto;

import framework.aplicacion.TablaGenerica;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.HashMap;
import java.util.Map;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;

public class pre_anual extends Pantalla{
	
	private Tabla tab_anual= new Tabla();
	private Tabla tab_mensual= new Tabla();
	private Tabla tab_reforma= new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_clasificador=new SeleccionTabla();
        private SeleccionTabla set_eje_devengado = new SeleccionTabla();
        private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();


	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioBodega ser_Bodega = (ServicioBodega) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	public pre_anual(){
            
                        		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
                
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		tab_anual.setId("tab_anual");
		tab_anual.setHeader("PRESUPUESTO ANUAL DE INGRESOS");
		tab_anual.setTabla("pre_anual", "ide_pranu", 1);
		tab_anual.setCondicion("not ide_prcla is null and ide_geani=-1");
		tab_anual.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_anual.getColumna("ide_prcla").setLectura(true);
		//tab_anual.getColumna("ide_prcla").setAutoCompletar();
		tab_anual.getColumna("ide_prpro").setVisible(false);
		tab_anual.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_anual.getColumna("ide_geani").setVisible(false);
		tab_anual.getColumna("ide_prfuf").setCombo("select ide_prfuf,detalle_prfuf from pre_fuente_financiamiento order by detalle_prfuf");
		tab_anual.getColumna("valor_reformado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_reformado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_reformado_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_codificado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_codificado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_codificado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_codificado_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_reformado_h_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_h_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_reformado_h_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_reformado_d_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_d_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_reformado_d_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_devengado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_devengado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_devengado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_precomprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_precomprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_precomprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_recaudado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
		tab_anual.getColumna("valor_eje_comprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_inicial_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_precomprometido_pranu").setVisible(false);
		tab_anual.getColumna("valor_eje_comprometido_pranu").setVisible(false);
		tab_anual.getColumna("ide_prpoa").setVisible(false);
		
		tab_anual.getColumna("activo_pranu").setValorDefecto("true");
		tab_anual.agregarRelacion(tab_mensual);
		tab_anual.agregarRelacion(tab_reforma);
		//requeridas
		tab_anual.getColumna("ide_prcla").setRequerida(true);
		tab_anual.getColumna("valor_inicial_pranu").setRequerida(true);

		tab_anual.setTipoFormulario(true);
		tab_anual.getGrid().setColumns(4);
		tab_anual.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anual);

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		///// mensual

		tab_mensual.setId("tab_mensual");
		tab_mensual.setHeader("EJECUCION PRESUPUESTARIA MENSUAL");
		tab_mensual.setIdCompleto("tab_tabulador:tab_mensual");
		tab_mensual.setTabla("pre_mensual", "ide_prmen", 2);
		tab_mensual.getColumna("ide_prtra").setVisible(false);
		tab_mensual.getColumna("ide_comov").setLectura(true);
      		tab_mensual.getColumna("pagado_prmen").setVisible(false);
		tab_mensual.getColumna("comprometido_prmen").setVisible(false);
		tab_mensual.getColumna("valor_anticipo_prmen").setVisible(false);
		tab_mensual.getColumna("ide_tecpo").setVisible(false);
		tab_mensual.getColumna("ide_prfuf").setVisible(false);
		tab_mensual.getColumna("ide_prcer").setVisible(false);
		tab_mensual.getColumna("ide_cndcc").setVisible(false);
		tab_mensual.getColumna("certificado_prmen").setVisible(false);

		tab_mensual.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "nombre_gemes", "");
		tab_mensual.getColumna("ide_gemes").setVisible(false);
		tab_mensual.getColumna("cobrado_prmen").setVisible(false);
		tab_mensual.getColumna("cobradoc_prmen").setVisible(false);
                
                tab_mensual.getColumna("ide_codem").setLectura(true);
		tab_mensual.getColumna("activo_prmen").setValorDefecto("true");
		//tab_mensual.setTipoFormulario(true);
		//tab_mensual.getGrid().setColumns(6);
		tab_mensual.dibujar();
		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_mensual);
		
////////REFORMA MES
		tab_reforma.setId("tab_reforma");
		tab_reforma.setHeader("REFORMA PRESUPUESTARIA MENSUAL");
		tab_reforma.setIdCompleto("tab_tabulador:tab_reforma");
		tab_reforma.setTabla("pre_reforma_mes", "ide_prrem", 3);
		tab_reforma.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "nombre_gemes", "");
		tab_reforma.getColumna("val_reforma_h_prrem").setMetodoChange("calcular");
		tab_reforma.getColumna("val_reforma_d_prrem").setMetodoChange("calcular");
		tab_reforma.getColumna("activo_prrem").setValorDefecto("true");
		tab_reforma.dibujar();
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_reforma);
		
		
		
		tab_tabulador.agregarTab("EJECUCION MENSUAL", pat_panel2);//intancia los tabuladores 
		tab_tabulador.agregarTab("REFORMA PRESUPUESTARIA MENSUAL",pat_panel3);

		
		Division div_division =new Division ();
		div_division.dividir2(pat_panel1, tab_tabulador, "50%", "h");
		agregarComponente(div_division);

		Boton bot_agregar=new Boton();
		bot_agregar.setValue("Agregar Partida Presupuestaria");
		bot_agregar.setMetodo("agregarClasificador");
		bar_botones.agregarBoton(bot_agregar);

		set_clasificador.setId("set_clasificador");
		set_clasificador.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
		set_clasificador.setRadio(); //solo selecciona una opcion
		set_clasificador.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1","-1"), "ide_prcla"); 
		set_clasificador.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true); //pone filtro
		set_clasificador.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);//pone filtro
		set_clasificador.getBot_aceptar().setMetodo("aceptarClasificador");
		agregarComponente(set_clasificador);
                
                Boton bot_devengado=new Boton();
		bot_devengado.setValue("Devengar Asientos Contables");
		bot_devengado.setMetodo("abrirPorDevengar");
		bar_botones.agregarBoton(bot_devengado);

		set_eje_devengado.setId("set_eje_devengado");
		set_eje_devengado.setTitle("SELECCIONE LOS MOVIMIENTOS CONTABLES A DEVENGARSE");
		set_eje_devengado.setSeleccionTabla(ser_presupuesto.getPorDevengarIngresos("-1", "1", "-1"), "ide_cndcc"); 
		set_eje_devengado.getBot_aceptar().setMetodo("aceptarPorDevengar");
		agregarComponente(set_eje_devengado); 
	}
		public void agregarClasificador(){
			//si no selecciono ningun valor en el combo
			if(com_anio.getValue()==null){
				utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
				return;
			}
			//Si la tabla esta vacia
			if(tab_anual.isEmpty()){
				utilitario.agregarMensajeInfo("No se puede agregar Clasificador, por que no existen registros", "");
				return;
			}
			//Filtrar los clasificadores del Año seleccionado
			set_clasificador.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true",com_anio.getValue().toString(),"1"));
			set_clasificador.getTab_seleccion().ejecutarSql();
			set_clasificador.dibujar();
		}

		public void aceptarClasificador(){
			if(set_clasificador.getValorSeleccionado()!=null){
				tab_anual.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
				//Actualiza 
				utilitario.addUpdate("tab_anual");//actualiza mediante ajax el objeto tab_poa
				set_clasificador.cerrar();
			}
			else{
				utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
			}
		}


	public void abrirPorDevengar(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
                //TablaGenerica tab_prcla = utilitario.consultar("select ide_prpro,ide_prcla from pre_programa  where ide_prpro = "+tab_anual.getValor("ide_prpro"));
	
		//Filtrar los clasificadores del Año seleccionado
		set_eje_devengado.getTab_seleccion().setSql(ser_presupuesto.getPorDevengarIngresos(tab_anual.getValor("ide_prcla"),"1","0"));
		set_eje_devengado.getTab_seleccion().ejecutarSql();
		set_eje_devengado.dibujar();

	}
        
        public void aceptarPorDevengar (){
            String str_seleccionados= set_eje_devengado.getSeleccionados();

            TablaGenerica tab_saldos_devengar = utilitario.consultar(ser_presupuesto.getPorDevengarIngresos(tab_anual.getValor("ide_prcla"),"2",str_seleccionados));
            if(str_seleccionados!=null){
                for(int i=0;i<tab_saldos_devengar.getTotalFilas();i++){
				tab_mensual.insertar();
				tab_mensual.setValor("fecha_ejecucion_prmen", tab_saldos_devengar.getValor(i, "fecha_trans_cnccc"));
                                tab_mensual.setValor("devengado_prmen", tab_saldos_devengar.getValor(i, "saldo_por_devengar"));
                                tab_mensual.setValor("ide_pranu", tab_anual.getValor("ide_pranu"));
                                tab_mensual.setValor("comprobante_prmen", tab_saldos_devengar.getValor(i, "numero_cnccc"));
                                tab_mensual.setValor("cobrado_prmen", "0");
                                tab_mensual.setValor("cobradoc_prmen", "0");
                                tab_mensual.setValor("pagado_prmen", "0");
                                tab_mensual.setValor("comprometido_prmen", "0");
                                tab_mensual.setValor("valor_anticipo_prmen","0");
                                tab_mensual.setValor("certificado_prmen","0");
                                tab_mensual.setValor("activo_prmen","true");
                                tab_mensual.setValor("ide_codem", tab_saldos_devengar.getValor(i, "ide_cndcc"));
                                tab_mensual.setValor("ide_cndcc", tab_saldos_devengar.getValor(i, "ide_cndcc"));
                                tab_mensual.setValor("ide_comov", tab_saldos_devengar.getValor(i, "ide_cnccc"));
				
			}
                calcularDevengado();
			set_eje_devengado.cerrar();
                       
			utilitario.addUpdate("tab_mensual");
            }
            else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
        }
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_anual.setCondicion("not ide_prcla is null and ide_geani="+com_anio.getValue());
			tab_anual.ejecutarSql();
			//tab_mes.ejecutarValorForanea(tab_poa.getValorSeleccionado());
                         tab_mensual.ejecutarValorForanea(tab_anual.getValorSeleccionado());
                       tab_reforma.ejecutarValorForanea(tab_anual.getValorSeleccionado());

		}
		else{
			utilitario.agregarMensajeInfo("Selecione un Año", "");

		}
	}
	public void calcularDevengado(){
            tab_anual.setValor("valor_devengado_pranu",utilitario.getFormatoNumero(tab_mensual.getSumaColumna("devengado_prmen"),3));
            tab_anual.setValor("valor_eje_comprometido_pranu",utilitario.getFormatoNumero(tab_mensual.getSumaColumna("comprometido_prmen"),3));
		tab_anual.modificar(tab_anual.getFilaActual());//para que haga el update

		utilitario.addUpdateTabla(tab_anual, "valor_eje_comprometido_pranu,valor_devengado_pranu", "tab_mensual");	

        }
	///// para subir vaslores de un tabla a otra 
	public void  calcularValor(){
		double dou_valor_h=0;
		double dou_valor_d=0;
		double dou_valor_reformado_debito=0;
		double dou_valor_codificado=0;
		double dou_valor_inicial=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_inicial=Double.parseDouble(tab_anual.getValor("valor_inicial_pranu"));
		} catch (Exception e) {
		}
		
		String valor1=tab_reforma.getSumaColumna("val_reforma_h_prrem")+"";
		dou_valor_h=Double.parseDouble(valor1);

		String valor2=tab_reforma.getSumaColumna("val_reforma_d_prrem")+"";
		dou_valor_d=Double.parseDouble(valor2);
		dou_valor_reformado_debito=dou_valor_d-dou_valor_h;
		dou_valor_codificado=dou_valor_inicial+dou_valor_reformado_debito;

		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_anual.setValor("valor_reformado_h_pranu",utilitario.getFormatoNumero(valor1,3));
		tab_anual.setValor("valor_reformado_d_pranu",utilitario.getFormatoNumero(valor2,3));
		tab_anual.setValor("valor_reformado_pranu",utilitario.getFormatoNumero(dou_valor_reformado_debito,3));
		tab_anual.setValor("valor_codificado_pranu",utilitario.getFormatoNumero(dou_valor_codificado,3));
		
		tab_anual.modificar(tab_anual.getFilaActual());//para que haga el update

		utilitario.addUpdateTabla(tab_anual, "valor_reformado_h_pranu,valor_reformado_d_pranu,valor_reformado_pranu,valor_codificado_pranu", "tab_reforma");	
	
	}
/// 
	public void calcular(AjaxBehaviorEvent evt) {
		tab_reforma.modificar(evt); //Siempre es la primera linea
		calcularValor();

	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;

		}
		if(tab_anual.isFocus()){
			tab_anual.insertar();
			tab_anual.setValor("ide_geani",com_anio.getValue()+"");
			}
		else if(tab_mensual.isFocus()){
			tab_mensual.insertar();
		}
			else if(tab_reforma.isFocus()){
				tab_reforma.insertar();
				
			}
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_anual.guardar()){
			if(tab_mensual.guardar()){
				if(tab_reforma.guardar()){
					
				}
			}
		}
		guardarPantalla();
	}
    //reporte
public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}
public void aceptarReporte(){
	if(rep_reporte.getReporteSelecionado().equals("Presupuesto Anual Ingresos"));{
		if (rep_reporte.isVisible()){
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");
			p_parametros.put("pide_anio",Integer.parseInt(com_anio.getValue().toString()));

			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
			
		self_reporte.dibujar();
		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}
		
}        
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_anual() {
		return tab_anual;
	}

	public void setTab_anual(Tabla tab_anual) {
		this.tab_anual = tab_anual;
	}

	public Tabla getTab_mensual() {
		return tab_mensual;
	}

	public void setTab_mensual(Tabla tab_mensual) {
		this.tab_mensual = tab_mensual;
	}

	public Tabla getTab_reforma() {
		return tab_reforma;
	}

	public void setTab_reforma(Tabla tab_reforma) {
		this.tab_reforma = tab_reforma;
	}
	public SeleccionTabla getSet_clasificador() {
		return set_clasificador;
	}
	public void setSet_clasificador(SeleccionTabla set_clasificador) {
		this.set_clasificador = set_clasificador;
	}

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSelf_reporte() {
        return self_reporte;
    }

    public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
        this.self_reporte = self_reporte;
    }

    public SeleccionTabla getSet_eje_devengado() {
        return set_eje_devengado;
    }

    public void setSet_eje_devengado(SeleccionTabla set_eje_devengado) {
        this.set_eje_devengado = set_eje_devengado;
    }
	

}

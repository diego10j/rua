package paq_presupuesto;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.event.NodeSelectEvent;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import servicios.sistema.ServicioSeguridad;
import sistema.aplicacion.Pantalla;

public class pre_anual_egresos extends Pantalla {
	
	private Tabla tab_anual= new Tabla();
	private Tabla tab_mensual= new Tabla();
	private Tabla tab_reforma= new Tabla();
	private Combo com_anio =new Combo();
        private Combo com_casas = new Combo();
        private SeleccionTabla sel_programas = new SeleccionTabla();
	private SeleccionTabla set_programa = new SeleccionTabla();
        private SeleccionTabla set_actividad = new SeleccionTabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
        private Dialogo dia_por_devengar = new Dialogo();
        private SeleccionTabla set_por_devengar = new SeleccionTabla();
	private SeleccionCalendario sel_calendario=new SeleccionCalendario();
        private Arbol arb_arbol = new Arbol();
        private Tabla tab_programa= new Tabla();
        	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioSeguridad ser_seguridad=(ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	public pre_anual_egresos(){
                //bar_botones.getBot_insertar().setRendered(false);
            
                            		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		com_anio.setId("com_anio");
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		//com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
                
                com_casas.setId("com_casas");
    		com_casas.setCombo(ser_presupuesto.getFuncionPrograma("1"));
		//com_casas.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione la Sucursal"));
		bar_botones.agregarComponente(com_casas);            
                
                Boton bot_actualizar = new Boton();
                bot_actualizar.setValue("Consultar");
                bot_actualizar.setMetodo("seleccionaElAnio");
                bar_botones.agregarBoton(bot_actualizar);

		
                arb_arbol.setId("arb_arbol");                
		arb_arbol.setArbol("pre_funcion_programa", "ide_prfup", "codigo_prfup||' '||detalle_prfup", "pre_ide_prfup");
		arb_arbol.setCondicion("ide_prfup=-1");
                arb_arbol.onSelect("seleccionoPrograma");	
		arb_arbol.dibujar();
                
                tab_programa.setId("tab_programa");
		tab_programa.setTabla("pre_programa", "ide_prpro", 4);
                tab_programa.setHeader("PROGRAMAS PRESUPUESTARIOS");
                tab_programa.setCondicion("ide_prpro=-1");
                tab_programa.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
                tab_programa.getColumna("ide_prcla").setLectura(true);
                tab_programa.getColumna("ide_prcla").setUnico(true);
                tab_programa.getColumna("ide_prfup").setUnico(true);
                tab_programa.getColumna("ide_prfup").setCombo("pre_funcion_programa", "ide_prfup", "codigo_prfup||' '||detalle_prfup","");
                tab_programa.getColumna("cod_programa_prpro").setLectura(true);
                tab_programa.getColumna("ide_prfup").setLectura(true);
                tab_programa.getColumna("ide_prfup").setAutoCompletar();
                tab_programa.agregarRelacion(tab_anual);
                tab_programa.dibujar ();
                PanelTabla pat_pane3=new PanelTabla();
		pat_pane3.setPanelTabla(tab_programa);

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_anual.setId("tab_anual");
		tab_anual.setHeader("EJECUCION PRESUPUESTARIA ANUAL");
		tab_anual.setTabla("pre_anual", "ide_pranu", 1);
		tab_anual.getColumna("ide_prcla").setCombo("select ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla from pre_clasificador order by codigo_clasificador_prcla");
		tab_anual.getColumna("ide_prcla").setVisible(false);
		//tab_anual.getColumna("ide_prfuf").setVisible(false);
		tab_anual.getColumna("ide_prfuf").setCombo("select ide_prfuf,detalle_prfuf from pre_fuente_financiamiento order by detalle_prfuf");

		tab_anual.setCondicion("not ide_prpro is null");
		tab_anual.getColumna("ide_prpro").setCombo(ser_presupuesto.getPrograma("true,false"));
		//tab_anual.getColumna("ide_prpro").setAutoCompletar();
		tab_anual.getColumna("ide_prpro").setLectura(true);
		tab_anual.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false", "false,true"));
		tab_anual.getColumna("ide_geani").setVisible(false);
		tab_anual.setCondicion("ide_geani=-1");
		tab_anual.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaNombre("select ide_geani from gen_anio"));
		tab_anual.getColumna("ide_prpoa").setAutoCompletar();
		tab_anual.getColumna("ide_prpoa").setLectura(true);
                tab_anual.getColumna("ide_prpoa").setVisible(false);

		tab_anual.getColumna("valor_reformado_h_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_h_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_reformado_h_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_reformado_d_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_d_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_reformado_d_pranu").setValorDefecto("0.00");
		//tab_anual.getColumna("ide_geani").setValorDefecto(com_anio.getValue().toString());
		//tab_anual.getColumna("ide_prfup").setCombo("pre_funcion_programa", "ide_prfup", "detalle_prfup,", "");
		tab_anual.getColumna("valor_reformado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_reformado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_reformado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_codificado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_codificado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_codificado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_devengado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_devengado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_devengado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_precomprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_precomprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_precomprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEtiqueta();
		tab_anual.getColumna("valor_eje_comprometido_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_eje_comprometido_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_recaudado_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEtiqueta();
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setValorDefecto("0.00");
		tab_anual.getColumna("valor_inicial_pranu").setMetodoChange("calcularValor");
		tab_anual.getColumna("valor_recaudado_pranu").setVisible(false);
		tab_anual.getColumna("valor_recaudado_efectivo_pranu").setVisible(false);
		tab_anual.getColumna("pagado_pranu").setVisible(false);


		
		/// requerida para grabar
		tab_anual.getColumna("ide_prpro").setRequerida(true);
		tab_anual.getColumna("valor_inicial_pranu").setRequerida(true);
		tab_anual.getColumna("activo_pranu").setValorDefecto("true");
		tab_anual.setTipoFormulario(true);
		tab_anual.getGrid().setColumns(6);
		tab_anual.agregarRelacion(tab_mensual);
		tab_anual.agregarRelacion(tab_reforma);
		tab_anual.dibujar();
		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anual);
		
		//////////////pre_mensual
		
		tab_mensual.setId("tab_mensual");
		tab_mensual.setHeader("DETALLE DE EJECUCION PRESUPUESTARIA");
		tab_mensual.setIdCompleto("tab_tabulador:tab_mensual");
		tab_mensual.setTabla("pre_mensual", "ide_prmen", 2);
		//tab_mensual.getColumna("ide_prtra").setVisible(false);
		tab_mensual.getColumna("ide_comov").setLectura(true);
		//tab_mensual.setCondicion("ide_prpro!=null");
		tab_mensual.getColumna("valor_anticipo_prmen").setVisible(false);
		tab_mensual.getColumna("ide_tecpo").setVisible(false);
		tab_mensual.getColumna("ide_prfuf").setVisible(false);
		tab_mensual.getColumna("ide_prcer").setVisible(false);
		tab_mensual.getColumna("ide_cndcc").setVisible(false);
		tab_mensual.getColumna("certificado_prmen").setVisible(false);
		tab_mensual.getColumna("cobrado_prmen").setVisible(false);
		tab_mensual.getColumna("cobradoc_prmen").setVisible(false);
		tab_mensual.getColumna("pagado_prmen").setVisible(false);

                
		//tab_mensual.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "nombre_gemes", "");
                tab_mensual.getColumna("ide_gemes").setVisible(false);

		tab_mensual.getColumna("ide_codem").setLectura(true);
		//tab_anual.getColumna("ide_prfup").setCombo("pre_funcion_programa", "ide_prfup", "detalle_prfup,", "");
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
		tab_reforma.setCampoForanea("ide_pranu");
		tab_reforma.dibujar();
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_reforma);
		
		
		
		tab_tabulador.agregarTab("DETALLE DE EJECUCION PRESUPUESTARIA", pat_panel2);//intancia los tabuladores 
		tab_tabulador.agregarTab("REFORMA PRESUPUESTARIA MENSUAL",pat_panel3);

		
		Division div_division =new Division ();
		div_division.dividir3(pat_pane3,pat_panel1, tab_tabulador,"20%", "50%", "h");
		
                Division div_division_arbol =new Division ();
		div_division_arbol.dividir2(arb_arbol, div_division, "20%", "V");
                agregarComponente(div_division_arbol);
                
                /*
		Boton bot_importarpoa = new Boton();
		bot_importarpoa.setValue("Importar POA");
		bot_importarpoa.setIcon("ui-icon-person");
		bot_importarpoa.setMetodo("importarPoa");
		bar_botones.agregarBoton(bot_importarpoa);
                */
		
		Boton bot_material = new Boton();
		bot_material.setValue("Agregar Programa");
		bot_material.setTitle("Solicitud Programa");
		bot_material.setIcon("ui-icon-person");
		bot_material.setMetodo("importarPrograma");
		bar_botones.agregarBoton(bot_material);
		
		set_programa.setId("set_programa");
		set_programa.setSeleccionTabla(ser_presupuesto.getPrograma("true,false"),"IDE_PRPRO");
		set_programa.getTab_seleccion().getColumna("cod_programa_prpro").setFiltroContenido();
		set_programa.getTab_seleccion().getColumna("codigo_prfup").setFiltroContenido();
		set_programa.getTab_seleccion().getColumna("detalle_prfup").setFiltroContenido();
		set_programa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();
                
		set_programa.getTab_seleccion().getColumna("cod_programa_prpro").setNombreVisual("COD. PROGRAMA");
		set_programa.getTab_seleccion().getColumna("codigo_prfup").setNombreVisual("COD. PROYECTO");
		set_programa.getTab_seleccion().getColumna("detalle_prfup").setNombreVisual("DET. PROYECTO");
		set_programa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setNombreVisual("CUENTA PRESUPUESTARIA");
                
		set_programa.getBot_aceptar().setMetodo("aceptarPrograma");
		set_programa.getTab_seleccion().ejecutarSql();
		set_programa.setRadio();
		agregarComponente(set_programa);

                
                //// SEL TABLA PARA DEVENGAR LOS PRESUPUESTOS
                
                Boton bot_por_devengar = new Boton();
		bot_por_devengar.setValue("MOVIMIENTOS POR DEVENGAR");
		bot_por_devengar.setTitle("MOVIMIENTOS CONTABLES POR DEVENGAR");
		bot_por_devengar.setIcon("ui-icon-person");
		bot_por_devengar.setMetodo("abrirPorDevengar");
		bar_botones.agregarBoton(bot_por_devengar);
		
		set_por_devengar.setId("set_por_devengar");
		set_por_devengar.setSeleccionTabla(ser_presupuesto.getPorDevengar("-1","1","0"),"ide_cndcc");
		set_por_devengar.getTab_seleccion().getColumna("nro_asiento").setFiltro(true);
		set_por_devengar.getTab_seleccion().getColumna("numero_cnccc").setFiltro(true);
		set_por_devengar.getTab_seleccion().getColumna("numero_cnccc").setNombreVisual("Nro. Comprobante");
		set_por_devengar.getTab_seleccion().getColumna("ide_cnlap").setVisible(false);
		set_por_devengar.getTab_seleccion().getColumna("ide_cndpc").setVisible(false);

                set_por_devengar.getTab_seleccion().ejecutarSql();
                set_por_devengar.getBot_aceptar().setMetodo("aceptarPorDevengar");

		agregarComponente(set_por_devengar);
                
                
		iniciaPoa();
                
                sel_programas.setId("sel_programas");
		sel_programas.setSeleccionTabla(ser_presupuesto.getFuncionPrograma("1"),"ide_prfup");
		sel_programas.setTitle("Seleccione el Programa");
		sel_programas.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_programas);
                //ACTIVIDAD
                set_actividad.setId("set_actividad");
		set_actividad.setSeleccionTabla(ser_presupuesto.getActividadPrograma("-1"),"ide_prfup");
		set_actividad.setTitle("Seleccione la actividad");
		set_actividad.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_actividad);                
                //Fechas
		sel_calendario.setId("sel_calendario");
		sel_calendario.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_calendario);

	}
        /**
	 * Se ejecuta cuando se selecciona algun nodo del arbol
	 */
		public void seleccionoPrograma(NodeSelectEvent evt){
		tab_programa.limpiar();	
		//Asigna evento al arbol
		arb_arbol.seleccionarNodo(evt);
		//Filtra la tabla Padre
                tab_programa.setCondicion("ide_prfup = "+arb_arbol.getValorSeleccionado()+"");
		tab_programa.ejecutarSql();
                //tab_programa.ejecutarValorPadre(arb_arbol.getValorSeleccionado());
		//Filtra la tabla tab_vigente
		tab_anual.setCondicion("ide_prpro="+tab_programa.getValorSeleccionado()+" and ide_geani= "+com_anio.getValue());
                tab_anual.ejecutarSql();
                tab_anual.imprimirSql();
                utilitario.addUpdate("tab_programa,tab_anual");
	  }
	public void iniciaPoa(){
		set_poa.setId("set_poa");
		set_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","false"),"ide_prpoa");
		set_poa.setTitle("Seleccione Poa");
		set_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);
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
		set_poa.getBot_aceptar().setMetodo("importarPoa");
		agregarComponente(set_poa);
	}
	public void importarPoa(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
		if(set_poa.isVisible()){
			String str_seleccionados=set_poa.getSeleccionados();
			if (str_seleccionados!=null){
				TablaGenerica tab_aprueba_poa =utilitario.consultar("select ide_prpoa,activo_prpoa,ide_prcla,ide_prfup,presupuesto_inicial_prpoa from pre_poa where ide_prpoa in ("+str_seleccionados+")");
				if(tab_aprueba_poa.getTotalFilas()>0){
					
					for(int i=0;i<tab_aprueba_poa.getTotalFilas();i++){
						TablaGenerica tab_programa=utilitario.consultar("select a.ide_prpro,a.ide_prfup,a.ide_prcla from pre_programa a, cont_vigente b where a.ide_prpro = b.ide_prpro and a.ide_prfup ="+tab_aprueba_poa.getValor(i,"ide_prfup")+"  and a.ide_prcla="+tab_aprueba_poa.getValor(i,"ide_prcla"));
						tab_anual.insertar();
						tab_anual.setValor("ide_prpro", tab_programa.getValor("ide_prpro"));
						tab_anual.setValor("ide_geani", com_anio.getValue().toString());
						tab_anual.setValor("ide_prpoa", tab_aprueba_poa.getValor(i,"ide_prpoa"));
						tab_anual.setValor("valor_reformado_pranu", "0");
						tab_anual.setValor("valor_inicial_pranu", tab_aprueba_poa.getValor(i,"presupuesto_inicial_prpoa"));
						tab_anual.setValor("valor_codificado_pranu", tab_aprueba_poa.getValor(i,"presupuesto_inicial_prpoa"));
						tab_anual.setValor("valor_reformado_h_pranu", "0");
						tab_anual.setValor("valor_reformado_d_pranu", "0");
						tab_anual.setValor("valor_devengado_pranu", "0");
						tab_anual.setValor("valor_precomprometido_pranu", "0");
						tab_anual.setValor("valor_eje_comprometido_pranu", "0");
						tab_anual.setValor("valor_recaudado_pranu", "0");
						tab_anual.setValor("valor_recaudado_efectivo_pranu", "0");
						tab_anual.setValor("activo_pranu", "true");
						tab_anual.guardar();
						guardarPantalla();
						
						String sql="update pre_poa set ejecutado_presupuesto_prpoa=true where ide_prpoa= "+tab_aprueba_poa.getValor(i,"ide_prpoa");
						utilitario.getConexion().ejecutarSql(sql);
					}
					
				}
				set_poa.cerrar();
			}

			else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
			}
		}
		else{
			set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","false"));
			set_poa.getTab_seleccion().ejecutarSql();
			set_poa.dibujar();
			}
	}
	///metodo Año
	public void seleccionaElAnio (){
                if(com_anio.getValue()==null){                        
			utilitario.agregarMensajeInfo("Selecione un Año","");
		}
                else if(com_casas.getValue()==null){                        
			utilitario.agregarMensajeInfo("Selecione un Casa","");
		}    
		else{
                    
                    arb_arbol.setCondicion("ide_prfup in ("+ser_presupuesto.getSubactvidadesFuncionPrograma(com_casas.getValue().toString())+")");
                    arb_arbol.ejecutarSql();
                    arb_arbol.setOptimiza(true);
                    utilitario.addUpdate("arb_arbol");
                    /* comento todo por la implemnetacion del arbol
                        tab_anual.setCondicion("not ide_prpro is null and ide_geani="+com_anio.getValue()+" and ide_prpro in ("+ser_presupuesto.getSubactvidadesPrograma(com_casas.getValue().toString())+")");
			tab_anual.ejecutarSql();
			//tab_mes.ejecutarValorForanea(tab_poa.getValorSeleccionado());
                        tab_mensual.ejecutarValorForanea(tab_anual.getValorSeleccionado());
                       tab_reforma.ejecutarValorForanea(tab_anual.getValorSeleccionado());
                       */
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
	public void importarPrograma(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
	
		//Filtrar los clasificadores del Año seleccionado
		set_programa.getTab_seleccion().setSql(ser_presupuesto.getPrograma("true,false"));
		set_programa.getTab_seleccion().ejecutarSql();
		set_programa.dibujar();

	}
	public void abrirPorDevengar(){
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
                TablaGenerica tab_prcla = utilitario.consultar("select ide_prpro,ide_prcla from pre_programa  where ide_prpro = "+tab_anual.getValor("ide_prpro"));
	
		//Filtrar los clasificadores del Año seleccionado
		set_por_devengar.getTab_seleccion().setSql(ser_presupuesto.getPorDevengar(tab_prcla.getValor("ide_prcla"),"1","0"));
		set_por_devengar.getTab_seleccion().ejecutarSql();
		set_por_devengar.dibujar();

	}
        
        public void aceptarPorDevengar (){
            String str_seleccionados= set_por_devengar.getSeleccionados();
            //int total_sel = set_por_devengar.getNumeroSeleccionados();
            TablaGenerica tab_prcla = utilitario.consultar("select ide_prpro,ide_prcla from pre_programa  where ide_prpro = "+tab_anual.getValor("ide_prpro"));

            TablaGenerica tab_saldos_devengar = utilitario.consultar(ser_presupuesto.getPorDevengar(tab_prcla.getValor("ide_prcla"),"2",str_seleccionados));
            if(str_seleccionados!=null){
                for(int i=0;i<tab_saldos_devengar.getTotalFilas();i++){
				tab_mensual.insertar();
				tab_mensual.setValor("fecha_ejecucion_prmen", tab_saldos_devengar.getValor(i, "fecha_trans_cnccc"));
                                tab_mensual.setValor("devengado_prmen", tab_saldos_devengar.getValor(i, "saldoxdevengar"));
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
                                tab_mensual.setValor("ide_comov", tab_saldos_devengar.getValor(i, "nro_asiento"));
				
			}
			set_por_devengar.cerrar();
                       calcularDevengado();
			utilitario.addUpdate("tab_mensual");
            }
            else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
        }
	public void aceptarPrograma(){
		String str_seleccionado=set_programa.getValorSeleccionado();
		if(str_seleccionado!=null){
			//Inserto los empleados seleccionados en la tabla de participantes 
			TablaGenerica tab_programa=ser_presupuesto.getTablaGenericaPrograma(str_seleccionado);
			//System.out.println(" tabla generica"+tab_programa.getSql());
			for(int i=0;i<tab_programa.getTotalFilas();i++){
				tab_anual.insertar();
				tab_anual.setValor("ide_prpro", tab_programa.getValor(i, "ide_prpro"));
                                tab_anual.setValor("ide_geani", com_anio.getValue().toString());
				
			}
			set_programa.cerrar();
                       
			utilitario.addUpdate("tab_anual");			
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
		}
	}
        //reporte
public void abrirListaReportes() {
        if(com_anio.getValue() == null){
            utilitario.agregarMensajeInfo("Seleccione el Año", "Seleccione el Año para poder continuar");
	
        }
        else {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
         }
}
String seleccionados="";
String actividades="";
public void aceptarReporte(){
        
            if(rep_reporte.getReporteSelecionado().equals("Presupuesto Anual Gastos")){
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
        else if(rep_reporte.getReporteSelecionado().equals("Compromisos Presupuestarios")){
                if (rep_reporte.isVisible()){
                    
                    sel_programas.getTab_seleccion().setSql(ser_presupuesto.getFuncionPrograma("1"));
                    sel_programas.getTab_seleccion().ejecutarSql();
                    sel_programas.dibujar();               
                    rep_reporte.cerrar();
                    
		}
                else if(sel_programas.isVisible()){
                    seleccionados=sel_programas.getSeleccionados();
                    sel_programas.cerrar();
                    set_actividad.getTab_seleccion().setSql(ser_presupuesto.getActividadPrograma(seleccionados));
                    set_actividad.getTab_seleccion().ejecutarSql();
                    set_actividad.dibujar(); 

                    }
                else if(set_actividad.isVisible()){
                    actividades=set_actividad.getSeleccionados();
                    set_actividad.cerrar();
                    sel_calendario.setFecha1(null);
                    sel_calendario.setFecha2(null);					
                    sel_calendario.dibujar();
                    }
                else if(sel_calendario.isVisible()){
                    
                    sel_calendario.cerrar();
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","COMPROMISO PRESUPUESTARIA");
			p_parametros.put("panio",Integer.parseInt(com_anio.getValue().toString()));
                        p_parametros.put("nombre",utilitario.getVariable("NICK"));
                        p_parametros.put("pprograma",seleccionados);
                        p_parametros.put("pactividad",actividades);
                        p_parametros.put("pfecha_inicial",sel_calendario.getFecha1String());
                        p_parametros.put("pfecha_final",sel_calendario.getFecha2String());
                        System.out.println("paso parametrios "+p_parametros);
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
                        self_reporte.dibujar();
                    }
        }
        else if(rep_reporte.getReporteSelecionado().equals("Ejecución Actividades Presupuestarias")){
                if (rep_reporte.isVisible()){
                    
                    sel_programas.getTab_seleccion().setSql(ser_presupuesto.getFuncionPrograma("1"));
                    sel_programas.getTab_seleccion().ejecutarSql();
                    sel_programas.dibujar();               
                    rep_reporte.cerrar();
                    
		}
                else if(sel_programas.isVisible()){
                    seleccionados=sel_programas.getSeleccionados();
                    sel_programas.cerrar();
                    set_actividad.getTab_seleccion().setSql(ser_presupuesto.getActividadPrograma(seleccionados));
                    set_actividad.getTab_seleccion().ejecutarSql();
                    set_actividad.dibujar(); 

                    }
                else if(set_actividad.isVisible()){
                    actividades=set_actividad.getSeleccionados();
                    set_actividad.cerrar();
                    sel_calendario.setFecha1(null);
                    sel_calendario.setFecha2(null);					
                    sel_calendario.dibujar();
                    }
                else if(sel_calendario.isVisible()){
                    
                    sel_calendario.cerrar();
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","COMPROMISO PRESUPUESTARIA");
			p_parametros.put("panio",Integer.parseInt(com_anio.getValue().toString()));
                        p_parametros.put("nombre",utilitario.getVariable("NICK"));
                        p_parametros.put("pprograma",seleccionados);
                        p_parametros.put("pactividad",actividades);
                        p_parametros.put("pfecha_inicial",sel_calendario.getFecha1String());
                        p_parametros.put("pfecha_final",sel_calendario.getFecha2String());
                        //System.out.println("paso parametrios "+p_parametros);
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
                        self_reporte.dibujar();
                    }
        }
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
       
         
		
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
			//if(validarAnual()){
			if(tab_mensual.guardar()){
				if(tab_reforma.guardar()){
					
				}
			}
		}
		guardarPantalla();
	}
	
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

    @Override
    public void atras() {
        super.atras(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void siguiente() {
        super.siguiente(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fin() {
        super.fin(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void inicio() {
        super.inicio(); //To change body of generated methods, choose Tools | Templates.
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


	public SeleccionTabla getSet_programa() {
		return set_programa;
	}


	public void setSet_programa(SeleccionTabla set_programa) {
		this.set_programa = set_programa;
	}
	public SeleccionTabla getSet_poa() {
		return set_poa;
	}
	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
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

    public Dialogo getDia_por_devengar() {
        return dia_por_devengar;
    }

    public void setDia_por_devengar(Dialogo dia_por_devengar) {
        this.dia_por_devengar = dia_por_devengar;
    }

    public SeleccionTabla getSet_por_devengar() {
        return set_por_devengar;
    }

    public void setSet_por_devengar(SeleccionTabla set_por_devengar) {
        this.set_por_devengar = set_por_devengar;
    }

    public SeleccionTabla getSel_programas() {
        return sel_programas;
    }

    public void setSel_programas(SeleccionTabla sel_programas) {
        this.sel_programas = sel_programas;
    }

    public SeleccionTabla getSet_actividad() {
        return set_actividad;
    }

    public void setSet_actividad(SeleccionTabla set_actividad) {
        this.set_actividad = set_actividad;
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_programa() {
        return tab_programa;
    }

    public void setTab_programa(Tabla tab_programa) {
        this.tab_programa = tab_programa;
    }


}

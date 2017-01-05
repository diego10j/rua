package paq_presupuesto;

import javax.ejb.EJB;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.event.NodeSelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
public class pre_programa extends Pantalla {
	
	private Tabla tab_programa=new Tabla();
	private Tabla tab_vigente=new Tabla();
	private Arbol arb_arbol=new Arbol();
	private SeleccionTabla set_clasificador=new SeleccionTabla();
	private Combo com_anio=new Combo();
        
        	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();

	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	

	public pre_programa(){
		
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

		tab_programa.setId("tab_programa");
		tab_programa.setHeader("PROGRAMA");
		tab_programa.setTabla("pre_programa", "ide_prpro", 1);
		//tab_programa.getColumna("ide_prfup").setVisible(false);
                tab_programa.getColumna("ide_prfup").setCombo("pre_funcion_programa", "ide_prfup", "codigo_prfup||' '||detalle_prfup", "");
		tab_programa.getColumna("ide_prfup").setAutoCompletar();
		tab_programa.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
                //tab_programa.getColumna("ide_prcla").setAutoCompletar();
                tab_programa.getColumna("ide_prcla").setLongitud(-1);
		tab_programa.getColumna("ide_prcla").setLectura(true);
		tab_programa.getColumna("activo_prpro").setValorDefecto("true");
		tab_programa.agregarRelacion(tab_vigente);
                tab_programa.setTipoFormulario(true);
		tab_programa.getGrid().setColumns(2);
		tab_programa.dibujar();
		PanelTabla pat_progama=new PanelTabla();
		pat_progama.setPanelTabla(tab_programa);
		
		///vigente
		tab_vigente.setId("tab_vigente");
		tab_vigente.setHeader("VIGENTE");
		tab_vigente.setTabla("cont_vigente", "ide_covig", 2);
		tab_vigente.setCondicion("ide_covig=-1");
		tab_vigente.getColumna("ide_prcla").setVisible(false);
		tab_vigente.getColumna("ide_prasp").setVisible(false);
		tab_vigente.getColumna("ide_cocac").setVisible(false);
		tab_vigente.getColumna("ide_prfup").setVisible(false);
		tab_vigente.getColumna("ide_geani").setAutoCompletar();
		tab_vigente.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false", "false,true"));
		tab_vigente.getColumna("ide_geani").setLectura(true);
		tab_vigente.getColumna("activo_covig").setValorDefecto("true");
		tab_vigente.dibujar();
		PanelTabla pat_vigente=new PanelTabla();
		pat_vigente.setPanelTabla(tab_vigente);
		

				Division div3 = new Division(); //UNE OPCION Y DIV 2
				div3.dividir2(pat_progama, pat_vigente, "30%", "H");
				
				agregarComponente(div3);
				///clasificador
				
				Boton bot_agregar=new Boton();
				bot_agregar.setValue("Agregar Clasificador");
				bot_agregar.setMetodo("agregarClasificador");
				bar_botones.agregarBoton(bot_agregar);

				set_clasificador.setId("set_clasificador");
				set_clasificador.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
				set_clasificador.setRadio(); //solo selecciona una opcion
				set_clasificador.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestarioAnio("true", "-1","0"), "ide_prcla"); 
				set_clasificador.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido(); //pone filtro
				set_clasificador.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();//pone filtro
				set_clasificador.getBot_aceptar().setMetodo("aceptarClasificador");
				agregarComponente(set_clasificador);


	}
	
	public void seleccionar_arbol(NodeSelectEvent evt) {
		
		arb_arbol.seleccionarNodo(evt);
		tab_programa.setCondicion("ide_prfup="+arb_arbol.getValorSeleccionado());
		tab_programa.ejecutarSql();		
		tab_programa.getColumna("IDE_PRFUP").setValorDefecto(arb_arbol.getValorSeleccionado());
		tab_vigente.ejecutarValorForanea(tab_programa.getValorSeleccionado());
		
		
	}

////clasificador agregar y aceptar

	
	public void agregarClasificador(){
		//si no selecciono ningun valor en el combo
                
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
                
		//Si la tabla esta vacia
		if(tab_programa.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede agregar Clasificador, por que no existen registros", "");
			return;
		}
		//Filtrar los clasificadores del Año seleccionado
		set_clasificador.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestarioAnio("true",com_anio.getValue().toString(),"0"));
		set_clasificador.getTab_seleccion().ejecutarSql();
		set_clasificador.dibujar();
	}

	public void aceptarClasificador(){
		if(set_clasificador.getValorSeleccionado()!=null){
			tab_programa.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
			//Actualiza 
			utilitario.addUpdate("tab_programa");//actualiza mediante ajax el objeto tab_poa
			set_clasificador.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}
///METODO AñO
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_vigente.setCondicion("ide_geani="+com_anio.getValue());
			tab_vigente.ejecutarSql();
			//tab_mes.ejecutarValorForanea(tab_poa.getValorSeleccionado());

		}
		else{
			utilitario.agregarMensajeInfo("Selecione un Año", "");

		}
	}

//reporte
public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}
public void aceptarReporte(){
	if(rep_reporte.getReporteSelecionado().equals("Programas Presupuestarios"));{
		if (rep_reporte.isVisible()){
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");

			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
			
		self_reporte.dibujar();
		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}
		
}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub

		if (tab_programa.isFocus()) {
			tab_programa.insertar();

		}
		else if (tab_vigente.isFocus()) {
			tab_vigente.insertar();
			tab_vigente.setValor("ide_geani", com_anio.getValue()+"");
            utilitario.addUpdateTabla(tab_vigente, "ide_geani", "");

		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		String funcion=tab_vigente.getValor("ide_prfup");
		TablaGenerica consultaFuncion=utilitario.consultar("select ide_prfup from pre_funcion_programa where ide_prfup="+funcion);
		if(tab_programa.guardar()){
			if(tab_vigente.guardar()){
				
				guardarPantalla();
			}
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}



	public Tabla getTab_programa() {
		return tab_programa;
	}



	public void setTab_programa(Tabla tab_programa) {
		this.tab_programa = tab_programa;
	}



	public Tabla getTab_vigente() {
		return tab_vigente;
	}



	public void setTab_vigente(Tabla tab_vigente) {
		this.tab_vigente = tab_vigente;
	}

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
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
	

}

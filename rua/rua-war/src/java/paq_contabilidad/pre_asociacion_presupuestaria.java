package paq_contabilidad;

import javax.ejb.EJB;
import java.util.HashMap;
import java.util.Map;
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
public class pre_asociacion_presupuestaria extends Pantalla {

	private Tabla tab_asociacion_presupuestaria =new Tabla();
	private Tabla tab_vigente =new Tabla();
	private Tabla tab_tipo_catalogo_cuenta = new Tabla();
	private Combo com_anio =new Combo();
	private SeleccionTabla set_clasificador=new SeleccionTabla();
	private SeleccionTabla set_catalogo=new SeleccionTabla();
	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto ) utilitario.instanciarEJB(ServicioPresupuesto.class);



	public pre_asociacion_presupuestaria (){
		
            
            		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
                
                
		tab_asociacion_presupuestaria.setId("tab_asociacion_presupuestaria");
		tab_asociacion_presupuestaria.setHeader("ASOCIACION PRESUPUESTARIA");
		tab_asociacion_presupuestaria.setTabla("pre_asociacion_presupuestaria", "ide_prasp", 1);
		tab_asociacion_presupuestaria.getColumna("ide_prcla").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_asociacion_presupuestaria.getColumna("ide_prcla").setAutoCompletar();
		tab_asociacion_presupuestaria.getColumna("devengado").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_asociacion_presupuestaria.getColumna("devengado").setAutoCompletar();
		tab_asociacion_presupuestaria.getColumna("pagado").setCombo(ser_presupuesto.getCatalogoPresupuestario("true,false"));
		tab_asociacion_presupuestaria.getColumna("pagado").setAutoCompletar();	
		tab_asociacion_presupuestaria.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable("true,false"));
		tab_asociacion_presupuestaria.getColumna("ide_cocac").setAutoCompletar();
		tab_asociacion_presupuestaria.getColumna("ide_cnlap").setCombo("con_lugar_aplicac","ide_cnlap","nombre_cnlap","");
		tab_asociacion_presupuestaria.getColumna("ide_prmop").setCombo("pre_movimiento_presupuestario","ide_prmop","detalle_prmop","");

		tab_asociacion_presupuestaria.dibujar();
		PanelTabla pat_asociacion_presupuestaria=new PanelTabla();
		pat_asociacion_presupuestaria.setPanelTabla(tab_asociacion_presupuestaria);

		Division div_Division=new Division();
		div_Division.setId("div_Division");
		div_Division.dividir1(pat_asociacion_presupuestaria);
		agregarComponente(div_Division);
		
		Boton bot_agregar=new Boton();
		bot_agregar.setValue("Agregar Clasificador");
		bot_agregar.setMetodo("agregarClasificador");
		bar_botones.agregarBoton(bot_agregar);

		set_clasificador.setId("set_clasificador");
		set_clasificador.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
		set_clasificador.setRadio(); //solo selecciona una opcion
		set_clasificador.setSeleccionTabla(ser_presupuesto.getCatalogoPresupuestario("true"), "ide_prcla"); 
		set_clasificador.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltroContenido(); //pone filtro
		set_clasificador.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltroContenido();//pone filtro
		set_clasificador.getBot_aceptar().setMetodo("aceptarClasificador");
		agregarComponente(set_clasificador);
	
		Boton bot_cuenta=new Boton();
		bot_cuenta.setValue("Agregar Catalogo Cuenta");
		bot_cuenta.setMetodo("agregarCatalogoCuenta");
		bar_botones.agregarBoton(bot_cuenta);

		set_catalogo.setId("set_catalogo");
		set_catalogo.setTitle("SELECCIONE UN CATALOGO DE CUENTA");
		set_catalogo.setRadio(); //solo selecciona una opcion
		set_catalogo.setSeleccionTabla(ser_contabilidad.servicioCatalogoCuentaAnio("true", "-1"), "ide_cocac"); 
		set_catalogo.getBot_aceptar().setMetodo("aceptarCatalogoCuenta");
		agregarComponente(set_catalogo);


	}
	public void agregarClasificador(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un A�o", "");
			return;
		}
		//Si la tabla esta vacia
		if(tab_asociacion_presupuestaria.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede agregar Clasificador, por que no existen registros", "");
			return;
		}
		//Filtrar los clasificadores del a�o seleccionado
		set_clasificador.getTab_seleccion().setSql(ser_presupuesto.getCatalogoPresupuestario("true"));
		set_clasificador.getTab_seleccion().ejecutarSql();
		set_clasificador.dibujar();
	}

	public void aceptarClasificador(){
		if(set_clasificador.getValorSeleccionado()!=null){
			tab_asociacion_presupuestaria.setValor("ide_prcla", set_clasificador.getValorSeleccionado());
			//Actualiza 
			utilitario.addUpdate("tab_asociacion_presupuestaria");//actualiza mediante ajax el objeto tab_poa
			set_clasificador.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Clasificador", "");
		}
	}
	public void agregarCatalogoCuenta(){
		//si no selecciono ningun valor en el combo
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un A�o", "");
			return;
		}
		//Si la tabla esta vacia
		if(tab_asociacion_presupuestaria.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede agregar Catalogo de Cuenta, por que no existen registros", "");
			return;
		}
		//Filtrar los clasificadores del a�o seleccionado
		set_catalogo.getTab_seleccion().setSql(ser_contabilidad.servicioCatalogoCuentaAnio("true",com_anio.getValue().toString()));
		set_catalogo.getTab_seleccion().ejecutarSql();
		set_catalogo.dibujar();
	}

	public void aceptarCatalogoCuenta(){
		if(set_catalogo.getValorSeleccionado()!=null){
			tab_asociacion_presupuestaria.setValor("ide_cocac", set_catalogo.getValorSeleccionado());
			//Actualiza 
			utilitario.addUpdate("tab_asociacion_presupuestaria");//actualiza mediante ajax el objeto tab_poa
			set_catalogo.cerrar();
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Catalogo Cuenta", "");
		}
	}
	public void seleccioneElAnio (){
		
			tab_vigente.setCondicion("not ide_prasp is null and ide_geani="+com_anio.getValue());
			tab_vigente.ejecutarSql();
	
		}
//reporte
public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}
public void aceptarReporte(){
	if(rep_reporte.getReporteSelecionado().equals("Asociacion Presupuestaria"));{
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
		
		if (tab_asociacion_presupuestaria.isFocus()) {
			tab_asociacion_presupuestaria.insertar();
			tab_asociacion_presupuestaria.setValor("ide_prcla", set_clasificador.getValorSeleccionado()+"");


		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_asociacion_presupuestaria.guardar();
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_asociacion_presupuestaria.eliminar();


	}

	public Tabla getTab_asociacion_presupuestaria() {
		return tab_asociacion_presupuestaria;
	}

	public void setTab_asociacion_presupuestaria(Tabla tab_asociacion_presupuestaria) {
		this.tab_asociacion_presupuestaria = tab_asociacion_presupuestaria;
	}


	public Tabla getTab_tipo_catalogo_cuenta() {
		return tab_tipo_catalogo_cuenta;
	}

	public void setTab_tipo_catalogo_cuenta(Tabla tab_tipo_catalogo_cuenta) {
		this.tab_tipo_catalogo_cuenta = tab_tipo_catalogo_cuenta;
	}


	public Tabla getTab_vigente() {
		return tab_vigente;
	}

	public void setTab_vigente(Tabla tab_vigente) {
		this.tab_vigente = tab_vigente;
	}
	public SeleccionTabla getSet_clasificador() {
		return set_clasificador;
	}
	public void setSet_clasificador(SeleccionTabla set_clasificador) {
		this.set_clasificador = set_clasificador;
	}
	public SeleccionTabla getSet_catalogo() {
		return set_catalogo;
	}
	public void setSet_catalogo(SeleccionTabla set_catalogo) {
		this.set_catalogo = set_catalogo;
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

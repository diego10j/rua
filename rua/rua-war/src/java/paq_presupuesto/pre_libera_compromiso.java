package paq_presupuesto;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;

public class pre_libera_compromiso extends Pantalla{
	private Tabla tab_libera_compromiso=new Tabla();
	private Tabla tab_detalle_libera=new Tabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
	
	 @EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
		       
	public pre_libera_compromiso(){
		tab_libera_compromiso.setId("tab_libera_compromiso");
		tab_libera_compromiso.setHeader("LIBERACION COMPROMISO");
		tab_libera_compromiso.setTabla("pre_libera_compromiso", "ide_prlic", 1);
		tab_libera_compromiso.getColumna("IDE_PRTRA").setCombo(ser_presupuesto.getTramite("true"));
		tab_libera_compromiso.getColumna("IDE_PRTRA").setAutoCompletar();
		tab_libera_compromiso.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_libera_compromiso.getColumna("activo_prlic").setValorDefecto("true");
		tab_libera_compromiso.agregarRelacion(tab_detalle_libera);
		tab_libera_compromiso.setTipoFormulario(true);
		tab_libera_compromiso.getGrid().setColumns(2);
		tab_libera_compromiso.dibujar();
		PanelTabla pat_libera=new PanelTabla();
		pat_libera.setPanelTabla(tab_libera_compromiso);
		
		//////detalle libera compromiso
		
		tab_detalle_libera.setId("tab_detalle_libera");
		tab_detalle_libera.setHeader("DETALLE LIBERA COMROMISO");
		tab_detalle_libera.setTabla("pre_detalle_libera_compro", "ide_prdlc", 2);
		tab_detalle_libera.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoa("select ide_geani from gen_anio","true,false","true,false"));
		tab_detalle_libera.getColumna("ide_prpoa").setAutoCompletar();
		tab_detalle_libera.getColumna("ide_prpoa").setLectura(true);
		tab_detalle_libera.getColumna("activo_prdlc").setValorDefecto("true");
		tab_detalle_libera.dibujar();
		PanelTabla pat_detalle=new PanelTabla();
		pat_detalle.setPanelTabla(tab_detalle_libera);
		
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_libera, pat_detalle, "50%", "H");
		agregarComponente(div_divi);
		/////boton buscar poa
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("importarPoa");
		bar_botones.agregarBoton(bot_buscar);

		set_poa.setId("set_poa");
	//	set_poa.setSeleccionTabla(ser_presupuesto.getPoa("true,false"),"ide_prpoa");
		set_poa.setTitle("Seleccione Poa");
		set_poa.getBot_aceptar().setMetodo("aceptarPoa");
		agregarComponente(set_poa);

		
		
	}
	
	////importar poa
	public void importarPoa(){
		System.out.println(" ingresar al importar");

	//	set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa("true,false"));
		set_poa.getTab_seleccion().ejecutarSql();
		set_poa.dibujar();

	}

	public  void aceptarPoa(){
		String str_seleccionados = set_poa.getSeleccionados();

		if (str_seleccionados!=null){
			TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaPoa(str_seleccionados);		
			for(int i=0;i<tab_poa.getTotalFilas();i++){
				tab_detalle_libera.insertar();
				tab_detalle_libera.setValor("ide_prpoa", tab_poa.getValor(i, "ide_prpoa"));
			}
			set_poa.cerrar();
			utilitario.addUpdate("tab_detalle_libera");
		}
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(tab_libera_compromiso.isFocus()){
			tab_libera_compromiso.insertar();
		}
		else if(tab_detalle_libera.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe ingresar un poa");
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_libera_compromiso.guardar()){
			if(tab_detalle_libera.guardar()){
				guardarPantalla();
				
			}
			
		}
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_libera_compromiso() {
		return tab_libera_compromiso;
	}

	public void setTab_libera_compromiso(Tabla tab_libera_compromiso) {
		this.tab_libera_compromiso = tab_libera_compromiso;
	}

	public Tabla getTab_detalle_libera() {
		return tab_detalle_libera;
	}

	public void setTab_detalle_libera(Tabla tab_detalle_libera) {
		this.tab_detalle_libera = tab_detalle_libera;
	}

	public SeleccionTabla getSet_poa() {
		return set_poa;
	}

	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}

}

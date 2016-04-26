package paq_general;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_grupo_cargo_departamento extends Pantalla {

	private Tabla tab_grupo_cargo=new Tabla();
	private Tabla tab_grupo_cargo_area=new Tabla();
	private Tabla tab_grupo_cargo_supervisa=new Tabla();
	private SeleccionTabla sel_tab_grupo_cargo_supervisa=new SeleccionTabla();	
	
	
	@EJB
	private ServicioEmpleado ser_empleado=(ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina=(ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	public pre_grupo_cargo_departamento() {

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		
		//  GRUPO CARGO (division 1)

		tab_grupo_cargo.setId("tab_grupo_cargo");
		tab_grupo_cargo.setSql("SELECT b.ide_gecaf,b.detalle_gecaf,c.ide_gegro,c.detalle_gegro,a.activo_gegrc FROM gen_grupo_cargo a, gen_cargo_funcional b , gen_grupo_ocupacional c " +
				"where a.ide_gecaf=b.ide_gecaf and a.ide_gegro=c.ide_gegro");
		tab_grupo_cargo.setClaveCompuesta("IDE_GECAF,IDE_GEGRO");
		tab_grupo_cargo.setNumeroTabla(1);
		tab_grupo_cargo.getColumna("ACTIVO_GEGRC").setCheck();
		tab_grupo_cargo.getColumna("ACTIVO_GEGRC").setValorDefecto("true");			
		tab_grupo_cargo.agregarRelacion(tab_grupo_cargo_area);
		tab_grupo_cargo.agregarRelacion(tab_grupo_cargo_supervisa);		
		tab_grupo_cargo.setLectura(true);
		tab_grupo_cargo.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_grupo_cargo);
		tab_grupo_cargo.setHeader("GRUPO CARGO");

		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		//  RECETA MEDICA (TABULADOR 1)
		tab_grupo_cargo_area.setId("tab_grupo_cargo_area");
		tab_grupo_cargo_area.setIdCompleto("tab_tabulador:tab_grupo_cargo_area");
		tab_grupo_cargo_area.setTabla("GEN_GRUPO_CARGO_AREA", "IDE_GEGCA", 2);
		tab_grupo_cargo_area.getColumna("ACTIVO_GEGCA").setCheck();
		tab_grupo_cargo_area.getColumna("ACTIVO_GEGCA").setValorDefecto("true");
		tab_grupo_cargo_area.getColumna("IDE_GEGRO").setVisible(false);
		tab_grupo_cargo_area.getColumna("IDE_GEDEP").setCombo("SELECT a.ide_gedep,b.detalle_geare,a.detalle_gedep FROM gen_departamento a, gen_area b " +
				"where a.ide_geare=b.ide_geare order by b.detalle_geare,a.detalle_gedep");
		tab_grupo_cargo_area.getColumna("IDE_GEDEP").setAutoCompletar();
		tab_grupo_cargo_area.getGrid().setColumns(6);
		tab_grupo_cargo_area.setTipoFormulario(true);
		tab_grupo_cargo_area.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_grupo_cargo_area);

		// FICHA CARGO SUPERVISA (TABULADOR 2)

		tab_grupo_cargo_supervisa.setId("tab_grupo_cargo_supervisa");
		tab_grupo_cargo_supervisa.setIdCompleto("tab_tabulador:tab_grupo_cargo_supervisa");
		tab_grupo_cargo_supervisa.setTabla("GEN_GRUPO_CARGO_SUPERVISA", "IDE_GEGCS", 3);
		tab_grupo_cargo_supervisa.getColumna("ACTIVO_GEGCS").setCheck();
		tab_grupo_cargo_supervisa.getColumna("ACTIVO_GEGCS").setValorDefecto("true");
		tab_grupo_cargo_supervisa.getColumna("IDE_GEGRO").setVisible(false);
		tab_grupo_cargo_supervisa.getColumna("GEN_IDE_GEGRO").setCombo("SELECT ide_gegro,detalle_gegro FROM gen_grupo_ocupacional");
		tab_grupo_cargo_supervisa.imprimirSql();
		tab_grupo_cargo_supervisa.getColumna("GEN_IDE_GEGRO").setAutoCompletar();
		tab_grupo_cargo_supervisa.getColumna("GEN_IDE_GEGRO").setMetodoChange("filtrarCargoFuncional");
		tab_grupo_cargo_supervisa.getColumna("GEN_IDE_GECAF").setCombo("SELECT ide_gecaf,detalle_gecaf FROM gen_cargo_funcional");
		tab_grupo_cargo_supervisa.getColumna("GEN_IDE_GECAF").setAutoCompletar();
		tab_grupo_cargo_supervisa.getGrid().setColumns(4);
		tab_grupo_cargo_supervisa.setTipoFormulario(true);
		tab_grupo_cargo_supervisa.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_grupo_cargo_supervisa);
//		pat_panel3.getMenuTabla().getItem_insertar().setRendered(true);
//		pat_panel3.getMenuTabla().getItem_insertar().setValue("Insertar Grupos Cargos Supervision");
//		pat_panel3.getMenuTabla().getItem_insertar().setMetodo("insertarGrupoCargoSupervisa");
//		

	
		tab_tabulador.agregarTab("GRUPO CARGO AREA", pat_panel2);
		tab_tabulador.agregarTab("GRUPO CARGO SUPERVISA", pat_panel3);
		
		//  DIVISION DE LA PANTALLA
		Division div_division=new Division();
		div_division.dividir2(pat_panel1,tab_tabulador,"60%","H");
		agregarComponente(div_division);
		
		sel_tab_grupo_cargo_supervisa.setId("sel_tab_grupo_cargo_supervisa");
		sel_tab_grupo_cargo_supervisa.setTitle("Selección de Grupo Cargo Supervisa");
		sel_tab_grupo_cargo_supervisa.setSeleccionTabla("SELECT b.ide_gecaf,b.detalle_gecaf,c.ide_gegro,c.detalle_gegro,a.activo_gegrc FROM gen_grupo_cargo a, gen_cargo_funcional b , gen_grupo_ocupacional c " +
				"where a.ide_gecaf=b.ide_gecaf and a.ide_gegro=c.ide_gegro","ide_gecaf");
		sel_tab_grupo_cargo_supervisa.getBot_aceptar().setMetodo("aceptarGrupoCargoSupervisa");
		agregarComponente(sel_tab_grupo_cargo_supervisa);

	}


	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_grupo_cargo.isFocus()){						
					tab_grupo_cargo.insertar();		
				}
		else if (tab_grupo_cargo_area.isFocus()){			
				if (tab_grupo_cargo.getTotalFilas()>0){
					tab_grupo_cargo_area.insertar();
					tab_grupo_cargo_area.setValor("IDE_GEGRO",tab_grupo_cargo.getValor("IDE_GEGRO"));
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Ficha Medica");				
				}						
		}
		else if (tab_grupo_cargo_supervisa.isFocus()){			
				if (tab_grupo_cargo.getTotalFilas()>0){
					tab_grupo_cargo_supervisa.insertar();
					tab_grupo_cargo_supervisa.setValor("IDE_GEGRO", tab_grupo_cargo.getValor("IDE_GEGRO"));
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Primero debe insertar un Ficha Medica");				
				}					
		}
	}

	@Override
	public void guardar() {				
//					if(validarFichaMedica()){
						if (tab_grupo_cargo.guardar()){	
//							if(validarRecetaMedica()){
								if(tab_grupo_cargo_area.guardar()){					
//									if(validarDiagnostico()){
										if (tab_grupo_cargo_supervisa.guardar()){																									
											guardarPantalla();	
											utilitario.addUpdateTabla(tab_grupo_cargo_supervisa, "GEN_IDE_GECAF", "");
										}
//									}
								}
//							}
						}
//					}							
	}
	

	@Override
	public void eliminar() {				
			if (tab_grupo_cargo.isFocus()){	
				tab_grupo_cargo.eliminar();							
			}else if(tab_grupo_cargo_area.isFocus()){
				tab_grupo_cargo_area.eliminar();
			}
			else if(tab_grupo_cargo_supervisa.isFocus()){
				tab_grupo_cargo_supervisa.eliminar();
			}
			else if(tab_grupo_cargo_area.isFocus()){
				tab_grupo_cargo_area.eliminar();
			}					
	}
	
	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public Tabla gettab_grupo_cargo() {
		return tab_grupo_cargo;
	}

	public void settab_grupo_cargo(Tabla tab_grupo_cargo) {
		this.tab_grupo_cargo = tab_grupo_cargo;
	}
	
	public Tabla getTab_grupo_cargo() {
		return tab_grupo_cargo;
	}

	public void setTab_grupo_cargo(Tabla tab_grupo_cargo) {
		this.tab_grupo_cargo = tab_grupo_cargo;
	}

	public Tabla getTab_grupo_cargo_area() {
		return tab_grupo_cargo_area;
	}

	public void setTab_grupo_cargo_area(Tabla tab_grupo_cargo_area) {
		this.tab_grupo_cargo_area = tab_grupo_cargo_area;
	}

	public Tabla gettab_grupo_cargo_supervisa() {
		return tab_grupo_cargo_supervisa;
	}

	public void settab_grupo_cargo_supervisa(Tabla tab_grupo_cargo_supervisa) {
		this.tab_grupo_cargo_supervisa = tab_grupo_cargo_supervisa;
	}
	
	public SeleccionTabla getSel_tab_grupo_cargo_supervisa() {
		return sel_tab_grupo_cargo_supervisa;
	}


	public void setSel_tab_grupo_cargo_supervisa(
			SeleccionTabla sel_tab_grupo_cargo_supervisa) {
		this.sel_tab_grupo_cargo_supervisa = sel_tab_grupo_cargo_supervisa;
	}


	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {	
	}
	
//	public boolean validarFichaMedica(){
//		if(tab_grupo_cargo.isFilaInsertada()){
//			if(tab_grupo_cargo.getValor("IDE_SATIC")==null || tab_grupo_cargo.getValor("IDE_SATIC").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Tipo de Consulta ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("PESO_SAFIM")==null || tab_grupo_cargo.getValor("PESO_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Peso ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("TEMPERATURA_SAFIM")==null || tab_grupo_cargo.getValor("TEMPERATURA_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Temperatura ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("FRE_RESPIRATORIA_SAFIM")==null || tab_grupo_cargo.getValor("FRE_RESPIRATORIA_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar  la Frecuencia Respiratoria ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("ANIO_EDAD_SAFIM")==null || tab_grupo_cargo.getValor("ANIO_EDAD_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Año de Edad ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("TALLA_SAFIM")==null || tab_grupo_cargo.getValor("TALLA_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Talla ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("PULSO_SAFIM")==null || tab_grupo_cargo.getValor("PULSO_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Pulso ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("FRE_CARDIACA_SAFIM")==null || tab_grupo_cargo.getValor("FRE_CARDIACA_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Frecuencia Cardiaca ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("ACTIVO_SAFIM")==null || tab_grupo_cargo.getValor("ACTIVO_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Estado Activo");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("IDE_SAESP")==null || tab_grupo_cargo.getValor("IDE_SAESP").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Tipo de Especialidad");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("MES_EDAD_SAFIM")==null || tab_grupo_cargo.getValor("MES_EDAD_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Mes");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("IMC_SAFIM")==null || tab_grupo_cargo.getValor("IMC_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el IMC ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("SUPERFICIE_CORPORAL_SAFIM")==null || tab_grupo_cargo.getValor("SUPERFICIE_CORPORAL_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Superficie Corporal ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("FECHA_CONSULTA_SAFIM")==null || tab_grupo_cargo.getValor("FECHA_CONSULTA_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha de Consulta ");
//				return false;
//			}
//			if(tab_grupo_cargo.getValor("OBSERVACIONES_SAFIM")==null || tab_grupo_cargo.getValor("OBSERVACIONES_SAFIM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Observacion");
//				return false;
//			}
//			if (utilitario.isFechaMenor(utilitario.getFecha(tab_grupo_cargo.getValor("FECHA_HASTA_CERT_SAFIM")), utilitario.getFecha(tab_grupo_cargo.getValor("FECHA_DESDE_CERT_SAFIM")))){
//				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha hasta no puede ser menor que la fecha desde");
//				return false;
//			}
//		}
//		return true;
//	}
//
//	public boolean validarRecetaMedica(){
//		if(tab_grupo_cargo_area.isFilaInsertada()){
//			if(tab_grupo_cargo_area.getValor("IDE_SAMED")==null || tab_grupo_cargo_area.getValor("IDE_SAMED").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar La Medicación ");
//				return false;
//			}
//			if(tab_grupo_cargo_area.getValor("CANTIDAD_SAREM")==null || tab_grupo_cargo_area.getValor("CANTIDAD_SAREM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Cantidad ");
//				return false;
//			}
//			if(tab_grupo_cargo_area.getValor("INDICACION_SAREM")==null || tab_grupo_cargo_area.getValor("INDICACION_SAREM").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Indicación ");
//				return false;
//			}
//		}
//		return true;
//	}
//	public boolean validarDiagnostico(){
//		if(tab_grupo_cargo_supervisa.isFilaInsertada()){
//			if(tab_grupo_cargo_supervisa.isFilaInsertada()){
//				if(tab_grupo_cargo_supervisa.getValor("IDE_SARED")==null || tab_grupo_cargo_supervisa.getValor("IDE_SARED").isEmpty()){
//					utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Registro del Diagnostico ");
//					return false;
//				}
//			}
//		}
//		return true;		
//	}
//	public boolean validarExamenes(){
//		if(tab_grupo_factor.isFilaInsertada()){
//			if(tab_grupo_factor.getValor("IDE_SAEXA")==null || tab_grupo_factor.getValor("IDE_SAEXA").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar los Examenes ");
//				return false;
//			}
//		}
//		return true;		
//	}
//	public boolean validarMotivoConsulta(){
//		if(tab_grupo_minimo_seleccion.isFilaInsertada()){
//			if(tab_grupo_minimo_seleccion.getValor("IDE_SAMOC")==null || tab_grupo_minimo_seleccion.getValor("IDE_SAMOC").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar El Motivo de la Consulta ");
//				return false;
//			}	
//		}
//		return true;		
//	}
//
//	public boolean validarAnamnesis(){
//		if(tab_ficha_anamnesis.isFilaInsertada()){
//			if(tab_ficha_anamnesis.getValor("IDE_SAANA")==null || tab_ficha_anamnesis.getValor("IDE_SAANA").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Anamnesis ");
//				return false;
//			}
//			if(tab_ficha_anamnesis.getValor("DETALLE_SAFIA")==null || tab_ficha_anamnesis.getValor("DETALLE_SAFIA").isEmpty()){
//				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar El Detalle de la Anamnesis ");
//				return false;
//			}
//		}
//		return true;		
//	}
	
	public void filtrarCargoFuncional(){
		if(tab_grupo_cargo_supervisa.getValor("IDE_GEGRO")!=null && !tab_grupo_cargo_supervisa.getValor("IDE_GEGRO").isEmpty()){
			if(tab_grupo_cargo_supervisa.isFilaInsertada()){
				System.out.println("GEN_IDE_GEGRO----"+tab_grupo_cargo_supervisa.getValor("GEN_IDE_GEGRO"));
				System.out.println("pasa el if de aquiii   SI INGRESA AL METODO filtrarCargoFuncional");
				tab_grupo_cargo_supervisa.getColumna("GEN_IDE_GECAF").setCombo("SELECT ide_gecaf,detalle_gecaf FROM gen_cargo_funcional where ide_gecaf in (select ide_gecaf from gen_grupo_cargo where ide_gegro="+tab_grupo_cargo_supervisa.getValor("GEN_IDE_GEGRO")+")");
				tab_grupo_cargo_supervisa.getColumna("GEN_IDE_GECAF").setAutoCompletar();		
				//			tab_grupo_cargo_supervisa.ejecutarSql();	
				tab_grupo_cargo_supervisa.imprimirSql();
			}		
		}
		utilitario.addUpdateTabla(tab_grupo_cargo_supervisa, "GEN_IDE_GECAF", "");
	}
	
	public void insertarGrupoCargoSupervisa(){
		System.out.println("si ingresa insertarGrupoCargoSupervisa");
		sel_tab_grupo_cargo_supervisa.dibujar();
	}
	public void aceptarGrupoCargoSupervisa(){
		System.out.println("si ingresa aceptarGrupoCargoSupervisa ------------");
		if(sel_tab_grupo_cargo_supervisa.getSeleccionados()!=null & !sel_tab_grupo_cargo_supervisa.getSeleccionados().isEmpty()){
			System.out.println("si ingresa aceptarGrupoCargoSupervisa ------------ pas el if");
			System.out.println("valor del sel tab_  "+sel_tab_grupo_cargo_supervisa.getSeleccionados());
//			for (int i = 0; i < tab_grupo_cargo_supervisa.getTotalFilas(); i++) {
//				tab_grupo_cargo_supervisa.insertar();
//				tab_grupo_cargo_supervisa.setValor("GEN_IDE_GEGRO", sel_tab_grupo_cargo_supervisa.getTab_seleccion().getValor(i, "IDE_GEGRO"));
//				tab_grupo_cargo_supervisa.setValor("GEN_IDE_GECAF", sel_tab_grupo_cargo_supervisa.getTab_seleccion().getValor(i, "IDE_GECAF"));
//			}
			
			
		}else{
			utilitario.agregarMensajeInfo("No se puede continuar", "Seleccione al menos un Grupo Cargo Supervisa");
		}
	}
}



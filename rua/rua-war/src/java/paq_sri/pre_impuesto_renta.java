/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import javax.ejb.EJB;
import paq_gestion.ejb.ServicioEmpleado;
import sistema.aplicacion.Pantalla;
import paq_nomina.ejb.ServicioSRI;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.reportes.ReporteDataSource;
/**
 *
 * @author Diego
 */
public class pre_impuesto_renta extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private Tabla tab_tabla3 = new Tabla();

	@EJB
	private ServicioSRI ser_sri = (ServicioSRI) utilitario.instanciarEJB(ServicioSRI.class);

	@EJB
	private ServicioEmpleado ser_empleados = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);

	private SeleccionTabla set_empleado=new SeleccionTabla();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();	
	private SeleccionCalendario sec_fecha_entrega=new SeleccionCalendario();

	public pre_impuesto_renta() {

		Boton bot_rdep=new Boton();
		bot_rdep.setValue("Generar Anexo RDEP");
		bot_rdep.setAjax(false);
		bot_rdep.setMetodo("generarRDEP");
		//bar_botones.agregarBoton(bot_rdep);

		bar_botones.agregarReporte();
		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("SRI_IMPUESTO_RENTA", "IDE_SRIMR", 1);
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.agregarRelacion(tab_tabla3);
		tab_tabla1.getColumna("ACTIVO_SRIMR").setCheck();
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);


		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("SRI_DETALLE_IMPUESTO_RENTA", "IDE_SRDIR", 2);
		tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);

		tab_tabulador.agregarTab("TABLA DE IMPUESTO A LA RENTA", pat_panel2);


		tab_tabla3.setId("tab_tabla3");
		tab_tabla3.setTabla("SRI_DEDUCIBLES", "IDE_SRDED", 3);
		tab_tabla3.getColumna("ACTIVO_SRDED").setCheck();        
		tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
		tab_tabla3.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_tabla3);       

		tab_tabulador.agregarTab("GASTOS PERSONABLES DEDUCIBLES", pat_panel3);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, tab_tabulador, "40%", "H");
		agregarComponente(div_division);

		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla(ser_empleados .getSQLEmpleadosActivos(), "IDE_GTEMP");
		set_empleado.getTab_seleccion().getColumna("NOMBRES ").setFiltro(true);		
		set_empleado.setTitle("SELECCION DE EMPLEADOS");		
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_empleado);


		sec_fecha_entrega.setId("sec_fecha_entrega");
		sec_fecha_entrega.setMultiple(false);
		sec_fecha_entrega.setTitle("Fecha de entrega del Formulario");
		sec_fecha_entrega.setFechaActual();
		sec_fecha_entrega.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sec_fecha_entrega);
		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		agregarComponente(rep_reporte);
	}

	/**
	 * Genera el Anexo RDEP del periodo seleccionado
	 */
	public void generarRDEP(){
		String str_archivo	=ser_sri.generarAnexoRDEP(tab_tabla1.getValor("IDE_SRIMR"));
		if(str_archivo!=null){
			utilitario.crearArchivo(str_archivo);
		}
		

	}

	@Override
	public void aceptarReporte() {
		if(!tab_tabla1.isEmpty()){			
			if (rep_reporte.getReporteSelecionado().equals("Formulario 107")){
				if(rep_reporte.isVisible()){
					sec_fecha_entrega.dibujar();
					rep_reporte.cerrar();
				}
				else if(sec_fecha_entrega.isVisible()){					
					sec_fecha_entrega.cerrar();
					set_empleado.dibujar();
				}
				else if(set_empleado.isVisible()){
					String str_seleccionados=set_empleado.getSeleccionados();
					System.out.printf("Esto me selecciona:"+str_seleccionados);
					if(str_seleccionados==null || str_seleccionados.isEmpty()){
						utilitario.agregarMensajeInfo("Debe seleccionar almenos un empleado", "");
					}
					else{
						sef_reporte.setSeleccionFormatoReporte(null, rep_reporte.getPath(),new ReporteDataSource(ser_sri.getFormulario107(str_seleccionados, tab_tabla1.getValor("IDE_SRIMR"), sec_fecha_entrega.getFecha1String())));						
						set_empleado.cerrar();
						sef_reporte.dibujar();
					}
				}
			}
		}
		else{
			utilitario.agregarMensajeInfo("Debe configurar un per�odo de impuesto a la renta", "");
		}		
	}

	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}


	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		tab_tabla1.guardar();
		tab_tabla2.guardar();
		tab_tabla3.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}

	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}

	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}

	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}
	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
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

	public SeleccionCalendario getSec_fecha_entrega() {
		return sec_fecha_entrega;
	}

	public void setSec_fecha_entrega(SeleccionCalendario sec_fecha_entrega) {
		this.sec_fecha_entrega = sec_fecha_entrega;
	}    


}

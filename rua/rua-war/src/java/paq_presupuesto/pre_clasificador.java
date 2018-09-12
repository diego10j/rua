package paq_presupuesto;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import paq_contabilidad.ejb.ServicioContabilidad;
import sistema.aplicacion.Pantalla;
import org.primefaces.event.NodeSelectEvent;
import paq_presupuesto.ejb.ServicioPresupuesto;
public class pre_clasificador extends Pantalla {
	private Tabla tab_presupuesto=new Tabla();
	private Tabla tab_asociacion_presupuestaria=new Tabla();
	private Arbol arb_clasificador=new Arbol();        
        //Reporte
        private Map map_parametros=new HashMap();
        private Reporte rep_reporte=new Reporte();
        private SeleccionFormatoReporte sel_rep=new SeleccionFormatoReporte();
	 @EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	 @EJB
        private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	
         


	public pre_clasificador(){
            
            //Reporte
            rep_reporte.setId("rep_reporte"); //id
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte"); //ejecuta el metodo al aceptar el reporte
            agregarComponente(rep_reporte); //agrega el componente a la pantalla            
            bar_botones.agregarReporte(); //aparece el boton de reportes en la barra de botones
            sel_rep.setId("sel_rep"); //id
            agregarComponente(sel_rep); //agrego el componente a la pantalla
		
		tab_presupuesto.setId("tab_presupuesto");
		tab_presupuesto.setTipoFormulario(true);
		tab_presupuesto.getGrid().setColumns(4);	
		tab_presupuesto.setHeader("CATALOGO PRESUPUESTARIO");
		tab_presupuesto.setTabla("pre_clasificador","ide_prcla", 1);
		tab_presupuesto.getColumna("ide_prcla").setNombreVisual("CODIGO");
		tab_presupuesto.getColumna("codigo_clasificador_prcla").setNombreVisual("CODIGO CLASIFICADOR");
		tab_presupuesto.getColumna("descripcion_clasificador_prcla").setNombreVisual("NOMBRE CLASIFICADOR");
		tab_presupuesto.getColumna("tipo_prcla").setNombreVisual("TIPO CLASIFICADOR");
		tab_presupuesto.getColumna("nivel_prcla").setNombreVisual("NIVEL");
		tab_presupuesto.getColumna("grupo_prcla").setNombreVisual("TIPO CUENTA");
		tab_presupuesto.getColumna("sigefc_prcla").setNombreVisual("SIGEF");
		tab_presupuesto.getColumna("ide_prgre").setNombreVisual("GRUPO");
		tab_presupuesto.getColumna("pre_ide_prcla").setCombo("select ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla from pre_clasificador order by codigo_clasificador_prcla");
		tab_presupuesto.getColumna("grupo_prcla").setCombo(ser_presupuesto.getListaGrupoCuentaPresupuesto());		
                tab_presupuesto.getColumna("ide_prgre").setCombo("pre_grupo_economico","ide_prgre","detalle_prgre","");
		tab_presupuesto.agregarRelacion(tab_asociacion_presupuestaria);				
		tab_presupuesto.setCampoPadre("pre_ide_prcla"); //necesarios para el arbol
		tab_presupuesto.setCampoNombre("(select codigo_clasificador_prcla||' '||descripcion_clasificador_prcla as descripcion_clasificador_prcla from pre_clasificador b where b. ide_prcla=a.ide_prcla)"); //necesarios para el arbol
		tab_presupuesto.agregarArbol(arb_clasificador);//necesarios para el arbol
		  List lista = new ArrayList();
	       Object fila1[] = {
	           "1", "INGRESOS"
	       };
	       Object fila2[] = {
	           "0", "EGRESOS"
	       };
	       
	       lista.add(fila1);
	       lista.add(fila2);
	    tab_presupuesto.getColumna("tipo_prcla").setRadio(lista, "1");
	    tab_presupuesto.getColumna("tipo_prcla").setRadioVertical(true);
		tab_presupuesto.dibujar();
		PanelTabla pat_clasificador=new PanelTabla();
		pat_clasificador.setPanelTabla(tab_presupuesto);
		
		arb_clasificador.setId("arb_clasificador");
    //DJ  Crea metodo al seleccionar el arbol
		arb_clasificador.onSelect("seleccionoClasificador");  
		arb_clasificador.setDynamic(true);
		arb_clasificador.dibujar();
		
		// tabla deaÃ±os vigente
		tab_asociacion_presupuestaria.setId("tab_vigente");
		tab_asociacion_presupuestaria.setHeader("ASOCIACION PRESUPUESTARIA");
		tab_asociacion_presupuestaria.setTabla("pre_asociacion_presupuestaria", "ide_prasp", 2);
                tab_asociacion_presupuestaria.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable("true,false"));
		tab_asociacion_presupuestaria.getColumna("ide_cocac").setAutoCompletar();
                tab_asociacion_presupuestaria.getColumna("ide_prmop").setCombo("pre_movimiento_presupuestario","ide_prmop","detalle_prmop","");
                tab_asociacion_presupuestaria.getColumna("ide_cnlap").setCombo("con_lugar_aplicac","ide_cnlap","nombre_cnlap","");
		//tab_vigente.setCondicion("ide_covig=-1");
		//tab_vigente.getColumna("ide_geani").setCombo("gen_anio","ide_geani","nom_geani","");
               /* tab_vigente.getColumna("ide_geani").setUnico(true);
		tab_vigente.getColumna("ide_prcla").setUnico(true);
		tab_vigente.getColumna("ide_prasp").setVisible(false);
		tab_vigente.getColumna("ide_cocac").setVisible(false);
		tab_vigente.getColumna("ide_prfup").setVisible(false);
		tab_vigente.getColumna("ide_prpro").setVisible(false);*/

		 //ocultar campos de las claves  foraneas
		TablaGenerica  tab_generica=ser_contabilidad.getTablaVigente("cont_vigente");
	//	for(int i=0;i<tab_generica.getTotalFilas();i++){
		//muestra los ides q quiere mostras.
	//	if(!tab_generica.getValor(i, "column_name").equals("ide_geani")){	
		//tab_vigente.getColumna(tab_generica.getValor(i, "column_name")).setVisible(false);	
		//}				
		
   	//	}
		tab_asociacion_presupuestaria.dibujar();
		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_asociacion_presupuestaria);
		
		//division2
      	Division div_vigente = new Division();
 		div_vigente.setId("div_vigente");
 		div_vigente.dividir2( pat_clasificador, pat_panel2,"50%","h");
 		agregarComponente(div_vigente);

				
		//arbol
		
		Division div_division=new Division();
		div_division.dividir2(arb_clasificador, div_vigente, "25%", "v");
      	agregarComponente(div_division);
      	

        
        
}
 //reporte      
        @Override  
        public void abrirListaReportes(){  
          rep_reporte.dibujar();
    
}

        @Override
        public void aceptarReporte() {
          if (rep_reporte.getReporteSelecionado().equals("Catalogo Presupuestario"))
            rep_reporte.cerrar(); //cierra la lista de reportes
            map_parametros.clear();//limpia parametros
            map_parametros.put("titulo","Catálogo Presupuestarios");
            sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
        
          sel_rep.dibujar();
        } 		
	 /**DJ
	 * Se ejecuta cuando se selecciona algun nodo del arbol
	 */
		public void seleccionoClasificador(NodeSelectEvent evt){
		//Asigna evento al arbol
		arb_clasificador.seleccionarNodo(evt);
		//Filtra la tabla Padre
		tab_presupuesto.ejecutarValorPadre(arb_clasificador.getValorSeleccionado());
		//Filtra la tabla tab_vigente
		tab_asociacion_presupuestaria.ejecutarValorForanea(tab_presupuesto.getValorSeleccionado());
	  }
	

		@Override
		public void insertar() {
			// TODO Auto-generated method stub
			utilitario.getTablaisFocus().insertar();

		}

		@Override
		public void guardar() {
			// TODO Auto-generated method stub
			
			if (tab_presupuesto.guardar()){
				if (tab_asociacion_presupuestaria.guardar()) {
					guardarPantalla();
					//Actualizar el arbol
					arb_clasificador.ejecutarSql();
					utilitario.addUpdate("arb_clasificador");
				}
				
			}
			

		}

		@Override
		public void eliminar() {
			// TODO Auto-generated method stub
			utilitario.getTablaisFocus().eliminar();
		}
		public Tabla getTab_presupuesto() {
			return tab_presupuesto;
		}
		public void setTab_presupuesto(Tabla tab_presupuesto) {
			this.tab_presupuesto = tab_presupuesto;
		}
		public Arbol getArb_clasificador() {
			return arb_clasificador;
		}
		public void setArb_clasificador(Arbol arb_clasificador) {
			this.arb_clasificador = arb_clasificador;
		}




		public Tabla getTab_vigente() {
			return tab_asociacion_presupuestaria;
		}




		public void setTab_vigente(Tabla tab_vigente) {
			this.tab_asociacion_presupuestaria = tab_vigente;
		}

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }
}
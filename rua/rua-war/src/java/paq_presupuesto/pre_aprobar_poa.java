package paq_presupuesto;

import javax.ejb.EJB;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;
/*
 * La clase aprobar (POA) permite generar el presupuesto inicial para gastos para esto se debe
 * considerar lo siguiente cuando se genera un POA el estado Aprobado (activo_prpoa) = False
 * a su vez el campo en la BBDD ide_prpro = null para proceder a la aprobacion del POA y generacion
 * del Presupuesto INICIAL de Gastos.
 */
public class pre_aprobar_poa extends Pantalla{
	private Tabla tab_poa= new Tabla();
	private SeleccionTabla set_egreso = new SeleccionTabla();
	private Dialogo dia_recibir_activo=new Dialogo();
	private Dialogo dia_ingreso=new Dialogo();
	private int int_maximo_detalle=-1;
	private Texto tex_maximo=new Texto();
	private Combo com_anio=new Combo();




	@EJB
	private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	
	
	
	public pre_aprobar_poa(){
		
		bar_botones.limpiar();
		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);
		
		
		Boton bot_importar= new Boton();
		bot_importar.setValue("Aprobar (POA)");
		bot_importar.setMetodo("importar");
		bar_botones.agregarBoton(bot_importar);

		
		

		BotonesCombo boc_seleccion_inversa = new BotonesCombo();
		ItemMenu itm_todas = new ItemMenu();
		ItemMenu itm_niguna = new ItemMenu();

		boc_seleccion_inversa.setValue("Selección Inversa");
		boc_seleccion_inversa.setIcon("ui-icon-circle-check");
		boc_seleccion_inversa.setMetodo("seleccinarInversa");
		boc_seleccion_inversa.setUpdate("tab_poa");
		itm_todas.setValue("Seleccionar Todo");
		itm_todas.setIcon("ui-icon-check");
		itm_todas.setMetodo("seleccionarTodas");
		itm_todas.setUpdate("tab_poa");
		boc_seleccion_inversa.agregarBoton(itm_todas);
		itm_niguna.setValue("Seleccionar Ninguna");
		itm_niguna.setIcon("ui-icon-minus");
		itm_niguna.setMetodo( "seleccionarNinguna");
		itm_niguna.setUpdate("tab_poa");
		boc_seleccion_inversa.agregarBoton(itm_niguna);
		
		//////POA
		tab_poa.setId("tab_poa");
		tab_poa.setSql(ser_presupuesto.getPoaPorAprobarse("true,false", "-1"));
		tab_poa.setNumeroTabla(1);
		tab_poa.setCampoPrimaria("ide_prpoa");
		tab_poa.getColumna("ide_prcla").setVisible(false);
		tab_poa.getColumna("ide_prfup").setVisible(false);
		tab_poa.getColumna("ide_geani").setVisible(false);

		tab_poa.getColumna("presupuesto_inicial_prpoa").setNombreVisual("PRESUPUESTO INICIAL");
		tab_poa.getColumna("codigo_clasificador_prcla").setNombreVisual("CODIGO PRESUPUESTARIO");
		tab_poa.getColumna("codigo_subactividad").setNombreVisual("CODIGO SUBACTIVIDAD");
		tab_poa.getColumna("descripcion_clasificador_prcla").setNombreVisual("NOMBRE CUENTA PRESUPUESTARIA");
		tab_poa.getColumna("detalle_subactividad").setNombreVisual("SUBACTIVIDAD");
		tab_poa.getColumna("detalle_actividad").setNombreVisual("ACTIVIDAD");
		tab_poa.getColumna("nom_geani").setNombreVisual("AñO");
		tab_poa.setColumnaSuma("presupuesto_inicial_prpoa");
		tab_poa.setLectura(true);
		tab_poa.setTipoSeleccion(true);
		tab_poa.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.getChildren().add(boc_seleccion_inversa);
		pat_panel.setPanelTabla(tab_poa);
		pat_panel.getMenuTabla().getItem_formato().setDisabled(true);

		Division div_division=new Division();
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
			
	}
	///////EGRESO BODEGA
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_poa.setSql(ser_presupuesto.getPoaPorAprobarse("false", com_anio.getValue().toString()));
			tab_poa.ejecutarSql();

		}
		else{
			utilitario.agregarMensajeInfo("Selecione un Año", "");

		}
	}
	
	public void importar(){
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		
	}
	
public void seleccionarTodas() {
	tab_poa.setSeleccionados(null);
    Fila seleccionados[] = new Fila[tab_poa.getTotalFilas()];
    for (int i = 0; i < tab_poa.getFilas().size(); i++) {
        seleccionados[i] = tab_poa.getFilas().get(i);
    }
    tab_poa.setSeleccionados(seleccionados);
}

/**DFJ**/
public void seleccinarInversa() {
        if (tab_poa.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_poa.getSeleccionados().length == tab_poa.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_poa.getTotalFilas() - tab_poa.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_poa.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_poa.getSeleccionados().length; j++) {
                    if (tab_poa.getSeleccionados()[j].equals(tab_poa.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_poa.getFilas().get(i);
                    cont++;
                }
            }
            tab_poa.setSeleccionados(seleccionados);
        }
    }

/**DFJ**/
public void seleccionarNinguna() {
	tab_poa.setSeleccionados(null);
    }
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}
	

	public Tabla getTab_poa() {
		return tab_poa;
	}
	public void setTab_poa(Tabla tab_poa) {
		this.tab_poa = tab_poa;
	}
	public Dialogo getDia_ingreso() {
		return dia_ingreso;
	}

	public void setDia_ingreso(Dialogo dia_ingreso) {
		this.dia_ingreso = dia_ingreso;
	}
		public Dialogo getDia_recibir_activo() {
		return dia_recibir_activo;
	}
	public void setDia_recibir_activo(Dialogo dia_recibir_activo) {
		this.dia_recibir_activo = dia_recibir_activo;
	}

}

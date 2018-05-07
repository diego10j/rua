
package paq_adquisicion;
/**
 *
 * @author Andres
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import static paq_presupuesto.pre_tramite.par_proveedor;
import sistema.aplicacion.Pantalla;

public class AreaAdministrativa extends Pantalla{
    private Tabla tab_area_administrativa = new Tabla();
   // private Tabla tab_area_partida = new Tabla();
    private Arbol arb_area = new Arbol();
    private SeleccionTabla sel_tab_tipo_area = new SeleccionTabla();
    private SeleccionTabla sel_tab_departamento = new SeleccionTabla();
    String depar = "";
    String area = "";
    
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
     
     public AreaAdministrativa (){
       tab_area_administrativa.setId("tab_area_administrativa");   //identificador
       tab_area_administrativa.setTabla("adq_area_administrativa", "ide_adarad", 1); 
       //para hacer una rbol
       tab_area_administrativa.setCampoPadre("ADQ_IDE_ADARAD"); //aqui va el nombre del campo recirsivo
       tab_area_administrativa.setCampoNombre("detalle_adarad"); // aqui va el nombre de loq ue queremos que visualice en el arbol
       tab_area_administrativa.agregarArbol(arb_area);
//       tab_area_administrativa.agregarRelacion(tab_area_partida);
       tab_area_administrativa.getColumna("ide_adtiar").setCombo(ser_adquisiciones.getTipoArea());
       tab_area_administrativa.getColumna("IDE_ADARAD").setNombreVisual("CODIGO");
       tab_area_administrativa.getColumna("IDE_ADTIAR").setNombreVisual("TIPO DE AREA");
       tab_area_administrativa.getColumna("IDE_ACUBA").setNombreVisual("DEPARTAMENTO");
       tab_area_administrativa.getColumna("DETALLE_ADARAD").setNombreVisual("DETALLE");
       tab_area_administrativa.getColumna("CODIGO_ADARAD").setNombreVisual("CODIGO DE AREA");
       tab_area_administrativa.getColumna("ide_adarad").setOrden(1);
       tab_area_administrativa.getColumna("IDE_ADTIAR").setOrden(2);
       tab_area_administrativa.getColumna("IDE_ACUBA").setOrden(3);
       tab_area_administrativa.getColumna("DETALLE_ADARAD").setOrden(4);
       tab_area_administrativa.getColumna("CODIGO_ADARAD").setOrden(5);
       tab_area_administrativa.getColumna("IDE_ACUBA").setVisible(false);
       tab_area_administrativa.dibujar();
       
        Boton bot_agregar_solicitante = new Boton();
        Boton bot_anular = new Boton();
        bot_anular.setIcon("ui-icon-search");
        bot_anular.setValue("IMPORTAR DEPARTAMENTO");
        bot_anular.setMetodo("abrirDialogoArea");
         agregarComponente(bot_anular);
         bar_botones.agregarBoton(bot_anular);
        
        sel_tab_tipo_area.setId("sel_tab_tipo_area");
        sel_tab_tipo_area.setTitle("TIPO DE ÁREA");
        sel_tab_tipo_area.setSeleccionTabla(ser_adquisiciones.getTipoArea(), "IDE_ADTIAR");
        sel_tab_tipo_area.setRadio();
        sel_tab_tipo_area.setWidth("80%");
        sel_tab_tipo_area.setHeight("70%");
        sel_tab_tipo_area.getBot_aceptar().setMetodo("aceptarArea");
        agregarComponente(sel_tab_tipo_area);
        
        sel_tab_departamento.setId("sel_tab_departamento");
        sel_tab_departamento.setTitle("DEPARTAMENTO");
        sel_tab_departamento.setSeleccionTabla(ser_adquisiciones.getDepartamento(), "IDE_ACUBA");
        sel_tab_departamento.setWidth("80%");
        sel_tab_departamento.setHeight("70%");
        sel_tab_departamento.getBot_aceptar().setMetodo("insertaDatos");
        agregarComponente(sel_tab_departamento);
       
      PanelTabla pat_area_administrativa = new PanelTabla();
      pat_area_administrativa.setId("pat_area_administrativa");
      pat_area_administrativa.setPanelTabla(tab_area_administrativa);
      
/*       tab_area_partida.setId("tab_area_partida");   //identificador
       tab_area_partida.setTabla("adq_area_partida", "ide_adarpa", 1); 
       tab_area_partida.getColumna("ide_adpapr").setCombo(ser_adquisiciones.getPartidaPresupuestaria());
       tab_area_partida.getColumna("IDE_ADARPA").setNombreVisual("CODIGO");
       tab_area_partida.getColumna("IDE_ADPAPR").setNombreVisual("PARTIDA PRESUPUESTARIA");
       tab_area_partida.getColumna("CODIGO_ADARPA").setNombreVisual("CODIGO PARTIDA");
       tab_area_partida.dibujar();
       PanelTabla pat_area_partida = new PanelTabla();
      pat_area_partida.setId("pat_area_partida");
      pat_area_partida.setPanelTabla(tab_area_partida);*/
      
      arb_area.setId("arb_area");
      arb_area.dibujar();
      Division div_area_administrativa = new Division();
      div_area_administrativa.setId("div_area_administrativa");
      Division div_arbol= new Division();
      div_arbol.dividir1(pat_area_administrativa);
      div_area_administrativa.dividir2(arb_area,div_arbol,"20%","V");
      
      // agregarComponente(div_arbol);
     
      agregarComponente(div_area_administrativa);
     }
     String tipo_area="";
     public void abrirDialogoArea(){
        sel_tab_tipo_area.dibujar();
    }
     public void aceptarArea(){
        tipo_area=sel_tab_tipo_area.getValorSeleccionado();
        sel_tab_departamento.dibujar();
        sel_tab_tipo_area.cerrar();
    }
     public void metodoValidacion(){
         depar = sel_tab_departamento.getSeleccionados();
         area = sel_tab_tipo_area.getValorSeleccionado();
        utilitario.agregarMensajeInfo("ADVERTENCIA","modalidad "+depar);
        utilitario.agregarMensajeInfo("ADVERTENCIA","jorna "+area);
    }
     
     public void insertaDatos(){
         
        String str_ins = sel_tab_departamento.getSeleccionados();
        TablaGenerica departamento = utilitario.consultar(ser_adquisiciones.getDepartamentoDatos(str_ins));

       //  if(sel_tab_departamento.getValorSeleccionado()!=null){
             for (int i=0;i<departamento.getTotalFilas();i++){
                tab_area_administrativa.insertar();
		tab_area_administrativa.setValor("ide_adtiar",tipo_area);
		tab_area_administrativa.setValor("detalle_adarad",departamento.getValor(i,"nombre_acuba"));
		tab_area_administrativa.setValor("codigo_adarad",departamento.getValor(i,"codigo_acuba"));
                tab_area_administrativa.setValor("ide_acuba",departamento.getValor(i,"ide_acuba"));
		
					
			}
             tab_area_administrativa.guardar();
             guardarPantalla();
             sel_tab_departamento.cerrar();
	        utilitario.addUpdate("tab_area_administrativa");
				   
        //  }
      /*  else {
		utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
				    }     */    
     }
     public void insertDatos(){
         //utilitario.get
         area =  sel_tab_tipo_area.getValorSeleccionado();
         depar = sel_tab_departamento.getSeleccionados();
         TablaGenerica departamento = utilitario.consultar(ser_adquisiciones.getDepartamentoDatos(depar));
         TablaGenerica area_admin = utilitario.consultar(ser_adquisiciones.getTipoAreaDatos(area));
         for (int i=0;i<departamento.getTotalFilas();i++){
              for (int j=0;j<area_admin.getTotalFilas();j++){
              String sql = "insert into adq_area_administrativa(ide_adarad, ide_adtiar, detalle_adarad, codigo_adarad, ide_acuba)\n" +
                           "  values("+departamento.getValor(i, "ide_acuba")+", "+area_admin.getValor(j, "ide_adtiar")+", "+departamento.getValor(i, "nombre_acuba")+", "+departamento.getValor(i, "codigo_acuba")+", "+departamento.getValor(i, "nombre_acuba")+")";
              utilitario.getConexion().ejecutarSql(sql);
                  sel_tab_departamento.cerrar();
         }
       } 
     }
     
     @Override
    public void insertar() {
        if(tab_area_administrativa.isFocus()){
       tab_area_administrativa.insertar();
        }
       /* else if (tab_area_partida.isFocus()){
            tab_area_partida.insertar();
        }*/
    }
    
    
    @Override
    public void guardar() {
        if(tab_area_administrativa.isFocus()){
       tab_area_administrativa.guardar();
        }
/*        else if (tab_area_partida.isFocus()){
            tab_area_partida.guardar();
        }*/
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       if(tab_area_administrativa.isFocus()){
       tab_area_administrativa.eliminar();
        }
      /*  else if (tab_area_partida.isFocus()){
            tab_area_partida.eliminar();
        }*/
    }

    public Tabla getTab_area_administrativa() {
        return tab_area_administrativa;
    }

    public void setTab_area_administrativa(Tabla tab_area_administrativa) {
        this.tab_area_administrativa = tab_area_administrativa;
    }

    public Arbol getArb_area() {
        return arb_area;
    }

    public void setArb_area(Arbol arb_area) {
        this.arb_area = arb_area;
    }
  
    /*public Tabla getTab_area_partida() {
        return tab_area_partida;
    }

    public void setTab_area_partida(Tabla tab_area_partida) {
        this.tab_area_partida = tab_area_partida;
    }*/

    public SeleccionTabla getSel_tab_tipo_area() {
        return sel_tab_tipo_area;
    }

    public void setSel_tab_tipo_area(SeleccionTabla sel_tab_tipo_area) {
        this.sel_tab_tipo_area = sel_tab_tipo_area;
    }

    public SeleccionTabla getSel_tab_departamento() {
        return sel_tab_departamento;
    }

    public void setSel_tab_departamento(SeleccionTabla sel_tab_departamento) {
        this.sel_tab_departamento = sel_tab_departamento;
    }
    
    
}

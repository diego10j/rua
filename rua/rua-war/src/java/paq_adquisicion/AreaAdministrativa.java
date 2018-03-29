
package paq_adquisicion;
/**
 *
 * @author Andres
 */
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class AreaAdministrativa extends Pantalla{
    private Tabla tab_area_administrativa = new Tabla();
    private Tabla tab_area_partida = new Tabla();
    private Arbol arb_area = new Arbol();
    
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
     
     public AreaAdministrativa (){
       tab_area_administrativa.setId("tab_area_administrativa");   //identificador
       tab_area_administrativa.setTabla("adq_area_administrativa", "ide_adarad", 1); 
       //para hacer una rbol
       tab_area_administrativa.setCampoPadre("ADQ_IDE_ADARAD"); //aqui va el nombre del campo recirsivo
       tab_area_administrativa.setCampoNombre("detalle_adarad"); // aqui va el nombre de loq ue queremos que visualice en el arbol
       tab_area_administrativa.agregarArbol(arb_area);
       tab_area_administrativa.agregarRelacion(tab_area_partida);
       tab_area_administrativa.getColumna("ide_adtiar").setCombo(ser_adquisiciones.getTipoArea());
       tab_area_administrativa.getColumna("IDE_ADARAD").setNombreVisual("CODIGO");
       tab_area_administrativa.getColumna("IDE_ADTIAR").setNombreVisual("TIPO DE AREA");
       tab_area_administrativa.getColumna("DETALLE_ADARAD").setNombreVisual("DETALLE");
       tab_area_administrativa.getColumna("CODIGO_ADARAD").setNombreVisual("CODIGO DE AREA");
       tab_area_administrativa.dibujar();
       
      PanelTabla pat_area_administrativa = new PanelTabla();
      pat_area_administrativa.setId("pat_area_administrativa");
      pat_area_administrativa.setPanelTabla(tab_area_administrativa);
      
       tab_area_partida.setId("tab_area_partida");   //identificador
       tab_area_partida.setTabla("adq_area_partida", "ide_adarpa", 1); 
       tab_area_partida.getColumna("ide_adpapr").setCombo(ser_adquisiciones.getPartidaPresupuestaria());
       tab_area_partida.getColumna("IDE_ADARPA").setNombreVisual("CODIGO");
       tab_area_partida.getColumna("IDE_ADPAPR").setNombreVisual("PARTIDA PRESUPUESTARIA");
       tab_area_partida.getColumna("CODIGO_ADARPA").setNombreVisual("CODIGO PARTIDA");
       tab_area_partida.dibujar();
       PanelTabla pat_area_partida = new PanelTabla();
      pat_area_partida.setId("pat_area_partida");
      pat_area_partida.setPanelTabla(tab_area_partida);
      
      arb_area.setId("arb_area");
      arb_area.dibujar();
      Division div_area_administrativa = new Division();
      div_area_administrativa.setId("div_area_administrativa");
      Division div_arbol= new Division();
      div_arbol.dividir2(pat_area_administrativa,pat_area_partida,"50%","H");
      div_area_administrativa.dividir2(arb_area,div_arbol,"20%","V");
      
      // agregarComponente(div_arbol);
     
      agregarComponente(div_area_administrativa);
     }
     @Override
    public void insertar() {
        if(tab_area_administrativa.isFocus()){
       tab_area_administrativa.insertar();
        }
        else if (tab_area_partida.isFocus()){
            tab_area_partida.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_area_administrativa.isFocus()){
       tab_area_administrativa.guardar();
        }
        else if (tab_area_partida.isFocus()){
            tab_area_partida.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       if(tab_area_administrativa.isFocus()){
       tab_area_administrativa.eliminar();
        }
        else if (tab_area_partida.isFocus()){
            tab_area_partida.eliminar();
        }
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

    public Tabla getTab_area_partida() {
        return tab_area_partida;
    }

    public void setTab_area_partida(Tabla tab_area_partida) {
        this.tab_area_partida = tab_area_partida;
    }
    
}

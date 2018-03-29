
package paq_adquisicion;

/**
 *
 * @author Andres
 */
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import sistema.aplicacion.Pantalla;
public class PartidaPresupuestaria extends Pantalla {
    
    private Tabla tab_partida_pre = new Tabla();
      
    public PartidaPresupuestaria (){
       tab_partida_pre.setId("tab_partida_pre");   //identificador
       tab_partida_pre.setTabla("adq_partida_presupuestaria", "ide_adpapr", 1); 
       tab_partida_pre.getColumna("IDE_ADPAPR").setNombreVisual("CODIGO");
       tab_partida_pre.getColumna("DETALLE_ADPAPR").setNombreVisual("DETALLE");
       tab_partida_pre.getColumna("CODIGO_ADPAPR").setNombreVisual("PARTIDA PRESUPUESTARIA");
       tab_partida_pre.dibujar();
       
      PanelTabla pat_partida_pre = new PanelTabla();
      pat_partida_pre.setId("pat_partida_pre");
      pat_partida_pre.setPanelTabla(tab_partida_pre);
      Division div_partida_pre = new Division();
      div_partida_pre.setId("div_partida_pre");
      div_partida_pre.dividir1(pat_partida_pre);
      agregarComponente(div_partida_pre);
      
      }
    @Override
    public void insertar() {
        tab_partida_pre.insertar();
    }

    @Override
    public void guardar() {
        tab_partida_pre.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_partida_pre.eliminar();
    }

    public Tabla getTab_partida_pre() {
        return tab_partida_pre;
    }

    public void setTab_partida_pre(Tabla tab_partida_pre) {
        this.tab_partida_pre = tab_partida_pre;
    }
    
 }
    

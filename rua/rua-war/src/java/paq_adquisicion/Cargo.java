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
public class Cargo extends Pantalla {
    
   private Tabla tab_cargo = new Tabla();
       
   public Cargo (){
       tab_cargo.setId("tab_cargo");   //identificador
       tab_cargo.setTabla("adq_cargo", "ide_adcarg", 1); 
       tab_cargo.getColumna("IDE_ADCARG").setNombreVisual("CODIGO");
       tab_cargo.getColumna("DETALLE_ADCARG").setNombreVisual("DETALLE");
       tab_cargo.dibujar();
       
      PanelTabla pat_cargo = new PanelTabla();
      pat_cargo.setId("pat_cargo");
      pat_cargo.setPanelTabla(tab_cargo);
      Division div_cargo = new Division();
      div_cargo.setId("div_cargo");
      div_cargo.dividir1(pat_cargo);
      agregarComponente(div_cargo);
      
}
    @Override
    public void insertar() {
        tab_cargo.insertar();
    }

    @Override
    public void guardar() {
        tab_cargo.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_cargo.eliminar();
    }

    public Tabla getTab_cargo() {
        return tab_cargo;
    }

    public void setTab_cargo(Tabla tab_cargo) {
        this.tab_cargo = tab_cargo;
    }
    
   }
       

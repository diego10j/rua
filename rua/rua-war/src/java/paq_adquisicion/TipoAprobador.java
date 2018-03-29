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

public class TipoAprobador extends Pantalla {
    private Tabla tab_tipo_aprobador = new Tabla();
    
    public TipoAprobador(){
        
       tab_tipo_aprobador.setId("tab_tipo_aprobador");   //identificador
       tab_tipo_aprobador.setTabla("adq_tipo_aprobador", "ide_adtiap", 1); 
       tab_tipo_aprobador.getColumna("IDE_ADTIAP").setNombreVisual("CODIGO");
       tab_tipo_aprobador.getColumna("DETALLE_ADTIAP").setNombreVisual("DETALLE");
       tab_tipo_aprobador.dibujar();
       
      PanelTabla pat_tipo_aprobador = new PanelTabla();
      pat_tipo_aprobador.setId("pat_tipo_aprobador");
      pat_tipo_aprobador.setPanelTabla(tab_tipo_aprobador);
      Division div_tipo_aprobador = new Division();
      div_tipo_aprobador.setId("div_tipo_aprobador");
      div_tipo_aprobador.dividir1(pat_tipo_aprobador);
      agregarComponente(div_tipo_aprobador);
      
      
    }
   @Override
    public void insertar() {
        tab_tipo_aprobador.insertar();
    }

    @Override
    public void guardar() {
        tab_tipo_aprobador.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_tipo_aprobador.eliminar();
    } 

    public Tabla getTab_tipo_aprobador() {
        return tab_tipo_aprobador;
    }

    public void setTab_tipo_aprobador(Tabla tab_tipo_aprobador) {
        this.tab_tipo_aprobador = tab_tipo_aprobador;
    }
    
   
}

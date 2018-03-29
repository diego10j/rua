
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

public class TipoDenominacion extends Pantalla{
    private Tabla tab_tipo_denominacion = new Tabla();
    
    public TipoDenominacion (){
       tab_tipo_denominacion.setId("tab_tipo_denominacion");   //identificador
       tab_tipo_denominacion.setTabla("adq_tipo_denominacion", "ide_adtide", 1);
       tab_tipo_denominacion.getColumna("IDE_ADTIDE").setNombreVisual("CODIGO");
       tab_tipo_denominacion.getColumna("DETALLE_ADTIDE").setNombreVisual("DETALLE");
       tab_tipo_denominacion.dibujar();     
       
      PanelTabla pat_tipo_denominacion = new PanelTabla();
      pat_tipo_denominacion.setId("pat_tipo_denominacion");
      pat_tipo_denominacion.setPanelTabla(tab_tipo_denominacion);
      Division div_tipo_denominacion = new Division();
      div_tipo_denominacion.setId("div_tipo_denominacion");
      div_tipo_denominacion.dividir1(pat_tipo_denominacion);
      agregarComponente(div_tipo_denominacion);       
    }
    @Override
    public void insertar() {
        tab_tipo_denominacion.insertar();
    }

    @Override
    public void guardar() {
        tab_tipo_denominacion.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_tipo_denominacion.eliminar();
    }

    public Tabla getTab_tipo_denominacion() {
        return tab_tipo_denominacion;
    }

    public void setTab_tipo_denominacion(Tabla tab_tipo_denominacion) {
        this.tab_tipo_denominacion = tab_tipo_denominacion;
    }
        
}

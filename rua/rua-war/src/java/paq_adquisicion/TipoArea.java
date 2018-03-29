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

public class TipoArea extends Pantalla {
    private Tabla tab_tipo_area = new Tabla();
    
    public TipoArea () {
       tab_tipo_area.setId("tab_tipo_area");   //identificador
       tab_tipo_area.setTabla("adq_tipo_area", "ide_adtiar", 1); 
       tab_tipo_area.getColumna("IDE_ADTIAR").setNombreVisual("CODIGO");
       tab_tipo_area.getColumna("DETALLE_ADTIAR").setNombreVisual("DETALLE");
       tab_tipo_area.dibujar();
       
      PanelTabla pat_tipo_area = new PanelTabla();
      pat_tipo_area.setId("pat_tipo_area");
      pat_tipo_area.setPanelTabla(tab_tipo_area);
      Division div_tipo_area = new Division();
      div_tipo_area.setId("div_tipo_area");
      div_tipo_area.dividir1(pat_tipo_area);
      agregarComponente(div_tipo_area);
    }
    @Override
    public void insertar() {
        tab_tipo_area.insertar();
    }

    @Override
    public void guardar() {
        tab_tipo_area.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_tipo_area.eliminar();
    }

    public Tabla getTab_tipo_area() {
        return tab_tipo_area;
    }

    public void setTab_tipo_area(Tabla tab_tipo_area) {
        this.tab_tipo_area = tab_tipo_area;
    }
    
}

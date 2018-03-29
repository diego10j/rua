
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

public class GrupoMaterial extends Pantalla {
    private Tabla tab_grupo_material = new Tabla();
    
     public GrupoMaterial (){
       tab_grupo_material.setId("tab_grupo_material");   //identificador
       tab_grupo_material.setTabla("adq_grupo_material", "ide_adgrma", 1); 
       tab_grupo_material.getColumna("IDE_ADGRMA").setNombreVisual("CODIGO");
       tab_grupo_material.getColumna("DETALLE_ADGRMA").setNombreVisual("DETALLE");
       tab_grupo_material.getColumna("CODIGO_ADGRMA").setNombreVisual("CODIGO GRUPO MATERIAL");
       tab_grupo_material.dibujar();
       
      PanelTabla pat_grupo_material = new PanelTabla();
      pat_grupo_material.setId("pat_grupo_material");
      pat_grupo_material.setPanelTabla(tab_grupo_material);
      Division div_grupo_material = new Division();
      div_grupo_material.setId("div_grupo_material");
      div_grupo_material.dividir1(pat_grupo_material);
      agregarComponente(div_grupo_material);
     }
     @Override
    public void insertar() {
        tab_grupo_material.insertar();
    }

    @Override
    public void guardar() {
        tab_grupo_material.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_grupo_material.eliminar();
    }

    public Tabla getTab_grupo_material() {
        return tab_grupo_material;
    }

    public void setTab_grupo_material(Tabla tab_grupo_material) {
        this.tab_grupo_material = tab_grupo_material;
    }
    
}

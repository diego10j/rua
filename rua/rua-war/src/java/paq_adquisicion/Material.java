
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
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class Material extends Pantalla {
    private Tabla tab_material = new Tabla();
    private Tabla tab_partida_material = new Tabla();
    
    @EJB
    private final  ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    
     public Material (){
         
       tab_material.setId("tab_material");   //identificador
       tab_material.setTabla("adq_material", "ide_admate", 1);
       tab_material.getColumna("IDE_ADGRMA").setCombo(ser_adquisiciones.getGrupoMaterial());
       tab_material.agregarRelacion(tab_partida_material);
       tab_material.getColumna("IDE_ADMATE").setNombreVisual("CODIGO");
       tab_material.getColumna("IDE_ADGRMA").setNombreVisual("CODIGO GRUPO MATERIAL");
       tab_material.getColumna("DETALLE_ADMATE").setNombreVisual("DETALLE");
       tab_material.getColumna("CODIGO_ADMATE").setNombreVisual("CODIGO MATERIAL");
       tab_material.getColumna("APLICA_COD_PRESU_ADMATE").setNombreVisual("APLICA CODIGO PRESUPUESTARIO");
       List lista = new ArrayList();
       Object fila1[] = {"2", "ACTIVO FIJO"};
       Object fila2[] = {"1", "STOCK EN BODEGA"};
       lista.add(fila1);
       lista.add(fila2);
       tab_material.getColumna("tipo_bien_admate").setCombo(lista);
       tab_material.dibujar();
       
      PanelTabla pat_material = new PanelTabla();
      pat_material.setId("pat_material");
      pat_material.setPanelTabla(tab_material);
      
       tab_partida_material.setId("tab_partida_material");   //identificador
       tab_partida_material.setTabla("adq_partida_material", "ide_adpama", 2); 
       tab_partida_material.getColumna("IDE_ADARPA").setCombo(ser_adquisiciones.getAreaPartida());
       tab_partida_material.getColumna("IDE_ADPAMA").setNombreVisual("CODIGO");
       tab_partida_material.getColumna("IDE_ADARPA").setNombreVisual("AREA PARTIDA");
       tab_partida_material.getColumna("CODIGO_ADPAMA").setNombreVisual("CODIGO PARTIDA MATERIAL");
     
       
       tab_partida_material.dibujar();
       
      PanelTabla pat_partida_material = new PanelTabla();
      pat_partida_material.setId("pat_partida_material");
      pat_partida_material.setPanelTabla(tab_partida_material);
      
      Division div_material = new Division();
      div_material.setId("div_material");
      div_material.dividir2(pat_material, pat_partida_material, "60%", "H");
      agregarComponente(div_material);
     }
     
     @Override
    public void insertar() {
        if(tab_material.isFocus()){
       tab_material.insertar();
        }
        else if (tab_partida_material.isFocus()){
            tab_partida_material.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_material.isFocus()){
       tab_material.guardar();
        }
        else if (tab_partida_material.isFocus()){
            tab_partida_material.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if(tab_material.isFocus()){
       tab_material.eliminar();
        }
        else if (tab_partida_material.isFocus()){
            tab_partida_material.eliminar();
        }
    }

    public Tabla getTab_material() {
        return tab_material;
    }

    public void setTab_material(Tabla tab_material) {
        this.tab_material = tab_material;
    }

    public Tabla getTab_partida_material() {
        return tab_partida_material;
    }

    public void setTab_partida_material(Tabla tab_partida_material) {
        this.tab_partida_material = tab_partida_material;
    }
    
}

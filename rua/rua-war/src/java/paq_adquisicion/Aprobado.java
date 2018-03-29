
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

public class Aprobado extends Pantalla {
    private Tabla tab_aprobado = new Tabla();
    
    public Aprobado (){
       tab_aprobado.setId("tab_aprobado");   //identificador
       tab_aprobado.setTabla("adq_aprobado", "ide_adapro", 1);
       tab_aprobado.getColumna("IDE_ADAPRO").setNombreVisual("CODIGO");
       tab_aprobado.getColumna("DETALLE_ADAPRO").setNombreVisual("DETALLE");
       tab_aprobado.dibujar();
       
       PanelTabla pat_sprobado = new PanelTabla();
       pat_sprobado.setId("pat_sprobado");
       pat_sprobado.setPanelTabla(tab_aprobado);
       Division div_aprobado = new Division();
       div_aprobado.setId("div_aprobado");
       div_aprobado.dividir1(pat_sprobado);
       agregarComponente(div_aprobado);
    }
    @Override
    public void insertar() {
        tab_aprobado.insertar();
    }

    @Override
    public void guardar() {
        tab_aprobado.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_aprobado.eliminar();
    }

    public Tabla getTab_aprobado() {
        return tab_aprobado;
    }

    public void setTab_aprobado(Tabla tab_aprobado) {
        this.tab_aprobado = tab_aprobado;
    }
    
}

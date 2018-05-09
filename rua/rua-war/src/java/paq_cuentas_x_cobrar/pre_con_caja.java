
package paq_cuentas_x_cobrar;

/**
 *
 * @author Andres
 */
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import sistema.aplicacion.Pantalla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;

public class pre_con_caja extends Pantalla {
      private Tabla tab_caja = new Tabla();
    public pre_con_caja(){
        
         tab_caja.setId("tab_caja");   //identificador
         tab_caja.setTabla("CONT_CAJA", "IDE_COCAJ", 1);
         tab_caja.dibujar();
         
         PanelTabla pat_caja = new PanelTabla();
        pat_caja.setId("pat_caja");
        pat_caja.setPanelTabla(tab_caja);
        Division div_caja = new Division();
        div_caja.setId("div_caja");
        div_caja.dividir1(pat_caja);
        agregarComponente(div_caja);
    }
    @Override
    public void insertar() {
        tab_caja.insertar();
    }

    @Override
    public void guardar() {
        tab_caja.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_caja.eliminar();
    }

    public Tabla getTab_caja() {
        return tab_caja;
    }

    public void setTab_caja(Tabla tab_caja) {
        this.tab_caja = tab_caja;
    }
    
    
    
}

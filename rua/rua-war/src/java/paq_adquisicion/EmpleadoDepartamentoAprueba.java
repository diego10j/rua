
package paq_adquisicion;

/**
 *
 * @author Andres
 */
import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class EmpleadoDepartamentoAprueba extends Pantalla{
    
    private Tabla tab_emp_depar = new Tabla();
    private Tabla tab_emp_aprueba = new Tabla();
    private AutoCompletar aut_emple_depa_aprue = new AutoCompletar();
    
    
    @EJB
    private final  ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    public EmpleadoDepartamentoAprueba (){
        
        aut_emple_depa_aprue.setId("aut_emple_depa_aprue");
        aut_emple_depa_aprue.setAutoCompletar(ser_adquisiciones.getAreaAdministrativa("0","0"));
        aut_emple_depa_aprue.setSize(75);
        aut_emple_depa_aprue.setMetodoChange("selecionoAutocompletar");
        aut_emple_depa_aprue.setTitle("EMPLEADOS INSTITUCIONALES");

        bar_botones.agregarComponente(new Etiqueta("Area Administrativa:"));
        bar_botones.agregarComponente(aut_emple_depa_aprue);
        
       tab_emp_depar.setId("tab_emp_depar");   //identificador
       tab_emp_depar.setTabla("adq_empleado_departamento", "ide_ademde", 1);
       tab_emp_depar.setCondicion("ide_adarad=-1");
       tab_emp_depar.getColumna("ide_adarad").setVisible(false);
       tab_emp_depar.getColumna("ide_adarad").setCombo(ser_adquisiciones.getAreaAdministrativa("0","0"));
       tab_emp_depar.getColumna("ide_ademple").setCombo(ser_adquisiciones.getEmpleado());
       tab_emp_depar.getColumna("ide_adcarg").setCombo(ser_adquisiciones.getCargo());
       tab_emp_depar.getColumna("ide_adtiap").setCombo(ser_adquisiciones.getTipoAprobador());
       tab_emp_depar.getColumna("IDE_ADEMDE").setNombreVisual("CODIGO");
       tab_emp_depar.getColumna("IDE_ADARAD").setNombreVisual("AREA ADMINISTRATIVA");
       tab_emp_depar.getColumna("IDE_ADEMPLE").setNombreVisual("EMPLEADO");
       tab_emp_depar.getColumna("IDE_ADCARG").setNombreVisual("CARGO");
       tab_emp_depar.getColumna("IDE_ADTIAP").setNombreVisual("TIPO APROBADOR");
       //tab_emp_depar.getColumna("ide_ademple").setVisible(false);
       tab_emp_depar.getColumna("FECHA_INICIO_ADEMDE").setNombreVisual("FECHA INICIO");
       tab_emp_depar.getColumna("FECHA_FINAL_ADEMDE").setNombreVisual("FECHA FINAL");
       tab_emp_depar.getColumna("ACTIVO_ADEMDE").setNombreVisual("ACTIVO");
       tab_emp_depar.agregarRelacion(tab_emp_aprueba);
       
       tab_emp_depar.dibujar();
       
      PanelTabla pat_emp_depar_aprue = new PanelTabla();
      pat_emp_depar_aprue.setId("pat_emp_depar_aprue");
      pat_emp_depar_aprue.setPanelTabla(tab_emp_depar);
      
      tab_emp_aprueba.setId("tab_emp_aprueba");   //identificador
       tab_emp_aprueba.setTabla("adq_empleado_aprueba", "ide_ademap", 1); 
       tab_emp_aprueba.getColumna("ide_ademple").setCombo(ser_adquisiciones.getEmpleado());
       tab_emp_aprueba.getColumna("ide_ademple").setNombreVisual("APRUEBA SOLICITUD");
       tab_emp_aprueba.getColumna("ide_adcarg").setCombo(ser_adquisiciones.getCargo());
       List lista = new ArrayList();
       Object fila1[] = {"1", "APROBADO OFICIAL"};
       Object fila2[] = {"2","APROBADO ENCARGADO"};
       lista.add(fila1);
       lista.add(fila2);
       tab_emp_aprueba.getColumna("categoria_ademap").setCombo(lista); 
       tab_emp_aprueba.getColumna("IDE_ADEMAP").setNombreVisual("CODIGO");
       tab_emp_aprueba.getColumna("IDE_ADEMPLE").setNombreVisual("EMPLEADO");
       tab_emp_aprueba.getColumna("FECHA_INICIAL_ADEMAP").setNombreVisual("FECHA INICIAL");
       tab_emp_aprueba.getColumna("FEHA_FINAL_ADEMAP").setNombreVisual("FECHA FINAL");
       tab_emp_aprueba.getColumna("CATEGORIA_ADEMAP").setNombreVisual("CATEGORIA");
       tab_emp_aprueba.getColumna("ACTIVO_ADEMAP").setNombreVisual("ACTIVO");
       tab_emp_aprueba.getColumna("IDE_ADCARG").setNombreVisual("CARGO");
       tab_emp_aprueba.dibujar();
       
      PanelTabla pat_emp_aprueba = new PanelTabla();
      pat_emp_aprueba.setId("pat_emp_aprueba");
      pat_emp_aprueba.setPanelTabla(tab_emp_aprueba);
      
      Division div_emp_aprueba = new Division();
      div_emp_aprueba.setId("div_emp_aprueba");
      div_emp_aprueba.dividir2(pat_emp_depar_aprue, pat_emp_aprueba, "50%","H");
      agregarComponente(div_emp_aprueba);
    }
    public void selecionoAutocompletar(SelectEvent evt){
             aut_emple_depa_aprue.onSelect(evt);
             if (aut_emple_depa_aprue.getValor() != null) {
            tab_emp_depar.setCondicion("ide_adarad="+aut_emple_depa_aprue.getValor());
            tab_emp_depar.ejecutarSql();
        }
 
     }
    @Override
    public void insertar() {
        if (aut_emple_depa_aprue.getValor() != null) {
        if(tab_emp_depar.isFocus()){
                  tab_emp_depar.insertar();
                  tab_emp_depar.setValor("ide_adarad", aut_emple_depa_aprue.getValor());
        }
        else if (tab_emp_aprueba.isFocus()){
            tab_emp_aprueba.insertar();
        }
        }
        else {
            utilitario.agregarMensajeError("Seleccione", "Seleccione el Area Administrativa");
        }
    }

    @Override
    public void guardar() {
        if(tab_emp_depar.isFocus()){
                  tab_emp_depar.guardar();
        }
        else if (tab_emp_aprueba.isFocus()){
            tab_emp_aprueba.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       if(tab_emp_depar.isFocus()){
                  tab_emp_depar.eliminar();
        }
        else if (tab_emp_aprueba.isFocus()){
            tab_emp_aprueba.eliminar();
        }
    }

    public Tabla getTab_emp_depar() {
        return tab_emp_depar;
    }

    public void setTab_emp_depar(Tabla tab_emp_depar) {
        this.tab_emp_depar = tab_emp_depar;
    }

    public Tabla getTab_emp_aprueba() {
        return tab_emp_aprueba;
    }

    public void setTab_emp_aprueba(Tabla tab_emp_aprueba) {
        this.tab_emp_aprueba = tab_emp_aprueba;
    }

    public AutoCompletar getAut_emple_depa_aprue() {
        return aut_emple_depa_aprue;
    }

    public void setAut_emple_depa_aprue(AutoCompletar aut_emple_depa_aprue) {
        this.aut_emple_depa_aprue = aut_emple_depa_aprue;
    }

    
}

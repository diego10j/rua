package paq_adquisicion;

/**
 *
 * @author Andres
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class Empleado extends Pantalla {

    private Tabla tab_empleado = new Tabla();
    private Texto text_texto = new Texto();
    private AutoCompletar autBusca = new AutoCompletar();

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);


    public Empleado() {

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT IDE_ADEMPLE,CEDULA_ADEMPLE,NOMBRES_ADEMPLE,DIRECCION_ADEMPLE from ADQ_EMPLEADO order by NOMBRES_ADEMPLE");
        autBusca.setSize(70);
        autBusca.setMetodoChange("buscaEmpleado");
        bar_botones.agregarComponente(new Etiqueta("Busca Empleado : "));
        bar_botones.agregarComponente(autBusca);

        tab_empleado.setId("tab_empleado");   //identificador
        tab_empleado.setTabla("adq_empleado", "ide_ademple", 1);
        tab_empleado.getColumna("IDE_ADTIDE").setCombo(ser_adquisiciones.getTipoDenominacion());
        tab_empleado.getColumna("IDE_USUA").setCombo(ser_adquisiciones.getUsuario("TRUE"));
        tab_empleado.getColumna("IDE_USUA").setAutoCompletar();
        tab_empleado.getColumna("IDE_ADEMPLE").setNombreVisual("CODIGO");
        tab_empleado.getColumna("IDE_ADTIDE").setNombreVisual("DENOMINACION");
        tab_empleado.getColumna("IDE_USUA").setNombreVisual("USUARIO");
        tab_empleado.getColumna("CEDULA_ADEMPLE").setNombreVisual("CEDULA");
        tab_empleado.getColumna("NOMBRES_ADEMPLE").setNombreVisual("NOMBRES");
        tab_empleado.getColumna("DIRECCION_ADEMPLE").setNombreVisual("DIRECCION");
        tab_empleado.getColumna("FIRMA_ADEMPLE").setNombreVisual("FIRMA");

        tab_empleado.getColumna("IDE_ADEMPLE").setOrden(0);
        tab_empleado.getColumna("IDE_ADTIDE").setOrden(3);
        tab_empleado.getColumna("IDE_USUA").setOrden(4);
        tab_empleado.getColumna("CEDULA_ADEMPLE").setOrden(1);
        tab_empleado.getColumna("NOMBRES_ADEMPLE").setOrden(2);
        tab_empleado.getColumna("DIRECCION_ADEMPLE").setOrden(5);

        tab_empleado.getColumna("FIRMA_ADEMPLE").setUpload();
        tab_empleado.getColumna("FIRMA_ADEMPLE").setImagen();
        tab_empleado.setTipoFormulario(true);
        tab_empleado.getGrid().setColumns(2);
        tab_empleado.dibujar();

        PanelTabla pat_empleado = new PanelTabla();
        pat_empleado.setId("pat_empleado");
        pat_empleado.setPanelTabla(tab_empleado);
        Division div_empleado = new Division();
        div_empleado.setId("div_empleado");
        div_empleado.dividir1(pat_empleado);
        agregarComponente(div_empleado);

//        text_texto.setId("text_texto");
//        text_texto.setSize(50);
//        bar_botones.agregarComponente(text_texto);
//      
//      Boton btn_empleado = new Boton ();
//        btn_empleado.setIcon("ui-icon-search");//fa-neuter  ui-icon-newwin
//        btn_empleado.setValue("BUSCAR");
//        btn_empleado.setTitle("BUSCAR");
//        bar_botones.agregarBoton(btn_empleado);    
//        btn_empleado.setMetodo("consultarEmpleado");
    }
//    public void consultarEmpleado(){
//        
//         TablaGenerica tabActual = adminRemuneracion.getVerificaDatos(text_texto.getValue().toString());
//         utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo ExtraOrdinario Pendiente");
//    }

    public void buscarPersona(SelectEvent evt) {
        autBusca.onSelect(evt);
        if (autBusca.getValor() != null) {
            tab_empleado.setFilaActual(autBusca.getValor());
            utilitario.addUpdate("tab_empleado");
        }
    }

    @Override
    public void insertar() {
        tab_empleado.insertar();
    }

    @Override
    public void guardar() {
        tab_empleado.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_empleado.eliminar();
    }

    public Tabla getTab_empleado() {
        return tab_empleado;
    }

    public void setTab_empleado(Tabla tab_empleado) {
        this.tab_empleado = tab_empleado;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

}

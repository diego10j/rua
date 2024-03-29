package paq_adquisicion;

/**
 *
 * @author Andres
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class Empleado extends Pantalla {

    private Tabla tab_empleado = new Tabla();
    private Tabla tab_caja_empleado = new Tabla();
    private Tabla tab_empleado_dat_factura = new Tabla();
    private Texto text_texto = new Texto();
    private AutoCompletar autBusca = new AutoCompletar();
    private SeleccionTabla sel_tab_empleado = new SeleccionTabla();
    private Combo com_empleado=new Combo();

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);


    public Empleado() {

      /* autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT IDE_ADEMPLE,CEDULA_ADEMPLE,NOMBRES_ADEMPLE,DIRECCION_ADEMPLE from ADQ_EMPLEADO order by NOMBRES_ADEMPLE");
        autBusca.setSize(70);
        autBusca.setMetodoChange("buscaEmpleado");
        bar_botones.agregarComponente(new Etiqueta("Busca Empleado : "));
        bar_botones.agregarComponente(autBusca);*/
List lista = new ArrayList();
	       Object fila1[] = {
	           "1", "INTERNO"
	       };
	       Object fila2[] = {
	           "0", "EXTERNO"
	       };
	       
	       lista.add(fila1);
	       lista.add(fila2);
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
        tab_empleado.getColumna("tipo_persona_ademple").setCombo(lista);         
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
        tab_empleado.agregarRelacion(tab_caja_empleado);
        tab_empleado.agregarRelacion(tab_empleado_dat_factura);
        tab_empleado.dibujar();
        
        PanelTabla pat_empleado = new PanelTabla();
        pat_empleado.setId("pat_empleado");
        pat_empleado.setPanelTabla(tab_empleado);
        
        Boton bot_agregar_solicitante = new Boton();
        Boton bot_anular = new Boton();
        bot_anular.setIcon("ui-icon-search");
        bot_anular.setValue("IMPORTAR EMPLEADO");
        bot_anular.setMetodo("validarcombo");
        agregarComponente(bot_anular);
        bar_botones.agregarBoton(bot_anular);
        
        sel_tab_empleado.setId("sel_tab_empleado");
        sel_tab_empleado.setTitle("EMPLEADO");
        sel_tab_empleado.setSeleccionTabla(ser_adquisiciones.getDatosEmpleado(), "ide_gtemp");
        sel_tab_empleado.setWidth("80%");
        sel_tab_empleado.setHeight("70%");
        sel_tab_empleado.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltroContenido();
        sel_tab_empleado.getTab_seleccion().getColumna("nombres_empleado").setFiltroContenido();
        sel_tab_empleado.getBot_aceptar().setMetodo("aceptarEmpleado");
        agregarComponente(sel_tab_empleado);

        
        tab_caja_empleado.setId("tab_caja_empleado");   //identificador
        tab_caja_empleado.setIdCompleto("tab_tabulador:tab_caja_empleado");
        tab_caja_empleado.setTabla("cxc_caja_empleado", "ide_cxcaem", 2);
        tab_caja_empleado.getColumna("ide_cocaj").setCombo(ser_adquisiciones.getTipoCaja());
        tab_caja_empleado.dibujar();
        
        PanelTabla pat_caja_empleado = new PanelTabla();
        pat_caja_empleado.setId("pat_caja_empleado");
        pat_caja_empleado.setPanelTabla(tab_caja_empleado);
        
        tab_empleado_dat_factura.setId("tab_empleado_dat_factura");   //identificador
        tab_empleado_dat_factura.setIdCompleto("tab_tabulador:tab_empleado_dat_factura");
        tab_empleado_dat_factura.setTabla("CXC_DAT_FAC_EMP", "IDE_CXDAEM", 3);
        tab_empleado_dat_factura.getColumna("IDE_CCDAF").setCombo(ser_adquisiciones.getDatosFactura());
        tab_empleado_dat_factura.dibujar();
        
        PanelTabla pat_empleado_dat_factura = new PanelTabla();
        pat_empleado_dat_factura.setId("pat_empleado_dat_factura");
        pat_empleado_dat_factura.setPanelTabla(tab_empleado_dat_factura);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");        
        tab_tabulador.agregarTab("CAJA EMPLEADO", pat_caja_empleado);
        tab_tabulador.agregarTab("PUNTO DE EMISIÒN", pat_empleado_dat_factura);
        
        Division div_empleado = new Division();
        div_empleado.setId("div_empleado");
        div_empleado.dividir2(pat_empleado, tab_tabulador, "60%", "H");
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
    String emple="";
    public void buscarPersona(SelectEvent evt) {
        autBusca.onSelect(evt);
        if (autBusca.getValor() != null) {
            tab_empleado.setFilaActual(autBusca.getValor());
            utilitario.addUpdate("tab_empleado");
        }
    }
    public void validarcombo() {
        //Valida que la factura este validar combo
        
        String smv = tab_empleado.getValor("tipo_persona_ademple");
        if (smv.equals("1")) {
            sel_tab_empleado.dibujar();
        }
        else{
            utilitario.agregarNotificacionInfo("Aviso","Debe ingresar los datos del empleado nuevo");
        }
        }
   /* publicoid abrirDialogoEmpleado(){
        sel_tab_empleado.dibujar();
    }
    */
    public void aceptarEmpleado(){
         String str_emple = sel_tab_empleado.getSeleccionados();
        
        TablaGenerica tab_dat_emple = utilitario.consultar(ser_adquisiciones.getDatosEmpleadoConsulta(str_emple));
        for (int i=0;i<tab_dat_emple.getTotalFilas();i++){
            if (tab_empleado.isFilaInsertada()==false){
                tab_empleado.insertar();
            }
            
		tab_empleado.setValor("ide_gtemp",tab_dat_emple.getValor(i, "ide_gtemp"));
                tab_empleado.setValor("ide_adtide",null);
		tab_empleado.setValor("ide_usua",null);
		tab_empleado.setValor("cedula_ademple",tab_dat_emple.getValor(i,"documento_identidad_gtemp"));
                tab_empleado.setValor("nombres_ademple",tab_dat_emple.getValor(i,"nombres_empleado"));
                tab_empleado.setValor("direccion_ademple",null);
               // tab_empleado.setValor("firma_ademple",null);	
                tab_empleado.getColumna("FIRMA_ADEMPLE").setUpload();
                tab_empleado.getColumna("FIRMA_ADEMPLE").setImagen();
	       }
            // tab_empleado.guardar();
            // guardarPantalla();
             sel_tab_empleado.cerrar();
	     utilitario.addUpdate("tab_empleado");
    }

    @Override
    public void insertar() {
        if (tab_empleado.isFocus()){
            tab_empleado.insertar();
        }
        else if (tab_caja_empleado.isFocus()){
            tab_caja_empleado.insertar();
        }
        else if (tab_empleado_dat_factura.isFocus()){
            tab_empleado_dat_factura.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_empleado.isFocus()){
            tab_empleado.guardar();
        }
        else if (tab_caja_empleado.isFocus()){
            tab_caja_empleado.guardar();
        }
        else if (tab_empleado_dat_factura.isFocus()){
            tab_empleado_dat_factura.guardar();
        }
        else if (tab_empleado_dat_factura.isFocus()){
            tab_empleado_dat_factura.guardar();
        }
        guardarPantalla();
            
    }

    @Override
    public void eliminar() {
        if (tab_empleado.isFocus()){
            tab_empleado.eliminar();
        }
        else if (tab_caja_empleado.isFocus()){
            tab_caja_empleado.eliminar();
        }
        else if (tab_empleado_dat_factura.isFocus()){
            tab_empleado_dat_factura.eliminar();
        }
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

    public Texto getText_texto() {
        return text_texto;
    }

    public void setText_texto(Texto text_texto) {
        this.text_texto = text_texto;
    }

    public SeleccionTabla getSel_tab_empleado() {
        return sel_tab_empleado;
    }

    public void setSel_tab_empleado(SeleccionTabla sel_tab_empleado) {
        this.sel_tab_empleado = sel_tab_empleado;
    }

    public String getEmple() {
        return emple;
    }

    public void setEmple(String emple) {
        this.emple = emple;
    }

    public Tabla getTab_caja_empleado() {
        return tab_caja_empleado;
    }

    public void setTab_caja_empleado(Tabla tab_caja_empleado) {
        this.tab_caja_empleado = tab_caja_empleado;
    }

    public Tabla getTab_empleado_dat_factura() {
        return tab_empleado_dat_factura;
    }

    public void setTab_empleado_dat_factura(Tabla tab_empleado_dat_factura) {
        this.tab_empleado_dat_factura = tab_empleado_dat_factura;
    }
  
}

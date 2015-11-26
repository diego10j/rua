/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_beans;

import framework.aplicacion.TablaGenerica;
import framework.componentes.BuscarTabla;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.ErrorSQL;
import framework.componentes.Etiqueta;
import framework.componentes.FormatoTabla;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.ImportarTabla;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.Menu;
import framework.componentes.MetodoRemoto;
import framework.componentes.Notificacion;
import framework.componentes.Radio;
import framework.componentes.SeleccionArchivo;
import framework.componentes.TerminalTabla;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
@ManagedBean
@SessionScoped
public class pre_index {

    private HtmlForm formulario = new HtmlForm();
    private Grupo dibuja = new Grupo();
    private Object clase;
    private HtmlInputHidden ith_alto = new HtmlInputHidden(); //Alto disponible
    private HtmlInputHidden alto = new HtmlInputHidden(); //Alto Browser
    private HtmlInputHidden ancho = new HtmlInputHidden();//Ancho Browser
    private Utilitario utilitario = new Utilitario();
    private Grupo mensajes = new Grupo();
    private ErrorSQL error_sql = new ErrorSQL();
    private BuscarTabla bus_buscar;
    private TerminalTabla term_tabla;
    private String str_paquete;
    private String str_tipo;
    private String str_manual;
    private String str_titulo = "Inicio";
    private FormatoTabla fot_formato;
    private ImportarTabla imt_importar;
    private Dialogo dia_sucu_usuario;
    private Radio rad_sucursales;

    private SeleccionArchivo sar_upload;

    public pre_index() {
        if (utilitario.getConexion() != null && utilitario.getVariable("NICK") != null) {
            dibuja.setId("dibuja");
            dibuja.setStyleClass("ui-layout-unit-content ui-widget-content");
            dibuja.setTransient(true);
            formulario.setTransient(true);
            dibuja.setDibuja(true);
            Menu menu = new Menu();
            menu.dibujar();
            menu.setId("menu");
            formulario.getChildren().add(menu);
            formulario.getChildren().add(dibuja);
            ith_alto.setId("ith_alto");
            formulario.getChildren().add(ith_alto);
            alto.setId("alto");
            formulario.getChildren().add(alto);
            ancho.setId("ancho");
            formulario.getChildren().add(ancho);
            utilitario.crearVariable("ALTO_PANTALLA", "1000");

            mensajes.setId("mensajes");
            mensajes.setTransient(true);
            bus_buscar = new BuscarTabla();
            bus_buscar.setId("bus_buscar");
            mensajes.getChildren().add(bus_buscar);

            term_tabla = new TerminalTabla();
            mensajes.getChildren().add(term_tabla);

            fot_formato = new FormatoTabla();
            mensajes.getChildren().add(fot_formato);

            sar_upload = new SeleccionArchivo();
            mensajes.getChildren().add(sar_upload);

            imt_importar = new ImportarTabla();
            mensajes.getChildren().add(imt_importar);
            error_sql.setId("error_sql");
            mensajes.getChildren().add(error_sql);
            formulario.getChildren().add(mensajes);
            Notificacion not_notificacion = new Notificacion();
            formulario.getChildren().add(not_notificacion);
            Confirmar con_guarda = new Confirmar();
            con_guarda.setId("con_guarda");
            con_guarda.setWidgetVar("con_guarda");
            con_guarda.setMessage("Está seguro que desea guardar?");
            con_guarda.getBot_aceptar().setOncomplete("con_guarda.hide();");
            con_guarda.getBot_aceptar().setMetodo("guardar");
            con_guarda.getBot_cancelar().setOnclick("con_guarda.hide();");

            formulario.getChildren().add(con_guarda);

            TablaGenerica tab_sucursales = utilitario.consultar("select sis_usuario_sucursal.sis_ide_sucu,ide_empr,nom_sucu "
                    + "from sis_usuario_sucursal "
                    + "INNER JOIN sis_sucursal on sis_usuario_sucursal.sis_ide_sucu=sis_sucursal.ide_sucu "
                    + "where ide_usua=" + utilitario.getVariable("IDE_USUA"));
            if (tab_sucursales.getTotalFilas() == 1) {
                seleccionarSucursal(tab_sucursales.getValor(0, "sis_ide_sucu"));
            } else {
                //Dialogo para seleccionar sucursales  
                dia_sucu_usuario = new Dialogo();
                dia_sucu_usuario.setId("dia_sucu_usuario");
                dia_sucu_usuario.setTitle("Seleccione una Sucursal");
                dia_sucu_usuario.setWidth("30%");
                dia_sucu_usuario.setHeight("30%");
                dia_sucu_usuario.getBot_cancelar().setMetodoRuta("pre_login.salir");
                dia_sucu_usuario.getBot_aceptar().setMetodoRuta("pre_index.aceptarSucursal");
                Grid gri_sucu = new Grid();
                gri_sucu.setStyle("font-size:15px;width:" + (dia_sucu_usuario.getAnchoPanel() - 10) + "px;height:" + (dia_sucu_usuario.getAltoPanel() - 50) + "px;overflow: auto;display: block;");

                if (tab_sucursales.getTotalFilas() > 0) {
                    List lis_sucu = new ArrayList();
                    for (int i = 0; i < tab_sucursales.getTotalFilas(); i++) {
                        Object[] obj = new Object[2];
                        obj[0] = tab_sucursales.getValor(i, "sis_ide_sucu");
                        obj[1] = tab_sucursales.getValor(i, "nom_sucu");
                        lis_sucu.add(obj);
                    }
                    rad_sucursales = new Radio();
                    rad_sucursales.setRadio(lis_sucu);
                    rad_sucursales.setVertical();
                    gri_sucu.getChildren().add(rad_sucursales);
                } else {
                    Etiqueta eti_no_sucursal = new Etiqueta();
                    eti_no_sucursal.setValue("No tiene sucursales asignadas, contactese con el administrador del sistema");
                    eti_no_sucursal.setStyle("border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
                    gri_sucu.getChildren().add(eti_no_sucursal);
                }
                dia_sucu_usuario.setDialogo(gri_sucu);
                dia_sucu_usuario.dibujar();
                formulario.getChildren().add(dia_sucu_usuario);
            }
            Link lin_salir = new Link();
            lin_salir.agregarImagen("imagenes/im_salir_sistema.png", "32", "32");
            lin_salir.setMetodoRuta("pre_login.salir");
            lin_salir.setTitle("Cerrar Sesión");
            lin_salir.setStyle("position:fixed;right:2px;top:1px;");
            formulario.getChildren().add(lin_salir);

        }
    }

    public void aceptarSucursal() {
        if (rad_sucursales.getValue() != null) {
            seleccionarSucursal(rad_sucursales.getValue() + "");
            dia_sucu_usuario.cerrar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione Sucursa", "Debe seleccionar una sucursal para iniciar el sistema");
        }
    }

    private void seleccionarSucursal(String ide_sucu) {
        TablaGenerica tab_sucursal = utilitario.consultar("SELECT * FROM SIS_SUCURSAL WHERE IDE_SUCU=" + ide_sucu);
        utilitario.crearVariable("IDE_EMPR", tab_sucursal.getValor(0, "IDE_EMPR"));
        utilitario.crearVariable("IDE_SUCU", ide_sucu);
        Etiqueta eti_sucursal = new Etiqueta();
        Imagen ima_empresa = new Imagen();
        eti_sucursal.setValue(tab_sucursal.getValor(0, "NOM_SUCU") + " <br/><br/>");
        ima_empresa.setValue("imagenes/logo.png");
        dibuja.getChildren().clear();
        Grupo gru_empresa = new Grupo();
        gru_empresa.setStyle("width: 100%;font-size: 30px;text-align: center;font-weight: bold;");
        eti_sucursal.setStyle("font-style: italic;border: none;text-shadow: 0px 2px 3px #ccc;background: none;text-transform: uppercase");
        gru_empresa.getChildren().add(eti_sucursal);
        gru_empresa.getChildren().add(ima_empresa);
        dibuja.getChildren().add(gru_empresa);
        utilitario.addUpdate("dibuja");
    }

    public void cambiarSucursal() {
        if (utilitario.getComponente("dia_sucu_usuario") != null) {
            rad_sucursales.setValue(utilitario.getVariable("IDE_SUCU"));
            dia_sucu_usuario.dibujar();
            dia_sucu_usuario.getBot_cancelar().setActionExpression(null);
            dia_sucu_usuario.getBot_cancelar().setActionListenerRuta("pre_index.cerrarDialogo");
        }
    }

    public void cargar(ActionEvent evt) {
        utilitario.crearVariable("IDE_OPCI", ((ItemMenu) evt.getComponent()).getCodigo());
        str_titulo = ((ItemMenu) evt.getComponent()).getValue() + "";
        utilitario.crearVariable("ALTO_PANTALLA", ith_alto.getValue() + "");  //Alto disponible
        utilitario.crearVariable("ALTO", alto.getValue() + "");  //Alto browser
        utilitario.crearVariable("ANCHO", ancho.getValue() + ""); //Ancho browser    
        utilitario.crearVariable("OPCION", str_titulo); //Opcion
        buscarOpcion();
        mensajes.getChildren().clear();
        mensajes.getChildren().add(error_sql);
        mensajes.getChildren().add(bus_buscar);
        mensajes.getChildren().add(term_tabla);
        mensajes.getChildren().add(fot_formato);
        mensajes.getChildren().add(imt_importar);
        mensajes.getChildren().add(sar_upload);
        dibuja.getChildren().clear();
        utilitario.getConexion().setSqlPantalla(new ArrayList<String>());
        clase = null;
        try {
            try {
                Runtime basurero = Runtime.getRuntime();
                basurero.gc();
            } catch (Exception e) {
            }
            Class pantalla = Class.forName(str_paquete + "." + str_tipo);
            clase = pantalla.newInstance();
            utilitario.buscarPermisosObjetos();
        } catch (Exception ex) {
            utilitario.crearError(ex.getMessage(), "pre_index en el método cargar() ", ex);
        }
        //Tacla Abajo y Arriba
        MetodoRemoto mer_abajo = new MetodoRemoto();
        mer_abajo.setName("teclaAbajo");
        mer_abajo.setMetodo("siguiente");
        dibuja.getChildren().add(mer_abajo);

        MetodoRemoto mer_arriba = new MetodoRemoto();
        mer_arriba.setName("teclaArriba");
        mer_arriba.setMetodo("atras");
        dibuja.getChildren().add(mer_arriba);

    }

    private void buscarOpcion() {
        TablaGenerica tab_opcion = utilitario.consultar("SELECT PAQUETE_OPCI,TIPO_OPCI,AUDITORIA_OPCI,MANUAL_OPCI FROM SIS_OPCION WHERE IDE_OPCI=" + utilitario.getVariable("IDE_OPCI"));
        if (tab_opcion.getTotalFilas() > 0) {
            str_paquete = tab_opcion.getValor(0, "PAQUETE_OPCI");
            str_tipo = tab_opcion.getValor(0, "TIPO_OPCI");
            if (tab_opcion.getValor(0, "AUDITORIA_OPCI") != null) {
                utilitario.crearVariable("AUDITORIA_OPCI", tab_opcion.getValor(0, "AUDITORIA_OPCI"));
            } else {
                utilitario.crearVariable("AUDITORIA_OPCI", "false");
            }
            str_manual = tab_opcion.getValor(0, "MANUAL_OPCI");
        }
    }

    public void cerrarDialogo(ActionEvent evt) {
        UIComponent com_padre = evt.getComponent();
        while (com_padre != null) {
            com_padre = com_padre.getParent();
            if (com_padre.getRendererType() != null && com_padre.getRendererType().equals("org.primefaces.component.DialogRenderer")) {
                break;
            }
        }
        if (com_padre != null) {
            ((Dialogo) com_padre).cerrar();
        }
    }

    public void cerrarConfirmar(ActionEvent evt) {
        UIComponent com_padre = evt.getComponent();
        while (com_padre != null) {
            com_padre = com_padre.getParent();
            String str_render = com_padre.getRendererType();
            if (str_render != null && str_render.equals("org.primefaces.component.ConfirmDialogRenderer")) {
                break;
            }
        }
        if (com_padre != null) {
            ((Confirmar) com_padre).cerrar();
        }
    }

    public void cargar_inicio() {
        utilitario.crearVariable("ALTO_PANTALLA", ith_alto.getValue() + ""); //Alto disponible
        utilitario.crearVariable("ALTO", alto.getValue() + "");  //Alto browser
        utilitario.crearVariable("ANCHO", ancho.getValue() + ""); //Ancho browser        
        utilitario.crearVariable("IDE_OPCI", "-1");
        str_titulo = "Inicio";
        mensajes.getChildren().clear();
        dibuja.getChildren().clear();
        clase = null;
        try {
            try {
                Runtime basurero = Runtime.getRuntime();
                basurero.gc();
            } catch (Exception e) {
            }
            Class pantalla = Class.forName("paq_sistema.pre_principal");
            clase = pantalla.newInstance();
            utilitario.buscarPermisosObjetos();
        } catch (Exception ex) {
            utilitario.crearError(ex.getMessage(), "pre_index en el método cargar_inicio() ", ex);
        }
    }

    public void cerrarSql() {
        error_sql.setVisible(false);
        error_sql.limpiar();
    }

    public void ayuda() {
        //Cargar la ayuda de la pantalla     
        if (str_manual != null) {
            utilitario.ejecutarJavaScript("window.open('" + utilitario.getURL() + "/manuales/" + str_paquete + "/" + str_manual + "','nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=800,height=600')");
        }
    }

    public HtmlForm getFormulario() {
        return formulario;
    }

    public void setFormulario(HtmlForm formulario) {
        this.formulario = formulario;
    }

    public Object getClase() {
        return clase;
    }

    public void setClase(Object clase) {
        this.clase = clase;
    }

    public BuscarTabla getBus_buscar() {
        return bus_buscar;
    }

    public void setBus_buscar(BuscarTabla bus_buscar) {
        this.bus_buscar = bus_buscar;
    }

    public String getStr_titulo() {
        return str_titulo;
    }

    public void setStr_titulo(String str_titulo) {
        this.str_titulo = str_titulo;
    }

    public ImportarTabla getImt_importar() {
        return imt_importar;
    }

    public void setImt_importar(ImportarTabla imt_importar) {
        this.imt_importar = imt_importar;
    }

    public FormatoTabla getFot_formato() {
        return fot_formato;
    }

    public void setFot_formato(FormatoTabla fot_formato) {
        this.fot_formato = fot_formato;
    }

    public Dialogo getDia_sucu_usuario() {
        return dia_sucu_usuario;
    }

    public void setDia_sucu_usuario(Dialogo dia_sucu_usuario) {
        this.dia_sucu_usuario = dia_sucu_usuario;
    }

    public TerminalTabla getTerm_tabla() {
        return term_tabla;
    }

    public void setTerm_tabla(TerminalTabla term_tabla) {
        this.term_tabla = term_tabla;
    }

    public SeleccionArchivo getSar_upload() {
        return sar_upload;
    }

    public void setSar_upload(SeleccionArchivo sar_upload) {
        this.sar_upload = sar_upload;
    }

}

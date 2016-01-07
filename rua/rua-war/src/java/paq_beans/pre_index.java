/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_beans;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
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
import framework.componentes.MenuLista;
import framework.componentes.MetodoRemoto;
import framework.componentes.Notificacion;
import framework.componentes.SeleccionArchivo;
import framework.componentes.TerminalTabla;
import framework.componentes.bootstrap.Alerta;
import framework.componentes.bootstrap.BotonBootstrap;
import framework.componentes.bootstrap.CajaBootstrap;
import framework.componentes.bootstrap.ContenidoBootstrap;
import framework.componentes.bootstrap.GrupoBootstrap;
import framework.componentes.bootstrap.ListaBootstrap;
import framework.componentes.bootstrap.PanelBootstrap;
import framework.componentes.bootstrap.RowBootstrap;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import servicios.sistema.ServicioSeguridad;
import servicios.sistema.ServicioSistema;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
@ManagedBean
@SessionScoped
public class pre_index {

    private HtmlForm formulario = new HtmlForm();
    private final Grupo dibuja = new Grupo();
    private Object clase;
    private final HtmlInputHidden ith_alto = new HtmlInputHidden(); //Alto disponible
    private final HtmlInputHidden alto = new HtmlInputHidden(); //Alto Browser
    private final HtmlInputHidden ancho = new HtmlInputHidden();//Ancho Browser
    private final Utilitario utilitario = new Utilitario();
    private final Grupo mensajes = new Grupo();
    private final ErrorSQL error_sql = new ErrorSQL();
    private BuscarTabla bus_buscar;
    private TerminalTabla term_tabla;
    private String str_paquete;
    private String str_tipo;
    private String str_manual;
    private String str_titulo = "Inicio";
    private FormatoTabla fot_formato;
    private ImportarTabla imt_importar;
    private Dialogo dia_sucu_usuario;
    private MenuLista mel_sucursales;
    private SeleccionArchivo sar_upload;
    private StreamedContent logo;
    private AutoCompletar aut_pantalla = new AutoCompletar();

    @EJB
    private ServicioSistema ser_sistema;
    @EJB
    private ServicioSeguridad ser_seguridad;

    @PostConstruct
    public void cargaSucursalesUsuario() {
        if (utilitario.getConexion() != null && utilitario.getVariable("NICK") != null) {
            TablaGenerica tab_sucursales = ser_seguridad.getSucursalesUsuario();

            if (tab_sucursales.getTotalFilas() == 1) {
                seleccionarSucursal(tab_sucursales.getValor(0, "sis_ide_sucu"));
            } else {
                //Dialogo para seleccionar sucursales  
                dia_sucu_usuario = new Dialogo();
                dia_sucu_usuario.setId("dia_sucu_usuario");
                dia_sucu_usuario.setTitle("Seleccione una Sucursal");
                dia_sucu_usuario.setWidth("40%");
                dia_sucu_usuario.setHeight("30%");
                dia_sucu_usuario.getBot_cancelar().setMetodoRuta("pre_login.salir");
                dia_sucu_usuario.getBot_aceptar().setRendered(false);
                Grid gri_sucu = new Grid();
                gri_sucu.setStyle("font-size:15px;width:" + (dia_sucu_usuario.getAnchoPanel() - 5) + "px;overflow: hidden;display: block;");

                if (tab_sucursales.getTotalFilas() > 0) {
                    List lis_sucu = new ArrayList();
                    for (int i = 0; i < tab_sucursales.getTotalFilas(); i++) {
                        Object[] obj = new Object[2];
                        obj[0] = tab_sucursales.getValor(i, "sis_ide_sucu");
                        obj[1] = tab_sucursales.getValor(i, "nom_sucu");
                        lis_sucu.add(obj);
                    }
                    mel_sucursales = new MenuLista();
                    mel_sucursales.setStyle("width:" + (dia_sucu_usuario.getAnchoPanel() - 20) + "px;" + "px;height:" + (dia_sucu_usuario.getAltoPanel() - 25) + "px;overflow: hidden;display: block;");
                    mel_sucursales.setActionListenerRuta("pre_index.aceptarSucursal");
                    mel_sucursales.setMenuLista(lis_sucu);
                    gri_sucu.getChildren().add(mel_sucursales);
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
        }

    }

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

            Link lin_salir = new Link();
            lin_salir.agregarImagen("imagenes/im_salir_sistema.png", "32", "32");
            lin_salir.setMetodoRuta("pre_login.salir");
            lin_salir.setTitle("Cerrar Sesión");
            lin_salir.setStyle("position:fixed;right:2px;top:1px;");
            formulario.getChildren().add(lin_salir);

            aut_pantalla.setId("aut_pantalla");
            aut_pantalla.setStyleClass("autocompletar-index");
            aut_pantalla.setRuta("pre_index");
            aut_pantalla.setMetodoChangeRuta("pre_index.seleccionarPantalla");

        }
    }

    /**
     * Acepta la sucursal seleccionada de la lista de seleccion
     *
     * @param evt
     */
    public void aceptarSucursal(ActionEvent evt) {
        seleccionarSucursal(((ItemMenu) evt.getComponent()).getCodigo() + "");
        dia_sucu_usuario.cerrar();
    }

    /**
     * Selecciona la sucursal
     *
     * @param ide_sucu
     */
    private void seleccionarSucursal(String ide_sucu) {
        dibuja.setStyle("width: 100%;overflow-x: hidden;overflow-y: auto;");

        aut_pantalla.setAutoCompletar(ser_sistema.getSqlPantallasPerfil(utilitario.getVariable("IDE_PERF")));
        aut_pantalla.limpiar();

        TablaGenerica tab_sucursal = ser_sistema.getSucursal(ide_sucu);
        utilitario.crearVariable("IDE_EMPR", tab_sucursal.getValor(0, "IDE_EMPR"));
        utilitario.crearVariable("IDE_SUCU", ide_sucu);
        TablaGenerica tab_empresa = ser_sistema.getEmpresa();
        TablaGenerica tab_usuario = ser_sistema.getUsuario();
        TablaGenerica tab_perfil = ser_sistema.getPerfil();
        TablaGenerica tab_ultimo_acceso = ser_seguridad.getUltimoAccesoUsuario(utilitario.getVariable("ide_usua"));

        logo = ser_sistema.getLogoEmpresa();
        dibuja.getChildren().clear();

        Alerta ale_inicio = new Alerta();
        ale_inicio.setAlertaCeleste("<strong>Bienvenido </strong> al Sistema Contable Financiero RUA v2.0 <span class='pull-right'> " + utilitario.getFechaLarga(utilitario.getFechaActual()) + " &nbsp;  </span>");
        dibuja.getChildren().add(ale_inicio);

        RowBootstrap row_cajas = new RowBootstrap();
        dibuja.getChildren().add(row_cajas);

        CajaBootstrap cb1 = new CajaBootstrap();
        cb1.setCajaBootstrap("USUARIO", tab_usuario.getValor("nom_usua").toUpperCase());
        cb1.setIcono("fa fa-user", "bg-blue");
        row_cajas.getChildren().add(cb1);

        CajaBootstrap cb2 = new CajaBootstrap();
        cb2.setCajaBootstrap("PERFIL", tab_perfil.getValor("nom_perf").toUpperCase());
        cb2.setIcono("fa fa-users", "bg-aqua");
        row_cajas.getChildren().add(cb2);

        CajaBootstrap cb3 = new CajaBootstrap();
        cb3.setCajaBootstrap("DIRECCIÓN IP", utilitario.getIp());
        cb3.setIcono("fa fa-laptop", "bg-green");
        row_cajas.getChildren().add(cb3);

        CajaBootstrap cb4 = new CajaBootstrap();
        String str_ultimo_acceso = "";
        if (!tab_ultimo_acceso.isEmpty()) {
            str_ultimo_acceso = utilitario.getFormatoFecha(utilitario.getFecha(tab_ultimo_acceso.getValor("fecha_auac")), "dd-MM-yyyy") + " </br> " + tab_ultimo_acceso.getValor("hora_auac");
        }
        cb4.setCajaBootstrap("ÚLTIMO ACCESO", str_ultimo_acceso);
        cb4.setIcono("fa fa-calendar", "bg-yellow");
        row_cajas.getChildren().add(cb4);

        RowBootstrap row_util = new RowBootstrap();
        dibuja.getChildren().add(row_util);
        ContenidoBootstrap cb_izquierda = new ContenidoBootstrap("col-md-6");
        row_util.getChildren().add(cb_izquierda);

        PanelBootstrap pb_empresa = new PanelBootstrap();
        cb_izquierda.getChildren().add(pb_empresa);
        pb_empresa.setPanelVerde();
        pb_empresa.setTitulo(tab_empresa.getValor("nom_empr"));
        pb_empresa.agregarComponenteContenido(new Etiqueta("<h3  style='font-weight: bold;text-align:center'>" + tab_sucursal.getValor("nom_sucu") + "</h3> <p align='center'>"));
        Imagen ima_empresa = new Imagen();
        ima_empresa.setValueExpression("value", "pre_index.logo");
        ima_empresa.setStyleClass("img-responsive");
        pb_empresa.agregarComponenteContenido(ima_empresa);
        pb_empresa.agregarComponenteContenido(new Etiqueta("</p>"));
        if (dia_sucu_usuario != null) {
            BotonBootstrap bb_cambia = new BotonBootstrap();
            bb_cambia.setMetodoRuta("pre_index.cambiarSucursal");
            bb_cambia.setValue("Cambiar Sucursal");
            bb_cambia.setBotonVerde();
            bb_cambia.setValueExpression("rendered", "pre_index.dia_sucu_usuario !=null");
            pb_empresa.agregarComponenteFooter(bb_cambia);

        }

        ContenidoBootstrap cb_derecha = new ContenidoBootstrap("col-md-6");
        row_util.getChildren().add(cb_derecha);

        PanelBootstrap pb_busca_pantalla = new PanelBootstrap();
        cb_derecha.getChildren().add(pb_busca_pantalla);
        pb_busca_pantalla.setPanelNaranja();
        pb_busca_pantalla.setTitulo("BUSCAR PANTALLA");

        GrupoBootstrap grb_abrir = new GrupoBootstrap();
        grb_abrir.setAutocompletar(aut_pantalla);
        BotonBootstrap bot_abrir = new BotonBootstrap();
        bot_abrir.setId("bot_buscaPantalla");
        bot_abrir.setBotonAzul();
        bot_abrir.setBotonUpdate("dibuja,:fortitulo");
        bot_abrir.setOnclick("dimensionesDisponibles()");
        bot_abrir.setActionListenerRuta("pre_index.cargar");
        bot_abrir.setValue("Abrir");
        grb_abrir.setBotonBootstrap(bot_abrir);
        pb_busca_pantalla.agregarComponenteContenido(grb_abrir);

        PanelBootstrap pb_pantallas = new PanelBootstrap();
        pb_pantallas.setPanelAzul();
        pb_pantallas.setTitulo("PANTALLAS MÁS USADAS");
        cb_derecha.getChildren().add(pb_pantallas);

        ListaBootstrap lib_pantallas = new ListaBootstrap();
        lib_pantallas.setActionListenerRuta("pre_index.cargar");
        lib_pantallas.setUpdate("dibuja,:fortitulo");
        lib_pantallas.setOnClick("dimensionesDisponibles()");
        lib_pantallas.setListaBootstrap(ser_seguridad.getSqlPantallasMasUsadas(utilitario.getVariable("ide_usua")));
        pb_pantallas.agregarComponenteContenido(lib_pantallas);

        utilitario.addUpdate("dibuja");
    }

    /**
     * Abre el dialgo de seleccionar sucursal
     */
    public void cambiarSucursal() {
        if (utilitario.getComponente("dia_sucu_usuario") != null) {
            dia_sucu_usuario.dibujar();
            dia_sucu_usuario.getBot_cancelar().setActionExpression(null);
            dia_sucu_usuario.getBot_cancelar().setActionListenerRuta("pre_index.cerrarDialogo");
        }
    }

    /**
     * Dibuja la pantalla seleccionada
     *
     * @param evt
     */
    public void cargar(ActionEvent evt) {
        dibuja.setStyle("");
        if (evt.getComponent().getRendererType() == null) { //ItemMenu
            utilitario.crearVariable("IDE_OPCI", ((ItemMenu) evt.getComponent()).getCodigo());
            str_titulo = ((ItemMenu) evt.getComponent()).getValue() + "";
        } else {
            utilitario.crearVariable("IDE_OPCI", ((Link) evt.getComponent()).getCodigo());
            str_titulo = ((Link) evt.getComponent()).getValue() + "";
        }
        if (utilitario.getVariable("IDE_OPCI") == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una Pantalla", "");
            return;
        }
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
            ser_seguridad.abrioPantalla();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
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

    /**
     * Busca configuración de una Pantalla Seleccionada
     */
    private void buscarOpcion() {
        TablaGenerica tab_opcion = ser_sistema.getOpcionPantalla();
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

    /**
     * Metodo génerico que cierra un Dialogo abierto
     *
     * @param evt
     */
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

    /**
     * Cierra el Dialogo de Confirmar
     *
     * @param evt
     */
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

    /**
     * Carga pantalla de inicio
     */
    public void cargar_inicio() {
        dibuja.setStyle("");
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            utilitario.crearError(ex.getMessage(), "pre_index en el método cargar_inicio() ", ex);
        }
    }

    /**
     * Cierra el Dialogo de Errores SQL
     */
    public void cerrarSql() {
        error_sql.setVisible(false);
        error_sql.limpiar();
    }

    /**
     * Abre la pantalla de ayuda si tiene configuracion
     */
    public void ayuda() {
        //Cargar la ayuda de la pantalla     
        if (str_manual != null) {
            utilitario.ejecutarJavaScript("window.open('" + utilitario.getURL() + "/manuales/" + str_paquete + "/" + str_manual + "','nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=800,height=600')");
        }
    }

    /**
     * Cuando selecciona una Pantalla en el Autocompletar
     *
     * @param evt
     */
    public void seleccionarPantalla(SelectEvent evt) {
        aut_pantalla.onSelect(evt);
        try {
            ((BotonBootstrap) utilitario.getComponente("bot_buscaPantalla")).setValue(aut_pantalla.getNombre());
            ((BotonBootstrap) utilitario.getComponente("bot_buscaPantalla")).setCodigo(aut_pantalla.getValor());
        } catch (Exception e) {
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

    public StreamedContent getLogo() {
        return logo;
    }

    public void setLogo(StreamedContent logo) {
        this.logo = logo;
    }

    public AutoCompletar getAut_pantalla() {
        return aut_pantalla;
    }

    public void setAut_pantalla(AutoCompletar aut_pantalla) {
        this.aut_pantalla = aut_pantalla;
    }

}

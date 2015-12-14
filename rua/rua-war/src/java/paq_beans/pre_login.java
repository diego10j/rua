/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_beans;

import framework.componentes.Boton;
import framework.componentes.Clave;
import framework.componentes.Dialogo;
import framework.componentes.ErrorSQL;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import org.primefaces.component.blockui.BlockUI;
import persistencia.Conexion;
import servicios.sistema.ServicioSeguridad;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
@ManagedBean
@SessionScoped
public class pre_login {

    private HtmlForm formulario = new HtmlForm();
    private final HtmlInputText tex_usuario = new HtmlInputText();
    private final HtmlInputSecret pas_clave = new HtmlInputSecret();
    private ErrorSQL error_sql = new ErrorSQL();
    private final Utilitario utilitario = new Utilitario();
    private final Clave cla_clave_actual = new Clave();
    private final Clave cla_nueva = new Clave();
    private final Clave cla_confirmar = new Clave();
    private final Dialogo dia_cambia = new Dialogo();
    @EJB
    private ServicioSeguridad ser_seguridad;

    /**
     * Creates a new instance of pre_login
     */
    public pre_login() {
        formulario.setTransient(true);

        HtmlPanelGroup pg_usuario = new HtmlPanelGroup();
        pg_usuario.setLayout("block");
        pg_usuario.setStyleClass("form-group has-feedback");
        tex_usuario.setId("tex_usuario");
        tex_usuario.setRequired(true);
        tex_usuario.getPassThroughAttributes().put("placeholder", "Usuario");
        tex_usuario.setRequiredMessage("Debe ingresar el usuario");
        tex_usuario.setAutocomplete("off");
        tex_usuario.setStyleClass("form-control");
        HtmlOutputText eti_icono_usuario = new HtmlOutputText();
        eti_icono_usuario.setStyleClass("glyphicon glyphicon-user form-control-feedback");
        pg_usuario.getChildren().add(tex_usuario);
        pg_usuario.getChildren().add(eti_icono_usuario);

        HtmlPanelGroup pg_clave = new HtmlPanelGroup();
        pg_clave.setLayout("block");
        pg_clave.setStyleClass("form-group has-feedback");

        pas_clave.setRequired(true);
        pas_clave.setId("pas_clave");
        pas_clave.setRequiredMessage("Debe ingresar la clave");
        pas_clave.getPassThroughAttributes().put("placeholder", "Clave");
        pas_clave.setStyleClass("form-control");
        HtmlOutputText eti_icono_clave = new HtmlOutputText();
        eti_icono_clave.setStyleClass("glyphicon glyphicon-lock form-control-feedback");
        pg_clave.getChildren().add(pas_clave);
        pg_clave.getChildren().add(eti_icono_clave);

        HtmlPanelGroup pg_boton = new HtmlPanelGroup();
        pg_boton.setLayout("block");
        pg_boton.setStyleClass("row");

        Boton bot_login = new Boton();
        bot_login.setId("bot_login");
        bot_login.setValue("Iniciar Sesión");
        bot_login.setStyleClass("btn btn-primary btn-block btn-flat");
        bot_login.setMetodoRuta("pre_login.ingresar");
        bot_login.setOnclick("dimiensionesNavegador()");

        pg_boton.getChildren().add(bot_login);

        BlockUI blo_panel = new BlockUI();
        blo_panel.setBlock("formulario:bot_login");
        blo_panel.setTrigger("bot_login");
        formulario.getChildren().add(blo_panel);
        error_sql.setId("error_sql");
        error_sql.setMetodoAceptar("pre_login.cerrarSql");
        formulario.getChildren().add(error_sql);
        formulario.getChildren().add(pg_usuario);
        formulario.getChildren().add(pg_clave);
        formulario.getChildren().add(pg_boton);

        HtmlInputHidden hih_alto = new HtmlInputHidden(); //Alto Browser
        HtmlInputHidden hih_ancho = new HtmlInputHidden();//Ancho Browser     
        hih_alto.setId("alto");
        formulario.getChildren().add(hih_alto);
        hih_ancho.setId("ancho");
        formulario.getChildren().add(hih_ancho);

        //Para cambiar contraseña
        dia_cambia.setId("dia_cambia");
        dia_cambia.setTitle("Cambiar la clave");
        dia_cambia.getBot_cancelar().setMetodoRuta("pre_login.cancelarCambiarClave");
        dia_cambia.getBot_aceptar().setMetodoRuta("pre_login.aceptarCambiarClave");
        cla_nueva.setFeedback(true);
        cla_confirmar.setFeedback(true);
        cla_clave_actual.setDisabled(true);
        Grid gri_matriz2 = new Grid();
        gri_matriz2.setColumns(2);
        gri_matriz2.getChildren().add(new Etiqueta("CLAVE ACTUAL :"));
        gri_matriz2.getChildren().add(cla_clave_actual);
        gri_matriz2.getChildren().add(new Etiqueta("CLAVE NUEVA :"));
        gri_matriz2.getChildren().add(cla_nueva);
        gri_matriz2.getChildren().add(new Etiqueta("CONFIRMAR CLAVE NUEVA :"));
        gri_matriz2.getChildren().add(cla_confirmar);
        dia_cambia.setDialogo(gri_matriz2);
        formulario.getChildren().add(dia_cambia);
    }

    public void ingresar() {
        Conexion conexion = utilitario.getConexion();
        if (conexion == null) {
            conexion = new Conexion();
            String str_recursojdbc = utilitario.getPropiedad("recursojdbc");
            conexion.setUnidad_persistencia(str_recursojdbc);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CONEXION", conexion);
            utilitario.crearVariable("IDE_EMPR", utilitario.getPropiedad("ide_empr"));
        }
        String str_mensaje = ser_seguridad.ingresar(tex_usuario.getValue().toString(), pas_clave.getValue().toString());
        if (str_mensaje.isEmpty()) {
            //Valida si tiene que cambiar la clave
            if (ser_seguridad.isCambiarClave(utilitario.getVariable("IDE_USUA"))) {
                cambiarClave();
            } else {
                try {
                    utilitario.crearVariable("NICK", tex_usuario.getValue() + "");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pre_index");
                    String str_url = utilitario.getURL() + "/index.jsf";
                    utilitario.ejecutarJavaScript("abrirPopUp('" + str_url + "')");
                } catch (Exception e) {
                }
            }
        } else {
            utilitario.agregarMensajeError(str_mensaje, "");
        }
    }

    private void cambiarClave() {
        dia_cambia.setWidth("40%");
        dia_cambia.setHeight("35%");
        cla_clave_actual.setValue(pas_clave.getValue());
        cla_confirmar.setValue("");
        cla_nueva.setValue("");
        dia_cambia.dibujar();
    }

    public void aceptarCambiarClave() {
        if (cla_nueva.getValue() == null) {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la nueva clave");
            return;
        }
        if (cla_confirmar.getValue() == null) {
            utilitario.agregarMensajeInfo("Validación", "Es necesario confirmar la nueva clave");
            return;
        }

        if (!cla_nueva.getValue().toString().equals(cla_confirmar.getValue().toString())) {
            utilitario.agregarMensajeInfo("Validación", "La clave nueva debe se igual a la clave de confirmación");
            return;
        }
        if (cla_clave_actual.getValue() != null && !cla_clave_actual.getValue().toString().isEmpty()) {
            String str_mensaje = ser_seguridad.getClaveValida(cla_nueva.getValue().toString());
            if (str_mensaje.isEmpty()) {
                if (ser_seguridad.isClaveNueva(utilitario.getVariable("IDE_USUA"), cla_nueva.getValue().toString())) {
                    ser_seguridad.cambiarClave(utilitario.getVariable("IDE_USUA"), cla_nueva.getValue().toString());
                    dia_cambia.cerrar();
                    try {
                        //Registro el ingreso en auditoria
                        utilitario.getConexion().ejecutarSql(ser_seguridad.crearSQLAuditoriaAcceso(utilitario.getVariable("IDE_USUA"), ser_seguridad.P_SIS_INGRESO_USUARIO, "El sistema le solicitó a cambiar de clave"));
                        utilitario.crearVariable("NICK", tex_usuario.getValue() + ""); //Usuario                                     
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pre_index");
                        String str_url = utilitario.getURL() + "/index.jsf";
                        utilitario.ejecutarJavaScript("abrirPopUp('" + str_url + "')");
                    } catch (Exception e) {
                    }

                } else {
                    utilitario.agregarMensajeInfo("Clave no válida", "La clave ingresada ya fue utilizada anteriormente, intente con otra clave");
                }
            } else {
                utilitario.agregarMensajeInfo(str_mensaje, "");
            }

        } else {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la clave actual");
        }
    }

    public void cancelarCambiarClave() {
        dia_cambia.cerrar();
    }

    public void caducoSession() {
        try {
            if (utilitario.getConexion() != null) {
                ser_seguridad.caduco(utilitario.getVariable("IDE_USUA"));
                //utilitario.getConexion().desconectar();
            }
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            utilitario.cerrarSesion();
        } catch (Exception ex) {
        }
    }

    public void salir() {
        try {
            if (utilitario.getConexion() != null) {
                ser_seguridad.salir(utilitario.getVariable("IDE_USUA"));
                //utilitario.getConexion().desconectar();
            }
            utilitario.cerrarSesion();
            utilitario.ejecutarJavaScript("location.href='about:blank';window.close();");
        } catch (Exception ex) {
        }
    }

    public String getTema() {
        String tema = utilitario.getVariable("TEMA");
        if (tema == null || tema.isEmpty() || tema.equals("null")) {
            if (utilitario.getPropiedad("temaInicial") != null) {
                return utilitario.getPropiedad("temaInicial");
            }
            return "excite-bike";
        } else {
            return tema;
        }
    }

    public ErrorSQL getError_sql() {
        return error_sql;
    }

    public void cerrarSql() {
        error_sql.setVisible(false);
    }

    public void setError_sql(ErrorSQL error_sql) {
        this.error_sql = error_sql;
    }

    public HtmlForm getFormulario() {
        return formulario;
    }

    public void setFormulario(HtmlForm formulario) {
        this.formulario = formulario;
    }
}

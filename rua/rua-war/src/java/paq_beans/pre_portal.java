/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_beans;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Encriptar;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


import persistencia.Conexion;
import servicios.sistema.ServicioSeguridad;
import sistema.aplicacion.Utilitario;

/**
 *
 * 
 */
@ManagedBean
@SessionScoped
public class pre_portal {

    private String usuario;
    private String clave;
    private Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioSeguridad ser_seguridad;
    private boolean booCambiaClave = false;
    private String strClaveActua;
    private String strClaveNueva;
    private String strConfirmaClaveNueva;

    public void ingresar() {
        Conexion conexion = utilitario.getConexion();
        if (conexion == null) {
            conexion = new Conexion();
            String str_recursojdbc = utilitario.getPropiedad("recursojdbc");
            conexion.setUnidad_persistencia(str_recursojdbc);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CONEXION", conexion);
        }
        utilitario.crearVariable("IDE_EMPR", utilitario.getPropiedad("ide_empr"));
        utilitario.crearVariable("IDE_SUCU", "0"); //para que sea biess quito
        String str_mensaje = ser_seguridad.ingresar(usuario, clave);
        if (str_mensaje.isEmpty()) {
            //Valida si tiene que cambiar la clave
            if (ser_seguridad.isCambiarClave(utilitario.getVariable("IDE_USUA"))) {
                booCambiaClave = true;
            }

            try {
                utilitario.crearVariable("NICK", usuario);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pre_index");
                //Verificamos que tenga asociado un empleado y Creamos la variable para identificar al empleado q esta hasociado el usuario
                TablaGenerica tab_usuario = ser_seguridad.getUsuario(utilitario.getVariable("IDE_USUA"));
                if (tab_usuario.isEmpty() == false && tab_usuario.getValor("IDE_GTEMP") != null) {
                    utilitario.crearVariable("IDE_GTEMP", tab_usuario.getValor("IDE_GTEMP"));
                    FacesContext.getCurrentInstance().getExternalContext().redirect("portal/datosEmpleado.jsf");
                } else {
                    utilitario.agregarMensajeError("No puede ingresar al Portal", "Su nombre de usuario no tiene asociado a un Empleado, contactese con el Administrador del sistema");
                }
            } catch (Exception e) {
            }

        } else {
            utilitario.agregarMensajeError(str_mensaje, "");
        }
    }

    public void cancelarCambiarClave() {
        if (booCambiaClave == false) {
            utilitario.ejecutarJavaScript("wdiaClave.hide();");
        } else {
            utilitario.agregarMensajeInfo("Debe cambiar su clave", "Para continuar es necesario que cambie su clave");
        }
    }

    public void cambiarClave() {


        if (strClaveNueva == null) {
            utilitario.agregarMensajeInfo("Validacion", "Es necesario ingresar la clave actual");
            return;
        }
        if (strConfirmaClaveNueva == null) {
            utilitario.agregarMensajeInfo("Validacion", "Es necesario confirmar la nueva clave");
            return;
        }

        if (!strClaveNueva.equals(strConfirmaClaveNueva)) {
            utilitario.agregarMensajeInfo("Validacion", "La clave nueva debe se igual a la clave de confirmacion");
            strClaveNueva = null;
            strConfirmaClaveNueva = null;
            return;
        }


        if (!strClaveActua.isEmpty()) {
            Encriptar encriptar = new Encriptar();
            if (ser_seguridad.getClaveUsuario(utilitario.getVariable("IDE_USUA")).equals(encriptar.getEncriptar(strClaveActua))) {
                String str_mensaje = ser_seguridad.getClaveValida(strClaveNueva);
                if (str_mensaje.isEmpty()) {
                    if (ser_seguridad.isClaveNueva(utilitario.getVariable("IDE_USUA"), strClaveNueva)) {
                        ser_seguridad.cambiarClave(utilitario.getVariable("IDE_USUA"), strClaveNueva);
                        utilitario.agregarMensaje("Cambio clave", "La clave a sido cambiada correctamente");
                        booCambiaClave = false;
                        utilitario.ejecutarJavaScript("wdiaClave.hide();");
                    } else {
                        utilitario.agregarMensajeInfo("Clave no v�lida", "La clave ingresada ya fue utilizada anteriormente, intente con otra clave");
                        strClaveNueva = null;
                        strConfirmaClaveNueva = null;
                    }
                } else {
                    utilitario.agregarMensajeInfo(str_mensaje, "");
                }
            } else {
                utilitario.agregarMensajeError("Error", "La clave actual es incorrecta");
                strClaveActua = null;
                strClaveNueva = null;
                strConfirmaClaveNueva = null;
            }
        } else {
            utilitario.agregarMensajeInfo("Validacion", "Es necesario ingresar la clave actual");
            strClaveNueva=null;
            strConfirmaClaveNueva=null;
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isBooCambiaClave() {
        return booCambiaClave;
    }

    public void setBooCambiaClave(boolean booCambiaClave) {
        this.booCambiaClave = booCambiaClave;
    }

    public String getStrFecha() {
        return utilitario.getFechaLarga(utilitario.getFechaActual()); //fecha completa 
    }

    public String getStrNick() {
        return usuario;
    }

    public String getStrClaveActua() {
        return strClaveActua;
    }

    public void setStrClaveActua(String strClaveActua) {
        this.strClaveActua = strClaveActua;
    }

    public String getStrClaveNueva() {
        return strClaveNueva;
    }

    public void setStrClaveNueva(String strClaveNueva) {
        this.strClaveNueva = strClaveNueva;
    }

    public String getStrConfirmaClaveNueva() {
        return strConfirmaClaveNueva;
    }

    public void setStrConfirmaClaveNueva(String strConfirmaClaveNueva) {
        this.strConfirmaClaveNueva = strConfirmaClaveNueva;
    }
}

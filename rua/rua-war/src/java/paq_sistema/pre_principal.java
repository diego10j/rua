/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.componentes.Calendario;
import framework.componentes.Clave;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Encriptar;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.ItemMenu;
import framework.componentes.ItemOpcion;
import framework.componentes.MenuLista;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.themeswitcher.ThemeSwitcher;
import servicios.sistema.ServicioSeguridad;
import servicios.sistema.ServicioSistema;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_principal extends Pantalla {

    private Tabla tab_form_usuario = new Tabla();
    private final Panel pan_opcion = new Panel();
    private int int_opcion = 1;
    private final ThemeSwitcher ths_temas = new ThemeSwitcher();
    private final Calendario cal_calendario = new Calendario();
    private final Clave cla_clave_actual = new Clave();
    private final Clave cla_nueva = new Clave();
    private final Clave cla_confirmar = new Clave();
    private final Efecto efecto = new Efecto();

    @EJB
    private final ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

    @EJB
    private final ServicioSistema ser_sistema = (ServicioSistema) utilitario.instanciarEJB(ServicioSistema.class);

    public pre_principal() {
        ItemMenu mit_datos = new ItemMenu();
        mit_datos.setIcon("ui-icon-person");
        mit_datos.setValue("Datos del Usuario");
        mit_datos.setUpdate("pan_opcion");
        mit_datos.setMetodo("dibujarDatosUusario");

        ItemMenu mit_clave = new ItemMenu();
        mit_clave.setValue("Cambiar Clave ");
        mit_clave.setIcon("ui-icon-key");
        mit_clave.setUpdate("pan_opcion");
        mit_clave.setMetodo("dibujarCambiarClave");

        ItemMenu mit_tema = new ItemMenu();
        mit_tema.setValue("Cambiar Tema ");
        mit_tema.setIcon("ui-icon-image");
        mit_tema.setUpdate("pan_opcion");
        mit_tema.setMetodo("dibujarTemas");

        MenuLista men_menu = new MenuLista();
        men_menu.setStyle("width:100%");
        men_menu.getChildren().add(mit_datos);
        men_menu.getChildren().add(mit_clave);
        men_menu.getChildren().add(mit_tema);
        Panel pan_menu = new Panel();
        pan_menu.setHeader("OPCIONES");
        pan_menu.getChildren().add(men_menu);

        cal_calendario.setMode("inline");
        Panel pan_calendario = new Panel();
        pan_calendario.setHeader("CALENDARIO");
        pan_calendario.getChildren().add(cal_calendario);
        Grupo gru_panel_izquierda = new Grupo();
        gru_panel_izquierda.getChildren().add(pan_menu);
        gru_panel_izquierda.getChildren().add(pan_calendario);

        Panel pan_empresa = new Panel();
        pan_empresa.setHeader("EMPRESA");
        Imagen ima_empresa = new Imagen();

        ima_empresa.setValueExpression("value", "pre_index.logo");
        pan_empresa.getChildren().add(ima_empresa);
        Etiqueta eti_sucursal = new Etiqueta();
        eti_sucursal.setStyle("width: 100%;font-size: 13px;text-align: left;font-weight: bold;");
        eti_sucursal.setValue("<br/>" + ser_sistema.getSucursal().getValor("nom_sucu"));
        pan_empresa.getChildren().add(eti_sucursal);
        gru_panel_izquierda.getChildren().add(pan_empresa);
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        efecto.setType("drop");
        efecto.setSpeed(150);
        efecto.setPropiedad("mode", "'show'");
        efecto.setEvent("load");
        pan_opcion.getChildren().add(efecto);
        dibujarDatosUusario();
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(gru_panel_izquierda, pan_opcion, "22%", "V");
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        agregarComponente(div_division);
    }

    public void dibujarTemas() {
        pan_opcion.setHeader("Tema del Usuario");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);
        Grupo gru_panel = new Grupo();
        Grid gri_tema = new Grid();
        gri_tema.setColumns(2);
        gri_tema.getChildren().add(new Etiqueta("<strong>TEMA :</strong>"));
        List<String> lis_temas = new ArrayList<String>();
        lis_temas.add("afterdark");
        lis_temas.add("afternoon");
        lis_temas.add("afterwork");
        lis_temas.add("aristo");
        lis_temas.add("black-tie");
        lis_temas.add("blitzer");
        lis_temas.add("bluesky");
        lis_temas.add("bootstrap");
        lis_temas.add("casablanca");
        lis_temas.add("cupertino");
        lis_temas.add("eggplant");
        lis_temas.add("excite-bike");
        lis_temas.add("flick");
        lis_temas.add("glass-x");
        lis_temas.add("home");
        lis_temas.add("hot-sneaks");
        lis_temas.add("humanity");
        lis_temas.add("le-frog");
        lis_temas.add("midnight");
        lis_temas.add("mint-choc");
        lis_temas.add("overcast");
        lis_temas.add("pepper-grinder");
        lis_temas.add("redmond");
        lis_temas.add("rocket");
        lis_temas.add("sam");
        lis_temas.add("smoothness");
        lis_temas.add("south-street");
        lis_temas.add("start");
        lis_temas.add("sunny");
        lis_temas.add("swanky-purse");
        lis_temas.add("trontastic");
        lis_temas.add("ui-lightness");
        lis_temas.add("vader");

        Grid gri_imagenes = new Grid();
        gri_imagenes.setColumns(5);
        gri_imagenes.setWidth("100%");
        gri_imagenes.setStyle("font-size: 13px;text-align: center;");

        for (String lis_tema : lis_temas) {
            ItemOpcion ito_tema = new ItemOpcion();
            ito_tema.setItemValue(lis_tema);
            ito_tema.setItemLabel(lis_tema + "");
            ths_temas.getChildren().add(ito_tema);
            Grid gri_actual = new Grid();
            gri_actual.getChildren().add(new Etiqueta(lis_tema + ""));
            Imagen ima_tema = new Imagen();
            ima_tema.setValue("/imagenes/temas/" + lis_tema + ".png");
            gri_actual.getChildren().add(ima_tema);
            gri_imagenes.getChildren().add(gri_actual);
        }
        ths_temas.setStyle("width:180px");
        ths_temas.setValue(utilitario.getVariable("TEMA"));
        gri_tema.getChildren().add(ths_temas);
        gru_panel.getChildren().add(gri_tema);
        gru_panel.getChildren().add(gri_imagenes);
        pan_opcion.getChildren().add(gru_panel);
        int_opcion = 3;

    }

    public void dibujarDatosUusario() {
        pan_opcion.setHeader("Datos del Usuario");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);
        tab_form_usuario = new Tabla();
        tab_form_usuario.setMostrarNumeroRegistros(false);
        tab_form_usuario.setId("tab_form_usuario");
        tab_form_usuario.setTabla("sis_usuario", "ide_usua", 0);
        tab_form_usuario.getColumna("IDE_USUA").setVisible(false);
        tab_form_usuario.getColumna("IDE_USUA").setLectura(true);
        tab_form_usuario.getColumna("IDE_PERF").setNombreVisual("PERFIL");
        tab_form_usuario.getColumna("IDE_PERF").setLectura(true);

        tab_form_usuario.getColumna("IDE_PERF").setCombo("SIS_PERFIL", "IDE_PERF", "NOM_PERF", "");
        tab_form_usuario.getColumna("FECHA_REG_USUA").setVisible(false);
        tab_form_usuario.getColumna("ACTIVO_USUA").setVisible(false);
        tab_form_usuario.getColumna("BLOQUEADO_USUA").setVisible(false);
        tab_form_usuario.getColumna("cambia_clave_usua").setVisible(false);
        tab_form_usuario.getColumna("fecha_caduc_usua").setNombreVisual("FECHA CADUCA");
        tab_form_usuario.getColumna("NICK_USUA").setLectura(true);
        tab_form_usuario.getColumna("fecha_caduc_usua").setLectura(true);
        tab_form_usuario.getColumna("TEMA_USUA").setLectura(true);
        tab_form_usuario.getColumna("TEMA_USUA").setNombreVisual("TEMA");
        tab_form_usuario.getColumna("NOM_USUA").setNombreVisual("NOMBRE COMPLETO");
        tab_form_usuario.getColumna("NOM_USUA").setLectura(true);
        tab_form_usuario.getColumna("NICK_USUA").setNombreVisual("NOMBRE LOGIN");
        tab_form_usuario.getColumna("MAIL_USUA").setNombreVisual("CORREO");
        tab_form_usuario.getColumna("MAIL_USUA").setLectura(true);
        tab_form_usuario.setTipoFormulario(true);
        tab_form_usuario.setCondicion("IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_form_usuario.dibujar();
        Grid gri_tabla = new Grid();
        gri_tabla.getChildren().add(tab_form_usuario);
        pan_opcion.getChildren().add(gri_tabla);
        int_opcion = 1;
    }

    public void dibujarCambiarClave() {
        pan_opcion.setHeader("Cambiar la clave");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);
        cla_clave_actual.setValue("");
        cla_confirmar.setValue("");
        cla_nueva.setValue("");
        cla_nueva.setImmediate(false);
        cla_nueva.setFeedback(true);
        cla_confirmar.setFeedback(true);
        cla_confirmar.setImmediate(false);
        Grid gri_matriz = new Grid();
        gri_matriz.setColumns(2);
        gri_matriz.getChildren().add(new Etiqueta("<strong>CLAVE ACTUAL :</strong>"));
        gri_matriz.getChildren().add(cla_clave_actual);
        gri_matriz.getChildren().add(new Etiqueta("<strong>CLAVE NUEVA :</strong>"));
        gri_matriz.getChildren().add(cla_nueva);
        gri_matriz.getChildren().add(new Etiqueta("<strong>CONFIRMAR CLAVE NUEVA :</strong>"));
        gri_matriz.getChildren().add(cla_confirmar);
        pan_opcion.getChildren().add(gri_matriz);
        int_opcion = 2;
    }

    private void guardarClave() {
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
            Encriptar encriptar = new Encriptar();
            if (ser_seguridad.getClaveUsuario(utilitario.getVariable("IDE_USUA")).equals(encriptar.getEncriptar(cla_clave_actual.getValue() + ""))) {
                String str_mensaje = ser_seguridad.getClaveValida(cla_nueva.getValue().toString());
                if (str_mensaje.isEmpty()) {
                    if (ser_seguridad.isClaveNueva(utilitario.getVariable("IDE_USUA"), cla_nueva.getValue().toString())) {
                        ser_seguridad.cambiarClave(utilitario.getVariable("IDE_USUA"), cla_nueva.getValue().toString());
                        utilitario.agregarMensaje("Cambio clave", "La clave a sido cambiada correctamente");
                    } else {
                        utilitario.agregarMensajeInfo("Clave no válida", "La clave ingresada ya fue utilizada anteriormente, intente con otra clave");
                    }
                } else {
                    utilitario.agregarMensajeInfo(str_mensaje, "");
                }
            } else {
                utilitario.agregarMensajeError("Error", "La clave actual es incorrecta");
            }
        } else {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la clave actual");
        }
    }

    private void guardarDatosUsuario() {
        tab_form_usuario.guardar();
        guardarPantalla();
    }

    @Override
    public void guardar() {
        if (int_opcion == 1) {
            guardarDatosUsuario();
        } else if (int_opcion == 2) {
            guardarClave();
        } else if (int_opcion == 3) {
            //Guarda si cambio de tema
            if (utilitario.getVariable("TEMA") == null || !utilitario.getVariable("TEMA").equals(ths_temas.getValue())) {
                String sql = "UPDATE SIS_USUARIO SET TEMA_USUA='" + ths_temas.getValue() + "' WHERE IDE_USUA=" + utilitario.getVariable("IDE_USUA");
                getConexion().ejecutarSql(sql);
                utilitario.agregarMensaje("Cambió Tema", "El tema se cambio correctamente");
                utilitario.crearVariable("TEMA", ths_temas.getValue() + "");
                if (int_opcion == 1) {
                    dibujarDatosUusario();
                }
            }
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_form_usuario() {
        return tab_form_usuario;
    }

    public void setTab_form_usuario(Tabla tab_form_usuario) {
        this.tab_form_usuario = tab_form_usuario;
    }

}

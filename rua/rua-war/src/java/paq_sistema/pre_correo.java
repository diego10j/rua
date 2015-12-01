/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.aplicacion.Fila;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;
import framework.correo.EnviarCorreo;
import org.primefaces.component.editor.Editor;
import org.primefaces.component.overlaypanel.OverlayPanel;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_correo extends Pantalla {

    
    private final Editor edi_texto = new Editor();
    private final Texto tex_destinatario = new Texto();
    private final Texto tex_asunto = new Texto();
    private final Boton bot_enviar = new Boton();
    private Upload upl_adjuntos = new Upload();
    private Tabla tab_tabla = new Tabla();
    private final OverlayPanel ovp_destinatario = new OverlayPanel();
    private final Boton bot_destinatario = new Boton();

    public pre_correo() {
        bar_botones.quitarBotonEliminar();
        bar_botones.getBot_insertar().setValue("Nuevo");
        bar_botones.getBot_insertar().setTitle("Nuevo");
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonsNavegacion();
        bot_enviar.setIcon("ui-icon-mail-closed");
        bot_enviar.setValue("Enviar");
        bot_enviar.setMetodo("enviar");
        bar_botones.agregarBoton(bot_enviar);

        Grid gri_correo = new Grid();
        gri_correo.setColumns(2);
        bot_destinatario.setValue("Para");
        bot_destinatario.setIcon("ui-icon-person");
        bot_destinatario.setId("bot_destinatario");
        bot_destinatario.setType("button");

        Grid gri_contactos = new Grid();
        gri_contactos.setStyle("display: block;");
        ovp_destinatario.setId("ovp_destinatario");
        ovp_destinatario.setWidgetVar("ovp_destinatario");
        ovp_destinatario.setHideEffect("fade");
        ovp_destinatario.setShowEffect("fade");
        ovp_destinatario.setFor("bot_destinatario");

        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql("(select mail_usua AS ide, nom_usua as CONTACTO,mail_usua AS CORREO from sis_usuario where mail_usua is not null and ide_empr=" + utilitario.getVariable("IDE_EMPR") + ") "
                + "UNION (select correo_geper as ide,nom_geper as CONTACTO,correo_geper AS CORREO from gen_persona where correo_geper is not null and ide_empr=" + utilitario.getVariable("IDE_EMPR") + ") order by contacto");
        tab_tabla.setCampoPrimaria("IDE");
        tab_tabla.getColumna("contacto").setFiltro(true);
        tab_tabla.getColumna("correo").setFiltro(true);
        tab_tabla.setTipoSeleccion(true);
        tab_tabla.setRows(15);
        tab_tabla.dibujar();
        gri_contactos.getChildren().add(tab_tabla);
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarContactos");
        bot_aceptar.setOncomplete("ovp_destinatario.hide();");
        gri_contactos.setFooter(bot_aceptar);
        ovp_destinatario.getChildren().add(gri_contactos);
        agregarComponente(ovp_destinatario);
        gri_correo.getChildren().add(bot_destinatario);
        tex_destinatario.setId("tex_destinatario");
        tex_destinatario.setSize(200);
        gri_correo.getChildren().add(tex_destinatario);
        gri_correo.getChildren().add(new Etiqueta("Asunto"));
        tex_asunto.setId("tex_asunto");
        tex_asunto.setSize(150);
        gri_correo.getChildren().add(tex_asunto);
        gri_correo.getChildren().add(new Etiqueta("Adjuntar"));
        upl_adjuntos.setMultiple(true);
        upl_adjuntos.setId("upl_adjuntos");
        upl_adjuntos.setUpload("upload/correo");
        gri_correo.getChildren().add(upl_adjuntos);
        edi_texto.setId("edi_texto");
        edi_texto.setWidth(Integer.parseInt(utilitario.getVariable("ANCHO")) - 20);
        gri_correo.setFooter(edi_texto);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(gri_correo);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_tabla.setSeleccionados(null);
        upl_adjuntos.limpiar();
        tex_asunto.limpiar();
        tex_destinatario.limpiar();
        edi_texto.setValue("");
        utilitario.addUpdate("tab_tabla,upl_adjuntos,tex_asunto,tex_destinatario,edi_texto");
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public void aceptarContactos() {
        String str_correos = "";
        if (tab_tabla.getSeleccionados() != null) {
            for (Fila fila : tab_tabla.getSeleccionados()) {
                if (!str_correos.isEmpty()) {
                    str_correos += ";";
                }
                str_correos += fila.getRowKey();
            }
        }
        if (!str_correos.isEmpty()) {
            str_correos += ";";
        }
        String str_valor = "";
        if (tex_destinatario.getValue() != null) {
            str_valor = tex_destinatario.getValue().toString();
        }
        if (str_valor.isEmpty()) {
            tex_destinatario.setValue(str_correos);
        } else {
            if (!str_valor.endsWith(";")) {
                str_valor += ";";
            }
            tex_destinatario.setValue(str_valor + str_correos);
        }
        tab_tabla.setSeleccionados(null);
        utilitario.addUpdate("tex_destinatario,tab_tabla");
    }

    public void enviar() {
        if (tex_destinatario.getValue() != null && tex_asunto.getValue() != null && edi_texto.getValue() != null) {
            EnviarCorreo correo = new EnviarCorreo();
            String str_msj = correo.enviar(tex_destinatario.getValue().toString(), tex_asunto.getValue().toString(), edi_texto.getValue().toString(), upl_adjuntos.getArchivos());
            if (!str_msj.isEmpty()) {
                utilitario.agregarMensajeInfo("Mensajes generados", str_msj);
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede enviar el correo", "Debe ingresar valores en los campos");
        }
    }

    public Upload getUpl_adjuntos() {
        return upl_adjuntos;
    }

    public void setUpl_adjuntos(Upload upl_adjuntos) {
        this.upl_adjuntos = upl_adjuntos;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema.aplicacion;

import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.BuscarTabla;
import framework.componentes.FormatoTabla;
import framework.componentes.Grupo;
import framework.componentes.ImportarTabla;
import framework.componentes.SeleccionArchivo;
import framework.componentes.Tabla;
import framework.componentes.TerminalTabla;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
public abstract class Pantalla {

    public Utilitario utilitario = new Utilitario();
    public Barra bar_botones = new Barra();
    public Grupo gru_pantalla = new Grupo();

    public Pantalla() {
        gru_pantalla.setTransient(true);
        gru_pantalla.getChildren().add(bar_botones);
        crearPantalla(gru_pantalla);
    }

    /**
     * Crea la pantalla en el navegador web
     *
     * @param pantalla Grupo que contiene todos los componentes de la pantalla
     */
    private void crearPantalla(Grupo pantalla) {
        Grupo pan_dibuja = utilitario.getPantalla();
        if (pan_dibuja != null) {
            pan_dibuja.getChildren().add(pantalla);
        } else {
            System.out.println("No se pudo crear la Pantalla");
        }
    }

    public Conexion getConexion() {
        return utilitario.getConexion();
    }

    protected void agregarComponente(UIComponent componente) {
        gru_pantalla.getChildren().add(componente);
    }

    /**
     * Ejecuta todos las sentencias sql generadas en la pantalla
     *
     * @return String con un mensaje si ocurrio algun error al ejecutar todas
     * las sentecnias, caso contrario retorna vacio
     */
    protected String guardarPantalla() {
        return utilitario.getConexion().guardarPantalla();
    }

    /**
     * Ejecuta este método cuando se da click en el boton insertar
     */
    public abstract void insertar();

    /**
     * Ejecuta este método cuando se da click en el boton guardar
     */
    public abstract void guardar();

    /**
     * Ejecuta este método cuando se da click en el boton eliminar
     */
    public abstract void eliminar();

    /**
     * Ejecuta cuando da click en el boton de Reportes de la Barra de Botones
     */
    public void abrirListaReportes() {
    }

    /**
     * Ejecuta cuando se selecciona un reporte de la lista
     */
    public void aceptarReporte() {
    }

    /**
     * Ejecuta cuando da click en el boton Calendario de la Barra de Botones
     */
    public void abrirRangoFecha() {
    }

    public Barra getBar_botones() {
        return bar_botones;
    }

    public void setBar_botones(Barra bar_botones) {
        this.bar_botones = bar_botones;
    }

    /**
     * Cuando se presiona en el boton inicio de la barra de navegación
     */
    public void inicio() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
    }

    /**
     * Cuando se presiona en el boton fin de la barra de navegación
     */
    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
    }

    /**
     * Cuando se presiona en el boton siguiente de la barra de navegación
     */
    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
    }

    /**
     * Cuando se presiona en el boton atras de la barra de navegación
     */
    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
    }

    /**
     * Cuando se presiona el item buscar del menu contextual
     */
    public void buscar() {
        //Busca en la tabla que tiene el foco
        Tabla tabla_foco = utilitario.getTablaisFocus();
        BuscarTabla bus_buscar = utilitario.getBuscaTabla();
        if (bus_buscar != null && tabla_foco != null) {
            bus_buscar.setBuscar(tabla_foco);
        }
    }

    /**
     * Cuando se presiona el item actualizar del menu contextual
     */
    public void actualizar() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.actualizar();
        }
    }

    /**
     * Cuando se presiona el item exportarXLS del menu contextual
     */
    public void exportarXLS() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.exportarXLS();
        }
    }

    public void exportarFiltradosXLS() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.exportarFiltradosXLS();
        }
    }

    public void terminal() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        TerminalTabla terminal = utilitario.getTerminal();
        if (terminal != null && tabla_foco != null) {
            terminal.setTerminalTabla(tabla_foco);
            terminal.dibujar();
        }
    }

    /**
     * Cuando se presiona el item importarXLS del menu contextual
     */
    public void importarXLS() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        ImportarTabla imt_importar = utilitario.getImportarTabla();
        if (imt_importar != null && tabla_foco != null) {
            imt_importar.setImportarTabla(tabla_foco);
            imt_importar.dibujar();
        }
    }

    /**
     * Cuando se presiona el item formatoTabla del menu contextual
     */
    public void formatoTabla() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        FormatoTabla fot_formato = utilitario.getFormatoTabla();
        if (fot_formato != null && tabla_foco != null) {
            fot_formato.setFormatoTabla(tabla_foco);
            fot_formato.dibujar();
        }
    }

    /**
     * Cuando se acepta la busqueda
     */
    public void aceptarBuscar() {
        BuscarTabla bus_buscar = utilitario.getBuscaTabla();
        if (bus_buscar != null) {
            bus_buscar.aceptarBuscar();
        }
    }

    /**
     * Cuando se necesitan subir archivos
     */
    public void abrirSeleccionarArchivo(ActionEvent evt) {
        SeleccionArchivo sar_upload = utilitario.getSeleccionArchivo();
        if (sar_upload != null) {
            Boton bot_subir = (Boton) evt.getComponent();
            if (bot_subir.getDir() != null) {
                sar_upload.getUpload().setSoloImagenes();

            } else {
                sar_upload.limpiar();
            }
            sar_upload.getUpload().setFileUploadListener(bot_subir.getUploadMetodo());
            sar_upload.getUpload().setUpdate(utilitario.getTablaisFocus().getIdCompleto());
            try {
                ((Tabla) bot_subir.getParent().getParent().getParent()).setFocus();
            } catch (Exception e) {
            }
            sar_upload.dibujar();
        }
    }
}

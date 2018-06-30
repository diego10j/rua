/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

import componentes.FacturaCxC;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Mensaje;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_busca_factura extends Pantalla {

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    @EJB
    private final ServicioComprobanteElectronico ser_facElect = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);

    private AutoCompletar aut_alumno = new AutoCompletar();

    private Tabla tab_tabla = new Tabla();
    private final Mensaje men_factura = new Mensaje();

    private FacturaCxC fcc_factura = new FacturaCxC();

    private final Dialogo dia_correo = new Dialogo();
    private final Texto tex_correo = new Texto();

    public pre_busca_factura() {
        bar_botones.limpiar();
        bar_botones.agregarComponente(new Etiqueta("ALUMNO :"));
        aut_alumno.setId("aut_alumno");
        aut_alumno.setAutoCompletar(ser_pensiones.getSqlComboAlumnos());
        aut_alumno.setSize(75);
        aut_alumno.setAutocompletarContenido(); // no startWith para la busqueda
        aut_alumno.setMetodoChange("seleccionarAlumno");
        bar_botones.agregarComponente(aut_alumno);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);
        bar_botones.agregarSeparador();
        Boton bot_enviar = new Boton();
        bot_enviar.setValue("Enviar al SRI");
        bot_enviar.setMetodo("enviarSRI");
        bot_enviar.setIcon("ui-icon-signal-diag");
        bar_botones.agregarBoton(bot_enviar);

        Boton bot_ride = new Boton();
        bot_ride.setValue("Imprimir");
        bot_ride.setTitle("Imprimir RIDE");
        bot_ride.setMetodo("abrirRIDE");
        bot_ride.setIcon("ui-icon-print");
        bar_botones.agregarBoton(bot_ride);

        Boton bot_reenviar = new Boton();
        bot_reenviar.setValue("Reenviar");
        bot_reenviar.setTitle("Enviar nuevamente al correo del cliente");
        bot_reenviar.setMetodo("reenviarFactura");
        bot_reenviar.setIcon("ui-icon-mail-closed");
        bar_botones.agregarBoton(bot_reenviar);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_pensiones.getFacturasAlumno("-1"));
        tab_tabla.setCampoPrimaria("ide_cccfa");
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("ide_cncre").setVisible(false);
        tab_tabla.getColumna("ide_geper").setVisible(false);
        tab_tabla.getColumna("direccion_cccfa").setVisible(false);
        tab_tabla.getColumna("fecha_trans_cccfa").setVisible(false);
        tab_tabla.getColumna("ide_ccefa").setVisible(false);
        tab_tabla.getColumna("nombre_ccefa").setFiltroContenido();
        tab_tabla.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
        tab_tabla.getColumna("nombre_ccefa").setVisible(true);
        tab_tabla.getColumna("nombre_ccefa").setNombreVisual("ESTADO");
        tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla.getColumna("secuencial_cccfa").setNombreVisual("SECUENCIAL");
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setNombreVisual("CLIENTE");
        tab_tabla.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla.getColumna("IDE_CNCCC").setLink();
        tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("ventas0").setNombreVisual("VENTAS 0");
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("ventas12").setNombreVisual("VENTAS IVA");
        tab_tabla.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_tabla.getColumna("valor_iva_cccfa").setNombreVisual("IVA");
        tab_tabla.getColumna("total_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.getColumna("total_cccfa").setNombreVisual("TOTAL");
        tab_tabla.getColumna("ide_srcom").setVisible(false);
        tab_tabla.setRows(25);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div = new Division();
        div.dividir1(pat_panel);
        agregarComponente(div);

        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);

        fcc_factura.setId("fcc_factura");
        fcc_factura.getBot_aceptar().setMetodo("guardar");
        agregarComponente(fcc_factura);

        dia_correo.setId("dia_correo");
        dia_correo.setTitle("REENVIAR FACTURA ELECTRONICA AL CLIENTE");
        dia_correo.setWidth("35%");
        dia_correo.setHeight("17%");
        dia_correo.getBot_aceptar().setMetodo("aceptarReenviar");
        tex_correo.setStyle("width:" + (dia_correo.getAnchoPanel() - 35) + "px");
        Grid gri = new Grid();
        gri.setStyle("width:" + (dia_correo.getAnchoPanel() - 5) + "px; height:" + dia_correo.getAltoPanel() + "px;overflow:auto;display:block;vertical-align:middle;");
        gri.getChildren().add(new Etiqueta("<strong> CORREO ELECTRÓNICO: </strong>"));
        gri.getChildren().add(tex_correo);
        dia_correo.setDialogo(gri);
        agregarComponente(dia_correo);
    }

    public void seleccionarAlumno(SelectEvent evt) {
        aut_alumno.onSelect(evt);
        if (aut_alumno.getValor() != null) {
            tab_tabla.setSql(ser_pensiones.getFacturasAlumno(aut_alumno.getValorArreglo(1)));
            tab_tabla.ejecutarSql();
        } else {
            tab_tabla.limpiar();
        }
    }

    public void limpiar() {
        aut_alumno.limpiar();
        tab_tabla.limpiar();
    }

    public void aceptarReenviar() {
        if (utilitario.isCorreoValido(String.valueOf(tex_correo.getValue()))) {
            ser_facElect.reenviarComprobante(String.valueOf(tex_correo.getValue()), tab_tabla.getValor("ide_srcom"));
            dia_correo.cerrar();
            tex_correo.setValue(null);
        } else {
            utilitario.agregarMensajeError("Correo electrónico no válido", "");
        }
    }

    public void enviarSRI() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            //Valida que se encuentre en estado PENDIENTE o RECIBIDA
            if ((tab_tabla.getValor("nombre_ccefa")) != null && (tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) || tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.RECIBIDA.getDescripcion())) {
                String mensaje = ser_facElect.enviarComprobante(tab_tabla.getValor("clave_acceso"));

                String aux = tab_tabla.getValorSeleccionado();
                tab_tabla.actualizar();
                tab_tabla.setFilaActual(aux);
                tab_tabla.calcularPaginaActual();

                if (mensaje.isEmpty()) {
                    String mensje = "<p> FACTURA NRO. " + tab_tabla.getValor("secuencial_cccfa") + " ";
                    mensje += "</br>AMBIENTE : <strong>" + (utilitario.getVariable("p_sri_ambiente_comp_elect").equals("1") ? "PRUEBAS" : "PRODUCCIÓN") + "</strong></p>";  //********variable ambiente facturacion electronica                    
                    mensje += "<p>ESTADO : <strong>" + tab_tabla.getValor("nombre_ccefa") + "</strong></p>";
                    mensje += "<p>NÚMERO DE AUTORIZACION : <span style='font-size:12px;font-weight: bold;'>" + tab_tabla.getValor("CLAVE_ACCESO") + "</span> </p>";
                    men_factura.setMensajeExito("FACTURACIÓN ELECTRÓNICA AUTORIZADA", mensje);
                    men_factura.dibujar();
                } else {
                    utilitario.agregarMensajeError(mensaje, "");
                }

            } else {
                utilitario.agregarMensajeInfo("La Factura seleccionada no se encuentra en estado PENDIENTE o RECIBIDA", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    public void abrirRIDE() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            if (tab_tabla.getValor("ide_srcom") != null) {
                if (tab_tabla.getValor("nombre_ccefa") != null && !tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.ANULADO.getDescripcion())) {
                    fcc_factura.visualizarRide(tab_tabla.getValor("ide_srcom"));
                } else {
                    utilitario.agregarMensajeError("No se puede Visualizar el Comprobate", "La Factura seleccionada se encuentara ANULADA");
                }
            } else {
                utilitario.agregarMensajeInfo("La factura seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    public void reenviarFactura() {
        //Valida que la factura este AUTORIZADA
        if (tab_tabla.getValor("ide_cccfa") != null) {
            if (tab_tabla.getValor("ide_srcom") != null) {
                if (tab_tabla.getValor("nombre_ccefa") != null && tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                    dia_correo.dibujar();
                } else {
                    utilitario.agregarMensajeError("No se puede reenviar la factura", "La factura seleccionada debe estar en estado AUTORIZADO");
                }
            } else {
                utilitario.agregarMensajeInfo("La factura seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {

    }

    public AutoCompletar getAut_alumno() {
        return aut_alumno;
    }

    public void setAut_alumno(AutoCompletar aut_alumno) {
        this.aut_alumno = aut_alumno;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public FacturaCxC getFcc_factura() {
        return fcc_factura;
    }

    public void setFcc_factura(FacturaCxC fcc_factura) {
        this.fcc_factura = fcc_factura;
    }
}

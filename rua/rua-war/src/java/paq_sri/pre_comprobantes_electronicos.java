/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.ceo.ServicioComprobanteElectronico;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_comprobantes_electronicos extends Pantalla {

    @EJB
    private final ServicioComprobanteElectronico ser_comprobante = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);
    private Tabla tab_facturas = new Tabla();

    private final Combo com_estados = new Combo();

    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();

    public pre_comprobantes_electronicos() {
        bar_botones.limpiar();

        com_estados.setCombo(ser_comprobante.getSqlComboEstados());
        com_estados.setMetodo("actualizarConsulta");

        bar_botones.agregarComponente(new Etiqueta("ESTADO COMPROBANTE:"));
        bar_botones.agregarComponente(com_estados);

        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_inicio);
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarConsulta");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarBoton(bot_consultar);

        bar_botones.agregarSeparador();

        Boton bot_pend = new Boton();
        bot_pend.setValue("Cambiar a PENDIENTE");
        bot_pend.setMetodo("cambiaPendiente");
        bar_botones.agregarBoton(bot_pend);

        Boton bot_rec = new Boton();
        bot_rec.setValue("Cambiar a RECIBIDO");
        bot_rec.setMetodo("cambiaRecibido");
        bar_botones.agregarBoton(bot_rec);

        bar_botones.agregarSeparador();

        Boton bot_enviar = new Boton();
        bot_enviar.setValue("Enviar al SRI");
        bot_enviar.setMetodo("enviarSRI");
        bot_enviar.setIcon("ui-icon-signal-diag");
        bar_botones.agregarBoton(bot_enviar);

        tab_facturas.setId("tab_facturas");
        tab_facturas.setSql(ser_comprobante.getSqlComprobantesElectronicos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados.getValue())));
        tab_facturas.getColumna("ide_srcom").setVisible(false);
        tab_facturas.getColumna("DOCUMENTO").setFiltroContenido();
        tab_facturas.getColumna("ESTADO").setFiltroContenido();
        tab_facturas.setLectura(true);
        tab_facturas.setRows(25);
        tab_facturas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas);

        Division div = new Division();
        div.dividir1(pat_panel);
        agregarComponente(div);
    }

    public void actualizarConsulta() {
        tab_facturas.setSql(ser_comprobante.getSqlComprobantesElectronicos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados.getValue())));
        tab_facturas.ejecutarSql();
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

    public void cambiaPendiente() {
        if (tab_facturas.getValor("ide_srcom") != null) {
            utilitario.getConexion().agregarSql("UPDATE sri_comprobante SET ide_sresc=" + EstadoComprobanteEnum.PENDIENTE.getCodigo() + " where ide_srcom=" + tab_facturas.getValor("ide_srcom"));
            if (guardarPantalla().isEmpty()) {
                String aux = tab_facturas.getValorSeleccionado();
                actualizarConsulta();
                tab_facturas.setFilaActual(aux);
                tab_facturas.calcularPaginaActual();
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un comprobante", "");
        }
    }

    public void cambiaRecibido() {
        if (tab_facturas.getValor("ide_srcom") != null) {
            utilitario.getConexion().agregarSql("UPDATE sri_comprobante SET ide_sresc=" + EstadoComprobanteEnum.RECIBIDA.getCodigo() + " where ide_srcom=" + tab_facturas.getValor("ide_srcom"));
            if (guardarPantalla().isEmpty()) {
                String aux = tab_facturas.getValorSeleccionado();
                actualizarConsulta();
                tab_facturas.setFilaActual(aux);
                tab_facturas.calcularPaginaActual();
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un comprobante", "");
        }
    }

    public void enviarSRI() {
        if (tab_facturas.getValor("ide_srcom") != null) {
            //Valida que se encuentre en estado PENDIENTE o RECIBIDA
            if ((tab_facturas.getValor("ESTADO")) != null && (tab_facturas.getValor("ESTADO").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) || tab_facturas.getValor("ESTADO").equals(EstadoComprobanteEnum.RECIBIDA.getDescripcion())) {
                ser_comprobante.enviarComprobante(tab_facturas.getValor("CLAVE_ACCESO"));

                String aux = tab_facturas.getValorSeleccionado();
                actualizarConsulta();
                tab_facturas.setFilaActual(aux);
                tab_facturas.calcularPaginaActual();

            } else {
                utilitario.agregarMensajeInfo("El comprobante seleccionada no se encuentra en estado PENDIENTE o RECIBIDO", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un comprobante", "");
        }
    }

    public Tabla getTab_facturas() {
        return tab_facturas;
    }

    public void setTab_facturas(Tabla tab_facturas) {
        this.tab_facturas = tab_facturas;
    }

}

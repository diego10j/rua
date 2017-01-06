/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import componentes.AsientoContable;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MarcaAgua;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_asientos extends Pantalla {

    @EJB
    private final ServicioComprobanteContabilidad ser_comp_contabilidad = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);
    private Tabla tab_cab_conta;
    private Tabla tab_deta_conta;
    private final AreaTexto ate_observacion_conta = new AreaTexto();
    private final Texto tex_num_transaccion = new Texto();
    private final Etiqueta eti_info = new Etiqueta();

    private AsientoContable asc_asiento = new AsientoContable();

    public pre_asientos() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();

        tex_num_transaccion.setId("tex_num_transaccion");
        tex_num_transaccion.setSoloEnteros();
        tex_num_transaccion.setTitle("Nº ASIENTO");
        tex_num_transaccion.setSize(15);

        MarcaAgua maa_marca = new MarcaAgua();
        maa_marca.setValue("Num. Asiento");
        maa_marca.setFor("tex_num_transaccion");
        agregarComponente(maa_marca);

        Boton bot_buscar_transaccion = new Boton();
        bot_buscar_transaccion.setTitle("Buscar Comprobante");
        bot_buscar_transaccion.setIcon("ui-icon-search");
        bot_buscar_transaccion.setMetodo("buscarComprobante");
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarComponente(tex_num_transaccion);
        bar_botones.agregarBoton(bot_buscar_transaccion);
        bar_botones.agregarComponente(bot_clean);
        bar_botones.agregarSeparador();
        Boton bot_imprimir = new Boton();
        bot_imprimir.setValue("Imprimir");
        bot_imprimir.setMetodo("imprimirAsiento");
        bar_botones.agregarBoton(bot_imprimir);
        bar_botones.agregarSeparador();
        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setTitle("Anular");
        bot_anular.setMetodo("anularComprobante");
        bar_botones.agregarBoton(bot_anular);

        Grupo grupo = new Grupo();
        tab_cab_conta = new Tabla();
        tab_deta_conta = new Tabla();
        tab_cab_conta.setId("tab_cab_conta");
        tab_cab_conta.setSql(ser_comp_contabilidad.getSqlCabeceraAsiento("-1"));
        tab_cab_conta.getColumna("ide_cnccc").setNombreVisual("TRANSACCIÓN");
        tab_cab_conta.getColumna("ide_cnccc").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cab_conta.getColumna("ide_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("numero_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("numero_cnccc").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cab_conta.getColumna("numero_cnccc").setNombreVisual("NUM. COMPROBANTE");
        tab_cab_conta.getColumna("numero_cnccc").setOrden(5);
        tab_cab_conta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_cab_conta.getColumna("fecha_trans_cnccc").setOrden(1);
        tab_cab_conta.getColumna("fecha_trans_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("nom_usua").setVisible(false);
        tab_cab_conta.getColumna("fecha_siste_cnccc").setVisible(false);
        tab_cab_conta.getColumna("hora_sistem_cnccc").setVisible(false);
        tab_cab_conta.getColumna("nom_modu").setEtiqueta();
        tab_cab_conta.getColumna("nom_modu").setNombreVisual("MÓDULO");
        tab_cab_conta.getColumna("nom_modu").setOrden(4);
        tab_cab_conta.getColumna("nom_modu").setEstilo("width:150px");
        tab_cab_conta.getColumna("nom_geper").setEtiqueta();
        tab_cab_conta.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
        tab_cab_conta.getColumna("nom_geper").setOrden(2);
        tab_cab_conta.getColumna("nombre_cntcm").setEtiqueta();
        tab_cab_conta.getColumna("nombre_cntcm").setNombreVisual("TIPO COMPROBANTE");
        tab_cab_conta.getColumna("nombre_cntcm").setEstilo("width:100px");
        tab_cab_conta.getColumna("nombre_cntcm").setOrden(3);
        tab_cab_conta.getColumna("OBSERVACION_CNCCC").setVisible(false);
        tab_cab_conta.setTipoFormulario(true);
        tab_cab_conta.getGrid().setColumns(6);
        tab_cab_conta.setMostrarNumeroRegistros(false);
        tab_cab_conta.setLectura(true);
        tab_cab_conta.dibujar();
        tab_cab_conta.setLectura(false);
        if (tab_cab_conta.isEmpty()) {
            tab_cab_conta.insertar();
        }

        tab_deta_conta.setId("tab_deta_conta");
        tab_deta_conta.setSql(ser_comp_contabilidad.getSqlDetalleAsiento(tab_cab_conta.getValorSeleccionado()));
        tab_deta_conta.getColumna("ide_cndcc").setVisible(false);
        tab_deta_conta.getColumna("codig_recur_cndpc").setNombreVisual("CÓDIGO CUENTA");
        tab_deta_conta.getColumna("nombre_cndpc").setNombreVisual("CUENTA");
        tab_deta_conta.setColumnaSuma("debe,haber");
        tab_deta_conta.getColumna("debe").alinearDerecha();
        tab_deta_conta.getColumna("debe").setLongitud(25);
        tab_deta_conta.getColumna("haber").alinearDerecha();
        tab_deta_conta.getColumna("haber").setLongitud(25);
        tab_deta_conta.getColumna("OBSERVACION_CNDCC").setNombreVisual("OBSERVACIÓN");
        tab_deta_conta.setScrollable(true);
        tab_deta_conta.setScrollHeight(utilitario.getAltoPantalla() - 190); //300
        tab_deta_conta.setLectura(true);
        tab_deta_conta.dibujar();
        tab_deta_conta.setRows(5000);
        tab_deta_conta.setValueExpression("rowStyleClass", "fila.campos[3] eq '0.00' ? 'text-red' : fila.campos[4] eq '0.00' ? 'text-red' : null");
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_conta);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);

        grupo.getChildren().add(tab_cab_conta);
        grupo.getChildren().add(pat_panel);
        Grid gri_observa = new Grid();
        gri_observa.setWidth("100%");
        gri_observa.setColumns(2);
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong>"));
        gri_observa.getChildren().add(new Etiqueta(""));
        ate_observacion_conta.setId("ate_observacion_conta");
        ate_observacion_conta.setCols(120);
        ate_observacion_conta.setDisabled(true);
        gri_observa.getChildren().add(ate_observacion_conta);
        eti_info.setId("eti_info");
        eti_info.setValue("<table style='padding-left:10px;width:300px;'><tr><td><strong>USUARIO CREADOR :</strong></td><td> </td></tr><td><strong>FECHA SISTEMA :</strong></td><td> </td><tr> </tr><td><strong>HORA SISTEMA :</strong></td><td> </td><tr> </tr></table>");
        gri_observa.getChildren().add(eti_info);
        pat_panel.setFooter(gri_observa);
        Division div = new Division();
        div.dividir1(grupo);
        agregarComponente(div);

        asc_asiento.setId("asc_asiento");
        agregarComponente(asc_asiento);
    }

    public void buscarComprobante() {
        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {
            tab_cab_conta.setSql(ser_comp_contabilidad.getSqlCabeceraAsiento(String.valueOf(tex_num_transaccion.getValue())));
            tab_cab_conta.ejecutarSql();
            if (tab_cab_conta.isEmpty()) {
                tab_cab_conta.insertar();
                utilitario.agregarMensajeError("No existe el Asiento Contable N." + String.valueOf(tex_num_transaccion.getValue()), "");
                eti_info.setValue("<table style='padding-left:10px;width:300px;'><tr><td><strong>USUARIO CREADOR :</strong></td><td> </td></tr><td><strong>FECHA SISTEMA :</strong></td><td> </td><tr> </tr><td><strong>HORA SISTEMA :</strong></td><td> </td><tr> </tr></table>");
            } else {
                eti_info.setValue("<table style='padding-left:10px;width:300px;'><tr><td><strong>USUARIO CREADOR :</strong></td><td>" + tab_cab_conta.getValor("nom_usua") + " </td></tr><td><strong>FECHA SISTEMA :</strong></td><td>" + utilitario.getFormatoFecha(tab_cab_conta.getValor("fecha_siste_cnccc")) + " </td><tr> </tr><td><strong>HORA SISTEMA :</strong></td><td>" + utilitario.getFormatoHora(tab_cab_conta.getValor("hora_sistem_cnccc")) + " </td><tr> </tr></table>");
            }
            ate_observacion_conta.setValue(tab_cab_conta.getValor("observacion_cnccc"));
            tab_deta_conta.setSql(ser_comp_contabilidad.getSqlDetalleAsiento(tab_cab_conta.getValorSeleccionado()));
            tab_deta_conta.ejecutarSql();
            utilitario.addUpdate("eti_info,ate_observacion_conta");
        }
    }

    public void imprimirAsiento() {
        if (tab_cab_conta.isFilaInsertada() == false) {
            asc_asiento.verAsientoContable(tab_cab_conta.getValor("ide_cnccc"));
        } else {
            utilitario.agregarMensajeInfo("Debe Seleccionar un Asiento Contable", "");
        }
    }

    public void anularComprobante() {
        if (tab_cab_conta.isFilaInsertada() == false) {
            if (ser_comp_contabilidad.anularComprobante(tab_cab_conta.getValor("ide_cnccc"))) {
                if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                    utilitario.agregarMensaje("Se anulo el Asiento Contable N.", tab_cab_conta.getValor("ide_cnccc"));
                    buscarComprobante();
                }
            } else {
                utilitario.agregarMensajeError("No se puede anular el Asiento Contable N.", tab_cab_conta.getValor("ide_cnccc"));
            }

        } else {
            utilitario.agregarMensajeInfo("Debe Seleccionar un Asiento Contable", "");
        }
    }

    @Override
    public void insertar() {
        asc_asiento.nuevoAsiento();
        asc_asiento.dibujar();
        limpiar();
    }

    public void limpiar() {
        tex_num_transaccion.limpiar();
        tab_cab_conta.limpiar();
        tab_cab_conta.insertar();
        eti_info.setValue("<table style='padding-left:10px;width:300px;'><tr><td><strong>USUARIO CREADOR :</strong></td><td> </td></tr><td><strong>FECHA SISTEMA :</strong></td><td> </td><tr> </tr><td><strong>HORA SISTEMA :</strong></td><td> </td><tr> </tr></table>");
        ate_observacion_conta.setValue(tab_cab_conta.getValor("observacion_cnccc"));
        tab_deta_conta.limpiar();
        utilitario.addUpdate("eti_info,ate_observacion_conta,tex_num_transaccion");
    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {

    }

    public Tabla getTab_cab_conta() {
        return tab_cab_conta;
    }

    public void setTab_cab_conta(Tabla tab_cab_conta) {
        this.tab_cab_conta = tab_cab_conta;
    }

    public Tabla getTab_deta_conta() {
        return tab_deta_conta;
    }

    public void setTab_deta_conta(Tabla tab_deta_conta) {
        this.tab_deta_conta = tab_deta_conta;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

}

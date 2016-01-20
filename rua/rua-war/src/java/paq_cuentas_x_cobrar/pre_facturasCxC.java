/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.FacturaCxC;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_facturasCxC extends Pantalla {

    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private final Combo com_pto_emision = new Combo();
    private Calendario cal_fecha_inicio = new Calendario();
    private Calendario cal_fecha_fin = new Calendario();

    private FacturaCxC fcc_factura = new FacturaCxC();

    private Tabla tab_facturas = new Tabla();

    public pre_facturasCxC() {
        bar_botones.limpiar();

        Boton bot_nueva = new Boton();
        bot_nueva.setValue("Nueva Factura");
        bot_nueva.setIcon("ui-icon-plusthick");
        bot_nueva.setMetodo("abrirCrearFactura");
        bar_botones.agregarBoton(bot_nueva);

        bar_botones.agregarSeparador();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Factura");
        bot_ver.setMetodo("abrirVerFactura");
        bot_ver.setIcon("ui-icon-search");
        bar_botones.agregarBoton(bot_ver);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Factura");
        bot_anular.setIcon("ui-icon-closethick");
        bar_botones.agregarBoton(bot_anular);

        bar_botones.agregarSeparador();
        Boton bot_asiento = new Boton();
        bot_asiento.setValue("Generar Asiento");
        bot_asiento.setIcon("ui-icon-note");
        bar_botones.agregarBoton(bot_asiento);

        Boton bot_retención = new Boton();
        bot_retención.setValue("Generar Retención");
        bot_retención.setIcon("ui-icon-note");
        bar_botones.agregarBoton(bot_retención);

        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        com_pto_emision.setMetodo("actualizarFacturas");
        com_pto_emision.eliminarVacio();

        Fieldset fis_consulta = new Fieldset();
        //fis_consulta.setLegend("Detalle de la Consulta");

        Grid gri_pto = new Grid();
        gri_pto.setColumns(2);
        gri_pto.getChildren().add(new Etiqueta("<strong>PUNTO DE EMISIÓN :</strong>"));
        gri_pto.getChildren().add(com_pto_emision);
        fis_consulta.getChildren().add(gri_pto);

        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(5);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
        cal_fecha_inicio = new Calendario();
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        gri_fechas.getChildren().add(cal_fecha_inicio);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
        cal_fecha_fin = new Calendario();
        cal_fecha_fin.setFechaActual();
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarFacturas");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        // agregarComponente(fis_consulta);
        tab_facturas.setId("tab_facturas");
        tab_facturas.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas.setCampoPrimaria("ide_cccfa");
        tab_facturas.getColumna("ide_cccfa").setVisible(false);
        tab_facturas.getColumna("ide_ccefa").setVisible(false);
        tab_facturas.getColumna("nombre_ccefa").setFiltroContenido();
        tab_facturas.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas.getColumna("nom_geper").setFiltroContenido();
        tab_facturas.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas.getColumna("ide_cnccc").setFiltroContenido();
        tab_facturas.getColumna("ventas0").alinearDerecha();
        tab_facturas.getColumna("ventas12").alinearDerecha();
        tab_facturas.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas.getColumna("total_cccfa").alinearDerecha();
        tab_facturas.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas.setRows(15);
        tab_facturas.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_facturas.setValueExpression("rowStyleClass", "fila.campos[2] eq '" + utilitario.getVariable("p_cxc_estado_factura_anulada") + "' ? 'text-red' : fila.campos[13] eq null  ? 'text-green' : null");
        tab_facturas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(fis_consulta);
        pat_panel.setPanelTabla(tab_facturas);
        agregarComponente(pat_panel);

        Division div = new Division();
        div.dividir1(pat_panel);
        agregarComponente(div);

        fcc_factura.setId("fcc_factura");
        fcc_factura.setFacturaCxC("GENERAR FACTURA DE VENTA");
        agregarComponente(fcc_factura);

    }

    public void abrirCrearFactura() {
        fcc_factura.nuevaFactura();
        fcc_factura.dibujar();
    }

    public void abrirVerFactura() {
        if (tab_facturas.getValorSeleccionado() != null) {
            fcc_factura.verFactura(tab_facturas.getValorSeleccionado());
            fcc_factura.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccionar Factura", "Debe seleccionar una factura de la tabla");
        }

    }

    public void actualizarFacturas() {
        tab_facturas.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
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

    public Tabla getTab_facturas() {
        return tab_facturas;
    }

    public void setTab_facturas(Tabla tab_facturas) {
        this.tab_facturas = tab_facturas;
    }

    public FacturaCxC getFcc_factura() {
        return fcc_factura;
    }

    public void setFcc_factura(FacturaCxC fcc_factura) {
        this.fcc_factura = fcc_factura;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.componentes.Dialogo;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
public class AsientoContable extends Dialogo {

    private final Utilitario utilitario = new Utilitario();

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    private Tabla tab_cabe_asiento;
    private Tabla tab_deta_asiento;

    public AsientoContable() {
        this.setWidth("85%");
        this.setHeight("90%");
        this.setTitle("ASIENTO CONTABLE");
        this.setResizable(false);
        this.setDynamic(false);
    }

    public Grid construir() {
        Grid contenido = new Grid();

        tab_cabe_asiento = new Tabla();
        tab_deta_asiento = new Tabla();
        tab_cabe_asiento.setId("tab_cabe_asiento");
        tab_cabe_asiento.setRuta("pre_index.clase." + getId());
        ser_comprobante.configurarTablaCabeceraComprobante(tab_cabe_asiento);
        tab_cabe_asiento.agregarRelacion(tab_deta_asiento);
        tab_cabe_asiento.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_cabe_asiento);

        tab_deta_asiento.setId("tab_deta_asiento");
        tab_deta_asiento.setRuta("pre_index.clase." + getId());
        ser_comprobante.configurarTablaDetalleComprobante(tab_deta_asiento);
        tab_deta_asiento.setTabla("con_det_comp_cont", "ide_cndcc", 2);
        tab_deta_asiento.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_deta_asiento);
        return contenido;
    }

    public Tabla getTab_cabe_asiento() {
        return tab_cabe_asiento;
    }

    public void setTab_cabe_asiento(Tabla tab_cabe_asiento) {
        this.tab_cabe_asiento = tab_cabe_asiento;
    }

    public Tabla getTab_deta_asiento() {
        return tab_deta_asiento;
    }

    public void setTab_deta_asiento(Tabla tab_deta_asiento) {
        this.tab_deta_asiento = tab_deta_asiento;
    }

}

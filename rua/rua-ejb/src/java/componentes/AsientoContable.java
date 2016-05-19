/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.componentes.AutoCompletar;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
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
    private boolean usaPresupuesto = true; //!!!****** PONER EN VARIABLE
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    private AutoCompletar aut_asiento_tipo;

    private Tabla tab_cabe_asiento;
    private Tabla tab_deta_asiento;
    private Tabla tab_presupuesto;

    public AsientoContable() {
        this.setWidth("85%");
        this.setHeight("90%");
        this.setTitle("ASIENTO CONTABLE");
        this.setResizable(false);
        this.setDynamic(false);
    }

    private void construir() {
        Grid contenido = new Grid();
        //Asientos tipo
        aut_asiento_tipo = new AutoCompletar();
        aut_asiento_tipo.setRuta("pre_index.clase." + getId());
        aut_asiento_tipo.setId("aut_asiento_tipo");
        aut_asiento_tipo.setAutocompletarContenido();
        aut_asiento_tipo.setSize(getAnchoPanel() / 7);

        Grid gri_as_tipo = new Grid();
        gri_as_tipo.setColumns(2);
        gri_as_tipo.getChildren().add(new Etiqueta("<strong>ASIENTO TIPO :</strong>"));
        gri_as_tipo.getChildren().add(aut_asiento_tipo);

        contenido.getChildren().add(gri_as_tipo);

        tab_cabe_asiento = new Tabla();
        tab_deta_asiento = new Tabla();
        tab_cabe_asiento.setId("tab_cabe_asiento");
        tab_cabe_asiento.setRuta("pre_index.clase." + getId());
        ser_comprobante.configurarTablaCabeceraComprobante(tab_cabe_asiento);
        tab_cabe_asiento.agregarRelacion(tab_deta_asiento);
        tab_cabe_asiento.setMostrarNumeroRegistros(false);
        tab_cabe_asiento.getColumna("ide_usua").setVisible(false);
        tab_cabe_asiento.getColumna("ide_modu").setVisible(false);
        tab_cabe_asiento.getColumna("ide_cneco").setVisible(false);
        tab_cabe_asiento.getColumna("observacion_cnccc").setOrden(100);
        tab_cabe_asiento.getColumna("observacion_cnccc").setControl("Texto");
        tab_cabe_asiento.getColumna("numero_cnccc").setControl("Texto");
        tab_cabe_asiento.getColumna("numero_cnccc").setOrden(1);
        tab_cabe_asiento.dibujar();
        tab_cabe_asiento.insertar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_cabe_asiento);

        contenido.getChildren().add(pat_panel1);

        tab_deta_asiento.setId("tab_deta_asiento");
        tab_deta_asiento.setRuta("pre_index.clase." + getId());
        ser_comprobante.configurarTablaDetalleComprobante(tab_deta_asiento);
        tab_deta_asiento.setTabla("con_det_comp_cont", "ide_cndcc", 2);
        tab_deta_asiento.setScrollable(true);
        tab_deta_asiento.setScrollWidth(getAnchoPanel() - 20);
        tab_deta_asiento.setScrollHeight(getAltoPanel() - 165); //165 sin presupuesto
        tab_deta_asiento.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_deta_asiento);
        contenido.getChildren().add(pat_panel2);

        contenido.setStyle("width:" + (getAnchoPanel() - 10) + "px; height:" + (getAltoPanel() - 5) + "px;overflow:hidden;display:block;");
        this.getGri_cuerpo().getChildren().clear();
        this.setDialogo(contenido);
        this.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".aceptar");

    }

    @Override
    public void dibujar() {
        construir();
        super.dibujar();
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

    public AutoCompletar getAut_asiento_tipo() {
        return aut_asiento_tipo;
    }

    public void setAut_asiento_tipo(AutoCompletar aut_asiento_tipo) {
        this.aut_asiento_tipo = aut_asiento_tipo;
    }

}

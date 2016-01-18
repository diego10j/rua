/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class FacturaCxC extends Dialogo {

    private final Utilitario utilitario = new Utilitario();

    /////FACTURA
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);
    private Tabla tab_cab_factura = new Tabla();
    private Tabla tab_deta_factura = new Tabla();

    private final Combo com_pto_emision = new Combo();

    public FacturaCxC() {
        this.setWidth("95%");
        this.setHeight("90%");
        this.setTitle("GENERAR FACTURA DE VENTA");
        this.setResizable(false);

    }

    @Override
    public void dibujar() {
        this.getGri_cuerpo().getChildren().clear();
        Tabulador tab_factura = new Tabulador();
        tab_factura.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + (getAltoPanel() - 10) + "px;overflow: auto;display: block;");
        tab_factura.setId("tab_factura");
        tab_factura.agregarTab("FACTURA ", dibujarFactura());
        tab_factura.agregarTab("RETENCIÓN", null);
        tab_factura.agregarTab("ASIENTO DE VENTA", null);
        tab_factura.agregarTab("ASIENTO DE COSTO", null);
        tab_factura.agregarTab("INVENTARIO", null);
        this.setDialogo(tab_factura);
        super.dibujar(); //To change body of generated methods, choose Tools | Templates.
    }

    private Grupo dibujarFactura() {
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        Grupo grupo = new Grupo();
        Grid gri_pto = new Grid();
        gri_pto.setColumns(2);
        gri_pto.getChildren().add(new Etiqueta("<strong>PUNTO DE EMISIÓN :</strong>"));
        gri_pto.getChildren().add(com_pto_emision);
        grupo.getChildren().add(gri_pto);

        tab_cab_factura = new Tabla();
        tab_cab_factura.setRuta("pre_index.clase." + getId()); 
        tab_cab_factura.setId("tab_cab_factura");
                
        tab_deta_factura = new Tabla();        
        tab_deta_factura.setRuta("pre_index.clase." + getId());
        ser_factura.configurarTablaFacturaCxC(tab_cab_factura, tab_deta_factura);
        
        tab_cab_factura.dibujar();
        tab_cab_factura.insertar();
        
        tab_deta_factura.setId("tab_deta_factura");        
        tab_deta_factura.setScrollable(true);
        tab_deta_factura.setScrollHeight(100); 
        tab_deta_factura.dibujar();
        tab_deta_factura.insertar();
        tab_deta_factura.insertar();
        
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_factura);

        grupo.getChildren().add(tab_cab_factura);
        grupo.getChildren().add(pat_panel);

        return grupo;
    }

    public Tabla getTab_cab_factura() {
        return tab_cab_factura;
    }

    public void setTab_cab_factura(Tabla tab_cab_factura) {
        this.tab_cab_factura = tab_cab_factura;
    }

    public Tabla getTab_deta_factura() {
        return tab_deta_factura;
    }

    public void setTab_deta_factura(Tabla tab_deta_factura) {
        this.tab_deta_factura = tab_deta_factura;
    }

}

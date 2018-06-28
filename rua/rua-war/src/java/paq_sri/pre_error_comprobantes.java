/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.ceo.ServicioComprobanteElectronico;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_error_comprobantes extends Pantalla {

    private Tabla tab_consulta = new Tabla();
    @EJB
    private final ServicioComprobanteElectronico ser_facElect = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);

    public pre_error_comprobantes() {
        bar_botones.limpiar();

        Boton bot_actualiza = new Boton();
        bot_actualiza.setMetodo("actualizar");
        bot_actualiza.setValue("Actualizar");
        bot_actualiza.setIcon("ui-icon-refresh");
        bar_botones.agregarBoton(bot_actualiza);

        tab_consulta.setId("tab_consulta");
        tab_consulta.setLectura(true);
        tab_consulta.setSql(ser_facElect.getSqlComprobantesconError());
        tab_consulta.setNumeroTabla(1);
        tab_consulta.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        Division div = new Division();
        div.dividir1(pat_panel);

        agregarComponente(pat_panel);

    }

    @Override
    public void actualizar() {
        super.actualizar();
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

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

}

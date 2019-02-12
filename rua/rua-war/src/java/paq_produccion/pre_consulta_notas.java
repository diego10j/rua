/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author HP_PRO
 */
public class pre_consulta_notas extends Pantalla {

    private Tabla tab_consulta = new Tabla();

    public pre_consulta_notas() {

        tab_consulta.setId("tab_consulta");
        tab_consulta.setTabla("inv_cab_comp_inve", "ide_incci", 1);
        tab_consulta.getColumna("ide_incci").setNombreVisual("CODIGO");
        tab_consulta.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
        tab_consulta.getColumna("ide_geper").setFiltroContenido();
        tab_consulta.getColumna("ide_geper").setAutoCompletar();
        tab_consulta.getColumna("ide_usua").setVisible(false);
        tab_consulta.getColumna("ide_intti").setCombo("inv_tip_tran_inve", "ide_intti", "nombre_intti", "");
        tab_consulta.getColumna("ide_geper").setFiltroContenido();
        tab_consulta.getColumna("ide_intti").setAutoCompletar();
        tab_consulta.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
        tab_consulta.getColumna("observacion_incci").setAutoCompletar();
        tab_consulta.getColumna("observacion_incci").setNombreVisual("OBSERVACION");
        tab_consulta.getColumna("fecha_trans_incci").setFiltroContenido();
        tab_consulta.getColumna("codigo_documento_incci").setNombreVisual("No FACTURA");
        tab_consulta.getColumna("ide_cnccc").setVisible(false);
        tab_consulta.getColumna("sis_ide_usua").setVisible(false);
        tab_consulta.getColumna("ide_inbod").setVisible(false);
        tab_consulta.getColumna("fec_cam_est_incci").setVisible(false);
        tab_consulta.getColumna("fecha_efect_incci").setVisible(false);
        tab_consulta.getColumna("fecha_siste_incci").setVisible(false);
        tab_consulta.getColumna("hora_sistem_incci").setVisible(false);
        tab_consulta.getColumna("ide_georg").setVisible(false);
        tab_consulta.getColumna("gth_ide_gtemp").setVisible(false);
        tab_consulta.getColumna("referencia_incci").setVisible(false);
        tab_consulta.getColumna("gth_ide_gtemp2").setVisible(false);
        tab_consulta.getColumna("ide_gtemp").setVisible(false);
        tab_consulta.getColumna("gth_ide_gtemp3").setVisible(false);
        tab_consulta.getColumna("maquina_incci").setVisible(false);
        tab_consulta.getColumna("ide_inepi").setVisible(false);
        tab_consulta.setLectura(true);
        tab_consulta.setRows(10);
        tab_consulta.dibujar();
        PanelTabla pat_consulta = new PanelTabla();
        pat_consulta.setId("pat_consulta");
        pat_consulta.setPanelTabla(tab_consulta);

        Division div_consulta = new Division();
        div_consulta.setId("div_consulta");
        div_consulta.dividir1(pat_consulta);
        agregarComponente(div_consulta);

    }


    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }
    
}       



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;
       
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;


/**
 *
 * @author HP_PRO
 */
public class pre_cabecera_contable extends Pantalla {

    private Tabla tab_cabecera = new Tabla ();

    public pre_cabecera_contable() {
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("con_cab_comp_cont","ide_cnccc",1);
        tab_cabecera.getColumna("ide_cnccc");
        tab_cabecera.getColumna("ide_cneco").setCombo("con_estado_compro","ide_cneco","nombre_cneco","");
        tab_cabecera.getColumna("ide_cneco").setNombreVisual("ESTADO COMPROBANTE");
        tab_cabecera.getColumna("ide_cneco").setLongitud(40);
        tab_cabecera.getColumna("ide_cntcm").setCombo("con_tipo_comproba","ide_cntcm","nombre_cntcm","");
        tab_cabecera.getColumna("ide_cntcm").setNombreVisual("TIPO COMPROBANTE");
        tab_cabecera.getColumna("ide_cntcm").setLongitud(40);
        tab_cabecera.getColumna("ide_geper").setCombo("gen_persona","ide_geper","nom_geper","");
        tab_cabecera.getColumna("ide_geper").setFiltroContenido();
        tab_cabecera.getColumna("ide_geper").setLongitud(100);
        tab_cabecera.getColumna("fecha_trans_cnccc");
        tab_cabecera.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA TRANSSACIÓN");
        tab_cabecera.getColumna("fecha_trans_cnccc").setLongitud(40);
        tab_cabecera.getColumna("numero_cnccc");
        tab_cabecera.getColumna("numero_cnccc").setFiltroContenido();
        tab_cabecera.getColumna("observacion_cnccc");  
        tab_cabecera.getColumna("observacion_cnccc").setFiltroContenido();
        tab_cabecera.getColumna("observacion_cnccc").setNombreVisual("OBSERVACION");
        tab_cabecera.getColumna("ide_empr").setVisible(false);
        tab_cabecera.getColumna("ide_modu").setVisible(false);
        tab_cabecera.getColumna("ide_sucu").setVisible(false);
        tab_cabecera.getColumna("ide_usua").setVisible(false);        
        tab_cabecera.setLectura(true);
        tab_cabecera.setRows(40);
        tab_cabecera.dibujar();
        
        PanelTabla pat_cabecera = new PanelTabla();
        pat_cabecera.setId("pat_cabecera");
        pat_cabecera.setPanelTabla(tab_cabecera);
        
        Division div_cabecera = new Division();
        div_cabecera.setId("div_cabecera");
        div_cabecera.dividir1(pat_cabecera);
        agregarComponente(div_cabecera);
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
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
    
    
    
}

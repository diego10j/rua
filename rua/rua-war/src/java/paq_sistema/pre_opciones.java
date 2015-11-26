/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import sistema.aplicacion.Pantalla;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

/**
 *
 * @author Diego
 */
public class pre_opciones extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Arbol arb_arbol = new Arbol();
    
    

    public pre_opciones() {
    	Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("SIS_OPCION", "IDE_OPCI", 1);
        tab_tabla1.setCampoPadre("SIS_IDE_OPCI");
        tab_tabla1.setCampoNombre("NOM_OPCI");
        tab_tabla1.getColumna("AUDITORIA_OPCI").setCheck();
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.agregarArbol(arb_arbol);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        arb_arbol.setId("arb_arbol");
        arb_arbol.dibujar();

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
        tab_tabla2.setTabla("sis_tabla", "IDE_TABL", 2);
        tab_tabla2.getColumna("genera_primaria_tabl").setValorDefecto("true");
        tab_tabla2.getColumna("FORMULARIO_TABL").setCheck();
        tab_tabla2.getColumna("GENERA_PRIMARIA_TABL").setCheck();
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
        tab_tabla3.setTabla("sis_objeto_opcion", "IDE_OBOP", 3);
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setIdCompleto("tab_tabulador:tab_tabla4");
        tab_tabla4.setTabla("sis_reporte", "IDE_REPO", 4);
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setPanelTabla(tab_tabla4);

        tab_tabulador.agregarTab("TABLAS", pat_panel2);
        tab_tabulador.agregarTab("OBJETOS COMPONENTES", pat_panel3);
        tab_tabulador.agregarTab("REPORTES", pat_panel4);

        Division div3 = new Division(); //UNE OPCION Y DIV 2
        div3.dividir2(pat_panel1, tab_tabulador, "60%", "H");
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, div3, "21%", "V");  //arbol y div3
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                if (tab_tabla3.guardar()) {
                    tab_tabla4.guardar();
                }
            }
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
    }
}

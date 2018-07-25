/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

/**
 *
 * @author ANDRES REDROBAN
 */

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import sistema.aplicacion.Pantalla;

public class pre_cursos extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    
    public pre_cursos(){
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setTabla("REC_CURSO", "IDE_RECUR", 1);
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);
    }
    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_tabla1.eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }


    
}

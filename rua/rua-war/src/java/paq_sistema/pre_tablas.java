/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;

import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_tablas extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();

    public pre_tablas() {
        Boton bot_importar = new Boton();
        bot_importar.setId("bot_importar");
        bot_importar.setValue("Importar");
        bot_importar.setUpdate("tab_tabla2");
        bot_importar.setTitle("Importar los campos de la base de datos de la tabla seleccionada");
        bot_importar.setMetodo("importar");
        bot_importar.setValueExpression("disabled", "pre_index.clase.tab_tabla1.totalFilas==0");
        bar_botones.agregarBoton(bot_importar);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sis_tabla", "ide_tabl", 1);
        tab_tabla1.setLectura(true);
        tab_tabla1.setRows(15);
        tab_tabla1.getColumna("IDE_TABL").setVisible(false);
        tab_tabla1.getColumna("IDE_OPCI").setNombreVisual("OPCION");
        tab_tabla1.getColumna("IDE_OPCI").setCombo("SIS_OPCION", "IDE_OPCI", "NOM_OPCI", "");
        tab_tabla1.getColumna("IDE_OPCI").setAutoCompletar();
        tab_tabla1.getColumna("PRIMARIA_TABL").setNombreVisual("CLAVE PRIMARIA");
        tab_tabla1.getColumna("NUMERO_TABL").setNombreVisual("NUMERO");
        tab_tabla1.getColumna("FORANEA_TABL").setVisible(false);
        tab_tabla1.getColumna("PADRE_TABL").setVisible(false);
        tab_tabla1.getColumna("NOMBRE_TABL").setVisible(false);
        tab_tabla1.getColumna("TABLA_TABL").setFiltro(true);
        tab_tabla1.getColumna("ORDEN_TABL").setVisible(false);
        tab_tabla1.getColumna("FILAS_TABL").setVisible(false);
        tab_tabla1.setCampoOrden("IDE_OPCI,TABLA_TABL");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("sis_campo", "ide_camp", 2);
        tab_tabla2.getColumna("NOM_CAMP").setLectura(true);
        tab_tabla2.getColumna("NOM_CAMP").setFiltro(true);
        tab_tabla2.getColumna("NOM_VISUAL_CAMP").setFiltro(true);
        tab_tabla2.getColumna("IDE_TABL").setVisible(false);
        tab_tabla2.getColumna("VISIBLE_CAMP").setCheck();
        tab_tabla2.getColumna("LECTURA_CAMP").setCheck();
        tab_tabla2.getColumna("FILTRO_CAMP").setCheck();
        tab_tabla2.getColumna("MAYUSCULA_CAMP").setCheck();
        tab_tabla2.getColumna("REQUERIDO_CAMP").setCheck();
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("SIS_COMBO", "IDE_COMB", 3);
        tab_tabla3.getColumna("IDE_TABL").setVisible(false);
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel3, "35%", "20%", "H");
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        if (tab_tabla3.isFocus()) {
            tab_tabla3.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla2.guardar()) {
            if (tab_tabla3.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        } else if (tab_tabla3.isFocus()) {
            tab_tabla3.eliminar();
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }

    public void importar() {
        Tabla tab_campos = new Tabla();
        tab_campos.setSql("SELECT * FROM " + tab_tabla1.getValor("TABLA_TABL"));
        for (int i = 0; i < tab_campos.getColumnas().length; i++) {
            if (!buscar_campo(tab_campos.getColumnas()[i].getNombre())) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("IDE_TABL", tab_tabla1.getValorSeleccionado());
                tab_tabla2.setValor("NOM_CAMP", tab_campos.getColumnas()[i].getNombre());
                tab_tabla2.setValor("NOM_VISUAL_CAMP", tab_campos.getColumnas()[i].getNombreVisual().replace("*", "").trim());
                tab_tabla2.setValor("ORDEN_CAMP", tab_campos.getColumnas()[i].getOrden() + "");
                tab_tabla2.setValor("VISIBLE_CAMP", tab_campos.getColumnas()[i].isVisible() + "");
                tab_tabla2.setValor("LECTURA_CAMP", tab_campos.getColumnas()[i].isLectura() + "");
                tab_tabla2.setValor("DEFECTO_CAMP", tab_campos.getColumnas()[i].getValorDefecto());
                tab_tabla2.setValor("MASCARA_CAMP", tab_campos.getColumnas()[i].getMascara());
                tab_tabla2.setValor("FILTRO_CAMP", tab_campos.getColumnas()[i].isFiltro() + "");
            }
        }
    }

    private boolean buscar_campo(String str_nombre) {
        //Busca si existe un campo en la tabla 2
        for (int i = 0; i < tab_tabla2.getFilas().size(); i++) {
            if (tab_tabla2.getValor(i, "NOM_CAMP").equalsIgnoreCase(str_nombre)) {
                return true;
            }
        }
        return false;
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
}

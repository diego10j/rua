/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_general;

import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_persona extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();

    private AutoCompletar aut_filtro_persona = new AutoCompletar();
    private String persona = "-1";

    public pre_persona() {

        aut_filtro_persona.setId("aut_filtro_persona");
        aut_filtro_persona.setAutoCompletar("select ide_geper,identificac_geper,nom_geper from gen_persona where nivel_geper='HIJO' "
                + "and ide_empr=" + utilitario.getVariable("ide_empr"));
        aut_filtro_persona.setMetodoChange("filtrar_persona");
        aut_filtro_persona.setAutocompletarContenido();
        bar_botones.agregarComponente(new Etiqueta("Persona: "));
        bar_botones.agregarComponente(aut_filtro_persona);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("gen_persona", "ide_geper", 1);
        tab_tabla1.getColumna("nivel_geper").setCombo(utilitario.getListaNiveles());
        tab_tabla1.getColumna("nivel_geper").setValorDefecto("HIJO");
        tab_tabla1.getColumna("nivel_geper").setPermitirNullCombo(true);
        tab_tabla1.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "");
        tab_tabla1.getColumna("ide_cntco").setCombo("con_tipo_contribu", "ide_cntco", "nombre_cntco", "");
        tab_tabla1.getColumna("ide_cntco").setLectura(true);
        tab_tabla1.getColumna("identificac_geper").setLectura(true);
        tab_tabla1.getColumna("identificac_geper").setUnico(true);
        tab_tabla1.getColumna("ide_rhtro").setVisible(false);
        tab_tabla1.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla1.getColumna("nivel_geper").setVisible(false);
        //tab_tabla1.getColumna("ide_getid").setVisible(false);
        tab_tabla1.getColumna("nombre_compl_geper").setVisible(false);
        tab_tabla1.getColumna("repre_legal_geper").setVisible(false);
        tab_tabla1.getColumna("pagina_web_geper").setVisible(false);
        tab_tabla1.getColumna("contacto_geper").setVisible(false);
        tab_tabla1.getColumna("observacion_geper").setVisible(false);
        tab_tabla1.getColumna("fax_geper").setVisible(false);
        tab_tabla1.getColumna("ide_rhcon").setVisible(false);
        tab_tabla1.getColumna("ide_teban").setVisible(false);
        tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("identificac_geper").setUnico(true);
        tab_tabla1.getColumna("nivel_geper").setRequerida(true);
        tab_tabla1.getColumna("ide_rheem").setVisible(false);
        tab_tabla1.getColumna("factu_hasta_geper").setVisible(false);
        tab_tabla1.getColumna("ide_rhtem").setVisible(false);
        tab_tabla1.getColumna("ide_rhmse").setVisible(false);
        tab_tabla1.getColumna("ide_rhseg").setVisible(false);
        tab_tabla1.getColumna("ide_rhcsa").setVisible(false);
        tab_tabla1.getColumna("ide_rhfpa").setVisible(false);
        tab_tabla1.getColumna("ide_rheor").setVisible(false);
        tab_tabla1.getColumna("ide_cotpr").setVisible(false);
        tab_tabla1.getColumna("ide_rhrtr").setVisible(false);
        tab_tabla1.getColumna("ide_rhtsa").setVisible(false);
        tab_tabla1.getColumna("ide_rhtco").setVisible(false);
        tab_tabla1.getColumna("ide_geeci").setVisible(false);
        tab_tabla1.getColumna("sueldo_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_ingre_geper").setVisible(false);
        tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("fecha_ingre_geper").setVisible(false);
        tab_tabla1.getColumna("foto_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_nacim_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_salid_geper").setVisible(false);
        tab_tabla1.getColumna("cuent_banco_geper").setVisible(false);
        tab_tabla1.getColumna("ide_tetcb").setVisible(false);
        tab_tabla1.getColumna("ide_coepr").setVisible(false);
        tab_tabla1.getColumna("ide_geubi").setVisible(false);
        tab_tabla1.getColumna("ide_gegen").setVisible(false);
        tab_tabla1.getColumna("ide_vgtcl").setVisible(false);
        tab_tabla1.getColumna("ide_vgecl").setVisible(false);
        tab_tabla1.getColumna("ide_vgven").setVisible(false);
        tab_tabla1.getColumna("jornada_inicio_geper").setVisible(false);
        tab_tabla1.getColumna("jornada_fin_geper").setVisible(false);
        tab_tabla1.getColumna("tipo_sangre_geper").setVisible(false);
        tab_tabla1.getColumna("tipo_sangre_geper").setVisible(false);
        tab_tabla1.getColumna("jornada_fin_geper").setVisible(false);
        tab_tabla1.getColumna("jornada_inicio_geper").setVisible(false);
        tab_tabla1.getColumna("numero_geper").setVisible(false);
        tab_tabla1.getColumna("ide_rhfre").setVisible(false);
        tab_tabla1.setCondicion("ide_geper=-1");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(2);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        bar_botones.getBot_insertar().setRendered(false);
        bar_botones.getBot_eliminar().setRendered(false);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        agregarComponente(div_division);
    }

    public void filtrar_persona(SelectEvent evt) {
        aut_filtro_persona.onSelect(evt);
        persona = aut_filtro_persona.getValor();
        tab_tabla1.setCondicion("ide_geper=" + aut_filtro_persona.getValor());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tabla1");
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        if (tab_tabla1.isFilaModificada()) {
            tab_tabla1.guardar();
            utilitario.getConexion().guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public AutoCompletar getAut_filtro_persona() {
        return aut_filtro_persona;
    }

    public void setAut_filtro_persona(AutoCompletar aut_filtro_persona) {
        this.aut_filtro_persona = aut_filtro_persona;
    }
}

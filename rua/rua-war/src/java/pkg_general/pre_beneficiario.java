
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_general;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_beneficiario extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();

    public pre_beneficiario() {

        //        bar_botones.agregarReporte();

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("gen_persona", "ide_geper", 1);
        tab_tabla1.setCondicion("es_proveedo_geper=false");
        tab_tabla1.setCondicion("es_cliente_geper=false");
        tab_tabla1.setCondicion("es_empleado_geper=false");
        tab_tabla1.getColumna("es_proveedo_geper").setValorDefecto("false");
        tab_tabla1.getColumna("es_cliente_geper").setValorDefecto("false");
        tab_tabla1.getColumna("es_empleado_geper").setValorDefecto("false");

        tab_tabla1.getColumna("es_proveedo_geper").setVisible(false);
        tab_tabla1.getColumna("es_cliente_geper").setVisible(false);
        tab_tabla1.getColumna("es_empleado_geper").setVisible(false);

        tab_tabla1.getColumna("nivel_geper").setCombo(utilitario.getListaNiveles());
        tab_tabla1.getColumna("nivel_geper").setValorDefecto("HIJO");
        tab_tabla1.getColumna("nivel_geper").setPermitirNullCombo(true);
        tab_tabla1.getColumna("identificac_geper").setUnico(true);

        tab_tabla1.getColumna("ide_vgven").setVisible(false);
        tab_tabla1.getColumna("ide_rhtro").setVisible(false);
        tab_tabla1.getColumna("ide_rhcon").setVisible(false);
        //tab_tabla1.getColumna("ide_teban").setVisible(false);

        tab_tabla1.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "");
        //    tab_tabla1.getColumna("ide_getid").setMetodoChange("validarTipoIdentificacion");
        tab_tabla1.getColumna("fecha_ingre_geper").setVisible(false);
        tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("ide_teban").setVisible(false);
        tab_tabla1.getColumna("ide_rheem").setVisible(false);
        tab_tabla1.getColumna("ide_rhtem").setVisible(false);
        tab_tabla1.getColumna("ide_rhmse").setVisible(false);
        tab_tabla1.getColumna("ide_vgecl").setVisible(false);
        tab_tabla1.getColumna("ide_rhseg").setVisible(false);
        tab_tabla1.getColumna("ide_rhcsa").setVisible(false);
        tab_tabla1.getColumna("ide_cntco").setCombo("con_tipo_contribu", "ide_cntco", "nombre_cntco", "");
        tab_tabla1.getColumna("ide_rhfpa").setVisible(false);
        tab_tabla1.getColumna("ide_rheor").setVisible(false);
        tab_tabla1.getColumna("ide_cotpr").setCombo("com_tipo_proveedo", "ide_cotpr", "nombre_cotpr", "");
        tab_tabla1.getColumna("ide_rhrtr").setVisible(false);
        tab_tabla1.getColumna("ide_rhtsa").setVisible(false);
        tab_tabla1.getColumna("ide_rhtco").setVisible(false);
        tab_tabla1.getColumna("ide_geeci").setVisible(false);
        tab_tabla1.getColumna("sueldo_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_ingre_geper").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_ingre_geper").setLectura(true);
        tab_tabla1.getColumna("foto_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_nacim_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_salid_geper").setVisible(false);
        tab_tabla1.getColumna("es_cliente_geper").setVisible(false);
        tab_tabla1.getColumna("es_empleado_geper").setVisible(false);
        tab_tabla1.getColumna("cuent_banco_geper").setVisible(false);
        tab_tabla1.getColumna("ide_tetcb").setVisible(false);
        tab_tabla1.getColumna("ide_vgtcl").setVisible(false);
        tab_tabla1.getColumna("ide_coepr").setCombo("com_estado_provee", "ide_coepr", "nombre_coepr", "");
        tab_tabla1.getColumna("factu_hasta_geper").setVisible(false);
        tab_tabla1.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        tab_tabla1.getColumna("ide_gegen").setCombo("gen_genero", "ide_gegen", "nombre_gegen", "");
        tab_tabla1.getColumna("tipo_sangre_geper").setVisible(false);
        tab_tabla1.getColumna("jornada_fin_geper").setVisible(false);
        tab_tabla1.getColumna("jornada_inicio_geper").setVisible(false);
        
        tab_tabla1.getColumna("numero_geper").setVisible(false);
        tab_tabla1.getColumna("nivel_geper").setVisible(false);
        tab_tabla1.getColumna("repre_legal_geper").setVisible(false);
        tab_tabla1.getColumna("ide_cntco").setVisible(false);
        tab_tabla1.getColumna("ide_cotpr").setVisible(false);
        tab_tabla1.getColumna("ide_gegen").setVisible(false);
        tab_tabla1.getColumna("ide_coepr").setVisible(false);

        utilitario.buscarNombresVisuales(tab_tabla1);


        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);

        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);


        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        agregarComponente(div_division);


    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();

    }

    @Override
    public void guardar() {
        if (tab_tabla1.getValor("nivel_geper").equals("PADRE")) {
            tab_tabla1.guardar();
        } //else if (validar()) {
        tab_tabla1.guardar();
        //}
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

//    public boolean validar() {
//        if (tab_tabla1.getValor("ide_getid") == null || tab_tabla1.getValor("ide_getid").isEmpty()) {
//            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar su tipo de identificación");
//            return false;
//        }
//        if (tab_tabla1.getValor("identificac_geper") == null || tab_tabla1.getValor("identificac_geper").isEmpty()) {
//            utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar su identificacion");
//            return false;
//        }
//        if (tab_tabla1.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
//            if (utilitario.validarCedula(tab_tabla1.getValor("identificac_geper"))) {
//            } else {
//                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el numero de cedula valida");
//                return false;
//            }
//        }
//        if (tab_tabla1.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
//            if (utilitario.validarRUC(tab_tabla1.getValor("identificac_geper"))) {
//            } else {
//                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el numero de ruc valido");
//                return false;
//            }
//        }
//        if (tab_tabla1.getValor("nivel_geper") == null || tab_tabla1.getValor("nivel_geper").isEmpty()) {
//            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar el nivel");
//            return false;
//        }
//        if (tab_tabla1.getValor("ide_cntco") == null || tab_tabla1.getValor("ide_cntco").isEmpty()) {
//            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar el tipo de contribuyente");
//            return false;
//        }
//        return true;
//    }
    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
}

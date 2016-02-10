/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.aplicacion.Parametro;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;

import sistema.aplicacion.Pantalla;
import sistema.parametros.Parametros;

/**
 *
 * @author Diego
 */
public class pre_parametros extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private List<Parametro> lis_parametros = new ArrayList<>();
    private SeleccionTabla set_nuevos_parametros = new SeleccionTabla();
    ///PARA CONFIGURAR Un Parametro
    private Dialogo dia_dialogo = new Dialogo();
    private final Texto tex_nombre = new Texto();
    private final Combo com_modulos = new Combo();
    private final Texto tex_valor = new Texto();
    private final AreaTexto ate_descripcion = new AreaTexto();
    private SeleccionTabla set_configura = new SeleccionTabla();
    private final Boton bot_reconfigura = new Boton();

    public pre_parametros() {

        Boton bot_importar = new Boton();
        Boton bot_configurar = new Boton();

        bot_importar.setValue("Importar Parámetros");
        bot_importar.setMetodo("importar");
        bar_botones.agregarBoton(bot_importar);

        bot_configurar.setValue("Configurar");
        bot_configurar.setMetodo("configurar");
        bar_botones.agregarBoton(bot_configurar);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sis_modulo", "ide_modu", 1);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.setRows(20);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("sis_parametros", "ide_para", 2);
        tab_tabla2.setLectura(true);
        tab_tabla2.getColumna("nom_para").setFiltro(true);
        tab_tabla2.getColumna("valor_para").setFiltro(true);
        tab_tabla2.setRows(25);
        tab_tabla2.dibujar();        
        tab_tabla2.setLectura(false);//solo para que permita guardar
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().quitarItemInsertar();
        pat_panel2.getMenuTabla().quitarItemEliminar();
        pat_panel2.getMenuTabla().quitarItemGuardar();

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "35%", "v");
        agregarComponente(div_division);

        set_nuevos_parametros.setId("set_nuevos_parametros");
        set_nuevos_parametros.setSeleccionTabla("select * from sis_parametros where ide_para=-1", "ide_para");
        set_nuevos_parametros.getTab_seleccion().getColumna("ide_modu").setCombo("sis_modulo", "ide_modu", "nom_modu", "");
        set_nuevos_parametros.getTab_seleccion().getColumna("ide_modu").setLectura(true);
        set_nuevos_parametros.getTab_seleccion().getColumna("nom_para").setFiltro(true);
        set_nuevos_parametros.getTab_seleccion().getColumna("valor_para").setFiltro(true);
        set_nuevos_parametros.setWidth("80%");
        set_nuevos_parametros.setHeight("80%");
        set_nuevos_parametros.setDynamic(false);
        set_nuevos_parametros.setTitle("Parámetros no existentes en la base de datos");
        set_nuevos_parametros.getBot_aceptar().setMetodo("aceptar_importar");
        agregarComponente(set_nuevos_parametros);

        //Dialogo
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setDynamic(false);
        dia_dialogo.setWidth("40%");
        dia_dialogo.setHeight("40%");
        dia_dialogo.getBot_aceptar().setMetodo("aceptar_configurar");
        dia_dialogo.setResizable(false);

        Grid gri_cuerpo = new Grid();
        gri_cuerpo.setColumns(2);
        gri_cuerpo.setWidth("100%");
        gri_cuerpo.setStyle("width:100%;overflow: auto;display: block;");

        gri_cuerpo.getChildren().add(new Etiqueta("NOMBRE PARAMETRO"));
        tex_nombre.setReadonly(true);
        tex_nombre.setSize(40);
        gri_cuerpo.getChildren().add(tex_nombre);

        gri_cuerpo.getChildren().add(new Etiqueta("MÓDULO"));
        com_modulos.setCombo("select ide_modu,nom_modu from sis_modulo");
        com_modulos.setDisabled(true);
        gri_cuerpo.getChildren().add(com_modulos);
        gri_cuerpo.getChildren().add(new Etiqueta("DESCRIPCIÓN :"));

        ate_descripcion.setStyle("width:" + (dia_dialogo.getAnchoPanel() / 1.8) + "px;height:50px;overflow:auto;");
        gri_cuerpo.getChildren().add(ate_descripcion);
        gri_cuerpo.getChildren().add(new Etiqueta("VALOR :"));

        tex_valor.setSize(40);
        tex_valor.setId("tex_valor");
        tex_valor.setReadonly(true);
        gri_cuerpo.getChildren().add(tex_valor);

        bot_reconfigura.setValue("Configurar");
        bot_reconfigura.setMetodo("reconfigurar");
        gri_cuerpo.setFooter(bot_reconfigura);
        dia_dialogo.setDialogo(gri_cuerpo);
        agregarComponente(dia_dialogo);

        ///
        set_configura.setId("set_configura");
        set_configura.setDynamic(false);
        set_configura.setWidth("50%");
        set_configura.setHeight("70%");
        set_configura.getBot_aceptar().setMetodo("aceptar_reconfigurar");

        agregarComponente(set_configura);

    }

    public void aceptar_reconfigurar() {
        tex_valor.setValue(set_configura.getSeleccionados());
        set_configura.cerrar();
        utilitario.addUpdate("tex_valor");
    }

    public void reconfigurar() {
        set_configura.redibujar();
        set_configura.setFilasSeleccionados((String) tex_valor.getValue());
    }

    public void aceptar_configurar() {
        tab_tabla2.setValor("valor_para", tex_valor.getValue().toString());
        tab_tabla2.setValor("descripcion_para", ate_descripcion.getValue().toString());
        tab_tabla2.modificar(tab_tabla2.getFilaActual());
        tab_tabla2.guardar();
        dia_dialogo.cerrar();
        if (utilitario.getConexion().guardarPantalla().isEmpty()) {
            String str_foco = tab_tabla2.getValorSeleccionado();
            tab_tabla2.ejecutarSql();
            tab_tabla2.setFilaActual(str_foco);
        }
    }

    public void configurar() {
        if (tab_tabla2.getValorSeleccionado() != null) {
            dia_dialogo.setHeader("CONFIGURAR PARÁMETRO : " + tab_tabla2.getValor("NOM_PARA"));
            tex_nombre.setValue(tab_tabla2.getValor("NOM_PARA"));
            com_modulos.setValue(tab_tabla2.getValor("IDE_MODU"));
            tex_valor.setValue(tab_tabla2.getValor("valor_PARA"));
            ate_descripcion.setValue(tab_tabla2.getValor("descripcion_PARA"));

            if (tab_tabla2.getValor("tabla_para") == null) {

                bot_reconfigura.setRendered(false);
                tex_valor.setReadonly(false);
            } else {
                bot_reconfigura.setRendered(true);
                tex_valor.setReadonly(true);
                set_configura.setHeader("CONFIGURAR PARÁMETRO : " + tab_tabla2.getValor("NOM_PARA"));
                set_configura.setSeleccionTabla(tab_tabla2.getValor("tabla_para"), tab_tabla2.getValor("campo_codigo_para"), tab_tabla2.getValor("campo_nombre_para") + " as NOMBRE," + tab_tabla2.getValor("campo_codigo_para") + " as VALOR");
                set_configura.getTab_seleccion().setCampoOrden(tab_tabla2.getValor("campo_nombre_para"));
            }
            dia_dialogo.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un parámetro para poder configurarlo", "");
        }
    }

    public void aceptar_importar() {
        //Guarda en la base los parametros seleccionados
        for (int i = 0; i < set_nuevos_parametros.getListaSeleccionados().size(); i++) {
            tab_tabla2.insertar();
            int num_fila = set_nuevos_parametros.getTab_seleccion().getNumeroFila(set_nuevos_parametros.getListaSeleccionados().get(i).getRowKey());
            tab_tabla2.setValor("nom_para", set_nuevos_parametros.getTab_seleccion().getValor(num_fila, "nom_para"));
            tab_tabla2.setValor("valor_para", set_nuevos_parametros.getTab_seleccion().getValor(num_fila, "valor_para"));
            tab_tabla2.setValor("descripcion_para", set_nuevos_parametros.getTab_seleccion().getValor(num_fila, "descripcion_para"));
            tab_tabla2.setValor("ide_modu", set_nuevos_parametros.getTab_seleccion().getValor(num_fila, "ide_modu"));
            tab_tabla2.setValor("tabla_para", set_nuevos_parametros.getTab_seleccion().getValor(num_fila, "tabla_para"));
            tab_tabla2.setValor("campo_codigo_para", set_nuevos_parametros.getTab_seleccion().getValor(num_fila, "campo_codigo_para"));
            tab_tabla2.setValor("campo_nombre_para", set_nuevos_parametros.getTab_seleccion().getValor(num_fila, "campo_nombre_para"));
        }
        tab_tabla2.guardar();
        if (guardarPantalla().isEmpty()) {
            tab_tabla2.ejecutarSql();
        }
        set_nuevos_parametros.getTab_seleccion().limpiar();
        set_nuevos_parametros.cerrar();
    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    private void parametrosSistema() {
        lis_parametros.clear();
        Parametros parametros = new Parametros();
        lis_parametros = parametros.getParametrosSistema();
    }

    public void importar() {
        parametrosSistema();
        //busca las variables que no se encuentren configuradas
        set_nuevos_parametros.dibujar();
        for (Parametro parametro : lis_parametros) {
            List lis_sql = utilitario.getConexion().consultar("SELECT * FROM sis_parametros WHERE NOM_PARA ='" + parametro.getNombre() + "'");
            if (lis_sql == null || lis_sql.isEmpty()) {
                set_nuevos_parametros.getTab_seleccion().insertar();
                set_nuevos_parametros.getTab_seleccion().setValor("nom_para", parametro.getNombre());
                set_nuevos_parametros.getTab_seleccion().setValor("valor_para", parametro.getValor());
                set_nuevos_parametros.getTab_seleccion().setValor("descripcion_para", parametro.getDetalle());
                set_nuevos_parametros.getTab_seleccion().setValor("ide_modu", parametro.getModulo());
                set_nuevos_parametros.getTab_seleccion().setValor("tabla_para", parametro.getTabla());
                set_nuevos_parametros.getTab_seleccion().setValor("campo_codigo_para", parametro.getCampoCodigo());
                set_nuevos_parametros.getTab_seleccion().setValor("campo_nombre_para", parametro.getCampoNombre());
            }
        }
        if (set_nuevos_parametros.getTab_seleccion().getTotalFilas() < 0) {
            set_nuevos_parametros.cerrar();
            set_nuevos_parametros.getTab_seleccion().limpiar();
            utilitario.agregarMensajeInfo("No existen nuevos Parámetros", "Actualmente el sistema cuenta con todos los parámetros disponibles");
        }
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

    public Dialogo getDia_dialogo() {
        return dia_dialogo;
    }

    public void setDia_dialogo(Dialogo dia_dialogo) {
        this.dia_dialogo = dia_dialogo;
    }

    public SeleccionTabla getSet_configura() {
        return set_configura;
    }

    public void setSet_configura(SeleccionTabla set_configura) {
        this.set_configura = set_configura;
    }

    public SeleccionTabla getSet_nuevos_parametros() {
        return set_nuevos_parametros;
    }

    public void setSet_nuevos_parametros(SeleccionTabla set_nuevos_parametros) {
        this.set_nuevos_parametros = set_nuevos_parametros;
    }
}

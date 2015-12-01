/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.MarcaAgua;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.TablaGrid;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.component.panel.Panel;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_empleados extends Pantalla {
    
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Division div_division = new Division();
    private PanelAcordion pac_acordion = new PanelAcordion();
    private Panel pan_emple = new Panel();
    private TablaGrid tag_empleados = new TablaGrid();
    private Texto tex_busca = new Texto();
    private Check che_activos = new Check();
    private String p_reh_estado_activo_empleado = utilitario.getVariable("p_reh_estado_activo_empleado");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();
    
    public pre_empleados() {
        pac_acordion.setId("pac_acordion");
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setIdCompleto("pac_acordion:tab_tabla1");
        tab_tabla1.setTabla("gen_persona", "ide_geper", 1);
        tab_tabla1.setCondicion("es_empleado_geper=true and ide_rheem=" + p_reh_estado_activo_empleado);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.getColumna("es_empleado_geper").setValorDefecto("true");
        tab_tabla1.getColumna("es_empleado_geper").setVisible(false);
        tab_tabla1.getColumna("nivel_geper").setVisible(false);
        tab_tabla1.getColumna("ide_vgven").setVisible(false);
        tab_tabla1.getColumna("ide_vgecl").setVisible(false);
        tab_tabla1.getColumna("es_cliente_geper").setVisible(false);
        tab_tabla1.getColumna("es_proveedo_geper").setVisible(false);
        tab_tabla1.getColumna("ide_vgtcl").setVisible(false);
        tab_tabla1.getColumna("factu_hasta_geper").setVisible(false);
        tab_tabla1.getColumna("ide_cntco").setVisible(false);
        tab_tabla1.getColumna("ide_cotpr").setVisible(false);
        tab_tabla1.getColumna("ide_coepr").setVisible(false);
        tab_tabla1.getColumna("repre_legal_geper").setVisible(false);
        tab_tabla1.getColumna("pagina_web_geper").setVisible(false);
        tab_tabla1.getColumna("fax_geper").setVisible(false);
        tab_tabla1.getColumna("contacto_geper").setVisible(false);
        tab_tabla1.getColumna("nombre_compl_geper").setVisible(false);
        tab_tabla1.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla1.getColumna("foto_geper").setUpload("upload/fotos");
        tab_tabla1.getColumna("foto_geper").setValorDefecto("upload/fotos/empleado.png");
        
        tab_tabla1.getColumna("foto_geper").setImagen("128", "128");
        tab_tabla1.getColumna("ide_rhtro").setCombo("reh_tipo_rol", "ide_rhtro", "nombre_rhtro", "");
        tab_tabla1.getColumna("ide_rhcon").setCombo("reh_condicion", "ide_rhcon", "nombre_rhcon", "");
        tab_tabla1.getColumna("ide_teban").setCombo("tes_banco", "ide_teban", "nombre_teban", "");
        tab_tabla1.getColumna("ide_rheem").setCombo("reh_estado_emplea", "ide_rheem", "nombre_rheem", "");
        tab_tabla1.getColumna("ide_rhtem").setCombo("reh_tipo_empleado", "ide_rhtem", "nombre_rhtem", "");
        tab_tabla1.getColumna("ide_tetcb").setCombo("tes_tip_cuen_banc", "ide_tetcb", "nombre_tetcb", "");
        tab_tabla1.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "");
        tab_tabla1.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        tab_tabla1.getColumna("ide_geubi").setAutoCompletar();
        tab_tabla1.getColumna("ide_gegen").setCombo("gen_genero", "ide_gegen", "nombre_gegen", "");
        tab_tabla1.getColumna("ide_rhseg").setCombo("reh_seguro", "ide_rhseg", "nombre_rhseg", "");
        tab_tabla1.getColumna("ide_rhcsa").setCombo("reh_causa_salida", "ide_rhcsa", "nombre_rhcsa", "");
        tab_tabla1.getColumna("ide_rhfpa").setCombo("reh_forma_pago", "ide_rhfpa", "nombre_rhfpa", "");
        tab_tabla1.getColumna("ide_rheor").setCombo("reh_estruc_organi", "ide_rheor", "nombre_rheor", "");
        tab_tabla1.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
        tab_tabla1.getColumna("ide_rhrtr").setCombo("reh_relaci_trabaj", "ide_rhrtr", "nombre_rhrtr", "");
        tab_tabla1.getColumna("ide_rhtsa").setCombo("reh_tipo_salario", "ide_rhtsa", "nombre_rhtsa", "");
        tab_tabla1.getColumna("ide_rhtco").setCombo("reh_tipo_contrato", "ide_rhtco", "nombre_rhtco", "");
        tab_tabla1.getColumna("ide_geeci").setCombo("gen_estado_civil", "ide_geeci", "nombre_geeci", "");
        tab_tabla1.getColumna("ide_rhfre").setCombo("reh_fondos_reserva", "ide_rhfre", "nombre_rhfre", "");
        tab_tabla1.getColumna("ide_getid").setRequerida(true);
        tab_tabla1.getColumna("identificac_geper").setRequerida(true);
        tab_tabla1.getColumna("direccion_geper").setRequerida(true);
        tab_tabla1.getColumna("numero_geper").setRequerida(true);
        tab_tabla1.getColumna("identificac_geper").setRequerida(true);
        tab_tabla1.getColumna("identificac_geper").setUnico(true);
        tab_tabla1.getColumna("telefono_geper").setRequerida(true);
        tab_tabla1.getColumna("ide_rhtsa").setRequerida(true);
        tab_tabla1.getColumna("ide_geubi").setRequerida(true);
        tab_tabla1.getColumna("ide_georg").setRequerida(true);
        tab_tabla1.getColumna("ide_rheor").setRequerida(true);
        tab_tabla1.getColumna("ide_rhmse").setCombo("reh_minimo_sector", "ide_rhmse", "nombre_rhmse", "nivel_rhmse='HIJO'");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("reh_carga", "ide_rhcar", 2);
        tab_tabla2.setCampoForanea("ide_geper");
        tab_tabla2.getColumna("ide_rhcon").setCombo("reh_condicion", "ide_rhcon", "nombre_rhcon", "");
        tab_tabla2.getColumna("ide_rhtca").setCombo("reh_tipo_carga", "ide_rhtca", "nombre_rhtca", "");
        tab_tabla2.setIdCompleto("pac_acordion:tab_tabla2");
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("reh_historial_empleado", "ide_rhhem", 3);
        tab_tabla2.setCampoForanea("ide_geper");
        tab_tabla3.setIdCompleto("pac_acordion:tab_tabla3");
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);
        
        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setTabla("reh_empleados_por_carac", "ide_rhepc", 4);
        tab_tabla4.setCampoForanea("ide_geper");
        tab_tabla4.getColumna("ide_rhcem").setCombo("reh_carac_empleados", "ide_rhcem", "nombre_rhcem", "");
        tab_tabla4.setIdCompleto("pac_acordion:tab_tabla4");
        tab_tabla4.dibujar();
        
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setPanelTabla(tab_tabla4);
        
        pac_acordion.agregarPanel("INFORMACIÓN DEL EMPLEADO", pat_panel1);
        pac_acordion.agregarPanel("CARGAS FAMILIARES", pat_panel2);
        pac_acordion.agregarPanel("HISTORIA EMPLEADO", pat_panel3);
        pac_acordion.agregarPanel("EMPLEADOS POR CARACTERISTICA", pat_panel4);
        pac_acordion.setRendered(false);
        
        pan_emple.setId("pan_emple");
        pan_emple.setHeader("Listado de Empleados");
        
        Grid gri_bus = new Grid();
        gri_bus.setColumns(3);
        tex_busca.setId("tex_busca");
        tex_busca.setMetodoKeyPress("buscarEmpleado");
        tex_busca.setSize(60);
        MarcaAgua maa_marca = new MarcaAgua();
        maa_marca.setValue("Buscar Empleado");
        maa_marca.setFor("tex_busca");
        agregarComponente(maa_marca);
        
        gri_bus.getChildren().add(tex_busca);
        Boton bot_ficha = new Boton();
        bot_ficha.setIcon("ui-icon-calculator");
        bot_ficha.setMetodo("dibujarAcordeon");
        bot_ficha.setTitle("Vista Ficha");
        gri_bus.getChildren().add(bot_ficha);
        
        Boton bot_grid = new Boton();
        bot_grid.setIcon("ui-icon-contact");
        bot_grid.setMetodo("dibujarGridEmpleados");
        bot_grid.setTitle("Vista Grid");
        gri_bus.getChildren().add(bot_grid);
        
        bar_botones.agregarComponente(gri_bus);
        
        che_activos.setValue(true);
        che_activos.setMetodoChange("filtroActivos");
        bar_botones.agregarComponente(new Etiqueta("Solo empleados activos "));
        bar_botones.agregarComponente(che_activos);
        
        pan_emple.getChildren().add(pac_acordion);
        
        Efecto efecto = new Efecto();
        efecto.setType("drop");
        efecto.setSpeed(150);
        efecto.setPropiedad("mode", "'show'");
        efecto.setEvent("load");
        pan_emple.getChildren().add(efecto);
        
        tag_empleados.setId("tag_empleados");
        tag_empleados.setTablaGrid(tab_tabla1);
        tag_empleados.setColumns(3);
        tag_empleados.setMostrarColumnas("nom_geper,IDENTIFICAC_GEPER,telefono_geper,correo_geper", false);
        tag_empleados.setImagen("foto_geper", "128", "128", "H");
        tag_empleados.setSeleccion("seleccionoEmpleado");
        tag_empleados.dibujar();
        pan_emple.getChildren().add(tag_empleados);
        
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_formato.setId("sel_formato");
        agregarComponente(sel_formato);
        agregarComponente(rep_reporte);
        bar_botones.agregarReporte();
        
        div_division.setId("div_division");
        div_division.dividir1(pan_emple);
        
        agregarComponente(div_division);
    }
    
    public void filtroActivos() {
        
        System.out.println(che_activos.getValue() + "    ...");
        if (che_activos.getValue().equals("true")) {
            tab_tabla1.setCondicion("es_empleado_geper=true and ide_rheem=" + p_reh_estado_activo_empleado);
        } else {
            tab_tabla1.setCondicion("es_empleado_geper=true");
        }
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("pan_emple");
        System.out.println(tab_tabla1.getSql() + "    ...");
    }
    
    public void buscarEmpleado() {
        if (tex_busca.getValue() != null && !tex_busca.getValue().toString().isEmpty()) {
            tab_tabla1.setCondicionBuscar("LOWER(NOM_GEPER) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%'");
        } else {
            tab_tabla1.setCondicionBuscar("");
        }
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tag_empleados");
    }
    
    public void seleccionoEmpleado() {
        pac_acordion.setRendered(true);
        tab_tabla1.setFilaActual(tag_empleados.getValorSeleccionado());
        tag_empleados.setRendered(false);
        utilitario.addUpdate("pan_emple");
    }
    
    public TablaGrid getTag_empleados() {
        return tag_empleados;
    }
    
    public void setTag_empleados(TablaGrid tag_empleados) {
        this.tag_empleados = tag_empleados;
    }
    
    public void dibujarAcordeon() {
        pac_acordion.setRendered(true);
        if (tag_empleados.getValorSeleccionado() != null) {
            tab_tabla1.setFilaActual(tag_empleados.getValorSeleccionado());
        }
        tag_empleados.setRendered(false);
        utilitario.addUpdate("pan_emple");
    }
    
    public void dibujarGridEmpleados() {
        
        pac_acordion.setRendered(false);
        tag_empleados.setRendered(true);
        utilitario.addUpdate("pan_emple");
    }
    
    public boolean validar() {
//        if (tab_tabla1.getValor("ide_getid") == null || tab_tabla1.getValor("ide_getid").isEmpty()) {
//            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar su tipo de identificación");
//            return false;
//        }
//        if (tab_tabla1.getValor("identificac_geper") == null || tab_tabla1.getValor("identificac_geper").isEmpty()) {
//            utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar su identificacion");
//            return false;
//        }
        if (tab_tabla1.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
            if (utilitario.validarCedula(tab_tabla1.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el numero de cedula valida");
                return false;
            }
        }
        if (tab_tabla1.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
            if (utilitario.validarRUC(tab_tabla1.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el numero de ruc valido");
                return false;
            }
        }
//        if (tab_tabla1.getValor("direccion_geper") == null || tab_tabla1.getValor("direccion_geper").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar la dirección");
//            return false;
//
//        }
//        if (tab_tabla1.getValor("numero_geper") == null || tab_tabla1.getValor("numero_geper").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe seleccionar el Numero de la Casa");
//            return false;
//        }
//        if (tab_tabla1.getValor("identificac_geper") == null || tab_tabla1.getValor("identificac_geper").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar la identificación");
//            return false;
//        }
//        if (tab_tabla1.getValor("telefono_geper") == null || tab_tabla1.getValor("telefono_geper").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar el teléfono");
//            return false;
//        }
//        if (tab_tabla1.getValor("ide_rhtsa") == null || tab_tabla1.getValor("ide_rhtsa").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar el tipo de salario");
//            return false;
//        }
//        if (tab_tabla1.getValor("nom_geper") == null || tab_tabla1.getValor("nom_geper").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar el nombre del empleado");
//            return false;
//        }
//        if (tab_tabla1.getValor("ide_geubi") == null || tab_tabla1.getValor("ide_geubi").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar la ubicación");
//            return false;
//        }
        return true;
    }
    
    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();
        
    }
    Map parametro = new HashMap();
    
    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista
        if (rep_reporte.getReporteSelecionado().equals("Empleados por Departamento")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_rheem", Integer.parseInt(tab_tabla1.getValor("ide_rheem")));
                System.out.println("Si parametro..." + parametro + "----->" + Integer.parseInt(utilitario.getVariable("p_reh_estado_activo_empleado")));
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        }
    }
    
    @Override
    public void insertar() {
        if (pac_acordion.isRendered() == false) {
            dibujarAcordeon();
            tab_tabla1.insertar();
        } else {
            utilitario.getTablaisFocus().insertar();
        }
        
    }
    
    @Override
    public void guardar() {
        if (validar()) {
            tab_tabla1.guardar();
            tab_tabla2.guardar();
            tab_tabla3.guardar();
            tab_tabla4.guardar();
        }
        guardarPantalla();
    }
    
    @Override
    public void eliminar() {
        if (utilitario.getTablaisFocus() != null) {
            utilitario.getTablaisFocus().eliminar();
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
    
    public Reporte getRep_reporte() {
        return rep_reporte;
    }
    
    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }
    
    public SeleccionFormatoReporte getSel_formato() {
        return sel_formato;
    }
    
    public void setSel_formato(SeleccionFormatoReporte sel_formato) {
        this.sel_formato = sel_formato;
    }
}

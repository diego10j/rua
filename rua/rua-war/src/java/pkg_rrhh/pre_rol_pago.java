/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.blockui.BlockUI;
import org.primefaces.event.SelectEvent;
import pkg_bancos.cls_bancos;
import pkg_contabilidad.VistaAsiento;
import pkg_contabilidad.cls_cab_comp_cont;
import pkg_contabilidad.cls_contabilidad;
import pkg_contabilidad.cls_det_comp_cont;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_rol_pago extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Division div_division = new Division();
    private Boton bot_importar_emp = new Boton();
    private Boton bot_importar_rub = new Boton();
    private Boton bot_generar_asiento = new Boton();
    private Boton bot_generar_asiento_provisiones = new Boton();
    private Texto tex_valor_a_recibir = new Texto();
    private Texto tex_sueldo_basico = new Texto();
    private Texto tex_aporte_personal = new Texto();
    private Texto tex_valor_horas_extras = new Texto();
    private Texto tex_valor_horas_suple = new Texto();
    private Texto tex_prestamo_hipo = new Texto();
    private Texto tex_prestamo_quiro = new Texto();
    private Texto tex_total_ingresos = new Texto();
    private Texto tex_total_egresos = new Texto();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private VistaAsiento via_comprobante_conta = new VistaAsiento();
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
    cls_nomina nomina = new cls_nomina();
    private AutoCompletar aut_periodo = new AutoCompletar();
    private Confirmar con_guardar = new Confirmar();

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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public pre_rol_pago() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {


            aut_periodo.setId("aut_periodo");
            aut_periodo.setAutoCompletar("select ide_rhcrp,mes_rhcrp||'  '||fecha_inicio_rhcrp ||'  '|| fecha_fin_rhcrp as periodo from reh_cab_rol_pago ");
            aut_periodo.setMetodoChange("filtrarPeriodo");
            
            bar_botones.agregarComponente(new Etiqueta("Periodo: "));

            bar_botones.agregarComponente(aut_periodo);

            Boton bot_clean = new Boton();
            bot_clean.setIcon("ui-icon-cancel");
            bot_clean.setTitle("Limpiar");
            bot_clean.setMetodo("limpiar");
            bar_botones.agregarComponente(bot_clean);


            bot_importar_rub.setValue("IMPORTAR RUBROS");
            bot_importar_rub.setMetodo("cargar_rubros_todos_empleados");
            bot_importar_rub.setUpdate("tab_tabla3");
            bar_botones.agregarBoton(bot_importar_rub);

            bot_importar_emp.setValue("IMPORTAR EMPLEADOS");
            bot_importar_emp.setMetodo("cargar_empleados");
            bot_importar_emp.setUpdate("tab_tabla2");
            bar_botones.agregarBoton(bot_importar_emp);

            Boton bot_cerrar_nomina = new Boton();
            bot_cerrar_nomina.setValue("CERRAR NOMINA");
            bot_cerrar_nomina.setMetodo("cerrarNomina");
            bar_botones.agregarBoton(bot_cerrar_nomina);


            con_guardar.setId("con_guardar");
            agregarComponente(con_guardar);

            bot_generar_asiento.setValue("GENERAR ASIENTO");
            bot_generar_asiento.setMetodo("generarAsiento");

            bot_generar_asiento_provisiones.setValue("ASIENTO PROVISIONES");
            bot_generar_asiento_provisiones.setMetodo("generarAsientoProvisiones");

            bar_botones.agregarBoton(bot_generar_asiento);
            bar_botones.agregarBoton(bot_generar_asiento_provisiones);
            bar_botones.agregarReporte();

// ************* PARA EL VISUALIZADOR DE COMPROBANTE DE CONTABILIDAD


            via_comprobante_conta.setId("via_comprobante_conta");
            via_comprobante_conta.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
            via_comprobante_conta.getBot_cancelar().setMetodo("cancelarDialogo");
            via_comprobante_conta.setDynamic(false);

            agregarComponente(via_comprobante_conta);

//------------------------------------------------------------------------------------
            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sel_formato.setId("sel_formato");
            sel_tab.setId("sel_tab");
            sel_tab.setSeleccionTabla("select ide_geper,nom_geper,identificac_geper from gen_persona where es_empleado_geper is true and ide_rheem=0 and ide_empr=" + utilitario.getVariable("ide_empr"), "ide_geper");
            sel_tab.getTab_seleccion().getColumna("nom_geper").setFiltro(true);
            gru_pantalla.getChildren().add(sel_tab);
            gru_pantalla.getChildren().add(sel_formato);

            sel_tab.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab.getBot_aceptar().setUpdate("sel_tab");

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("reh_cab_rol_pago", "ide_rhcrp", 1);

            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setAutoCompletar();
            tab_tabla1.onSelect("seleccionar_tabla1");
            tab_tabla1.getColumna("IDE_RHERO").setCombo("reh_estado_rol", "ide_rhero", "nombre_rhero", "");
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("mes_rhcrp").setLectura(true);
            tab_tabla1.getColumna("fecha_sistema_rhcrp").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_sistema_rhcrp").setLectura(true);
            tab_tabla1.getColumna("ide_cnccc").setLectura(true);
            tab_tabla1.getColumna("mes_rhcrp").setUnico(true);
            tab_tabla1.setCampoOrden("ide_rhcrp desc");
            tab_tabla1.setRecuperarLectura(true);
            tab_tabla1.setCondicion("ide_rhcrp=-1");
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("reh_empleados_rol", "ide_rherl", 2);
            tab_tabla2.getColumna("ide_rheem").setCombo("reh_estado_emplea", "ide_rheem", "nombre_rheem", "");
            tab_tabla2.getColumna("ide_rheem").setAutoCompletar();
            tab_tabla2.getColumna("ide_rheem").setLectura(true);
            tab_tabla2.getColumna("ide_rheor").setCombo("reh_estruc_organi", "ide_rheor", "nombre_rheor", "");
            tab_tabla2.getColumna("ide_rheor").setAutoCompletar();
            tab_tabla2.getColumna("ide_rheor").setLectura(true);

            tab_tabla2.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper", "");
            tab_tabla2.getColumna("ide_geper").setAutoCompletar();
            tab_tabla2.getColumna("ide_geper").setLectura(true);

            tab_tabla2.getColumna("ide_rhtco").setCombo("reh_tipo_contrato", "ide_rhtco", "nombre_rhtco", "");
            tab_tabla2.getColumna("ide_rhtco").setAutoCompletar();
            tab_tabla2.getColumna("ide_rhtco").setLectura(true);

            tab_tabla2.getColumna("IDE_RHSEG").setCombo("reh_seguro", "IDE_RHSEG", "NOMBRE_RHSEG", "");
            tab_tabla2.getColumna("IDE_RHSEG").setAutoCompletar();
            tab_tabla2.getColumna("IDE_RHSEG").setLectura(true);

            tab_tabla2.getColumna("IDE_RHFPA").setCombo("reh_forma_pago", "IDE_RHFPA", "NOMBRE_RHFPA", "");
            tab_tabla2.getColumna("IDE_RHFPA").setAutoCompletar();
            tab_tabla2.getColumna("IDE_RHFPA").setLectura(true);
            tab_tabla2.getColumna("IDE_GEORG").setCombo("gen_organigrama", "IDE_GEORG", "NOMBRE_GEORG", "");
            tab_tabla2.getColumna("IDE_GEORG").setAutoCompletar();
            tab_tabla2.getColumna("IDE_GEORG").setLectura(true);
            tab_tabla2.onSelect("seleccionar_tabla2");

            tab_tabla2.setRecuperarLectura(false);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            tab_tabla3.setId("tab_tabla3");
            tab_tabla3.setTabla("reh_rubros_rol", "ide_rhrro", 3);
            tab_tabla3.getColumna("IDE_RHCRU").setCombo("reh_cab_rubro", "IDE_RHCRU", "nombre_rhcru", "");
            tab_tabla3.getColumna("IDE_RHCRU").setAutoCompletar();
            tab_tabla3.setCampoOrden("ide_rhcru desc");
            tab_tabla3.getColumna("valor_rhrro").setMetodoChange("actualizar_rubros_rol");
            tab_tabla3.getColumna("ide_rherl").setVisible(false);
            tab_tabla3.getColumna("orden_rhrro").setVisible(false);
            tab_tabla3.setCampoOrden("orden_rhrro asc");
            tab_tabla3.dibujar();

            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_tabla3);

            BlockUI bloc_tabla3 = new BlockUI();
            bloc_tabla3.setBlock("tab_tabla3");
            bloc_tabla3.setTrigger("formulario:tab_tabla3");
            agregarComponente(bloc_tabla3);


            tex_sueldo_basico.setId("tex_sueldo_basico");
            tex_sueldo_basico.setDisabled(true);
            tex_aporte_personal.setId("tex_aporte_personal");
            tex_aporte_personal.setDisabled(true);
            tex_valor_horas_extras.setId("tex_valor_horas_extras");
            tex_valor_horas_extras.setDisabled(true);
            tex_valor_horas_suple.setId("tex_valor_horas_suple");
            tex_valor_horas_suple.setDisabled(true);
            tex_prestamo_hipo.setId("tex_prestamo_hipo");
            tex_prestamo_hipo.setDisabled(true);
            tex_prestamo_quiro.setId("tex_prestamo_quiro");
            tex_prestamo_quiro.setDisabled(true);

            tex_valor_a_recibir.setId("tex_valor_a_recibir");
            tex_valor_a_recibir.setDisabled(true);
            tex_total_ingresos.setId("tex_total_ingresos");
            tex_total_ingresos.setDisabled(true);
            tex_total_egresos.setId("tex_total_egresos");
            tex_total_egresos.setDisabled(true);

            Grid gri_valores = new Grid();
            gri_valores.setColumns(4);
            gri_valores.getChildren().add(new Etiqueta("Total Ingresos"));
            gri_valores.getChildren().add(tex_total_ingresos);
            gri_valores.getChildren().add(new Etiqueta("Total Egresos"));
            gri_valores.getChildren().add(tex_total_egresos);
            gri_valores.getChildren().add(new Etiqueta("Valor a Recibir"));
            gri_valores.getChildren().add(tex_valor_a_recibir);


            div_division.setId("div_division");
            Division div_1 = new Division();
            div_1.dividir2(pat_panel3, gri_valores, "80%", "H");

            Division div_2 = new Division();
            div_2.dividir2(pat_panel2, div_1, "50%", "V");




            div_division.dividir2(pat_panel1, div_2, "25%", "H");
            agregarComponente(div_division);
            agregarComponente(rep_reporte);

            cargar_tabla3();
            cargar_etiquetas();
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void cerrarNomina() {
        if (tab_tabla1.getTotalFilas() > 0) {
            if (!tab_tabla1.getValor("ide_rhero").equalsIgnoreCase(utilitario.getVariable("p_reh_estado_cerrada"))) {
                if (tab_tabla1.getValor("ide_rhero").equalsIgnoreCase(utilitario.getVariable("p_reh_estado_pre_nomina"))) {

                    if (!con_guardar.isVisible()) {
                        con_guardar.setHeader("CONFIRMACION");
                        con_guardar.setMessage("Esta seguro de Cerrar la nomina, Si cierra la nomina los datos no podran ser modificados");
                        con_guardar.getBot_aceptar().setMetodo("cerrarNomina");
                        con_guardar.dibujar();
                        utilitario.addUpdate("con_guardar");
                    } else {
                        con_guardar.cerrar();
                        utilitario.getConexion().agregarSqlPantalla("update reh_cab_rol_pago set ide_rhero=" + utilitario.getVariable("p_reh_estado_cerrada") + " where ide_rhcrp=" + tab_tabla1.getValorSeleccionado());
                        utilitario.getConexion().agregarSqlPantalla("update reh_empleados_rol set revisado_rherl=true where ide_rhcrp=" + tab_tabla1.getValorSeleccionado());
                        utilitario.getConexion().guardarPantalla();
                        tab_tabla1.setCondicion("ide_rhcrp=" + aut_periodo.getValor());
                        tab_tabla1.ejecutarSql();
                        cargar_tabla3();
                        cargar_etiquetas();
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede cerrar la nomina", "La nomina seleccionada no tiene estado de pre nomina");
                }

            } else {
                utilitario.agregarMensajeInfo("No se puede cerrar la nomina", "La nomina seleccionada ya se encuentra cerrada");
            }
        } else {
            utilitario.agregarMensajeInfo("No existen nominas en pantalla", "");
        }
    }

    public void limpiar() {
        aut_periodo.limpiar();
        tab_tabla1.setCondicion("ide_rhcrp=-1");
        tab_tabla1.ejecutarSql();
        cargar_tabla3();
        cargar_etiquetas();
        utilitario.addUpdate("aut_periodo");
    }

    public void filtrarPeriodo(SelectEvent evt) {
        aut_periodo.onSelect(evt);
        tab_tabla1.setCondicion("ide_rhcrp=" + aut_periodo.getValor());
        tab_tabla1.ejecutarSql();
        cargar_tabla3();
        cargar_etiquetas();

    }

    public List resumirComprobante(List<cls_det_comp_cont> detalles) {
        //Unifica las cuentas
        List<cls_det_comp_cont> resumen = new ArrayList();

        List l_cuenta = new ArrayList();
        List l_cab_conta = new ArrayList();
        List l_observacion = new ArrayList();
        List l_lug_apli = new ArrayList();
        List l_suma = new ArrayList();
        int band = 0;
        String cuen = "";
        double suma = 0;
        for (int i = 0; i < detalles.size(); i++) {
            cuen = detalles.get(i).getIde_cndpc();
            for (int k = 0; k < l_cuenta.size(); k++) {
                if (cuen.equals(l_cuenta.get(k).toString())) {
                    band = 1;
                }
            }
            if (band == 0) {
                l_cuenta.add(cuen);
                l_cab_conta.add(detalles.get(i).getIde_cnccc());
                l_observacion.add(detalles.get(i).getObservacion_cndcc());
                l_lug_apli.add(detalles.get(i).getIde_cnlap());
            }
            band = 0;
        }
        for (int i = 0; i < l_cuenta.size(); i++) {
            cuen = l_cuenta.get(i).toString();
            for (int j = 0; j < detalles.size(); j++) {
                if (cuen.equals(detalles.get(j).getIde_cndpc().toString())) {
                    suma = detalles.get(j).getValor_cndcc() + suma;
                }
            }
            l_suma.add(suma);
            suma = 0;
        }

        for (int i = 0; i < l_cuenta.size(); i++) {
            resumen.add(new cls_det_comp_cont(l_lug_apli.get(i).toString(), l_cuenta.get(i).toString(), Double.parseDouble(l_suma.get(i).toString()), l_observacion.get(i).toString()));
        }
        return resumen;
    }

    public void cancelarDialogo() {
        if (via_comprobante_conta.isVisible()) {
            via_comprobante_conta.cerrar();
            utilitario.addUpdate("via_comprobante_conta");
        }
        //utilitario.getConexion().rollback();******
        utilitario.getConexion().getSqlPantalla().clear();
    }

    public String obtenerCuentaContableBancos(String ide_tecba) {
        List cuenta_contable_banco1_sql = utilitario.getConexion().consultar("select ide_cndpc from tes_cuenta_banco where ide_tecba=" + ide_tecba);
        if (cuenta_contable_banco1_sql.get(0) != null) {
            return cuenta_contable_banco1_sql.get(0).toString();
        } else {
            return "";
        }
    }

    public String getParametroOrganigrama(String parametro, String ide_georg) {
        if (parametro != null && !parametro.isEmpty()
                && ide_georg != null && !ide_georg.isEmpty()) {
            TablaGenerica tab_organigrama = utilitario.consultar("select * from gen_organigrama WHERE ide_georg=" + ide_georg);
            if (tab_organigrama.getTotalFilas() > 0) {
                if (tab_organigrama.getValor(0, parametro) != null && !tab_organigrama.getValor(0, parametro).isEmpty()) {
                    return tab_organigrama.getValor(0, parametro);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    List lis_empleados = new ArrayList();
    List lis_rubro = new ArrayList();
    List lis_valor = new ArrayList();
    List lis_cuentas = new ArrayList();
    List lis_valor_cuentas = new ArrayList();
    List lis_lugar_aplica = new ArrayList();
    List lis_observacion = new ArrayList();
    String str_cuenta_contable_banco;
    String str_cuenta_banco;
    String str_empleados_sin_departamneto;

    public void generarAsiento() {

        if (tab_tabla1.getTotalFilas() > 0) {
            if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_rhero").equalsIgnoreCase(utilitario.getVariable("p_reh_estado_cerrada"))) {
                if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cnccc") == null
                        || tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cnccc").isEmpty()) {

                    lis_empleados.clear();
                    lis_rubro.clear();
                    lis_valor.clear();
                    lis_valor_cuentas.clear();
                    lis_cuentas.clear();
                    lis_lugar_aplica.clear();
                    lis_observacion.clear();
                    str_cuenta_contable_banco = "";
                    List lis_empl = new ArrayList();
                    TablaGenerica tab_rubros_configurados = utilitario.consultar("select cr.ide_rhcru,cr.nombre_rhcru from reh_cab_rubro cr, reh_detalle_rubro dr "
                            + "where cr.ide_rhcru=dr.ide_rhcru "
                            + "and cr.ide_rhcru not in (58,56) "
                            + "and cr.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                            + "group by cr.ide_rhcru");
                    System.out.println("rub conf "+tab_rubros_configurados.getSql());
                    TablaGenerica tab_empleados_rol = utilitario.consultar("select *from reh_empleados_rol where revisado_rherl is TRUE and ide_rhcrp=" + tab_tabla1.getValor("ide_rhcrp"));
                    for (int i = 0; i < tab_rubros_configurados.getTotalFilas(); i++) {
                        for (int j = 0; j < tab_empleados_rol.getTotalFilas(); j++) {
                            TablaGenerica tab_rubro_rol = utilitario.consultar("select *from reh_rubros_rol where ide_rherl=" + tab_empleados_rol.getValor(j, "ide_rherl") + " and ide_rhcru=" + tab_rubros_configurados.getValor(i, "ide_rhcru"));
                            if (tab_rubro_rol.getTotalFilas() > 0) {
                                if (tab_rubro_rol.getValor(0, "valor_rhrro") != null && !tab_rubro_rol.getValor(0, "valor_rhrro").isEmpty()) {
                                    lis_empl.add(tab_empleados_rol.getValor(j, "ide_geper"));
                                    lis_rubro.add(tab_rubros_configurados.getValor(i, "ide_rhcru"));
                                    lis_valor.add(tab_rubro_rol.getValor(0, "valor_rhrro"));
                                }
                            }
                        }
                    }
                    System.out.println("lis empleados " + lis_empl.size());
                    String ide_georg;
                    String ide_cndpc;
                    String ide_cnlap;
                    String observacion;
                    for (int i = 0; i < lis_empl.size(); i++) {
                        // CONFIGURACIONES DEBE
                        String ide_georg_asiento = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_georg", utilitario.getVariable("p_con_lugar_debe"));
                        if (ide_georg_asiento == null || ide_georg_asiento.isEmpty()) {
                            ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cndpc", utilitario.getVariable("p_con_lugar_debe"));
                            ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cnlap", utilitario.getVariable("p_con_lugar_debe"));
                            if (ide_cndpc != null && ide_cnlap != null) {
                                lis_empleados.add(lis_empl.get(i));
                                lis_cuentas.add(ide_cndpc);
                                lis_valor_cuentas.add(lis_valor.get(i));
                                lis_lugar_aplica.add(ide_cnlap);
                                lis_observacion.add("");
                            }
                        } else {
                            ide_georg = obtenerDepartamentoEmpleado(lis_empl.get(i) + "");
                            ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cndpc", utilitario.getVariable("p_con_lugar_debe"));
                            ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cnlap", utilitario.getVariable("p_con_lugar_debe"));
                            if (ide_georg != null) {
                                if (ide_cndpc != null && ide_cnlap != null) {
                                    lis_empleados.add(lis_empl.get(i));
                                    lis_cuentas.add(ide_cndpc);
                                    lis_valor_cuentas.add(lis_valor.get(i));
                                    lis_lugar_aplica.add(ide_cnlap);
                                    lis_observacion.add(getParametroOrganigrama("nombre_georg", ide_georg));
                                }
                            } else {

                                str_empleados_sin_departamneto += obtenerNombreEmpleado(lis_empl.get(i) + "\n");
                            }
                        }
                        // CONFIGURACION PARA EL HABER
                        ide_georg_asiento = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_georg", utilitario.getVariable("p_con_lugar_haber"));
                        if (ide_georg_asiento == null || ide_georg_asiento.isEmpty()) {
                            ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cndpc", utilitario.getVariable("p_con_lugar_haber"));
                            ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cnlap", utilitario.getVariable("p_con_lugar_haber"));
                            if (ide_cndpc != null && ide_cnlap != null) {
                                lis_empleados.add(lis_empl.get(i));
                                lis_cuentas.add(ide_cndpc);
                                lis_valor_cuentas.add(lis_valor.get(i));
                                lis_lugar_aplica.add(ide_cnlap);
                                lis_observacion.add("");
                            }
                        } else {
                            ide_georg = obtenerDepartamentoEmpleado(lis_empl.get(i) + "");
                            ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cndpc", utilitario.getVariable("p_con_lugar_haber"));
                            ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cnlap", utilitario.getVariable("p_con_lugar_haber"));
                            if (ide_georg != null) {
                                if (ide_cndpc != null && ide_cnlap != null) {
                                    lis_empleados.add(lis_empl.get(i));
                                    lis_cuentas.add(ide_cndpc);
                                    lis_valor_cuentas.add(lis_valor.get(i));
                                    lis_lugar_aplica.add(ide_cnlap);
                                    lis_observacion.add(getParametroOrganigrama("nombre_georg", ide_georg));
                                }
                            } else {
                                str_empleados_sin_departamneto += obtenerNombreEmpleado(lis_empl.get(i) + "\n");
                            }
                        }
                    }
                    System.out.println("empl sin departamento " + str_empleados_sin_departamneto);


                    // para el dialogo comprobante contabilidad
                    conta.limpiar();
                    lista_detalles.clear();
                    cab_com_con = new cls_cab_comp_cont(utilitario.getVariable("p_con_tipo_comprobante_egreso"), utilitario.getVariable("p_con_estado_comprobante_normal"), "7", utilitario.getVariable("p_gen_beneficiario_roles"), utilitario.getFechaActual(), "");

                    double valor_acu = 0;
                    for (int i = 0; i < lis_cuentas.size(); i++) {
                        try {
                            if (lis_valor_cuentas.get(i) != null && !lis_valor_cuentas.get(i).toString().isEmpty()) {
                                if (Double.parseDouble(lis_valor_cuentas.get(i) + "") != 0) {
                                    lista_detalles.add(new cls_det_comp_cont(lis_lugar_aplica.get(i) + "", lis_cuentas.get(i) + "", Double.parseDouble((lis_valor_cuentas.get(i) + "")), lis_observacion.get(i) + ""));
                                    valor_acu = Double.parseDouble((lis_valor_cuentas.get(i) + "")) + valor_acu;
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                    //cuenta contable del banco por defecto hasta saber 
                    String ide_cuenta_contable = getParametroConfiguracionAsiento(utilitario.getVariable("p_reh_rubro_valor_recibir"), null, "ide_cndpc", utilitario.getVariable("p_con_lugar_haber"));
                    System.out.println("ide cuenta contableee " + ide_cuenta_contable);
                    if (ide_cuenta_contable != null) {
                        TablaGenerica tab_cuenta_banco = utilitario.consultar("select *from tes_cuenta_banco where ide_cndpc=" + ide_cuenta_contable);
                        if (tab_cuenta_banco.getTotalFilas() > 0) {
                            str_cuenta_banco = tab_cuenta_banco.getValor(0, "ide_tecba");
                        } else {
                            str_cuenta_banco = null;
                        }
                    }

//            str_cuenta_banco = "5";
//            str_cuenta_contable_banco = obtenerCuentaContableBancos("5");// cuenta bancaria 5 para el asiento 
//            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), str_cuenta_contable_banco, Double.parseDouble(utilitario.getFormatoNumero(valor_acu, 2)), ""));
//            String str_ide_cndpc=getParametroConfiguracionAsiento("37", null, "ide_cndpc");
//            String str_ide_cnlap=getParametroConfiguracionAsiento("37", null, "ide_cnlap");
//            lista_detalles.add(new cls_det_comp_cont(str_ide_cnlap, str_ide_cndpc, Double.parseDouble(utilitario.getFormatoNumero(valor_acu, 2)), ""));
                    cab_com_con.setDetalles(lista_detalles);
                    via_comprobante_conta.setVistaAsiento(cab_com_con);
                    boo_hizo_asiento_provisiones = false;
                    via_comprobante_conta.dibujar();
                    utilitario.addUpdate("via_comprobante_conta");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el Asiento", "El rol de pago seleciionado ya tiene generado un asiento contable");
                }

            } else {
                utilitario.agregarMensajeInfo("No se puede generar el asiento", "la nomina seleccionada no se encuentra cerrada");
            }
        } else {
            utilitario.agregarMensajeInfo("No existen datos", "No tiene roles de pago");
        }

    }
//yo
    boolean boo_hizo_asiento_provisiones = false;

    public void generarAsientoProvisiones() {

        if (tab_tabla1.getTotalFilas() > 0) {

            if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_rhero").equalsIgnoreCase(utilitario.getVariable("p_reh_estado_cerrada"))) {
                lis_empleados.clear();
                lis_rubro.clear();
                lis_valor.clear();
                lis_valor_cuentas.clear();
                lis_cuentas.clear();
                lis_lugar_aplica.clear();
                lis_observacion.clear();
                str_cuenta_contable_banco = "";
                List lis_empl = new ArrayList();
                TablaGenerica tab_rubros_configurados = utilitario.consultar("select cr.ide_rhcru,cr.nombre_rhcru from reh_cab_rubro cr, reh_detalle_rubro dr "
                        + "where cr.ide_rhcru=dr.ide_rhcru "
                        + "and cr.ide_rhcru in (58,56) "
                        + "and cr.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "group by cr.ide_rhcru");
                TablaGenerica tab_empleados_rol = utilitario.consultar("select *from reh_empleados_rol where revisado_rherl is TRUE and ide_rhcrp=" + tab_tabla1.getValor("ide_rhcrp"));
                for (int i = 0; i < tab_rubros_configurados.getTotalFilas(); i++) {
                    for (int j = 0; j < tab_empleados_rol.getTotalFilas(); j++) {
                        TablaGenerica tab_rubro_rol = utilitario.consultar("select *from reh_rubros_rol where ide_rherl=" + tab_empleados_rol.getValor(j, "ide_rherl") + " and ide_rhcru=" + tab_rubros_configurados.getValor(i, "ide_rhcru"));
                        if (tab_rubro_rol.getTotalFilas() > 0) {
                            if (tab_rubro_rol.getValor(0, "valor_rhrro") != null && !tab_rubro_rol.getValor(0, "valor_rhrro").isEmpty()) {
                                lis_empl.add(tab_empleados_rol.getValor(j, "ide_geper"));
                                lis_rubro.add(tab_rubros_configurados.getValor(i, "ide_rhcru"));
                                lis_valor.add(tab_rubro_rol.getValor(0, "valor_rhrro"));
                            }
                        }
                    }
                }
                System.out.println("lis empleados " + lis_empl.size());
                String ide_georg;
                String ide_cndpc;
                String ide_cnlap;
                String observacion;
                for (int i = 0; i < lis_empl.size(); i++) {
                    // CONFIGURACIONES DEBE
                    String ide_georg_asiento = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_georg", utilitario.getVariable("p_con_lugar_debe"));
                    if (ide_georg_asiento == null || ide_georg_asiento.isEmpty()) {
                        ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cndpc", utilitario.getVariable("p_con_lugar_debe"));
                        ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cnlap", utilitario.getVariable("p_con_lugar_debe"));
                        if (ide_cndpc != null && ide_cnlap != null) {
                            lis_empleados.add(lis_empl.get(i));
                            lis_cuentas.add(ide_cndpc);
                            lis_valor_cuentas.add(lis_valor.get(i));
                            lis_lugar_aplica.add(ide_cnlap);
                            lis_observacion.add("");
                        }
                    } else {
                        ide_georg = obtenerDepartamentoEmpleado(lis_empl.get(i) + "");
                        ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cndpc", utilitario.getVariable("p_con_lugar_debe"));
                        ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cnlap", utilitario.getVariable("p_con_lugar_debe"));
                        if (ide_georg != null) {
                            if (ide_cndpc != null && ide_cnlap != null) {
                                lis_empleados.add(lis_empl.get(i));
                                lis_cuentas.add(ide_cndpc);
                                lis_valor_cuentas.add(lis_valor.get(i));
                                lis_lugar_aplica.add(ide_cnlap);
                                lis_observacion.add(getParametroOrganigrama("nombre_georg", ide_georg));
                            }
                        } else {

                            str_empleados_sin_departamneto += obtenerNombreEmpleado(lis_empl.get(i) + "\n");
                        }
                    }
                    // CONFIGURACION PARA EL HABER
                    ide_georg_asiento = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_georg", utilitario.getVariable("p_con_lugar_haber"));
                    if (ide_georg_asiento == null || ide_georg_asiento.isEmpty()) {
                        ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cndpc", utilitario.getVariable("p_con_lugar_haber"));
                        ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", null, "ide_cnlap", utilitario.getVariable("p_con_lugar_haber"));
                        if (ide_cndpc != null && ide_cnlap != null) {
                            lis_empleados.add(lis_empl.get(i));
                            lis_cuentas.add(ide_cndpc);
                            lis_valor_cuentas.add(lis_valor.get(i));
                            lis_lugar_aplica.add(ide_cnlap);
                            lis_observacion.add("");
                        }
                    } else {
                        ide_georg = obtenerDepartamentoEmpleado(lis_empl.get(i) + "");
                        ide_cndpc = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cndpc", utilitario.getVariable("p_con_lugar_haber"));
                        ide_cnlap = getParametroConfiguracionAsiento(lis_rubro.get(i) + "", ide_georg, "ide_cnlap", utilitario.getVariable("p_con_lugar_haber"));
                        if (ide_georg != null) {
                            if (ide_cndpc != null && ide_cnlap != null) {
                                lis_empleados.add(lis_empl.get(i));
                                lis_cuentas.add(ide_cndpc);
                                lis_valor_cuentas.add(lis_valor.get(i));
                                lis_lugar_aplica.add(ide_cnlap);
                                lis_observacion.add(getParametroOrganigrama("nombre_georg", ide_georg));
                            }
                        } else {
                            str_empleados_sin_departamneto += obtenerNombreEmpleado(lis_empl.get(i) + "\n");
                        }
                    }
                }
                System.out.println("empl sin departamento " + str_empleados_sin_departamneto);


                // para el dialogo comprobante contabilidad
                conta.limpiar();
                lista_detalles.clear();
                cab_com_con = new cls_cab_comp_cont(utilitario.getVariable("p_con_tipo_comprobante_diario"), utilitario.getVariable("p_con_estado_comprobante_normal"), "7", utilitario.getVariable("p_gen_beneficiario_roles"), utilitario.getFechaActual(), "");

                double valor_acu = 0;
                for (int i = 0; i < lis_cuentas.size(); i++) {
                    try {
                        if (lis_valor_cuentas.get(i) != null && !lis_valor_cuentas.get(i).toString().isEmpty()) {
                            if (Double.parseDouble(lis_valor_cuentas.get(i) + "") != 0) {
                                lista_detalles.add(new cls_det_comp_cont(lis_lugar_aplica.get(i) + "", lis_cuentas.get(i) + "", Double.parseDouble((lis_valor_cuentas.get(i) + "")), lis_observacion.get(i) + ""));
                                valor_acu = Double.parseDouble((lis_valor_cuentas.get(i) + "")) + valor_acu;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                //cuenta contable del banco por defecto hasta saber 
                String ide_cuenta_contable = getParametroConfiguracionAsiento(utilitario.getVariable("p_reh_rubro_valor_recibir"), null, "ide_cndpc", utilitario.getVariable("p_con_lugar_haber"));
                System.out.println("ide cuenta contableee " + ide_cuenta_contable);
                if (ide_cuenta_contable != null) {
                    TablaGenerica tab_cuenta_banco = utilitario.consultar("select *from tes_cuenta_banco where ide_cndpc=" + ide_cuenta_contable);
                    if (tab_cuenta_banco.getTotalFilas() > 0) {
                        str_cuenta_banco = tab_cuenta_banco.getValor(0, "ide_tecba");
                    } else {
                        str_cuenta_banco = null;
                    }
                }

                cab_com_con.setDetalles(lista_detalles);
                via_comprobante_conta.setVistaAsiento(cab_com_con);
                boo_hizo_asiento_provisiones = true;
                via_comprobante_conta.dibujar();
                utilitario.addUpdate("via_comprobante_conta");
            } else {
                utilitario.agregarMensajeInfo("No se puede generar el asiento", "la nomina seleccionada no se encuentra cerrada ");
            }

        }

    }

    public String getParametroConfiguracionAsiento(String ide_rhcru, String ide_georg, String parametro, String ide_cnlap) {

        if (parametro != null && !parametro.isEmpty()) {
            if (ide_rhcru != null && !ide_rhcru.isEmpty()) {
                if (ide_cnlap != null && !ide_cnlap.isEmpty()) {
                    TablaGenerica tab_detalle_rubro;
                    if (ide_georg != null && !ide_georg.isEmpty()) {
                        tab_detalle_rubro = utilitario.consultar("select *from reh_detalle_rubro where ide_rhcru=" + ide_rhcru + " and ide_georg=" + ide_georg + " and ide_cnlap=" + ide_cnlap);
                    } else {
                        tab_detalle_rubro = utilitario.consultar("select *from reh_detalle_rubro where ide_rhcru=" + ide_rhcru + " and ide_cnlap=" + ide_cnlap);
                    }
                    if (tab_detalle_rubro.getTotalFilas() > 0) {
                        return tab_detalle_rubro.getValor(0, parametro);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void aceptarComprobanteContabilidad() {

        if (via_comprobante_conta.validarComprobante()) {
            cab_com_con.setObservacion_cnccc(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            lista_detalles.clear();
            for (int i = 0; i < via_comprobante_conta.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            conta.generarAsientoContable(cab_com_con);
            String ide_cnccc = conta.getTab_cabecera().getValor("ide_cnccc");
            if (ide_cnccc != null) {
                if (boo_hizo_asiento_provisiones == false) {
                    cls_bancos bancos = new cls_bancos();
                    TablaGenerica tab_empl_rol = utilitario.consultar("select * from reh_empleados_rol "
                            + "where ide_rhcrp=" + tab_tabla1.getValor("ide_rhcrp") + " "
                            + "and revisado_rherl is TRUE");

                    String ide_cuenta_contable = getParametroConfiguracionAsiento(utilitario.getVariable("p_reh_rubro_valor_recibir"), null, "ide_cndpc", utilitario.getVariable("p_con_lugar_haber"));
                    double valor = 0;
                    for (int i = 0; i < via_comprobante_conta.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                        if (ide_cuenta_contable.equals(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"))) {
                            valor = Double.parseDouble(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc"));
                        }
                    }
                    bancos.guardarLibroBancoRolesPago(ide_cnccc, str_cuenta_banco, valor, utilitario.getVariable("p_gen_beneficiario_roles"), via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
                    utilitario.getConexion().agregarSqlPantalla("UPDATE reh_cab_rol_pago set ide_cnccc=" + ide_cnccc + " where ide_rhcrp=" + tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_rhcrp"));
                } else {
                    utilitario.getConexion().agregarSqlPantalla("UPDATE reh_cab_rol_pago set ide_cnccc_provisiones=" + ide_cnccc + " where ide_rhcrp=" + tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_rhcrp"));
                }
            }
            via_comprobante_conta.cerrar();
            utilitario.addUpdate("via_comprobante_conta");
            utilitario.getConexion().guardarPantalla();
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_rhcrp"));
            tab_tabla2.ejecutarValorForanea(tab_tabla2.getValor("ide_rherl"));
            utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3");
        }
    }
    String ide_cab_libro_banco;

    public String obtenerCuentaContable(String ide_rhcru, String ide_georg) {
        String ide_cndpc = "";
        if (!ide_georg.isEmpty()) {
            TablaGenerica tab_detalle_rubro = utilitario.consultar("select *from reh_detalle_rubro where ide_rhcru=" + ide_rhcru + " and ide_georg=" + ide_georg);
            if (tab_detalle_rubro.getTotalFilas() > 0) {
                if (tab_detalle_rubro.getValor(0, "ide_cndpc") != null) {
                    ide_cndpc = tab_detalle_rubro.getValor(0, "ide_cndpc") + "";
                }
            }
        } else {
            TablaGenerica tab_detalle_rubro = utilitario.consultar("select *from reh_detalle_rubro where ide_rhcru=" + ide_rhcru + " and ide_georg is null ");
            if (tab_detalle_rubro.getTotalFilas() > 0) {
                if (tab_detalle_rubro.getValor(0, "ide_cndpc") != null) {
                    ide_cndpc = tab_detalle_rubro.getValor(0, "ide_cndpc") + "";
                }
            }
        }

        return ide_cndpc;
    }

    public String obtenerLugarAplica(String ide_rhcru, String ide_georg) {
        String ide_cnlap = "";
        if (!ide_georg.isEmpty()) {
            TablaGenerica tab_detalle_rubro = utilitario.consultar("select *from reh_detalle_rubro where ide_rhcru=" + ide_rhcru + " and ide_georg=" + ide_georg);
            if (tab_detalle_rubro.getTotalFilas() > 0) {
                if (tab_detalle_rubro.getValor(0, "ide_cnlap") != null) {
                    ide_cnlap = tab_detalle_rubro.getValor(0, "ide_cnlap") + "";
                }
            }
        } else {
            TablaGenerica tab_detalle_rubro = utilitario.consultar("select *from reh_detalle_rubro where ide_rhcru=" + ide_rhcru + " and ide_georg is null");
            if (tab_detalle_rubro.getTotalFilas() > 0) {
                if (tab_detalle_rubro.getValor(0, "ide_cnlap") != null) {
                    ide_cnlap = tab_detalle_rubro.getValor(0, "ide_cnlap") + "";
                }
            }
        }

        return ide_cnlap;
    }

    public String obtenerNombreEmpleado(String ide_geper) {
        if (ide_geper != null && !ide_geper.isEmpty()) {
            TablaGenerica tab_persona = utilitario.consultar("select * from gen_persona where ide_geper=" + ide_geper);
            if (tab_persona.getTotalFilas() > 0) {
                if (tab_persona.getValor(0, "nom_geper") != null && !tab_persona.getValor(0, "nom_geper").isEmpty()) {
                    return tab_persona.getValor(0, "nom_geper");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String obtenerDepartamentoEmpleado(String ide_geper) {
        if (ide_geper != null && !ide_geper.isEmpty()) {
            TablaGenerica tab_persona = utilitario.consultar("select * from gen_persona where ide_geper=" + ide_geper);
            if (tab_persona.getTotalFilas() > 0) {
                if (tab_persona.getValor(0, "ide_georg") != null && !tab_persona.getValor(0, "ide_georg").isEmpty()) {
                    return tab_persona.getValor(0, "ide_georg");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

//    public void generarTransaccionCxP() {
//        
//        String str_ide_ttr_factura = utilitario.getVariable("p_cxp_tipo_trans_factura");
//        Tabla tab_cab_tran_cxp = new Tabla();
//        tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr", 0);
//        tab_cab_tran_cxp.setCondicion("ide_cpctr=-1");
//        tab_cab_tran_cxp.ejecutarSql();
//        
//        
//        for (int i = 0; i < lis_empleados; i++) {
//            
//        }
//        tab_cab_tran_cxp.insertar();
//        tab_cab_tran_cxp.setValor("ide_cpttr", str_ide_ttr_factura + "");
//        tab_cab_tran_cxp.setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
//        tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_tabla1.getValor("fecha_trans_cpcfa"));
//        tab_cab_tran_cxp.setValor("observacion_cpctr", tab_tabla1.getValor("observacion_cpcfa"));
//
//        Tabla tab_det_tran_cxp = new Tabla();
//        tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr", 1);
//        tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
//        tab_det_tran_cxp.ejecutarSql();
//        tab_cab_tran_cxp.guardar();
//
//        tab_det_tran_cxp.insertar();
//        tab_det_tran_cxp.setValor("ide_cpcfa", tab_tabla1.getValor("ide_cpcfa"));
//        tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
//        tab_det_tran_cxp.setValor("ide_cpttr", p_tipo_transaccioncxp);
//        tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
//        tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_tabla1.getValor("fecha_trans_cpcfa"));
//        double valor = Double.parseDouble(tab_tabla1.getValor("total_cpcfa")) - total_retenido;
//        tab_det_tran_cxp.setValor("valor_cpdtr", valor + "");
//        tab_det_tran_cxp.setValor("observacion_cpdtr", tab_tabla1.getValor("observacion_cpcfa"));
//        tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
//        tab_det_tran_cxp.setValor("fecha_venci_cpdtr", obtener_fecha_vencimiento());
//        tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_tabla1.getValor("numero_cpcfa"));
//
//        tab_det_tran_cxp.guardar();
//
//    }
    public void actualizar_rubros_rol(AjaxBehaviorEvent evt) {
        tab_tabla3.modificar(evt);
        cargar_rubros();
        utilitario.addUpdate("tab_tabla3");
        cargar_etiquetas();
    }

    @Override
    public void insertar() {
        limpiar();
        if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.insertar();
        } else if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_rhero", utilitario.getVariable("p_reh_estado_pre_nomina"));
        } else {
            utilitario.getTablaisFocus().insertar();
        }

    }

    public int obtener_fila_rubro_etiqueta(String ide_rhcru) {
        int i = -1;
        if (tab_tabla3.getTotalFilas() > 0) {
            do {
                i = i + 1;
            } while (!tab_tabla3.getValor(i, "ide_rhcru").equals(ide_rhcru));
            return i;
        }
        return i;
    }

    public void cargar_etiquetas() {
        // cargar etiqueta sueldo basico
        if (obtener_fila_rubro_etiqueta("7") != -1) {
            tex_sueldo_basico.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("7"), "valor_rhrro"));
        } else {
            tex_sueldo_basico.setValue("");
        }
        if (obtener_fila_rubro_etiqueta("28") != -1) {
            tex_aporte_personal.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("28"), "valor_rhrro"));
        } else {
            tex_aporte_personal.setValue("");
        }
        if (obtener_fila_rubro_etiqueta("37") != -1) {
            tex_valor_a_recibir.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("37"), "valor_rhrro"));
        } else {
            tex_valor_a_recibir.setValue("");
        }
        //PI
        if (obtener_fila_rubro_etiqueta("30") != -1) {
            tex_prestamo_hipo.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("30"), "valor_rhrro"));
        } else {
            tex_prestamo_hipo.setValue("");
        }
        //PQ
        if (obtener_fila_rubro_etiqueta("29") != -1) {
            tex_prestamo_quiro.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("29"), "valor_rhrro"));
        } else {
            tex_prestamo_quiro.setValue("");
        }

        //extras
        if (obtener_fila_rubro_etiqueta("19") != -1) {
            tex_valor_horas_extras.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("19"), "valor_rhrro"));
        } else {
            tex_valor_horas_extras.setValue("");
        }
        //supl
        if (obtener_fila_rubro_etiqueta("17") != -1) {
            tex_valor_horas_suple.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("17"), "valor_rhrro"));
        } else {
            tex_valor_horas_suple.setValue("");
        }

        //tot ing
        if (obtener_fila_rubro_etiqueta("27") != -1) {
            tex_total_ingresos.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("27"), "valor_rhrro"));
        } else {
            tex_total_ingresos.setValue("");
        }

        //tot egre
        if (obtener_fila_rubro_etiqueta("33") != -1) {
            tex_total_egresos.setValue(tab_tabla3.getValor(obtener_fila_rubro_etiqueta("33"), "valor_rhrro"));
        } else {
            tex_total_egresos.setValue("");
        }


        utilitario.addUpdate("tex_sueldo_basico,tex_aporte_personal,tex_prestamo_hipo,tex_prestamo_quiro,tex_valor_a_recibir,tex_valor_horas_extras,tex_valor_horas_suple,tex_total_egresos,tex_total_ingresos");

    }

    @Override
    public void guardar() {
        tab_tabla1.setValor("mes_rhcrp", retornar_mes_letras(utilitario.getMes(tab_tabla1.getValor("fecha_inicio_rhcrp"))));
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        if (tab_tabla3.isFilaModificada()) {
            tab_tabla3.guardar();
        }

        guardarPantalla();
    }

    public String retornar_mes_letras(int mes) {
        String mes1 = "";
        if (mes == 1) {
            mes1 = "ENERO";
        }
        if (mes == 2) {
            mes1 = "FEBRERO";
        }
        if (mes == 3) {
            mes1 = "MARZO";
        }
        if (mes == 4) {
            mes1 = "ABRIL";
        }
        if (mes == 5) {
            mes1 = "MAYO";
        }
        if (mes == 6) {
            mes1 = "JUNIO";
        }
        if (mes == 7) {
            mes1 = "JULIO";
        }
        if (mes == 8) {
            mes1 = "AGOSTO";
        }
        if (mes == 9) {
            mes1 = "SEPTIEMBRE";
        }
        if (mes == 10) {
            mes1 = "OCTUBRE";
        }
        if (mes == 11) {
            mes1 = "NOVIEMBRE";
        }
        if (mes == 12) {
            mes1 = "DICIEMBRE";
        }
        return mes1;
    }

    public void cargar_empleados() {
        List lista_empleados = utilitario.getConexion().consultar("select ide_rheem,ide_rheor,ide_geper,ide_rhtco,ide_rhseg,ide_rhfpa,ide_georg from gen_persona where es_empleado_geper=TRUE and ide_rheem=0 order by nom_geper desc");
        for (int i = 0; i < lista_empleados.size(); i++) {
            Object[] fila = (Object[]) lista_empleados.get(i);
            tab_tabla2.insertar();
            tab_tabla2.setValor("ide_rheem", "" + fila[0]);
            tab_tabla2.setValor("ide_rheor", "" + fila[1]);
            tab_tabla2.setValor("ide_geper", "" + fila[2]);
            tab_tabla2.setValor("ide_rhtco", "" + fila[3]);
            tab_tabla2.setValor("ide_rhseg", "" + fila[4]);
            tab_tabla2.setValor("ide_rhfpa", "" + fila[5]);
            tab_tabla2.setValor("ide_georg", "" + fila[6]);
        }


    }

    public void cargar_rubros_todos_empleados() {
        if (tab_tabla3.getTotalFilas() == 0) {
            nomina.cargarRubrosEmpleados(tab_tabla2, tab_tabla1.getValor("fecha_fin_rhcrp"));
            cargar_tabla3();
            cargar_etiquetas();
        } else {
            utilitario.agregarMensajeInfo("Atencion", "ya se encuentran rubros importados");
        }
    }

    public String getParametroEmpleado(String parametro, String ide_geper) {
        if (parametro != null && !parametro.isEmpty() && ide_geper != null && !ide_geper.isEmpty()) {
            TablaGenerica tab_empl = utilitario.consultar("select * FROM gen_persona where ide_geper=" + ide_geper);
            if (tab_empl.getTotalFilas() > 0) {
                if (tab_empl.getValor(0, parametro) != null && !tab_empl.getValor(0, parametro).isEmpty()) {
                    return tab_empl.getValor(0, parametro);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void cargar_rubros() {

        nomina.asignarRubrosSinFormula(tab_tabla2.getValor("ide_geper"), tab_tabla1.getValor("fecha_fin_rhcrp"));
        if (tab_tabla3.getTotalFilas() > 0) {
            TablaGenerica tab_rubros = new TablaGenerica();
            tab_rubros = utilitario.consultar("select * from reh_cab_rubro where ide_rhfca=1");// 1 es el ide de forma de calculo por teclado
            // asignacion de rubros ingresados por teclado
            nomina.asignarRubrosTipoTeclado(tab_tabla3);
            // calculo de los rubros que tienen formula
            boolean boo_acumula_fondos = nomina.acumulaFondosReserva(tab_tabla2.getValor("ide_geper"));
            nomina.calcularRubrosConFormula(boo_acumula_fondos);
            for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
                for (int j = 0; j < nomina.getLis_rubros().size(); j++) {
                    String[] fila = (String[]) nomina.getLis_rubros().get(j);
                    if (tab_tabla3.getValor(i, "ide_rhcru").equals(fila[0])) {
                        tab_tabla3.modificar(i);
                        tab_tabla3.setValor(i, "valor_rhrro", fila[1]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.eliminar();
        } else {
            utilitario.getTablaisFocus().eliminar();
        }
    }

    public void cargar_tabla3() {
        if (tab_tabla2.getTotalFilas() == 0) {
            tab_tabla3.setCondicion("ide_rherl=-1");
            tab_tabla3.ejecutarSql();
        } else {

            tab_tabla3.setCondicion("ide_rherl=" + tab_tabla2.getValor("ide_rherl"));
            tab_tabla3.ejecutarSql();

            for (int j = 0; j < tab_tabla3.getTotalFilas(); j++) {
                tab_tabla3.getFilas().get(j).setLectura(true);
            }
            TablaGenerica tab_rub_teclado = utilitario.consultar("select * from reh_cab_rubro where ide_rhfca=1");
            if (tab_tabla1.getValor("IDE_RHERO") != null
                    && !tab_tabla1.getValor("IDE_RHERO").isEmpty()
                    && tab_tabla1.getValor("IDE_RHERO").equalsIgnoreCase(utilitario.getVariable("p_reh_estado_pre_nomina"))) {
                for (int i = 0; i < tab_rub_teclado.getTotalFilas(); i++) {
                    for (int j = 0; j < tab_tabla3.getTotalFilas(); j++) {
                        if (tab_rub_teclado.getValor(i, "IDE_RHCRU").equalsIgnoreCase(tab_tabla3.getValor(j, "IDE_RHCRU"))) {
                            tab_tabla3.getFilas().get(j).setLectura(false);
                            break;
                        }
                    }
                }
            }

        }
        utilitario.addUpdate("tab_tabla3");
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        if (tab_tabla2.getTotalFilas() == 0) {
            tab_tabla3.setCondicion("ide_rherl=-1");
            tab_tabla3.ejecutarSql();
            cargar_etiquetas();
        } else {
            tab_tabla3.setCondicion("ide_rherl=" + tab_tabla2.getValor("ide_rherl"));
            tab_tabla3.ejecutarSql();
            cargar_etiquetas();
        }
    }

    public void seleccionar_tabla2(SelectEvent evt) {
        tab_tabla2.seleccionarFila(evt);
        
        tab_tabla3.setCondicion("ide_rherl=" + tab_tabla2.getValor("ide_rherl"));
        tab_tabla3.ejecutarSql();


            for (int j = 0; j < tab_tabla3.getTotalFilas(); j++) {
                tab_tabla3.getFilas().get(j).setLectura(true);
            }
            TablaGenerica tab_rub_teclado = utilitario.consultar("select * from reh_cab_rubro where ide_rhfca=1");
            if (tab_tabla1.getValor("IDE_RHERO") != null
                    && !tab_tabla1.getValor("IDE_RHERO").isEmpty()
                    && tab_tabla1.getValor("IDE_RHERO").equalsIgnoreCase(utilitario.getVariable("p_reh_estado_pre_nomina"))) {
                for (int i = 0; i < tab_rub_teclado.getTotalFilas(); i++) {
                    for (int j = 0; j < tab_tabla3.getTotalFilas(); j++) {
                        if (tab_rub_teclado.getValor(i, "IDE_RHCRU").equalsIgnoreCase(tab_tabla3.getValor(j, "IDE_RHCRU"))) {
                            tab_tabla3.getFilas().get(j).setLectura(false);
                            break;
                        }
                    }
                }
            }
        
        
        cargar_etiquetas();

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
        if (rep_reporte.getReporteSelecionado().equals("rol de pagos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                if (tab_tabla3.getTotalFilas() > 0) {
                    sel_tab.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_tab");
                } else {
                    utilitario.agregarMensaje("informacion", "CABECERA SELECCIONADA NO TIENE RUBROS");
                    utilitario.addUpdate("rep_reporte");
                }
            } else if (sel_tab.isVisible()) {
                parametro.put("ide_geper", sel_tab.getSeleccionados());
                parametro.put("ide_rhcru", Integer.parseInt(utilitario.getVariable("p_reh_dias_trabajados")));
                parametro.put("ide_rhcrp", Integer.parseInt(tab_tabla1.getValor("ide_rhcrp")));
                System.out.println(sel_tab.getSeleccionados());
                sel_tab.cerrar();
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("sel_tab,sel_formato");
                System.out.println(tab_tabla1.getValor("ide_rhcrp"));
                System.out.println(utilitario.getVariable("p_reh_dias_trabajados"));

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Rol de Pagos Global")) {
//            String es_empleado_geper = "True";
//            String imprime_rhcru = "True";
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                //System.out.println("si entra");
                rep_reporte.cerrar();
                parametro.put("ide_rhcrp", Integer.parseInt(tab_tabla1.getValor("ide_rhcrp")));
//                parametro.put("es_empleado_geper", Boolean.parseBoolean(es_empleado_geper));
//                parametro.put("imprime_rhcru", Boolean.parseBoolean(imprime_rhcru));
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Empleados por Departamento")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_rheem", Integer.parseInt(utilitario.getVariable("p_reh_estado_activo_empleado")));
                System.out.println("Si parametro..." + parametro + "----->" + Integer.parseInt(utilitario.getVariable("p_reh_estado_activo_empleado")));
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Provisiones Sociales")) {

            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                //System.out.println("si entra");
                rep_reporte.cerrar();
                parametro.put("ide_rhcrp", Integer.parseInt(tab_tabla1.getValor("ide_rhcrp")));
//                parametro.put("es_empleado_geper", Boolean.parseBoolean(es_empleado_geper));
//                parametro.put("imprime_rhcru", Boolean.parseBoolean(imprime_rhcru));
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null && !tab_tabla1.getValor("ide_cnccc").isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_formato");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene compraqbante de contabilidad");
                }


            }
        } else if (rep_reporte.getReporteSelecionado().equals("Provisiones Sociales Anual")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        }



    }

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
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

    public VistaAsiento getVia_comprobante_conta() {
        return via_comprobante_conta;
    }

    public void setVia_comprobante_conta(VistaAsiento via_comprobante_conta) {
        this.via_comprobante_conta = via_comprobante_conta;
    }

    public AutoCompletar getAut_periodo() {
        return aut_periodo;
    }

    public void setAut_periodo(AutoCompletar aut_periodo) {
        this.aut_periodo = aut_periodo;
    }
}

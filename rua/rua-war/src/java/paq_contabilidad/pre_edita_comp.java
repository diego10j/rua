/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import pkg_contabilidad.*;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.MarcaAgua;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import paq_presupuesto.ejb.ServicioPresupuesto;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_edita_comp extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Etiqueta eti_suma_debe = new Etiqueta();
    private Etiqueta eti_suma_haber = new Etiqueta();
    private Etiqueta eti_suma_diferencia = new Etiqueta();
    //Parametros del sistema
    private String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    String p_con_usa_presupuesto = utilitario.getVariable("p_con_usa_presupuesto");

    cls_contabilidad con = new cls_contabilidad();
    private Texto tex_num_transaccion = new Texto();
    private Boton bot_buscar_transaccion = new Boton();
    private String str_aux_fecha = null;
    private String str_tipo_comp = null;
    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);
    ///Presupuesto
    @EJB
    private ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);

    private Tabla tab_tabla3;
    private Dialogo dia_asociacion;
    private Tabla tab_sel_aso;
    private Etiqueta eti_cuenta_aso;
    private Etiqueta eti_valor_aso;

    private TablaGenerica tab_pres;

    public pre_edita_comp() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.quitarBotonsNavegacion();

            tex_num_transaccion.setId("tex_num_transaccion");
            tex_num_transaccion.setSoloEnteros();
            tex_num_transaccion.setSize(15);
            bot_buscar_transaccion.setTitle("Buscar");
            bot_buscar_transaccion.setIcon("ui-icon-search");
            bot_buscar_transaccion.setMetodo("buscarTransaccion");
            bar_botones.agregarComponente(new Etiqueta("NUM. ASIENTO: "));
            bar_botones.agregarComponente(tex_num_transaccion);
            bar_botones.agregarBoton(bot_buscar_transaccion);

            MarcaAgua maa_marca = new MarcaAgua();
            maa_marca.setValue("Num. Asiento");
            maa_marca.setFor("tex_num_transaccion");
            agregarComponente(maa_marca);

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("con_cab_comp_cont", "ide_cnccc", 1);
            tab_tabla1.setCondicionSucursal(true);
            tab_tabla1.getColumna("ide_cneco").setCombo("con_estado_compro", "ide_cneco", "nombre_cneco", "");
            tab_tabla1.getColumna("fecha_siste_cnccc").setVisible(false);
            tab_tabla1.getColumna("numero_cnccc").setEtiqueta();
            tab_tabla1.getColumna("numero_cnccc").setEstilo("font-size:11px;font-weight: bold");
            tab_tabla1.getColumna("fecha_siste_cnccc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_trans_cnccc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_sistem_cnccc").setVisible(false);
            tab_tabla1.getColumna("hora_sistem_cnccc").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("ide_cntcm").setVisible(false);
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("ide_modu").setCombo("sis_modulo", "ide_modu", "nom_modu", "");
            tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_geper").setRequerida(true);
            tab_tabla1.getColumna("OBSERVACION_CNCCC").setControl("Texto");
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.setCondicion("ide_cntcm=-1");
            tab_tabla1.setValidarInsertar(true);
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(6);
            tab_tabla1.getColumna("ide_cneco").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_normal"));
            tab_tabla1.getColumna("ide_cneco").setLectura(true);
            tab_tabla1.setCampoOrden("ide_cnccc desc");
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);
            pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("con_det_comp_cont", "ide_cndcc", 2);
            tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0));
            tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla2.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla2.getColumna("ide_cnlap").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
            tab_tabla2.getColumna("ide_cnlap").setPermitirNullCombo(false);
            tab_tabla2.getColumna("ide_cnlap").setMetodoChange("cambioLugarAplica");
            tab_tabla2.setCampoOrden("ide_cnlap desc");
            tab_tabla2.getColumna("valor_cndcc").setMetodoChange("ingresaCantidad");
            tab_tabla2.setRows(10);
            if (isPresupuesto()) {
                tab_tabla3 = new Tabla();
                tab_tabla2.agregarRelacion(tab_tabla3);
            }
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);
            PanelTabla pat_panel3 = new PanelTabla();
            if (isPresupuesto()) {

                Boton botPresupuesto = new Boton();
                botPresupuesto.setValue("Presupuesto CxP");
                botPresupuesto.setIcon("ui-icon-star");
                botPresupuesto.setMetodo("abrirPresupuesto");
                bar_botones.agregarSeparador();
                bar_botones.agregarBoton(botPresupuesto);

                dia_asociacion = new Dialogo();
                dia_asociacion.setId("dia_asociacion");
                dia_asociacion.setTitle("ASOCIACION PRESUPUESTARIA");
                dia_asociacion.setWidth("75%");
                dia_asociacion.setHeight("60%");
                agregarComponente(dia_asociacion);

                dia_asociacion.getBot_aceptar().setMetodo("aceptarAsociacion");
                dia_asociacion.getBot_cancelar().setMetodo("cancelarAsociacion");

                tab_sel_aso = new Tabla();
                tab_sel_aso.setId("tab_sel_aso");
                tab_sel_aso.setSql(ser_comprobante.getSqlAsociacionPresupuestariaCxP("-1", "-1", "-1"));
                tab_sel_aso.setCampoPrimaria("ide_prcof");
                for (int i = 0; i < tab_sel_aso.getTotalColumnas(); i++) {
                    tab_sel_aso.getColumnas()[i].setVisible(false);
                }
                tab_sel_aso.setRows(100);
                tab_sel_aso.setScrollable(true);
                tab_sel_aso.setScrollHeight(dia_asociacion.getAltoPanel() - 110);
                tab_sel_aso.getColumna("valor_devengar_prcof").setVisible(true);
                tab_sel_aso.getColumna("valor_devengar_prcof").setNombreVisual("VALOR DEVENGAR");

                tab_sel_aso.getColumna("VALOR").setVisible(true);
                tab_sel_aso.getColumna("VALOR").setLectura(true);
                tab_sel_aso.getColumna("detalle_prfup").setVisible(true);
                tab_sel_aso.getColumna("cod_programa_prpro").setVisible(true);
                tab_sel_aso.getColumna("detalle_prfup").setLectura(true);
                tab_sel_aso.getColumna("cod_programa_prpro").setLectura(true);
                tab_sel_aso.getColumna("SELECCIONADO").setNombreVisual("");
                tab_sel_aso.getColumna("SELECCIONADO").setVisible(true);
                tab_sel_aso.getColumna("SELECCIONADO").setAncho(1);
                tab_sel_aso.getColumna("SELECCIONADO").setLongitud(1);

                tab_sel_aso.dibujar();
                PanelTabla pat_panel4 = new PanelTabla();
                pat_panel4.setPanelTabla(tab_sel_aso);

                dia_asociacion.setDialogo(pat_panel4);

                Grid gri1 = new Grid();
                gri1.setColumns(2);
                gri1.getChildren().add(new Etiqueta("CUENTA:"));
                eti_cuenta_aso = new Etiqueta();
                gri1.getChildren().add(eti_cuenta_aso);
                gri1.getChildren().add(new Etiqueta("VALOR:"));
                eti_valor_aso = new Etiqueta();
                gri1.getChildren().add(eti_valor_aso);
                pat_panel4.setHeader(gri1);

                tab_tabla3.setId("tab_tabla3");
                tab_tabla3.setTabla("pre_mensual", "ide_prmen", 3);
                tab_tabla3.setHeader("PRESUPUESTO");
                tab_tabla3.setValorForanea("-1");
                tab_tabla3.setLectura(true);
                //AUMENTAR COMBOS !!!!!!
                tab_tabla3.getColumna("ide_pranu").setCombo(ser_presupuesto.sqlTablaPresupuestoAnual("2", "-1"));
                //tab_tabla3.getColumna("ide_pranu").setAutoCompletar();
                tab_tabla3.getColumna("ide_codem").setVisible(false);
                tab_tabla3.getColumna("ide_gemes").setVisible(false);
                tab_tabla3.getColumna("cobrado_prmen").setVisible(false);
                tab_tabla3.getColumna("cobradoc_prmen").setVisible(false);
                tab_tabla3.getColumna("pagado_prmen").setVisible(false);
                tab_tabla3.getColumna("comprometido_prmen").setVisible(false);
                tab_tabla3.getColumna("valor_anticipo_prmen").setVisible(false);
                tab_tabla3.getColumna("certificado_prmen").setVisible(false);
                tab_tabla3.getColumna("ide_prfuf").setVisible(false);
                tab_tabla3.getColumna("ide_prcer").setVisible(false);
                tab_tabla3.getColumna("ide_tecpo").setVisible(false);
                tab_tabla3.getColumna("ide_comov").setVisible(false);

                tab_tabla3.dibujar();

                tab_pres = new TablaGenerica();
                tab_pres.setTabla("pre_mensual", "ide_prmen", 3);
                tab_pres.setCondicion("ide_cndcc=-1");

                pat_panel3.setPanelTabla(tab_tabla3);
            }

            Grid gri_totales = new Grid();
            gri_totales.setId("gri_totales");

            gri_totales.setColumns(3);
            eti_suma_debe.setValue("TOTAL DEBE : 0");
            eti_suma_debe.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_haber.setValue("TOTAL HABER : 0");
            eti_suma_haber.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_diferencia.setValue("DIFERENCIA : 0");
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
            gri_totales.setWidth("100%");
            gri_totales.getChildren().add(eti_suma_diferencia);
            gri_totales.getChildren().add(eti_suma_debe);
            gri_totales.getChildren().add(eti_suma_haber);
            Division div_division = new Division();
            div_division.setId("div_division");
            Division div_detalle = new Division();
            div_detalle.setFooter(pat_panel2, gri_totales, "85%");
            if (isPresupuesto()) {
                div_division.dividir3(pat_panel1, div_detalle, pat_panel3, "30%", "25%", "H");
            } else {
                div_division.dividir2(pat_panel1, div_detalle, "40%", "H");
            }

            gru_pantalla.getChildren().add(bar_botones);
            gru_pantalla.getChildren().add(div_division);
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    private int intRecorre = 0;

    public void abrirPresupuesto() {
        if (tab_tabla1.isEmpty() == false) {
            tab_pres.limpiar();
            intRecorre = 0;
            buscaPresupuestoCxP();
        } else {
            utilitario.agregarMensajeInfo("Primero debe buscar un asiento contable", "");
        }

    }

    private void buscaPresupuestoCxP() {
        tab_tabla2.setFilaActual(intRecorre);
        //Busca las facturas cxp que tengan el asiento seleccionado
        TablaGenerica tab_cxp = utilitario.consultar("Select ide_cpcfa,ide_cnccc from cxp_cabece_factur where ide_cnccc=" + tab_tabla1.getValor("ide_cnccc") + " GROUP BY ide_cpcfa,ide_cnccc ");
        String relacion = tab_cxp.getStringColumna("ide_cpcfa");
        //System.out.println("Se encontraron facturs CXP ++++++ " + relacion);
        if (relacion == null || relacion.isEmpty()) {
            //NO HAY FACTURAS CXP ASOCIADAS AL ASIENTO
            relacion = "-1";
        }
        //System.out.println("-  " + intRecorre + " --- " + tab_tabla2.getValorArreglo("ide_cndpc", 2));
        tab_sel_aso.setSql(ser_comprobante.getSqlAsociacionPresupuestariaCxP(tab_tabla2.getValor("ide_cndpc"), tab_tabla2.getValor("ide_cnlap"), relacion));
        tab_sel_aso.ejecutarSql();

        if (!tab_sel_aso.isEmpty()) {
            dia_asociacion.setTitle("ASOCIACION PRESUPUESTARIA");
            eti_cuenta_aso.setValue("<span style='font-size:12px;font-weight: bold;'>" + tab_tabla2.getValorArreglo("ide_cndpc", 1) + "   " + tab_tabla2.getValorArreglo("ide_cndpc", 2) + "</span>");
            eti_valor_aso.setValue(("<span style='font-size:13px;font-weight: bold;'>" + utilitario.getFormatoNumero(tab_tabla2.getValor("valor_cndcc")) + "</span>"));
            dia_asociacion.dibujar();
        } else {
            intRecorre++;
            aceptarAsociacion();
        }
    }

    public void aceptarAsociacion() {
        //valida que los seleccionado sea igual al valor del asiento
        if (dia_asociacion.isVisible() == true) {
            double dou_sum_valor_debengado = 0;
            for (int i = 0; i < tab_sel_aso.getTotalFilas(); i++) {
                if (tab_sel_aso.getValor(i, "SELECCIONADO").equals("true")) {
                    double valor_actual = 0;
                    try {
                        valor_actual = Double.parseDouble(tab_sel_aso.getValor(i, "valor_devengar_prcof"));
                    } catch (Exception e) {
                    }
                    dou_sum_valor_debengado += valor_actual;
                }
            }
            double dou_valor_detalle = Double.parseDouble(tab_tabla2.getValor("valor_cndcc"));
            if (dou_valor_detalle == dou_sum_valor_debengado) {
                //agrega                            
                for (int i = 0; i < tab_sel_aso.getTotalFilas(); i++) {
                    if (tab_sel_aso.getValor(i, "SELECCIONADO").equals("true")) {
                        tab_pres.insertar();
                        tab_pres.setValor("ide_pranu", tab_sel_aso.getValor(i, "ide_pranu"));
                        tab_pres.setValor("ide_prtra", tab_sel_aso.getValor(i, "ide_prtra"));
                        tab_pres.setValor("fecha_ejecucion_prmen", tab_tabla1.getValor(i, "fecha_trans_cnccc"));
                        tab_pres.setValor("ide_codem", tab_tabla2.getValor("ide_cndcc"));
                        tab_pres.setValor("comprobante_prmen", tab_tabla1.getValor("numero_cnccc"));
                        tab_pres.setValor("devengado_prmen", tab_sel_aso.getValor(i, "valor_devengar_prcof"));
                        tab_pres.setValor("cobrado_prmen", "0");
                        tab_pres.setValor("cobradoc_prmen", "0");
                        tab_pres.setValor("pagado_prmen", "0");
                        tab_pres.setValor("comprometido_prmen", "0");
                        tab_pres.setValor("valor_anticipo_prmen", "0");
                        tab_pres.setValor("certificado_prmen", "0");
                        tab_pres.setValor("ide_comov", tab_tabla1.getValor("ide_cnccc"));
                        tab_pres.setValor("activo_prmen", "true");
                        tab_pres.setValor("ide_cndcc", tab_tabla2.getValor("ide_cndcc"));
                    }
                }
                tab_pres.guardar();
                utilitario.getConexion().setImprimirSqlConsola(true);
                utilitario.getConexion().ejecutarListaSql();
                utilitario.getConexion().setImprimirSqlConsola(false);
                intRecorre++;
                dia_asociacion.cerrar();
                if (isPresupuesto()) {
                    tab_tabla3.actualizar();
                }

            } else {
                utilitario.agregarMensajeError("La suma del valor devengado debe ser igual a " + dou_valor_detalle, "");
                return;
            }
        }
        if (intRecorre < tab_tabla2.getTotalFilas()) {
            buscaPresupuestoCxP();
        }

////        if (tex_valor_pre.getValue() == null || String.valueOf(tex_valor_pre.getValue()).isEmpty()) {
////            utilitario.agregarMensajeError("Debe ingresar un valor", "");
////        }
////        if (tab_sel_aso.getSeleccionados() != null) {
////            dia_asociacion.cerrar();
////            tab_tabla2.setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pre.getValue()));
////            tab_tabla2.getFilaSeleccionada().setLectura(true);
////            utilitario.addUpdate("tab_tabla2");
////        } else {
////            utilitario.agregarMensajeError("Debe seleccionar una asociación presupuestaria", "");
////        }   
    }

    public void cancelarAsociacion() {
        dia_asociacion.cerrar();
    }

    public void buscarTransaccion() {
        str_aux_fecha = null;
        str_tipo_comp = null;
        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {
            tab_tabla1.setCondicion("ide_cnccc=" + tex_num_transaccion.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            if (isPresupuesto()) {
                tab_tabla3.ejecutarValorForanea(tab_tabla2.getValorSeleccionado());
            }
            str_aux_fecha = tab_tabla1.getValor("fecha_trans_cnccc");
            str_tipo_comp = tab_tabla1.getValor("ide_cntcm");
            if (tab_tabla1.getTotalFilas() > 0) {
                calcularTotal();
            } else {
                utilitario.agregarMensajeInfo("Atencion", "El numero de Asiento no existe");
            }
            utilitario.addUpdate("tab_tabla1,tab_tabla2,gri_totales");
        }
    }

    @Override
    public void insertar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        }
    }

    @Override
    public void guardar() {
        if (validar()) {
            TablaGenerica tab_persona = utilitario.consultar("select * from gen_persona where ide_geper=" + tab_tabla1.getValor("ide_geper"));
            utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set beneficiari_teclb='" + tab_persona.getValor("nom_geper") + "' where ide_cnccc=" + tab_tabla1.getValor("ide_cnccc"));
            if (str_aux_fecha != null) {
                //valida si cambio mes del asiento
                String str_fecha = tab_tabla1.getValor("fecha_trans_cnccc");
                //valida mismo mes
                if (utilitario.getMes(str_fecha) != utilitario.getMes(str_aux_fecha)) {
                    tab_tabla1.setValor("numero_cnccc", ser_comprobante.getSecuencial(tab_tabla1.getValor("fecha_trans_cnccc"), tab_tabla1.getValor("ide_cntcm")));
                    tab_tabla1.modificar(tab_tabla1.getFilaActual());
                }
            }
            //valida si cambia tipo de comprobante
            if (str_tipo_comp != null) {
                String str_tipo = tab_tabla1.getValor("ide_cntcm");
                if (!str_tipo.equals(str_tipo_comp)) {
                    tab_tabla1.setValor("numero_cnccc", ser_comprobante.getSecuencial(tab_tabla1.getValor("fecha_trans_cnccc"), tab_tabla1.getValor("ide_cntcm")));
                    tab_tabla1.modificar(tab_tabla1.getFilaActual());
                }
            }
            tab_tabla1.guardar();
            tab_tabla2.guardar();
            utilitario.getConexion().guardarPantalla();
        }

    }

    public void cambioLugarAplica(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public boolean validar() {
        if (calcularTotal()) {
            return true;
        } else {
            utilitario.agregarMensajeInfo("La suma de los detalles del DEBE tiene que ser igual al del HABER", "");
        }
        return false;
    }

    public boolean calcularTotal() {
        double dou_debe = 0;
        double dou_haber = 0;
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {

            try {
                if (tab_tabla2.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                    dou_debe += Double.parseDouble(tab_tabla2.getValor(i, "valor_cndcc"));
                } else if (tab_tabla2.getValor(i, "ide_cnlap").equals(p_con_lugar_haber)) {
                    dou_haber += Double.parseDouble(tab_tabla2.getValor(i, "valor_cndcc"));
                }
            } catch (Exception e) {
            }
        }
        eti_suma_debe.setValue("TOTAL DEBE : " + utilitario.getFormatoNumero(dou_debe));
        eti_suma_haber.setValue("TOTAL HABER : " + utilitario.getFormatoNumero(dou_haber));

        double dou_diferencia = Double.parseDouble(utilitario.getFormatoNumero(dou_debe)) - Double.parseDouble(utilitario.getFormatoNumero(dou_haber));
        eti_suma_diferencia.setValue("DIFERENCIA : " + utilitario.getFormatoNumero(dou_diferencia));
        if (dou_diferencia != 0.0) {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold;color:red");
        } else {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
            return true;
        }
        return false;
    }

    public void ingresaCantidad(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    @Override
    public void eliminar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        }
    }

    public final boolean isPresupuesto() {

        if (p_con_usa_presupuesto == null) {
            return false;
        } else if (p_con_usa_presupuesto.equals("false")) {
            return false;
        } else if (p_con_usa_presupuesto.equals("true")) {
            return true;
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

    public Dialogo getDia_asociacion() {
        return dia_asociacion;
    }

    public void setDia_asociacion(Dialogo dia_asociacion) {
        this.dia_asociacion = dia_asociacion;
    }

    public Tabla getTab_sel_aso() {
        return tab_sel_aso;
    }

    public void setTab_sel_aso(Tabla tab_sel_aso) {
        this.tab_sel_aso = tab_sel_aso;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

}

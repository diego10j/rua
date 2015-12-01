/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.AjaxBehaviorEvent;
import sistema.aplicacion.Utilitario;


/**
 *
 * @author Diego
 */
public class VistaAsiento extends Dialogo {

    private Tabla tab_cab_comp_cont_vasiento = new Tabla(); // cabecera comprobante contabilidad
    private Tabla tab_det_comp_cont_vasiento = new Tabla(); // detalle comprobante contabilidad
    private String ruta = "pre_index.clase";
    private Grid gri_cuerpo_vasiento = new Grid();
    private Utilitario utilitario = new Utilitario();
    private cls_cab_comp_cont cab_com_con;
    cls_contabilidad conta=new cls_contabilidad();
    private boolean aux_tabla = false;
    private Grid gri_totales_vasiento = new Grid();
    private Etiqueta eti_suma_debe_vasiento = new Etiqueta();
    private Etiqueta eti_suma_haber_vasiento = new Etiqueta();
    private Etiqueta eti_suma_diferencia_vasiento = new Etiqueta();

    public VistaAsiento() {
        this.setTitle("COMPROBANTE DE CONTABILIDAD");
        this.setWidth("70%");
        this.setHeight("85%");

        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        gri_totales_vasiento.setId("gri_totales_vasiento");
        gri_totales_vasiento.setColumns(3);
        eti_suma_debe_vasiento.setValue("TOTAL DEBE : 0");
        eti_suma_debe_vasiento.setStyle("font-size: 14px;font-weight: bold");
        eti_suma_haber_vasiento.setValue("TOTAL HABER : 0");
        eti_suma_haber_vasiento.setStyle("font-size: 14px;font-weight: bold");
        eti_suma_diferencia_vasiento.setValue("DIFERENCIA : 0");
        eti_suma_diferencia_vasiento.setStyle("font-size: 14px;font-weight: bold");
        gri_totales_vasiento.getChildren().add(eti_suma_debe_vasiento);
        gri_totales_vasiento.getChildren().add(eti_suma_haber_vasiento);
        gri_totales_vasiento.getChildren().add(eti_suma_diferencia_vasiento);
        tab_cab_comp_cont_vasiento.setId("tab_cab_comp_cont_vasiento");
        tab_cab_comp_cont_vasiento.setTabla("con_cab_comp_cont", "ide_cnccc", -1);
        tab_cab_comp_cont_vasiento.getColumna("ide_cneco").setCombo("con_estado_compro", "ide_cneco", "nombre_cneco", "");
        tab_cab_comp_cont_vasiento.getColumna("ide_cntcm").setCombo("con_tipo_comproba", "ide_cntcm", "nombre_cntcm", "");
        tab_cab_comp_cont_vasiento.getColumna("fecha_siste_cnccc").setVisible(false);
        tab_cab_comp_cont_vasiento.getColumna("numero_cnccc").setEtiqueta();
        tab_cab_comp_cont_vasiento.getColumna("numero_cnccc").setEstilo("font-size:11px;font-weight: bold");
        tab_cab_comp_cont_vasiento.getColumna("fecha_siste_cnccc").setValorDefecto(utilitario.getFechaActual());
        tab_cab_comp_cont_vasiento.getColumna("fecha_trans_cnccc").setValorDefecto(utilitario.getFechaActual());
        tab_cab_comp_cont_vasiento.getColumna("hora_sistem_cnccc").setVisible(false);
        tab_cab_comp_cont_vasiento.getColumna("hora_sistem_cnccc").setValorDefecto(utilitario.getHoraActual());
        tab_cab_comp_cont_vasiento.getColumna("ide_cntcm").setVisible(false);
        tab_cab_comp_cont_vasiento.getColumna("ide_cntcm").setPermitirNullCombo(false);
        tab_cab_comp_cont_vasiento.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
        tab_cab_comp_cont_vasiento.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_comp_cont_vasiento.getColumna("ide_usua").setLectura(true);
        tab_cab_comp_cont_vasiento.getColumna("ide_modu").setVisible(false);
        tab_cab_comp_cont_vasiento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_cab_comp_cont_vasiento.getColumna("ide_geper").setAutoCompletar();
        tab_cab_comp_cont_vasiento.getColumna("ide_geper").setLectura(true);
        tab_cab_comp_cont_vasiento.getColumna("ide_cneco").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_normal"));
        tab_cab_comp_cont_vasiento.getColumna("ide_cneco").setLectura(true);
        tab_cab_comp_cont_vasiento.getColumna("ide_cntcm").setValorDefecto(utilitario.getVariable("p_con_tipo_comprobante_ingreso"));
        tab_cab_comp_cont_vasiento.getColumna("numero_cnccc").setVisible(false);
        tab_cab_comp_cont_vasiento.setCondicion("ide_cnccc=-1");
        tab_cab_comp_cont_vasiento.setCampoOrden("ide_cnccc desc");
        tab_cab_comp_cont_vasiento.setTipoFormulario(true);
        tab_cab_comp_cont_vasiento.getGrid().setColumns(4);
        tab_cab_comp_cont_vasiento.setMostrarNumeroRegistros(false);

        utilitario.buscarNombresVisuales(tab_cab_comp_cont_vasiento);

        tab_det_comp_cont_vasiento.setId("tab_det_comp_cont_vasiento");
        tab_det_comp_cont_vasiento.setTabla("con_det_comp_cont", "ide_cndcc", -1);
        tab_det_comp_cont_vasiento.setCondicion("ide_cndcc=-1");
        tab_det_comp_cont_vasiento.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0));
        tab_det_comp_cont_vasiento.getColumna("ide_cndpc").setAutoCompletar();
        tab_det_comp_cont_vasiento.getColumna("ide_cnlap").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
        tab_det_comp_cont_vasiento.getColumna("ide_cnlap").setPermitirNullCombo(false);
        tab_det_comp_cont_vasiento.getColumna("ide_cnlap").setRequerida(true);
        tab_det_comp_cont_vasiento.getColumna("ide_cnccc").setVisible(false);
        tab_det_comp_cont_vasiento.getColumna("ide_cndcc").setVisible(false);
        tab_det_comp_cont_vasiento.getColumna("ide_cndpc").setRequerida(true);
        tab_det_comp_cont_vasiento.getColumna("valor_cndcc").setRequerida(true);
        tab_det_comp_cont_vasiento.setCampoOrden("ide_cnlap desc");
        utilitario.buscarNombresVisuales(tab_det_comp_cont_vasiento);

        this.setDialogo(gri_cuerpo_vasiento);
        this.setDynamic(false);

    }

    public void insertar() {
        tab_det_comp_cont_vasiento.insertar();
        calculaTotales();
    }

    public void eliminar() {
        tab_det_comp_cont_vasiento.eliminar();
        calculaTotales();
    }

    public void ingresaCantidad(AjaxBehaviorEvent evt) {
        tab_det_comp_cont_vasiento.modificar(evt);
        calculaTotales();
        utilitario.addUpdate("gri_totales_vasiento");
    }

    public void cambioLugarAplica(AjaxBehaviorEvent evt) {
        tab_det_comp_cont_vasiento.modificar(evt);
        calculaTotales();
        utilitario.addUpdate("gri_totales_vasiento");
    }

    public boolean calculaTotales() {
        double tot_debe = 0;
        double tot_haber = 0;
        for (int i = 0; i < tab_det_comp_cont_vasiento.getTotalFilas(); i++) {

            if (tab_det_comp_cont_vasiento.getValor(i, "valor_cndcc") != null && !tab_det_comp_cont_vasiento.getValor(i, "valor_cndcc").isEmpty()) {
                if (tab_det_comp_cont_vasiento.getValor(i, "ide_cnlap") != null && !tab_det_comp_cont_vasiento.getValor(i, "ide_cnlap").isEmpty()) {
                    if (tab_det_comp_cont_vasiento.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                        tot_debe = tot_debe + Double.parseDouble(tab_det_comp_cont_vasiento.getValor(i, "valor_cndcc"));
                    }
                    if (tab_det_comp_cont_vasiento.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_haber"))) {
                        tot_haber = tot_haber + Double.parseDouble(tab_det_comp_cont_vasiento.getValor(i, "valor_cndcc"));
                    }
                }
            }

        }
        eti_suma_debe_vasiento.setValue("Total Debe: " + utilitario.getFormatoNumero(tot_debe, 2));
        eti_suma_haber_vasiento.setValue("Total Haber: " + utilitario.getFormatoNumero(tot_haber, 2));
        double dou_diferencia = Double.parseDouble(utilitario.getFormatoNumero(tot_debe)) - Double.parseDouble(utilitario.getFormatoNumero(tot_haber));
        eti_suma_diferencia_vasiento.setValue("DIFERENCIA : " + utilitario.getFormatoNumero(dou_diferencia));
        if (dou_diferencia != 0.0) {
            eti_suma_diferencia_vasiento.setStyle("font-size: 14px;font-weight: bold;color:red");
        } else {
            eti_suma_diferencia_vasiento.setStyle("font-size: 14px;font-weight: bold");
        }
        if (dou_diferencia == 0) {
            return true;
        } else {
            return false;
        }

    }

    public List resumirComprobante(List<cls_det_comp_cont> detalles) {
        //Unifica las cuentas
        List<cls_det_comp_cont> resumen = new ArrayList();
        if (!detalles.isEmpty()) {
            List l_cuenta = new ArrayList();
            List l_observacion = new ArrayList();
            List l_lug_apli = new ArrayList();
            List l_suma = new ArrayList();
            List l_valor = new ArrayList();
            int band = 0;
            String cuen;
            String ide_cnlap;
            String observacion;
            double suma = 0;
            double valor = 0;
            for (int i = 0; i < detalles.size(); i++) {
                try {
                    cuen = detalles.get(i).getIde_cndpc() + "";
                    ide_cnlap = detalles.get(i).getIde_cnlap() + "";
                    observacion = detalles.get(i).getObservacion_cndcc() + "";
                    valor = detalles.get(i).getValor_cndcc();
                    for (int k = 0; k < l_cuenta.size(); k++) {
                        try {
                            if (detalles.get(i).getIde_cndpc() != null && !detalles.get(i).getIde_cndpc().isEmpty()) {
                                if (cuen.equals(l_cuenta.get(k))
                                        && ide_cnlap.equals(l_lug_apli.get(k))
                                        && observacion.equals(l_observacion.get(k))) {
                                    band = 1;
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (band == 0) {
                        l_cuenta.add(cuen);
                        l_observacion.add(observacion);
                        l_lug_apli.add(ide_cnlap);
                        l_valor.add(valor);
                    }
                    band = 0;
                } catch (Exception e) {
                }
            }
//            for (int j = 0; j < detalles.size(); j++) {
//                System.out.println("lsta detalles ide_cuenta " + detalles.get(j).getIde_cndpc()+"");
//            }
//            for (int i = 0; i < l_cuenta.size(); i++) {
//                System.out.println("ide_cuenta " + l_cuenta.get(i) + "" + " valor " + l_valor.get(i));
//            }
            for (int i = 0; i < l_cuenta.size(); i++) {
                try {
                    cuen = l_cuenta.get(i) + "";
                    ide_cnlap = l_lug_apli.get(i) + "";
                    observacion = l_observacion.get(i) + "";
                    for (int j = 0; j < detalles.size(); j++) {
                        try {
                            if (cuen != null && !cuen.isEmpty()) {
                                if (cuen.equals(detalles.get(j).getIde_cndpc().toString())
                                        && ide_cnlap.equals(detalles.get(j).getIde_cnlap().toString())
                                        && observacion.equals(detalles.get(j).getObservacion_cndcc().toString())) {
                                    suma = detalles.get(j).getValor_cndcc() + suma;
                                }
                            } else {
                                suma=Double.parseDouble(l_valor.get(i)+"");
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                    l_suma.add(suma);
                    suma = 0;
                } catch (Exception e) {
                }
            }
            for (int i = 0; i < l_cuenta.size(); i++) {
                try {
                    resumen.add(new cls_det_comp_cont(l_lug_apli.get(i) + "", l_cuenta.get(i) + "", Double.parseDouble(l_suma.get(i) + ""), l_observacion.get(i) + ""));
                } catch (Exception e) {
                }
            }
        }
        return resumen;

    }

    public void setVistaAsiento(cls_cab_comp_cont cabecera) {
        cabecera.setDetalles(resumirComprobante(cabecera.getDetalles()));
        this.cab_com_con = cabecera;
        if (aux_tabla == false) {
            tab_cab_comp_cont_vasiento.setRuta(ruta + "." + this.getId());
            tab_det_comp_cont_vasiento.setRuta(ruta + "." + this.getId());
            tab_det_comp_cont_vasiento.getColumna("ide_cnlap").setMetodoChangeRuta(ruta + "." + this.getId() + ".cambioLugarAplica");
            tab_det_comp_cont_vasiento.getColumna("valor_cndcc").setMetodoChangeRuta(ruta + "." + this.getId() + ".ingresaCantidad");
            tab_cab_comp_cont_vasiento.dibujar();
            tab_det_comp_cont_vasiento.dibujar();
            PanelTabla pat_panel8 = new PanelTabla();
            pat_panel8.setPanelTabla(tab_det_comp_cont_vasiento);
            pat_panel8.getMenuTabla().quitarItemGuardar();
            pat_panel8.getMenuTabla().quitarSubmenuOtros();
            gri_cuerpo_vasiento.getChildren().add(tab_cab_comp_cont_vasiento);
            gri_cuerpo_vasiento.getChildren().add(gri_totales_vasiento);
            gri_cuerpo_vasiento.getChildren().add(pat_panel8);
            aux_tabla = true;
        } else {
            tab_cab_comp_cont_vasiento.limpiar();
            tab_det_comp_cont_vasiento.limpiar();
        }

        tab_cab_comp_cont_vasiento.insertar();
        tab_cab_comp_cont_vasiento.setValor("fecha_trans_cnccc", cab_com_con.getFecha_trans_cnccc());
        tab_cab_comp_cont_vasiento.setValor("numero_cnccc", "");
        tab_cab_comp_cont_vasiento.setValor("observacion_cnccc", cab_com_con.getObservacion_cnccc());
        tab_cab_comp_cont_vasiento.setValor("ide_geper", cab_com_con.getIde_geper());
        for (int i = 0; i < cabecera.getDetalles().size(); i++) {
            tab_det_comp_cont_vasiento.insertar();
            tab_det_comp_cont_vasiento.setValor("ide_cnlap", cabecera.getDetalles().get(i).getIde_cnlap());
            tab_det_comp_cont_vasiento.setValor("ide_cndpc", cabecera.getDetalles().get(i).getIde_cndpc());
            tab_det_comp_cont_vasiento.setValor("valor_cndcc", utilitario.getFormatoNumero(cabecera.getDetalles().get(i).getValor_cndcc()));
            tab_det_comp_cont_vasiento.setValor("observacion_cndcc", cabecera.getDetalles().get(i).getObservacion_cndcc());
        }
        calculaTotales();
    }

    public boolean validarComprobante() {


        if (tab_cab_comp_cont_vasiento.getValor("observacion_cnccc") == null || tab_cab_comp_cont_vasiento.getValor("observacion_cnccc").isEmpty()) {
            utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Debe ingresar una observacion");
            return false;
        }
        if (tab_cab_comp_cont_vasiento.getValor("fecha_trans_cnccc") == null || tab_cab_comp_cont_vasiento.getValor("fecha_trans_cnccc").isEmpty()) {
            utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Debe ingresar una fecha de transaccion");
            return false;
        }
        if (tab_det_comp_cont_vasiento.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("Error al Guardar el Comprobante", "El asiento no cuadra");
            return false;
        }

        if (calculaTotales() == false) {
            utilitario.agregarMensajeError("Error al Guardar el Comprobante", "El asiento no cuadra");
            return false;
        }
        
        for (int i = 0; i < tab_det_comp_cont_vasiento.getTotalFilas(); i++) {
            
            if (!conta.esCuentaContableHija(tab_det_comp_cont_vasiento.getValor(i, "ide_cndpc"))) {
                utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Una de las filas no tiene nivel HIJO");
                return false;
            }
            if (tab_det_comp_cont_vasiento.getValor(i, "ide_cndpc") == null || tab_det_comp_cont_vasiento.getValor(i, "ide_cndpc").isEmpty()) {
                utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Una de las filas no tiene seleccionada una cuenta contable");
                return false;
            }
            if (tab_det_comp_cont_vasiento.getValor(i, "ide_cnlap") == null || tab_det_comp_cont_vasiento.getValor(i, "ide_cnlap").isEmpty()) {
                utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Una de las filas no tiene seleccionado Lugar aplica (Debe o Haber)");
                return false;
            }
            if (tab_det_comp_cont_vasiento.getValor(i, "valor_cndcc") == null || tab_det_comp_cont_vasiento.getValor(i, "valor_cndcc").isEmpty()) {
                utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Una de las filas no tiene valor ");
                return false;
            }

        }
        for (int i = 0; i < tab_det_comp_cont_vasiento.getTotalFilas(); i++) {
            if (Double.parseDouble(tab_det_comp_cont_vasiento.getValor(i, "valor_cndcc")) <= 0.0) {
                utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Una de las filas tiene valor de cero o negativo ");
                return false;
            }
        }


        return true;
    }

    @Override
    public void dibujar() {
        gri_totales_vasiento.setWidth((getAnchoPanel() - 15) + "");
        gri_cuerpo_vasiento.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        super.dibujar();
    }

    public Tabla getTab_cab_comp_cont_vasiento() {
        return tab_cab_comp_cont_vasiento;
    }

    public void setTab_cab_comp_cont_vasiento(Tabla tab_cab_comp_cont_vasiento) {
        this.tab_cab_comp_cont_vasiento = tab_cab_comp_cont_vasiento;
    }

    public Tabla getTab_det_comp_cont_vasiento() {
        return tab_det_comp_cont_vasiento;
    }

    public void setTab_det_comp_cont_vasiento(Tabla tab_det_comp_cont_vasiento) {
        this.tab_det_comp_cont_vasiento = tab_det_comp_cont_vasiento;
    }

    public String getRuta() {
        return ruta;
    }

    public Grid getGri_cuerpo_vasiento() {
        return gri_cuerpo_vasiento;
    }

    public void setGri_cuerpo_vasiento(Grid gri_cuerpo_vasiento) {
        this.gri_cuerpo_vasiento = gri_cuerpo_vasiento;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}

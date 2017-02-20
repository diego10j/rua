/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inversiones;

import componentes.AsientoContable;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import servicios.inversiones.ServicioInversiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_inversiones extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private Radio rad_hace_asiento;
    private Tabla tab_tabla1;
    private Tabla tab_tabla2;
    @EJB
    private final ServicioInversiones ser_inversion = (ServicioInversiones) utilitario.instanciarEJB(ServicioInversiones.class);
    private AsientoContable asc_asiento = new AsientoContable();
    private AutoCompletar aut_inversion;
    private Grid gri = new Grid();

    public pre_inversiones() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonInsertar();
        mep_menu.setMenuPanel("INVERSIONES BANCARIAS", "20%");
        mep_menu.agregarItem("Listado de Inversiones Bancarias", "dibujarListadoB", "ui-icon-note");//2
        mep_menu.agregarItem("Nuevo Certificado", "dibujarCertificadoB", "ui-icon-contact"); //1
        mep_menu.agregarItem("Pago de Interes", "dibujarPagoB", "ui-icon-contact"); //4
        mep_menu.agregarSubMenu("INVERSIONES SALESIANAS");
        mep_menu.agregarSubMenu("INVERSIONES FONDO DE DESVINCULACIÓN");
        agregarComponente(mep_menu);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_cancelar().setMetodo("cerrarAsiento");
        agregarComponente(asc_asiento);
    }

    public void cerrarAsiento() {
        //limpia sql guardados
        utilitario.getConexion().getSqlPantalla().clear();
        asc_asiento.cerrar();
    }

    public void dibujarPagoB() {
        Grupo gru = new Grupo();
        aut_inversion = new AutoCompletar();
        aut_inversion.setId("aut_inversion");
        aut_inversion.setAutoCompletar(ser_inversion.getSqlListaInversionesBancarias());
        aut_inversion.setMaxResults(25);
        aut_inversion.setMetodoChange("cargarPagosInteres");
        Grid gr = new Grid();
        gr.setColumns(2);
        gr.getChildren().add(new Etiqueta("<strong>INVERSIÓN BANCARIA : </strong>"));
        gr.getChildren().add(aut_inversion);
        gru.getChildren().add(gr);

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setHeader("NUEVO PAGO DE INTERESES");
        tab_tabla2.setTabla("iyp_pago_interes", "ide_ippin", 4);
        tab_tabla2.setCondicion("ide_ippin=-1");
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.setValidarInsertar(true);
        tab_tabla2.getColumna("ide_ippin").setVisible(false);
        tab_tabla2.getColumna("ide_ipcer").setVisible(false);
        tab_tabla2.getColumna("fecha_sistema_ippin").setVisible(false);
        tab_tabla2.getColumna("valor_ippin").alinearDerecha();
        tab_tabla2.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setMostrarNumeroRegistros(false);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().setRendered(false);
        tab_tabla2.insertar();

        gri = new Grid();
        gri.setId("gri");
        // gri.setRendered(false); //solo cuando se inseta se hace visible
        gri.setColumns(2);
        gri.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        gri.getChildren().add(rad_hace_asiento);

        gru.getChildren().add(pat_panel2);
        Boton bt = new Boton();
        bt.setMetodo("guardar");
        bt.setValue("Aceptar");
        gru.getChildren().add(gri);
        gru.getChildren().add(bt);
        gru.getChildren().add(new Separator());

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("DETALLE DE PAGOS INTERESES");
        tab_tabla1.setTabla("iyp_pago_interes", "ide_ippin", 4);
        tab_tabla1.setCondicion("ide_ippin=-1");
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("valor_ippin");
        tab_tabla1.setValidarInsertar(true);
        tab_tabla1.getColumna("ide_ippin").setVisible(false);
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("fecha_sistema_ippin").setVisible(false);
        tab_tabla1.getColumna("valor_ippin").alinearDerecha();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.setRows(5);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(true);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(4, "PAGO DE INTERESES INVERSIONES BANCARIAS", gru);

    }

    public void dibujarListadoB() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesBancarias());
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.setColumnaSuma("CAPITAL,INTERES,CAPITAL_MAS_INTERES");
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.setRows(200);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Modificar");
        itemedita.setIcon("ui-icon-pencil");
        itemedita.setMetodo("abrirModificarB");
        pat_panel.getMenuTabla().getChildren().add(itemedita);

        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(2, "LISTADO DE INVERSIONES BANCARIAS", pat_panel);

    }

    public void cargarPagosInteres(SelectEvent evt) {
        aut_inversion.onSelect(evt);
        if (aut_inversion.getValor() != null) {
            tab_tabla1.setCondicion("ide_ipcer=" + aut_inversion.getValor());
            tab_tabla1.ejecutarSql();
            //gri.setRendered(false);
            utilitario.addUpdate("gri");
        } else {
            tab_tabla1.limpiar();
            utilitario.agregarMensajeInfo("Seleccione una inversión Bancaria", "");
        }
    }

    public void dibujarCertificadoB() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 1);
        tab_tabla1.setCondicion("ide_ipcer=-1");
        //oculta todas las columnas        
        for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
            tab_tabla1.getColumnas()[i].setVisible(false);
        }
        tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
        tab_tabla1.getColumna("ide_tecba").setVisible(true);
        tab_tabla1.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and es_caja_teban=false and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
        tab_tabla1.getColumna("ide_tecba").setRequerida(true);
        tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
        tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
        tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
        tab_tabla1.getColumna("capital_ipcer").setVisible(true);
        tab_tabla1.getColumna("interes_ipcer").setVisible(true);
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
        tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
        tab_tabla1.getColumna("ide_cnmod").setVisible(true);
        tab_tabla1.getColumna("ide_cnccc").setVisible(true);
        tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
        tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
        tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
        tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
        tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
        tab_tabla1.getColumna("ide_iptin").setValorDefecto("0"); //BANCOS
        tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("true"); //BANCOS
        tab_tabla1.getColumna("ide_geper").setValorDefecto("1294"); //SOCIEDAD SALESIANA EN EL ECUADOR
        tab_tabla1.getColumna("ide_ipein").setValorDefecto("0"); //NO CANCELADO
        tab_tabla1.getColumna("ide_ipcin").setValorDefecto("1");
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("nuevo").setValorDefecto("true");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
        }
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_importar().setRendered(false);

        Grid gri1 = new Grid();
        gri1.setColumns(2);
        gri1.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        gri1.getChildren().add(rad_hace_asiento);
        pat_panel.getChildren().add(gri1);

        mep_menu.dibujar(1, "CERTIFICADO DE INVERSIÓN BANCARIA", pat_panel);
    }

    public void abrirModificarB() {
        String ide_aux = tab_tabla1.getValor("ide_ipcer");
        if (ide_aux != null) {
            tab_tabla1 = new Tabla();
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 1);
            tab_tabla1.setCondicion("ide_ipcer=" + ide_aux);
            //oculta todas las columnas        
            for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
                tab_tabla1.getColumnas()[i].setVisible(false);
            }
            tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
            tab_tabla1.getColumna("ide_tecba").setVisible(true);
            tab_tabla1.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and es_caja_teban=false and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            tab_tabla1.getColumna("ide_tecba").setRequerida(true);
            tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
            tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
            tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
            tab_tabla1.getColumna("capital_ipcer").setVisible(true);
            tab_tabla1.getColumna("interes_ipcer").setVisible(true);
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
            tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
            tab_tabla1.getColumna("ide_cnmod").setVisible(true);
            tab_tabla1.getColumna("ide_cnccc").setVisible(true);
            tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
            tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
            tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
            tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
            tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
            tab_tabla1.getColumna("ide_iptin").setValorDefecto("0"); //BANCOS
            tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("true"); //BANCOS
            tab_tabla1.getColumna("ide_geper").setValorDefecto("1294"); //SOCIEDAD SALESIANA EN EL ECUADOR
            tab_tabla1.getColumna("ide_ipein").setValorDefecto("0"); //NO CANCELADO
            tab_tabla1.getColumna("ide_ipcin").setValorDefecto("1");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("nuevo").setValorDefecto("true");
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(4);
            tab_tabla1.setMostrarNumeroRegistros(false);
            tab_tabla1.dibujar();
            if (tab_tabla1.isEmpty()) {
                tab_tabla1.insertar();
            }
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla1);
            pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
            pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
            pat_panel.getMenuTabla().getItem_importar().setRendered(false);

            mep_menu.dibujar(3, "MODIFICAR CERTIFICADO DE INVERSIÓN BANCARIA", pat_panel);
        } else {
            utilitario.agregarMensajeInfo("Seleccione un certificado", "");
        }

    }

    public boolean obtenetFechaVencimiento() {
        try {
            if (mep_menu.getOpcion() == 1) {
                if (tab_tabla1.getValor("plazo_ipcer") != null && !tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
                    tab_tabla1.setValor("fecha_vence_ipcer", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla1.getValor("plazo_ipcer")))));
                    utilitario.addUpdateTabla(tab_tabla1, "fecha_vence_ipcer", "");
                    return true;
                } else {
                    tab_tabla1.setValor("fecha_vence_ipcer", "");
                    utilitario.addUpdateTabla(tab_tabla1, "fecha_vence_ipcer", "");
                    return false;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void obtenetFechaVencimiento(DateSelectEvent evt) {
//        tab_renovacion_inversion.modificar(evt);
        if (obtenetFechaVencimiento()) {
            calculainteres();
        }
    }

    public void calculainteres() {
        double capital = 0;
        double tasa = 0;
        double plazo = 0;
        double interes = 0;
        double valortotal = 0;
        if (tab_tabla1.getValor("capital_ipcer") != null && !tab_tabla1.getValor("capital_ipcer").isEmpty()) {
            try {
                capital = Double.parseDouble(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "capital_ipcer"));
            } catch (Exception e) {
                capital = 0;
            }
        }
        if (tab_tabla1.getValor("tasa_ipcer") != null && !tab_tabla1.getValor("tasa_ipcer").isEmpty()) {
            try {
                tasa = Double.parseDouble(tab_tabla1.getValor("tasa_ipcer"));

            } catch (Exception e) {
                tasa = 0;
            }
        }
        if (tab_tabla1.getValor("plazo_ipcer") != null && !tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
            plazo = Double.parseDouble(tab_tabla1.getValor("plazo_ipcer"));
            obtenetFechaVencimiento();
        } else {
            plazo = 0;
            obtenetFechaVencimiento();
        }
        interes = (capital * tasa * plazo) / 36000;
        valortotal = capital + interes;
        tab_tabla1.setValor("interes_ipcer", utilitario.getFormatoNumero(interes));
        tab_tabla1.setValor("valor_a_pagar_ipcer", utilitario.getFormatoNumero(valortotal));
        utilitario.addUpdateTabla(tab_tabla1, "interes_ipcer,valor_a_pagar_ipcer", "");
    }

    public void calcularInteres(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        calculainteres();
    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            if (validarCertificado()) {
                if (String.valueOf(rad_hace_asiento.getValue()).equals("true")) {

                    asc_asiento.nuevoAsiento();
                    asc_asiento.dibujar();
                    asc_asiento.getBot_aceptar().setMetodo("aceptarCrearCertificadoB");
                } else {
                    tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                    if (tab_tabla1.guardar()) {
                        guardarPantalla();
                        dibujarListadoB();
                    }
                }
            }
        } else if (mep_menu.getOpcion() == 3) {  //MODIFICAR
            if (tab_tabla1.guardar()) {
                guardarPantalla();
                dibujarListadoB();
            }
        } else if (mep_menu.getOpcion() == 4) {  //PAGO INTERESES
            if (aut_inversion.getValor() != null) {
                tab_tabla2.setValor("fecha_sistema_ippin", utilitario.getFechaActual());
                tab_tabla2.setValor("ide_ipcer", aut_inversion.getValor());
            } else {
                utilitario.agregarMensajeInfo("Seleccione una inversión Bancaria", "");
                return;
            }
            if (validarPagoInteres()) {
                if (String.valueOf(rad_hace_asiento.getValue()).equals("true")) {
                    asc_asiento.nuevoAsiento();
                    asc_asiento.dibujar();
                    asc_asiento.getBot_aceptar().setMetodo("aceptarPagoInteresB");
                } else {
                    if (tab_tabla2.guardar()) {
                        guardarPantalla();
                        tab_tabla1.actualizar();
                        tab_tabla2.limpiar();
                        tab_tabla2.insertar();
                    }
                }
            }
        }
    }

    public void aceptarPagoInteresB() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                tab_tabla2.setValor("ide_cnccc", asc_asiento.getIde_cnccc());
                tab_tabla2.guardar();
                guardarPantalla();
                tab_tabla1.actualizar();
                tab_tabla2.limpiar();
                tab_tabla2.insertar();
            }
        }
    }

    public void aceptarCrearCertificadoB() {

        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                tab_tabla1.setValor("ide_cnccc", asc_asiento.getIde_cnccc());
                tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                tab_tabla1.guardar();
                guardarPantalla();
                dibujarListadoB();

            }
        }
    }

    public boolean validarPagoInteres() {
        if (tab_tabla2.getValor("fecha_pago_ippin") == null || tab_tabla2.getValor("fecha_pago_ippin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la fecha de pago");
            return false;
        }
        if (tab_tabla2.getValor("valor_ippin") == null || tab_tabla2.getValor("valor_ippin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el valor a pagar");
            return false;
        }

        return true;
    }

    public boolean validarCertificado() {
        if (tab_tabla1.getValor("capital_ipcer") == null || tab_tabla1.getValor("capital_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el capital");
            return false;
        }
        if (tab_tabla1.getValor("plazo_ipcer") == null || tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el plazo");
            return false;
        }

        if (tab_tabla1.getValor("tasa_ipcer") == null || tab_tabla1.getValor("tasa_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la tasa de interes");
            return false;
        }

        if (tab_tabla1.getValor("fecha_emision_ipcer") == null || tab_tabla1.getValor("fecha_emision_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la fecha de emisión");
            return false;
        }
        if (tab_tabla1.getValor("observacion_ipcer") == null || tab_tabla1.getValor("observacion_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la observación");
            return false;
        }
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el Beneficiario");
            return false;
        }
        if (tab_tabla1.getValor("ide_iptin") == null || tab_tabla1.getValor("ide_iptin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el Tipo de Inversión");
            return false;
        }
        if (tab_tabla1.getValor("ide_cnmod") == null || tab_tabla1.getValor("ide_cnmod").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la Moneda");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipein") == null || tab_tabla1.getValor("ide_ipein").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el estado");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipcin") == null || tab_tabla1.getValor("ide_ipcin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la Clase de Inversión");
            return false;
        }

        return true;
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla1.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
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

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public AutoCompletar getAut_inversion() {
        return aut_inversion;
    }

    public void setAut_inversion(AutoCompletar aut_inversion) {
        this.aut_inversion = aut_inversion;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

}

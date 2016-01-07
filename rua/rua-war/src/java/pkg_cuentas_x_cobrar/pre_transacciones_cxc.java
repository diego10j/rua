/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class pre_transacciones_cxc extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private AutoCompletar aut_filtro_proveedor = new AutoCompletar();
    private String proveedor_actual = "-1";
    private Etiqueta eti_saldo = new Etiqueta();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Texto tex_saldo = new Texto();
    // para Anticipos de clientes
    private Boton bot_anticipo = new Boton();
    private Boton bot_cliente = new Boton();
    private Dialogo dia_anticipo = new Dialogo();
    private Dialogo dia_cliente = new Dialogo();
    private AutoCompletar aut_cliente_anticipo = new AutoCompletar();
    private Combo com_banco_anticipo = new Combo();
    private Combo com_cuenta_banco_anticipo = new Combo();
    private Texto tex_num_anticipo = new Texto();
    private Texto tex_valor_anticipo = new Texto();
    private AreaTexto tex_observacion_anticipo = new AreaTexto();
// Asiento
    private String banco_actual = "-1";
    private VistaAsiento via_asiento = new VistaAsiento();
    private String p_tipo_comprobante_egreso = utilitario.getVariable("p_con_tipo_comprobante_egreso");
    private String p_estado_comprobante_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private String p_modulo = "7";
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
    private Boton bot_clean = new Boton();
    private Consulta consul_pxc = new Consulta();

    public pre_transacciones_cxc() {
        bar_botones.agregarReporte();

        aut_filtro_proveedor.setId("aut_filtro_proveedor");
        aut_filtro_proveedor.setAutoCompletar("select ide_geper,identificac_geper,nom_geper from gen_persona where es_cliente_geper is TRUE");
        aut_filtro_proveedor.setMetodoChange("filtrar_por_proveedor");
        bar_botones.agregarComponente(new Etiqueta("Cliente: "));
        bar_botones.agregarComponente(aut_filtro_proveedor);
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarComponente(bot_clean);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("cxc_detall_transa", "ide_ccdtr", 2);
        tab_tabla1.getColumna("ide_ccctr").setVisible(false);
        tab_tabla1.getColumna("ide_cccfa").setVisible(false);
        tab_tabla1.getColumna("ide_ccttr").setCombo("cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr", "");
        tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
        tab_tabla1.setCondicion("ide_ccdtr=-1");
        tab_tabla1.setCampoOrden("ide_ccdtr desc");
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla1);
        consul_pxc.setId("consul_pxc");
        consul_pxc.setTitle("Consulta ClientesXProductos");
        consul_pxc.setConsulta("SELECT cdf.ide_ccdfa,cf.fecha_emisi_cccfa,iart.nombre_inarti ,cdf.cantidad_ccdfa,cdf.precio_ccdfa,cdf.total_ccdfa from "
                + " cxc_deta_factura cdf "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=cdf.ide_cccfa "
                + "left join inv_articulo iart on  iart.ide_inarti=cdf.ide_inarti "
                + "left join gen_persona gp on gp.ide_geper=cf.ide_geper "
                + "where cf.ide_geper=-1 "
                + "ORDER BY cf.fecha_emisi_cccfa", "ide_ccdfa");
        consul_pxc.getBot_aceptar().setRendered(false);
        utilitario.addUpdate("consul_pxc");

        //**************** Dialogo Anticipos ******************************
        bot_anticipo.setId("bot_anticipo");
        bot_anticipo.setValue("Anticipo");
        bot_anticipo.setMetodo("abrirDialogoAnticipo");
        bar_botones.agregarBoton(bot_anticipo);

        bot_cliente.setId("bot_cliente");
        bot_cliente.setValue("ProductoXCliente");
        bot_cliente.setMetodo("abrirClienteXProducto");
        agregarComponente(consul_pxc);
        utilitario.addUpdate("consul_pxc");

        bar_botones.agregarBoton(bot_cliente);

        Etiqueta eti_proveedor_a = new Etiqueta();
        eti_proveedor_a.setValue("Cliente ");
        Etiqueta eti_banco_a = new Etiqueta();
        eti_banco_a.setValue("Banco ");
        Etiqueta eti_cuenta_banco_a = new Etiqueta();
        eti_cuenta_banco_a.setValue("Cuenta Banco ");
        Etiqueta eti_num_a = new Etiqueta();
        eti_num_a.setValue("Numero ");
        Etiqueta eti_valor_a = new Etiqueta();
        eti_valor_a.setValue("Valor ");
        Etiqueta eti_observacion_a = new Etiqueta();
        eti_observacion_a.setValue("Observacion ");

        aut_cliente_anticipo.setId("aut_cliente_anticipo");
        aut_cliente_anticipo.setAutoCompletar("select ide_geper,nom_geper from gen_persona where es_cliente_geper is TRUE and ide_empr=" + utilitario.getVariable("ide_empr"));
        tex_num_anticipo.setId("tex_num_anticipo");
        com_banco_anticipo.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
        com_banco_anticipo.setMetodo("cargarCuentaBancosAnticipo");
        com_cuenta_banco_anticipo.setId("com_cuenta_banco_anticipo");
        com_cuenta_banco_anticipo.setCombo(new ArrayList());
        com_cuenta_banco_anticipo.setMetodo("cargarNumChequeAnticipo");
        tex_valor_anticipo.setSoloNumeros();
        tex_observacion_anticipo.setCols(50);

        Grid grid_anticipo = new Grid();
        grid_anticipo.setColumns(2);

        grid_anticipo.getChildren().add(eti_proveedor_a);
        grid_anticipo.getChildren().add(aut_cliente_anticipo);
        grid_anticipo.getChildren().add(eti_banco_a);
        grid_anticipo.getChildren().add(com_banco_anticipo);
        grid_anticipo.getChildren().add(eti_cuenta_banco_a);
        grid_anticipo.getChildren().add(com_cuenta_banco_anticipo);
        grid_anticipo.getChildren().add(eti_num_a);
        grid_anticipo.getChildren().add(tex_num_anticipo);
        grid_anticipo.getChildren().add(eti_valor_a);
        grid_anticipo.getChildren().add(tex_valor_anticipo);
        grid_anticipo.getChildren().add(eti_observacion_a);
        grid_anticipo.getChildren().add(tex_observacion_anticipo);

        dia_anticipo.setId("dia_anticipo");
        dia_anticipo.setTitle("Anticipo Proveedores");
        dia_anticipo.setWidth("50%");
        dia_anticipo.setHeight("50%");
        dia_anticipo.setDialogo(grid_anticipo);
        dia_anticipo.setDynamic(false);
        grid_anticipo.setStyle("width:" + (dia_anticipo.getAnchoPanel() - 5) + "px;height:" + dia_anticipo.getAltoPanel() + "px;overflow: auto;display: block;");
        dia_anticipo.getBot_aceptar().setMetodo("aceptarDialogoAnticipo");
        dia_anticipo.getBot_cancelar().setMetodo("cancelarDialogo");
        gru_pantalla.getChildren().add(dia_anticipo);

        //  CONFIGURACION COMPROBANTE CONTABILIDAD
        via_asiento.setId("via_asiento");
        via_asiento.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
        via_asiento.getBot_cancelar().setMetodo("cancelarDialogo");
        via_asiento.setDynamic(false);

        gru_pantalla.getChildren().add(via_asiento);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_rep.setId("sel_rep");
        gru_pantalla.getChildren().add(sel_rep);
        gru_pantalla.getChildren().add(rep_reporte);

        Grid gri_saldo = new Grid();
        gri_saldo.setColumns(2);
        eti_saldo.setId("eti_saldo");
        eti_saldo.setValue("Saldo: ");
        eti_saldo.setStyle("font-size: 16px;font-weight: bold");
        tex_saldo.setId("tex_saldo");
        tex_saldo.setStyle("font-size: 16px;font-weight: bold");
        tex_saldo.setDisabled(true);
        gri_saldo.getChildren().add(eti_saldo);
        gri_saldo.getChildren().add(tex_saldo);
        Division div_footer = new Division();
        div_footer.setFooter(pat_panel2, gri_saldo, "90%");

        div_division.setId("div_division");
        div_division.dividir1(div_footer);

        gru_pantalla.getChildren().add(div_division);
    }

    public void aceptarComprobanteContabilidad() {

        if (via_asiento.validarComprobante()) {
            cab_com_con.setObservacion_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            lista_detalles.clear();
            for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), ""));
            }
            cab_com_con.setDetalles(lista_detalles);
            conta.generarAsientoContable(cab_com_con);
            String ide_cnccc = conta.getTab_cabecera().getValor("ide_cnccc");
            if (ide_cnccc != null) {
                cls_bancos bancos = new cls_bancos();
                via_asiento.cerrar();
                if (asiento_anticipo == true) {
                    asiento_anticipo = false;
                    bancos.generarLibroBanco(aut_cliente_anticipo.getValor() + "", utilitario.getFechaActual(), utilitario.getVariable("p_tes_tran_deposito"), com_cuenta_banco_anticipo.getValue() + "", ide_cnccc, Double.parseDouble(tex_valor_anticipo.getValue() + ""), tex_observacion_anticipo.getValue() + "", tex_num_anticipo.getValue() + "");
                    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                    cxc.generarAnticipoCxC(bancos.getTab_cab_libro_banco(), aut_cliente_anticipo.getValor());
                    utilitario.getConexion().guardarPantalla();
                }
            }
            utilitario.addUpdate("via_asiento");
//                cargar_saldo();

        } else {
            utilitario.agregarMensajeError("EL Asiento no Cuadra", "El asiento no cuadra");
        }

    }

    public void cancelarDialogo() {
        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        } else if (dia_anticipo.isVisible()) {
            dia_anticipo.cerrar();
            utilitario.addUpdate("dia_anticipo");
        }
        // utilitario.getConexion().rollback(); *********
        utilitario.getConexion().getSqlPantalla().clear();
    }

    public void cancelarDialogoCliente() {
        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        } else if (dia_anticipo.isVisible()) {
            dia_anticipo.cerrar();
            utilitario.addUpdate("dia_anticipo");
        }
        //  utilitario.getConexion().rollback();***********
        utilitario.getConexion().getSqlPantalla().clear();
    }

    public void generarTransaccionCxPAnticipo(String ide_cnccc, String cab_libr_ban) {

        Tabla tab_cab_tran_cxp = new Tabla();
        tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr", 0);
        tab_cab_tran_cxp.setCondicion("ide_cpctr=-1");
        tab_cab_tran_cxp.ejecutarSql();
        tab_cab_tran_cxp.insertar();
        tab_cab_tran_cxp.setValor("ide_cpttr", utilitario.getVariable("p_cxp_tipo_trans_pago"));
        tab_cab_tran_cxp.setValor("ide_geper", str_proveedor_anticipo);
        tab_cab_tran_cxp.setValor("fecha_trans_cpctr", utilitario.getFechaActual());
        tab_cab_tran_cxp.setValor("observacion_cpctr", "transaccion por anticipo");

        Tabla tab_det_tran_cxp = new Tabla();
        tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr", 1);
        tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
        tab_det_tran_cxp.ejecutarSql();
        tab_cab_tran_cxp.guardar();

        tab_det_tran_cxp.insertar();
//        tab_det_tran_cxp.setValor("ide_cpcfa", tab_tabla1.getValor("ide_cpcfa"));
        tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxp.setValor("ide_cpttr", "7");// tipo transaccion anticipo
        tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
        tab_det_tran_cxp.setValor("fecha_trans_cpdtr", utilitario.getFechaActual());
        tab_det_tran_cxp.setValor("valor_cpdtr", str_valor_anticipo);
        tab_det_tran_cxp.setValor("observacion_cpdtr", "transaccion por anticipo");
        tab_det_tran_cxp.setValor("numero_pago_cpdtr", 1 + "");
        tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFechaActual());
        tab_det_tran_cxp.setValor("docum_relac_cpdtr", str_num_doc_anticipo);
        tab_det_tran_cxp.setValor("ide_cnccc", ide_cnccc);
        if (!cab_libr_ban.isEmpty()) {
            tab_det_tran_cxp.setValor("ide_teclb", cab_libr_ban);
        }

        tab_det_tran_cxp.guardar();

    }

    public String guardarLibroBancoAnticipo(String ide_cnccc) {

        Tabla tab_cab_libro_banco = new Tabla();
        tab_cab_libro_banco.setId("tab_cab_libro_banco");
        tab_cab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", 0);
        tab_cab_libro_banco.setCondicion("ide_teclb=-1");
        tab_cab_libro_banco.ejecutarSql();
        tab_cab_libro_banco.insertar();
        tab_cab_libro_banco.setValor("ide_tecba", str_cuenta_banco_anticipo);
        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", str_valor_anticipo);
        tab_cab_libro_banco.setValor("numero_teclb", str_num_doc_anticipo);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", utilitario.getFechaActual());
        tab_cab_libro_banco.setValor("fecha_venci_teclb", utilitario.getFechaActual());
        tab_cab_libro_banco.setValor("beneficiari_teclb", conta.obtenerParametroPersona("nom_geper", str_proveedor_anticipo));
        tab_cab_libro_banco.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_cheque"));//parametrizar a cheque
        tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
        tab_cab_libro_banco.setValor("observacion_teclb", "Pago de anticipo a proveedor");
        tab_cab_libro_banco.guardar();
        return tab_cab_libro_banco.getValor("ide_teclb");
    }
    boolean asiento_anticipo;
    String str_cuenta_banco_anticipo;
    String str_proveedor_anticipo;
    String str_num_doc_anticipo;
    String str_valor_anticipo;

    public void generarAsientoAnticipo() {
        cls_bancos banco = new cls_bancos();
        asiento_anticipo = true;
        str_cuenta_banco_anticipo = com_cuenta_banco_anticipo.getValue() + "";
        str_proveedor_anticipo = aut_cliente_anticipo.getValor();
        str_num_doc_anticipo = tex_num_anticipo.getValue() + "";
        str_valor_anticipo = tex_valor_anticipo.getValue() + "";
        conta.limpiar();
        cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_egreso, p_estado_comprobante_normal, p_modulo, aut_cliente_anticipo.getValor(), utilitario.getFechaActual(), tex_observacion_anticipo.getValue() + "");
        lista_detalles.clear();
        String cuenta1 = banco.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_banco_anticipo.getValue() + "");
        if (cuenta1 != null) {
            double valor = Double.parseDouble(tex_valor_anticipo.getValue() + "");
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), cuenta1, Double.parseDouble(utilitario.getFormatoNumero(valor)), ""));
        }
        cab_com_con.setDetalles(lista_detalles);
        via_asiento.setVistaAsiento(cab_com_con);
        via_asiento.dibujar();
        utilitario.addUpdate("via_asiento");
    }

    public void aceptarDialogoAnticipo() {
        if (validarDialogoAnticipo()) {
            dia_anticipo.cerrar();
            utilitario.addUpdate("dia_anticipo");
            generarAsientoAnticipo();
        }
    }

    public boolean validarDialogoAnticipo() {
        if (aut_cliente_anticipo.getValor() == null || aut_cliente_anticipo.getValor().isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe seleccionar un proveedor");
            return false;
        }
        if (com_banco_anticipo.getValue() == null) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe seleccionar un Banco");
            return false;
        }
        if (com_cuenta_banco_anticipo.getValue() == null) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe seleccionar una Cuenta Bancaria");
            return false;
        }
        if (tex_num_anticipo.getValue() == null) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe ingresar el numero de documento");
            return false;
        } else if (tex_num_anticipo.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe ingresar el numero de documento");
            return false;
        }
        if (tex_valor_anticipo.getValue() == null) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe ingresar el valor del anticipo");
            return false;
        } else if (tex_valor_anticipo.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe ingresar el valor del anticipo");
            return false;
        }
        if (tex_observacion_anticipo.getValue() == null) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe ingresar una observacion");
            return false;
        } else if (tex_observacion_anticipo.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "Debe ingresar una observacion");
            return false;
        }

        return true;

    }

    public void cargarNumChequeAnticipo() {
        cls_bancos banco = new cls_bancos();
        tex_num_anticipo.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco_anticipo.getValue() + "", utilitario.getVariable("p_tes_tran_cheque")));
        utilitario.addUpdate("tex_num_anticipo");
    }

    public void cargarCuentaBancosAnticipo() {

        if (com_banco_anticipo.getValue() != null) {
            banco_actual = com_banco_anticipo.getValue().toString();
            com_cuenta_banco_anticipo.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and  ide_teban=" + banco_actual);
            tex_num_anticipo.setValue("");
        } else {
            com_cuenta_banco_anticipo.setValue("");
            tex_num_anticipo.setValue("");
        }
        utilitario.addUpdate("com_cuenta_banco_anticipo,tex_num_anticipo");
    }

    public void abrirDialogoAnticipo() {
        System.out.println("Anticipos ******");
        aut_cliente_anticipo.setValor("");
        tex_num_anticipo.setValue("");
        tex_observacion_anticipo.setValue("");
        tex_valor_anticipo.setValue("");
        dia_anticipo.dibujar();
        utilitario.addUpdate("dia_anticipo");
    }

    public void abrirClienteXProducto() {
        proveedor_actual = aut_filtro_proveedor.getValor();
        if (aut_filtro_proveedor.getValor() != null) {
            consul_pxc.getTab_consulta_dialogo().setSql(
                    "SELECT cdf.ide_ccdfa,cf.fecha_emisi_cccfa,iart.nombre_inarti ,cdf.cantidad_ccdfa,cdf.precio_ccdfa,cdf.total_ccdfa from "
                    + " cxc_deta_factura cdf "
                    + "left join cxc_cabece_factura cf on cf.ide_cccfa=cdf.ide_cccfa "
                    + "left join inv_articulo iart on  iart.ide_inarti=cdf.ide_inarti "
                    + "left join gen_persona gp on gp.ide_geper=cf.ide_geper "
                    + "where cf.ide_geper=" + aut_filtro_proveedor.getValor() + " "
                    + "ORDER BY cf.fecha_emisi_cccfa");

            consul_pxc.setStyle("font-size: 80%;font-weight: bold");
            consul_pxc.dibujar();

        } else {

            utilitario.agregarMensajeInfo("Atencion", "No existe un Cliente por Producto");
        }
    }

    public void filtrar_por_proveedor(SelectEvent evt) {

        aut_filtro_proveedor.onSelect(evt);
        proveedor_actual = aut_filtro_proveedor.getValor();

        tab_tabla1.setCondicion("ide_ccctr in (select ide_ccctr from cxc_cabece_transa where ide_geper=" + proveedor_actual + ")");
        tab_tabla1.ejecutarSql();
        generar_estado_cuenta_proveedor();

    }

    public void generar_estado_cuenta_proveedor() {
        double saldo_pos = 0;
        double saldo_neg = 0;
        double saldo;

        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            List signo_sql = utilitario.getConexion().consultar("SELECT signo_ccttr from cxc_tipo_transacc where ide_ccttr=" + tab_tabla1.getValor(i, "ide_ccttr"));
            if (signo_sql.get(0) != null) {
                if (Integer.parseInt(signo_sql.get(0).toString()) == 1) {
                    saldo_pos = Double.parseDouble(tab_tabla1.getValor(i, "valor_ccdtr")) + saldo_pos;
                } else {
                    if (Integer.parseInt(signo_sql.get(0).toString()) == -1) {
                        saldo_neg = Double.parseDouble(tab_tabla1.getValor(i, "valor_ccdtr")) + saldo_neg;
                    }
                }
            }
        }
        saldo = saldo_pos - saldo_neg;
        eti_saldo.setValue("Saldo x Cobrar: ");
        eti_saldo.setStyle("font-size: 18px;font-weight: bold");
        tex_saldo.setValue(utilitario.getFormatoNumero(saldo));
        tex_saldo.setStyle("font-size: 16px;font-weight: bold");
        tex_saldo.setDisabled(true);
        utilitario.addUpdate("eti_saldo,tex_saldo");
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra    
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    public void aceptarReporte() {
        //Se ejecuta cuando se selecciona un reporte de la lista
        if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprombante de contabilidad");
                }

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Cheque")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_teclb") != null && !tab_tabla1.getValor("ide_teclb").isEmpty()) {
                    TablaGenerica tab_lib = utilitario.consultar("SELECT * FROM tes_cab_libr_banc WHERE ide_teclb =" + tab_tabla1.getValor("ide_teclb"));
                    if (tab_lib.getTotalFilas() > 0) {
                        if (tab_lib.getValor(0, "ide_tettb").equalsIgnoreCase(utilitario.getVariable("p_tes_tran_cheque"))) {

                            parametro = new HashMap();
                            rep_reporte.cerrar();
                            cls_bancos banco = new cls_bancos();
                            parametro.put("beneficiario", tab_lib.getValor("beneficiari_teclb") + "");
                            parametro.put("monto", tab_lib.getValor("valor_teclb") + "");
                            parametro.put("anio", utilitario.getAnio(tab_lib.getValor("fecha_trans_teclb")) + "");
                            parametro.put("mes", utilitario.getMes(tab_lib.getValor("fecha_trans_teclb")) + "");
                            parametro.put("dia", utilitario.getDia(tab_lib.getValor("fecha_trans_teclb")) + "");
                            parametro.put("monto_letras", banco.agregarAsteriscosCheque(utilitario.getLetrasDolarNumero(tab_lib.getValor("valor_teclb"))));
                            parametro.put("ide_cnccc", Long.parseLong(tab_lib.getValor("ide_cnccc")));
                            parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                            parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                            parametro.put("p_num_cheque", tab_lib.getValor("numero_teclb") + "");
                            parametro.put("p_num_trans", tab_lib.getValor("ide_teclb") + "");
                            List lis_geper = utilitario.getConexion().consultar("select identificac_geper from gen_persona where ide_geper=(select ide_geper from con_cab_comp_cont where ide_cnccc =" + tab_lib.getValor("ide_cnccc") + ")");
                            if (lis_geper.size() > 0) {
                                parametro.put("p_identificacion", lis_geper.get(0) + "");
                            } else {
                                parametro.put("p_identificacion", "");
                            }
                            sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                            sel_rep.dibujar();
                            utilitario.addUpdate("rep_reporte,sel_rep");
                        } else {
                            utilitario.agregarMensajeInfo("No se puede generar el cheque", "La fila seleccionada no es un cheque");
                        }
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el cheque", "La fila seleccionada no es un cheque");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Cuentas x Cobrar")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sef_rep");
            }
        }
    }

    public void limpiar() {
        aut_filtro_proveedor.setValue(null);
        tab_tabla1.limpiar();
        tex_saldo.limpiar();
        utilitario.addUpdate("aut_filtro_proveedor,tab_tabla1,consul_pxc,tex_saldo");

    }

    @Override
    public void insertar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.insertar();
        } else {
            utilitario.getTablaisFocus().insertar();
        }
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.eliminar();
        } else {
            utilitario.getTablaisFocus().eliminar();
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public AutoCompletar getAut_filtro_proveedor() {
        return aut_filtro_proveedor;
    }

    public void setAut_filtro_proveedor(AutoCompletar aut_filtro_proveedor) {
        this.aut_filtro_proveedor = aut_filtro_proveedor;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public AutoCompletar getAut_cliente_anticipo() {
        return aut_cliente_anticipo;
    }

    public void setAut_cliente_anticipo(AutoCompletar aut_cliente_anticipo) {
        this.aut_cliente_anticipo = aut_cliente_anticipo;
    }

    public Dialogo getDia_anticipo() {
        return dia_anticipo;
    }

    public void setDia_anticipo(Dialogo dia_anticipo) {
        this.dia_anticipo = dia_anticipo;
    }

    public VistaAsiento getVia_asiento() {
        return via_asiento;
    }

    public void setVia_asiento(VistaAsiento via_asiento) {
        this.via_asiento = via_asiento;
    }

    public Boton getBot_cliente() {
        return bot_cliente;
    }

    public void setBot_cliente(Boton bot_cliente) {
        this.bot_cliente = bot_cliente;
    }

    public Consulta getConsul_pxc() {
        return consul_pxc;
    }

    public void setConsul_pxc(Consulta consul_pxc) {
        this.consul_pxc = consul_pxc;
    }

    public Dialogo getDia_cliente() {
        return dia_cliente;
    }

    public void setDia_cliente(Dialogo dia_cliente) {
        this.dia_cliente = dia_cliente;
    }
}

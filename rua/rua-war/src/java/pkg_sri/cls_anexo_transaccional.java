/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor
 */
package pkg_sri;

import framework.aplicacion.TablaGenerica;
import java.text.SimpleDateFormat;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_anexo_transaccional {

    private Document doc_anexo;
    private String path = "";
    private String nombre = "";
    private String fecha_inicio = "";
    private String fecha_fin = "";
    private Utilitario utilitario = new Utilitario();
    private String opcionAnexo = "1";

    public String AnexoTransaccional(String anio, String mes) {
        String mensaje = "";

        fecha_inicio = utilitario.getFormatoFecha(anio + "-" + mes + "-01");
        fecha_fin = utilitario.getUltimaFechaMes(fecha_inicio);
        int aux_contador = 0;
        try {
            TablaGenerica tab_empresa = utilitario.consultar("SELECT identificacion_empr,nom_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
            if (tab_empresa.getTotalFilas() > 0) {
                doc_anexo = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                doc_anexo.setXmlVersion("1.0");
                doc_anexo.setXmlStandalone(true);

                TablaGenerica tab_estab = utilitario.consultar("select  count(*),(sum(base_grabada_cccfa)+sum(base_no_objeto_iva_cccfa)+sum(base_tarifa0_cccfa)) as total_ventas, substr(df.serie_ccdaf, 1, 3) as establecimiento "
                        + " from cxc_cabece_factura cf "
                        + " LEFT JOIN cxc_datos_fac df ON (df.ide_ccdaf = cf.ide_ccdaf)"
                        + " where fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal")
                        + " GROUP BY substr(df.serie_ccdaf, 1, 3)");
                //System.out.println("1111:  " + tab_estab.getSql());
                Element raiz = doc_anexo.createElement("iva");
                raiz.appendChild(crearElemento("TipoIDInformante", null, "R")); //  Ruc
                raiz.appendChild(crearElemento("IdInformante", null, tab_empresa.getValor("identificacion_empr")));
                raiz.appendChild(crearElemento("razonSocial", null, tab_empresa.getValor("nom_empr")));
                raiz.appendChild(crearElemento("Anio", null, anio));
                raiz.appendChild(crearElemento("Mes", null, mes));
                raiz.appendChild(crearElemento("numEstabRuc", null, "00" + tab_estab.getTotalFilas()));

                double totalNotasCredito = 0;
                try {
                    totalNotasCredito = Double.parseDouble(getTotalNotasCredito());
                } catch (Exception e) {
                }
                double totalVentas = tab_estab.getSumaColumna("total_ventas") - totalNotasCredito;
                raiz.appendChild(crearElemento("totalVentas", null, utilitario.getFormatoNumero(totalVentas)));
                raiz.appendChild(crearElemento("codigoOperativo", null, "IVA"));

                doc_anexo.appendChild(raiz);

                if (opcionAnexo.equals("1") || opcionAnexo.equals("2")) {
                    //COMPRAS
                    Element compras = doc_anexo.createElement("compras");
                    raiz.appendChild(compras);
                    ////////////////////BUSCAR TODAS LAS COMPRAS ESTO ES EN UN FOR
                    TablaGenerica tab_compras = utilitario.consultar("Select cabece.ide_cpcfa, cabece.ide_cncre,suste.alterno_srtst,iden.alterno1_getid,prove.identificac_geper,docu.alter_tribu_cntdo,iden.alterno2_getid,  "
                            + " cabece.fecha_trans_cpcfa,cabece.numero_cpcfa,cabece.fecha_emisi_cpcfa,cabece.autorizacio_cpcfa,cabece.total_cpcfa,valor_ice_cpcfa, "
                            + " cabece.base_grabada_cpcfa,cabece.base_tarifa0_cpcfa,cabece.base_no_objeto_iva_cpcfa,cabece.valor_iva_cpcfa, "
                            + " rete.numero_cncre,rete.autorizacion_cncre,rete.fecha_emisi_cncre,dpa.alterno_ats,cabece.ide_cntdo, fecha_emision_nc_cpcfa,numero_nc_cpcfa,autorizacio_nc_cpcfa,motivo_nc_cpcfa "
                            + " from cxp_cabece_factur cabece "
                            + " left join gen_persona prove on cabece.ide_geper= prove.ide_geper "
                            + " left join sri_tipo_sustento_tributario suste on cabece.ide_srtst=suste.ide_srtst "
                            + " left join gen_tipo_identifi iden on prove.ide_getid=iden.ide_getid "
                            + " left join con_tipo_document docu on cabece.ide_cntdo=docu.ide_cntdo "
                            + " left join con_cabece_retenc rete on cabece.ide_cncre=rete.ide_cncre "
                            + " left join con_deta_forma_pago dpa on cabece.ide_cndfp=dpa.ide_cndfp "
                            + " where  cabece.fecha_emisi_cpcfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'"
                            + " and ide_rem_cpcfa is null and ide_cpefa=0"
                            + " order by cabece.fecha_emisi_cpcfa, cabece.ide_cpcfa ");
                    //System.out.println("COMPRAS:  " + tab_compras.getSql());
                    String p_con_tipo_documento_reembolso = utilitario.getVariable("p_con_tipo_documento_reembolso");
                    String p_con_tipo_documento_nota_credito = utilitario.getVariable("p_con_tipo_documento_nota_credito");
                    String p_con_tipo_documento_nota_debito = utilitario.getVariable("p_con_tipo_documento_nota_debito"); //26-01-2021 DFJ
                    if (p_con_tipo_documento_nota_debito == null) {
                        p_con_tipo_documento_nota_debito = "";
                    }
                    String ideRetenciones = "";
                    for (int i = 0; i < tab_compras.getTotalFilas(); i++) {
                        String act_ret = "";
                        if (tab_compras.getValor(i, "ide_cncre") != null) {
                            act_ret = tab_compras.getValor(i, "ide_cncre") + "";
                        }
                        act_ret = act_ret.replace("null", ""); //si hay documentos sin retenciones    

                        if (act_ret.isEmpty() == false) {
                            if (ideRetenciones.isEmpty() == false) {
                                ideRetenciones += ",";
                            }
                            ideRetenciones += act_ret;
                        }

                    }
                    ideRetenciones = ideRetenciones.replace("'null',", ""); //si hay documentos sin retenciones 
                    ideRetenciones = ideRetenciones.replace("'null'", ""); //si hay documentos sin retenciones 
                    ideRetenciones = ideRetenciones.replace("null,", ""); //si hay documentos sin retenciones  
                    //ideRetenciones = ideRetenciones.replace(",)", ")"); //si hay documentos sin retenciones  
                    ideRetenciones = ideRetenciones.replace(",,", ","); //si hay documentos sin retenciones   

                    if (ideRetenciones.equals("'null'") || ideRetenciones.equals("null") || ideRetenciones.isEmpty()) {
                        ideRetenciones = "-1";
                    }

                    TablaGenerica tab_rete_iva_bienes_ = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre,cabece.ide_cncre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                            + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                            + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva30") + " and cabece.ide_cncre in(" + ideRetenciones + ") order by cabece.ide_cncre");
                    //System.out.println("tab_rete_iva_bienes_:  " + tab_rete_iva_bienes_.getSql());
                    TablaGenerica tab_rete_iva_servicios_ = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre,cabece.ide_cncre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                            + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                            + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva70") + " and cabece.ide_cncre in(" + ideRetenciones + ") order by cabece.ide_cncre");
                    // System.out.println("tab_rete_iva_servicios_:  " + tab_rete_iva_servicios_.getSql());
                    TablaGenerica tab_rete_iva_servicios100_ = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre,cabece.ide_cncre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                            + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                            + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva100") + " and cabece.ide_cncre in(" + ideRetenciones + ") order by cabece.ide_cncre");
                    //05-04-2019 Iva 20% , Iva 10% Contribuyentes especiales
                    TablaGenerica tab_rete_iva10_ = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre,cabece.ide_cncre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                            + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                            + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva10") + " and cabece.ide_cncre in(" + ideRetenciones + ") order by cabece.ide_cncre");
                    TablaGenerica tab_rete_iva20_ = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre,cabece.ide_cncre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                            + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                            + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva20") + " and cabece.ide_cncre in(" + ideRetenciones + ") order by cabece.ide_cncre");
                    // System.out.println("tab_rete_iva_servicios100_:  " + tab_rete_iva_servicios100_.getSql());
                    TablaGenerica tab_retencion_ = utilitario.consultar("SELECT impuesto.casillero_cncim,sum(base_cndre) as base_cndre,porcentaje_cndre,sum(valor_cndre) as valor_cndre,cabece.ide_cncre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                            + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                            + "where impuesto.ide_cnimp=" + utilitario.getVariable("p_con_impuesto_renta") + " and cabece.ide_cncre in(" + ideRetenciones + ") GROUP BY impuesto.casillero_cncim,porcentaje_cndre,cabece.ide_cncre order by cabece.ide_cncre");
                    // System.out.println("tab_retencion_:  " + tab_retencion_.getSql());
                    for (int i = 0; i < tab_compras.getTotalFilas(); i++) {
                        Element detalleCompras = doc_anexo.createElement("detalleCompras");
                        compras.appendChild(detalleCompras);
                        detalleCompras.appendChild(crearElemento("codSustento", null, tab_compras.getValor(i, "alterno_srtst")));
                        detalleCompras.appendChild(crearElemento("tpIdProv", null, tab_compras.getValor(i, "alterno1_getid")));
                        detalleCompras.appendChild(crearElemento("idProv", null, tab_compras.getValor(i, "identificac_geper")));
                        detalleCompras.appendChild(crearElemento("tipoComprobante", null, tab_compras.getValor(i, "alter_tribu_cntdo")));

                        if (tab_compras.getValor(i, "alterno2_getid").equals("06")) {//PASAPORTE
                            TablaGenerica tab_persona = utilitario.consultar("SELECT identificac_geper,nom_geper,alter_tribu_cntco from gen_persona cli "
                                    + "left join con_tipo_contribu tcon on cli.ide_cntco=tcon.ide_cntco "
                                    + "where identificac_geper='" + tab_compras.getValor(i, "identificac_geper") + "'");
                            detalleCompras.appendChild(crearElemento("tipoProv", null, tab_persona.getValor("alter_tribu_cntco") == null ? "02" : tab_persona.getValor("alter_tribu_cntco")));
                            detalleCompras.appendChild(crearElemento("denoProv", null, tab_persona.getValor("nom_geper") == null ? "SIN NOMBRE" : tab_persona.getValor("nom_geper")));
                        }

                        detalleCompras.appendChild(crearElemento("parteRel", null, "NO"));
                        if (tab_compras.getValor(i, "fecha_emisi_cncre") != null) {
                            detalleCompras.appendChild(crearElemento("fechaRegistro", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cncre"))));
                        } else {
                            detalleCompras.appendChild(crearElemento("fechaRegistro", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));
                        }
                        String numero = tab_compras.getValor(i, "numero_cpcfa");
                        detalleCompras.appendChild(crearElemento("establecimiento", null, numero.substring(0, 3)));
                        detalleCompras.appendChild(crearElemento("puntoEmision", null, numero.substring(3, 6)));
                        detalleCompras.appendChild(crearElemento("secuencial", null, numero.substring(6, numero.length())));
                        detalleCompras.appendChild(crearElemento("fechaEmision", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));
                        detalleCompras.appendChild(crearElemento("autorizacion", null, tab_compras.getValor(i, "autorizacio_cpcfa")));
                        detalleCompras.appendChild(crearElemento("baseNoGraIva", null, tab_compras.getValor(i, "base_no_objeto_iva_cpcfa")));
                        detalleCompras.appendChild(crearElemento("baseImponible", null, tab_compras.getValor(i, "base_tarifa0_cpcfa")));
                        detalleCompras.appendChild(crearElemento("baseImpGrav", null, tab_compras.getValor(i, "base_grabada_cpcfa")));
                        detalleCompras.appendChild(crearElemento("baseImpExe", null, utilitario.getFormatoNumero("0")));
                        detalleCompras.appendChild(crearElemento("montoIce", null, tab_compras.getValor(i, "valor_ice_cpcfa")));
                        detalleCompras.appendChild(crearElemento("montoIva", null, tab_compras.getValor(i, "valor_iva_cpcfa")));

                        //// 
                        String ide_retencion = tab_compras.getValor(i, "ide_cncre");
                        if (ide_retencion == null) {
                            ide_retencion = "-1";
                        }

                        boolean booEncontro = false;
                        for (int x = 0; x < tab_rete_iva10_.getTotalFilas(); x++) {
                            if (ide_retencion.equals(tab_rete_iva10_.getValor(x, "ide_cncre"))) {
                                tab_rete_iva10_.setFilaActual(x);
                                booEncontro = true;
                                break;
                            }
                        }
                        if (booEncontro) {
                            detalleCompras.appendChild(crearElemento("valRetBien10", null, tab_rete_iva10_.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valRetBien10", null, "0.00"));
                        }

                        booEncontro = false;
                        for (int x = 0; x < tab_rete_iva20_.getTotalFilas(); x++) {
                            if (ide_retencion.equals(tab_rete_iva20_.getValor(x, "ide_cncre"))) {
                                tab_rete_iva20_.setFilaActual(x);
                                booEncontro = true;
                                break;
                            }
                        }
                        if (booEncontro) {
                            detalleCompras.appendChild(crearElemento("valRetServ20", null, tab_rete_iva20_.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valRetServ20", null, "0.00"));
                        }

//                        TablaGenerica tab_rete_iva_bienes = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
//                                + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
//                                + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva30") + " and cabece.ide_cncre=" + ide_retencion);
                        booEncontro = false;
                        for (int x = 0; x < tab_rete_iva_bienes_.getTotalFilas(); x++) {
                            if (ide_retencion.equals(tab_rete_iva_bienes_.getValor(x, "ide_cncre"))) {
                                tab_rete_iva_bienes_.setFilaActual(x);
                                booEncontro = true;
                                break;
                            }
                        }

                        if (booEncontro) {
                            detalleCompras.appendChild(crearElemento("valorRetBienes", null, tab_rete_iva_bienes_.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valorRetBienes", null, "0.00"));
                        }

                        ///
                        detalleCompras.appendChild(crearElemento("valRetServ50", null, "0.00"));

//                        TablaGenerica tab_rete_iva_servicios = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
//                                + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
//                                + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva70") + " and cabece.ide_cncre=" + ide_retencion);
                        booEncontro = false;
                        for (int x = 0; x < tab_rete_iva_servicios_.getTotalFilas(); x++) {
                            if (ide_retencion.equals(tab_rete_iva_servicios_.getValor(x, "ide_cncre"))) {
                                tab_rete_iva_servicios_.setFilaActual(x);
                                booEncontro = true;
                                break;
                            }
                        }
                        if (booEncontro) {
                            detalleCompras.appendChild(crearElemento("valorRetServicios", null, tab_rete_iva_servicios_.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valorRetServicios", null, "0.00"));
                        }

//                        TablaGenerica tab_rete_iva_servicios100 = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
//                                + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
//                                + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva100") + " and cabece.ide_cncre=" + ide_retencion);
                        booEncontro = false;
                        for (int x = 0; x < tab_rete_iva_servicios100_.getTotalFilas(); x++) {
                            if (ide_retencion.equals(tab_rete_iva_servicios100_.getValor(x, "ide_cncre"))) {
                                tab_rete_iva_servicios100_.setFilaActual(x);
                                booEncontro = true;
                                break;
                            }
                        }
                        if (booEncontro) {
                            detalleCompras.appendChild(crearElemento("valRetServ100", null, tab_rete_iva_servicios100_.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valRetServ100", null, "0.00"));
                        }
                        if (p_con_tipo_documento_reembolso.equals(tab_compras.getValor(i, "ide_cntdo"))) {
                            //reembolsos
                            TablaGenerica tab_sum_reembolso = utilitario.consultar("SELECT  ide_rem_cpcfa,sum(base_tarifa0_cpcfa + base_grabada_cpcfa +  base_no_objeto_iva_cpcfa) as valor\n"
                                    + "from cxp_cabece_factur "
                                    + "WHERE ide_rem_cpcfa=" + tab_compras.getValor(i, "ide_cpcfa") + " and ide_cpefa=0 group by ide_rem_cpcfa");
                            if (tab_sum_reembolso.getTotalFilas() > 0) {
                                detalleCompras.appendChild(crearElemento("totbasesImpReemb", null, utilitario.getFormatoNumero(tab_sum_reembolso.getValor("valor"))));
                            } else {
                                detalleCompras.appendChild(crearElemento("totbasesImpReemb", null, "0.00"));
                            }
                        } else {
                            detalleCompras.appendChild(crearElemento("totbasesImpReemb", null, "0.00"));
                        }

                        Element pagoExterior = doc_anexo.createElement("pagoExterior");
                        detalleCompras.appendChild(pagoExterior);
                        pagoExterior.appendChild(crearElemento("pagoLocExt", null, "01"));
                        pagoExterior.appendChild(crearElemento("paisEfecPago", null, "NA"));
                        pagoExterior.appendChild(crearElemento("aplicConvDobTrib", null, "NA"));
                        pagoExterior.appendChild(crearElemento("pagExtSujRetNorLeg", null, "NA"));

                        double dou_total_factura = Double.parseDouble(tab_compras.getValor(i, "total_cpcfa"));

//                        double dou_base_imp = Double.parseDouble(tab_compras.getValor(i, "base_tarifa0_cpcfa"));
//                        double dou_base_grava = Double.parseDouble(tab_compras.getValor(i, "base_grabada_cpcfa"));
//if (dou_total_factura >= 1000 || dou_base_imp > 0 || dou_base_grava > 0) {
                        if (dou_total_factura >= 1000) {
                            Element formasDePago = doc_anexo.createElement("formasDePago");
                            detalleCompras.appendChild(formasDePago);
                            formasDePago.appendChild(crearElemento("formaPago", null, tab_compras.getValor(i, "alterno_ats")));
                        }

                        if (p_con_tipo_documento_reembolso.equals(tab_compras.getValor(i, "ide_cntdo"))) {
                            //reembolsos
                            TablaGenerica tab_reembolso = utilitario.consultar("SELECT motivo_nc_cpcfa,alter_tribu_cntdo, \n"
                                    + "substr(numero_cpcfa, 1, 3) as establecimiento,\n"
                                    + "substr(numero_cpcfa, 4, 3) as puntoemision , substr(numero_cpcfa, 7, 14) as   numero_cpcfa,\n"
                                    + "fecha_emisi_cpcfa,autorizacio_cpcfa,base_tarifa0_cpcfa,base_grabada_cpcfa,base_no_objeto_iva_cpcfa,\n"
                                    + "valor_iva_cpcfa,valor_ice_cpcfa\n"
                                    + " from cxp_cabece_factur ree\n"
                                    + "inner join con_tipo_document tdo on ree.ide_cntdo=tdo.ide_cntdo\n"
                                    + "WHERE ide_rem_cpcfa=" + tab_compras.getValor(i, "ide_cpcfa") + " and ide_cpefa=0");
                            if (tab_reembolso.getTotalFilas() > 0) {
                                String numero_retencion = tab_compras.getValor(i, "numero_cncre");
                                if (numero_retencion != null) {
                                    try {
                                        String estabRetencion1 = numero_retencion.substring(0, 3);
                                        String ptoEmiRetencion1 = numero_retencion.substring(3, 6);
                                        int secRetencion1 = Integer.parseInt(numero_retencion.substring(6, numero_retencion.length()));
                                        if (secRetencion1 > 0) {
                                            detalleCompras.appendChild(crearElemento("estabRetencion1", null, estabRetencion1));
                                            detalleCompras.appendChild(crearElemento("ptoEmiRetencion1", null, ptoEmiRetencion1));
                                            detalleCompras.appendChild(crearElemento("secRetencion1", null, secRetencion1 + ""));
                                            detalleCompras.appendChild(crearElemento("autRetencion1", null, tab_compras.getValor(i, "autorizacion_cncre")));
                                            //AQUI X SI LA FECHA DE EMISION DE LA RETE ES ANTERIOS                                    
                                            detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));
                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                Element reembolsos = doc_anexo.createElement("reembolsos");
                                detalleCompras.appendChild(reembolsos);
                                for (int r = 0; r < tab_reembolso.getTotalFilas(); r++) {
                                    aux_contador++;
                                    Element reembolso = doc_anexo.createElement("reembolso");
                                    reembolsos.appendChild(reembolso);
                                    reembolso.appendChild(crearElemento("tipoComprobanteReemb", null, tab_reembolso.getValor(r, "alter_tribu_cntdo")));
                                    reembolso.appendChild(crearElemento("tpIdProvReemb", null, "01"));//!!!!!!
                                    reembolso.appendChild(crearElemento("idProvReemb", null, tab_reembolso.getValor(r, "motivo_nc_cpcfa")));
                                    reembolso.appendChild(crearElemento("establecimientoReemb", null, tab_reembolso.getValor(r, "establecimiento")));
                                    reembolso.appendChild(crearElemento("puntoEmisionReemb", null, tab_reembolso.getValor(r, "puntoemision")));
                                    reembolso.appendChild(crearElemento("secuencialReemb", null, tab_reembolso.getValor(r, "numero_cpcfa")));
                                    reembolso.appendChild(crearElemento("fechaEmisionReemb", null, getFormatoFecha(tab_reembolso.getValor(r, "fecha_emisi_cpcfa"))));
                                    reembolso.appendChild(crearElemento("autorizacionReemb", null, tab_reembolso.getValor(r, "autorizacio_cpcfa")));
                                    reembolso.appendChild(crearElemento("baseImponibleReemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "base_tarifa0_cpcfa"))));
                                    reembolso.appendChild(crearElemento("baseImpGravReemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "base_grabada_cpcfa"))));
                                    reembolso.appendChild(crearElemento("baseNoGraIvaReemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "base_no_objeto_iva_cpcfa"))));
                                    reembolso.appendChild(crearElemento("baseImpExeReemb", null, utilitario.getFormatoNumero("0"))); /////!!!!!!!
                                    reembolso.appendChild(crearElemento("montoIceRemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "valor_ice_cpcfa"))));
                                    reembolso.appendChild(crearElemento("montoIvaRemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "valor_iva_cpcfa"))));
                                }
                            }
                        } else {
                            ///////////
                            Element air = doc_anexo.createElement("air");
                            detalleCompras.appendChild(air);
//                            TablaGenerica tab_retencion = utilitario.consultar("SELECT impuesto.casillero_cncim,sum(base_cndre) as base_cndre,porcentaje_cndre,sum(valor_cndre) as valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
//                                    + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
//                                    + "where impuesto.ide_cnimp=" + utilitario.getVariable("p_con_impuesto_renta") + " and cabece.ide_cncre=" + ide_retencion + " GROUP BY impuesto.casillero_cncim,porcentaje_cndre");

                            booEncontro = false;
                            for (int x = 0; x < tab_retencion_.getTotalFilas(); x++) {
                                if (ide_retencion.equals(tab_retencion_.getValor(x, "ide_cncre"))) {
                                    tab_retencion_.setFilaActual(x);
                                    booEncontro = true;
                                    break;
                                }
                            }
                            if (booEncontro) {
                                double dou_total = 0;
                                for (int j = 0; j < tab_retencion_.getTotalFilas(); j++) {
                                    if (ide_retencion.equals(tab_retencion_.getValor(j, "ide_cncre"))) {

                                        Element detalleAir = doc_anexo.createElement("detalleAir");
                                        air.appendChild(detalleAir);
                                        double dou_porcen = 0;
                                        double dou_base = 0;
                                        dou_total = 0;
                                        try {
                                            dou_porcen = Double.parseDouble(tab_retencion_.getValor(j, "porcentaje_cndre"));
                                            if (dou_porcen > 0) {
                                                dou_porcen = dou_porcen / 100;
                                            }
                                            dou_base = Double.parseDouble(tab_retencion_.getValor(j, "base_cndre"));
                                            //dou_total = dou_porcen * dou_base;  //Descuadraba los decimales
                                            dou_total += Double.parseDouble(tab_retencion_.getValor(j, "valor_cndre"));
                                        } catch (Exception e) {
                                        }
                                        if (tab_retencion_.getValor(j, "casillero_cncim").startsWith("332")) {
                                            detalleAir.appendChild(crearElemento("codRetAir", null, "332"));
                                        } else {
                                            detalleAir.appendChild(crearElemento("codRetAir", null, tab_retencion_.getValor(j, "casillero_cncim")));
                                        }

                                        detalleAir.appendChild(crearElemento("baseImpAir", null, utilitario.getFormatoNumero(dou_base)));
//////                                        if (dou_base != 0.00) {
//////                                            detalleAir.appendChild(crearElemento("baseImpAir", null, utilitario.getFormatoNumero(dou_base)));
//////                                        } else {
//////                                            double suma = Double.parseDouble(tab_compras.getValor(i, "base_grabada_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_tarifa0_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_no_objeto_iva_cpcfa"));
//////                                            detalleAir.appendChild(crearElemento("baseImpAir", null, utilitario.getFormatoNumero(suma)));
//////                                        }
                                        detalleAir.appendChild(crearElemento("porcentajeAir", null, tab_retencion_.getValor(j, "porcentaje_cndre")));
                                        detalleAir.appendChild(crearElemento("valRetAir", null, utilitario.getFormatoNumero(dou_total)));
                                    }
                                }
/////BORRA 332 AUTOMATICO                                
                                String numero_retencion = tab_compras.getValor(i, "numero_cncre");
                                if (numero_retencion != null) {
                                    if (tab_compras.getValor(i, "autorizacion_cncre").startsWith("000000") == false) {
                                        if (dou_total > 0) {
                                            detalleCompras.appendChild(crearElemento("estabRetencion1", null, numero_retencion.substring(0, 3)));
                                            detalleCompras.appendChild(crearElemento("ptoEmiRetencion1", null, numero_retencion.substring(3, 6)));
                                            detalleCompras.appendChild(crearElemento("secRetencion1", null, Integer.parseInt(numero_retencion.substring(6, numero_retencion.length())) + ""));
                                            detalleCompras.appendChild(crearElemento("autRetencion1", null, tab_compras.getValor(i, "autorizacion_cncre")));
                                            //AQUI X SI LA FECHA DE EMISION DE LA RETE ES ANTERIOS
                                            detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cncre"))));                                    //========================                                   
                                            //detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));
                                        }
                                    }
//////                                    else {
//////                                        para las retenciones q no se imprimen las de % 0
//////                                        detalleCompras.appendChild(crearElemento("estabRetencion1", null, "000"));
//////                                        detalleCompras.appendChild(crearElemento("ptoEmiRetencion1", null, "000"));
//////                                        detalleCompras.appendChild(crearElemento("secRetencion1", null, "000000000"));
//////                                        detalleCompras.appendChild(crearElemento("autRetencion1", null, "0000"));
//////                                        detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, "00/00/0000"));
//////                                    }
                                }

                            } else {
                                //si no hay retención
//                                Element detalleAir = doc_anexo.createElement("detalleAir");
//                                air.appendChild(detalleAir);
//                                detalleAir.appendChild(crearElemento("codRetAir", null, "332"));//OTRAS COMPRAS Y SERVICIOS NO SUJETAS A RETENCIÓN.
//                                double suma = Double.parseDouble(tab_compras.getValor(i, "base_grabada_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_tarifa0_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_no_objeto_iva_cpcfa"));
//                                detalleAir.appendChild(crearElemento("baseImpAir", null, utilitario.getFormatoNumero(suma)));
//                                detalleAir.appendChild(crearElemento("porcentajeAir", null, "0"));
//                                detalleAir.appendChild(crearElemento("valRetAir", null, "0.00"));
//                            detalleCompras.appendChild(crearElemento("estabRetencion1", null, "000"));
//                            detalleCompras.appendChild(crearElemento("ptoEmiRetencion1", null, "000"));
//                            detalleCompras.appendChild(crearElemento("secRetencion1", null, "000000000"));
//                            detalleCompras.appendChild(crearElemento("autRetencion1", null, "0000"));
//                            detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, "00/00/0000"));
                            }
                        }
//                        detalleCompras.appendChild(crearElemento("docModificado", null, "0"));
//                        detalleCompras.appendChild(crearElemento("estabModificado", null, "000"));
//                        detalleCompras.appendChild(crearElemento("ptoEmiModificado", null, "000"));
//                        detalleCompras.appendChild(crearElemento("secModificado", null, "0000000"));
//                        detalleCompras.appendChild(crearElemento("autModificado", null, "0000"));

                        //Si es nota de credito aumento estos campos     
                        if (p_con_tipo_documento_nota_credito.equals(tab_compras.getValor(i, "ide_cntdo")) || p_con_tipo_documento_nota_debito.equals(tab_compras.getValor(i, "ide_cntdo"))) {
                            //fecha_emision_nc_cpcfa,numero_nc_cpcfa,autorizacio_nc_cpcfa,motivo_nc_cpcfa
                            String numero_doc_modificado = tab_compras.getValor(i, "numero_nc_cpcfa");
                            numero_doc_modificado = numero_doc_modificado.replace("-", "");
                            String estabModificado = numero_doc_modificado.substring(0, 3);
                            String ptoEmiModificado = numero_doc_modificado.substring(3, 6);
                            String secModificado = numero_doc_modificado.substring(6, numero_doc_modificado.length());
                            detalleCompras.appendChild(crearElemento("docModificado", null, "01"));  //01= factura
                            detalleCompras.appendChild(crearElemento("estabModificado", null, estabModificado));
                            detalleCompras.appendChild(crearElemento("ptoEmiModificado", null, ptoEmiModificado));
                            detalleCompras.appendChild(crearElemento("secModificado", null, secModificado));
                            detalleCompras.appendChild(crearElemento("autModificado", null, tab_compras.getValor(i, "autorizacio_nc_cpcfa")));
                        }
                    }
                }

                if (opcionAnexo.equals("1") || opcionAnexo.equals("3")) {
                    //VENTAS

                    //Actualiza valores retencion iva y fuente en ventas
                    utilitario.getConexion().ejecutarSql("update cxc_cabece_factura cab\n"
                            + "set ret_iva_cccfa = (\n"
                            + "select sum (valor_cndre)  as retiva from con_cabece_retenc a\n"
                            + "inner join con_detall_retenc b on a.ide_cncre= b.ide_cncre\n"
                            + "where es_venta_cncre =TRUE\n"
                            + "and ide_cncim IN (SELECT ide_cncim FROM con_cabece_impues where ide_cnimp=0 )\n" //antes 1
                            + "and a.ide_cncre=cab.ide_cncre\n"
                            + "),\n"
                            + "ret_fuente_cccfa = (\n"
                            + "select sum (valor_cndre)  as retiva from con_cabece_retenc a\n"
                            + "inner join con_detall_retenc b on a.ide_cncre= b.ide_cncre\n"
                            + "where es_venta_cncre =TRUE\n"
                            + "and ide_cncim IN (SELECT ide_cncim FROM con_cabece_impues where ide_cnimp=1 )\n" //antes 0
                            + "and a.ide_cncre=cab.ide_cncre\n"
                            + ")\n"
                            + "where cab.fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal")
                    );

                    Element ventas = doc_anexo.createElement("ventas");
                    raiz.appendChild(ventas);
                    TablaGenerica tab_ventas = utilitario.consultar("select tide.alterno2_getid,cli.identificac_geper,doc.alter_tribu_cntdo,count(cab.ide_geper) as numcomprobantes, "
                            + "sum(cab.base_tarifa0_cccfa)as base_tarifa0_cccfa,sum(base_grabada_cccfa)as base_grabada, "
                            + "sum(base_no_objeto_iva_cccfa) as base_no_objeto_iva_cccfa, "
                            + "sum(valor_iva_cccfa) as valor_iva_cccfa , sum(ret_iva_cccfa) as retiva, sum(ret_fuente_cccfa) as retrenta "
                            + "from cxc_cabece_factura cab "
                            + "left join gen_persona cli on cab.ide_geper=cli.ide_geper "
                            + "left join gen_tipo_identifi tide on cli.ide_getid=tide.ide_getid "
                            + "left join con_tipo_document doc on cab.ide_cntdo=doc.ide_cntdo "
                            + "left join con_cabece_retenc rete on rete.ide_cncre=cab.ide_cncre and rete.es_venta_cncre=true "
                            + " where cab.fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal")
                            + " group by tide.alterno2_getid,cli.identificac_geper,doc.alter_tribu_cntdo");

                    //20/03/2019
                    String cedulasFacturas = tab_ventas.getStringColumna("identificac_geper");
                    cedulasFacturas = cedulasFacturas.replace("'null',", "");
                    cedulasFacturas = cedulasFacturas.replace("'null'", "");
                    cedulasFacturas = cedulasFacturas.replace("null,", "");
                    cedulasFacturas = cedulasFacturas.replace(",,", ",");
                    if (cedulasFacturas.equals("'null'") || cedulasFacturas.equals("null") || cedulasFacturas.isEmpty()) {
                        cedulasFacturas = "-1";
                    }
                    TablaGenerica tab_formaPago = utilitario.consultar("select identificac_geper,alterno_ats from cxc_cabece_factura cab "
                            + "inner join con_deta_forma_pago dp on cab.ide_cndfp = dp.ide_cndfp "
                            + "inner join gen_persona p on cab.ide_geper= p.ide_geper"
                            + " where identificac_geper in (" + cedulasFacturas + ") "
                            + " and cab.fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal")
                            + " group by alterno_ats,identificac_geper "
                            + " order by identificac_geper");
                    //                   System.out.println("FORMAS DE PAGO --- " + tab_formaPago.getSql());
                    // System.out.println("VENTAS --- " + tab_ventas.getSql());
                    for (int i = 0; i < tab_ventas.getTotalFilas(); i++) {
                        ////////////////////BUSCAR TODAS LAS VENTAS ESTO ES EN UN FOR
                        Element detalleVentas = doc_anexo.createElement("detalleVentas");
                        ventas.appendChild(detalleVentas);
                        detalleVentas.appendChild(crearElemento("tpIdCliente", null, tab_ventas.getValor(i, "alterno2_getid")));
                        detalleVentas.appendChild(crearElemento("idCliente", null, tab_ventas.getValor(i, "identificac_geper")));
                        if (!tab_ventas.getValor(i, "alterno2_getid").equals("07")) {
                            detalleVentas.appendChild(crearElemento("parteRelVtas", null, "NO"));
                        }

                        if (tab_ventas.getValor(i, "alterno2_getid").equals("06")) {//PASAPORTE
                            TablaGenerica tab_persona = utilitario.consultar("SELECT identificac_geper,nom_geper,alter_tribu_cntco from gen_persona cli "
                                    + "left join con_tipo_contribu tcon on cli.ide_cntco=tcon.ide_cntco "
                                    + "where identificac_geper='" + tab_ventas.getValor(i, "identificac_geper") + "'");
                            detalleVentas.appendChild(crearElemento("tipoCliente", null, tab_persona.getValor("alter_tribu_cntco") == null ? "01" : tab_persona.getValor("alter_tribu_cntco")));
                            detalleVentas.appendChild(crearElemento("denoCli", null, tab_persona.getValor("nom_geper")));
                        }

                        detalleVentas.appendChild(crearElemento("tipoComprobante", null, "18"));//tab_ventas.getValor(i, "alter_tribu_cntdo")
                        detalleVentas.appendChild(crearElemento("tipoEmision", null, "F"));
                        detalleVentas.appendChild(crearElemento("numeroComprobantes", null, tab_ventas.getValor(i, "numcomprobantes")));
                        detalleVentas.appendChild(crearElemento("baseNoGraIva", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_no_objeto_iva_cccfa"))));
                        detalleVentas.appendChild(crearElemento("baseImponible", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_tarifa0_cccfa"))));
                        detalleVentas.appendChild(crearElemento("baseImpGrav", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_grabada"))));
                        //double montoIva = Double.parseDouble(utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_grabada"))) * 0.12;///!!!!!!RECUPER TARIFA IVA                         
                        //detalleVentas.appendChild(crearElemento("montoIva", null, utilitario.getFormatoNumero(montoIva)));
                        detalleVentas.appendChild(crearElemento("montoIva", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "valor_iva_cccfa"))));
                        detalleVentas.appendChild(crearElemento("montoIce", null, "0.00"));
                        if (tab_ventas.getValor(i, "retiva") == null) {
                            detalleVentas.appendChild(crearElemento("valorRetIva", null, "0.00"));
                        } else {
                            detalleVentas.appendChild(crearElemento("valorRetIva", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "retiva"))));
                        }
                        if (tab_ventas.getValor(i, "retrenta") == null) {
                            detalleVentas.appendChild(crearElemento("valorRetRenta", null, "0.00"));
                        } else {
                            detalleVentas.appendChild(crearElemento("valorRetRenta", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "retrenta"))));
                        }

                        Element formasDePago = doc_anexo.createElement("formasDePago");
                        detalleVentas.appendChild(formasDePago);
                        //20-03-2019 Agrega todas las formas de pago encontradas en el mes
                        String cedulaActual = tab_ventas.getValor(i, "identificac_geper");
                        boolean encontro = false;
                        for (int f = 0; f < tab_formaPago.getTotalFilas(); f++) {
                            if (cedulaActual.equals(tab_formaPago.getValor(f, "identificac_geper"))) {
                                encontro = true;
                                formasDePago.appendChild(crearElemento("formaPago", null, tab_formaPago.getValor(f, "alterno_ats")));
                            } else {
                                if (encontro) {
                                    break;
                                }
                            }
                        }
                        if (encontro == false) {
                            formasDePago.appendChild(crearElemento("formaPago", null, "01")); // 01 --SIN UTILIZACIÓN DEL SISTEMA FINANCIERO
                        }
                    }

                    ////NOTAS DE CREDITO
                    TablaGenerica tab_notaC = utilitario.consultar("select tide.alterno2_getid,cli.identificac_geper,doc.alter_tribu_cntdo,count(cab.ide_geper) as numcomprobantes, \n"
                            + "sum(cab.base_tarifa0_cpcno)as base_tarifa0_cpcno,sum(base_grabada_cpcno)as base_grabada, \n"
                            + "sum(base_no_objeto_iva_cpcno) as base_no_objeto_iva_cpcno, \n"
                            + "sum(valor_iva_cpcno) as valor_iva_cpcno\n"
                            + "from cxp_cabecera_nota cab \n"
                            + "left join gen_persona cli on cab.ide_geper=cli.ide_geper \n"
                            + "left join gen_tipo_identifi tide on cli.ide_getid=tide.ide_getid \n"
                            + "left join con_tipo_document doc on cab.ide_cntdo=doc.ide_cntdo \n"
                            + "where cab.fecha_emisi_cpcno BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_cpeno= 1\n"
                            + " group by tide.alterno2_getid,cli.identificac_geper,doc.alter_tribu_cntdo");
                    // System.out.println("NOTAS CREDITO --- " + tab_notaC.getSql());
                    for (int i = 0; i < tab_notaC.getTotalFilas(); i++) {
                        ////////////////////BUSCAR TODAS LAS VENTAS ESTO ES EN UN FOR
                        Element detalleVentas = doc_anexo.createElement("detalleVentas");
                        ventas.appendChild(detalleVentas);
                        detalleVentas.appendChild(crearElemento("tpIdCliente", null, tab_notaC.getValor(i, "alterno2_getid")));
                        detalleVentas.appendChild(crearElemento("idCliente", null, tab_notaC.getValor(i, "identificac_geper")));
                        if (!tab_notaC.getValor(i, "alterno2_getid").equals("07")) {
                            detalleVentas.appendChild(crearElemento("parteRelVtas", null, "NO"));
                        }

                        if (tab_notaC.getValor(i, "alterno2_getid").equals("06")) {//PASAPORTE
                            TablaGenerica tab_persona = utilitario.consultar("SELECT identificac_geper,nom_geper,alter_tribu_cntco from gen_persona cli "
                                    + "left join con_tipo_contribu tcon on cli.ide_cntco=tcon.ide_cntco "
                                    + "where identificac_geper='" + tab_notaC.getValor(i, "identificac_geper") + "'");
                            detalleVentas.appendChild(crearElemento("tipoCliente", null, tab_persona.getValor("alter_tribu_cntco") == null ? "01" : tab_persona.getValor("alter_tribu_cntco")));
                            detalleVentas.appendChild(crearElemento("denoCli", null, tab_persona.getValor("nom_geper")));
                        }

                        detalleVentas.appendChild(crearElemento("tipoComprobante", null, "04"));
                        detalleVentas.appendChild(crearElemento("tipoEmision", null, "F"));
                        detalleVentas.appendChild(crearElemento("numeroComprobantes", null, tab_notaC.getValor(i, "numcomprobantes")));
                        detalleVentas.appendChild(crearElemento("baseNoGraIva", null, utilitario.getFormatoNumero(tab_notaC.getValor(i, "base_no_objeto_iva_cpcno"))));
                        detalleVentas.appendChild(crearElemento("baseImponible", null, utilitario.getFormatoNumero(tab_notaC.getValor(i, "base_tarifa0_cpcno"))));
                        detalleVentas.appendChild(crearElemento("baseImpGrav", null, utilitario.getFormatoNumero(tab_notaC.getValor(i, "base_grabada"))));
                        //double montoIva = Double.parseDouble(utilitario.getFormatoNumero(tab_notaC.getValor(i, "base_grabada"))) * 0.12;///!!!!!!RECUPER TARIFA IVA                         
                        //detalleVentas.appendChild(crearElemento("montoIva", null, utilitario.getFormatoNumero(montoIva)));
                        detalleVentas.appendChild(crearElemento("montoIva", null, utilitario.getFormatoNumero(tab_notaC.getValor(i, "valor_iva_cpcno"))));
                        detalleVentas.appendChild(crearElemento("montoIce", null, "0.00"));
                        detalleVentas.appendChild(crearElemento("valorRetIva", null, "0.00"));
                        detalleVentas.appendChild(crearElemento("valorRetRenta", null, "0.00"));
//                        Element formasDePago = doc_anexo.createElement("formasDePago");
//                        detalleVentas.appendChild(formasDePago);
//                        if (tab_notaC.getValor(i, "alterno_ats") == null) {
//                            formasDePago.appendChild(crearElemento("formaPago", null, "20"));
//                        } else {
//                            formasDePago.appendChild(crearElemento("formaPago", null, tab_notaC.getValor(i, "alterno_ats")));
//                        }

                    }

                    //ventasEstablecimiento
                    Element ventasEstablecimiento = doc_anexo.createElement("ventasEstablecimiento");
                    raiz.appendChild(ventasEstablecimiento);

                    for (int i = 0; i < tab_estab.getTotalFilas(); i++) {
                        Element ventaEst = doc_anexo.createElement("ventaEst");
                        ventasEstablecimiento.appendChild(ventaEst);
                        ventaEst.appendChild(crearElemento("codEstab", null, tab_estab.getValor(i, "establecimiento")));
                        double tv = 0;
                        double tnc = 0;
                        try {
                            tv = Double.parseDouble(utilitario.getFormatoNumero(tab_estab.getValor(i, "total_ventas")));
                        } catch (Exception e) {
                        }
                        try {
                            tnc = Double.parseDouble(utilitario.getFormatoNumero(getTotalNotasCreditoEstablecimiento(tab_estab.getValor(i, "establecimiento"))));
                        } catch (Exception e) {
                        }
                        ventaEst.appendChild(crearElemento("ventasEstab", null, utilitario.getFormatoNumero((tv - tnc))));
                    }

                }
                //FACTURAS ANULADOS
                Element anulados = doc_anexo.createElement("anulados");
                raiz.appendChild(anulados);
                TablaGenerica tab_anulados = utilitario.consultar("SELECT td.alter_tribu_cntdo, cf.secuencial_cccfa, df.autorizacion_ccdaf, "
                        + " substr(df.serie_ccdaf, 1, 3) as establecimiento, "
                        + " substr(df.serie_ccdaf, 4, 6) as puntoemision "
                        + " FROM cxc_cabece_factura cf "
                        + " INNER JOIN con_tipo_document td ON (td.ide_cntdo = cf.ide_cntdo) "
                        + " INNER JOIN cxc_datos_fac df ON (df.ide_ccdaf = cf.ide_ccdaf) "
                        + " INNER JOIN cxc_estado_factura ef ON (ef.ide_ccefa = cf.ide_ccefa) "
                        + " WHERE ef.ide_ccefa = " + utilitario.getVariable("p_cxc_estado_factura_anulada")
                        + " and cf.fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                        + " and  autorizacion_ccdaf is not null"
                        + " ORDER BY cf.secuencial_cccfa ");

                for (int i = 0; i < tab_anulados.getTotalFilas(); i++) {
                    ////////////////////BUSCAR TODAS LAS VENTAS ANULADAS ESTO ES EN UN FOR
                    Element detalleAnulados = doc_anexo.createElement("detalleAnulados");
                    anulados.appendChild(detalleAnulados);
                    detalleAnulados.appendChild(crearElemento("tipoComprobante", null, tab_anulados.getValor(i, "alter_tribu_cntdo")));
                    detalleAnulados.appendChild(crearElemento("establecimiento", null, tab_anulados.getValor(i, "establecimiento")));
                    detalleAnulados.appendChild(crearElemento("puntoEmision", null, tab_anulados.getValor(i, "puntoemision")));
                    detalleAnulados.appendChild(crearElemento("secuencialInicio", null, Integer.parseInt(tab_anulados.getValor(i, "secuencial_cccfa")) + ""));
                    detalleAnulados.appendChild(crearElemento("secuencialFin", null, Integer.parseInt(tab_anulados.getValor(i, "secuencial_cccfa")) + ""));
                    detalleAnulados.appendChild(crearElemento("autorizacion", null, tab_anulados.getValor(i, "autorizacion_ccdaf")));
                }
                //RETENCIONES ANULADAS
                TablaGenerica tab_anulados_rete = utilitario.consultar("select '07' as alter_tribu_cntdo,\n"
                        + "autorizacion_cncre\n"
                        + ",substr(numero_cncre, 1, 3) as establecimiento,\n"
                        + "substr(numero_cncre, 4, 3) as puntoemision,\n"
                        + "substr(numero_cncre, 7, 14) as secuencial\n"
                        + "from con_cabece_retenc \n"
                        + "where ide_cnere=1 "
                        + " and fecha_emisi_cncre BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'"
                        + " and es_venta_cncre=false"
                        + " order by  numero_cncre");
                for (int i = 0; i < tab_anulados_rete.getTotalFilas(); i++) {
                    ////////////////////BUSCAR TODAS LAS VENTAS ANULADAS ESTO ES EN UN FOR
                    //si es 0 no añade
                    int sec = 0;
                    try {
                        sec = Integer.parseInt(tab_anulados_rete.getValor(i, "secuencial"));
                    } catch (Exception e) {
                    }
                    if (sec == 0) {
                        continue;
                    }
                    Element detalleAnulados = doc_anexo.createElement("detalleAnulados");
                    anulados.appendChild(detalleAnulados);
                    detalleAnulados.appendChild(crearElemento("tipoComprobante", null, tab_anulados_rete.getValor(i, "alter_tribu_cntdo")));
                    detalleAnulados.appendChild(crearElemento("establecimiento", null, tab_anulados_rete.getValor(i, "establecimiento")));
                    detalleAnulados.appendChild(crearElemento("puntoEmision", null, tab_anulados_rete.getValor(i, "puntoemision")));
                    detalleAnulados.appendChild(crearElemento("secuencialInicio", null, Integer.parseInt(tab_anulados_rete.getValor(i, "secuencial")) + ""));
                    detalleAnulados.appendChild(crearElemento("secuencialFin", null, Integer.parseInt(tab_anulados_rete.getValor(i, "secuencial")) + ""));
                    detalleAnulados.appendChild(crearElemento("autorizacion", null, tab_anulados_rete.getValor(i, "autorizacion_cncre")));
                }
                ///ESCRIBE EL DOCUMENTO                
                Source source = new DOMSource(doc_anexo);
                String master = System.getProperty("user.dir");
                //String master = "D:";
                nombre = "ATS.xml";
                path = master + "/" + nombre;
                System.out.println("path " + path);
                Result console = new StreamResult(System.out);
                Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
                //Result result = new StreamResult("D:/" + nombre); //nombre del archivo
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);
                transformer.transform(source, console);
            } else {
                mensaje = "Debe ingresar la información de la empresa";
            }

        } catch (Exception e) {
            e.printStackTrace();
            utilitario.agregarMensajeError("No se pudo generar el Anexo", "No hay información para generar el anexo");
        }

        return mensaje;
    }

    private String getTotalNotasCredito() {
        String str_valor = "0.00";

        TablaGenerica tab_total = utilitario.consultar(
                "select  count(*),1*(sum(base_grabada_cpcno)+sum(base_no_objeto_iva_cpcno)+sum(base_tarifa0_cpcno)) as total_ventas \n"
                + "from cxp_cabecera_nota where fecha_emisi_cpcno BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_cpeno=1 ");
//        tab_total.imprimirSql();
        str_valor = utilitario.getFormatoNumero(tab_total.getSumaColumna("total_ventas"));

//        str_valor = "0.00";
        return str_valor;
    }

    private String getTotalNotasCreditoEstablecimiento(String codEstab) {
        String str_valor = "0.00";

        TablaGenerica tab_total = utilitario.consultar(
                "select  count(*),1*(sum(base_grabada_cpcno)+sum(base_no_objeto_iva_cpcno)+sum(base_tarifa0_cpcno)) as total_ventas \n"
                + "from cxp_cabecera_nota a "
                + " inner join cxc_datos_fac b on a.ide_ccdaf=b.ide_ccdaf  "
                + "where fecha_emisi_cpcno BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_cpeno=1 "
                + " and serie_ccdaf like '" + codEstab + "%' ");
        //tab_total.imprimirSql();
        str_valor = utilitario.getFormatoNumero(tab_total.getSumaColumna("total_ventas"));

        //  str_valor = "0.00";
        return str_valor;
    }

    private Element crearElemento(String nombre, String[] atributos, String texto) {
        Element elemento = doc_anexo.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        if (texto == null) {
            texto = "";
        }
        elemento.appendChild(doc_anexo.createTextNode(texto));
        return elemento;
    }

    private Element crearElementoCDATA(String nombre, String[] atributos, String texto) {
        Element elemento = doc_anexo.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        elemento.appendChild(doc_anexo.createCDATASection(texto));
        return elemento;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOpcionAnexo() {
        return opcionAnexo;
    }

    public void setOpcionAnexo(String opcionAnexo) {
        this.opcionAnexo = opcionAnexo;
    }

    public String getFormatoFecha(String fecha) {
        SimpleDateFormat formatoFecha1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatoFecha1.format(utilitario.getFecha(fecha));
        } catch (Exception e) {
        }
        return (String) fecha;
    }
}

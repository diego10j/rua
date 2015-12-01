/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

        try {
            TablaGenerica tab_empresa = utilitario.consultar("SELECT identificacion_empr,nom_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
            if (tab_empresa.getTotalFilas() > 0) {
                doc_anexo = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                doc_anexo.setXmlVersion("1.0");
                doc_anexo.setXmlStandalone(true);

                TablaGenerica tab_estab = utilitario.consultar("select  count(*),(sum(base_grabada_cccfa)+sum(base_no_objeto_iva_cccfa)+sum(base_tarifa0_cccfa)) as total_ventas, substr(df.serie_ccdaf, 1, 3) as establecimiento "
                        + " from cxc_cabece_factura cf "
                        + " INNER JOIN cxc_datos_fac df ON (df.ide_ccdaf = cf.ide_ccdaf)"
                        + " where fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal")
                        + " GROUP BY substr(df.serie_ccdaf, 1, 3)");


                Element raiz = doc_anexo.createElement("iva");
                raiz.appendChild(crearElemento("TipoIDInformante", null, "R")); //  Ruc
                raiz.appendChild(crearElemento("IdInformante", null, tab_empresa.getValor("identificacion_empr")));
                raiz.appendChild(crearElemento("razonSocial", null, tab_empresa.getValor("nom_empr")));
                raiz.appendChild(crearElemento("Anio", null, anio));
                raiz.appendChild(crearElemento("Mes", null, mes));
                raiz.appendChild(crearElemento("numEstabRuc", null, "00" + tab_estab.getTotalFilas()));
                raiz.appendChild(crearElemento("totalVentas", null, getTotalVentas()));
                raiz.appendChild(crearElemento("codigoOperativo", null, "IVA"));


                doc_anexo.appendChild(raiz);

                if (opcionAnexo.equals("1") || opcionAnexo.equals("2")) {
                    //COMPRAS
                    Element compras = doc_anexo.createElement("compras");
                    raiz.appendChild(compras);
                    ////////////////////BUSCAR TODAS LAS COMPRAS ESTO ES EN UN FOR
                    TablaGenerica tab_compras = utilitario.consultar("Select cabece.ide_cpcfa, cabece.ide_cncre,suste.alterno_srtst,iden.alterno1_getid,prove.identificac_geper,docu.alter_tribu_cntdo,  "
                            + " cabece.fecha_trans_cpcfa,cabece.numero_cpcfa,cabece.fecha_emisi_cpcfa,cabece.autorizacio_cpcfa,cabece.total_cpcfa,valor_ice_cpcfa, "
                            + " cabece.base_grabada_cpcfa,cabece.base_tarifa0_cpcfa,cabece.base_no_objeto_iva_cpcfa,cabece.valor_iva_cpcfa, "
                            + " rete.numero_cncre,rete.autorizacion_cncre,rete.fecha_emisi_cncre,dpa.alterno_ats "
                            + " from cxp_cabece_factur cabece "
                            + " left join gen_persona prove on cabece.ide_geper= prove.ide_geper "
                            + " left join sri_tipo_sustento_tributario suste on cabece.ide_srtst=suste.ide_srtst "
                            + " left join gen_tipo_identifi iden on prove.ide_getid=iden.ide_getid "
                            + " left join con_tipo_document docu on cabece.ide_cntdo=docu.ide_cntdo "
                            + " left join con_cabece_retenc rete on cabece.ide_cncre=rete.ide_cncre "
                            + " left join con_deta_forma_pago dpa on cabece.ide_cndfp=dpa.ide_cndfp "
                            + " where  cabece.fecha_emisi_cpcfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "'");

                    for (int i = 0; i < tab_compras.getTotalFilas(); i++) {
                        Element detalleCompras = doc_anexo.createElement("detalleCompras");
                        compras.appendChild(detalleCompras);
                        detalleCompras.appendChild(crearElemento("codSustento", null, tab_compras.getValor(i, "alterno_srtst")));
                        detalleCompras.appendChild(crearElemento("tpIdProv", null, tab_compras.getValor(i, "alterno1_getid")));
                        detalleCompras.appendChild(crearElemento("idProv", null, tab_compras.getValor(i, "identificac_geper")));
                        detalleCompras.appendChild(crearElemento("tipoComprobante", null, tab_compras.getValor(i, "alter_tribu_cntdo")));
                        detalleCompras.appendChild(crearElemento("fechaRegistro", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));
                        String numero = tab_compras.getValor(i, "numero_cpcfa");
                        detalleCompras.appendChild(crearElemento("establecimiento", null, numero.substring(0, 3)));
                        detalleCompras.appendChild(crearElemento("puntoEmision", null, numero.substring(3, 6)));
                        detalleCompras.appendChild(crearElemento("secuencial", null, numero.substring(6, numero.length())));
                        detalleCompras.appendChild(crearElemento("fechaEmision", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));
                        detalleCompras.appendChild(crearElemento("autorizacion", null, tab_compras.getValor(i, "autorizacio_cpcfa")));
                        detalleCompras.appendChild(crearElemento("baseNoGraIva", null, tab_compras.getValor(i, "base_no_objeto_iva_cpcfa")));
                        detalleCompras.appendChild(crearElemento("baseImponible", null, tab_compras.getValor(i, "base_tarifa0_cpcfa")));
                        detalleCompras.appendChild(crearElemento("baseImpGrav", null, tab_compras.getValor(i, "base_grabada_cpcfa")));
                        detalleCompras.appendChild(crearElemento("montoIce", null, tab_compras.getValor(i, "valor_ice_cpcfa")));
                        detalleCompras.appendChild(crearElemento("montoIva", null, tab_compras.getValor(i, "valor_iva_cpcfa")));

                        String ide_retencion = tab_compras.getValor(i, "ide_cncre");
                        if (ide_retencion == null) {
                            ide_retencion = "-1";
                        }

                        TablaGenerica tab_rete_iva_bienes = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                                + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                                + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva30") + " and cabece.ide_cncre=" + ide_retencion);

                        if (tab_rete_iva_bienes.getTotalFilas() > 0) {
                            detalleCompras.appendChild(crearElemento("valorRetBienes", null, tab_rete_iva_bienes.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valorRetBienes", null, "0.00"));
                        }

                        TablaGenerica tab_rete_iva_servicios = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                                + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                                + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva70") + " and cabece.ide_cncre=" + ide_retencion);
                        if (tab_rete_iva_servicios.getTotalFilas() > 0) {
                            detalleCompras.appendChild(crearElemento("valorRetServicios", null, tab_rete_iva_servicios.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valorRetServicios", null, "0.00"));
                        }

                        TablaGenerica tab_rete_iva_servicios100 = utilitario.consultar("SELECT detalle.ide_cncim,valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                                + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                                + "where impuesto.ide_cncim=" + utilitario.getVariable("p_con_impuesto_iva100") + " and cabece.ide_cncre=" + ide_retencion);


                        if (tab_rete_iva_servicios100.getTotalFilas() > 0) {
                            detalleCompras.appendChild(crearElemento("valRetServ100", null, tab_rete_iva_servicios100.getValor("valor_cndre")));
                        } else {
                            detalleCompras.appendChild(crearElemento("valRetServ100", null, "0.00"));
                        }

                        Element pagoExterior = doc_anexo.createElement("pagoExterior");
                        detalleCompras.appendChild(pagoExterior);
                        pagoExterior.appendChild(crearElemento("pagoLocExt", null, "01"));
                        pagoExterior.appendChild(crearElemento("paisEfecPago", null, "NA"));
                        pagoExterior.appendChild(crearElemento("aplicConvDobTrib", null, "NA"));
                        pagoExterior.appendChild(crearElemento("pagExtSujRetNorLeg", null, "NA"));


                        double dou_total_factura = Double.parseDouble(tab_compras.getValor(i, "total_cpcfa"));
                        if (dou_total_factura >= 1000) {
                            Element formasDePago = doc_anexo.createElement("formasDePago");
                            detalleCompras.appendChild(formasDePago);
                            formasDePago.appendChild(crearElemento("formaPago", null, tab_compras.getValor(i, "alterno_ats")));
                        }

                        //reembolsos
                        TablaGenerica tab_reembolso = utilitario.consultar("select tid.alterno1_getid,identificacion_cpdcr, substr(SERIE_CPDCR, 1, 3) as establecimiento,"
                                + "substr(SERIE_CPDCR, 4, 6) as puntoemision , secuencial_cpdcr ,"
                                + "secuencial_cpdcr,fecha_cpdcr,autorizacion_cpdcr,base_imponible_cpdcr,base_no_objeto_cpdcr,"
                                + "base_tarifa0_cpdcr,valor_iva_cpdcr,valor_ice_cpdcr, "
                                + "alter_tribu_cntdo "
                                + "from cxp_datos_com_reembolso ree "
                                + "inner JOIN gen_tipo_identifi tid on ree.ide_getid=tid.ide_getid "
                                + "inner join con_tipo_document tdo on ree.ide_cntdo=tdo.ide_cntdo "
                                + "where ide_cpcfa=" + tab_compras.getValor(i, "ide_cpcfa"));

                        if (tab_reembolso.getTotalFilas() > 0) {
                            Element reembolsos = doc_anexo.createElement("reembolsos");
                            detalleCompras.appendChild(reembolsos);
                            for (int r = 0; r < tab_reembolso.getTotalFilas(); r++) {
                                Element reembolso = doc_anexo.createElement("reembolso");
                                reembolsos.appendChild(reembolsos);
                                reembolso.appendChild(crearElemento("tipoComprobanteReemb", null, tab_reembolso.getValor(r, "alter_tribu_cntdo")));
                                reembolso.appendChild(crearElemento("tpIdProvReemb", null, tab_reembolso.getValor(r, "alterno1_getid")));
                                reembolso.appendChild(crearElemento("idProvReemb", null, tab_reembolso.getValor(r, "identificacion_cpdcr")));
                                reembolso.appendChild(crearElemento("establecimientoReemb", null, tab_reembolso.getValor(r, "establecimiento")));
                                reembolso.appendChild(crearElemento("puntoEmisionReemb", null, tab_reembolso.getValor(r, "puntoemision")));
                                reembolso.appendChild(crearElemento("secuencialReemb", null, tab_reembolso.getValor(r, "secuencial_cpdcr")));
                                reembolso.appendChild(crearElemento("fechaEmisionReemb", null, getFormatoFecha(tab_reembolso.getValor(r, "fecha_cpdcr"))));
                                reembolso.appendChild(crearElemento("autorizacionReemb", null, tab_reembolso.getValor(r, "autorizacion_cpdcr")));
                                reembolso.appendChild(crearElemento("baseImponibleReemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "base_tarifa0_cpdcr"))));
                                reembolso.appendChild(crearElemento("baseImpGravReemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "base_imponible_cpdcr"))));
                                reembolso.appendChild(crearElemento("baseNoGraIvaReemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "base_no_objeto_cpdcr"))));
                                reembolso.appendChild(crearElemento("montoIceRemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "valor_ice_cpdcr"))));
                                reembolso.appendChild(crearElemento("montoIvaRemb", null, utilitario.getFormatoNumero(tab_reembolso.getValor(r, "valor_iva_cpdcr"))));
                            }
                        }

                        ///////////

                        Element air = doc_anexo.createElement("air");
                        detalleCompras.appendChild(air);

                        TablaGenerica tab_retencion = utilitario.consultar("SELECT impuesto.casillero_cncim,sum(base_cndre) as base_cndre,porcentaje_cndre,sum(valor_cndre) as valor_cndre FROM con_cabece_retenc cabece INNER JOIN con_detall_retenc detalle on detalle.ide_cncre=cabece.ide_cncre "
                                + "INNER JOIN con_cabece_impues impuesto on  detalle.ide_cncim=impuesto.ide_cncim "
                                + "where impuesto.ide_cnimp=" + utilitario.getVariable("p_con_impuesto_renta") + " and cabece.ide_cncre=" + ide_retencion + " GROUP BY impuesto.casillero_cncim,porcentaje_cndre");

                        if (tab_retencion.getTotalFilas() > 0) {
                            for (int j = 0; j < tab_retencion.getTotalFilas(); j++) {
                                Element detalleAir = doc_anexo.createElement("detalleAir");
                                air.appendChild(detalleAir);
                                double dou_porcen = 0;
                                double dou_base = 0;
                                double dou_total = 0;
                                try {
                                    dou_porcen = Double.parseDouble(tab_retencion.getValor(j, "porcentaje_cndre"));
                                    if (dou_porcen > 0) {
                                        dou_porcen = dou_porcen / 100;
                                    }
                                    dou_base = Double.parseDouble(tab_retencion.getValor(j, "base_cndre"));
                                    dou_total = dou_porcen * dou_base;
                                } catch (Exception e) {
                                }
                                detalleAir.appendChild(crearElemento("codRetAir", null, tab_retencion.getValor(j, "casillero_cncim")));
                                if (dou_base != 0.00) {
                                    detalleAir.appendChild(crearElemento("baseImpAir", null, utilitario.getFormatoNumero(dou_base)));
                                } else {
                                    double suma = Double.parseDouble(tab_compras.getValor(i, "base_grabada_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_tarifa0_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_no_objeto_iva_cpcfa"));
                                    detalleAir.appendChild(crearElemento("baseImpAir", null, utilitario.getFormatoNumero(suma)));
                                }
                                detalleAir.appendChild(crearElemento("porcentajeAir", null, tab_retencion.getValor(j, "porcentaje_cndre")));
                                detalleAir.appendChild(crearElemento("valRetAir", null, utilitario.getFormatoNumero(dou_total)));
                            }
                            String numero_retencion = tab_compras.getValor(i, "numero_cncre");
                            if (numero_retencion != null) {
                                if (tab_compras.getValor(i, "autorizacion_cncre").startsWith("000000") == false) {

                                    detalleCompras.appendChild(crearElemento("estabRetencion1", null, numero_retencion.substring(0, 3)));
                                    detalleCompras.appendChild(crearElemento("ptoEmiRetencion1", null, numero_retencion.substring(3, 6)));
                                    detalleCompras.appendChild(crearElemento("secRetencion1", null, Integer.parseInt(numero_retencion.substring(6, numero_retencion.length())) + ""));
                                    detalleCompras.appendChild(crearElemento("autRetencion1", null, tab_compras.getValor(i, "autorizacion_cncre")));
                                    //AQUI X SI LA FECHA DE EMISION DE LA RETE ES ANTERIOS
                                    //detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cncre"))));                                    //========================                                   
                                    
                                    detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, getFormatoFecha(tab_compras.getValor(i, "fecha_emisi_cpcfa"))));
                                }
//                                else {
//                                    //para las retenciones q no se imprimen las de % 0
//                                    detalleCompras.appendChild(crearElemento("estabRetencion1", null, "000"));
//                                    detalleCompras.appendChild(crearElemento("ptoEmiRetencion1", null, "000"));
//                                    detalleCompras.appendChild(crearElemento("secRetencion1", null, "000000000"));
//                                    detalleCompras.appendChild(crearElemento("autRetencion1", null, "0000"));
//                                    detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, "00/00/0000"));
//                                }
                            }
                        } else {
                            //si no hay retención
                            Element detalleAir = doc_anexo.createElement("detalleAir");
                            air.appendChild(detalleAir);
                            detalleAir.appendChild(crearElemento("codRetAir", null, "332"));//OTRAS COMPRAS Y SERVICIOS NO SUJETAS A RETENCIÓN.
                            double suma = Double.parseDouble(tab_compras.getValor(i, "base_grabada_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_tarifa0_cpcfa")) + Double.parseDouble(tab_compras.getValor(i, "base_no_objeto_iva_cpcfa"));
                            detalleAir.appendChild(crearElemento("baseImpAir", null, utilitario.getFormatoNumero(suma)));
                            detalleAir.appendChild(crearElemento("porcentajeAir", null, "0"));
                            detalleAir.appendChild(crearElemento("valRetAir", null, "0.00"));
//                            detalleCompras.appendChild(crearElemento("estabRetencion1", null, "000"));
//                            detalleCompras.appendChild(crearElemento("ptoEmiRetencion1", null, "000"));
//                            detalleCompras.appendChild(crearElemento("secRetencion1", null, "000000000"));
//                            detalleCompras.appendChild(crearElemento("autRetencion1", null, "0000"));
//                            detalleCompras.appendChild(crearElemento("fechaEmiRet1", null, "00/00/0000"));
                        }
//                        detalleCompras.appendChild(crearElemento("docModificado", null, "0"));
//                        detalleCompras.appendChild(crearElemento("estabModificado", null, "000"));
//                        detalleCompras.appendChild(crearElemento("ptoEmiModificado", null, "000"));
//                        detalleCompras.appendChild(crearElemento("secModificado", null, "0000000"));
//                        detalleCompras.appendChild(crearElemento("autModificado", null, "0000"));
                    }
                }
                if (opcionAnexo.equals("1") || opcionAnexo.equals("3")) {
                    //VENTAS
                    Element ventas = doc_anexo.createElement("ventas");
                    raiz.appendChild(ventas);
                    TablaGenerica tab_ventas = utilitario.consultar("select tide.alterno2_getid,cli.identificac_geper,doc.alter_tribu_cntdo,count(cab.ide_geper) as numcomprobantes, "
                            + "sum(cab.base_tarifa0_cccfa)as base_tarifa0_cccfa,sum(base_grabada_cccfa)as base_grabada, "
                            + "sum(base_no_objeto_iva_cccfa) as base_no_objeto_iva_cccfa, "
                            + "sum(valor_iva_cccfa) as valor_iva_cccfa , sum(dret1.valor_cndre) as retiva, sum(dret2.valor_cndre) as retrenta "
                            + "from cxc_cabece_factura cab "
                            + "left join gen_persona cli on cab.ide_geper=cli.ide_geper "
                            + "left join gen_tipo_identifi tide on cli.ide_getid=tide.ide_getid "
                            + "left join con_tipo_document doc on cab.ide_cntdo=doc.ide_cntdo "
                            + "left join con_cabece_retenc rete on rete.ide_cncre=cab.ide_cncre and rete.es_venta_cncre=true "
                            + "left join con_detall_retenc dret1 on rete.ide_cncre= dret1.ide_cncre  and dret1.ide_cncim=1 "
                            + "left join con_detall_retenc dret2 on rete.ide_cncre= dret2.ide_cncre  and dret1.ide_cncim=0 "
                            + " where cab.fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal")
                            + " group by tide.alterno2_getid,cli.identificac_geper,doc.alter_tribu_cntdo");

                    for (int i = 0; i < tab_ventas.getTotalFilas(); i++) {
                        ////////////////////BUSCAR TODAS LAS VENTAS ESTO ES EN UN FOR
                        Element detalleVentas = doc_anexo.createElement("detalleVentas");
                        ventas.appendChild(detalleVentas);
                        detalleVentas.appendChild(crearElemento("tpIdCliente", null, tab_ventas.getValor(i, "alterno2_getid")));
                        detalleVentas.appendChild(crearElemento("idCliente", null, tab_ventas.getValor(i, "identificac_geper")));
                        detalleVentas.appendChild(crearElemento("tipoComprobante", null, "18"));//tab_ventas.getValor(i, "alter_tribu_cntdo")
                        detalleVentas.appendChild(crearElemento("numeroComprobantes", null, tab_ventas.getValor(i, "numcomprobantes")));
                        detalleVentas.appendChild(crearElemento("baseNoGraIva", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_no_objeto_iva_cccfa"))));
                        detalleVentas.appendChild(crearElemento("baseImponible", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_tarifa0_cccfa"))));
                        detalleVentas.appendChild(crearElemento("baseImpGrav", null, utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_grabada"))));
                        double montoIva = Double.parseDouble(utilitario.getFormatoNumero(tab_ventas.getValor(i, "base_grabada"))) * 0.12;
                        detalleVentas.appendChild(crearElemento("montoIva", null, utilitario.getFormatoNumero(montoIva)));
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
                    }

                    //ventasEstablecimiento




                    Element ventasEstablecimiento = doc_anexo.createElement("ventasEstablecimiento");
                    raiz.appendChild(ventasEstablecimiento);

                    for (int i = 0; i < tab_estab.getTotalFilas(); i++) {
                        Element ventaEst = doc_anexo.createElement("ventaEst");
                        ventasEstablecimiento.appendChild(ventaEst);
                        ventaEst.appendChild(crearElemento("codEstab", null, tab_estab.getValor(i, "establecimiento")));
                        ventaEst.appendChild(crearElemento("ventasEstab", null, utilitario.getFormatoNumero(tab_estab.getValor(i, "total_ventas"))));
                    }

                }
                //ANULADOS
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

                ///ESCRIBE EL DOCUMENTO
                Source source = new DOMSource(doc_anexo);
                String master = System.getProperty("user.dir");
                nombre = "AT" + mes + anio + ".xml";
                Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
                path = master + "/" + nombre;
                Result console = new StreamResult(System.out);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);
                transformer.transform(source, console);
            } else {
                mensaje = "Debe ingresar la información de la empresa";
            }

        } catch (Exception e) {
            utilitario.agregarMensajeError("No se pudo generar el Anexo", "No hay información para generar el anexo");
        }

        return mensaje;
    }

    private String getTotalVentas() {
        String str_valor = "0.00";

        TablaGenerica tab_total = utilitario.consultar("select  count(*),(sum(base_grabada_cccfa)+sum(base_no_objeto_iva_cccfa)+sum(base_tarifa0_cccfa)) as total_ventas "
                + " from cxc_cabece_factura where fecha_emisi_cccfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal"));

        if (tab_total.getTotalFilas() > 0) {
            str_valor = utilitario.getFormatoNumero(tab_total.getValor("total_ventas"));
        }
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

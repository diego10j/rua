/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_cobrar;

import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless
public class ServicioCuentasCxC extends ServicioBase {

    @PostConstruct
    public void init() {
        //Recupera todos los parametros que se van a ocupar en el EJB  
        parametros = utilitario.getVariables("p_cxc_estado_factura_normal", "p_cxc_tipo_trans_factura", "p_cxc_tipo_trans_pago", "p_con_tipo_documento_factura", "p_con_tipo_documento_nota_credito", "p_cxc_tipo_trans_retencion", "p_con_estado_comprobante_anulado");
    }

    /**
     * Retorna la sentencia SQL con los puntos de emision de facturas x empresa
     *
     * @return
     */
    public String getSqlPuntosEmisionFacturas() {
        return "select ide_ccdaf,serie_ccdaf, coalesce(autorizacion_ccdaf,''),observacion_ccdaf from cxc_datos_fac where ide_cntdoc=" + parametros.get("p_con_tipo_documento_factura") + " and  ide_sucu=" + utilitario.getVariable("IDE_SUCU");
    }

    public String getSqlPuntosEmisionNotasCredito() {
        return "select ide_ccdaf,serie_ccdaf, coalesce(autorizacion_ccdaf,''),observacion_ccdaf from cxc_datos_fac where ide_cntdoc= " + parametros.get("p_con_tipo_documento_nota_credito") + " and  ide_sucu=" + utilitario.getVariable("IDE_SUCU");
    }

    /**
     * Registra la Factura en una transaccion cxp
     *
     * @param ide_cccfa
     * @param valorRetencion
     * @return ide_cpctr Cabecera de la Transaccion CxP
     */
    public String generarModificarTransaccionRetencion(String ide_cccfa, double valorRetencion) {
        String ide_cpctr = "-1";
        TablaGenerica tab_cab_factura = utilitario.consultar("SELECT * FROM cxc_cabece_factura WHERE ide_cccfa=" + ide_cccfa);
        if (tab_cab_factura.isEmpty() == false) {
            TablaGenerica tab_cab_tran_cxp = new TablaGenerica();
            tab_cab_tran_cxp.setTabla("cxc_cabece_transa", "ide_ccctr");
            tab_cab_tran_cxp.setCondicion("ide_cccfa=" + ide_cccfa);
            tab_cab_tran_cxp.ejecutarSql();
            TablaGenerica tab_det_tran_cxp = new TablaGenerica();
            tab_det_tran_cxp.setTabla("cxc_detall_transa", "ide_ccdtr");
            tab_det_tran_cxp.getColumna("ide_ccdtr").setExterna(false);
            tab_det_tran_cxp.setCondicion("ide_ccctr=" + tab_cab_tran_cxp.getValor("ide_ccctr") + " and ide_ccttr=" + parametros.get("p_cxc_tipo_trans_retencion"));
            tab_det_tran_cxp.ejecutarSql();
            if (tab_det_tran_cxp.isEmpty()) {
                tab_det_tran_cxp.insertar();
            } else {
                tab_det_tran_cxp.modificar(tab_det_tran_cxp.getFilaActual());
            }
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_ccctr", tab_cab_tran_cxp.getValor("ide_ccctr"));
            tab_det_tran_cxp.setValor("ide_cccfa", ide_cccfa);
            tab_det_tran_cxp.setValor("ide_ccttr", parametros.get("p_cxc_tipo_trans_retencion"));
            tab_det_tran_cxp.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_det_tran_cxp.setValor("valor_ccdtr", utilitario.getFormatoNumero(valorRetencion));
            tab_det_tran_cxp.setValor("observacion_ccdtr", tab_cab_factura.getValor("observacion_cccfa"));
            tab_det_tran_cxp.setValor("numero_pago_ccdtr", "0");
            tab_det_tran_cxp.setValor("docum_relac_ccdtr", tab_cab_factura.getValor("secuencial_cccfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));

            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_ccctr");
        }
        return ide_cpctr;
    }

    public boolean isPuntoEmisionElectronico(String ide_ccdaf) {
        TablaGenerica tag = utilitario.consultar("select ide_ccdaf,es_electronica_ccdaf from cxc_datos_fac where ide_ccdaf=" + ide_ccdaf);
        if (tag.isEmpty() == false) {
            if (tag.getValor("es_electronica_ccdaf").equalsIgnoreCase("true")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna el secuencial maximo de un punto de emisión
     *
     * @param ide_ccdaf
     * @return
     */
    public int getSecuencialFactura(String ide_ccdaf) {
        int max = 0;
        TablaGenerica tab_secuencia = utilitario.consultar("select ide_ccdaf,max(CAST(coalesce(secuencial_cccfa, '0') AS Integer)) as num_max FROM cxc_cabece_factura where ide_ccdaf=" + ide_ccdaf + " GROUP BY ide_ccdaf ");
        if (tab_secuencia.isEmpty() == false) {
            try {
                max = Integer.parseInt(tab_secuencia.getValor("num_max"));
            } catch (Exception e) {
            }
        }
        max++;
        return max;
    }

    public int getSecuencialGuiaRemision(String ide_ccdaf) {
        int max = 0;
        TablaGenerica tab_secuencia = utilitario.consultar("select ide_ccdaf,max(CAST(coalesce(numero_ccgui, '0') AS Integer)) as num_max FROM cxc_guia where ide_ccdaf=" + ide_ccdaf + " GROUP BY ide_ccdaf ");
        if (tab_secuencia.isEmpty() == false) {
            try {
                max = Integer.parseInt(tab_secuencia.getValor("num_max"));
            } catch (Exception e) {
            }
        }
        max++;
        return max;
    }

    /**
     * Retorna todas las facturas generadas en un punto de emisión x sucursal
     *
     * @param ide_ccdaf Punto de Emisión
     * @param fechaInicio Fecha desde
     * @param fechaFin Fecha hasta
     * @return
     */
    public String getSqlFacturas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        if (isFacturaElectronica(ide_ccdaf)) { //si tiene facturacion electrónica
            return "select a.ide_cccfa, secuencial_cccfa,ide_cnccc,a.ide_ccefa,nombre_sresc as nombre_ccefa, fecha_emisi_cccfa,(select numero_cncre from con_cabece_retenc where ide_cncre=a.ide_cncre)as NUM_RETENCION,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                    + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,descuento_cccfa as DESCUENTO,valor_iva_cccfa,total_cccfa, "
                    + "claveacceso_srcom as CLAVE_ACCESO, fecha_trans_cccfa,a.ide_cncre,d.ide_srcom,a.ide_geper,direccion_cccfa,orden_compra_cccfa AS NUM_REFERENCIA  "
                    + "from cxc_cabece_factura a "
                    + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                    + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                    + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "                    
                    + "left join con_deta_forma_pago x on a.ide_cndfp1=x.ide_cndfp "
                    + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                    + "and ide_ccdaf=" + ide_ccdaf + " "
                    // + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " " 
                    + "AND a.ide_ccefa =" + parametros.get("p_cxc_estado_factura_normal")
                    + " ORDER BY secuencial_cccfa desc,ide_cccfa desc";
        } else {
            return "select a.ide_cccfa, secuencial_cccfa, ide_cnccc,a.ide_ccefa,nombre_ccefa ,fecha_emisi_cccfa,(select numero_cncre from con_cabece_retenc where ide_cncre=a.ide_cncre)as NUM_RETENCION,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                    + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, "
                    + "observacion_cccfa, nombre_vgven as VENDEDOR,fecha_trans_cccfa,a.ide_cncre,a.ide_geper,direccion_cccfa "
                    + "from cxc_cabece_factura a "
                    + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                    + "inner join cxc_estado_factura c on  a.ide_ccefa=c.ide_ccefa "
                    + "left join ven_vendedor v on a.ide_vgven=v.ide_vgven "
                    + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                    + "and ide_ccdaf=" + ide_ccdaf + " "
                    // + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                    + "AND a.ide_ccefa =" + parametros.get("p_cxc_estado_factura_normal")
                    + " ORDER BY secuencial_cccfa desc,ide_cccfa desc";
        }
    }

    public String getSqlFacturasElectronicasPorEstado(String ide_ccdaf, String fechaInicio, String fechaFin, EstadoComprobanteEnum estado) {
        return "select a.ide_cccfa, secuencial_cccfa,ide_cnccc,a.ide_ccefa,nombre_sresc as nombre_ccefa, fecha_emisi_cccfa,(select numero_cncre from con_cabece_retenc where ide_cncre=a.ide_cncre)as NUM_RETENCION,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,descuento_cccfa as DESCUENTO,valor_iva_cccfa,total_cccfa, "
                + "claveacceso_srcom as CLAVE_ACCESO, fecha_trans_cccfa,ide_cncre,d.ide_srcom,a.ide_geper,direccion_cccfa,orden_compra_cccfa AS NUM_REFERENCIA "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf + " "
                + " and d.ide_sresc =" + estado.getCodigo() + " "
                + "ORDER BY secuencial_cccfa desc,ide_cccfa desc";
    }

    /**
     * Retorna las facturas anuladas de un punto de emsión en un rango de fechas
     *
     * @param ide_ccdaf
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlFacturasAnuladas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        return "select a.ide_cccfa, secuencial_cccfa, ide_cnccc,fecha_emisi_cccfa,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, "
                + "observacion_cccfa, fecha_trans_cccfa "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf + " "
                + "AND a.ide_ccefa !=" + parametros.get("p_cxc_estado_factura_normal")
                //    + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "ORDER BY secuencial_cccfa desc,ide_cccfa desc";
    }

    /**
     * Retorna las facturas no contabilizadas de un punto de emsión en un rango
     * de fechas
     *
     * @param ide_ccdaf
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlFacturasNoContabilizadas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        return "select a.ide_cccfa, secuencial_cccfa,fecha_emisi_cccfa,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, "
                + "observacion_cccfa, fecha_trans_cccfa,a.ide_geper "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf + " "
                + "AND a.ide_ccefa =" + parametros.get("p_cxc_estado_factura_normal")
                + " and a.ide_cnccc is null "
                //    + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "ORDER BY secuencial_cccfa desc,ide_cccfa desc";
    }

    /**
     * Retorna las facturas por cobrar de un punto de emsión en un rango de
     * fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlFacturasPorCobrar(String fechaInicio, String fechaFin) {
        return "select  cf.ide_cccfa, secuencial_cccfa,dias_credito_cccfa as dias_credito,fecha_emisi_cccfa,nom_geper,telefono_geper,\n"
                + "to_char(fecha_emisi_cccfa + CAST(dias_credito_cccfa||' days' AS INTERVAL),'YYYY-MM-DD')as FECHA_VENCE,\n"
                + "case when extract(days from ( now()- (fecha_emisi_cccfa+CAST(dias_credito_cccfa||' days' AS INTERVAL)))) > 0 then extract(days from ( now()- (fecha_emisi_cccfa+CAST(dias_credito_cccfa||' days' AS INTERVAL))))  end as DIAS_VENCIDOS, \n"
                + "base_grabada_cccfa as ventas12, base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, \n"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar, observacion_cccfa "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr  "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "left join gen_persona b on cf.ide_geper=b.ide_geper "
                + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and cf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY cf.ide_cccfa,nom_geper,telefono_geper "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY 7,6";
    }

    /**
     * Retorna los pagos realizados a una Factura
     *
     * @param ide_cccfa
     * @return
     */
    public String getSqlPagosFactura(String ide_cccfa) {
        return "SELECT ide_ccdtr,fecha_trans_ccdtr,docum_relac_ccdtr,nombre_tettb ,valor_ccdtr, nombre_tecba ||' '||nombre_teban as DESTINO,observacion_ccdtr as OBSERVACION,c.ide_tecba  "
                + "FROM cxc_detall_transa a "
                + "left JOIN  cxc_tipo_transacc b on a.IDE_CCTTR =b.IDE_CCTTR "
                + "left join  tes_cab_libr_banc c on a.ide_teclb=c.ide_teclb "
                + "left join tes_cuenta_banco  d on c.ide_tecba=d.ide_tecba "
                + "left join tes_banco e on d.ide_teban=e.ide_teban "
                + "left join tes_tip_tran_banc f on c.ide_tettb=f.ide_tettb "
                + "where numero_pago_ccdtr > 0 "
                + "and ide_cccfa=" + ide_cccfa + " "
                + "order by fecha_trans_ccdtr";
    }

    /**
     * Registra la Factura en una transaccion cxc
     *
     * @param tab_cab_factura
     * @return ide_ccctr Cabecera de la Transaccion CxC
     */
    public String generarTransaccionFactura(Tabla tab_cab_factura) {
        String ide_ccctr = "-1";
        if (tab_cab_factura != null) {
            TablaGenerica tab_cab_tran_cxc = new TablaGenerica();
            String str_p_cxc_tipo_trans_pago = parametros.get("p_cxc_tipo_trans_pago");
            TablaGenerica tab_det_tran_cxc = new TablaGenerica();
            tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
            tab_cab_tran_cxc.getColumna("ide_ccctr").setExterna(false);
            //SI EXISTE UN PAGO ADICIONAL OCUPA ES TRANSACCIÓN
            tab_cab_tran_cxc.setCondicion("ide_ccttr=" + str_p_cxc_tipo_trans_pago + " AND ide_cccfa is null and ide_geper=" + tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxc.ejecutarSql();
            String str_p_cxc_tipo_trans_factura = parametros.get("p_cxc_tipo_trans_factura");//Tipo transaccion Factura 
            if (tab_cab_tran_cxc.isEmpty()) {
                tab_cab_tran_cxc.insertar();
                tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
                tab_cab_tran_cxc.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
                tab_cab_tran_cxc.setValor("fecha_trans_ccctr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
                tab_cab_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
                tab_cab_tran_cxc.setValor("observacion_ccctr", "V/. FACTURA " + tab_cab_factura.getValor("secuencial_cccfa") + " ");
            } else {
                tab_cab_tran_cxc.modificar(tab_cab_tran_cxc.getFilaActual());
                tab_cab_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
                utilitario.getConexion().agregarSql("UPDATE cxc_detall_transa set ide_cccfa=" + tab_cab_factura.getValor("ide_cccfa") + " where ide_ccctr=" + tab_cab_tran_cxc.getValor("ide_ccctr"));
            }

            tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
            tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
            tab_det_tran_cxc.setCondicion("ide_ccctr=-1");
            tab_det_tran_cxc.ejecutarSql();

            tab_cab_tran_cxc.guardar();
            tab_det_tran_cxc.insertar();
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_det_tran_cxc.setValor("valor_ccdtr", tab_cab_factura.getValor("total_cccfa"));
            tab_det_tran_cxc.setValor("observacion_ccdtr", "V/. FACTURA " + tab_cab_factura.getValor("secuencial_cccfa") + " ");
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", "0");
            int dias_credito = 0;
            try {
                dias_credito = Integer.parseInt(tab_cab_factura.getValor("dias_credito_cccfa"));
            } catch (Exception e) {
            }
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cccfa")), dias_credito)));
            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_factura.getValor("secuencial_cccfa"));
            tab_det_tran_cxc.guardar();
            ide_ccctr = tab_cab_tran_cxc.getValor("ide_ccctr");

        }
        return ide_ccctr;
    }

    /**
     * Registra la Nota de Crédito de una Factrua
     *
     *
     * @param ide_cccfa
     * @param tab_cab_nota
     * @return ide_ccctr Cabecera de la Transaccion CxC
     */
    public String generarTransaccionNotaCredito(String ide_cccfa, Tabla tab_cab_nota) {

        TablaGenerica tab_cab_tran_cxc = new TablaGenerica();
        TablaGenerica tab_det_tran_cxc = new TablaGenerica();
        tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
        tab_cab_tran_cxc.getColumna("ide_ccctr").setExterna(false);
        tab_cab_tran_cxc.setCondicion("ide_cccfa=" + ide_cccfa);
        tab_cab_tran_cxc.ejecutarSql();
        String str_p_cxc_tipo_trans_nota = "1"; //****NOTA DE CREDITO
        if (tab_cab_tran_cxc.isEmpty()) {
            tab_cab_tran_cxc.insertar();
            tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_nota);
            tab_cab_tran_cxc.setValor("ide_geper", tab_cab_nota.getValor("ide_geper"));
            tab_cab_tran_cxc.setValor("fecha_trans_ccctr", tab_cab_nota.getValor("fecha_emisi_cpcno"));
            if (ide_cccfa != null) {
                tab_cab_tran_cxc.setValor("ide_cccfa", ide_cccfa);
            }
            tab_cab_tran_cxc.setValor("observacion_ccctr", "V/. NOTA DE CREDITO FAC. " + tab_cab_nota.getValor("num_doc_mod_cpcno") + " ");
            tab_cab_tran_cxc.guardar();
        }
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccctr=-1");
        tab_det_tran_cxc.ejecutarSql();

        tab_det_tran_cxc.limpiar();

        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
        tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
        if (ide_cccfa != null) {
            tab_det_tran_cxc.setValor("ide_cccfa", ide_cccfa);
        }
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_nota);
        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_nota.getValor("fecha_emisi_cpcno"));
        tab_det_tran_cxc.setValor("valor_ccdtr", tab_cab_nota.getValor("total_cpcno"));
        tab_det_tran_cxc.setValor("observacion_ccdtr", "V/. NOTA DE CREDITO FAC. " + tab_cab_nota.getValor("num_doc_mod_cpcno") + " ");
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", "0");
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_nota.getValor("numero_cpcno"));
        tab_det_tran_cxc.guardar();
        return tab_cab_tran_cxc.getValor("ide_ccctr");
    }

    /**
     * Registra una transaccion cxc de comisón por cheque devuelto
     *
     * @param ide_geper
     * @param valor
     * @param fecha
     * @param observacion
     * @param numero
     * @param ide_teclb
     * @return ide_ccctr Cabecera de la Transaccion CxC
     */
    public String generarTransaccionComisionCheDev(String ide_geper, String valor, String fecha, String observacion, String numero, String ide_teclb) {
        String ide_ccctr = "-1";
        TablaGenerica tab_cab_tran_cxc = new TablaGenerica();
        TablaGenerica tab_det_tran_cxc = new TablaGenerica();
        tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
        tab_cab_tran_cxc.getColumna("ide_ccctr").setExterna(false);
        tab_cab_tran_cxc.setCondicion("ide_ccctr=-1");
        tab_cab_tran_cxc.ejecutarSql();
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccdtr=" + ide_ccctr);
        tab_det_tran_cxc.ejecutarSql();
        String str_p_cxc_tipo_trans_checdev = "17";//Tipo transaccion comision cheque devuelto 

        tab_cab_tran_cxc.insertar();

        tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_checdev);
        tab_cab_tran_cxc.setValor("ide_geper", ide_geper);
        tab_cab_tran_cxc.setValor("fecha_trans_ccctr", fecha);

        tab_cab_tran_cxc.setValor("observacion_ccctr", observacion);
        tab_cab_tran_cxc.guardar();
        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
        tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_checdev);
        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", fecha);
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", fecha);
        tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", "0");
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", fecha);
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", numero);
        tab_det_tran_cxc.setValor("ide_teclb", ide_teclb);
        tab_det_tran_cxc.guardar();
        ide_ccctr = tab_cab_tran_cxc.getValor("ide_ccctr");

        return ide_ccctr;
    }

    public String generarModificarTransaccionFactura(Tabla tab_cab_factura) {
        String ide_ccctr = "-1";
        TablaGenerica tab_cab_tran_cxc = new TablaGenerica();
        TablaGenerica tab_det_tran_cxc = new TablaGenerica();
        tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
        tab_cab_tran_cxc.getColumna("ide_ccctr").setExterna(false);
        tab_cab_tran_cxc.setCondicion("ide_cccfa=" + tab_cab_factura.getValor("ide_cccfa"));
        tab_cab_tran_cxc.ejecutarSql();
        ide_ccctr = tab_cab_tran_cxc.getValor("ide_ccctr");
        if (ide_ccctr == null || ide_ccctr.isEmpty()) {
            ide_ccctr = "-1";
        }
        String str_p_cxc_tipo_trans_factura = parametros.get("p_cxc_tipo_trans_factura");//Tipo transaccion Factura     
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccctr=" + ide_ccctr + " and ide_ccttr=" + str_p_cxc_tipo_trans_factura);
        tab_det_tran_cxc.ejecutarSql();

        if (tab_cab_tran_cxc.isEmpty()) {
            tab_cab_tran_cxc.insertar();
        } else {
            tab_cab_tran_cxc.modificar(tab_cab_tran_cxc.getFilaActual());
        }

        if (tab_cab_factura != null) {
            tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_cab_tran_cxc.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxc.setValor("fecha_trans_ccctr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_cab_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_cab_tran_cxc.setValor("observacion_ccctr", "V/. FACTURA " + tab_cab_factura.getValor("secuencial_cccfa") + " ");
            tab_cab_tran_cxc.guardar();
            if (tab_det_tran_cxc.isEmpty()) {
                tab_det_tran_cxc.insertar();
            } else {
                tab_det_tran_cxc.modificar(tab_det_tran_cxc.getFilaActual());
            }

            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_det_tran_cxc.setValor("valor_ccdtr", tab_cab_factura.getValor("total_cccfa"));
            tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_tran_cxc.getValor("observacion_ccctr"));
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", "0");
            int dias_credito = 0;
            try {
                dias_credito = Integer.parseInt(tab_cab_factura.getValor("dias_credito_cccfa"));
            } catch (Exception e) {
            }
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cccfa")), dias_credito)));

            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_factura.getValor("secuencial_cccfa"));
            tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxc.guardar();
            ide_ccctr = tab_cab_tran_cxc.getValor("ide_ccctr");
        }
        return ide_ccctr;
    }

    /**
     * Genera la transaccion cxc de un pago registrado en tesoreria de una
     * factura cxc
     *
     * @param tab_cab_factura Cebecera Factura
     * @param ide_ccctr Cabecera Transaccion cxc
     * @param ide_teclb Cabecera Libro Bancos
     * @param valor valor del pago
     * @param observacion observacion
     * @param num_documento num documento relacionado
     */
    public void generarTransaccionPago(Tabla tab_cab_factura, String ide_ccctr, String ide_teclb, double valor, String observacion, String num_documento) {
        TablaGenerica tab_det_tran_cxc = new TablaGenerica();
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccdtr=-1");
        tab_det_tran_cxc.ejecutarSql();
        String str_p_cxc_tipo_trans_pago = parametros.get("p_cxc_tipo_trans_pago");

        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_teclb", ide_teclb);
        tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
        tab_det_tran_cxc.setValor("ide_ccctr", ide_ccctr);

        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
        tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", getNumeroPagoFactura(ide_ccctr) + "");
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
        if (num_documento == null || num_documento.isEmpty()) {
            num_documento = tab_cab_factura.getValor("secuencial_cccfa");
        }
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", num_documento);
        tab_det_tran_cxc.guardar();
    }

    public void generarTransaccionPago(TablaGenerica tab_cab_factura, String ide_ccctr, String ide_teclb, double valor, String observacion, String num_documento, String ide_tettb, String fecha_chequepos, String fecha_movimiento) {

        TablaGenerica tab_det_tran_cxc = new TablaGenerica();
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccdtr=-1");
        tab_det_tran_cxc.ejecutarSql();
        String str_p_cxc_tipo_trans_pago = parametros.get("p_cxc_tipo_trans_pago");
        String fecha_vence_pago = utilitario.getFechaActual();
        if (ide_tettb != null && ide_tettb.equals("13")) {
            str_p_cxc_tipo_trans_pago = "10";//CHEQUE POSTFECHADO
            fecha_vence_pago = fecha_chequepos;
        }

        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_teclb", ide_teclb);
        tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
        tab_det_tran_cxc.setValor("ide_ccctr", ide_ccctr);

        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", fecha_movimiento);
        tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", getNumeroPagoFactura(ide_ccctr) + "");
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", fecha_vence_pago);

        if (num_documento == null || num_documento.isEmpty()) {
            num_documento = tab_cab_factura.getValor("secuencial_cccfa");
        }
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", num_documento);

        tab_det_tran_cxc.guardar();
    }

    /**
     * Cuando realizan un pago a dicional y no hat factura , queda el saldo a
     * favor para una proxima venta
     *
     * @param ide_geper
     * @param ide_teclb
     * @param valor
     * @param ide_tettb
     * @param fecha_chequepos
     * @param fecha_movimiento
     */
    public void generarTransaccionPagoAdicionalCxC(String ide_geper, String ide_teclb, double valor, String ide_tettb, String fecha_chequepos, String fecha_movimiento) {

        TablaGenerica tab_cab_tran_cxc = new TablaGenerica();
        String str_p_cxc_tipo_trans_pago = parametros.get("p_cxc_tipo_trans_pago");

        tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
        tab_cab_tran_cxc.getColumna("ide_ccctr").setExterna(false);
        //tab_cab_tran_cxc.setCondicion("ide_ccctr= -1");        
        tab_cab_tran_cxc.setCondicion("ide_ccttr=" + str_p_cxc_tipo_trans_pago + " AND ide_cccfa is null and ide_geper=" + ide_geper);
        tab_cab_tran_cxc.ejecutarSql();
        if (tab_cab_tran_cxc.isEmpty() == false) {
            tab_cab_tran_cxc.insertar();
            tab_cab_tran_cxc.setValor("ide_geper", ide_geper);
            tab_cab_tran_cxc.setValor("fecha_trans_ccctr", fecha_movimiento);
            tab_cab_tran_cxc.setValor("observacion_ccctr", "V/. SALDO A FAVOR PAGO ADICIONAL ");
            tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
            tab_cab_tran_cxc.guardar();
        }

        String ide_ccctr = tab_cab_tran_cxc.getValor("ide_ccctr");
        TablaGenerica tab_det_tran_cxc = new TablaGenerica();
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccdtr=-1");
        tab_det_tran_cxc.ejecutarSql();

        String fecha_vence_pago = utilitario.getFechaActual();
        if (ide_tettb != null && ide_tettb.equals("13")) {
            str_p_cxc_tipo_trans_pago = "10";//CHEQUE POSTFECHADO
            fecha_vence_pago = fecha_chequepos;
        }
        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_teclb", ide_teclb);
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
        tab_det_tran_cxc.setValor("ide_ccctr", ide_ccctr);
        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", fecha_movimiento);
        tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxc.setValor("observacion_ccdtr", "V/. SALDO A FAVOR PAGO ADICIONAL ");
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", "1");
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", fecha_vence_pago);
        tab_det_tran_cxc.guardar();
    }

    public void generarTransaccionPagoAdicionalContable(TablaGenerica tab_cab_factura, String ide_ccctr, String ide_teclb, double valor, String observacion, String num_documento, String ide_ccttr, String fecha_movimiento) {

        TablaGenerica tab_det_tran_cxc = new TablaGenerica();
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccdtr=-1");
        tab_det_tran_cxc.ejecutarSql();

        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_teclb", ide_teclb);
        tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
        String str_p_cxc_tipo_trans_pago = parametros.get("p_cxc_tipo_trans_pago");
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
        tab_det_tran_cxc.setValor("ide_ccctr", ide_ccctr);

        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", fecha_movimiento);
        tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", getNumeroPagoFactura(ide_ccctr) + "");
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());

        if (num_documento == null || num_documento.isEmpty()) {
            num_documento = tab_cab_factura.getValor("secuencial_cccfa");
        }
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", num_documento);

        tab_det_tran_cxc.guardar();
    }

    public int getNumeroPagoFactura(String ide_ccctr) {
        //RETORNA EL PAGO MAXIMO         
        List lis_sql = utilitario.getConexion().consultar("select max(numero_pago_ccdtr) from cxc_detall_transa where ide_ccctr=" + ide_ccctr);
        int num = 0;
        if (lis_sql.isEmpty() == false && lis_sql.get(0) != null) {
            try {
                num = Integer.parseInt(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return num + 1;
    }

    /**
     * Sql de total de ventas por mes de una sucrsal y por vendedor
     *
     * @param anio
     * @param ide_vgven
     * @return
     */
    public String getSqlTotalVentasMensuales(String anio, String ide_vgven) {
        if (ide_vgven == null) {
            ide_vgven = "";
        }
        if (ide_vgven.equalsIgnoreCase("null")) {
            ide_vgven = "";
        }

        String condicion_vendedor = " and a.ide_sucu= " + utilitario.getVariable("IDE_SUCU") + " ";
        if (ide_vgven.isEmpty() == false) {
            condicion_vendedor += " and ide_vgven=" + ide_vgven + " ";
        }
        return "select nombre_gemes,"
                + "(select count(ide_cccfa) as num_facturas from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") " + condicion_vendedor + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_grabada_cccfa) as ventas12 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") " + condicion_vendedor + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_tarifa0_cccfa+base_no_objeto_iva_cccfa) as ventas0 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") " + condicion_vendedor + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(valor_iva_cccfa) as iva from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") " + condicion_vendedor + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(total_cccfa) as total from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") " + condicion_vendedor + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + ") "
                + "from gen_mes "
                + "order by ide_gemes";
    }

    /**
     * sql con total de ventas en el dia actual y numero de facturas de un punto
     * de emision
     *
     * @param ide_ccdaf
     * @return
     */
    public String getSqlTotalVentasHoy(String ide_ccdaf) {
        return "select count(ide_cccfa) as num_facturas,sum(total_cccfa) as total "
                + "from cxc_cabece_factura "
                + "where fecha_emisi_cccfa='" + utilitario.getFechaActual() + "' "
                + "and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal")
                + " and ide_ccdaf=" + ide_ccdaf;
    }

    /**
     * sql con total de ventas en el mes actual y numero de facturas de un punto
     * de emision
     *
     * @param ide_ccdaf
     * @return
     */
    public String getSqlTotalVentasMes(String ide_ccdaf) {
        return "select count(ide_cccfa) as num_facturas,sum(total_cccfa) as total "
                + "from cxc_cabece_factura "
                + "where  EXTRACT(MONTH FROM fecha_emisi_cccfa)=" + utilitario.getMes(utilitario.getFechaActual())
                + " and EXTRACT(YEAR FROM fecha_emisi_cccfa)=" + utilitario.getAnio(utilitario.getFechaActual())
                + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal")
                + " and ide_ccdaf=" + ide_ccdaf;
    }

    /**
     * sql con total de ventas en el año actual y numero de facturas de un punto
     * de emision
     *
     * @param ide_ccdaf
     * @return
     */
    public String getSqlTotalVentasAnio(String ide_ccdaf) {
        return "select count(ide_cccfa) as num_facturas,sum(total_cccfa) as total "
                + "from cxc_cabece_factura "
                + "where EXTRACT(YEAR FROM fecha_emisi_cccfa)=" + utilitario.getAnio(utilitario.getFechaActual())
                + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal")
                + " and ide_ccdaf=" + ide_ccdaf;
    }

    /**
     * Retorna el sql con los anios que exuste facturacion en la empresa
     *
     * @return
     */
    public String getSqlAniosFacturacion() {
        return "select distinct EXTRACT(YEAR FROM fecha_emisi_cccfa)||'' as anio,EXTRACT(YEAR FROM fecha_emisi_cccfa)||'' as nom_anio  "
                + "from cxc_cabece_factura where ide_empr=" + utilitario.getVariable("IDE_EMPR") + " order by 1 desc ";
    }

    /**
     * Sql con los meses calendarios
     *
     * @return
     */
    public String getSqlMeses() {
        return "select ide_gemes,nombre_gemes from gen_mes  where ide_empr=" + utilitario.getVariable("IDE_EMPR") + " order by ide_gemes";
    }

    /**
     * Sql que retorna las ventas de un punto de emision en un año y mes
     *
     * @param numeroMes
     * @param anio
     * @return
     */
    public String getSqlVentasMensuales(String numeroMes, String anio) {
        String fechaInicio = utilitario.getFormatoFecha(anio + "-" + numeroMes + "-01");
        String fechaFin = utilitario.getUltimaFechaMes(fechaInicio);
        return "select a.ide_cccfa,fecha_emisi_cccfa, secuencial_cccfa,"
                + "nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, "
                + "observacion_cccfa "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "where fecha_emisi_cccfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU")
                + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal")
                + " ORDER BY secuencial_cccfa ,ide_cccfa desc";
    }

    public String getSqlNotasCreditoMensuales(String numeroMes, String anio) {
        String fechaInicio = utilitario.getFormatoFecha(anio + "-" + numeroMes + "-01");
        String fechaFin = utilitario.getUltimaFechaMes(fechaInicio);
        return "select a.ide_cpcno,fecha_emisi_cpcno, numero_cpcno,"
                + "nom_geper,identificac_geper,base_grabada_cpcno as ventas12,"
                + "base_tarifa0_cpcno+base_no_objeto_iva_cpcno as ventas0,valor_iva_cpcno,total_cpcno, "
                + "observacion_cpcno "
                + "from cxp_cabecera_nota a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "where fecha_emisi_cpcno BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU")
                + " and ide_cpeno= 1"//estado 1= normal
                + " ORDER BY numero_cpcno ,ide_cpcno desc";
    }

    public String getSqlCabeceraFactura(String ide_cccfa) {
        return "SELECT * from cxc_cabece_factura where ide_cccfa=" + ide_cccfa;
    }

    public String getSqlActualizaPagoFactura(String ide_cccfa) {
        return "update cxc_cabece_factura set pagado_cccfa=true where ide_cccfa=" + ide_cccfa;
    }

    /**
     * Cambia de estado a anulado la factura y la transaccion cxc
     *
     * @param ide_cccfa
     */
    public void anularFactura(String ide_cccfa) {
        //Anula Fctura
        utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_anulada") + ",ide_cnccc=null where ide_cccfa=" + ide_cccfa);
        //Transaccion CXC Generar reverso de la transaccion FACTURA
        TablaGenerica tab_cab = utilitario.consultar("SELECT a.ide_cccfa,ide_ccctr,secuencial_cccfa,ide_srcom,ide_ccdaf from cxc_cabece_transa a inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa where a.ide_cccfa=" + ide_cccfa + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal"));
////        if (tab_cab.getTotalFilas() > 0) {
////            reversarTransaccionCxC(tab_cab.getValor("ide_ccctr"), "V/. ANULACIÓN FACTURA : " + tab_cab.getValor("secuencial_cccfa"));
////        }

        //Anula Asiento
        TablaGenerica tab_busca = utilitario.consultar("SELECT * FROM con_cab_comp_cont where ide_cnccc = (select ide_cnccc from cxc_cabece_factura where ide_cccfa=" + ide_cccfa + ")");
        if (tab_busca.getTotalFilas() > 0) {
            String p_con_estado_comprobante_anulado = parametros.get("p_con_estado_comprobante_anulado");
            utilitario.getConexion().agregarSqlPantalla("update con_cab_comp_cont set ide_cneco=" + p_con_estado_comprobante_anulado + " where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
            utilitario.getConexion().agregarSqlPantalla("UPDATE con_det_comp_cont set valor_cndcc=0 where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
        }
        //eliminar transacciones cxp
        utilitario.getConexion().agregarSqlPantalla("delete from cxc_detall_transa where ide_cccfa=" + ide_cccfa);
        utilitario.getConexion().agregarSqlPantalla("delete from cxc_cabece_transa where ide_cccfa=" + ide_cccfa);

        //Anula transaccion inventario
        utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_inepi=" + utilitario.getVariable("p_inv_estado_anulado") + " where ide_incci in (select ide_incci from inv_det_comp_inve where ide_cccfa=" + ide_cccfa + " group by ide_incci)");
        ////////****!!!!!!!!!!!crear variable  p_inv_estado_anulado

        if (isFacturaElectronica(tab_cab.getValor("ide_ccdaf"))) {
            //cambia de estado el compobante pendiente
            if (tab_cab.getValor("ide_srcom") != null) {
                utilitario.getConexion().agregarSql("update sri_comprobante set ide_sresc=" + EstadoComprobanteEnum.ANULADO.getCodigo() + ",reutiliza_srcom=true  where ide_srcom=" + tab_cab.getValor("ide_srcom") + " and ide_sresc=" + EstadoComprobanteEnum.PENDIENTE.getCodigo());
                //anula guia
                String guia = getCodigoGuiaElectronica(tab_cab.getValor("ide_srcom"));
                if (guia != null) {
                    utilitario.getConexion().agregarSql("update sri_comprobante set ide_sresc=" + EstadoComprobanteEnum.ANULADO.getCodigo() + ",reutiliza_srcom=true  where ide_srcom=" + guia + " and ide_sresc=" + EstadoComprobanteEnum.PENDIENTE.getCodigo());
                }

            }
        }

    }

    public void anularSecuencial(String secuencial_cccfa, String ide_ccdaf) {
        TablaGenerica tab_fac = new TablaGenerica();
        tab_fac.setTabla("cxc_cabece_factura", "ide_cccfa");
        tab_fac.setCondicion("secuencial_cccfa='" + secuencial_cccfa + "' and ide_ccdaf=" + ide_ccdaf);
        tab_fac.ejecutarSql();
        if (tab_fac.isEmpty() == false) {
            //existe
            anularFactura(tab_fac.getValor("ide_cccfa"));
        } else {
            //no existe , inserta la factura anulada
            tab_fac.insertar();
            tab_fac.setValor("secuencial_cccfa", secuencial_cccfa);
            tab_fac.setValor("ide_ccdaf", ide_ccdaf);
            tab_fac.setValor("ide_ccefa", parametros.get("p_cxc_estado_factura_anulada"));
            tab_fac.setValor("ide_cntdo", utilitario.getVariable("p_con_tipo_documento_factura"));
            tab_fac.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_fac.setValor("fecha_trans_cccfa", utilitario.getFechaActual());
            tab_fac.setValor("fecha_emisi_cccfa", utilitario.getFechaActual());
            tab_fac.setValor("pagado_cccfa", "false");
            tab_fac.setValor("total_cccfa", "0");
            tab_fac.setValor("base_grabada_cccfa", "0");
            tab_fac.setValor("valor_iva_cccfa", "0");
            tab_fac.setValor("base_no_objeto_iva_cccfa", "0");
            tab_fac.setValor("base_tarifa0_cccfa", "0");
            tab_fac.setValor("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa"));//sociedad salesianos                
            tab_fac.guardar();
        }
    }

    public void reversarTransaccionCxC(String ide_ccctr, String observacion) {
        if (ide_ccctr != null && !ide_ccctr.isEmpty()) {

            TablaGenerica tab_det_tran_cxc = new TablaGenerica();
            tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr");
            tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
            tab_det_tran_cxc.setCondicion("ide_ccdtr=-1");
            tab_det_tran_cxc.ejecutarSql();

            TablaGenerica tab_det = utilitario.consultar("SELECT * from cxc_detall_transa where ide_ccctr=" + ide_ccctr);
            tab_det_tran_cxc.limpiar();
            for (int i = 0; i < tab_det.getTotalFilas(); i++) {
                tab_det_tran_cxc.insertar();
                tab_det_tran_cxc.setValor("ide_ccctr", ide_ccctr);
                tab_det_tran_cxc.setValor("ide_cccfa", tab_det.getValor(i, "ide_cccfa"));
                tab_det_tran_cxc.setValor("ide_cnccc", tab_det.getValor(i, "ide_cnccc"));
                tab_det_tran_cxc.setValor("ide_teclb", tab_det.getValor(i, "ide_teclb"));
                tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                if (tab_det.getValor(i, "ide_ccttr") != null && !tab_det.getValor(i, "ide_ccttr").isEmpty()) {
                    if (Integer.parseInt(getSignoTransaccionCxC(tab_det.getValor(i, "ide_ccttr"))) > 0) {
                        tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_mas"));
                    } else {
                        tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_menos"));
                    }
                }
                tab_det_tran_cxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
                tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
                tab_det_tran_cxc.setValor("numero_pago_ccdtr", tab_det.getValor(i, "numero_pago_ccdtr"));
                tab_det_tran_cxc.setValor("valor_ccdtr", tab_det.getValor(i, "valor_ccdtr"));
                tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_det.getValor(i, "docum_relac_ccdtr"));
                tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
            }
            tab_det_tran_cxc.guardar();
        }
    }

    public void reversarTransaccionCxCChequeDevuelto(String ide_ccdtr, String ide_teclb, String observacion, String fecha) {
        if (ide_ccdtr != null && !ide_ccdtr.isEmpty()) {

            TablaGenerica tab_det_tran_cxc = new TablaGenerica();
            tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr");
            tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
            tab_det_tran_cxc.setCondicion("ide_ccdtr=-1");
            tab_det_tran_cxc.ejecutarSql();

            TablaGenerica tab_det = utilitario.consultar("SELECT * from cxc_detall_transa where ide_ccdtr=" + ide_ccdtr);

            tab_det_tran_cxc.insertar();
            tab_det_tran_cxc.setValor("ide_ccctr", tab_det.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_det.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_cnccc", null); ///****pendiente
            tab_det_tran_cxc.setValor("ide_teclb", ide_teclb);
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_mas")); //Dj antes p_cxc_tipo_trans_reversar_menos
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", fecha);
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", tab_det.getValor("numero_pago_ccdtr"));
            tab_det_tran_cxc.setValor("valor_ccdtr", tab_det.getValor("valor_ccdtr"));
            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_det.getValor("docum_relac_ccdtr"));
            tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
            tab_det_tran_cxc.guardar();
        }
    }

    public String getSignoTransaccionCxC(String ide_ccttr) {
        TablaGenerica tab_tipo_transacciones = utilitario.consultar("select * from cxc_tipo_transacc where ide_ccttr=" + ide_ccttr);
        if (tab_tipo_transacciones.getTotalFilas() > 0) {
            if (tab_tipo_transacciones.getValor(0, "signo_ccttr") != null && !tab_tipo_transacciones.getValor(0, "signo_ccttr").isEmpty()) {
                return tab_tipo_transacciones.getValor(0, "signo_ccttr");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Retorna Todas lOS SALDOS CXc de los clientes
     *
     * @return
     */
    public String getSqlTransaccionesCxC() {
        return "SELECT d.ide_geper,identificac_geper AS IDENTIFICACION,nom_geper as CLIENTE,sum (valor_ccdtr*signo_ccttr) as SALDO\n"
                + "FROM cxc_detall_transa a \n"
                + "INNER JOIN cxc_tipo_transacc b on a.IDE_CCTTR =b.IDE_CCTTR \n"
                + "INNER JOIN cxc_cabece_transa d on a.ide_ccctr=d.ide_ccctr \n"
                + "INNER JOIN gen_persona e on d.ide_geper= e.ide_geper and es_cliente_geper=true \n"
                + "WHERE d.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY d.ide_geper,identificac_geper,nom_geper\n"
                + "order by nom_geper";
    }

    public boolean isFacturaElectronica(String ide_ccdaf) {
//        String p_sri_activa_comp_elect = utilitario.getVariable("p_sri_activa_comp_elect");
//        if (p_sri_activa_comp_elect == null) {
//            return false;
//        }
//        return p_sri_activa_comp_elect.equalsIgnoreCase("true");
        return isPuntoEmisionElectronico(ide_ccdaf);
    }

    public void setHaceFacturaElectronica(String value) {
        utilitario.getConexion().agregarSqlPantalla("UPDATE sis_parametros SET valor_para='" + value + "' where nom_para='p_sri_activa_comp_elect'");
    }

    /**
     * Registra la Factura en una transaccion cxp
     *
     * @param ide_cccfa
     * @param valorRetencion
     * @return ide_cpctr Cabecera de la Transaccion CxP
     */
    public String generarTransaccionRetencion(String ide_cccfa, double valorRetencion) {
        String ide_cpctr = "-1";
        TablaGenerica tab_cab_factura = utilitario.consultar("SELECT * FROM cxc_cabece_factura WHERE ide_cccfa=" + ide_cccfa);
        if (tab_cab_factura.isEmpty() == false) {
            TablaGenerica tab_cab_tran_cxc = new TablaGenerica();
            tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr");
            tab_cab_tran_cxc.setCondicion("ide_cccfa=" + ide_cccfa);
            tab_cab_tran_cxc.ejecutarSql();
            if (tab_cab_tran_cxc.isEmpty()) {
                //tab_cab_tran_cxc.imprimirSql();
                //Si se hace factura de un anticipo
                TablaGenerica tab_cab_aux = utilitario.consultar("SELECT ide_ccctr,ide_cccfa,observacion_ccdtr FROM cxc_detall_transa WHERE ide_cccfa=" + ide_cccfa);
                tab_cab_tran_cxc.setCondicion("ide_ccctr=" + tab_cab_aux.getValor("ide_ccctr"));
                tab_cab_tran_cxc.ejecutarSql();
                tab_cab_tran_cxc.setValor("ide_cccfa", ide_cccfa);
                tab_cab_tran_cxc.setValor("observacion_ccctr", tab_cab_aux.getValor("observacion_ccdtr"));
                tab_cab_tran_cxc.modificar(tab_cab_tran_cxc.getFilaActual());
                tab_cab_tran_cxc.guardar();
            }

            TablaGenerica tab_det_tran_cxc = new TablaGenerica();
            tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr");
            tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
            tab_det_tran_cxc.setCondicion("ide_ccctr=" + tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.ejecutarSql();
            tab_det_tran_cxc.insertar();
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", ide_cccfa);
            tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_retencion"));
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_trans_cccfa"));
            tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valorRetencion));
            tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_factura.getValor("observacion_cccfa"));
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", "0");
            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_factura.getValor("secuencial_cccfa"));
            tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxc.guardar();
            ide_cpctr = tab_cab_tran_cxc.getValor("ide_ccctr");
        }
        return ide_cpctr;
    }

    ///NOTAS DE CREDITO
    /**
     * Retorna el secuencial maximo de un punto de emisión
     *
     * @param ide_ccdaf
     * @return
     */
    public int getSecuencialNotaCredito(String ide_ccdaf) {
        int max = 0;
        TablaGenerica tab_secuencia = utilitario.consultar("select ide_ccdaf,max(CAST(coalesce(numero_cpcno, '0') AS Integer)) as num_max FROM cxp_cabecera_nota where ide_ccdaf=" + ide_ccdaf + " GROUP BY ide_ccdaf ");
        if (tab_secuencia.isEmpty() == false) {
            try {
                max = Integer.parseInt(tab_secuencia.getValor("num_max"));
            } catch (Exception e) {
            }
        }
        max++;
        return max;
    }

    public TablaGenerica getFacturaPorSecuencial(String num_doc_mod_cpcno) {
        if (num_doc_mod_cpcno != null) {
            num_doc_mod_cpcno = num_doc_mod_cpcno.replace("-", "");
        }
        String secuencial_cccfa = num_doc_mod_cpcno.substring(6, 15);
        String serie_ccdaf = num_doc_mod_cpcno.substring(0, 6);      
        return utilitario.consultar("select ide_geper,fecha_emisi_cccfa,total_cccfa,base_grabada_cccfa,base_no_objeto_iva_cccfa,base_tarifa0_cccfa,valor_iva_cccfa,\n"
                + "ide_inarti,ide_inuni,cantidad_ccdfa,precio_ccdfa,total_ccdfa,iva_inarti_ccdfa,observacion_ccdfa,descuento_ccdfa\n"
                + "from cxc_cabece_factura a\n" 
                + "inner join cxc_deta_factura b on a.ide_cccfa=b.ide_cccfa\n"
                + "inner join cxc_datos_fac c on a.ide_ccdaf=c.ide_ccdaf\n"
                + "where secuencial_cccfa='" + secuencial_cccfa + "'\n"
                + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "and serie_ccdaf='" + serie_ccdaf + "' order by ide_ccefa ");
    }

    /**
     * Retorna el ide_cccfa de una factura por secuencial
     *
     * @param num_doc_mod_cpcno
     * @return
     */
    public String getIdeFacturaPorSecuencial(String num_doc_mod_cpcno) {
        if (num_doc_mod_cpcno != null) {
            num_doc_mod_cpcno = num_doc_mod_cpcno.replace("-", "");
        }
        String secuencial_cccfa = num_doc_mod_cpcno.substring(6, 15);
        String serie_ccdaf = num_doc_mod_cpcno.substring(0, 6);
        return utilitario.consultar("select a.ide_cccfa,secuencial_cccfa"
                + " from cxc_cabece_factura a\n"
                + "inner join cxc_datos_fac c on a.ide_ccdaf=c.ide_ccdaf\n"
                + "where secuencial_cccfa='" + secuencial_cccfa + "'\n"
                + "and serie_ccdaf='" + serie_ccdaf + "' ").getValor("ide_cccfa");
    }

    public String getSqlNotas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        if (isFacturaElectronica(ide_ccdaf)) { //si tiene facturacion electrónica
            return "select a.ide_cpcno, numero_cpcno,ide_cnccc,a.ide_cpeno,nombre_sresc as nombre_cpeno, fecha_emisi_cpcno,motivo_srcom as MOTIVO,nom_geper,identificac_geper,base_grabada_cpcno as ventas12,"
                    + "base_tarifa0_cpcno+base_no_objeto_iva_cpcno as ventas0,valor_iva_cpcno,total_cpcno, "
                    + "claveacceso_srcom as CLAVE_ACCESO, fecha_trans_cpcno,d.ide_srcom,a.ide_geper ,num_doc_mod_cpcno "
                    + "from cxp_cabecera_nota a "
                    + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                    + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                    + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                    + "where fecha_emisi_cpcno BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                    + "and ide_ccdaf=" + ide_ccdaf + " "
                    // + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " " 
                    + "ORDER BY numero_cpcno desc,ide_cpcno desc";
        } else {
            return "select a.ide_cpcno, numero_cpcno, ide_cnccc,a.ide_cpeno,nombre_cpeno ,fecha_emisi_cpcno,nom_geper,identificac_geper,base_grabada_cpcno as ventas12,"
                    + "base_tarifa0_cpcno+base_no_objeto_iva_cpcno as ventas0,valor_iva_cpcno,total_cpcno, "
                    + "observacion_cccfa, fecha_trans_cpcno "
                    + "from cxp_cabecera_nota a "
                    + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                    + "inner join cxc_estado_factura c on  a.ide_cpeno=c.ide_cpeno "
                    + "where fecha_emisi_cpcno BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                    + "and ide_ccdaf=" + ide_ccdaf + " "
                    // + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                    + "ORDER BY numero_cpcno desc,ide_cpcno desc";
        }

    }

    public String getSqlNotasElectronicasPorEstado(String ide_ccdaf, String fechaInicio, String fechaFin, EstadoComprobanteEnum estado) {

        return "select a.ide_cpcno, numero_cpcno,ide_cnccc,a.ide_cpeno,nombre_sresc as nombre_cpeno, fecha_emisi_cpcno,motivo_srcom as MOTIVO,nom_geper,identificac_geper,base_grabada_cpcno as ventas12,"
                + "base_tarifa0_cpcno+base_no_objeto_iva_cpcno as ventas0,valor_iva_cpcno,total_cpcno, "
                + "claveacceso_srcom as CLAVE_ACCESO, fecha_trans_cpcno,d.ide_srcom  "
                + "from cxp_cabecera_nota a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "where fecha_emisi_cpcno BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf + " "
                + " and d.ide_sresc =" + estado.getCodigo() + " "
                + "ORDER BY numero_cpcno desc,ide_cpcno desc";

    }

    public String getSqlNotasAnuladas(String ide_ccdaf, String fechaInicio, String fechaFin) {

        return "select a.ide_cpcno, numero_cpcno,nombre_cpeno ,fecha_emisi_cpcno,nom_geper,identificac_geper,base_grabada_cpcno as ventas12,"
                + "base_tarifa0_cpcno+base_no_objeto_iva_cpcno as ventas0,valor_iva_cpcno,total_cpcno, "
                + "observacion_cccfa, fecha_trans_cpcno "
                + "from cxp_cabecera_nota a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "inner join cxc_estado_factura c on  a.ide_cpeno=c.ide_cpeno "
                + "where fecha_emisi_cpcno BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf + " and ide_cpeno=0 "
                // + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "ORDER BY numero_cpcno desc,ide_cpcno desc";

    }

    /**
     * Cambia de estado a anulado la factura y la transaccion cxc
     *
     * @param ide_cpcno
     */
    public void anularNotaCredito(String ide_cpcno) {
        //Anula Fctura
        utilitario.getConexion().agregarSqlPantalla("update cxp_cabecera_nota set ide_cpeno=0,ide_cnccc=null where ide_cpcno=" + ide_cpcno);//0 anulado
////        //Transaccion CXC Generar reverso de la transaccion FACTURA
////        TablaGenerica tab_cab = utilitario.consultar("SELECT a.ide_cpcno,ide_ccctr,secuencial_cccfa,ide_srcom from cxc_cabece_transa a inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa where a.ide_cccfa=" + ide_cccfa + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal"));
        TablaGenerica tab_cab = utilitario.consultar("SELECT ide_cpcno,ide_srcom,ide_ccdaf from cxp_cabecera_nota where ide_cpcno=" + ide_cpcno);
////        if (tab_cab.getTotalFilas() > 0) {
////            reversarTransaccionCxC(tab_cab.getValor("ide_ccctr"), "V./ ANULACIÓN FACTURA : " + tab_cab.getValor("secuencial_cccfa"));
////        }
////        //Anula transaccion inventario
////        utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_inepi=" + utilitario.getVariable("p_inv_estado_anulado") + " where ide_incci in (select ide_incci from inv_det_comp_inve where ide_cccfa=" + ide_cccfa + " group by ide_incci)");
////        ////////****!!!!!!!!!!!crear variable  p_inv_estado_anulado
////
        if (isFacturaElectronica(tab_cab.getValor("ide_ccdaf"))) {
            //cambia de estado el compobante pendiente
            if (tab_cab.getValor("ide_srcom") != null) {
                utilitario.getConexion().agregarSql("update sri_comprobante set ide_sresc=" + EstadoComprobanteEnum.ANULADO.getCodigo() + ",reutiliza_srcom=true  where ide_srcom=" + tab_cab.getValor("ide_srcom") + " and ide_sresc=" + EstadoComprobanteEnum.PENDIENTE.getCodigo());
            }
        }
////        //Produquimic elimina kardex
////        ser_integracion.anularFactura_Escritorio(ide_cccfa);
    }

    /**
     * Retorna el ide_srcom de la guia electrónica asociada a una factura
     *
     * @param factura
     * @return
     */
    public String getCodigoGuiaElectronica(String factura) {
        TablaGenerica tab_secuencia = utilitario.consultar("select a.ide_cccfa,b.ide_srcom\n"
                + "from cxc_cabece_factura a\n"
                + "inner join cxc_guia b on a.ide_cccfa=b.ide_cccfa\n"
                + "where a.ide_srcom=" + factura);
        return tab_secuencia.getValor("ide_srcom");

    }

    public void generarNuevaClaveAcceso(String factura) {
        utilitario.getConexion().agregarSqlPantalla("UPDATE sri_comprobante set claveacceso_srcom=null where ide_srcom=" + factura);
    }

    /**
     * Retorna los nombres de los vendedores
     *
     * @return
     */
    public String getSqlVendedoresNombre() {
        return "SELECT nombre_vgven,nombre_vgven FROM ven_vendedor ORDER BY nombre_vgven";
    }

    /**
     * Retorna los nombres de los vendedores
     *
     * @return
     */
    public String getSqlComboVendedores() {
        return "SELECT ide_vgven,nombre_vgven FROM ven_vendedor ORDER BY nombre_vgven";
    }

    /**
     * Retorna los nombres de los vendedores
     *
     * @return
     */
    public String getNombreVendedorFactura(String ide_cccfa) {
        return utilitario.consultar("select ide_cccfa,nombre_vgven from cxc_cabece_factura a\n"
                + "inner join ven_vendedor b on a.ide_vgven= b.ide_vgven\n"
                + "where ide_cccfa=" + ide_cccfa).getValor("nombre_vgven");
    }

    /**
     * Actualiza el vendedor de una factura
     *
     * @param ide_cccfa
     * @param ide_vgven
     */
    public void actualizarVendedorFactura(String ide_cccfa, String ide_vgven) {
        utilitario.getConexion().ejecutarSql("UPDATE cxc_cabece_factura set ide_vgven =" + ide_vgven + " where ide_cccfa=" + ide_cccfa);
    }

    public int getDiasCreditoFormaPago(String ide_cndfp) {
        int dias = 0;
        TablaGenerica tag = utilitario.consultar("select ide_cndfp,dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
        if (tag.isEmpty() == false) {
            try {
                dias = Integer.parseInt(tag.getValor("dias_cndfp"));
            } catch (Exception e) {
            }
        }
        return dias;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_cobrar;

import comprobantesElectronicos.dao.ComprobanteDAOLocal;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.ServicioBase;
import servicios.contabilidad.ServicioContabilidadGeneral;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless
public class ServicioFacturaCxC extends ServicioBase {

    @EJB
    private ServicioContabilidadGeneral ser_conta_general;
    @EJB
    private ComprobanteDAOLocal comprobateElectronico;

    @PostConstruct
    public void init() {
        //Recupera todos los parametros que se van a ocupar en el EJB
        parametros = utilitario.getVariables("p_cxc_estado_factura_normal", "p_cxc_tipo_trans_factura", "p_cxc_tipo_trans_pago");
    }

    /**
     * Retorna la sentencia SQL con los puntos de emision de facturas x empresa
     *
     * @return
     */
    public String getSqlPuntosEmision() {
        return "select ide_ccdaf,serie_ccdaf, autorizacion_ccdaf,observacion_ccdaf from cxc_datos_fac where ide_empr=" + utilitario.getVariable("IDE_EMPR");
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

    /**
     * Retorna todas las facturas generadas en un punto de emisión x sucursal
     *
     * @param ide_ccdaf Punto de Emisión
     * @param fechaInicio Fecha desde
     * @param fechaFin Fecha hasta
     * @return
     */
    public String getSqlFacturas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        return "select a.ide_cccfa, secuencial_cccfa, ide_cnccc,a.ide_ccefa,nombre_ccefa ,fecha_emisi_cccfa,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, "
                + "observacion_cccfa, fecha_trans_cccfa "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "inner join cxc_estado_factura c on  a.ide_ccefa=c.ide_ccefa "
                + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf + " "
                // + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
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
     * @param ide_ccdaf
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlFacturasPorCobrar(String ide_ccdaf, String fechaInicio, String fechaFin) {
        return "select cf.ide_cccfa, secuencial_cccfa,cf.ide_cnccc,fecha_emisi_cccfa,nom_geper,identificac_geper,base_grabada_cccfa as ventas12, "
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar, "
                + "observacion_cccfa, fecha_trans_cccfa "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr  "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "left join gen_persona b on cf.ide_geper=b.ide_geper "
                + "where cf.ide_ccdaf=" + ide_ccdaf + " "
                + "and fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                // + " and cf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY cf.ide_cccfa,nom_geper,identificac_geper "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY secuencial_cccfa desc,cf.ide_cccfa desc";
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
        String str_p_cxc_tipo_trans_factura = parametros.get("p_cxc_tipo_trans_factura");//Tipo transaccion Factura     
        tab_cab_tran_cxc.limpiar();
        tab_det_tran_cxc.limpiar();
        tab_cab_tran_cxc.insertar();
        if (tab_cab_factura != null) {
            tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_cab_tran_cxc.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxc.setValor("fecha_trans_ccctr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_cab_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_cab_tran_cxc.setValor("observacion_ccctr", "V/. Factura " + tab_cab_factura.getValor("secuencial_cccfa") + " ");
            tab_cab_tran_cxc.guardar();
            tab_det_tran_cxc.insertar();
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_det_tran_cxc.setValor("valor_ccdtr", tab_cab_factura.getValor("total_cccfa"));
            tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_tran_cxc.getValor("observacion_ccctr"));
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", "0");
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cccfa")), ser_conta_general.getDiasFormaPago(tab_cab_factura.getValor("ide_cndfp")))));
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

    public void generarTransaccionPago(TablaGenerica tab_cab_factura, String ide_ccctr, String ide_teclb, double valor, String observacion, String num_documento) {
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

    public String guardarComprobanteElectronicoFactura(Tabla tab_factura, Tabla tab_detalle) {
        return comprobateElectronico.guardarComprobanteFactura(tab_factura, tab_detalle);
    }

    /**
     * Sql de total de ventas por mes de un punto de emisión por anio(s)
     *
     * @param ide_ccdaf
     * @param anio
     * @return
     */
    public String getSqlTotalVentasMensuales(String ide_ccdaf, String anio) {

        return "select nombre_gemes,"
                + "(select count(ide_cccfa) as num_facturas from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and ide_ccdaf=" + ide_ccdaf + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_grabada_cccfa) as ventas12 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and ide_ccdaf=" + ide_ccdaf + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_tarifa0_cccfa+base_no_objeto_iva_cccfa) as ventas0 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and ide_ccdaf=" + ide_ccdaf + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(valor_iva_cccfa) as iva from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and ide_ccdaf=" + ide_ccdaf + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(total_cccfa) as total from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and ide_ccdaf=" + ide_ccdaf + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + ") "
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
     * @param ide_ccdaf
     * @param numeroMes
     * @param anio
     * @return
     */
    public String getSqlVentasMensuales(String ide_ccdaf, String numeroMes, String anio) {
        String fechaInicio = utilitario.getFormatoFecha(anio + "-" + numeroMes + "-01");
        String fechaFin = utilitario.getUltimaFechaMes(fechaInicio);
        return "select a.ide_cccfa,fecha_emisi_cccfa, secuencial_cccfa,"
                + "nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa,"
                + "observacion_cccfa "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "where fecha_emisi_cccfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf
                + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal")
                + " ORDER BY secuencial_cccfa desc,ide_cccfa desc";
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
        utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_anulada") + " where ide_cccfa=" + ide_cccfa);
        //Transaccion CXC Generar reverso de la transaccion FACTURA
        TablaGenerica tab_cab = utilitario.consultar("SELECT a.ide_cccfa,ide_ccctr,secuencial_cccfa from cxc_cabece_transa a inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa where a.ide_cccfa=" + ide_cccfa + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal"));
        if (tab_cab.getTotalFilas() > 0) {
            reversarTransaccionCxC(tab_cab.getValor("ide_ccctr"), "V./ ANULACIÓN FACTURA : " + tab_cab.getValor("secuencial_cccfa"));
        }
        //Anula transaccion inventario
        utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_inepi=" + utilitario.getVariable("p_inv_estado_anulado") + " where ide_incci in (select ide_incci from inv_det_comp_inve where ide_cccfa=" + ide_cccfa + " group by ide_incci)");
        ////////****!!!!!!!!!!!crear variable  p_inv_estado_anulado
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
                        tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_menos"));
                    } else {
                        tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_mas"));
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
                + "INNER JOIN gen_persona e on d.ide_geper= e.ide_geper\n"
                + "WHERE d.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY d.ide_geper,identificac_geper,nom_geper\n"
                + "order by nom_geper";
    }

}

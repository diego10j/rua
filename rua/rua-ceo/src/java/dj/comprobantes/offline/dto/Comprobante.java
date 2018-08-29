/*
 *********************************************************************
 Objetivo: Clase Comprobante
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;

import dj.comprobantes.offline.conexion.ConexionCEO;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author diego.jacome
 */
public final class Comprobante implements Serializable {

    private Long codigocomprobante;
    private String tipoemision;
    private String claveacceso;
    private String coddoc;
    private String estab;
    private String ptoemi;
    private String secuencial;
    private Date fechaemision;
    private String direstablecimiento; //Direccion de la oficina
    private String guiaremision;
    private String numAutorizacion;
    private BigDecimal totalsinimpuestos;
    private BigDecimal totaldescuento;
    private BigDecimal propina;
    private BigDecimal importetotal;
    private String moneda;
    private String periodofiscal;
    private String rise;
    private String coddocmodificado;
    private String numdocmodificado;
    private Date fechaemisiondocsustento;
    private BigDecimal valormodificacion;
    private Firma codigofirma;
    private Integer codigoestado;
    private String oficina;
    private String rucEmpresa;
    private Date fechaautoriza;
    private Cliente cliente;
    private BigDecimal subtotal0;
    private BigDecimal subtotal;
    private BigDecimal subtotalNoObjeto;  //29/08/2018
    private BigDecimal iva;
    private List<DetalleComprobante> detalle;
    private Boolean enNube = false;
    private List<DetalleImpuesto> impuesto;
    private String formaCobro = "01"; //por defecto efectivo
    private String motivo;
    private int diasCredito;
    private String numOrdenCompra;
    private String infoAdicional1;
    private String infoAdicional2;
    private String infoAdicional3;

    private List<InfoAdicional> infoAdicional;

    private String correo;
    private String direccionFactura;

    //Campos para guias de remisión
    private String dirPartida;
    private Date fechaIniTransporte;
    private Date fechaFinTransporte;
    private String placa;
    private Destinatario destinatario;
    private Long codigoComprobanteFactura;
    private String telefonos;
    private boolean sinFinesLucro = false;
    private String correoEmpresa;

    public Comprobante() {
    }

    public Comprobante(ResultSet resultado, ConexionCEO con) {
        try {
            this.codigocomprobante = resultado.getLong("ide_srcom");
            this.codigoComprobanteFactura = resultado.getLong("sri_ide_srcom");
            this.tipoemision = resultado.getString("tipoemision_srcom");
            this.claveacceso = resultado.getString("claveacceso_srcom");
            this.coddoc = resultado.getString("coddoc_srcom");
            this.estab = resultado.getString("estab_srcom");
            this.ptoemi = resultado.getString("ptoemi_srcom");
            this.secuencial = resultado.getString("secuencial_srcom");
            this.fechaemision = resultado.getDate("fechaemision_srcom");
            //this.direstablecimiento =   Direccion donde se factura
            this.guiaremision = resultado.getString("num_guia_srcom");
            this.totalsinimpuestos = resultado.getBigDecimal("subtotal_srcom");
            this.placa = resultado.getString("placa_srcom");
            this.totaldescuento = resultado.getBigDecimal("descuento_srcom");
            this.propina = new BigDecimal(0.00);
            this.importetotal = resultado.getBigDecimal("total_srcom");
            this.moneda = "DOLAR";
            this.periodofiscal = resultado.getString("periodo_fiscal_srcom");
            // this.rise = resultado.getString("");
            this.coddocmodificado = resultado.getString("codigo_docu_mod_srcom");
            this.numdocmodificado = resultado.getString("num_doc_mod_srcom");
            this.fechaemisiondocsustento = resultado.getDate("fecha_emision_mod_srcom");
            this.valormodificacion = resultado.getBigDecimal("valor_mod_srcom");
            this.numAutorizacion = resultado.getString("autorizacion_srcom");
            this.correo = resultado.getString("correo_srcom");

            this.diasCredito = resultado.getInt("dias_credito_srcom");
            this.numOrdenCompra = resultado.getString("orden_compra_srcom");

            this.infoAdicional1 = resultado.getString("infoadicional1_srcom");
            this.infoAdicional2 = resultado.getString("infoadicional2_srcom");
            this.infoAdicional3 = resultado.getString("infoadicional3_srcom");

            if (resultado.getString("en_nube_srcom") != null) {
                this.enNube = resultado.getBoolean("en_nube_srcom");
            }
            //inicio 02-02-2018 
            if (resultado.getString("ide_sucu") != null) {
                this.oficina = resultado.getString("ide_sucu");
            }
            if (resultado.getString("identificacion_empr") != null) {
                this.rucEmpresa = resultado.getString("identificacion_empr");
            }

            if (resultado.getString("telefono_empr") != null) {
                this.telefonos = resultado.getString("telefono_empr");
            }
            if (resultado.getString("fines_lucro_empr") != null) {
                this.sinFinesLucro = resultado.getBoolean("fines_lucro_empr");
            }
            this.setCorreoEmpresa(resultado.getString("mail_empr"));
            //fin

            this.motivo = resultado.getString("motivo_srcom");
            if (resultado.getString("ide_srfid") != null) {
                this.codigofirma = new Firma(resultado.getInt("ide_srfid"));
            }
            this.codigoestado = resultado.getInt("ide_sresc");
            if (resultado.getBigDecimal("subtotal0_srcom") == null) {
                this.subtotal0 = new BigDecimal("0");
            } else {
                this.subtotal0 = resultado.getBigDecimal("subtotal0_srcom");
            }

            if (resultado.getBigDecimal("subtotal_no_objeto_srcom") == null) {
                this.subtotalNoObjeto = new BigDecimal("0");
            } else {
                this.subtotalNoObjeto = resultado.getBigDecimal("subtotal_no_objeto_srcom");
            }

            if (resultado.getBigDecimal("base_grabada_srcom") == null) {
                this.subtotal = this.totalsinimpuestos;
            } else {
                this.subtotal = resultado.getBigDecimal("base_grabada_srcom");
            }
            if (resultado.getBigDecimal("iva_srcom") == null) {
                this.iva = new BigDecimal("0");
            } else {
                this.iva = resultado.getBigDecimal("iva_srcom");
            }
            this.formaCobro = resultado.getString("forma_cobro_srcom");

            this.fechaautoriza = resultado.getDate("fechaautoriza_srcom");

            this.fechaIniTransporte = resultado.getDate("fecha_ini_trans_srcom");
            this.fechaFinTransporte = resultado.getDate("fecha_fin_trans_srcom");
            this.dirPartida = resultado.getString("direcion_partida_srcom");

            direccionFactura = resultado.getString("direcion_partida_srcom");

            //Busca el cliente 
            try {
                Statement sentensia = con.getConnection().createStatement();
                String sql = "SELECT ide_geper,identificac_geper,alterno2_getid,nom_geper,direccion_geper,telefono_geper,movil_geper,correo_geper FROM gen_persona a "
                        + "inner join gen_tipo_identifi b on a.ide_getid=b.ide_getid "
                        + " where ide_geper=" + resultado.getString("ide_geper");
                ResultSet res = sentensia.executeQuery(sql);
                if (res.next()) {
                    cliente = new Cliente(res);
                }
                sentensia.close();
                res.close();
                if (direccionFactura != null) {
                    try {
                        cliente.setDireccion(direccionFactura);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (this.coddoc.equals(TipoComprobanteEnum.FACTURA.getCodigo())) {

                //busca InfoAdicional
                infoAdicional = new ArrayList<>();
                try {
                    Statement sentensia = con.getConnection().createStatement();
                    String sql = "SELECT nombre_srina,valor_srina from sri_info_adicional"
                            + " where ide_srcom=" + this.codigocomprobante;
                    ResultSet res = sentensia.executeQuery(sql);
                    while (res.next()) {
                        InfoAdicional inf = new InfoAdicional(res);
                        inf.setComprobante(this);
                        infoAdicional.add(inf);
                    }
                    sentensia.close();
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Busca los detalles del Comprobante
                try {
                    detalle = new ArrayList<>();
                    String sql = "select f.ide_inarti,codigo_inarti,COALESCE(nombre_inuni,'') ||' '|| observacion_ccdfa as nombre_inarti,cantidad_ccdfa\n"
                            + ",precio_ccdfa,iva_inarti_ccdfa,descuento_ccdfa,total_ccdfa,nombre_inuni, tarifa_iva_cccfa \n"
                            + "from cxc_cabece_factura  a\n"
                            + "inner join cxc_deta_factura c on a.ide_cccfa=c.ide_cccfa\n"
                            + "inner join  inv_articulo f on c.ide_inarti =f.ide_inarti\n"
                            + "left join  inv_unidad g on c.ide_inuni =g.ide_inuni\n"
                            + "where a.ide_srcom=" + this.codigocomprobante;
                    Statement sentensia = con.getConnection().createStatement();
                    ResultSet res = sentensia.executeQuery(sql);
                    while (res.next()) {
                        DetalleComprobante dt = new DetalleComprobante(res);
                        dt.setComprobante(this);
                        detalle.add(dt);
                    }
                    sentensia.close();
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (this.coddoc.equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo())) {
                //Busca los detalles del Comprobante
                try {
                    detalle = new ArrayList<>();
                    String sql = "select f.ide_inarti,codigo_inarti,observacion_cpdno as observacion_ccdfa,COALESCE(nombre_inuni,'') ||' '|| nombre_inarti as nombre_inarti,cantidad_cpdno as cantidad_ccdfa\n"
                            + ",precio_cpdno as precio_ccdfa,iva_inarti_cpdno as iva_inarti_ccdfa,valor_cpdno as total_ccdfa,nombre_inuni, tarifa_iva_cpcno as tarifa_iva_cccfa,descuento_cpdno as descuento_ccdfa \n"
                            + "from cxp_cabecera_nota  a\n"
                            + "inner join cxp_detalle_nota c on a.ide_cpcno=c.ide_cpcno\n"
                            + "inner join  inv_articulo f on c.ide_inarti =f.ide_inarti\n"
                            + "left join  inv_unidad g on c.ide_inuni =g.ide_inuni\n"
                            + "where a.ide_srcom=" + this.codigocomprobante;
                    Statement sentensia = con.getConnection().createStatement();
                    ResultSet res = sentensia.executeQuery(sql);
                    while (res.next()) {
                        DetalleComprobante dt = new DetalleComprobante(res);
                        dt.setComprobante(this);
                        detalle.add(dt);
                    }
                    sentensia.close();
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (this.coddoc.equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
                //Busca datos de la guia de remision
                try {
                    String sql = "select c.identificac_geper,c.nom_geper,c.direccion_geper,nombre_cctgi,c.telefono_geper,c.correo_geper,\n"
                            + "h.coddoc_srcom,h.estab_srcom,h.ptoemi_srcom,h.secuencial_srcom,h.claveacceso_srcom,h.fechaemision_srcom\n"
                            + "from cxc_guia a\n"
                            + "inner join gen_persona b on a.ide_geper = b.ide_geper  \n"
                            + "inner join gen_persona c on a.ide_geper = c.ide_geper  \n"
                            + "inner join cxc_tipo_guia e on a.ide_cctgi=e.ide_cctgi\n"
                            + "inner join sri_comprobante f on a.ide_srcom=f.ide_srcom\n"
                            + "inner join sri_comprobante h on f.sri_ide_srcom=h.ide_srcom\n"
                            + "where a.ide_srcom=" + this.codigocomprobante;
                    Statement sentensia = con.getConnection().createStatement();
                    ResultSet res = sentensia.executeQuery(sql);
                    if (res.next()) {
                        destinatario = new Destinatario(res);
                    }
                    sentensia.close();
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Busca los detalles del Comprobante Factura asociado a la guia
                try {
                    detalle = new ArrayList<>();
                    String sql = "select f.ide_inarti,codigo_inarti,observacion_ccdfa,COALESCE(nombre_inuni,'') ||' '|| observacion_ccdfa as nombre_inarti,cantidad_ccdfa\n"
                            + ",precio_ccdfa,iva_inarti_ccdfa,total_ccdfa,nombre_inuni,(tarifa_iva_cccfa*100)as tarifa_iva_cccfa \n"
                            + "from cxc_cabece_factura  a\n"
                            + "inner join cxc_deta_factura c on a.ide_cccfa=c.ide_cccfa\n"
                            + "inner join  inv_articulo f on c.ide_inarti =f.ide_inarti\n"
                            + "left join  inv_unidad g on c.ide_inuni =g.ide_inuni\n"
                            + "where a.ide_srcom=" + this.codigoComprobanteFactura;
                    Statement sentensia = con.getConnection().createStatement();
                    ResultSet res = sentensia.executeQuery(sql);
                    while (res.next()) {
                        DetalleComprobante dt = new DetalleComprobante(res);
                        dt.setComprobante(this);
                        detalle.add(dt);
                    }
                    sentensia.close();
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (this.coddoc.equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo())) {
                //Busca los detalles de impuestos del Comprobante
                try {
                    impuesto = new ArrayList();
                    String sql = "select codigo_fe_cnimp,\n"
                            + "coalesce(codigo_fe_retencion_cncim,casillero_cncim) as codigo_fe_retencion_cncim,\n"
                            + "base_cndre,porcentaje_cndre,valor_cndre,\n"
                            + "alter_tribu_cntdo,numero_cpcfa,fecha_emisi_cpcfa\n"
                            + "from con_detall_retenc a\n"
                            + "inner join con_cabece_impues b on a.ide_cncim=b.ide_cncim\n"
                            + "inner join con_impuesto c on b.ide_cnimp = c.ide_cnimp\n"
                            + "inner join con_cabece_retenc d on a.ide_cncre=d.ide_cncre\n"
                            + "inner join cxp_cabece_factur e on d.ide_cncre=e.ide_cncre\n"
                            + "inner join con_tipo_document g on e.ide_cntdo=g.ide_cntdo\n"
                            + "where  ide_srcom=" + this.codigocomprobante;
                    Statement sentensia = con.getConnection().createStatement();
                    ResultSet res = sentensia.executeQuery(sql);
                    while (res.next()) {
                        DetalleImpuesto di = new DetalleImpuesto(res);
                        di.setComprobante(this);
                        impuesto.add(di);
                    }
                    sentensia.close();
                    res.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Long getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Long codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }

    public String getTipoemision() {
        return tipoemision;
    }

    public void setTipoemision(String tipoemision) {
        this.tipoemision = tipoemision;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public String getCoddoc() {
        return coddoc;
    }

    public void setCoddoc(String coddoc) {
        this.coddoc = coddoc;
    }

    public String getEstab() {
        return estab;
    }

    public void setEstab(String estab) {
        this.estab = estab;
    }

    public String getPtoemi() {
        return ptoemi;
    }

    public void setPtoemi(String ptoemi) {
        this.ptoemi = ptoemi;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public Date getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(Date fechaemision) {
        this.fechaemision = fechaemision;
    }

    public String getDirestablecimiento() {
        return direstablecimiento;
    }

    public void setDirestablecimiento(String direstablecimiento) {
        this.direstablecimiento = direstablecimiento;
    }

    public String getGuiaremision() {
        return guiaremision;
    }

    public void setGuiaremision(String guiaremision) {
        this.guiaremision = guiaremision;
    }

    public BigDecimal getTotalsinimpuestos() {
        return totalsinimpuestos;
    }

    public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
        this.totalsinimpuestos = totalsinimpuestos;
    }

    public BigDecimal getTotaldescuento() {
        return totaldescuento;
    }

    public void setTotaldescuento(BigDecimal totaldescuento) {
        this.totaldescuento = totaldescuento;
    }

    public BigDecimal getPropina() {
        return propina;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public BigDecimal getImportetotal() {
        return importetotal;
    }

    public void setImportetotal(BigDecimal importetotal) {
        this.importetotal = importetotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getPeriodofiscal() {
        return periodofiscal;
    }

    public void setPeriodofiscal(String periodofiscal) {
        this.periodofiscal = periodofiscal;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getCoddocmodificado() {
        return coddocmodificado;
    }

    public void setCoddocmodificado(String coddocmodificado) {
        this.coddocmodificado = coddocmodificado;
    }

    public String getNumdocmodificado() {
        return numdocmodificado;
    }

    public void setNumdocmodificado(String numdocmodificado) {
        this.numdocmodificado = numdocmodificado;
    }

    public Date getFechaemisiondocsustento() {
        return fechaemisiondocsustento;
    }

    public void setFechaemisiondocsustento(Date fechaemisiondocsustento) {
        this.fechaemisiondocsustento = fechaemisiondocsustento;
    }

    public BigDecimal getValormodificacion() {
        return valormodificacion;
    }

    public void setValormodificacion(BigDecimal valormodificacion) {
        this.valormodificacion = valormodificacion;
    }

    public Firma getCodigofirma() {
        return codigofirma;
    }

    public void setCodigofirma(Firma codigofirma) {
        this.codigofirma = codigofirma;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public Date getFechaautoriza() {
        return fechaautoriza;
    }

    public void setFechaautoriza(Date fechaautoriza) {
        this.fechaautoriza = fechaautoriza;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getSubtotal0() {
        return subtotal0;
    }

    public void setSubtotal0(BigDecimal subtotal0) {
        this.subtotal0 = subtotal0;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public List<DetalleComprobante> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleComprobante> detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public Integer getCodigoestado() {
        return codigoestado;
    }

    public void setCodigoestado(Integer codigoestado) {
        this.codigoestado = codigoestado;
    }

    public String getFormaCobro() {
        return formaCobro;
    }

    public void setFormaCobro(String formaCobro) {
        this.formaCobro = formaCobro;
    }

    public Boolean getEnNube() {
        return enNube;
    }

    public void setEnNube(Boolean enNube) {
        this.enNube = enNube;
    }

    public List<DetalleImpuesto> getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(List<DetalleImpuesto> impuesto) {
        this.impuesto = impuesto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(int diasCredito) {
        this.diasCredito = diasCredito;
    }

    public String getNumOrdenCompra() {
        return numOrdenCompra;
    }

    public void setNumOrdenCompra(String numOrdenCompra) {
        this.numOrdenCompra = numOrdenCompra;
    }

    public String getDirPartida() {
        return dirPartida;
    }

    public void setDirPartida(String dirPartida) {
        this.dirPartida = dirPartida;
    }

    public Date getFechaIniTransporte() {
        return fechaIniTransporte;
    }

    public void setFechaIniTransporte(Date fechaIniTransporte) {
        this.fechaIniTransporte = fechaIniTransporte;
    }

    public Date getFechaFinTransporte() {
        return fechaFinTransporte;
    }

    public void setFechaFinTransporte(Date fechaFinTransporte) {
        this.fechaFinTransporte = fechaFinTransporte;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Destinatario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Destinatario destinatario) {
        this.destinatario = destinatario;
    }

    public Long getCodigoComprobanteFactura() {
        return codigoComprobanteFactura;
    }

    public void setCodigoComprobanteFactura(Long codigoComprobanteFactura) {
        this.codigoComprobanteFactura = codigoComprobanteFactura;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getInfoAdicional1() {
        return infoAdicional1;
    }

    public void setInfoAdicional1(String infoAdicional1) {
        this.infoAdicional1 = infoAdicional1;
    }

    public String getInfoAdicional2() {
        return infoAdicional2;
    }

    public void setInfoAdicional2(String infoAdicional2) {
        this.infoAdicional2 = infoAdicional2;
    }

    public String getInfoAdicional3() {
        return infoAdicional3;
    }

    public void setInfoAdicional3(String infoAdicional3) {
        this.infoAdicional3 = infoAdicional3;
    }

    public List<InfoAdicional> getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(List<InfoAdicional> infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public boolean isSinFinesLucro() {
        return sinFinesLucro;
    }

    public void setSinFinesLucro(boolean sinFinesLucro) {
        this.sinFinesLucro = sinFinesLucro;
    }

    public String getCorreoEmpresa() {
        return correoEmpresa;
    }

    public void setCorreoEmpresa(String correoEmpresa) {
        this.correoEmpresa = correoEmpresa;
    }

    public String getDireccionFactura() {
        return direccionFactura;
    }

    public void setDireccionFactura(String direccionFactura) {
        this.direccionFactura = direccionFactura;
    }

    public BigDecimal getSubtotalNoObjeto() {
        return subtotalNoObjeto;
    }

    public void setSubtotalNoObjeto(BigDecimal subtotalNoObjeto) {
        this.subtotalNoObjeto = subtotalNoObjeto;
    }

}

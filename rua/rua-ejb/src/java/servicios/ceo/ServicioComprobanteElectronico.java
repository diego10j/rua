/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.ceo;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoEmisionEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.service.ArchivoService;
import dj.comprobantes.offline.service.CPanelService;
import dj.comprobantes.offline.service.ComprobanteService;
import framework.aplicacion.TablaGenerica;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import servicios.ServicioBase;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioComprobanteElectronico extends ServicioBase {

//////    @EJB
//////    private ServicioConfiguracion ser_configuracion;
    @EJB
    private ComprobanteService comprobanteService;
    @EJB
    private ArchivoService archivoService;
    @EJB
    private CPanelService cPanelService;

    /**
     * retorna la direccion de una sucursal
     *
     * @param ide_sucu
     * @return
     */
    public String getDireccionSucursal(String ide_sucu) {
        return utilitario.consultar("select direccion_sucu,ide_sucu from sis_sucursal where ide_sucu=" + ide_sucu).getValor("direccion_sucu");
    }

    /**
     * Genera un comprobante electrónico a partir de una factura ya guardada
     *
     * @param ide_cccfa
     * @return
     */
    public String generarFacturaElectronica(String ide_cccfa) {
        String ide_srcom = "-1";
//////****COMENTADO POR QUE SE VA A LEER EL DETALLE DE LA FACTURA 
//////        TablaGenerica tab_factura = utilitario.consultar("select a.ide_cccfa,secuencial_cccfa,fecha_emisi_cccfa,serie_ccdaf,base_grabada_cccfa\n"
//////                + ",base_tarifa0_cccfa,valor_iva_cccfa,total_cccfa,alterno_ats,identificac_geper\n"
//////                + ",a.ide_geper,ide_cntdo,f.ide_inarti,codigo_inarti,observacion_ccdfa,nombre_inarti,cantidad_ccdfa\n"
//////                + ",precio_ccdfa,iva_inarti_ccdfa,total_ccdfa,ide_srcom,nombre_inuni\n"
//////                + "from cxc_cabece_factura  a \n"
//////                + "inner join gen_persona b on a.ide_geper = b.ide_geper  \n"
//////                + "inner join cxc_deta_factura c on a.ide_cccfa=c.ide_cccfa \n"
//////                + "inner join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
//////                + "inner join con_deta_forma_pago e on a.ide_cndfp=e.ide_cndfp\n"
//////                + "inner join  inv_articulo f on c.ide_inarti =f.ide_inarti\n"
//////                + "left join  inv_unidad g on c.ide_inuni =g.ide_inuni\n"
//////                + "where a.ide_cccfa=" + ide_cccfa);

        TablaGenerica tab_factura = utilitario.consultar("select a.ide_cccfa,secuencial_cccfa,fecha_emisi_cccfa,serie_ccdaf,base_grabada_cccfa\n"
                + ",base_tarifa0_cccfa,valor_iva_cccfa,total_cccfa,e.alterno_ats,identificac_geper\n"
                + ",a.ide_geper,ide_cntdo,ide_srcom,dias_credito_cccfa,orden_compra_cccfa,correo_cccfa,nombre_vgven,f.nombre_cndfp,OBSERVACION_CCCFA \n"
                + "from cxc_cabece_factura a \n"
                + "inner join gen_persona b on a.ide_geper = b.ide_geper  \n"
                + "inner join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
                + "inner join con_deta_forma_pago e on a.ide_cndfp=e.ide_cndfp\n"
                + "inner join con_deta_forma_pago f on a.ide_cndfp1=f.ide_cndfp\n"
                + "inner join ven_vendedor g on a.ide_vgven=g.ide_vgven\n"
                + "where a.ide_cccfa=" + ide_cccfa);
        if (tab_factura.isEmpty() == false) {
            if (tab_factura.getValor("ide_srcom") != null) {
                ide_srcom = tab_factura.getValor("ide_srcom");
            }
            TablaGenerica tab_cabecara = new TablaGenerica();
            tab_cabecara.setTabla("sri_comprobante", "ide_srcom");
            tab_cabecara.setCondicion("ide_srcom=" + ide_srcom);
            tab_cabecara.ejecutarSql();
//////****COMENTADO POR QUE SE VA A LEER EL DETALLE DE LA FACTURA 
//////            TablaGenerica tab_detalle = new TablaGenerica();
//////            tab_detalle.setTabla("sri_detalle_comprobante", "ide_srdec");
//////            tab_detalle.setCondicion("ide_srcom=" + ide_srcom);
//////            tab_detalle.ejecutarSql();

            //Inserta cabecera
            if (tab_cabecara.isEmpty()) {
                tab_cabecara.insertar();
            } else {
                tab_cabecara.modificar(tab_cabecara.getFilaActual());
            }
            double dou_base0 = 0;
            double dou_basegraba = 0;
            double dou_subtotal = 0;
            try {
                dou_base0 = Double.parseDouble(tab_factura.getValor("base_tarifa0_cccfa"));
            } catch (Exception e) {
            }
            try {
                dou_basegraba = Double.parseDouble(tab_factura.getValor("base_grabada_cccfa"));
            } catch (Exception e) {
            }
            dou_subtotal = dou_base0 + dou_basegraba;

            tab_cabecara.setValor("ide_sresc", String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()));
            tab_cabecara.setValor("coddoc_srcom", TipoComprobanteEnum.FACTURA.getCodigo());
            tab_cabecara.setValor("tipoemision_srcom", TipoEmisionEnum.NORMAL.getCodigo());
            tab_cabecara.setValor("estab_srcom", tab_factura.getValor("serie_ccdaf").substring(0, 3));
            tab_cabecara.setValor("ptoemi_srcom", tab_factura.getValor("serie_ccdaf").substring(3, 6));
            tab_cabecara.setValor("fechaemision_srcom", tab_factura.getValor("fecha_emisi_cccfa"));
            tab_cabecara.setValor("reutiliza_srcom", "false");//no reutiliza por defecto
            tab_cabecara.setValor("fecha_sistema_srcom", utilitario.getFechaActual());
            tab_cabecara.setValor("ip_genera_srcom", utilitario.getIp());
            tab_cabecara.setValor("subtotal0_srcom", utilitario.getFormatoNumero(dou_base0));
            tab_cabecara.setValor("base_grabada_srcom", utilitario.getFormatoNumero(dou_basegraba));
            tab_cabecara.setValor("subtotal_srcom", utilitario.getFormatoNumero(dou_subtotal));
            tab_cabecara.setValor("iva_srcom", tab_factura.getValor("valor_iva_cccfa"));
            tab_cabecara.setValor("descuento_srcom", "0.00");
            tab_cabecara.setValor("total_srcom", tab_factura.getValor("total_cccfa"));
            tab_cabecara.setValor("identificacion_srcom", tab_factura.getValor("identificac_geper"));
            tab_cabecara.setValor("en_nube_srcom", "false");
            tab_cabecara.setValor("ide_geper", tab_factura.getValor("ide_geper"));
            tab_cabecara.setValor("ide_cntdo", tab_factura.getValor("ide_cntdo"));
            tab_cabecara.setValor("dias_credito_srcom", tab_factura.getValor("dias_credito_cccfa"));
            tab_cabecara.setValor("orden_compra_srcom", tab_factura.getValor("orden_compra_cccfa"));
            tab_cabecara.setValor("forma_cobro_srcom", tab_factura.getValor("alterno_ats"));
            tab_cabecara.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cabecara.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_cabecara.setValor("correo_srcom", tab_factura.getValor("correo_cccfa"));

            tab_cabecara.setValor("infoadicional1_srcom", tab_factura.getValor("nombre_vgven"));
            tab_cabecara.setValor("infoadicional2_srcom", tab_factura.getValor("nombre_cndfp"));
            tab_cabecara.setValor("infoadicional3_srcom", tab_factura.getValor("OBSERVACION_CCCFA"));

            tab_cabecara.guardar();
            ide_srcom = tab_cabecara.getValor("ide_srcom");
//////****COMENTADO POR QUE SE VA A LEER EL DETALLE DE LA FACTURA 
//////            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
//////                tab_detalle.insertar();
//////                tab_detalle.setValor("ide_srcom", ide_srcom);
//////                tab_detalle.setValor("codigo_principal_srdec", tab_factura.getValor(i, "ide_inarti"));
//////                tab_detalle.setValor("codigo_auxiliar_srdec", tab_factura.getValor(i, "codigo_inarti"));
//////                String descripcion = "";
//////                if (tab_factura.getValor(i, "nombre_inuni") != null) {
//////                    descripcion += tab_factura.getValor(i, "nombre_inuni");
//////                }
//////                descripcion += " " + tab_factura.getValor(i, "nombre_inarti");
//////                tab_detalle.setValor("descripcion_srdec", descripcion.trim());
//////                tab_detalle.setValor("cantidad_srdec", tab_factura.getValor(i, "cantidad_ccdfa"));
//////                tab_detalle.setValor("precio_srdec", tab_factura.getValor(i, "precio_ccdfa"));
//////                tab_detalle.setValor("descuento_detalle_srdec", "0.00");
//////                tab_detalle.setValor("total_detalle_srdec", tab_factura.getValor(i, "total_ccdfa"));
//////
//////                if (tab_factura.getValor(i, "iva_inarti_ccdfa").equalsIgnoreCase("1")) {
//////                    tab_detalle.setValor("porcentaje_iva_srdec", String.valueOf((ser_configuracion.getPorcentajeIva() * 100)));
//////                } else {
//////                    tab_detalle.setValor("porcentaje_iva_srdec", "0");
//////                }
//////            }
//////            if (tab_cabecara.isFilaModificada()) {
//////                //elimina detalles si modifica
//////                utilitario.getConexion().agregarSqlPantalla("delete from sri_detalle_comprobante where ide_srcom =" + ide_srcom);
//////            }
//////            tab_detalle.guardar();

            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                //Si la factura es nueva Asigna nuevo secuencial
                if (tab_cabecara.getValor("secuencial_srcom") == null) {
                    String strSecuencialF = getSecuencialComprobante(TipoComprobanteEnum.FACTURA);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET secuencial_srcom='" + strSecuencialF + "' where ide_srcom=" + ide_srcom);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET reutiliza_srcom= false where secuencial_srcom='" + strSecuencialF + "' and reutiliza_srcom=true and coddoc_srcom='" + TipoComprobanteEnum.FACTURA.getCodigo() + "'");
                    utilitario.getConexion().ejecutarSql("UPDATE cxc_cabece_factura SET  ide_srcom=" + ide_srcom + ", secuencial_cccfa='" + strSecuencialF + "' where ide_cccfa=" + ide_cccfa);

                    utilitario.getConexion().ejecutarSql("UPDATE cxc_cabece_transa SET  observacion_ccctr='V/. Factura " + strSecuencialF + "' where ide_cccfa=" + ide_cccfa);
                    utilitario.getConexion().ejecutarSql("UPDATE cxc_detall_transa SET  observacion_ccdtr='V/. Factura " + strSecuencialF + "',docum_relac_ccdtr='" + strSecuencialF + "' where ide_cccfa=" + ide_cccfa);

                }
                //Si esta en estado PENDIENTE genero nueva clave de acceso por si se modifico la fecha
                if (tab_cabecara.getValor("ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()))) {
                    try {
                        Comprobante comprobanteFactura = comprobanteService.getComprobantePorId(Long.parseLong(ide_srcom));
                        String strClaveAcceso = comprobanteService.getClaveAcceso(comprobanteFactura);
                        utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET claveacceso_srcom='" + strClaveAcceso + "' where ide_srcom=" + ide_srcom);
                    } catch (NumberFormatException | GenericException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return ide_srcom;
    }

    /**
     * Genera un comprobante electrónico a partir de una nota de crédito ya
     * guardada
     *
     * @param ide_cpcno
     * @return
     */
    public String generarNotaCreditoElectronica(String ide_cpcno) {
        String ide_srcom = "-1";
//////****COMENTADO POR QUE SE VA A LEER EL DETALLE DE LA NOTA DE CRÉDITO
//////        TablaGenerica tab_factura = utilitario.consultar("select a.ide_cpcno,numero_cpcno,fecha_emisi_cpcno,serie_ccdaf,base_grabada_cpcno\n"
//////                + ",base_tarifa0_cpcno,valor_iva_cpcno,total_cpcno,alterno_ats,identificac_geper\n"
//////                + ",a.ide_geper,ide_cntdo,f.ide_inarti,codigo_inarti,observacion_cpdno,nombre_inarti,cantidad_cpdno\n"
//////                + ",precio_cpdno,iva_inarti_cpdno,valor_cpdno,ide_srcom,nombre_inuni,nombre_cpmno,num_doc_mod_cpcno,fecha_emision_mod_cpcno,valor_mod_cpcno \n"
//////                + "from cxp_cabecera_nota  a \n"
//////                + "inner join gen_persona b on a.ide_geper = b.ide_geper  \n"
//////                + "inner join cxp_detalle_nota c on a.ide_cpcno=c.ide_cpcno \n"
//////                + "inner join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
//////                + "inner join con_deta_forma_pago e on a.ide_cndfp=e.ide_cndfp\n"
//////                + "inner join  inv_articulo f on c.ide_inarti =f.ide_inarti\n"
//////                + "left join  inv_unidad g on c.ide_inuni =g.ide_inuni\n"
//////                + "inner join  cxp_motivo_nota h on a.ide_cpmno =h.ide_cpmno\n"
//////                + "where a.ide_cpcno=" + ide_cpcno);

        TablaGenerica tab_factura = utilitario.consultar("select a.ide_cpcno,numero_cpcno,fecha_emisi_cpcno,serie_ccdaf,base_grabada_cpcno\n"
                + ",base_tarifa0_cpcno,valor_iva_cpcno,total_cpcno,alterno_ats,identificac_geper,\n"
                + "a.ide_geper,ide_cntdo,ide_srcom,nombre_cpmno,num_doc_mod_cpcno,fecha_emision_mod_cpcno,valor_mod_cpcno,correo_geper \n"
                + "from cxp_cabecera_nota  a \n"
                + "inner join gen_persona b on a.ide_geper = b.ide_geper  \n"
                + "inner join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
                + "inner join con_deta_forma_pago e on a.ide_cndfp=e.ide_cndfp\n"
                + "inner join  cxp_motivo_nota h on a.ide_cpmno =h.ide_cpmno\n"
                + "where a.ide_cpcno=" + ide_cpcno);

        if (tab_factura.isEmpty() == false) {
            if (tab_factura.getValor("ide_srcom") != null) {
                ide_srcom = tab_factura.getValor("ide_srcom");
            }
            TablaGenerica tab_cabecara = new TablaGenerica();
            tab_cabecara.setTabla("sri_comprobante", "ide_srcom");
            tab_cabecara.setCondicion("ide_srcom=" + ide_srcom);
            tab_cabecara.ejecutarSql();
//////            TablaGenerica tab_detalle = new TablaGenerica();
//////            tab_detalle.setTabla("sri_detalle_comprobante", "ide_srdec");
//////            tab_detalle.setCondicion("ide_srcom=" + ide_srcom);
//////            tab_detalle.ejecutarSql();

            //Inserta cabecera
            if (tab_cabecara.isEmpty()) {
                tab_cabecara.insertar();
            } else {
                tab_cabecara.modificar(tab_cabecara.getFilaActual());
            }
            double dou_base0 = 0;
            double dou_basegraba = 0;
            double dou_subtotal = 0;
            try {
                dou_base0 = Double.parseDouble(tab_factura.getValor("base_tarifa0_cpcno"));
            } catch (Exception e) {
            }
            try {
                dou_basegraba = Double.parseDouble(tab_factura.getValor("base_grabada_cpcno"));
            } catch (Exception e) {
            }
            dou_subtotal = dou_base0 + dou_basegraba;

            tab_cabecara.setValor("num_doc_mod_srcom", tab_factura.getValor("num_doc_mod_cpcno"));
            tab_cabecara.setValor("fecha_emision_mod_srcom", tab_factura.getValor("fecha_emision_mod_cpcno"));
            tab_cabecara.setValor("valor_mod_srcom", tab_factura.getValor("valor_mod_cpcno"));
            tab_cabecara.setValor("codigo_docu_mod_srcom", TipoComprobanteEnum.FACTURA.getCodigo());
            tab_cabecara.setValor("motivo_srcom", tab_factura.getValor("nombre_cpmno"));

            tab_cabecara.setValor("ide_sresc", String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()));
            tab_cabecara.setValor("coddoc_srcom", TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo());
            tab_cabecara.setValor("tipoemision_srcom", TipoEmisionEnum.NORMAL.getCodigo());
            tab_cabecara.setValor("estab_srcom", tab_factura.getValor("serie_ccdaf").substring(0, 3));
            tab_cabecara.setValor("ptoemi_srcom", tab_factura.getValor("serie_ccdaf").substring(3, 6));
            tab_cabecara.setValor("fechaemision_srcom", tab_factura.getValor("fecha_emisi_cpcno"));
            tab_cabecara.setValor("reutiliza_srcom", "false");//no reutiliza por defecto
            tab_cabecara.setValor("fecha_sistema_srcom", utilitario.getFechaActual());
            tab_cabecara.setValor("ip_genera_srcom", utilitario.getIp());
            tab_cabecara.setValor("subtotal0_srcom", utilitario.getFormatoNumero(dou_base0));
            tab_cabecara.setValor("base_grabada_srcom", utilitario.getFormatoNumero(dou_basegraba));
            tab_cabecara.setValor("subtotal_srcom", utilitario.getFormatoNumero(dou_subtotal));
            tab_cabecara.setValor("iva_srcom", tab_factura.getValor("valor_iva_cpcno"));
            tab_cabecara.setValor("descuento_srcom", "0.00");
            tab_cabecara.setValor("total_srcom", tab_factura.getValor("total_cpcno"));
            tab_cabecara.setValor("identificacion_srcom", tab_factura.getValor("identificac_geper"));
            tab_cabecara.setValor("en_nube_srcom", "false");
            tab_cabecara.setValor("ide_geper", tab_factura.getValor("ide_geper"));
            tab_cabecara.setValor("ide_cntdo", tab_factura.getValor("ide_cntdo"));
            tab_cabecara.setValor("forma_cobro_srcom", tab_factura.getValor("alterno_ats"));
            tab_cabecara.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cabecara.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            //tab_cabecara.setValor("numero_cpcno", tab_factura.getValor("secuencial_cccfa")); !!!!!SOLO PARA PRUEBAS COMENTADO
            tab_cabecara.setValor("correo_srcom", tab_factura.getValor("correo_geper"));

            tab_cabecara.guardar();
            ide_srcom = tab_cabecara.getValor("ide_srcom");

//////            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
//////                tab_detalle.insertar();
//////                tab_detalle.setValor("ide_srcom", ide_srcom);
//////                tab_detalle.setValor("codigo_principal_srdec", tab_factura.getValor(i, "ide_inarti"));
//////                tab_detalle.setValor("codigo_auxiliar_srdec", tab_factura.getValor(i, "codigo_inarti"));
//////                String descripcion = "";
//////                if (tab_factura.getValor(i, "nombre_inuni") != null) {
//////                    descripcion += tab_factura.getValor(i, "nombre_inuni");
//////                }
//////                descripcion += " " + tab_factura.getValor(i, "nombre_inarti");
//////                tab_detalle.setValor("descripcion_srdec", descripcion.trim());
//////                tab_detalle.setValor("cantidad_srdec", tab_factura.getValor(i, "cantidad_cpdno"));
//////                tab_detalle.setValor("precio_srdec", tab_factura.getValor(i, "precio_cpdno"));
//////                tab_detalle.setValor("descuento_detalle_srdec", "0.00");
//////                tab_detalle.setValor("total_detalle_srdec", tab_factura.getValor(i, "valor_cpdno"));
//////
//////                if (tab_factura.getValor(i, "iva_inarti_cpdno").equalsIgnoreCase("1")) {
//////                    tab_detalle.setValor("porcentaje_iva_srdec", String.valueOf((ser_configuracion.getPorcentajeIva() * 100)));
//////                } else {
//////                    tab_detalle.setValor("porcentaje_iva_srdec", "0");
//////                }
//////            }
//////            if (tab_cabecara.isFilaModificada()) {
//////                //elimina detalles si modifica
//////                utilitario.getConexion().agregarSqlPantalla("delete from sri_detalle_comprobante where ide_srcom is null");
//////            }
//////            tab_detalle.guardar();
            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                //Si la Nota de Credito es nueva Asigna nuevo secuencial
                if (tab_cabecara.getValor("secuencial_srcom") == null) {
                    String strSecuencialF = getSecuencialComprobante(TipoComprobanteEnum.NOTA_DE_CREDITO);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET secuencial_srcom='" + strSecuencialF + "' where ide_srcom=" + ide_srcom);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET reutiliza_srcom= false where secuencial_srcom='" + strSecuencialF + "' and reutiliza_srcom=true and coddoc_srcom='" + TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo() + "'");
                    utilitario.getConexion().ejecutarSql("UPDATE cxp_cabecera_nota SET  ide_srcom=" + ide_srcom + ", numero_cpcno='" + strSecuencialF + "' where ide_cpcno=" + ide_cpcno);
                }
                //Si esta en estado PENDIENTE genero nueva clave de acceso por si se modifico la fecha
                if (tab_cabecara.getValor("ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()))) {
                    try {
                        Comprobante comprobanteNotaC = comprobanteService.getComprobantePorId(Long.parseLong(ide_srcom));
                        String strClaveAcceso = comprobanteService.getClaveAcceso(comprobanteNotaC);
                        utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET claveacceso_srcom='" + strClaveAcceso + "' where ide_srcom=" + ide_srcom);
                    } catch (NumberFormatException | GenericException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ide_srcom;
    }

    /**
     * Genera un comprobante electrónico a partir de una guia de remision
     * guardada
     *
     * @param ide_ccgui
     * @return
     */
    public String generarGuiaRemisionElectronica(String ide_ccgui) {
        String ide_srcom = "-1";

        TablaGenerica tab_guia = utilitario.consultar("select serie_ccdaf,fecha_emision_ccgui,identificac_geper,\n"
                + "a.gen_ide_geper,ide_cntdo,a.ide_srcom, e.ide_srcom as ide_srcom_factura,a.placa_gecam,fecha_ini_trasla_ccgui,fecha_fin_trasla_ccgui,punto_partida_ccgui,correo_cccfa \n"
                + "from cxc_guia a\n"
                + "inner join gen_persona b on a.gen_ide_geper = b.ide_geper  \n"
                + "left join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
                + "inner join cxc_cabece_factura e on a.ide_cccfa=e.ide_cccfa "
                + "where a.ide_ccgui=" + ide_ccgui);

        if (tab_guia.isEmpty() == false) {
            if (tab_guia.getValor("ide_srcom") != null) {
                ide_srcom = tab_guia.getValor("ide_srcom");
            }
            TablaGenerica tab_cabecara = new TablaGenerica();
            tab_cabecara.setTabla("sri_comprobante", "ide_srcom");
            tab_cabecara.setCondicion("ide_srcom=" + ide_srcom);
            tab_cabecara.ejecutarSql();
            //Inserta cabecera
            if (tab_cabecara.isEmpty()) {
                tab_cabecara.insertar();
            } else {
                tab_cabecara.modificar(tab_cabecara.getFilaActual());
            }

            tab_cabecara.setValor("sri_ide_srcom", tab_guia.getValor("ide_srcom_factura"));
            tab_cabecara.setValor("placa_srcom", tab_guia.getValor("placa_gecam"));
            tab_cabecara.setValor("fecha_fin_trans_srcom", tab_guia.getValor("fecha_fin_trasla_ccgui"));
            tab_cabecara.setValor("fecha_ini_trans_srcom", tab_guia.getValor("fecha_ini_trasla_ccgui"));
            tab_cabecara.setValor("direcion_partida_srcom", tab_guia.getValor("punto_partida_ccgui"));

            tab_cabecara.setValor("ide_sresc", String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()));
            tab_cabecara.setValor("coddoc_srcom", TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo());
            tab_cabecara.setValor("tipoemision_srcom", TipoEmisionEnum.NORMAL.getCodigo());
            tab_cabecara.setValor("estab_srcom", tab_guia.getValor("serie_ccdaf").substring(0, 3));
            tab_cabecara.setValor("ptoemi_srcom", tab_guia.getValor("serie_ccdaf").substring(3, 6));
            tab_cabecara.setValor("fechaemision_srcom", tab_guia.getValor("fecha_emision_ccgui"));
            tab_cabecara.setValor("reutiliza_srcom", "false");//no reutiliza por defecto
            tab_cabecara.setValor("fecha_sistema_srcom", utilitario.getFechaActual());
            tab_cabecara.setValor("ip_genera_srcom", utilitario.getIp());
            tab_cabecara.setValor("identificacion_srcom", tab_guia.getValor("identificac_geper"));
            tab_cabecara.setValor("en_nube_srcom", "false");
            tab_cabecara.setValor("ide_geper", tab_guia.getValor("gen_ide_geper"));
            tab_cabecara.setValor("ide_cntdo", "7"); //Guia de Remisión
            tab_cabecara.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cabecara.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_cabecara.setValor("correo_srcom", tab_guia.getValor("correo_cccfa"));

            if (tab_cabecara.guardar()) {
                ide_srcom = tab_cabecara.getValor("ide_srcom");
                if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                    //Si la Nota de Credito es nueva Asigna nuevo secuencial
                    if (tab_cabecara.getValor("secuencial_srcom") == null) {
                        String strSecuencialF = getSecuencialComprobante(TipoComprobanteEnum.GUIA_DE_REMISION);
                        utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET secuencial_srcom='" + strSecuencialF + "' where ide_srcom=" + ide_srcom);
                        utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET num_guia_srcom='" + tab_cabecara.getValor("estab_srcom") + "-" + tab_cabecara.getValor("ptoemi_srcom") + "-" + strSecuencialF + "' where ide_srcom=" + tab_cabecara.getValor("sri_ide_srcom"));
                        utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET reutiliza_srcom= false where secuencial_srcom='" + strSecuencialF + "' and reutiliza_srcom=true and coddoc_srcom='" + TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo() + "'");
                        utilitario.getConexion().ejecutarSql("UPDATE cxc_guia SET  ide_srcom=" + ide_srcom + ", numero_ccgui='" + strSecuencialF + "' where ide_ccgui=" + ide_ccgui);
                    }
                    //Si esta en estado PENDIENTE genero nueva clave de acceso por si se modifico la fecha
                    if (tab_cabecara.getValor("ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()))) {
                        try {
                            Comprobante comprobanteNotaC = comprobanteService.getComprobantePorId(Long.parseLong(ide_srcom));
                            String strClaveAcceso = comprobanteService.getClaveAcceso(comprobanteNotaC);
                            utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET claveacceso_srcom='" + strClaveAcceso + "' where ide_srcom=" + ide_srcom);
                        } catch (NumberFormatException | GenericException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return ide_srcom;
    }

    /**
     * Genera un comprobante electrónico a partir de un comprobante de retencion
     * guardada
     *
     * @param ide_cncre
     *
     * @return
     */
    public String generarRetencionElectronica(String ide_cncre) {
        String ide_srcom = "-1";

        TablaGenerica tab_factura = utilitario.consultar("select serie_ccdaf,fecha_emisi_cncre,identificac_geper"
                + ",e.ide_geper,a.ide_srcom,correo_cncre,"
                + "(select sum(valor_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre ) as valor_cndre "
                + "from con_cabece_retenc a "
                + "inner join cxp_cabece_factur e on a.ide_cncre=e.ide_cncre\n"
                + "inner join gen_persona b on e.ide_geper = b.ide_geper  \n"
                + "inner join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
                + "where a.ide_cncre=" + ide_cncre);

        if (tab_factura.isEmpty() == false) {
            if (tab_factura.getValor("ide_srcom") != null) {
                ide_srcom = tab_factura.getValor("ide_srcom");
            }
            TablaGenerica tab_cabecara = new TablaGenerica();
            tab_cabecara.setTabla("sri_comprobante", "ide_srcom");
            tab_cabecara.setCondicion("ide_srcom=" + ide_srcom);
            tab_cabecara.ejecutarSql();
            //Inserta cabecera
            if (tab_cabecara.isEmpty()) {
                tab_cabecara.insertar();
            } else {
                tab_cabecara.modificar(tab_cabecara.getFilaActual());
            }
            tab_cabecara.setValor("ide_sresc", String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()));
            tab_cabecara.setValor("coddoc_srcom", TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo());
            tab_cabecara.setValor("tipoemision_srcom", TipoEmisionEnum.NORMAL.getCodigo());
            tab_cabecara.setValor("estab_srcom", tab_factura.getValor("serie_ccdaf").substring(0, 3));
            tab_cabecara.setValor("ptoemi_srcom", tab_factura.getValor("serie_ccdaf").substring(3, 6));
            tab_cabecara.setValor("fechaemision_srcom", tab_factura.getValor("fecha_emisi_cncre"));
            tab_cabecara.setValor("reutiliza_srcom", "false");//no reutiliza por defecto
            tab_cabecara.setValor("fecha_sistema_srcom", utilitario.getFechaActual());
            tab_cabecara.setValor("ip_genera_srcom", utilitario.getIp());
            tab_cabecara.setValor("correo_srcom", tab_factura.getValor("correo_cncre"));

            tab_cabecara.setValor("identificacion_srcom", tab_factura.getValor("identificac_geper"));
            tab_cabecara.setValor("en_nube_srcom", "false");
            tab_cabecara.setValor("ide_geper", tab_factura.getValor("ide_geper"));
            tab_cabecara.setValor("ide_cntdo", "8");  // tipo retención ---PONER PARÁMETRO 
            tab_cabecara.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cabecara.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_cabecara.setValor("periodo_fiscal_srcom", utilitario.getMes(tab_factura.getValor("fecha_emisi_cncre")) < 10 ? "0" + utilitario.getMes(tab_factura.getValor("fecha_emisi_cncre")) + "/" + utilitario.getAnio(tab_factura.getValor("fecha_emisi_cncre")) : utilitario.getMes(tab_factura.getValor("fecha_emisi_cncre")) + "/" + utilitario.getAnio(tab_factura.getValor("fecha_emisi_cncre")));
            tab_cabecara.setValor("total_srcom", utilitario.getFormatoNumero(tab_factura.getValor("valor_cndre")));
            tab_cabecara.guardar();
            ide_srcom = tab_cabecara.getValor("ide_srcom");

            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                //Si la Nota de Credito es nueva Asigna nuevo secuencial
                if (tab_cabecara.getValor("secuencial_srcom") == null) {
                    String strSecuencialF = getSecuencialComprobante(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET secuencial_srcom='" + strSecuencialF + "' where ide_srcom=" + ide_srcom);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET reutiliza_srcom= false where secuencial_srcom='" + strSecuencialF + "' and reutiliza_srcom=true and coddoc_srcom='" + TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo() + "'");
                    utilitario.getConexion().ejecutarSql("UPDATE con_cabece_retenc SET  ide_srcom=" + ide_srcom + " , numero_cncre='" + tab_factura.getValor("serie_ccdaf") + strSecuencialF + "' where ide_cncre=" + ide_cncre);
                }
                //Si esta en estado PENDIENTE genero nueva clave de acceso por si se modifico la fecha
                if (tab_cabecara.getValor("ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()))) {
                    try {
                        Comprobante comprobanteNotaC = comprobanteService.getComprobantePorId(Long.parseLong(ide_srcom));
                        String strClaveAcceso = comprobanteService.getClaveAcceso(comprobanteNotaC);
                        utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET claveacceso_srcom='" + strClaveAcceso + "' where ide_srcom=" + ide_srcom);
                        utilitario.getConexion().ejecutarSql("UPDATE con_cabece_retenc SET  autorizacion_cncre='" + strClaveAcceso + "' where ide_cncre=" + ide_cncre);
                    } catch (NumberFormatException | GenericException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ide_srcom;
    }

    /**
     * Retorna el secuencial de los comprobantes Electrónicos
     *
     * @param tipoComprobante
     * @return
     */
    public String getSecuencialComprobante(TipoComprobanteEnum tipoComprobante) {
        long maximo = 0;
        //reutiliza secuenciales si existe
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT secuencial_srcom FROM sri_comprobante where coddoc_srcom='").append(tipoComprobante.getCodigo()).append("' and reutiliza_srcom=true  and ide_sucu=").append(utilitario.getVariable("ide_sucu")).append(" order by secuencial_srcom limit 1");
            List lisResultado = utilitario.getConexion().consultar(sql.toString());
            return (String.valueOf(lisResultado.get(0)));
        } catch (Exception e) {
        }

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT MAX(secuencial_srcom) FROM sri_comprobante where coddoc_srcom='").append(tipoComprobante.getCodigo()).append("' and ide_sucu=").append(utilitario.getVariable("ide_sucu"));
            List lisResultado = utilitario.getConexion().consultar(sql.toString());
            maximo = Long.parseLong(String.valueOf(lisResultado.get(0)));
            //System.out.println("///// " + maximo);
        } catch (Exception e) {
        }

        if (maximo == 0) {
            //Busca numero inicial en tabla de datos de emision
            try {
                String sql = "select num_inicia_ccdaf from cxc_datos_fac where es_electronica_ccdaf=true and ide_sucu=" + utilitario.getVariable("ide_sucu");
                if (tipoComprobante.getDescripcion().equals(TipoComprobanteEnum.FACTURA.getDescripcion())) {
                    sql += " and ide_cntdoc=3 ";
                } else if (tipoComprobante.getDescripcion().equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getDescripcion())) {
                    sql += " and ide_cntdoc=0 ";
                } else if (tipoComprobante.getDescripcion().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getDescripcion())) {
                    sql += " and ide_cntdoc=7 ";
                } else if (tipoComprobante.getDescripcion().equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getDescripcion())) {
                    sql += " and ide_cntdoc=8 ";
                }
                List lisResultado = utilitario.getConexion().consultar(sql);
                String ini = String.valueOf(lisResultado.get(0));
                String secuencial = String.format("%09d", new Object[]{Integer.parseInt(ini)});
                return secuencial;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        maximo++;
        Formatter fmt = new Formatter();
        String secuencial = fmt.format("%09d", maximo).toString();
        fmt.close();
        return secuencial;
    }

    public String enviarComprobante(String claveAcceso) {
        String mensaje = "";
        try {
            comprobanteService.enviarComprobante(claveAcceso);
        } catch (Exception e) {
            mensaje = e.getMessage() + "";
        }
        return mensaje;
    }

    public void getRIDE(String ide_srcom) throws GenericException {
        try {
            byte[] ar = archivoService.getPdf(comprobanteService.getComprobantePorId(new Long(ide_srcom)));
            //crea el archivo 
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String realPath = (String) ec.getRealPath("/reportes");
            File fil_reporte = new File(realPath + "/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf");
            FileOutputStream fileOuputStream = new FileOutputStream(fil_reporte);
            fileOuputStream.write(ar);
        } catch (NumberFormatException | GenericException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getSqlXmlComprobante(String ide_srcom) {
        return "select xml_srxmc ,msg_recepcion_srxmc,msg_autoriza_srxmc from sri_xml_comprobante where ide_srcom=" + ide_srcom;
    }

    public int getNumeroComprobantesporEstado(TipoComprobanteEnum tipo, EstadoComprobanteEnum estado) {
        String sql = "SELECT coddoc_srcom,count(coddoc_srcom) as num FROM sri_comprobante WHERE coddoc_srcom =" + tipo.getCodigo() + "  AND ide_sresc=" + estado.getCodigo() + " group by coddoc_srcom,ide_sresc";
        TablaGenerica tab = utilitario.consultar(sql);
        return tab.getTotalFilas();
    }

    /**
     * Retorna el número de comprobantes por estado
     *
     * @param fecha_inicio
     * @param fecha_fin
     * @param tipoComprobante
     * @return
     */
    public String getSqlTotalComprobantesPorEstado(String fecha_inicio, String fecha_fin, TipoComprobanteEnum tipoComprobante) {
        return "select count(1) as contador,nombre_sresc,a.ide_sresc from sri_comprobante a\n"
                + "inner join sri_estado_comprobante b on a.ide_sresc=b.ide_sresc\n"
                + "where coddoc_srcom='" + tipoComprobante.getCodigo() + "'\n"
                + "and fechaemision_srcom BETWEEN '" + fecha_inicio + "' and '" + fecha_fin + "'\n"
                + "and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY nombre_sresc,a.ide_sresc";
    }

    /**
     * Retorna un comprobante electronico
     *
     * @param ide_srcom
     * @return
     */
    public String getSqlComprobanteElectronico(String ide_srcom) {
        return "SELECT * from sri_comprobante where ide_srcom=" + ide_srcom;
    }

    /**
     * Reenvia al correo de un cliente el comprobante
     *
     * @param correo
     * @param ide_srcom
     */
    public void reenviarComprobante(String correo, String ide_srcom) {
        try {
            cPanelService.reenviarComprobante(correo, Long.parseLong(ide_srcom));
            utilitario.agregarMensaje("Se envio correctamente", "");
        } catch (NumberFormatException | GenericException e) {
            utilitario.crearError("Error al reenviar el comprobante electrónico", "En el método reenviarComprobante", e);
        }
    }

}

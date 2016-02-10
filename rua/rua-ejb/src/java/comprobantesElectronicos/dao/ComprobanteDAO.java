/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Clavecontingencia;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Tipocomprobante;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ComprobanteDAO implements ComprobanteDAOLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private EstadoComprobanteDAOLocal estadoComprobanteDAO;
    
    @EJB
    private TipoComprobanteDAOLocal tipoComprobanteDAO;
    
    private final Utilitario utilitario = new Utilitario();
    
    @Override
    public Comprobante getComprobanteporNumero(String estab, String ptoEmi, String secuencial) {
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE secuencial_srcom ='" + secuencial + "' and ptoemi_srcom='" + ptoEmi + "' and estab_srcom='" + estab + "'");
        if (tab_consulta.isEmpty() == false) {
            return new Comprobante(tab_consulta);
        }
        return null;
    }
    
    @Override
    public void actualizarClaveAccesoyEstado(Comprobante comprobante, String claveAcceso, Estadocomprobante estadoComprobante) {
        comprobante.setClaveacceso(claveAcceso);
        comprobante.setCodigoestado(estadoComprobante);
        actualizar(comprobante);
    }
    
    @Override
    public void actualizarEstado(Comprobante comprobante, Estadocomprobante estadoComprobante) {
        comprobante.setCodigoestado(estadoComprobante);
        actualizar(comprobante);
    }
    
    @Override
    public void actualizarClaveAcceso(Comprobante comprobante, String claveAcceso) {
        comprobante.setClaveacceso(claveAcceso);
        actualizar(comprobante);
    }
    
    @Override
    public void actualizarClaveContingencia(Comprobante comprobante, Clavecontingencia claveContingencia) {
        comprobante.setCodigoclave(claveContingencia);
        actualizar(comprobante);
    }
    
    @Override
    public Comprobante getComprobanteporCodigocomprobante(String ide_srcom) {
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE ide_srcom =" + ide_srcom + "");
        if (tab_consulta.isEmpty() == false) {
            return new Comprobante(tab_consulta);
        }
        return null;
    }
    
    @Override
    public Comprobante getComprobanteporClaveAcceso(String claveAcceso) {
        
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE claveacceso_srcom='" + claveAcceso + "'");
        Comprobante comprobante = new Comprobante(tab_consulta);
        return comprobante;
        
    }
    
    @Override
    public List<Comprobante> getComprobantesEstado(Estadocomprobante estadoComprobante) {
        List<Comprobante> lisComprobantesEstado = new ArrayList();
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE va_estado_comprobante='" + estadoComprobante.getCodigoestado() + "'");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantesEstado.add(new Comprobante(tab_consulta, i));
        }
        return lisComprobantesEstado;
    }
    
    public void actualizar(Comprobante comprobante) {
        
        String va_estado_comprobante = comprobante.getCodigoestado() == null ? "Null" : "'" + comprobante.getCodigoestado().getCodigoestado() + "'";
        String va_clave_acceso = comprobante.getClaveacceso() == null ? "Null" : "'" + comprobante.getClaveacceso() + "'";
        String va_firma = comprobante.getCodigofirma() == null ? "Null" : "" + comprobante.getCodigofirma().getCodigofirma() + "";
        String va_clave_contingencia = comprobante.getCodigoclave() == null ? "Null" : "" + comprobante.getCodigoclave().getCodigoclave() + "";
        String va_autorizacion_sri = comprobante.getNumAutorizacion() == null ? "Null" : "'" + comprobante.getNumAutorizacion() + "'";
        String va_tipo_emision = comprobante.getTipoemision() == null ? "Null" : "" + comprobante.getTipoemision() + "";
        String va_fec_autoriza = comprobante.getFechaautoriza() == null ? "Null" : "'" + utilitario.getFormatoFechaHora(comprobante.getFechaautoriza()) + "'";
        
        String sql = "UPDATE sri_comprobante set"
                + " ide_sresc=" + va_estado_comprobante
                + " ,claveacceso_srcom=" + va_clave_acceso
                + " ,ide_srfid =" + va_firma
                + " ,ide_srclc=" + va_clave_contingencia
                + " ,autorizacion_srcom =" + va_autorizacion_sri
                + " ,tipoemision_srcom=" + va_tipo_emision
                + " ,fechaautoriza_srcom=" + va_fec_autoriza
                + " WHERE ide_srcom =" + comprobante.getCodigocomprobante();
        utilitario.getConexion().ejecutarSql(sql);
        
    }
    
    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion_srcom) {
        
        List<Comprobante> lisComprobantes = new ArrayList();
        
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE identificacion_srcom='" + identificacion_srcom + "' and ide_sresc=" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + " order by fechaemision_srcom desc");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantes.add(new Comprobante(tab_consulta, i));
        }
        
        return lisComprobantes;
    }
    
    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion_srcom, Tipocomprobante tipoComprobante) {
        List<Comprobante> lisComprobantes = new ArrayList();
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE identificacion_srcom='" + identificacion_srcom + "' and coddoc_srcom='" + tipoComprobante.getAlternotipcomp() + "' and ide_sresc=" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + " order by fechaemision_srcom desc");
        tab_consulta.imprimirSql();
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantes.add(new Comprobante(tab_consulta, i));
        }
        return lisComprobantes;
    }
    
    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion_srcom, String secuencial) {
        
        List<Comprobante> lisComprobantes = new ArrayList();
        
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE identificacion_srcom='" + identificacion_srcom + "' and secuencial_srcom='" + secuencial + "' and ide_sresc=" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + " order by fechaemision_srcom desc");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantes.add(new Comprobante(tab_consulta, i));
        }
        
        return lisComprobantes;
    }
    
    @Override
    public List<Comprobante> getComprobantesTipo(Tipocomprobante tipoComprobante) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Comprobante> getComprobantesTipoEstado(Tipocomprobante tipoComprobante, Estadocomprobante estadoComprobante) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String guardarComprobanteFactura(Tabla tab_factura, Tabla tab_detalle) {
        //Guarda la factura en la tabla de comprobantes electronicos
        TablaGenerica tab_datos_factura = utilitario.consultar("SELECT * FROM cxc_datos_fac WHERE ide_ccdaf=" + tab_factura.getValor("ide_ccdaf"));
        TablaGenerica tab_persona = utilitario.consultar("select ide_geper,identificac_geper from gen_persona where ide_geper=" + tab_factura.getValor("ide_geper"));
        String serie = tab_datos_factura.getValor("serie_ccdaf");
        String estab = null;
        String ptoEmi = null;
        //Separa el stablecimiento y el punto de emisión
        if (serie.length() == 6) {
            estab = serie.substring(3);
            ptoEmi = serie.substring(3, 6);
        }
        
        TablaGenerica tab_comprobante = new TablaGenerica();
        Tipocomprobante tic_factura = tipoComprobanteDAO.getTipoFactura();
        tab_comprobante.setTabla("sri_comprobante", "ide_srcom", -1);
        tab_comprobante.setCondicion("ide_srcom=-1");
        tab_comprobante.ejecutarSql();
        tab_comprobante.insertar();
        tab_comprobante.setValor("ide_sresc", String.valueOf(estadoComprobanteDAO.getEstadoContingencia().getCodigoestado()));
        tab_comprobante.setValor("ide_cntdo", String.valueOf(tic_factura.getCodigotipcomp()));
        tab_comprobante.setValor("ide_cccfa", tab_factura.getValor("ide_cccfa"));
        tab_comprobante.setValor("tipoemision_srcom", "1"); //EMISION NORMAL        
        tab_comprobante.setValor("coddoc_srcom", tic_factura.getAlternotipcomp());
        tab_comprobante.setValor("claveacceso_srcom", "[CLAVEACCESO]");
        tab_comprobante.setValor("estab_srcom", estab);
        tab_comprobante.setValor("ptoemi_srcom", ptoEmi);
        tab_comprobante.setValor("secuencial_srcom", tab_factura.getValor("secuencial_cccfa"));
        tab_comprobante.setValor("fechaemision_srcom", tab_factura.getValor("fecha_emisi_cccfa"));
        tab_comprobante.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
        tab_comprobante.setValor("ide_empr", utilitario.getVariable("ide_empr"));
        tab_comprobante.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_comprobante.setValor("identificacion_srcom", tab_persona.getValor("identificac_geper"));
        
        if (tab_comprobante.guardar()) {
            TablaGenerica tab_xml_comprobante = new TablaGenerica();
            tab_xml_comprobante.setTabla("sri_xml_comprobante", "ide_srxmc", -1);
            tab_xml_comprobante.setCondicion("ide_srxmc=-1");
            tab_xml_comprobante.setGenerarPrimaria(false);
            tab_xml_comprobante.getColumna("ide_srxmc").setExterna(true);
            tab_xml_comprobante.getColumna("fecha_hora_srxmc").setTipoJava("java.lang.String");
            tab_xml_comprobante.getColumna("fecha_hora_srxmc").setTipo("Text");
            tab_xml_comprobante.ejecutarSql();
            tab_xml_comprobante.insertar();
            tab_xml_comprobante.setValor("ide_srcom", tab_comprobante.getValor("ide_srcom"));
            tab_xml_comprobante.setValor("ide_sresc", String.valueOf(estadoComprobanteDAO.getEstadoContingencia().getCodigoestado()));
            tab_xml_comprobante.setValor("fecha_hora_srxmc", utilitario.getFormatoFechaHora(utilitario.getFechaHoraActual()));
            tab_xml_comprobante.setValor("xml_srxmc", getXmlFactura(tab_factura, tab_detalle));
            if (tab_xml_comprobante.guardar()) {
                return tab_comprobante.getValor("ide_srcom");
            }
        }
        return null;
    }

    /**
     * Genera el xml de comprobante factura
     *
     * @param ide_cccfa
     * @return
     */
    private String getXmlFactura(Tabla tab_factura, Tabla tab_detalle_factura) {
        
        String str_xml = null;
        if (tab_factura.isEmpty() == false) {
            String ambiente = utilitario.getVariable("p_sri_ambiente_comp_elect");  //********* variable   1=pruebas   2=produccion
            String tipoEmision = "1"; //NORMAL
            String moneda = utilitario.getVariable("p_sri_moneda_comp_elect");
            
            TablaGenerica tab_empresa = utilitario.consultar("SELECT * FROM sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
            TablaGenerica tab_persona = utilitario.consultar("SELECT ide_geper,alterno2_getid,nom_geper,identificac_geper,direccion_geper,telefono_geper,correo_geper FROM gen_persona  a\n"
                    + "inner join gen_tipo_identifi b on a.ide_getid=b.ide_getid\n"
                    + "where ide_geper=" + tab_factura.getValor("ide_geper"));
            TablaGenerica tab_datos_factura = utilitario.consultar("SELECT * FROM cxc_datos_fac WHERE ide_ccdaf=" + tab_factura.getValor("ide_ccdaf"));
            
            String serie = tab_datos_factura.getValor("serie_ccdaf");
            String estab = null;
            String ptoEmi = null;
            //Separa el stablecimiento y el punto de emisión
            if (serie.length() == 6) {
                estab = serie.substring(3);
                ptoEmi = serie.substring(3, 6);
            }
            //tab_comprobante.setValor("guiaremision_srcom", "");
            double dou_base_no_objeto_iva = 0;
            double dou_base_tarifa0 = 0;
            double dou_base_grabada = 0;
            try {
                dou_base_no_objeto_iva = Double.parseDouble(tab_factura.getValor("base_no_objeto_iva_cccfa"));
            } catch (Exception e) {
            }
            try {
                dou_base_tarifa0 = Double.parseDouble(tab_factura.getValor("base_tarifa0_cccfa"));
            } catch (Exception e) {
            }
            try {
                dou_base_grabada = Double.parseDouble(tab_factura.getValor("base_grabada_cccfa"));
            } catch (Exception e) {
            }
            //totalImpuesto
            String codigoIva = utilitario.getVariable("p_sri_codigoIva_comp_elect"); //2 CODIGO SRI IVA            //*********!!!Poner variable
            String codigoPorcentajeIva = utilitario.getVariable("p_sri_codigoPorcentajeIva_comp_elect");    //2 SI IVA       //*********!!!Poner variable   
            String codigoPorcentajeIva0 = utilitario.getVariable("p_sri_codigoPorcentajeIva0_comp_elect");      //0 NO IVA      //*********!!!Poner variable  
            String codigoPorcentajeIvaNoObjeto = utilitario.getVariable("p_sri_codigoPorcentajeIvaNoObjeto_comp_elect"); //6 NO IVA  //*********!!!Poner variable  
            String PorcentajeIva = utilitario.getVariable("p_sri_porcentajeIva_comp_elect"); //12.00 PORCENTAJE IVA    //*********!!!Poner variable
            String PorcentajeIva0 = "0.00"; //PORCENTAJE NO IVA 

            double totalSinImpuestos = dou_base_no_objeto_iva + dou_base_tarifa0 + dou_base_grabada;
            str_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> \n"
                    + "     <factura id=\"comprobante\" version=\"1.1.0\"> \n"
                    + "		<infoTributaria> \n"
                    + "			<ambiente>" + ambiente + "</ambiente> \n"
                    + "			<tipoEmision>" + tipoEmision + "</tipoEmision> \n"
                    + "			<razonSocial>" + tab_empresa.getValor("nom_corto_empr") + "</razonSocial> \n"
                    + "			<nombreComercial>" + tab_empresa.getValor("nom_empr") + "</nombreComercial> \n"
                    + "			<ruc>" + tab_empresa.getValor("identificacion_empr") + "</ruc> \n"
                    + "			<claveAcceso>[CLAVEACCESO]</claveAcceso> \n"
                    + "			<codDoc>" + tipoComprobanteDAO.getTipoFactura().getAlternotipcomp() + "</codDoc> \n"
                    + "			<estab>" + estab + "</estab> \n"
                    + "			<ptoEmi>" + ptoEmi + "</ptoEmi> \n"
                    + "			<secuencial>" + tab_factura.getValor("secuencial_cccfa") + "</secuencial> \n"
                    + "			<dirMatriz>" + tab_empresa.getValor("direccion_empr") + "</dirMatriz> \n"
                    + "		</infoTributaria> \n"
                    + "		<infoFactura> \n"
                    + "			<fechaEmision>" + utilitario.getFormatoFecha(utilitario.getFecha(tab_factura.getValor("fecha_emisi_cccfa")), "dd/MM/yyyy") + "</fechaEmision> \n"
                    + "			<dirEstablecimiento>" + tab_empresa.getValor("direccion_empr") + "</dirEstablecimiento> \n"
                    + "			<contribuyenteEspecial>" + tab_empresa.getValor("contribuyenteespecial_empr") + "</contribuyenteEspecial> \n"
                    + "			<obligadoContabilidad>" + tab_empresa.getValor("obligadocontabilidad_empr") + "</obligadoContabilidad> \n"
                    + "			<tipoIdentificacionComprador>" + tab_persona.getValor("alterno2_getid") + "</tipoIdentificacionComprador> \n"
                    + "			<razonSocialComprador>" + tab_persona.getValor("nom_geper") + "</razonSocialComprador> \n"
                    + "			<identificacionComprador>" + tab_persona.getValor("identificac_geper") + "</identificacionComprador> \n"
                    + "			<totalSinImpuestos>" + utilitario.getFormatoNumero(totalSinImpuestos) + "</totalSinImpuestos> \n"
                    + "			<totalDescuento>" + utilitario.getFormatoNumero(0) + "</totalDescuento> \n"
                    + "			<totalConImpuestos> \n"
                    + "				<totalImpuesto> \n"
                    + "					<codigo>" + codigoIva + "</codigo> \n"
                    + "					<codigoPorcentaje>" + codigoPorcentajeIva + "</codigoPorcentaje> \n"
                    + "					<descuentoAdicional>" + utilitario.getFormatoNumero(0) + "</descuentoAdicional> \n"
                    + "					<baseImponible>" + tab_factura.getValor("base_grabada_cccfa") + "</baseImponible> \n"
                    + "					<valor>" + tab_factura.getValor("valor_iva_cccfa") + "</valor> \n"
                    + "				</totalImpuesto> \n"
                    + "			</totalConImpuestos> \n"
                    + "			<propina>" + utilitario.getFormatoNumero(0) + "</propina> \n"
                    + "			<importeTotal>" + tab_factura.getValor("total_cccfa") + "</importeTotal> \n"
                    + "			<moneda>" + moneda + "</moneda> \n"
                    + "		</infoFactura> \n"
                    + "		<detalles> \n";
            for (int i = 0; i < tab_detalle_factura.getTotalFilas(); i++) {
                TablaGenerica tab_articulo = utilitario.consultar("SELECT codigo_inarti,nombre_inarti FROM inv_articulo where ide_inarti=" + tab_detalle_factura.getValor(i, "ide_inarti"));
                str_xml += "			<detalle> \n"
                        + "				<codigoPrincipal>" + tab_detalle_factura.getValor(i, "ide_inarti") + "</codigoPrincipal> \n"
                        + "				<codigoAuxiliar>" + tab_articulo.getValor("codigo_inarti") + "</codigoAuxiliar> \n"
                        + "				<descripcion>" + tab_articulo.getValor("nombre_inarti") + "</descripcion> \n"
                        + "				<cantidad>" + tab_detalle_factura.getValor(i, "cantidad_ccdfa") + "</cantidad> \n"
                        + "				<precioUnitario>" + tab_detalle_factura.getValor(i, "precio_ccdfa") + "</precioUnitario> \n"
                        + "				<descuento>" + utilitario.getFormatoNumero(0) + "</descuento> \n"
                        + "				<precioTotalSinImpuesto>" + tab_detalle_factura.getValor(i, "total_ccdfa") + "</precioTotalSinImpuesto> \n"
                        + "				<impuestos> \n"
                        + "					<impuesto> \n"
                        + "						<codigo>" + codigoIva + "</codigo> \n"
                        + "						<codigoPorcentaje>" + (tab_detalle_factura.getValor(i, "precio_ccdfa").equals("1") ? codigoPorcentajeIva : tab_detalle_factura.getValor(i, "precio_ccdfa").equals("-1") ? codigoPorcentajeIva0 : codigoPorcentajeIvaNoObjeto) + "</codigoPorcentaje> \n"
                        + "						<tarifa>" + (tab_detalle_factura.getValor(i, "precio_ccdfa").equals("1") ? PorcentajeIva : PorcentajeIva0) + "</tarifa> \n"
                        + "						<baseImponible>" + tab_detalle_factura.getValor(i, "total_ccdfa") + "</baseImponible> \n"
                        + "						<valor>" + (Double.parseDouble(tab_detalle_factura.getValor(i, "total_ccdfa")) * ((Double.parseDouble((tab_detalle_factura.getValor(i, "precio_ccdfa").equals("1") ? PorcentajeIva : PorcentajeIva0))) / 100)) + "</valor> \n"
                        + "					</impuesto>             \n"
                        + "				</impuestos> \n"
                        + "			</detalle> \n";
            }
            str_xml += "		</detalles> \n"
                    + "		<retenciones> \n"
                    + "			<retencion> \n"
                    + "				<codigo>4</codigo>             \n"
                    + "				<codigoPorcentaje>3</codigoPorcentaje> \n"
                    + "				<tarifa>1</tarifa> \n"
                    + "				<valor>0.00</valor> \n"
                    + "			</retencion> \n"
                    + "		</retenciones> \n"
                    + "		<infoAdicional> \n";
            if (tab_persona.getValor("direccion_geper") != null) {
                str_xml += "      		<campoAdicional nombre=\"Dirección\">" + tab_persona.getValor("direccion_geper") + "</campoAdicional> \n";
            }
            if (tab_persona.getValor("correo_geper") != null) {
                str_xml += "      		<campoAdicional nombre=\"Email\">" + tab_persona.getValor("correo_geper") + "</campoAdicional> \n";
            }
            if (tab_persona.getValor("telefono_geper") != null) {
                str_xml += "      		<campoAdicional nombre=\"Teléfono\">" + tab_persona.getValor("telefono_geper") + "</campoAdicional> \n";
            }
            str_xml += "		</infoAdicional> \n"
                    + "     </factura>";
        }
        
        return str_xml;
    }
    
}

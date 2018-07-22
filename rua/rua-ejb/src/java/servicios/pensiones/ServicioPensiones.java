/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.pensiones;

import framework.aplicacion.TablaGenerica;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.ServicioBase;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.contabilidad.ServicioConfiguracion;

/**
 *
 * @author djacome
 */
@Stateless

public class ServicioPensiones extends ServicioBase {

    @EJB
    private ServicioConfiguracion ser_configuracion;

    @EJB
    private ServicioComprobanteElectronico ser_comprobante_electronico;

    public String generarFacturaElectronica(String ide_) {
        String ide_cccfa_return = null;
        TablaGenerica tag = utilitario.consultar("select * from pen_tmp_lista_fact where cod_factura_petlf='" + ide_ + "'");
        if (tag.isEmpty() == false) {
            TablaGenerica tab_clientes = new TablaGenerica();
            tab_clientes.setTabla("gen_persona", "ide_geper");
            tab_clientes.setCondicion("identificac_geper in (" + tag.getStringColumna("cedula_petlf") + ",'9999999999')");
            tab_clientes.ejecutarSql();
            //inserta facturas
            TablaGenerica tab_cab_fac = new TablaGenerica();
            tab_cab_fac.setTabla("cxc_cabece_factura", "ide_cccfa");
            tab_cab_fac.setCondicion("ide_cccfa=-1");
            tab_cab_fac.setGenerarPrimaria(false);
            tab_cab_fac.getColumna("ide_cccfa").setExterna(false);
            tab_cab_fac.ejecutarSql();

            TablaGenerica tab_det_fac = new TablaGenerica();
            tab_det_fac.setTabla("cxc_deta_factura", "ide_ccdfa");
            tab_det_fac.setCondicion("ide_ccdfa=-1");
            tab_det_fac.ejecutarSql();

            TablaGenerica tab_info_adicional = new TablaGenerica();
            tab_info_adicional.setTabla("sri_info_adicional", "ide_srina");
            tab_info_adicional.setCondicion("ide_srina=-1");
            tab_info_adicional.ejecutarSql();

            long ide_cccfa = utilitario.getConexion().getMaximo("cxc_cabece_factura", "ide_cccfa", 1);
            //Recupera porcentaje iva 
            double tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());

            String cedula_petlf = tag.getValor("cedula_petlf");
            if (cedula_petlf == null || cedula_petlf.isEmpty()) {
                cedula_petlf = "9999999999";
            }
            String ide_geper = null;
            for (int j = 0; j < tab_clientes.getTotalFilas(); j++) {
                if (cedula_petlf.equals(tab_clientes.getValor(j, "identificac_geper"))) {
                    ide_geper = tab_clientes.getValor(j, "ide_geper");
                    break;
                }
            }
            tab_cab_fac.insertar();
            tab_cab_fac.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_cab_fac.setValor("ide_cntdo", "3"); //3 = FACTURA
            tab_cab_fac.setValor("ide_ccefa", "0"); //0 = NORMAL
            tab_cab_fac.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_cab_fac.setValor("fecha_trans_cccfa", utilitario.getFechaActual());
            tab_cab_fac.setValor("fecha_emisi_cccfa", tag.getValor("fecha_petlf"));
            tab_cab_fac.setValor("dias_credito_cccfa", "0");
            tab_cab_fac.setValor("ide_cndfp", "13");//13=OTROS SIN UTILIZAR SITEMA FINANCIERO
            tab_cab_fac.setValor("ide_cndfp1", "8");//8=CRÉDITO 10 DÍAS
            tab_cab_fac.setValor("dias_credito_cccfa", "10");//10 DÍAS
            tab_cab_fac.setValor("DIRECCION_CCCFA", tag.getValor("direccion_petlf"));
            tab_cab_fac.setValor("base_grabada_cccfa", "0");
            tab_cab_fac.setValor("base_no_objeto_iva_cccfa", "0");
            tab_cab_fac.setValor("valor_iva_cccfa", "0");
            tab_cab_fac.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(tag.getValor("subtotal_petlf")));
            tab_cab_fac.setValor("descuento_cccfa", utilitario.getFormatoNumero(tag.getValor("rebaja_petlf")));
            tab_cab_fac.setValor("orden_compra_cccfa", tag.getValor("cedula_alumno_petlf"));  //Guarda la cedula del alumno 
            tab_cab_fac.setValor("total_cccfa", utilitario.getFormatoNumero(tag.getValor("total_petlf")));
            tab_cab_fac.setValor("correo_cccfa", tag.getValor("correo_petlf"));
            tab_cab_fac.setValor("pagado_cccfa", "false");
            tab_cab_fac.setValor("ide_geper", ide_geper);
            tab_cab_fac.setValor("ide_ccdaf", "2"); //2= FACTURAS ELECTRONICAS
            tab_cab_fac.setValor("telefono_cccfa", tag.getValor("telefono_petlf"));
            tab_cab_fac.setValor("tarifa_iva_cccfa", utilitario.getFormatoNumero((tarifaIVA * 100)));

            tab_det_fac.insertar();
            tab_det_fac.setValor("ide_inarti", "57"); //55 == servicios colegio
            tab_det_fac.setValor("CANTIDAD_CCDFA", "1");
            tab_det_fac.setValor("PRECIO_CCDFA", utilitario.getFormatoNumero(tag.getValor("subtotal_petlf")));
            tab_det_fac.setValor("iva_inarti_ccdfa", "-1");
            tab_det_fac.setValor("descuento_ccDfa", utilitario.getFormatoNumero(tag.getValor("rebaja_petlf")));
            tab_det_fac.setValor("total_ccdfa", utilitario.getFormatoNumero(tag.getValor("subtotal_petlf")));
            tab_det_fac.setValor("OBSERVACION_CCDFA", tag.getValor("concepto_petlf"));
            tab_det_fac.setValor("ALTERNO_CCDFA", "00");
            tab_det_fac.setValor("ide_cccfa", String.valueOf(ide_cccfa));

            //info adicional
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Código Alumno");
            tab_info_adicional.setValor("valor_srina", tag.getValor("codigo_alumno_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Nombre Alumno");
            tab_info_adicional.setValor("valor_srina", tag.getValor("nombre_alumno_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Paralelo");
            tab_info_adicional.setValor("valor_srina", tag.getValor("paralelo_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Período Lectivo");
            tab_info_adicional.setValor("valor_srina", tag.getValor("periodo_lectivo_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Recibo");
            tab_info_adicional.setValor("valor_srina", tag.getValor("cod_factura_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Usuario");
            tab_info_adicional.setValor("valor_srina", utilitario.getVariable("NICK"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));

            if (tab_cab_fac.guardar()) {
                if (tab_det_fac.guardar()) {
                    if (tab_info_adicional.guardar()) {
                        //Guarda la cuenta por cobrar
                        // ser_factura.generarTransaccionFactura(tab_cab_factura);  ///Cambiar uno a uno
                        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                            ide_cccfa_return = tab_cab_fac.getValor("ide_cccfa");
                            ser_comprobante_electronico.generarFacturaElectronica(tab_cab_fac.getValor("ide_cccfa"));
                            utilitario.getConexion().ejecutarSql("UPDATE sri_info_adicional a set ide_srcom = (select ide_srcom from cxc_cabece_factura where ide_cccfa=a.ide_cccfa) where ide_srcom IS null");
                            //elimina de la tabla temporal
                            utilitario.getConexion().ejecutarSql("delete from pen_tmp_lista_fact where cod_factura_petlf='" + ide_ + "'");
                        }
                    }
                }
            }
        }
        return ide_cccfa_return;
    }

    /**
     * Listado de Alumnos
     *
     * @return
     */
    public String getSqlAlumnos() {
        return "SELECT a.ide_geper,a.codigo_geper as CODIGO,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES,a.direccion_geper AS DIRECCION,a.telefono_geper AS TELEFONO,b.nom_geper  as REPRESENTANTE,a.ide_vgecl AS ACTIVO FROM gen_persona a left join  gen_persona b on a.rep_ide_geper = b.ide_geper where a.ide_vgtcl=1 and a.nivel_geper='HIJO' order by a.nom_geper";
    }

    public String getSqlComboAlumnos() {
        return "SELECT a.ide_geper,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES,a.codigo_geper as CODIGO FROM gen_persona a where a.ide_vgtcl=1 and a.nivel_geper='HIJO' order by a.nom_geper";
    }

    /**
     *
     * @param cedula_alumno
     * @return
     */
    public String getFacturasAlumno(String cedula_alumno) {
        return "select a.ide_cccfa, secuencial_cccfa,ide_cnccc,a.ide_ccefa,nombre_sresc as nombre_ccefa, fecha_emisi_cccfa,(select numero_cncre from con_cabece_retenc where ide_cncre=a.ide_cncre)as NUM_RETENCION,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,descuento_cccfa as DESCUENTO,valor_iva_cccfa,total_cccfa, "
                + "claveacceso_srcom as CLAVE_ACCESO, fecha_trans_cccfa,a.ide_cncre,d.ide_srcom,a.ide_geper,direccion_cccfa,orden_compra_cccfa AS NUM_REFERENCIA  "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "left join con_deta_forma_pago x on a.ide_cndfp1=x.ide_cndfp "
                + "where orden_compra_cccfa='" + cedula_alumno + "' "
                + "AND a.ide_ccefa =0"
                + " ORDER BY secuencial_cccfa desc,ide_cccfa desc";
    }

    /**
     *
     * @param ide_geper Alumno
     * @param fechaInicio Fecha Inicio
     * @param fechaFin Facha Fin
     * @return
     */
    public String getSqlFacturasAlumno(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT * FROM cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + " where a.ide_geper=" + ide_geper + " fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' ";
    }

}

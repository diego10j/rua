/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_cobrar;

import framework.componentes.Tabla;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless
public class ServicioFacturaCxC {

    private final Utilitario utilitario = new Utilitario();

    /**
     * Retorna la sentencia SQL con los puntos de emision de facturas x sucursal
     *
     * @return
     */
    public String getSqlPuntosEmision() {
        return "select ide_ccdaf,serie_ccdaf, autorizacion_ccdaf,observacion_ccdaf from cxc_datos_fac where ide_sucu=" + utilitario.getVariable("IDE_SUCU");
    }

    public void configurarTablaFacturaCxC(Tabla cabecera, Tabla detalle) {

        cabecera.setTabla("cxc_cabece_factura", "ide_cccfa", -1);
        cabecera.setMostrarNumeroRegistros(false);
        cabecera.getColumna("ide_cnccc").setVisible(false);
        cabecera.getColumna("ide_cccfa").setVisible(false);
        cabecera.getColumna("ide_cncre").setVisible(false);
        cabecera.getColumna("ide_vgven").setCombo("ven_vendedor", "ide_vgven", "nombre_vgven", "");
        cabecera.getColumna("ide_vgven").setNombreVisual("VENDEDOR");
        cabecera.getColumna("ide_cntdo").setVisible(false);
        cabecera.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));

        cabecera.getColumna("ide_ccefa").setVisible(false);
        cabecera.getColumna("ide_ccefa").setValorDefecto(utilitario.getVariable("p_cxc_estado_factura_normal"));

        cabecera.getColumna("ide_geubi").setVisible(false);

        cabecera.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        cabecera.getColumna("ide_usua").setVisible(false);

        cabecera.getColumna("fecha_trans_cccfa").setValorDefecto(utilitario.getFechaActual());
        cabecera.getColumna("fecha_trans_cccfa").setVisible(false);

        cabecera.getColumna("fecha_emisi_cccfa").setValorDefecto(utilitario.getFechaActual());

        cabecera.getColumna("ide_ccdaf").setVisible(false);

        cabecera.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        cabecera.getColumna("ide_geper").setAutoCompletar();

        cabecera.getColumna("pagado_cccfa").setValorDefecto("false");
        cabecera.getColumna("pagado_cccfa").setVisible(false);
        cabecera.getColumna("total_cccfa").setEtiqueta();
        cabecera.getColumna("total_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        cabecera.getColumna("total_cccfa").setValorDefecto("0,00");
        cabecera.getColumna("secuencial_cccfa").setEstilo("font-size: 15px;font-weight: bold;text-align-right");
        cabecera.getColumna("secuencial_cccfa").setMascara("9999999");
        cabecera.getColumna("secuencial_cccfa").setOrden(1);

        cabecera.getColumna("base_grabada_cccfa").setEtiqueta();
        cabecera.getColumna("base_grabada_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        cabecera.getColumna("base_grabada_cccfa").setValorDefecto("0");
        cabecera.getColumna("valor_iva_cccfa").setEtiqueta();
        cabecera.getColumna("valor_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        cabecera.getColumna("valor_iva_cccfa").setValorDefecto("0");
        cabecera.getColumna("base_no_objeto_iva_cccfa").setEtiqueta();
        cabecera.getColumna("base_no_objeto_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        cabecera.getColumna("base_no_objeto_iva_cccfa").setValorDefecto("0");
        cabecera.getColumna("base_tarifa0_cccfa").setEtiqueta();
        cabecera.getColumna("base_tarifa0_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        cabecera.getColumna("base_tarifa0_cccfa").setValorDefecto("0");
        cabecera.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp!=" + utilitario.getVariable("p_con_for_pag_reembolso_caja"));
        cabecera.getColumna("ide_cndfp").setPermitirNullCombo(false);
        cabecera.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
        cabecera.getColumna("solo_guardar_cccfa").setValorDefecto("false");
        cabecera.getColumna("solo_guardar_cccfa").setVisible(false);
        cabecera.getColumna("ide_vgven").setVisible(false);
        cabecera.getColumna("ide_geubi").setVisible(false);
        cabecera.getColumna("ide_usua").setVisible(false);
        cabecera.setTipoFormulario(true);
        cabecera.setRecuperarLectura(true);
        cabecera.getGrid().setColumns(6);
        cabecera.agregarRelacion(detalle);
        cabecera.setCondicion("ide_cccfa=-1");
        cabecera.setCondicionSucursal(true);

        
        detalle.setTabla("cxc_deta_factura", "ide_ccdfa", -1);
        detalle.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        detalle.getColumna("ide_inarti").setAutoCompletar();    
        detalle.getColumna("total_ccdfa").setEtiqueta();
        detalle.getColumna("total_ccdfa").setEstilo("font-size:13px;font-weight: bold;");
        detalle.getColumna("precio_promedio_ccdfa").setLectura(true);

    }

}

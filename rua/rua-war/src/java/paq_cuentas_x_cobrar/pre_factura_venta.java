/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.sistema.ServicioSistema;

/**
 *
 * @author Diego
 */
public class pre_factura_venta extends Pantalla {

    //private FacturaCxC fcc_factura = new FacturaCxC();
    private Etiqueta eti_cajero = new Etiqueta();
    private Etiqueta eti_caja = new Etiqueta();
    private Etiqueta eti_emision = new Etiqueta();
    private Tabla tab_cabece_factura = new Tabla();
    private Tabla tab_detalle_factura = new Tabla();
   
    
    
    @EJB
    private final ServicioCliente ser_cliente= (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);
    @EJB
    private final ServicioSistema ser_sistema = (ServicioSistema) utilitario.instanciarEJB(ServicioSistema.class);
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    public pre_factura_venta() {
         if (tienePerfilSecretaria() != 0) {
        /*    if (tienePerfilResponsable()) {
         } else {
         utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el control de Asistencia. Consulte con el Administrador");
         }     */
        tab_cabece_factura.setId("tab_cabece_factura");   //identificador
        tab_cabece_factura.setTabla("cxc_cabece_factura", "ide_cccfa", 1);
        tab_cabece_factura.agregarRelacion(tab_detalle_factura);
         //tab_cabece_factura.setTipoFormulario(true);
        //tab_cabece_factura.getGrid().setColumns(3);

        tab_cabece_factura.setMostrarNumeroRegistros(false);
        tab_cabece_factura.getColumna("ide_cnccc").setVisible(false);
        tab_cabece_factura.getColumna("ide_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("ide_cncre").setVisible(false);
        tab_cabece_factura.getColumna("ide_vgven").setVisible(false);

        tab_cabece_factura.getColumna("telefono_cccfa").setNombreVisual("TELEFONO");
        tab_cabece_factura.getColumna("telefono_cccfa").setOrden(5);
        tab_cabece_factura.getColumna("ide_cntdo").setVisible(false);
        tab_cabece_factura.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));
        tab_cabece_factura.getColumna("ide_ccefa").setVisible(false);
        tab_cabece_factura.getColumna("ide_ccefa").setValorDefecto(utilitario.getVariable("p_cxc_estado_factura_normal"));
        tab_cabece_factura.getColumna("ide_geubi").setVisible(false);
        tab_cabece_factura.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cabece_factura.getColumna("ide_usua").setVisible(false);
        tab_cabece_factura.getColumna("fecha_trans_cccfa").setValorDefecto(utilitario.getFechaActual());
        tab_cabece_factura.getColumna("fecha_trans_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("fecha_emisi_cccfa").setOrden(2);
        tab_cabece_factura.getColumna("fecha_emisi_cccfa").setMetodoChangeRuta(tab_cabece_factura.getRuta() + ".cambioFecha");
        tab_cabece_factura.getColumna("fecha_emisi_cccfa").setValorDefecto(utilitario.getFechaActual());
        tab_cabece_factura.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA EMISION");
        tab_cabece_factura.getColumna("fecha_emisi_cccfa").setRequerida(true);
        tab_cabece_factura.getColumna("ide_ccdaf").setVisible(false);
        tab_cabece_factura.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO' and identificac_geper is not null");
        tab_cabece_factura.getColumna("ide_geper").setAutoCompletar();
        tab_cabece_factura.getColumna("ide_geper").setOrden(3);
        tab_cabece_factura.getColumna("ide_geper").setRequerida(true);
        tab_cabece_factura.getColumna("ide_geper").setMetodoChange("datosCliente");
        tab_cabece_factura.getColumna("ide_geper").setNombreVisual("CLIENTE");
        tab_cabece_factura.getColumna("pagado_cccfa").setValorDefecto("false");
        tab_cabece_factura.getColumna("pagado_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("total_cccfa").setEtiqueta();
        tab_cabece_factura.getColumna("total_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cabece_factura.getColumna("total_cccfa").setValorDefecto("0");
        tab_cabece_factura.getColumna("secuencial_cccfa").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cabece_factura.getColumna("secuencial_cccfa").setLongitud(10);
        tab_cabece_factura.getColumna("secuencial_cccfa").setOrden(1);
        tab_cabece_factura.getColumna("secuencial_cccfa").setRequerida(true);
        tab_cabece_factura.getColumna("secuencial_cccfa").setNombreVisual("SECUENCIAL");
        tab_cabece_factura.getColumna("secuencial_cccfa").setMascara("999999999");
        tab_cabece_factura.getColumna("base_grabada_cccfa").setEtiqueta();
        tab_cabece_factura.getColumna("base_grabada_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cabece_factura.getColumna("base_grabada_cccfa").setValorDefecto("0");
        tab_cabece_factura.getColumna("valor_iva_cccfa").setEtiqueta();
        tab_cabece_factura.getColumna("valor_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cabece_factura.getColumna("valor_iva_cccfa").setValorDefecto("0");
        tab_cabece_factura.getColumna("base_no_objeto_iva_cccfa").setEtiqueta();
        tab_cabece_factura.getColumna("base_no_objeto_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cabece_factura.getColumna("base_no_objeto_iva_cccfa").setValorDefecto("0");
        tab_cabece_factura.getColumna("base_tarifa0_cccfa").setEtiqueta();
        tab_cabece_factura.getColumna("base_tarifa0_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cabece_factura.getColumna("base_tarifa0_cccfa").setValorDefecto("0");
        tab_cabece_factura.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp!=" + utilitario.getVariable("p_con_for_pag_reembolso_caja"));
        tab_cabece_factura.getColumna("ide_cndfp").setOrden(4);
        tab_cabece_factura.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
        tab_cabece_factura.getColumna("ide_cndfp").setEstilo("width:140px");
        tab_cabece_factura.getColumna("ide_cndfp").setRequerida(true);
        tab_cabece_factura.getColumna("TARIFA_IVA_CCCFA").setVisible(false);
        tab_cabece_factura.setCondicionSucursal(false);
        tab_cabece_factura.getColumna("DIRECCION_CCCFA").setOrden(6);
        tab_cabece_factura.getColumna("DIRECCION_CCCFA").setNombreVisual("DIRECCIÓN");
        tab_cabece_factura.getColumna("DIRECCION_CCCFA").setRequerida(true);
        tab_cabece_factura.getColumna("OBSERVACION_CCCFA").setVisible(false);
        //tab_cab_factura.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
        tab_cabece_factura.getColumna("solo_guardar_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("ide_geubi").setVisible(false);
        tab_cabece_factura.getColumna("ide_usua").setVisible(false);
        tab_cabece_factura.setTipoFormulario(true);
        tab_cabece_factura.getGrid().setColumns(6);
        //tab_cab_factura.agregarRelacion(tab_deta_factura);
        tab_cabece_factura.setCondicion("ide_cccfa=-1");
        tab_cabece_factura.getColumna("base_grabada_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("base_no_objeto_iva_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("valor_iva_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("base_tarifa0_cccfa").setVisible(false);
        tab_cabece_factura.getColumna("total_cccfa").setVisible(false);
        tab_cabece_factura.setRecuperarLectura(true);
        tab_cabece_factura.getColumna("RET_FUENTE_CCCFA").setVisible(false);
        tab_cabece_factura.getColumna("IDE_SRCOM").setVisible(false);
        tab_cabece_factura.getColumna("CORREO_CCCFA ").setVisible(false);
        tab_cabece_factura.getColumna("ORDEN_COMPRA_CCCFA").setVisible(false);
        tab_cabece_factura.getColumna("IDE_CNDFP1").setVisible(false);
      
        tab_cabece_factura.getColumna("RET_IVA_CCCFA").setVisible(false);
        tab_cabece_factura.getColumna("DIAS_CREDITO_CCCFA").setVisible(false);
        tab_cabece_factura.getColumna("DESCUENTO_CCCFA").setVisible(false);
        tab_cabece_factura.getColumna("FACT_MIG_CCCFA").setVisible(false);
        tab_cabece_factura.getColumna("ide_ademple").setVisible(true);
        tab_cabece_factura.getColumna("ide_ademple").setCombo(ser_adquisiciones.getEmpleado());
        
        eti_cajero.setStyle("font-size:16px;font-weight: bold");
                    eti_cajero.setValue("Cajero:"+empleado);
                    
        eti_caja.setStyle("font-size:16px;font-weight: bold");
                    eti_caja.setValue("Caja:"+caja);
                    
        eti_emision.setStyle("font-size:16px;font-weight: bold");
                    eti_emision.setValue("Emision:"+emision);
                    
                    Grid grup_titulo = new Grid();
                    grup_titulo.setColumns(1);
                    grup_titulo.setWidth("100%");
                    grup_titulo.setId("grup_titulo");
                    grup_titulo.getChildren().add(eti_cajero);
                    grup_titulo.getChildren().add(eti_caja);
                    grup_titulo.getChildren().add(eti_emision);
                    
        
         
  
        
        
      
        tab_cabece_factura.dibujar();
        PanelTabla pat_cabece_factura = new PanelTabla();
        pat_cabece_factura.setId("pat_cabece_factura");
        pat_cabece_factura.setPanelTabla(tab_cabece_factura);

        tab_detalle_factura.setId("tab_detalle_factura");   //identificador
        tab_detalle_factura.setTabla("cxc_deta_factura", "ide_ccdfa", 2);
        tab_detalle_factura.getColumna("ide_ccdfa").setVisible(false);
        tab_detalle_factura.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_detalle_factura.getColumna("ide_inarti").setAutoCompletar();
        tab_detalle_factura.getColumna("ide_inarti").setNombreVisual("ARTICULO");
        tab_detalle_factura.getColumna("ide_inarti").setMetodoChangeRuta(tab_detalle_factura.getRuta() + ".seleccionarProducto");
        tab_detalle_factura.getColumna("ide_inarti").setRequerida(true);
        tab_detalle_factura.getColumna("ide_cccfa").setVisible(false);
        tab_detalle_factura.getColumna("SECUENCIAL_CCDFA").setVisible(false);
        tab_detalle_factura.getColumna("ide_inarti").setOrden(1);
        tab_detalle_factura.getColumna("CANTIDAD_CCDFA").setNombreVisual("CANTIDAD");
        tab_detalle_factura.getColumna("CANTIDAD_CCDFA").setMetodoChangeRuta(tab_detalle_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_detalle_factura.getColumna("CANTIDAD_CCDFA").setOrden(2);
        tab_detalle_factura.getColumna("CANTIDAD_CCDFA").setRequerida(true);
        tab_detalle_factura.getColumna("PRECIO_CCDFA").setNombreVisual("PRECIO");
        tab_detalle_factura.getColumna("PRECIO_CCDFA").setMetodoChangeRuta(tab_detalle_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_detalle_factura.getColumna("PRECIO_CCDFA").setOrden(3);
        tab_detalle_factura.getColumna("PRECIO_CCDFA").setRequerida(true);
        // tab_detalle_factura.getColumna("iva_inarti_ccdfa").setCombo(ser_producto.getListaTipoIVA());
        tab_detalle_factura.getColumna("iva_inarti_ccdfa").setPermitirNullCombo(false);
        tab_detalle_factura.getColumna("iva_inarti_ccdfa").setOrden(4);
        tab_detalle_factura.getColumna("iva_inarti_ccdfa").setNombreVisual("IVA");
        tab_detalle_factura.getColumna("iva_inarti_ccdfa").setMetodoChangeRuta(tab_detalle_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_detalle_factura.getColumna("iva_inarti_ccdfa").setLongitud(-1);
        tab_detalle_factura.getColumna("total_ccdfa").setNombreVisual("TOTAL");
        tab_detalle_factura.getColumna("total_ccdfa").setOrden(5);
        tab_detalle_factura.getColumna("OBSERVACION_CCDFA").setNombreVisual("OBSERVACION");
        tab_detalle_factura.getColumna("total_ccdfa").setEtiqueta();
        tab_detalle_factura.getColumna("total_ccdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_detalle_factura.getColumna("total_ccdfa").alinearDerecha();
        tab_detalle_factura.getColumna("precio_promedio_ccdfa").setLectura(true);
        tab_detalle_factura.getColumna("ALTERNO_CCDFA").setValorDefecto("00");
        tab_detalle_factura.setScrollable(true);
        // tab_detalle_factura.setScrollHeight(getAltoPanel() - 320);
        tab_detalle_factura.setRecuperarLectura(true);
        tab_detalle_factura.dibujar();
        PanelTabla pat_detalle_factura = new PanelTabla();
        pat_detalle_factura.setId("pat_detalle_factura");
        pat_detalle_factura.setPanelTabla(tab_detalle_factura);

        Division div_factura = new Division();
        
        div_factura.setId("div_factura");
        div_factura.dividir2(pat_cabece_factura, pat_detalle_factura, "50%", "H");
        
        
        Division div_cabecera=new Division();
        div_cabecera.setId("div_cabecera");
        div_cabecera.setFooter(grup_titulo, div_factura, "15%");
        agregarComponente(div_cabecera);
        
        
 } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para la facturacion. Consulte con el Administrador");
        }
    }
    
    
String empleado = "";
    String cedula = "";
    String ide_ademple = "";
    String caja = "";
     String emision = "";
     public void datosCliente(SelectEvent evt)
     {
         
         TablaGenerica tab_datocliente=ser_cliente.getCliente(tab_cabece_factura.getValor("ide_geper"));
         tab_cabece_factura.setValor("telefono_cccfa",tab_datocliente.getValor("telefono_geper"));
         tab_cabece_factura.setValor("direccion_cccfa",tab_datocliente.getValor("direccion_geper"));
         utilitario.addUpdateTabla(tab_cabece_factura, "telefono_cccfa,direccion_cccfa","");
         
         
         
    
     
    }
    private int tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioCaja(utilitario.getVariable("IDE_USUA")));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);
            empleado = fila[2].toString();
            cedula = fila[1].toString();
            ide_ademple = fila[0].toString();
            caja = fila[3].toString();
            emision = fila[4].toString();
            return 1;
            

        } else {
            return 0;
            
        }
    }
    
    @Override
    public void insertar() {
        if (tab_cabece_factura.isFocus()) {
            tab_cabece_factura.insertar();
            
            tab_cabece_factura.setValor("ide_ademple", ide_ademple);
            /*tab_cabece_factura.setValor("ide_cocaj", ide_cocaj);*/
            
        } else if (tab_detalle_factura.isFocus()) {
            tab_detalle_factura.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_cabece_factura.isFocus()) {
            tab_cabece_factura.guardar();
        } else if (tab_detalle_factura.isFocus()) {
            tab_detalle_factura.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_cabece_factura.isFocus()) {
            tab_cabece_factura.eliminar();
        } else if (tab_detalle_factura.isFocus()) {
            tab_detalle_factura.eliminar();
        }
    }

    public Tabla getTab_cabece_factura() {
        return tab_cabece_factura;
    }

    public void setTab_cabece_factura(Tabla tab_cabece_factura) {
        this.tab_cabece_factura = tab_cabece_factura;
    }

    public Tabla getTab_detalle_factura() {
        return tab_detalle_factura;
    }

    public void setTab_detalle_factura(Tabla tab_detalle_factura) {
        this.tab_detalle_factura = tab_detalle_factura;
    }

}

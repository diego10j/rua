/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.Upload;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author ANDRES
 */
public class pre_factura_compras extends Pantalla{
private SeleccionTabla sel_tab_compras = new SeleccionTabla();
private SeleccionTabla sel_tab_detalle_compra = new SeleccionTabla();
private Tabla tab_cab_documento = new Tabla();
private Tabla tab_det_documento = new Tabla();
String solicitud ="";
double dou_total = 0;
double dou_base_ingresada = 0;
private double tarifaIVA = 0;
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);

    @EJB
    private final ServicioComprobanteContabilidad ser_comp_contabilidad = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);
    
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    public pre_factura_compras(){
        
       if (tienePerfilSecretaria() != 0) {
        
        tab_cab_documento.setId("tab_cab_documento");
//        tab_cab_documento.setRuta("pre_index.clase." + getId());
        tab_cab_documento.setMostrarNumeroRegistros(false);
        ;
        tab_cab_documento.setTabla("cxp_cabece_factur", "ide_cpcfa", 1);
        tab_cab_documento.setCondicion("ide_cpcfa=-1");
        tab_cab_documento.getColumna("ide_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("ide_cntdo").setVisible(false);
 //       tab_cab_documento.getColumna("ide_cpefa").setValorDefecto(parametros.get("p_cxp_estado_factura_normal"));
        tab_cab_documento.getColumna("ide_cpefa").setVisible(false);
        tab_cab_documento.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp=3");
        tab_cab_documento.getColumna("ide_cndfp").setRequerida(false);
        tab_cab_documento.getColumna("TARIFA_IVA_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
        tab_cab_documento.getColumna("ide_cndfp").setOrden(5);
        tab_cab_documento.getColumna("ide_cndfp").setVisible(false);
        tab_cab_documento.getColumna("dias_credito_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("dias_credito_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("dias_credito_cpcfa").setValorDefecto("0");

        tab_cab_documento.getColumna("ide_cndfp1").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp!=3");
        tab_cab_documento.getColumna("ide_cndfp1").setOrden(50);
        tab_cab_documento.getColumna("ide_cndfp1").setNombreVisual("DÍAS CREDITO");
        tab_cab_documento.getColumna("ide_cndfp1").setEstilo("width:140px");
        tab_cab_documento.getColumna("ide_cndfp1").setRequerida(false);

        tab_cab_documento.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_documento.getColumna("ide_usua").setVisible(false);
        tab_cab_documento.getColumna("MONTO_COM_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("IDE_CNTIC").setVisible(false);
        tab_cab_documento.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_documento.getColumna("fecha_trans_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA EMISIÓN");
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setOrden(1);
        
        tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", ""); //por defecto no carga los clientes
        tab_cab_documento.getColumna("ide_geper").setAutoCompletar();
        tab_cab_documento.getColumna("ide_geper").setRequerida(false);
        tab_cab_documento.getColumna("ide_geper").setLectura(true);
        tab_cab_documento.getColumna("ide_geper").setNombreVisual("PROVEEDOR");

        tab_cab_documento.getColumna("ide_geper").setOrden(3);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setOrden(5);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setNombreVisual("NUM. AUTORIZACIÓN");
        tab_cab_documento.getColumna("autorizacio_cpcfa").setEstilo("font-weight: bold");
       
        tab_cab_documento.getColumna("observacion_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("observacion_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("pagado_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("pagado_cpcfa").setValorDefecto("False");
        tab_cab_documento.getColumna("total_cpcfa").setNombreVisual("TOTAL:");
        tab_cab_documento.getColumna("total_cpcfa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
        tab_cab_documento.getColumna("total_cpcfa").setVisible(true);
        tab_cab_documento.getColumna("total_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("total_cpcfa").setEtiqueta();
        tab_cab_documento.getColumna("numero_cpcfa").setEstilo("font-size: 12px;font-weight: bold");
        tab_cab_documento.getColumna("numero_cpcfa").setNombreVisual("NÚMERO");
        tab_cab_documento.getColumna("numero_cpcfa").setOrden(4);
        tab_cab_documento.getColumna("numero_cpcfa").setAncho(10);
        tab_cab_documento.getColumna("numero_cpcfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
        tab_cab_documento.getColumna("numero_cpcfa").setMascara("999-999-999999999");
        tab_cab_documento.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_cab_documento.getColumna("numero_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("base_grabada_cpcfa").setVisible(true);
        tab_cab_documento.getColumna("base_grabada_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("valor_iva_cpcfa").setVisible(true);
        tab_cab_documento.getColumna("valor_iva_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setVisible(true);
        tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("base_tarifa0_cpcfa").setVisible(true);
        tab_cab_documento.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("otros_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("ide_srtst").setCombo("sri_tipo_sustento_tributario", "ide_srtst", "alterno_srtst,nombre_srtst", "");

        tab_cab_documento.getColumna("ide_srtst").setNombreVisual("SUSTENTO TRIBUTARIO");
        tab_cab_documento.getColumna("ide_srtst").setOrden(7);
        tab_cab_documento.getColumna("ide_cncre").setVisible(false);
        tab_cab_documento.getColumna("ide_cnccc").setVisible(false);
        tab_cab_documento.getColumna("valor_ice_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("valor_ice_cpcfa").setVisible(true);
        tab_cab_documento.getColumna("OTROS_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("DESCUENTO_CPCFA").setVisible(true);
        tab_cab_documento.getColumna("PORCEN_DESC_CPCFA").setVisible(true);
        
        tab_cab_documento.getColumna("PORCEN_DESC_CPCFA").setEtiqueta();
        tab_cab_documento.getColumna("PORCEN_DESC_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_cab_documento.getColumna("DESCUENTO_CPCFA").setEtiqueta();
        tab_cab_documento.getColumna("DESCUENTO_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_cab_documento.getColumna("valor_ice_cpcfa").setEtiqueta();
        tab_cab_documento.getColumna("valor_ice_cpcfa").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_cab_documento.getColumna("BASE_NO_OBJETO_IVA_CPCFA").setEtiqueta();
        tab_cab_documento.getColumna("BASE_NO_OBJETO_IVA_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_cab_documento.getColumna("BASE_GRABADA_CPCFA").setEtiqueta();
        tab_cab_documento.getColumna("BASE_GRABADA_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
       
        
        tab_cab_documento.setTipoFormulario(true);
        tab_cab_documento.getGrid().setColumns(4);
        tab_cab_documento.setCondicion("ide_cpcfa=-1");
        tab_cab_documento.setRecuperarLectura(false);

        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setOrden(8);
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setNombreVisual("TIPO DOC. MODI.");
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setOrden(9);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setNombreVisual("FECHA EMISIÓN DOC. MODI.");
        tab_cab_documento.getColumna("numero_nc_cpcfa").setOrden(10);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setNombreVisual("NÚMERO DOC. MODI.");
        tab_cab_documento.getColumna("numero_nc_cpcfa").setMascara("999-999-999999999");
        tab_cab_documento.getColumna("numero_nc_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setNombreVisual("AUTORIZACIÓN DOC. MODI.");
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setOrden(11);
       
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setOrden(12);
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setNombreVisual("MOTIVO");
        tab_cab_documento.getColumna("ide_rem_cpcfa").setVisible(false);
        //solo para que dibuje el *
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setRequerida(false);
        
        tab_cab_documento.getColumna("ide_ademple").setNombreVisual("RESPONSABLE");
        tab_cab_documento.getColumna("ide_ademple").setLectura(true);
        tab_cab_documento.getColumna("ide_ademple").setCombo(ser_adquisiciones.getEmpleado());
        tab_cab_documento.getColumna("ide_ademple").setAutoCompletar();
        tab_cab_documento.getColumna("ide_adcomp").setVisible(false);
        tab_cab_documento.getColumna("RECIBIDO_COMPRA_CPCFA").setLectura(true);
        tab_cab_documento.agregarRelacion(tab_det_documento);
        
        
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setRequerida(false);
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setRequerida(false);
        tab_cab_documento.dibujar();
        

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_cab_documento);

        tab_det_documento.setId("tab_det_documento");
        tab_det_documento.setTabla("cxp_detall_factur", "ide_cpdfa", 2);
        tab_det_documento.setCondicion("ide_cpcfa=-1");
        tab_det_documento.getColumna("ide_cpdfa").setVisible(false);
        tab_det_documento.getColumna("ide_cpcfa").setVisible(false);
        tab_det_documento.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_det_documento.getColumna("ide_inarti").setAutoCompletar();
        tab_det_documento.getColumna("ide_inarti").setNombreVisual("ARTICULO");
        tab_det_documento.getColumna("ide_inarti").setOrden(1);
        tab_det_documento.getColumna("cantidad_cpdfa").setOrden(2);
        tab_det_documento.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
       // tab_det_documento.getColumna("cantidad_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("precio_cpdfa").setOrden(3);
        tab_det_documento.getColumna("precio_cpdfa").setNombreVisual("PRECIO");
        tab_det_documento.getColumna("cantidad_cpdfa").setMetodoChange("calcularDetalle");
        tab_det_documento.getColumna("precio_cpdfa").setMetodoChange("calcularDetalle");
      //tab_det_documento.getColumna("precio_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("valor_cpdfa").setEtiqueta();
        tab_det_documento.getColumna("valor_cpdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_det_documento.getColumna("valor_cpdfa").setOrden(5);
        tab_det_documento.getColumna("valor_cpdfa").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_det_documento.getColumna("valor_cpdfa").setNombreVisual("TOTAL");
        tab_det_documento.getColumna("observacion_cpdfa").setNombreVisual("OBSERVACIÓN");
        tab_det_documento.getColumna("observacion_cpdfa").setOrden(6);
        tab_det_documento.getColumna("secuencial_cpdfa").setNombreVisual("SERIE / SECUENCIAL");
        tab_det_documento.getColumna("secuencial_cpdfa").setOrden(7);
        tab_det_documento.getColumna("devolucion_cpdfa").setValorDefecto("false");
        tab_det_documento.getColumna("alter_tribu_cpdfa").setRequerida(false);
        tab_det_documento.getColumna("alter_tribu_cpdfa").setValorDefecto("00");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setCombo(ser_producto.getListaTipoIVA());
      //  tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setPermitirNullCombo(false);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setValorDefecto("1");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setOrden(4);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setLongitud(-1);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setNombreVisual("APLICA IVA");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChange("calcularDetalle");
       // tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("recibido_compra_cpdfa").setValorDefecto("FALSE");
   
        tab_det_documento.getColumna("credi_tribu_cpdfa").setVisible(false);
        tab_det_documento.getColumna("credi_tribu_cpdfa").setValorDefecto("false");
        tab_det_documento.getColumna("devolucion_cpdfa").setVisible(false);
        //tab_det_documento.setRecuperarLectura(false);
        tab_det_documento.setRows(100);
        tab_det_documento.getColumna("recibido_compra_cpdfa").setLectura(true);
        tab_det_documento.dibujar();

        PanelTabla pat_pane2 = new PanelTabla();
        pat_pane2.setId("pat_pane2");
        pat_pane2.setPanelTabla(tab_det_documento);
             
        Division div_factura = new Division();
        div_factura.setId("div_factura");
        div_factura.dividir2(pat_panel1, pat_pane2, "50%", "H");
        agregarComponente(div_factura);
     
        Boton bot_busca_presu = new Boton();
        bot_busca_presu.setValue("BUSCAR SOLICITUD DE COMPRA");
        bot_busca_presu.setIcon("ui-icon-search");
        bot_busca_presu.setMetodo("abrirDialogoSolicitud");
        
        bar_botones.agregarBoton(bot_busca_presu);        
        
        sel_tab_compras.setId("sel_tab_compras");
        sel_tab_compras.setTitle("SELECCIONA UNA SOLICITUD DE COMPRA");
        sel_tab_compras.setSeleccionTabla(ser_adquisiciones.getSolicitudCompra("1", ""),"ide_adcomp");
        sel_tab_compras.setWidth("80%");
        sel_tab_compras.setHeight("70%");
        sel_tab_compras.setRadio();
        sel_tab_compras.getTab_seleccion().getColumna("numero_orden_adcomp").setFiltroContenido();
        sel_tab_compras.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        sel_tab_compras.getBot_aceptar().setMetodo("aceptarSolicitud");
        agregarComponente(sel_tab_compras);
      //  sel_tab_compras.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
      //  sel_tab_compras.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
      //  
        
        sel_tab_detalle_compra.setId("sel_tab_detalle_compra");
        sel_tab_detalle_compra.setTitle("SELECCIONA EL DETALLE DE LA COMPRA");
        sel_tab_detalle_compra.setSeleccionTabla(ser_adquisiciones.getdetalleSolicitudCompra("1", ""),"ide_adcobi");
        sel_tab_detalle_compra.setWidth("80%");
        sel_tab_detalle_compra.setHeight("70%");
        //sel_tab_detalle_compra.setRadio();
        sel_tab_detalle_compra.getBot_aceptar().setMetodo("generarCabecera");
        agregarComponente(sel_tab_detalle_compra);
      
        
        
        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el registro de la orden de gasto de Compras. Consulte con el Administrador");
        }     
       
    }
    
    public void calcularTotalDocumento() {
        tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());
        double base_grabada = 0;
        double base_no_objeto = 0;
        double base_tarifa0 = 0;
        double valor_iva = 0;
        // double porcentaje_iva = 0;

        for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
            String iva = tab_det_documento.getValor(i, "iva_inarti_cpdfa");
            switch (iva) {
                case "1":
                    //SI IVA
                    base_grabada = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_grabada;
                    break;
                case "-1":
                    // NO IVA
                    base_tarifa0 = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_tarifa0;
                    break;
                case "0":
                    // NO OBJETO
                    base_no_objeto = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_no_objeto;
                    break;
                default:
                    break;
            }
        }

        double porce_descuento = 0;

            try {
                porce_descuento = Double.parseDouble(utilitario.getFormatoNumero(tab_cab_documento.getValor("porcen_desc_cpcfa")));
            } catch (Exception e) {
            }

        

        double descuento = 0;
        double valor_ice = 0;

        
            try {
                descuento = Double.parseDouble(utilitario.getFormatoNumero(tab_cab_documento.getValor("descuento_cpcfa")));
            } catch (Exception e) {
            }
        
        
            try {
                valor_ice = Double.parseDouble(utilitario.getFormatoNumero(tab_cab_documento.getValor("valor_ice_cpcfa")));
            } catch (Exception e) {
            }
        
        //base_grabada = base_grabada - descuento;
        valor_iva = (base_grabada - descuento) * tarifaIVA; //0.12
        if (valor_ice > 0) {
            valor_iva += (valor_ice * tarifaIVA); //0.12
        }
        tab_cab_documento.setValor("porcen_desc_cpcfa", utilitario.getFormatoNumero(porce_descuento));
        tab_cab_documento.setValor("descuento_cpcfa", utilitario.getFormatoNumero(descuento));
        tab_cab_documento.setValor("valor_ice_cpcfa", utilitario.getFormatoNumero(valor_ice));

        tab_cab_documento.setValor("base_grabada_cpcfa", utilitario.getFormatoNumero(base_grabada));
        tab_cab_documento.setValor("base_no_objeto_iva_cpcfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_cab_documento.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(valor_iva));
        tab_cab_documento.setValor("base_tarifa0_cpcfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_cab_documento.setValor("total_cpcfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva + valor_ice));

       // tex_subtotal12.setValue(utilitario.getFormatoNumero(base_grabada));
       // tex_subtotal0.setValue(utilitario.getFormatoNumero(base_no_objeto + base_tarifa0));
       // tex_iva.setValue(utilitario.getFormatoNumero(valor_iva));
       // tex_total.setValue(utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva + valor_ice));
        utilitario.addUpdate("tab_cab_documento");
    }
    
    public void abrirDialogoSolicitud(){
        sel_tab_compras.dibujar();
    }
    public void aceptarSolicitud(){
        solicitud = sel_tab_compras.getValorSeleccionado();
        sel_tab_compras.cerrar();
        sel_tab_detalle_compra.getTab_seleccion().setSql(ser_adquisiciones.getdetalleSolicitudCompra("2", solicitud));
        sel_tab_detalle_compra.getTab_seleccion().ejecutarSql();   
        sel_tab_detalle_compra.dibujar();
    }
    public void generarCabecera(){
        TablaGenerica tab_cabece_fac = utilitario.consultar("select ide_adcomp, numero_orden_adcomp, a.ide_geper, b.identificac_geper, b.nom_geper, fecha_solicitud_adcomp, valor_adcomp, detalle_adcomp\n" +
                                                            "from adq_compra a\n" +
                                                            "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
                                                            "where a.ide_adcomp = "+solicitud+"");
        for (int i=0; i < tab_cabece_fac.getTotalFilas(); i++ ){
            if (tab_cab_documento.isFilaInsertada() == false){
                tab_cab_documento.insertar();
                tab_cab_documento.setValor("ide_ademple", ide_ademple);
            }
            tab_cab_documento.setValor("ide_geper",tab_cabece_fac.getValor(i, "ide_geper"));
            tab_cab_documento.setValor("ide_adcomp",tab_cabece_fac.getValor(i, "ide_adcomp"));
        }
        tab_cab_documento.guardar();
        guardarPantalla();
        sel_tab_detalle_compra.cerrar();
	utilitario.addUpdate("tab_cab_documento");
        generaDetalle();
    }
    public void generaDetalle(){
        String detalle = sel_tab_detalle_compra.getSeleccionados();
        TablaGenerica tab_deta_fac = utilitario.consultar("select ide_adcobi, ide_adcomp, b.ide_inarti, b.nombre_inarti, cantidad_adcobi, especificaciones_adcobi from adq_compra_bienes a\n" +
                                                          "left join inv_articulo b on a.ide_inarti = b.ide_inarti\n" +
                                                          "where a.ide_adcomp = "+solicitud+"\n" +
                                                          "and ide_adcobi in ("+detalle+")");
        for (int i=0; i<tab_deta_fac.getTotalFilas(); i++){
            tab_det_documento.insertar();
            tab_det_documento.setValor("ide_inarti",tab_deta_fac.getValor(i, "ide_inarti"));
            tab_det_documento.setValor("cantidad_cpdfa",tab_deta_fac.getValor(i, "cantidad_adcobi"));
        }
        tab_det_documento.guardar();
        guardarPantalla();
        utilitario.addUpdate("tab_det_documento");
        utilitario.getConexion().ejecutarSql("update adq_compra  set facturado_adcomp = true where ide_adcomp = "+solicitud+"");
    }
    
    public void calcularDetalle(AjaxBehaviorEvent evt){
        tab_det_documento.modificar(evt);
        calcular(); 
    }
    public void calcular() {
        double dou_cantidad_fac = 0;
        double dou_precio_fac = 0;
        double dou_subtotal_fac = 0;
        try {
            //Obtenemos el valor de la cantidad
            dou_cantidad_fac = Double.parseDouble(tab_det_documento.getValor("cantidad_cpdfa"));
        } catch (Exception e) {
        }

        try {
            //Obtenemos el valor
            dou_precio_fac = Double.parseDouble(tab_det_documento.getValor("precio_cpdfa"));
        } catch (Exception e) {
        }
        //Calculamos el subtotal y ganancias
        dou_subtotal_fac = dou_cantidad_fac * dou_precio_fac;
        
        //Asignamos el total a la tabla detalle, con 2 decimales
        tab_det_documento.setValor("valor_cpdfa", utilitario.getFormatoNumero(dou_subtotal_fac, 2));
        
        try {
         //Obtenemos el valor del subtotal
           dou_precio_fac = Double.parseDouble(tab_det_documento.getValor("valor_cpdfa"));
        } catch (Exception e) {
        }
        //Actualizamos el campo de la tabla AJAX
        utilitario.addUpdateTabla(tab_det_documento, "valor_cpdfa", "tab_det_documento");
        utilitario.addUpdate("tab_det_documento");
        calcularTotal();
    }
    public void calcularTotal(){
        dou_total = 0;
        dou_base_ingresada = 0;
        for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
            dou_base_ingresada += Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa"));
        }
        tab_cab_documento.setValor("total_cpcfa", utilitario.getFormatoNumero(dou_base_ingresada, 2));
        tab_cab_documento.modificar(tab_cab_documento.getFilaActual());//para que haga el update        
        utilitario.addUpdate("tab_cab_documento");
        tab_cab_documento.guardar();
        tab_det_documento.guardar();
        guardarPantalla();
        calcularTotalDocumento();
    }
    
    String empleado = "";
    String cedula = "";
    String ide_ademple = "";
    
    private int tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioSistemaEmpleado(utilitario.getVariable("IDE_USUA")));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);

            empleado = fila[1].toString();
            cedula = fila[2].toString();
            ide_ademple = fila[0].toString();
            return 1;
            

        } else {
            return 0;
            
        }
    }
    @Override
    public void insertar() {
        if (tab_cab_documento.isFocus()) {
            tab_cab_documento.insertar();
             tab_cab_documento.setValor("ide_ademple", ide_ademple);
        } else if (tab_det_documento.isFocus()) {
            tab_det_documento.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_cab_documento.isFocus()) {
            tab_cab_documento.guardar();
        } else if (tab_det_documento.isFocus()) {
            tab_det_documento.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_cab_documento.isFocus()) {
            tab_cab_documento.eliminar();
        } else if (tab_det_documento.isFocus()) {
            tab_det_documento.eliminar();
        }
    }

    public SeleccionTabla getSel_tab_compras() {
        return sel_tab_compras;
    }

    public void setSel_tab_compras(SeleccionTabla sel_tab_compras) {
        this.sel_tab_compras = sel_tab_compras;
    }

    public Tabla getTab_cab_documento() {
        return tab_cab_documento;
    }

    public void setTab_cab_documento(Tabla tab_cab_documento) {
        this.tab_cab_documento = tab_cab_documento;
    }

    public Tabla getTab_det_documento() {
        return tab_det_documento;
    }

    public void setTab_det_documento(Tabla tab_det_documento) {
        this.tab_det_documento = tab_det_documento;
    }

    public SeleccionTabla getSel_tab_detalle_compra() {
        return sel_tab_detalle_compra;
    }

    public void setSel_tab_detalle_compra(SeleccionTabla sel_tab_detalle_compra) {
        this.sel_tab_detalle_compra = sel_tab_detalle_compra;
    }

   
}

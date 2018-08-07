package paq_adquisicion;

/**
 *
 * @author Andres Redroban
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import static paq_adquisicion.AdquisicionesOrdenadorGasto.par_aprueba_gasto;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class Adquisiciones extends Pantalla {

    private Tabla tab_adquisiones = new Tabla();
    private Tabla tab_presupuesto = new Tabla();
    private Tabla tab_compra_bienes = new Tabla();
    
    private Reporte rep_reporte = new Reporte(); //Listado de Reportes, siempre se llama rep_reporte
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte(); //formato de salida del reporte
    private Map map_parametros = new HashMap();//Parametros del reporte
    public static String par_ti_anulado;
    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();   
    private SeleccionTabla sel_tab_proveedor = new SeleccionTabla();
    private SeleccionTabla sel_tab_presupuesto = new SeleccionTabla();
    double dou_total = 0;
    double dou_base_ingresada = 0;

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public Adquisiciones() {
        
        if (tienePerfilSecretaria() != 0) {
         
        rep_reporte.setId("rep_reporte");
        agregarComponente(rep_reporte);
        bar_botones.agregarReporte();
        
        Boton bot_agregar_solicitante= new Boton();
        
        Boton bot_anular = new Boton();
        bot_anular.setIcon("ui-icon-search");
        bot_anular.setValue("ANULAR LA SOLICITUD");
        bot_anular.setMetodo("anular");
        
        bar_botones.agregarBoton(bot_anular);
        
         Boton bot_imprimir = new Boton();
        bot_imprimir.setIcon("ui-icon-print");
        bot_imprimir.setValue("IMPRIMIR SOLICITUD");
        bot_imprimir.setMetodo("generarPDF");
        
        Boton bot_importar = new Boton();
        bot_imprimir.setIcon("ui-icon-print");
        bot_imprimir.setValue("IMPORTAR PROVEEDOR");
        bot_imprimir.setMetodo("abrirDialogoProveedor");
        
        bar_botones.agregarBoton(bot_imprimir);
        
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_adquisiones.setId("tab_adquisiones");   //identificador
        tab_adquisiones.setTabla("adq_compra", "ide_adcomp", 1);
        List lista = new ArrayList();
        List lista1 = new ArrayList();
        List lista2 = new ArrayList();
        Object fila1[] = {"1", "SI"};
        Object fila2[] = {"2", "NO"};
        Object fila5[] = {"1", "COMPRA EN STOCK"};
        Object fila6[] = {"2", "COMPRA DE CONSUMO DIRECTO"};
        Object fila7[] = {"1", "BODEGA"};
        Object fila8[] = {"2", "ACTIVOS FIJOS"};
        lista.add(fila1);
        lista.add(fila2);
        lista2.add(fila5);
        lista2.add(fila6);
        lista1.add(fila7);
        lista1.add(fila8);
        tab_adquisiones.getColumna("existe_adcomp").setRadio(lista, "1");
        tab_adquisiones.getColumna("tipo_compra_adcomp").setCombo(lista2);
        //tab_adquisiones.getColumna("IDE_ADEMAP").setMetodoChange("prueba");
        tab_adquisiones.getColumna("INGRESO_ADCOMP").setCombo(lista1);
        tab_adquisiones.getColumna("APRUEBA_ADCOMP").setRadio(lista, "1");
        tab_adquisiones.getColumna("IDE_ADAPRO").setCombo(ser_adquisiciones.getAprobado());
        tab_adquisiones.getColumna("IDE_ADEMAP").setCombo(ser_adquisiciones.getEmpleadoAprueba("3","","",""));
        tab_adquisiones.getColumna("IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDep());
        tab_adquisiones.getColumna("IDE_ADEMDE").setLectura(true);
        tab_adquisiones.getColumna("IDE_ADEMPLE").setCombo(ser_adquisiciones.getEmpleado());
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3","1","1","1"));
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3","1","1","1"));
        tab_adquisiones.getColumna("IDE_ADARAD ").setCombo(ser_adquisiciones.getAreaAdministrativa("0", "0"));
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setNombreVisual("APROBADOR DE GASTO");
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setNombreVisual("APROBADOR DE EXISTENCIA");
        tab_adquisiones.agregarRelacion(tab_presupuesto);
        tab_adquisiones.agregarRelacion(tab_compra_bienes);
        tab_adquisiones.setTipoFormulario(true);
        tab_adquisiones.getGrid().setColumns(6);
        tab_adquisiones.getColumna("IDE_ADEMAP").setNombreVisual("SOLICITANTE");
        tab_adquisiones.getColumna("IDE_ADEMDE").setNombreVisual("RESPONSABLE SOLICITUD");
        tab_adquisiones.getColumna("IDE_ADCOMP").setNombreVisual("CODIGO");
        tab_adquisiones.getColumna("IDE_ADAPRO").setNombreVisual("APROBACION");
        tab_adquisiones.getColumna("TIPO_COMPRA_ADCOMP").setNombreVisual("TIPO DE COMPRA");
        tab_adquisiones.getColumna("DESCRIPCION_ADCOMP").setNombreVisual("DESCRIPCION");
        tab_adquisiones.getColumna("INGRESO_ADCOMP").setNombreVisual("INGRESO");
        tab_adquisiones.getColumna("EXISTE_ADCOMP").setNombreVisual("EXISTE");
        tab_adquisiones.getColumna("APRUEBA_ADCOMP").setNombreVisual("APRUEBA");
        tab_adquisiones.getColumna("NUMERO_ORDEN_ADCOMP").setNombreVisual("NUMERO DE ORDEN");
        tab_adquisiones.getColumna("FECHA_SOLICITUD_ADCOMP").setNombreVisual("FECHA DE SOLICITUD");
        tab_adquisiones.getColumna("VALOR_PRESUPUESTADO_ADCOMP").setNombreVisual("VALOR PRESUPUESTADO");
        tab_adquisiones.getColumna("VALOR_ADCOMP").setNombreVisual("VALOR");
        tab_adquisiones.getColumna("VALOR_ADCOMP").setEtiqueta();
        tab_adquisiones.getColumna("VALOR_ADCOMP").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
        tab_adquisiones.getColumna("FECHA_ADJUDICADO_ADCOMP").setNombreVisual("FECHA ADJUDICADO");
        tab_adquisiones.getColumna("ADJUDICADOR_ADCOMP").setNombreVisual("ADJUDICADOR");
        tab_adquisiones.getColumna("PROVEEDOR_ADCOMP").setNombreVisual("PROVEEDOR");
        tab_adquisiones.getColumna("RUC_PROVEEDOR_ADCOMP").setNombreVisual("RUC PROVEEDOR");
        tab_adquisiones.getColumna("CODIGO_SIS_PROV_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("DESCUENTO_ADCOMP").setNombreVisual("DESCUENTO");
        tab_adquisiones.getColumna("SUBTOTAL_ADCOMP").setNombreVisual("SUBTOTAL");
        tab_adquisiones.getColumna("IVA_ADCOMP").setNombreVisual("IVA");
        tab_adquisiones.getColumna("NUMERO_PROFORMA_ADCOMP").setNombreVisual("NUMERO PROFORMA");
        tab_adquisiones.getColumna("FECHA_PROFORMA_ADCOMP").setNombreVisual("FECHA PROFORMA");
        tab_adquisiones.getColumna("NOMBRE_OFERENTE1_ADCOMP").setNombreVisual("NOMBRE OFERENTE");
        tab_adquisiones.getColumna("FACTURA_PROFORMA_OF1_ADCOMP").setNombreVisual("FACTURA PROFORMA");
        tab_adquisiones.getColumna("VALOR_PROFORMA_OF1_ADCOMP").setNombreVisual("VALOR PROFORMA");
        tab_adquisiones.getColumna("FECHA_PROFORMA_OF1_ADCOMP").setNombreVisual("FECHA PROFORMA");
        tab_adquisiones.getColumna("NOMBRE_OFERENTE2_ADCOMP").setNombreVisual("NOMBRE OFERENTE");
        tab_adquisiones.getColumna("FACTURA_PROFORA_OF2_ADCOMP").setNombreVisual("FACTURA PROFORMA");
        tab_adquisiones.getColumna("VALOR_PROFORMA_OF2_ADCOMP").setNombreVisual("VALOR PROFORMA");
        tab_adquisiones.getColumna("FECHA_PROFORMA_OF2_ADCOMP").setNombreVisual("FECHA PROFORMA");
        tab_adquisiones.getColumna("DETALLE_ADCOMP").setNombreVisual("DETALLE");
        tab_adquisiones.getColumna("USO_ADCOMP").setNombreVisual("USO");
        tab_adquisiones.getColumna("OBSERVACIONES_ADCOMP").setNombreVisual("OBSERVACIONES");
        tab_adquisiones.getColumna("DESTINO_DEL_BIEN_ADCOMP").setNombreVisual("DESTINO");
        //tab_adquisiones.setValor("valor_adcomp",""+tab_presupuesto.getSumaColumna("valor_adpres"));
        tab_adquisiones.getColumna("APLICA_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("APRUEBA_DIRECTOR_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("ATIENDE_BODEGA_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("APRUEBA_GASTO_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("REGISTRA_COMPRAS_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("OBSERVACIONES_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("DESCRIPCION_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("FECHA_ADJUDICADO_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("ADJUDICADOR_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("EXISTE_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("APRUEBA_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("IDE_ADEMPLE").setVisible(false);
        tab_adquisiones.getColumna("ide_geper").setVisible(false);
        
      /*  tab_adquisiones.getColumna("IDE_ADAPRO").setVisible(false);
        tab_adquisiones.getColumna("TIPO_COMPRA_ADCOMP").setVisible(true);
        tab_adquisiones.getColumna("DESCRIPCION_ADCOMP").setVisible(true);
        tab_adquisiones.getColumna("INGRESO_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("EXISTE_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("INGRESO_ADCOMP").setVisible(true);
        tab_adquisiones.getColumna("NUMERO_ORDEN_ADCOMP").setVisible(true);
        tab_adquisiones.getColumna("FECHA_SOLICITUD_ADCOMP").setVisible(true);
        tab_adquisiones.getColumna("INGRESO_ADCOMP").setVisible(false);*/
        tab_adquisiones.getColumna("VALOR_PRESUPUESTADO_ADCOMP").setVisible(false);/*
        tab_adquisiones.getColumna("VALOR_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("FECHA_ADJUDICADO_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("ADJUDICADOR_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("PROVEEDOR_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("RUC_PROVEEDOR_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("CODIGO_SIS_PROV_ADCOMP").setVisible(false);*/
        tab_adquisiones.getColumna("DESCUENTO_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("SUBTOTAL_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("IVA_ADCOMP").setVisible(false);/*
        tab_adquisiones.getColumna("NUMERO_PROFORMA_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("FECHA_PROFORMA_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("NOMBRE_OFERENTE1_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("FACTURA_PROFORMA_OF1_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("VALOR_PROFORMA_OF1_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("FECHA_PROFORMA_OF1_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("NOMBRE_OFERENTE2_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("FACTURA_PROFORA_OF2_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("VALOR_PROFORMA_OF2_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("FECHA_PROFORMA_OF2_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("DETALLE_ADCOMP").setVisible(true);
        tab_adquisiones.getColumna("USO_ADCOMP").setVisible(true);
        tab_adquisiones.getColumna("OBSERVACIONES_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("DESTINO_DEL_BIEN_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("APRUEBA_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("APLICA_ADCOMP").setVisible(false);
        tab_adquisiones.getColumna("IDE_ADEMAP").setVisible(false);
        tab_adquisiones.getColumna("IDE_ADEMDE").setVisible(false);
        tab_adquisiones.getColumna("IDE_ADEMPLE").setVisible(false);
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setVisible(false);
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setVisible(false);*/
        
        tab_adquisiones.dibujar();

        PanelTabla pat_adquisiciones = new PanelTabla();
        pat_adquisiciones.setId("pat_adquisiciones");
        pat_adquisiciones.setPanelTabla(tab_adquisiones);

        
        tab_presupuesto.setId("tab_presupuesto");
        tab_presupuesto.setIdCompleto("tab_tabulador:tab_presupuesto");
        tab_presupuesto.setTabla("adq_presupuesto", "ide_adpres", 2);
      /*  List lista3 = new ArrayList();
        Object fila3[] = {"1", "CERTIFICACION"};
        Object fila4[] = {"2", "COMPROMISO"};
        lista3.add(fila3);
        lista3.add(fila4);
        tab_presupuesto.getColumna("tipo_documento_adcert").setCombo(lista3);
        tab_presupuesto.getColumna("IDE_ADCERT").setNombreVisual("CODIGO");
        tab_presupuesto.getColumna("TIPO_DOCUMENTO_ADCERT").setNombreVisual("TIPO DOCUMENTO");
        tab_presupuesto.getColumna("NRO_CERTIFICACION_ADCERT").setNombreVisual("NUMERO CERTIFICACION");
        tab_presupuesto.getColumna("PARTIDA_ADCERT").setNombreVisual("PARTIDA");
        tab_certificacion.getColumna("VALOR_ADCERT").setNombreVisual("VALOR");*/
        tab_presupuesto.getColumna("ide_adpres").setNombreVisual("CODIGO");
        tab_presupuesto.getColumna("ide_prpot").setNombreVisual("PARTIDA PRESUPUESTARIA");
        tab_presupuesto.getColumna("ide_prpot").setCombo(ser_adquisiciones.getDatosPresupuestoConsulta());
        tab_presupuesto.getColumna("ide_prpot").setAutoCompletar();
        tab_presupuesto.getColumna("valor_adpres").setNombreVisual("VALOR PRESUPUESTO");
        tab_presupuesto.getColumna("saldo_adpres").setNombreVisual("SALDO PRESUPUESTO");
	tab_presupuesto.getColumna("valor_adpres").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:blue");//Estilo
        tab_presupuesto.getColumna("saldo_adpres").setEtiqueta();
	tab_presupuesto.getColumna("saldo_adpres").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
	tab_presupuesto.setColumnaSuma("valor_adpres");
        tab_presupuesto.getColumna("valor_adpres").setMetodoChange("CalcularSuma");
        tab_presupuesto.dibujar();
        
        Grid gri_presupuesto = new Grid();
        gri_presupuesto.setColumns(3);
        Boton bot_busca_presu = new Boton();
        bot_busca_presu.setValue("BUSCAR PRESUPUESTO");
        bot_busca_presu.setIcon("ui-icon-search");
        bot_busca_presu.setMetodo("abrirDialogoPresupuesto");
        
        gri_presupuesto.getChildren().add(bot_busca_presu);
        
        PanelTabla pat_panel_presupuesto = new PanelTabla();
        pat_panel_presupuesto.setId("pat_panel_presupuesto");
        pat_panel_presupuesto.getChildren().add(gri_presupuesto);
        pat_panel_presupuesto.setPanelTabla(tab_presupuesto);
        
        tab_compra_bienes.setId("tab_compra_bienes");
        tab_compra_bienes.setIdCompleto("tab_tabulador:tab_compra_bienes");
        tab_compra_bienes.setTabla("ADQ_COMPRA_BIENES", "IDE_ADCOBI", 3);
        tab_compra_bienes.getColumna("IDE_INARTI").setCombo(ser_adquisiciones.getMaterial("0","0"));
        tab_compra_bienes.getColumna("IDE_INARTI").setAutoCompletar();
        tab_compra_bienes.getColumna("IDE_ADCOBI").setNombreVisual("CODIGO");
        tab_compra_bienes.getColumna("IDE_INARTI").setNombreVisual("ARTICULO");
        tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setNombreVisual("CANTIDAD");
        tab_compra_bienes.getColumna("ESPECIFICACIONES_ADCOBI").setNombreVisual("ESPECIFICACIONES");
        tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setNombreVisual("VALOR UNITARIO");
        tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setNombreVisual("DESCUENTO");
        tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setNombreVisual("PORCENTAJE DESCUENTO");
        tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setNombreVisual("SUBTOTAL");
        tab_compra_bienes.getColumna("IVA_ADCOBI").setNombreVisual("IVA");
        tab_compra_bienes.getColumna("TOTAL_ADCOBI").setNombreVisual("TOTAL");
        tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setNombreVisual("CANTIDAD");

        tab_compra_bienes.getColumna("IDE_ADCOBI").setOrden(1);
        tab_compra_bienes.getColumna("IDE_INARTI").setOrden(2);
        tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setOrden(3);        
        
        tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setVisible(false);
        tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setVisible(false);
        tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setVisible(false);
        tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setVisible(false);
        tab_compra_bienes.getColumna("IVA_ADCOBI").setVisible(false);
        tab_compra_bienes.getColumna("TOTAL_ADCOBI").setVisible(false);
        tab_compra_bienes.getColumna("NO_EXISTE_ADCOBI").setVisible(false);
         tab_compra_bienes.getColumna("NO_EXISTE_ADCOBI").setValorDefecto("0");
       
        tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setValorDefecto("0");
        tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setValorDefecto("0");
        tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setValorDefecto("0");
        tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setValorDefecto("0");
        tab_compra_bienes.getColumna("IVA_ADCOBI").setValorDefecto("0");
        tab_compra_bienes.getColumna("TOTAL_ADCOBI").setValorDefecto("0");
        tab_compra_bienes.dibujar();
        PanelTabla pat_panel_compra_bienes = new PanelTabla();
        pat_panel_compra_bienes.setId("pat_panel_compra_bienes");
        pat_panel_compra_bienes.setPanelTabla(tab_compra_bienes);

        tab_tabulador.agregarTab("PRESUPUESTO", pat_panel_presupuesto);
        tab_tabulador.agregarTab("COMPRA BIENES", pat_panel_compra_bienes);

        Division div_adquisiciones = new Division();
        div_adquisiciones.setId("div_adquisiciones");
        div_adquisiciones.dividir2(pat_adquisiciones, tab_tabulador, "60%", "H");
        agregarComponente(div_adquisiciones);
        
        sel_tab_proveedor.setId("sel_tab_proveedor");
        sel_tab_proveedor.setTitle("PROVEEDOR");
        sel_tab_proveedor.setSeleccionTabla(ser_adquisiciones.getDatosProveedor(), "ide_geper");
        sel_tab_proveedor.setWidth("80%");
        sel_tab_proveedor.setHeight("70%");
        sel_tab_proveedor.setRadio();
        sel_tab_proveedor.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
        sel_tab_proveedor.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        sel_tab_proveedor.getBot_aceptar().setMetodo("aceptarProveedor");
        
        sel_tab_presupuesto.setId("sel_tab_presupuesto");
        sel_tab_presupuesto.setTitle("PRESUPUESTO");
        sel_tab_presupuesto.setSeleccionTabla(ser_adquisiciones.getDatosPresupuesto("1", "2015","ide_prpot"), "ide_prpot");
        sel_tab_presupuesto.setWidth("80%");
        sel_tab_presupuesto.setHeight("70%");
        sel_tab_presupuesto.setRadio();
        sel_tab_presupuesto.getTab_seleccion().getColumna("cod_programa_prpro").setFiltroContenido();
        sel_tab_presupuesto.getTab_seleccion().getColumna("detalle_actividad").setFiltroContenido();
        sel_tab_presupuesto.getBot_aceptar().setMetodo("aceptarPresupuesto");
        
        
        agregarComponente(sel_tab_proveedor);
        agregarComponente(sel_tab_presupuesto);
        
        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        
        vipdf_comprobante.setId("vipdf_comprobante");
        vipdf_comprobante.setTitle("SOLICITUD DE COMPRA");
        agregarComponente(vipdf_comprobante);
        
        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el registro de la orden de gasto de Compras. Consulte con el Administrador");
        }

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
    
    public void CalcularSuma(AjaxBehaviorEvent evt){
        tab_presupuesto.modificar(evt); //Siempre es la primera linea
        double valor_presupuesto=0;
        double valor_saldo=0;
        valor_presupuesto= Double.parseDouble(tab_presupuesto.getValor("valor_adpres"));
        valor_saldo=Double.parseDouble(tab_presupuesto.getValor("saldo_adpres"));
        if(valor_presupuesto<=0){
			utilitario.agregarMensajeError("Ingrese un valor positivo o mayor a cero", "Ingrese un valor positivo o mayor a cero");
		    tab_presupuesto.setValor("valor_adpres", "0");
		    utilitario.addUpdateTabla(tab_presupuesto, "valor_adpres","");
		    return;
		}
		if(valor_saldo<valor_presupuesto){
			utilitario.agregarMensajeError("No puede Exceder valor asignado", "Ingrese un valor menor igual al disponible en saldo: "+valor_saldo);
		    tab_presupuesto.setValor("valor_adpres", "0");
		    utilitario.addUpdateTabla(tab_presupuesto, "valor_adpres","");
		    return;
		}
                tab_presupuesto.setColumnaSuma("valor_adpres");
                utilitario.addUpdateTabla(tab_presupuesto, "valor_adpres","");	
                utilitario.addUpdate("tab_presupuesto");
              //  tab_adquisiones.getColumna("VALOR_ADCOMP").
              //  tab_adquisiones.setValor("valor_adcomp",""+tab_presupuesto.getSumaColumna("valor_adpres"));
	//	tab_adquisiones.modificar(tab_adquisiones.getFilaActual());
	//	utilitario.addUpdateTabla(tab_adquisiones, "VALOR_ADCOMP","");	
            //    tab_presupuesto.setColumnaSuma("valor_adpres");
                calcularTotal();

    }
    
    public void calcularTotal(){
        dou_total = 0;
        dou_base_ingresada = 0;
        for (int i = 0; i < tab_presupuesto.getTotalFilas(); i++) {
            dou_base_ingresada += Double.parseDouble(tab_presupuesto.getValor(i, "valor_adpres"));
        }
        tab_adquisiones.setValor("valor_adcomp", utilitario.getFormatoNumero(dou_base_ingresada, 2));
        tab_adquisiones.modificar(tab_adquisiones.getFilaActual());//para que haga el update        
        utilitario.addUpdate("tab_adquisiones");
        utilitario.addUpdate("tab_presupuesto");
      }
    
    public void prueba (AjaxBehaviorEvent evt){
        tab_adquisiones.modificar(evt);
        String valor=tab_adquisiones.getValor("IDE_ADEMAP");
        TablaGenerica empleado = utilitario.consultar("select ide_ademap,FECHA_INGRE from ADQ_EMPLEADO_APRUEBA where IDE_ADEMAP="+valor);
        tab_adquisiones.setValor("uso_adcomp", empleado.getValor("FECHA_INGRE"));
        utilitario.addUpdate("tab_adquisiones");
        
    }
        public void generarPDF() {
        if (tab_adquisiones.getValorSeleccionado() != null) {
                        ///////////AQUI ABRE EL REPORTE
                        Map parametros = new HashMap();
                        parametros.put("pide_requisicion", Integer.parseInt(tab_adquisiones.getValor("IDE_ADCOMP")));
                                 map_parametros.put("p_usuario", utilitario.getVariable("NICK"));

                        //System.out.println(" " + str_titulos);
                        vipdf_comprobante.setVisualizarPDF("rep_compras/rep_solicitudcompra.jasper", parametros);
                        vipdf_comprobante.dibujar();
                        utilitario.addUpdate("vipdf_comprobante");
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Solititud de compra", "");
        }
    }
    public void abrirDialogoProveedor(){
        sel_tab_proveedor.dibujar();
    }
    public void abrirDialogoPresupuesto(){
        sel_tab_presupuesto.getTab_seleccion().setSql(ser_adquisiciones.getDatosPresupuesto("3", "2", "ide_prpot"));
        sel_tab_presupuesto.getTab_seleccion().ejecutarSql();
        sel_tab_presupuesto.dibujar();
    }
    public void aceptarProveedor(){
         String str_prove = sel_tab_proveedor.getValorSeleccionado();
        
        TablaGenerica tab_dat_prove = utilitario.consultar(ser_adquisiciones.getDatosProveedorConsulta(str_prove));
        for (int i=0;i<tab_dat_prove.getTotalFilas();i++){
            if (tab_adquisiones.isFilaInsertada()==false){
                tab_adquisiones.insertar();
            }
                tab_adquisiones.setValor("ide_geper",tab_dat_prove.getValor(i,"ide_geper"));
		tab_adquisiones.setValor("proveedor_adcomp",tab_dat_prove.getValor(i,"nom_geper"));
                tab_adquisiones.setValor("ruc_proveedor_adcomp",tab_dat_prove.getValor(i,"identificac_geper"));
                //tab_adquisiones.setValor("direccion_ademple",null);
               
	       }
            // tab_empleado.guardar();
            // guardarPantalla();
             sel_tab_proveedor.cerrar();
	     utilitario.addUpdate("tab_adquisiones");
    }
    
    public void aceptarPresupuesto(){
         String str_presu = sel_tab_presupuesto.getValorSeleccionado();
        
        TablaGenerica tab_dat_presu = utilitario.consultar(ser_adquisiciones.getDatosPresupuesto("4", "2",str_presu));
        for (int i=0;i<tab_dat_presu.getTotalFilas();i++){
            if (tab_presupuesto.isFilaInsertada()==false){
                tab_presupuesto.insertar();
            }
		tab_presupuesto.setValor("ide_prpot",tab_dat_presu.getValor(i,"ide_prpot"));
                tab_presupuesto.setValor("valor_adpres",tab_dat_presu.getValor(i,"saldoxdevengado"));
                tab_presupuesto.setValor("saldo_adpres",tab_dat_presu.getValor(i,"saldoxdevengado"));
                //tab_adquisiones.setValor("direccion_ademple",null);
               
	       }
            // tab_empleado.guardar();
            // guardarPantalla();
             sel_tab_presupuesto.cerrar();
             tab_presupuesto.setColumnaSuma("valor_adpres");
             utilitario.addUpdateTabla(tab_presupuesto, "valor_adpres","");	
             utilitario.addUpdate("tab_presupuesto");
             calcularTotal();
             tab_adquisiones.guardar();
             tab_presupuesto.guardar();
             guardarPantalla();
    }
    
    @Override
      public void abrirListaReportes() {
        // TODO Auto-generated method stub
        rep_reporte.dibujar();
      }
    @Override
      public void aceptarReporte() {
         if (rep_reporte.getReporteSelecionado().equals("Solicitu de Compra")){
                      rep_reporte.cerrar();
         map_parametros.clear();
         map_parametros.put("pide_requisicion", Integer.parseInt(tab_adquisiones.getValor("ide_adcomp")));
         
         map_parametros.put("p_usuario", utilitario.getVariable("NICK"));
         sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
         sel_rep.dibujar();
         }
      }      

    @Override
    public void insertar() {
        if (tab_adquisiones.isFocus()) {
           tab_adquisiones.insertar();
           tab_adquisiones.setValor("IDE_ADEMDE", ide_ademple);
           System.out.println("empleado "+ide_ademple);
            
        } else if (tab_compra_bienes.isFocus()) {
            tab_compra_bienes.insertar();
        } else if (tab_presupuesto.isFocus()) {
            tab_presupuesto.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_adquisiones.isFocus()) {
            tab_adquisiones.guardar();
        } else if (tab_compra_bienes.isFocus()) {
            tab_compra_bienes.guardar();
        } else if (tab_presupuesto.isFocus()) {
            tab_presupuesto.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_adquisiones.isFocus()) {
            tab_adquisiones.eliminar();
        } else if (tab_compra_bienes.isFocus()) {
            tab_compra_bienes.eliminar();
        } else if (tab_presupuesto.isFocus()) {
            tab_presupuesto.eliminar();
        }
    }

    public Tabla getTab_adquisiones() {
        return tab_adquisiones;
    }

    public void setTab_adquisiones(Tabla tab_adquisiones) {
        this.tab_adquisiones = tab_adquisiones;
    }

   /* public Tabla getTab_certificacion() {
        return tab_certificacion;
    }

    public void setTab_certificacion(Tabla tab_certificacion) {
        this.tab_certificacion = tab_certificacion;
    }*/

    public Tabla getTab_compra_bienes() {
        return tab_compra_bienes;
    }

    public void setTab_compra_bienes(Tabla tab_compra_bienes) {
        this.tab_compra_bienes = tab_compra_bienes;
    }

    public VisualizarPDF getVipdf_comprobante() {
        return vipdf_comprobante;
    }

    public void setVipdf_comprobante(VisualizarPDF vipdf_comprobante) {
        this.vipdf_comprobante = vipdf_comprobante;
    }

    public Tabla getTab_presupuesto() {
        return tab_presupuesto;
    }

    public void setTab_presupuesto(Tabla tab_presupuesto) {
        this.tab_presupuesto = tab_presupuesto;
    }

    public SeleccionTabla getSel_tab_proveedor() {
        return sel_tab_proveedor;
    }

    public void setSel_tab_proveedor(SeleccionTabla sel_tab_proveedor) {
        this.sel_tab_proveedor = sel_tab_proveedor;
    }

    public SeleccionTabla getSel_tab_presupuesto() {
        return sel_tab_presupuesto;
    }

    public void setSel_tab_presupuesto(SeleccionTabla sel_tab_presupuesto) {
        this.sel_tab_presupuesto = sel_tab_presupuesto;
    }
public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public Map getMap_parametros() {
        return map_parametros;
    }

    public void setMap_parametros(Map map_parametros) {
        this.map_parametros = map_parametros;
    }
}

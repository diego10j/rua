package paq_adquisicion;

/**
 *
 * @author Andres Redroban
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class Adquisiciones extends Pantalla {

    private Tabla tab_adquisiones = new Tabla();
    private Tabla tab_certificacion = new Tabla();
    private Tabla tab_compra_bienes = new Tabla();
    
    private Reporte rep_reporte = new Reporte(); //Listado de Reportes, siempre se llama rep_reporte
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte(); //formato de salida del reporte
    private Map map_parametros = new HashMap();//Parametros del reporte
    public static String par_ti_anulado;
    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();    

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public Adquisiciones() {
        
        
         
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
        Object fila7[] = {"1", "BODEGA MUNICIPAL"};
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
        tab_adquisiones.getColumna("IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3","1","1","1"));
        tab_adquisiones.getColumna("IDE_ADEMPLE").setCombo(ser_adquisiciones.getEmpleado());
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3","1","1","1"));
        tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3","1","1","1"));
        tab_adquisiones.agregarRelacion(tab_certificacion);
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

        tab_certificacion.setId("tab_certificacion");
        tab_certificacion.setIdCompleto("tab_tabulador:tab_certificacion");
        tab_certificacion.setTabla("ADQ_CERTIFICACION", "IDE_ADCERT", 2);
        List lista3 = new ArrayList();
        Object fila3[] = {"1", "CERTIFICACION"};
        Object fila4[] = {"2", "COMPROMISO"};
        lista3.add(fila3);
        lista3.add(fila4);
        tab_certificacion.getColumna("tipo_documento_adcert").setCombo(lista3);
        tab_certificacion.getColumna("IDE_ADCERT").setNombreVisual("CODIGO");
        tab_certificacion.getColumna("TIPO_DOCUMENTO_ADCERT").setNombreVisual("TIPO DOCUMENTO");
        tab_certificacion.getColumna("NRO_CERTIFICACION_ADCERT").setNombreVisual("NUMERO CERTIFICACION");
        tab_certificacion.getColumna("PARTIDA_ADCERT").setNombreVisual("PARTIDA");
        tab_certificacion.getColumna("VALOR_ADCERT").setNombreVisual("VALOR");
        tab_certificacion.dibujar();
        PanelTabla pat_panel_certificacion = new PanelTabla();
        pat_panel_certificacion.setId("pat_panel_certificacion");
        pat_panel_certificacion.setPanelTabla(tab_certificacion);

        tab_compra_bienes.setId("tab_compra_bienes");
        tab_compra_bienes.setIdCompleto("tab_tabulador:tab_compra_bienes");
        tab_compra_bienes.setTabla("ADQ_COMPRA_BIENES", "IDE_ADCOBI", 3);
        tab_compra_bienes.getColumna("IDE_ADMATE").setCombo(ser_adquisiciones.getMaterial("0","0"));
        tab_compra_bienes.getColumna("IDE_ADCOBI").setNombreVisual("CODIGO");
        tab_compra_bienes.getColumna("IDE_ADMATE").setNombreVisual("MATERIAL");
        tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setNombreVisual("CANTIDAD");
        tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setNombreVisual("VALOR UNITARIO");
        tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setNombreVisual("DESCUENTO");
        tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setNombreVisual("PORCENTAJE DESCUENTO");
        tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setNombreVisual("SUBTOTAL");
        tab_compra_bienes.getColumna("IVA_ADCOBI").setNombreVisual("IVA");
        tab_compra_bienes.getColumna("TOTAL_ADCOBI").setNombreVisual("TOTAL");
        tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setNombreVisual("CANTIDAD");

        tab_compra_bienes.getColumna("IDE_ADCOBI").setOrden(1);
        tab_compra_bienes.getColumna("IDE_ADMATE").setOrden(2);
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

        tab_tabulador.agregarTab("CERTIFICACION", pat_panel_certificacion);
        tab_tabulador.agregarTab("COMPRA BIENES", pat_panel_compra_bienes);

        Division div_adquisiciones = new Division();
        div_adquisiciones.setId("div_adquisiciones");
        div_adquisiciones.dividir2(pat_adquisiciones, tab_tabulador, "70%", "H");
        agregarComponente(div_adquisiciones);
        
        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        
        vipdf_comprobante.setId("vipdf_comprobante");
        vipdf_comprobante.setTitle("SOLICITUD DE COMPRA");
        agregarComponente(vipdf_comprobante);
        

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
      

    @Override
    public void insertar() {
        if (tab_adquisiones.isFocus()) {
            tab_adquisiones.insertar();
        } else if (tab_certificacion.isFocus()) {
            tab_certificacion.insertar();
        } else if (tab_compra_bienes.isFocus()) {
            tab_compra_bienes.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_adquisiones.isFocus()) {
            tab_adquisiones.guardar();
        } else if (tab_certificacion.isFocus()) {
            tab_certificacion.guardar();
        } else if (tab_compra_bienes.isFocus()) {
            tab_compra_bienes.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_adquisiones.isFocus()) {
            tab_adquisiones.eliminar();
        } else if (tab_certificacion.isFocus()) {
            tab_certificacion.eliminar();
        } else if (tab_compra_bienes.isFocus()) {
            tab_compra_bienes.eliminar();
        }
    }

    public Tabla getTab_adquisiones() {
        return tab_adquisiones;
    }

    public void setTab_adquisiones(Tabla tab_adquisiones) {
        this.tab_adquisiones = tab_adquisiones;
    }

    public Tabla getTab_certificacion() {
        return tab_certificacion;
    }

    public void setTab_certificacion(Tabla tab_certificacion) {
        this.tab_certificacion = tab_certificacion;
    }

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

}

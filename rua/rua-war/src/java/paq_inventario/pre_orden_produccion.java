/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import framework.aplicacion.Columna;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_orden_produccion extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);

    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

    private Confirmar con_confirma = new Confirmar();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private AutoCompletar aut_ordenes = new AutoCompletar();

    public pre_orden_produccion() {

        bar_botones.agregarReporte();
        bar_botones.quitarBotonsNavegacion();

        aut_ordenes.setId("aut_ordenes");
        aut_ordenes.setAutoCompletar(ser_inventario.getSqlOrdenesProduccion());
        aut_ordenes.setMetodoChange("seleccionarOrden");
        aut_ordenes.setSize(80);
        aut_ordenes.setMaxResults(20);
        aut_ordenes.setAutocompletarContenido();
        bar_botones.agregarComponente(aut_ordenes);

        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setTitle("Anular Orden");
        bot_anular.setMetodo("abrirAnularOrden");
        bar_botones.agregarSeparador();
        bar_botones.agregarBoton(bot_anular);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular la Orden de Producción Seleccionada ?");
        con_confirma.setTitle("ANULAR ORDEN DE PRODUCCIÓN");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_rep.setId("sel_rep");
        agregarComponente(rep_reporte);
        agregarComponente(sel_rep);

        tab_tabla1.setHeader("ORDEN DE PRODUCCIÓN");
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_cab_comp_inve", "ide_incci", 1);
        tab_tabla1.setTipoFormulario(true);
        //tab_tabla1.agregarRelacion(tab_tabla2);
        //oculta todas las columnas
        for (Columna columna : tab_tabla1.getColumnas()) {
            columna.setVisible(false);
        }
        tab_tabla1.getColumna("ide_geper").setValorDefecto(utilitario.getVariable("p_con_beneficiario_empresa"));
        tab_tabla1.getColumna("ide_inepi").setValorDefecto("1");//normal
        tab_tabla1.getColumna("ide_inepi").setCombo("inv_est_prev_inve", "ide_inepi", "nombre_inepi", "");
        tab_tabla1.getColumna("ide_inepi").setVisible(true);
        tab_tabla1.getColumna("ide_inepi").setLectura(true);
        tab_tabla1.getColumna("ide_intti").setValorDefecto("16");//ORDEN DE PRODUCCION
        tab_tabla1.getColumna("ide_intti").setCombo("inv_tip_tran_inve", "ide_intti", "nombre_intti", "ide_intti in (16,28)");
        tab_tabla1.getColumna("ide_intti").setVisible(true);
        tab_tabla1.getColumna("ide_intti").setLectura(true);
        tab_tabla1.getColumna("ide_inbod").setCombo(ser_producto.getSqlBodegasCombo());
        tab_tabla1.getColumna("ide_inbod").setVisible(true);
        tab_tabla1.getColumna("ide_inbod").setLectura(true);
        tab_tabla1.setCondicion("ide_intti=16 and ide_incci=-1");
        tab_tabla1.getColumna("fecha_trans_incci").setVisible(true);
        tab_tabla1.getColumna("fecha_trans_incci").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.setCampoOrden("fecha_trans_incci");
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.setCampoOrden("ide_incci desc");
        tab_tabla1.onSelect("seleccionarOrden");
        tab_tabla1.dibujar();

        tab_tabla1.getGrid().setColumns(8);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setCondicionSucursal(true);
        tab_tabla2.setTabla("inv_orden_prod", "ide_inorp", 2);
        tab_tabla2.getColumna("ide_incfo").setRequerida(true);
        tab_tabla2.getColumna("ide_incfo").setCombo("select ide_incfo,concepto_incfo,num_formula_incfo,codigo_inarti,nombre_inarti,cantidad_incfo , nombre_inuni \n"
                + "from inv_cab_formula a inner join inv_articulo b on a.ide_inarti= b.ide_inarti \n"
                + "left join inv_unidad c on a.ide_inuni=c.ide_inuni\n"
                + "order by concepto_incfo");
        tab_tabla2.getColumna("ide_incfo").setAutoCompletar();
        tab_tabla2.getColumna("ide_incfo").setMetodoChange("seleccionarFormula");
        tab_tabla2.getColumna("ide_inorp").setVisible(false);

        tab_tabla2.getColumna("ide_incci").setVisible(true);
        tab_tabla2.getColumna("inv_ide_incci").setVisible(true);

        tab_tabla2.getColumna("ide_incci").setEstilo("font-size: 12px;font-weight: bold");
        tab_tabla2.getColumna("inv_ide_incci").setEstilo("font-size: 12px;font-weight: bold");
        tab_tabla2.getColumna("ide_incci").setEtiqueta();
        tab_tabla2.getColumna("inv_ide_incci").setEtiqueta();

        tab_tabla2.getColumna("num_orden_inorp").setEtiqueta();
        tab_tabla2.getColumna("num_orden_inorp").setEstilo("font-size: 13px;font-weight: bold");
        tab_tabla2.getColumna("fecha_entrega_inorp").setValorDefecto(utilitario.getFechaActual());
        tab_tabla2.getColumna("fecha_entrega_inorp").setRequerida(true);
        tab_tabla2.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_tabla2.getColumna("ide_inuni").setRequerida(true);//DFJ
        tab_tabla2.getColumna("fecha_caduca_inorp").setValorDefecto(utilitario.getFechaActual());
        tab_tabla2.getColumna("concepto_inorp").setRequerida(true);
        tab_tabla2.getColumna("cantidad_inorp").setRequerida(true);
        tab_tabla2.getColumna("cantidad_inorp").setMetodoChange("calculaOrden");
        tab_tabla2.getColumna("total_materia_inorp").setLectura(true);
        tab_tabla2.getColumna("total_produccion_inorp").setLectura(true);
        tab_tabla2.getColumna("total_materia_inorp").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_tabla2.getColumna("total_produccion_inorp").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_tabla2.getColumna("total_servicios_inorp").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_tabla2.getColumna("total_gastos_inorp").setValorDefecto(utilitario.getFormatoNumero("0"));
        if (tab_tabla1.isEmpty() == false) {
            tab_tabla2.setCondicion("ide_incci=" + tab_tabla1.getValor("ide_incci")); //detalle orden
        } else {
            tab_tabla2.setCondicion("ide_incci=-1");
        }

        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.getGrid().setColumns(6);
        tab_tabla2.setMostrarNumeroRegistros(false);
        tab_tabla2.dibujar();

        Grupo gru = new Grupo();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.setStyle("width:99%;overflow: hiden;");
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        gru.getChildren().add(pat_panel);
        gru.getChildren().add(pat_panel2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setHeader("DETALLE DE LA ORDEN DE PRODUCCIÓN");
        tab_tabla3.setTabla("inv_det_comp_inve", "ide_indci", 3);

        tab_tabla3.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaProductos());
        tab_tabla3.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla3.getColumna("ide_inarti").setLectura(false);
        tab_tabla3.getColumna("cantidad1_indci").setVisible(true);
        tab_tabla3.getColumna("cantidad1_indci").setEtiqueta();
        tab_tabla3.getColumna("cantidad_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla3.getColumna("precio_indci").setMetodoChange("calcularTotalDetalles");
        tab_tabla3.getColumna("cantidad_indci").setRequerida(true);
        tab_tabla3.getColumna("cantidad_indci").setFormatoNumero(3);
        tab_tabla3.getColumna("cantidad1_indci").setFormatoNumero(3);
        tab_tabla3.getColumna("precio_indci").setRequerida(true);
        tab_tabla3.getColumna("ide_inarti").setRequerida(true);
        tab_tabla3.getColumna("valor_indci").setRequerida(true);
        tab_tabla3.getColumna("valor_indci").setEtiqueta();
        tab_tabla3.getColumna("valor_indci").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla3.getColumna("referencia_indci").setVisible(false);
        tab_tabla3.getColumna("referencia1_indci").setVisible(false);
        tab_tabla3.getColumna("secuencial_indci").setVisible(false);
        tab_tabla3.getColumna("precio_promedio_indci").setVisible(false);
        tab_tabla3.getColumna("ide_cccfa").setVisible(false);
        tab_tabla3.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla3.getColumna("ide_incci").setVisible(false);
        tab_tabla3.getColumna("ide_indci").setVisible(false);
        if (tab_tabla2.isEmpty() == false) {
            tab_tabla3.setCondicion("ide_incci=" + tab_tabla2.getValor("inv_ide_incci"));
        } else {
            tab_tabla3.setCondicion("ide_incci=-1");
        }
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);
        pat_panel3.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel3.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel3.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel3.getMenuTabla().getItem_buscar().setRendered(false);

        Division div = new Division();
        div.dividir2(gru, pat_panel3, "50%", "H");

        agregarComponente(div);

    }

    public void seleccionarOrden(SelectEvent evt) {
        aut_ordenes.onSelect(evt);
        if (aut_ordenes.getValue() != null) {
            String ide_incci = aut_ordenes.getValorArreglo(7);
            if (ide_incci != null) {
                //tab_tabla1.setCondicion("ide_intti=16 and ide_incci=" + ide_incci);
                tab_tabla1.setCondicion("ide_incci=" + ide_incci);
                tab_tabla1.ejecutarSql();
                tab_tabla2.setCondicion("ide_incci=" + tab_tabla1.getValor("ide_incci")); //detalle orden
                tab_tabla2.ejecutarSql();
                tab_tabla3.setCondicion("ide_incci=" + tab_tabla2.getValor("inv_ide_incci")); //carga formula        
                tab_tabla3.ejecutarSql();
            } else {
                utilitario.agregarMensajeError("La Orden de Producción no se creeo correctamente", "");
            }
        } else {
            limpiar();
        }
    }

    public void abrirAnularOrden() {
        if (tab_tabla2.getValor("ide_inorp") != null) {
            con_confirma.getBot_aceptar().setMetodo("anularOrden");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Orden de Producción", "");
        }
    }

    public void anularOrden() {
        if (tab_tabla2.getValor("ide_inorp") != null) {
            ser_inventario.anularComprobanteInventario(tab_tabla2.getValor("ide_incci"));
            ser_inventario.anularComprobanteInventario(tab_tabla2.getValor("inv_ide_incci"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                tab_tabla1.actualizar();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Orden de Producción", "");
        }
    }

    public void seleccionarFormula(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        if (tab_tabla2.getValor("ide_incfo") != null) {
            TablaGenerica tag = ser_inventario.getDetalleFormula(tab_tabla2.getValor("ide_incfo"));
            tab_tabla3.limpiar();
            for (int i = 0; i < tag.getTotalFilas(); i++) {
                tab_tabla3.insertar();
                tab_tabla3.setValor("ide_inarti", tag.getValor(i, "ide_inarti"));
                tab_tabla3.setValor("cantidad1_indci", tag.getValor(i, "cantidad_indfo"));
                tab_tabla3.setValor("cantidad_indci", tag.getValor(i, "cantidad_indfo"));
                double precio = ser_producto.getUltimoPrecioIngresoInventario(tag.getValor(i, "ide_inarti"));
                tab_tabla3.setValor("precio_indci", utilitario.getFormatoNumero(precio));
                double cantidad = 0;
                try {
                    cantidad = Double.parseDouble(tag.getValor(i, "cantidad_indfo"));
                } catch (Exception e) {
                }
                tab_tabla3.setValor("valor_indci", utilitario.getFormatoNumero(cantidad * precio));
            }
            utilitario.addUpdate("tab_tabla3");
        } else {
            tab_tabla3.limpiar();
        }

    }

    /**
     * Calcula totales de la Orden
     */
    public void calcularTotalOrden() {
        try {
            double total_meteria = 0;

            double total_servicio = 0;
            double total_gasto = 0;

            for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
                try {
                    total_meteria = Double.parseDouble(tab_tabla3.getValor(i, "valor_indci")) + total_meteria;
                } catch (Exception e) {
                }
            }

            try {
                total_servicio = Double.parseDouble(tab_tabla2.getValor("total_servicios_inorp"));
            } catch (Exception e) {
            }

            try {
                total_gasto = Double.parseDouble(tab_tabla2.getValor("total_gastos_inorp"));
            } catch (Exception e) {
            }

            tab_tabla2.setValor("total_materia_inorp", utilitario.getFormatoNumero(total_meteria));
            tab_tabla2.setValor("total_produccion_inorp", utilitario.getFormatoNumero(total_meteria + total_servicio + total_gasto));

            utilitario.addUpdate("tab_tabla2");
        } catch (Exception e) {
        }
    }

    private void calculaOrden() {
        if (tab_tabla2.isEmpty() == false) {
            double cantidad = 0;
            try {
                cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_inorp"));
            } catch (Exception e) {
            }
            double formula_cantidad = 0;

            try {
                formula_cantidad = Double.parseDouble(tab_tabla2.getValorArreglo("ide_incfo", 5));
            } catch (Exception e) {
            }
            if (cantidad > 0) {
                //recorre tabla
                for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
                    double cant_detalle = 0;
                    try {
                        cant_detalle = Double.parseDouble(tab_tabla3.getValor(i, "cantidad1_indci"));
                    } catch (Exception e) {
                    }
                    double new_cantidad = (cantidad * cant_detalle) / formula_cantidad;
                    tab_tabla3.setValor(i, "cantidad_indci", utilitario.getFormatoNumero(new_cantidad,3));

                    //calcula subtotal de la fila
                    double dou_cantidad = 0;
                    double dou_precio = 0;
                    double dou_valor = 0;
                    try {
                        dou_cantidad = Double.parseDouble(tab_tabla3.getValor(i, "cantidad_indci"));
                        dou_precio = Double.parseDouble(tab_tabla3.getValor(i, "precio_indci"));
                    } catch (Exception e) {
                        dou_cantidad = 0;
                        dou_precio = 0;
                    }
                    dou_valor = dou_cantidad * dou_precio;
                    tab_tabla3.setValor(i, "valor_indci", utilitario.getFormatoNumero(dou_valor));
                    if (tab_tabla1.isFilaInsertada() == false) {
                        tab_tabla3.modificar(i);
                    }
                }
                utilitario.addUpdate("tab_tabla3");
                calcularTotalOrden();
            } else {
                utilitario.agregarMensajeError("La Cantidad ingresada no no válida", "");
            }

        }
    }

    public void limpiar() {
        aut_ordenes.limpiar();
        tab_tabla1.limpiar();
        tab_tabla2.limpiar();
        tab_tabla3.limpiar();
    }

    public void calculaOrden(AjaxBehaviorEvent evt) {
        //Calcula 
        tab_tabla2.modificar(evt);
        calculaOrden();
    }

//    private void actualizarTablas() {
//        utilitario.addUpdate("tab_tabla1");
//        if (tab_tabla1.getValor("ide_incci") != null) {
//            tab_tabla2.setCondicion("ide_incci=" + tab_tabla1.getValor("ide_incci")); //detalle orden
//            tab_tabla2.ejecutarSql();
//            tab_tabla2.imprimirSql();
//            tab_tabla3.setCondicion("ide_incci=" + tab_tabla2.getValor("inv_ide_incci")); //carga formula        
//            tab_tabla3.ejecutarSql();
//            tab_tabla3.imprimirSql();
//        } else {
//            utilitario.agregarMensajeError("La Orden de Producción no tiene comprobantes de inventario", "");
//        }
//
//    }
    public void calcularTotalDetalles(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularDetalles();
    }

    private void calcularDetalles() {
        double dou_cantidad = 0;
        double dou_precio = 0;
        double dou_valor = 0;
        try {
            dou_cantidad = Double.parseDouble(tab_tabla3.getValor("cantidad_indci"));
            dou_precio = Double.parseDouble(tab_tabla3.getValor("precio_indci"));
        } catch (Exception e) {
            dou_cantidad = 0;
            dou_precio = 0;
        }
        double dou_existencia = ser_producto.getCantidadProductoBodega(tab_tabla3.getValor("ide_inarti"), tab_tabla1.getValor("ide_inbod"));
        if (dou_cantidad > dou_existencia) {
            utilitario.agregarMensajeError("La cantidad ingresada es mayor a la existencia en Inventario", "Existencia actual de " + tab_tabla3.getValorArreglo("ide_inarti", 1) + " es :" + utilitario.getFormatoNumero(dou_existencia));
        }

        dou_valor = dou_cantidad * dou_precio;
        tab_tabla3.setValor("valor_indci", utilitario.getFormatoNumero(dou_valor));
        tab_tabla3.sumarColumnas();
        utilitario.addUpdateTabla(tab_tabla3, "valor_indci", "");
        calcularTotalOrden();
    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_inbod", ser_inventario.getBodegaSucursal());
            tab_tabla2.insertar();
            tab_tabla2.setValor("num_orden_inorp", ser_inventario.getSecuencialOrden());
            tab_tabla3.limpiar();
        }
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Comprobante de Inventario")) {
            if (rep_reporte.isVisible()) {

                if (tab_tabla2.getValor("ide_incci") != null) {
                    rep_reporte.cerrar();
                    Map parametro = new HashMap();
                    parametro.put("ide_incci", Long.parseLong(tab_tabla2.getValor("inv_ide_incci")));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "El Orden de Producción seleccionada no tiene comprobante de inventario");
                }
            }
        }

    }

    @Override
    public void guardar() {
        tab_tabla1.setValor("ide_intti", "16");
        boolean es_nuevo = tab_tabla1.isFilaInsertada();
        tab_tabla1.setValor("observacion_incci", "ORDEN DE PRODUCCIÓN N. " + tab_tabla2.getValor("num_orden_inorp") + " : " + tab_tabla2.getValorArreglo("ide_incfo", 1));
        if (es_nuevo) {
            tab_tabla1.setValor("numero_incci", ser_inventario.getSecuencialComprobanteInventario(tab_tabla1.getValor("ide_inbod")));
            tab_tabla1.setValidarInsertar(false);
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_inbod", ser_inventario.getBodegaSucursal());
            tab_tabla1.setValor(0, "ide_intti", "28"); //FORMULA DE PRODUCCIÓN        
            tab_tabla1.setValor(0, "numero_incci", ser_inventario.getSecuencialComprobanteInventario(tab_tabla1.getValor("ide_inbod")));
            tab_tabla1.setValor(0, "observacion_incci", "ORDEN DE PRODUCCIÓN N. " + tab_tabla2.getValor("num_orden_inorp") + " : " + tab_tabla2.getValorArreglo("ide_incfo", 1));
            tab_tabla1.setValor(0, "ide_inbod", ser_inventario.getBodegaSucursal());
            tab_tabla1.setValor(0, "fecha_trans_incci", tab_tabla1.getValor(1, "fecha_trans_incci"));
        } else {
            tab_tabla1.modificar(tab_tabla1.getFilaActual());
        }

        if (tab_tabla1.guardar()) {

            if (es_nuevo) {
                tab_tabla2.setValor("num_orden_inorp", ser_inventario.getSecuencialOrden());
                tab_tabla2.setValor("inv_ide_incci", tab_tabla1.getValor(0, "ide_incci")); //formula
                tab_tabla2.setValor("ide_incci", tab_tabla1.getValor(1, "ide_incci"));            //orden 
            }

            if (tab_tabla2.guardar()) {
                for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
                    tab_tabla3.setValor(i, "ide_incci", tab_tabla2.getValor("inv_ide_incci"));
                    if (es_nuevo == false) {
                        tab_tabla3.modificar(i);
                    }
                }
                if (es_nuevo == false) {
                    //Elimina detalle de inventario creado
                    utilitario.getConexion().agregarSqlPantalla("DELETE FROM inv_det_comp_inve WHERE ide_incci=" + tab_tabla2.getValor("ide_incci"));
                }
                //crea la orden de produccion                    
                tab_tabla3.insertar();
                tab_tabla3.setValor(0, "ide_inarti", ser_inventario.getProductoFormula(tab_tabla2.getValor("ide_incfo")));
                tab_tabla3.setValor(0, "ide_incci", tab_tabla2.getValor("ide_incci"));
                tab_tabla3.setValor(0, "cantidad_indci", tab_tabla2.getValor("cantidad_inorp"));
                tab_tabla3.setValor(0, "valor_indci", tab_tabla2.getValor("total_produccion_inorp"));

                double precio = 0;
                try {
                    precio = ((Double.parseDouble(tab_tabla2.getValor("total_produccion_inorp"))) / (Double.parseDouble(tab_tabla2.getValor("cantidad_inorp"))));
                } catch (Exception e) {
                }
                tab_tabla3.setValor(0, "precio_indci", utilitario.getFormatoNumero(precio));

                if (tab_tabla3.guardar()) {
                    //guarda comprobante de inventario con la orden de producción
                    if (guardarPantalla().isEmpty()) {
                        tab_tabla3.setCondicion("ide_incci=" + tab_tabla2.getValor("inv_ide_incci")); //carga formula        
                        tab_tabla3.ejecutarSql();
                    }
                }

            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
            tab_tabla1.eliminar();
            tab_tabla2.eliminar();
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public AutoCompletar getAut_ordenes() {
        return aut_ordenes;
    }

    public void setAut_ordenes(AutoCompletar aut_ordenes) {
        this.aut_ordenes = aut_ordenes;
    }

}

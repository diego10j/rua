/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_adquisicion;

/**
 *
 * @author Andres
 */
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;
import sistema.aplicacion.Utilitario;

public class AdquisicionesOrdenadorGasto extends Pantalla {

    private Tabla tab_adquisiones = new Tabla();
    private Tabla tab_certificacion = new Tabla();
    private Tabla tab_compra_bienes = new Tabla();
    public static String par_tipo_secretaria = "";
    private Combo com_direccion = new Combo();
    public static String par_tipo_bodeguero = "";
    public static String par_aprueba_gasto = "";
    public static String par_aprueba_solicitud = "";
    public static String par_anulado = "";

    private Reporte rep_reporte = new Reporte(); //Listado de Reportes, siempre se llama rep_reporte
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte(); //formato de salida del reporte
    private Map map_parametros = new HashMap();//Parametros del reporte     
    private Confirmar con_guardar_aprobado = new Confirmar();
    private Confirmar con_guardar_anulado = new Confirmar();

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public AdquisicionesOrdenadorGasto() {
        par_tipo_secretaria = utilitario.getVariable("p_tipo_secretaria");
        par_tipo_bodeguero = utilitario.getVariable("p_tipo_bodeguero");
        par_aprueba_gasto = utilitario.getVariable("p_tipo_generador_gasto");
        par_aprueba_solicitud = utilitario.getVariable("p_tipo_aprueba_solicitud");
        par_anulado = utilitario.getVariable("p_tipo_anulado");

        bar_botones.getBot_insertar().setRendered(false);

        //if (tienePerfilSecretaria() != 0) {

            Boton bot_aprobar = new Boton();

            bot_aprobar.setIcon("ui-icon-search");
            bot_aprobar.setValue("APROBAR GASTO");
            bot_aprobar.setMetodo("aprobarSolicitud");

            Boton bot_anular = new Boton();

            bot_anular.setIcon("ui-icon-search");
            bot_anular.setValue("ANULAR SOLICITUD");
            bot_anular.setMetodo("anularSolicitud");

            Boton bot_check = new Boton();

            bot_check.setIcon("ui-icon-check");
            bot_check.setValue("MOSTRAR PENDIENTES");
            bot_check.setMetodo("filtroDireccion");

            rep_reporte.setId("rep_reporte");
            agregarComponente(rep_reporte);
            bar_botones.agregarReporte();
            sel_rep.setId("sel_rep");
            agregarComponente(sel_rep);

          //  com_direccion.setId("com_direccion");
            //com_direccion.setCombo(ser_adquisiciones.getAreaAdministrativa("1", ide_ademple));
//            agregarComponente(com_direccion);
//            bar_botones.agregarComponente(com_direccion);
//            com_direccion.setMetodo("filtroDireccion");
            bar_botones.agregarBoton(bot_check);
            bar_botones.agregarBoton(bot_aprobar);
            //          bar_botones.agregarBoton(bot_anular);

            Tabulador tab_tabulador = new Tabulador();
            tab_tabulador.setId("tab_tabulador");

            tab_adquisiones.setId("tab_adquisiones");   //identificador
            tab_adquisiones.setTabla("adq_compra", "ide_adcomp", 1);
            tab_adquisiones.setCondicion("ide_adcomp=-1");
            List lista = new ArrayList();
            List lista1 = new ArrayList();
            List lista2 = new ArrayList();
            List listax = new ArrayList();
            Object fila1[] = {"1", "SI"};
            Object fila2[] = {"2", "NO"};
            Object fila5[] = {"1", "COMPRA EN STOCK"};
            Object fila6[] = {"2", "COMPRA DE CONSUMO DIRECTO"};
            Object fila7[] = {"1", "BODEGA MUNICIPAL"};
            Object fila8[] = {"2", "ACTIVOS FIJOS"};
            Object fila9[] = {"1", "NO EXISTE"};
            Object fila10[] = {"0", "EXISTE"};
            lista.add(fila1);
            lista.add(fila2);
            lista2.add(fila5);
            lista2.add(fila6);
            lista1.add(fila7);
            lista1.add(fila8);
            listax.add(fila9);
            listax.add(fila10);
            tab_adquisiones.getColumna("existe_adcomp").setRadio(lista, "1");
            tab_adquisiones.getColumna("tipo_compra_adcomp").setCombo(lista2);
            tab_adquisiones.getColumna("INGRESO_ADCOMP").setCombo(lista1);
            tab_adquisiones.getColumna("APLICA_ADCOMP").setCombo(listax);
            tab_adquisiones.getColumna("APLICA_ADCOMP").setNombreVisual("EXISTE/NO EXISTE EN BODEGA");
            tab_adquisiones.getColumna("APRUEBA_ADCOMP").setRadio(lista, "1");
            tab_adquisiones.getColumna("IDE_ADAPRO").setCombo(ser_adquisiciones.getAprobado());
            tab_adquisiones.getColumna("IDE_ADEMAP").setCombo(ser_adquisiciones.getEmpleadoAprueba("3", "", "1", ide_ademple));
            tab_adquisiones.getColumna("IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3", "1", "1", "1"));
            tab_adquisiones.getColumna("IDE_ADEMDE").setAutoCompletar();
            tab_adquisiones.getColumna("IDE_ADEMDE").setLectura(true);
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3", "1", "1", "1"));
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3", "1", "1", "1"));// representa el nombre del boeguero
            tab_adquisiones.agregarRelacion(tab_certificacion);
            tab_adquisiones.agregarRelacion(tab_compra_bienes);
            tab_adquisiones.setTipoFormulario(true);
            tab_adquisiones.getGrid().setColumns(6);
            tab_adquisiones.getColumna("IDE_ADEMAP").setNombreVisual("SOLICITANTE");
            tab_adquisiones.getColumna("IDE_ADEMDE").setNombreVisual("REGISTRO SOLICITUD");
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setNombreVisual("APROBADOR BODEGA");

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

            tab_adquisiones.getColumna("IDE_ADAPRO").setVisible(false);
            tab_adquisiones.getColumna("TIPO_COMPRA_ADCOMP").setVisible(true);
            tab_adquisiones.getColumna("DESCRIPCION_ADCOMP").setVisible(true);
            //tab_adquisiones.getColumna("INGRESO_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("EXISTE_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("INGRESO_ADCOMP").setVisible(true);
            tab_adquisiones.getColumna("NUMERO_ORDEN_ADCOMP").setVisible(true);
            tab_adquisiones.getColumna("FECHA_SOLICITUD_ADCOMP").setVisible(true);
            //tab_adquisiones.getColumna("INGRESO_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("VALOR_PRESUPUESTADO_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("VALOR_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("FECHA_ADJUDICADO_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("ADJUDICADOR_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("PROVEEDOR_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("RUC_PROVEEDOR_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("CODIGO_SIS_PROV_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("DESCUENTO_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("SUBTOTAL_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("IVA_ADCOMP").setVisible(false);
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
            //tab_adquisiones.getColumna("APLICA_ADCOMP").setVisible(false);
            //tab_adquisiones.getColumna("IDE_ADEMAP").setVisible(false);
            tab_adquisiones.getColumna("IDE_ADARAD").setVisible(false);
            tab_adquisiones.getColumna("IDE_ADEMPLE").setVisible(false);
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setVisible(false);
            //tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setVisible(false);
            //tab_adquisiones.getColumna("PRUEBA_DIRECTOR_ADCOMP").setVisible(false);
            //tab_adquisiones.getColumna("ATIENDE_BODEGA_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("APRUEBA_GASTO_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("REGISTRA_COMPRAS_ADCOMP").setVisible(false);
            tab_adquisiones.setLectura(true);
            tab_adquisiones.dibujar();

            PanelTabla pat_adquisiciones = new PanelTabla();
            pat_adquisiciones.setId("pat_adquisiciones");
            pat_adquisiciones.setPanelTabla(tab_adquisiones);

         /*   tab_certificacion.setId("tab_certificacion");
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
            tab_certificacion.setLectura(true);
            tab_certificacion.dibujar();
            PanelTabla pat_panel_certificacion = new PanelTabla();
            pat_panel_certificacion.setId("pat_panel_certificacion");
            pat_panel_certificacion.setPanelTabla(tab_certificacion);*/

            tab_compra_bienes.setId("tab_compra_bienes");
            tab_compra_bienes.setIdCompleto("tab_tabulador:tab_compra_bienes");
            tab_compra_bienes.setTabla("ADQ_COMPRA_BIENES", "IDE_ADCOBI", 3);
            tab_compra_bienes.getColumna("IDE_INARTI").setCombo(ser_adquisiciones.getMaterial("0", "0"));
            tab_compra_bienes.getColumna("IDE_ADCOBI").setNombreVisual("CODIGO");
            tab_compra_bienes.getColumna("IDE_INARTI").setNombreVisual("MATERIAL");
            tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setNombreVisual("CANTIDAD");
            tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setNombreVisual("VALOR UNITARIO");
            tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setNombreVisual("DESCUENTO");
            tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setNombreVisual("PORCENTAJE DESCUENTO");
            tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setNombreVisual("SUBTOTAL");
            tab_compra_bienes.getColumna("IVA_ADCOBI").setNombreVisual("IVA");
            tab_compra_bienes.getColumna("TOTAL_ADCOBI").setNombreVisual("TOTAL");
            tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setVisible(false);
            tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setVisible(false);
            tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setVisible(false);
            tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setVisible(false);
            tab_compra_bienes.getColumna("IVA_ADCOBI").setVisible(false);
            tab_compra_bienes.getColumna("TOTAL_ADCOBI").setVisible(false);
            tab_compra_bienes.getColumna("NO_EXISTE_ADCOBI").setVisible(false);
            tab_compra_bienes.getColumna("NO_EXISTE_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("IDE_INARTI").setLongitud(250);
            tab_compra_bienes.getColumna("IDE_ADCOBI").setOrden(1);
            tab_compra_bienes.getColumna("IDE_INARTI").setOrden(2);
            tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setOrden(3);

            tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("IVA_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("TOTAL_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.setLectura(true);
            tab_compra_bienes.dibujar();
            PanelTabla pat_panel_compra_bienes = new PanelTabla();
            pat_panel_compra_bienes.setId("pat_panel_compra_bienes");
            pat_panel_compra_bienes.setPanelTabla(tab_compra_bienes);

          //  tab_tabulador.agregarTab("CERTIFICACION", pat_panel_certificacion);
           // tab_tabulador.agregarTab("COMPRA BIENES", pat_panel_compra_bienes);

            Division div_adquisiciones = new Division();
            div_adquisiciones.setId("div_adquisiciones");
            div_adquisiciones.dividir2(pat_adquisiciones, pat_panel_compra_bienes, "70%", "H");
            agregarComponente(div_adquisiciones);

           // con_guardar_aprobado.setId("con_guardar_aprobado");
            //agregarComponente(con_guardar_aprobado);

            //con_guardar_anulado.setId("con_guardar_anulado");
            //agregarComponente(con_guardar_anulado);
       // } else {
         //   utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el registro de la orden de gasto de Compras. Consulte con el Administrador");
       // }
    }
    String empleado = "";
    String cedula = "";
    String ide_ademple = "";

    private int tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioSistema(utilitario.getVariable("IDE_USUA"), "1", par_aprueba_gasto));

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

    public void aprobarAnulado() {
        System.out.println("valor del formulario " + tab_adquisiones.getValorSeleccionado());
        if (tab_adquisiones.getValorSeleccionado() != null) {
            con_guardar_anulado.setMessage("Esta Seguro de ANULAR la solicitud " + tab_adquisiones.getValor("NUMERO_ORDEN_ADCOMP"));
            con_guardar_anulado.setTitle("Confirmación de Anular");
            con_guardar_anulado.getBot_aceptar().setMetodo("guardarAnulado");
            con_guardar_anulado.dibujar();
            utilitario.addUpdate("con_guardar_anulado");
        } else {
            utilitario.agregarMensajeError("No existen registros", "No existen registros para su anulaciòn");
        }
    }

    public void guardarAnulado() {

//        tab_adquisiones.setValor("IDE_ADAPRO", par_anulado);
//        tab_adquisiones.setValor("ADQ_IDE_ADEMDE2", ide_ademple);
//
//        tab_adquisiones.modificar(tab_adquisiones.getFilaActual());
//
//        utilitario.addUpdate("tab_adquisiones");
//        tab_adquisiones.guardar();
//        guardarPantalla();
        ser_adquisiciones.setUpdateEstadoGastos(Integer.parseInt(tab_adquisiones.getValor("ide_adcomp") + ""), ide_ademple);
        filtroDireccion();
        con_guardar_anulado.cerrar();

    }

    public void aprobarSolicitud() {
        //System.out.println("valor del formulario "+tab_adquisiones.getValorSeleccionado());
        if (tab_adquisiones.getValorSeleccionado() != null) {
            con_guardar_aprobado.setMessage("Esta Seguro de Aprobar la compra de la solicitud " + tab_adquisiones.getValor("NUMERO_ORDEN_ADCOMP"));
            con_guardar_aprobado.setTitle("Confirmación de aprobar");
            con_guardar_aprobado.getBot_aceptar().setMetodo("guardarAprobacion");
            con_guardar_aprobado.dibujar();
            utilitario.addUpdate("con_guardar_aprobado");
        } else {
            utilitario.agregarMensajeError("No existen registros", "No existen registros pendientes de aprobaciòn");
        }
    }

    public void guardarAprobacion() {

//        tab_adquisiones.setValor("APRUEBA_GASTO_ADCOMP", "true");
//        tab_adquisiones.setValor("ADQ_IDE_ADEMDE", ide_ademple);
//
//        tab_adquisiones.modificar(tab_adquisiones.getFilaActual());
//
//        utilitario.addUpdate("tab_adquisiones");
//        tab_adquisiones.guardar();
//        guardarPantalla();
        ser_adquisiciones.setUpdateEstadoGastos(Integer.parseInt(tab_adquisiones.getValor("ide_adcomp") + ""), ide_ademple);
        filtroDireccion();
        con_guardar_aprobado.cerrar();
        utilitario.agregarMensaje("Registro Aprobado", "");
    }

    public void filtroDireccion() {
        System.err.println("->" + cedula);
        tab_adquisiones.setCondicion("ide_adarad in (select DISTINCT ide_adarad from adq_empleado_departamento\n"
                + "inner join ADQ_TIPO_APROBADOR on adq_empleado_departamento.ide_adtiap = ADQ_TIPO_APROBADOR.ide_adtiap\n"
                + "where  DETALLE_ADTIAP = 'APROBADOR DE GASTO'\n"
                + "and ide_ademple = ( select ide_ademple from ADQ_EMPLEADO where cedula_Ademple ='" + cedula + "'))\n"
                + "and APLICA_ADCOMP=1 AND ATIENDE_BODEGA_ADCOMP=1 AND APRUEBA_GASTO_ADCOMP=0");
        tab_adquisiones.ejecutarSql();
        tab_certificacion.ejecutarValorForanea(tab_adquisiones.getValorSeleccionado());
        tab_compra_bienes.ejecutarValorForanea(tab_adquisiones.getValorSeleccionado());

        utilitario.addUpdate("tab_adquisiones,tab_certificacion,tab_compra_bienes");

    }

    @Override
    public void insertar() {
        if (com_direccion.getValue() == null) {

            utilitario.agregarMensajeInfo("ADVERTENCIA", "Seleccione la Direccion Administrativa para generar la solicitud");
            return;
        } else if (tab_adquisiones.isFocus()) {
            tab_adquisiones.insertar();
            tab_adquisiones.setValor("IDE_ADEMDE", ide_ademple);
            tab_adquisiones.setValor("IDE_ADARAD", com_direccion.getValue().toString());
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

    @Override
    public void abrirListaReportes() {
        // TODO Auto-generated method stub
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Solicitu de Compra")) {
            rep_reporte.cerrar();
            map_parametros.clear();
            map_parametros.put("pide_requisicion", Integer.parseInt(tab_adquisiones.getValor("ide_adcomp")));

            map_parametros.put("p_usuario", utilitario.getVariable("NICK"));
            sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
            sel_rep.dibujar();
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

    public Confirmar getCon_guardar_aprobado() {
        return con_guardar_aprobado;
    }

    public void setCon_guardar_aprobado(Confirmar con_guardar_aprobado) {
        this.con_guardar_aprobado = con_guardar_aprobado;
    }

    public Confirmar getCon_guardar_anulado() {
        return con_guardar_anulado;
    }

    public void setCon_guardar_anulado(Confirmar con_guardar_anulado) {
        this.con_guardar_anulado = con_guardar_anulado;
    }

}

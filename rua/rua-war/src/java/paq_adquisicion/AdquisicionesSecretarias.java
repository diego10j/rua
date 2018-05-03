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
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;
import sistema.aplicacion.Utilitario;

public class AdquisicionesSecretarias extends Pantalla {

    private Tabla tab_adquisiones = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla tab_certificacion = new Tabla();
    private Tabla tab_compra_bienes = new Tabla();
    public static String par_tipo_secretaria = "";
    private Combo com_direccion = new Combo();
    public static String par_tipo_bodeguero = "";
    public static String par_aprueba_gasto = "";
    public static String par_aprueba_solicitud = "";
    private Reporte rep_reporte = new Reporte(); //Listado de Reportes, siempre se llama rep_reporte
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte(); //formato de salida del reporte
    private Map map_parametros = new HashMap();//Parametros del reporte    
    private SeleccionTabla sel_tabla_material = new SeleccionTabla();
    private SeleccionTabla sel_tabla_certificado = new SeleccionTabla();
    private Conexion conOracle = new Conexion();
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public AdquisicionesSecretarias() {
        par_tipo_secretaria = utilitario.getVariable("p_tipo_secretaria");
        par_tipo_bodeguero = utilitario.getVariable("p_tipo_bodeguero");
        par_aprueba_gasto = utilitario.getVariable("p_tipo_generador_gasto");
        par_aprueba_solicitud = utilitario.getVariable("p_tipo_aprueba_solicitud");

        /*
         * Permite tener acceso a información, de los datos de registro
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();
        
        /*
         * Cadena de conexión base de datos
         */
       // conOracle.setUnidad_persistencia(utilitario.getPropiedad("oraclejdbc"));
        //conOracle.NOMBRE_MARCA_BASE = "oracle";

        String fecha;

        if (String.valueOf((utilitario.getMes(utilitario.getFechaActual()))).length() > 1) {
            fecha = String.valueOf((utilitario.getMes(utilitario.getFechaActual())));
        } else {
            fecha = "0" + String.valueOf((utilitario.getMes(utilitario.getFechaActual())));
        }

       // sel_tabla_certificado.setId("sel_tabla_certificado");
        /*sel_tabla_certificado.getTab_seleccion().setConexion(conOracle);
        sel_tabla_certificado.setSeleccionTabla("select DISTINCT CONCAT(CONCAT(CONCAT(CONCAT(cedtmc,'-'),NDOCDC),'-'),AUAD01) as id, NDOCDC as Certificado,cedtmc as partida,AUAD02,MONTDT as Valor,AUAD01 as Proyecto\n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "inner join USFIMRU.PRCO01 on  CUENDT = CUENDC and AUAD02 = AUA2DC\n"
                + "inner join USFIMRU.TIGSA_GLM03 on CUENMC = CUENDT\n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=1" + (Integer.parseInt(utilitario.getFechaActual().toString().substring(2, 4)) - 1) + "14 \n"
                + "AND SAPRDT<=1" + (Integer.parseInt(utilitario.getFechaActual().toString().substring(2, 4)) - 1) + "15 \n"
                + "AND AUAD02 is not null \n"
                + "AND ANIODC =" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "AND TIPLMC= 'R'\n"
                + "AND substr(FDOCDT,1,5) <= 1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()))).substring(2, 4) + "" + fecha + "", "id");*/
      //  sel_tabla_certificado.getTab_seleccion().getColumna("Certificado").setFiltro(true);
       // sel_tabla_certificado.getTab_seleccion().getColumna("partida").setFiltro(true);
        sel_tabla_certificado.setRadio();
        //sel_tabla_certificado.getBot_aceptar().setMetodo("filtraDatos");
        sel_tabla_certificado.setHeader("CERTIFICACIONES Y PROYECTOS");
        agregarComponente(sel_tabla_certificado);

       // if (tienePerfilSecretaria()) {
            Boton bot_agregar_solicitante = new Boton();

            rep_reporte.setId("rep_reporte");
            agregarComponente(rep_reporte);
            bar_botones.agregarReporte();
            sel_rep.setId("sel_rep");
            agregarComponente(sel_rep);

            com_direccion.setId("com_direccion");
          //  com_direccion.setCombo(ser_adquisiciones.getAreaAdministrativa("1", ide_ademple));
            agregarComponente(com_direccion);
            bar_botones.agregarComponente(com_direccion);
          //  com_direccion.setMetodo("filtroDireccion");

            Tabulador tab_tabulador = new Tabulador();
            tab_tabulador.setId("tab_tabulador");

            tab_adquisiones.setId("tab_adquisiones");   //identificador
            tab_adquisiones.setTabla("adq_compra", "ide_adcomp", 1);
            tab_adquisiones.setCondicion("ide_adcomp=-1");
            tab_adquisiones.setCampoOrden("ide_adcomp desc");
            List lista = new ArrayList();
            List lista1 = new ArrayList();
            List lista2 = new ArrayList();
            Object fila1[] = {"1", "SI"};
            Object fila2[] = {"2", "NO"};
            Object fila5[] = {"1", "COMPRA EN STOCK"};
            //Object fila6[] = {"2", "COMPRA DE CONSUMO DIRECTO"};
            Object fila7[] = {"1", "BODEGA MUNICIPAL"};
            Object fila8[] = {"2", "ACTIVOS FIJOS"};
            lista.add(fila1);
            lista.add(fila2);
            lista2.add(fila5);
            //lista2.add(fila6);
            lista1.add(fila7);
            lista1.add(fila8);
            tab_adquisiones.getColumna("existe_adcomp").setRadio(lista, "1");
            tab_adquisiones.getColumna("tipo_compra_adcomp").setCombo(lista2);
            tab_adquisiones.getColumna("INGRESO_ADCOMP").setCombo(lista1);

            tab_adquisiones.getColumna("APRUEBA_ADCOMP").setRadio(lista, "1");
            tab_adquisiones.getColumna("IDE_ADAPRO").setCombo(ser_adquisiciones.getAprobado());
                tab_adquisiones.getColumna("IDE_ADEMAP").setCombo(ser_adquisiciones.getEmpleadoAprueba("2", ide_ademple, "1", ide_ademple));
            tab_adquisiones.getColumna("IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3", "1", "1", "1"));
            tab_adquisiones.getColumna("IDE_ADEMDE").setAutoCompletar();
            tab_adquisiones.getColumna("IDE_ADEMDE").setLectura(true);
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3", "1", "1", "1"));
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setCombo(ser_adquisiciones.getEmpleadoDepartamento("3", "1", "1", "1"));
            tab_adquisiones.getColumna("APRUEBA_DIRECTOR_ADCOMP").setValorDefecto("0");
            tab_adquisiones.getColumna("ATIENDE_BODEGA_ADCOMP").setValorDefecto("0");
            tab_adquisiones.getColumna("APRUEBA_GASTO_ADCOMP").setValorDefecto("0");
            tab_adquisiones.getColumna("REGISTRA_COMPRAS_ADCOMP").setValorDefecto("0");
            tab_adquisiones.getColumna("FECHA_SOLICITUD_ADCOMP").setValorDefecto(utilitario.getFechaActual());
            tab_adquisiones.agregarRelacion(tab_certificacion);
            tab_adquisiones.agregarRelacion(tab_compra_bienes);
            tab_adquisiones.setTipoFormulario(true);
            tab_adquisiones.getGrid().setColumns(6);
            tab_adquisiones.getColumna("NUMERO_ORDEN_ADCOMP").setLectura(true);
            tab_adquisiones.getColumna("IDE_ADEMAP").setNombreVisual("SOLICITANTE");
            tab_adquisiones.getColumna("IDE_ADEMDE").setNombreVisual("REGISTRO SOLICITUD");
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
            
            tab_adquisiones.getColumna("ide_adcomp").setVisible(false);
            tab_adquisiones.getColumna("IDE_ADAPRO").setVisible(false);
            tab_adquisiones.getColumna("IDE_ADAPRO").setRequerida(true);
            tab_adquisiones.getColumna("IDE_ADAPRO").setValorDefecto("1");
            
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
            tab_adquisiones.getColumna("APLICA_ADCOMP").setVisible(false);
            //tab_adquisiones.getColumna("IDE_ADEMAP").setVisible(false);
            tab_adquisiones.getColumna("IDE_ADARAD").setVisible(false);
            tab_adquisiones.getColumna("IDE_ADEMPLE").setVisible(false);
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE").setVisible(false);
            tab_adquisiones.getColumna("ADQ_IDE_ADEMDE2").setVisible(false);
            tab_adquisiones.getColumna("APRUEBA_DIRECTOR_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("ATIENDE_BODEGA_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("APRUEBA_GASTO_ADCOMP").setVisible(false);
            tab_adquisiones.getColumna("REGISTRA_COMPRAS_ADCOMP").setVisible(false);
            tab_adquisiones.dibujar();

            PanelTabla pat_adquisiciones = new PanelTabla();
            pat_adquisiciones.setId("pat_adquisiciones");
            pat_adquisiciones.setPanelTabla(tab_adquisiones);

          /*  tab_certificacion.setId("tab_certificacion");
            tab_certificacion.setIdCompleto("tab_tabulador:tab_certificacion");
            tab_certificacion.setTabla("ADQ_CERTIFICACION", "IDE_ADCERT", 2);
            List lista3 = new ArrayList();
            Object fila3[] = {"1", "CERTIFICACION"};
            //Object fila4[] = {"2", "COMPROMISO"};
            lista3.add(fila3);
            //lista3.add(fila4);
            tab_certificacion.getColumna("tipo_documento_adcert").setCombo(lista3);
            tab_certificacion.getColumna("tipo_documento_adcert").setMetodoChange("buscaCertificacion");
            tab_certificacion.getColumna("IDE_ADCERT").setNombreVisual("CODIGO");
            tab_certificacion.getColumna("TIPO_DOCUMENTO_ADCERT").setNombreVisual("TIPO DOCUMENTO");
            tab_certificacion.getColumna("NRO_CERTIFICACION_ADCERT").setNombreVisual("NUMERO CERTIFICACION");
            tab_certificacion.getColumna("PARTIDA_ADCERT").setNombreVisual("PARTIDA");
            tab_certificacion.dibujar();*/

            Grid gri_certificacion = new Grid();
            gri_certificacion.setColumns(3);

            Boton bot_insert_certificacion = new Boton();
            bot_insert_certificacion.setValue("INSERTAR");
            bot_insert_certificacion.setIcon("ui-icon-document");
            bot_insert_certificacion.setMetodo("insertCertificacion");

            Boton bot_save_certificacion = new Boton();
            bot_save_certificacion.setValue("GUARDAR");
            bot_save_certificacion.setIcon("ui-icon-disk");
            bot_save_certificacion.setMetodo("guardar");

            Boton bot_delete_certificacion = new Boton();
            bot_delete_certificacion.setValue("QUITAR");
            bot_delete_certificacion.setIcon("ui-icon-cancel");
            bot_delete_certificacion.setMetodo("eliminar");

            gri_certificacion.getChildren().add(bot_insert_certificacion);
            gri_certificacion.getChildren().add(bot_save_certificacion);
            gri_certificacion.getChildren().add(bot_delete_certificacion);

            /*PanelTabla pat_panel_certificacion = new PanelTabla();
            pat_panel_certificacion.setId("pat_panel_certificacion");
            pat_panel_certificacion.getChildren().add(gri_certificacion);
            pat_panel_certificacion.setPanelTabla(tab_certificacion);*/

            tab_compra_bienes.setId("tab_compra_bienes");
            tab_compra_bienes.setIdCompleto("tab_tabulador:tab_compra_bienes");
            tab_compra_bienes.setTabla("ADQ_COMPRA_BIENES", "IDE_ADCOBI", 3);
            tab_compra_bienes.getColumna("IDE_INARTI").setCombo(ser_adquisiciones.getMaterial("0", "0"));
            tab_compra_bienes.getColumna("IDE_INARTI").setAutoCompletar();
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

            tab_compra_bienes.getColumna("IDE_INARTI").setLectura(true);

            tab_compra_bienes.getColumna("IDE_ADCOBI").setOrden(1);
            tab_compra_bienes.getColumna("IDE_INARTI").setOrden(2);
            tab_compra_bienes.getColumna("CANTIDAD_ADCOBI").setOrden(3);

            tab_compra_bienes.getColumna("VALOR_UNITARIO_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("DECUENTO_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("PORCENTAJE_DESCUENTO_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("SUBTOTAL_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("IVA_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.getColumna("TOTAL_ADCOBI").setValorDefecto("0");
            tab_compra_bienes.dibujar();

            Grid gri_compra_bienes = new Grid();
            gri_compra_bienes.setColumns(3);
            Boton bot_insert_compra_bienes = new Boton();
            bot_insert_compra_bienes.setValue("INSERTAR");
            bot_insert_compra_bienes.setIcon("ui-icon-document");
            bot_insert_compra_bienes.setMetodo("insertCompraBienes");

            Boton bot_save_compra_bienes = new Boton();
            bot_save_compra_bienes.setValue("GUARDAR");
            bot_save_compra_bienes.setIcon("ui-icon-disk");
            bot_save_compra_bienes.setMetodo("guardar");

            Boton bot_delete_compra_bienes = new Boton();
            bot_delete_compra_bienes.setValue("QUITAR");
            bot_delete_compra_bienes.setIcon("ui-icon-cancel");
            bot_delete_compra_bienes.setMetodo("eliminar");

//            gri_compra_bienes.getChildren().add(bot_insert_compra_bienes);
            gri_compra_bienes.getChildren().add(bot_save_compra_bienes);
            gri_compra_bienes.getChildren().add(bot_delete_compra_bienes);

            PanelTabla pat_panel_compra_bienes = new PanelTabla();
            pat_panel_compra_bienes.setId("pat_panel_compra_bienes");
            pat_panel_compra_bienes.getChildren().add(gri_compra_bienes);
            pat_panel_compra_bienes.setPanelTabla(tab_compra_bienes);

          //  tab_tabulador.agregarTab("CERTIFICACION", pat_panel_certificacion);
           // tab_tabulador.agregarTab("COMPRA BIENES", pat_panel_compra_bienes);

            Division div_adquisiciones = new Division();
            div_adquisiciones.setId("div_adquisiciones");
            div_adquisiciones.dividir2(pat_adquisiciones, pat_panel_compra_bienes, "50%", "H");
            agregarComponente(div_adquisiciones);

            sel_tabla_material.setId("sel_tabla_material");
            sel_tabla_material.setTitle("MATERIAL DE BODEGA");
            sel_tabla_material.setSeleccionTabla(ser_adquisiciones.getMaterial("1", "-1"), "IDE_INARTI");
            sel_tabla_material.getTab_seleccion().getColumna("NOMBRE_INARTI").setFiltroContenido();
            sel_tabla_material.setWidth("80%");
            sel_tabla_material.setHeight("70%");
            sel_tabla_material.setRadio();
           

            Boton bot_sel_material = new Boton();
            bot_sel_material.setValue("SELECCIONAR MATERIAL");
           // bot_sel_material.setMetodo("seleccionarMaterial");
            bar_botones.agregarBoton(bot_sel_material);
            //bot_sel_material.setMetodo("seleccionarMaterial");

      //  } else {
        //    utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el registro de Solicitudes de Compra. Consulte con el Administrador");
        //}
    }
    String empleado = "";
    String cedula = "";
    String ide_ademple = "";

    public void buscaCertificacion() {
        sel_tabla_certificado.dibujar();
    }

    public void filtraDatos() {
        String fecha;

        if (String.valueOf((utilitario.getMes(utilitario.getFechaActual()))).length() > 1) {
            fecha = String.valueOf((utilitario.getMes(utilitario.getFechaActual())));
        } else {
            fecha = "0" + String.valueOf((utilitario.getMes(utilitario.getFechaActual())));
        }
        TablaGenerica tabDato = ser_adquisiciones.getMuestraDatos(sel_tabla_certificado.getValorSeleccionado()+"", utilitario.getAnio(utilitario.getFechaActual()), (Integer.parseInt(utilitario.getFechaActual().toString().substring(2, 4)) - 1), String.valueOf((utilitario.getAnio(utilitario.getFechaActual()))).substring(2, 4) + "" + fecha);
        if (!tabDato.isEmpty()) {
            System.err.println("certificado->> "+tabDato.getValor("NDOCDC"));
            tab_certificacion.setValor("NRO_CERTIFICACION_ADCERT", tabDato.getValor("NDOCDC")+"");
            tab_certificacion.setValor("PARTIDA_ADCERT", tabDato.getValor("AUAD02")+"."+tabDato.getValor("cedtmc")+"");
            tab_certificacion.setValor("VALOR_ADCERT", tabDato.getValor("MONTDT")+"");
             tab_certificacion.setValor("PARTIDA_NOM_ADCERT", tabDato.getValor("NOLAAD")+"");
            System.err.println("Valor->> "+tab_certificacion.getValor("VALOR_ADCERT"));
            utilitario.addUpdateTabla(tab_certificacion, "NRO_CERTIFICACION_ADCERT,PARTIDA_ADCERT,VALOR_ADCERT,PARTIDA_NOM_ADCERT", "");//actualiza solo componentes
//            
//            utilitario.addUpdate("tab_certificacion");
//            utilitario.addUpdateTabla(tab_certificacion, cedula, empleado);
        }else {
        System.err.println("No ai datos");
        }
        sel_tabla_certificado.cerrar();
    }

    private boolean tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioSistema(utilitario.getVariable("IDE_USUA"), "1", par_tipo_secretaria));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);

            empleado = fila[1].toString();
            cedula = fila[2].toString();
            ide_ademple = fila[0].toString();
            return true;

        } else {
            return false;
        }
    }

    public void filtroDireccion() {

        tab_adquisiones.setCondicion("IDE_ADARAD=" + com_direccion.getValue().toString() + " and IDE_ADEMDE=" + ide_ademple + " and APRUEBA_DIRECTOR_ADCOMP =0 ");
        tab_adquisiones.ejecutarSql();
        tab_certificacion.ejecutarValorForanea(tab_adquisiones.getValorSeleccionado());
        tab_compra_bienes.ejecutarValorForanea(tab_adquisiones.getValorSeleccionado());

        utilitario.addUpdate("tab_adquisiones,tab_certificacion,tab_compra_bienes");

    }

    public void seleccionarMaterial() {
        if (tab_adquisiones.getValor("INGRESO_ADCOMP") != null) {
            sel_tabla_material.getTab_seleccion().setSql(ser_adquisiciones.getMaterial("1", tab_adquisiones.getValor("INGRESO_ADCOMP")));
            sel_tabla_material.getTab_seleccion().ejecutarSql();
            sel_tabla_material.dibujar();
        } else {
            utilitario.agregarMensajeError("Seleccione el material", "Seleccione el tipo de ingreso");
        }
    }

    public void aceptarMaterial() {
        tab_compra_bienes.insertar();
        tab_compra_bienes.setValor("IDE_ADMATE", sel_tabla_material.getValorSeleccionado());
        sel_tabla_material.cerrar();
        utilitario.addUpdate("tab_compra_bienes");
    }

    public void insertCertificacion() {
        tab_certificacion.insertar();
    }

    public void insertCompraBienes() {
        tab_compra_bienes.insertar();
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
//            tab_adquisiones.actualizarCombosFormulario();

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
        System.out.println("imprimo "+tab_adquisiones.getValorSeleccionado());
        System.out.println("imprimoss "+tab_adquisiones.getValor("NUMERO_ORDEN_ADCOMP"));
        //System.out.println("imprimodd "+tab_adquisiones.getValor("IDE_ADCOMP"));
        if(tab_adquisiones.getValor("NUMERO_ORDEN_ADCOMP") == null){
                    System.out.println(" xx  ");

        utilitario.getConexion().ejecutarSql("update ADQ_COMPRA\n"
                + "set NUMERO_ORDEN_ADCOMP=numero\n"
                + "from (\n"
                + "select (case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1 as maximo,\n"
                + "len((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1 )as longitu,\n"
                + "(case when len((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1 ) =1 then concat('00000',((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1)) \n"
                + "when len((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1 ) =2 then concat('0000',((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1)) \n"
                + "when len((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1 ) =3 then concat('000',((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1)) \n"
                + "when len((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1 ) =4 then concat('00',((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1)) \n"
                + "when len((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1 ) =5 then concat('0',((case when max(NUMERO_ORDEN_ADCOMP) is null then 0 else max(NUMERO_ORDEN_ADCOMP) end) +1)) \n"
                + "end) as numero\n"
                + "from ADQ_COMPRA\n"
                + ") a where ADQ_COMPRA.IDE_ADCOMP =" + tab_adquisiones.getValorSeleccionado());
        System.out.println(" vv  ");
        tab_adquisiones.ejecutarSql();
        }
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

            map_parametros.put("p_usuario", tabConsulta.getValor("NICK_USUA") + "");
            sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
            System.err.println("parametros ->> " +map_parametros);
            System.err.println("reporte ->>" +rep_reporte.getPath());
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

    public SeleccionTabla getSel_tabla_material() {
        return sel_tabla_material;
    }

    public void setSel_tabla_material(SeleccionTabla sel_tabla_material) {
        this.sel_tabla_material = sel_tabla_material;
    }

    public SeleccionTabla getSel_tabla_certificado() {
        return sel_tabla_certificado;
    }

    public void setSel_tabla_certificado(SeleccionTabla sel_tabla_certificado) {
        this.sel_tabla_certificado = sel_tabla_certificado;
    }

    public Conexion getConOracle() {
        return conOracle;
    }

    public void setConOracle(Conexion conOracle) {
        this.conOracle = conOracle;
    }
}

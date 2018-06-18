/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.service.ComprobanteService;
import dj.comprobantes.offline.service.ComprobanteServiceImp;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.html.HtmlPanelGroup;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.component.panel.Panel;
import org.primefaces.event.FileUploadEvent;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_genera_fac_lote extends Pantalla {

    private Upload upl_importa = new Upload();
    private Texto tex_columna = new Texto();
    private Texto tex_inicio = new Texto();
    private Texto tex_numero_hoja = new Texto();
    private Texto tex_fin = new Texto();
    private Tabla tab_detalle = new Tabla();
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioComprobanteElectronico ser_comprobante_electronico = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);
    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);
    @EJB
    private final ComprobanteService comprobanteService = (ComprobanteService) utilitario.instanciarEJBCEO(ComprobanteServiceImp.class); 

    public pre_genera_fac_lote() {
        bar_botones.limpiar();

        Panel pan = new Panel();
        pan.setStyle("width:100%;");

        Grid gri_archivo = new Grid();
        gri_archivo.setWidth("95%");
        gri_archivo.setColumns(3);

        Grid gri_matriz = new Grid();
        gri_matriz.setMensajeInfo("Seleccione un archivo Excel con extensión <strong>.xls<strong>");
        gri_matriz.setStyle("width:100%;");
        gri_matriz.setColumns(4);

        gri_matriz.getChildren().add(new Etiqueta("Número o Nombre de la Hoja"));
        tex_numero_hoja.setValue("1");
        gri_matriz.getChildren().add(tex_numero_hoja);
        gri_matriz.getChildren().add(new Etiqueta("Número de Fila con los Nombres de las Columnas"));
        tex_columna.setSize(5);
        tex_columna.setValue("1");
        tex_columna.setSoloEnteros();
        gri_matriz.getChildren().add(tex_columna);

        gri_matriz.getChildren().add(new Etiqueta("Número de la Primera Fila de Datos "));
        tex_inicio.setSize(5);
        tex_inicio.setValue("2");
        tex_inicio.setSoloEnteros();
        gri_matriz.getChildren().add(tex_inicio);
        gri_matriz.getChildren().add(new Etiqueta("Número de la Última Fila de Datos"));
        tex_fin.setId("tex_fin");
        tex_fin.setSize(5);
        tex_fin.setSoloEnteros();
        gri_matriz.getChildren().add(tex_fin);

        gri_archivo.getChildren().add(gri_matriz);

        Grid gri_valida = new Grid();
        gri_valida.setId("gri_valida");
        gri_valida.setColumns(3);
        Etiqueta eti_valida = new Etiqueta();
        eti_valida.setValueExpression("value", "pre_index.clase.upl_importa.nombreReal");
        eti_valida.setValueExpression("rendered", "pre_index.clase.upl_importa.nombreReal != null");
        gri_valida.getChildren().add(eti_valida);

        Imagen ima_valida = new Imagen();
        ima_valida.setWidth("22");
        ima_valida.setHeight("22");
        ima_valida.setValue("/imagenes/im_excel.gif");
        ima_valida.setValueExpression("rendered", "pre_index.clase.upl_importa.nombreReal != null");
        gri_valida.getChildren().add(ima_valida);
        gri_archivo.setFooter(gri_valida);

        upl_importa.setId("upl_importa");
        upl_importa.setUpdate("gri_valida");
        upl_importa.setAllowTypes("/(\\.|\\/)(xls)$/");
        upl_importa.setMetodo("seleccionarArchivo");
        upl_importa.setProcess("@all");
        upl_importa.setUploadLabel("Validar Archivo .xls");
        upl_importa.setAuto(false);

        gri_matriz.setFooter(upl_importa);
        pan.setHeader("Configurar Archivo");

        pan.getChildren().add(gri_archivo);

        tab_detalle.setId("tab_detalle");
        tab_detalle.setTabla("pen_tmp_lista_fact", "ide_petlf", 1);
        tab_detalle.getColumna("ide_petlf").setVisible(false);
        tab_detalle.setRows(20);
        tab_detalle.setLectura(true);
        tab_detalle.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_detalle);

        HtmlPanelGroup gri = new HtmlPanelGroup();

        gri.getChildren().add(pan);
        gri.getChildren().add(tab_detalle);

        Boton bot_subir = new Boton();
        bot_subir.setValue("Generar Facturas");
        bot_subir.setMetodo("generarFacturas");
        bot_subir.setIcon("ui-icon-check");
        bar_botones.agregarBoton(bot_subir);

        bar_botones.agregarSeparador();
        Boton bot_enviar = new Boton();
        bot_enviar.setValue("Enviar al SRI");
        bot_enviar.setMetodo("enviarSRI");
        bot_enviar.setIcon("ui-icon-signal-diag");
        bar_botones.agregarBoton(bot_enviar);
        
        bar_botones.agregarSeparador();
        Boton bot_genera = new Boton();
        bot_genera.setValue("Generar Claves de Acceso");
        bot_genera.setMetodo("generarClaveAcceso");
        bot_genera.setIcon("ui-icon-refresh");
        bar_botones.agregarBoton(bot_genera);
        
        

        Division div = new Division();
        div.dividir1(gri);
        agregarComponente(div);

    }

    public void enviarSRI() {
        try {
            comprobanteService.enviarRecepcionSRI();
            comprobanteService.enviarAutorizacionSRI();
        } catch (Exception e) {
            utilitario.crearError("Error al enviar al SRI", "enviarSRI()", e);
        }
    }

    public void generarFacturas() {
        if (tab_detalle.isEmpty() == false) {
            TablaGenerica tab_clientes = new TablaGenerica();
            tab_clientes.setTabla("gen_persona", "ide_geper");
            tab_clientes.setCondicion("identificac_geper in (" + tab_detalle.getStringColumna("cedula_petlf") + ",'9999999999')");
            tab_clientes.ejecutarSql();
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                //1 inserta Clientes
                String cedula_petlf = tab_detalle.getValor(i, "cedula_petlf");
                if (cedula_petlf == null || cedula_petlf.isEmpty()) {
                    cedula_petlf = "9999999999";
                    tab_detalle.setValor(i, "cedula_petlf", cedula_petlf);
                    tab_detalle.setValor(i, "representante_petlf", "CONSUMIDOR FINAL");
                    tab_detalle.setValor(i, "direccion_petlf", "S/D");
                    tab_detalle.setValor(i, "correo_petlf", "facturas_pensiones@ctsdonbosco.edu.ec");
                    tab_detalle.setValor(i, "telefono_petlf", "9999999");
                }
                boolean existe_cliente = false;
                for (int j = 0; j < tab_clientes.getTotalFilas(); j++) {
                    if (cedula_petlf.equals(tab_clientes.getValor(j, "identificac_geper"))) {
                        existe_cliente = true;
                        break;
                    }
                }
                if (existe_cliente == false) {
                    tab_clientes.insertar();
                    tab_clientes.setValor("identificac_geper", tab_detalle.getValor(i, "cedula_petlf"));
                    tab_clientes.setValor("nombre_compl_geper", tab_detalle.getValor(i, "representante_petlf"));
                    tab_clientes.setValor("nom_geper", tab_detalle.getValor(i, "representante_petlf"));
                    tab_clientes.setValor("direccion_geper", tab_detalle.getValor(i, "direccion_petlf"));
                    tab_clientes.setValor("telefono_geper", tab_detalle.getValor(i, "telefono_petlf"));
                    tab_clientes.setValor("correo_geper", tab_detalle.getValor(i, "correo_petlf"));
                    tab_clientes.setValor("es_cliente_geper", "true");
                    tab_clientes.setValor("nivel_geper", "HIJO");
                    tab_clientes.setValor("fecha_ingre_geper", utilitario.getFechaActual());
                    tab_clientes.setValor("gen_ide_geper", "4");//4 = REPRESENTANTES
                    tab_clientes.setValor("ide_vgtcl", "0"); // REPRESENTANTES                    
                    if (tab_detalle.getValor(i, "cedula_petlf").length() == 10) {
                        tab_clientes.setValor("ide_getid", "0"); // CEDULA        
                        if (tab_detalle.getValor(i, "cedula_petlf").equals("9999999999")) {
                            tab_clientes.setValor("ide_getid", "3"); // CONSUMIDOR FINAL 
                        }
                    } else if (tab_detalle.getValor(i, "cedula_petlf").length() == 10) {
                        tab_clientes.setValor("ide_getid", "1"); // RUC        
                    } else {
                        tab_clientes.setValor("ide_getid", "2"); // PASAPORTE
                    }
                    tab_clientes.setValor("ide_cntco", "2"); // PERSONA NATURAL
                    tab_clientes.setValor("ide_vgecl", "0"); // ACTIVO
                    tab_clientes.setValor("ide_cndfp", "13"); // OTROS SIN UTILIZAR EL SISTEMA FINANCIERO
                }
            }
            if (tab_clientes.guardar()) {
                utilitario.getConexion().ejecutarListaSql();
            }

            tab_clientes.setCondicion("identificac_geper in (" + tab_detalle.getStringColumna("cedula_petlf") + ",'9999999999')");
            tab_clientes.ejecutarSql();

            //inserta facturas
            Tabla tab_cab_fac = new Tabla();
            tab_cab_fac.setTabla("cxc_cabece_factura", "ide_cccfa", 999);
            tab_cab_fac.setCondicion("ide_cccfa=-1");
            tab_cab_fac.setGenerarPrimaria(false);
            tab_cab_fac.getColumna("ide_cccfa").setExterna(false);
            tab_cab_fac.ejecutarSql();

            Tabla tab_det_fac = new Tabla();
            tab_det_fac.setTabla("cxc_deta_factura", "ide_ccdfa", 999);
            tab_det_fac.setCondicion("ide_ccdfa=-1");
            tab_det_fac.ejecutarSql();

            Tabla tab_info_adicional = new Tabla();
            tab_info_adicional.setTabla("sri_info_adicional", "ide_srina", 999);
            tab_info_adicional.setCondicion("ide_srina=-1");
            tab_info_adicional.ejecutarSql();

            long ide_cccfa = utilitario.getConexion().getMaximo("cxc_cabece_factura", "ide_cccfa", tab_detalle.getTotalFilas());
            //Recupera porcentaje iva 
            double tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                String cedula_petlf = tab_detalle.getValor(i, "cedula_petlf");
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
                tab_cab_fac.setValor("fecha_emisi_cccfa", tab_detalle.getValor(i, "fecha_petlf"));
                tab_cab_fac.setValor("dias_credito_cccfa", "0");
                tab_cab_fac.setValor("ide_cndfp", "13");//13=OTROS SIN UTILIZAR SITEMA FINANCIERO
                tab_cab_fac.setValor("ide_cndfp1", "3");//3=EFECTIVO
                tab_cab_fac.setValor("DIRECCION_CCCFA", tab_detalle.getValor(i, "direccion_petlf"));
                tab_cab_fac.setValor("base_grabada_cccfa", "0");
                tab_cab_fac.setValor("base_no_objeto_iva_cccfa", "0");
                tab_cab_fac.setValor("valor_iva_cccfa", "0");
                tab_cab_fac.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(tab_detalle.getValor(i, "subtotal_petlf")));
                tab_cab_fac.setValor("descuento_cccfa", utilitario.getFormatoNumero(tab_detalle.getValor(i, "rebaja_petlf")));
                tab_cab_fac.setValor("orden_compra_cccfa", tab_detalle.getValor(i, "cod_factura_petlf"));
                tab_cab_fac.setValor("total_cccfa", utilitario.getFormatoNumero(tab_detalle.getValor(i, "total_petlf")));
                tab_cab_fac.setValor("correo_cccfa", tab_detalle.getValor(i, "correo_petlf"));
                tab_cab_fac.setValor("pagado_cccfa", "false");
                tab_cab_fac.setValor("ide_geper", ide_geper);
                tab_cab_fac.setValor("ide_ccdaf", "2"); //2= FACTURAS ELECTRONICAS
                tab_cab_fac.setValor("telefono_cccfa", tab_detalle.getValor(i, "telefono_petlf"));
                tab_cab_fac.setValor("tarifa_iva_cccfa", utilitario.getFormatoNumero((tarifaIVA * 100)));

                tab_det_fac.insertar();
                tab_det_fac.setValor("ide_inarti", "57"); //55 == servicios colegio
                tab_det_fac.setValor("CANTIDAD_CCDFA", "1");
                tab_det_fac.setValor("PRECIO_CCDFA", utilitario.getFormatoNumero(tab_detalle.getValor(i, "subtotal_petlf")));
                tab_det_fac.setValor("iva_inarti_ccdfa", "-1");
                tab_det_fac.setValor("total_ccdfa", utilitario.getFormatoNumero(tab_detalle.getValor(i, "subtotal_petlf")));
                tab_det_fac.setValor("OBSERVACION_CCDFA", tab_detalle.getValor(i, "concepto_petlf"));
                tab_det_fac.setValor("ALTERNO_CCDFA", "00");
                tab_det_fac.setValor("ide_cccfa", String.valueOf(ide_cccfa));

                //info adicional
                tab_info_adicional.insertar();
                tab_info_adicional.setValor("nombre_srina", "Código Alumno");
                tab_info_adicional.setValor("valor_srina", tab_detalle.getValor(i, "codigo_alumno_petlf"));
                tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
                tab_info_adicional.insertar();
                tab_info_adicional.setValor("nombre_srina", "Nombre Alumno");
                tab_info_adicional.setValor("valor_srina", tab_detalle.getValor(i, "nombre_alumno_petlf"));
                tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
                tab_info_adicional.insertar();
                tab_info_adicional.setValor("nombre_srina", "Paralelo");
                tab_info_adicional.setValor("valor_srina", tab_detalle.getValor(i, "paralelo_petlf"));
                tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));                
                tab_info_adicional.insertar();
                tab_info_adicional.setValor("nombre_srina", "Período Lectivo");
                tab_info_adicional.setValor("valor_srina", tab_detalle.getValor(i, "periodo_lectivo_petlf"));
                tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
                
                

                ide_cccfa++;
            }
            if (tab_cab_fac.guardar()) {
                if (tab_det_fac.guardar()) {
                    if (tab_info_adicional.guardar()) {
                        //Guarda la cuenta por cobrar
                        // ser_factura.generarTransaccionFactura(tab_cab_factura);  ///Cambiar uno a uno
                        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                            for (int i = 0; i < tab_cab_fac.getTotalFilas(); i++) {
                                ser_comprobante_electronico.generarFacturaElectronica(tab_cab_fac.getValor(i, "ide_cccfa"));
                            }
                            utilitario.getConexion().ejecutarSql("UPDATE sri_info_adicional a set ide_srcom = (select ide_srcom from cxc_cabece_factura where ide_cccfa=a.ide_cccfa) where ide_srcom IS NOT  null");
                            utilitario.agregarMensaje("Se guardo Correctamente", "");
                        }
                    }
                }
            }
        } else {
            utilitario.agregarMensajeInfo("No se existen facturas importadas", "");
        }
    }

    public void seleccionarArchivo(FileUploadEvent event) {
        try {
            Workbook archivoExcel = Workbook.getWorkbook(event.getFile().getInputstream());
            Sheet hoja = null;
            int int_numero_hoja = -1;
            int int_inicio = -1;
            int int_fin = -1;
            int int_numColumnas = -1;
            List lis_campos_excel = new ArrayList();
//Abro la hoja indicada            
            try {
                int_numero_hoja = Integer.parseInt(tex_numero_hoja.getValue() + "");
            } catch (Exception e) {
                int_numero_hoja = -1;
            }

            if (int_numero_hoja == -1) {
                String[] hojas = archivoExcel.getSheetNames();
                for (int i = 0; i < hojas.length; i++) {
                    if (hojas[i].equalsIgnoreCase(tex_numero_hoja.getValue() + "")) {
                        hoja = archivoExcel.getSheet(i);
                        break;
                    }
                }
            } else {
                hoja = archivoExcel.getSheet(int_numero_hoja - 1);
            }

            if (hoja == null) {
                utilitario.agregarMensajeError("La Hoja: " + tex_numero_hoja.getValue() + " no existe en el Archivo seleccionado", "");
                return;
            }

            int_numColumnas = hoja.getColumns();

            try {
                int_inicio = Integer.parseInt(tex_inicio.getValue() + "");
                int_inicio = int_inicio - 1;
            } catch (Exception e) {
                int_inicio = -1;
            }

            try {
                int_fin = Integer.parseInt(tex_fin.getValue() + "");
            } catch (Exception e) {
                int_fin = -1;
            }

            if (tex_fin.getValue() == null || tex_fin.getValue().toString().isEmpty()) {
                //Si no hay valor en fin le pongo a el total de filas de la Hoja
                int_fin = hoja.getRows();
                tex_fin.setValue(String.valueOf(int_fin));
                utilitario.addUpdate("tex_fin");
            }

            if ((int_inicio == -1 || int_fin == -1) || (int_inicio > int_fin)) {
                utilitario.agregarMensajeError("Los valores ingresados en Primera o Última fila de datos no son válidos", "");
                return;
            }

            int int_fila_columna = -1;

            try {
                int_fila_columna = Integer.parseInt(tex_columna.getValue() + "");
                int_fila_columna = int_fila_columna - 1;
            } catch (Exception e) {
                int_fila_columna = -1;
            }

            if (int_fila_columna == -1) {
                utilitario.agregarMensajeError("El Número de Fila con los Nombres de las Columnas no es válido", "");
                return;
            }

            //Recupera titulos de las columnas
            for (int i = 0; i < int_numColumnas; i++) {
                try {
                    Object[] obj = new Object[2];
                    obj[0] = i;
                    obj[1] = hoja.getCell(i, int_fila_columna).getContents();
                    lis_campos_excel.add(obj);
                } catch (Exception e) {
                    System.out.println("Fallo al leer columnas del archivo xls :" + e.getMessage());
                }
            }
            //Recorre filas e inserta en la tabla recorre de manera inversa el archivo
            tab_detalle.setLectura(false);
            for (int fila = (int_fin - 1); fila >= int_inicio; fila--) {
                String codigo_alumno = hoja.getCell(0, fila).getContents();
                String nombre_alumno = hoja.getCell(1, fila).getContents();
                String paralelo = hoja.getCell(2, fila).getContents();
                String subtotal = hoja.getCell(3, fila).getContents();
                String rebaja = hoja.getCell(4, fila).getContents();
                String total = hoja.getCell(5, fila).getContents();
                String cod_factura = hoja.getCell(6, fila).getContents();
                String fecha = hoja.getCell(7, fila).getContents();  //dd/mm/yyyy
                String concepto = hoja.getCell(8, fila).getContents();
                String representante = hoja.getCell(9, fila).getContents();
                String cedula = hoja.getCell(10, fila).getContents();
                String periodo_lectivo = hoja.getCell(11, fila).getContents();
                String correo = hoja.getCell(12, fila).getContents();
                String direccion = hoja.getCell(13, fila).getContents();
                String telefono = hoja.getCell(14, fila).getContents();

                tab_detalle.insertar();
                tab_detalle.setValor("codigo_alumno_petlf", codigo_alumno);
                tab_detalle.setValor("nombre_alumno_petlf", nombre_alumno);
                tab_detalle.setValor("paralelo_petlf", paralelo);
                if (utilitario.getFormatoNumero(subtotal.replace(",", ".")) != null) {
                    if (!utilitario.getFormatoNumero(subtotal.replace(",", ".")).equals("0.00")) {
                        tab_detalle.setValor("subtotal_petlf", utilitario.getFormatoNumero(subtotal.replace(",", ".")));
                    }
                }
                if (utilitario.getFormatoNumero(rebaja.replace(",", ".")) != null) {
                    if (!utilitario.getFormatoNumero(rebaja.replace(",", ".")).equals("0.00")) {
                        tab_detalle.setValor("rebaja_petlf", utilitario.getFormatoNumero(rebaja.replace(",", ".")));
                    }
                }
                if (utilitario.getFormatoNumero(total.replace(",", ".")) != null) {
                    if (!utilitario.getFormatoNumero(total.replace(",", ".")).equals("0.00")) {
                        tab_detalle.setValor("total_petlf", utilitario.getFormatoNumero(total.replace(",", ".")));
                    }
                }
                tab_detalle.setValor("cod_factura_petlf", cod_factura);
                tab_detalle.setValor("fecha_petlf", utilitario.getFormatoFecha(utilitario.toDate(fecha, "dd/MM/YYYY")));
                tab_detalle.setValor("concepto_petlf", concepto);
                tab_detalle.setValor("representante_petlf", representante);
                tab_detalle.setValor("cedula_petlf", cedula);
                tab_detalle.setValor("periodo_lectivo_petlf", periodo_lectivo);
                tab_detalle.setValor("correo_petlf", correo);
                tab_detalle.setValor("direccion_petlf", direccion);
                tab_detalle.setValor("telefono_petlf", telefono);
            }
            tab_detalle.setLectura(true);

            //tab_detalle.sumarColumnas();
            upl_importa.setNombreReal(event.getFile().getFileName());
            utilitario.addUpdate("gri_valida,tab_detalle");
        } catch (Exception e) {
            e.printStackTrace();
            utilitario.agregarMensajeError("No se puede abrir el archivo", e.getMessage());
        }
    }

    public void generarClaveAcceso() {
        try {
            TablaGenerica tab = utilitario.consultar("select ide_srcom,claveacceso_srcom from sri_comprobante where ide_sresc=5");
            for (int i = 0; i < tab.getTotalFilas(); i++) {
                String ide_srcom = tab.getValor(i, "ide_srcom");
                Comprobante comprobanteFactura = comprobanteService.getComprobantePorId(Long.parseLong(ide_srcom));
                String strClaveAcceso = comprobanteService.getClaveAcceso(comprobanteFactura);
                utilitario.getConexion().agregarSqlPantalla("UPDATE sri_comprobante SET claveacceso_srcom='" + strClaveAcceso + "' where ide_srcom=" + ide_srcom);
            }
            guardarPantalla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {

    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public Upload getUpl_importa() {
        return upl_importa;
    }

    public void setUpl_importa(Upload upl_importa) {
        this.upl_importa = upl_importa;
    }
}

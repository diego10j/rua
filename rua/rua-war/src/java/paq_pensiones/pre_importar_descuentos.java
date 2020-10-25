/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
import framework.componentes.VisualizarPDF;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import paq_general.ejb.ServicioGeneral;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Jhon
 */
public class pre_importar_descuentos extends Pantalla {


    private Combo com_periodo = new Combo();
    private Combo com_ruta = new Combo();
    private Dialogo dia_importar = new Dialogo();
    private Editor edi_mensajes = new Editor();
    private Upload upl_archivo = new Upload();
    private VisualizarPDF vipdf_imp_lectura = new VisualizarPDF();
    private String ide_lectura = "";
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);


    public pre_importar_descuentos() {

            bar_botones.getBot_eliminar().setRendered(false);
            bar_botones.getBot_insertar().setRendered(false);
            bar_botones.getBot_guardar().setRendered(false);
            bar_botones.quitarBotonsNavegacion();

            com_periodo.setId("com_periodo");
            com_periodo.setCombo(ser_pensiones.getPeriodoAcademico("true"));
            bar_botones.agregarComponente(new Etiqueta("PERIODO ACADEMICO:"));
            bar_botones.agregarComponente(com_periodo);
            bar_botones.setStyle("font-size:15px;font-weight: bold");


            Boton bot_subir = new Boton();
            bot_subir.setIcon(" ui-icon-arrowthickstop-1-n");
            bot_subir.setTitle("SUBIR ARCHIVO");
            bot_subir.setValue("SUBIR ARCHIVO");
            bot_subir.setMetodo("subrirArchivo");
            bar_botones.agregarBoton(bot_subir);

            Boton bot_anular_descuento = new Boton();
            bot_anular_descuento.setIcon(" ui-icon-arrowthickstop-1-n");
            bot_anular_descuento.setTitle("APLICAR CERO DESCUENTOS");
            bot_anular_descuento.setValue("APLICAR CERO DESCUENTOS");
            bot_anular_descuento.setMetodo("ceroDescuento");
            bar_botones.agregarBoton(bot_anular_descuento);

            //DIALGO SUBIR ARCHIVO
            Grid gri_cuerpo_archivo = new Grid();

            Grid gri_impo = new Grid();
            gri_impo.setColumns(1);

            gri_impo.getChildren().add(new Etiqueta("Seleccione el archivo: "));
            upl_archivo.setId("upl_archivo");
            upl_archivo.setMetodo("validarArchivo");
            upl_archivo.setUpdate("gri_valida");
            upl_archivo.setAuto(false);
            upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
            upl_archivo.setUploadLabel("Validar");
            upl_archivo.setCancelLabel("Cancelar Seleccion");

            gri_impo.getChildren().add(upl_archivo);
            gri_impo.setWidth("100%");

            Grid gri_valida = new Grid();
            gri_valida.setId("gri_valida");
            gri_valida.setColumns(3);

            Etiqueta eti_valida = new Etiqueta();
            eti_valida.setValueExpression("value", "pre_index.clase.upl_archivo.nombreReal");
            eti_valida.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
            gri_valida.getChildren().add(eti_valida);

            Imagen ima_valida = new Imagen();
            ima_valida.setWidth("22");
            ima_valida.setHeight("22");
            ima_valida.setValue("/imagenes/im_excel.gif");
            ima_valida.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
            gri_valida.getChildren().add(ima_valida);

            edi_mensajes.setControls("");
            edi_mensajes.setId("edi_mensajes");
            edi_mensajes.setStyle("overflow:auto;");
            edi_mensajes.setWidth(dia_importar.getAnchoPanel() - 15);
            edi_mensajes.setDisabled(true);
            gri_valida.setFooter(edi_mensajes);

            gri_cuerpo_archivo.setStyle("width:" + (dia_importar.getAnchoPanel() - 5) + "px;");
            gri_cuerpo_archivo.setMensajeInfo("Esta opción  permite subir descuentos de pensiones anticipadas desde un archivo xls");
            gri_cuerpo_archivo.getChildren().add(gri_impo);
            gri_cuerpo_archivo.getChildren().add(gri_valida);
            gri_cuerpo_archivo.getChildren().add(edi_mensajes);
            gri_cuerpo_archivo.getChildren().add(new Espacio("0", "10"));

            dia_importar.setDialogo(gri_cuerpo_archivo);
            dia_importar.getBot_aceptar().setRendered(false);
            dia_importar.setDynamic(false);

            agregarComponente(dia_importar);


    }

    public void ceroDescuento(){
        if (com_periodo.getValue() != null) {
            utilitario.getConexion().ejecutarSql("update rec_alumno_periodo set descuento_recalp=false,valor_descuento_recalp=0 where ide_repea="+com_periodo.getValue());
            edi_mensajes.setValue(null);
            utilitario.agregarMensaje("EJECUTADO CORRECTAMENTE,", "LA ACTUALIZACIÓN DE CERO DESCUENTOS SE APLICO CON EXITO");

        } else {
            utilitario.agregarNotificacionInfo("ADVERTENCIA,", "SELECCIONE EL PERIODO ACADEMICO PARA REGISTRAR EL DESCUENTO");
        }

    }
    public void imprimirLectura() {
        System.out.println("INGRESE AL METODO IMPRIIR " + ide_lectura);
        Map map_parametros = new HashMap();
        map_parametros.put("ide_saplec", Integer.parseInt(ide_lectura));
        vipdf_imp_lectura.setVisualizarPDF("rep_agua_potable/rep_recibo_lectura.jasper", map_parametros);
        vipdf_imp_lectura.dibujar();
        utilitario.addUpdate("vipdf_imp_lectura");
    }

   
String str_resultado = "1";
    public void validarArchivo(FileUploadEvent evt) {
        //Leer el archivo
        String str_msg_info = "";
        String str_msg_adve = "";
        String str_msg_erro = "";
        String codigo_yaldap = "";
        String codigo_discapacidad = "";
        String str_resul2 = "";
        //System.out.println(" valor de resultado " + str_resultado);
        if (validarArchivoMensa(evt).equals("1")) {
           // System.out.println(" entre if_valida " + str_resultado);
            try {
                Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
                Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA

                if (hoja == null) {
                    utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
                    return;
                }

                int int_fin = hoja.getRows();
                upl_archivo.setNombreReal(evt.getFile().getFileName());
                str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas que se subieron con exito, puede verificar en la pantalla Alumnos Cursos");
                utilitario.getConexion().ejecutarSql("update rec_alumno_periodo set descuento_recalp=false,valor_descuento_recalp=0 where ide_repea="+com_periodo.getValue());
                //System.out.println(" valor int_fin " + int_fin);
                for (int i = 0; i < int_fin; i++) {

                String cedula_alumno = hoja.getCell(0, i).getContents();
                cedula_alumno = cedula_alumno.trim();

                String valor_descuento = hoja.getCell(1, i).getContents();
                valor_descuento = valor_descuento.trim();

                utilitario.getConexion().ejecutarSql(" update rec_alumno_periodo set descuento_recalp=true,valor_descuento_recalp="+valor_descuento  +
                                        " from (" +
                                        " select ide_recalp,ide_geper as contador  from rec_alumno_periodo where ide_repea ="+com_periodo.getValue()+" and ide_geper in (select ide_geper from gen_persona where identificac_geper='"+cedula_alumno+"') \n" +
                                        " ) a " +
                                        " where a.ide_recalp=rec_alumno_periodo.ide_recalp");
                
                }

                archivoExcel.close();
            } catch (Exception e) {
                // TODO: handle exception
            }

            if (!str_msg_info.isEmpty()) {
                str_resultado = "<strong><font color='#3333ff'>INFORMACIÓN</font></strong>" + str_msg_info;
            }
            //inicio insertando y calculando en la tabla


                   edi_mensajes.setValue(str_resultado);
            utilitario.addUpdate("edi_mensajes");

        } else {
            edi_mensajes.setValue(str_resultado);

            utilitario.addUpdate("edi_mensajes");
        }
    }

    public String validarArchivoMensa(FileUploadEvent evt) {
        //Leer el archivo
        str_resultado = "1";
        String str_msg_info = "";
        String str_msg_adve = "";
        String str_msg_erro = "";
        try {
            Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
            Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA

            int int_fin = hoja.getRows();
            upl_archivo.setNombreReal(evt.getFile().getFileName());
            double dou_tot_debe=0;
            double dou_tot_haber=0;
            for (int i = 0; i < int_fin; i++) {

                 String cedula_alumno = hoja.getCell(0, i).getContents();
                cedula_alumno = cedula_alumno.trim();

                String valor_descuento = hoja.getCell(1, i).getContents();
                valor_descuento = valor_descuento.trim();

               
                if (cedula_alumno == null || cedula_alumno.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("En el archivo excel el campo cedula se encuentra vació en la fila: " + (i + 1));
                } else {
                    TablaGenerica tab_casa = utilitario.consultar("select 1 as codigo,count(ide_geper) as contador  from rec_alumno_periodo where ide_repea ="+com_periodo.getValue()+" and ide_geper in (select ide_geper from gen_persona where identificac_geper='"+cedula_alumno+"')  having count(ide_geper) >1");
                    if (tab_casa.getTotalFilas()>0) {
                        //No existe el documento en la tabla de empleados
                        str_msg_erro += getFormatoError("El estudiante con número de cédula << " + cedula_alumno + " >> se encuentra registrado en la base de datos del sistema RUA en más de una ocasión, favor revisar, no es válido");
                    }
                    else{
                       TablaGenerica tab_existe_alumno = utilitario.consultar("select ide_geper,ide_geper as contador  from rec_alumno_periodo where ide_repea ="+com_periodo.getValue()+" and ide_geper in (select ide_geper from gen_persona where identificac_geper='"+cedula_alumno+"') ");
                      // tab_existe_alumno.imprimirSql();
                       if (tab_existe_alumno.getTotalFilas()>0) {
                           
                        }
                        else{
                        str_msg_erro += getFormatoError("El número de cédula << " + cedula_alumno + " >> no se encuentra registrado en el listado de alumnos del Sistema RUA, revisar en la Fila: " + (i + 1) + " no es válido");
                        }
                    }
                    if (valor_descuento == null || valor_descuento.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("En el archivo excel el campo valor descuento se encuentra vació en la fila: " + (i + 1));
                }
                }
                             
                
            }

            archivoExcel.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (!str_msg_adve.isEmpty()) {
            str_resultado += "<strong><font color='#ffcc33'>ADVERTENCIAS</font></strong>" + str_msg_adve;
        }
        if (!str_msg_erro.isEmpty()) {
            str_resultado += "<strong><font color='#ff0000'>ERRORES</font></strong>" + str_msg_erro;
        }

        return str_resultado;
    }

    /**
     * Genera un mensaje de información color azul
     *
     * @param mensaje
     * @return
     */
    private String getFormatoInformacion(String mensaje) {
        return "<div><font color='#3333ff'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
    }

    /**
     * Genera un mensaje de Advertencia color tomate
     *
     * @param mensaje
     * @return
     */
    private String getFormatoAdvertencia(String mensaje) {
        return "<div><font color='#ffcc33'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
    }

    /**
     * Genera un mensaje de Error color rojo
     *
     * @param mensaje
     * @return
     */
    private String getFormatoError(String mensaje) {
        return "<div><font color='#ff0000'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
    }

    /**
     * Genera un mensaje de exito color verde
     *
     * @param mensaje
     * @return
     */
    private String getFormatoOk(String mensaje) {
        return "<div><font color='#00FF00'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
    }

    public void subrirArchivo() {
        if (com_periodo.getValue() != null) {
            upl_archivo.limpiar();
            dia_importar.dibujar();
            edi_mensajes.setValue(null);
        } else {
            utilitario.agregarNotificacionInfo("ADVERTENCIA,", "SELECCIONE EL PERIODO ACADEMICO PARA REGISTRAR EL DESCUENTO");
        }
    }

    String empleado = "";
    String documento = "";
    String ide_empleado = "";


    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {
        guardarPantalla();
        imprimirLectura();
    }

    @Override
    public void eliminar() {
    }



    public Upload getUpl_archivo() {
        return upl_archivo;
    }

    public void setUpl_archivo(Upload upl_archivo) {
        this.upl_archivo = upl_archivo;
    }

     public VisualizarPDF getVipdf_imp_lectura() {
        return vipdf_imp_lectura;
    }

    public void setVipdf_imp_lectura(VisualizarPDF vipdf_imp_lectura) {
        this.vipdf_imp_lectura = vipdf_imp_lectura;
    }

}

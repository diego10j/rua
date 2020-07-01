/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
import java.util.List;
import javax.ejb.EJB;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_gerencial.ejb.ServicioGerencial;
import paq_presupuesto.ejb.ServicioPresupuesto;
import persistencia.Conexion;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.sistema.ServicioSistema;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Luis Toapanta
 */
public class UpCedulasMensuales extends Pantalla {

    private Etiqueta eti_perfil = new Etiqueta();
    private Combo com_balance = new Combo();
    private Combo com_ano = new Combo();
    private Combo com_mes = new Combo();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Upload upl_archivo = new Upload();
    private Editor edi_mensajes = new Editor();
    private Dialogo dia_importar = new Dialogo();
    private Confirmar con_confirma = new Confirmar();
    private Etiqueta eti_anio = new Etiqueta();
    private Etiqueta eti_obra = new Etiqueta();
    private Etiqueta eti_tip_balance = new Etiqueta();
    private Etiqueta eti_mes = new Etiqueta();

    @EJB
    private final ServicioGerencial ser_gerencial = (ServicioGerencial) utilitario.instanciarEJB(ServicioGerencial.class);
    @EJB
    private final ServicioSistema ser_cliente = (ServicioSistema) utilitario.instanciarEJB(ServicioSistema.class);
    @EJB
    private final ServiciosAdquisiones ser_persona = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public UpCedulasMensuales() {
        if (tienePerfil()) {

            bar_botones.getBot_guardar().setRendered(false);
            bar_botones.getBot_eliminar().setRendered(false);
            bar_botones.getBot_insertar().setRendered(false);

            eti_perfil.setStyle("font-size: 14px;font-weight: bold");
            eti_perfil.setValue("Usuario: <<< " + empleado + " >>>  ");
            bar_botones.agregarComponente(eti_perfil);

            com_ano.setId("com_ano");
            com_ano.setCombo(ser_gerencial.getAnio("true", "1", ""));
            com_ano.setMetodo("seleccionaElAnio");
            bar_botones.agregarComponente(new Etiqueta("Seleccione el Año:"));
            bar_botones.agregarComponente(com_ano);

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setHeader("CASAS SALESIANAS REGISTRADAS EN EL PERIODO FISCAL");
            tab_tabla1.setTabla("ger_cedula_cabecera", "ide_gececa", 1);
            tab_tabla1.setCondicion("ide_geani=-1");
            tab_tabla1.getColumna("ide_gerest").setCombo(ser_gerencial.getEstado());
            tab_tabla1.getColumna("ide_geani").setVisible(false);
            tab_tabla1.getColumna("ide_gerobr").setCombo(ser_gerencial.getObra());
            tab_tabla1.getColumna("responsable_gececa").setEtiqueta();
            tab_tabla1.getColumna("responsable_gececa").setLongitud(25);
            tab_tabla1.getColumna("responsable_gececa").setValorDefecto(utilitario.getVariable("NICK"));
            tab_tabla1.getColumna("ide_gececa").setNombreVisual("CODIGO");
            tab_tabla1.getColumna("ide_gerest").setNombreVisual("ESTADO");
            tab_tabla1.getColumna("ide_gerobr").setNombreVisual("OBRAS SALESIANAS");
            tab_tabla1.getColumna("responsable_gececa").setNombreVisual("RESPONSABLE");
            tab_tabla1.getColumna("fecha_apertura_gececa").setNombreVisual("FECHA APERTURA");
            tab_tabla1.getColumna("fecha_cierre_gececa").setNombreVisual("FECHA CIERRE");
            tab_tabla1.getColumna("observacion_gececa").setNombreVisual("OBSERVACION");
            tab_tabla1.getColumna("observacion_gececa").setVisible(false);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(6);
            tab_tabla1.setLectura(true);
            tab_tabla1.dibujar();

            PanelTabla pat_tabla1 = new PanelTabla();
            pat_tabla1.setId("pat_tabla1");
            pat_tabla1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("ger_cedula_mensual", "ide_geceme", 2);
            tab_tabla2.getColumna("ide_gerest").setCombo(ser_gerencial.getEstado());
            tab_tabla2.getColumna("ide_getiba").setCombo(ser_gerencial.getTipoBalance("-1", ""));
            tab_tabla2.getColumna("ide_gemes").setCombo(ser_gerencial.getMes("-1", "-1"));
            tab_tabla2.getColumna("ide_gececa").setUnico(true);
            tab_tabla2.getColumna("ide_gerest").setUnico(true);
            tab_tabla2.getColumna("ide_getiba").setUnico(true);
            tab_tabla2.getColumna("ide_gemes").setUnico(true);
            tab_tabla2.getColumna("responsable_geceme").setLectura(true);
            tab_tabla2.getColumna("responsable_geceme").setValorDefecto(utilitario.getVariable("NICK"));
            tab_tabla2.getColumna("ide_geceme").setNombreVisual("CODIGO");
            tab_tabla2.getColumna("ide_gerest").setNombreVisual("ESTADO");
            tab_tabla2.getColumna("ide_getiba").setNombreVisual("TIPO BALANCE");
            tab_tabla2.getColumna("ide_gemes").setNombreVisual("MES");
            tab_tabla2.getColumna("responsable_geceme").setNombreVisual("RESPONSABLE");
            tab_tabla2.getColumna("fecha_apertura_geceme").setNombreVisual("FEHCHA APERTURA");
            tab_tabla2.getColumna("fecha_cierre_geceme").setNombreVisual("FECHA CIERRE");
            tab_tabla2.getColumna("observacion_geceme").setNombreVisual("OBSERVACION");
            tab_tabla2.agregarRelacion(tab_tabla3);
            //tab_tabla2.setTipoFormulario(true);
            //tab_tabla2.getGrid().setColumns(6);
            tab_tabla2.setLectura(true);
            tab_tabla2.dibujar();

            PanelTabla pat_tabla2 = new PanelTabla();
            pat_tabla2.setId("pat_tabla2");
            pat_tabla2.setPanelTabla(tab_tabla2);

            tab_tabla3.setId("tab_tabla3");
            tab_tabla3.setTabla("ger_cedula", "ide_gerced", 3);
            tab_tabla3.getColumna("ide_prcla").setCombo(ser_gerencial.getPlanPresupuestario());
            tab_tabla3.getColumna("ide_prcla").setAutoCompletar();
            tab_tabla3.getColumna("ide_prcla").setLectura(true);
            tab_tabla3.getColumna("inicial_gerced").setLectura(true);
            tab_tabla3.getColumna("reforma_gerced").setLectura(true);
            tab_tabla3.getColumna("devengado_gerced").setLectura(true);
            tab_tabla3.getColumna("comprometido_gerced").setLectura(true);
            tab_tabla3.getColumna("ide_gerced").setNombreVisual("CODIGO");
            tab_tabla3.getColumna("ide_prcla").setNombreVisual("PARTIDA PRESUPUESTARIA");
            tab_tabla3.getColumna("inicial_gerced").setNombreVisual("ASIG. INICIAL");
            tab_tabla3.getColumna("reforma_gerced").setNombreVisual("VAL- REFORMADO");
            tab_tabla3.getColumna("devengado_gerced").setNombreVisual("DEVENGADO");
            tab_tabla3.getColumna("comprometido_gerced").setNombreVisual("COMPROMETIDO");
            //tab_tabla3.setColumnaSuma("valor_debe_gebade,valor_haber_gebade");
            tab_tabla3.dibujar();

            PanelTabla pat_tabla3 = new PanelTabla();
            pat_tabla3.setId("pat_tabla3");
            pat_tabla3.setPanelTabla(tab_tabla3);

            Division div_division = new Division();
            div_division.setId("div_division");
            div_division.dividir3(pat_tabla1, pat_tabla2, pat_tabla3, "20%", "50%", "H");

            agregarComponente(div_division);

            Boton bot_archivo = new Boton();
            bot_archivo.setValue("Subir Balances Mensuales");
            bot_archivo.setIcon("ui-icon-clipboard");
            bot_archivo.setMetodo("abrirDialogoImportar");
            bar_botones.agregarBoton(bot_archivo);

            //Dialogo para importar 
		dia_importar.setId("dia_importar");		
		dia_importar.setTitle("IMPORTAR BALANCES GENERALES");
		//dia_importar.setPosition("left");
		dia_importar.setWidth("50%");
		dia_importar.setHeight("85%");
		dia_importar.getBot_aceptar().setRendered(false);
            Grid gri_cuerpo_archivo = new Grid();
            // GRID CARGA DATOS DE LA OBRA A SUBIR
            eti_anio.setStyle("font-size:14px;font-weight: bold;color:green");
            eti_obra.setStyle("font-size:14px;font-weight: bold;color:green");
            eti_tip_balance.setStyle("font-size:14px;font-weight: bold;color:green");
            eti_mes.setStyle("font-size:14px;font-weight: bold;color:green");

            Etiqueta eti_des_a = new Etiqueta("Año:");
            Etiqueta eti_des_c = new Etiqueta("Casa-Obra:");
            Etiqueta eti_des_t = new Etiqueta("Tipo Balance:");
            Etiqueta eti_des_m = new Etiqueta("Año:");
            Etiqueta eti_des_s = new Etiqueta("Seleccione el archivo: ");
            eti_des_a.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_c.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_t.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_m.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_s.setStyle("font-size:14px;font-weight: bold;color:black");

            Grid gri_detalle_casa = new Grid();
            gri_detalle_casa.setColumns(2);
            gri_detalle_casa.getChildren().add(eti_des_a);
            gri_detalle_casa.getChildren().add(eti_anio);
            gri_detalle_casa.getChildren().add(eti_des_c);
            gri_detalle_casa.getChildren().add(eti_obra);
            gri_detalle_casa.getChildren().add(eti_des_t);
            gri_detalle_casa.getChildren().add(eti_tip_balance);
            gri_detalle_casa.getChildren().add(eti_des_m);
            gri_detalle_casa.getChildren().add(eti_mes);

            Grid gri_impo = new Grid();
            gri_impo.setColumns(2);

            gri_impo.getChildren().add(eti_des_s);
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

            edi_mensajes.setControls("");
            edi_mensajes.setId("edi_mensajes");
            edi_mensajes.setStyle("overflow:auto;");
            edi_mensajes.setWidth(dia_importar.getAnchoPanel() - 15);
            edi_mensajes.setDisabled(true);
            gri_valida.setFooter(edi_mensajes);

            gri_cuerpo_archivo.setStyle("width:" + (dia_importar.getAnchoPanel() - 5) + "px;");
            gri_cuerpo_archivo.setMensajeInfo("Esta opción  permite subir un registro a partir de un archivo xls");
            gri_cuerpo_archivo.getChildren().add(gri_detalle_casa);
            gri_cuerpo_archivo.getChildren().add(gri_impo);
            gri_cuerpo_archivo.getChildren().add(gri_valida);
            gri_cuerpo_archivo.getChildren().add(edi_mensajes);
            gri_cuerpo_archivo.getChildren().add(new Espacio("0", "10"));

            dia_importar.setDialogo(gri_cuerpo_archivo);
            dia_importar.getBot_aceptar().setMetodo("aceptaDiaImp");
            dia_importar.setDynamic(false);

            agregarComponente(dia_importar);

            //CONFIRMAR
            con_confirma.setId("con_confirma");
            con_confirma.setMessage("Está seguro que desea remplazar este archivo");
            con_confirma.setTitle("REMPLAZAR ARCHIVO EXCEL");
            con_confirma.getBot_aceptar().setValue("Si");
            con_confirma.getBot_cancelar().setValue("No");
            agregarComponente(con_confirma);

        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "El usuario ingresado no tiene permiso para esta actividad");
        }

    }
    String perfil = "";
    String ide_empleado = "";
    String empleado = "";
    String documento = "";
    //String  = "";

    public void SubirArchivo() {
        tab_tabla1.getFilaActual();
        System.out.println("valor seleccionado " + tab_tabla1.getFilaActual());
        con_confirma.getBot_aceptar().setMetodo("abrirDialogoImportar");
        con_confirma.dibujar();
    }

    public void abrirDialogoImportar() {
        if (!tab_tabla1.getValor("ide_gerest").equals(utilitario.getVariable("p_ger_estado_activo"))) {
            utilitario.agregarMensajeError("Año", "La Casa y Obra debe encontrarse en estado ACTIVO para el Año Físcal vigente y poder subir el Balance General");
            return;
        }
        if (!tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_gerest").equals(utilitario.getVariable("p_ger_estado_activo"))) {
            utilitario.agregarMensajeError("Mes", "La Casa y Obra debe encontrarse en estado ACTIVO para el Mes Físcal vigente y poder subir el Balance General");
            return;
        }
        TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraPeriodoFiscal("", "", "2", tab_tabla1.getValor("ide_gecobc")));
        eti_obra.setValue(tab_casa_obra.getValor("nombre_gercas") + " " + tab_casa_obra.getValor("nombre_gerobr"));
        TablaGenerica tab_anio_eti = utilitario.consultar(ser_gerencial.getAnio("", "2", com_ano.getValue().toString()));
        eti_anio.setValue(tab_anio_eti.getValor("nom_geani"));
        TablaGenerica tab_tipo_balance = utilitario.consultar(ser_gerencial.getTipoBalance("2", tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_getiba")));
        eti_tip_balance.setValue(tab_tipo_balance.getValor("detalle_getiba"));
        TablaGenerica tab_mes = utilitario.consultar(ser_gerencial.getMes("0", tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_gemes")));
        eti_mes.setValue(tab_mes.getValor("nombre_gemes"));

        upl_archivo.limpiar();
        dia_importar.dibujar();
        edi_mensajes.setValue(null);
        utilitario.addUpdate("eti_obra,eti_anio,eti_mes");
    }

    public void filtroComboPeriodo() {
        com_balance.setCombo(ser_gerencial.getTipoBalance("-1", ""));
        utilitario.addUpdate("com_balance");

    }

    public void seleccionaElAnio() {
        if (com_ano.getValue() != null) {
            tab_tabla1.setCondicion(" ide_geani=" + com_ano.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            tab_tabla3.ejecutarValorForanea(tab_tabla2.getValorSeleccionado());

        } else {
            utilitario.agregarMensajeInfo("Selecione un Año", "");

        }
    }

    private boolean tienePerfil() {
        List sql = utilitario.getConexion().consultar(ser_gerencial.getDatoEmpleado(utilitario.getVariable("IDE_USUA")));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);
            empleado = fila[2].toString();
            documento = fila[4].toString();
            ide_empleado = fila[1].toString();
            return true;
        } else {
            return false;
        }
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
            utilitario.getConexion().ejecutarSql(ser_gerencial.deleteTempBalance(utilitario.getVariable("ide_usua")));
            try {
                Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
                Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA

                if (hoja == null) {
                    utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
                    return;
                }

                int int_fin = hoja.getRows();
                upl_archivo.setNombreReal(evt.getFile().getFileName());
                str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas que se subieron con exito, dar clic en aceptar y actualizar la pantalla de inscripciones");

                System.out.println(" valor int_fin " + int_fin);
                for (int i = 0; i < int_fin; i++) {

                String casa_obra = hoja.getCell(0, i).getContents();
                casa_obra = casa_obra.trim();

                String tipo_balance = hoja.getCell(1, i).getContents();
                tipo_balance = tipo_balance.trim();

                String anio = hoja.getCell(2, i).getContents();
                anio = anio.trim();

                String mes = hoja.getCell(3, i).getContents();
                mes = mes.trim();

                String responsable = hoja.getCell(4, i).getContents();
                responsable = responsable.trim();
                
                String cuenta = hoja.getCell(5, i).getContents();
                cuenta = cuenta.trim();
                
                String valor_debe = hoja.getCell(6, i).getContents();
                valor_debe = valor_debe.replaceAll(",", ".");
                
                String valor_haber = hoja.getCell(7, i).getContents();
                valor_haber = valor_haber.replaceAll(",", ".");
                TablaGenerica tab_cuenta = utilitario.consultar("select ide_cndpc,replace(codig_recur_cndpc,'.','') as nuevo from con_det_plan_cuen where replace(codig_recur_cndpc,'.','') ='"+cuenta+"' and ide_cnncu=5");
                //inserto en tabla temporal
                utilitario.getConexion().ejecutarSql(ser_gerencial.insertTempBalance(tab_cuenta.getValor("ide_cndpc"),valor_debe,valor_haber,utilitario.getVariable("ide_usua"),"5" ));                
                
                }

                archivoExcel.close();
            } catch (Exception e) {
                // TODO: handle exception
            }

            if (!str_msg_info.isEmpty()) {
                str_resultado = "<strong><font color='#3333ff'>INFORMACION</font></strong>" + str_msg_info;
            }
            //inicio insertando y calculando en la tabla
            int nivel_nuevo=0;
            for(int j=5;j>2; j--){
                nivel_nuevo=j-1;
               // System.out.println("j  "+j);
                TablaGenerica tab_temporal=utilitario.consultar(ser_gerencial.getCalTemBalance(utilitario.getVariable("ide_usua"), j+""));
                for(int k=0;k<tab_temporal.getTotalFilas();k++){     
                   // System.out.println("segundo insert k "+k);
                    utilitario.getConexion().ejecutarSql(ser_gerencial.insertTempBalance(tab_temporal.getValor(k, "con_ide_cndpc"),tab_temporal.getValor(k, "debe"),tab_temporal.getValor(k, "haber"),utilitario.getVariable("ide_usua"),nivel_nuevo+"" ));                
                }                         
                
            }
            TablaGenerica tab_temporal_insert=utilitario.consultar(ser_gerencial.getCalTemBalance(utilitario.getVariable("ide_usua"), "2,3,4,5,6"));
            for(int k=0;k<tab_temporal_insert.getTotalFilas();k++){        
            tab_tabla3.insertar();
                tab_tabla3.setValor("ide_cndpc",tab_temporal_insert.getValor(k,"con_ide_cndpc"));
                tab_tabla3.setValor("ide_gebame",tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_gebame"));
                tab_tabla3.setValor("valor_debe_gebade",tab_temporal_insert.getValor(k,"debe"));
                tab_tabla3.setValor("valor_haber_gebade",tab_temporal_insert.getValor(k,"haber"));
            }
                   
            // hasta aqui termino la insercion
            edi_mensajes.setValue(str_resultado);
            utilitario.addUpdate("edi_mensajes");
            tab_tabla3.guardar();
            guardarPantalla();
            tab_tabla3.ejecutarValorForanea(tab_tabla2.getValorSeleccionado());
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

                 String casa_obra = hoja.getCell(0, i).getContents();
                casa_obra = casa_obra.trim();

                String tipo_balance = hoja.getCell(1, i).getContents();
                tipo_balance = tipo_balance.trim();

                String anio = hoja.getCell(2, i).getContents();
                anio = anio.trim();

                String mes = hoja.getCell(3, i).getContents();
                mes = mes.trim();

                String responsable = hoja.getCell(4, i).getContents();
                responsable = responsable.trim();
                
                String cuenta = hoja.getCell(5, i).getContents();
                cuenta = cuenta.trim();
                
                String valor_debe = hoja.getCell(6, i).getContents();
                valor_debe = valor_debe.replaceAll(",", ".");
                double dou_valor_debe=0;
		try {
                    //Valida que sea una cantidad numerica 
                    dou_valor_debe=(Double.parseDouble(valor_debe));
                    dou_tot_debe +=dou_valor_debe;
		} catch (Exception e) {
                    // TODO: handle exception
                    str_msg_erro+=getFormatoError("Valor numerico no valido as debe, columna (G), fila "+(i+1));
		}
                String valor_haber = hoja.getCell(7, i).getContents();
                valor_haber = valor_haber.replaceAll(",", ".");
                double dou_valor_haber=0;
		try {
                    //Valida que sea una cantidad numerica 
                    dou_valor_haber=(Double.parseDouble(valor_haber));	
                    dou_tot_haber +=dou_valor_haber;
		} catch (Exception e) {
                    // TODO: handle exception
                    str_msg_erro+=getFormatoError("Valor numerico no valido al haber, columna (H), fila "+(i+1));
		}

                if (casa_obra == null || casa_obra.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("No existe el código de la Obra, columna(A) en la Fila: " + (i + 1));
                } else {
                    TablaGenerica tab_casa = utilitario.consultar("select * from ger_obra  where codigo_gerobr='"+casa_obra+"'");
                    if (tab_casa.isEmpty()) {
                        //No existe el documento en la tabla de empleados
                        str_msg_erro += getFormatoError("El código de obra << " + casa_obra + " >> no se encuentra registrado en la base de datos revisar en la Fila: " + (i + 1) + " no es válido");
                    }
                    else if(!tab_casa.getValor("ide_gerobr").equals(tab_tabla1.getValor("ide_gerobr"))){
                        str_msg_erro += getFormatoError("El código de obra << " + casa_obra + " >> no se corresponde a la obra seleccionada en la Fila: " + (i + 1) + " no es válido");
                    }
                }
                if (tipo_balance == null || tipo_balance.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("No existe el código del tipo de balance, columna(B) en la Fila: " + (i + 1));
                } else {
                    TablaGenerica tab_balance = utilitario.consultar("select * from ger_tipo_balance  where ide_getiba="+tipo_balance);
                    if (tab_balance.isEmpty()) {
                        //No existe el documento en la tabla de empleados
                        str_msg_erro += getFormatoError("El código tipo de balance << " + tipo_balance + " >> no se encuentra registrado en la base de datos revisar en la Fila: " + (i + 1) + " no es válido");
                    }
                    else if (!tipo_balance.equals(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_getiba"))){
                        str_msg_erro += getFormatoError("El código tipo balance << " + tipo_balance + " >> no corresponde al seleccionado "+eti_tip_balance+" en la Fila: " + (i + 1) + " no es válido");
                    }
                }
                //anio
                if (anio == null || anio.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("No existe el año, columna(C) en la Fila: " + (i + 1));
                } else {
                    TablaGenerica tab_anio = utilitario.consultar("select * from gen_anio where nom_geani='"+anio+"'");
                    if (tab_anio.isEmpty()) {
                        //No existe el documento en la tabla de empleados
                        str_msg_erro += getFormatoError("El año << " + anio + " >> no se encuentra registrado en la base de datos revisar en la Fila: " + (i + 1) + " no es válido");
                    }
                    else if(!tab_anio.getValor("ide_geani").equals(com_ano.getValue().toString())){
                        str_msg_erro += getFormatoError("El año << " + anio + " >> no corresponde al seleccionado "+eti_anio+" en la Fila: " + (i + 1) + " no es válido"); 
                    }
                }
                //mes
                if (mes == null || mes.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("No existe el mes, columna(D) en la Fila: " + (i + 1));
                } else {
                    TablaGenerica tab_mes = utilitario.consultar("select * from gen_mes where nombre_gemes='"+mes+"'");
                    if (tab_mes.isEmpty()) {
                        //No existe el documento en la tabla de empleados
                        str_msg_erro += getFormatoError("El mes << " + mes + " >> no se encuentra registrado en la base de datos revisar en la Fila: " + (i + 1) + " no es válido");
                    }
                    else if(!tab_mes.getValor("ide_gemes").equals(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_gemes"))){
                        str_msg_erro += getFormatoError("El mes << " + mes + " >> no corresponde al seleccionado "+eti_mes+" en la Fila: " + (i + 1) + " no es válido"); 
                    }
                }
                 //usuario responsable
                if (responsable == null || responsable.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("No existe el responsable, columna(E) en la Fila: " + (i + 1));
                }
                // cuenta contable
                if (cuenta == null || cuenta.isEmpty()) {
                    //No existe registro de la obra
                    str_msg_erro += getFormatoError("No existe la cuenta contable, columna(F) en la Fila: " + (i + 1));
                } else {
                    TablaGenerica tab_cuenta = utilitario.consultar("select ide_cndpc,replace(codig_recur_cndpc,'.','') as nuevo from con_det_plan_cuen where replace(codig_recur_cndpc,'.','') ='"+cuenta+"' and ide_cnncu=5");
                    if (tab_cuenta.isEmpty()) {
                        //No existe el documento en la tabla de empleados
                        str_msg_erro += getFormatoError("La cuenta << " + cuenta + " >> no se encuentra registrado en la base de datos revisar en la Fila: " + (i + 1) + " no es válido");
                    }
                   
                }
            }
            if(!utilitario.getFormatoNumero(dou_tot_debe, 2).equals(utilitario.getFormatoNumero(dou_tot_haber, 2))){
                str_msg_erro += getFormatoError("Los totales de la columna del debe son distintas al haber <<< debe: "+utilitario.getFormatoNumero(dou_tot_debe, 2)+" haber: "+utilitario.getFormatoNumero(dou_tot_haber, 2)+" >>>");
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
    @Override
    public void insertar() {
        tab_tabla2.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla2.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla2.eliminar();
    }

    public Combo getCom_ano() {
        return com_ano;
    }

    public void setCom_ano(Combo com_ano) {
        this.com_ano = com_ano;
    }

    public Etiqueta getEti_perfil() {
        return eti_perfil;
    }

    public void setEti_perfil(Etiqueta eti_perfil) {
        this.eti_perfil = eti_perfil;
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

}

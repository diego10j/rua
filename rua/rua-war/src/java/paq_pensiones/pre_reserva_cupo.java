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
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
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
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author usuario
 */
public class pre_reserva_cupo extends Pantalla {

    private Combo com_periodo_academico = new Combo();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Dialogo dia_importar = new Dialogo();
    private AutoCompletar aut_rubros = new AutoCompletar();
    private Texto txt_texto = new Texto();
    private Upload upl_archivo = new Upload();
    private Editor edi_mensajes = new Editor();
    private Etiqueta eti_num_nomina = new Etiqueta();
    private List<String[]> lis_importa = null;
    private Confirmar con_confirma = new Confirmar();

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);

    public pre_reserva_cupo() {

        bar_botones.agregarComponente(new Etiqueta("Periodo Académico:"));
        com_periodo_academico.setId("cmb_periodo_academico");
        com_periodo_academico.setCombo(ser_pensiones.getPeriodoAcademico("true"));
        com_periodo_academico.setMetodo("mostrarAlumnos");
        bar_botones.agregarComponente(com_periodo_academico);

        Boton bot_impor_alumno = new Boton();
        bot_impor_alumno.setValue("Importar Alumnos");
        bot_impor_alumno.setIcon("ui-icon-note");
        bot_impor_alumno.setMetodo("abrirDialogoImportar");
        bar_botones.agregarBoton(bot_impor_alumno);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("rec_reserva_cupo", "ide_rerec", 1);
        tab_tabla1.setCondicion("ide_rerec=-1");
        tab_tabla1.dibujar();
        tab_tabla1.setRows(20);

        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);

        dia_importar.setId("dia_importar");
        dia_importar.setTitle("IMPORTAR ALUMNOS NUEVOS");
        dia_importar.setWidth("50%");
        dia_importar.setHeight("70%");
        dia_importar.getBot_aceptar().setRendered(true);

        Grid gri_cuerpo = new Grid();

        Grid gri_impo = new Grid();
        gri_impo.setColumns(2);

        gri_impo.getChildren().add(new Etiqueta("Codigo ultimo del alumno: "));
        txt_texto.setId("txt_texto");
        txt_texto.setSoloNumeros();
        gri_impo.getChildren().add(txt_texto);

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

        gri_cuerpo.setStyle("width:" + (dia_importar.getAnchoPanel() - 5) + "px;");
        gri_cuerpo.setMensajeInfo("Esta opción  permite subir valores a un rubro a partir de un archivo xls");
        gri_cuerpo.getChildren().add(gri_impo);
        gri_cuerpo.getChildren().add(gri_valida);
        gri_cuerpo.getChildren().add(edi_mensajes);
        gri_cuerpo.getChildren().add(new Espacio("0", "10"));

        dia_importar.setDialogo(gri_cuerpo);
        dia_importar.setDynamic(false);

        agregarComponente(dia_importar);

        //CONFIRMAR
        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro que desea continuar pese a las Alvertencias ");
        con_confirma.setTitle("CONFIRMAR");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("gen_persona", "ide_geper", 2);
        //tab_tabla2.setTipoFormulario(true);

    }

    public void mostrarAlumnos() {
        if (com_periodo_academico.getValue() == null) {
            tab_tabla1.setCondicion("ide_repea=-1");
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1");
        } else {
            tab_tabla1.setCondicion("ide_repea=" + com_periodo_academico.getValue().toString());
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1");
        }
    }

    public void abrirDialogoImportar() {

        if (com_periodo_academico.getValue() != null) {
            dia_importar.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un Periodo ", "");
        }
    }

    /**
     * Valida el archivo para que pueda importar un rubro a la nomina
     *
     * @param evt
     */
    public void validarArchivo(FileUploadEvent evt) {

        //Leer el archivo
        int acumulador = 0;
        String str_msg_info = "";
        String str_msg_adve = "";
        String str_msg_erro = "";
        String str_msg_ok = "";

        try {

            Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
            Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA
            if (hoja == null) {
                utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
                return;
            }
            int int_fin = hoja.getRows();
            upl_archivo.setNombreReal(evt.getFile().getFileName());

            str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas");
            str_msg_ok += getFormatoOk("Se mporto los datos con exito");

            for (int i = 0; i < int_fin; i++) {
                String str_cedula = hoja.getCell(0, i).getContents();
                str_cedula = str_cedula.trim();

                String str_nombreyapellido = hoja.getCell(1, i).getContents();
                str_nombreyapellido = str_nombreyapellido.trim();

                String str_curso = hoja.getCell(2, i).getContents();
                str_curso = str_curso.trim();

                TablaGenerica tab_cedula = utilitario.consultar("select ide_geper,identificac_geper,nom_geper,ide_vgtcl from gen_persona where identificac_geper='" + str_cedula + "'");
                TablaGenerica tab_tipo = utilitario.consultar("select ide_vgtcl,nombre_vgtcl from ven_tipo_cliente where ide_vgtcl=" + tab_cedula.getValor("ide_vgtcl"));
                TablaGenerica tab_curso = utilitario.consultar("select ide_recur,CONCAT (UPPER (descripcion_recur)) as descripcion_recur from rec_curso  where descripcion_recur='" + str_curso.toUpperCase() + "'");
                //tab_curso.imprimirSql();
                if (tab_curso.getTotalFilas() > 0) {
                    if (tab_cedula.getTotalFilas() > 0) {
                        str_msg_erro += getFormatoError("El nombre: " + str_nombreyapellido + " con número de cedula:" + str_cedula + " ya existe en la base de datos con cedula:" + tab_cedula.getValor("identificac_geper") + " nombre: " + tab_cedula.getValor("nom_geper") + " y de tipo " + tab_tipo.getValor("nombre_vgtcl") + ", fila " + (i + 1));
                        acumulador = acumulador + 1;
                    } else {
                        acumulador = 0;
                    }
                } else {
                    str_msg_erro += getFormatoError("El curso: " + str_curso + " no se encuentra registrado en la base de datos, fila " + (i + 1));
                    acumulador = acumulador + 1;
                }
            }

            if (acumulador > 0) {
                String str_resultado = "";
                if (!str_msg_info.isEmpty()) {
                    str_resultado = "<strong><font color='#3333ff'>INFORMACION</font></strong>" + str_msg_info;
                }
                if (!str_msg_adve.isEmpty()) {
                    str_resultado += "<strong><font color='#ffcc33'>ADVERTENCIAS</font></strong>" + str_msg_adve;
                }
                if (!str_msg_erro.isEmpty()) {
                    str_resultado += "<strong><font color='#ff0000'>ERRORES</font></strong>" + str_msg_erro;
                }
                edi_mensajes.setValue(str_resultado);
                utilitario.addUpdate("edi_mensajes");
            } else {
                for (int j = 0; j < int_fin; j++) {
                    String str_cedu = hoja.getCell(0, j).getContents();
                    str_cedu = str_cedu.trim();

                    String str_nom_ape = hoja.getCell(1, j).getContents();
                    str_nom_ape = str_nom_ape.trim();

                    String str_cur = hoja.getCell(2, j).getContents();
                    str_cur = str_cur.trim();

                    tab_tabla2.insertar();
                    tab_tabla2.setValor("nom_geper", j + str_nom_ape.toUpperCase());
                    tab_tabla2.setValor("identificac_geper", str_cedu);

                }
                tab_tabla2.guardar();
                guardarPantalla();
                for (int k = 0; k < int_fin; k++) {
                    String str_cedu = hoja.getCell(0, k).getContents();
                    str_cedu = str_cedu.trim();

                    String str_nom_ape = hoja.getCell(1, k).getContents();
                    str_nom_ape = str_nom_ape.trim();

                    String str_cur = hoja.getCell(2, k).getContents();
                    str_cur = str_cur.trim();
                    TablaGenerica tab_curso = utilitario.consultar("select ide_recur,CONCAT (UPPER (descripcion_recur)) as descripcion_recur from rec_curso  where descripcion_recur='" + str_cur.toUpperCase() + "'");
                    TablaGenerica tab_consulta = utilitario.consultar("select ide_geper,nom_geper,identificac_geper from gen_persona where identificac_geper='" + str_cedu + "'");
                    tab_consulta.imprimirSql();
                    tab_tabla1.insertar();
                    tab_tabla1.setValor("ide_geper", tab_consulta.getValor("ide_geper"));
                    tab_tabla1.setValor("ide_repea", com_periodo_academico.getValue().toString());
                    tab_tabla1.setValor("ide_recur", tab_curso.getValor("ide_recur"));
                    tab_tabla1.setValor("fecha_registro_rerec", utilitario.getFechaActual());
                    tab_tabla1.setValor("matriculado_rerec", "false");
                    
                }
                tab_tabla1.guardar();
                guardarPantalla();

                String str_resultado = "";
                if (!str_msg_info.isEmpty()) {
                    str_resultado = "<strong><font color='#3333ff'>INFORMACION</font></strong>" + str_msg_info;
                }
                if (!str_msg_ok.isEmpty()) {
                    str_resultado += "<strong><font color='#00FF00'>SUCCESSFUL</font></strong>" + str_msg_ok;
                }
                edi_mensajes.setValue(str_resultado);
                utilitario.addUpdate("edi_mensajes");

            }
            archivoExcel.close();

        } catch (Exception e) {
            // TODO: handle exception
        }

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

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Combo getCom_periodo_academico() {
        return com_periodo_academico;
    }

    public void setCom_periodo_academico(Combo com_periodo_academico) {
        this.com_periodo_academico = com_periodo_academico;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Dialogo getDia_importar() {
        return dia_importar;
    }

    public void setDia_importar(Dialogo dia_importar) {
        this.dia_importar = dia_importar;
    }

    public Upload getUpl_archivo() {
        return upl_archivo;
    }

    public void setUpl_archivo(Upload upl_archivo) {
        this.upl_archivo = upl_archivo;
    }

    public List<String[]> getLis_importa() {
        return lis_importa;
    }

    public void setLis_importa(List<String[]> lis_importa) {
        this.lis_importa = lis_importa;
    }

    public Editor getEdi_mensajes() {
        return edi_mensajes;
    }

    public void setEdi_mensajes(Editor edi_mensajes) {
        this.edi_mensajes = edi_mensajes;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

}

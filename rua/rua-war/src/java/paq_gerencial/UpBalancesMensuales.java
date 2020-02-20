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
 * @author IORI YAGAMI
 */
public class UpBalancesMensuales extends Pantalla {

    private Etiqueta eti_perfil = new Etiqueta();
    private Etiqueta eti_ano = new Etiqueta();
    private Combo com_balance = new Combo();
    private Combo com_ano = new Combo();
    private Tabla tab_tabla1 = new Tabla();
    private Conexion conPostgres = new Conexion();
    private Upload upl_archivo = new Upload();
    private Editor edi_mensajes = new Editor();
    private Dialogo dia_importar = new Dialogo();
    private Confirmar con_confirma = new Confirmar();


    @EJB
    private final ServicioGerencial ser_gerencial = (ServicioGerencial) utilitario.instanciarEJB(ServicioGerencial.class);
    @EJB
    private final ServicioSistema ser_cliente = (ServicioSistema) utilitario.instanciarEJB(ServicioSistema.class);
    @EJB
    private final ServiciosAdquisiones ser_persona = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    

    public UpBalancesMensuales() {
        if (tienePerfil()) {
            
            bar_botones.getBot_insertar().setRendered(false);
            bar_botones.getBot_eliminar().setRendered(false);
            bar_botones.quitarBotonsNavegacion();
            
            conPostgres.setUnidad_persistencia("rua_gerencial");
            conPostgres.NOMBRE_MARCA_BASE = "postgres";

            eti_perfil.setStyle("font-size: 16px;font-weight: bold");
            eti_perfil.setValue("Usuario: " + empleado +"    ");  
            bar_botones.agregarComponente(eti_perfil);
            
            com_ano.setConexion(conPostgres);
            com_ano.setId("com_ano");
            com_ano.setCombo(ser_gerencial.getAnio("true,false"));
            com_ano.setMetodo("seleccionaElAnio ");
            bar_botones.agregarComponente(new Etiqueta("Año seleccion:"));
            bar_botones.agregarComponente(com_ano);

            com_balance.setConexion(conPostgres);
            com_balance.setId("com_balance");      
            com_balance.setCombo(ser_gerencial.getTipoBalance());
            com_balance.setMetodo("filtroComboPeriodo");
            bar_botones.agregarComponente(new Etiqueta("Tipo Balance:"));
            bar_botones.agregarComponente(com_balance);
                               
            tab_tabla1.setConexion(conPostgres);
            tab_tabla1.setTabla("ger_cont_balance_cabecera", "ide_gecobc", 1);
            tab_tabla1.setCondicion("ide_geani=-1");
            

            Boton bot_archivo = new Boton();
            bot_archivo.setValue("Cargar Archivo");
            bot_archivo.setIcon("ui-icon-clipboard");
            bot_archivo.setMetodo("SubirArchivo");
            bar_botones.agregarBoton(bot_archivo);
            
            Grid gri_cuerpo_archivo = new Grid();

            Grid gri_impo = new Grid();
            gri_impo.setColumns(2);

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

            edi_mensajes.setControls("");
            edi_mensajes.setId("edi_mensajes");
            edi_mensajes.setStyle("overflow:auto;");
            edi_mensajes.setWidth(dia_importar.getAnchoPanel() - 15);
            edi_mensajes.setDisabled(true);
            gri_valida.setFooter(edi_mensajes);
            
            gri_cuerpo_archivo.setStyle("width:" + (dia_importar.getAnchoPanel() - 5) + "px;");
            gri_cuerpo_archivo.setMensajeInfo("Esta opción  permite subir un registro a partir de un archivo xls");
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



        }else{
            utilitario.agregarNotificacionInfo("Mensaje", "El usuario ingresado no tiene permiso para esta actividad");
        }

    }
    String perfil = "";
    String ide_empleado = "";
    String empleado = "";
    String documento = "";
    //String  = "";

    public void SubirArchivo() {
        con_confirma.getBot_aceptar().setMetodo("abrirDialogoImportar");
        con_confirma.dibujar();
    }
    
    public void abrirDialogoImportar() {
        upl_archivo.limpiar();
        dia_importar.dibujar();
        edi_mensajes.setValue(null);
   
    }
    
    public void filtroComboPeriodo() {
        com_balance.setCombo(ser_gerencial.getTipoBalance());
        utilitario.addUpdate("com_balance");

    }
    
    public void seleccionaElAnio() {
         if (com_ano.getValue() != null) {
            tab_tabla1.setCondicion(" ide_geani=" + com_ano.getValue());
            tab_tabla1.ejecutarSql();

        } else {
            utilitario.agregarMensajeInfo("Selecione un Año", "");

        }
    }

    private boolean tienePerfil(){
        List sql = utilitario.getConexion().consultar(ser_gerencial.getDatoEmpleado(utilitario.getVariable("IDE_USUA")));
        
        if(!sql.isEmpty()){
            Object[] fila = (Object[]) sql.get(0);
                empleado = fila[2].toString();
                documento = fila[4].toString();
                ide_empleado = fila[1].toString();
                return true;
        }else{
            return false;
        }
}
    
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
            str_msg_ok += getFormatoOk("Se importo los datos con exito");

            for (int i = 0; i < int_fin; i++) {
                String año = hoja.getCell(0, i).getContents();
                año = año.trim();

                String periodo = hoja.getCell(1, i).getContents();
                periodo = periodo.trim();

                String Cuenta = hoja.getCell(2, i).getContents();
                Cuenta = Cuenta.trim();

                String Debe = hoja.getCell(3, i).getContents();
                Debe = Debe.trim();

                String Haber = hoja.getCell(4, i).getContents();
                Haber = Haber.trim();

                
                System.out.println("Informacion: "+ i+" "+año+" "+periodo+" "+Cuenta+" "+Debe+" "+Haber);            
            }
             //guardarPantalla();
            String str_resultado = "";
            if (!str_msg_info.isEmpty()) {
                str_resultado = "<strong><font color='#3333ff'>INFORMACION</font></strong>" + str_msg_info;
            }
            if (!str_msg_ok.isEmpty()) {
                str_resultado += "<strong><font color='#00FF00'>SUCCESSFUL</font></strong>" + str_msg_ok;
            }
            edi_mensajes.setValue(str_resultado);
            utilitario.addUpdate("edi_mensajes");

            archivoExcel.close();
            //tab_tabla1.actualizar();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("ERROR AL SUBIR ARHCIVO " + e);
        }
     }
     
     
    private String getFormatoInformacion(String mensaje) {
        return "<div><font color='#3333ff'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
    }

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

    public Etiqueta getEti_ano() {
        return eti_ano;
    }

    public void setEti_ano(Etiqueta eti_ano) {
        this.eti_ano = eti_ano;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }
    
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

/**
 *
 * @author ANDRES REDROBAN
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
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
import org.w3c.dom.Text;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_contabilidad.ejb.ServicioContabilidad;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_alumno_periodo_edita extends Pantalla {

    private Combo com_periodo_academico = new Combo();
    private Tabla tab_tabla1 = new Tabla();
    private Dialogo dia_importar = new Dialogo();
    private AutoCompletar aut_rubros = new AutoCompletar();
    private Texto txt_texto = new Texto();
    private Upload upl_archivo = new Upload();
    private Editor edi_mensajes = new Editor();
    private Etiqueta eti_num_nomina = new Etiqueta();
    private List<String[]> lis_importa = null;

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    @EJB
    private ServiciosAdquisiones ser_adqusiiones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

    public pre_alumno_periodo_edita() {

        bar_botones.agregarComponente(new Etiqueta("Periodo Académico:"));
        com_periodo_academico.setId("cmb_periodo_academico");
        com_periodo_academico.setCombo(ser_pensiones.getPeriodoAcademico("true"));
        com_periodo_academico.setMetodo("mostrarAlumnos");
        bar_botones.agregarComponente(com_periodo_academico);

        
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setTabla("REC_ALUMNO_PERIODO", "IDE_RECALP", 1);
        tab_tabla1.setCondicion("ide_recalp=-1");
        tab_tabla1.getColumna("descripcion_recalp").setVisible(false);
        tab_tabla1.getColumna("ide_repea").setCombo(ser_pensiones.getPeriodoAcademico("true,false"));
        tab_tabla1.getColumna("ide_repar").setCombo(ser_pensiones.getParalelos("true,false"));
        tab_tabla1.getColumna("ide_recur").setCombo(ser_pensiones.getCursos("true,false"));
        tab_tabla1.getColumna("ide_reces").setCombo(ser_pensiones.getEspecialidad("true,false"));
        tab_tabla1.getColumna("ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("ide_geper").setFiltroContenido();
        //tab_tabla1.getColumna("ide_repar").setAutoCompletar();
        //tab_tabla1.getColumna("ide_recur").setAutoCompletar();
        //tab_tabla1.getColumna("ide_reces").setAutoCompletar();
        //tab_tabla1.getColumna("ide_repar").setLectura(true);
        //tab_tabla1.getColumna("ide_recur").setLectura(true);
        //tab_tabla1.getColumna("ide_reces").setLectura(true);
        //tab_tabla1.getColumna("ide_geper").setLectura(true);
        //tab_tabla1.getColumna("retirado_recalp").setLectura(true);
        //tab_tabla1.getColumna("retirado_recalp").setLectura(true);
        tab_tabla1.getColumna("retirado_recalp").setValorDefecto("FALSE");
        tab_tabla1.getColumna("descuento_recalp").setValorDefecto("false");
        tab_tabla1.getColumna("activo_recalp").setValorDefecto("true");
        //tab_tabla1.getColumna("activo_recalp").setLectura(true);
        //tab_tabla1.getColumna("detalle_retiro_recalp").setLectura(true);
        //tab_tabla1.getColumna("fecha_retiro_recalp").setLectura(true);
        tab_tabla1.getColumna("valor_descuento_recalp").setValorDefecto("0");

        tab_tabla1.getColumna("gen_ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_repar").setAncho(20);
        tab_tabla1.getColumna("ide_geper").setEstilo("width:20 px");
        tab_tabla1.dibujar();
        tab_tabla1.setRows(15);
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);

        
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
    
    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
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
    

}

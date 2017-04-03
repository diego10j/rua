/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

import framework.aplicacion.TablaGenerica;
import framework.reportes.GenerarReporte;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class ControladorEvaluacion {

    private String strOpcion = "1";
    private Utilitario utilitario = new Utilitario();
    private List lisperiodos;
    private List lisevaluadores;
    private List lisresultado;
    private Object objperiodo;
    private Object objevaluador;
    private String strPathReporte;

    @PostConstruct
    public void cargarDatos() {
        lisperiodos = utilitario.getConexion().consultar("SELECT IDE_EVDES, IDE_GEEDP, IDE_GTEMP, FECHA_DESDE_EVDES, FECHA_HASTA_EVDES, TITULO_PROFESIONAL_EVDES FROM EVL_DESEMPENIO WHERE IDE_GTEMP=" + utilitario.getVariable("IDE_GTEMP") + " ORDER BY IDE_EVDES");

        lisevaluadores = utilitario.getConexion().consultar("SELECT a.IDE_EVEVA,c.EMPLEADO,a.FECHA_EVEVA,a.FECHA_EVALUACION_EVEVA,a.POR_PESO_EVEVA FROM EVL_EVALUADORES a\n"
                + "INNER JOIN(\n"
                + "SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR\n"
                + ")b on b.IDE_GEEDP=a.IDE_GEEDP\n"
                + "INNER JOIN (\n"
                + "SELECT IDE_GTEMP,APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP ||' '|| PRIMER_NOMBRE_GTEMP ||' '|| SEGUNDO_NOMBRE_GTEMP as EMPLEADO FROM GTH_EMPLEADO\n"
                + ")c on c.IDE_GTEMP=b.IDE_GTEMP WHERE IDE_EVDES =-1 ORDER BY IDE_EVEVA");

        lisresultado = utilitario.getConexion().consultar("SELECT a.IDE_EVRES,c.DETALLE_EVFAE,a.RESULTADO_EVRES,a.PESO_FACTOR_EVRES FROM EVL_RESULTADO a\n"
                + "INNER JOIN (\n"
                + "SELECT IDE_EVEVA from EVL_EVALUADORES\n"
                + ")b on a.IDE_EVEVA=b.IDE_EVEVA\n"
                + "INNER JOIN (\n"
                + "SELECT IDE_EVFAE,DETALLE_EVFAE from EVL_FACTOR_EVALUACION\n"
                + ")c on c.IDE_EVFAE=a.IDE_EVFAE WHERE b.IDE_EVEVA =-1 ");
        strPathReporte = utilitario.getURL() + "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf";
    }

    public void seleccionarPeriodo(SelectEvent evt) {
        if (objperiodo != null) {
            lisevaluadores = utilitario.getConexion().consultar("SELECT a.IDE_EVEVA,c.EMPLEADO,a.FECHA_EVEVA,a.FECHA_EVALUACION_EVEVA,a.POR_PESO_EVEVA FROM EVL_EVALUADORES a\n"
                    + "INNER JOIN(\n"
                    + "SELECT IDE_GEEDP,IDE_GTEMP FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR\n"
                    + ")b on b.IDE_GEEDP=a.IDE_GEEDP\n"
                    + "INNER JOIN (\n"
                    + "SELECT IDE_GTEMP,APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP ||' '|| PRIMER_NOMBRE_GTEMP ||' '|| SEGUNDO_NOMBRE_GTEMP as EMPLEADO FROM GTH_EMPLEADO\n"
                    + ")c on c.IDE_GTEMP=b.IDE_GTEMP WHERE IDE_EVDES =" + ((Object[]) objperiodo)[0] + " ORDER BY IDE_EVEVA");
        }
    }

    public void seleccionarEvaluador(SelectEvent evt) {
        if (objevaluador != null) {
            lisresultado = utilitario.getConexion().consultar("SELECT a.IDE_EVRES,c.DETALLE_EVFAE,a.RESULTADO_EVRES,a.PESO_FACTOR_EVRES FROM EVL_RESULTADO a\n"
                    + "INNER JOIN (\n"
                    + "SELECT IDE_EVEVA from EVL_EVALUADORES\n"
                    + ")b on a.IDE_EVEVA=b.IDE_EVEVA\n"
                    + "INNER JOIN (\n"
                    + "SELECT IDE_EVFAE,DETALLE_EVFAE from EVL_FACTOR_EVALUACION\n"
                    + ")c on c.IDE_EVFAE=a.IDE_EVFAE WHERE b.IDE_EVEVA =" + ((Object[]) objevaluador)[0] + " ");
        }
    }

    public void visualizarReporte() {
        if (objperiodo != null) {
            GenerarReporte ger = new GenerarReporte();
            Map map_parametros = new HashMap();
            map_parametros.put("ide_evdes", Integer.parseInt(String.valueOf(((Object[]) objperiodo)[0])));
            map_parametros.put("titulo", "DETALLE RESULTADO EVALUACI�N");
            ger.generar(map_parametros, "/reportes/rep_evaluacion/rep_evaluacion_detalle.jasper");
        }
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public List getLisperiodos() {
        return lisperiodos;
    }

    public void setLisperiodos(List lisperiodos) {
        this.lisperiodos = lisperiodos;
    }

    public List getLisevaluadores() {
        return lisevaluadores;
    }

    public void setLisevaluadores(List lisevaluadores) {
        this.lisevaluadores = lisevaluadores;
    }

    public List getLisresultado() {
        return lisresultado;
    }

    public void setLisresultado(List lisresultado) {
        this.lisresultado = lisresultado;
    }

    public Object getObjperiodo() {
        return objperiodo;
    }

    public void setObjperiodo(Object objperiodo) {
        this.objperiodo = objperiodo;
    }

    public Object getObjevaluador() {
        return objevaluador;
    }

    public void setObjevaluador(Object objevaluador) {
        this.objevaluador = objevaluador;
    }

    public String getStrPathReporte() {
        return strPathReporte;
    }

    public void setStrPathReporte(String strPathReporte) {
        this.strPathReporte = strPathReporte;
    }
}

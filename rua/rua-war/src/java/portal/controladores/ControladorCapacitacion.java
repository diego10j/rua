/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class ControladorCapacitacion {

    private String strOpcion = "1";
    private Utilitario utilitario = new Utilitario();
    private List liscapacitacionesEmpleado;

    @PostConstruct
    public void cargarDatos() {
        liscapacitacionesEmpleado = utilitario.getConexion().consultar("SELECT DETALLE_GEINS,DETALLE_gedip,DETALLE_GTCEM,DETALLE_getpr,TIPO_GTCEM,INSTRUCTOR_GTCEM,FECHA_GTCEM,DURACION_GTCEM FROM GTH_CAPACITACION_EMPLEADO cap\n"
                + "left join GEN_INSTITUCION ins on CAP.IDE_GEINS=INS.IDE_GEINS\n"
                + "LEFT JOIN GEN_DIVISION_POLITICA div on cap.ide_gedip=div.ide_gedip\n"
                + "left join GEN_TIPO_PERIODO per on cap.ide_getpr=per.ide_getpr\n"
                + "WHERE IDE_GTEMP=" + utilitario.getVariable("IDE_GTEMP"));
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public List getLiscapacitacionesEmpleado() {
        return liscapacitacionesEmpleado;
    }

    public void setLiscapacitacionesEmpleado(List liscapacitacionesEmpleado) {
        this.liscapacitacionesEmpleado = liscapacitacionesEmpleado;
    }
}

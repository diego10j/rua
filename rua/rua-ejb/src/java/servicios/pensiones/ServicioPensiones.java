/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.pensiones;

import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author djacome
 */
@Stateless

public class ServicioPensiones extends ServicioBase {

    /**
     * Listado de Alumnos
     *
     * @return
     */
    public String getSqlAlumnos() {
        return "SELECT a.ide_geper,a.codigo_geper as CODIGO,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES,a.direccion_geper AS DIRECCION,a.telefono_geper AS TELEFONO,b.nom_geper  as REPRESENTANTE,a.ide_vgecl AS ACTIVO FROM gen_persona a left join  gen_persona b on a.rep_ide_geper = b.ide_geper where a.ide_vgtcl=1 and a.nivel_geper='HIJO' order by a.nom_geper";
    }

    public String getSqlComboAlumnos() {
        return "SELECT a.ide_geper,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES,a.codigo_geper as CODIGO FROM gen_persona a where a.ide_vgtcl=1 and a.nivel_geper='HIJO' order by a.nom_geper";
    }
 
    /**
     *
     * @param cedula_alumno
     * @return
     */
    public String getFacturasAlumno(String cedula_alumno) {
        return "select a.ide_cccfa, secuencial_cccfa,ide_cnccc,a.ide_ccefa,nombre_sresc as nombre_ccefa, fecha_emisi_cccfa,(select numero_cncre from con_cabece_retenc where ide_cncre=a.ide_cncre)as NUM_RETENCION,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,descuento_cccfa as DESCUENTO,valor_iva_cccfa,total_cccfa, "
                + "claveacceso_srcom as CLAVE_ACCESO, fecha_trans_cccfa,a.ide_cncre,d.ide_srcom,a.ide_geper,direccion_cccfa,orden_compra_cccfa AS NUM_REFERENCIA  "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "left join con_deta_forma_pago x on a.ide_cndfp1=x.ide_cndfp "
                + "where orden_compra_cccfa='" + cedula_alumno + "' "
                + "AND a.ide_ccefa =0" 
                + " ORDER BY secuencial_cccfa desc,ide_cccfa desc";
    }

    /**
     *
     * @param ide_geper Alumno
     * @param fechaInicio Fecha Inicio
     * @param fechaFin Facha Fin
     * @return
     */
    public String getSqlFacturasAlumno(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT * FROM cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + " where a.ide_geper=" + ide_geper + " fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' ";
    }

}

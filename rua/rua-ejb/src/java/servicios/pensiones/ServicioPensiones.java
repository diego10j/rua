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

    public String getSqlAlumnos() {
        return "SELECT a.ide_geper,a.codigo_geper as CODIGO,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES,a.direccion_geper AS DIRECCION,a.telefono_geper AS TELEFONO,b.nom_geper  as REPRESENTANTE,a.ide_vgecl AS ACTIVO FROM gen_persona a left join  gen_persona b on a.rep_ide_geper = b.ide_geper where a.ide_vgtcl=1 and a.nivel_geper='HIJO' order by a.nom_geper";
    }

}

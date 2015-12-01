package pkg_general;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_unificar extends Pantalla {

    private Division div_division = new Division();
    private AutoCompletar aut_persona_a_reemplazar = new AutoCompletar();
    private AutoCompletar aut_persona_unifica = new AutoCompletar();
    private Boton bot_unificar_persona = new Boton();

    public pre_unificar() {

        aut_persona_a_reemplazar.setId("aut_persona_a_reemplazar");
        aut_persona_a_reemplazar.setAutoCompletar("select ide_geper,identificac_geper,nom_geper from gen_persona where nivel_geper='HIJO' and ide_empr=" + utilitario.getVariable("ide_empr"));

        aut_persona_unifica.setId("aut_persona_unifica");
        aut_persona_unifica.setAutoCompletar("select ide_geper,identificac_geper,nom_geper from gen_persona where nivel_geper='HIJO' and ide_empr=" + utilitario.getVariable("ide_empr"));

        bot_unificar_persona.setId("bot_unificar_persona");
        bot_unificar_persona.setValue("ACEPTAR");
        bot_unificar_persona.setMetodo("aceptarUnificarPersona");

        Grid gri_inificar = new Grid();
        gri_inificar.setColumns(2);
        gri_inificar.getChildren().add(new Etiqueta("PERSONA A REEMPLAZAR"));
        gri_inificar.getChildren().add(aut_persona_a_reemplazar);
        gri_inificar.getChildren().add(new Etiqueta("PERSONA UNIFICA"));
        gri_inificar.getChildren().add(aut_persona_unifica);
        gri_inificar.setFooter(bot_unificar_persona);

        div_division.setId("div_division");
        div_division.dividir1(gri_inificar);
        agregarComponente(div_division);
    }

    public void aceptarUnificarPersona() {
        if (aut_persona_a_reemplazar.getValor() != null) {
            if (aut_persona_unifica.getValor() != null) {
                System.out.println("Si ingresa al boto aceptar");
                System.out.println("valor de la persona a REEMPLAZAR: " + aut_persona_a_reemplazar.getValor());
                System.out.println("valor de la persona UNIFICA: " + aut_persona_unifica.getValor());
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("con_cab_comp_cont"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("cxc_guia"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("act_asignacion_activo"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("con_det_conf_asie"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("cxc_cabece_factura"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("cxc_cabece_transa"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("cxp_cabece_factur"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("cxp_cabece_transa"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("cxp_cabecera_nota"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("cxp_datos_factura"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("inv_cab_comp_inve"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("iyp_cab_prestamo"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("iyp_certificado"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("act_activo_fijo"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("reh_carga"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("reh_empleados_por_carac"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("reh_empleados_rol"));
                utilitario.getConexion().agregarSqlPantalla(getSQLReemplazar("reh_historial_empleado"));
                utilitario.getConexion().agregarSqlPantalla("DELETE FROM gen_persona WHERE ide_geper="+aut_persona_a_reemplazar.getValor());
                guardarPantalla();
            } else {
                utilitario.agregarMensaje("No se puede Unificar la Persona", "Debe ingresar la persona a unificar");
            }
        } else {
            utilitario.agregarMensaje("No se puede Unificar la Persona", "Debe ingresar la persona a reemplazar");
        }
    }

    private String getSQLReemplazar(String Tabla) {
        String sqlReeemplazar = utilitario.getConexion().ejecutarSql("UPDATE " + Tabla + " SET ide_geper=" + aut_persona_unifica.getValor() + " WHERE ide_geper=" + aut_persona_a_reemplazar.getValor());
        return sqlReeemplazar;
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {

        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
    }

    public AutoCompletar getAut_persona_a_reemplazar() {
        return aut_persona_a_reemplazar;
    }

    public void setAut_persona_a_reemplazar(AutoCompletar aut_persona_a_reemplazar) {
        this.aut_persona_a_reemplazar = aut_persona_a_reemplazar;
    }

    public AutoCompletar getAut_persona_unifica() {
        return aut_persona_unifica;
    }

    public void setAut_persona_unifica(AutoCompletar aut_persona_unifica) {
        this.aut_persona_unifica = aut_persona_unifica;
    }
}

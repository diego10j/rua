/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.sistema.enums;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public enum EnumAccionesAuditoria {

    INGRESO_USUARIO("0"),
    /**
     * Sirve para cuando no se produce un acceso al sistema por parte de un
     * usuario
     */
    FALLO_INGRESO("1"),
    /**
     * Sirve para cuando se bloqueao a un usuario
     */
    BLOQUEA_USUARIO("2"),
    /**
     * Sirve para cuando se pone en estado inactivo a un usuario
     */
    DESACTIVA_USUARIO("3"),
    /**
     * Sirve para cuando se pone en estado activo a un usuario
     */
    ACTIVA_USUARIO("4"),
    /**
     * Sirve para cuando un usuario cambia su clave
     */
    CAMBIO_CLAVE("5"),
    /**
     * Sirve para cuando se resetea la clave de un usuario
     */
    RESETEO_CLAVE("6"),
    /**
     * Sirve para cuando a un usuario se le expira el tiempo de su session
     */
    CADUCO_SESSION("7"),
    /**
     * Sirve para cuando un usuario sale del sistema
     */
    SALIO_USUARIO("8"),
    /**
     * Sirve para cuando se crea un usuario en el sistema
     */
    CREAR_USUARIO("9"),
    /**
     * Sirve para cuando se desbloquea a un usuario
     */
    DESBLOQUEA_USUARIO("10"),
    /**
     * Sirve para cuando el usuario Abre una Pantalla
     */
    ABRIO_PANTALLA("11");

    private final String codigo;

    private EnumAccionesAuditoria(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

}

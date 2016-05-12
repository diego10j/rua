/**
 * EJB que contiene los metodos necesarios para el módulo de seguridad
 *
 * Creada Mayo 29 del 2013
 *
 * @author Diego Jácome
 * @since 1.0
 */
package servicios.sistema;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Encriptar;
import java.util.Date;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

@Stateless
public class ServicioSeguridad {

    private final Utilitario utilitario = new Utilitario();
    private final Encriptar encriptar = new Encriptar();

    /**
     * Sirve para cuando un usuario accede exitosamente al sistema
     */
    public final static String P_SIS_INGRESO_USUARIO = "0";
    /**
     * Sirve para cuando no se produce un acceso al sistema por parte de un
     * usuario
     */
    public final static String P_SIS_FALLO_INGRESO = "1";
    /**
     * Sirve para cuando se bloqueao a un usuario
     */
    public final static String P_SIS_BLOQUEA_USUARIO = "2";
    /**
     * Sirve para cuando se pone en estado inactivo a un usuario
     */
    public final static String P_SIS_DESACTIVA_USUARIO = "3";
    /**
     * Sirve para cuando se pone en estado activo a un usuario
     */
    public final static String P_SIS_ACTIVA_USUARIO = "4";
    /**
     * Sirve para cuando un usuario cambia su clave
     */
    public final static String P_SIS_CAMBIO_CLAVE = "5";
    /**
     * Sirve para cuando se resetea la clave de un usuario
     */
    public final static String P_SIS_RESETEO_CLAVE = "6";
    /**
     * Sirve para cuando a un usuario se le expira el tiempo de su session
     */
    public final static String P_SIS_CADUCO_SESSION = "7";
    /**
     * Sirve para cuando un usuario sale del sistema
     */
    public final static String P_SIS_SALIO_USUARIO = "8";
    /**
     * Sirve para cuando se crea un usuario en el sistema
     */
    public final static String P_SIS_CREAR_USUARIO = "9";
    /**
     * Sirve para cuando se desbloquea a un usuario
     */
    public final static String P_SIS_DESBLOQUEA_USUARIO = "10";
    /**
     * Sirve para cuando el usuario Abre una Pantalla
     */
    public final static String P_SIS_ABRIO_PANTALLA = "11";

    /**
     * Forma la sentencia sql para el insert en la tabla de auditoria de acceso,
     * sirve para cuando un usuario realiza alguna accion en el sistema
     *
     * @param ide_usua Clave primaria del usuario
     * @param accion Clave primaria de la accion que va a realizar
     * @param detalle Detalle de la accion
     * @return la sentencia sql
     */
    public String crearSQLAuditoriaAcceso(String ide_usua, String accion,
            String detalle) {
        // Forma el sql para el acceso de auditoria
        if (detalle == null || detalle.isEmpty()) {
            detalle = "NULL";
        } else {
            detalle = "'" + detalle + "'";
        }
        String str_sql = "INSERT INTO sis_auditoria_acceso (ide_usua,ide_acau,fecha_auac,hora_auac,ip_auac,sis_ide_usua,detalle_auac,id_session_auac,fin_auac) "
                + "VALUES(" + ide_usua + "," + accion + ","
                + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) + "," + utilitario.getFormatoHoraSQL(utilitario.getHoraActual())
                + ",'" + utilitario.getIp() + "'," + utilitario.getVariable("IDE_USUA")
                + "," + detalle + ",'" + utilitario.getIdSession() + "',false)";
        return str_sql;
    }

    /**
     * Forma la sentencia sql para el insert en la tabla de historial de
     * auditoria de usuarios
     *
     * @param ide_usua Clave primaria del usuario
     * @param clave la clave sin encriptar
     * @return la sentencia sql
     */
    public String crearSQLHistorialClave(String ide_usua, String clave) {
        String str_sql = "INSERT INTO sis_historial_claves (ide_usua,clave_hicl,fecha_hicl,hora_hicl) VALUES("
                + ide_usua + ",'" + encriptar.getEncriptar(clave) + "',"
                + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) + ","
                + utilitario.getFormatoHoraSQL(utilitario.getHoraActual()) + ")";
        return str_sql;
    }

    /**
     * Resetea la clave de un usuario
     *
     * @param ide_usua Clave primaria del usuario
     * @param clave Nueva clave
     */
    public void resetearClave(String ide_usua, String clave) {
        // Resete la Clave
        String str_sql = "UPDATE sis_usuario_clave SET CLAVE_USCL='"
                + encriptar.getEncriptar(clave) + "' WHERE IDE_USUA="
                + ide_usua + " AND ACTIVO_USCL=true";
        // Actualiza el campo para que a lo que ngrese al sistema le obligue a
        // cambiar la clave
        utilitario.getConexion().agregarSql(str_sql);
        str_sql = "UPDATE sis_usuario SET cambia_clave_usua=true WHERE IDE_USUA="
                + ide_usua;
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para reseteo de clavel
        utilitario.getConexion().agregarSql(
                crearSQLAuditoriaAcceso(ide_usua, P_SIS_RESETEO_CLAVE,
                        "Reseteo de clave"));
        utilitario.getConexion().agregarSql(
                crearSQLHistorialClave(ide_usua, clave));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Activa un usuario
     *
     * @param ide_usua Clave primaria del usuario
     */
    public void activarUsuario(String ide_usua) {
        // Activa estado usuario
        String str_sql = "UPDATE sis_usuario SET activo_usua=true WHERE IDE_USUA="
                + ide_usua + "";
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para activacion del usuario
        utilitario.getConexion().agregarSql(
                crearSQLAuditoriaAcceso(ide_usua, P_SIS_ACTIVA_USUARIO,
                        "Activar usuario"));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Desactiva un usuario
     *
     * @param ide_usua Clave primaria del usuario
     */
    public void desactivarUsuario(String ide_usua) {
        // Desactiva estado usuario
        String str_sql = "UPDATE sis_usuario SET activo_usua=false WHERE IDE_USUA="
                + ide_usua + "";
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para desactivacion del usuario
        utilitario.getConexion().agregarSql(
                crearSQLAuditoriaAcceso(ide_usua, P_SIS_DESACTIVA_USUARIO,
                        "Desactivar usuario"));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Bloquea un usuario
     *
     * @param ide_usua Clave primaria del usuario
     * @param detalle Detalle de por que se bloquea el usuario
     */
    public void bloquearUsuario(String ide_usua, String detalle) {
        // Bloquea el estado
        String str_sql = "UPDATE sis_usuario SET bloqueado_usua=true WHERE IDE_USUA="
                + ide_usua + "";
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para bloqueo
        utilitario.getConexion().agregarSql(
                crearSQLAuditoriaAcceso(ide_usua, P_SIS_BLOQUEA_USUARIO,
                        detalle));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Desbloquea un usuario
     *
     * @param ide_usua Clave primaria del usuario
     */
    public void desbloquearUsuario(String ide_usua) {
        // Bloquea el estado
        String str_sql = "UPDATE sis_usuario SET bloqueado_usua=false WHERE IDE_USUA="
                + ide_usua + "";
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para bloqueo
        utilitario.getConexion().agregarSql(
                crearSQLAuditoriaAcceso(ide_usua, P_SIS_DESBLOQUEA_USUARIO,
                        "Desbloquear Usuario"));
        //pongo fin a todos sus seciones no finalizadas

        str_sql = "update SIS_AUDITORIA_ACCESO set  FIN_AUAC=true WHERE IDE_USUA="
                + ide_usua + "";
        utilitario.getConexion().agregarSql(str_sql);

        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Ingresar al sistema
     *
     * @param nick Nick de login del usuario
     * @param clave Clave del usuario
     * @return mensaje de error si el fallo alguna validación, caso contrario
     * retorna vacio
     */
    public String ingresar(String nick, String clave) {
        String str_mensaje = "";
        TablaGenerica tab_usuario = utilitario
                .consultar("SELECT * FROM SIS_USUARIO WHERE NICK_USUA='" + nick + "' AND ACTIVO_USUA=true");
        if (tab_usuario.getTotalFilas() > 0) {
            utilitario.crearVariable("IDE_USUA", tab_usuario.getValor("IDE_USUA"));
            // Verifico que el usuario no este bloqueado
            if (tab_usuario.getValor("BLOQUEADO_USUA") == null || tab_usuario.getValor("BLOQUEADO_USUA").equals("false") || tab_usuario.getValor("BLOQUEADO_USUA").equals("0")) {

                // Verifica que el usuario no este caducoado
                if (tab_usuario.getValor("fecha_caduc_usua") != null) {
                    if (utilitario.isFechaMayor(utilitario.getDate(), utilitario.getFecha(tab_usuario.getValor("fecha_caduc_usua")))) {
                        str_mensaje = "La vigencia de su usuario a caducado, contactese con el administrador del sistema";
                        return str_mensaje;
                    }
                }
                // Verifica que el usuario no haya iniciado en otra maquina
                if (isUsuarioLogeado(tab_usuario.getValor("IDE_USUA")) == false) {

                    // Verifica intentos fallidos vs intentos maximon
                    int int_maximo_intentos = getNumeroIntentosAcceso();
                    int int_intentos = getIntentosAcceso(
                            tab_usuario.getValor("IDE_USUA"), utilitario.getFechaActual());
                    if (int_intentos < int_maximo_intentos) {
                        // Verifica que exista una clave activa para el usuario
                        TablaGenerica tab_clave = getClaveActivaUusario(tab_usuario.getValor("IDE_USUA"));
                        if (tab_clave.getTotalFilas() > 0) {
                            // Verifica que la clave no haya caducado
                            if (tab_clave.getValor("fecha_vence_uscl") != null) {
                                if (utilitario.isFechaMayor(utilitario
                                        .getDate(), utilitario
                                        .getFecha(tab_clave
                                                .getValor("fecha_vence_uscl")))) {
                                    str_mensaje = "La vigencia de su clave a caducado, contactese con el administrador del sistema";
                                    return str_mensaje;
                                }
                            }
                            // Verifico que la clave sea correcta
                            if (encriptar.getEncriptar(clave).equals(
                                    tab_clave.getValor("CLAVE_USCL"))) {
                                try {
                                    if (isPerfilActivo(tab_usuario.getValor("IDE_PERF"))) {
                                        utilitario.crearVariable("IDE_PERF", tab_usuario.getValor("IDE_PERF"));
                                        utilitario.crearVariable("TEMA", tab_usuario.getValor("TEMA_USUA"));
                                        solicitarCambiarClave(tab_usuario.getValor("IDE_USUA"));
                                        // Guarda en auditoria de acceso si no tiene
                                        // q cambiar la clave el usuario
                                        if (isCambiarClave(tab_usuario.getValor("IDE_USUA")) == false) {
                                            //si tiene intentos fallidos de acceso los reseteo poniendoles fin
                                            utilitario.getConexion().agregarSql("UPDATE sis_auditoria_acceso set fin_auac=true WHERE ide_usua=" + tab_usuario.getValor("IDE_USUA") + " and fecha_auac=" + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) + " and ip_auac='" + utilitario.getIp() + "'");
                                            utilitario.getConexion().agregarSql(crearSQLAuditoriaAcceso(tab_usuario.getValor("IDE_USUA"), P_SIS_INGRESO_USUARIO, "Ingresó al sistema"));
                                            utilitario.getConexion().ejecutarListaSql();
                                        }
                                    } else {
                                        str_mensaje = "El perfil de su usuario esta desactivado, contactese con el administrador del sistema";
                                    }

                                } catch (Exception e) {
                                }
                            } else {
                                // Fallo el ingreso
                                utilitario.getConexion().ejecutarSql(crearSQLAuditoriaAcceso(tab_usuario.getValor("IDE_USUA"), P_SIS_FALLO_INGRESO, "Fallo ingreso intento : " + int_intentos));
                                str_mensaje = "La clave ingresada es incorrecta";
                                int_intentos++;
                                if (int_intentos == int_maximo_intentos) {
                                    str_mensaje = " A sobrepasado el número máximo de intentos para acceder al sistema, se bloqueara el usuario, contáctese con el administrador del sistema para desbloquearlo";
                                    bloquearUsuario(tab_usuario.getValor("IDE_USUA"), "Bloquear usuario por sobrepasar el numero maximo de intentos : " + int_maximo_intentos);
                                }
                            }
                        } else {
                            str_mensaje = "El usuario no tiene una clave activa contactese con el administrador del sistema";
                        }
                    } else {
                        // bloqueo el usuario por sobrepasar el numero maximo de
                        // intentos permitidos
                        str_mensaje = "A sobrepasado el número máximo de intentos para acceder al sistema, se bloqueara el usuario, contáctese con el administrador del sistema para desbloquearlo";
                        bloquearUsuario(tab_usuario.getValor("IDE_USUA"), "Bloquear usuario por sobrepasar el numero maximo de intentos : " + int_maximo_intentos);
                    }
                } else {
                    // Guarda intento fallido al acceder se ya se encuentra en
                    // ssesion
                    utilitario.getConexion().ejecutarSql(crearSQLAuditoriaAcceso(tab_usuario.getValor("IDE_USUA"), P_SIS_FALLO_INGRESO, "Fallo ingreso, el usuario tiene sessión iniciada en otra máquina "));

                    str_mensaje = "El usuario ingresado tiene sessión iniciada en otra máquina,contactese con el administrador del sistema ";
                }
            } else {
                str_mensaje = "El usuario esta bloqueado contactese con el administrador del sistema";
            }
        } else {
            str_mensaje = "El nombre del usuario es incorrecto o no está activo";
        }
        return str_mensaje;
    }

    /**
     * Salir del sistema
     *
     * @param ide_usua Clave primaria del usuario
     */
    public void salir(String ide_usua) {
        String str_sql = "UPDATE SIS_AUDITORIA_ACCESO SET FIN_AUAC=true WHERE ID_SESSION_AUAC='" + utilitario.getIdSession() + "' AND IDE_USUA="
                + utilitario.getVariable("IDE_USUA");
        utilitario.getConexion().agregarSql(str_sql);
        utilitario.getConexion().agregarSql(crearSQLAuditoriaAcceso(ide_usua, P_SIS_SALIO_USUARIO, "Salió del sistema"));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Caducó la session del usuario
     *
     * @param ide_usua Clave primaria del usuario
     */
    public void caduco(String ide_usua) {
        utilitario.getConexion().ejecutarSql(crearSQLAuditoriaAcceso(ide_usua, P_SIS_CADUCO_SESSION, "Caducó la sessión"));
    }

    /**
     * Verifica si un usuario tiene una clave activa
     *
     * @param ide_usua Clave primaria del usuario
     * @return
     */
    public boolean isClaveActiva(String ide_usua) {
        TablaGenerica tab_clave = getClaveActivaUusario(ide_usua);
        return tab_clave.isEmpty();
    }

    /**
     * Método que busca si la clave de un usuario a una fecha determinada esta
     * vigente o esta caducada
     *
     * @param ide_usua es la identificador del usuario
     * @param fecha es la fecha a la cual quiero comparar la fecha de caducidad
     * @return true si la clave esta vigente y false si la clave ya caduco
     *
     */
    public boolean isClaveVigente(String ide_usua, String fecha) {
        // Si la clave de un usuario a una fecha determinada esta caducada
        TablaGenerica tab_clave = getClaveActivaUusario(ide_usua);
        if (tab_clave.isEmpty() == false) {
            // Verifica que la clave no haya caducado
            if (tab_clave.getValor("fecha_vence_uscl") != null) {
                if (utilitario.isFechaMayor(utilitario.getFecha(fecha),
                        utilitario.getFecha(tab_clave
                                .getValor("fecha_vence_uscl")))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Método que busca el numero maximo de intentos para poder acceder al
     * sistema
     *
     * @return Número entero de intentos permitidos.
     */
    public int getNumeroIntentosAcceso() {
        // Retorna el numero de fallos para el acceso al sistema
        int int_intentos = 3; // valor por defecto
        TablaGenerica tab_intentos = getReglas();
        if (tab_intentos.isEmpty() == false) {
            if (tab_intentos.getValor("intentos_recl") != null) {
                try {
                    int_intentos = Integer.parseInt(tab_intentos.getValor("intentos_recl"));
                } catch (Exception e) {
                }
            }
        }
        return int_intentos;
    }

    /**
     * Consulta el número de intentos fallidos al tratar de acceder al sistema
     *
     * @param ide_usua Clave primaria del usuario
     * @param fecha Fecha de consulta
     * @return
     */
    public int getIntentosAcceso(String ide_usua, String fecha) {
        // Retorna el numero de intentos para acceder al sistema de un usuario
        // en un dia y por ip
        int int_intentos = 0;
        TablaGenerica tab_intentos = utilitario
                .consultar("select ide_usua,count(*) as intentos from sis_auditoria_acceso where ide_usua="
                        + ide_usua + " " + "and fecha_auac=" + utilitario.getFormatoFechaSQL(fecha)
                        + " and ip_auac='" + utilitario.getIp() + "' and fin_auac=false and ide_acau=" + P_SIS_FALLO_INGRESO
                        + " group by ide_usua");
        if (tab_intentos.isEmpty() == false) {
            try {
                int_intentos = Integer.parseInt(tab_intentos.getValor("intentos"));
            } catch (Exception e) {
            }
            // Cuenta los desbloqueos para otra ves encerar el numero de
            // intentos despues de q se desbloque el usuario
            TablaGenerica tab_desbloqueos = utilitario.consultar("select ide_usua,count(*) as desbloqueos from sis_auditoria_acceso where ide_usua="
                    + ide_usua + " " + "and fecha_auac=" + utilitario.getFormatoFechaSQL(fecha) + " and ip_auac='"
                    + utilitario.getIp() + "' and ide_acau=" + P_SIS_DESBLOQUEA_USUARIO + " group by ide_usua");
            if (tab_desbloqueos.isEmpty() == false) {
                int int_num_desbloqueos = 0;
                try {
                    int_num_desbloqueos = Integer.parseInt(tab_desbloqueos.getValor("desbloqueos"));
                } catch (Exception e) {
                }
                if (int_num_desbloqueos > 0) {
                    int_num_desbloqueos = int_num_desbloqueos * getNumeroIntentosAcceso();
                    int_intentos = int_intentos - int_num_desbloqueos;
                }
            }
        }
        return int_intentos;
    }

    public void cambiarClave(String ide_usua, String clave) {
        String str_sql = "UPDATE sis_usuario_clave SET clave_uscl='" + encriptar.getEncriptar(clave) + "' WHERE IDE_USUA="
                + ide_usua + " and activo_uscl=true";
        utilitario.getConexion().agregarSql(str_sql);
        str_sql = "UPDATE sis_usuario SET cambia_clave_usua=false WHERE IDE_USUA=" + ide_usua;
        utilitario.getConexion().agregarSql(str_sql);
        // historial de claves de usuario
        utilitario.getConexion().agregarSql(crearSQLHistorialClave(ide_usua, clave));
        // Auditoria de Cambio de clave solo si el usuario no es nuevo
        utilitario.getConexion().agregarSql(crearSQLAuditoriaAcceso(ide_usua, P_SIS_CAMBIO_CLAVE, "Cambió clave el usuario"));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Crea una nueva clave a un usuario
     *
     * @param ide_usua Clave primaria del usuario
     * @param clave Nueva Clave
     */
    public void crearClave(String ide_usua, String clave) {
        // Pone en estado inactivo a las contraseñas de la tabla 2 por que solo
        // puede estar una clave activa
        utilitario.getConexion().agregarSql("UPDATE sis_usuario_clave SET activo_uscl=false where IDE_USUA=" + ide_usua);
        // Guarda en historial de contraseñas
        utilitario.getConexion().agregarSql(crearSQLHistorialClave(ide_usua, clave));
        // Obliga al usuario a cambiar la calve
        String str_sql = "UPDATE sis_usuario SET cambia_clave_usua=true WHERE IDE_USUA=" + ide_usua;
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria de Cambio de clave solo si el usuario no es nuevo
        utilitario.getConexion().agregarSql(crearSQLAuditoriaAcceso(ide_usua, P_SIS_CAMBIO_CLAVE, "Cambió clave el administrador"));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Valida si una clave cumple con las reglas definidas en la tabla
     *
     * @param clave
     * @return Mensaje de error si fallo alguna validación, caso contrario
     * retorna vacio
     */
    public String getClaveValida(String clave) {
        // Valida si la clave cumple con las reglas minimas definidas
        String str_mensaje = "No se a definido reglas para las claves";
        TablaGenerica tab_regla = getReglas();
        if (tab_regla.isEmpty() == false) {
            int int_longitud_minima = 0;
            int int_num_carac_espe = 0;
            int int_num_mayus = 0;
            int int_num_minusc = 0;
            int int_num_numeros = 0;
            try {
                int_longitud_minima = Integer.parseInt(tab_regla
                        .getValor("longitud_minima_recl"));
            } catch (Exception e) {
            }
            try {
                int_num_carac_espe = Integer.parseInt(tab_regla
                        .getValor("num_carac_espe_recl"));
            } catch (Exception e) {
            }
            try {
                int_num_mayus = Integer.parseInt(tab_regla
                        .getValor("num_mayus_recl"));
            } catch (Exception e) {
            }
            try {
                int_num_minusc = Integer.parseInt(tab_regla
                        .getValor("num_minusc_recl"));
            } catch (Exception e) {
            }
            try {
                int_num_numeros = Integer.parseInt(tab_regla
                        .getValor("num_numeros_recl"));
            } catch (Exception e) {
            }
            // Cuento las reglas de la clave
            int int_con_longitud_minima = 0;
            int int_con_num_carac_espe = 0;
            int int_con_num_mayus = 0;
            int int_con_num_minusc = 0;
            int int_con_num_numeros = 0;
            for (int i = 0; i < clave.length(); i++) {
                char chr_actual = clave.charAt(i);
                int_con_longitud_minima++;
                if (Character.isLetterOrDigit(chr_actual)) {
                    if (Character.isLetter(chr_actual)) {
                        if (Character.isUpperCase(chr_actual)) {
                            int_con_num_mayus++;
                        } else {
                            int_con_num_minusc++;
                        }
                    } else {
                        int_con_num_numeros++;
                    }
                } else {
                    int_con_num_carac_espe++;
                }
            }
            // Comparo si cumple con todo
            if (int_con_longitud_minima < int_longitud_minima) {
                str_mensaje = "La clave ingresada no cumple con la regla de longitud mínima = "
                        + int_longitud_minima;
            } else {
                if (int_con_num_carac_espe < int_num_carac_espe) {
                    str_mensaje = "La clave ingresada no cumple con la regla de cantidad mínima de caracteres especiales = "
                            + int_num_carac_espe;
                } else {
                    if (int_con_num_mayus < int_num_mayus) {
                        str_mensaje = "La clave ingresada no cumple con la regla de cantidad mínima de letras mayúsculas = "
                                + int_num_mayus;
                    } else {
                        if (int_con_num_minusc < int_num_minusc) {
                            str_mensaje = "La clave ingresada no cumple con la regla de cantidad mínima de letras minúsculas = "
                                    + int_num_minusc;
                        } else {
                            if (int_con_num_numeros < int_num_numeros) {
                                str_mensaje = "La clave ingresada no cumple con la regla de cantidad mínima de caracteres numéricos = "
                                        + int_num_numeros;
                            } else {
                                str_mensaje = "";
                            }
                        }
                    }
                }
            }
        }
        return str_mensaje;
    }

    /**
     * Valida que la clave no exista en el historial
     *
     * @param ide_usua Clave primaria del usuario
     * @param clave
     * @return
     */
    public boolean isClaveNueva(String ide_usua, String clave) {
        //Busco cuantas claves anteriores valido, estan sis_reglas 
        TablaGenerica tab_reglas = getReglas();
        int int_num_antes = 0;
        try {
            int_num_antes = Integer.parseInt(tab_reglas.getValor("NUM_VALIDA_ANTERIOR_RECL"));
        } catch (Exception e) {
        }
        if (int_num_antes == 0) {
            //valido todas
            TablaGenerica tab_historia = utilitario.consultar("SELECT * FROM sis_historial_claves where ide_usua="
                    + ide_usua + " and clave_hicl='" + encriptar.getEncriptar(clave) + "'");
            return tab_historia.isEmpty();
        } else {
            TablaGenerica tab_historia = utilitario.consultar("SELECT * FROM sis_historial_claves where ide_usua="
                    + ide_usua + " order by FECHA_HICL desc");
            for (int i = 0; i < int_num_antes; i++) {
                if (tab_historia.getValor("CLAVE_HICL") != null && (tab_historia.getValor("CLAVE_HICL").equals(encriptar.getEncriptar(clave)))) {
                    return false;
                }
            }
            return true;
        }
    }

    public String getClaveUsuario(String ide_usua) {
        String str_clave = null;
        // Retorna la clave activa de un usuario
        TablaGenerica tab_clave = getClaveActivaUusario(ide_usua);
        if (tab_clave.isEmpty() == false) {
            str_clave = tab_clave.getValor("CLAVE_USCL");
        }
        return str_clave;
    }

    /**
     * Consulta si es necesario que el usuario cambie
     *
     * @param ide_usua
     * @return
     */
    public boolean isCambiarClave(String ide_usua) {
        // Si el cambia_clave esta en true
        TablaGenerica tab_usuario = utilitario
                .consultar("SELECT * FROM SIS_USUARIO WHERE IDE_USUA="
                        + ide_usua + " AND cambia_clave_usua=true");
        return !tab_usuario.isEmpty();
    }

    /**
     * Verifica si el usuario tiene que cambiar su clave de acuerdo a los
     * periodos de vigencia
     *
     * @param ide_usua Clave primaria del usuario
     */
    public void solicitarCambiarClave(String ide_usua) {
        // Obliga al usuario a cambiar su clave cada periodo de tiempoq tenga
        // asignado
        TablaGenerica tab_clave = getClaveActivaUusario(ide_usua);
        // Verifica si la clave activa del usuario tiene periodo de cambio de
        // contraseña
        if (tab_clave.isEmpty() == false) {
            int int_dias_cambio = getDiasPeriodoClave(tab_clave
                    .getValor("ide_pecl"));
            if (int_dias_cambio > 0) {
                // fecha de registro de la clave + los dias q tiene q hacer el
                // cambio
                Date dat_fecha_cambio = utilitario.sumarDiasFecha(utilitario
                        .getFecha(tab_clave.getValor("fecha_registro_uscl")),
                        int_dias_cambio);
                Date dat_fecha_actual = new Date();
                int int_dias_diferencia = utilitario.getDiferenciasDeFechas(
                        dat_fecha_cambio, dat_fecha_actual);
                if (int_dias_diferencia % int_dias_cambio == 0) {
                    // Busca si el usuario ya cambio la clave el dia actual
                    TablaGenerica tab_cambio = utilitario
                            .consultar("SELECT * FROM SIS_AUDITORIA_ACCESO WHERE IDE_ACAU="
                                    + P_SIS_CAMBIO_CLAVE
                                    + " AND FECHA_AUAC="
                                    + utilitario.getFormatoFechaSQL(utilitario
                                            .getFechaActual()));
                    if (tab_cambio.isEmpty()) {
                        // Debe cambiar la clave
                        String str_sql = "UPDATE sis_usuario SET cambia_clave_usua=true WHERE IDE_USUA="
                                + ide_usua;
                        utilitario.getConexion().ejecutarSql(str_sql);
                    }
                }
            }
        }
    }

    /**
     * Información de un usuario
     *
     * @param ide_usua Clave primaria del usuario
     * @return TablaGenerica con la información del usuario
     */
    public TablaGenerica getUsuario(String ide_usua) {
        return utilitario.consultar("SELECT * FROM SIS_USUARIO WHERE IDE_USUA="
                + ide_usua);
    }

    /**
     * Información de la clave activa de un usuario
     *
     * @param ide_usua Clave primaria del usuario
     * @return
     */
    public TablaGenerica getClaveActivaUusario(String ide_usua) {
        return utilitario
                .consultar("SELECT * FROM SIS_USUARIO_CLAVE WHERE IDE_USUA="
                        + ide_usua + "  AND ACTIVO_USCL=true");
    }

    /**
     * Busca el número de días de un periodo de vigencia de claves
     *
     * @param ide_pecl Clave primaria del período de claves
     * @return el numero de dias del periodo de cambio de clave
     */
    public int getDiasPeriodoClave(String ide_pecl) {
        //
        int int_dias = 0;
        if (ide_pecl != null) {
            TablaGenerica tab_periodos = utilitario
                    .consultar("SELECT * FROM sis_periodo_clave WHERE ide_pecl="
                            + ide_pecl);
            if (tab_periodos.isEmpty() == false) {
                if (tab_periodos.getValor("num_dias") != null) {
                    try {
                        int_dias = Integer.parseInt(tab_periodos
                                .getValor("num_dias"));
                    } catch (Exception e) {
                    }
                }
            }
        }
        return int_dias;
    }

    /**
     * Consulta las reglas de clave de la empresa
     *
     * @return
     */
    public TablaGenerica getReglas() {

        return utilitario
                .consultar("SELECT * FROM sis_reglas_clave where ide_empr="
                        + utilitario.getPropiedad("ide_empr"));
    }

    /**
     * Busca la longitud mínima para el campo nick del login
     *
     * @return
     */
    public int getLongitudMinimaLogin() {
        // retorna el tamaño maximo para el login de un usuario
        int int_longitud = 0;
        TablaGenerica tab_intentos = getReglas();
        if (tab_intentos.isEmpty() == false) {
            if (tab_intentos.getValor("longitud_login_recl") != null) {
                try {
                    int_longitud = Integer.parseInt(tab_intentos
                            .getValor("longitud_login_recl"));
                } catch (Exception e) {
                }
            }
        }
        return int_longitud;
    }

    /**
     * Borra toda la auditoria de transacciones y de acceso
     */
    public void borrarAuditoria() {
        String str_sql = "DELETE FROM SIS_AUDITORIA";
        utilitario.getConexion().agregarSql(str_sql);
        str_sql = "DELETE FROM SIS_AUDITORIA_ACCESO";
        utilitario.getConexion().agregarSql(str_sql);
        if (utilitario.getConexion().NOMBRE_MARCA_BASE
                .equalsIgnoreCase("oracle")) {
            // Reinicio los sequence de oracle para la auditoria
            str_sql = "DROP SEQUENCE SEQ_AUDI_ACCESO";
            utilitario.getConexion().agregarSql(str_sql);
            str_sql = "CREATE SEQUENCE SEQ_AUDI_ACCESO START WITH 1 INCREMENT BY 1";
            utilitario.getConexion().agregarSql(str_sql);
            str_sql = "DROP SEQUENCE SEQ_AUDITORIA";
            utilitario.getConexion().agregarSql(str_sql);
            str_sql = "CREATE SEQUENCE SEQ_AUDITORIA START WITH 1 INCREMENT BY 1";
            utilitario.getConexion().agregarSql(str_sql);
        }
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Busca si el usuario ya se encuentra logeado en una máquina
     *
     * @param ide_usua Clave primaria del usuario
     * @return
     */
    public boolean isUsuarioLogeado(String ide_usua) {
        // Busca si el usuario no tiene una session activa en otra ip
        boolean boo_logeado = false;
        // Busco en auditoria acceso los ingresos q se encuentran activos por
        // fecha,ip,usuario y estado fin de session = false
        TablaGenerica tab_logeo = utilitario
                .consultar("SELECT * FROM SIS_AUDITORIA_ACCESO WHERE IDE_USUA="
                        + ide_usua
                        + " AND FECHA_AUAC="
                        + utilitario.getFormatoFechaSQL(utilitario
                                .getFechaActual())
                        + " AND FIN_AUAC=false AND IDE_ACAU="
                        + P_SIS_INGRESO_USUARIO);

        if (!tab_logeo.isEmpty()) {
            // Comparo si la ip del cliente con las de la tabla
            String str_ip = utilitario.getIp();
            for (int i = 0; i < tab_logeo.getTotalFilas(); i++) {
                if (!tab_logeo.getValor(i, "IP_AUAC").equalsIgnoreCase(str_ip)) {
                    // ya tiene una session en otra ip
                    boo_logeado = true;
                    break;
                }
            }
        }
        return boo_logeado;
    }

    /**
     * Desconecta ausuario(s) que no tengan finalizada una sessión iniciada
     *
     * @param ide_auditoria Clave o claves primarias de la tabla de auditoria
     * acceso
     */
    public void desconectarUsuarios(String ide_auditoria) {
        // Desconecta a los usuarios recibe los ide_auac
        String str_sql = "UPDATE SIS_AUDITORIA_ACCESO  SET FIN_AUAC=true where IDE_AUAC IN("
                + ide_auditoria + ")";
        utilitario.getConexion().ejecutarSql(str_sql);

    }

    /**
     * Valida que el perfil del usuario este activo
     *
     * @param IDE_PERF
     */
    public boolean isPerfilActivo(String IDE_PERF) {
        TablaGenerica tab_perf = utilitario.consultar("SELECT * FROM SIS_PERFIL WHERE IDE_PERF=" + IDE_PERF);
        if (tab_perf.getValor("activo_perf") != null && (tab_perf.getValor("activo_perf").equals("true") || tab_perf.getValor("activo_perf").equals("1"))) {
            if (tab_perf.getValor("perm_util_perf") != null && (tab_perf.getValor("perm_util_perf").equals("true") || tab_perf.getValor("perm_util_perf").equals("1"))) {
                utilitario.crearVariable("PERM_UTIL_PERF", "true");
            } else {
                utilitario.crearVariable("PERM_UTIL_PERF", "false");
            }
            return true;
        }
        return false;
    }

    public String getSqlUsuariosConectados() {
        return "SELECT IDE_AUAC,NOM_USUA,IP_AUAC,FECHA_AUAC,HORA_AUAC FROM SIS_AUDITORIA_ACCESO au "
                + "INNER JOIN SIS_USUARIO us on au.IDE_USUA = us.IDE_USUA "
                + "WHERE IDE_ACAU="
                + P_SIS_INGRESO_USUARIO
                + " AND FIN_AUAC=false AND IDE_AUAC=-1";

    }

       public String getSqlUsuarios() {
        return "SELECT IDE_USUA,NOM_USUA,nick_usua from SIS_USUARIO order by NOM_USUA";

    }
    
    public TablaGenerica getUltimoAccesoUsuario(String ide_usua) {
        return utilitario.consultar("select * from sis_auditoria_acceso where ide_usua=" + ide_usua + " and ide_acau=" + P_SIS_INGRESO_USUARIO + " \n"
                + "and ide_auac = (select max(ide_auac) from sis_auditoria_acceso where ide_usua=" + ide_usua + " and ide_acau=" + P_SIS_INGRESO_USUARIO + " and fin_auac=true)");
    }

    public void abrioPantalla() {
        utilitario.getConexion().ejecutarSql(crearSQLAuditoriaAcceso(utilitario.getVariable("IDE_USUA"), P_SIS_ABRIO_PANTALLA,
                utilitario.getVariable("IDE_OPCI")));
    }

    public String getSqlPantallasMasUsadas(String ide_usua) {
        //solo postgres
        return "select detalle_auac,(SELECT nom_opci FROM sis_opcion where ide_opci=a.detalle_auac::integer),count(detalle_auac) \n"
                + "from sis_auditoria_acceso a\n"
                + "where ide_usua=" + ide_usua + " and ide_acau=" + P_SIS_ABRIO_PANTALLA + "\n"
                + "GROUP BY ide_usua,detalle_auac \n"
                + "order by 3 desc\n"
                + "limit 5";
    }

    public TablaGenerica getSucursalesUsuario() {
        return utilitario.consultar("select sis_usuario_sucursal.sis_ide_sucu,ide_empr,nom_sucu "
                + "from sis_usuario_sucursal "
                + "INNER JOIN sis_sucursal on sis_usuario_sucursal.sis_ide_sucu=sis_sucursal.ide_sucu "
                + "where ide_usua=" + utilitario.getVariable("IDE_USUA"));
    }

}

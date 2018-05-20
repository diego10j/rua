/*
 *********************************************************************
 Objetivo: Clase de Exceción Generica para el proyecto
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.exception;

import java.sql.SQLException;

/**
 *
 * @author diego.jacome
 */
public class GenericException extends Exception {

    private String codigo;

    public GenericException(String codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
        cause.printStackTrace();
    }

    public GenericException(Throwable cause) {
        super(cause);
        cause.printStackTrace();
    }

    public GenericException(String codigo, String message, Throwable cause) {
        super(message, cause);
        this.codigo = codigo;
        cause.printStackTrace();
    }

    /**
     * Si la excepción es de tipo SQL recupera el codigo de error
     *
     * @return
     */
    public String getCodigoError() {
        if (codigo == null || codigo.isEmpty()) {
            try {
                codigo = String.valueOf(((SQLException) this.getCause()).getErrorCode());
            } catch (Exception e) {
                codigo = "9999";
            }
        }
        return codigo;
    }

    public GenericException() {
        super();
    }

    public GenericException(String message) {
        super(message);
    }
}

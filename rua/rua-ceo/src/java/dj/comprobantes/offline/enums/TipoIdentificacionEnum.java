/*
 *********************************************************************
 Objetivo: Enum Tipos de Identificación del Cliente
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.enums;

/**
 *
 * @author diego.jacome
 */
public enum TipoIdentificacionEnum {

    CONSUMIDOR_FINAL("07", "CONSUMIDOR FINAL"),
    RUC("04", "RUC"),
    CEDULA("05", "CEDULA"),
    PASAPORTE("06", "PASAPORTE"),
    IDENTIFICACION_EXTERIOR("08", "IDENTIFICACION EXTERIOR"),
    PLACA("09", "PLACA");

    private final String codigo;
    private final String descripcion;

    private TipoIdentificacionEnum(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    /**
     * obtiene el codigo de la numeracion
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * obtiene la descripcion de la numeracion
     *
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Dado el codigo retorna la descripcion
     *
     * @param codigo1
     * @return
     */
    public static String getDescripcion(String codigo1) {
        String valor = null;
        if (codigo1.equals(CONSUMIDOR_FINAL.getCodigo())) {
            valor = CONSUMIDOR_FINAL.getDescripcion();
        } else if (codigo1.equals(RUC.getCodigo())) {
            valor = RUC.getDescripcion();
        } else if (codigo1.equals(CEDULA.getCodigo())) {
            valor = CEDULA.getDescripcion();
        } else if (codigo1.equals(PASAPORTE.getCodigo())) {
            valor = PASAPORTE.getDescripcion();
        } else if (codigo1.equals(IDENTIFICACION_EXTERIOR.getCodigo())) {
            valor = IDENTIFICACION_EXTERIOR.getDescripcion();
        } else if (codigo1.equals(PLACA.getCodigo())) {
            valor = PLACA.getDescripcion();
        }
        return valor;
    }

}

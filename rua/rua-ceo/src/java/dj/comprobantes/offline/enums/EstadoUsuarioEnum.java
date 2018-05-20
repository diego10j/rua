/*
 *********************************************************************
 Objetivo: Enum Estados de un Comprobante Electronico
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
public enum EstadoUsuarioEnum {

    ACTIVO(1, "ACTIVO"),
    INACTIVO(2, "INACTIVO"),
    BLOQUEADO(3, "BLOQUEADO"),
    RESETEADO(4, "RESETEADO"),
    NUEVO(5, "NUEVO");
    private final Integer codigo;
    private final String descripcion;

    private EstadoUsuarioEnum(Integer codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Dado el nombre retorna el codigo correspondiente
     *
     * @param valor
     * @return
     */
    public static Integer getCodigo(String valor) {

        Integer codigo = null;
        if (valor.equals(ACTIVO.getDescripcion())) {
            codigo = ACTIVO.getCodigo();
        } else if (valor.equals(INACTIVO.getDescripcion())) {
            codigo = INACTIVO.getCodigo();
        } else if (valor.equals(BLOQUEADO.getDescripcion())) {
            codigo = BLOQUEADO.getCodigo();
        } else if (valor.equals(RESETEADO.getDescripcion())) {
            codigo = RESETEADO.getCodigo();
        } else if (valor.equals(NUEVO.getDescripcion())) {
            codigo = NUEVO.getCodigo();
        }
        return codigo;
    }

    /**
     * obtiene el codigo de la numeracion
     *
     * @return
     */
    public Integer getCodigo() {
        return codigo;
    }
}

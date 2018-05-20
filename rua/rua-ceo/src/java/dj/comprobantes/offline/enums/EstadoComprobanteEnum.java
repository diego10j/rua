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
public enum EstadoComprobanteEnum {

    RECIBIDA(1, "RECIBIDA"),
    DEVUELTA(2, "DEVUELTA"),
    AUTORIZADO(3, "AUTORIZADO"),
    RECHAZADO(4, "RECHAZADO"),
    NOAUTORIZADO(6, "NO AUTORIZADO"),
    PENDIENTE(5, "PENDIENTE"),
    ENPROCESO(1, "EN PROCESO"), //Similar a Recibida - Nueva por el SRI
    ANULADO(0, "ANULADO");  //
    private final Integer codigo;
    private final String descripcion;

    private EstadoComprobanteEnum(Integer codigo, String descripcion) {
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
        if (valor.equals(RECIBIDA.getDescripcion())) {
            codigo = RECIBIDA.getCodigo();
        } else if (valor.equals(DEVUELTA.getDescripcion())) {
            codigo = DEVUELTA.getCodigo();
        } else if (valor.equals(AUTORIZADO.getDescripcion())) {
            codigo = AUTORIZADO.getCodigo();
        } else if (valor.equals(RECHAZADO.getDescripcion())) {
            codigo = RECHAZADO.getCodigo();
        } else if (valor.equals(NOAUTORIZADO.getDescripcion())) {
            codigo = NOAUTORIZADO.getCodigo();
        } else if (valor.equals(PENDIENTE.getDescripcion())) {
            codigo = PENDIENTE.getCodigo();
        } else if (valor.equals(ANULADO.getDescripcion())) {
            codigo = ANULADO.getCodigo();
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

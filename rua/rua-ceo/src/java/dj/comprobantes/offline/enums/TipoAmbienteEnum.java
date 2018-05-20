/*
 *********************************************************************
 Objetivo: Enum Tipo de Ambientes del SRI para Comprobantes Electrónicos
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
public enum TipoAmbienteEnum {

    PRODUCCION("2"),
    PRUEBAS("1");
    private final String codigo;

    private TipoAmbienteEnum(final String codigo) {
        this.codigo = codigo;
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
     * Dado el codigo retorna el la descripcion del tipo de ambiente
     *
     * @param codigo1
     * @return
     */
    public static String getDescripcion(String codigo1) {
        String valor = null;
        if (codigo1.equals(PRODUCCION.getCodigo())) {
            valor = "PRODUCCIÓN";
        } else if (codigo1.equals(PRUEBAS.getCodigo())) {
            valor = "PRUEBAS";
        }
        return valor;
    }
}

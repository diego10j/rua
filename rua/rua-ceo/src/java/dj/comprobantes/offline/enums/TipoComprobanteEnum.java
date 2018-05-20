/*
 *********************************************************************
 Objetivo: Enum Tipos de Comprobantes Electrónicos
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
public enum TipoComprobanteEnum {

    LOTE("00", "LOTE MASIVO"),
    FACTURA("01", "FACTURA"),
    NOTA_DE_CREDITO("04", "NOTA DE CREDITO"),
    NOTA_DE_DEBITO("05", "NOTA DE DEBITO"),
    GUIA_DE_REMISION("06", "GUIA DE REMISION"),
    COMPROBANTE_DE_RETENCION("07", "COMPROBANTE DE RETENCION"),
    LIQUIDACION_DE_COMPRAS("03", "LIQ.DE COMPRAS");

    private final String codigo;
    private final String descripcion;

    private TipoComprobanteEnum(String codigo, String descripcion) {
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

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Dado el nombre retorna el codigo correspondiente
     *
     * @param valor
     * @return
     */
    public static String getCodigo(String valor) {
        String codigo = null;
        if (valor.equals(FACTURA.getDescripcion())) {
            codigo = FACTURA.getCodigo();
        } else if (valor.equals(NOTA_DE_DEBITO.getDescripcion())) {
            codigo = NOTA_DE_DEBITO.getCodigo();
        } else if (valor.equals(NOTA_DE_CREDITO.getDescripcion())) {
            codigo = NOTA_DE_CREDITO.getCodigo();
        } else if (valor.equals(COMPROBANTE_DE_RETENCION.getDescripcion())) {
            codigo = COMPROBANTE_DE_RETENCION.getCodigo();
        } else if (valor.equals(GUIA_DE_REMISION.getDescripcion())) {
            codigo = GUIA_DE_REMISION.getCodigo();
        } else if (valor.equals(LIQUIDACION_DE_COMPRAS.getDescripcion())) {
            codigo = LIQUIDACION_DE_COMPRAS.getCodigo();
        }
        return codigo;
    }

    /**
     * Dado el codigo retorna el la descripcion
     *
     * @param codigo1
     * @return
     */
    public static String getDescripcion(String codigo1) {
        String valor = null;
        if (codigo1.equals(FACTURA.getCodigo())) {
            valor = FACTURA.getDescripcion();
        } else if (codigo1.equals(NOTA_DE_DEBITO.getCodigo())) {
            valor = NOTA_DE_DEBITO.getDescripcion();
        } else if (codigo1.equals(NOTA_DE_CREDITO.getCodigo())) {
            valor = NOTA_DE_CREDITO.getDescripcion();
        } else if (codigo1.equals(COMPROBANTE_DE_RETENCION.getCodigo())) {
            valor = COMPROBANTE_DE_RETENCION.getDescripcion();
        } else if (codigo1.equals(GUIA_DE_REMISION.getCodigo())) {
            valor = GUIA_DE_REMISION.getDescripcion();
        } else if (codigo1.equals(LIQUIDACION_DE_COMPRAS.getCodigo())) {
            valor = LIQUIDACION_DE_COMPRAS.getDescripcion();
        }
        return valor;
    }

}

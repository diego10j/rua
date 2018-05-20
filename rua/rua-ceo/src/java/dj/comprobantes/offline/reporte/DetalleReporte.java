/*
 *********************************************************************
 Objetivo: Clase que almacenta el detalle de los comprobantes para ser
 enviado para generar el Reporte
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.reporte;

/**
 *
 * @author diego.jacome
 */
public class DetalleReporte {

    private String[] nombreColumna;
    private Object[] valor;

    public DetalleReporte() {
    }

    public DetalleReporte(String[] nombreColumna, Object[] valor) {
        this.nombreColumna = nombreColumna;
        this.valor = valor;
    }

    public String[] getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String[] nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    public Object[] getValor() {
        return valor;
    }

    public void setValor(Object[] valor) {
        this.valor = valor;
    }

}

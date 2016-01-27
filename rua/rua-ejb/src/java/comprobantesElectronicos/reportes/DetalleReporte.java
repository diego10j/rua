/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.reportes;

/**
 *
 * @author dfjacome
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

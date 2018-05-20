/*
 *********************************************************************
 Objetivo: Clase DataSorce que se envia al generador de reportes
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.reporte;

import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author diego.jacome
 */
public class ReporteDataSource implements JRDataSource {

    private List<DetalleReporte> lista;

    private int indice = -1;

    public ReporteDataSource(List<DetalleReporte> lista) {
        this.lista = lista;
    }

    @Override
    public boolean next() throws JRException {
        return ++indice < lista.size();
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object valor = null;
        if (lista != null) {
            for (int i = 0; i < lista.get(indice).getNombreColumna().length; i++) {
                if (lista.get(indice).getNombreColumna()[i].equalsIgnoreCase(jrField.getName())) {
                    valor = lista.get(indice).getValor()[i];
                    break;
                }
            }
        }
        return valor;
    }

    public List<DetalleReporte> getLista() {
        return lista;
    }

    public void setLista(List<DetalleReporte> lista) {
        this.lista = lista;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

}

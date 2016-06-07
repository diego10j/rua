/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import componentes.AsientoContable;
import componentes.CuentaCxc;
import componentes.DocumentoCxP;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_prueba extends Pantalla {

    private CuentaCxc fac = new CuentaCxc();
    private DocumentoCxP dcp = new DocumentoCxP();
    private AsientoContable asi = new AsientoContable();

    public pre_prueba() {
        fac.setId("fac");
        agregarComponente(fac);

        asi.setId("asi");
        agregarComponente(asi);

        dcp.setId("dcp");        
        agregarComponente(dcp);
    }

    @Override
    public void insertar() {
        fac.dibujar();
    }

    @Override
    public void guardar() {
        asi.dibujar();
    }

    @Override
    public void eliminar() {
        dcp.setDocumentoCxP("CREAR DOCUMENTO CXP");
        dcp.dibujar();
    }

    public CuentaCxc getFac() {
        return fac;
    }

    public void setFac(CuentaCxc fac) {
        this.fac = fac;
    }

    public AsientoContable getAsi() {
        return asi;
    }

    public void setAsi(AsientoContable asi) {
        this.asi = asi;
    }

    public DocumentoCxP getDcp() {
        return dcp;
    }

    public void setDcp(DocumentoCxP dcp) {
        this.dcp = dcp;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

/**
 *
 * @author Diego
 */
public class cls_det_comp_cont {

    private String ide_cndcc;
    private String ide_cnlap;
    private String ide_empr;
    private String ide_sucu;
    private String ide_cnccc;
    private String ide_cndpc;
    private double valor_cndcc;
    private String observacion_cndcc;

    public cls_det_comp_cont(String ide_cnlap, String ide_cndpc, double valor_cndcc, String observacion_cndcc) {
        this.ide_cnlap = ide_cnlap;
        this.ide_cndpc = ide_cndpc;
        this.valor_cndcc = valor_cndcc;
        this.observacion_cndcc = observacion_cndcc;
    }


    public String getIde_cnccc() {
        return ide_cnccc;
    }

    public void setIde_cnccc(String ide_cnccc) {
        this.ide_cnccc = ide_cnccc;
    }

    public String getIde_cndcc() {
        return ide_cndcc;
    }

    public void setIde_cndcc(String ide_cndcc) {
        this.ide_cndcc = ide_cndcc;
    }

    public String getIde_cndpc() {
        return ide_cndpc;
    }

    public void setIde_cndpc(String ide_cndpc) {
        this.ide_cndpc = ide_cndpc;
    }

    public String getIde_cnlap() {
        return ide_cnlap;
    }

    public void setIde_cnlap(String ide_cnlap) {
        this.ide_cnlap = ide_cnlap;
    }

    public String getIde_empr() {
        return ide_empr;
    }

    public void setIde_empr(String ide_empr) {
        this.ide_empr = ide_empr;
    }

    public String getIde_sucu() {
        return ide_sucu;
    }

    public void setIde_sucu(String ide_sucu) {
        this.ide_sucu = ide_sucu;
    }

    public String getObservacion_cndcc() {
        return observacion_cndcc;
    }

    public void setObservacion_cndcc(String observacion_cndcc) {
        this.observacion_cndcc = observacion_cndcc;
    }

    public double getValor_cndcc() {

        return valor_cndcc;
    }

    public void setValor_cndcc(double valor_cndcc) {
        this.valor_cndcc = valor_cndcc;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import java.util.ArrayList;
import java.util.List;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_cab_comp_cont {

    private String ide_cnccc;
    private String ide_usua;
    private String ide_cntcm;
    private String ide_cneco;
    private String ide_modu;
    private String ide_sucu;
    private String ide_geper;
    private String ide_empr;
    private String fecha_trans_cnccc;
    private String observacion_cnccc;
    private String numero_cnccc;
    private String fecha_siste_cnccc;
    private String hora_sistem_cnccc;
    private List<cls_det_comp_cont> detalles = new ArrayList<cls_det_comp_cont>();

    public cls_cab_comp_cont(String ide_cntcm, String ide_cneco, String ide_modu, String ide_geper, String fecha_trans_cnccc, String observacion_cnccc) {
        this.ide_cntcm = ide_cntcm;
        this.ide_cneco = ide_cneco;
        this.ide_modu = ide_modu;
        this.ide_geper = ide_geper;
        this.fecha_trans_cnccc = fecha_trans_cnccc;
        this.observacion_cnccc = observacion_cnccc;
    }

    public boolean validarComprobane() {
        Utilitario utilitario = new Utilitario();
        if (detalles.isEmpty()) {
            utilitario.agregarMensajeError("Comprobante no válido", "El comprobante no tiene detales de cuentas");
            return false;
        } else {
            double total_debe = 0.0;
            double total_haber = 0.0;
            double diferencia = 0.0;
            String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
            String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
            for (int i = 0; i < detalles.size(); i++) {
                if (detalles.get(i).getIde_cnlap().equals(p_con_lugar_debe)) {
                    total_debe += detalles.get(i).getValor_cndcc();
                } else if (detalles.get(i).getIde_cnlap().equals(p_con_lugar_haber)) {
                    total_haber += detalles.get(i).getValor_cndcc();
                }
            }
            diferencia = Double.parseDouble(utilitario.getFormatoNumero(total_debe)) - Double.parseDouble(utilitario.getFormatoNumero(total_haber));
            System.out.println("tot debe "+total_debe+" tot_haber "+total_haber+" diferencia "+diferencia);
            if (diferencia != 0.0) {
                utilitario.agregarMensajeError("Comprobante no válido", "Diferencia :" + diferencia + " entre debe y haber");
                return false;
            } else {
                return true;
            }

        }
    }

    public void resumirComprobane() {
        //Unifica las cuentas
        List<cls_det_comp_cont> resumen = new ArrayList<cls_det_comp_cont>();

        if (!detalles.isEmpty()) {
            List l_cuenta = new ArrayList();
            List l_observacion = new ArrayList();
            List l_lug_apli = new ArrayList();
            List l_suma = new ArrayList();
            List l_valor = new ArrayList();
            int band = 0;
            String cuen;
            String ide_cnlap;
            String observacion;
            double suma = 0;
            double valor = 0;
            for (int i = 0; i < detalles.size(); i++) {
                try {
                    cuen = detalles.get(i).getIde_cndpc() + "";
                    ide_cnlap = detalles.get(i).getIde_cnlap() + "";
                    observacion = detalles.get(i).getObservacion_cndcc() + "";
                    valor = detalles.get(i).getValor_cndcc();
                    for (int k = 0; k < l_cuenta.size(); k++) {
                        try {
                            if (detalles.get(i).getIde_cndpc() != null && !detalles.get(i).getIde_cndpc().isEmpty()) {
                                if (cuen.equals(l_cuenta.get(k))
                                        && ide_cnlap.equals(l_lug_apli.get(k))
                                        && observacion.equals(l_observacion.get(k))) {
                                    band = 1;
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (band == 0) {
                        l_cuenta.add(cuen);
                        l_observacion.add(observacion);
                        l_lug_apli.add(ide_cnlap);
                        l_valor.add(valor);
                    }
                    band = 0;
                } catch (Exception e) {
                }
            }
//            for (int j = 0; j < detalles.size(); j++) {
//                System.out.println("lsta detalles ide_cuenta " + detalles.get(j).getIde_cndpc()+"");
//            }
//            for (int i = 0; i < l_cuenta.size(); i++) {
//                System.out.println("ide_cuenta " + l_cuenta.get(i) + "" + " valor " + l_valor.get(i));
//            }
            for (int i = 0; i < l_cuenta.size(); i++) {
                try {
                    cuen = l_cuenta.get(i) + "";
                    ide_cnlap = l_lug_apli.get(i) + "";
                    observacion = l_observacion.get(i) + "";
                    for (int j = 0; j < detalles.size(); j++) {
                        try {
                            if (cuen != null && !cuen.isEmpty()) {
                                if (cuen.equals(detalles.get(j).getIde_cndpc().toString())
                                        && ide_cnlap.equals(detalles.get(j).getIde_cnlap().toString())
                                        && observacion.equals(detalles.get(j).getObservacion_cndcc().toString())) {
                                    suma = detalles.get(j).getValor_cndcc() + suma;
                                }
                            } else {
                                suma = Double.parseDouble(l_valor.get(i) + "");
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                    l_suma.add(suma);
                    suma = 0;
                } catch (Exception e) {
                }
            }
            for (int i = 0; i < l_cuenta.size(); i++) {
                try {
                    resumen.add(new cls_det_comp_cont(l_lug_apli.get(i) + "", l_cuenta.get(i) + "", Double.parseDouble(l_suma.get(i) + ""), l_observacion.get(i) + ""));
                } catch (Exception e) {
                }
            }
        }
        detalles = resumen;
    }

    public static void main(String args[]) {
        System.out.println(" ss");
        cls_det_comp_cont dt = new cls_det_comp_cont("0", "1", 320, "ddsd");
        cls_det_comp_cont dt1 = new cls_det_comp_cont("1", "0", 120, "ddsd");
        cls_det_comp_cont dt2 = new cls_det_comp_cont("0", "2", 320, "ddsd");
        cls_det_comp_cont dt3 = new cls_det_comp_cont("0", "3", 20, "ddsd");
        cls_det_comp_cont dt4 = new cls_det_comp_cont("1", "0", 100, "ddsd");
        cls_det_comp_cont dt5 = new cls_det_comp_cont("0", "2", 12, "ddsd");
        cls_det_comp_cont dt6 = new cls_det_comp_cont("0", "3", 56, "ddsd");
        cls_det_comp_cont dt7 = new cls_det_comp_cont("1", "4", 450, "ddsd");
        cls_det_comp_cont dt8 = new cls_det_comp_cont("1", "1", 100, "ddsd");

        cls_cab_comp_cont c = new cls_cab_comp_cont(null, null, null, null, null, null);
        c.getDetalles().add(dt);
        c.getDetalles().add(dt1);
        c.getDetalles().add(dt2);
        c.getDetalles().add(dt3);
        c.getDetalles().add(dt4);
        c.getDetalles().add(dt5);
        c.getDetalles().add(dt6);
        c.getDetalles().add(dt7);
        c.getDetalles().add(dt8);

        c.resumirComprobane();

    }

    public String getFecha_siste_cnccc() {
        return fecha_siste_cnccc;
    }

    public void setFecha_siste_cnccc(String fecha_siste_cnccc) {
        this.fecha_siste_cnccc = fecha_siste_cnccc;
    }

    public String getFecha_trans_cnccc() {
        return fecha_trans_cnccc;
    }

    public void setFecha_trans_cnccc(String fecha_trans_cnccc) {
        this.fecha_trans_cnccc = fecha_trans_cnccc;
    }

    public String getHora_sistem_cnccc() {
        return hora_sistem_cnccc;
    }

    public void setHora_sistem_cnccc(String hora_sistem_cnccc) {
        this.hora_sistem_cnccc = hora_sistem_cnccc;
    }

    public String getIde_cnccc() {
        return ide_cnccc;
    }

    public void setIde_cnccc(String ide_cnccc) {
        this.ide_cnccc = ide_cnccc;
    }

    public String getIde_cneco() {
        return ide_cneco;
    }

    public void setIde_cneco(String ide_cneco) {
        this.ide_cneco = ide_cneco;
    }

    public String getIde_cntcm() {
        return ide_cntcm;
    }

    public void setIde_cntcm(String ide_cntcm) {
        this.ide_cntcm = ide_cntcm;
    }

    public String getIde_empr() {
        return ide_empr;
    }

    public void setIde_empr(String ide_empr) {
        this.ide_empr = ide_empr;
    }

    public String getIde_geper() {
        return ide_geper;
    }

    public void setIde_geper(String ide_geper) {
        this.ide_geper = ide_geper;
    }

    public String getIde_modu() {
        return ide_modu;
    }

    public void setIde_modu(String ide_modu) {
        this.ide_modu = ide_modu;
    }

    public String getIde_sucu() {
        return ide_sucu;
    }

    public void setIde_sucu(String ide_sucu) {
        this.ide_sucu = ide_sucu;
    }

    public String getIde_usua() {
        return ide_usua;
    }

    public void setIde_usua(String ide_usua) {
        this.ide_usua = ide_usua;
    }

    public String getNumero_cnccc() {
        return numero_cnccc;
    }

    public void setNumero_cnccc(String numero_cnccc) {
        this.numero_cnccc = numero_cnccc;
    }

    public String getObservacion_cnccc() {
        return observacion_cnccc;
    }

    public void setObservacion_cnccc(String observacion_cnccc) {
        this.observacion_cnccc = observacion_cnccc;
    }

    public List<cls_det_comp_cont> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<cls_det_comp_cont> detalles) {
        this.detalles = detalles;
    }
}

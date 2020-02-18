/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.List;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_presupuesto.ejb.ServicioPresupuesto;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;      

/**
 *
 * @author IORI YAGAMI
 */
public class UpBalancesMensuales extends Pantalla {

    private Etiqueta eti_perfil = new Etiqueta();
    private Etiqueta eti_ano = new Etiqueta();
    private Combo com_periodo = new Combo();
    private Combo com_ano = new Combo();

    @EJB
    private final ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    @EJB
    private final ServiciosAdquisiones ser_adquisiones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public UpBalancesMensuales() {
        if (tienePerfil()) {

            com_ano.setId("com_ano");
            com_ano.setCombo(ser_presupuesto.getAnio("true,false"));

            com_periodo.setId("com_periodo");      
            com_periodo.setCombo(ser_contabilidad.getSqlComboPeridos());
            bar_botones.agregarComponente(new Etiqueta("Periodo "));
            bar_botones.agregarComponente(com_periodo);
            com_periodo.setMetodo("filtroComboPeriodo");

            eti_perfil.setStyle("font-size: 16px;font-weight: bold");
            eti_perfil.setValue("Perfil: " + perfil);

        }
    }
    String perfil = "";
    String ide_empleado = "";
    //String  = "";

    public void filtroComboPeriodo() {
        com_ano.setCombo(ser_presupuesto.getAnio("true"));
        utilitario.addUpdate("com_ano");

    }

    private boolean tienePerfil(){
        return false;
}

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Combo getCom_periodo() {
        return com_periodo;
    }

    public void setCom_periodo(Combo com_periodo) {
        this.com_periodo = com_periodo;
    }

    public Combo getCom_ano() {
        return com_ano;
    }

    public void setCom_ano(Combo com_ano) {
        this.com_ano = com_ano;
    }

    public Etiqueta getEti_perfil() {
        return eti_perfil;
    }

    public void setEti_perfil(Etiqueta eti_perfil) {
        this.eti_perfil = eti_perfil;
    }

    public Etiqueta getEti_ano() {
        return eti_ano;
    }

    public void setEti_ano(Etiqueta eti_ano) {
        this.eti_ano = eti_ano;
    }

}

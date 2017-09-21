/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.cuentas_x_cobrar.ServicioCliente;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author User
 */
public class pre_persona_cuenta extends Pantalla{
    
    Tabla tab_tabla1 = new Tabla();
    Tabla tab_tabla2 = new Tabla();
   @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);
    
    public pre_persona_cuenta(){
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("gen_persona", "ide_geper", 1);
        tab_tabla1.getColumna("identificac_geper").setFiltro(true);
        tab_tabla1.getColumna("nom_geper").setFiltro(true);
        tab_tabla1.getColumna("ide_vgven").setVisible(false);
        tab_tabla1.getColumna("ide_empr").setVisible(false);
        tab_tabla1.getColumna("ide_rhtro").setVisible(false);
        tab_tabla1.getColumna("ide_rhcon").setVisible(false);
        tab_tabla1.getColumna("ide_getid").setVisible(false);
        tab_tabla1.getColumna("ide_gegen").setVisible(false);
        tab_tabla1.getColumna("ide_rheem").setVisible(false);
        tab_tabla1.getColumna("ide_teban").setVisible(false);
        tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("ide_coepr").setVisible(false);
        tab_tabla1.getColumna("ide_rhtem").setVisible(false);
        tab_tabla1.getColumna("ide_geeci").setVisible(false);
        tab_tabla1.getColumna("ide_rhmse").setVisible(false);
        tab_tabla1.getColumna("ide_vgecl").setVisible(false);
        tab_tabla1.getColumna("ide_rhseg").setVisible(false);
        tab_tabla1.getColumna("ide_rhcsa").setVisible(false);
        tab_tabla1.getColumna("ide_geubi").setVisible(false);
        tab_tabla1.getColumna("ide_cntco").setVisible(false);
        tab_tabla1.getColumna("ide_vgtcl").setVisible(false);
        tab_tabla1.getColumna("ide_rhfpa").setVisible(false);
        tab_tabla1.getColumna("ide_rheor").setVisible(false);
        tab_tabla1.getColumna("ide_cotpr").setVisible(false);
        tab_tabla1.getColumna("ide_rhrtr").setVisible(false);
        tab_tabla1.getColumna("ide_sucu").setVisible(false);
        tab_tabla1.getColumna("ide_rhtsa").setVisible(false);
        tab_tabla1.getColumna("ide_tetcb").setVisible(false);
        tab_tabla1.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla1.getColumna("ide_rhtco").setVisible(false);
        tab_tabla1.getColumna("sueldo_geper").setVisible(false);
        tab_tabla1.getColumna("cuent_banco_geper").setVisible(false);
        tab_tabla1.getColumna("contacto_geper").setVisible(false);
        tab_tabla1.getColumna("direccion_geper").setVisible(false);
        tab_tabla1.getColumna("telefono_geper").setVisible(false);
        tab_tabla1.getColumna("fax_geper").setVisible(false);
        tab_tabla1.getColumna("movil_geper").setVisible(false);
        tab_tabla1.getColumna("foto_geper").setVisible(false);
        tab_tabla1.getColumna("correo_geper").setVisible(false);
        tab_tabla1.getColumna("pagina_web_geper").setVisible(false);
        tab_tabla1.getColumna("repre_legal_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_nacim_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_ingre_geper").setVisible(false);
        tab_tabla1.getColumna("fecha_salid_geper").setVisible(false);
        tab_tabla1.getColumna("observacion_geper").setVisible(false);
        tab_tabla1.getColumna("factu_hasta_geper").setVisible(false);
        tab_tabla1.getColumna("jornada_inicio_geper").setVisible(false);
        tab_tabla1.getColumna("jornada_fin_geper").setVisible(false);
        tab_tabla1.getColumna("numero_geper").setVisible(false);
        tab_tabla1.getColumna("ide_rhfre").setVisible(false);
        tab_tabla1.getColumna("ide_usua").setVisible(false);
        
        tab_tabla1.agregarRelacion(tab_tabla2);  
        tab_tabla1.setLectura(true);
       
        tab_tabla1.dibujar();
        tab_tabla1.setRows(10);
        
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("con_persona_cuenta", "ide_cnpec", 2);
        tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen","ide_cndpc","codig_recur_cndpc,nombre_cndpc","");
        tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla2.dibujar();
        
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        Division div_dividir = new Division();
        div_dividir.dividir2(pat_panel1, pat_panel2,"50%", "H");
        
        agregarComponente(div_dividir);
    }

    @Override
    public void insertar() {
     tab_tabla2.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla2.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla2.eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}

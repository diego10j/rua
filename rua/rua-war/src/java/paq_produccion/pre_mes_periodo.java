/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;




/**
 *
 * @author Nicolas Cajilema
 */
public class pre_mes_periodo extends Pantalla {
 private Tabla tab_mes_perido=new Tabla();
 private Combo com_periodo_academico = new Combo();

 @EJB
 private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class); 
    
 
 public  pre_mes_periodo (){
        
          
        
        com_periodo_academico.setId("com_periodo_academico");
        com_periodo_academico.setCombo(ser_pensiones .getPeriodoAcademico("true"));
        bar_botones.agregarComponente(new Etiqueta("Periodo Academico"));
        bar_botones.agregarComponente(com_periodo_academico);
        com_periodo_academico.setMetodo("filtroComboPeriodoAcademico");
        
        
           Boton bot_clean = new Boton();
           bot_clean.setIcon("ui-icon-cancel");
           bot_clean.setTitle("Limpiar");
           bot_clean.setMetodo("limpiar");
           bar_botones.agregarComponente(bot_clean);
            
            
        tab_mes_perido.setId("tab_mes_perido");
        tab_mes_perido.setTabla("rec_mes_periodo","ide_remep",1);
        tab_mes_perido.setHeader("MES PERIODO");
        tab_mes_perido.dibujar();
        
        PanelTabla pat_mes_periodo = new PanelTabla();
        pat_mes_periodo.setId("tab_mes_perido");
        pat_mes_periodo.setPanelTabla(tab_mes_perido);
        
        
        Division div_mes_periodo = new Division();
        div_mes_periodo.setId("div_mes_periodo");
        div_mes_periodo.dividir1(pat_mes_periodo);
        
        agregarComponente(div_mes_periodo);   

    }

   @Override  
    public void insertar() 
    
     {
        tab_mes_perido.insertar();
        tab_mes_perido.setValor("ide_repea", com_periodo_academico.getValue().toString());

        
    }
    
    
      public void filtroComboPeriodoAcademnico(){
        
        tab_mes_perido.setCondicion("ide_repea="+com_periodo_academico.getValue().toString());
        tab_mes_perido.ejecutarSql();
        utilitario.addUpdate("tab_mes_perido");
      }

    @Override 
    public void guardar() {
        tab_mes_perido.guardar();
        guardarPantalla();
    }

    @Override 
    public void eliminar() {
        tab_mes_perido.eliminar();
                  
    }
    
    
    
    public void limpiar() {
        tab_mes_perido.limpiar();
        com_periodo_academico.limpiar();
    }
    public Tabla getTab_mes_perido() {
        return tab_mes_perido;
    }

    public void setTab_mes_perido(Tabla tab_mes_perido) {
        this.tab_mes_perido = tab_mes_perido;
    }

    public Combo getCom_periodo_academico() {
        return com_periodo_academico;
    }

    public void setCom_periodo_academico(Combo com_periodo_academico) {
        this.com_periodo_academico = com_periodo_academico;
    }
    
}

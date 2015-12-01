/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_presupuesto;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelAcordion;
import framework.componentes.Tabla;
import java.util.ArrayList;
import pkg_rrhh.cls_graficas;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego P
 */
public class pre_grafico_presupuesto extends Pantalla {
    
    private Division div_division = new Division();
    private cls_graficas graficas;
    private PanelAcordion pac_acordion = new PanelAcordion();
    private Combo com_periodo_presu = new Combo();
    private Grid grid_periodo = new Grid();
    private Grid gri_contenedor2 = new Grid();
    private Grid gri_contenedor3 = new Grid();
    private Tabla tab_tabla1 = new Tabla();
    
    public pre_grafico_presupuesto() {
        
        gri_contenedor2.setId("gri_contenedor2");
        gri_contenedor3.setId("gri_contenedor3");
        
        Etiqueta eti1 = new Etiqueta("SELECCIONE UN PERIODO PRESUPUESTARIO");
        grid_periodo.setId("grid1");
        grid_periodo.setWidth("200");
        grid_periodo.setRendered(true);
        grid_periodo.setHeader(eti1);
        
        pac_acordion.setId("pac_acordion");

        //grid periodo presupuestario
        com_periodo_presu.setId("com_periodo_presu");
        com_periodo_presu.setCombo("select ide_prppr,nombre_prppr  from pre_periodo_presu");
        com_periodo_presu.setMetodo("CrearGrafico");
        grid_periodo.getChildren().add(com_periodo_presu);
        
        pac_acordion.agregarPanel("SUELDOS Y SALARIOS", grid_periodo);
        Division div2 = new Division();
        div2.dividir2(pac_acordion, gri_contenedor2, "50%", "H");
        div_division.setId("div_division");
        div_division.dividir2(div2, gri_contenedor3, "25%", "V");
        agregarComponente(div_division);
        //  CrearGrafico();
    }
    
    public void CrearGrafico() {
        
        gri_contenedor2.getChildren().clear();
        gri_contenedor3.getChildren().clear();
        
        System.out.println("si ingresa el ameto");
        try {
            if (com_periodo_presu.getValue().toString() != null && !com_periodo_presu.getValue().toString().isEmpty()) {
                
                System.out.println("paso if");
                String valor_mes = com_periodo_presu.getValue().toString();
                System.out.println("--->" + valor_mes);
                System.out.println("si ingresa el ameto ---" + valor_mes);
                utilitario.addUpdate("gri_contenedor3,gri_contenedor2,pac_acordion");
                ArrayList<String> departamentos = new ArrayList<String>();
                tab_tabla1.setSql("SELECT org.ide_georg as codigo,org.nombre_georg as departamento,sum(dp.total_prdep) as total from gen_organigrama org  "
                        + " LEFT join pre_cab_plan_presu cp on org.ide_georg=cp.ide_georg  "
                        + " LEFT JOIN pre_descripcion_presu desp ON cp.ide_prcppr=desp.ide_prcppr  "
                        + " LEFT join pre_detalle_presu dp on desp.ide_prdpr=dp.ide_prdpr  "
                        + " where cp.ide_georg in (select ide_georg from gen_organigrama  "
                        + " ORDER BY ide_georg) and cp.ide_prppr=" + valor_mes + "   and desp.ide_prtrp=157 and cp.ide_sucu=" + utilitario.getVariable("ide_sucu") + " and cp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + " GROUP BY  org.ide_georg,org.nombre_georg ORDER BY nombre_georg");

                //añado nombres de campos de la consulta sql en un Array
                departamentos.add("departamento");
                departamentos.add("total");
                
                tab_tabla1.ejecutarSql();
                
                graficas = new cls_graficas();
                String label = "DEPARTAMENTOS";
                String titulo = "SUELDO Y SALARIOS CASA INSPECTORIAL";
                tab_tabla1.ejecutarSql();
                tab_tabla1.setId("tab_tabla1");
                tab_tabla1.setCampoPrimaria("codigo");
                tab_tabla1.getColumna("codigo").setVisible(false);
                tab_tabla1.getColumna("departamento").setLectura(true);
                tab_tabla1.getColumna("total").setLectura(true);
                tab_tabla1.getGrid().setColumns(4);
                tab_tabla1.setLectura(true);
                tab_tabla1.setTipoFormulario(false);
                
                tab_tabla1.dibujar();

            //GRID GRAFICO(3)
                Etiqueta eti = new Etiqueta("GRÁFICOS ESTADÍSTICOS PARA EL CONTROL DEL PRESUPUESTO");
                eti.setStyle("font-size:18px;");
                gri_contenedor3.setHeader(eti);

                //GRID RESULTADOS(2)
                Etiqueta eti_cabeza_resul = new Etiqueta("DATOS RESULTANTES");
                gri_contenedor2.setStyle("font-size:14px;");
                
                gri_contenedor2.setHeader(eti_cabeza_resul);
                
                graficas.createGraficoGeneral(tab_tabla1, departamentos, titulo, label);
                graficas.getBarchart().setValue(graficas.getCategoryModel());
                gri_contenedor3.getChildren().add(graficas.getBarchart());
                gri_contenedor2.getChildren().add(tab_tabla1);
                
            } else {
                System.out.println("entro else");
            }
            
        } catch (Exception e) {
            gri_contenedor3.getChildren().clear();
            utilitario.agregarMensajeInfo("Error al Gráficar", "Seleccione un periodo presupuestario");
        }
        utilitario.addUpdate("gri_contenedor3,gri_contenedor2");
        
    }
    
    @Override
    public void insertar() {
    }
    
    @Override
    public void guardar() {
    }
    
    @Override
    public void eliminar() {
    }
    
    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }
    
    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
}

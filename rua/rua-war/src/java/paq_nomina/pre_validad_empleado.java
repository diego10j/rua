/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author Diego
 */
public class pre_validad_empleado extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();

    public pre_validad_empleado() {
    	Boton bot_emp_nomina=new Boton();
		bot_emp_nomina.setValue("EMPLEADO-NOMINA");
		bot_emp_nomina.setMetodo("validarEmpleadoNomina");
		Boton bot_nomina_emp=new Boton();
		bot_nomina_emp.setValue("NOMINA-EMPLEADO");
		bot_nomina_emp.setMetodo("validarNominaEmpleado");
    	Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
    	Etiqueta eti_opcion=new Etiqueta("Escoga Opcion a Mostrar:");
		bar_botones.agregarComponente(eti_opcion);
		bar_botones.agregarBoton(bot_emp_nomina);
		bar_botones.agregarBoton(bot_nomina_emp);
		bar_botones.agregarBoton(bot_limpiar);
    	
    ;
		
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql("select a.ide_gtemp,a.empleado,a.fecha_geedp,( case when b.ide_gtemp is null then 'NO EXISTE EL EMPLEADO EN LA PRENOMINA VIGENTE' else null end) as novedad_nomina " +
        		"from (select a.ide_gtemp,b.apellido_paterno_gtemp||' '||b.apellido_materno_gtemp||' '||b.primer_nombre_gtemp||' '||b.segundo_nombre_gtemp as empleado,a.fecha_geedp " +
        		"from gen_empleados_departamento_par a, gth_empleado b " +
        		"where activo_geedp=1 and a.ide_gtemp=b.ide_gtemp " +
        		"order by b.apellido_paterno_gtemp,b.apellido_materno_gtemp " +
        		") a left join ( " +
        		"select a.ide_gtemp,b.apellido_paterno_gtemp||' '||b.apellido_materno_gtemp||' '||b.primer_nombre_gtemp||' '||b.segundo_nombre_gtemp as empleado,a.fecha_geedp " +
        		"from gen_empleados_departamento_par a, gth_empleado b " +
        		"where activo_geedp=1 and a.ide_gtemp=b.ide_gtemp and ide_geedp in ( " +
        		"select ide_geedp from nrh_detalle_rol where ide_nrrol in ( " +
        		"select ide_nrrol from nrh_rol where ide_nresr=2 " +
        		") group by ide_geedp ) " +
        		") b on a.ide_gtemp= b.ide_gtemp " +
        		"order by a.empleado");
        tab_tabla1.setLectura(true);
        //tab_tabla1.setRows(0);
        tab_tabla1.setPaginator(false);
      //tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setSql("select a.ide_gtemp,a.empleado,a.fecha_geedp,( case when b.ide_gtemp is null then null else 'EXISTE UN EMPLEADO INACTIVO EN LA PRE NOMINA VIGENTE' end) as novedad_nomina " +
        		"from (select a.ide_gtemp,b.apellido_paterno_gtemp||' '||b.apellido_materno_gtemp||' '||b.primer_nombre_gtemp||' '||b.segundo_nombre_gtemp as empleado,a.fecha_geedp " +
        		"from gen_empleados_departamento_par a, gth_empleado b " +
        		"where activo_geedp=1 and a.ide_gtemp=b.ide_gtemp and ide_geedp in ( " +
        		"select ide_geedp from nrh_detalle_rol where ide_nrrol in ( " +
        		"select ide_nrrol from nrh_rol where ide_nresr=2 " +
        		") group by ide_geedp) " +
        		") a left join ( " +
        		"select a.ide_gtemp,b.apellido_paterno_gtemp||' '||b.apellido_materno_gtemp||' '||b.primer_nombre_gtemp||' '||b.segundo_nombre_gtemp as empleado,a.fecha_geedp " +
        		"from gen_empleados_departamento_par a, gth_empleado b " +
        		"where activo_geedp=0 and a.ide_gtemp=b.ide_gtemp " +
        		"order by b.apellido_paterno_gtemp,b.apellido_materno_gtemp " +
        		") b on a.ide_gtemp= b.ide_gtemp " +
        		"order by a.empleado");
       // tab_tabla2.dibujar();
        tab_tabla2.setLectura(true);
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla2);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }
	
	public void limpiar() {
		tab_tabla1.limpiar();
		tab_tabla2.limpiar();
				
	}
    public void validarEmpleadoNomina(){
    	tab_tabla1.dibujar();
    }
public void validarNominaEmpleado(){
	tab_tabla2.dibujar();
    	
    }
    
    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
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

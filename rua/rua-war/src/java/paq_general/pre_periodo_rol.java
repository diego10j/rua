/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import javax.faces.event.AjaxBehaviorEvent;

import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_periodo_rol extends Pantalla {

    
    private Tabla tab_tabla = new Tabla();

    public pre_periodo_rol() {        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_PERIDO_ROL", "IDE_GEPRO", 1);
        tab_tabla.getColumna("ACTIVO_GEPRO").setCheck();
        tab_tabla.getColumna("ACTIVO_GEPRO").setValorDefecto("TRUE");
        tab_tabla.getColumna("IDE_NRTIT").setCombo("NRH_TIPO_ROL", "IDE_NRTIT", "DETALLE_NRTIT", "");
        tab_tabla.getColumna("IDE_GEANI").setCombo("GEN_ANIO", "IDE_GEANI", "nom_geani", "");
        tab_tabla.getColumna("IDE_GEANI").setMetodoChange("cargarMes");
        tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.nombre_gemes FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI="+tab_tabla.getValor("IDE_GEANI"));                        
        tab_tabla.getColumna("GEN_IDE_GEPRO").setCombo("select ide_gepro,fecha_inicial_gepro,fecha_final_gepro,detalle_periodo_gepro from gen_perido_rol order by fecha_inicial_gepro desc");                        

        tab_tabla.setTipoFormulario(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        actualizarCombos();
    }
    
    public void cargarMes(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.nombre_gemes FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI="+tab_tabla.getValor("IDE_GEANI"));
        utilitario.addUpdateTabla(tab_tabla, "IDE_GEMES", "");
    }
    
    private void actualizarCombos() {
    	tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.nombre_gemes FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI="+tab_tabla.getValor("IDE_GEANI"));
        tab_tabla.actualizarCombosFormulario();
    }

   @Override
public void buscar() {
	// TODO Auto-generated method stub
	   //Esto es para que le muestre todos los meses ya que se un  combo con filtro
	tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.nombre_gemes FROM GEN_MES a");
	super.buscar();	 
} 
   
    
    @Override
    public void insertar() {
    	tab_tabla.getColumna("IDE_GEMES").setCombo("SELECT a.IDE_GEMES,a.nombre_gemes FROM GEN_MES a, GEN_PERIODO b WHERE a.IDE_GEMES=b.IDE_GEMES and b.IDE_GEANI=-1");
        tab_tabla.insertar();
    }

    @Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		actualizarCombos();
	}
    
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		actualizarCombos();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		actualizarCombos();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		actualizarCombos();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizarCombos();
	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
		if (utilitario.getBuscaTabla().isVisible() == false) {
            actualizarCombos();
        }
	}

	@Override
    public void guardar() {
		if (tab_tabla.getTotalFilas() > 0) {
         //   TablaGenerica tab_perido_rol = utilitario.consultar("SELECT * FROM GEN_PERIDO_ROL");
        } else {
        }
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
    	if (tab_tabla.eliminar()) {
            actualizarCombos();
        }
    }
    
    

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}

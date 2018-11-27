/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import paq_nomina.*;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import servicios.activos.ServicioActivosFijos;

/**
 *
 */
public class pre_actas extends Pantalla {

    
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Combo com_tipo_acta=new Combo();
    private Dialogo dia_activos= new Dialogo();
    private SeleccionTabla set_activos=new SeleccionTabla();
    private SeleccionTabla set_tipo_acta=new SeleccionTabla();
    @EJB
    private ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);
    @EJB
    private ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private final ServicioProduccion ser_valtiempo= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
    String condicion="";

    public pre_actas() { 
        
        com_tipo_acta.setCombo(ser_activos.getTipoActa());
        com_tipo_acta.setMetodo("seleccionarTipoActa");
	bar_botones.agregarComponente(new Etiqueta("Seleccione El Tipo de Acta:"));
	bar_botones.agregarComponente(com_tipo_acta);
        
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);
        
        Boton bot_agrega_activo= new Boton();
        bot_agrega_activo.setIcon("ui-icon-search");
	bot_agrega_activo.setValue("BUSCAR ACTIVOS");
	bot_agrega_activo.setMetodo("importarActivos");
	bar_botones.agregarBoton(bot_agrega_activo);
            
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("act_acta_constata","ide_acact", 1);
        tab_tabla.setCondicion("ide_actia=-1");
        tab_tabla.getColumna("ide_gecas").setCombo(ser_activos.getCasa());
        tab_tabla.getColumna("ide_geobr").setCombo(ser_activos.getObras());
        tab_tabla.getColumna("ide_acuba").setCombo(ser_activos.getUbicacionActivo());
        tab_tabla.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_tabla.getColumna("gen_ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_tabla.getColumna("ide_gecas").setAutoCompletar();
        tab_tabla.getColumna("ide_geobr").setAutoCompletar();
        tab_tabla.getColumna("ide_acuba").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setRequerida(true);       
        tab_tabla.getColumna("secuencial_acact").setRequerida(true);
   
        tab_tabla.getColumna("bloqueado_acact").setLectura(true);
        tab_tabla.getColumna("bloqueado_acact").setValorDefecto("false");
        tab_tabla.getColumna("secuencial_acact").setEtiqueta();
        tab_tabla.getColumna("secuencial_acact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
        tab_tabla.getColumna("ide_actia").setVisible(false);
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(6);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("act_movimiento","ide_acmov", 2);
        tab_tabla2.getColumna("ide_aceaf").setCombo(ser_activos.getEstadoActivo());
        tab_tabla2.getColumna("ide_acafi").setCombo(ser_activos.getDatoActivoBasico());
        tab_tabla2.getColumna("ide_aceaf").setAutoCompletar();
        tab_tabla2.getColumna("ide_acafi").setAutoCompletar();
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel,pat_panel2,"30%","H");
        agregarComponente(div_division);
        
        set_activos.setId("set_activos");
	set_activos.setTitle("SELECCIONE LOS ACTIVOS FIJOS");
	set_activos.setSeleccionTabla(ser_activos.getSqlListaActivosFijos("1","-1","0","-1","1"),"ide_acafi");
	set_activos.getTab_seleccion().getColumna("observacion_acafi").setFiltro(true);
	set_activos.getTab_seleccion().getColumna("TIPO_ACTIVO").setFiltro(true);
        set_activos.getTab_seleccion().getColumna("observacion_acafi").setOrden(1);
	set_activos.getTab_seleccion().getColumna("TIPO_ACTIVO").setOrden(2);
	set_activos.getBot_aceptar().setMetodo("aceptarActivo");
        agregarComponente(set_activos);
    }
    public void importarActivos(){
        if(com_tipo_acta.getValue()==null){
            utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un tipo de acta");
            return;
		
	}
        String custodio_actual=tab_tabla.getValor("ide_geper");
        String estado_activo=utilitario.getVariable("p_act_estado_activo_valora_deprec");
        String clase_activo="1,2"; //indica los tipo de ACTIVO FIJO, Y BIENES DE CONTROL sie n la tabla cambian los codigos o generan nuevos se debe agregar aqui
        valor_combo=com_tipo_acta.getValue().toString();            
            if(valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))){
                       set_activos.getTab_seleccion().setSql(ser_activos.getSqlListaActivosFijos("1",custodio_actual,"1",estado_activo,clase_activo));
                       set_activos.getTab_seleccion().ejecutarSql();
                       set_activos.dibujar(); 
            }
            else if(valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))){
                TablaGenerica tab_activos_custodio =utilitario.consultar(ser_activos.getSqlListaActivosFijos("1",custodio_actual,"1",estado_activo,clase_activo));
                if(tab_activos_custodio.getTotalFilas()>0){
                    for(int i=0; i<tab_activos_custodio.getTotalFilas();i++){
                        tab_tabla2.insertar();
                        tab_tabla2.setValor(i, "ide_acafi", tab_activos_custodio.getValor(i, "ide_acafi"));
                        tab_tabla2.setValor(i, "ide_aceaf", tab_activos_custodio.getValor(i, "ide_aceaf"));
                    }
                    utilitario.addUpdate("tab_tabla2");
                }
                else{
                    utilitario.agregarMensajeInfo("No Registra Activos", "El custodio consultado no registra bienes a su cargo revise por favor");
                }
            }
            else if(valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))){
                       set_activos.getTab_seleccion().setSql(ser_activos.getSqlListaActivosFijos("1",custodio_actual,"1",estado_activo,clase_activo));
                       set_activos.getTab_seleccion().ejecutarSql();
                       set_activos.dibujar();
                
            }

    }
    
    	public void aceptarActivo(){

		String str_seleccionado = set_activos.getSeleccionados();
                TablaGenerica tabla_activos=utilitario.consultar("select * from act_activo_fijo where ide_acafi in ("+str_seleccionado+")");
		if (str_seleccionado!=null){
                        
                        for(int i=0; i<tabla_activos.getTotalFilas();i++){
                        tab_tabla2.insertar();
                        tab_tabla2.setValor(i, "ide_acafi", tabla_activos.getValor(i, "ide_acafi"));
                        tab_tabla2.setValor(i, "ide_aceaf", tabla_activos.getValor(i, "ide_aceaf"));
                        }
			utilitario.addUpdate("tab_tabla2");
				
		}
		else {
			utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
		    }
		
	}
    public void seleccionarTipoActa(){
		if(com_tipo_acta.getValue()!=null){
                        actualizarCampos();
			tab_tabla.setCondicion("ide_actia="+com_tipo_acta.getValue());
               		tab_tabla.ejecutarSql();
                        tab_tabla2.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
                        
                        //tab_tabla2.ejecutarSql();
		}
		else{
			tab_tabla.setCondicion("ide_actia=-1");
			tab_tabla.ejecutarSql();
		}
	}
    public void actualizarCampos(){
        tab_tabla.getColumna("observacion_acact").setLectura(true);
        utilitario.addUpdate("tab_tabla");
    }
    public void limpiar() {
        com_tipo_acta.limpiar();
        tab_tabla.limpiar();   
        tab_tabla2.limpiar();
    }
    String valor_combo="";
    String sec_modulo="";
    @Override
    public void insertar() {
   
        
        if(com_tipo_acta.getValue()==null){
            utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un tipo de acta");
            return;
		
	}
        if(tab_tabla.isFocus()){
            valor_combo=com_tipo_acta.getValue().toString();
            tab_tabla.insertar();
            tab_tabla.setValor("ide_actia",valor_combo);
            if(valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))){
                sec_modulo=utilitario.getVariable("p_act_secuecial_entrega_recepcion");
            }
            else if(valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))){
                sec_modulo=utilitario.getVariable("p_act_secuecial_constatacion");
            }
            else if(valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))){
                sec_modulo=utilitario.getVariable("p_act_secuecial_baja");
            }
            TablaGenerica tab_secuencial=utilitario.consultar(ser_valtiempo.getSecuencialModulo(sec_modulo));
            tab_tabla.setValor("secuencial_acact", tab_secuencial.getValor("nuevo_secuencial"));
            utilitario.addUpdate("tab_tabla");
        }
        else if(tab_tabla2.isFocus()){
            tab_tabla2.insertar();
        }
    }

    @Override
    public void guardar() {
        
        if(tab_tabla.guardar()){
            
            valor_combo=com_tipo_acta.getValue().toString();
            if (tab_tabla.isFilaInsertada()){
                
                if(valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))){
                    sec_modulo=utilitario.getVariable("p_act_secuecial_entrega_recepcion");
                }
                else if(valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))){
                    sec_modulo=utilitario.getVariable("p_act_secuecial_constatacion");
                }
                 else if(valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))){
                 sec_modulo=utilitario.getVariable("p_act_secuecial_baja");
                 }
                    utilitario.getConexion().ejecutarSql(ser_valtiempo.getActualizarSecuencial(sec_modulo));
            
                }
            
            if(tab_tabla2.guardar()){
                guardarPantalla();
            }
        }
        
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Dialogo getDia_activos() {
        return dia_activos;
    }

    public void setDia_activos(Dialogo dia_activos) {
        this.dia_activos = dia_activos;
    }

    public SeleccionTabla getSet_activos() {
        return set_activos;
    }

    public void setSet_activos(SeleccionTabla set_activos) {
        this.set_activos = set_activos;
    }

    public SeleccionTabla getSet_tipo_acta() {
        return set_tipo_acta;
    }

    public void setSet_tipo_acta(SeleccionTabla set_tipo_acta) {
        this.set_tipo_acta = set_tipo_acta;
    }
    
}

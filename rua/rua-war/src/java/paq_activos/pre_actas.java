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
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Tabla tab_acta_entrega = new Tabla();
    private Combo com_tipo_acta=new Combo();
    private Dialogo dia_activos= new Dialogo();
    private SeleccionTabla set_activos=new SeleccionTabla();
    private SeleccionTabla set_tipo_acta=new SeleccionTabla();
    private Dialogo dia_actas = new Dialogo();
    private Dialogo dia_radio = new Dialogo();
    private Radio rad_bloqueado= new Radio();
    @EJB
    private ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);
    @EJB
    private ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private final ServicioProduccion ser_valtiempo= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
    String condicion="";
    
            	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

    public pre_actas() { 
        
        ///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
        
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
        tab_tabla.setCampoOrden("ide_acact desc");
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
        tab_tabla.setLectura(true);
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
        
        dia_actas = new Dialogo();
        dia_actas.setTitle("DATOS PARA EL ACTA");
        dia_actas.setId("dia_actas");
        dia_actas.setHeight("45%");
        dia_actas.setWidth("80%");
        dia_actas.getBot_aceptar().setMetodo("guardarDialogo");
        agregarComponente(dia_actas);
        
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Bloquear Actas"
        };
        Object fila2[] = {
            "2", "Previsualizar"
        };
        lista.add(fila1);
        lista.add(fila2);
        rad_bloqueado = new Radio();
        rad_bloqueado.setRadio(lista);
        rad_bloqueado.setValue("2"); //Por defecto beneficiario
        
        dia_radio = new Dialogo();
        dia_radio.setTitle("SELECCIONE LA OPCION PARA IMPRIMIR EL ACTA");
        dia_radio.setId("dia_radio");
        dia_radio.setHeight("25%");
        dia_radio.setWidth("25%");
        dia_radio.setDialogo(dia_radio);
        dia_radio.getBot_aceptar().setMetodo("guardarDialogo");
        agregarComponente(dia_radio);
        
        
    }
    public void actaEntregaRecepcion(){
        
        if(com_tipo_acta.getValue()==null){
            utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un tipo de acta");
            return;
		
	}
        tab_acta_entrega.limpiar();
         dia_actas.getGri_cuerpo().getChildren().clear();
         valor_combo=com_tipo_acta.getValue().toString();

            if(valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))){
                sec_modulo=utilitario.getVariable("p_act_secuecial_entrega_recepcion");
            }
            else if(valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))){
                sec_modulo=utilitario.getVariable("p_act_secuecial_constatacion");
            }
            else if(valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))){
                sec_modulo=utilitario.getVariable("p_act_secuecial_baja");
            }
            else if(valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))){
                sec_modulo=utilitario.getVariable("p_act_secuecial_cambio");
            }
            TablaGenerica tab_secuencial=utilitario.consultar(ser_valtiempo.getSecuencialModulo(sec_modulo));
            
        
        tab_acta_entrega = new Tabla();
        tab_acta_entrega.setId("tab_acta_entrega");
        tab_acta_entrega.setTabla("act_acta_constata","ide_acact", 1);
        tab_acta_entrega.setCondicion("ide_actia=-1");
        tab_acta_entrega.getColumna("ide_gecas").setCombo(ser_activos.getCasa());
        tab_acta_entrega.getColumna("ide_geobr").setCombo(ser_activos.getObras());
        tab_acta_entrega.getColumna("ide_acuba").setCombo(ser_activos.getUbicacionActivo());
        tab_acta_entrega.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_acta_entrega.getColumna("gen_ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_acta_entrega.getColumna("ide_gecas").setAutoCompletar();
        tab_acta_entrega.getColumna("ide_geobr").setAutoCompletar();
        tab_acta_entrega.getColumna("ide_acuba").setAutoCompletar();
        tab_acta_entrega.getColumna("ide_geper").setAutoCompletar();
        tab_acta_entrega.getColumna("gen_ide_geper").setAutoCompletar();
        if(valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))){
                tab_acta_entrega.getColumna("ide_geobr").setVisible(false);
                tab_acta_entrega.getColumna("ide_gecas").setVisible(false);
                tab_acta_entrega.getColumna("ide_acuba").setVisible(false);
                tab_acta_entrega.getColumna("ide_geper").setVisible(false);
                tab_acta_entrega.getColumna("ide_geper").setValorDefecto(utilitario.getVariable("p_act_custodio"));
                tab_acta_entrega.getColumna("gen_ide_geper").setNombreVisual("CUSTODIO");
        }
        if(valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))|| valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))){
                tab_acta_entrega.getColumna("ide_geobr").setVisible(false);
                tab_acta_entrega.getColumna("ide_gecas").setVisible(false);
                tab_acta_entrega.getColumna("ide_acuba").setVisible(false);
        }
        tab_acta_entrega.getColumna("ide_geper").setRequerida(true);       
        tab_acta_entrega.getColumna("secuencial_acact").setRequerida(true);
        tab_acta_entrega.getColumna("secuencial_acact").setValorDefecto(tab_secuencial.getValor("nuevo_secuencial"));
        tab_acta_entrega.getColumna("bloqueado_acact").setLectura(true);
        tab_acta_entrega.getColumna("bloqueado_acact").setValorDefecto("false");
        tab_acta_entrega.getColumna("secuencial_acact").setEtiqueta();
        tab_acta_entrega.getColumna("secuencial_acact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
        tab_acta_entrega.getColumna("ide_actia").setVisible(false);
        tab_acta_entrega.getColumna("bloqueado_acact").setVisible(false);
        tab_acta_entrega.setTipoFormulario(true);
        tab_acta_entrega.getGrid().setColumns(4);
        tab_acta_entrega.dibujar();
        tab_acta_entrega.insertar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_acta_entrega);
        pat_panel.getMenuTabla().setRendered(false);
        pat_panel.setStyle("overflow:auto");
        
       
        dia_actas.setDialogo(pat_panel);
        dia_actas.dibujar();
        
        
    }
    public void guardarDialogo(){
        tab_acta_entrega.guardar();
        guardarPantalla();
        dia_actas.cerrar();
        tab_tabla.ejecutarSql();
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
                      tab_tabla.getColumna("observacion_acact").setVisible(true);  
                    //actualizarCampos();
			tab_tabla.setCondicion("ide_actia="+com_tipo_acta.getValue());
               		tab_tabla.ejecutarSql();
                        tab_tabla2.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
                        
                        //tab_tabla2.ejecutarSql();
		}
		else{
			tab_tabla.setCondicion("ide_actia=-1");
			tab_tabla.ejecutarSql();
		}
                utilitario.addUpdate("tab_tabla");
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
   
        actaEntregaRecepcion();/*
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
*/
    }
        //reporte
public void abrirListaReportes() {
    
        if(com_tipo_acta.getValue() == null){
            utilitario.agregarMensajeInfo("Seleccione el Tipo de Acta", "Seleccione el Año para poder continuar");
	
        }
        else {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
         }
}
public void aceptarReporte(){
    if(tab_tabla.getValor("dia_radio").equals("false")){
        dia_radio.dibujar();
    }
    else {
            if(rep_reporte.getReporteSelecionado().equals("Acta Entrega Recepcion")){
		if (rep_reporte.isVisible()){
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");
			p_parametros.put("ide_acact",tab_acta_entrega.getValor("ide_acact"));
                        p_parametros.put("nombre", utilitario.getVariable("NICK"));
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
                        self_reporte.dibujar();
		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}
        else if(rep_reporte.getReporteSelecionado().equals("Acta Constacion Fisica")){
                if (rep_reporte.isVisible()){
                    
              
                    rep_reporte.cerrar();
                    
		
                	p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","COMPROMISO PRESUPUESTARIA");
                        p_parametros.put("nombre", utilitario.getVariable("NICK"));
                        p_parametros.put("ide_acact",tab_acta_entrega.getValor("ide_acact"));
                        //System.out.println("paso parametrios "+p_parametros);
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
                        self_reporte.dibujar();
                    }
        }
else if(rep_reporte.getReporteSelecionado().equals("Acta de Bajas")){
                if (rep_reporte.isVisible()){
                    
              
                    rep_reporte.cerrar();
                    
		
                	p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","COMPROMISO PRESUPUESTARIA");
                        p_parametros.put("nombre", utilitario.getVariable("NICK"));
                        p_parametros.put("ide_acact",tab_acta_entrega.getValor("ide_acact"));
                        //System.out.println("paso parametrios "+p_parametros);
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
                        self_reporte.dibujar();
                    }
        }
else if(rep_reporte.getReporteSelecionado().equals("Acta Cambio de Custodio")){
                if (rep_reporte.isVisible()){
                    
              
                    rep_reporte.cerrar();
                    
		
                	p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","COMPROMISO PRESUPUESTARIA");
                        p_parametros.put("nombre", utilitario.getVariable("NICK"));
                        p_parametros.put("ide_acact",tab_acta_entrega.getValor("ide_acact"));
                        //System.out.println("paso parametrios "+p_parametros);
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
                        self_reporte.dibujar();
                    }
        }            
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

    public Tabla getTab_acta_entrega() {
        return tab_acta_entrega;
    }

    public void setTab_acta_entrega(Tabla tab_acta_entrega) {
        this.tab_acta_entrega = tab_acta_entrega;
    }

    public Dialogo getDia_actas() {
        return dia_actas;
    }

    public void setDia_actas(Dialogo dia_actas) {
        this.dia_actas = dia_actas;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSelf_reporte() {
        return self_reporte;
    }

    public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
        this.self_reporte = self_reporte;
    }

    public Dialogo getDia_radio() {
        return dia_radio;
    }

    public void setDia_radio(Dialogo dia_radio) {
        this.dia_radio = dia_radio;
    }
    
}

package paq_presupuesto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_presupuesto.ejb.ServicioPresupuesto;
import servicios.sistema.ServicioSeguridad;
import sistema.aplicacion.Pantalla;

public class pre_cedula_presupuestaria extends Pantalla {
	private Tabla tab_fuente_financiamiento=new Tabla();
        private SeleccionCalendario sel_cal = new SeleccionCalendario();
        private Combo com_tipo_cedula = new Combo();
        private Combo com_sucursal = new Combo ();
        private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
        private Check che_todos_emp=new Check();        
        @EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
        
        @EJB
	private ServicioSeguridad ser_seguridad=(ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);

	 public pre_cedula_presupuestaria() {
		// TODO Auto-generated constructor stub
		 bar_botones.getBot_insertar().setRendered(false);
                 bar_botones.getBot_guardar().setRendered(false);
		 bar_botones.getBot_eliminar().setRendered(false);
                 
                 che_todos_emp.setId("che_todos_emp");
		
		Etiqueta eti_todos_emp=new Etiqueta("Consolidado");
		bar_botones.agregarComponente(eti_todos_emp);
		bar_botones.agregarComponente(che_todos_emp);
                 
                                         		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
                 
                 com_sucursal.setId("com_sucursal");
                 com_tipo_cedula.setId("com_tipo_cedula");
                 List tipo_cedula = new ArrayList();
                 Object fila1[] ={
                        "1","CEDULA INGRESOS"
                 };
                 Object fila2[] ={
                     "0","CEDULA GASTOS"
                 };
                 tipo_cedula.add(fila1);
                 tipo_cedula.add(fila2);
                         
                 com_tipo_cedula.setCombo(tipo_cedula);
                 com_tipo_cedula.setMetodo("seleccionaElAnio");
                 bar_botones.agregarComponente(com_tipo_cedula);                 
                 com_sucursal.setCombo(ser_presupuesto.getFuncionPrograma("1"));
                 bar_botones.agregarComponente(com_sucursal);
                 
		 tab_fuente_financiamiento.setId("tab_fuente_financiamiento");
		 tab_fuente_financiamiento.setTabla("pre_cedula_presupuestaria", "ide_prcep", 1);
                 tab_fuente_financiamiento.setCondicion("tipo_cuenta_prcer=-1");
		 tab_fuente_financiamiento.setLectura(true);
                 tab_fuente_financiamiento.dibujar();
                 
		 PanelTabla pat_fuente_financiamiento=new PanelTabla();
		 pat_fuente_financiamiento.setPanelTabla(tab_fuente_financiamiento);
		 
		 agregarComponente(pat_fuente_financiamiento);
		  
		  sel_cal.setId("sel_cal");
                  agregarComponente(sel_cal);
                  
                  
		Boton bot_agregar=new Boton();
		bot_agregar.setValue("EJECUTAR CEDULA PRESUPUESTARIA");
		bot_agregar.setMetodo("importar");
		bar_botones.agregarBoton(bot_agregar);
	}
public void seleccionaElAnio (){
		if(com_tipo_cedula.getValue()!=null){
                    tab_fuente_financiamiento.setCondicion("tipo_cuenta_prcer="+com_tipo_cedula.getValue());
                    tab_fuente_financiamiento.ejecutarSql();


		}
		else{
			utilitario.agregarMensajeInfo("Selecione el Tipo de Cedula Presupuestaria", "");

		}
	}
		
public void importar(){
            if(com_tipo_cedula.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar el tipo de Cedula Presupuestaria a Generar", "");
			return;
		}
            else {
		sel_cal.setTitle("GENERAR CEDULAS PRESUPUESTARIAS");
		sel_cal.setFooter("Recuerde que si ya existen generado las cedulas presupuestarias se volveran a sobrescribir");
		sel_cal.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_cal.setFecha2(utilitario.sumarDiasFecha(utilitario.getDate(), -1));
		sel_cal.getBot_aceptar().setMetodo("aceptarImportar");
		sel_cal.dibujar();
            }
	}
        String selecciono_casas="1";
        String casa="-1";
	public void aceptarImportar(){
		//int int_total=ser_control.resumirAsistencia(sec_importar.getFecha1String(), sec_importar.getFecha2String());		
		
                if(che_todos_emp.getValue()!=null && !che_todos_emp.getValue().toString().isEmpty()
				&& che_todos_emp.getValue().toString().equalsIgnoreCase("true")){
                    selecciono_casas="0";
                }
                else{
                    selecciono_casas="1";
                    casa =com_sucursal.getValue().toString();
                }
                
                //System.out.println("valores seleccionados "+selecciono_casas+" ide casas "+casa);
                if(sel_cal.getFecha1().getYear() != sel_cal.getFecha2().getYear()){
                    //System.out.println("entre ");
                    utilitario.agregarMensajeInfo("Debe seleccionar fechas dentro del rengo de un mismo año", "");
			return;
                }
                else if(com_tipo_cedula.getValue().toString().equals("0")){
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getEliminaCedulaRua());
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaCedulaRua(com_tipo_cedula.getValue().toString(), sel_cal.getFecha1String(), sel_cal.getFecha2String()));
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaInicialGastosRua(selecciono_casas,casa));
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaReformaGastoRua(sel_cal.getFecha1String(), sel_cal.getFecha2String(),selecciono_casas,casa));
                    if(sel_cal.getFecha1().getMonth() == sel_cal.getFecha2().getMonth()){ //mismo mes
                       utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaEjecucionGastoPeriodoRua(sel_cal.getFecha1String(), sel_cal.getFecha2String(), "2",selecciono_casas,casa));
                    }
                    else {
                        TablaGenerica tab_meses=utilitario.consultar("select 1 as codigo,extract (year from cast('"+sel_cal.getFecha2String()+"' as date))||'' as anio,extract (month from cast('"+sel_cal.getFecha1String()+"'as date))||'' as mes_inicial,extract (month from cast('"+sel_cal.getFecha2String()+"'as date))||'' as mes_final");
                        int mes =Integer.parseInt(tab_meses.getValor("mes_final"))-1;
                        int nuro_dias=utilitario.getNrodias(mes, Integer.parseInt(tab_meses.getValor("anio")));
                        String fecha_final_acumulado =tab_meses.getValor("anio")+"-"+mes+"-"+nuro_dias;
                        String fecha_inicial_acumulado=tab_meses.getValor("anio")+"-"+tab_meses.getValor("mes_final")+"-01";
                       utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaEjecucionGastoPeriodoRua(fecha_inicial_acumulado, sel_cal.getFecha2String(), "2",selecciono_casas,casa));
                       utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaEjecucionGastoPeriodoRua(sel_cal.getFecha1String(), fecha_final_acumulado, "1",selecciono_casas,casa));
                        
                    }
                    TablaGenerica tab_nivel =utilitario.consultar("select 1 as codigo,max(nivel_prcla) as nivel from pre_clasificador where tipo_prcla ="+com_tipo_cedula.getValue().toString());
                    int nivel= Integer.parseInt(tab_nivel.getValor("nivel"));
                    while(nivel>1){
                        utilitario.getConexion().ejecutarSql(ser_presupuesto.getActualizaSaldoRua(nivel));
                        nivel --;
                    }
                }
                
                else if(com_tipo_cedula.getValue().toString().equals("1")){ //// GENERA CEDULA PRESUPUESTARIA DE INGRESOS
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getEliminaCedulaRua());
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaCedulaRua(com_tipo_cedula.getValue().toString(), sel_cal.getFecha1String(), sel_cal.getFecha2String()));
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaInicialIngresosRua());
                    utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaReformaIngresoRua(sel_cal.getFecha1String(), sel_cal.getFecha2String(),selecciono_casas,casa));
                    if(sel_cal.getFecha1().getMonth() == sel_cal.getFecha2().getMonth()){ //mismo mes
                       utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaEjecucionIngresoPeriodoRua(sel_cal.getFecha1String(), sel_cal.getFecha2String(), "2"));
                    }
                    else {
                        TablaGenerica tab_meses=utilitario.consultar("select 1 as codigo,extract (year from cast('"+sel_cal.getFecha2String()+"' as date))||'' as anio,extract (month from cast('"+sel_cal.getFecha1String()+"'as date))||'' as mes_inicial,extract (month from cast('"+sel_cal.getFecha2String()+"'as date))||'' as mes_final");
                        int mes =Integer.parseInt(tab_meses.getValor("mes_final"))-1;
                        int nuro_dias=utilitario.getNrodias(mes, Integer.parseInt(tab_meses.getValor("anio")));
                        String fecha_final_acumulado =tab_meses.getValor("anio")+"-"+mes+"-"+nuro_dias;
                        String fecha_inicial_acumulado=tab_meses.getValor("anio")+"-"+tab_meses.getValor("mes_final")+"-01";
                       utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaEjecucionIngresoPeriodoRua(fecha_inicial_acumulado, sel_cal.getFecha2String(), "2"));
                       utilitario.getConexion().ejecutarSql(ser_presupuesto.getInsertaEjecucionIngresoPeriodoRua(sel_cal.getFecha1String(), fecha_final_acumulado, "1"));
                        
                    }
                    TablaGenerica tab_nivel =utilitario.consultar("select 1 as codigo,max(nivel_prcla) as nivel from pre_clasificador where tipo_prcla ="+com_tipo_cedula.getValue().toString());
                    int nivel= Integer.parseInt(tab_nivel.getValor("nivel"));
                    while(nivel>1){
                        utilitario.getConexion().ejecutarSql(ser_presupuesto.getActualizaSaldoRua(nivel));
                        nivel --;
                    }
                }
                utilitario.getConexion().ejecutarSql(ser_presupuesto.getActualizaSaldosFinalesRua());
                tab_fuente_financiamiento.setCondicion("tipo_cuenta_prcer="+com_tipo_cedula.getValue());
                tab_fuente_financiamiento.ejecutarSql();
                sel_cal.cerrar();
                
		
				
	}
        
   //reporte
public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}
public void aceptarReporte(){
	if(rep_reporte.getReporteSelecionado().equals("Cedula Ingresos")){
		if (rep_reporte.isVisible()){
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
			
		self_reporte.dibujar();
		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}
        else if(rep_reporte.getReporteSelecionado().equals("Cedula Gastos")){
		if (rep_reporte.isVisible()){
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");
			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
			
		self_reporte.dibujar();
		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}	
}         
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_fuente_financiamiento.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_fuente_financiamiento.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_fuente_financiamiento.eliminar();
		
	}


	public Tabla getTab_fuente_financiamiento() {
		return tab_fuente_financiamiento;
	}


	public void setTab_fuente_financiamiento(Tabla tab_fuente_financiamiento) {
		this.tab_fuente_financiamiento = tab_fuente_financiamiento;
	}

    public SeleccionCalendario getSel_cal() {
        return sel_cal;
    }

    public void setSel_cal(SeleccionCalendario sel_cal) {
        this.sel_cal = sel_cal;
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
	

}

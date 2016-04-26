package paq_nomina;


import java.awt.geom.Area;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.primefaces.component.dialog.Dialog;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

public class pre_interfaz_contabilidad extends Pantalla {

	private Tabla tab_mov_contable=new Tabla();
	private Tabla tab_det_mov=new Tabla();
	private Confirmar con_guardar=new Confirmar();
	private SeleccionTabla set_nomina=new SeleccionTabla();

	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();

	private Combo com_periodo=new Combo();

	private Dialogo dia_razon_anula=new Dialogo();
	private AreaTexto are_razon_anula=new AreaTexto();

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioGestion serv_gestion=(ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	public pre_interfaz_contabilidad(){

		bar_botones.agregarReporte();


		com_periodo.setId("com_periodo");
		com_periodo.setCombo("SELECT PER.IDE_GEPRO,mes.nombre_gemes,ani.nom_geani,TIR.DETALLE_NRTIT, " +
				"PER.FECHA_INICIAL_GEPRO,PER.FECHA_FINAL_GEPRO " +
				"FROM GEN_PERIDO_ROL PER " +
				"INNER JOIN NRH_TIPO_ROL TIR ON TIR.IDE_NRTIT=PER.IDE_NRTIT " +
				"INNER JOIN GEN_MES MES ON MES.IDE_GEMES=PER.IDE_GEMES " +
				"INNER JOIN GEN_ANIO ANI ON ANI.IDE_GEANI=PER.IDE_GEANI WHERE ACTIVO_GEPRO=TRUE "+
				"ORDER BY mes.IDE_GEMES ASC");

		com_periodo.setMetodo("seleccionaPeriodo");

		bar_botones.agregarComponente(new Etiqueta("P�riodo Rol:"));
		bar_botones.agregarComponente(com_periodo);

		tab_mov_contable.setId("tab_mov_contable");
		tab_mov_contable.setTabla("CNT_MOVIMIENTO_CONTABLE", "IDE_CNMOC", 1);
		tab_mov_contable.getColumna("IDE_CNTIC").setCombo("CNT_TIPO_COMPROBANTE","IDE_CNTIC","DETALLE_CNTIC","");
		tab_mov_contable.getColumna("IDE_GEANI").setVisible(false);
		tab_mov_contable.getColumna("IDE_GEMES").setVisible(false);
		tab_mov_contable.getColumna("IDE_GEBEN").setVisible(false);
		tab_mov_contable.getColumna("IDE_GEBEN").setCombo("GEN_BENEFICIARIO","IDE_GEBEN","TITULAR_GEBEN","");
		tab_mov_contable.getColumna("IDE_GETIA").setCombo("GEN_TIPO_ASIENTO","IDE_GETIA","DETALLE_GETIA","");
		tab_mov_contable.getColumna("IDE_GETIA").setRequerida(true);
		tab_mov_contable.getColumna("IDE_USUA").setCombo("SIS_USUARIO","IDE_USUA","NOM_USUA","");
		tab_mov_contable.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("IDE_USUA"));
		tab_mov_contable.getColumna("IDE_USUA").setLectura(true);
		tab_mov_contable.getColumna("FECHA_MOVIMIENTO_CNMOC").setValorDefecto(utilitario.getFechaActual());
		tab_mov_contable.getColumna("FECHA_MOVIMIENTO_CNMOC").setRequerida(true);
		tab_mov_contable.getColumna("ACTIVO_CNMOC").setLectura(true);
		tab_mov_contable.getColumna("ACTIVO_CNMOC").setCheck();
		tab_mov_contable.getColumna("ACTIVO_CNMOC").setValorDefecto("false");
		tab_mov_contable.getColumna("ANULADO_CNMOC").setLectura(true);
		tab_mov_contable.getColumna("ANULADO_CNMOC").setCheck();
		tab_mov_contable.getColumna("ANULADO_CNMOC").setValorDefecto("false");
		tab_mov_contable.getColumna("RAZON_ANULACION_CNMOC").setLectura(true);
		tab_mov_contable.getColumna("CMC_ID").setLectura(true);
		tab_mov_contable.agregarRelacion(tab_det_mov);
		tab_mov_contable.setCondicion("IDE_GEANI=-1 AND IDE_GEMES=-1");
		tab_mov_contable.setTipoFormulario(true);
		tab_mov_contable.getGrid().setColumns(6);
		tab_mov_contable.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_mov_contable);

		tab_det_mov.setId("tab_det_mov");
		tab_det_mov.setTabla("CNT_DETALLE_MOVIMIENTO", "IDE_CNDEM", 2);
		tab_det_mov.getColumna("IDE_GECUC").setCombo("GEN_CUENTA_CONTABLE","IDE_GECUC","CODIGO_CUENTA_GECUC,DETALLE_GECUC","");
		tab_det_mov.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE","DETALLE_GEARE","");
		tab_det_mov.getColumna("ACTIVO_CNDEM").setCheck();
		tab_det_mov.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL","IDE_SUCU","NOM_SUCU","");
		tab_det_mov.setMostrarcampoSucursal(true);
		tab_det_mov.getColumna("ACTIVO_CNDEM").setValorDefecto("true");
		tab_det_mov.getColumna("HABER_CNDEM").setFormatoNumero(2);
		tab_det_mov.getColumna("DEBE_CNDEM").setFormatoNumero(2);
		tab_det_mov.setColumnaSuma("DEBE_CNDEM,HABER_CNDEM");

		tab_det_mov.setRecuperarLectura(true);
		tab_det_mov.dibujar();


		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_det_mov);


		Division div=new Division();
		div.dividir2(pat_panel1,pat_panel2,"30%","H");


		con_guardar.setId("con_guardar");
		con_guardar.setMessage("Ya se encuentra generado un asiento, Desea sobreescribir el asiento");
		con_guardar.setTitle("CONFIRMACION INTERFAZ ENTRE NOMINA Y CONTABILIDAD");
		con_guardar.getBot_aceptar().setMetodo("abrirNominasGeneradas");

		agregarComponente(con_guardar);

		Boton bot_generar_asiento=new Boton();
		bot_generar_asiento.setTitle("Generar Asiento");
		bot_generar_asiento.setValue("Generar Asiento");
		bot_generar_asiento.setMetodo("abrirNominasGeneradas");
		bar_botones.agregarBoton(bot_generar_asiento); 


		Boton bot_cerrar_asiento=new Boton();
		bot_cerrar_asiento.setTitle("Cerrar Asiento");
		bot_cerrar_asiento.setValue("Cerrar Asiento");
		bot_cerrar_asiento.setMetodo("cerrarAsiento");
		bar_botones.agregarBoton(bot_cerrar_asiento); 


		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);


		set_nomina.setId("set_nomina");
		set_nomina.setSeleccionTabla("SELECT DTN.IDE_NRDTN,DETALLE_NRTIN,DETALLE_GTTEM " +
				"FROM NRH_DETALLE_TIPO_NOMINA DTN " +
				"INNER JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"where DTN.IDE_NRTIN in (select IDE_NRTIN from GEN_TIPO_ASIENTO where IDE_GETIA=-1)","IDE_NRDTN");
		set_nomina.getBot_aceptar().setMetodo("aceptarReporte");
		set_nomina.setTitle("SELECCIONE TIPO DE NOMINA");
		set_nomina.setWidth("50%");
		set_nomina.setHeight("50%");
		set_nomina.setRadio();
		set_nomina.setDynamic(false);
		agregarComponente(set_nomina);

		agregarComponente(div);

		Boton bot_core=new Boton();
		bot_core.setMetodo("enviarCore");
		//bot_core.setValue("Enviar al Core Bancario");
		bar_botones.agregarBoton(bot_core);


		Boton bot_anular=new Boton();
		bot_anular.setMetodo("anularInterfaz");
		bot_anular.setValue("Anular");
		bar_botones.agregarBoton(bot_anular);


		dia_razon_anula.setId("dia_razon_anula");
		dia_razon_anula.setTitle("RAZON ANULACION INTERFAZ CONTABLE");
		Grid gri_raz_anula=new Grid();
		gri_raz_anula.setColumns(2);
		are_razon_anula.setStyle("width: 500 px");
		gri_raz_anula.getChildren().add(new Etiqueta("Razon Anula: "));
		gri_raz_anula.getChildren().add(are_razon_anula);
		dia_razon_anula.setDialogo(gri_raz_anula);
		dia_razon_anula.setWidth("30");
		dia_razon_anula.setHeight("25");
		agregarComponente(dia_razon_anula);
	}




	public void enviarCore(){

		if (tab_mov_contable.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz","No existe movimiento contable en pantalla");
			return;
		}

		if (tab_det_mov.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz","No existe movimiento contable en pantalla");
			return;
		}

		if (Double.parseDouble(utilitario.getFormatoNumero(tab_det_mov.getSumaColumna("HABER_CNDEM")))
				!= Double.parseDouble(utilitario.getFormatoNumero(tab_det_mov.getSumaColumna("DEBE_CNDEM")))){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "El asiento no cuadra");
			return;
		}

		if (tab_mov_contable.getValor("ACTIVO_CNMOC")==null || tab_mov_contable.getValor("ACTIVO_CNMOC").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "El asiento no se encuentra cerrado");
			return;
		}

		if (tab_mov_contable.getValor("ACTIVO_CNMOC")!=null && !tab_mov_contable.getValor("ACTIVO_CNMOC").isEmpty()
				&& tab_mov_contable.getValor("ACTIVO_CNMOC").equalsIgnoreCase("false")){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "El asiento no se encuentra cerrado");
			return;
		}



		if (utilitario.getVariable("p_CMC_MODULO_MALLA")==null || utilitario.getVariable("p_CMC_MODULO_MALLA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "No existe el parametro cmc_modulo para el envio de interfaz al core bancario");
			return;
		}

		if (utilitario.getVariable("p_CMC_PROCESO_MALLA")==null || utilitario.getVariable("p_CMC_PROCESO_MALLA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "No existe el parametro cmc_PROCESO para el envio de interfaz al core bancario");
			return;
		}


		if (utilitario.getVariable("p_CMC_OPERADOR_MALLA")==null || utilitario.getVariable("p_CMC_OPERADOR_MALLA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "No existe el parametro cmc_operador para el envio de interfaz al core bancario");
			return;
		}

		if (utilitario.getVariable("p_CMC_ESTADO_MALLA")==null || utilitario.getVariable("p_CMC_ESTADO_MALLA").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "No existe el parametro cmc_estado para el envio de interfaz al core bancario");
			return;
		}

		if (tab_mov_contable.getValor("CMC_ID")!=null && !tab_mov_contable.getValor("CMC_ID").isEmpty()){

			utilitario.agregarMensajeInfo("No se puede enviar la interfaz", "El asiento ya se envio al core bancario");
			return;
		}


		if (!con_guardar.isVisible()){



			con_guardar.setMessage("ESTA SEGURO DE ENVIAR LA INTERFAZ SELECCIONADA AL CORE BANCARIO");
			con_guardar.setTitle("CONFIRMACION ENVIO DE INTERFAZ AL CORE BANCARIO");
			con_guardar.getBot_aceptar().setMetodo("enviarCore");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");

		}else{
			con_guardar.cerrar();

			try {


				Conexion con_core=new Conexion();
				con_core.setUnidad_persistencia("core");

				//consulta detalle movimiento contable a enviar(SISTEMA BIESS) 
				TablaGenerica tab_det_m=utilitario.consultar("select sum (debe_cndem) as debe_cndem,sum(HABER_CNDEM) as haber_cndem,ide_gelua,CODIGO_CUENTA_GECUC,CODIGO_CORE_GEARE "+
						" from ( " +
						" SELECT DEM.IDE_CNDEM," +
						"DEBE_CNDEM,HABER_CNDEM,IDE_GELUA ,ARE.IDE_GEARE,ARE.CODIGO_CORE_GEARE " +
						",CUC.CODIGO_CUENTA_GECUC " +
						"from CNT_DETALLE_MOVIMIENTO DEM " +
						"LEFT JOIN GEN_AREA ARE ON ARE.IDE_GEARE=DEM.IDE_GEARE " +
						"LEFT JOIN GEN_CUENTA_CONTABLE CUC ON CUC.IDE_GECUC=DEM.IDE_GECUC " +
						"where IDE_CNMOC="+tab_mov_contable.getValorSeleccionado()+" "+
						") a "+
						"group by ide_gelua,CODIGO_CUENTA_GECUC,CODIGO_CORE_GEARE ");

				// proceso llenar tabla cabecera

				//Clave primaria de la malla
				long CMC_ID=0;

				List rset=con_core.consultar("SELECT max(CMC_ID) AS NUM_MAX from MAL_CABECERAMC_TBL");
				try {
					if (rset.get(0)!=null){
						CMC_ID=Long.parseLong(rset.get(0)+"")+1;
					}else{
						CMC_ID=CMC_ID+1;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				
				// codigo del proceso contable 
				String cmc_proceso="PRO";
				try {
					if (ser_nomina.getTipoNominaContable(tab_mov_contable.getValorSeleccionado()).getTotalFilas()>0){
						cmc_proceso=ser_nomina.getTipoNominaContable(tab_mov_contable.getValorSeleccionado()).getValor("PROCESO_CORE_GTTEM");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				String str_insert_cabecera_malla="INSERT INTO MAL_CABECERAMC_TBL (" +
						"CMC_ID," +
						"CMC_MODULO, "+
						"CMC_PROCESO, "+
						"CMC_OPERADOR, "+
						"CMC_ESTADO, "+
						"CMC_REGISTROS_PRO," +
						"CMC_TOTAL_DEBITO," +
						"CMC_OBSERVACION," +
						"CMC_PERIODO," +
						"CMC_FEC_COT," +
						"CMC_FEC_GENERACION," +
						"CMC_TOTAL_CREDITO" +
						") " +
						"VALUES ( " +
						""+CMC_ID+", " +
						"'"+utilitario.getVariable("p_CMC_MODULO_MALLA")+"', "+
						"'"+cmc_proceso+"', "+
						"'"+utilitario.getVariable("p_CMC_OPERADOR_MALLA")+"', "+
						"'"+utilitario.getVariable("p_CMC_ESTADO_MALLA")+"', "+
						""+tab_det_m.getTotalFilas()+""+", " +
						""+utilitario.getFormatoNumero(tab_det_mov.getSumaColumna("DEBE_CNDEM"))+", " +
						"'PROCESO: [NOMINA] DE ["+utilitario.getNombreMes(Integer.parseInt(tab_mov_contable.getValor("IDE_GEMES")))+" "+ser_nomina.getAnio(tab_mov_contable.getValor("IDE_GEANI")).getValor("nom_geani")+"]', "+
						""+ser_nomina.getAnio(tab_mov_contable.getValor("IDE_GEANI")).getValor("nom_geani")+", " +
						"to_date('"+tab_mov_contable.getValor("FECHA_MOVIMIENTO_CNMOC")+"','yy-mm-dd'), " +
						"to_date('"+utilitario.getFechaActual()+"','yy-mm-dd'), " +
						""+utilitario.getFormatoNumero(tab_det_mov.getSumaColumna("HABER_CNDEM"))+" " +
						")";

				System.out.println("insert mal cabecera "+str_insert_cabecera_malla);
				String msg_insert_cabecera=con_core.ejecutarSql(str_insert_cabecera_malla);
				if (!msg_insert_cabecera.isEmpty()){
					utilitario.agregarNotificacionInfo("No se pudo enviar la interfaz al core bancario","No se pudo insertar en la tabla MAL_CABECERAMC_TBL "+msg_insert_cabecera);
//					con_core.desconectar();
					return ;
				}


				// proceso para llenar tabla detalle de core



				//Clave primaria del detalle de la malla
				long MC_ID=0;
				List rset1=con_core.consultar("SELECT max(MC_ID) AS NUM_MAX from CN_MALLASCARGA");
				try {
					if (rset1.get(0)!=null){
						MC_ID=Long.parseLong(rset1.get(0)+"")+1;
					}else{
						MC_ID=MC_ID+1;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}



				//Proceso de insertar en la tabla detalle malla del core

				for (int i = 0; i < tab_det_m.getTotalFilas(); i++) {
					String str_deb_cred="";
					if (tab_det_m.getValor(i, "IDE_GELUA").equalsIgnoreCase("1")){
						str_deb_cred="D";
					}else{
						str_deb_cred="C";
					}

					String str_insert_detalle_malla="insert into CN_MALLASCARGA ( " +
							"MC_ID, " +
							"MC_IDMALLA, " +
							"MC_COD_SEC_MOV, " +
							"MC_COD_CTA, " +
							"MC_COD_C_COSTO, " +
							"MC_CRED_LOCAL, " +
							"MC_DEB_LOCAL, " +
							"MC_FEC_COT, " +
							"MC_DEBCRED, " +
							"MC_ANEXO" +
							") " +
							"values ( " +
							""+(MC_ID+i)+", " +
							""+CMC_ID+", " +
							""+(i+1)+", " +
							"'"+tab_det_m.getValor(i, "CODIGO_CUENTA_GECUC")+"', " +
							"'"+tab_det_m.getValor(i, "CODIGO_CORE_GEARE")+"', " +
							""+utilitario.getFormatoNumero(tab_det_m.getValor(i, "HABER_CNDEM"))+", " +
							""+utilitario.getFormatoNumero(tab_det_m.getValor(i, "DEBE_CNDEM"))+", " +
							"to_date('"+utilitario.getFechaActual()+"','yy-mm-dd'), " +
							"'"+str_deb_cred+"', " +
							"null)";

					con_core.agregarSql(str_insert_detalle_malla);
				}

				System.out.println("det malla "+con_core.getSqlPantalla());		

				String msg_insert_det=con_core.ejecutarListaSql();

				if (msg_insert_det.isEmpty()){

					String ide_cnmoc=tab_mov_contable.getValorSeleccionado();
					utilitario.getConexion().ejecutarSql("update CNT_MOVIMIENTO_CONTABLE set CMC_ID="+CMC_ID+" where IDE_CNMOC="+tab_mov_contable.getValorSeleccionado());
					utilitario.agregarMensaje("La interfaz se envio correctamente","");
					TablaGenerica gen_periodo=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
					tab_mov_contable.setCondicion("IDE_GEANI="+gen_periodo.getValor("IDE_GEANI")+" AND IDE_GEMES="+gen_periodo.getValor("IDE_GEMES")+"");
					tab_mov_contable.ejecutarSql();
					tab_mov_contable.setFilaActual(ide_cnmoc);
					tab_det_mov.ejecutarValorForanea(tab_mov_contable.getValor("IDE_CNMOC"));
					tab_det_mov.sumarColumnas();
				}else{
//					con_core.ejecutarSql("delete from MAL_CABECERAMC_TBL where CMC_ID="+CMC_ID);
					utilitario.agregarNotificacionInfo("No se pudo enviar la interfaz al core bancario","No se pudo insertar en la tabla CN_MALLASCARGA "+msg_insert_det);
//					con_core.desconectar();
					return;
				}
				//Fin del Proceso
//				con_core.desconectar();
			} catch (Exception e) {
				// TODO: handle exception
				utilitario.agregarNotificacionInfo("No se puede enviar la interfaz al core bancario", "No se puede establecer la conexion con el core bancario, Revisar que la persistencia core exista");

			}

		}


	}



	public void cerrarAsiento(){

		if (com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede cerrar el asiento", "Primero debe seleccionar un periodo");
			return;
		}
		if (tab_mov_contable.getTotalFilas()==0 ){
			utilitario.agregarMensajeInfo("No se puede cerrar el asiento", "No se existe movimiento contable");
			return;
		}

		for (int i = 0; i < tab_mov_contable.getTotalFilas(); i++) {
			if (tab_mov_contable.isFilaInsertada(i)){
				utilitario.agregarMensajeInfo("No se puede cerrar el asiento", "Primero debe guardar la transaccion");
				return;
			}
		}

		if (tab_det_mov.getTotalFilas()==0 ){
			utilitario.agregarMensajeInfo("No se puede cerrar el asiento", "No se existe movimiento contable");
			return;
		}

		if (tab_mov_contable.getValor("ACTIVO_CNMOC")!=null && !tab_mov_contable.getValor("ACTIVO_CNMOC").isEmpty()
				&& (tab_mov_contable.getValor("ACTIVO_CNMOC").equalsIgnoreCase("true") || tab_mov_contable.getValor("ACTIVO_CNMOC").equalsIgnoreCase("true"))){
			utilitario.agregarMensajeInfo("No se puede cerrar el asiento", "El asiento ya se encuentra cerrado");
			return;
		}


		if (Double.parseDouble(utilitario.getFormatoNumero(tab_det_mov.getSumaColumna("HABER_CNDEM")))
				!= Double.parseDouble(utilitario.getFormatoNumero(tab_det_mov.getSumaColumna("DEBE_CNDEM")))){
			utilitario.agregarMensajeInfo("No se puede cerrar el asiento", "El asiento no cuadra");
			return;
		}


		if (con_guardar.isVisible()){
			con_guardar.cerrar();
			utilitario.getConexion().agregarSqlPantalla("update CNT_MOVIMIENTO_CONTABLE set activo_cnmoc=true where ide_cnmoc="+tab_mov_contable.getValorSeleccionado());
			
			utilitario.getConexion().guardarPantalla();

			String ide_cnmoc=tab_mov_contable.getValorSeleccionado();
			TablaGenerica gen_periodo=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
			tab_mov_contable.setCondicion("IDE_GEANI="+gen_periodo.getValor("IDE_GEANI")+" AND IDE_GEMES="+gen_periodo.getValor("IDE_GEMES")+"");
			tab_mov_contable.ejecutarSql();
			tab_mov_contable.setFilaActual(ide_cnmoc);
			tab_det_mov.ejecutarValorForanea(tab_mov_contable.getValor("IDE_CNMOC"));

		}else{
			con_guardar.setMessage("ESTA SEGURO DE CERRAR EL ASIENTO");
			con_guardar.setTitle("CONFIRMACION CIERRE DE ASIENTO");
			con_guardar.getBot_aceptar().setMetodo("cerrarAsiento");

			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");

		}




	}

	public void seleccionaPeriodo(){

		if (com_periodo.getValue()!=null){
			TablaGenerica gen_periodo=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
			tab_mov_contable.setCondicion("IDE_GEANI="+gen_periodo.getValor("IDE_GEANI")+" AND IDE_GEMES="+gen_periodo.getValor("IDE_GEMES")+"");
			tab_mov_contable.ejecutarSql();

			tab_det_mov.ejecutarValorForanea(tab_mov_contable.getValor("IDE_CNMOC"));

		}else{
			tab_mov_contable.setCondicion("IDE_GEANI=-1 AND IDE_GEMES=-1");
			tab_mov_contable.ejecutarSql();
		}
		calcularTotales();
	}


	public void calcularTotales(){



		tab_det_mov.ejecutarValorForanea(tab_mov_contable.getValorSeleccionado());
		tab_det_mov.getColumna("HABER_CNDEM").setTotal(tab_det_mov.getSumaColumna("HABER_CNDEM"));
		tab_det_mov.getColumna("DEBE_CNDEM").setTotal(tab_det_mov.getSumaColumna("DEBE_CNDEM"));

	}
	Map parametro = new HashMap();



	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		if (rep_reporte.getReporteSelecionado().equals("Reporte de Contabilizacion")){
			if (rep_reporte.isVisible()){
				parametro=new HashMap();
				rep_reporte.cerrar();					
				set_nomina.getTab_seleccion().setSql("SELECT DTN.IDE_NRDTN,DETALLE_NRTIN,DETALLE_GTTEM " +
						"FROM NRH_DETALLE_TIPO_NOMINA DTN " +
						"INNER JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
						"INNER JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
						"where DTN.IDE_NRTIN in (select IDE_NRTIN from GEN_TIPO_ASIENTO where IDE_GETIA="+tab_mov_contable.getValor("IDE_GETIA")+")");
				set_nomina.getTab_seleccion().ejecutarSql();
				set_nomina.getBot_aceptar().setMetodo("aceptarReporte");
				set_nomina.setRadio();
				set_nomina.dibujar();


			}else if (set_nomina.isVisible()){
				if (set_nomina.getValorSeleccionado()!=null && !set_nomina.getValorSeleccionado().isEmpty()){
					parametro.put("ide_cnmoc",tab_mov_contable.getValorSeleccionado());
					parametro.put("ide_nrdtn",set_nomina.getValorSeleccionado());

					TablaGenerica tab_det_tip_nom=ser_nomina.getDetalleTipoNominaNombres(set_nomina.getValorSeleccionado());
					String nombre_nomina="";
					for (int i = 0; i < tab_det_tip_nom.getTotalFilas(); i++) {
						nombre_nomina+=tab_det_tip_nom.getValor(i, "DETALLE_GTTEM")+" - ";
					}
					nombre_nomina=nombre_nomina.substring(0, nombre_nomina.length()-2);

					parametro.put("nombre_tipo_nomina",nombre_nomina);
					String nom_mes=serv_gestion.getMes(tab_mov_contable.getValor("IDE_GEMES")).getValor("nombre_gemes");
					String anio=serv_gestion.getAnio(tab_mov_contable.getValor("IDE_GEANI")).getValor("nom_geani");
					parametro.put("nombre_mes",nom_mes);
					parametro.put("anio",anio);

					System.out.println("ide moc "+tab_mov_contable.getValorSeleccionado());
					System.out.println("ide dtn "+set_nomina.getValorSeleccionado());
					System.out.println("nom mes "+nom_mes);
					System.out.println("anio "+anio);

					set_nomina.cerrar();
					sef_reporte.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
					sef_reporte.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar al menos un tipo de nomina");
				}
			}
		}	
	}

	boolean boo_sobreescribir=false;
	public void generarAsiento(){
		if (boo_sobreescribir){
			utilitario.getConexion().ejecutarSql("delete from CNT_DETALLE_MOVIMIENTO where IDE_CNMOC="+tab_mov_contable.getValor("IDE_CNMOC"));
		}

		TablaGenerica tab_haber=utilitario.consultar("select " +
				"rua.IDE_GEARE,SUC.IDE_SUCU,ARE.DETALLE_GEARE,CUC.IDE_GECUC,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB, " +
				"CASE WHEN IDE_GELUA="+utilitario.getVariable("p_gen_lugar_aplica_debe")+" THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS DEBE , " +
				"CASE WHEN IDE_GELUA="+utilitario.getVariable("p_gen_lugar_aplica_haber")+" THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS HABER, " +
				"ROL.IDE_NRDTN "+
				"from NRH_ROL ROL " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON DTN.IDE_NRTIN=TIN.IDE_NRTIN " +
				"INNER JOIN NRH_DETALLE_ROL DRO ON DRO.IDE_NRROL=ROL.IDE_NRROL " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_RUBRO_ASIENTO RUA ON RUA.IDE_NRRUB=RUB.IDE_NRRUB " +
				"INNER JOIN GEN_CUENTA_CONTABLE CUC ON CUC.IDE_GECUC=RUA.IDE_GECUC " +
				"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_GETIA=RUA.IDE_GETIA and TIA.IDE_NRTIN=TIN.IDE_NRTIN " +
				"AND DTN.IDE_NRTIN=TIA.IDE_NRTIN "+
				"INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DRO.IDE_GEEDP " +
				"INNER JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"INNER JOIN GEN_AREA ARE ON rua.ide_geare=are.ide_geare " +
				"WHERE ROL.IDE_NRROL in ("+set_nomina.getValorSeleccionado()+") " +
				"AND RUA.IDE_GETIA="+tab_mov_contable.getValor("IDE_GETIA")+" " +
				"and todos_nrrua=false " +
				"GROUP BY rua.IDE_GEARE,SUC.IDE_SUCU,SUC.NOM_SUCU,ARE.DETALLE_GEARE,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB,CUC.IDE_GECUC,ROL.IDE_NRDTN " +
				"having SUM (DRO.VALOR_NRDRO) !=0 "+
				"ORDER BY SUC.NOM_SUCU ASC,ARE.DETALLE_GEARE asc");

		System.out.println("tab_haber "+tab_haber.getSql());
		double dou_tot_haber=0;
		double dou_haber=0;
		for (int i = 0; i < tab_haber.getTotalFilas(); i++) {
			tab_det_mov.insertar();
			tab_det_mov.setValor("IDE_GECUC",tab_haber.getValor(i, "IDE_GECUC"));
			tab_det_mov.setValor("IDE_SUCU",tab_haber.getValor(i, "IDE_SUCU"));
			tab_det_mov.setValor("IDE_GEARE",tab_haber.getValor(i, "IDE_GEARE"));
			tab_det_mov.setValor("DEBE_CNDEM",tab_haber.getValor(i, "DEBE"));
			tab_det_mov.setValor("HABER_CNDEM",tab_haber.getValor(i, "HABER"));
			tab_det_mov.setValor("IDE_GELUA",utilitario.getVariable("p_gen_lugar_aplica_haber"));
			tab_det_mov.setValor("IDE_NRDTN",tab_haber.getValor(i, "IDE_NRDTN"));

			try {
				dou_haber=Double.parseDouble(tab_haber.getValor(i, "HABER"));
			} catch (Exception e) {
				// TODO: handle exception
				dou_haber=0;
			}
			dou_tot_haber=dou_tot_haber+dou_haber;
		}


		TablaGenerica tab_debe=utilitario.consultar("select " +
				"EDP.IDE_GEARE,SUC.IDE_SUCU,ARE.DETALLE_GEARE,CUC.IDE_GECUC,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB, " +
				"CASE WHEN IDE_GELUA="+utilitario.getVariable("p_gen_lugar_aplica_debe")+" THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS DEBE , " +
				"CASE WHEN IDE_GELUA="+utilitario.getVariable("p_gen_lugar_aplica_haber")+" THEN SUM (DRO.VALOR_NRDRO) ELSE 0 END AS HABER, " +
				"ROL.IDE_NRDTN "+
				"from NRH_ROL ROL " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON DTN.IDE_NRTIN=TIN.IDE_NRTIN " +
				"INNER JOIN NRH_DETALLE_ROL DRO ON DRO.IDE_NRROL=ROL.IDE_NRROL " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRDER=DRO.IDE_NRDER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_RUBRO_ASIENTO RUA ON RUA.IDE_NRRUB=RUB.IDE_NRRUB " +
				"INNER JOIN GEN_CUENTA_CONTABLE CUC ON CUC.IDE_GECUC=RUA.IDE_GECUC "+
				"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_GETIA=RUA.IDE_GETIA and TIA.IDE_NRTIN=TIN.IDE_NRTIN " +
				"AND DTN.IDE_NRTIN=TIA.IDE_NRTIN "+
				"INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEEDP=DRO.IDE_GEEDP " +
				"INNER JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU "+
				"INNER JOIN GEN_AREA ARE ON ARE.IDE_GEARE=EDP.IDE_GEARE " +
				"WHERE ROL.IDE_NRROL in ("+set_nomina.getValorSeleccionado()+") " +
				"AND RUA.IDE_GETIA="+tab_mov_contable.getValor("IDE_GETIA")+" " +
				"and todos_nrrua=true " +
				"GROUP BY EDP.IDE_GEARE,SUC.IDE_SUCU,SUC.NOM_SUCU,ARE.DETALLE_GEARE,RUB.IDE_NRRUB,RUA.IDE_GELUA,RUB.DETALLE_NRRUB,CUC.IDE_GECUC,ROL.IDE_NRDTN " +
				"having SUM (DRO.VALOR_NRDRO) !=0 "+
				"ORDER BY SUC.NOM_SUCU ASC,ARE.DETALLE_GEARE ASC");
		System.out.println("tab_debe "+tab_debe.getSql());
		double dou_tot_debe=0;
		double dou_debe=0;

		for (int i = 0; i < tab_debe.getTotalFilas(); i++) {
			tab_det_mov.insertar();
			tab_det_mov.setValor("IDE_GECUC",tab_debe.getValor(i, "IDE_GECUC"));
			tab_det_mov.setValor("IDE_SUCU",tab_debe.getValor(i, "IDE_SUCU"));
			tab_det_mov.setValor("IDE_GEARE",tab_debe.getValor(i, "IDE_GEARE"));
			tab_det_mov.setValor("DEBE_CNDEM",tab_debe.getValor(i, "DEBE"));
			tab_det_mov.setValor("HABER_CNDEM",tab_debe.getValor(i, "HABER"));
			tab_det_mov.setValor("IDE_GELUA",utilitario.getVariable("p_gen_lugar_aplica_debe"));
			tab_det_mov.setValor("IDE_NRDTN",tab_debe.getValor(i, "IDE_NRDTN"));
			try {
				dou_debe=Double.parseDouble(tab_debe.getValor(i, "DEBE"));
			} catch (Exception e) {
				// TODO: handle exception
				dou_debe=0;
			}
			dou_tot_debe=dou_tot_debe+dou_debe;
		}


		tab_det_mov.guardar();
		guardarPantalla();

		utilitario.getConexion().ejecutarSql("update NRH_ROL set IDE_CNMOC="+tab_mov_contable.getValorSeleccionado()+" where IDE_NRROL in ("+set_nomina.getValorSeleccionado()+")");
		tab_mov_contable.setFilaActual(tab_mov_contable.getValorSeleccionado());
		calcularTotales();
	}

	public void aceptarNominaParaAsiento(){
		if(set_nomina.getValorSeleccionado()!=null && !set_nomina.getValorSeleccionado().isEmpty()){
			set_nomina.cerrar();

			utilitario.getConexion().agregarSqlPantalla("update NRH_ROL set IDE_CNMOC=NULL where IDE_CNMOC="+tab_mov_contable.getValorSeleccionado()+" "  +
					"and ide_nrrol not in ("+set_nomina.getValorSeleccionado()+")");			
			generarAsiento();

		}else{
			utilitario.agregarMensajeInfo("Debe seleccionar por lo menos un rol","");
		}
	}

	public void abrirNominasGeneradas(){

		if (com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede generar el asiento", "Primero debe seleccionar un periodo");
			return;
		}

		if (tab_mov_contable.getTotalFilas()==0 ){
			utilitario.agregarMensajeInfo("No se puede generar el asiento", "No se encuentran datos en pantalla");
			return;
		}

		if (tab_mov_contable.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede generar el asiento", "Primero debe guardar la transaccion");
			return;
		}

		if (tab_mov_contable.getValor("ACTIVO_CNMOC")!=null && !tab_mov_contable.getValor("ACTIVO_CNMOC").isEmpty()
				&& (tab_mov_contable.getValor("ACTIVO_CNMOC").equalsIgnoreCase("1") || tab_mov_contable.getValor("ACTIVO_CNMOC").equalsIgnoreCase("true"))){
			utilitario.agregarMensajeInfo("No se puede generar el asiento", "El asiento ya se encuentra cerrado");
			return;
		}

		if (utilitario.consultar("select IDE_NRROL, " +
				"DETALLE_NRTIN, " +
				"DETALLE_GTTEM " +
				"from NRH_ROL rol " +
				"inner join NRH_DETALLE_TIPO_NOMINA dtn on DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_NRTIN=TIN.IDE_NRTIN "+
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"where ROL.IDE_GEPRO in ("+com_periodo.getValue()+") " +
				"and rol.IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada")+" AND TIA.IDE_GETIA="+tab_mov_contable.getValor("IDE_GETIA")).getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede generar el asiento","No existen nominas cerradas");
			return;
		}

		if (con_guardar.isVisible()){
			con_guardar.cerrar();
			boo_sobreescribir=true;

			set_nomina.getTab_seleccion().setSql("select IDE_NRROL, " +
					"DETALLE_NRTIN, " +
					"DETALLE_GTTEM " +
					"from NRH_ROL rol " +
					"inner join NRH_DETALLE_TIPO_NOMINA dtn on DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
					"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
					"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_NRTIN=TIN.IDE_NRTIN "+
					"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
					"where ide_cnmoc= "+tab_mov_contable.getValorSeleccionado()+" " +
					"and rol.IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada"));

			set_nomina.getTab_seleccion().ejecutarSql();

			if (set_nomina.getTab_seleccion().getTotalFilas()==0){
				set_nomina.getTab_seleccion().setSql("select IDE_NRROL, " +
						"DETALLE_NRTIN, " +
						"DETALLE_GTTEM " +
						"from NRH_ROL rol " +
						"inner join NRH_DETALLE_TIPO_NOMINA dtn on DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
						"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
						"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_NRTIN=TIN.IDE_NRTIN "+
						"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
						"where ROL.IDE_GEPRO in ("+com_periodo.getValue()+") " +
						"and rol.IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada")+" AND TIA.IDE_GETIA="+tab_mov_contable.getValor("IDE_GETIA")+" "+
						"and rol.ide_nrrol not in (select IDE_NRROL from NRH_ROL where IDE_CNMOC is not NULL)");
				set_nomina.getTab_seleccion().ejecutarSql();
			}

			set_nomina.getBot_aceptar().setMetodo("aceptarNominaParaAsiento");
			set_nomina.setRadio();
			set_nomina.dibujar();
		}else{

			if (tab_det_mov.getTotalFilas()>0){
				boo_sobreescribir=false;
				con_guardar.setMessage("Ya se encuentra generado un asiento, Desea sobreescribir el asiento");
				con_guardar.setTitle("CONFIRMACION INTERFAZ ENTRE NOMINA Y CONTABILIDAD");
				con_guardar.getBot_aceptar().setMetodo("abrirNominasGeneradas");

				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				set_nomina.getTab_seleccion().setSql("select IDE_NRROL, " +
						"DETALLE_NRTIN, " +
						"DETALLE_GTTEM " +
						"from NRH_ROL rol " +
						"inner join NRH_DETALLE_TIPO_NOMINA dtn on DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
						"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
						"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_NRTIN=TIN.IDE_NRTIN "+
						"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
						"where ide_cnmoc= "+tab_mov_contable.getValorSeleccionado());

				set_nomina.getTab_seleccion().ejecutarSql();

				if (set_nomina.getTab_seleccion().getTotalFilas()==0){
					set_nomina.getTab_seleccion().setSql("select IDE_NRROL, " +
							"DETALLE_NRTIN, " +
							"DETALLE_GTTEM " +
							"from NRH_ROL rol " +
							"inner join NRH_DETALLE_TIPO_NOMINA dtn on DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
							"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
							"INNER JOIN GEN_TIPO_ASIENTO TIA ON TIA.IDE_NRTIN=TIN.IDE_NRTIN "+
							"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
							"where ROL.IDE_GEPRO in ("+com_periodo.getValue()+") " +
							"and rol.IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada")+" AND TIA.IDE_GETIA="+tab_mov_contable.getValor("IDE_GETIA")+" "+
							"and rol.ide_nrrol not in (select IDE_NRROL from NRH_ROL where IDE_CNMOC is not NULL)");
					set_nomina.getTab_seleccion().ejecutarSql();
				}

				set_nomina.getBot_aceptar().setMetodo("aceptarNominaParaAsiento");
				set_nomina.setRadio();
				set_nomina.dibujar();

			}
		}

	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede insertar una cabecera de asiento", "Primero debe seleccionar un periodo");
			return;
		}

		if (tab_mov_contable.isFocus()){
			tab_mov_contable.insertar();
			TablaGenerica gen_periodo=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
			tab_mov_contable.setValor("IDE_GEANI", gen_periodo.getValor("IDE_GEANI"));
			tab_mov_contable.setValor("IDE_GEMES", gen_periodo.getValor("IDE_GEMES"));
			calcularTotales();
		}

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_mov_contable.isFocus()){
			if (tab_mov_contable.guardar()){
				guardarPantalla();
			}
		}
	}

	public void eliminarInterfaz(){

		if (tab_mov_contable.getValor("CMC_ID")!=null && !tab_mov_contable.getValor("CMC_ID").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede eliminar la interfaz", "El asiento ya se envio al core bancario");
			return;
		}

		utilitario.getConexion().agregarSqlPantalla("update NRH_ROL set IDE_CNMOC=NULL where IDE_CNMOC="+tab_mov_contable.getValorSeleccionado());			

		utilitario.getConexion().agregarSqlPantalla("delete from CNT_DETALLE_MOVIMIENTO where IDE_CNMOC="+tab_mov_contable.getValorSeleccionado());

		utilitario.getConexion().agregarSqlPantalla("delete from CNT_MOVIMIENTO_CONTABLE where IDE_CNMOC="+tab_mov_contable.getValorSeleccionado());

		guardarPantalla();

		con_guardar.cerrar();
		TablaGenerica gen_periodo=ser_nomina.getPeriodoRol(com_periodo.getValue()+"");
		tab_mov_contable.setCondicion("IDE_GEANI="+gen_periodo.getValor("IDE_GEANI")+" AND IDE_GEMES="+gen_periodo.getValor("IDE_GEMES")+"");
		tab_mov_contable.ejecutarSql();

		tab_det_mov.ejecutarValorForanea(tab_mov_contable.getValorSeleccionado());
		calcularTotales();
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_mov_contable.isFocus()){


			if (tab_mov_contable.isFilaInsertada()){
				tab_mov_contable.eliminar();
			}else{

				if (tab_mov_contable.getValor("activo_cnmoc")!=null 
						&& !tab_mov_contable.getValor("activo_cnmoc").isEmpty() 
						&& tab_mov_contable.getValor("activo_cnmoc").equalsIgnoreCase("true")){
					utilitario.agregarMensajeInfo("No se puede eliminar la interfaz seleccionada", "La interfaz ya se encuentra cerrada");
					return;
				}

				con_guardar.setMessage("Esta seguro de eliminar el asiento seleccionado");
				con_guardar.setTitle("CONFIRMACION ELIMINACION DE INTERFAZ CONTABLE");
				con_guardar.getBot_aceptar().setMetodo("eliminarInterfaz");

				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}
		}
	}

	public Tabla getTab_mov_contable() {
		return tab_mov_contable;
	}

	public void setTab_mov_contable(Tabla tab_mov_contable) {
		this.tab_mov_contable = tab_mov_contable;
	}

	public Tabla getTab_det_mov() {
		return tab_det_mov;
	}

	public void setTab_det_mov(Tabla tab_det_mov) {
		this.tab_det_mov = tab_det_mov;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}

	public SeleccionTabla getSet_nomina() {
		return set_nomina;
	}

	public void setSet_nomina(SeleccionTabla set_nomina) {
		this.set_nomina = set_nomina;
	}

	public Dialogo getDia_razon_anula() {
		return dia_razon_anula;
	}

	public void setDia_razon_anula(Dialogo dia_razon_anula) {
		this.dia_razon_anula = dia_razon_anula;
	}


}

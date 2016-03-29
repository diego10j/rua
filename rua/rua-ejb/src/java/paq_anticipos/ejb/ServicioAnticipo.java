package paq_anticipos.ejb;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import framework.aplicacion.TablaGenerica;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Utilitario;

@Stateless
public class ServicioAnticipo{
	private Utilitario utilitario = new Utilitario();
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	
	public int getNumVecesGaranteAnticipo(String ide_geedp){
		TablaGenerica tab_garante=utilitario.consultar("select * from NRH_GARANTE GAR " +
				"inner join NRH_ANTICIPO ant on ANT.IDE_NRANT=GAR.IDE_NRANT " +
				"where GAR.IDE_GEEDP in ( " +
				"select IDE_GEEDP from NRH_GARANTE where IDE_GEEDP="+ide_geedp+" " +
				") " +
				"AND ANT.ANTICIPO_NRANT=true " +				
				"AND ANT.ACTIVO_NRANT=true");
		tab_garante.imprimirSql();
		if (tab_garante.getTotalFilas()>0){
			return tab_garante.getTotalFilas();
		}
		return 0;
	}


	public boolean generarAbonoAnticipo(String ide_nrant,double monto,double tasa_interes,int plazo,int amortizacion_cada,int gracia,String fecha_inicio,String ide_nrcap){

		TablaGenerica tab_amort;
		if (ide_nrcap!=null && !ide_nrcap.isEmpty()){
			tab_amort=getTablaAmortizacion(monto, 
					tasa_interes, 
					plazo, 
					amortizacion_cada, 
					gracia, 
					fecha_inicio,
					ide_nrcap);
	
		}else{
			tab_amort=getTablaAmortizacion(monto, 
					tasa_interes, 
					plazo, 
					amortizacion_cada, 
					gracia, 
					fecha_inicio,
					null);
		}
		

//		if (getCapacidadPago(ide_nrant).getTotalFilas()>0){
//			utilitario.getConexion().agregarSqlPantalla("DELETE FROM NRH_AMORTIZACION AMO " +
//					"WHERE IDE_NRRUB NOT IN ( " +
//					"SELECT IDE_NRRUB FROM NRH_RUBRO_DETALLE_PAGO WHERE IDE_NRCAP IN ( " +
//					"SELECT IDE_NRCAP FROM NRH_CAPACIDAD_PAGO WHERE IDE_NRANT="+ide_nrant+")) " +
//					"AND  IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+ide_nrant+") " +
//					"and ACTIVO_NRAMO=0");
//		}else{
			utilitario.getConexion().agregarSqlPantalla("DELETE FROM NRH_AMORTIZACION AMO " +
					"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+ide_nrant+") " +
					"and ACTIVO_NRAMO=false");
//		}


		
		TablaGenerica tab_amortizacion=new TablaGenerica();
		tab_amortizacion.setTabla("NRH_AMORTIZACION", "IDE_NRAMO", -1);
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setCheck();
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setCheck();
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setValorDefecto("false");
		String str_ide_nrani=getAnticipoInteres(ide_nrant).getValor("IDE_NRANI");
		tab_amortizacion.getColumna("IDE_NRANI").setValorDefecto(str_ide_nrani);
		tab_amortizacion.setCondicion("IDE_NRAMO=-1");
		tab_amortizacion.setCampoOrden("FECHA_VENCIMIENTO_NRAMO ASC");
		tab_amortizacion.ejecutarSql();
		

//		int int_num_cuotas_pagadas=getAmortizacion(ide_nrant, true).getTotalFilas();
//		int int_num_cuota=0;
		for (int i = 0; i < tab_amort.getTotalFilas(); i++) {
			// inserto las filas de la amortizacion
			tab_amortizacion.insertar();
			tab_amortizacion.setValor("CAPITAL_NRAMO", tab_amort.getValor(i, "CAPITAL_NRAMO"));
			tab_amortizacion.setValor("INTERES_NRAMO", tab_amort.getValor(i, "INTERES_NRAMO"));
			tab_amortizacion.setValor("CUOTA_NRAMO", tab_amort.getValor(i, "CUOTA_NRAMO"));
			tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", tab_amort.getValor(i, "FECHA_VENCIMIENTO_NRAMO"));
			tab_amortizacion.setValor("PRINCIPAL_NRAMO", tab_amort.getValor(i, "PRINCIPAL_NRAMO"));
			tab_amortizacion.setValor("NRO_CUOTA_NRAMO",tab_amort.getValor(i, "NRO_CUOTA_NRAMO"));
			tab_amortizacion.setValor("IDE_NRRUB", tab_amort.getValor(i, "IDE_NRRUB"));
		}
		tab_amortizacion.guardar();
//
//
//
		System.out.println("commit 1 "+utilitario.getConexion().getSqlPantalla());
		if (utilitario.getConexion().guardarPantalla().isEmpty()){
			tab_amortizacion.setCondicion("IDE_NRANI="+str_ide_nrani);
			tab_amortizacion.ejecutarSql();
			return true;

//			for (int i = 0; i < tab_amortizacion.getTotalFilas(); i++) {
//				tab_amortizacion.modificar(i);
//				tab_amortizacion.setValor(i,"NRO_CUOTA_NRAMO", (i+1)+"");
//			}
//			tab_amortizacion.guardar();
//			System.out.println("commit 2 "+utilitario.getConexion().getSqlPantalla());
//			if (utilitario.getConexion().guardarPantalla().isEmpty()){
//				return true;	
//			}

		}

		return false;

	}

	public String getTotalPagarAnticipo(String IDE_NRANT){
		TablaGenerica tab_amor=utilitario.consultar("select IDE_NRANI,sum (CUOTA_NRAMO) as total_pagar_anticipo from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+IDE_NRANT+") " +
				"and ACTIVO_NRAMO=false " +
				"GROUP BY IDE_NRANI");
		if (tab_amor.getTotalFilas()>0){
			return tab_amor.getValor("total_pagar_anticipo");
		}
		return null;
	}

	public TablaGenerica getCapacidadPago(String IDE_NRANT){
		TablaGenerica tab_ant_int=utilitario.consultar("SELECT * FROM NRH_CAPACIDAD_PAGO WHERE IDE_NRANT="+IDE_NRANT);
		return tab_ant_int;
	}

	public TablaGenerica getAnticipoInteres(String IDE_NRANT){
		TablaGenerica tab_ant_int=utilitario.consultar("select * from NRH_ANTICIPO_INTERES where IDE_NRANT="+IDE_NRANT);
		return tab_ant_int;
	}

	public TablaGenerica getAmortizacion(String IDE_NRANT,boolean pagados){
		TablaGenerica tab_amor=new TablaGenerica();

		if (pagados){
			// solo anticipos pagados
			tab_amor=utilitario.consultar("select * from NRH_AMORTIZACION " +
					"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+IDE_NRANT+") " +
					"and ACTIVO_NRAMO=true ORDER BY NRO_CUOTA_NRAMO ASC");		
		}else{
			// solo anticipos no pagados
			tab_amor=utilitario.consultar("select * from NRH_AMORTIZACION " +
					"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+IDE_NRANT+") " +
					"and ACTIVO_NRAMO=false ORDER BY NRO_CUOTA_NRAMO ASC");		
		}
		return tab_amor;
	}

	public TablaGenerica getAmortizacion(String IDE_NRANT){
		TablaGenerica tab_amor=utilitario.consultar("select * from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+IDE_NRANT+") " +
				"ORDER BY NRO_CUOTA_NRAMO ASC");
		return tab_amor;
	}

	public boolean isCxPActivos(String IDE_GTEMP){
		TablaGenerica tab_anticipos=utilitario.consultar("select * from " +
				"NRH_ANTICIPO where ANTICIPO_NRANT=FALSE AND ACTIVO_NRANT=true " +
				"and IDE_GTEMP="+IDE_GTEMP);

		if (tab_anticipos.getTotalFilas()>0){
			return true;
		}
		return false;
	}


	public boolean isPagosRealizadosAnticipo(String IDE_NRANT){
		TablaGenerica tab_amort=utilitario.consultar("select IDE_NRAMO,NRO_CUOTA_NRAMO, " +
				"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
				"from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI " +
				"IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+IDE_NRANT+") " +
				"and ACTIVO_NRAMO=true " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
		if (tab_amort.getTotalFilas()>0){
			return true;
		}
		return false;
	}


	public boolean isAnticiposActivos(String IDE_GEEDP){
		TablaGenerica tab_anticipos=utilitario.consultar("select * from NRH_ANTICIPO where ANTICIPO_NRANT=TRUE AND ACTIVO_NRANT=true and IDE_GTEMP="+ser_nomina.getEmpleadoDepartamento(IDE_GEEDP).getValor("IDE_GTEMP"));
		System.out.println("sql anticipos activos "+tab_anticipos.getSql());
		if (tab_anticipos.getTotalFilas()>0){
			return true;
		}
		return false;
	}


	public boolean isPagosPendientesAnticipo(String IDE_GEEDP){
		TablaGenerica tab_amort=utilitario.consultar("SELECT * FROM NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN ( " +
				"SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES " +
				"WHERE IDE_NRANT IN ( " +
				"SELECT IDE_NRANT FROM NRH_ANTICIPO " +
				"WHERE IDE_GEEDP="+IDE_GEEDP+" AND ANTICIPO_NRANT=TRUE AND ACTIVO_NRANT=true)) " +
				"AND ACTIVO_NRAMO != true " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC ");

		if (tab_amort.getTotalFilas()>0){
			return true;
		}
		return false;
	}


	public TablaGenerica getCuotasPagadas(String ide_nrant){
		if (ide_nrant==null || ide_nrant.isEmpty()){
			ide_nrant="-1";
		}
		return utilitario.consultar("select * from NRH_AMORTIZACION " +
				"where IDE_NRANI=(select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+ide_nrant+") " +
				"and ACTIVO_NRAMO=true " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
	}

	public TablaGenerica getCuotasPorPagar(String ide_nrant){
		if (ide_nrant==null || ide_nrant.isEmpty()){
			ide_nrant="-1";
		}

		return utilitario.consultar("select * from NRH_AMORTIZACION " +
				"where IDE_NRANI=(select ide_nrani from NRH_ANTICIPO_INTERES where IDE_NRANT="+ide_nrant+") " +
				"and (ACTIVO_NRAMO=false or ACTIVO_NRAMO!=true) " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");

	}

	/**
	 * Metodo que busca cuotas de anticipos por pagar de un empleado en un periodo de rol
	 * 
	 * @param IDE_GEEDP  :EMPLEADO DEPARTAMENTO
	 * @param IDE_GEPRO  :PERIODO DE ROL
	 * @return  TablaGenerica    : resultado
	 */
	public TablaGenerica getCuotasAnticipoPorPagarEmpleado(String IDE_GTEMP,String IDE_GEPRO){
		TablaGenerica tab_per_rol=ser_nomina.getPeriodoRol(IDE_GEPRO);
		String str_fecha_ini_per_rol=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
		String str_fecha_fin_per_rol=tab_per_rol.getValor("FECHA_FINAL_GEPRO");
		TablaGenerica tab_amort=utilitario.consultar("SELECT * FROM NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN ( " +
				"SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES " +
				"WHERE IDE_NRANT IN ( " +
				"SELECT IDE_NRANT FROM NRH_ANTICIPO " +
				"WHERE IDE_GTEMP="+IDE_GTEMP+" " +
				"AND CALIFICADO_NRANT=true AND APROBADO_NRANT=true AND ACTIVO_NRANT=true)) " +
				"AND ACTIVO_NRAMO != true " +
				"and FECHA_VENCIMIENTO_NRAMO BETWEEN TO_DATE('"+str_fecha_ini_per_rol+"','yyyy-mm-dd') " +
				"and TO_DATE('"+str_fecha_fin_per_rol+"', 'yyyy-mm-dd') " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC ");
		System.out.println("----ENTRE A LA AMORTIZACION-----");
		tab_amort.imprimirSql();
		return tab_amort;
	}



	/**
	 * Metodo que busca los rubros de pago de anticipos ordenados ascendentemente por la fecha de pago de rubro
	 * @param IDE_NRCAP : IDE DE LA TABLA CAPACIDAD DE PAGO
	 * @return TablaGenerica : resultado
	 */
	public TablaGenerica getRubrosDetallePago(String IDE_NRCAP){
		TablaGenerica tab_rub_det_pago=utilitario.consultar("select * from NRH_RUBRO_DETALLE_PAGO where IDE_NRCAP="+IDE_NRCAP+" and ACTIVO_NRRDP=true order by FECHA_PAGO_RUBRO_NRRDP ASC");
		return tab_rub_det_pago;
	}

	public double getNumPeriodos(int plazo,int amortizacion_cada,int gracia){
		double num_periodos=0;
		if (amortizacion_cada!=0){
			num_periodos=(((Double.parseDouble(plazo+"")/12)*360)/amortizacion_cada) - (((Double.parseDouble(gracia+"")/12)*360)/amortizacion_cada);
		}
		return num_periodos;
	}

	public String getCuota(double monto,double tasa_interes,double plazo,double amortizacion_cada,double gracia){
		double fm1=0;
		double fm2=0;
		double fexp=0;
		double dividendo=0;
		if (amortizacion_cada!=0){
			fm1=360/amortizacion_cada;
		}
		if (fm1!=0){
			fm2=(tasa_interes/100)/fm1;
		}
		fexp=fm1*((plazo/12)-(gracia/12));
		if (fm2!=0){
			double div=((1-(1/Math.pow((1+fm2),fexp)))/fm2);
			if (div!=0){
				dividendo=(monto)/div;
			}else{
				if (plazo!=0){
					dividendo=(monto)/(plazo);	
				}

			}
		}else{
			if (plazo!=0){
				dividendo=(monto)/(plazo);
			}
		}
		return dividendo+"";
	}

	public TablaGenerica getDetalleRubrosPago(String ide_nrant){
		TablaGenerica tab_trp=utilitario.consultar("select " +
				"AMO.IDE_NRAMO,AMO.IDE_NRANI,FECHA_VENCIMIENTO_NRAMO,CAPITAL_NRAMO,INTERES_NRAMO,PRINCIPAL_NRAMO,CUOTA_NRAMO,FECHA_CANCELADO_NRAMO,NRO_CUOTA_NRAMO,PRE_CANCELADO_NRAMO,ACTIVO_NRAMO,amo.IDE_NRRUB " +
				"from NRH_AMORTIZACION amo " +
				"inner join NRH_RUBRO_DETALLE_PAGO rdp on rdp.IDE_NRRUB=AMO.IDE_NRRUB " +
				"inner join NRH_CAPACIDAD_PAGO cap on cap.IDE_NRCAP=rdp.ide_nrcap " +
				"inner join NRH_ANTICIPO ant on ant.ide_nrant=cap.ide_nrant " +
				"inner join NRH_ANTICIPO_INTERES ani on ani.ide_nrant=ant.ide_nrant and amo.ide_nrani=ani.ide_nrani " +
				"where ant.IDE_NRANT="+ide_nrant+" " +
				"and AMO.ACTIVO_NRAMO!=true ");
		return tab_trp;

	}

	public double getMontoTotalRubrosPago(String ide_nrant){
		try {
			TablaGenerica tab_trp=getDetalleRubrosPago(ide_nrant);
			return tab_trp.getSumaColumna("CUOTA_NRAMO");

		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}
	public double getMontoTotalRubrosPago(TablaGenerica tab_rubro_detalle_pago){
		double tot_rubro_pago_anticipo=0;
		for (int i = 0; i < tab_rubro_detalle_pago.getTotalFilas(); i++) {
			try {
				tot_rubro_pago_anticipo=tot_rubro_pago_anticipo+Double.parseDouble(tab_rubro_detalle_pago.getValor(i, "VALOR_DESCUENTO_NRRDP"));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return tot_rubro_pago_anticipo;
	}



	/**
	 * Metodo que devuelve la tabla de amortizacion del anticipo de un empleado
	 * 
	 * @param ide_nrdtn  : IDE TIPO DE NOMINA DEL EMPPLEADO
	 * @param monto      : total capital a amortizar
	 * @param tasa_interes 
	 * @param plazo      : numero de cuotas (meses)
	 * @param amortizacion_cada : periodo (dias)
	 * @param gracia : mes de gracia (dias) 
	 * @param fecha_inicio : fecha de calculo
	 * @param ide_nrcap  : IDE CAPACIDAD DE PAGO DEL ANTICIPO DEL EMPLEADO
	 * @return resultado :TablaGenerica 
	 */
	public TablaGenerica getTablaAmortizacion(double monto,double tasa_interes,int plazo,int amortizacion_cada,int gracia,String fecha_inicio,String ide_nrcap){


		System.out.println("monto "+monto);
		List lis_cuotas=new ArrayList();
		List lis_fecha=new ArrayList();
		List lis_nrder=new ArrayList();


		double dou_tot_rubros_pagos=0;
		TablaGenerica tab_rubros_detalle_pago=new TablaGenerica();
		if (ide_nrcap!=null && !ide_nrcap.isEmpty()){
			tab_rubros_detalle_pago=getRubrosDetallePago(ide_nrcap);
			if (tab_rubros_detalle_pago.getTotalFilas()>0){
				dou_tot_rubros_pagos=getMontoTotalRubrosPago(tab_rubros_detalle_pago);
			}
		}

		String str_fecha_vencimiento_mensual=fecha_inicio;
		double dou_saldo_capital=monto;
		double dou_principal=0;
		double dou_interes_mensual=0;
		double dou_num_periodos=getNumPeriodos(plazo, amortizacion_cada, gracia);
		double dou_cuota=0;		

		dou_cuota=Double.parseDouble(getCuota(monto-dou_tot_rubros_pagos, tasa_interes, plazo, amortizacion_cada, gracia));

		System.out.println("primera cuota "+dou_cuota);
		TablaGenerica tab_amortizacion=new TablaGenerica();
		tab_amortizacion.setTabla("NRH_AMORTIZACION", "IDE_NRAMO", -1);
		tab_amortizacion.setCondicion("IDE_NRAMO=-1");
		tab_amortizacion.ejecutarSql();

		//		String str_ide_nrder_descuentos=ser_nomina.getDetalleRubro(ide_nrdtn, utilitario.getVariable("p_nrh_rubro_descuento_nomina")).getValor("IDE_NRDER");

		String str_ide_nrder_descuentos=utilitario.getVariable("p_nrh_rubro_descuento_nomina");

		int dia_ini=utilitario.getDia(str_fecha_vencimiento_mensual);
		int mes_ini=utilitario.getMes(str_fecha_vencimiento_mensual);
		int anio_ini=utilitario.getAnio(str_fecha_vencimiento_mensual);
		
		for (int i = 0; i < dou_num_periodos; i++) {
			// calculo interes mensual
			dou_interes_mensual=(dou_saldo_capital*(tasa_interes/100))/(360/amortizacion_cada);// formula calculo intereses
			// calculo de fecha de pago mensual 


			str_fecha_vencimiento_mensual=anio_ini+"-"+mes_ini+"-"+dia_ini;
			str_fecha_vencimiento_mensual=utilitario.getFormatoFecha(str_fecha_vencimiento_mensual);

//			str_fecha_vencimiento_mensual=utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(str_fecha_vencimiento_mensual),amortizacion_cada));			

			// calculo de columna saldo principal de la tabla de amortizacion
			dou_principal=dou_cuota-dou_interes_mensual;

			//calculo de columna saldo_capital de la tabla de amortizacion

			dou_saldo_capital=dou_saldo_capital-dou_principal;

			System.out.println("iteracion "+(i+1)+" fecha "+str_fecha_vencimiento_mensual+" cuota rubro "+dou_cuota+" saldo capital "+dou_saldo_capital+" principal "+dou_principal);

			// redondeo los valores para mejor visualizacion 
			BigDecimal big_int_men=new BigDecimal(dou_interes_mensual);
			big_int_men=big_int_men.setScale(2, RoundingMode.HALF_UP);

			BigDecimal big_saldo_capital=new BigDecimal(dou_saldo_capital);
			big_saldo_capital=big_saldo_capital.setScale(2, RoundingMode.HALF_UP);

			BigDecimal big_principal=new BigDecimal(dou_principal);
			big_principal=big_principal.setScale(2, RoundingMode.HALF_UP);

			BigDecimal big_cuota=new BigDecimal(dou_cuota);
			big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);

			if (ide_nrcap==null || ide_nrcap.isEmpty()){
				// insertamos una fila en la TablaGenerica Amortizacion
				tab_amortizacion.insertar();
				tab_amortizacion.setValor("NRO_CUOTA_NRAMO", (i+1)+"");
				tab_amortizacion.setValor("CAPITAL_NRAMO", big_saldo_capital+"");
				tab_amortizacion.setValor("INTERES_NRAMO", big_int_men+"");
				tab_amortizacion.setValor("CUOTA_NRAMO", big_cuota+"");
				tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", str_fecha_vencimiento_mensual);
				tab_amortizacion.setValor("PRINCIPAL_NRAMO", big_principal+"");
				tab_amortizacion.setValor("IDE_NRRUB", str_ide_nrder_descuentos);
			}else{

				lis_cuotas.add(big_cuota);
				lis_fecha.add(str_fecha_vencimiento_mensual);
				lis_nrder.add(str_ide_nrder_descuentos);
			}
			mes_ini=mes_ini+1;
			
			if (mes_ini>12){
				mes_ini=1;
				anio_ini=anio_ini+1;
			}

		}

		// solo si tiene Rubros Detalle Pago de anticipos 
		if (ide_nrcap!=null && !ide_nrcap.isEmpty()){
			if (tab_rubros_detalle_pago.getTotalFilas()>0){

				for (int i = 0; i < tab_rubros_detalle_pago.getTotalFilas(); i++) {

					lis_cuotas.add(tab_rubros_detalle_pago.getValor(i,"VALOR_DESCUENTO_NRRDP"));
					lis_fecha.add(tab_rubros_detalle_pago.getValor(i,"FECHA_PAGO_RUBRO_NRRDP"));
					lis_nrder.add(tab_rubros_detalle_pago.getValor(i,"IDE_NRRUB"));
				}
			}

			// ordeno por fechas 
			String aux_fecha="";
			String aux_cuota="";
			String aux_nrder="";
			for (int i = 0; i < lis_fecha.size(); i++) {
				for (int j = i+1; j < lis_fecha.size(); j++) {
					try {
						if (utilitario.isFechaMayor(utilitario.getFecha(lis_fecha.get(i)+""), utilitario.getFecha(lis_fecha.get(j)+""))){
							aux_fecha=lis_fecha.get(i)+"";
							aux_cuota=lis_cuotas.get(i)+"";
							aux_nrder=lis_nrder.get(i)+"";
							lis_fecha.set(i, lis_fecha.get(j));
							lis_cuotas.set(i, lis_cuotas.get(j));
							lis_nrder.set(i, lis_nrder.get(j));
							lis_fecha.set(j, aux_fecha);
							lis_cuotas.set(j, aux_cuota);
							lis_nrder.set(j, aux_nrder);
						}
					} catch (Exception e) {
						System.out.println("error exepcion servicio anticipo metodo calcular tabla amortizacion ");
						// TODO: handle exception
					}
				}
			}
			dou_saldo_capital=monto;				
			for (int j = 0; j < lis_fecha.size(); j++) {
				tab_amortizacion.insertar();
				tab_amortizacion.setValor("NRO_CUOTA_NRAMO", (j+1)+"");
				tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", lis_fecha.get(j)+"");
				tab_amortizacion.setValor("CUOTA_NRAMO", lis_cuotas.get(j)+"");
				tab_amortizacion.setValor("IDE_NRRUB", lis_nrder.get(j)+"");

				// asignacion cuota
				dou_cuota=Double.parseDouble(lis_cuotas.get(j)+"");
				// calculo interes mensual
				dou_interes_mensual=(dou_saldo_capital*(tasa_interes/100))/(360/amortizacion_cada);// formula calculo intereses
				// calculo de columna saldo principal de la tabla de amortizacion
				dou_principal=dou_cuota-dou_interes_mensual;
				//calculo de columna saldo_capital de la tabla de amortizacion
				dou_saldo_capital=dou_saldo_capital-dou_principal;

				// redondeo los valores para mejor visualizacion 
				BigDecimal big_int_men=new BigDecimal(dou_interes_mensual);
				big_int_men=big_int_men.setScale(2, RoundingMode.HALF_UP);

				BigDecimal big_saldo_capital=new BigDecimal(dou_saldo_capital);
				big_saldo_capital=big_saldo_capital.setScale(2, RoundingMode.HALF_UP);
				BigDecimal big_principal=new BigDecimal(dou_principal);
				big_principal=big_principal.setScale(2, RoundingMode.HALF_UP);
				BigDecimal big_cuota=new BigDecimal(dou_cuota);
				big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);

				tab_amortizacion.setValor("CAPITAL_NRAMO",big_saldo_capital+"");
				tab_amortizacion.setValor("INTERES_NRAMO",big_int_men+"");
				tab_amortizacion.setValor("PRINCIPAL_NRAMO", big_principal+"");
			}

		}
		
		// PARA MAYOR EXACTITUD EN LA ULTIMA FILA DE LA TABLA 

		if (tasa_interes==0){
			double dou_tot_cuotas=0;
			try {
				if (tab_amortizacion!=null && tab_amortizacion.getTotalFilas()>0){
					dou_tot_cuotas=tab_amortizacion.getSumaColumna("CUOTA_NRAMO");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			double dou_diferencia=monto-dou_tot_cuotas;
			System.out.println("deferencia "+dou_diferencia);

			double dou_ultima_cuota=0;

			try {
				if (tab_amortizacion!=null && tab_amortizacion.getTotalFilas()>0){
					dou_ultima_cuota=Double.parseDouble(tab_amortizacion.getValor(0, "CUOTA_NRAMO"));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			dou_ultima_cuota=dou_ultima_cuota+dou_diferencia; 

			BigDecimal big_ultima_cuota=new BigDecimal(dou_ultima_cuota);
			big_ultima_cuota=big_ultima_cuota.setScale(2, RoundingMode.HALF_UP);

			if (tab_amortizacion.getTotalFilas()>0){
				tab_amortizacion.setValor(0,"CUOTA_NRAMO",big_ultima_cuota+"");
				try {
					if (tab_amortizacion.getTotalFilas()>1){
						dou_saldo_capital=Double.parseDouble(tab_amortizacion.getValor(1, "CAPITAL_NRAMO"));	
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}


			dou_cuota=dou_ultima_cuota;
			// calculo interes mensual
			dou_interes_mensual=(dou_saldo_capital*(tasa_interes/100))/(360/amortizacion_cada);// formula calculo intereses
			// calculo de columna saldo principal de la tabla de amortizacion
			dou_principal=dou_cuota-dou_interes_mensual;
			//calculo de columna saldo_capital de la tabla de amortizacion
			dou_saldo_capital=dou_saldo_capital-dou_principal;
			
						
			// redondeo los valores para mejor visualizacion 
			BigDecimal big_int_men=new BigDecimal(dou_interes_mensual);
			big_int_men=big_int_men.setScale(2, RoundingMode.HALF_UP);

			
			BigDecimal big_saldo_capital=new BigDecimal(dou_saldo_capital);
			big_saldo_capital=big_saldo_capital.setScale(2, RoundingMode.HALF_UP);

			double dou=Double.parseDouble(big_saldo_capital+"");
			System.out.println("sal do capi "+dou);
			
			BigDecimal big_principal=new BigDecimal(dou_principal);
			big_principal=big_principal.setScale(2, RoundingMode.HALF_UP);
			
			BigDecimal big_cuota=new BigDecimal(dou_cuota);
			big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);


			if (tab_amortizacion.getTotalFilas()>1){
				tab_amortizacion.setValor(0,"CAPITAL_NRAMO","0");
				tab_amortizacion.setValor(0,"INTERES_NRAMO",big_int_men+"");
				tab_amortizacion.setValor(0,"PRINCIPAL_NRAMO", big_principal+"");
			}


		}
		return tab_amortizacion;
	}

	/**
	 * Metodo que devuelve la tabla de amortizacion del anticipo de un empleado
	 * 
	 * @param ide_nrdtn  : IDE TIPO DE NOMINA DEL EMPPLEADO
	 * @param monto      : total capital a amortizar
	 * @param tasa_interes 
	 * @param plazo      : numero de cuotas (meses)
	 * @param amortizacion_cada : periodo (dias)
	 * @param gracia : mes de gracia (dias) 
	 * @param fecha_inicio : fecha de calculo
	 * @param ide_nrcap  : IDE CAPACIDAD DE PAGO DEL ANTICIPO DEL EMPLEADO
	 * @return resultado :TablaGenerica 
	 */
	public TablaGenerica getTablaAmortizacionSimulador(double monto,double tasa_interes,int plazo,int amortizacion_cada,int gracia,String fecha_inicio,String fecha_d4,double valor_descuento_d4,String fecha_d3,double valor_descuento_d3){


		System.out.println("monto "+monto);
		List lis_cuotas=new ArrayList();
		List lis_fecha=new ArrayList();
		List lis_nrder=new ArrayList();


		double dou_tot_rubros_pagos=0;

		if (fecha_d4!=null && !fecha_d4.isEmpty()){
			dou_tot_rubros_pagos=dou_tot_rubros_pagos+valor_descuento_d4;
		}
		if (fecha_d3!=null && !fecha_d3.isEmpty()){
			dou_tot_rubros_pagos=dou_tot_rubros_pagos+valor_descuento_d3;
		}

		String str_fecha_vencimiento_mensual=fecha_inicio;
		double dou_saldo_capital=monto;
		double dou_principal=0;
		double dou_interes_mensual=0;
		double dou_num_periodos=getNumPeriodos(plazo, amortizacion_cada, gracia);
		double dou_cuota=0;		

		dou_cuota=Double.parseDouble(getCuota(monto-dou_tot_rubros_pagos, tasa_interes, plazo, amortizacion_cada, gracia));

		System.out.println("primera cuota "+dou_cuota);
		TablaGenerica tab_amortizacion=new TablaGenerica();
		tab_amortizacion.setTabla("NRH_AMORTIZACION", "IDE_NRAMO", -1);
		tab_amortizacion.setCondicion("IDE_NRAMO=-1");
		tab_amortizacion.ejecutarSql();

		//		String str_ide_nrder_descuentos=ser_nomina.getDetalleRubro(ide_nrdtn, utilitario.getVariable("p_nrh_rubro_descuento_nomina")).getValor("IDE_NRDER");

		String str_ide_nrder_descuentos=utilitario.getVariable("p_nrh_rubro_descuento_nomina");


		for (int i = 0; i < dou_num_periodos; i++) {
			// calculo interes mensual
			dou_interes_mensual=(dou_saldo_capital*(tasa_interes/100))/(360/amortizacion_cada);// formula calculo intereses
			// calculo de fecha de pago mensual 
			str_fecha_vencimiento_mensual=utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(str_fecha_vencimiento_mensual),amortizacion_cada));			

			// calculo de columna saldo principal de la tabla de amortizacion
			dou_principal=dou_cuota-dou_interes_mensual;

			//calculo de columna saldo_capital de la tabla de amortizacion

			dou_saldo_capital=dou_saldo_capital-dou_principal;

			System.out.println("iteracion "+(i+1)+" fecha "+str_fecha_vencimiento_mensual+" cuota rubro "+dou_cuota+" saldo capital "+dou_saldo_capital+" principal "+dou_principal);

			// redondeo los valores para mejor visualizacion 
			BigDecimal big_int_men=new BigDecimal(dou_interes_mensual);
			big_int_men=big_int_men.setScale(2, RoundingMode.HALF_UP);

			BigDecimal big_saldo_capital=new BigDecimal(dou_saldo_capital);
			big_saldo_capital=big_saldo_capital.setScale(2, RoundingMode.HALF_UP);

			BigDecimal big_principal=new BigDecimal(dou_principal);
			big_principal=big_principal.setScale(2, RoundingMode.HALF_UP);

			BigDecimal big_cuota=new BigDecimal(dou_cuota);
			big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);

			if ((fecha_d4==null || fecha_d4.isEmpty())
					&& (fecha_d3==null || fecha_d3.isEmpty())){
				// insertamos una fila en la TablaGenerica Amortizacion
				tab_amortizacion.insertar();
				tab_amortizacion.setValor("NRO_CUOTA_NRAMO", (i+1)+"");
				tab_amortizacion.setValor("CAPITAL_NRAMO", big_saldo_capital+"");
				tab_amortizacion.setValor("INTERES_NRAMO", big_int_men+"");
				tab_amortizacion.setValor("CUOTA_NRAMO", big_cuota+"");
				tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", str_fecha_vencimiento_mensual);
				tab_amortizacion.setValor("PRINCIPAL_NRAMO", big_principal+"");
				tab_amortizacion.setValor("IDE_NRRUB", str_ide_nrder_descuentos);
			}else{

				lis_cuotas.add(big_cuota);
				lis_fecha.add(str_fecha_vencimiento_mensual);
				lis_nrder.add(str_ide_nrder_descuentos);
			}
		}

		// solo si tiene Rubros Detalle Pago de anticipos 
		if ((fecha_d4!=null && !fecha_d4.isEmpty())
				|| (fecha_d3!=null && !fecha_d3.isEmpty())){

			if (fecha_d4!=null && !fecha_d4.isEmpty()){
				lis_cuotas.add(valor_descuento_d4);
				lis_fecha.add(fecha_d4);
				lis_nrder.add("-1");
			}
			if (fecha_d3!=null && !fecha_d3.isEmpty()){
				lis_cuotas.add(valor_descuento_d3);
				lis_fecha.add(fecha_d3);
				lis_nrder.add("-2");
			}

			// ordeno por fechas 
			String aux_fecha="";
			String aux_cuota="";
			String aux_nrder="";
			for (int i = 0; i < lis_fecha.size(); i++) {
				for (int j = i+1; j < lis_fecha.size(); j++) {
					try {
						if (utilitario.isFechaMayor(utilitario.getFecha(lis_fecha.get(i)+""), utilitario.getFecha(lis_fecha.get(j)+""))){
							aux_fecha=lis_fecha.get(i)+"";
							aux_cuota=lis_cuotas.get(i)+"";
							aux_nrder=lis_nrder.get(i)+"";
							lis_fecha.set(i, lis_fecha.get(j));
							lis_cuotas.set(i, lis_cuotas.get(j));
							lis_nrder.set(i, lis_nrder.get(j));
							lis_fecha.set(j, aux_fecha);
							lis_cuotas.set(j, aux_cuota);
							lis_nrder.set(j, aux_nrder);
						}
					} catch (Exception e) {
						System.out.println("error exepcion servicio anticipo metodo calcular tabla amortizacion ");
						// TODO: handle exception
					}
				}
			}
			dou_saldo_capital=monto;				
			for (int j = 0; j < lis_fecha.size(); j++) {
				tab_amortizacion.insertar();
				tab_amortizacion.setValor("NRO_CUOTA_NRAMO", (j+1)+"");
				tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", lis_fecha.get(j)+"");
				tab_amortizacion.setValor("CUOTA_NRAMO", lis_cuotas.get(j)+"");
				tab_amortizacion.setValor("IDE_NRRUB", lis_nrder.get(j)+"");

				// asignacion cuota
				dou_cuota=Double.parseDouble(lis_cuotas.get(j)+"");
				// calculo interes mensual
				dou_interes_mensual=(dou_saldo_capital*(tasa_interes/100))/(360/amortizacion_cada);// formula calculo intereses
				// calculo de columna saldo principal de la tabla de amortizacion
				dou_principal=dou_cuota-dou_interes_mensual;
				//calculo de columna saldo_capital de la tabla de amortizacion
				dou_saldo_capital=dou_saldo_capital-dou_principal;

				// redondeo los valores para mejor visualizacion 
				BigDecimal big_int_men=new BigDecimal(dou_interes_mensual);
				big_int_men=big_int_men.setScale(2, RoundingMode.HALF_UP);

				BigDecimal big_saldo_capital=new BigDecimal(dou_saldo_capital);
				big_saldo_capital=big_saldo_capital.setScale(2, RoundingMode.HALF_UP);
				BigDecimal big_principal=new BigDecimal(dou_principal);
				big_principal=big_principal.setScale(2, RoundingMode.HALF_UP);
				BigDecimal big_cuota=new BigDecimal(dou_cuota);
				big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);

				tab_amortizacion.setValor("CAPITAL_NRAMO",big_saldo_capital+"");
				tab_amortizacion.setValor("INTERES_NRAMO",big_int_men+"");
				tab_amortizacion.setValor("PRINCIPAL_NRAMO", big_principal+"");
			}

		}
		
		
		// PARA MAYOR EXACTITUD EN LA ULTIMA FILA DE LA TABLA 

		if (tasa_interes==0){
			double dou_tot_cuotas=0;
			try {
				if (tab_amortizacion!=null && tab_amortizacion.getTotalFilas()>0){
					dou_tot_cuotas=tab_amortizacion.getSumaColumna("CUOTA_NRAMO");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			double dou_diferencia=monto-dou_tot_cuotas;
			System.out.println("deferencia "+dou_diferencia);

			double dou_ultima_cuota=0;

			try {
				if (tab_amortizacion!=null && tab_amortizacion.getTotalFilas()>0){
					dou_ultima_cuota=Double.parseDouble(tab_amortizacion.getValor(0, "CUOTA_NRAMO"));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			dou_ultima_cuota=dou_ultima_cuota+dou_diferencia; 

			BigDecimal big_ultima_cuota=new BigDecimal(dou_ultima_cuota);
			big_ultima_cuota=big_ultima_cuota.setScale(2, RoundingMode.HALF_UP);

			if (tab_amortizacion.getTotalFilas()>0){
				tab_amortizacion.setValor(0,"CUOTA_NRAMO",big_ultima_cuota+"");
				try {
					if (tab_amortizacion.getTotalFilas()>1){
						dou_saldo_capital=Double.parseDouble(tab_amortizacion.getValor(1, "CAPITAL_NRAMO"));	
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}


			dou_cuota=dou_ultima_cuota;
			// calculo interes mensual
			dou_interes_mensual=(dou_saldo_capital*(tasa_interes/100))/(360/amortizacion_cada);// formula calculo intereses
			// calculo de columna saldo principal de la tabla de amortizacion
			dou_principal=dou_cuota-dou_interes_mensual;
			//calculo de columna saldo_capital de la tabla de amortizacion
			dou_saldo_capital=dou_saldo_capital-dou_principal;

			// redondeo los valores para mejor visualizacion 
			BigDecimal big_int_men=new BigDecimal(dou_interes_mensual);
			big_int_men=big_int_men.setScale(2, RoundingMode.HALF_UP);

			BigDecimal big_saldo_capital=new BigDecimal(dou_saldo_capital);
			big_saldo_capital=big_saldo_capital.setScale(2, RoundingMode.HALF_UP);
			BigDecimal big_principal=new BigDecimal(dou_principal);
			big_principal=big_principal.setScale(2, RoundingMode.HALF_UP);
			BigDecimal big_cuota=new BigDecimal(dou_cuota);
			big_cuota=big_cuota.setScale(2, RoundingMode.HALF_UP);


			if (tab_amortizacion.getTotalFilas()>1){
				tab_amortizacion.setValor(0,"CAPITAL_NRAMO",big_saldo_capital+"");
				tab_amortizacion.setValor(0,"INTERES_NRAMO",big_int_men+"");
				tab_amortizacion.setValor(0,"PRINCIPAL_NRAMO", big_principal+"");
			}


		}
		return tab_amortizacion;
	}


	private TablaGenerica getTablaRubroDetallePago(String IDE_NRCAP){
		TablaGenerica tab_rubro_det_pago=utilitario.consultar("select * from NRH_RUBRO_DETALLE_PAGO WHERE IDE_NRCAP="+IDE_NRCAP);
		return tab_rubro_det_pago;
	}

	private TablaGenerica getTablaCondicionAnticipo(String IDE_GTTCO){
		TablaGenerica tab_cond_antp=utilitario.consultar("select * from NRH_CONDICION_ANTICIPO where IDE_GTTCO="+IDE_GTTCO+" and ACTIVO_NRCOA=true");
		return tab_cond_antp;
	}

	public boolean validarUnicoRubroDetallePagoAnticipo(TablaGenerica tab_rubro_det_pago,String IDE_NRCAP){
		TablaGenerica tab_rub_det_pago=getTablaRubroDetallePago(IDE_NRCAP);
		for (int i = 0; i < tab_rub_det_pago.getTotalFilas(); i++) {
			for (int j = 0; j < tab_rubro_det_pago.getTotalFilas(); j++) {
				if (tab_rubro_det_pago.isFilaInsertada(j)){
					if (tab_rubro_det_pago.getValor(j, "IDE_NRRUB").equals(tab_rub_det_pago.getValor(i, "IDE_NRRUB"))){
						utilitario.agregarMensajeInfo("No se puede guardar", "Restriccion unica para el campo IDE_NRRUB");
						return false;
					}
				}
			}
		}
		return true;
	}

	public String getMinimoDiasLaboradosParaAnticipos(String IDE_GEEDP){

		TablaGenerica tab_condicion_anticipo=getTablaCondicionAnticipo(ser_nomina.getEmpleadoDepartamento(IDE_GEEDP).getValor("IDE_GTTCO"));
		if (tab_condicion_anticipo.getTotalFilas()>0){
			if (tab_condicion_anticipo.getValor(0, "MINIMO_TRABAJO_NRCOA")!=null && !tab_condicion_anticipo.getValor(0, "MINIMO_TRABAJO_NRCOA").isEmpty()){
				return tab_condicion_anticipo.getValor(0, "MINIMO_TRABAJO_NRCOA");
			}
		}
		return null;
	}


	public String getPlazoMaximoPagoAnticipos(String IDE_GEEDP){
		TablaGenerica tab_condicion_anticipo=getTablaCondicionAnticipo(ser_nomina.getEmpleadoDepartamento(IDE_GEEDP).getValor("IDE_GTTCO"));

		if (tab_condicion_anticipo.getTotalFilas()>0){
			if (tab_condicion_anticipo.getValor(0, "PLAZO_MAXIMO_PAGO_NRCOA")!=null && !tab_condicion_anticipo.getValor(0, "PLAZO_MAXIMO_PAGO_NRCOA").isEmpty()){
				return tab_condicion_anticipo.getValor(0, "PLAZO_MAXIMO_PAGO_NRCOA");
			}
		}
		return null;
	}


	private String getNumMaxRemuneracionesAnticipo(String IDE_GEEDP){
		TablaGenerica tab_condicion_anticipo=getTablaCondicionAnticipo(ser_nomina.getEmpleadoDepartamento(IDE_GEEDP).getValor("IDE_GTTCO"));
		if (tab_condicion_anticipo.getTotalFilas()>0){
			if (tab_condicion_anticipo.getValor(0, "NRO_REMUNERACIONES_NRCOA")!=null && !tab_condicion_anticipo.getValor(0, "NRO_REMUNERACIONES_NRCOA").isEmpty()){
				String num_max_remunera=tab_condicion_anticipo.getValor(0, "NRO_REMUNERACIONES_NRCOA");
				return num_max_remunera;
			}
		}
		return null;
	}

	public String getMontoMaximoPermitidoAnticipo(String IDE_GEEDP){
		String RMB=ser_nomina.getRemuneracionEmpleado(IDE_GEEDP);
		String num_max_rem=getNumMaxRemuneracionesAnticipo(IDE_GEEDP);
		double dou_monto_maximo_anticipo=0;
		try {
			dou_monto_maximo_anticipo=Double.parseDouble(RMB)*Double.parseDouble(num_max_rem);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return utilitario.getFormatoNumero(dou_monto_maximo_anticipo, 2);
	}

	public String getNumeroAnticipo(String IDE_GTEMP){
		TablaGenerica tab_num_max_anticipos=utilitario.consultar("select IDE_GTEMP,max(NRO_ANTICIPO_NRANT) as max_anticipos from NRH_ANTICIPO where ANTICIPO_NRANT=TRUE AND IDE_GTEMP="+IDE_GTEMP+" GROUP BY IDE_GTEMP");
		if (tab_num_max_anticipos.getTotalFilas()>0){
			if (tab_num_max_anticipos.getValor(0,"max_anticipos")!=null && !tab_num_max_anticipos.getValor(0,"max_anticipos").isEmpty()){
				try {
					return (Integer.parseInt(tab_num_max_anticipos.getValor(0,"max_anticipos"))+1)+"";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else{
				return "1";
			}
		}else{
			return "1";
		}
		return null;
	}

	public String getNumeroCuentaCobrar(String IDE_GTEMP){
		TablaGenerica tab_num_max_anticipos=utilitario.consultar("select IDE_GTEMP,max(NRO_ANTICIPO_NRANT) as max_anticipos from NRH_ANTICIPO where ANTICIPO_NRANT=0 AND IDE_GTEMP="+IDE_GTEMP+" GROUP BY IDE_GTEMP");
		if (tab_num_max_anticipos.getTotalFilas()>0){
			if (tab_num_max_anticipos.getValor(0,"max_anticipos")!=null && !tab_num_max_anticipos.getValor(0,"max_anticipos").isEmpty()){
				try {
					return (Integer.parseInt(tab_num_max_anticipos.getValor(0,"max_anticipos"))+1)+"";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else{
				return "1";
			}
		}else{
			return "1";
		}
		return null;
	}

}

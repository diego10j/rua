/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina; 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.primefaces.event.SelectEvent;

import com.lowagie.text.pdf.codec.Base64.OutputStream;

import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Encriptar;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
/**
 *
 * 
 */
public class pre_spi extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();

	private SeleccionTabla set_nominas_cerradas=new SeleccionTabla();
	private  Dialogo dia_ingreso= new Dialogo();
	private Texto tex_maximo=new Texto();
	private int int_maximo_detalle=-1;
	private Etiqueta eti_totales=new Etiqueta();
	private double dou_total=0;

	private SeleccionTabla sel_tab_tipo_nomina=new SeleccionTabla();

	public pre_spi() {

		Boton bot_importar= new Boton();
		bot_importar.setValue("1) Importar Nominas Cerradas");
		bot_importar.setMetodo("importar");
		bar_botones.agregarBoton(bot_importar);

		Boton bot_generar= new Boton();
		bot_generar.setValue("2) Genarar Archivo SPI");
		bot_generar.setMetodo("generarArchivo");
		bot_generar.setAjax(false);
		bar_botones.agregarBoton(bot_generar);

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("SPI_TRANSFERENCIAS", "IDE_SPTRA", 1);
		tab_tabla1.getColumna("ACTIVO_SPTRA").setCheck();
		tab_tabla1.getColumna("ACTIVO_SPTRA").setValorDefecto("true");	
		tab_tabla1.getColumna("FECHA_SPTRA").setValorDefecto(utilitario.getFechaActual());
		tab_tabla1.getColumna("HORA_SPTRA").setValorDefecto(utilitario.getFormatoHora("12:00:00"));
//		tab_tabla1.getColumna("ide_tetic").setCombo(ser_Tesoreria.getConsultaTipoConcepto("true,false"));
		tab_tabla1.agregarRelacion(tab_tabla2);		
		tab_tabla1.onSelect("seleccionarTabla1");
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("SPI_TRANSFERENCIAS_DETALLE", "IDE_SPTRD", 2);
		tab_tabla2.setLectura(true);
		tab_tabla2.setRows(15);
		tab_tabla2.getColumna("ACTIVO_SPTRD").setCheck();
		tab_tabla2.getColumna("ACTIVO_SPTRD").setValorDefecto("true");
		tab_tabla2.dibujar();		
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);
		pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);
		pat_panel2.getMenuTabla().getItem_eliminar().setValue("Eliminar Todos");
		pat_panel2.getMenuTabla().getItem_eliminar().setMetodo("eliminarDetalles");
		tab_tabla2.setLectura(false);
		Division div_division = new Division();
		div_division.setId("div_division");

		eti_totales.setId("eti_totales");		
		eti_totales.setEstiloCabecera("width:%;font-size: 16px;font-weight: bold;display: block;padding-left:10px;");

		Grid gri_totales=new Grid();		
		gri_totales.setWidth("100%");
		gri_totales.getChildren().add(eti_totales);
		gri_totales.setStyle("overflow:hidden;");;
		Division div_div=new Division();
		div_div.setFooter(pat_panel2, gri_totales, "90%");			

		div_division.dividir2(pat_panel1, div_div, "30%", "H");
		agregarComponente(div_division);

		set_nominas_cerradas.setId("set_nominas_cerradas");
		set_nominas_cerradas.setTitle("Importar Nominas Procesadas");
		set_nominas_cerradas.getGri_cuerpo().setMensajeInfo("Seleccione el Periodo para generar el archivo S.P.I.");
		set_nominas_cerradas.setSeleccionTabla("select  PERI.IDE_GEPRO, nom_geani,nombre_gemes from NRH_ROL rol inner join GEN_PERIDO_ROL peri on ROL.IDE_GEPRO = PERI.IDE_GEPRO inner join GEN_MES mes on PERI.IDE_GEMES=MES.IDE_GEMES  inner join GEN_ANIO anio on PERI.IDE_GEANI=anio.IDE_GEANI   where IDE_NRESR=1 group by PERI.IDE_GEPRO, nom_geani,nombre_gemes,MES.IDE_GEMES order by nom_geani DESC ,MES.IDE_GEMES desc", "IDE_GEPRO");		
		set_nominas_cerradas.getTab_seleccion().getColumna("nom_geani").setNombreVisual("AñO");
		set_nominas_cerradas.getTab_seleccion().getColumna("nom_geani").setLongitud(12);
		set_nominas_cerradas.getTab_seleccion().getColumna("nom_geani").setFiltro(true);
		set_nominas_cerradas.getTab_seleccion().getColumna("nombre_gemes").setNombreVisual("MES");
		set_nominas_cerradas.getTab_seleccion().getColumna("nombre_gemes").setFiltro(true);
		set_nominas_cerradas.getTab_seleccion().getColumna("nombre_gemes").setLongitud(20);
		set_nominas_cerradas.setRadio();
		set_nominas_cerradas.setWidth("50%");
		set_nominas_cerradas.setHeight("60%");
		set_nominas_cerradas.getBot_aceptar().setMetodo("aceptarImportar");
		set_nominas_cerradas.getBot_cancelar().setMetodo("cancelarImportar");
		agregarComponente(set_nominas_cerradas);

		dia_ingreso.setId("dia_ingreso");
		dia_ingreso.setTitle("SECUENCIAL DETALLE");
		dia_ingreso.setWidth("30%");
		dia_ingreso.setHeight("23%");		
		dia_ingreso.getBot_aceptar().setMetodo("importar");	

		Grid gri_grid=new Grid();
		gri_grid.setStyle("height:" + (dia_ingreso.getAltoPanel()-10) + "px;overflow: auto;display: block;");
		gri_grid.setColumns(1);
		gri_grid.setWidth("98%");
		gri_grid.getChildren().add(new Etiqueta("Ingrese el n�mero maximo del secuencial del detalle, desde este numero se iniciara el detalle de los empleados "));
		tex_maximo.setStyle("width:98%");
		tex_maximo.setSoloEnteros();
		gri_grid.getChildren().add(tex_maximo);		
		dia_ingreso.setDialogo(gri_grid);		
		agregarComponente(dia_ingreso);


		dou_total=tab_tabla2.getSumaColumna("MONTO_TRANSFERIDO_SPTRD");
		eti_totales.setValue("TOTAL : "+utilitario.getFormatoNumero(dou_total));


		sel_tab_tipo_nomina.setId("sel_tab_tipo_nomina");
		sel_tab_tipo_nomina.setHeader("N�minas Cerradas");
		sel_tab_tipo_nomina.getBot_aceptar().setMetodo("aceptarImportar");
		sel_tab_tipo_nomina.getBot_cancelar().setMetodo("cancelarImportar");
		
		sel_tab_tipo_nomina.setSeleccionTabla("select " +
				"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
				"TEM.DETALLE_GTTEM," +
				"TIC.DETALLE_GTTCO, " +
				"SUC.NOM_SUCU " +
				"from NRH_ROL ROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"where ROL.IDE_GEPRO=-1","IDE_NRROL");
		agregarComponente(sel_tab_tipo_nomina);


		Boton bot_excel=new Boton();
		bot_excel.setValue("Exportar EXCEL");
		bot_excel.setAjax(false);
		bot_excel.setMetodo("exportarExcel");
		bar_botones.agregarBoton(bot_excel);

	}

	public void exportarExcel(){
		if (tab_tabla2.getTotalFilas()>0){
				exportarXLS("spi.xls","1","1");
			
		}
	}
	public void exportarXLS(String nombre,String tipo_nomina,String mes) { 
		try { 
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); 
			String nom = nombre; 
			File result = new File(extContext.getRealPath("/" + nom)); 
			WritableWorkbook archivo_xls = Workbook.createWorkbook(result); 
			WritableSheet hoja_xls = archivo_xls.createSheet("Tabla", 0); 
			WritableFont fuente = new WritableFont(WritableFont.TAHOMA, 10);
			WritableCellFormat formato_celda = new WritableCellFormat(fuente); 
			formato_celda.setAlignment(jxl.format.Alignment.LEFT); 
			formato_celda.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda.setOrientation(Orientation.HORIZONTAL); 
			//formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			WritableFont fuente_suc = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_suc = new WritableCellFormat(fuente_suc); 
			formato_celda_suc.setAlignment(jxl.format.Alignment.LEFT); 
			formato_celda_suc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda_suc.setOrientation(Orientation.HORIZONTAL); 
			formato_celda_suc.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			WritableFont fuente_totales = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat formato_celda_totales = new WritableCellFormat(fuente_suc); 
			formato_celda_totales.setAlignment(jxl.format.Alignment.RIGHT); 
			formato_celda_totales.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda_totales.setOrientation(Orientation.HORIZONTAL); 
			formato_celda_totales.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.RED);

			WritableCellFormat formato_celda_valor_rubro = new WritableCellFormat(fuente); 
			formato_celda_valor_rubro.setAlignment(jxl.format.Alignment.RIGHT); 
			formato_celda_valor_rubro.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
			formato_celda_valor_rubro.setOrientation(Orientation.HORIZONTAL); 
			formato_celda_valor_rubro.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

			int int_columna = 0; 
			int int_fila=4;

			CellView cv=new CellView();
			for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
					// NOMBRES DE COLUMNAS
					// DEPARTAMENTO
					jxl.write.Label lab1 = new jxl.write.Label(0, 0, "CEDULA/RUC", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(0,cv);

					// CEDULA
					lab1 = new jxl.write.Label(1, 0, "REFERENCIA", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(1,cv);

					// NOMBRE 
					lab1 = new jxl.write.Label(2, 0, "NOMBRE", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(2,cv);

					// INSTITUCION FINANCIERA
					lab1 = new jxl.write.Label(3, 0, "INSTITUCION FINANCIERA", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(3,cv);

					// CUENTA BENEFICIARIO
					lab1 = new jxl.write.Label(4, 0, "CUENTA BENEFICIARIO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(4,cv);		
					
					// TIPOCUENTA
					lab1 = new jxl.write.Label(5, 0, "TIPOCUENTA", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(5,cv);

					// VALOR
					lab1 = new jxl.write.Label(6, 0, "VALOR", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(6,cv);

					// CONCEPTO
					lab1 = new jxl.write.Label(7, 0, "CONCEPTO", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(7,cv);
					
					// DETALLE
					lab1 = new jxl.write.Label(8, 0, "DETALLE", formato_celda);
					hoja_xls.addCell(lab1);
					cv=new CellView();
					cv.setAutosize(true);
					hoja_xls.setColumnView(8,cv);	
					
					
						//Cargamos RUC
						jxl.write.Label lab2 = new jxl.write.Label(0, i+1,tab_tabla2.getValor(i, "cedula_sptrd"), formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(0,cv);
						//Cargamos REFRECNIA
						jxl.write.Number labnum1 = new jxl.write.Number(1, i+1,Double.parseDouble(tab_tabla2.getValor(i, "secuencial_sptrd")), formato_celda);
						hoja_xls.addCell(labnum1);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(1,cv);

						//Cargamos PROVEEDRO

						lab2 = new jxl.write.Label(2, i+1,tab_tabla2.getValor(i, "empleado_sptrd"), formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(2,cv);
						//Cargamos INSITUCION FINANCIERA
						jxl.write.Number labnum2 = new jxl.write.Number(3, i+1,Double.parseDouble(tab_tabla2.getValor(i, "cod_banco_sptrd")), formato_celda);
						hoja_xls.addCell(labnum2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(3,cv);
						
						//Cargamos CUENTA BENEFICIARIO
						jxl.write.Number labnum3 = new jxl.write.Number(4, i+1,Double.parseDouble(tab_tabla2.getValor(i, "nro_cuenta_sptrd")), formato_celda);
						hoja_xls.addCell(labnum3);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(4,cv);
						
						//Cargamos tipo cuenta bancaria
						jxl.write.Number labnum4 = new jxl.write.Number(5, i+1,Double.parseDouble(tab_tabla2.getValor(i, "tipo_cuenta_sptrd")), formato_celda);
						hoja_xls.addCell(labnum4);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(5,cv);
						
						//Cargamos VALOR PAGAR
						jxl.write.Number labnum = new jxl.write.Number(6, i+1,Double.parseDouble(tab_tabla2.getValor(i, "monto_transferido_sptrd")), formato_celda);
						hoja_xls.addCell(labnum);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(6,cv);
						
						//Cargamos CODIGO CONCEPTO
						//System.out.println("antes concepto ");

						TablaGenerica tab_concepto=utilitario.consultar("select ide_tetic,detalle_tetic,codigo_tetic from tes_tipo_concepto where ide_tetic in (select ide_tetic from spi_transferencias where ide_sptra="+tab_tabla1.getValorSeleccionado()+")");
						//System.out.println(" concepto ");
						//tab_concepto.imprimirSql();
						jxl.write.Number labnum5 = new jxl.write.Number(7, i+1,Double.parseDouble(tab_concepto.getValor("codigo_tetic")), formato_celda);
						hoja_xls.addCell(labnum5);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(7,cv);
						
						//Cargamos RUC
						TablaGenerica tab_detalle=utilitario.consultar("select ide_sptra,detalle_tranferencia_sptra from spi_transferencias where ide_sptra="+tab_tabla1.getValorSeleccionado());
						tab_detalle.imprimirSql();

						lab2 = new jxl.write.Label(8, i+1,tab_detalle.getValor( "detalle_tranferencia_sptra"), formato_celda);
						hoja_xls.addCell(lab2);
						cv=new CellView();
						cv.setAutosize(true);
						hoja_xls.setColumnView(8,cv);
					
					
				int_fila=int_fila+1;
			}



			archivo_xls.write(); 
			archivo_xls.close(); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(extContext.getRequestContextPath() + "/" + nom); 
		} catch (Exception e) { 
			System.out.println("Error no se genero el XLS :" + e.getMessage()); 
		} 
	}


	public void seleccionarTabla1(SelectEvent evt){
		tab_tabla1.seleccionarFila(evt);
		sumarValores();
	}
	public void seleccionarTabla1(AjaxBehaviorEvent evt){
		tab_tabla1.seleccionarFila(evt);
		sumarValores();
	}


	private void sumarValores(){
		dou_total=tab_tabla2.getSumaColumna("MONTO_TRANSFERIDO_SPTRD");
		eti_totales.setValue("TOTAL : "+utilitario.getFormatoNumero(dou_total));
		utilitario.addUpdate("eti_totales");
	}

	public void cancelarImportar(){
		int_maximo_detalle=-1;
		set_nominas_cerradas.cerrar();
		sel_tab_tipo_nomina.cerrar();
		tex_maximo.limpiar();
	}

	/**
	 * Genera el archivo zip que contiene dos archivos el .txt y el .md5
	 */
	public void generarArchivo(){		
		if(tab_tabla1.getValor("IDE_SPTRA")==null || tab_tabla2.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "No existen registros para la generación del archivo");
			return;
		}
		if(tab_tabla2.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede generar el Archivo", "Primero debe guardar los registros importados");
			return;
		}

		//Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
		try {
			//StringBuilder str_spi=new StringBuilder();
			String str_fecha_hora=tab_tabla1.getValor("FECHA_SPTRA");
			String str_cuenta_banco=tab_tabla1.getValor("NRO_CUENTA_BANCO_SPTRA");
			String str_tipo_cuenta_banco=tab_tabla1.getValor("TIPO_CUENTA_SPTRA");
			String str_institucion=tab_tabla1.getValor("INSTITUCION_SPTRA");
			String str_localidad=tab_tabla1.getValor("LOCALIDAD_SPTRA");
			String str_detalle_archivo=tab_tabla1.getValor("DETALLE_TRANFERENCIA_SPTRA");

			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			String fileName= utilitario.getNombreMes(utilitario.getMes(str_fecha_hora))+utilitario.getAnio(str_fecha_hora);
			String path= extContext.getRealPath("/");			

			File archivotxt=new File(path+fileName.concat(".txt"));
			BufferedWriter escribir=new BufferedWriter(new FileWriter(archivotxt)); 

			String str_dia=utilitario.getDia(str_fecha_hora)>=10?utilitario.getDia(str_fecha_hora)+"":"0"+utilitario.getDia(str_fecha_hora);
			String str_mes=utilitario.getMes(str_fecha_hora)>=10?utilitario.getMes(str_fecha_hora)+"":"0"+utilitario.getMes(str_fecha_hora);



			str_fecha_hora=str_dia+"/"+str_mes+"/"+utilitario.getAnio(str_fecha_hora);
			str_fecha_hora=str_fecha_hora.concat(" ").concat(tab_tabla1.getValor("HORA_SPTRA"));			
			StringBuilder str_cabecera=new StringBuilder(str_fecha_hora).append(",");		
			str_cabecera.append(tab_tabla1.getValor("SECUECIAL_SPI_SPTRA")).append(",");
			str_cabecera.append(tab_tabla2.getTotalFilas()).append(",");
			str_cabecera.append(utilitario.getFormatoNumero(dou_total)).append(",");
			str_cabecera.append(utilitario.getVariable("p_sri_control_spi")).append(",");
			str_cabecera.append(str_cuenta_banco);		
			//str_spi.append(str_cabecera);
			escribir.write(str_cabecera.toString());	
			tab_tabla2.ejecutarSql();			
			for(int i=0;i<tab_tabla2.getTotalFilas();i++){
		//		str_spi.append("\r\n");
				StringBuilder str_detalle=new StringBuilder();
				str_detalle.append(str_fecha_hora).append(",");			
				str_detalle.append(tab_tabla2.getValor(i, "SECUENCIAL_SPTRD")).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "COD_ORIGEN_PAGO_SPTRD")).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "CODIGO_MONEDA_SPTRD")).append(",");
				str_detalle.append(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "MONTO_TRANSFERIDO_SPTRD"))).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "COD_CONCEPTO_PAGO_SPTRD")).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "COD_BANCO_SPTRD")).append(",");				
				str_detalle.append(str_cuenta_banco).append(",");
				str_detalle.append(str_tipo_cuenta_banco).append(",");
				str_detalle.append(str_institucion).append(",");
				str_detalle.append(str_localidad).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "NRO_CUENTA_SPTRD")).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "TIPO_CUENTA_SPTRD")).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "EMPLEADO_SPTRD")).append(",");
				str_detalle.append(str_detalle_archivo).append(",");
				str_detalle.append(tab_tabla2.getValor(i, "CEDULA_SPTRD"));	
				String str_spi_encr="";
				try {
					 str_spi_encr=str_detalle.toString();
					str_spi_encr=str_spi_encr.replaceAll("�", "A");
					str_spi_encr=str_spi_encr.replaceAll("�", "E");
					str_spi_encr=str_spi_encr.replaceAll("�", "I");
					str_spi_encr=str_spi_encr.replaceAll("�", "O");
					str_spi_encr=str_spi_encr.replaceAll("�", "U");
					str_spi_encr=str_spi_encr.replaceAll("�", "a");
					str_spi_encr=str_spi_encr.replaceAll("�", "e");
					str_spi_encr=str_spi_encr.replaceAll("�", "i");
					str_spi_encr=str_spi_encr.replaceAll("�", "o");
					str_spi_encr=str_spi_encr.replaceAll("�", "u");
					str_spi_encr=str_spi_encr.replaceAll("�", "N");
					str_spi_encr=str_spi_encr.replaceAll("�", "n");
				} catch (Exception e) {
					// TODO: handle exception
				}				
				//str_spi.append(str_spi_encr);
				escribir.newLine();
				escribir.write(str_spi_encr.toString());				
			}
			escribir.close();
			Encriptar encriptar=new Encriptar();

			
			File archivomd5=new File(path+fileName.concat(".md5"));
			
			//String str_spi_encr=str_spi.toString();
			String str_spi_encr="";
			BufferedReader entrada;
			try {
			entrada = new BufferedReader( new FileReader( archivotxt ) );
			String linea;
			while(entrada.ready()){
			linea = entrada.readLine();
			if(str_spi_encr.isEmpty()==false){
				str_spi_encr+="\r\n";
			}
			str_spi_encr+=linea;
			}
			}catch (IOException e) {
			e.printStackTrace();
			}
			
			
			escribir=new BufferedWriter(new FileWriter(archivomd5));
			System.out.println("str spi "+str_spi_encr.toString());
			escribir.write(encriptar.getEncriptar(str_spi_encr).concat(" *").concat(fileName).concat(".txt"));
			escribir.close();					
			utilitario.crearArchivoZIP(new File[]{archivotxt,archivomd5}, fileName.concat(".zip"));						
		} catch (Exception e) {
			// TODO: handle exception
			utilitario.crearError("xxx", "xxss", e);
		}
	}

	/**Calcula el numero maximo del secuencial de la cabecera del SPI
	 * @return
	 */
	private int getMaximoSecuencialCabecera(){
		List lis_max=utilitario.getConexion().consultar("SELECT MAX(SECUECIAL_SPI_SPTRA) AS MAXIMO FROM SPI_TRANSFERENCIAS");
		if(lis_max!=null){
			try {
				return Integer.parseInt(lis_max.get(0).toString())+1;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}

	/**Calcula el numero maximo del secuencial del detalle de los empleados
	 * @return
	 */
	private int getMaximoSecuencialEmpleados(){
		List lis_max=utilitario.getConexion().consultar("SELECT MAX(SECUENCIAL_SPTRD)AS MAXIMO FROM SPI_TRANSFERENCIAS_DETALLE");
		if(lis_max!=null){
			try {
				return Integer.parseInt(lis_max.get(0).toString())+1;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}



	public void importar(){
		if(tab_tabla2.isEmpty()){
			if(getMaximoSecuencialEmpleados()==0 && int_maximo_detalle==-1){
				if(dia_ingreso.isVisible()){
					if(tex_maximo.getValue()!=null && !tex_maximo.getValue().toString().isEmpty()){
						try {
							int_maximo_detalle=Integer.parseInt(tex_maximo.getValue().toString());
						} catch (Exception e) {
							// TODO: handle exception
						}
						dia_ingreso.cerrar();
					}
					else{
						utilitario.agregarMensajeInfo("Debe ingresar un valor en el campo de texto", "");
						return;
					}
				}else{
					dia_ingreso.dibujar();	
					return;
				}			
			}

			if(!tab_tabla1.isEmpty()){
				set_nominas_cerradas.dibujar();	
			}
			else{
				utilitario.agregarMensajeInfo("No se puede Importar", "Debe tener un registro en la tabla");
			}		

		}
		else{
			utilitario.agregarMensajeInfo("No se puede Importar ", "Ya existen  empleados importados");
		}

	}

	String str_seleccionados;
	public void aceptarImportar(){

		if(set_nominas_cerradas.isVisible()){
			str_seleccionados=set_nominas_cerradas.getValorSeleccionado();
			if(str_seleccionados!=null && !str_seleccionados.isEmpty()){
				set_nominas_cerradas.cerrar();

				sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
						"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
						"TEM.DETALLE_GTTEM," +
						"TIC.DETALLE_GTTCO, " +
						"SUC.NOM_SUCU " +
						"from NRH_ROL ROL " +
						"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
						"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
						"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
						"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
						"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
						"where ROL.IDE_GEPRO="+str_seleccionados +"  AND IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada"));

				sel_tab_tipo_nomina.dibujar();

			}
			else{
				utilitario.agregarMensajeInfo("Debe seleccionar un per�odo", "");
			}
		}
		else if(sel_tab_tipo_nomina.isVisible()){
				String str_rol=sel_tab_tipo_nomina.getSeleccionados();
				if(str_rol!=null && !str_rol.isEmpty()){
					sel_tab_tipo_nomina.cerrar();
					TablaGenerica tab_importa=utilitario.consultar("select deta.VALOR_NRDRO,substring (APELLIDO_PATERNO_GTEMP || ' ' || (case when APELLIDO_MATERNO_GTEMP is null then '' else APELLIDO_MATERNO_GTEMP end) || ' ' || PRIMER_NOMBRE_GTEMP || ' ' || (case when SEGUNDO_NOMBRE_GTEMP is null then '' else SEGUNDO_NOMBRE_GTEMP  end)  from 1 for 30) AS NOMBRES,CODIGO_GTTCB, DOCUMENTO_IDENTIDAD_GTEMP,NUMERO_CUENTA_GTCBE,CODIGO_BANCO_GEINS from NRH_DETALLE_ROL deta inner join NRH_DETALLE_RUBRO dtr on DETA.IDE_NRDER=DTR.IDE_NRDER inner join GEN_EMPLEADOS_DEPARTAMENTO_PAR par on deta.ide_geedp=par.ide_geedp inner join GTH_EMPLEADO emp on par.ide_gtemp=emp.ide_gtemp left join GTH_CUENTA_BANCARIA_EMPLEADO cuenta on cuenta.IDE_GTEMP=emp.IDE_GTEMP  and ACREDITACION_GTCBE=true left join GEN_INSTITUCION insti on insti.IDE_GEINS=cuenta.IDE_GEINS  left join GTH_TIPO_CUENTA_BANCARIA tipoc on cuenta.IDE_GTTCB=tipoc.IDE_GTTCB where DETA.IDE_NRROL IN (SELECT IDE_NRROL FROM NRH_ROL WHERE IDE_GEPRO = "+str_seleccionados+" AND IDE_NRROL in("+str_rol+"))  and DTR.IDE_NRRUB="+utilitario.getVariable("p_nrh_rubro_valor_recibir")+" and deta.VALOR_NRDRO > 0 order by APELLIDO_PATERNO_GTEMP ");
					tab_tabla2.setDibujo(false);			
					String p_sri_tipo_moneda_spi=utilitario.getVariable("p_sri_tipo_moneda_spi");
					String p_sri_tipo_pago_spi=utilitario.getVariable("p_sri_tipo_pago_spi");
					String p_sri_concepto_spi=utilitario.getVariable("p_sri_concepto_spi");

					if(int_maximo_detalle==-1){
						int_maximo_detalle=getMaximoSecuencialEmpleados();
					}

					for(int i=0;i<tab_importa.getTotalFilas();i++){
						tab_tabla2.insertar();
						tab_tabla2.setValor(0, "SECUENCIAL_SPTRD",String.valueOf(int_maximo_detalle));
						tab_tabla2.setValor(0, "COD_ORIGEN_PAGO_SPTRD",p_sri_tipo_pago_spi);///////?????via internet
						tab_tabla2.setValor(0, "CODIGO_MONEDA_SPTRD",p_sri_tipo_moneda_spi);////////???? moneda dolar
						tab_tabla2.setValor(0, "MONTO_TRANSFERIDO_SPTRD",tab_importa.getValor(i, "VALOR_NRDRO"));
						tab_tabla2.setValor(0, "COD_CONCEPTO_PAGO_SPTRD",p_sri_concepto_spi);
						tab_tabla2.setValor(0, "COD_BANCO_SPTRD",tab_importa.getValor(i, "CODIGO_BANCO_GEINS"));
						tab_tabla2.setValor(0, "NRO_CUENTA_SPTRD",tab_importa.getValor(i, "NUMERO_CUENTA_GTCBE"));
						tab_tabla2.setValor(0, "TIPO_CUENTA_SPTRD",tab_importa.getValor(i, "CODIGO_GTTCB"));
						tab_tabla2.setValor(0, "EMPLEADO_SPTRD",tab_importa.getValor(i, "NOMBRES"));
						tab_tabla2.setValor(0, "CEDULA_SPTRD",tab_importa.getValor(i, "DOCUMENTO_IDENTIDAD_GTEMP"));				
						tab_tabla2.setValor(0, "IDE_SPTRA",tab_tabla1.getValor("IDE_SPTRA"));
						int_maximo_detalle++;
					}

					String str_sin_cuenta=getEmpleadosSinCuentaBancaria();
					if(!str_sin_cuenta.isEmpty()){
						utilitario.agregarMensajeInfo("Los siguientes empleados no tienen configurado cuenta bancaria", str_sin_cuenta);
						tab_tabla2.limpiar();
					}
					
					
					dou_total=tab_tabla2.getSumaColumna("MONTO_TRANSFERIDO_SPTRD");
					eti_totales.setValue("TOTAL : "+utilitario.getFormatoNumero(dou_total));
					tab_tabla2.setDibujo(true);	  
					sumarValores();
					utilitario.addUpdate("tab_tabla2");
					
					
					

				}
				else{
					utilitario.agregarMensajeInfo("Debe seleccionar por lo menos un tipo de n�mina", "");
				}
				}


	}

	
	private String getEmpleadosSinCuentaBancaria(){
		String str_msj="";		
		for(int i=0;i<tab_tabla2.getTotalFilas();i++){
			if(tab_tabla2.getValor(i, "NRO_CUENTA_SPTRD")==null || tab_tabla2.getValor(i, "NRO_CUENTA_SPTRD").isEmpty()){
				str_msj +=tab_tabla2.getValor(i,"EMPLEADO_SPTRD")+" \n";
			}
		}		
		return str_msj;		
	}

	@Override
	public void insertar() {

		tab_tabla1.insertar();		
		tab_tabla1.setValor("FECHA_SPTRA", utilitario.getFechaHoraActual());
		tab_tabla1.setValor("SECUECIAL_SPI_SPTRA", String.valueOf(getMaximoSecuencialCabecera()));





		sumarValores();
	}

	@Override
	public void guardar() {
		if(tab_tabla1.guardar()){
			tab_tabla2.guardar();
		}
		guardarPantalla();
	}

	public void eliminarDetalles() {
		if(tab_tabla1.getValorSeleccionado()!=null){
			String str_mensaje=utilitario.getConexion().ejecutarSql("DELETE FROM SPI_TRANSFERENCIAS_DETALLE WHERE IDE_SPTRA="+tab_tabla1.getValorSeleccionado());
			if(	str_mensaje.isEmpty()){
				utilitario.addUpdate("tab_tabla2");
				utilitario.agregarMensaje("Se Guardo Correctamente", "Se eliminaron todos los detalles");
				tab_tabla2.limpiar();
			}
			else{
				utilitario.agregarMensajeError("No se pudo eliminar los detalles", str_mensaje);
			}		
		}
		sumarValores();

	}

	@Override
	public void eliminar() {
		tab_tabla1.eliminar();
		sumarValores();
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

	public SeleccionTabla getSet_nominas_cerradas() {
		return set_nominas_cerradas;
	}

	public void setSet_nominas_cerradas(SeleccionTabla set_nominas_cerradas) {
		this.set_nominas_cerradas = set_nominas_cerradas;
	}


	public Dialogo getDia_ingreso() {
		return dia_ingreso;
	}


	public void setDia_ingreso(Dialogo dia_ingreso) {
		this.dia_ingreso = dia_ingreso;
	}


	public SeleccionTabla getSel_tab_tipo_nomina() {
		return sel_tab_tipo_nomina;
	}

	public void setSel_tab_tipo_nomina(SeleccionTabla sel_tab_tipo_nomina) {
		this.sel_tab_tipo_nomina = sel_tab_tipo_nomina;
	}


}

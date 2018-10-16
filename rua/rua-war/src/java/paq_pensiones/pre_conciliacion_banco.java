/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

/**
 *
 * @author ANDRES
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import org.primefaces.event.FileUploadEvent;
import sistema.aplicacion.Pantalla;
import jxl.Sheet;
import jxl.Workbook;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.pensiones.ServicioPensiones;

public class pre_conciliacion_banco extends Pantalla{
    private Upload upl_archivo=new Upload();
    private List<String[]> lis_importa=null; //Guardo los empleados y el valor del rubro
    private Tabla tab_tabla = new Tabla();
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    public pre_conciliacion_banco (){
                upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivo");

	//	upl_archivo.setUpdate("gri_valida");		
		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
		upl_archivo.setUploadLabel("Validar");
		upl_archivo.setCancelLabel("Cancelar Seleccion");
		bar_botones.agregarComponente(upl_archivo);
                
                tab_tabla.setId("tab_tabla");
                //tab_tabla.setTabla("rec_valores", "ide_titulo_recval", 1);
                //tab_tabla.setCondicion("ide_recest = "+ utilitario.getVariable("p_pen_deuda_activa"));
                tab_tabla.setSql(ser_pensiones.getAlumnosDeuda(utilitario.getVariable("p_pen_deuda_activa")));
                tab_tabla.setCampoOrden("ide_titulo_recval limit 500");
                /*tab_tabla.getColumna("ide_geper").setVisible(false);
                tab_tabla.getColumna("ide_recalp").setCombo("select ide_recalp, c.nom_geani, b.descripcion_repea, b.fecha_inicial_repea, b.fecha_final_repea\n" +
                                                     "from rec_alumno_periodo a\n" +
                                                     "inner join rec_periodo_academico b on a.ide_repea = b.ide_repea\n" +
                                                     "inner join gen_anio c on b.ide_geani = c.ide_geani\n" +
                                                     "order by fecha_inicial_repea");
                tab_tabla.getColumna("ide_recalp").setAutoCompletar();
                tab_tabla.getColumna("ide_cocaj").setCombo(ser_adquisiciones.getTipoCaja());
                tab_tabla.getColumna("gen_ide_geper").setCombo(ser_pensiones.getSqlComboRepresentantes());
                tab_tabla.getColumna("gen_ide_geper").setAutoCompletar();
                tab_tabla.getColumna("gen_ide_geper").setNombreVisual("REPRESENTANTE");
                tab_tabla.getColumna("IDE_GTEMP").setCombo("gth_empleado", "ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp,primer_nombre_gtemp", "");
                tab_tabla.getColumna("IDE_GTEMP").setAutoCompletar();
                tab_tabla.getColumna("IDE_GTEMP").setNombreVisual("REGISTRADORR");
                tab_tabla.getColumna("GTH_IDE_GTEMP").setCombo("gth_empleado", "ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp,primer_nombre_gtemp", "");
                tab_tabla.getColumna("GTH_IDE_GTEMP").setAutoCompletar();
                tab_tabla.getColumna("GTH_IDE_GTEMP").setNombreVisual("RECAUDADOR");
                tab_tabla.getColumna("ide_concepto_recon").setCombo("rec_concepto", "ide_concepto_recon", "des_concepto_recon", "");
                tab_tabla.getColumna("ide_concepto_recon").setRequerida(true);
                tab_tabla.getColumna("ide_recest").setCombo("rec_estados", "ide_recest", "descripcion_recest", "");
                tab_tabla.getColumna("TOTAL_RECVA ").setEtiqueta();
                tab_tabla.getColumna("TOTAL_RECVA ").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:blue");//Estilo*/
                tab_tabla.setRows(500);
                tab_tabla.setLectura(true);
                tab_tabla.dibujar();
                PanelTabla pat_panel = new PanelTabla();
                pat_panel.setPanelTabla(tab_tabla);

                Division div_division = new Division();
                div_division.setId("div_division");
                div_division.dividir1(pat_panel);
                agregarComponente(div_division);
    }
    
    public void validarArchivo(FileUploadEvent evt){	
			//Leer el archivo
			String str_msg_info="";
			String str_msg_adve="";
			String str_msg_erro="";
			double dou_tot_valor_imp=0;
                        String tab_alumn_val = "";
                        String tab_alumn_valor_deuda = "";
                        String ide_geper = "";
			try {
				//Válido que el rubro seleccionado este configurado en los tipo de nomina

				Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
				Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA
				if (hoja == null) {
					utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
					return;
				}
				int int_fin = hoja.getRows();
				upl_archivo.setNombreReal(evt.getFile().getFileName());

				str_msg_info+=getFormatoInformacion("El archivo "+upl_archivo.getNombreReal()+" contiene "+int_fin+" filas");
                                
                                
				lis_importa=new ArrayList<String[]>();
			        int total_tabla= tab_tabla.getRowCount();
				//System.out.println("valor de la tabal "+total_tabla+" valor impreso"+tab_tabla.getValor(1, "ide_fafac"));
				for(int k=0; k< tab_tabla.getTotalFilas();k ++){
				tab_alumn_val = tab_tabla.getValor(k, "codigo_alumno");
                                tab_alumn_valor_deuda = tab_tabla.getValor(k, "valor_total");
                                ide_geper = tab_tabla.getValor(k, "ide_geper");
				for (int i = 0; i < int_fin; i++) {
					//leo los valores a considerar en la conciliacion del archivo excel
					String str_codigo_alumno = hoja.getCell(7, i).getContents();	
                                        String str_fecha_pago = hoja.getCell(1, i).getContents();
                                        String valor_pago = hoja.getCell(10, i).getContents();
                                        String num_comprobante = hoja.getCell(15, i).getContents();
					str_codigo_alumno=str_codigo_alumno.trim(); 
                                       
                                        valor_pago=valor_pago.trim(); 
					num_comprobante=num_comprobante.trim(); 
                                        str_fecha_pago = str_fecha_pago.trim(); 
                                        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
                                        String fecha= formatoDeFecha.format(str_fecha_pago);
                                        //System.out.println(formatoDeFecha.format(utilitario.getFormatoFecha(str_fecha_pago)));
                                        if (tab_alumn_val.equals(str_codigo_alumno) && tab_alumn_valor_deuda.equals(valor_pago)){
                                               TablaGenerica tab_valores_deuda = utilitario.consultar("select * from rec_valores where ide_recest = "+utilitario.getVariable("p_pen_deuda_activa")+" and ide_geper = "+ide_geper+"");
                                               for (int m=0; m<tab_valores_deuda.getTotalFilas(); m++){
                                                String ide_titulo = tab_valores_deuda.getValor(m, "ide_titulo_recval");
                                                   System.out.println("titulo "+ide_titulo);
                                                utilitario.getConexion().ejecutarSql("update rec_valores "
                                                                                   + "set ide_recest = "+utilitario.getVariable("p_pen_deuda_recaudada")+" , ide_cndfp = "+utilitario.getVariable("p_pen_transferencia_forma_pago")+", num_titulo_recva = "+num_comprobante+", fecha_pago_recva = '"+fecha+"' "
                                                                                   + "where ide_titulo_recval = "+ide_titulo+"");
                                               }
                                        }
					
					/*System.out.println("imprimo valor celda factura "+str_codigo_alumno);
                                        System.out.println("imprimo fecha "+str_fecha_pago);
                                        System.out.println("imprimo valor "+valor_pago);*/
					
				//	TablaGenerica tab_factura=ser_facturacion.getDatosClienteFactura(str_cedula_cliente, str_codigo_tercero);

				/*	if(tab_factura.isEmpty() ){
						//No existe el documento en la tabla de tab_factura
						str_msg_erro+=getFormatoError("El documento de Identidad: "+str_cedula_cliente+" no se encuentra registrado en la base de datos, fila "+(i+1));
					}*/
					  
					/*String str_valor_conciliar = hoja.getCell(3, i).getContents();
					String str_valor = hoja.getCell(19, i).getContents();
					double double_valor_conciliar= Double.parseDouble(str_valor_conciliar.replace(",", "."));
					System.out.println("imprimo valro a conciliar "+str_valor);
					tab_tabla.insertar();
					tab_tabla.setValor(0, "valor_conciliado_fafac", double_valor_conciliar+"");*/
                                        
                                         
                                        

				}
				
				}
				utilitario.addUpdate("tab_tabla");;

			} catch (Exception e) {
				// TODO: handle exception
                                utilitario.agregarMensajeError("No se encuentran bien ingresados los valores en el archivo", "Por favor Verificar.");
			}	
			
	}
    private String getFormatoInformacion(String mensaje){
		return "<div><font color='#3333ff'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
    }
    private String getFormatoError(String mensaje){
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}
    
    @Override
    public void insertar() {
        //tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        //tab_tabla1.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       //tab_tabla1.eliminar();
    }

    public Upload getUpl_archivo() {
        return upl_archivo;
    }

    public void setUpl_archivo(Upload upl_archivo) {
        this.upl_archivo = upl_archivo;
    }

    public List<String[]> getLis_importa() {
        return lis_importa;
    }

    public void setLis_importa(List<String[]> lis_importa) {
        this.lis_importa = lis_importa;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
}

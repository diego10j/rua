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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.FileUploadEvent;
import sistema.aplicacion.Pantalla;
import jxl.Sheet;
import jxl.Workbook;

public class pre_conciliacion_banco extends Pantalla{
    private Upload upl_archivo=new Upload();
    private List<String[]> lis_importa=null; //Guardo los empleados y el valor del rubro
    private Tabla tab_tabla = new Tabla();
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
                tab_tabla.setTabla("rec_valores", "ide_titulo_recval", 1);
                tab_tabla.setCondicion("1=1 ");
                tab_tabla.setCampoOrden("ide_titulo_recval limit 500");
                tab_tabla.setRows(500);
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
				System.out.println("valor de la tabal "+total_tabla+" valor impreso"+tab_tabla.getValor(1, "ide_fafac"));
				for(int k=0; k< tab_tabla.getTotalFilas();k ++){
				
				for (int i = 0; i < int_fin; i++) {
					//codigo tercero remplaza a str_cedula permite leer el codigo de la factutra
					String str_codigo_tercero = hoja.getCell(4, i).getContents();	
					str_codigo_tercero=str_codigo_tercero.trim(); 
					String str_cedula_cliente =hoja.getCell(32, i).getContents();
					str_cedula_cliente = str_cedula_cliente.trim();
					
					System.out.println("imprimo valor celda factura "+str_codigo_tercero+"  numero d ecedula" +str_cedula_cliente);
					
				//	TablaGenerica tab_factura=ser_facturacion.getDatosClienteFactura(str_cedula_cliente, str_codigo_tercero);

				/*	if(tab_factura.isEmpty() ){
						//No existe el documento en la tabla de tab_factura
						str_msg_erro+=getFormatoError("El documento de Identidad: "+str_cedula_cliente+" no se encuentra registrado en la base de datos, fila "+(i+1));
					}*/
					  
					String str_valor_conciliar = hoja.getCell(3, i).getContents();
					String str_valor = hoja.getCell(19, i).getContents();
					double double_valor_conciliar= Double.parseDouble(str_valor_conciliar.replace(",", "."));
					System.out.println("imprimo valro a conciliar "+str_valor);
					tab_tabla.insertar();
					tab_tabla.setValor(0, "valor_conciliado_fafac", double_valor_conciliar+"");

				}
				
				}
				utilitario.addUpdate("tab_tabla");;

			} catch (Exception e) {
				// TODO: handle exception
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

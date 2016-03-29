/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.component.chart.line.LineChart;
import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import com.lowagie.text.pdf.ArabicLigaturizer;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;

import sistema.aplicacion.Utilitario;


/**
 *
 * @author HP-USER
 */
public class cls_graficas extends BarChart {


	private Utilitario utilitario=new Utilitario();
	public cls_graficas() {


	}

	
	public List getColumnasEjeX(TablaGenerica tab_rep){
		String str_sql_rep=tab_rep.getSql();
		str_sql_rep=str_sql_rep.toLowerCase();
		
		String str_sql_col="SELECT a.ide_col_ejex,a.nom_col_ejex FROM ( ";
		
		str_sql_col=str_sql_col.concat(str_sql_rep);

		String orden=str_sql_rep.substring(str_sql_rep.indexOf("order by")+9,str_sql_rep.length());

		
		do {
			if (orden.indexOf("order by")!=-1){
				orden=orden.substring(orden.indexOf("order by")+9,orden.length());
			}
		} while (orden.indexOf("order by")!=-1);
			
		
		if (orden.indexOf(",")!=-1){
			orden=orden.substring(orden.indexOf(",")+1, orden.length());
		}
		
		
		str_sql_col=str_sql_col.concat(" ) a GROUP BY a.ide_col_ejex,a.nom_col_ejex,"+orden+" " +
				"ORDER by a."+orden);
		

		TablaGenerica tab_col=utilitario.consultar(str_sql_col);
		System.out.println("str sql columnas "+str_sql_col);
		List lis_col_ejeX=new ArrayList();
		for (int i = 0; i < tab_col.getTotalFilas(); i++) {
			lis_col_ejeX.add(tab_col.getValor(i, "nom_col_ejex"));
		}

		return lis_col_ejeX;
	}
	
	private CartesianChartModel categoryModel = new CartesianChartModel();
	int int_num_label=0;
	List lis_col_ejeX=new ArrayList();

	private CartesianChartModel getCategoryModel(TablaGenerica tab_rep){

		if (tab_rep.getTotalFilas()>0){
			

			ChartSeries serie = new ChartSeries();


			String str_detalle_label="";
			String str_ide_label="";
			int_num_label=0;
			lis_col_ejeX=getColumnasEjeX(tab_rep);
			System.out.println("num col eje x "+lis_col_ejeX.size());

			int int_indice_col_eje_x=0;
			for (int i = 0; i < tab_rep.getTotalFilas(); i++) {
				if (i==0){
					str_ide_label=tab_rep.getValor(i, "ide_label");
					serie.setLabel(tab_rep.getValor(i, "detalle_label"));
					int_num_label+=1;
				}
				System.out.println("label "+str_ide_label+" label recorre "+tab_rep.getValor(i, "ide_label"));
				if (str_ide_label.equalsIgnoreCase(tab_rep.getValor(i, "ide_label"))){
					str_detalle_label=tab_rep.getValor(i, "nom_col_ejeX");
					String nom_col_eje_x="";
					do {
						try {
							nom_col_eje_x=lis_col_ejeX.get(int_indice_col_eje_x)+"";
						} catch (Exception e) {
							// TODO: handle exception
						}
						int_indice_col_eje_x=int_indice_col_eje_x+1;
						if (nom_col_eje_x.equalsIgnoreCase(str_detalle_label)){
							double valor=0;
							try {
								valor=Double.parseDouble(tab_rep.getValor(i, "MONTO"));
							} catch (Exception e) {
								// TODO: handle exception
							}
							serie.set(str_detalle_label,valor);
						}else{
							serie.set(nom_col_eje_x,0);
						}
					} while (!nom_col_eje_x.equalsIgnoreCase(str_detalle_label));

				}else{

					System.out.println("indice col eje x "+int_indice_col_eje_x+" lis col eje x "+lis_col_ejeX.size());
					if (int_indice_col_eje_x<lis_col_ejeX.size()){
						do {
							serie.set(lis_col_ejeX.get(int_indice_col_eje_x),0);
							int_indice_col_eje_x=int_indice_col_eje_x+1;
						} while (int_indice_col_eje_x<=lis_col_ejeX.size()-1);
					}

					categoryModel.addSeries(serie);

					serie = new ChartSeries();
					str_ide_label=tab_rep.getValor(i, "ide_label");
					serie.setLabel(tab_rep.getValor(i, "detalle_label"));
					int_num_label+=1;
					str_detalle_label=tab_rep.getValor(i, "nom_col_ejeX");
					int_indice_col_eje_x=0;
					String nom_col_eje_x="";

					do {
						nom_col_eje_x=lis_col_ejeX.get(int_indice_col_eje_x)+"";
						int_indice_col_eje_x=int_indice_col_eje_x+1;
						if (nom_col_eje_x.equalsIgnoreCase(str_detalle_label)){
							double valor=0;
							try {
								valor=Double.parseDouble(tab_rep.getValor(i, "MONTO"));
							} catch (Exception e) {
								// TODO: handle exception
							}
							serie.set(str_detalle_label,valor);
						}else{
							serie.set(nom_col_eje_x,0);
						}
					} while (!nom_col_eje_x.equalsIgnoreCase(str_detalle_label));
					
				}
			}
			categoryModel.addSeries(serie);				
			return categoryModel;
			
		}else{
			
			utilitario.agregarMensajeInfo("No existen datos","");
			return null;
		}
	}
	
	/**
	 * @param tab_rep :tabla generica que contiene la consulta a graficar con el respectivo formato
	 * @param titulo 
	 * @param width
	 * @param heigth
	 * @return BarChart: el grafico en barras  
	 */
	public BarChart getBarchar(TablaGenerica tab_rep,String titulo,int width,int heigth){

		System.out.println("sql construir "+tab_rep.getSql());
		if (tab_rep.getTotalFilas()==0){
			BarChart barchart=new BarChart();
			barchart.setId("basic");
			barchart.setWidgetVar("basic");
			barchart.getSeriesColors();
			barchart.setShowDatatip(true);
			barchart.setAnimate(true);
			barchart.setRendered(true);
			barchart.setXaxisAngle(23);
			barchart.setLegendPosition("nw");

			barchart.setMin(0);
			barchart.setMax(0);
			return barchart;
		}
		String sql=tab_rep.getSql();
		sql=sql.toLowerCase();
		System.out.println("index "+sql.indexOf("order by"));
		if (sql.indexOf("order by")==-1){
			sql="select * from ( ".concat(sql);
			sql+=") a order by a.ide_label,a.ide_col_ejeX";
			tab_rep.setSql(sql);
			tab_rep.ejecutarSql();
			System.out.println("nuevo sql "+tab_rep.getSql());
		}
		categoryModel = new CartesianChartModel();

		categoryModel=getCategoryModel(tab_rep);

		BarChart barchart=new BarChart();
		barchart.setId("basic");
		barchart.setWidgetVar("basic");
		barchart.getSeriesColors();
		barchart.setShowDatatip(true);
		barchart.setAnimate(true);
		barchart.setRendered(true);
		barchart.setXaxisAngle(23);
		barchart.setLegendPosition("nw");

		barchart.setMin(0);
		// calculo valor maximo
		String str_sql="";
		str_sql=str_sql.concat("select * FROM ( ");
		str_sql=str_sql.concat(tab_rep.getSql());
		str_sql=str_sql.concat(") a ORDER by monto DESC");
		TablaGenerica tab_val_max=utilitario.consultar(str_sql);
		double dou_val_max=0;
		try {
			dou_val_max=Double.parseDouble(tab_val_max.getValor(0,"MONTO"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		barchart.setMax(dou_val_max+20);

		if(dou_val_max>1000){
			barchart.setMax(dou_val_max+2000);
		}

		// calculo ancho del barchar
		int int_ancho_label=35;
		if (int_num_label==1){
			int_ancho_label=70;
		}
		System.out.println("num labels "+int_num_label+" col eje x "+lis_col_ejeX.size());
		int int_ancho_barchar=(int_ancho_label*int_num_label)*lis_col_ejeX.size();
		System.out.println("ancho barchar "+int_ancho_barchar);
		if (int_ancho_barchar<width){
			int_ancho_barchar=width;
		}

		barchart.setStyle("height:"+heigth+"px;width:"+int_ancho_barchar+"px");
		barchart.setTitle(titulo);
		barchart.setValue(categoryModel);
		
		return barchart;
	}


	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}


	public void setCategoryModel(CartesianChartModel categoryModel) {
		this.categoryModel = categoryModel;
	}
	
	
}

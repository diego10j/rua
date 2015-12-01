/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.component.chart.line.LineChart;
import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import pkg_contabilidad.cls_contabilidad;
import sistema.aplicacion.Utilitario;


/**
 *
 * @author stalin
 */
public class cls_graficas extends BarChart {

    public BarChart barchart = new BarChart();
    
    private LineChart linechart=new LineChart();
    
    private PieChart piechart=new PieChart();
    public CartesianChartModel categoryModel;
    private Utilitario utilitario = new Utilitario();
    cls_contabilidad con = new cls_contabilidad();
    ArrayList<String> mes=new ArrayList<String>();

    //PIE
    private PieChartModel pieModel;

    public cls_graficas() {
       //estilo BARCHART
        barchart.setAnimate(true);
        barchart.setId("basic");
        barchart.setWidgetVar("basic");
        barchart.setStyle("height:500px");
        barchart.setStyle("Width:900px");
        barchart.getSeriesColors();
        barchart.setShowDatatip(true);
        barchart.setRendered(true);
        barchart.setXaxisAngle(23);
        barchart.setLegendPosition("ne");
       
        

        //estilo LINECHART
        linechart.setAnimate(true);
//        linechart.setZoom(true);
        linechart.setId("category");
        linechart.setWidgetVar("category");
        linechart.setStyle("height:500px");
        linechart.setStyle("Width:900px");
        linechart.setRendered(true);
        linechart.setXaxisAngle(23);
        linechart.setLegendPosition("ne");
        
        
        //estilo pie
        piechart.setId("custom");
        piechart.setWidgetVar("custom");
        piechart.setFill(false);
        piechart.setShowDataLabels(true);
        piechart.setDiameter(250);
        piechart.setSliceMargin(5);
        piechart.setLegendPosition("e");
        piechart.setStyle("width:900px;height:500px");
        piechart.setTitle("Empledos x Departamentos");
        

        
        mes.add("ENERO");
        mes.add("FEBRERO");
        mes.add("MARZO");
        mes.add("ABRIL");
                 
        
        
       
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();


        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void createGraficoRubros(Tabla consulta_sql, ArrayList<String> rubros) {

        categoryModel = new CartesianChartModel();
        
//         barchart.setZoom(true);
        ChartSeries sueldos = new ChartSeries();
        sueldos.setLabel("SUELDOS");
        ChartSeries aportes = new ChartSeries();
        aportes.setLabel("APORTES");
        ChartSeries prestamos = new ChartSeries();
        prestamos.setLabel("PRESTAMOS");
        ChartSeries otros_descuentos = new ChartSeries();
        otros_descuentos.setLabel("OTROS DESCUENTOS");
        
        

        for (int i = 0; i < consulta_sql.getTotalFilas(); i++) {

            sueldos.set(consulta_sql.getValor(i, rubros.get(0)), Double.parseDouble(consulta_sql.getValor(i, rubros.get(1))));
            aportes.set(consulta_sql.getValor(i, rubros.get(0)), Double.parseDouble(consulta_sql.getValor(i, rubros.get(2))));
            prestamos.set(consulta_sql.getValor(i, rubros.get(0)), Double.parseDouble(consulta_sql.getValor(i, rubros.get(3))));
            otros_descuentos.set(consulta_sql.getValor(i, rubros.get(0)), Double.parseDouble(consulta_sql.getValor(i, rubros.get(4))));
        }
        
        
        categoryModel.addSeries(sueldos);
        categoryModel.addSeries(aportes);
        categoryModel.addSeries(prestamos);
        categoryModel.addSeries(otros_descuentos);
    }
    
  public void createGraficoGeneral(Tabla consulta_sql, ArrayList<String> rubros,String titulo,String Label) {

        categoryModel = new CartesianChartModel();
        barchart.setTitle(titulo);
       
        
        ChartSeries serie = new ChartSeries();
        serie.setLabel(Label);

        



        for (int i = 0; i < consulta_sql.getTotalFilas(); i++) {

            serie.set(consulta_sql.getValor(i, rubros.get(0)), Double.parseDouble(consulta_sql.getValor(i, rubros.get(1))));
            



        }

        categoryModel.addSeries(serie);
        
        
    }
  
  public void createGraficoGeneralPie(Tabla consulta_sql, ArrayList<String> rubros) {
   pieModel=new PieChartModel();
        
        for (int i = 0; i < consulta_sql.getTotalFilas(); i++) {

            pieModel.set(consulta_sql.getValor(i, rubros.get(0)), Double.parseDouble(consulta_sql.getValor(i, rubros.get(1))));
            
        }

        
  }

    public void crearGraficoBalance(ArrayList<String> fechas) {
        categoryModel = new CartesianChartModel();

        List lis_totales;
        List lis_totales_resultados;

        ChartSeries activos = new ChartSeries();
        activos.setLabel("ACTIVOS");
        ChartSeries pasivos = new ChartSeries();
        pasivos.setLabel("PASIVOS");
        ChartSeries patrimonio = new ChartSeries();
        patrimonio.setLabel("PATRIMONIO");
        ChartSeries ingresos = new ChartSeries();
        ingresos.setLabel("INGRESOS");
        ChartSeries gastos = new ChartSeries();
        gastos.setLabel("GASTOS");
        ChartSeries costos = new ChartSeries();
        costos.setLabel("COSTOS");
        
        String str_mes;
        
        for (int i = 0; i < fechas.size(); i++) {
            lis_totales = con.obtenerTotalesBalanceGeneral(false,con.getFechaInicialPeriodo(fechas.get(i)),fechas.get(i));
            lis_totales_resultados = con.obtenerTotalesEstadoResultados(false,con.getFechaInicialPeriodo(fechas.get(i)),fechas.get(i));
//            str_mes=retornar_mes_letras(utilitario.getMes(fechas.get(i)));
            activos.set(retornar_mes_letras(utilitario.getMes(fechas.get(i))),Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(lis_totales.get(0) + ""))));
            pasivos.set(retornar_mes_letras(utilitario.getMes(fechas.get(i))),Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(lis_totales.get(1) + ""))));
            patrimonio.set(retornar_mes_letras(utilitario.getMes(fechas.get(i))),Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(lis_totales.get(2) + ""))));
            ingresos.set(retornar_mes_letras(utilitario.getMes(fechas.get(i))),Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(0) + ""))));
            gastos.set(retornar_mes_letras(utilitario.getMes(fechas.get(i))),Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(1) + ""))));
            costos.set(retornar_mes_letras(utilitario.getMes(fechas.get(i))),Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(2) + ""))));
        }



        categoryModel.addSeries(activos);
        categoryModel.addSeries(pasivos);
        categoryModel.addSeries(patrimonio);
        categoryModel.addSeries(ingresos);
        categoryModel.addSeries(gastos);
        categoryModel.addSeries(costos);
        

    }

    


    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CartesianChartModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public BarChart getBarchart() {
        return barchart;
    }

    public void setBarchart(BarChart barchart) {
        this.barchart = barchart;
    }
    
    public String retornar_mes_letras(int mes) {
        String mes1 = "";
        if (mes == 1) {
            mes1 = "ENERO";
        }
        if (mes == 2) {
            mes1 = "FEBRERO";
        }
        if (mes == 3) {
            mes1 = "MARZO";
        }
        if (mes == 4) {
            mes1 = "ABRIL";
        }
        if (mes == 5) {
            mes1 = "MAYO";
        }
        if (mes == 6) {
            mes1 = "JUNIO";
        }
        if (mes == 7) {
            mes1 = "JULIO";
        }
        if (mes == 8) {
            mes1 = "AGOSTO";
        }
        if (mes == 9) {
            mes1 = "SEPTIEMBRE";
        }
        if (mes == 10) {
            mes1 = "OCTUBRE";
        }
        if (mes == 11) {
            mes1 = "NOVIEMBRE";
        }
        if (mes == 12) {
            mes1 = "DICIEMBRE";
        }
        return mes1;
    }

    public LineChart getLinechart() {
        return linechart;
    }

    public void setLinechart(LineChart linechart) {
        this.linechart = linechart;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public PieChart getPiechart() {
        return piechart;
    }

    public void setPiechart(PieChart piechart) {
        this.piechart = piechart;
    }




            

}

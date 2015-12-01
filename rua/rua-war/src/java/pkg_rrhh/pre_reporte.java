/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imprimir;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import pkg_contabilidad.cls_contabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author stalin
 */
public class pre_reporte extends Pantalla {

    private Division div_division = new Division();
    private PanelAcordion pac_acordion = new PanelAcordion();
    private Combo com_rubros = new Combo();
    private Boton boton_sueldos = new Boton();
    private Grid grid1 = new Grid();
    private Grid grid_grafico = new Grid();
    private Grid grid_contabilidad = new Grid();
    private Grid grid_resultados = new Grid();
    private Grid grid_presupuesto = new Grid();
    private cls_graficas graficas;
    private cls_graficas graficas1;
    private SeleccionTabla sel_tab = new SeleccionTabla();
    ArrayList<String> rubros = new ArrayList<String>();
    cls_contabilidad con = new cls_contabilidad();
    private Check che_rubros_anual = new Check();
    private Check che_departamentos = new Check();
    private SeleccionCalendario sec_rango_balance = new SeleccionCalendario();
    private Radio rad_opcion = new Radio();
    private Tabla tab_num_empleados_x_departamentos = new Tabla();
    private Tabla tab_tabla = new Tabla();
    private Radio rad_tipo_grafico = new Radio();

    public pre_reporte() {
        graficas = new cls_graficas();
//        graficas1 = new cls_graficas();

        //tabla rubros de rol de pagos
        tab_tabla.setId("tab_tabla");
        tab_tabla.setCampoPrimaria("codigo");

        Imprimir imp_grafico = new Imprimir();
        imp_grafico.setNombreComponente("grid_grafico");
        bar_botones.agregarComponente(imp_grafico);

        Etiqueta eti = new Etiqueta("GRÁFICOS ESTADÍSTICOS SOCIEDAD SALESIANA");
        eti.setStyle("font-size:18px;");
        Etiqueta eti1 = new Etiqueta("PARÁMETROS RUBROS");
        eti1.setStyle("font-size:10px; aling:left;");
        grid_grafico.setHeader(eti);
        grid1.setId("grid1");
        grid1.setWidth("200");
        grid1.setRendered(true);
        grid1.setHeader(eti1);
        grid_grafico.setId("grid_grafico");
        boton_sueldos.setId("boton_sueldos");
        boton_sueldos.setValue("GRAFICAR");

        Boton bot_ficha = new Boton();
        bot_ficha.setIcon("ui-icon-zoomin");
        bot_ficha.setTitle("ZOO IN");
        bot_ficha.setMetodo("crearZoom");
        bot_ficha.setUpdate("grid_grafico,grid_resultados");

        Boton bot_zoomOut = new Boton();
        bot_zoomOut.setIcon("ui-icon-zoomout");
        bot_zoomOut.setTitle("ZOO OUT");

        bot_zoomOut.setMetodo("crearZoomOut");
        bot_zoomOut.setUpdate("grid_grafico,grid_resultados");

        pac_acordion.setId("pac_acordion");

//        boton_sueldos.setMetodo("graficarNumeroEmpleados_x_Departamento");
////        boton_sueldos.setUpdate("grid_grafico,grid_resultados,pac_acordion");
//        bar_botones.agregarBoton(boton_sueldos);
        // grid de GTH
        com_rubros.setId("com_rubros");
        com_rubros.setCombo("select ide_rhcrp,mes_rhcrp from reh_cab_rol_pago where ide_rhero=0");
        com_rubros.setMetodo("graficarRubrosMes");

        //check rubros por año
        che_rubros_anual.setId("che_rubros_anual");
        che_rubros_anual.setValue("false");
        che_rubros_anual.setMetodoChange("graficarRubrosAnual");

        //check departamentos numero de empleados
        che_departamentos.setId("che_departamentos");
        che_departamentos.setValue("false");
        che_departamentos.setMetodoChange("graficarNumeroEmpleados_x_Departamento");

        grid1.getChildren().add(new Etiqueta("Anual"));
        grid1.getChildren().add(che_rubros_anual);

        grid1.getChildren().add(new Etiqueta("Mes: "));
        grid1.getChildren().add(com_rubros);

        grid1.getChildren().add(new Etiqueta("Departamentos: "));
        grid1.getChildren().add(che_departamentos);

        grid1.setColumns(4);

        //grid contabilidad
        grid_contabilidad.setId("grid_contabilidad");
        grid_contabilidad.setWidth("200");
        grid_contabilidad.setRendered(true);
        Etiqueta eti_titulo_conta = new Etiqueta("PARÁMETROS CUENTAS");
        eti_titulo_conta.setStyle("font-size:10px; aling:left;");
        grid_contabilidad.setHeader(eti_titulo_conta);
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "ANUAL"
        };
        Object fila2[] = {
            "-1", "MESES"
        };

        lista.add(fila1);
        lista.add(fila2);

        rad_opcion.setId("rad_opcion");
        rad_opcion.setRadio(lista);
        rad_opcion.setMetodoChange("escojerTipoBalance");

        //RADIO TIPO DE GRAFICO
        List lista_tipo_grafico = new ArrayList();
        Object fila_1[] = {
            "1", "BAR"
        };
        Object fila_2[] = {
            "-1", "LINE"
        };
        Object fila_3[] = {
            "0", "PIE"
        };

        lista_tipo_grafico.add(fila_1);
        lista_tipo_grafico.add(fila_2);
        lista_tipo_grafico.add(fila_3);
        Etiqueta eti_tipo_grafico = new Etiqueta("TIPO DE GRÁFICO");
        eti_tipo_grafico.setStyle("font-size:14px; aling:left;");

        rad_tipo_grafico.setId("rad_tipo_grafico");
        rad_tipo_grafico.setRadio(lista_tipo_grafico);
        rad_tipo_grafico.setValue("1");
        rad_tipo_grafico.setMetodoChange("escojerTipoGrafico");
        bar_botones.agregarComponente(eti_tipo_grafico);
        bar_botones.agregarComponente(rad_tipo_grafico);
        bar_botones.agregarBoton(bot_ficha);
        bar_botones.agregarBoton(bot_zoomOut);

        sec_rango_balance.setId("sec_rango_balance");
        sec_rango_balance.getBot_aceptar().setMetodo("graficarBalanceMes");
        sec_rango_balance.getBot_aceptar().setUpdate("grid_grafico,grid_resultados");

        sec_rango_balance.setMultiple(false);
        agregarComponente(sec_rango_balance);

        grid_contabilidad.getChildren().add(rad_opcion);
        grid_contabilidad.setColumns(4);

        //GRID RESULTADOS
        Etiqueta eti_cabeza_resul = new Etiqueta("DATOS RESULTADOS EN TABLA");
        eti_cabeza_resul.setStyle("font-size:14px;");
        grid_resultados.setId("grid_resultados");
        grid_resultados.setHeader(eti_cabeza_resul);

        PanelTabla pat_panel2 = new PanelTabla();
        PanelTabla pat_panel3 = new PanelTabla();

        pac_acordion.agregarPanel("GESTIÓN TALENTO HUMANO", grid1);
        pac_acordion.agregarPanel("ACTIVOS FIJOS", pat_panel2);
        pac_acordion.agregarPanel("CONTABILIDAD", grid_contabilidad);
        pac_acordion.agregarPanel("PRESUPUESTO", grid_presupuesto);
        pac_acordion.setRendered(true);

        Division div2 = new Division();
        div2.dividir2(pac_acordion, grid_resultados, "50%", "H");

        div_division.setId("div_division");
        div_division.dividir2(div2, grid_grafico, "25%", "V");

        agregarComponente(div_division);

    }

    public void crearZoom() {
        graficas.getBarchart().setZoom(true);
        graficas.getLinechart().setZoom(true);
    }

    public void crearZoomOut() {
        graficas.getBarchart().setZoom(false);
        graficas.getLinechart().setZoom(false);
    }

    public void abrirRango() {

        sec_rango_balance.setMultiple(false);
        sec_rango_balance.dibujar();
        System.out.println("entra al check");

    }
    //Metodo cuando se escoje el tipo de grafico

    public void escojerTipoGrafico() {
        crearZoomOut();

        //si check de numeros de empleados por departamentos es tre
        if (che_departamentos.getValue().toString().equals("true")) {
            graficarNumeroEmpleados_x_Departamento();
        } else //si check grafica: valor total rubros de rol de pagos anual es true     
        if (che_rubros_anual.getValue().equals("true")) {
            graficarRubrosAnual();
        }//si check grafica:valor total de rubros de rol de pagos por mes es true 
        else if (com_rubros.getValue() != null) {
            graficarRubrosMes();

        } else if (rad_opcion.getValue().toString().equals("1")) {
//            graficarBalanceAnual();
            escojerTipoBalance();
        } else if (rad_opcion.getValue().toString().equals("-1")) {
            grid_grafico.getChildren().clear();
            grid_resultados.getChildren().clear();
            escojerTipoBalance();
//            abrirRango();
//            if (rad_tipo_grafico.getValue().toString().equals("1")){
//            abrirRango();
//            }
//            if (rad_tipo_grafico.getValue().toString().equals("-1")){
//                graficarBalanceMes();
//            }
//            if (rad_tipo_grafico.getValue().toString().equals("0")){
//                graficarBalanceMes();
//            }
        }
        utilitario.addUpdate("grid_grafico,grid_resultados");
    }
//metodo numero de empleados por departamentos

    public void graficarNumeroEmpleados_x_Departamento() {
        System.out.println("entra al metodo # de empleados de departamentos");
        ArrayList<String> departamentos = new ArrayList<String>();
        utilitario.addUpdate("grid_grafico,grid_resultados,pac_acordion");

        if (che_departamentos.getValue().toString().equals("true")) {

            com_rubros.setValue(null);
            che_rubros_anual.setValue(false);
            rad_opcion.setValue(null);
            grid_resultados.getChildren().clear();

            tab_num_empleados_x_departamentos.setSql("select  org.ide_georg codigo,org.nombre_georg departamento,count(*) total from gen_persona pers "
                    + " left join gen_organigrama org on pers.ide_georg=org.ide_georg where es_empleado_geper is true and ide_rheem=0 "
                    + " and pers.ide_georg in (select DISTINCT org.ide_georg from gen_organigrama org, gen_persona pers where org.ide_georg=pers.ide_georg) "
                    + " GROUP BY org.ide_georg,org.nombre_georg "
                    + " order by org.nombre_georg asc ");

            //añado nombres de campos de la consulta sql en un Array
            departamentos.add("departamento");
            departamentos.add("total");

            tab_num_empleados_x_departamentos.ejecutarSql();
            tab_num_empleados_x_departamentos.setId("tab_num_empleados_x_departamentos");
            tab_num_empleados_x_departamentos.setCampoPrimaria("codigo");
            tab_num_empleados_x_departamentos.getColumna("codigo").setVisible(false);
            tab_num_empleados_x_departamentos.getColumna("departamento").setLectura(true);
            tab_num_empleados_x_departamentos.getColumna("total").setLectura(true);
            tab_num_empleados_x_departamentos.getGrid().setColumns(4);
            tab_num_empleados_x_departamentos.setLectura(true);
            tab_num_empleados_x_departamentos.setTipoFormulario(false);

            graficas = new cls_graficas();
            String label = "DEPARTAMENTOS";
            String titulo = "# EMPLEADOS X DEPARTAMENTO";

            //si es tipo de grafico BAR
            if (rad_tipo_grafico.getValue().toString().equals("1")) {

                grid_resultados.getChildren().clear();
                grid_grafico.getChildren().clear();
                tab_num_empleados_x_departamentos.dibujar();
                grid_resultados.getChildren().add(tab_num_empleados_x_departamentos);
                graficas.createGraficoGeneral(tab_num_empleados_x_departamentos, departamentos, titulo, label);
                graficas.getBarchart().setValue(graficas.getCategoryModel());
                grid_grafico.getChildren().add(graficas.getBarchart());
            } else {
                //si es tipo de grafico LINE
                if (rad_tipo_grafico.getValue().toString().equals("-1")) {

                    grid_resultados.getChildren().clear();
                    tab_num_empleados_x_departamentos.dibujar();
                    grid_resultados.getChildren().add(tab_num_empleados_x_departamentos);
                    grid_grafico.getChildren().clear();
                    graficas.createGraficoGeneral(tab_num_empleados_x_departamentos, departamentos, titulo, label);
                    graficas.getLinechart().setValue(graficas.getCategoryModel());

                    grid_grafico.getChildren().add(graficas.getLinechart());
                } else if (rad_tipo_grafico.getValue().toString().equals("0")) {
                    //grafico pie

                    grid_resultados.getChildren().clear();
                    tab_num_empleados_x_departamentos.dibujar();
                    grid_resultados.getChildren().add(tab_num_empleados_x_departamentos);
                    grid_grafico.getChildren().clear();
                    graficas.createGraficoGeneralPie(tab_num_empleados_x_departamentos, departamentos);
                    graficas.getPiechart().setValue(graficas.getPieModel());
                    grid_grafico.getChildren().add(graficas.getPiechart());
                }

            }

        } else {
            grid_grafico.getChildren().clear();
            grid_resultados.getChildren().clear();
        }
        utilitario.addUpdate("grid_grafico,grid_resultados");
    }
//metodo grafica de valor total de rubros de rol de pagos por mes

    public void graficarRubrosMes() {

        grid_resultados.getChildren().clear();
        String valor_mes = com_rubros.getValue().toString();
        utilitario.addUpdate("grid_grafico,grid_resultados,pac_acordion");
        che_rubros_anual.setValue(false);
        che_departamentos.setValue(false);
        rad_opcion.setValue(null);
        tab_tabla.setSql("select cabrol.ide_rhcrp as codigo,cabrol.mes_rhcrp as mes, "
                + " sum(case when rubrol.ide_rhcru=37 then rubrol.valor_rhrro else 0 end) as sueldos, "
                + " sum(case when rubrol.ide_rhcru=28 then rubrol.valor_rhrro else 0 end) as aportes_personales, "
                + " sum(case when rubrol.ide_rhcru=29 then rubrol.valor_rhrro else 0 end) as prestamo_quirografario, "
                + " sum(case when rubrol.ide_rhcru=60 then rubrol.valor_rhrro else 0 end) as otros_descuentos "
                + " from reh_rubros_rol rubrol "
                + " left join reh_empleados_rol emrol on rubrol.ide_rherl=emrol.ide_rherl "
                + " left join reh_cab_rol_pago cabrol on cabrol.ide_rhcrp=emrol.ide_rhcrp "
                + " where cabrol.ide_rhcrp= " + valor_mes + " and emrol.revisado_rherl is true and cabrol.ide_rhero=0 "
                + " GROUP BY cabrol.ide_rhcrp ");
        tab_tabla.ejecutarSql();

        tab_tabla.getColumna("codigo").setVisible(false);
        tab_tabla.getColumna("mes").setLectura(true);
        tab_tabla.getColumna("aportes_personales").setLectura(true);
        tab_tabla.getColumna("prestamo_quirografario").setLectura(true);
        tab_tabla.getColumna("otros_descuentos").setLectura(true);
        tab_tabla.setLectura(true);

        rubros.add("mes");
        rubros.add("sueldos");
        rubros.add("aportes_personales");
        rubros.add("prestamo_quirografario");
        rubros.add("otros_descuentos");

        if (rad_tipo_grafico.getValue().toString().equals("1")) {

            grid_resultados.getChildren().clear();
            grid_grafico.getChildren().clear();
            tab_tabla.dibujar();
            grid_resultados.getChildren().add(tab_tabla);
            graficas.createGraficoRubros(tab_tabla, rubros);
            graficas.getBarchart().setValue(graficas.getCategoryModel());
            grid_grafico.getChildren().add(graficas.getBarchart());
        } else if (rad_tipo_grafico.getValue().toString().equals("-1")) {

            grid_resultados.getChildren().clear();
            tab_tabla.dibujar();
            grid_resultados.getChildren().add(tab_tabla);
            grid_grafico.getChildren().clear();
            graficas.createGraficoRubros(tab_tabla, rubros);

            graficas.getLinechart().setValue(graficas.getCategoryModel());
            grid_grafico.getChildren().add(graficas.getLinechart());
        } else if (rad_tipo_grafico.getValue().toString().equals("0")) {
            utilitario.agregarNotificacionInfo("GRAFICA PIE", "EL REPORTE NO ES APTO PARA ESTE TIPO DE GRÁFICO");
            grid_grafico.getChildren().clear();
        }

        utilitario.addUpdate("grid_grafico,grid_resultados");
    }

    public void graficarRubrosAnual() {

        if (che_rubros_anual.getValue().equals("true")) {

            com_rubros.setValue(null);
            che_departamentos.setValue(false);
            rad_opcion.setValue(null);
            grid_resultados.getChildren().clear();
            utilitario.addUpdate("grid_grafico,grid_resultados,pac_acordion");

            tab_tabla.setSql("select cabrol.ide_rhcrp as codigo,cabrol.mes_rhcrp as mes, "
                    + " sum(case when rubrol.ide_rhcru=37 then rubrol.valor_rhrro else 0 end) as sueldos, "
                    + " sum(case when rubrol.ide_rhcru=28 then rubrol.valor_rhrro else 0 end) as aportes_personales, "
                    + " sum(case when rubrol.ide_rhcru=29 then rubrol.valor_rhrro else 0 end) as prestamo_quirografario, "
                    + " sum(case when rubrol.ide_rhcru=60 then rubrol.valor_rhrro else 0 end) as otros_descuentos "
                    + " from reh_rubros_rol rubrol "
                    + " left join reh_empleados_rol emrol on rubrol.ide_rherl=emrol.ide_rherl "
                    + " left join reh_cab_rol_pago cabrol on cabrol.ide_rhcrp=emrol.ide_rhcrp "
                    + " where cabrol.ide_rhcrp in (select ide_rhcrp from reh_cab_rol_pago where ide_rhero=0) and emrol.revisado_rherl is true "
                    + " GROUP BY cabrol.ide_rhcrp "
                    + " order by cabrol.ide_rhcrp ");
            tab_tabla.ejecutarSql();

            rubros.add("mes");
            rubros.add("sueldos");
            rubros.add("aportes_personales");
            rubros.add("prestamo_quirografario");
            rubros.add("otros_descuentos");

//            tab_tabla.setId("tab_tabla");
//            tab_tabla.setCampoPrimaria("codigo");
            tab_tabla.getColumna("codigo").setVisible(false);
            tab_tabla.getColumna("mes").setLectura(true);
            tab_tabla.getColumna("aportes_personales").setLectura(true);
            tab_tabla.getColumna("prestamo_quirografario").setLectura(true);
            tab_tabla.getColumna("otros_descuentos").setLectura(true);
            tab_tabla.getGrid().setColumns(4);
            tab_tabla.setLectura(true);

            grid_grafico.getChildren().clear();
            if (rad_tipo_grafico.getValue().toString().equals("1")) {

                grid_resultados.getChildren().clear();
                grid_grafico.getChildren().clear();
                tab_tabla.dibujar();
                grid_resultados.getChildren().add(tab_tabla);
                graficas.createGraficoRubros(tab_tabla, rubros);
                graficas.getBarchart().setValue(graficas.getCategoryModel());
                grid_grafico.getChildren().add(graficas.getBarchart());
            } else {
                if (rad_tipo_grafico.getValue().toString().equals("-1")) {

                    grid_resultados.getChildren().clear();
                    tab_tabla.dibujar();
                    grid_resultados.getChildren().add(tab_tabla);
                    grid_grafico.getChildren().clear();
                    graficas.createGraficoRubros(tab_tabla, rubros);

                    graficas.getLinechart().setValue(graficas.getCategoryModel());
                    grid_grafico.getChildren().add(graficas.getLinechart());
                } else if (rad_tipo_grafico.getValue().toString().equals("0")) {
                    utilitario.agregarNotificacionInfo("GRAFICA PIE", "EL REPORTE NO ES APTO PARA ESTE TIPO DE GRÁFICO");
                }
            }

        } else {
            System.out.println(" no check");
            grid_grafico.getChildren().clear();
            grid_resultados.getChildren().clear();

        }
        utilitario.addUpdate("grid_grafico,grid_resultados");

    }

    public void escojerTipoBalance() {
        if (rad_opcion.getValue().toString().equals("1")) {
            graficarBalanceAnual();
        } else if (rad_opcion.getValue().toString().equals("-1")) {
            grid_grafico.getChildren().clear();
            grid_resultados.getChildren().clear();
            abrirRango();
        }
        utilitario.addUpdate("grid_grafico,grid_resultados");
    }

    public void graficarBalanceAnual() {
        List lis_totales_balance;
        List lis_totales_resultados;
        ArrayList<String> fecha = new ArrayList<String>();
        che_rubros_anual.setValue(false);
        che_departamentos.setValue(false);
        com_rubros.setValue(null);
        utilitario.addUpdate("grid_grafico,grid_resultados,pac_acordion");

        if (rad_opcion.getValue().toString().equals("1")) {
            grid_resultados.getChildren().clear();
            grid_grafico.getChildren().clear();
            fecha.add("2013-01-31");
            fecha.add("2013-02-28");
            fecha.add("2013-03-31");
            fecha.add("2013-04-30");

            //etiquetas para grid de resultados
            Etiqueta eti_mes = new Etiqueta("MES");
            eti_mes.setStyle("font-size:12px; font-weight:bold;");
            Etiqueta eti_activo = new Etiqueta("ACTIVO");
            eti_activo.setStyle("font-size:12px; font-weight:bold;");
            Etiqueta eti_pasivo = new Etiqueta("PASIVO");
            eti_pasivo.setStyle("font-size:12px; font-weight:bold;");
            Etiqueta eti_patrimonio = new Etiqueta("PATRIMONIO");
            eti_patrimonio.setStyle("font-size:12px; font-weight:bold;");
            Etiqueta eti_ingresos = new Etiqueta("INGRESOS");
            eti_ingresos.setStyle("font-size:12px; font-weight:bold;");
            Etiqueta eti_gastos = new Etiqueta("GASTOS");
            eti_gastos.setStyle("font-size:12px; font-weight:bold;");
            Etiqueta eti_costos = new Etiqueta("COSTOS");
            eti_costos.setStyle("font-size:12px; font-weight:bold;");

            grid_resultados.getChildren().add(eti_mes);
            grid_resultados.getChildren().add(eti_activo);
            grid_resultados.getChildren().add(eti_pasivo);
            grid_resultados.getChildren().add(eti_patrimonio);
            grid_resultados.getChildren().add(eti_ingresos);
            grid_resultados.getChildren().add(eti_gastos);
            grid_resultados.getChildren().add(eti_costos);

            Etiqueta eti_detalle_mes;

            for (int i = 0; i < fecha.size(); i++) {
                eti_detalle_mes = new Etiqueta(graficas.retornar_mes_letras(utilitario.getMes(fecha.get(i))));
                eti_detalle_mes.setStyle("font-size:10px; font-weight:bold;");
                lis_totales_balance = con.obtenerTotalesBalanceGeneral(false, con.getFechaInicialPeriodo(fecha.get(i)), fecha.get(i));
                lis_totales_resultados = con.obtenerTotalesEstadoResultados(false, con.getFechaInicialPeriodo(fecha.get(i)), fecha.get(i));
                grid_resultados.getChildren().add(eti_detalle_mes);
                grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_balance.get(0).toString()))));
                grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_balance.get(1).toString()))));
                grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_balance.get(2).toString()))));
                grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(0).toString()))));
                grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(1).toString()))));
                grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(2).toString()))));
            }
            grid_resultados.setColumns(7);

            if (rad_tipo_grafico.getValue().toString().equals("1")) {

                grid_grafico.getChildren().clear();
                graficas.crearGraficoBalance(fecha);
                graficas.getBarchart().setValue(graficas.getCategoryModel());
                grid_grafico.getChildren().add(graficas.getBarchart());
            } else {
                if (rad_tipo_grafico.getValue().toString().equals("-1")) {

                    grid_grafico.getChildren().clear();
                    graficas.crearGraficoBalance(fecha);

                    graficas.getLinechart().setValue(graficas.getCategoryModel());
                    grid_grafico.getChildren().add(graficas.getLinechart());
                } else if (rad_tipo_grafico.getValue().toString().equals("0")) {
                    utilitario.agregarNotificacionInfo("GRAFICA PIE", "EL REPORTE NO ES APTO PARA ESTE TIPO DE GRÁFICO");
                    grid_resultados.getChildren().clear();
                }
            }
        }

    }

    public void graficarBalanceMes() {
        ArrayList<String> fecha = new ArrayList<String>();
        List lis_totales_balance;
        List lis_totales_resultados;
        che_rubros_anual.setValue(false);
        che_departamentos.setValue(false);
        com_rubros.setValue(null);
        utilitario.addUpdate("grid_grafico,grid_resultados,pac_acordion");
//        if (sec_rango_balance.isVisible()) {
        grid_resultados.getChildren().clear();
        String str_mes = sec_rango_balance.getFecha1String();
        System.out.println("mes:" + str_mes);

        fecha.add(str_mes);
        lis_totales_balance = con.obtenerTotalesBalanceGeneral(false, con.getFechaInicialPeriodo(str_mes), str_mes);
        lis_totales_resultados = con.obtenerTotalesEstadoResultados(false, con.getFechaInicialPeriodo(str_mes), str_mes);

        sec_rango_balance.cerrar();
//            grid_grafico.getChildren().clear();
//
//            graficas.crearGraficoBalance(fecha);
//
//            graficas.getBarchart().setValue(graficas.getCategoryModel());
//            grid_grafico.getChildren().add(graficas.getBarchart());

        grid_resultados.getChildren().add(new Etiqueta("MES"));
        grid_resultados.getChildren().add(new Etiqueta(graficas.retornar_mes_letras(utilitario.getMes(str_mes))));
        grid_resultados.getChildren().add(new Etiqueta("ACTIVOS"));
        grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_balance.get(0).toString()))));
        grid_resultados.getChildren().add(new Etiqueta("PASIVOS"));
        grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_balance.get(1).toString()))));
        grid_resultados.getChildren().add(new Etiqueta("PATRIMONIO"));
        grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_balance.get(2).toString()))));
        grid_resultados.getChildren().add(new Etiqueta("INGRESOS"));
        grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(0).toString()))));
        grid_resultados.getChildren().add(new Etiqueta("GASTOS"));
        grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(1).toString()))));
        grid_resultados.getChildren().add(new Etiqueta("COSTOS"));
        grid_resultados.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(Double.parseDouble(lis_totales_resultados.get(2).toString()))));
        grid_resultados.setColumns(2);
        System.out.println("mes:==>>" + graficas.retornar_mes_letras(utilitario.getMes(str_mes)));

        if (rad_tipo_grafico.getValue().toString().equals("1")) {

            grid_grafico.getChildren().clear();
            graficas.crearGraficoBalance(fecha);
            graficas.getBarchart().setValue(graficas.getCategoryModel());
            grid_grafico.getChildren().add(graficas.getBarchart());
        } else {
            if (rad_tipo_grafico.getValue().toString().equals("-1")) {
                sec_rango_balance.cerrar();
                grid_grafico.getChildren().clear();
                graficas.crearGraficoBalance(fecha);

                graficas.getLinechart().setValue(graficas.getCategoryModel());
                grid_grafico.getChildren().add(graficas.getLinechart());
            } else if (rad_tipo_grafico.getValue().toString().equals("0")) {
                sec_rango_balance.cerrar();
                utilitario.agregarNotificacionInfo("GRAFICA PIE", "EL REPORTE NO ES APTO PARA ESTE TIPO DE GRÁFICO");
                grid_resultados.getChildren().clear();
            }
        }
//        }
    }

    @Override
    public void insertar() {
//        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
//        tab_tabla.guardar();
//        guardarPantalla();
    }

    @Override
    public void eliminar() {
//        tab_tabla.eliminar();
    }

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
    }

    public Tabla getTab_num_empleados_x_departamentos() {
        return tab_num_empleados_x_departamentos;
    }

    public void setTab_num_empleados_x_departamentos(Tabla tab_num_empleados_x_departamentos) {
        this.tab_num_empleados_x_departamentos = tab_num_empleados_x_departamentos;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}

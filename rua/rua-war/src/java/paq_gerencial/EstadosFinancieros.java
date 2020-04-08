/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import componentes.AsientoContable;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.ListaSeleccion;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import framework.reportes.ReporteDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import paq_gerencial.ejb.ServicioGerencial;
import persistencia.Conexion;
import pkg_contabilidad.cls_contabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Luis Toapanta
 */
public class EstadosFinancieros extends Pantalla {

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    @EJB
    private final ServicioGerencial ser_gerencial = (ServicioGerencial) utilitario.instanciarEJB(ServicioGerencial.class);
    
    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_cuenta;
    private AutoCompletar aut_persona;
    private Conexion conPersistencia = new Conexion();
    private Etiqueta eti_anio_estado = new Etiqueta();
    private Etiqueta eti_obra = new Etiqueta();
    private Etiqueta eti_tip_balance = new Etiqueta();
    private Etiqueta eti_mes = new Etiqueta();
    private Etiqueta eti_estado_detalle = new Etiqueta();
    private Tabla tab_consulta;
    private Tabla tab_tabla3=new Tabla();
    private cls_contabilidad con = new cls_contabilidad();
    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;
    private Radio rad_niveles;

    //Reportes
    private final String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private final String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_casa_obras = new SeleccionTabla();
    private Map parametro = new HashMap();
    private String fecha_fin;
    private String fecha_inicio;
    private VisualizarPDF vipdf_mayor = new VisualizarPDF();
    private AsientoContable asc_asiento = new AsientoContable();
    private Dialogo dia_cerrar_periodo = new Dialogo();
    private Combo com_estado_anio_fiscal=new Combo();
    private Combo com_estado_mes_fiscal=new Combo();
    private Combo com_mes=new Combo();
    private Combo com_tipo_balance=new Combo();
    private Combo com_periodo;
    private Combo com_periodo_financiero;
    private ListaSeleccion lis_tipo_balance = new ListaSeleccion();
    private ListaSeleccion lis_meses = new ListaSeleccion();
    private Radio rad_nivel_inicial = new Radio();
     private Radio rad_nivel_final = new Radio();
    String ide_gebame="-1";
    String str_impresion="-1";
    public EstadosFinancieros() {
        bar_botones.limpiar();

        // Etiquetas de la obra
        eti_anio_estado.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_obra.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_tip_balance.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_mes.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_estado_detalle.setStyle("font-size:14px;font-weight: bold;color:green");
        
        


        mep_menu.setMenuPanel("ESTADOS FINANCIEROS CONTABLES CONSOLIDADOS", "20%");
        mep_menu.agregarItem("Transferir Balance", "dibujarTransferirBalance", "ui-icon-bookmark"); //1
        mep_menu.agregarItem("Estados Financieros", "dibujarEstadosFinancieros", "ui-icon-bookmark");//2
        mep_menu.agregarItem("Balance General", "dibujarBalanceGeneral", "ui-icon-bookmark");//3
        mep_menu.agregarItem("Estado de Resultados", "dibujarEstadoResultados", "ui-icon-bookmark");//4
        mep_menu.agregarSubMenu("GRAFICOS");
        mep_menu.agregarItem("Gráfico Balance", "dibujarGrafico", "ui-icon-bookmark");
        agregarComponente(mep_menu);
        tipoImpresion();

    }

    public void dibujarGrafico() {

    }

    public void dibujarTransferirBalance() {
        com_periodo = new Combo();
        com_periodo.setCombo(ser_gerencial.getAnio("true,false", "1", ""));
        com_periodo.eliminarVacio();
        com_periodo.setMetodo("seleccionaPeriodo");
        com_periodo.setStyle("width: 150px;");
        
        Grupo gru_grupo = new Grupo();
        TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"),com_periodo.getValue().toString(),"1"));
        if(tab_casa_obra.getTotalFilas()>0){
        eti_obra.setValue(tab_casa_obra.getValor("nombre_gercas") + " " + tab_casa_obra.getValor("nombre_gerobr"));
        } else{
            eti_obra.setValue("No existe obras habilitadas para el periódo físcal seleccionado");
        }
        
        com_estado_anio_fiscal.setId("com_estado_anio_fiscal");
        com_estado_anio_fiscal.setCombo(ser_gerencial.getEstado());
        com_estado_anio_fiscal.setValue(tab_casa_obra.getValor("ide_gerest"));
        com_estado_anio_fiscal.setDisabled(true);
        com_estado_anio_fiscal.setStyle("width: 100px;");
        
        
        com_estado_mes_fiscal.setId("com_estado_mes_fiscal");
        com_estado_mes_fiscal.setCombo(ser_gerencial.getEstado());
        com_estado_mes_fiscal.setDisabled(true);
        com_estado_mes_fiscal.setStyle("width: 100px;");
       
        com_tipo_balance.setId("com_tipo_balance");
        com_tipo_balance.setCombo(ser_gerencial.getTipoBalance("1", "-1"));
        com_tipo_balance.setMetodo("actualizarEstadoBalance");
        com_tipo_balance.setStyle("width: 100px;");
        com_mes.setId("com_mes");
        com_mes.setCombo(ser_gerencial.getMes("-1", ""));
        com_mes.setMetodo("actualizarTipoBalance");
        com_mes.setStyle("width: 100px;");
        

        
        Etiqueta eti_des_a = new Etiqueta("Estado Año Fiscal:");
            Etiqueta eti_des_c = new Etiqueta("Casa-Obra:");
            Etiqueta eti_des_t = new Etiqueta("Tipo Balance:");
            Etiqueta eti_des_esta_ba = new Etiqueta("Mes:");
            Etiqueta eti_des_s = new Etiqueta("Estado Mes Fiscal:");
            Etiqueta eti_anio = new Etiqueta("Año Fiscal:");
            eti_anio.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_a.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_c.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_t.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_esta_ba.setStyle("font-size:14px;font-weight: bold;color:black");
            eti_des_s.setStyle("font-size:14px;font-weight: bold;color:black");
            
        Fieldset fis_consulta = new Fieldset();
        Grid gr_dato_titulo=new Grid();
        gr_dato_titulo.setColumns(2);
        gr_dato_titulo.getChildren().add(eti_anio);
        gr_dato_titulo.getChildren().add(com_periodo);
        gr_dato_titulo.getChildren().add(eti_des_c);
        gr_dato_titulo.getChildren().add(eti_obra);
        
        Grid gr_dato_obra=new Grid();
        gr_dato_obra.setColumns(4);
        gr_dato_obra.getChildren().add(eti_des_a);
        gr_dato_obra.getChildren().add(com_estado_anio_fiscal);
        gr_dato_obra.getChildren().add(eti_des_esta_ba);
        gr_dato_obra.getChildren().add(com_mes);
        gr_dato_obra.getChildren().add(eti_des_t);
        gr_dato_obra.getChildren().add(com_tipo_balance);
        gr_dato_obra.getChildren().add(eti_des_s);
        gr_dato_obra.getChildren().add(com_estado_mes_fiscal);
        
        Grid gp = new Grid();
        gp.setColumns(3);

        Boton bot_imprimir_cuentas = new Boton();
        bot_imprimir_cuentas.setIcon("ui-icon-print");
        bot_imprimir_cuentas.setTitle("Imprimir");
        bot_imprimir_cuentas.setValue("Transferir Balance General");
        bot_imprimir_cuentas.setMetodo("transferirBalance");
        
        Boton bot_loquear = new Boton();
        bot_loquear.setIcon("ui-icon-print");
        bot_loquear.setValue("Bloquear Transacción");
        bot_loquear.setMetodo("transferirBalance");
        
        fis_consulta.getChildren().add(gr_dato_titulo);
        fis_consulta.getChildren().add(gr_dato_obra);
        gp.getChildren().add(bot_imprimir_cuentas);
        gp.getChildren().add(bot_loquear);
        gp.getChildren().add(new Etiqueta(""));

        fis_consulta.getChildren().add(gp);
        // combo de fechas
        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(5);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
        cal_fecha_inicio = new Calendario();
        cal_fecha_inicio.setId("cal_fecha_inicio");
        //cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));        
        gri_fechas.getChildren().add(cal_fecha_inicio);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
        cal_fecha_fin = new Calendario();
        cal_fecha_fin.setId("cal_fecha_fin");
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);
        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);

        gru_grupo.getChildren().add(fis_consulta);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("ger_balance_detalle", "ide_gebade", 3);
            tab_tabla3.getColumna("ide_cndpc").setCombo(ser_gerencial.getPlanCuentas());
            tab_tabla3.setCondicion("ide_gebame=-1");
             tab_tabla3.getColumna("ide_gebame").setVisible(false);
            tab_tabla3.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla3.getColumna("ide_cndpc").setLectura(true);
            tab_tabla3.getColumna("valor_debe_gebade").setLectura(true);
            tab_tabla3.getColumna("valor_haber_gebade").setLectura(true);
            tab_tabla3.getColumna("ide_gebade").setNombreVisual("CODIGO");
            tab_tabla3.getColumna("ide_cndpc").setNombreVisual("CUENTA CONTABLE");
            tab_tabla3.getColumna("valor_debe_gebade").setNombreVisual("VALOR DEBE");
            tab_tabla3.getColumna("valor_haber_gebade").setNombreVisual("VALOR HABER");
            tab_tabla3.setRows(15);
            tab_tabla3.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla3);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "TRANSFERENCIA DEL BALANCE GENERAL", gru_grupo);
    }
    public void transferirBalance(){
        if(cal_fecha_inicio.getFecha()==null||cal_fecha_fin.getFecha()==null){
            utilitario.agregarMensajeError("Fechas no validas", "Necesita seleccionar la fecha inicial o fecha final");
            return;
        }
        if(utilitario.isFechasValidas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha())){
            utilitario.agregarMensajeError("Fechas no validas", "Las fecha final del reporte es mayor a la fecha inicial");
            return;
        }
        if(com_estado_mes_fiscal.getValue().toString().equals(utilitario.getVariable("p_ger_estado_activo"))){
        String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
        String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
        String estado="-1";
        if(com_tipo_balance.getValue().toString().equals("1")){  //Inicial
            estado=estado_inicial;
        }
        else if(com_tipo_balance.getValue().toString().equals("2")){  //mensual
            estado=estado_normal;
        }
        TablaGenerica tab_cuenta=utilitario.consultar(ser_gerencial.getTranseferirAsientos(estado, utilitario.getVariable("ide_sucu"), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        utilitario.getConexion().ejecutarSql(ser_gerencial.deleteTempBalance(utilitario.getVariable("ide_usua")));
        if(tab_cuenta.getTotalFilas()>0){
            for(int i=0;i<tab_cuenta.getTotalFilas();i++){    
                //System.out.println("primer insert i "+i);
                utilitario.getConexion().ejecutarSql(ser_gerencial.insertTempBalance(tab_cuenta.getValor(i, "ide_cndpc"),tab_cuenta.getValor(i, "debe"),tab_cuenta.getValor(i, "haber"),utilitario.getVariable("ide_usua"),tab_cuenta.getValor(i, "ide_cnncu") ));                
            }
            int nivel_nuevo=0;
            for(int j=7;j>2; j--){
                nivel_nuevo=j-1;
                System.out.println("j  "+j);
                TablaGenerica tab_temporal=utilitario.consultar(ser_gerencial.getCalTemBalance(utilitario.getVariable("ide_usua"), j+""));
                for(int k=0;k<tab_temporal.getTotalFilas();k++){     
                   // System.out.println("segundo insert k "+k);
                    utilitario.getConexion().ejecutarSql(ser_gerencial.insertTempBalance(tab_temporal.getValor(k, "con_ide_cndpc"),tab_temporal.getValor(k, "debe"),tab_temporal.getValor(k, "haber"),utilitario.getVariable("ide_usua"),nivel_nuevo+"" ));                
                }                         
                
            }
            TablaGenerica tab_temporal_insert=utilitario.consultar(ser_gerencial.getCalTemBalance(utilitario.getVariable("ide_usua"), "2,3,4,5,6"));
            for(int k=0;k<tab_temporal_insert.getTotalFilas();k++){        
            tab_tabla3.insertar();
                tab_tabla3.setValor("ide_cndpc",tab_temporal_insert.getValor(k,"con_ide_cndpc"));
                tab_tabla3.setValor("ide_gebame",ide_gebame);
                tab_tabla3.setValor("valor_debe_gebade",tab_temporal_insert.getValor(k,"debe"));
                tab_tabla3.setValor("valor_haber_gebade",tab_temporal_insert.getValor(k,"haber"));
            }
        }
        tab_tabla3.guardar();
        guardarPantalla();
        tab_tabla3.setCondicion("ide_gebame="+ide_gebame);
        tab_tabla3.ejecutarSql();
        }else{
            utilitario.agregarMensajeError("Periódo no habilitado", "NO se puede realizar la transferencia, el período se encuentra inhabilitado");
        }
        
    }
public void actualizarEstadoBalance(){
    TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"),com_periodo.getValue().toString(),"1"));
    TablaGenerica tab_detalle=utilitario.consultar("select * from ger_balance_mensual  where ide_gecobc="+tab_casa_obra.getValor("ide_gecobc")+" and ide_getiba="+com_tipo_balance.getValue());
    com_estado_mes_fiscal.setValue(tab_detalle.getValor("ide_gerest"));
    ide_gebame=tab_detalle.getValor("ide_gebame");
    tab_tabla3.setCondicion("ide_gebame="+ide_gebame);
    tab_tabla3.ejecutarSql();
    utilitario.addUpdate("com_estado_mes_fiscal");
    
}            
public void actualizarTipoBalance(){
    com_tipo_balance.setCombo(ser_gerencial.getTipoBalance("1", com_mes.getValue().toString()));
    com_estado_mes_fiscal.setValue("-1");
    utilitario.addUpdate("com_tipo_balance,com_estado_mes_fiscal");
}
    public void seleccionaPeriodo() {
    TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"),com_periodo.getValue().toString(),"1"));
    com_estado_mes_fiscal.setValue("");
    ide_gebame="-1";
    com_tipo_balance.setValue("");
    com_mes.setValue("");
    com_estado_anio_fiscal.setValue(tab_casa_obra.getValor("ide_gerest"));
    eti_obra.setValue("No existe obra habilitada para el periódo físcal seleccionado");
    tab_tabla3.setCondicion("ide_gebame="+ide_gebame);
    tab_tabla3.ejecutarSql();
    utilitario.addUpdate("com_estado_mes_fiscal,com_tipo_balance,com_mes,com_estado_anio_fiscal,eti_obra");
    }

    public void dibujarEstadosFinancieros() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(3);

        com_periodo_financiero = new Combo();
        com_periodo_financiero.setCombo(ser_gerencial.getAnio("true,false", "1", ""));
        com_periodo_financiero.eliminarVacio();
        com_periodo_financiero.setMetodo("seleccionaPeriodoReporte");
        gri_fechas.getChildren().add(new Etiqueta("<strong>AÑO FÍSCAL </strong>"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Etiqueta("<strong>TIPO BALANCE </strong>"));
        gri_fechas.getChildren().add(com_periodo_financiero);       
        //GENERANDO LISTA TIPO BALANCE
        lis_tipo_balance.setId("lis_tipo_balance");
        lis_tipo_balance.setListaSeleccion(ser_gerencial.getTipoBalance("-1", "-1"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(lis_tipo_balance); 
        gri_fechas.getChildren().add(new Etiqueta("<strong>NIVEL INICIAL  </strong>"));
       
        //NIVEL INICIAL
        List lista = new ArrayList();
	       Object fila1[] = {
	           "1", "1"
	       };
	       Object fila2[] = {
	           "2", "2"
	       };
               Object fila3[] = {
	           "3", "3"
	       };
               Object fila4[] = {
	           "4", "4"
	       };
               Object fila5[] = {
	           "5", "5"
	       };	       
	       lista.add(fila1);
	       lista.add(fila2);
               lista.add(fila3);
               lista.add(fila4);
               lista.add(fila5);
        rad_nivel_inicial.setId("rad_nivel_inicial");
       rad_nivel_inicial.setRadio(lista);
       gri_fechas.getChildren().add(rad_nivel_inicial);
       gri_fechas.getChildren().add(new Espacio("1", "1"));
       gri_fechas.getChildren().add(new Etiqueta("<strong>NIVEL FINAL </strong>"));
       rad_nivel_final.setId("rad_nivel_final");
       rad_nivel_final.setRadio(lista);   
       gri_fechas.getChildren().add(rad_nivel_final);
        fis_consulta.getChildren().add(gri_fechas);

         Separator separar_mes = new Separator();
        fis_consulta.getChildren().add(separar_mes);
        //GENERANDO MESES
        Grid gri_mes = new Grid();
        gri_mes.setColumns(1); 
        lis_meses.setId("lis_meses");
        lis_meses.setListaSeleccion(ser_gerencial.getMes("-1", "-1"));
        gri_mes.getChildren().add(new Etiqueta("<strong>SELECCIONAR MES </strong>"));
        gri_mes.getChildren().add(lis_meses);
        fis_consulta.getChildren().add(gri_mes);
        // SEPARAR BOTN SLEECCION INVERSA
         Separator separar_sel = new Separator();
        fis_consulta.getChildren().add(separar_sel);
        //BOTON SELECCION INVERSA
         BotonesCombo boc_seleccion_inversa = new BotonesCombo();
            ItemMenu itm_todas = new ItemMenu();
            ItemMenu itm_niguna = new ItemMenu();

            boc_seleccion_inversa.setValue("Selección Inversa");
            boc_seleccion_inversa.setIcon("ui-icon-circle-check");
            boc_seleccion_inversa.setMetodo("seleccinarInversa");
            boc_seleccion_inversa.setUpdate("tab_consulta");
            itm_todas.setValue("Seleccionar Todo");
            itm_todas.setIcon("ui-icon-check");
            itm_todas.setMetodo("seleccionarTodas");
            itm_todas.setUpdate("tab_consulta");
            boc_seleccion_inversa.agregarBoton(itm_todas);
            itm_niguna.setValue("Seleccionar Ninguna");
            itm_niguna.setIcon("ui-icon-minus");
            itm_niguna.setMetodo("seleccionarNinguna");
            itm_niguna.setUpdate("tab_consulta");
            boc_seleccion_inversa.agregarBoton(itm_niguna);
            fis_consulta.getChildren().add(boc_seleccion_inversa);
        // HASTA AQUI BOTON SELECCION INVERSA
        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setId("tab_consulta");
            tab_consulta.setHeader("CASAS SALESIANAS REGISTRADAS EN EL PERIODO FISCAL");
            tab_consulta.setSql(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo_financiero.getValue().toString(), str_impresion));
            tab_consulta.getColumna("ide_gerest").setCombo(ser_gerencial.getEstado());
            tab_consulta.getColumna("ide_gerest").setLongitud(30);
            tab_consulta.getColumna("ide_gecobc").setNombreVisual("IDE");
            tab_consulta.getColumna("ide_gerest").setNombreVisual("ESTADO");
            tab_consulta.getColumna("nombre_gercas").setNombreVisual("CASA SALESIANAS");
            tab_consulta.getColumna("nombre_gerobr").setNombreVisual("OBRA SALESIANA");
            tab_consulta.getColumna("codigo_gerobr").setNombreVisual("CODIGO OBRA");           
            tab_consulta.setTipoSeleccion(true);
            tab_consulta.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "REPORTE DE ESTADOS FINANCIEROS", gru_grupo);
    }
    public void tipoImpresion(){
        if(utilitario.getVariable("p_ger_imprimir_admin").equals("false")){
            str_impresion="1";
        }
    }
   
    public void seleccionaPeriodoReporte(){
        tab_consulta.setSql(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo_financiero.getValue().toString(), str_impresion));
        tab_consulta.ejecutarSql();
    }
   
 public void seleccionarTodas() {
        tab_consulta.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_consulta.getTotalFilas()];
        for (int i = 0; i < tab_consulta.getFilas().size(); i++) {
            seleccionados[i] = tab_consulta.getFilas().get(i);
        }
        tab_consulta.setSeleccionados(seleccionados);
        //calculoTotal();

    }

    public void seleccinarInversa() {
        if (tab_consulta.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_consulta.getSeleccionados().length == tab_consulta.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_consulta.getTotalFilas() - tab_consulta.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_consulta.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_consulta.getSeleccionados().length; j++) {
                    if (tab_consulta.getSeleccionados()[j].equals(tab_consulta.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_consulta.getFilas().get(i);
                    cont++;
                }
            }
            tab_consulta.setSeleccionados(seleccionados);
        }
        //calculoTotal();
    }

    public void seleccionarNinguna() {
        tab_consulta.setSeleccionados(null);

    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public AutoCompletar getAut_cuenta() {
        return aut_cuenta;
    }

    public void setAut_cuenta(AutoCompletar aut_cuenta) {
        this.aut_cuenta = aut_cuenta;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public Dialogo getDia_cerrar_periodo() {
        return dia_cerrar_periodo;
    }

    public void setDia_cerrar_periodo(Dialogo dia_cerrar_periodo) {
        this.dia_cerrar_periodo = dia_cerrar_periodo;
    }

    public AutoCompletar getAut_persona() {
        return aut_persona;
    }

    public void setAut_persona(AutoCompletar aut_persona) {
        this.aut_persona = aut_persona;
    }

    public VisualizarPDF getVipdf_mayor() {
        return vipdf_mayor;
    }

    public void setVipdf_mayor(VisualizarPDF vipdf_mayor) {
        this.vipdf_mayor = vipdf_mayor;
    }

    public Conexion getConPersistencia() {
        return conPersistencia;
    }

    public void setConPersistencia(Conexion conPersistencia) {
        this.conPersistencia = conPersistencia;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public ListaSeleccion getLis_tipo_balance() {
        return lis_tipo_balance;
    }

    public void setLis_tipo_balance(ListaSeleccion lis_tipo_balance) {
        this.lis_tipo_balance = lis_tipo_balance;
    }

    public ListaSeleccion getLis_meses() {
        return lis_meses;
    }

    public void setLis_meses(ListaSeleccion lis_meses) {
        this.lis_meses = lis_meses;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

/**
 *
 * @
 */
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_lista_valores extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private AutoCompletar autAlumno = new AutoCompletar();
    private SeleccionTabla sel_tab_alumno = new SeleccionTabla();
    private Confirmar con_confirma = new Confirmar();
    String alumno = "";
    private Combo com_periodo_academico = new Combo();
    private Combo com_mes = new Combo();
    private Combo com_cursos = new Combo();
    private Combo com_paralelos = new Combo();
    private Combo com_emitir = new Combo();

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public pre_lista_valores() {

        com_periodo_academico.setId("cmb_periodo_academico");
        com_periodo_academico.setCombo(ser_pensiones.getPeriodoAcademico("true"));
        bar_botones.agregarComponente(new Etiqueta("Periodo Academico"));
        bar_botones.agregarComponente(com_periodo_academico);

        com_mes.setId("com_mes");
        com_mes.setCombo("select ide_gemes, nombre_gemes from gen_mes order by ide_gemes");
        bar_botones.agregarComponente(new Etiqueta("Mes"));
        bar_botones.agregarComponente(com_mes);

        com_cursos.setId("com_cursos");
        com_cursos.setCombo(ser_pensiones.getCursos("true,false"));
        bar_botones.agregarComponente(new Etiqueta("Curso"));
        bar_botones.agregarComponente(com_cursos);

        com_paralelos.setId("com_paralelos");
        com_paralelos.setCombo(ser_pensiones.getParalelos("true,false"));
        bar_botones.agregarComponente(new Etiqueta("Paralelo"));
        bar_botones.agregarComponente(com_paralelos);

        com_emitir.setId("com_emitir");
        com_emitir.setCombo(ser_pensiones.estado_emitido());
        bar_botones.agregarComponente(new Etiqueta("Emitido"));
        bar_botones.agregarComponente(com_emitir);

        Boton bot_filtro_consulta = new Boton();
        bot_filtro_consulta.setIcon("ui-icon-search");
        bot_filtro_consulta.setValue("CONSULTAR");
        bot_filtro_consulta.setMetodo("filtroAlumno");
        bar_botones.agregarBoton(bot_filtro_consulta);
        
        // boton seleccion inversa
        BotonesCombo boc_seleccion_inversa = new BotonesCombo();
        ItemMenu itm_todas = new ItemMenu();
        ItemMenu itm_niguna = new ItemMenu();

        boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodo("seleccinarInversa");
        boc_seleccion_inversa.setUpdate("tab_tabla1");
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodo("seleccionarTodas");
        itm_todas.setUpdate("tab_tabla1");
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodo("seleccionarNinguna");
        itm_niguna.setUpdate("tab_tabla1");
        boc_seleccion_inversa.agregarBoton(itm_niguna);
        

        

        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setHeader("EMISIÓN DE FACTURAS PENSIONES POR BLOQUES");
        tab_tabla1.setTabla("rec_valores", "ide_titulo_recval", 1);
        tab_tabla1.setCondicion("ide_titulo_recval = -1");
        tab_tabla1.getColumna("ide_concepto_recon").setVisible(false);
        tab_tabla1.getColumna("ide_cccfa").setCombo("select ide_cccfa,secuencial_cccfa from cxc_cabece_factura");
        tab_tabla1.getColumna("ide_gtemp").setVisible(false);
        tab_tabla1.getColumna("gth_ide_gtemp").setVisible(false);
        tab_tabla1.getColumna("ide_recest").setVisible(false);
        tab_tabla1.getColumna("ide_cndfp").setVisible(false);
        tab_tabla1.getColumna("ide_empr").setVisible(false);
        tab_tabla1.getColumna("ide_sucu").setVisible(false);
        tab_tabla1.getColumna("fecha_pago_recva").setVisible(false);
        tab_tabla1.getColumna("fecha_vence_recva").setVisible(false);
        tab_tabla1.getColumna("valor_imponible_recva").setVisible(false);
        tab_tabla1.getColumna("num_titulo_recva").setVisible(false);
        tab_tabla1.getColumna("base_no_objeto_iva_recva").setVisible(false);
        tab_tabla1.getColumna("base_tarifa0_recva").setVisible(false);
        tab_tabla1.getColumna("base_grabada_recva").setVisible(false);
        tab_tabla1.getColumna("aplica_convenio_recva").setVisible(false);
        tab_tabla1.getColumna("fecha_iniconve_recva").setVisible(false);
        tab_tabla1.getColumna("fecha_finconve_recva").setVisible(false);
        tab_tabla1.getColumna("fecha_descuento_recva").setVisible(false);
        tab_tabla1.getColumna("ide_cocaj").setVisible(false);
        tab_tabla1.getColumna("ide_cccfa").setLectura(true);
        tab_tabla1.getColumna("ide_recalp").setCombo("select ide_recalp, c.nom_geani ||' '|| b.descripcion_repea ||' '|| b.fecha_inicial_repea ||' '|| b.fecha_final_repea as detalle\n" +
                                                     "from rec_alumno_periodo a\n" +
                                                     "inner join rec_periodo_academico b on a.ide_repea = b.ide_repea\n" +
                                                     "inner join gen_anio c on b.ide_geani = c.ide_geani\n" +
                                                     "order by fecha_inicial_repea");
        tab_tabla1.getColumna("ide_recalp").setAutoCompletar();
        tab_tabla1.getColumna("ide_cocaj").setCombo(ser_adquisiciones.getTipoCaja());
        tab_tabla1.getColumna("ide_geper").setNombreVisual("ALUMNO/A");
        tab_tabla1.getColumna("gen_ide_geper").setNombreVisual("REPRESENTANTE");
        tab_tabla1.getColumna("ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("gen_ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("IDE_GTEMP").setNombreVisual("RESPONSABLE");
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setCombo("gth_empleado", "ide_gtemp", "documento_identidad_gtemp,apellido_paterno_gtemp,primer_nombre_gtemp", "");
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setAutoCompletar();
        tab_tabla1.getColumna("GTH_IDE_GTEMP").setNombreVisual("CAJERO");
        tab_tabla1.getColumna("ide_concepto_recon").setCombo("rec_concepto", "ide_concepto_recon", "des_concepto_recon", "");
        tab_tabla1.getColumna("ide_concepto_recon").setRequerida(true);
        tab_tabla1.getColumna("ide_recest").setCombo("rec_estados", "ide_recest", "descripcion_recest", "");
        tab_tabla1.getColumna("TOTAL_RECVA").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:blue");//Estilo
        tab_tabla1.getColumna("IDE_CNDFP").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "");
        tab_tabla1.getColumna("ide_gemes").setCombo("select ide_gemes, nombre_gemes from gen_mes order by ide_gemes");
        tab_tabla1.getColumna("ide_recest").setRequerida(true);
        tab_tabla1.getColumna("valor_iva_recva").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:blue");//Estilo
        tab_tabla1.getColumna("tarifa_iva_recva").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:blue");//Estilo
        tab_tabla1.getColumna("base_tarifa0_recva").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:blue");//Estilo

        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.dibujar(); 
        tab_tabla1.setRows(15);
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.getChildren().add(boc_seleccion_inversa);
        pat_tabla1.setPanelTabla(tab_tabla1);

        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);

        Boton bot_fac_elec = new Boton();
        bot_fac_elec.setIcon("ui-icon-search");
        bot_fac_elec.setValue("GENERAR FACTURA");
        bot_fac_elec.setMetodo("generarFactura");
        //bot_fac_elec.setMetodo(ser_pensiones.generarFacturaElectronica("-1")); 
        agregarComponente(bot_fac_elec);
        bar_botones.agregarBoton(bot_fac_elec);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro que desea enviar a generar factura");
        con_confirma.setTitle("ENVIAR A GENERAR FACTURA");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);
    }

    public void generarFactura() {
        if (tab_tabla1.getFilasSeleccionadas() != "") {
            con_confirma.getBot_aceptar().setMetodo("aprobarEmision");
            utilitario.addUpdate("con_confirma");
            con_confirma.dibujar();

        }  else {
            utilitario.agregarMensajeError("No se puede continuar", "Debe seleccionar al menos un registro para generar los rubros");
        }
    }

    public void aprobarEmision() {
        String maximo = "";
        // String defecto = 0;

        String alumnos_seleccionados = tab_tabla1.getFilasSeleccionadas();
        
        
        TablaGenerica tab_cons_alumperiodo = utilitario.consultar("select * from rec_valores where ide_titulo_recval in (" + alumnos_seleccionados + ")");
        
        for (int j = 0; j < tab_cons_alumperiodo.getTotalFilas(); j++) {
        String codig_val= tab_cons_alumperiodo.getValor(j, "ide_titulo_recval");
        TablaGenerica tab_datos_temp = utilitario.consultar(ser_pensiones.selectPenTemp(codig_val));
        for (int i = 0; i < tab_datos_temp.getTotalFilas(); i++) {
            //ser_pensiones.generarFacturaElectronica(alumno);
            TablaGenerica cod_max = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("pen_tmp_lista_fact", "ide_petlf"));
            maximo = cod_max.getValor("maximo");
            utilitario.getConexion().ejecutarSql("INSERT INTO pen_tmp_lista_fact (ide_petlf, codigo_alumno_petlf, nombre_alumno_petlf, paralelo_petlf, subtotal_petlf, rebaja_petlf, total_petlf, cod_factura_petlf, fecha_petlf, concepto_petlf, representante_petlf, cedula_petlf, periodo_lectivo_petlf, correo_petlf, direccion_petlf, telefono_petlf, cedula_alumno_petlf, ide_sucu, ide_empr)\n"
                    + "VALUES (" + maximo + ", " + tab_datos_temp.getValor(i, "codigo_alumno") + ", '" + tab_datos_temp.getValor(i, "nombres_alumno") + "', '" + tab_datos_temp.getValor(i, "paralelo") + "', " + tab_datos_temp.getValor(i, "total") + ", " + tab_datos_temp.getValor(i, "valor_descuento_revad") + ", " + tab_datos_temp.getValor(i, "total") + ", " + tab_datos_temp.getValor(i, "codigo_fac") + ", '" + tab_datos_temp.getValor(i, "fecha") + "', '" + tab_datos_temp.getValor(i, "concepto") + "', "
                    + "'" + tab_datos_temp.getValor(i, "nom_repre") + "', '" + tab_datos_temp.getValor(i, "cedula_repre") + "', '" + tab_datos_temp.getValor(i, "periodo_academico") + "', '" + tab_datos_temp.getValor(i, "correo_repre") + "', '" + tab_datos_temp.getValor(i, "direccion_repre") + "', '" + tab_datos_temp.getValor(i, "telefono_repre") + "', '" + tab_datos_temp.getValor(i, "cedula_alumno") + "', " + tab_datos_temp.getValor(i, "ide_sucu") + ", " + tab_datos_temp.getValor(i, "ide_empr") + ")");
        }
        //utilitario.getConexion().ejecutarSql(ser_pensiones.generarFacturaElectronica(codig_val));
        try {
            String ide_ccfa = ser_pensiones.generarFacturaElectronica(codig_val);
            utilitario.getConexion().ejecutarSql("update rec_valores set ide_cccfa=" + ide_ccfa + ",generado_fact_recva=true where IDE_TITULO_RECVAL=" + codig_val);
        } catch (Exception e) {
            System.out.println("Error: emitir " +e);
            utilitario.agregarMensajeError("Error al generar la factura", "Verifique datos o consulte con el administrador");
        }
        
        
        }
        utilitario.agregarMensaje("Se ha realizado con éxito las emisiones", "Gracias");
        filtroAlumno();
        con_confirma.cerrar();
    }

    public void filtroAlumno() {
        String cm_per_aca = "";
        String cm_mes = com_mes.getValue() + "";
        String cm_cur = com_cursos.getValue() + "";
        String cm_par = com_paralelos.getValue() + "";
        String cm_emi = com_emitir.getValue() + "";
        System.out.println("entre al filtro del alumno");
        String condicion = "";
        if (com_periodo_academico.getValue() == null) {
            utilitario.agregarMensajeError("Seleccione Registro", "Para consultar listado de alumnos debe seleccionar un periodo academico");

        } else {
            condicion += " ide_recalp in (select ide_recalp from rec_alumno_periodo where ide_repea = " + com_periodo_academico.getValue();
            if (!cm_mes.equals("null")) {
                condicion += " and ide_gemes= " + cm_mes;
            }
            if (!cm_cur.equals("null")) {
                condicion += " and ide_recur= " + cm_cur;
            }
            if (!cm_par.equals("null")) {
                condicion += " and ide_repar= " + cm_par;
            }
            condicion +=" )";
            if (!cm_emi.equals("null")) {
                if (cm_emi.equals("true")) {
                    condicion += " and generado_fact_recva=true ";
                } else if (cm_emi.equals("false")) {
                    condicion += " and generado_fact_recva=false ";
                }
            }
            tab_tabla1.setCondicion(condicion);
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1");
        }
    }

    public void filtroComboPeriodoAcademnico() {

        tab_tabla1.setCondicion(" ide_recalp in (select ide_recalp from rec_alumno_periodo where ide_repea=" + com_periodo_academico.getValue().toString() + ") ");
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");

    }

    public void filtroComboMes() {

        tab_tabla1.setCondicion("ide_gemes in " + com_periodo_academico.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");

    }

    public void seleccionarTodas() {
        tab_tabla1.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_tabla1.getTotalFilas()];
        for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {
            seleccionados[i] = tab_tabla1.getFilas().get(i);
        }
        tab_tabla1.setSeleccionados(seleccionados);
        //calculoTotal();

    }

    public void seleccinarInversa() {
        if (tab_tabla1.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_tabla1.getSeleccionados().length == tab_tabla1.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_tabla1.getTotalFilas() - tab_tabla1.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_tabla1.getSeleccionados().length; j++) {
                    if (tab_tabla1.getSeleccionados()[j].equals(tab_tabla1.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_tabla1.getFilas().get(i);
                    cont++;
                }
            }
            tab_tabla1.setSeleccionados(seleccionados);
        }
        //calculoTotal();
    }

    public void seleccionarNinguna() {
        tab_tabla1.setSeleccionados(null);

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

    public ServicioPensiones getSer_pensiones() {
        return ser_pensiones;
    }

    public void setSer_pensiones(ServicioPensiones ser_pensiones) {
        this.ser_pensiones = ser_pensiones;
    }

    public AutoCompletar getAutAlumno() {
        return autAlumno;
    }

    public void setAutAlumno(AutoCompletar autAlumno) {
        this.autAlumno = autAlumno;
    }

    public SeleccionTabla getSel_tab_alumno() {
        return sel_tab_alumno;
    }

    public void setSel_tab_alumno(SeleccionTabla sel_tab_alumno) {
        this.sel_tab_alumno = sel_tab_alumno;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public Combo getCom_periodo_academico() {
        return com_periodo_academico;
    }

    public void setCom_periodo_academico(Combo com_periodo_academico) {
        this.com_periodo_academico = com_periodo_academico;
    }

    public Combo getCom_cursos() {
        return com_cursos;
    }

    public void setCom_cursos(Combo com_cursos) {
        this.com_cursos = com_cursos;
    }

    public Combo getCom_paralelos() {
        return com_paralelos;
    }

    public void setCom_paralelos(Combo com_paralelos) {
        this.com_paralelos = com_paralelos;
    }

    public Combo getCom_emitir() {
        return com_emitir;
    }

    public void setCom_emitir(Combo com_emitir) {
        this.com_emitir = com_emitir;
    }

}

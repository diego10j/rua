
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
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_transferencias extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();

    private Combo com_periodo_academico = new Combo();
    private Combo com_mes = new Combo();
    private Combo com_cursos = new Combo();
    private Combo com_paralelos = new Combo();
    private Combo com_emitir = new Combo();

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public pre_transferencias() throws UnknownHostException, SocketException {

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

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("TRANSFERENCIA BANCOS");
        tab_tabla1.setSql(ser_pensiones.gettranfertabla("-1", utilitario.getVariable("p_co"), utilitario.getVariable("p_num_cuenta"), utilitario.getVariable("p_moneda"), utilitario.getVariable("p_rec"), utilitario.getVariable("p_36"), utilitario.getVariable("p_c"), utilitario.getVariable("p_ciudad")));
        tab_tabla1.setLectura(true);
        tab_tabla1.setRows(20);
        tab_tabla1.dibujar();  
        
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);

        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);
        

    }

    

    public void filtroAlumno() {

        String cm_mes = com_mes.getValue() + "";
        String cm_cur = com_cursos.getValue() + "";
        String cm_par = com_paralelos.getValue() + "";
        String cm_emi = com_emitir.getValue() + "";
        System.out.println("entre al filtro del alumno");
        String condicion = "";
        if (com_periodo_academico.getValue() == null) {
            utilitario.agregarMensajeError("Seleccione Registro", "Para consultar listado de alumnos debe seleccionar un periodo academico");

        } else {
            condicion += ser_pensiones.gettranferencias(com_periodo_academico.getValue().toString(), utilitario.getVariable("p_co"), utilitario.getVariable("p_num_cuenta"), utilitario.getVariable("p_moneda"), utilitario.getVariable("p_rec"), utilitario.getVariable("p_36"), utilitario.getVariable("p_c"), utilitario.getVariable("p_ciudad"));
            if (!cm_mes.equals("null")) {
                condicion += " and a.ide_gemes= " + cm_mes;
            }
            if (!cm_cur.equals("null")) {
                condicion += " and ide_recur= " + cm_cur;
            }
            if (!cm_par.equals("null")) {
                condicion += " and ide_repar= " + cm_par;
            }
            condicion += "  ) group by a.ide_titulo_recval,b.codigo_geper,nom_geper,detalle_revad,nombre_gemes ";
            if (!cm_emi.equals("null")) {
                if (cm_emi.equals("true")) {
                    condicion += "  and generado_fact_recva=true ";
                } else if (cm_emi.equals("false")) {
                    condicion += "  and generado_fact_recva=false ";
                }
            }
            
            tab_tabla1.setSql(condicion);
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

    public ServicioPensiones getSer_pensiones() {
        return ser_pensiones;
    }

    public void setSer_pensiones(ServicioPensiones ser_pensiones) {
        this.ser_pensiones = ser_pensiones;
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

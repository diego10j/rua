/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_recaudacion_consulta extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private AutoCompletar autAlumno = new AutoCompletar();
    private Dialogo dia_emision = new Dialogo();
    private Calendario fecha = new Calendario();
    private Combo com_forma_pago = new Combo();
    private AreaTexto area_dialogo = new AreaTexto();
    private Etiqueta eti_fecha = new Etiqueta();
    private Etiqueta eti_valor_pagar = new Etiqueta();
    private final Etiqueta eti_cajero = new Etiqueta();
    private final Etiqueta eti_caja = new Etiqueta();
    private final Etiqueta eti_emision = new Etiqueta();
    private Map parametro = new HashMap();
    String alumno = "";
    String seleccion_alumno = "";
    String valor_pagar = "";
    String fecha_actual = "";
    String nombre_alumno = "";
    private SeleccionCalendario sec_rango_fechas = new SeleccionCalendario();
    private Etiqueta eti_rango_fechas = new Etiqueta("DESDE: 0/0/0       HASTA: 0/0/0 ");
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    private VisualizarPDF vipdf_recaudacion = new VisualizarPDF();
    private VisualizarPDF vipdf_cierre = new VisualizarPDF();

    public pre_recaudacion_consulta() {

        if (tienePerfilSecretaria() != 0) {

            bar_botones.quitarBotonInsertar();
            bar_botones.quitarBotonEliminar();
            bar_botones.quitarBotonGuardar();
            autAlumno.setId("autAlumno");
            autAlumno.setAutoCompletar(ser_pensiones.getSqlComboAlumnos());
            autAlumno.setSize(75);
            autAlumno.setAutocompletarContenido(); // no startWith para la busqueda
            autAlumno.setMetodoChange("seleccionarAlumno");
            bar_botones.agregarComponente(new Etiqueta("Buscar Alumno : "));
            bar_botones.agregarComponente(autAlumno);

            Boton bot_clean = new Boton();
            bot_clean.setIcon("ui-icon-cancel");
            bot_clean.setTitle("Limpiar");
            bot_clean.setMetodo("limpiar");
            bar_botones.agregarComponente(bot_clean);

            Boton bot_recaudar = new Boton();
            bot_recaudar.setTitle("Limpiar");
            bot_recaudar.setIcon("ui-icon-search");
            bot_recaudar.setValue("RECAUDARr");
            //bot_recaudar.setMetodo("generarPDFrecaudacion");
            bot_recaudar.setMetodo("abrirDialogo");
            bar_botones.agregarComponente(bot_recaudar);

            //boton cierre recaudaion
            Boton bot_abrir = new Boton();
            bot_abrir.setValue("REPORTE RECAUDACIONES");
            bot_abrir.setIcon("ui-calendario");
            bot_abrir.setMetodo("abrirRango");
            bar_botones.agregarBoton(bot_abrir);
            bar_botones.getBot_insertar().setRendered(false);
            bar_botones.getBot_eliminar().setRendered(false);
            bar_botones.getBot_guardar().setRendered(false);

            sec_rango_fechas.setId("sec_rango_fecha");
            sec_rango_fechas.getBot_aceptar().setMetodo("aceptarRango");
            sec_rango_fechas.setFechaActual();
            agregarComponente(sec_rango_fechas);

            //
            tab_tabla1.setId("tab_tabla1");   //identificador
            tab_tabla1.setSql(ser_pensiones.getAlumnosDeudaConsulta(utilitario.getVariable("p_pen_deuda_activa") + " and ide_titulo_recval = -1"));
            tab_tabla1.setRows(500);
            tab_tabla1.setLectura(true);
            tab_tabla1.setTipoSeleccion(true);
            tab_tabla1.dibujar();
            PanelTabla pat_tabla1 = new PanelTabla();
            pat_tabla1.setId("pat_tabla1");
            pat_tabla1.setPanelTabla(tab_tabla1);
            Division div_tabla1 = new Division();
            div_tabla1.setId("div_tabla1");
            div_tabla1.dividir1(pat_tabla1);
            agregarComponente(div_tabla1);

            eti_cajero.setStyle("font-size:16px;font-weight: bold");
            eti_cajero.setValue("Cajero:" + empleado);

            eti_caja.setStyle("font-size:16px;font-weight: bold");
            eti_caja.setValue("Caja:" + caja);

            eti_emision.setStyle("font-size:16px;font-weight: bold");
            eti_emision.setValue("Emision:" + emision);

            Grid grup_titulo = new Grid();
            grup_titulo.setColumns(1);
            grup_titulo.setWidth("100%");
            grup_titulo.setId("grup_titulo");
            grup_titulo.getChildren().add(eti_cajero);
            grup_titulo.getChildren().add(eti_caja);
            grup_titulo.getChildren().add(eti_emision);

            Division div_cabecera = new Division();
            div_cabecera.setId("div_cabecera");
            div_cabecera.setFooter(grup_titulo, div_tabla1, "15%");
            agregarComponente(div_cabecera);

            dia_emision.setId("dia_emision");
            dia_emision.setTitle("Seleccion los parámetros");
            dia_emision.setWidth("30%");
            dia_emision.setHeight("28%");
            dia_emision.getBot_aceptar().setMetodo("recaudarRubro");
            dia_emision.setResizable(false);

            com_forma_pago.setId("com_forma_pago");
            com_forma_pago.setCombo("select ide_cndfp, nombre_cndfp from con_deta_forma_pago order by nombre_cndfp");

            Grid gru_cuerpo = new Grid();
            gru_cuerpo.setColumns(2);
            Etiqueta eti_mensaje = new Etiqueta();
            eti_mensaje.setValue("FORMA DE PAGO:                                             ");
            eti_mensaje.setStyle("font-size: 11px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
            gru_cuerpo.getChildren().add(eti_mensaje);
            gru_cuerpo.getChildren().add(com_forma_pago);

            area_dialogo = new AreaTexto();
            area_dialogo.setCols(45);
            area_dialogo.setMaxlength(60);
            area_dialogo.setRows(1);
            //gru_cuerpo.getChildren().add(new Etiqueta("RECIBO N°: "));
            //gru_cuerpo.getChildren().add(area_dialogo);

            gru_cuerpo.getChildren().add(new Etiqueta("FECHA: "));
            //fecha.setId("fecha");
            //fecha.setFechaActual();
            //fecha.setTipoBoton(true); 
            //java.util.Date fecha2 = new Date();
            //eti_fecha.setValue(fecha2);
            //eti_fecha.setStyle("font-size: 11px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
            gru_cuerpo.getChildren().add(eti_fecha);
            //gru_cuerpo.getChildren().add(fecha);

            dia_emision.setDialogo(gru_cuerpo);
            agregarComponente(dia_emision);

            //add component visualizarPDF
            vipdf_recaudacion.setId("vipdf_recaudacion");
            vipdf_recaudacion.setTitle("RECIBO RECAUDACIONES");
            agregarComponente(vipdf_recaudacion);
            //
            vipdf_cierre.setId("vipdf_cierre");
            vipdf_cierre.setTitle("REPORTE CIERRE DE RECAUDACIONES");
            agregarComponente(vipdf_cierre);
        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para la facturacion. Consulte con el Administrador");
        }
    }
    String empleado = "";
    String cedula = "";
    String ide_ademple = "";
    String caja = "";
    String emision = "";

    private int tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioCaja(utilitario.getVariable("IDE_USUA")));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);
            empleado = fila[2].toString();
            cedula = fila[1].toString();
            ide_ademple = fila[0].toString();
            caja = fila[3].toString();
            emision = fila[4].toString();
            return 1;

        } else {
            return 0;

        }
    }

    public void seleccionarAlumno(SelectEvent evt) {
        autAlumno.onSelect(evt);
        alumno = autAlumno.getValor();
        if (autAlumno.getValor() != null) {
            TablaGenerica tab_nom_alumno = utilitario.consultar("select ide_geper, nom_geper  from gen_persona where ide_geper = " + alumno + "");
            nombre_alumno = tab_nom_alumno.getValor("nom_geper");
            tab_tabla1.setSql(ser_pensiones.getAlumnosDeudaConsulta(utilitario.getVariable("p_pen_deuda_activa")) + " and a.ide_geper = " + alumno + "");
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1");
            if (tab_tabla1.isEmpty()) {
                utilitario.agregarMensajeInfo("El estudiante " + nombre_alumno + " no presenta deudas a cobrar", "Ingrese otro alumno.");
            }
        } else {
            tab_tabla1.limpiar();
        }
    }

    public void limpiar() {
        autAlumno.limpiar();
        tab_tabla1.limpiar();
    }

    public void abrirRango() {

        sec_rango_fechas.dibujar();

    }

    public void aceptarRango() {

        if (sec_rango_fechas.isFechasValidas()) {

            generarPDFcierreRecaudaciones();
            sec_rango_fechas.cerrar();

        } else {

            utilitario.agregarMensajeError("Las fechas selecionadas no son válidas", "");
        }
    }

    public void generarPDFcierreRecaudaciones() {
//sec_rango_fechas.getFecha1String(), sec_rango_fechas.getFecha2String()
        ///////////AQUI ABRE EL REPORTE
        Map map_parametro = new HashMap();
        map_parametro.put("nombre", utilitario.getVariable("NICK"));
        map_parametro.put("fecha_inicio", "10-08-2018");
        map_parametro.put("fecha_final","10-09-2018");

        System.out.println(" " + map_parametro);
        //vipdf_comprobante.setVisualizarPDF(data, docente, map_parametros);
        vipdf_cierre.setVisualizarPDF("rep_escuela_colegio/rec_cierre_recaudaciones.jasper", map_parametro);
        //vipdf_cierre.setVisualizarPDF("rep_escuela_colegio/prueba.jasper");
        vipdf_cierre.dibujar();
        utilitario.addUpdate("vipdf_cierre");
    }

    public void abrirDialogo() {
        if (tab_tabla1.getTotalFilas() > 0) {
            if (tab_tabla1.getFilasSeleccionadas().isEmpty()) {
                utilitario.agregarMensajeError("Debe seleccionar al menos un valor a recaudar para continuar", "");
            } else {
                fecha_actual = utilitario.getFechaActual();
                seleccion_alumno = tab_tabla1.getFilasSeleccionadas();
                TablaGenerica tab_suma_rubro = utilitario.consultar("select 1 as codigo, sum(total_recva) as valor_pago from rec_valores where ide_titulo_recval in (" + seleccion_alumno + ")");
                eti_fecha.setValue(fecha_actual);
                eti_fecha.setStyle("font-size: 14px;border: none;text-shadow: 0px 2px 3px #ccc;background: none; color:black;");
                valor_pagar = tab_suma_rubro.getValor("valor_pago");
                eti_valor_pagar.setValue(valor_pagar);
                eti_valor_pagar.setStyle("font-size: 16px;border: none;text-shadow: 0px 2px 3px #ccc;background: none; color:red;");
                dia_emision.setTitle("TOTAL A COBRAR: $" + eti_valor_pagar.getValue());
                dia_emision.dibujar();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un estudiante para realizar la recaudación", "");
        }
    }

    public void recaudarRubro() {
        if (validar()) {
            //System.out.println("combo: "+com_forma_pago.getValue());
            //System.out.println("area: "+area_dialogo.getValue());
            //System.out.println("fecha: "+eti_fecha.getValue());
            try {
                String alumnos_seleccionados = tab_tabla1.getFilasSeleccionadas();
                TablaGenerica tab_seleccionados = utilitario.consultar("select ide_titulo_recval,ide_geper from rec_valores \n"
                        + "where ide_titulo_recval in (" + alumnos_seleccionados + ")");

                for (int i = 0; i < tab_seleccionados.getTotalFilas(); i++) {
                    TablaGenerica codigo_maximo = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("rec_valores", "cast(num_titulo_recva as integer)"));

                    utilitario.getConexion().ejecutarSql("update rec_valores\n"
                            + "set ide_recest = " + utilitario.getVariable("p_pen_deuda_recaudada") + " , ide_cndfp = " + com_forma_pago.getValue() + ", num_titulo_recva = " + area_dialogo.getValue() + ", fecha_pago_recva = '" + eti_fecha.getValue() + "'\n"
                            + "where ide_titulo_recval in (" + tab_seleccionados.getValor(i, "ide_titulo_recval") + ")");
                    dia_emision.cerrar();
                    tab_tabla1.actualizar();
                    utilitario.addUpdate("tab_tabla1");
                    generarPDFrecaudacion(tab_seleccionados.getValor(i, "ide_titulo_recval"));
                }

                //utilitario.agregarMensaje("Se ha recaudado correctamente la(s) pension(es) del alumno " + nombre_alumno + "", "");
                area_dialogo.limpiar();
            } catch (Exception e) {

            }
        }
    }

    public boolean validar() {
        if (com_forma_pago.getValue() == null || com_forma_pago.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede continuar", "Debe ingresar la Forma de Pago");
            return false;
        }
        /* if (area_dialogo.getValue() == null || area_dialogo.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede continuar", "Debe ingresar el valor del recibo");
            return false;
        }*/
        return true;
    }

    public void generarPDFrecaudacion(String seleccionado) {
        System.out.println("PARAMETROS TITULO: " + seleccionado);
        Map parametro = new HashMap();
        parametro.put("pide_titulo", Integer.parseInt(seleccionado));
        vipdf_recaudacion.setVisualizarPDF("rep_escuela_colegio/rec_recaudacion.jasper", parametro);
        vipdf_recaudacion.dibujar();
        utilitario.addUpdate("vipdf_recaudacion");

    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public VisualizarPDF getVipdf_recaudacion() {
        return vipdf_recaudacion;
    }

    public void setVipdf_recaudacion(VisualizarPDF vipdf_recaudacion) {
        this.vipdf_recaudacion = vipdf_recaudacion;
    }

    public Map getParametro() {
        return parametro;
    }

    public void setParametro(Map parametro) {
        this.parametro = parametro;
    }

    public SeleccionCalendario getSec_rango_fechas() {
        return sec_rango_fechas;
    }

    public void setSec_rango_fechas(SeleccionCalendario sec_rango_fechas) {
        this.sec_rango_fechas = sec_rango_fechas;
    }

    public Etiqueta getEti_rango_fechas() {
        return eti_rango_fechas;
    }

    public void setEti_rango_fechas(Etiqueta eti_rango_fechas) {
        this.eti_rango_fechas = eti_rango_fechas;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public ServicioPensiones getSer_pensiones() {
        return ser_pensiones;
    }

    public void setSer_pensiones(ServicioPensiones ser_pensiones) {
        this.ser_pensiones = ser_pensiones;
    }

    public VisualizarPDF getVipdf_cierre() {
        return vipdf_cierre;
    }

    public void setVipdf_cierre(VisualizarPDF vipdf_cierre) {
        this.vipdf_cierre = vipdf_cierre;
    }

    public AutoCompletar getAutAlumno() {
        return autAlumno;
    }

    public void setAutAlumno(AutoCompletar autAlumno) {
        this.autAlumno = autAlumno;
    }

    public Dialogo getDia_emision() {
        return dia_emision;
    }

    public void setDia_emision(Dialogo dia_emision) {
        this.dia_emision = dia_emision;
    }

    public Calendario getFecha() {
        return fecha;
    }

    public void setFecha(Calendario fecha) {
        this.fecha = fecha;
    }

    public Combo getCom_forma_pago() {
        return com_forma_pago;
    }

    public void setCom_forma_pago(Combo com_forma_pago) {
        this.com_forma_pago = com_forma_pago;
    }

    public AreaTexto getArea_dialogo() {
        return area_dialogo;
    }

    public void setArea_dialogo(AreaTexto area_dialogo) {
        this.area_dialogo = area_dialogo;
    }

    public Etiqueta getEti_fecha() {
        return eti_fecha;
    }

    public void setEti_fecha(Etiqueta eti_fecha) {
        this.eti_fecha = eti_fecha;
    }

    public Etiqueta getEti_valor_pagar() {
        return eti_valor_pagar;
    }

    public void setEti_valor_pagar(Etiqueta eti_valor_pagar) {
        this.eti_valor_pagar = eti_valor_pagar;
    }

}

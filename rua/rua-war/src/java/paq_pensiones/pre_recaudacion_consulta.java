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
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_recaudacion_consulta extends Pantalla {

    private VisualizarPDF vipdf_recaudacion = new VisualizarPDF();
    private VisualizarPDF vipdf_cierre = new VisualizarPDF();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_alumno_periodo = new Tabla();
    private Tabla tab_persona = new Tabla();
    private Tabla tab_rec_valores = new Tabla();
    private AutoCompletar autAlumno = new AutoCompletar();
    private Dialogo dia_emision = new Dialogo();
    private Dialogo dia_recaudacion = new Dialogo();
    private Dialogo dia_matricula = new Dialogo();
    private Calendario fecha = new Calendario();
    private Calendario fechaConsulta = new Calendario();
    private Combo com_forma_pago = new Combo();
    private AreaTexto area_dialogo = new AreaTexto();
    private Etiqueta eti_fecha = new Etiqueta();
    private Etiqueta eti_valor_pagar = new Etiqueta();
    private final Etiqueta eti_cajero = new Etiqueta();
    private final Etiqueta eti_caja = new Etiqueta();
    private final Etiqueta eti_emision = new Etiqueta();
    private final Combo com_periodo_academico = new Combo();
    private final Combo com_curso = new Combo();
    private final Combo com_paralelo = new Combo();
    private final Combo com_especialidad = new Combo();
    private final Combo com_rubro = new Combo();
    private final Combo com_tipo_documento = new Combo();
    private final Combo com_tipo_pago = new Combo();
    private final Combo com_mes = new Combo();
    private Texto txt_cedula_alum = new Texto();
    private Texto txt_nom_alum = new Texto();
    private Texto txt_nom_repre = new Texto();
    private Texto txt_cedula = new Texto();
    private Texto txt_correo = new Texto();
    private Texto txt_telefono = new Texto();
    private Texto txt_direccion = new Texto();
    private Texto txt_filtro = new Texto();
    private Reporte rep_reporte = new Reporte();
    private Map parametro = new HashMap();
    private SeleccionTabla sel_concepto = new SeleccionTabla();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

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
    @EJB
    private final ServicioProduccion ser_produccion = (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class);
    @EJB
    private final ServicioProduccion ser_valtiempo = (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class);

    public pre_recaudacion_consulta() {

        if (tienePerfilSecretaria() != 0) {

            bar_botones.quitarBotonInsertar();
            bar_botones.quitarBotonEliminar();
            bar_botones.quitarBotonGuardar();
            //bar_botones.agregarReporte();
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

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setIcon("ui-icon-search");
            bot_consultar.setMetodo("ConsultarRepresentate");
            //bar_botones.agregarBoton(bot_consultar);

            Boton bot_recaudar = new Boton();
            bot_recaudar.setTitle("Limpiar");
            bot_recaudar.setIcon("ui-icon-search");
            bot_recaudar.setValue("RECAUDAR");
            //bot_recaudar.setMetodo("generarPDFrecaudacion");
            bot_recaudar.setMetodo("abrirDialogo");
            bar_botones.agregarComponente(bot_recaudar);

            //boton cierre recaudaion
            Boton bot_abrir = new Boton();
            bot_abrir.setValue("REPORTE RECAUDACIONES");
            bot_abrir.setIcon("ui-print");
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

            //Dialogo
            dia_matricula.setId("dia_matricula");
            dia_matricula.setTitle("ACTUALIZA DATOS");
            dia_matricula.setWidth("40%");
            dia_matricula.setHeight("80%");
            dia_matricula.setResizable(false);

            Grid gri_cuerpo = new Grid();

            Grid gri_cabecera = new Grid();
            gri_cabecera.setColumns(2);
            gri_cabecera.setWidth("100%");
            gri_cabecera.setStyle("width:100%;overflow: auto;display: block;");
            gri_cabecera.getChildren().clear();
            gri_cabecera.setHeader(new Etiqueta("DATOS DEL ALUMNO"));
            gri_cabecera.getChildren().add(new Etiqueta("CEDULA: "));
            txt_cedula_alum.setId("txt_cedula_alum");
            gri_cabecera.getChildren().add(txt_cedula_alum);
            gri_cabecera.getChildren().add(new Etiqueta("NOMBRE: "));
            txt_nom_alum.setId("txt_nom_alum");
            txt_nom_alum.setSize(50);
            gri_cabecera.getChildren().add(txt_nom_alum);
            gri_cabecera.getChildren().add(new Etiqueta("AÑO LECTIVO: "));
            com_periodo_academico.setCombo(ser_pensiones.getPeriodoAcademico("true,false"));
            com_periodo_academico.setDisabled(true);
            gri_cabecera.getChildren().add(com_periodo_academico);
            gri_cabecera.getChildren().add(new Etiqueta("CURSO: "));
            com_curso.setCombo(ser_pensiones.getCursos("true,false"));
            com_curso.setDisabled(true);
            gri_cabecera.getChildren().add(com_curso);
            gri_cabecera.getChildren().add(new Etiqueta("ESPECIALIDAD: "));
            com_especialidad.setCombo(ser_pensiones.getEspecialidad("true,false"));
            gri_cabecera.getChildren().add(com_especialidad);
            gri_cabecera.getChildren().add(new Etiqueta("PARALELO: "));
            com_paralelo.setCombo(ser_pensiones.getParalelos("true,false"));
            gri_cabecera.getChildren().add(com_paralelo);

            Grid gri_representante = new Grid();
            gri_representante.setId("gri_representante");
            gri_representante.setColumns(3);
            gri_representante.setHeader(new Etiqueta("DATOS DEL REPRESENTANTE"));
            gri_representante.getChildren().add(new Etiqueta("TIPO DOCUMENTO: "));
            com_tipo_documento.setCombo("select ide_getid,nombre_getid from gen_tipo_identifi");
            gri_representante.getChildren().add(com_tipo_documento);
            Espacio esp = new Espacio();
            gri_representante.getChildren().add(esp);
            gri_representante.getChildren().add(new Etiqueta("CEDULA: "));
            txt_cedula.setId("txt_cedula");
            gri_representante.getChildren().add(txt_cedula);
            gri_representante.getChildren().add(bot_consultar);
            Grid gri_dato = new Grid();
            gri_dato.setId("gri_dato");
            gri_dato.setColumns(2);
            gri_dato.getChildren().add(new Etiqueta("NOMBRE: "));
            txt_nom_repre.setId("txt_nom_repre");
            txt_nom_repre.setSize(50);
            txt_nom_repre.setDisabled(true);
            gri_dato.getChildren().add(txt_nom_repre);
            gri_dato.getChildren().add(new Etiqueta("CORREO ELECTRONICO: "));
            txt_correo.setId("txt_correo");
            txt_correo.setSize(50);
            txt_correo.setDisabled(true);
            gri_dato.getChildren().add(txt_correo);
            gri_dato.getChildren().add(new Etiqueta("DIRECCION: "));
            txt_direccion.setId("txt_direccion");
            txt_direccion.setSize(50);
            txt_direccion.setDisabled(true);
            gri_dato.getChildren().add(txt_direccion);
            gri_dato.getChildren().add(new Etiqueta("TELEFONO: "));
            txt_telefono.setId("txt_telefono");
            txt_telefono.setDisabled(true);
            gri_dato.getChildren().add(txt_telefono);
            gri_dato.getChildren().add(new Etiqueta("RUBRO: "));
            com_rubro.setDisabled(true);
            com_rubro.setCombo(ser_pensiones.getSqlConceptos());
            gri_dato.getChildren().add(com_rubro);
            gri_dato.getChildren().add(new Etiqueta("FORMA DE PAGO: "));
            com_tipo_pago.setCombo("select ide_cndfp,nombre_cndfp from con_deta_forma_pago");
            gri_dato.getChildren().add(com_tipo_pago);
            gri_dato.getChildren().add(new Etiqueta("MES: "));
            com_mes.setCombo("select ide_gemes, nombre_gemes from gen_mes order by ide_gemes");
            gri_dato.getChildren().add(com_mes);
            gri_cuerpo.getChildren().add(gri_cabecera);
            gri_cuerpo.getChildren().add(gri_representante);
            gri_cuerpo.getChildren().add(gri_dato);
            dia_matricula.getBot_aceptar().setMetodo("aceptarDialogo");
            dia_matricula.setDialogo(gri_cuerpo);
            agregarComponente(dia_matricula);

            tab_alumno_periodo.setId("tab_alumno_periodo");
            tab_alumno_periodo.setTabla("rec_alumno_periodo", "ide_recalp", 2);

            tab_persona.setId("tab_persona");
            tab_persona.setTabla("gen_persona", "ide_geper", 3);

            tab_rec_valores.setId("tab_rec_valores");
            tab_rec_valores.setTabla("rec_valores", "ide_titulo_recval", 4);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("rec_valor_detalle", "ide_valdet_revad", 5);

            sel_concepto.setId("sel_concepto");
            sel_concepto.setSeleccionTabla("select ide_concepto_recon,des_concepto_recon from rec_concepto", "ide_concepto_recon");
            sel_concepto.setWidth("40%");
            sel_concepto.setHeight("40%");
            sel_concepto.getBot_aceptar().setMetodo("AceptarConcepto");
            agregarComponente(sel_concepto);

            dia_recaudacion.setId("dia_recaudacion");
            dia_recaudacion.setTitle("Seleccione la fecha");
            dia_recaudacion.setWidth("25%");
            dia_recaudacion.setHeight("15%");
            dia_recaudacion.getBot_aceptar().setMetodo("dialogoRecaudacion");
            dia_recaudacion.setResizable(true);

            Grid gra_cuerpo = new Grid();
            gra_cuerpo.setColumns(2);
            Etiqueta eti_date = new Etiqueta();
            eti_date.setValue("FECHA: ");
            eti_date.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
            gra_cuerpo.getChildren().add(eti_date);
            fechaConsulta.setId("fechaConsulta");
            fechaConsulta.setTipoBoton(true);
            gra_cuerpo.getChildren().add(fechaConsulta);
            dia_recaudacion.setDialogo(gra_cuerpo);
            agregarComponente(dia_recaudacion);

            sec_rango_reporte.setId("sec_rango_reporte");
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sec_rango_reporte.setMultiple(false);
            agregarComponente(sec_rango_reporte);

            rep_reporte.setId("rep_reporte");
            agregarComponente(rep_reporte);
            bar_botones.agregarReporte();

            sel_rep.setId("sel_rep");
            agregarComponente(sel_rep);

        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para la facturacion. Consulte con el Administrador");
        }
    }
    String empleado = "";
    String cedula = "";
    String ide_ademple = "";
    String caja = "";
    String emision = "";
    String num_caja = "";

    public boolean validarTipoDocumento(Texto cedula) {
        System.out.println("ESTOY EN VALIDAR " + cedula.getValue());
        if (cedula.getValue() != null && cedula.getValue().equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
            System.out.println("ESTOY EN VALIDAR " + cedula.getValue());
            if (utilitario.validarCedula(cedula.getValue().toString())) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de cédula válida");
                return false;
            }
        }
        if (cedula.getValue() != null && cedula.getValue().equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
            if (utilitario.validarRUC(cedula.getValue().toString())) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de ruc válido");
                return false;
            }
        }
        return true;
    }
    int bandera = 0;

    public void estadoTexto(boolean estado) {
        //txt_cedula.setDisabled(estado);
        txt_nom_repre.setDisabled(estado);
        txt_correo.setDisabled(estado);
        txt_direccion.setDisabled(estado);
        txt_telefono.setDisabled(estado);
    }

    public void limpiartxt() {
        txt_nom_repre.limpiar();
        txt_correo.limpiar();
        txt_direccion.limpiar();
        txt_telefono.limpiar();
    }

    public void ConsultarRepresentate() {
        if (com_tipo_documento.getValue() != null) {
            if (txt_cedula.getValue() != null || txt_cedula.getValue().toString().isEmpty()) {
                //validarTipoDocumento(txt_cedula);
                if (validarTipoDocumento(txt_cedula)) {
                    TablaGenerica tab_representante = utilitario.consultar("select ide_geper,identificac_geper,nom_geper,direccion_geper,telefono_geper,correo_geper from gen_persona where identificac_geper='" + txt_cedula.getValue() + "'");
                    tab_representante.imprimirSql();
                    if (tab_representante.getTotalFilas() > 0) {
                        bandera = 0;
                        txt_nom_repre.setValue(tab_representante.getValor("nom_geper"));
                        txt_correo.setValue(tab_representante.getValor("correo_geper"));
                        txt_direccion.setValue(tab_representante.getValor("direccion_geper"));
                        txt_telefono.setValue(tab_representante.getValor("telefono_geper"));
                        utilitario.addUpdate("txt_cedula,txt_correo,txt_direccion,txt_telefono,txt_nom_repre");
                        estadoTexto(true);
                    } else {
                        bandera = 1;
                        utilitario.agregarMensajeInfo("ADVERTENCIA,", "La cédula ingresada con el número cédula " + txt_cedula.getValue() + " no esta registrado en la Base de Datos");
                        txt_nom_repre.setPlaceHolder("txt_nom_repre");
                        utilitario.addUpdate("txt_cedula,txt_correo,txt_direccion,txt_telefono,txt_nom_repre");
                        estadoTexto(false);
                        limpiartxt();
                    }
                }
            } else {
                utilitario.agregarMensajeInfo("ADVERTENCIA", "Ingrese el número de Cedula o Ruc del representante");
            }
        } else {
            utilitario.agregarMensajeInfo("ADVERTENCIA", "Seleccione el tipo de documento");
        }
    }

    public void insertaRecValore(String codigo, String representante) {
        TablaGenerica tab_caja = utilitario.consultar("select ide_ademple,ide_gtemp,ide_usua from adq_empleado where  ide_usua=" + utilitario.getVariable("IDE_USUA") + "");
        String recaudador = tab_caja.getValor("ide_gtemp");
        TablaGenerica tab_secuencial = utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_pen_num_sec_recibo_recaudacion")));
        String secuencia = tab_secuencial.getValor("nuevo_secuencial");
        TablaGenerica tab_concepto = utilitario.consultar(ser_pensiones.getConceptoRecaudacion(com_rubro.getValue().toString()));
        tab_rec_valores.insertar();
        tab_rec_valores.setValor("ide_concepto_recon", com_rubro.getValue().toString());
        tab_rec_valores.setValor("ide_recest", utilitario.getVariable("p_pen_deuda_recaudada"));
        tab_rec_valores.setValor("ide_geper", alumno);
        tab_rec_valores.setValor("gen_ide_geper", representante);
        tab_rec_valores.setValor("ide_gtemp", recaudador);
        tab_rec_valores.setValor("gth_ide_gtemp", recaudador);
        tab_rec_valores.setValor("ide_cocaj", num_caja);
        tab_rec_valores.setValor("ide_cndfp", com_tipo_pago.getValue().toString());
        tab_rec_valores.setValor("ide_gemes", com_mes.getValue().toString());
        tab_rec_valores.setValor("ide_recalp", codigo);
        tab_rec_valores.setValor("ide_empr", utilitario.getVariable("IDE_EMPR"));
        tab_rec_valores.setValor("ide_sucu", utilitario.getVariable("IDE_SUCU"));
        tab_rec_valores.setValor("detalle_recva", tab_concepto.getValor("des_impuesto_reimp"));
        tab_rec_valores.setValor("fecha_emision_recva", utilitario.getFechaActual());
        tab_rec_valores.setValor("fecha_vence_recva", utilitario.getFechaActual());
        tab_rec_valores.setValor("fecha_pago_recva", utilitario.getFechaActual());
        tab_rec_valores.setValor("valor_imponible_recva", "0");
        tab_rec_valores.setValor("num_titulo_recva", secuencia);
        tab_rec_valores.setValor("base_no_objeto_iva_recva", "0");
        tab_rec_valores.setValor("base_tarifa0_recva", "0");
        tab_rec_valores.setValor("base_grabada_recva", "0");
        tab_rec_valores.setValor("valor_iva_recva", "0");
        tab_rec_valores.setValor("total_recva", "0");
        tab_rec_valores.setValor("tarifa_iva_recva", "0");
        tab_rec_valores.setValor("valor_descuento_recva", "0");
        tab_rec_valores.setValor("porcentaje_decuento_recva", "0");
        tab_rec_valores.setValor("aplica_total_descuento_recva", "false");
        tab_rec_valores.guardar();
        for (int i = 0; i < tab_concepto.getTotalFilas(); i++) {
            tab_tabla2.insertar();
            tab_tabla2.setValor("ide_titulo_recval", tab_rec_valores.getValor("ide_titulo_recval"));
            tab_tabla2.setValor("ide_impuesto_reimp", tab_concepto.getValor(i, "ide_impuesto_reimp"));
            tab_tabla2.setValor("detalle_revad", tab_concepto.getValor(i, "des_impuesto_reimp"));
            tab_tabla2.setValor("precio_revad", tab_concepto.getValor(i, "valor_reimp"));
            tab_tabla2.setValor("cantidad_revad", "1");
            calcular();
            tab_tabla2.guardar();
        }
        guardarPantalla();
        TablaGenerica tab_total = utilitario.consultar("select '1' as a,sum(total_revad) as total from rec_valor_detalle  where ide_titulo_recval=" + tab_rec_valores.getValor("ide_titulo_recval") + "");
        utilitario.getConexion().ejecutarSql("update rec_valores set total_recva=" + tab_total.getValor("total") + " where ide_titulo_recval=" + tab_rec_valores.getValor("ide_titulo_recval") + "");
        generarPDFrecaudacion(tab_rec_valores.getValor("ide_titulo_recval"));
    }

    public void calcular() {
        //Variables para almacenar y calcular el total del detalle
        double dou_cantidad = 0;
        double dou_precio = 0;
        double dou_total = 0;

        try {
            //Obtenemos el valor de la cantidad
            dou_cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_revad"));
        } catch (Exception e) {
        }

        try {
            //Obtenemos el valor
            dou_precio = Double.parseDouble(tab_tabla2.getValor("precio_revad"));
        } catch (Exception e) {
        }
        //Calculamos el total
        dou_total = dou_cantidad * dou_precio;
        //Asignamos el total a la tabla detalle, con 2 decimales
        tab_tabla2.setValor("total_revad", utilitario.getFormatoNumero(dou_total, 2));
        tab_rec_valores.setValor("TOTAL_RECVA", "" + tab_tabla2.getSumaColumna("total_revad"));
        tab_rec_valores.modificar(tab_rec_valores.getFilaActual());
        //utilitario.addUpdateTabla(tab_tabla1, "TOTAL_RECVA", "tab_tabla1");
        //utilitario.addUpdateTabla(tab_tabla2, "total_revad", "tab_tabla2");
        //utilitario.addUpdate("tab_tabla1");
        //utilitario.addUpdate("tab_tabla2");
    }

    public void aceptarDialogo() {
        if (com_especialidad.getValue() != null) {
            if (com_paralelo.getValue() != null) {
                if (com_tipo_documento.getValue() != null) {
                    if (txt_cedula.getValue() != null) {
                        if (txt_nom_repre.getValue() != null) {
                            if (txt_correo.getValue() != null) {
                                if (txt_direccion.getValue() != null) {
                                    if (com_tipo_pago.getValue() != null) {
                                        if (com_mes.getValue() != null) {
                                            TablaGenerica tab_representante = utilitario.consultar("select ide_geper,identificac_geper,nom_geper,direccion_geper,telefono_geper,correo_geper from gen_persona where identificac_geper='" + txt_cedula.getValue() + "'");
                                            //ACTUALIZO DATOS DEL ALUMNO
                                            utilitario.getConexion().ejecutarSql("update gen_persona set identificac_geper='" + txt_cedula_alum.getValue() + "' ,nom_geper='" + txt_nom_alum.getValue() + "' where ide_geper=" + alumno + " ");
                                            if (bandera == 0) {
                                                tab_alumno_periodo.insertar();
                                                tab_alumno_periodo.setValor("ide_geper", alumno);
                                                tab_alumno_periodo.setValor("ide_repea", com_periodo_academico.getValue().toString());
                                                tab_alumno_periodo.setValor("ide_repar", com_paralelo.getValue().toString());
                                                tab_alumno_periodo.setValor("ide_recur", com_curso.getValue().toString());
                                                tab_alumno_periodo.setValor("ide_reces", com_especialidad.getValue().toString());
                                                tab_alumno_periodo.setValor("gen_ide_geper", tab_representante.getValor("ide_geper"));
                                                tab_alumno_periodo.setValor("correo_recalp", txt_correo.getValue().toString());
                                                tab_alumno_periodo.guardar();
                                                guardarPantalla();
                                                insertaRecValore(tab_alumno_periodo.getValor("ide_recalp"), tab_representante.getValor("ide_geper"));
                                                //ACTUALIZO TABLA RECERVA CUPO
                                                utilitario.getConexion().ejecutarSql("update rec_reserva_cupo set fecha_matricula_rerec= '" + utilitario.getFechaActual() + "',matriculado_rerec=true where ide_geper=" + alumno);

                                            } else if (bandera == 1) {
                                                tab_persona.insertar();
                                                tab_persona.setValor("ide_getid", com_tipo_documento.getValue().toString());
                                                tab_persona.setValor("ide_vgecl", utilitario.getVariable("p_pen_estado_client"));
                                                tab_persona.setValor("ide_vgtcl", utilitario.getVariable("p_pen_tipo_client_representante"));
                                                tab_persona.setValor("gen_ide_geper", utilitario.getVariable("p_pen_grupo_representante"));
                                                tab_persona.setValor("nom_geper", txt_nom_repre.getValue().toString());
                                                tab_persona.setValor("identificac_geper", txt_cedula.getValue().toString());
                                                tab_persona.setValor("direccion_geper", txt_direccion.getValue().toString());
                                                tab_persona.setValor("telefono_geper", txt_telefono.getValue().toString());
                                                tab_persona.setValor("correo_geper", txt_correo.getValue().toString());
                                                tab_persona.setValor("nivel_geper", "HIJO");
                                                tab_persona.guardar();
                                                guardarPantalla();

                                                tab_alumno_periodo.insertar();
                                                tab_alumno_periodo.setValor("ide_geper", alumno);
                                                tab_alumno_periodo.setValor("ide_repea", com_periodo_academico.getValue().toString());
                                                tab_alumno_periodo.setValor("ide_repar", com_paralelo.getValue().toString());
                                                tab_alumno_periodo.setValor("ide_recur", com_curso.getValue().toString());
                                                tab_alumno_periodo.setValor("ide_reces", com_especialidad.getValue().toString());
                                                tab_alumno_periodo.setValor("gen_ide_geper", tab_persona.getValor("ide_geper"));
                                                tab_alumno_periodo.setValor("correo_recalp", txt_correo.getValue().toString());
                                                tab_alumno_periodo.setValor("activo_recalp", "true");
                                                tab_alumno_periodo.setValor("descuento_recalp", "false");
                                                tab_alumno_periodo.setValor("retirado_recalp", "false");
                                                tab_alumno_periodo.setValor("aplica_convenio_pago_recalp", "false");
                                                tab_alumno_periodo.guardar();
                                                guardarPantalla();
                                                //ACTUALIZO TABLA RECERVA CUPO
                                                insertaRecValore(tab_alumno_periodo.getValor("ide_recalp"), tab_representante.getValor("ide_geper"));
                                                utilitario.getConexion().ejecutarSql("update rec_reserva_cupo set fecha_matricula_rerec= '" + utilitario.getFechaActual() + "',matriculado_rerec=true where ide_geper=" + alumno);

                                            }
                                            dia_matricula.cerrar();
                                        } else {
                                            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Seleccione el mes");
                                        }
                                    } else {
                                        utilitario.agregarMensajeInfo("ADVERTENCIA,", "Seleccione el tipo de pago");
                                    }
                                } else {
                                    utilitario.agregarMensajeInfo("ADVERTENCIA,", "Ingrese la direccion domiciliaria");
                                }
                            } else {
                                utilitario.agregarMensajeInfo("ADVERTENCIA,", "Ingrese el correo eléctronico del representante");
                            }
                        } else {
                            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Ingrese el nombre del representante");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("ADVERTENCIA,", "Ingrese la cedula del representante");
                    }
                } else {
                    utilitario.agregarMensajeInfo("ADVERTENCIA,", "Selecione el tipo de documento");
                }
            } else {
                utilitario.agregarMensajeInfo("ADVERTENCIA,", "Selecione el paralelo");
            }
        } else {
            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Selecione la especialidad");
        }
    }

    private int tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioCaja(utilitario.getVariable("IDE_USUA")));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);
            empleado = fila[2].toString();
            cedula = fila[1].toString();
            ide_ademple = fila[0].toString();
            caja = fila[3].toString();
            emision = fila[4].toString();
            num_caja = fila[5].toString();

            return 1;

        } else {
            return 0;

        }
    }

    public void seleccionarAlumno(SelectEvent evt) {
        autAlumno.onSelect(evt);
        alumno = autAlumno.getValor();
        TablaGenerica tab_matricula = utilitario.consultar("select * from rec_reserva_cupo a left join gen_persona b on a.ide_geper=b.ide_geper  where matriculado_rerec=false and a.ide_geper =" + alumno);
        if (tab_matricula.getTotalFilas() > 0) {
            com_periodo_academico.setValue(tab_matricula.getValor("ide_repea"));
            com_curso.setValue(tab_matricula.getValor("ide_recur"));
            com_rubro.setValue(utilitario.getVariable("p_pen_concepto"));
            txt_cedula_alum.setValue(tab_matricula.getValor("identificac_geper"));
            txt_nom_alum.setValue(tab_matricula.getValor("nom_geper"));
            com_especialidad.limpiar();
            com_paralelo.limpiar();
            com_tipo_documento.limpiar();
            txt_cedula.limpiar();
            txt_cedula.setDisabled(false);
            limpiartxt();
            dia_matricula.dibujar();
        } else if (autAlumno.getValor() != null) {
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

            generarPDFcierreR();
            sec_rango_fechas.cerrar();

        } else {

            utilitario.agregarMensajeError("Las fechas selecionadas no son válidas", "");
        }
    }

    public void generarPDFcierreR() {
//sec_rango_fechas.getFecha1String(), sec_rango_fechas.getFecha2String()
        ///////////AQUI ABRE EL REPORTE
        Map map_parametro = new HashMap();
        map_parametro.put("nombre", utilitario.getVariable("NICK"));
        map_parametro.put("fecha_inicio", sec_rango_fechas.getFecha1String());
        map_parametro.put("fecha_final", sec_rango_fechas.getFecha2String());
        vipdf_cierre.setVisualizarPDF("rep_escuela_colegio/rep_cierre_recaudaciones.jasper", map_parametro);
        vipdf_cierre.dibujar();
        utilitario.addUpdate("vipdf_cierre");
    }

    public void abrirDialogo() {
        TablaGenerica tab_caja = utilitario.consultar("select ide_ademple,ide_gtemp,ide_usua from adq_empleado where  ide_usua=" + utilitario.getVariable("IDE_USUA") + "");
        String recaudador = tab_caja.getValor("ide_gtemp");

        if (recaudador == null) {
            utilitario.agregarNotificacionInfo("Notificación", "No puede recaudar por que no tiene un empleado registrado para recaudar");
        } else if (tab_tabla1.getTotalFilas() > 0) {
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
                TablaGenerica tab_caja = utilitario.consultar("select ide_ademple,ide_gtemp,ide_usua from adq_empleado where  ide_usua=" + utilitario.getVariable("IDE_USUA") + "");
                String recaudador = tab_caja.getValor("ide_gtemp");
                //System.out.println("ide " + recaudador);

                for (int i = 0; i < tab_seleccionados.getTotalFilas(); i++) {
                    //TablaGenerica codigo_maximo = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("rec_valores", "cast(num_titulo_recva as integer)"));
                    //System.out.println("ESTOY EN EL FOR " + i);
                    TablaGenerica tab_secuencial = utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_pen_num_sec_recibo_recaudacion")));
                    String secuencia = tab_secuencial.getValor("nuevo_secuencial");
                    //System.out.println("N° recibo: " + secuencia);
                    String sql = "update rec_valores\n"
                            + "set  gth_ide_gtemp = " + recaudador + ",ide_recest = " + utilitario.getVariable("p_pen_deuda_recaudada") + " ,ide_cocaj=" + num_caja + ", ide_cndfp = " + com_forma_pago.getValue() + ", num_titulo_recva = " + secuencia + ", fecha_pago_recva = '" + eti_fecha.getValue() + "'\n"
                            + "where ide_titulo_recval in (" + tab_seleccionados.getValor(i, "ide_titulo_recval") + ")";
                    //System.out.println("SQL: " + sql);
                    utilitario.getConexion().ejecutarSql(sql);
                    dia_emision.cerrar();

                    tab_tabla1.actualizar();

                    utilitario.getConexion().ejecutarSql(ser_valtiempo.getActualizarSecuencial(utilitario.getVariable("p_pen_num_sec_recibo_recaudacion")));

                    utilitario.addUpdate("tab_tabla1");

                }
                generarPDFrecaudacion(alumnos_seleccionados);
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

    public void generarPDFrecaudacion(String titulo) {
        //System.out.println("PARAMETROS TITULO: " + titulo);
        Map parametro = new HashMap();
        parametro.put("pide_titulo", titulo);
        vipdf_recaudacion.setVisualizarPDF("rep_escuela_colegio/rep_recaudacion.jasper", parametro);
        vipdf_recaudacion.dibujar();
        utilitario.addUpdate("vipdf_recaudacion");

    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }


    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Recaudaciones")) {

            rep_reporte.cerrar();
            sec_rango_reporte.dibujar();
        } else {
            utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro");
        }
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

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public Map getParametro() {
        return parametro;
    }

    public void setParametro(Map parametro) {
        this.parametro = parametro;
    }

    public SeleccionTabla getSel_concepto() {
        return sel_concepto;
    }

    public void setSel_concepto(SeleccionTabla sel_concepto) {
        this.sel_concepto = sel_concepto;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public AutoCompletar getAutAlumno() {
        return autAlumno;
    }

    public void setAutAlumno(AutoCompletar autAlumno) {
        this.autAlumno = autAlumno;
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

    public SeleccionCalendario getSec_rango_fechas() {
        return sec_rango_fechas;
    }

    public void setSec_rango_fechas(SeleccionCalendario sec_rango_fechas) {
        this.sec_rango_fechas = sec_rango_fechas;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public VisualizarPDF getVipdf_cierre() {
        return vipdf_cierre;
    }

    public void setVipdf_cierre(VisualizarPDF vipdf_cierre) {
        this.vipdf_cierre = vipdf_cierre;
    }

    public Dialogo getDia_emision() {
        return dia_emision;
    }

    public void setDia_emision(Dialogo dia_emision) {
        this.dia_emision = dia_emision;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Tabla getTab_alumno_periodo() {
        return tab_alumno_periodo;
    }

    public void setTab_alumno_periodo(Tabla tab_alumno_periodo) {
        this.tab_alumno_periodo = tab_alumno_periodo;
    }

    public Tabla getTab_persona() {
        return tab_persona;
    }

    public void setTab_persona(Tabla tab_persona) {
        this.tab_persona = tab_persona;
    }

    public Tabla getTab_rec_valores() {
        return tab_rec_valores;
    }

    public void setTab_rec_valores(Tabla tab_rec_valores) {
        this.tab_rec_valores = tab_rec_valores;
    }

}

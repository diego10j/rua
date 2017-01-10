/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.componentes.AutoCompletar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Encriptar;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import servicios.sistema.ServicioSeguridad;

/**
 *
 * @author Diego
 */
public class pre_usuarios extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private final Encriptar encriptar = new Encriptar();
    private Dialogo dia_clave = new Dialogo();
    private final Etiqueta eti_clave = new Etiqueta();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Map map_parametros = new HashMap();
    private SeleccionTabla set_tab_recursos = new SeleccionTabla();
    private SeleccionTabla set_perfiles = new SeleccionTabla();
    private final ListaSeleccion lis_estado_usuarios = new ListaSeleccion();
    private Dialogo dia_estado_usuarios = new Dialogo();
    private final ListaSeleccion lis_estado_usuarios_2 = new ListaSeleccion();
    private final SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    @EJB
    private final ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario
            .instanciarEJB(ServicioSeguridad.class);

    @EJB
    private final ServicioNomina ser_nomina = (ServicioNomina) utilitario
            .instanciarEJB(ServicioNomina.class);

    private final int int_longitud_minima_login = ser_seguridad
            .getLongitudMinimaLogin();
    private final Texto tex_nick;

    private AutoCompletar aut_usuarios = new AutoCompletar();

    public pre_usuarios() {

        Boton bot_generar = new Boton();
        Boton bot_activar = new Boton();
        Boton bot_desbloquear = new Boton();

        aut_usuarios.setId("aut_usuarios");
        aut_usuarios.setAutoCompletar(ser_seguridad.getSqlUsuarios());
        aut_usuarios.setMetodoChange("seleccionarUsuario");
        aut_usuarios.setAutocompletarContenido();
        aut_usuarios.setTitle("BUSCAR USUARIO");
        bar_botones.agregarComponente(aut_usuarios);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        bar_botones.agregarSeparador();
        bot_generar.setValue("Generar Nueva Clave");
        bot_generar.setMetodo("abrirGenerarClave");
        bar_botones.agregarBoton(bot_generar);

        bot_activar.setValue("Activar / Desactivar");
        bot_activar.setMetodo("activarUsuario");
        bot_activar.setTitle("Activar / Desactivar al usuario actual");
        bar_botones.agregarBoton(bot_activar);

        bot_desbloquear.setValue("Desbloquear");
        bot_desbloquear.setTitle("Desbloquear al usuario actual");
        bot_desbloquear.setMetodo("desbloquearUsuario");
        bar_botones.agregarBoton(bot_desbloquear);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.setTabla("sis_usuario", "ide_usua", 1);
        tab_tabla1.getColumna("IDE_PERF").setCombo("SIS_PERFIL", "IDE_PERF",
                "NOM_PERF", "ACTIVO_PERF=true");
        tab_tabla1.getColumna("IDE_PERF").setRequerida(true);
        tab_tabla1.getColumna("NICK_USUA").setNombreVisual("NICK NAME");
        tab_tabla1.getColumna("NICK_USUA").setUnico(true);
        tab_tabla1.getColumna("ACTIVO_USUA").setCheck();
        tab_tabla1.getColumna("ACTIVO_USUA").setValorDefecto("true");
        tab_tabla1.getColumna("ACTIVO_USUA").setLectura(true);
        tab_tabla1.getColumna("BLOQUEADO_USUA").setLectura(true);
        tab_tabla1.getColumna("TEMA_USUA").setLectura(true);
        tab_tabla1.getColumna("TEMA_USUA").setValorDefecto("sam");
        tab_tabla1.getColumna("BLOQUEADO_USUA").setCheck();
        tab_tabla1.getColumna("FECHA_REG_USUA").setValorDefecto(
                utilitario.getFechaActual());
        tab_tabla1.getColumna("FECHA_REG_USUA").setLectura(true);
        tab_tabla1.getColumna("NICK_USUA").setMetodoChange("asignarClave");
        tab_tabla1.getColumna("CAMBIA_CLAVE_USUA").setValorDefecto("true");
        tab_tabla1.getColumna("CAMBIA_CLAVE_USUA").setLectura(true);
        tab_tabla1.getColumna("CAMBIA_CLAVE_USUA").setCheck();

        tab_tabla1.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
        tab_tabla1.getColumna("ide_gtemp").setAutoCompletar();
        tab_tabla1.getColumna("ide_gtemp").setMetodoChange("seleccionaEmpelado");

        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1
                .setMensajeWarn("Cuando se crean un usuario nuevo la clave es la misma que el valor del campo NICK NAME");
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("sis_usuario_sucursal", "ide_ussu", 2);
        tab_tabla2.getColumna("sis_ide_sucu").setCombo("sis_sucursal",
                "ide_sucu", "nom_sucu",
                "ide_empr=" + utilitario.getVariable("IDE_EMPR"));
        tab_tabla2.getColumna("sis_ide_sucu").setPermitirNullCombo(false);
        tab_tabla2.setHeader("ACCESO A SUCCURSALES");
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("sis_usuario_clave", "ide_uscl", 3);
        tab_tabla3.setCampoOrden("activo_uscl desc");
        tab_tabla3.getColumna("ide_pecl").setCombo("sis_periodo_clave",
                "ide_pecl", "nom_pecl", "");
        tab_tabla3.getColumna("CLAVE_USCL").setClave();
        tab_tabla3.getColumna("CLAVE_USCL").setLectura(false);
        tab_tabla3.getColumna("FECHA_REGISTRO_USCL").setLectura(true);
        tab_tabla3.getColumna("FECHA_REGISTRO_USCL").setValorDefecto(
                utilitario.getFechaHoraActual());
        tab_tabla3.getColumna("activo_uscl").setValorDefecto("true");
        tab_tabla3.getColumna("activo_uscl").setLectura(true);
        tab_tabla3.getColumna("activo_uscl").setCheck();
        tab_tabla3.setHeader("DETALLE DE CLAVE");
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);
        pat_panel3.getMenuTabla().getItem_eliminar().setRendered(false);

        Division div_horizontal = new Division();
        div_horizontal.dividir2(pat_panel1, pat_panel2, "60%", "V");
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(div_horizontal, pat_panel3, "65%", "H");
        agregarComponente(div_division);

        dia_clave.setId("dia_clave");
        dia_clave.setTitle("Generar Nueva Clave");
        dia_clave.setWidth("40%");
        dia_clave.setHeight("18%");
        dia_clave.getBot_aceptar().setMetodo("aceptarClave");
        dia_clave.setResizable(false);
        Grupo gru_cuerpo = new Grupo();
        Etiqueta eti_mensaje = new Etiqueta();
        eti_mensaje
                .setValue("El sistema generó una nueva clave para el usuario seleccionado, para asignar la clave presionar el botón aceptar");
        eti_mensaje
                .setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
        eti_clave.setStyle("font-size: 25px;");
        Grid gri_clave = new Grid();
        gri_clave.setWidth("100%");
        gri_clave.setStyle("text-align: center;");
        gri_clave.getChildren().add(eti_clave);
        gru_cuerpo.getChildren().add(eti_mensaje);
        gru_cuerpo.getChildren().add(gri_clave);

        dia_clave.setDialogo(gru_cuerpo);
        agregarComponente(dia_clave);

        bar_botones.agregarReporte();
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);

        set_tab_recursos.setId("set_tab_recursos");
        set_tab_recursos.setSeleccionTabla("select IDE_OPCI,NOM_OPCI as RECURSO from SIS_OPCION "
                + "GROUP BY IDE_OPCI,NOM_OPCI,SIS_IDE_OPCI "
                + "HAVING SIS_IDE_OPCI is not NULL "
                + "ORDER BY NOM_OPCI ASC", "ide_opci");
        set_tab_recursos.getBot_aceptar().setMetodo("aceptarReporte");
        set_tab_recursos.setTitle("SELECCION DE RECURSOS");
        agregarComponente(set_tab_recursos);

        set_perfiles.setId("set_perfiles");
        set_perfiles.setSeleccionTabla("select IDE_PERF,upper(NOM_PERF) as Perfil,DESCRIPCION_PERF from SIS_PERFIL", "ide_perf");
        set_perfiles.getBot_aceptar().setMetodo("aceptarReporte");
        set_perfiles.setTitle("SELECCION DE PERFILES");
        agregarComponente(set_perfiles);

        List lista = new ArrayList();
        Object fila1[] = {"1", "ACTIVOS"};
        Object fila2[] = {"0", "INACTIVOS"};
        lista.add(fila1);
        lista.add(fila2);
        lis_estado_usuarios.setListaSeleccion(lista);
        lis_estado_usuarios.setVertical();

        List lista1 = new ArrayList();
        Object fila11[] = {"1", "BLOQUEADOS"};
        Object fila22[] = {"0", "DESBLOQUEADOS"};
        lista1.add(fila11);
        lista1.add(fila22);
        lis_estado_usuarios_2.setListaSeleccion(lista1);
        lis_estado_usuarios_2.setVertical();

        Grid gri_accion_audi = new Grid();
        gri_accion_audi.getChildren().add(lis_estado_usuarios);
        gri_accion_audi.getChildren().add(lis_estado_usuarios_2);
        dia_estado_usuarios.setId("dia_estado_usuarios");
        dia_estado_usuarios.setTitle("SELECCION DE ESTADO DE USUARIOS");
        dia_estado_usuarios.setWidth("30%");
        dia_estado_usuarios.setHeight("40%");
        dia_estado_usuarios.setDialogo(gri_accion_audi);
        dia_estado_usuarios.setDynamic(false);
        gri_accion_audi.setStyle("width:"
                + (dia_estado_usuarios.getAnchoPanel() - 5) + "px;height:"
                + dia_estado_usuarios.getAltoPanel()
                + "px;overflow: auto;display: block;");
        dia_estado_usuarios.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(dia_estado_usuarios);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sec_rango_reporte);

        //Para activar o desactivar el campo nick	
        tex_nick = ((Texto) utilitario.getComponente(tab_tabla1.getColumna("NICK_USUA").getId()));
        cambiarEstadoNick();

    }

    public void seleccionaEmpelado(SelectEvent evt) {
        if (tab_tabla1.getValor("ide_gtemp") != null) {
            tab_tabla1.setValor("nom_usua", tab_tabla1.getValorArreglo("ide_gtemp", 1) + tab_tabla1.getValorArreglo("ide_gtemp", 2) + tab_tabla1.getValorArreglo("ide_gtemp", 3) + tab_tabla1.getValorArreglo("ide_gtemp", 4));
        }
    }

    public void limpiar() {
        aut_usuarios.limpiar();
    }

    public void seleccionarUsuario(SelectEvent evt) {
        aut_usuarios.onSelect(evt);
        if (aut_usuarios.getValor() != null) {
            tab_tabla1.setFilaActual(aut_usuarios.getValor());
            utilitario.addUpdate("tab_tabla1");
        }
    }

    /**
     * Activa o desactiva el cuadro de texto del nick
     */
    private void cambiarEstadoNick() {
        if (tab_tabla1.isFilaInsertada()) {
            //si la fila es insertada activo el cuadro de texto
            tex_nick.setDisabled(false);
        } else {
            tex_nick.setDisabled(true);
        }
    }

    @Override
    public void inicio() {
        // TODO Auto-generated method stub
        super.inicio();
        cambiarEstadoNick();
    }

    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
        cambiarEstadoNick();
    }

    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
        cambiarEstadoNick();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
        cambiarEstadoNick();
    }

    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        super.actualizar();
        cambiarEstadoNick();
    }

    @Override
    public void aceptarBuscar() {
        // TODO Auto-generated method stub
        super.aceptarBuscar();
        cambiarEstadoNick();
    }

    @Override
    public void aceptarReporte() {
        // TODO Auto-generated method stub
        if (rep_reporte.getReporteSelecionado().equals("Usuarios Por Recurso")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                rep_reporte.cerrar();
                set_tab_recursos.dibujar();
                utilitario.addUpdate("rep_reporte,set_perfiles");
            } else if (set_tab_recursos.isVisible()) {
                if (set_tab_recursos.getListaSeleccionados().size() > 0) {
                    map_parametros.put("ide_opci", set_tab_recursos.getSeleccionados());
                    set_tab_recursos.cerrar();
                    map_parametros.put("titulo", "USUARIOS POR RECURSO");
                    sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,set_perfiles");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos un perfil de usuario");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Usuarios Por Perfil")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                rep_reporte.cerrar();
                set_perfiles.dibujar();
                utilitario.addUpdate("rep_reporte,set_perfiles");
            } else if (set_perfiles.isVisible()) {
                if (set_perfiles.getListaSeleccionados().size() > 0) {
                    map_parametros.put("ide_perf", set_perfiles.getSeleccionados());
                    set_perfiles.cerrar();
                    map_parametros.put("titulo", "USUARIOS POR PERFIL");
                    sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,set_perfiles");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos un perfil de usuario");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Usuarios Por Estado")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                rep_reporte.cerrar();
                lis_estado_usuarios.setValue(null);
                lis_estado_usuarios_2.setValue(null);
                dia_estado_usuarios.dibujar();
                utilitario.addUpdate("rep_reporte,dia_estado_usuarios");
            } else if (dia_estado_usuarios.isVisible()) {
                if ((lis_estado_usuarios.getSeleccionados() != null && !lis_estado_usuarios.getSeleccionados().isEmpty())
                        || (lis_estado_usuarios_2.getSeleccionados() != null && !lis_estado_usuarios_2.getSeleccionados().isEmpty())) {
                    if (lis_estado_usuarios.getSeleccionados() != null && !lis_estado_usuarios.getSeleccionados().isEmpty()) {
                        map_parametros.put("activo", lis_estado_usuarios.getSeleccionados());
                        if (lis_estado_usuarios_2.getSeleccionados() != null && !lis_estado_usuarios_2.getSeleccionados().isEmpty()) {
                            map_parametros.put("bloqueado", lis_estado_usuarios_2.getSeleccionados());
                        } else {
                            map_parametros.put("bloqueado", "-1");
                        }
                        dia_estado_usuarios.cerrar();
                        map_parametros.put("titulo", "USUARIOS POR ESTADO");
                        sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep,dia_estado_usuarios");
                    } else {
                        map_parametros.put("bloqueado", lis_estado_usuarios_2.getSeleccionados());
                        dia_estado_usuarios.cerrar();
                        map_parametros.put("titulo", "USUARIOS POR ESTADO");
                        rep_reporte.setPath("/reportes/rep_seguridad/rep_bloqueado.jasper");
                        sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep,dia_estado_usuarios");
                    }
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos un estado de usuario");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Caducidad de Usuarios")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(false);
                sec_rango_reporte.setFecha1(null);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.isFechasValidas()) {
                    map_parametros.put("fecha_cacduc_usua", sec_rango_reporte.getFecha1String());
                    sec_rango_reporte.cerrar();
                    map_parametros.put("titulo", "CADUCIDAD DE USUARIOS");
                    sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sec_rango_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar la fecha de caducidad");
                }
            }
        }

    }

    @Override
    public void abrirListaReportes() {
        // TODO Auto-generated method stub
        rep_reporte.dibujar();
    }

    public void desbloquearUsuario() {
        if (tab_tabla1.getValor("bloqueado_usua").equals("true")) {
            ser_seguridad.desbloquearUsuario(tab_tabla1.getValor("ide_usua"));
            utilitario.agregarMensaje(
                    "Se desbloqueo al usuario "
                    + tab_tabla1.getValor("nom_usua"), "");
            tab_tabla1.setValor("bloqueado_usua", "false");
            utilitario.addUpdate("tab_tabla1");
        } else {
            utilitario.agregarMensajeInfo(
                    "El usuario seleccionado no esta Bloqueado", "");
        }
    }

    /**
     * Activa el usuario seleccionado
     */
    public void activarUsuario() {
        if (tab_tabla1.getValor("activo_usua").equals("true")) {
            ser_seguridad.desactivarUsuario(tab_tabla1.getValor("ide_usua"));
            utilitario.agregarMensaje(
                    "Se desactivo al usuario "
                    + tab_tabla1.getValor("nom_usua"), "");
            tab_tabla1.setValor("activo_usua", "false");
            utilitario.addUpdate("tab_tabla1");
        } else {
            ser_seguridad.activarUsuario(tab_tabla1.getValor("ide_usua"));
            utilitario.agregarMensaje(
                    "Se activo al usuario " + tab_tabla1.getValor("nom_usua"),
                    "");
            tab_tabla1.setValor("activo_usua", "true");
            utilitario.addUpdate("tab_tabla1");
        }
    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            if (tab_tabla1.isFilaInsertada() == false) {
                tab_tabla1.insertar();
                tab_tabla2.insertar();
                tab_tabla3.insertar();
            } else {
                utilitario.agregarMensajeInfo("No se puede Insertar",
                        "Debe guardar el Usuario actual");
            }
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        }
        cambiarEstadoNick();
    }

    public void asignarClave(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.isFilaInsertada()) {
            tab_tabla3.setValor("CLAVE_USCL",
                    encriptar.getEncriptar(tab_tabla1.getValor("NICK_USUA")));
            utilitario.addUpdate("tab_tabla3");
        }
    }

    @Override
    public void guardar() {
        // valida la longitud minima del campo nick si inserto o modifico
        if (tab_tabla1.isFilaInsertada() || tab_tabla1.isFilaModificada()) {
            if (tab_tabla1.getValor("NICK_USUA") == null
                    || (tab_tabla1.getValor("NICK_USUA").length() < int_longitud_minima_login)) {
                utilitario.agregarMensajeError("No se puede guardar",
                        "El campo de login debe cumplir con la regla de longitud mínima "
                        + int_longitud_minima_login);
                return;
            }
        }

        if (tab_tabla1.guardar()) {
            // Si creo un usuario guardo en auditoria acceso
            if (tab_tabla1.isFilaInsertada() == true) {
                utilitario.getConexion().agregarSqlPantalla(
                        ser_seguridad.crearSQLAuditoriaAcceso(
                                tab_tabla1.getValor("IDE_USUA"),
                                ser_seguridad.P_SIS_CREAR_USUARIO,
                                "Crear Uusario"));
            }
            if (tab_tabla2.guardar()) {
                if (tab_tabla1.isFilaInsertada() == false
                        && tab_tabla3.isFilaInsertada()) {
                    ser_seguridad.crearClave(tab_tabla1.getValor("IDE_USUA"),
                            tab_tabla1.getValor("NICK_USUA"));
                }
                tab_tabla3.guardar();
            }
        }
        if (guardarPantalla().isEmpty()) {
            aut_usuarios.actualizar();
        }
        cambiarEstadoNick();
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            if (tab_tabla1.isFilaInsertada()) {
                tab_tabla1.eliminar();
            } else {
                ser_seguridad
                        .desactivarUsuario(tab_tabla1.getValor("IDE_USUA"));
                ser_seguridad.bloquearUsuario(tab_tabla1.getValor("IDE_USUA"),
                        "Se eliminó el usuario");
                tab_tabla1.setValor("bloqueado_usua", "true");
                tab_tabla1.setValor("activo_usua", "true");
                utilitario.agregarMensajeInfo("Usuario deshabilitado", "");
            }
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        }
        cambiarEstadoNick();
    }

    public void abrirGenerarClave() {
        if (!tab_tabla1.isFilaInsertada()) {
            eti_clave.setValue(encriptar.getGenerarClave());

            dia_clave.dibujar();
        } else {
            utilitario
                    .agregarMensaje(
                            "No se puede generar una nueva clave a usuarios nuevos",
                            "");
        }
    }

    public void aceptarClave() {
        // Resetea la Clave
        ser_seguridad.resetearClave(tab_tabla1.getValorSeleccionado(),
                eti_clave.getValue().toString());
        utilitario.agregarMensaje("Cambio clave",
                "La clave a sido cambiada correctamente");
        tab_tabla1.setValor("CAMBIA_CLAVE_USUA", "true");
        utilitario.addUpdate("tab_tabla1");
        dia_clave.cerrar();
    }

    public Dialogo getDia_clave() {
        return dia_clave;
    }

    public void setDia_clave(Dialogo dia_clave) {
        this.dia_clave = dia_clave;
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
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

    public SeleccionTabla getSet_tab_recursos() {
        return set_tab_recursos;
    }

    public void setSet_tab_recursos(SeleccionTabla set_tab_recursos) {
        this.set_tab_recursos = set_tab_recursos;
    }

    public SeleccionTabla getSet_perfiles() {
        return set_perfiles;
    }

    public void setSet_perfiles(SeleccionTabla set_perfiles) {
        this.set_perfiles = set_perfiles;
    }

    public Dialogo getDia_estado_usuarios() {
        return dia_estado_usuarios;
    }

    public void setDia_estado_usuarios(Dialogo dia_estado_usuarios) {
        this.dia_estado_usuarios = dia_estado_usuarios;
    }

    public AutoCompletar getAut_usuarios() {
        return aut_usuarios;
    }

    public void setAut_usuarios(AutoCompletar aut_usuarios) {
        this.aut_usuarios = aut_usuarios;
    }

}

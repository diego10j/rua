/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import framework.aplicacion.TablaGenerica;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.DateSelectEvent;
import paq_gestion.ejb.ServicioEmpleado;
import sistema.aplicacion.Utilitario;
import portal.entidades.AsiMotivo;
import portal.entidades.AsiPermisosVacacionHext;
import portal.entidades.GenAnio;
import portal.entidades.GenMes;
import portal.entidades.GenPeriodo;
import portal.entidades.GenPeriodoPK;
import portal.servicios.ServicioEmpleadoJPA;
import portal.servicios.ServicioVacacionesJPA;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class ControladorVacaciones {

    private String strOpcion = "1";
    private Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioEmpleadoJPA servicioEmpleado;
    @EJB
    private ServicioVacacionesJPA servicioVacaciones;
    private AsiPermisosVacacionHext solicitudVacaciones;
    private List listaMotivos;
    private List listaAnios;
    private List listaMeses;
    private List<AsiPermisosVacacionHext> listaSolicitudes;
    @EJB
    private ServicioEmpleado ser_empleado;
    private Object objDiasVacacionesActiva;
    private List lisResumenVacaciones;

    @PostConstruct
    public void cargarDatos() {
        solicitudVacaciones = new AsiPermisosVacacionHext();
        solicitudVacaciones.setIdeAsmot(new AsiMotivo().getIdeAsmot());
        //  solicitudVacaciones.setGenPeriodo(new GenPeriodo());
        //  solicitudVacaciones.getGenPeriodo().setGenPeriodoPK(new GenPeriodoPK());
        //  solicitudVacaciones.getGenPeriodo().setGenAnio(new GenAnio());
        //  solicitudVacaciones.getGenPeriodo().setGenMes(new GenMes());
        solicitudVacaciones.setTipoAspvh(new Integer("2")); //Tipo vacaciones
        solicitudVacaciones.setIdeGtemp(servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP")));
        solicitudVacaciones.setIdeGeedp(servicioEmpleado.getEmpleadoDepartamentoPartida(solicitudVacaciones.getIdeGtemp().getIdeGtemp().toString()));
        // solicitudVacaciones.setIdeSucu(solicitudVacaciones.getIdeGeedp().getGenPartidaGrupoCargo().GETIDE);
        solicitudVacaciones.setFechaSolicitudAspvh(utilitario.getDate());
        solicitudVacaciones.setActivoAspvh(new Boolean(true));
        solicitudVacaciones.setAprobadoAspvh(new Boolean(false));
        listaAnios = servicioVacaciones.getAnios();
        listaMeses = servicioVacaciones.getMeses();
        listaMotivos = servicioVacaciones.getMotivosVacaciones();


        listaSolicitudes = servicioVacaciones.getSolicitudesVacaciones(solicitudVacaciones.getIdeGeedp().getIdeGeedp().toString());
        if (listaSolicitudes == null) {
            listaSolicitudes = new ArrayList<AsiPermisosVacacionHext>();
        }

        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
        if (tab_partida != null) {
            objDiasVacacionesActiva = servicioVacaciones.getPeriodosVacaciones(tab_partida.getValor("IDE_GEEDP"));
            lisResumenVacaciones = servicioVacaciones.getResumeVacaciones(tab_partida.getValor("IDE_GEEDP"));
        }

        if (objDiasVacacionesActiva == null) {
            objDiasVacacionesActiva = new Object[6];
        }
        if (lisResumenVacaciones == null) {
            lisResumenVacaciones = new ArrayList();
        }
    }

    public void calcularDias(DateSelectEvent evt) {
        calcularDias();
    }

    public void calcularDias(AjaxBehaviorEvent evt) {
        calcularDias();
    }

    private void calcularDias() {
        if (solicitudVacaciones.getFechaDesdeAspvh() == null || solicitudVacaciones.getFechaHastaAspvh() == null) {
            return;
        }
//        if (solicitudVacaciones.getFechaDesdeAspvh().equals(solicitudVacaciones.getFechaHastaAspvh())) {
//            utilitario.agregarMensajeInfo("Fechas no v�lidas", "La Fecha Hasta debe ser mayor a la Fecha Desde");
//            solicitudVacaciones.setFechaHastaAspvh(null);
//            return;
//        }

        if (utilitario.isFechasValidas(utilitario.getFormatoFecha(solicitudVacaciones.getFechaDesdeAspvh()), utilitario.getFormatoFecha(solicitudVacaciones.getFechaHastaAspvh()))) {
            solicitudVacaciones.setNroDiasAspvh(new Integer((utilitario.getDiferenciasDeFechas(solicitudVacaciones.getFechaDesdeAspvh(), solicitudVacaciones.getFechaHastaAspvh()) + 1) + ""));
        } else {
            solicitudVacaciones.setNroDiasAspvh(null);
            utilitario.agregarMensajeInfo("Fechas no v�lidas", "La Fecha Hasta debe ser mayor o igual a la Fecha Desde");
            solicitudVacaciones.setFechaHastaAspvh(null);
        }
    }

    public void guardarSolicitud(ActionEvent evt) {
        //Valido que tenga dias de vacaciones
        if (((Object[]) objDiasVacacionesActiva)[6] != null) {
            try {
                double dou_dias = Double.parseDouble(((Object[]) objDiasVacacionesActiva)[6] + "");
                double dou_solicita = Double.parseDouble(solicitudVacaciones.getNroDiasAspvh() + "");
                if (dou_solicita > dou_dias) {
                    utilitario.agregarMensaje("Solicitud no v�lida", "Solo dispone maximo de " + dou_dias + " d�as de vacaciones");
                    return;
                }
            } catch (Exception e) {
            }
        } else {
            utilitario.agregarMensaje("Solicitud no v�lida", "No tiene d�as de vacaciones");
            return;
        }

        String str_mensaje = servicioVacaciones.guardarSolicitudVacaciones(solicitudVacaciones);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
            cargarDatos();
        } else {
            utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
        }
    }

    public AsiPermisosVacacionHext getSolicitudVacaciones() {
        return solicitudVacaciones;
    }

    public void setSolicitudVacaciones(AsiPermisosVacacionHext solicitudVacaciones) {
        this.solicitudVacaciones = solicitudVacaciones;
    }

    public List getListaMotivos() {
        return listaMotivos;
    }

    public void setListaMotivos(List listaMotivos) {
        this.listaMotivos = listaMotivos;
    }

    public List getListaAnios() {
        return listaAnios;
    }

    public void setListaAnios(List listaAnios) {
        this.listaAnios = listaAnios;
    }

    public List getListaMeses() {
        return listaMeses;
    }

    public void setListaMeses(List listaMeses) {
        this.listaMeses = listaMeses;
    }

    public List<AsiPermisosVacacionHext> getListaSolicitudes() {
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<AsiPermisosVacacionHext> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public Object getObjDiasVacacionesActiva() {
        return objDiasVacacionesActiva;
    }

    public void setObjDiasVacacionesActiva(Object objDiasVacacionesActiva) {
        this.objDiasVacacionesActiva = objDiasVacacionesActiva;
    }

    public List getLisResumenVacaciones() {
        return lisResumenVacaciones;
    }

    public void setLisResumenVacaciones(List lisResumenVacaciones) {
        this.lisResumenVacaciones = lisResumenVacaciones;
    }
}

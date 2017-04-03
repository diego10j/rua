/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import framework.aplicacion.TablaGenerica;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import paq_gestion.ejb.ServicioEmpleado;
import sistema.aplicacion.Utilitario;
import portal.entidades.AsiDetalleHorasExtras;
import portal.entidades.AsiMotivo;
import portal.entidades.AsiPermisosVacacionHext;
import portal.servicios.ServicioEmpleadoJPA;
import portal.servicios.ServicioHorasExtraJPA;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class ControladorHorasExtra {

    private String strOpcion = "1";
    private Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioHorasExtraJPA servicioHorasExtra;
    @EJB
    private ServicioEmpleado ser_empleado;
    @EJB
    private ServicioEmpleadoJPA servicioEmpleado;
    private List listaMarcacionesHorasExtra; // tabla con las marcaciones
    private Date datFechaInicio;
    private Date datFechaFin;
    private Object[] horasSeleccionadas;
    private AsiPermisosVacacionHext cabSolicitud;
    private List<AsiDetalleHorasExtras> detSolicitud;
    private List listaMotivos; //combo motivos
    private List<AsiPermisosVacacionHext> lisSolicitudes; //muestra todas los solicitudes de horas extra
    private AsiPermisosVacacionHext horaSeleccionada;
    private List<AsiDetalleHorasExtras> lisDetalle;

    @PostConstruct
    public void cargarDatos() {
        //Pongo el fecha inicio y fecha fin al mes acutual
        if (listaMarcacionesHorasExtra == null) {
            datFechaInicio = utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-" + utilitario.getMes(utilitario.getFechaActual()) + "-01");
            datFechaFin = utilitario.getFecha(utilitario.getUltimaFechaMes(utilitario.getFechaActual()));

        }

        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
        if (tab_partida != null) {
            listaMarcacionesHorasExtra = servicioHorasExtra.getMarcacionesHorasExtra(tab_partida.getValor("IDE_GEEDP"), utilitario.getFormatoFecha(datFechaInicio), utilitario.getFormatoFecha(datFechaFin));
        }
        horasSeleccionadas = null;

        listaMotivos = servicioHorasExtra.getListaMotivos();

        cabSolicitud = new AsiPermisosVacacionHext();
        cabSolicitud.setIdeAsmot(new AsiMotivo().getIdeAsmot());
        cabSolicitud.setIdeGtemp(servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP")));
        cabSolicitud.setIdeGeedp(servicioEmpleado.getEmpleadoDepartamentoPartida(cabSolicitud.getIdeGtemp().getIdeGtemp().toString()));
        cabSolicitud.setFechaSolicitudAspvh(utilitario.getDate());
        cabSolicitud.setActivoAspvh(new Boolean(true));
        cabSolicitud.setAprobadoAspvh(new Boolean(false));
        cabSolicitud.setTipoAspvh(new Integer("3")); //Tipo Horas extra
        //Por defecto selecciona el motivo horas extra
        if (utilitario.getVariable("p_motivo_hora_extra") != null) {
            //cabSolicitud.getIdeAsmot().setIdeAsmot(new Integer(utilitario.getVariable("p_motivo_hora_extra")));
        } else {
            System.out.println("FALTA EL PARAMETRO  p_motivo_hora_extra");
        }

        if (cabSolicitud.getIdeGeedp() != null) {
            lisSolicitudes = servicioHorasExtra.getSolicitudesHorasExtra(cabSolicitud.getIdeGeedp().getIdeGeedp().toString());
        }

        detSolicitud = new ArrayList<AsiDetalleHorasExtras>();



        if (lisSolicitudes == null) {
            lisSolicitudes = new ArrayList<AsiPermisosVacacionHext>();
        }


    }

    public void guardarSolicitud() {
        String str_mensaje = servicioHorasExtra.guardarHorasExtra(cabSolicitud, detSolicitud);
        if (str_mensaje.isEmpty()) {
            //Pongo estado importado a las cabeceras de solicitud para que ya no puedan importar otra ves
            String str_ide = "";
            if (horasSeleccionadas != null) {
                for (int i = 0; i < horasSeleccionadas.length; i++) {
                    if (i > 0) {
                        str_ide += ",";
                    }
                    Object[] object = (Object[]) horasSeleccionadas[i];
                    str_ide += object[0] + "";
                }
                System.out.println("cambio estado : " + str_ide);
                if (str_ide.isEmpty() == false) {
                    servicioHorasExtra.cambiarEstadoImportado(str_ide);
                }
            }
            utilitario.agregarMensaje("Se guardo Correctamente", "");
            cargarDatos();
            utilitario.ejecutarJavaScript("diaHE.hide()");
        } else {
            utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
        }
    }

    public void solicitarHorasExtra() {
        //Crea una solicitud de horas extra con las horas extra seleccionadas        
        if (horasSeleccionadas != null) {
            if (horasSeleccionadas.length == 0) {
                utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro de la tabla", "");
            } else {
                cabSolicitud = new AsiPermisosVacacionHext();
                cabSolicitud.setTipoAspvh(new Integer("3")); //Pr defecto 3 para saber q son horas extra
                cabSolicitud.setIdeGtemp(servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP")));
                cabSolicitud.setIdeGeedp(servicioEmpleado.getEmpleadoDepartamentoPartida(utilitario.getVariable("IDE_GTEMP")));
                cabSolicitud.setFechaSolicitudAspvh(new Date());
                cabSolicitud.setIdeSucu(cabSolicitud.getIdeSucu());
                cabSolicitud.setAprobadoAspvh(new Boolean(false));
                cabSolicitud.setActivoAspvh(new Boolean(true));
//0                cabSolicitud.setIdeAsmot(new AsiMotivo());
//                cabSolicitud.getIdeAsmot().setIdeAsmot(new Integer(utilitario.getVariable("p_motivo_hora_extra")));

                detSolicitud = new ArrayList<AsiDetalleHorasExtras>();
                TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
                if (tab_partida != null) {
                    for (int i = 0; i < horasSeleccionadas.length; i++) {
                        Object[] object = (Object[]) horasSeleccionadas[i];
                        //Consulto los horarios de horas extra configurados para la fecha de marcación
                        TablaGenerica tab_horarios = servicioHorasExtra.getHorariosHorasExtraporDia(utilitario.getNumeroDiasSemana((Date) object[1]) + "", tab_partida.getValor("IDE_SUCU"), tab_partida.getValor("IDE_GTGRE"));

                        String str_hora_fin = utilitario.getFormatoFecha(object[3]);  //hora fin de marcación
                        double dou_diferencia = 0; // Diferencia de horas
                        try {
                            dou_diferencia = Double.parseDouble(object[4] + "");
                        } catch (Exception e) {
                        }

                        if (!tab_horarios.isEmpty()) {
                            for (int j = 0; j < tab_horarios.getTotalFilas(); j++) {
                                String str_fin_horario = tab_horarios.getValor(j, "HORA_FIN");

                                if (utilitario.isFechaMayor(utilitario.getHora(str_hora_fin), utilitario.getHora(str_fin_horario))) {
                                    //Solo saca el numero de horas hasta la hora final del horario
                                    double dou_horas_fila = utilitario.getDiferenciaHoras(utilitario.getHora(utilitario.getFormatoHora(tab_horarios.getValor(j, "HORA_INICIO"))), utilitario.getHora(utilitario.getFormatoHora(tab_horarios.getValor(j, "HORA_FIN"))));
                                    AsiDetalleHorasExtras adhe_nueva = new AsiDetalleHorasExtras();
                                    adhe_nueva.setActivoAsdhe(new Boolean(false));
                                    adhe_nueva.setIdeAsgri(servicioHorasExtra.getTipoHora(tab_horarios.getValor(j, "IDE_ASGRI")));
                                    adhe_nueva.setFechaAsdhe(utilitario.getFecha(utilitario.getFormatoFecha(object[1])));
                                    adhe_nueva.setHoraInicialAsdhe(utilitario.getHora(utilitario.getFormatoHora(tab_horarios.getValor(j, "HORA_INICIO"))));
                                    adhe_nueva.setHoraFinalAsdhe(utilitario.getHora(utilitario.getFormatoHora(tab_horarios.getValor(j, "HORA_FIN"))));
                                    adhe_nueva.setNroHorasAsdhe(new BigDecimal(utilitario.getFormatoNumero(dou_horas_fila)));
                                    //AUMENTO CAMPO LA MADRE CON DETALLE HORAS EXTRA
                                    //  if (object[5] != null) {
                                    //    adhe_nueva.setIdeAsdhe(new BigDecimal(utilitario.getFormatoNumero(object[5]))); 0
                                    //  }
                                    dou_diferencia = dou_diferencia - dou_horas_fila;
                                    detSolicitud.add(adhe_nueva);
                                } else {
                                    //Esta en el rango de la marcación seleccionada
                                    AsiDetalleHorasExtras adhe_nueva = new AsiDetalleHorasExtras();
                                    adhe_nueva.setActivoAsdhe(new Boolean(false));
                                    adhe_nueva.setIdeAsgri(servicioHorasExtra.getTipoHora(tab_horarios.getValor(j, "IDE_ASGRI")));
                                    adhe_nueva.setFechaAsdhe(utilitario.getFecha(utilitario.getFormatoFecha(object[1])));

                                    adhe_nueva.setHoraInicialAsdhe(utilitario.getHoraCalendario(tab_horarios.getValor(j, "HORA_INICIO")));
                                    adhe_nueva.setHoraFinalAsdhe(utilitario.getHoraCalendario(str_hora_fin));
                                    adhe_nueva.setNroHorasAsdhe(new BigDecimal(utilitario.getFormatoNumero(dou_diferencia)));
                                    detSolicitud.add(adhe_nueva);
                                    break;
                                }
                            }
                        } else {
                            //Considero todo como horas suplementarias
                        }
                    }
                }
                utilitario.ejecutarJavaScript("diaHE.show()");
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro de la tabla", "");
        }
    }

    public void actualizarMarcaciones() {
        //Actualiza las marcaciones en un nuevo rango de fechas
        if (datFechaInicio != null && datFechaFin != null) {
            if (utilitario.isFechasValidas(utilitario.getFormatoFecha(datFechaInicio), utilitario.getFormatoFecha(datFechaFin))) {
                TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
                if (tab_partida != null) {
                    listaMarcacionesHorasExtra = servicioHorasExtra.getMarcacionesHorasExtra(tab_partida.getValor("IDE_GEEDP"), utilitario.getFormatoFecha(datFechaInicio), utilitario.getFormatoFecha(datFechaFin));
                    horasSeleccionadas = null;
                }
            } else {
                utilitario.agregarMensajeInfo("Rango de Fechas no válidas", "");
            }

        }
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public List getListaMarcacionesHorasExtra() {
        return listaMarcacionesHorasExtra;
    }

    public void setListaMarcacionesHorasExtra(List listaMarcacionesHorasExtra) {
        this.listaMarcacionesHorasExtra = listaMarcacionesHorasExtra;
    }

    public Date getDatFechaInicio() {
        return datFechaInicio;
    }

    public void setDatFechaInicio(Date datFechaInicio) {
        this.datFechaInicio = datFechaInicio;
    }

    public Date getDatFechaFin() {
        return datFechaFin;
    }

    public void setDatFechaFin(Date datFechaFin) {
        this.datFechaFin = datFechaFin;
    }

    public Object[] getHorasSeleccionadas() {
        return horasSeleccionadas;
    }

    public void setHorasSeleccionadas(Object[] horasSeleccionadas) {
        this.horasSeleccionadas = horasSeleccionadas;
    }

    public AsiPermisosVacacionHext getCabSolicitud() {
        return cabSolicitud;
    }

    public void setCabSolicitud(AsiPermisosVacacionHext cabSolicitud) {
        this.cabSolicitud = cabSolicitud;
    }

    public List<AsiDetalleHorasExtras> getDetSolicitud() {
        return detSolicitud;
    }

    public void setDetSolicitud(List<AsiDetalleHorasExtras> detSolicitud) {
        this.detSolicitud = detSolicitud;
    }

    public void seleccionarHoraExtra(SelectEvent evt) {
        horaSeleccionada = (AsiPermisosVacacionHext) evt.getObject();
        if (horaSeleccionada != null) {
            lisDetalle=servicioHorasExtra.getDetallesHorasExtra(horaSeleccionada.getIdeAspvh().toString());
        }
    }

    public List getListaMotivos() {
        return listaMotivos;
    }

    public void setListaMotivos(List listaMotivos) {
        this.listaMotivos = listaMotivos;
    }

    public List<AsiPermisosVacacionHext> getLisSolicitudes() {
        return lisSolicitudes;
    }

    public void setLisSolicitudes(List<AsiPermisosVacacionHext> lisSolicitudes) {
        this.lisSolicitudes = lisSolicitudes;
    }

    public AsiPermisosVacacionHext getHoraSeleccionada() {
        return horaSeleccionada;
    }

    public void setHoraSeleccionada(AsiPermisosVacacionHext horaSeleccionada) {
        this.horaSeleccionada = horaSeleccionada;
    }

    public List<AsiDetalleHorasExtras> getLisDetalle() {
        return lisDetalle;
    }

    public void setLisDetalle(List<AsiDetalleHorasExtras> lisDetalle) {
        this.lisDetalle = lisDetalle;
    }
}

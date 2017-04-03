/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import framework.aplicacion.TablaGenerica;
import framework.reportes.GenerarReporte;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Utilitario;
import portal.entidades.GenEmpleadosDepartamentoPar;
import portal.entidades.GthTelefono;
import portal.entidades.GthTipoDocumentoIdentidad;
import portal.entidades.GthTipoTelefono;
import portal.entidades.NrhAnticipo;
import portal.entidades.NrhGarante;
import portal.entidades.NrhMotivoAnticipo;
import portal.entidades.NrhTipoGarante;
import portal.servicios.ServicioCreditosJPA;
import portal.servicios.ServicioEmpleadoJPA;

@ManagedBean
@ViewScoped
public class ControladorCreditos {

    private String strOpcion = "1";
    private Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioEmpleadoJPA servicioEmpleado;
    @EJB
    private ServicioCreditosJPA servicioCreditos;
    private NrhAnticipo solicitudAnticipo;
    private List listaTipoDocumento;
    private List listaTipoGarante;
    private List listaEmpleadosDepartamento;
    private NrhGarante garanteSolicitud;
    private List<NrhAnticipo> listaAnticipos;
    private List listaMotivos;
    private GthTelefono garanteTelefono;
    private boolean ingresarGarante = true;
    private List listaTiposTelefono;
    private Object objEmpleadoGarante;
    //Saldos de creditos
    private List listaCabCreditos;
    private Object objCreditoSeleccionado;
    private List listaDetaCreditos;
    @EJB
    private ServicioEmpleado ser_empleado;
    private String strPathReporte;
    private String str_saldo = "0.00";
    private NrhAnticipo anticipoSeleccionado;
    
    @EJB
    private ServicioAnticipo servAnticipo;
    @EJB

	private ServicioNomina ser_nomina;

    @PostConstruct
    public void cargarDatos() {


        solicitudAnticipo = new NrhAnticipo();
        solicitudAnticipo.setIdeGtemp(servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP")));
        solicitudAnticipo.setIdeGeedp(servicioEmpleado.getEmpleadoDepartamentoPartida(solicitudAnticipo.getIdeGtemp().getIdeGtemp().toString()));
        solicitudAnticipo.setIdeNrmoa(new NrhMotivoAnticipo());
        solicitudAnticipo.setFechaSolicitudNrant(utilitario.getDate());
        solicitudAnticipo.setActivoNrant(new Boolean(true));
        solicitudAnticipo.setAnticipoNrant(new Boolean(true));

        garanteSolicitud = new NrhGarante();
        garanteSolicitud.setActivoNrgar(new Boolean(true));
        garanteSolicitud.setIdeGttdi(new GthTipoDocumentoIdentidad());
        garanteSolicitud.setIdeNrtig(new NrhTipoGarante());
        garanteSolicitud.setIdeNrant(solicitudAnticipo);

        listaTipoDocumento = servicioEmpleado.getTiposDocumentoIdentidad();
        listaTipoGarante = servicioCreditos.getTiposGarante();
        listaEmpleadosDepartamento = servicioCreditos.getEmpleadosDepartamentos();

        objEmpleadoGarante=null;

        listaAnticipos = servicioCreditos.getSolicitudesAnticipo(solicitudAnticipo.getIdeGeedp().getIdeGeedp().toString());
        if (listaAnticipos == null) {
            listaAnticipos = new ArrayList<NrhAnticipo>();
        }
        solicitudAnticipo.setNroAnticipoNrant(new Integer((listaAnticipos.size() + 1) + ""));
        solicitudAnticipo.setCalificadoNrant(new Boolean ("false")); //false
        solicitudAnticipo.setAprobadoNrant(new Boolean("false"));

        listaMotivos = servicioCreditos.getMotivosAnticipo();


        garanteTelefono = new GthTelefono();
//        garanteTelefono.setIdeNrgar(garanteSolicitud);
        garanteTelefono.setIdeGttit(new GthTipoTelefono());
//        garanteTelefono.setIdeNrgar(garanteSolicitud);

        listaTiposTelefono = servicioEmpleado.getTiposTelefono();

        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
        if (tab_partida != null) {
            listaCabCreditos = servicioCreditos.getCreditosAprobado(tab_partida.getValor("IDE_GEEDP"));
        }
        strPathReporte = utilitario.getURL() + "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf";
    }

    public void visualizarSolicitud() {
        if (anticipoSeleccionado != null) {
            GenerarReporte ger = new GenerarReporte();
            Map parametros = new HashMap();
            parametros.put("IDE_NRANT", Integer.parseInt(anticipoSeleccionado.getIdeNrant() + ""));
            parametros.put("ACTIVO_NRANT", "1");
            parametros.put("titulo", " SOLICITUD DE ANTICIPOS");
            ger.generar(parametros, "/reportes/rep_anticipos/rep_datos_anticipo.jasper");
        }
    }

    public void seleccionarGarante(SelectEvent evt) {
        if (objEmpleadoGarante != null) {
            GenEmpleadosDepartamentoPar emple = servicioCreditos.getEmpleadoDepartamentoPartida(((Object[]) objEmpleadoGarante)[0] + "");
            if (emple != null) {
                garanteSolicitud.setDocumentoIdentidadcNrgar(emple.getIdeGtemp().getDocumentoIdentidadGtemp());
                garanteSolicitud.setIdeGttdi(emple.getIdeGtemp().getIdeGttdi());
                garanteSolicitud.setPrimerNombregNrgar(emple.getIdeGtemp().getPrimerNombreGtemp());
                garanteSolicitud.setSegundoNombregNrgar(emple.getIdeGtemp().getSegundoNombreGtemp());
                garanteSolicitud.setApellidoPaternogNrgar(emple.getIdeGtemp().getApellidoPaternoGtemp());
                garanteSolicitud.setApellidoMaternogNrgar(emple.getIdeGtemp().getApellidoMaternoGtemp());
                garanteSolicitud.setLugarTrabajoNrgar(utilitario.getCampoEmpresa("NOM_EMPR"));
                garanteSolicitud.setRmuNrgar(emple.getRmuGeedp());
                //garanteSolicitud.setIdeNrtig(new NrhTipoGarante(Integer.parseInt(utilitario.getVariable("p_nrh_tipo_garante"))));
                //garanteSolicitud.setIdeNrtig(garanteSolicitud.getIdeNrtig().getIdeNrtig());
            }
        }
    }
    
  

    public void guardarSolicitud(ActionEvent evt) {
        //Valido identificaci�n del garante
        if (validarDocumentoIdentidad(garanteSolicitud.getIdeGttdi().getIdeGttdi().toString(), garanteSolicitud.getDocumentoIdentidadcNrgar()) == false) {
            return;
        }
        
        //Validaciones MADRE
        
        if(servAnticipo.isAnticiposActivos(solicitudAnticipo.getIdeGeedp().getIdeGeedp()+"")){
        	utilitario.agregarMensajeInfo("No se puede realizar otro anticipo", "Tiene un anticipo activo");
        	return;
        }
        
        if(validarCondicionesAnticipo(solicitudAnticipo.getIdeGeedp().getIdeGeedp()+"")==false){
        	return;
        }
        
        double monto=0;
        try {
			monto=Double.parseDouble(solicitudAnticipo.getMontoSolicitadoNrant()+"");
		} catch (Exception e) {
			// TODO: handle exception
			monto=0;
		}
        
        if(monto<=0){
        	utilitario.agregarMensajeError("Monto no v�lido", "El monto debe ser mayor a 0");
        	return;
        }
        
        if(validarMontoMaximoAnticipo(monto,solicitudAnticipo.getIdeGeedp().getIdeGeedp()+"")==false){
        	return;
        }

        if (isIngresarGarante()) {
            if (garanteSolicitud.getIdeNrtig().getIdeNrtig() != null) {
                garanteSolicitud.setIdeNrtig(servicioCreditos.getGarante(garanteSolicitud.getIdeNrtig().getIdeNrtig().toString()));
            }
            if (garanteSolicitud.getIdeGttdi().getIdeGttdi() != null) {
                garanteSolicitud.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(garanteSolicitud.getIdeGttdi().getIdeGttdi().toString()));
            }
            if (objEmpleadoGarante != null) {
                garanteSolicitud.setIdeGeedp(servicioCreditos.getEmpleadoDepartamentoPartida(((Object[]) objEmpleadoGarante)[0] + ""));
            }
            if (garanteTelefono.getIdeGttit().getIdeGttit() != null) {
                garanteTelefono.setIdeGttit(servicioEmpleado.getTipoTelefono(garanteTelefono.getIdeGttit().getIdeGttit().toString()));
                garanteTelefono.setActivoGttel(new Boolean("true"));
            }
        }

        String str_mensaje = servicioCreditos.guardarSolicitudAnticipo(solicitudAnticipo, garanteSolicitud, garanteTelefono);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
            cargarDatos();
        } else {
            utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
        }
    }
    
    
    private boolean validarCondicionesAnticipo(String ide_geedp_activo){
		String num_dias_trabajados=ser_nomina.getTotalDiasAntiguedadEmp(ide_geedp_activo, utilitario.getFechaActual());
		String minimo_dias=servAnticipo.getMinimoDiasLaboradosParaAnticipos(ide_geedp_activo);
		if (minimo_dias!=null){
			try {
				if (Integer.parseInt(num_dias_trabajados)<Integer.parseInt(minimo_dias)){
					utilitario.agregarMensajeInfo("No se puede realizar el Anticipo", "El empleado seleccionado no cumple con las condiciones de anticipos");
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede realizar el Anticipo", "El empleado seleccionado no tiene condiciones de anticipos");
			return false;
		}
		return true;
	}
    
    
    public boolean validarMontoMaximoAnticipo(double monto,String ide_geedp_activo){

		String monto_max_anticipo=servAnticipo.getMontoMaximoPermitidoAnticipo(ide_geedp_activo);
		//String monto_solicitado=tab_anticipo.getValor("MONTO_SOLICITADO_NRANT");
		//		if (monto_solicitado==null || monto_solicitado.isEmpty()){
		//			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No ha ingresado el monto a solicitar");
		//			return false;
		//		}
		if (monto_max_anticipo==null || monto_max_anticipo.isEmpty()){
			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No existe un monto maximo de anticipo establecido para el empleado");
			return false;
		}
		if (Double.parseDouble(monto_max_anticipo)==0){
			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No existe un monto maximo de anticipo establecido para el empleado");			
			return false;
		}
		if (monto > Double.parseDouble(monto_max_anticipo)){
			utilitario.agregarMensajeError("Monto solicitado invalido", "El Monto solicitado supera al monto maximo de anticipos para el empleado, MONTO MAXIMO PERMITIDO "+monto_max_anticipo);
			return false;
		}

		return true;
	}


    public List autocompletar(String query) {
        List suggestions = new ArrayList();
        for (int i = 0; i < listaEmpleadosDepartamento.size(); i++) {
            Object[] f = (Object[]) listaEmpleadosDepartamento.get(i);
            for (int j = 1; j < f.length; j++) {
                if (f[j] != null) {
                    String fl = f[j] + "";
                    if (fl.toUpperCase().startsWith(query.trim().toUpperCase())) {
                        suggestions.add(f);
                        break;
                    }
                }
            }
            if (suggestions.size() >= 10) {
                break;
            }
        }
        return suggestions;
    }

    /**
     * @param ide_gttdi
     * @param documento_identidad_gttdi
     * @return
     *
     * metodo booleano para validar el tipo de documento de identidad cedula y
     * ruc
     */
    private boolean validarDocumentoIdentidad(String ide_gttdi, String documento_identidad) {
        if (ide_gttdi != null && !ide_gttdi.isEmpty()) {
            if (documento_identidad != null && !documento_identidad.isEmpty()) {
                if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))) {
                    if (!utilitario.validarCedula(documento_identidad)) {
                        utilitario.agregarMensajeInfo("Atencion", "El n�mero de cedula ingresado no es valido");
                        return false;
                    }
                } else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))) {
                    if (!utilitario.validarRUC(documento_identidad)) {
                        utilitario.agregarMensajeInfo("Atencion", "El n�mero de RUC ingresado no es valido");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void actualizarTablaAmortizacion(AjaxBehaviorEvent evt) {

        if (objCreditoSeleccionado != null) {
            //Carga la tabla de amortizaci�n seleccionada
            listaDetaCreditos = servicioCreditos.getTablaAmortizacionCredito(((Object[]) objCreditoSeleccionado)[0] + "");
            //Calculo el saldo 
            TablaGenerica tab_pagos = utilitario.consultar("select IDE_NRAMO,NRO_CUOTA_NRAMO,"
                    + "FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO "
                    + "from NRH_AMORTIZACION "
                    + "WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT=" + ((Object[]) objCreditoSeleccionado)[0] + ") "
                    + "and ACTIVO_NRAMO=0 "
                    + "ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
            str_saldo = utilitario.getFormatoNumero(tab_pagos.getSumaColumna("CUOTA_NRAMO"));

        }
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public NrhAnticipo getSolicitudAnticipo() {
        return solicitudAnticipo;
    }

    public void setSolicitudAnticipo(NrhAnticipo solicitudAnticipo) {
        this.solicitudAnticipo = solicitudAnticipo;
    }

    public List<NrhAnticipo> getListaAnticipos() {
        return listaAnticipos;
    }

    public void setListaAnticipos(List<NrhAnticipo> listaAnticipos) {
        this.listaAnticipos = listaAnticipos;
    }

    public List getListaMotivos() {
        return listaMotivos;
    }

    public void setListaMotivos(List listaMotivos) {
        this.listaMotivos = listaMotivos;
    }

    public List getListaTipoDocumento() {
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public List getListaTipoGarante() {
        return listaTipoGarante;
    }

    public void setListaTipoGarante(List listaTipoGarante) {
        this.listaTipoGarante = listaTipoGarante;
    }

    public List getListaEmpleadosDepartamento() {
        return listaEmpleadosDepartamento;
    }

    public void setListaEmpleadosDepartamento(List listaEmpleadosDepartamento) {
        this.listaEmpleadosDepartamento = listaEmpleadosDepartamento;
    }

    public NrhGarante getGaranteSolicitud() {
        return garanteSolicitud;
    }

    public void setGaranteSolicitud(NrhGarante garanteSolicitud) {
        this.garanteSolicitud = garanteSolicitud;
    }

    public boolean isIngresarGarante() {
        return ingresarGarante;
    }

    public void setIngresarGarante(boolean ingresarGarante) {
        this.ingresarGarante = ingresarGarante;
    }

    public GthTelefono getGaranteTelefono() {
        return garanteTelefono;
    }

    public void setGaranteTelefono(GthTelefono garanteTelefono) {
        this.garanteTelefono = garanteTelefono;
    }

    public List getListaTiposTelefono() {
        return listaTiposTelefono;
    }

    public void setListaTiposTelefono(List listaTiposTelefono) {
        this.listaTiposTelefono = listaTiposTelefono;
    }

    public Object getObjEmpleadoGarante() {
        return objEmpleadoGarante;
    }

    public void setObjEmpleadoGarante(Object objEmpleadoGarante) {
        this.objEmpleadoGarante = objEmpleadoGarante;
    }

    public List getListaCabCreditos() {
        return listaCabCreditos;
    }

    public void setListaCabCreditos(List listaCabCreditos) {
        this.listaCabCreditos = listaCabCreditos;
    }

    public Object getObjCreditoSeleccionado() {
        return objCreditoSeleccionado;
    }

    public void setObjCreditoSeleccionado(Object objCreditoSeleccionado) {
        this.objCreditoSeleccionado = objCreditoSeleccionado;
    }

    public List getListaDetaCreditos() {
        return listaDetaCreditos;
    }

    public void setListaDetaCreditos(List listaDetaCreditos) {
        this.listaDetaCreditos = listaDetaCreditos;
    }

    public String getStrPathReporte() {
        return strPathReporte;
    }

    public void setStrPathReporte(String strPathReporte) {
        this.strPathReporte = strPathReporte;
    }

    public String getStr_saldo() {
        return str_saldo;
    }

    public void setStr_saldo(String str_saldo) {
        this.str_saldo = str_saldo;
    }

    public NrhAnticipo getAnticipoSeleccionado() {
        return anticipoSeleccionado;
    }

    public void setAnticipoSeleccionado(NrhAnticipo anticipoSeleccionado) {
        this.anticipoSeleccionado = anticipoSeleccionado;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.sri;

import comprobantesElectronicos.dao.ComprobanteDAOLocal;
import comprobantesElectronicos.ejb.ejbAutorizaComprobante;
import comprobantesElectronicos.ejb.ejbClaveAcceso;
import comprobantesElectronicos.ejb.ejbContingencia;
import comprobantesElectronicos.ejb.ejbRecepcionComprobante;
import comprobantesElectronicos.entidades.Comprobante;
import framework.aplicacion.TablaGenerica;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless
public class ServicioComprobatesElectronicos {

    @EJB
    private ComprobanteDAOLocal comprobanteDAO;
    @EJB
    private ejbClaveAcceso ejbClaveAcceso;
    @EJB
    private ejbRecepcionComprobante ejbRecepcion;
    @EJB
    private ejbAutorizaComprobante ejbAutoriza;
    @EJB
    private ejbContingencia ejbContingencia;

    /**
     * Genera un comprobante electronico, lo firma digitalmente y lo envia al
     * SRI
     *
     * @param ide_srcom Codigo Comprobante
     * @return
     */
    public String generarComprobanteElectronico(String ide_srcom) {
        Utilitario utilitario = new Utilitario();
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_xml_comprobante WHERE ide_srcom=" + ide_srcom + " order by fecha_hora_srxmc desc limit 1");
        String mensaje = "";
        String cadenaXML = tab_consulta.getValor("xml_srxmc");
        //Verifica que el comprobante se encuentre en la base de datos
        cadenaXML = cadenaXML == null ? "" : cadenaXML.replace("'", "\"");
        if (cadenaXML.trim().isEmpty()) {
            return "Error estructura XML en blanco";
        }
        //remplaza ñ y tildes
        cadenaXML = cadenaXML.replaceAll("Ñ", "N");
        cadenaXML = cadenaXML.replaceAll("ñ", "n");
        cadenaXML = cadenaXML.replaceAll("Á", "A");
        cadenaXML = cadenaXML.replaceAll("á", "a");
        cadenaXML = cadenaXML.replaceAll("É", "E");
        cadenaXML = cadenaXML.replaceAll("é", "e");
        cadenaXML = cadenaXML.replaceAll("Í", "I");
        cadenaXML = cadenaXML.replaceAll("í", "i");
        cadenaXML = cadenaXML.replaceAll("ó", "o");
        cadenaXML = cadenaXML.replaceAll("Ó", "O");
        cadenaXML = cadenaXML.replaceAll("Ú", "U");
        cadenaXML = cadenaXML.replaceAll("ú", "u");

        //System.out.println(cadenaXML);
        Comprobante comprobante = comprobanteDAO.getComprobanteporNumero(
                ejbClaveAcceso.getValorEtiqueta(cadenaXML, "estab"),
                ejbClaveAcceso.getValorEtiqueta(cadenaXML, "ptoEmi"),
                ejbClaveAcceso.getValorEtiqueta(cadenaXML, "secuencial"));
        if (comprobante == null) {
            return "El comprobante no se encuentra registrado en la base de datos";
        }
        mensaje = ejbRecepcion.eviarDocumentoFirmado(comprobante, cadenaXML);
        //Autorizar el comprobante si el estado es RECIBIDA
        if (mensaje.equalsIgnoreCase("RECIBIDA")) {
            //Tiempo de espera antes de autorizar
            ejbAutoriza.esperarAntesAutorizar();
            mensaje = ejbAutoriza.enviarAutorizacionDocumento(comprobante.getClaveacceso());
        }
        return mensaje;
    }

    /**
     * Recibe la autorización del SRI de un comprobante electrónico
     *
     * @param claveAcceso
     * @return
     */
    public String autorizarComprobanteElectronico(String claveAcceso) {
        String mensaje = ejbAutoriza.enviarAutorizacionDocumento(claveAcceso);
        return mensaje;
    }

    /**
     * Envia todos los comprobantes que se encuentran en estado de contingencia
     * para que el SRI los autorice
     *
     * @return
     */
    public String enviarComprobantesContingencia() {
        String mensajesContingencia = ejbContingencia.enviarComprobantesContingencia();
        String mensajesRecibidos = ejbContingencia.autorizarComprobantesRecibidos();
        if (mensajesContingencia == null && mensajesRecibidos == null) {
            return "No hay comprobantes por enviar\n";
        } else {
            String mensajes = mensajesContingencia != null ? mensajesContingencia + "  \n" : "";
            mensajes += mensajesRecibidos != null ? mensajesRecibidos : "";
            return mensajes;
        }
    }
}

/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.ejb;

import comprobantesElectronicos.dao.ClaveContingenciaDAOLocal;
import comprobantesElectronicos.dao.ComprobanteDAOLocal;
import comprobantesElectronicos.entidades.Clavecontingencia;
import comprobantesElectronicos.entidades.Comprobante;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ejbClaveAcceso {

    @EJB
    private ClaveContingenciaDAOLocal claveContingenciaDAO;
    @EJB
    private ComprobanteDAOLocal comprobanteDAO;

    private final Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /**
     * Obtiene la clave de acceso de manera normal para el documento electronico
     *
     * @param comprobante
     * @return
     */
    public String getClaveAcceso(Comprobante comprobante) {
        String fechaEmision = utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd/MM/yyyy");
        String tipoComprobante = comprobante.getCodigotipcomp().getAlternotipcomp();
        String ruc = comprobante.getCodigoemisor().getRuc();
        String ambiente = comprobante.getCodigoemisor().getAmbiente().toString();
        String serie = comprobante.getEstab() + comprobante.getPtoemi();
        String numeroComprobante = comprobante.getSecuencial();
        String tipoEmision = comprobante.getTipoemision().toString();
        String oficina = utilitario.getVariable("IDE_SUCU"); //Oficina  char(3)
        oficina = oficina == null ? "000" : oficina;
        if (oficina.length() == 1) {
            oficina = "00" + oficina;
        } else if (oficina.length() == 2) {
            oficina = "0" + oficina;
        }

        if (ruc != null && ruc.length() < 13) {
            ruc = String.format("%013d", new Object[]{Integer.parseInt(ruc)});
        }

        if (numeroComprobante != null && numeroComprobante.length() < 9) {
            numeroComprobante = String.format("%09d", new Object[]{Integer.parseInt(numeroComprobante)});
        }
        int verificador = 0;

        String fecha = fechaEmision.replaceAll("/", "");
        String codigoNumerico = getNumberCod(fecha.substring(4), oficina);

        StringBuilder clave = new StringBuilder(fecha);
        clave.append(tipoComprobante);
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(serie);
        clave.append(numeroComprobante);
        clave.append(codigoNumerico);
        clave.append(tipoEmision);
        verificador = getVerifierModule11(clave.toString());
        clave.append(Integer.valueOf(verificador));

        if (clave.toString().length() != 49) {
            System.out.println("Error al generar la clave de acceso!!");
            return null;
        }
        return clave.toString();
    }

    public String getClaveContingencia(Comprobante comprobante) {
        StringBuilder clave = new StringBuilder();
        //Obtengo una clave de contingencia que este disponible
        Clavecontingencia claveContingencia = claveContingenciaDAO.getClaveDisponible();
        //Valida que existan claves de contingencia disponibles
        if (claveContingencia != null) {
            String fechaEmision = utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd/MM/yyyy");
            String tipoComprobante = comprobante.getCodigotipcomp().getAlternotipcomp();
            String tipoEmision = comprobante.getTipoemision().toString();
            String fecha = fechaEmision.replaceAll("/", "");
            clave.append(fecha);
            clave.append(tipoComprobante);
            clave.append(claveContingencia.getClavecont());
            clave.append(tipoEmision);
            int verificador = getVerifierModule11(clave.toString());
            if (verificador != 10) {
                clave.append(Integer.valueOf(verificador));
            }
            if (clave.toString().length() != 49) {
                return "Error al generar la clave de acceso";
            } else {
                //Actualiza a no disponible la clave asignada
                claveContingenciaDAO.actualizarClaveNoDisponible(claveContingencia);
                //Actualiza la clave de contingencia al comprobante
                comprobante.setClaveacceso(clave.toString());
                comprobanteDAO.actualizarClaveContingencia(comprobante, claveContingencia);
            }
        } else {
            return "Error no tiene claves de contigencia disponibles";
        }
        return clave.toString();
    }

    /**
     * Obtiene el digito verificador de la clave de acceso en modulo 11
     *
     * @param cadena
     * @return
     */
    public int getVerifierModule11(String cadena) {
        int baseMultiplicador = 7;
        int aux[] = new int[cadena.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador = 0;
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt((new StringBuilder()).append("").append(cadena.charAt(i)).toString());
            aux[i] = aux[i] * multiplicador;
            if (++multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
            total += aux[i];
        }
        //--Ya tenemos el total
        if (total == 0 || total == 1) {
            verificador = 0;
        } else {
            verificador = (11 - (total % 11)) != 11 ? 11 - total % 11 : 0;
        }
        if (verificador == 10) {
            verificador = 1;
        }
        return verificador;
    }

    /**
     * Genera el codigo numerico de 8 caracteres en funcion de la oficina y el
     * año de una fecha
     *
     * @param anio
     * @param idlocation
     * @return
     */
    public String getNumberCod(String anio, String idlocation) {
        return "0" + idlocation + "" + anio;
    }

    public String getValorEtiqueta(String cadenaXML, String etiqueta) {
        String str_valor = "";
        try {
            String str_etiqueta1 = "<" + etiqueta + ">";
            String str_etiqueta2 = "</" + etiqueta + ">";
            str_valor = cadenaXML.substring((cadenaXML.indexOf(str_etiqueta1) + str_etiqueta1.length()), (cadenaXML.indexOf(str_etiqueta2)));
        } catch (Exception e) {
        }
        return str_valor;
    }
}

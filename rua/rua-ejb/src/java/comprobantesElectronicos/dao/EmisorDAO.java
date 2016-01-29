/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Emisor;
import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class EmisorDAO implements EmisorDAOLocal {

    private final Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Emisor getEmisor() {

        Emisor emisor = null;

        TablaGenerica tab_consulta = utilitario.consultar("select * from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
        if (tab_consulta.isEmpty() == false) {
            emisor = new Emisor();
            emisor.setCodigoemisor(new Integer(tab_consulta.getValor("ide_empr")));
            emisor.setRuc(tab_consulta.getValor("identificacion_empr"));
            emisor.setRazonsocial(tab_consulta.getValor("nom_corto_empr"));
            emisor.setNombrecomercial(tab_consulta.getValor("nom_empr"));
            emisor.setDirmatriz(tab_consulta.getValor("direccion_empr"));
            emisor.setContribuyenteespecial(tab_consulta.getValor("contribuyenteespecial_empr"));
            emisor.setObligadocontabilidad(tab_consulta.getValor("obligadocontabilidad_empr"));
            //emisor.setLogoempresa(tab_consulta.getValor("re_logoempresa")); //Logo 

            //Consulta ws de recepcion 1 y de autorizacion 2  *********!!!!PONER VARIABLES 
            TablaGenerica tab_ws = utilitario.consultar("SELECT * FROM gen_webservice where ide_gewes in (1,2)");

            for (int i = 0; i < tab_ws.getTotalFilas(); i++) {
                if (tab_ws.getValor(i, "ide_gewes").equals("1")) {//WS RECEPCION
                    emisor.setWsdlrecepcion(tab_ws.getValor(i, "wsdl_gewes"));
                } else if (tab_ws.getValor(i, "ide_gewes").equals("2")) {//WS RECEPCION
                    emisor.setWsdlautirizacion(tab_ws.getValor(i, "wsdl_gewes"));
                    emisor.setTiempomaxespera(new Integer(tab_ws.getValor(i, "tiempomax_gewes")));
                }
            }

            emisor.setAmbiente(1); //*********!!!!PONER VARIABLES   //1 pruebas   //2 produccion
            emisor.setXmlversion("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");//*********!!!!PONER VARIABLES 
        }

        return emisor;
    }

}

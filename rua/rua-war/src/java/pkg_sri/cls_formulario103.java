/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_sri;

import framework.aplicacion.TablaGenerica;
import java.util.List;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_formulario103 {

    private Document doc_formulario103;
    private String path = "";
    private String nombre = "";
    private String tipoDeclaracion = "O";
    private String numSustituye = "";
    private final Utilitario utilitario = new Utilitario();
    private final String[] nom_mes = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
    private String fecha_inicio = "";
    private String fecha_fin = "";
    private String v302, v303, v304, v307, v308, v309, v310, v312, v319, v320, v322, v323, v325, v327, v328, v332, v340, v341, v342, v343, v344, v349, v352, v353, v354, v357, v358, v359, v360, v362, v369, v370, v372, v373, v375, v377, v378, v390, v391, v392, v393, v394, v399;
    private String v401, v403, v405, v421, v427, v429, v451, v453, v455, v471, v498, v499;
    private String v890, v897, v898, v899, v880;
    private String v902, v903, v904, v999, v905, v907, v908, v909, v910, v911, v912, v913, v915;

    public String Formulario103(String anio, String mes) {
        try {
            fecha_inicio = utilitario.getFormatoFecha(anio + "-" + mes + "-01");
            fecha_fin = utilitario.getUltimaFechaMes(fecha_inicio);
            TablaGenerica tab_empresa = utilitario.consultar("SELECT identificacion_empr,nom_empr,identi_repre_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
            if (tab_empresa.getTotalFilas() == 1) {
                doc_formulario103 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                doc_formulario103.setXmlVersion("1.0");
                doc_formulario103.setXmlStandalone(true);

                Element raiz = doc_formulario103.createElement("formulario");
                raiz.setAttribute("version", "2.0");
                doc_formulario103.appendChild(raiz);
                //CABECERA
                Element cabecera = doc_formulario103.createElement("cabecera");
                raiz.appendChild(cabecera);
                cabecera.appendChild(crearElemento("codigo_version_formulario", null, "03201202"));
                cabecera.appendChild(crearElemento("ruc", null, tab_empresa.getValor("identificacion_empr")));
                cabecera.appendChild(crearElemento("codigo_moneda", null, "1"));
                //DETALLE
                Element detalle = doc_formulario103.createElement("detalle");
                raiz.appendChild(detalle);
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "31"}, getTipoDeclaracion())); // O ORIGINAL S SUTITUTIVA
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "101"}, mes)); // MES
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "102"}, anio)); // AÑO
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "104"}, getNumSustituye())); //NUMERO DE FORMULARIO QUE LO SUSTITUYE
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "198"}, tab_empresa.getValor("identi_repre_empr")));//IDENTIDFICACION REPRESENTANTE
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "199"}, "0200749620001")); //RUC CONTADOR
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "201"}, tab_empresa.getValor("identificacion_empr")));//RUC
                //detalle.appendChild(crearElementoCDATA("campo", new String[]{"numero", "202"}, tab_empresa.getValor("nom_empr")));//NOMBRE
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "202"}, tab_empresa.getValor("nom_empr")));//NOMBRE
                //base
                v302 = consultarRenta(utilitario.getVariable("p_sri_base_renta"));
                v303 = consultarBaseCasillero("303");
                v304 = consultarBaseCasillero("304");
                v307 = consultarBaseCasillero("307");
                v308 = consultarBaseCasillero("308");
                v309 = consultarBaseCasillero("309");
                v310 = consultarBaseCasillero("310");
                v312 = consultarBaseCasillero("312");
                v319 = consultarBaseCasillero("319");
                v320 = consultarBaseCasillero("320");
                v322 = consultarBaseCasillero("322");
                v323 = consultarBaseCasillero("323");
                v325 = consultarBaseCasillero("325");
                v327 = consultarBaseCasillero("327");
                v328 = consultarBaseCasillero("328");
                v332 = consultarBaseCasillero("332");
                v340 = consultarBaseCasillero("340");
                v341 = consultarBaseCasillero("341");
                v342 = consultarBaseCasillero("342");
                v343 = consultarBaseCasillero("343");
                v344 = consultarBaseCasillero("344");
                v349 = utilitario.getFormatoNumero(Double.parseDouble(v302)
                        + Double.parseDouble(v303) + Double.parseDouble(v304)
                        + Double.parseDouble(v307) + Double.parseDouble(v308)
                        + Double.parseDouble(v309) + Double.parseDouble(v310)
                        + Double.parseDouble(v312) + Double.parseDouble(v319)
                        + Double.parseDouble(v320) + Double.parseDouble(v322)
                        + Double.parseDouble(v323) + Double.parseDouble(v325)
                        + Double.parseDouble(v327) + Double.parseDouble(v328)
                        + Double.parseDouble(v332) + Double.parseDouble(v340)
                        + Double.parseDouble(v341) + Double.parseDouble(v342)
                        + Double.parseDouble(v343) + Double.parseDouble(v344));

                //cuadra con ats por facturas que no generan retencion 332 
                double dou_total_ats = Double.parseDouble(consultarComprasAts());
                double dou_total = Double.parseDouble(v349);
                if (dou_total != dou_total_ats) {
                    //1 si es mayor el total en el ats
                    double d332 = Double.parseDouble(v332);
                    if (dou_total_ats > dou_total) {
                        double diferencia = dou_total_ats - dou_total;
                        //sumo al 332 actual la diferencia                        
                        d332 += diferencia;
                    } else {
                        double diferencia = dou_total - dou_total_ats;
                        //resto al 332 actual la diferencia                        
                        d332 -= diferencia;
                    }
                    v332 = utilitario.getFormatoNumero(d332);
                    v349 = utilitario.getFormatoNumero(Double.parseDouble(v302)
                            + Double.parseDouble(v303) + Double.parseDouble(v304)
                            + Double.parseDouble(v307) + Double.parseDouble(v308)
                            + Double.parseDouble(v309) + Double.parseDouble(v310)
                            + Double.parseDouble(v312) + Double.parseDouble(v319)
                            + Double.parseDouble(v320) + Double.parseDouble(v322)
                            + Double.parseDouble(v323) + Double.parseDouble(v325)
                            + Double.parseDouble(v327) + Double.parseDouble(v328)
                            + Double.parseDouble(v332) + Double.parseDouble(v340)
                            + Double.parseDouble(v341) + Double.parseDouble(v342)
                            + Double.parseDouble(v343) + Double.parseDouble(v344));
                }

                //valor
                v352 = consultarRenta(utilitario.getVariable("p_sri_impuesto_renta"));
                v353 = consultarValorCasillero("303");
                v354 = consultarValorCasillero("304");
                v357 = consultarValorCasillero("307");
                v358 = consultarValorCasillero("308");
                v359 = consultarValorCasillero("309");
                v360 = consultarValorCasillero("310");
                v362 = consultarValorCasillero("312");
                v369 = consultarValorCasillero("319");
                v370 = consultarValorCasillero("320");
                v372 = consultarValorCasillero("322");
                v373 = consultarValorCasillero("323");
                v375 = consultarValorCasillero("325");
                v377 = consultarValorCasillero("327");
                v378 = consultarValorCasillero("328");
                v390 = consultarValorCasillero("340");
                v391 = consultarValorCasillero("341");
                v392 = consultarValorCasillero("342");
                v393 = consultarValorCasillero("343");
                v394 = consultarValorCasillero("344");
                v399 = utilitario.getFormatoNumero(Double.parseDouble(v352) + Double.parseDouble(v353) + Double.parseDouble(v354) + Double.parseDouble(v357) + Double.parseDouble(v358) + Double.parseDouble(v359) + Double.parseDouble(v360) + Double.parseDouble(v362) + Double.parseDouble(v369) + Double.parseDouble(v370) + Double.parseDouble(v372) + Double.parseDouble(v373) + Double.parseDouble(v375) + Double.parseDouble(v377) + Double.parseDouble(v378) + Double.parseDouble(v390) + Double.parseDouble(v391) + Double.parseDouble(v392) + Double.parseDouble(v393) + Double.parseDouble(v394));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "302"}, v302));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "303"}, v303));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "304"}, v304));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "307"}, v307));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "308"}, v308));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "309"}, v309));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "310"}, v310));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "312"}, v312));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "319"}, v319));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "320"}, v320));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "322"}, v322));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "323"}, v323));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "325"}, v325));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "327"}, v327));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "328"}, v328));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "332"}, v332));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "340"}, v340));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "341"}, v341));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "342"}, v342));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "343"}, v343));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "344"}, v344));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "349"}, v349));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "352"}, v352));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "353"}, v353));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "354"}, v354));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "357"}, v357));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "358"}, v358));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "359"}, v359));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "360"}, v360));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "362"}, v362));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "369"}, v369));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "370"}, v370));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "372"}, v372));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "373"}, v373));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "375"}, v375));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "377"}, v377));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "378"}, v378));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "390"}, v390));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "391"}, v391));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "392"}, v392));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "393"}, v393));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "394"}, v394));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "399"}, v399));
                //PAGOS AL EXTERIOR
                v401 = "0.00";
                v403 = "0.00";
                v405 = "0.00";
                v421 = "0.00";
                v427 = "0.00";
                v429 = utilitario.getFormatoNumero((Double.parseDouble(v401) + Double.parseDouble(v403) + Double.parseDouble(v405) + Double.parseDouble(v421) + Double.parseDouble(v427)));
                v451 = "0.00";
                v453 = "0.00";
                v455 = "0.00";
                v471 = "0.00";
                v498 = utilitario.getFormatoNumero((Double.parseDouble(v451) + Double.parseDouble(v453) + Double.parseDouble(v455) + Double.parseDouble(v471)));
                v499 = utilitario.getFormatoNumero(Double.parseDouble(v399) + Double.parseDouble(v498));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "401"}, v401));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "403"}, v403));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "405"}, v405));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "421"}, v421));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "427"}, v427));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "429"}, v429));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "451"}, v451));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "453"}, v453));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "455"}, v455));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "471"}, v471));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "498"}, v498));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "499"}, v499));
                //INTERESES POR SUSTITUCIÓN DE FORMULARIO
                v890 = "0.00";
                v897 = "0.00";
                v898 = "0.00";
                v899 = "0.00";
                v880 = "0.00";
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "880"}, v880));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "890"}, v890));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "897"}, v897));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "898"}, v898));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "899"}, v899));
                //VALORES A PAGAR Y FORMA DE PAGO
                v902 = utilitario.getFormatoNumero((Double.parseDouble(v499) - Double.parseDouble(v898)));
                v903 = "0.00";
                v904 = "0.00";
                v999 = utilitario.getFormatoNumero((Double.parseDouble(v902) + Double.parseDouble(v903) + Double.parseDouble(v904)));
                v905 = v999;
                v907 = "0.00";
                v908 = "";
                v909 = "0.00";
                v910 = "";
                v911 = "0.00";
                v912 = "";
                v913 = "0.00";
                v915 = "0.00";
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "902"}, v902));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "903"}, v903));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "904"}, v904));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "905"}, v905));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "907"}, v907));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "908"}, v908));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "909"}, v909));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "910"}, v910));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "911"}, v911));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "912"}, v912));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "913"}, v913));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "915"}, v915));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "922"}, "16"));
                detalle.appendChild(crearElemento("campo", new String[]{"numero", "999"}, v999));

                ///ESCRIBE EL DOCUMENTO
                Source source = new DOMSource(doc_formulario103);
                String master = System.getProperty("user.dir");
                nombre = "03ORI_" + nom_mes[Integer.parseInt(mes) - 1] + anio + ".xml";
                Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
                path = master + "/" + nombre;
                Result console = new StreamResult(System.out);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);
                transformer.transform(source, console);
                return utilitario.getStringFromDocument(doc_formulario103);
            }
        } catch (Exception e) {
            System.err.println("Error al generar el Formulario 103: " + e.getMessage());
            utilitario.agregarMensajeError("No se pudo generar el Formulario", "No hay información para generar el formulario");
        }
        return null;
    }

    private Element crearElemento(String nombre, String[] atributos, String texto) {
        Element elemento = doc_formulario103.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        if (texto == null) {
            texto = "";
        }
        elemento.appendChild(doc_formulario103.createTextNode(texto));
        return elemento;
    }

    private Element crearElementoCDATA(String nombre, String[] atributos, String texto) {
        Element elemento = doc_formulario103.createElement(nombre);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                elemento.setAttribute(atributos[0], atributos[1]);
            }
        }
        elemento.appendChild(doc_formulario103.createCDATASection(texto));
        return elemento;
    }

    public String consultarRenta(String rubro) {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(rubr.valor_rhrro) from reh_cab_rol_pago cabr "
                + "inner join reh_empleados_rol empr on empr.ide_rhcrp= cabr.ide_rhcrp "
                + "inner join reh_rubros_rol rubr on rubr.ide_rherl=empr.ide_rherl "
                + "where rubr.ide_rhcru =" + rubro + " "
                + "and cabr.fecha_inicio_rhcrp = '" + fecha_inicio + "' "
                + "and  cabr.fecha_fin_rhcrp='" + fecha_fin + "' ");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(utilitario.getFormatoNumero(lis_sql.get(0)) + "");
            } catch (Exception e) {
                dou_valor = 0;
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    public String consultarBaseCasillero(String casillero) {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT SUM(dr.base_cndre) "
                + " FROM con_cabece_retenc cr "
                + " LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) "
                + " JOIN con_cabece_impues ci ON (ci.ide_cncim = dr.ide_cncim) "
                + " WHERE cr.fecha_emisi_cncre BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                + " AND ci.casillero_cncim in (" + utilitario.generarComillaSimple(casillero) + ") "
                + " AND ide_cnere=0 "//no anuladas
                + " and es_venta_cncre=false");
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
                dou_valor = 0;
            }
        }
        return utilitario.getFormatoNumero(dou_valor);

    }

    public String consultarValorCasillero(String casillero) {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("SELECT SUM(dr.porcentaje_cndre/100 * dr.base_cndre) "
                + " FROM con_cabece_retenc cr "
                + " LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) "
                + " JOIN con_cabece_impues ci ON (ci.ide_cncim = dr.ide_cncim) "
                + " WHERE cr.fecha_emisi_cncre BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                + " AND ci.casillero_cncim in (" + utilitario.generarComillaSimple(casillero) + ") "
                + " AND ide_cnere=0 "//no anuladas
                + " and es_venta_cncre=false");

        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    public String consultarComprasAts() {
        double dou_valor = 0;
        List lis_sql = utilitario.getConexion().consultar("select sum(base_grabada_cpcfa + base_no_objeto_iva_cpcfa+base_tarifa0_cpcfa) as tatal from cxp_cabece_factur\n"
                + "where fecha_emisi_cpcfa BETWEEN '" + fecha_inicio + "' AND '" + fecha_fin + "' "
                + "and ide_rem_cpcfa is null and ide_cpefa=0");  //filtra no anuladas
        if (lis_sql != null && !lis_sql.isEmpty()) {
            try {
                dou_valor = Double.parseDouble(lis_sql.get(0) + "");
            } catch (Exception e) {
                dou_valor = 0;
            }
        }
        return utilitario.getFormatoNumero(dou_valor);
    }

    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(String tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public String getNumSustituye() {
        return numSustituye;
    }

    public void setNumSustituye(String numSustituye) {
        if (numSustituye == null) {
            numSustituye = "";
        }
        this.numSustituye = numSustituye;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.pensiones;

import framework.aplicacion.TablaGenerica;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.ServicioBase;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.contabilidad.ServicioConfiguracion;

/**
 *
 * @author djacome
 */
@Stateless

public class ServicioPensiones extends ServicioBase {

    @EJB
    private ServicioConfiguracion ser_configuracion;

    @EJB
    private ServicioComprobanteElectronico ser_comprobante_electronico;

    public String generarFacturaElectronica(String ide_recva) {
        String ide_cccfa_return = null;
        TablaGenerica tag = utilitario.consultar("select * from pen_tmp_lista_fact where cod_factura_petlf='" + ide_recva + "'");
        if (tag.isEmpty() == false) {
            TablaGenerica tab_clientes = new TablaGenerica();
            tab_clientes.setTabla("gen_persona", "ide_geper");
            tab_clientes.setCondicion("identificac_geper in (" + tag.getStringColumna("cedula_petlf") + ",'9999999999')");
            tab_clientes.ejecutarSql();
            //inserta facturas
            TablaGenerica tab_cab_fac = new TablaGenerica();
            tab_cab_fac.setTabla("cxc_cabece_factura", "ide_cccfa");
            tab_cab_fac.setCondicion("ide_cccfa=-1");
            tab_cab_fac.setGenerarPrimaria(false);
            tab_cab_fac.getColumna("ide_cccfa").setExterna(false);
            tab_cab_fac.ejecutarSql();

            TablaGenerica tab_det_fac = new TablaGenerica();
            tab_det_fac.setTabla("cxc_deta_factura", "ide_ccdfa");
            tab_det_fac.setCondicion("ide_ccdfa=-1");
            tab_det_fac.ejecutarSql();

            TablaGenerica tab_info_adicional = new TablaGenerica();
            tab_info_adicional.setTabla("sri_info_adicional", "ide_srina");
            tab_info_adicional.setCondicion("ide_srina=-1");
            tab_info_adicional.ejecutarSql();

            long ide_cccfa = utilitario.getConexion().getMaximo("cxc_cabece_factura", "ide_cccfa", 1);
            //Recupera porcentaje iva 
            double tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());

            String cedula_petlf = tag.getValor("cedula_petlf");
            if (cedula_petlf == null || cedula_petlf.isEmpty()) {
                cedula_petlf = "9999999999";
            }
            String ide_geper = null;
            for (int j = 0; j < tab_clientes.getTotalFilas(); j++) {
                if (cedula_petlf.equals(tab_clientes.getValor(j, "identificac_geper"))) {
                    ide_geper = tab_clientes.getValor(j, "ide_geper");
                    break;
                }
            }
            tab_cab_fac.insertar();
            tab_cab_fac.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_cab_fac.setValor("ide_cntdo", "3"); //3 = FACTURA
            tab_cab_fac.setValor("ide_ccefa", "0"); //0 = NORMAL
            tab_cab_fac.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_cab_fac.setValor("fecha_trans_cccfa", utilitario.getFechaActual());
            tab_cab_fac.setValor("fecha_emisi_cccfa", tag.getValor("fecha_petlf"));
            tab_cab_fac.setValor("dias_credito_cccfa", "0");
            tab_cab_fac.setValor("ide_cndfp", "13");//13=OTROS SIN UTILIZAR SITEMA FINANCIERO
            tab_cab_fac.setValor("ide_cndfp1", "8");//8=CRÉDITO 10 DÍAS
            tab_cab_fac.setValor("dias_credito_cccfa", "10");//10 DÍAS
            tab_cab_fac.setValor("DIRECCION_CCCFA", tag.getValor("direccion_petlf"));
            tab_cab_fac.setValor("base_grabada_cccfa", "0");
            tab_cab_fac.setValor("base_no_objeto_iva_cccfa", "0");
            tab_cab_fac.setValor("valor_iva_cccfa", "0");
            tab_cab_fac.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(tag.getValor("subtotal_petlf")));
            tab_cab_fac.setValor("descuento_cccfa", utilitario.getFormatoNumero(tag.getValor("rebaja_petlf")));
            tab_cab_fac.setValor("orden_compra_cccfa", tag.getValor("cedula_alumno_petlf"));  //Guarda la cedula del alumno 
            tab_cab_fac.setValor("total_cccfa", utilitario.getFormatoNumero(tag.getValor("total_petlf")));
            tab_cab_fac.setValor("correo_cccfa", tag.getValor("correo_petlf"));
            tab_cab_fac.setValor("pagado_cccfa", "false");
            tab_cab_fac.setValor("ide_geper", ide_geper);
            tab_cab_fac.setValor("ide_ccdaf", "2"); //2= FACTURAS ELECTRONICAS
            tab_cab_fac.setValor("telefono_cccfa", tag.getValor("telefono_petlf"));
            tab_cab_fac.setValor("tarifa_iva_cccfa", utilitario.getFormatoNumero((tarifaIVA * 100)));

            TablaGenerica tag_con_detalle = utilitario.consultar("        select ide_valdet_revad,IDE_TITULO_RECVA, detalle_revad, cantidad_revad, precio_revad, total_revad, valor_descuento_revad, c.iva_inarti, c.ide_inarti\n"
                    + "        from rec_valor_detalle  a\n"
                    + "        left join rec_impuesto b on a.ide_impuesto_reimp = b.ide_impuesto_reimp\n"
                    + "        left join inv_articulo c on b.ide_inarti = c.ide_inarti\n"
                    + "        where a.IDE_TITULO_RECVAL = " + tag.getValor("cod_factura_petlf"));
            for (int j = 0; j < tag_con_detalle.getTotalFilas(); j++) {
                tab_det_fac.insertar();
                tab_det_fac.setValor("ide_inarti", tag_con_detalle.getValor(j, "ide_inarti")); //57 == servicios colegio
                tab_det_fac.setValor("CANTIDAD_CCDFA", tag_con_detalle.getValor(j, "cantidad_revad"));
                tab_det_fac.setValor("PRECIO_CCDFA", utilitario.getFormatoNumero(tag_con_detalle.getValor(j, "precio_revad")));
                tab_det_fac.setValor("iva_inarti_ccdfa", tag_con_detalle.getValor(j, "iva_inarti"));
                tab_det_fac.setValor("descuento_ccdfa", utilitario.getFormatoNumero(tag_con_detalle.getValor(j, "valor_descuento_revad")));
                tab_det_fac.setValor("total_ccdfa", utilitario.getFormatoNumero(tag_con_detalle.getValor(j, "total_revad")));
                tab_det_fac.setValor("OBSERVACION_CCDFA", tag_con_detalle.getValor(j, "detalle_revad"));
                tab_det_fac.setValor("ALTERNO_CCDFA", "00");
                tab_det_fac.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            }

            //info adicional
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Código Alumno");
            tab_info_adicional.setValor("valor_srina", tag.getValor("codigo_alumno_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Nombre Alumno");
            tab_info_adicional.setValor("valor_srina", tag.getValor("nombre_alumno_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Paralelo");
            tab_info_adicional.setValor("valor_srina", tag.getValor("paralelo_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Período Lectivo");
            tab_info_adicional.setValor("valor_srina", tag.getValor("periodo_lectivo_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Recibo");
            tab_info_adicional.setValor("valor_srina", tag.getValor("cod_factura_petlf"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));
            tab_info_adicional.insertar();
            tab_info_adicional.setValor("nombre_srina", "Usuario");
            tab_info_adicional.setValor("valor_srina", utilitario.getVariable("NICK"));
            tab_info_adicional.setValor("ide_cccfa", String.valueOf(ide_cccfa));

            if (tab_cab_fac.guardar()) {
                if (tab_det_fac.guardar()) {
                    if (tab_info_adicional.guardar()) {
                        //Guarda la cuenta por cobrar
                        // ser_factura.generarTransaccionFactura(tab_cab_factura);  ///Cambiar uno a uno
                        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                            ide_cccfa_return = tab_cab_fac.getValor("ide_cccfa");
                            ser_comprobante_electronico.generarFacturaElectronica(tab_cab_fac.getValor("ide_cccfa"));
                            utilitario.getConexion().ejecutarSql("UPDATE sri_info_adicional a set ide_srcom = (select ide_srcom from cxc_cabece_factura where ide_cccfa=a.ide_cccfa) where ide_srcom IS null");
                            //elimina de la tabla temporal
                            utilitario.getConexion().ejecutarSql("delete from pen_tmp_lista_fact where cod_factura_petlf='" + ide_recva + "'");
                        }
                    }
                }
            }
        }
        return ide_cccfa_return;
    }

    /**
     * Listado de Alumnos
     *
     * @return
     */
    public String getSqlAlumnos() {
        return "SELECT a.ide_geper,a.codigo_geper as CODIGO,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES,a.direccion_geper AS DIRECCION,a.telefono_geper AS TELEFONO,b.nom_geper  as REPRESENTANTE,a.ide_vgecl AS ACTIVO FROM gen_persona a left join  gen_persona b on a.rep_ide_geper = b.ide_geper where a.ide_vgtcl=1 and a.nivel_geper='HIJO' order by a.nom_geper";
    }

    public String getSqlComboAlumnos() {
        return "SELECT a.ide_geper,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES,a.codigo_geper as CODIGO FROM gen_persona a where a.ide_vgtcl=1 and a.nivel_geper='HIJO' order by a.nom_geper";
    }

    public String getSqlComboRepresentantes() {
        return "SELECT a.ide_geper,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES FROM gen_persona a where a.ide_vgtcl=0 and a.nivel_geper='HIJO' order by a.nom_geper";

    }

    public String getSqlRepresentantes() {
        return "SELECT a.ide_geper,a.identificac_geper AS CEDULA,a.nom_geper AS APELLIDOS_Y_NOMBRES, correo_geper as CORREO FROM gen_persona a where a.ide_vgtcl=0  and a.nivel_geper='HIJO' order by a.nom_geper";
    }

    /**
     *
     * @param cedula_alumno
     * @return
     */
    public String getFacturasAlumno(String cedula_alumno) {
        return "select a.ide_cccfa, secuencial_cccfa,ide_cnccc,a.ide_ccefa,nombre_sresc as nombre_ccefa, fecha_emisi_cccfa,(select numero_cncre from con_cabece_retenc where ide_cncre=a.ide_cncre)as NUM_RETENCION,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,descuento_cccfa as DESCUENTO,valor_iva_cccfa,total_cccfa, "
                + "claveacceso_srcom as CLAVE_ACCESO, fecha_trans_cccfa,a.ide_cncre,d.ide_srcom,a.ide_geper,direccion_cccfa,orden_compra_cccfa AS NUM_REFERENCIA  "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "left join con_deta_forma_pago x on a.ide_cndfp1=x.ide_cndfp "
                + "where orden_compra_cccfa='" + cedula_alumno + "' "
                + "AND a.ide_ccefa =0"
                + " ORDER BY secuencial_cccfa desc,ide_cccfa desc";
    }

    /**
     *
     * @param ide_geper Alumno
     * @param fechaInicio Fecha Inicio
     * @param fechaFin Facha Fin
     * @return
     */
    public String getSqlFacturasAlumno(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT * FROM cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + " where a.ide_geper=" + ide_geper + " fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' ";
    }

    public String getPeriodoAcademico(String activo) {
        String sql = "";
        sql = "select a.ide_repea, b.nom_geani, a.descripcion_repea, a.fecha_inicial_repea, a.fecha_final_repea\n"
                + "from rec_periodo_academico a\n"
                + "inner join gen_anio b on a.ide_geani = b.ide_geani\n"
                + "where activo_repea in (" + activo + ")\n"
                + "order by a.descripcion_repea";
        return sql;
    }

    public String getCursos(String activo) {
        String sql = "";
        sql = "select ide_recur, descripcion_recur \n"
                + "from rec_curso \n"
                + "where activo_recur in (" + activo + ") \n"
                + "order by orden_recur";
        return sql;
    }

    public String getParalelos(String activo) {
        String sql = "";
        sql = "select ide_repar, descripcion_repar\n"
                + "from rec_paralelos \n"
                + "where activo_repar in (" + activo + ") \n"
                + "order by descripcion_repar";
        return sql;
    }

    public String getEspecialidad(String activo) {
        String sql = "";
        sql = "select ide_reces, descripcion_reces\n"
                + "from rec_especialidad \n"
                + "where activo_reces in (" + activo + ") \n"
                + "order by descripcion_reces";
        return sql;
    }

    public String getListaAlumnos(String condicion,String tipo_persona) {
        String sql="select ide_geper, nom_geper, identificac_geper from gen_persona";
                if(condicion.equals("1")){
                    sql +=" where ide_vgtcl in("+tipo_persona+") order by nom_geper ";
                }
        return sql;
    }

    public String getPeriodosEstudiantes(String codigo) {
        return "select a.ide_recalp, c.nom_geani, b.descripcion_repea, b.fecha_inicial_repea, b.fecha_final_repea\n"
                + "from rec_alumno_periodo a\n"
                + "inner join rec_periodo_academico b on a.ide_repea = b.ide_repea\n"
                + "inner join gen_anio c on b.ide_geani = c.ide_geani\n"
                + "where a.ide_geper =  " + codigo + "";
    }

    public String selectPenTemp(String codigo) {
        return "select a.IDE_TITULO_RECVAL as codigo_fac, b.ide_geper, b.codigo_geper as codigo_alumno, b.identificac_geper as cedula_alumno, b.nom_geper as nombres_alumno,\n" +
"               c.identificac_geper as cedula_repre, c.nom_geper as nom_repre, c.direccion_geper as direccion_repre, c.telefono_geper as telefono_repre, c.correo_geper as correo_repre,\n" +
"               d.periodo_lectivo as periodo_academico, d.paralelo_alumno as paralelo, a.TOTAL_RECVA as total, des_concepto_recon as concepto, a.FECHA_EMISION_RECVA as fecha, a.ide_sucu as ide_sucu, a.ide_empr as ide_empr, \n" +
"               f.detalle_revad, f.cantidad_revad, f.precio_revad, f.total_revad, f.valor_descuento_revad, f.iva_inarti\n" +
"               from rec_valores a \n" +
"               left join gen_persona b on a.ide_geper = b.ide_geper and b.ide_vgtcl=1 and b.nivel_geper='HIJO'\n" +
"			   left join gen_persona c on a.gen_ide_geper = c.ide_geper and c.nivel_geper='HIJO' and c.ide_vgtcl=0\n" +
"			   left join  (select ide_recalp, c.nom_geani||' '||b.descripcion_repea as periodo_lectivo,\n" +
"			               d.descripcion_recur||' '||descripcion_repar as paralelo_alumno from rec_alumno_periodo a \n" +
"			               inner join rec_periodo_academico b on a.ide_repea = b.ide_repea\n" +
"			               inner join gen_anio c on b.ide_geani = c.ide_geani\n" +
"			               inner join rec_curso d on a.ide_recur = d.ide_recur\n" +
"			               inner join rec_paralelos e on a.ide_repar = e.ide_repar) d on a.ide_recalp = d.ide_recalp\n" +
"	        left join rec_concepto e on a.IDE_CONCEPTO_RECON = e.IDE_CONCEPTO_RECON \n" +
"	        left join (\n" +
"	        select ide_valdet_revad,IDE_TITULO_RECVAL, detalle_revad, cantidad_revad, precio_revad, total_revad, valor_descuento_revad, c.iva_inarti\n" +
"	        from rec_valor_detalle  a\n" +
"	        left join rec_impuesto b on a.ide_impuesto_reimp = b.ide_impuesto_reimp\n" +
"	        left join inv_articulo c on b.ide_inarti = c.ide_inarti\n" +
"	        ) f on a.IDE_TITULO_RECVAL = f.IDE_TITULO_RECVAL\n" +
"	        where a.IDE_TITULO_RECVAL = "+codigo+"";
               /* + "select a.IDE_TITULO_RECVAL as codigo_fac, b.ide_geper, b.codigo_geper as codigo_alumno, b.identificac_geper as cedula_alumno, b.nom_geper as nombres_alumno,\n"
                + "c.identificac_geper as cedula_repre, c.nom_geper as nom_repre, c.direccion_geper as direccion_repre, c.telefono_geper as telefono_repre, c.correo_geper as correo_repre,\n"
                + "d.periodo_lectivo as periodo_academico, d.paralelo_alumno as paralelo, a.TOTAL_RECVA as total, des_concepto_recon as concepto, a.FECHA_EMISION_RECVA as fecha, a.ide_sucu as ide_sucu, a.ide_empr as ide_empr "
                + " from rec_valores a \n"
                + "left join gen_persona b on a.ide_geper = b.ide_geper\n"
                + "left join gen_persona c on a.gen_ide_geper = c.ide_geper\n"
                + "left join  (select ide_recalp, c.nom_geani||' '||b.descripcion_repea as periodo_lectivo,\n"
                + "            d.descripcion_recur||' '||descripcion_repar as paralelo_alumno from rec_alumno_periodo a \n"
                + "            inner join rec_periodo_academico b on a.ide_repea = b.ide_repea\n"
                + "            inner join gen_anio c on b.ide_geani = c.ide_geani\n"
                + "            inner join rec_curso d on a.ide_recur = d.ide_recur\n"
                + "            inner join rec_paralelos e on a.ide_repar = e.ide_repar) d on a.ide_recalp = d.ide_recalp \n"
                + "left join rec_concepto e on a.IDE_CONCEPTO_RECON = e.IDE_CONCEPTO_RECON \n"
                + "where b.ide_vgtcl=1 and b.nivel_geper='HIJO' and c.ide_vgtcl=0 and c.nivel_geper='HIJO'\n"
                + "and a.IDE_TITULO_RECVAL =  " + codigo + "";*/
    }

    public String getCodigoMaximoTabla(String tabla, String primario) {
        String sql = "";
        sql = "select 1 as codigo, (case when  max(" + primario + ") is null then 1 else max(" + primario + ") end) + 1 as maximo from " + tabla;
        return sql;
    }
    public String getSqlConceptos(){
        String sql = "";
        sql = "select ide_concepto_recon, des_concepto_recon from rec_concepto order by des_concepto_recon";
        return sql;
    }
      public String getAlumnosDeuda(String parametro_deuda){
      String sql="";
      sql ="select ide_geper, codigo_geper as CODIGO_ALUMNO, ALUMNO,  REPRESENTANTE, sum(VALOR_TOTAL) as valor_total from (\n" +
           "       select a.ide_titulo_recval,a.ide_geper, b.codigo_geper, b.nom_geper as ALUMNO, c.nom_geper as REPRESENTANTE, d.suma as VALOR_TOTAL\n" +
           "       from rec_valores a\n" +
           "       left join gen_persona b on a.ide_geper = b.ide_geper\n" +
           "       left join gen_persona c on a.gen_ide_geper = c.ide_geper\n" +
           "       left join (select ide_titulo_recval, sum(total_revad) as suma from rec_valor_detalle group by ide_titulo_recval) d on a.ide_titulo_recval = d.ide_titulo_recval\n" +
           "       where ide_recest = "+parametro_deuda+"\n" +
           " ) a \n" +
           "group by ide_geper,ALUMNO,  REPRESENTANTE, CODIGO_ALUMNO";
      return sql;
  }
      public String getAlumnosDeudaConsulta(String parametro){
          String sql="";
          sql = "select a.ide_titulo_recval, b.nom_geper as REPRESENTANTE,  descripcion_recest as ESTADO,nombre_gemes as MES, total_recva as VALOR_TOTAL\n" +
                "from rec_valores a\n" +
                "left join gen_persona b on a.gen_ide_geper = b.ide_geper\n" +
                "left join rec_estados c on a.ide_recest = c.ide_recest\n" +
                "left join gen_mes d on a.ide_gemes = d.ide_gemes\n" +
                "where a.ide_recest = "+parametro+"";
          return sql;
      }
      
      public String getAlumnosDeudaConsultaTotal(String parametro){
          String sql="";
          sql = "select a.ide_titulo_recval,REPRESENTANTE,ESTADO,des_concepto_recon,MES,VALOR_TOTAL,\n" +
"nom_geper as alumno,identificac_geper as cd_alumno,codigo_geper as codigo,descripcion_repea,nom_geani,descripcion_repar,descripcion_recur,descripcion_reces,activo_recalp,retirado_recalp,fecha_retiro_recalp,detalle_retiro_recalp,aplica_convenio_recva,fecha_iniconve_recva,fecha_finconve_recva\n" +
"from (\n" +
"select a.ide_titulo_recval,a.ide_recalp, b.nom_geper as REPRESENTANTE,  descripcion_recest as ESTADO,des_concepto_recon,nombre_gemes as MES, total_recva as VALOR_TOTAL,aplica_convenio_recva,fecha_iniconve_recva,fecha_finconve_recva \n" +
"                from rec_valores a\n" +
"                left join gen_persona b on a.gen_ide_geper = b.ide_geper\n" +
"                left join rec_estados c on a.ide_recest = c.ide_recest\n" +
"                left join gen_mes d on a.ide_gemes = d.ide_gemes\n" +
"                left join rec_concepto e on a.ide_concepto_recon= e.ide_concepto_recon\n" +
"                where a.ide_recest=2\n" +
") a\n" +
"left join (              \n" +
"\n" +
"select nom_geper,identificac_geper,codigo_geper,descripcion_repea,nom_geani,descripcion_repar,descripcion_recur,descripcion_reces,activo_recalp,a.ide_repea,a.ide_repar,a.ide_recur,a.ide_reces,\n" +
"retirado_recalp,fecha_retiro_recalp,detalle_retiro_recalp,a.ide_recalp\n" +
"from rec_alumno_periodo a, gen_persona b,rec_periodo_academico c,gen_anio d,rec_paralelos g,rec_curso e, rec_especialidad f\n" +
"where a.ide_geper= b.ide_geper\n" +
"and a.ide_repea = c.ide_repea\n" +
"and c.ide_geani= d.ide_geani\n" +
"and a.ide_repar = g.ide_repar\n" +
"and a.ide_recur = e.ide_recur\n" +
"and a.ide_reces = f.ide_reces\n" +
"order by a.ide_repea,descripcion_reces,descripcion_recur,descripcion_repar,nom_geper\n" +
"\n" +
") b on a.ide_recalp = b.ide_recalp";
          return sql;
      }
}

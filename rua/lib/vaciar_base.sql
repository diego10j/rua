delete from sis_auditoria_acceso;
delete from sis_auditoria;
update con_det_conf_asie set ide_cndpc=null, ide_inarti=null;
delete from cxc_guia;
delete from cxc_detall_transa;
delete from cxc_cabece_transa;
delete from cxp_detall_transa;
delete from cxp_cabece_transa;
delete from tes_det_libr_banc;
delete from tes_cab_libr_banc;
delete from inv_articulo_carac;
delete from inv_deta_formula;
delete from inv_orden_prod;
delete from inv_conversion_unidad;
delete from inv_cab_formula;
delete from inv_det_comp_inve;
delete from inv_cab_comp_inve;
delete from iyp_pago_interes;
delete from  iyp_deta_prestamo;
delete from  iyp_cab_prestamo;
delete from  iyp_certificado;
delete from gen_camion;
delete from cxp_detalle_nota;
delete from cxp_cabecera_nota;
delete from cxp_detall_factur;
delete from cxp_cabece_factur;
delete from cxc_deta_factura;
delete from cxc_cabece_factura;
delete from nrh_detalle_asiento;
delete from con_detall_retenc;
delete from con_cabece_retenc;
delete from con_det_comp_cont;
delete from con_cab_comp_cont;
delete from cont_asiento_tipo;
delete from cont_nombre_asiento_contable;
delete from tes_cuenta_banco;
delete from tes_cuenta_banco;
delete from pre_asociacion_presupuestaria;
delete from pre_tipo_aporte_presu;
delete from con_det_plan_cuen where con_ide_cndpc is not  null;
delete from nrh_detalle_rol;
delete from sri_detalle_proyecccion_ingres;
delete from sri_proyeccion_ingres;
delete from  gen_empleados_departamento_par;
delete from gen_partida_grupo_cargo;
delete from gen_departamento_sucursal;
delete from gen_departamento;
delete from sis_bloqueo;
delete from sri_xml_comprobante;
delete from sri_comprobante ;
update con_cabece_impues set ide_sucu=0;
update con_det_conf_asie set ide_sucu=0;
update con_detall_impues set ide_sucu=0;
update con_deta_forma_pago set ide_sucu=0;
update con_estado_compro set ide_sucu=0;
update con_periodo set ide_sucu=0;
update con_vigenc_impues set ide_sucu=0;
update con_vig_conf_asie set ide_sucu=0;
update cxp_datos_factura set ide_sucu=0;
update sri_emisor set ide_sucu=0;
update cxc_datos_fac set ide_sucu=0;
update gen_organigrama set ide_sucu=0;
update gen_persona set ide_sucu=0;
update inv_articulo set ide_sucu=0;
update act_estado_activo_fijo set ide_sucu=0;
update cxc_tipo_transacc set ide_sucu=0;
update cxc_tipo_transacc set ide_sucu=0;
update inv_bodega set ide_sucu =0;
update inv_marca set ide_sucu =0;
update act_ubicacion_activo set ide_sucu =0;
update pre_tipo_aporte_presu set ide_sucu=0;
update pre_tipo_participantes set ide_sucu=0;
update pre_tipo_rubro_presu set ide_sucu=0;
update reh_detalle_rubro set ide_sucu=0;
update reh_cab_rubro set ide_sucu=0;
update reh_carga set ide_sucu=0;
update reh_empleados_rol set ide_sucu=0;
update reh_empleados_por_carac set ide_sucu=0;
update reh_estruc_organi set ide_sucu=0;
update reh_historial_empleado set ide_sucu=0;
update reh_tipo_asiento set ide_sucu=0;
update reh_tipo_contrato set ide_sucu=0;
update reh_tipo_empleado set ide_sucu=0;
update sis_usuario_sucursal set ide_sucu=0;
delete from iyp_pago_interes;
delete from iyp_deta_prestamo;
delete from iyp_cab_prestamo;
delete from iyp_certificado;
delete from iyp_clase_inversion;
delete from iyp_cab_inversion;
delete from reh_empleados_por_carac;
delete from reh_empleados_rol;
delete from reh_historial_empleado;
delete from sri_detalle_proyecccion_ingres;
delete from gth_correo;
delete from sri_deducibles_empleado;
delete from pre_mensual;
delete from pre_tramite;
delete from sri_proyeccion_ingres;
delete from nrh_detalle_rol;
delete from sri_proyeccion_ingres;
delete from gen_empleados_departamento_par;
delete from gen_detalle_empleado_departame;
delete from gth_empleado;
delete from nrh_rol;
delete from act_foto_activo;
delete from act_transaccion;
delete from act_asignacion_activo;
delete from act_acta_constata;
delete from act_activo_fijo;

delete from adq_empleado;
delete from adq_empleado;


delete from nrh_cabecera_asiento;
delete from gen_perido_rol;
delete from gen_periodo;
delete from bodt_costo_articulo;
delete from  bodt_articulos;
DELETE FROM cxp_datos_factura;
update con_det_conf_asie set ide_geper = null ;
delete from reh_carga;
delete from gen_persona where gen_ide_geper is not null;

delete from cont_vigente;
delete from pre_poa_tramite;
delete from pre_anual;
delete from pre_programa;
delete from pre_funcion_programa;
delete from modulomigra;
delete from pre_funcion_programa;
DELETE from gen_persona where nivel_geper !='PADRE';
DELETE from gen_persona where es_proveedo_geper=true;
delete  from inv_articulo where nivel_inarti='HIJO';
delete from pre_clasificador;
delete from migra_actividadp;
delete  from adq_area_administrativa;
-- 0 y 11 no borrar
delete from sis_usuario_sucursal where ide_usua not in (0,11);
delete from sis_usuario_clave where ide_usua not in (0,11);
delete from sis_usuario where ide_usua not in (0,11);

delete from sis_sucursal where ide_sucu !=0;

delete from cxc_datos_fac;

	
--contar cuantos registros tiene cada tabla
SELECT
nspname AS schemaname,relname,reltuples::integer
FROM pg_class C
LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace)
WHERE
nspname NOT IN ('pg_catalog', 'information_schema') AND
relkind='r'
ORDER BY reltuples DESC;


---borrar permiso a perfil

delete from sis_perfil_reporte where ide_repo in (select ide_repo from sis_reporte where ide_opci in (select ide_opci from sis_opcion where sis_ide_opci= 278));
delete from sis_perfil_reporte where ide_repo in (select ide_repo from sis_reporte where ide_opci =278);
delete from sis_reporte where ide_opci in (select ide_opci from sis_opcion where sis_ide_opci= 278);
delete from sis_reporte where ide_opci =278;
delete from sis_campo where ide_tabl in ( select ide_tabl from sis_tabla where ide_opci in (select ide_opci from sis_opcion where sis_ide_opci= 278));
delete from sis_campo where ide_tabl in ( select ide_tabl from sis_tabla where ide_opci =278);
delete from sis_tabla where ide_opci in (select ide_opci from sis_opcion where sis_ide_opci= 278);
delete from sis_tabla where ide_opci =278;
delete from sis_perfil_opcion where ide_opci in (select ide_opci from sis_opcion where sis_ide_opci= 278);
delete from sis_perfil_opcion where ide_opci =278;



---borrar trans


delete from inv_det_comp_inve;
delete from inv_cab_comp_inve;
delete from cxp_detall_transa;
delete from cxp_detall_factur;
delete from cxp_cabece_transa;
delete from cxp_cabece_factur;
delete from sri_info_adicional;
delete from cxc_detall_transa;
delete from cxc_deta_factura;
delete from cxc_cabece_transa;
delete from cxc_cabece_factura;
delete from con_detall_retenc;
delete from con_cabece_retenc;
delete from cxp_detalle_nota;
delete from cxp_cabecera_nota;
delete from sri_xml_comprobante;
delete from sri_comprobante;
delete from con_det_comp_cont;
delete from con_cab_comp_cont;
delete from sis_bloqueo;
delete  from gen_persona where nivel_geper='HIJO' and usuario_ingre is not null;
delete from act_foto_activo;
delete from act_asignacion_activo;
delete from act_transaccion;
delete from act_acta_constata;
delete from act_activo_fijo; 
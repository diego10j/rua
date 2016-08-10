DROP TABLE cont_asiento_tipo;
DROP TABLE cont_nombre_asiento_contable;
CREATE TABLE cont_nombre_asiento_contable
(
  ide_conac bigint NOT NULL, -- Este campo define la clave primaria de la tabla cont_nombre_asiento_contable
  detalle_conac character varying(250), -- detalle_conac nombre del asiento contable ejemplo: ASIENTO DE VENTAS
  consolidado_conac boolean, -- Este campo define si se encuentre igual True  saco un solo asiento para el devengado y el pagado, se se encuentra igual a False se genera un primer asiento por devengado y un primer asiento por pagado.
  individual_conac boolean, -- Este campo define cuando si  el campo es igual a True puedo generar asientos individuales Ej. asiento nomina permite generar asiento por empleado, si es igual a False saca un solo asiento por empleado
  activo_conac boolean, -- Define el estado del registro si se encuentra activo o inactivo.
  usuario_ingre character varying(50), -- Este campo define el ultimo usuario que realiza la actualizacion del registro.
  fecha_ingre date, -- Este campo define la fecha que se realizo el ingreso por primera ves el registro.
  hora_ingre time without time zone, -- Este campo define la ultima hora que el usuario realiza la actualizacion del registro.
  usuario_actua character varying(50), -- Este campo define el ultimo usuario que realiza la actualizacion del registro.
  fecha_actua date, -- Este campo define la ultima fecha que realiza la actualizacion del registro.
  hora_actua time without time zone, -- Este campo define la hora que se realizo el ingreso por primera ves el registro.
  ide_modu integer, -- aqui van los moidulos al que pertenece el asiento
  CONSTRAINT pk_cont_nombre_asiento_contabl PRIMARY KEY (ide_conac),
  CONSTRAINT fk_cont_nombre_asiento_contable_reference_gen_modulo FOREIGN KEY (ide_modu)
      REFERENCES sis_modulo (ide_modu) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=TRUE
);
ALTER TABLE cont_nombre_asiento_contable OWNER TO postgres;
COMMENT ON TABLE cont_nombre_asiento_contable IS 'Esta tabla define todos los tipos de asiento contable que se encuentran en la aplicación Ej. asiento ingreso compra, asiento pago proveedor, asiento nómina, asiento descripción, asiento tipo crédito.';
COMMENT ON COLUMN cont_nombre_asiento_contable.ide_conac IS 'Este campo define la clave primaria de la tabla cont_nombre_asiento_contable';
COMMENT ON COLUMN cont_nombre_asiento_contable.detalle_conac IS 'detalle_conac';
COMMENT ON COLUMN cont_nombre_asiento_contable.consolidado_conac IS 'Este campo define si se encuentre igual True  saco un solo asiento para el devengado y el pagado, se se encuentra igual a False se genera un primer asiento por devengado y un primer asiento por pagado.  ';
COMMENT ON COLUMN cont_nombre_asiento_contable.individual_conac IS 'Este campo define cuando si  el campo es igual a True puedo generar asientos individuales Ej. asiento nomina permite generar asiento por empleado, si es igual a False saca un solo asiento por empleado ';
COMMENT ON COLUMN cont_nombre_asiento_contable.activo_conac IS 'Define el estado del registro si se encuentra activo o inactivo.';
COMMENT ON COLUMN cont_nombre_asiento_contable.usuario_ingre IS 'Este campo define el ultimo usuario que realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_nombre_asiento_contable.fecha_ingre IS 'Este campo define la fecha que se realizo el ingreso por primera ves el registro.';
COMMENT ON COLUMN cont_nombre_asiento_contable.hora_ingre IS 'Este campo define la ultima hora que el usuario realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_nombre_asiento_contable.usuario_actua IS 'Este campo define el ultimo usuario que realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_nombre_asiento_contable.fecha_actua IS 'Este campo define la ultima fecha que realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_nombre_asiento_contable.hora_actua IS 'Este campo define la hora que se realizo el ingreso por primera ves el registro.';


CREATE TABLE cont_asiento_tipo
(
  ide_coast bigint NOT NULL, -- Este campo define la clave primaria de la tabla cont_tipo_asiento
  ide_cndpc bigint, -- Este campo define la clave primaria d ela cuenta contable
  ide_cnlap bigint, -- Este campo define la clave primaria de la tabla gen_lugar_aplica valor entero generado.
  ide_inarti bigint, -- Articulo 
  activo_coast boolean, -- Define el estado del registro si se encuentra activo o inactivo.
  usuario_ingre character varying(50), -- Este campo define el ultimo usuario que realiza la actualizacion del registro.
  fecha_ingre date, -- Este campo define la fecha que se realizo el ingreso por primera ves el registro.
  hora_ingre time without time zone, -- Este campo define la ultima hora que el usuario realiza la actualizacion del registro.
  usuario_actua character varying(50), -- Este campo define el ultimo usuario que realiza la actualizacion del registro.
  fecha_actua date, -- Este campo define la ultima fecha que realiza la actualizacion del registro.
  hora_actua time without time zone, -- Este campo define la hora que se realizo el ingreso por primera ves el registro.
  ide_conac integer,
  tipo_iva_coast boolean,
  base_imponible_coast boolean,
  valor_total_coast boolean,
  factor_menos_coast boolean,
  ide_prcla integer, -- este campo define la clave primaria del catalogo presupuestario
  ide_prmop integer, -- este campo define el tipo de movieminto presupuestario.
  CONSTRAINT pk_cont_asiento_tipo PRIMARY KEY (ide_coast),
  CONSTRAINT fk_cont_asi_reference_cont_cat FOREIGN KEY (ide_cndpc)
      REFERENCES con_det_plan_cuen (ide_cndpc) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_cont_asi_reference_cont_nombre_asiento_contable FOREIGN KEY (ide_conac)
      REFERENCES cont_nombre_asiento_contable (ide_conac) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_cont_asi_reference_gen_luga FOREIGN KEY (ide_cnlap)
      REFERENCES con_lugar_aplicac (ide_cnlap) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
	  CONSTRAINT fk_con_inv_articulo FOREIGN KEY (ide_inarti)
      REFERENCES inv_articulo (ide_inarti) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
	  	  CONSTRAINT fk_con_pre_clasificador FOREIGN KEY (ide_prcla)
      REFERENCES pre_clasificador (ide_prcla) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
	  	  CONSTRAINT fk_con_pre_movimiento_presupuestario FOREIGN KEY (ide_prmop)
      REFERENCES pre_movimiento_presupuestario (ide_prmop) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
	  
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cont_asiento_tipo OWNER TO postgres;
COMMENT ON TABLE cont_asiento_tipo IS 'cont_asiento_tipo';
COMMENT ON COLUMN cont_asiento_tipo.ide_coast IS 'Este campo define la clave primaria de la tabla cont_tipo_asiento';
COMMENT ON COLUMN cont_asiento_tipo.ide_cnlap IS 'Este campo define la clave primaria de la tabla gen_lugar_aplica valor entero generado.';
COMMENT ON COLUMN cont_asiento_tipo.activo_coast IS 'Define el estado del registro si se encuentra activo o inactivo.';
COMMENT ON COLUMN cont_asiento_tipo.usuario_ingre IS 'Este campo define el ultimo usuario que realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_asiento_tipo.fecha_ingre IS 'Este campo define la fecha que se realizo el ingreso por primera ves el registro.';
COMMENT ON COLUMN cont_asiento_tipo.hora_ingre IS 'Este campo define la ultima hora que el usuario realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_asiento_tipo.usuario_actua IS 'Este campo define el ultimo usuario que realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_asiento_tipo.fecha_actua IS 'Este campo define la ultima fecha que realiza la actualizacion del registro.';
COMMENT ON COLUMN cont_asiento_tipo.hora_actua IS 'Este campo define la hora que se realizo el ingreso por primera ves el registro.';
--------------------------------------------------


DROP TABLE pre_asociacion_presupuestaria;

CREATE TABLE pre_asociacion_presupuestaria
(
  ide_prasp bigint NOT NULL, -- Este campo define la clave primaria de la tabla pre_asociacion_presupuestaria
  ide_prcla bigint, -- Este campo define la clave primaria de la tabla pre_clasificador.
  ide_cocac bigint, -- Este campo define la clave primaria de la tabla cont_catalogo_cuenta
  ide_cnlap bigint, -- Este campo define la clave primaria de la tabla gen_lugar_aplica valor entero generado.
  ide_prmop bigint, -- Este campo define la clave primaria de la tabla pre_asociacion_presupuestaria
  activo_prasp boolean NOT NULL, -- Define el estado del registro si se encuentra activo o inactivo.
  usuario_ingre character varying(50), -- Este campo define el ultimo usuario que realiza la actualizacion del registro.
  fecha_ingre date, -- Este campo define la fecha que se realizo el ingreso por primera ves el registro.
  hora_ingre time without time zone, -- Este campo define la ultima hora que el usuario realiza la actualizacion del registro.
  usuario_actua character varying(50), -- Este campo define el ultimo usuario que realiza la actualizacion del registro.
  fecha_actua date, -- Este campo define la ultima fecha que realiza la actualizacion del registro.
  hora_actua time without time zone, -- Este campo define la hora que se realizo el ingreso por primera ves el registro.
  cuenta_padre_prasp boolean,
  pagado integer,
  devengado integer,
  ide_cndpc integer,
  CONSTRAINT pk_pre_asociacion_presupuestar PRIMARY KEY (ide_prasp),
  CONSTRAINT fk_pre_asoc_reference_gen_luga FOREIGN KEY (ide_cnlap)
      REFERENCES con_lugar_aplicac (ide_cnlap) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_pre_asoc_reference_pre_clas FOREIGN KEY (ide_prcla)
      REFERENCES pre_clasificador (ide_prcla) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_pre_asoc_reference_pre_movi FOREIGN KEY (ide_prmop)
      REFERENCES pre_movimiento_presupuestario (ide_prmop) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT pk_plan_deta_cuenta_rua FOREIGN KEY (ide_cndpc)
      REFERENCES con_det_plan_cuen (ide_cndpc) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pre_asociacion_presupuestaria OWNER TO postgres;
COMMENT ON TABLE pre_asociacion_presupuestaria IS 'pre_asociacion_presupuestaria';
COMMENT ON COLUMN pre_asociacion_presupuestaria.ide_prasp IS 'Este campo define la clave primaria de la tabla pre_asociacion_presupuestaria';
COMMENT ON COLUMN pre_asociacion_presupuestaria.ide_prcla IS 'Este campo define la clave primaria de la tabla pre_clasificador.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.ide_cocac IS 'Este campo define la clave primaria de la tabla cont_catalogo_cuenta';
COMMENT ON COLUMN pre_asociacion_presupuestaria.ide_cnlap IS 'Este campo define la clave primaria de la tabla gen_lugar_aplica valor entero generado.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.ide_prmop IS 'Este campo define la clave primaria de la tabla pre_asociacion_presupuestaria';
COMMENT ON COLUMN pre_asociacion_presupuestaria.activo_prasp IS 'Define el estado del registro si se encuentra activo o inactivo.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.usuario_ingre IS 'Este campo define el ultimo usuario que realiza la actualizacion del registro.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.fecha_ingre IS 'Este campo define la fecha que se realizo el ingreso por primera ves el registro.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.hora_ingre IS 'Este campo define la ultima hora que el usuario realiza la actualizacion del registro.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.usuario_actua IS 'Este campo define el ultimo usuario que realiza la actualizacion del registro.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.fecha_actua IS 'Este campo define la ultima fecha que realiza la actualizacion del registro.';
COMMENT ON COLUMN pre_asociacion_presupuestaria.hora_actua IS 'Este campo define la hora que se realizo el ingreso por primera ves el registro.';


--CAMBIOS VARIOS'
--GUARDAR TELEFONO EN LA FACTURA DE VENTA
ALTER TABLE cxc_cabece_factura
  ADD COLUMN telefono_cccfa character varying(30);

--GUARDAR TARIFA DEL IVA EN FACTURA DE VENTA
ALTER TABLE cxc_cabece_factura
  ADD COLUMN tarifa_iva_cccfa  numeric(12,2);

--ACTUALIZA TARIFA IVA EN FACTURAS ANTERIORES AL 1 DE JUNIO DEL 2016
UPDATE cxc_cabece_factura set tarifa_iva_cccfa=0.12 WHERE  fecha_emisi_cccfa <= '2016/05/31';
UPDATE cxc_cabece_factura set tarifa_iva_cccfa=0.14 WHERE  fecha_emisi_cccfa > '2016/05/31';



--GUARDAR TARIFA DEL IVA EN DOCUMENTOS CXP
ALTER TABLE cxp_cabece_factur
  ADD COLUMN tarifa_iva_cpcfa  numeric(12,2);

--ACTUALIZA TARIFA IVA EN FACTURAS ANTERIORES AL 1 DE JUNIO DEL 2016
UPDATE cxp_cabece_factur set tarifa_iva_cpcfa=0.12 WHERE  fecha_emisi_cpcfa <= '2016/05/31';
UPDATE cxp_cabece_factur set tarifa_iva_cpcfa=0.14 WHERE  fecha_emisi_cpcfa > '2016/05/31';


--CAMPOS NOTA DE CREDITO
--TIPO DOCUMENTO NC
ALTER TABLE cxp_cabece_factur  ADD COLUMN ide_cntdo_nc_cpcfa integer;
--FECHA EMISION NC
ALTER TABLE cxp_cabece_factur  ADD COLUMN fecha_emision_nc_cpcfa date;
--NUMERO DOCUMENTO NC
ALTER TABLE cxp_cabece_factur  ADD COLUMN numero_nc_cpcfa character varying(20);
--AUTORIZACION NC
ALTER TABLE cxp_cabece_factur  ADD COLUMN autorizacio_nc_cpcfa character varying(50);
--MOTIVO NC
ALTER TABLE cxp_cabece_factur  ADD COLUMN motivo_nc_cpcfa character varying(60);
--fk
ALTER TABLE cxp_cabece_factur ADD CONSTRAINT fk_cxp_cabe_relations_con_tipo_nc FOREIGN KEY (ide_cntdo_nc_cpcfa)
REFERENCES con_tipo_document (ide_cntdo) MATCH SIMPLE
ON UPDATE RESTRICT ON DELETE RESTRICT;


--FACTURAS DE REEMBOLSO RECURSIVO
ALTER TABLE cxp_cabece_factur  ADD COLUMN ide_rem_cpcfa bigint;

ALTER TABLE cxp_cabece_factur ADD CONSTRAINT fk_cxp_cabe_relations_rembolso FOREIGN KEY (ide_rem_cpcfa)
REFERENCES cxp_cabece_factur (ide_cpcfa) MATCH SIMPLE
ON UPDATE RESTRICT ON DELETE RESTRICT;

--asiento tipo add columna tipo comprobante 
ALTER TABLE "public"."cont_nombre_asiento_contable"
ADD COLUMN "ide_cntcm" int4;
ALTER TABLE "public"."cont_nombre_asiento_contable"
ALTER COLUMN "ide_cntcm" SET NOT NULL;
ALTER TABLE "public"."cont_nombre_asiento_contable"
ADD CONSTRAINT "fk_tipo_comp_asiento_tipo" FOREIGN KEY ("ide_cntcm") REFERENCES "public"."con_tipo_comproba" ("ide_cntcm");

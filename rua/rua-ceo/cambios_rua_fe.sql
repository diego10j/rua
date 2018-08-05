--CAMNIOS BASE RUA

CREATE TABLE "public"."sri_emisor" (
"ide_sremi" int2 NOT NULL,
"tipoemision_sremi" varchar(1) COLLATE "default" NOT NULL,
"tiempo_espera_sremi" int4,
"wsdl_recep_offline_sremi" varchar(250) COLLATE "default" NOT NULL,
"wsdl_autori_offline_sremi" varchar(250) COLLATE "default" NOT NULL,
"ambiente_sremi" varchar(2) COLLATE "default",
"ide_empr" int4,
"ide_sucu" int4
)
WITH (OIDS=FALSE)
;
CREATE UNIQUE INDEX "sri_emisor_pk" ON "public"."sri_emisor" USING btree (ide_sremi);
ALTER TABLE "public"."sri_emisor" ADD PRIMARY KEY ("ide_sremi");


ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "periodo_fiscal_srcom" varbit(10);


ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "ide_srcom" int8;

create table sri_imp_comprobante (
ide_srico      			  integer   		 not null,
  ide_srcom integer,
codigo_srico                        character varying(2)     not null,
codigo_retencion_srico               character varying(4)     not null, 
base_imponible_srico                 numeric(12,2)          not null, 
porcentaje_retener_srico             numeric(12,2)          not null, 
valor_retenido_srico                 numeric(12,2)          not null, 
cod_doc_sustento_srico                character varying(4)     not null, 
num_doc_sustento_srico                character varying(16)     not null,  
fecha_emi_doc_sust_srico       date       	not null,
CONSTRAINT pk_sri_imp_comprobante PRIMARY KEY (ide_srico),
  CONSTRAINT fk_sri_imp_relations_sri_comp FOREIGN KEY (ide_srcom)
      REFERENCES sri_comprobante (ide_srcom) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
);
CREATE INDEX rel_com_impuesto_fk
  ON sri_imp_comprobante
  USING btree
  (ide_srcom);

CREATE UNIQUE INDEX sri_imp_comprobante_pk
  ON sri_imp_comprobante
  USING btree
  (ide_srico);

--ALTER TABLE "public"."sri_emisor"
--DROP COLUMN "establecimiento_sremi",
--DROP COLUMN "puntoemision_sremi";


ALTER TABLE "public"."cxc_deta_factura"
ADD COLUMN "ide_inuni" int8;

ALTER TABLE "public"."cxc_deta_factura"
ADD CONSTRAINT "fk_cxc_deta_rel_unidad" FOREIGN KEY ("ide_inuni") REFERENCES "public"."inv_unidad" ("ide_inuni") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."con_cabece_retenc"
ALTER COLUMN "autorizacion_cncre" TYPE varchar(49) COLLATE "default";

ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "fecha_sistema_srcom" date;

ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "reutiliza_srcom" bool;

ALTER TABLE "public"."cxc_cabece_factura"
ALTER COLUMN "ide_srcom" TYPE int8, ALTER COLUMN "ide_srcom" SET DEFAULT NULL;


ALTER TABLE "public"."gen_persona"
ADD COLUMN "codigo_geper" varchar(20);

CREATE INDEX "index_identicacion" ON "public"."gen_persona" USING btree ("identificac_geper");

ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "ret_fuente_cccfa" numeric(12,2),
ADD COLUMN "ret_iva_cccfa" numeric(12,2);


ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "ide_ccdaf" int8,
ADD COLUMN "total_cpcno" numeric(12,2);

ALTER TABLE "public"."cxp_cabecera_nota"
DROP COLUMN "autorizacio_cpcno";

ALTER TABLE "public"."cxp_cabecera_nota"
ADD CONSTRAINT "fk_cxp_cab_nota_datos_factura" FOREIGN KEY ("ide_ccdaf") REFERENCES "public"."cxc_datos_fac" ("ide_ccdaf") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "num_doc_mod_cpcno" varchar(20);

ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "fecha_emision_mod_cpcno" date,
ADD COLUMN "valor_mod_cpcno" numeric(12,2);

ALTER TABLE "public"."cxp_cabecera_nota"
DROP COLUMN "ide_cpttr";

ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "ide_srcom" int8;



ALTER TABLE "public"."cxp_cabecera_nota"
ADD CONSTRAINT "fk_cxp_cab_nota_comelec" FOREIGN KEY ("ide_srcom") REFERENCES "public"."sri_comprobante" ("ide_srcom") ON DELETE RESTRICT ON UPDATE RESTRICT;



ALTER TABLE "public"."cxp_detalle_nota"
ADD COLUMN "iva_inarti_cpdno" int2,
ADD COLUMN "ide_inuni" int8;

ALTER TABLE "public"."cxp_detalle_nota"
ADD CONSTRAINT "fk_cxp_deta_not_unidad" FOREIGN KEY ("ide_inuni") REFERENCES "public"."inv_unidad" ("ide_inuni") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "ide_cndfp" int4;

ALTER TABLE "public"."cxp_cabecera_nota"
ADD CONSTRAINT "fk_cxp_cab_nota_forma_pago" FOREIGN KEY ("ide_cndfp") REFERENCES "public"."con_deta_forma_pago" ("ide_cndfp") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "ide_cntdo" int4;

ALTER TABLE "public"."cxp_cabecera_nota"
ADD CONSTRAINT "fk_cxp_cab_nota_tipo_doc" FOREIGN KEY ("ide_cntdo") REFERENCES "public"."con_tipo_document" ("ide_cntdo") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."cxp_detalle_nota"
ALTER COLUMN "credi_tribu_cpdno" DROP NOT NULL;

ALTER TABLE "public"."cxp_detalle_nota"
ALTER COLUMN "devolucion_cpdno" DROP NOT NULL;

ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "ide_cnccc" int8;


ALTER TABLE "public"."cxp_cabecera_nota"
ADD CONSTRAINT "fk_cxp_cab_nota_asiento" FOREIGN KEY ("ide_cnccc") REFERENCES "public"."con_cab_comp_cont" ("ide_cnccc") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "fact_mig_cccfa" int8;



---=------

ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "orden_compra_cccfa" varchar(25);

ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "dias_credito_cccfa" int4;

ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "correo_cccfa" varchar(100);

CREATE TABLE "public"."gen_camion" (
"placa_gecam" varchar(15) NOT NULL,
"descripcion_gecam" varchar(190),
"identificacion_gecam" varchar(20),
"razon_social_gecam" varchar(150),
PRIMARY KEY ("placa_gecam")
)
WITH (OIDS=FALSE)
;

ALTER TABLE "public"."cxc_guia"
ADD COLUMN "placa_gecam" varchar(15);


ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "dias_credito_srcom" int4;

ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "num_guia_srcom" varchar(20),
ADD COLUMN "orden_compra_srcom" varchar(20);

 
ALTER TABLE "public"."cxc_deta_factura"
ALTER COLUMN "precio_ccdfa" TYPE numeric(12,2),
ALTER COLUMN "total_ccdfa" TYPE numeric(12,2);

ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "sri_ide_srcom" int8;

ALTER TABLE "public"."sri_comprobante"
ADD CONSTRAINT "fk_sri_comp_recursivo" FOREIGN KEY ("sri_ide_srcom") REFERENCES "public"."sri_comprobante" ("ide_srcom") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."cxc_guia"
ADD COLUMN "ide_ccdaf" int4;

ALTER TABLE "public"."cxc_guia"
ADD CONSTRAINT "fk_cxc_guia_datos_sri" FOREIGN KEY ("ide_ccdaf") REFERENCES "public"."cxc_datos_fac" ("ide_ccdaf") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."cxc_guia"
ADD COLUMN "ide_srcom" int8;

ALTER TABLE "public"."cxc_guia"
ADD CONSTRAINT "fk_cxc_guia_electronica" FOREIGN KEY ("ide_srcom") REFERENCES "public"."sri_comprobante" ("ide_srcom") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."gen_camion"
ADD COLUMN "ide_geper" int8;

ALTER TABLE "public"."gen_camion"
ADD CONSTRAINT "fk_persona_camion" FOREIGN KEY ("ide_geper") REFERENCES "public"."gen_persona" ("ide_geper") ON DELETE RESTRICT ON UPDATE RESTRICT;

--ALTER TABLE "public"."sri_comprobante"
--ALTER COLUMN "total_srcom" DROP NOT NULL;

ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "placa_srcom" varchar(15);


ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "fecha_ini_trans_srcom" date,
ADD COLUMN "fecha_fin_trans_srcom" date,
ADD COLUMN "direcion_partida_srcom" varchar(160);


ALTER TABLE "public"."cxp_cabecera_nota"
ADD COLUMN "tarifa_iva_cpcno" numeric(12,2);


--ALTER TABLE "public"."con_porcen_impues"
--ADD COLUMN "fecha_desde_cnpim" date,
--ADD COLUMN "fecha_fin_cnpim" date,
--ADD COLUMN "activo_cnpim" bool;





--ALTER TABLE "public"."cxp_cabece_factur"
--ADD COLUMN "ide_cntic" int4;

--ALTER TABLE "public"."cxp_cabece_factur"
--ADD COLUMN "monto_com_cpcfa" numeric(12,2);



--/////////////////////////////////RETENCIONES ELECTRONICAS
---renta 1
---iva   2

ALTER TABLE "public"."con_cabece_retenc"
ADD COLUMN "ide_ccdaf" int4;
ALTER TABLE "public"."con_cabece_retenc"
ADD CONSTRAINT "fk_datos_retencion" FOREIGN KEY ("ide_ccdaf") REFERENCES "public"."cxc_datos_fac" ("ide_ccdaf") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."con_cabece_retenc"
ADD COLUMN "ide_srcom" int8;

ALTER TABLE "public"."con_cabece_retenc"
ADD CONSTRAINT "fk_con_cabece_retenc_comelec" FOREIGN KEY ("ide_srcom") REFERENCES "public"."sri_comprobante" ("ide_srcom") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."con_impuesto"
ADD COLUMN "codigo_fe_cnimp" varchar(3);

ALTER TABLE "public"."con_cabece_impues"
ADD COLUMN "codigo_fe_retencion_cncim" varchar(3);

ALTER TABLE "public"."con_cabece_retenc"
ALTER COLUMN "numero_cncre" DROP NOT NULL,
ALTER COLUMN "autorizacion_cncre" DROP NOT NULL;

ALTER TABLE "public"."sri_comprobante"
ALTER COLUMN "periodo_fiscal_srcom" TYPE varchar(10);

ALTER TABLE "public"."cxp_cabece_factur"
ADD COLUMN "dias_credito_cpcfa" int4;


ALTER TABLE "public"."cxp_cabece_factur"
ALTER COLUMN "numero_cpcfa" TYPE varchar(17) COLLATE "default";

ALTER TABLE "public"."cxp_detall_factur"
ADD COLUMN "ide_inuni" int4;
ALTER TABLE "public"."cxp_detall_factur"
ADD CONSTRAINT "fk_unidad_deta_cxp" FOREIGN KEY ("ide_inuni") REFERENCES "public"."inv_unidad" ("ide_inuni") ON DELETE RESTRICT ON UPDATE RESTRICT;



---Catalogo de caracteristicas  de articulos
CREATE TABLE inv_caracteristica
(
    ide_incar int NOT NULL,
    nombre_incar  character varying(60),
CONSTRAINT pk_inv_caractristica PRIMARY KEY (ide_incar)
);

---Catalogo de Areas de aplicación  de articulos
CREATE TABLE inv_area
(
    ide_inare int NOT NULL,
    nombre_inare   character varying(60),
CONSTRAINT pk_inv_area PRIMARY KEY (ide_inare)
);


CREATE TABLE inv_articulo_carac
(
    ide_inarc int NOT NULL,
    ide_inarti int,
    ide_incar int,
    ide_inare int,
    detalle_inarc   text,
CONSTRAINT pk_inv_articulo_carac PRIMARY KEY (ide_inarc),
CONSTRAINT fk_inv_arti_reference_inv_caract FOREIGN KEY (ide_incar)
      REFERENCES inv_caracteristica (ide_incar) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_inv_arti_reference_inv_area FOREIGN KEY (ide_inare)
      REFERENCES inv_area (ide_inare) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fk_inv_arti_reference_inv_articulo FOREIGN KEY (ide_inarti)
      REFERENCES inv_articulo (ide_inarti) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



ALTER TABLE "public"."con_cabece_retenc"
ADD COLUMN "correo_cncre" varchar(100);


ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "correo_srcom" varchar(100);



---------------------22222  face











---datos de emision
ALTER TABLE "public"."cxc_datos_fac"
ADD COLUMN "num_inicia_ccdaf" int8;
ALTER TABLE "public"."cxc_datos_fac"
ADD COLUMN "es_electronica_ccdaf" bool;
ALTER TABLE "public"."cxc_datos_fac"
ADD COLUMN "ide_cntdo" int8;
ALTER TABLE "public"."cxc_datos_fac"
ADD COLUMN "activo_ccdaf" bool;
ALTER TABLE "public"."cxc_datos_fac"
ADD CONSTRAINT "fk_cxc_dato_relat_tipo_doc" FOREIGN KEY ("ide_cntdo") REFERENCES "public"."con_tipo_document" ("ide_cntdo") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "public"."cxc_datos_fac"
ALTER COLUMN "autorizacion_ccdaf" DROP NOT NULL;
ALTER TABLE "public"."cxc_datos_fac" RENAME "ide_cntdo" TO "ide_cntdoc";

ALTER TABLE "public"."sri_firma_digital"
ADD COLUMN "ide_empr" int4,
ADD COLUMN "ide_sucu" int4;
---
ALTER TABLE "public"."gen_persona"
ADD COLUMN "limite_credito_geper" decimal(12,2);

--AUMENTA CAMPO CEUNTA CONTABLE PARA LOS GASTOS
--ALTER TABLE "public"."inv_articulo"
--ADD COLUMN "ide_cndpc" int8;
--nombre comercial sucursal
ALTER TABLE "public"."sis_sucursal"
ADD COLUMN "nombre_comercial_sucu" varchar(100);
---................
---////inserar en tes_tipo transaccion  CHEQUE POSFECHADO + - 13 Y 14 
---///CXP TIPO TRANSACCION CAMBIAR 19 A CHEQUE POSFECHADO
---///poner ruc en transportista 
---////borrar transaccion cxc anuladas 


------------------
---//cambia a estado autorizado
--update sri_comprobante set ide_sresc=3 where coddoc_srcom in ('01','06') and ide_sresc=5
---//actualizar secuenciales comp inventario


--select ide_incci from inv_cab_comp_inve where ide_incci in
---(select ide_incci from inv_det_comp_inve where ide_cccfa in (select ide_cccfa from cxc_cabece_factura where ide_ccefa=1));

--select * from inv_det_comp_inve where ide_cccfa in (select ide_cccfa from cxc_cabece_factura where ide_ccefa=1);




---///////////////////////

CREATE TABLE "inv_cab_formula" (
"ide_incfo" int8 NOT NULL,
"ide_inarti" int8,
"fecha_incfo" date,
"num_formula_incfo" varchar(10),
"cantidad_incfo" decimal(12,2),
"concepto_incfo" varchar(200),
"num_referencia_incfo" varchar(15),
"ide_inuni" int8,
"ide_inepi" int8,
"comentarios_incfo" varchar(200),
"total_materia_incfo" decimal(12,2),
"total_servicios_incfo" decimal(12,2),
"total_gastos_incfo" decimal(12,2),
"total_produccion_incfo" decimal(12,2),
"ide_empr" int8,
"ide_sucu" int8,
PRIMARY KEY ("ide_incfo")
);

ALTER TABLE inv_cab_formula
ADD COLUMN "usuario_ingre" varchar(50),
ADD COLUMN "fecha_ingre" date,
ADD COLUMN "hora_ingre" time,
ADD COLUMN "usuario_actua" varchar(50),
ADD COLUMN "fecha_actua" date,
ADD COLUMN "hora_actua" time;
ALTER TABLE "public"."inv_cab_formula"
ADD CONSTRAINT "fk_producto_cab_formul" FOREIGN KEY ("ide_inarti") REFERENCES "public"."inv_articulo" ("ide_inarti") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "public"."inv_cab_formula"
ADD CONSTRAINT "fk_estado_comp_inv" FOREIGN KEY ("ide_inepi") REFERENCES "public"."inv_est_prev_inve" ("ide_inepi") ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE inv_deta_formula (
"ide_indfo" int8 NOT NULL,
"ide_incfo" int8,
"ide_inarti" int8,
"descripcion_indfo" varchar(200),
"ide_inuni" int8,
"cantidad_indfo" decimal(12,2),
"costo_indfo" decimal(12,4),
"total_indfo" decimal(12,2),
"ide_empr" int8,
"ide_sucu" int8,
PRIMARY KEY ("ide_indfo")
)
;
ALTER TABLE inv_deta_formula
ADD COLUMN "usuario_ingre" varchar(50),
ADD COLUMN "fecha_ingre" date,
ADD COLUMN "hora_ingre" time,
ADD COLUMN "usuario_actua" varchar(50),
ADD COLUMN "fecha_actua" date,
ADD COLUMN "hora_actua" time;
ALTER TABLE "public"."inv_deta_formula"
ADD CONSTRAINT "fk_cab_det_formula" FOREIGN KEY ("ide_incfo") REFERENCES "public"."inv_cab_formula" ("ide_incfo") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "public"."inv_deta_formula"
ADD CONSTRAINT "fk_producto_det_formul" FOREIGN KEY ("ide_inarti") REFERENCES "public"."inv_articulo" ("ide_inarti") ON DELETE RESTRICT ON UPDATE RESTRICT;



---//////////////---TESORERIA
CREATE TABLE tes_det_libr_banc (
"ide_tedlb" int8 NOT NULL,
"ide_teclb" int8,
"ide_inarti" int8,
"concepto_tedlb" varchar(150),
"valor_tedlb" decimal(12,2),
"ide_empr" int8,
"ide_sucu" int8,
PRIMARY KEY ("ide_tedlb")
)
;
ALTER TABLE tes_det_libr_banc
ADD COLUMN "usuario_ingre" varchar(50),
ADD COLUMN "fecha_ingre" date,
ADD COLUMN "hora_ingre" time,
ADD COLUMN "usuario_actua" varchar(50),
ADD COLUMN "fecha_actua" date,
ADD COLUMN "hora_actua" time;

ALTER TABLE "public"."tes_det_libr_banc"
ADD CONSTRAINT "fk_cab_lib_det" FOREIGN KEY ("ide_teclb") REFERENCES "public"."tes_cab_libr_banc" ("ide_teclb") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "public"."tes_det_libr_banc"
ADD CONSTRAINT "fk_gasto_lib_det" FOREIGN KEY ("ide_inarti") REFERENCES "public"."inv_articulo" ("ide_inarti") ON DELETE RESTRICT ON UPDATE RESTRICT;

--//////////// CXP_TIPO_TRANSACCION
--19	0	0	PAGO CHEQUE POSFECHADO	-1

--///tes_cab_libr_banc  aumenta realcción recursiva
ALTER TABLE "public"."tes_cab_libr_banc"
ADD COLUMN "tes_ide_teclb" int8;

ALTER TABLE "public"."tes_cab_libr_banc"
ADD CONSTRAINT "fk_tes_cab_recursiva" FOREIGN KEY ("tes_ide_teclb") REFERENCES "public"."tes_cab_libr_banc" ("ide_teclb") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."tes_cab_libr_banc"
ADD COLUMN "tes_ide_teclb1" int8;

ALTER TABLE "public"."tes_cab_libr_banc"
ADD COLUMN "depositado_teclb" boolean;
ALTER TABLE "public"."tes_cab_libr_banc"
ALTER COLUMN "depositado_teclb" SET DEFAULT false;

ALTER TABLE "public"."tes_cab_libr_banc"
ADD COLUMN "devuelto_teclb" boolean;
ALTER TABLE "public"."tes_cab_libr_banc"
ALTER COLUMN "devuelto_teclb" SET DEFAULT false;


update tes_cab_libr_banc set  depositado_teclb=false,devuelto_teclb=false;

ALTER TABLE "public"."tes_cab_libr_banc"
ADD CONSTRAINT "fk_tes_cab_recursiva1" FOREIGN KEY ("tes_ide_teclb1") REFERENCES "public"."tes_cab_libr_banc" ("ide_teclb") ON DELETE RESTRICT ON UPDATE RESTRICT;


--======tes_cab_libr_banc CREAR TIPO TRANSACCION CHEQUE DEVUELTO
--15	0	0	CHEQUE DEVUELTO	-1	CHD
--16	0	0	COMISION CH DEVUELTO	-1	CCHD

--++++++CONFIGURAR PARAMETRO p_con_beneficiario_empresa

--====MODIFICAR cxc_tipo_transaccion  
--17	0	0	COMISION CH DEVUELTO	-1



CREATE TABLE inv_conversion_unidad (
"ide_incon" int8 NOT NULL,
"ide_inarti" int8,
"ide_inuni" int8,
"cantidad_incon" decimal(12,2),
"inv_ide_inuni" int8,
"valor_incon" decimal(12,2),
"observacion" varchar(150),
"ide_empr" int8,
"ide_sucu" int8,
PRIMARY KEY ("ide_incon")
);

ALTER TABLE "public"."inv_conversion_unidad"
ADD CONSTRAINT "fk_conv_articulo" FOREIGN KEY ("ide_inarti") REFERENCES "public"."inv_articulo" ("ide_inarti") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."inv_conversion_unidad"
ADD CONSTRAINT "fk_uni_articulo" FOREIGN KEY ("ide_inuni") REFERENCES "public"."inv_unidad" ("ide_inuni") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."inv_conversion_unidad"
ADD CONSTRAINT "fk_uni_articulo_valor" FOREIGN KEY ("inv_ide_inuni") REFERENCES "public"."inv_unidad" ("ide_inuni") ON DELETE RESTRICT ON UPDATE RESTRICT;


--///////////////////////////////////////////////////////////////////////////////
--///////ORDEN DE PRODUCCIÓN

CREATE TABLE "inv_orden_prod" (
"ide_inorp" int8 NOT NULL,
"ide_incfo" int8,
"ide_incci" int8,
"inv_ide_incci" int8,
"num_orden_inorp" varchar(10),
"fecha_entrega_inorp" date,
"concepto_inorp" varchar(150),
"cantidad_inorp" decimal(12,2),
"num_serie_inorp" varchar(15),
"fecha_caduca_inorp" date,
"ide_inuni" int8,
"total_materia_inorp" decimal(12,2),
"total_servicios_inorp" decimal(12,2),
"total_gastos_inorp" decimal(12,2),
"total_produccion_inorp" decimal(12,2),
"ide_empr" int8,
"ide_sucu" int8,
PRIMARY KEY ("ide_inorp")
);

ALTER TABLE inv_orden_prod
ADD COLUMN "usuario_ingre" varchar(50),
ADD COLUMN "fecha_ingre" date,
ADD COLUMN "hora_ingre" time,
ADD COLUMN "usuario_actua" varchar(50),
ADD COLUMN "fecha_actua" date,
ADD COLUMN "hora_actua" time;

ALTER TABLE "public"."inv_orden_prod"
ADD CONSTRAINT "fk_orden_inv_com" FOREIGN KEY ("ide_incci") REFERENCES "public"."inv_cab_comp_inve" ("ide_incci") ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE "public"."inv_orden_prod"
ADD CONSTRAINT "fk_orden__formula_inv_com" FOREIGN KEY ("inv_ide_incci") REFERENCES "public"."inv_cab_comp_inve" ("ide_incci") ON DELETE RESTRICT ON UPDATE RESTRICT;





--/////MODIFICAR TRANSACCIONES DE INVENTARIO  inv_tip_tran_inve
--16	0	0	0	f	Orden de Producción
--28	1	0	0	t	Formula de Producción


--///cxp_tipo_transacc  modificar signo NOTA DE CREDITRO

--2	0	0	NOTA DE CREDITO	-1

--//campo banco de cheque
ALTER TABLE "public"."tes_cab_libr_banc"
ADD COLUMN "ide_teban" int8;
--//
ALTER TABLE "public"."tes_cab_libr_banc"
ADD CONSTRAINT "fk_tes_cab_banco_cheque" FOREIGN KEY ("ide_teban") REFERENCES "public"."tes_banco" ("ide_teban") ON DELETE RESTRICT ON UPDATE RESTRICT;

--==configurar  p_con_beneficiario_empresa

--ALTER TABLE cxp_cabece_factur
--ADD COLUMN "usuario_ingre" varchar(50),
--ADD COLUMN "fecha_ingre" date,
--ADD COLUMN "hora_ingre" time,
--ADD COLUMN "usuario_actua" varchar(50),
--ADD COLUMN "fecha_actua" date,
--ADD COLUMN "hora_actua" time;

--ALTER TABLE cxp_detall_factur
--ADD COLUMN "usuario_ingre" varchar(50),
--ADD COLUMN "fecha_ingre" date,
--ADD COLUMN "hora_ingre" time,
--ADD COLUMN "usuario_actua" varchar(50),
--ADD COLUMN "fecha_actua" date,
--ADD COLUMN "hora_actua" time;

--ALTER TABLE cxp_cabece_transa
--ADD COLUMN "usuario_ingre" varchar(50),
--ADD COLUMN "fecha_ingre" date,
--ADD COLUMN "hora_ingre" time,
--ADD COLUMN "usuario_actua" varchar(50),
--ADD COLUMN "fecha_actua" date,
--ADD COLUMN "hora_actua" time;

--ALTER TABLE cxp_detall_transa
--ADD COLUMN "usuario_ingre" varchar(50),
--ADD COLUMN "fecha_ingre" date,
--ADD COLUMN "hora_ingre" time,
--ADD COLUMN "usuario_actua" varchar(50),
--ADD COLUMN "fecha_actua" date,
--ADD COLUMN "hora_actua" time;

--////tabla secueciales transacciones de bancos
CREATE TABLE tes_secuencial_trans (
"ide_tesec" int8 NOT NULL,
"ide_tettb" int8,
"ide_tecba" int8,
"ide_empr" int4,
"ide_sucu" int4,
"secuencial_tesec" int8,
PRIMARY KEY ("ide_tesec")
)
;


--////inv


ALTER TABLE "public"."inv_cab_comp_inve"
ADD COLUMN "referencia_incci" varchar(12);

--/////cantidad 3 decimales
ALTER TABLE "public"."inv_det_comp_inve"
ALTER COLUMN "cantidad_indci" TYPE numeric(12,3),
ALTER COLUMN "cantidad1_indci" TYPE numeric(12,3);


--////31-03-2018 forma de pago 
ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "ide_cndfp1" int4;

ALTER TABLE "public"."gen_persona"
ADD COLUMN "ide_cndfp" int4;

ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "infoadicional1_srcom" varchar(150),
ADD COLUMN "infoadicional2_srcom" varchar(150),
ADD COLUMN "infoadicional3_srcom" varchar(150);

--////29-04-2018
--///Campos guia y transporte
ALTER TABLE "public"."cxc_guia"
ADD COLUMN "gen_ide_geper" int4;

------
----////



ALTER TABLE "public"."sri_firma_digital"
DROP COLUMN "archivo_srfid";


ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "ip_genera_srcom" varchar(20),
ADD COLUMN "subtotal0_srcom" decimal(12,2),
ADD COLUMN "base_grabada_srcom" decimal(12,2),
ADD COLUMN "subtotal_srcom" decimal(12,2),
ADD COLUMN "iva_srcom" decimal(12,2),
ADD COLUMN "descuento_srcom" decimal(12,2),
ADD COLUMN "total_srcom" decimal(12,2),
ADD COLUMN "en_nube_srcom" bool,
ADD COLUMN "ide_geper" int4,
ADD COLUMN "forma_cobro_srcom" varchar(10);

ALTER TABLE "public"."sri_comprobante"
ADD COLUMN "codigo_docu_mod_srcom" varchar(2),
ADD COLUMN "num_doc_mod_srcom" varchar(20),
ADD COLUMN "fecha_emision_mod_srcom" date,
ADD COLUMN "valor_mod_srcom" decimal(12,2),
ADD COLUMN "motivo_srcom" varbit(100);



ALTER TABLE "public"."sri_comprobante"
ALTER COLUMN "claveacceso_srcom" DROP NOT NULL,
ALTER COLUMN "coddoc_srcom" DROP NOT NULL,
ALTER COLUMN "estab_srcom" DROP NOT NULL,
ALTER COLUMN "ptoemi_srcom" DROP NOT NULL,
ALTER COLUMN "secuencial_srcom" DROP NOT NULL,
ALTER COLUMN "fechaemision_srcom" DROP NOT NULL;


---MODIFICAR datos tabla sti_estado_comprobante


ALTER TABLE "public"."sri_comprobante"
ALTER COLUMN "tipoemision_srcom" TYPE varchar(1);

---MODIFICAR datos tabla con_impuesto  y con_cabece_impues  (IVA )


----------02-06-2018

ALTER TABLE "public"."sri_comprobante"
ALTER COLUMN "motivo_srcom" TYPE varchar(100),
ADD COLUMN "ide_ccdaf1" int4;


ALTER TABLE "public"."sri_comprobante"
DROP COLUMN "ide_cccfa",
DROP COLUMN "ide_cncre";


------09-06-2018

ALTER TABLE "public"."cxc_cabece_factura"
ADD COLUMN "descuento_cccfa" decimal(12,2);

ALTER TABLE "public"."cxp_cabece_factur"
ADD COLUMN "ide_cndfp1" int4;


-----------PENSIONES

CREATE TABLE pen_tmp_lista_fact (
"ide_petlf" int4 NOT NULL,
"codigo_alumno_petlf" varchar(15),
"nombre_alumno_petlf" varchar(200),
"paralelo_petlf" varchar(180),
"subtotal_petlf" decimal(12,2),
"rebaja_petlf" decimal(12,2),
"total_petlf" decimal(12,2),
"cod_factura_petlf" varchar(20),
"fecha_petlf" date,
"concepto_petlf" varchar(200),
"representante_petlf" varchar(200),
"cedula_petlf" varchar(20),
"periodo_lectivo_petlf" varchar(30),
"correo_petlf" varchar(200),
"direccion_petlf" varchar(250),
"telefono_petlf" varchar(80),
"cedula_alumno_petlf" varchar(20),
PRIMARY KEY ("ide_petlf")
);

------
ALTER TABLE "public"."gen_persona"
ALTER COLUMN "correo_geper" TYPE varchar(200) COLLATE "default";


CREATE TABLE sri_info_adicional (
"ide_srina" int4 NOT NULL,
"nombre_srina" varchar(150),
"valor_srina" varchar(200),
"ide_srcom" int4,
"ide_cccfa" int4,
PRIMARY KEY ("ide_srina"),
CONSTRAINT "fk_comprobante_info" FOREIGN KEY ("ide_srcom") REFERENCES "public"."sri_comprobante" ("ide_srcom"),
CONSTRAINT "fk_comprobante_cab_fac" FOREIGN KEY ("ide_cccfa") REFERENCES "public"."cxc_cabece_factura" ("ide_cccfa")
)
;

--23-06-2016
ALTER TABLE "public"."sis_empresa"
ADD COLUMN "fines_lucro_empr" bool;


---
ALTER TABLE "public"."gen_persona"
ADD COLUMN "rep_ide_geper" int4;

ALTER TABLE "public"."gen_persona"
ADD CONSTRAINT "fk_representante" FOREIGN KEY ("rep_ide_geper") REFERENCES "public"."gen_persona" ("ide_geper") ON DELETE RESTRICT ON UPDATE RESTRICT;

--descuento deta fac
ALTER TABLE "public"."cxc_deta_factura"
ADD COLUMN "descuento_ccdfa" decimal(12,2);

--indice orden compra 
CREATE INDEX "in_orden_comp_ref" ON "public"."cxc_cabece_factura" USING btree ("orden_compra_cccfa");

--descuento nota credito
ALTER TABLE "public"."cxp_detalle_nota"
ADD COLUMN "descuento_cpdno" decimal(12,2);

---04-07-2018
--decimales en facturas electronicas
ALTER TABLE "public"."sri_emisor"
ADD COLUMN "cant_decim_sremi" integer,
ADD COLUMN "preciou_decim_sremi" integer;
ALTER TABLE "public"."sri_emisor"
ADD CONSTRAINT "fk_emisor_sucursal" FOREIGN KEY ("ide_sucu") REFERENCES "public"."sis_sucursal" ("ide_sucu") ON DELETE RESTRICT ON UPDATE RESTRICT;
---por defecto 2 decimales 
update sri_emisor set cant_decim_sremi=2 , preciou_decim_sremi=4 
---
ALTER TABLE "public"."cxc_deta_factura"
ALTER COLUMN "cantidad_ccdfa" TYPE numeric(12,2),
ALTER COLUMN "precio_ccdfa" TYPE numeric(12,4);
---
ALTER TABLE "public"."inv_det_comp_inve"
ALTER COLUMN "cantidad_indci" TYPE numeric(12,2),
ALTER COLUMN "cantidad1_indci" TYPE numeric(12,2),
ALTER COLUMN "precio_indci" TYPE numeric(12,4);

ALTER TABLE "public"."cxp_detall_factur"
ALTER COLUMN "cantidad_cpdfa" TYPE numeric(12,2),
ALTER COLUMN "precio_cpdfa" TYPE numeric(12,4);
---cambiar a entero columnas decimales
ALTER TABLE "public"."sri_emisor"
ALTER COLUMN "cant_decim_sremi" TYPE int2,
ALTER COLUMN "preciou_decim_sremi" TYPE int2;










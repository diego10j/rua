-----TABLA DE CONFIGURACION DE LAS DIRECCIONES Y CREDENCIALES DE LOS WEB SERVICES
CREATE TABLE public.gen_webservice
(
   ide_gewes smallint NOT NULL, 
   nombre_gewes character varying(150), 
   wsdl_gewes text, 
   usuario_gewes character varying(100), 
   password_gewes character varying(80), 
   tiempomax_gewes integer,
   ide_empr                        bigint,       
   CONSTRAINT "PK_GEN_WEBSERVICE" PRIMARY KEY (ide_gewes)
) 
WITH (
  OIDS = FALSE
)
;
COMMENT ON TABLE public.gen_webservice
  IS 'TABLA DE CONFIGURACION DE DIRECCIONES WSDL DE WEB SERVICES ';

INSERT INTO gen_webservice VALUES(1,'Recepcion Comprobantes SRI','https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl',NULL,NULL,50000,0);
INSERT INTO gen_webservice VALUES(2,'Autorizacion Comprobantes SRI','https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl',NULL,NULL,50000,0);

-----AUMENTA COLUMNAS A LA TABLA DE SIS_EMPRESA
ALTER TABLE sis_empresa
  ADD COLUMN contribuyenteespecial_empr character varying(20);
ALTER TABLE sis_empresa
  ADD COLUMN obligadocontabilidad_empr character varying(20);
COMMENT ON COLUMN sis_empresa.contribuyenteespecial_empr IS 'NUMERO DE RESOLUCION DE CONTRIBUYENTE ESPECIAL';

-----CREA TABLA DE CLAVES DE CONTINGENCIA
CREATE TABLE public.sri_claves_contingencia 
( 
    ide_srclc         bigint NOT NULL,
    clavecont_srclc   character varying(80), 
    disponible_srclc  boolean,
    ide_empr                        bigint,    
   CONSTRAINT "PK_SRI_CLAVES_CONTIGENCIA" PRIMARY KEY (ide_srclc)
)
WITH (
  OIDS = FALSE
)
;


----CREA TABLA ESTADO COMPROBANTE ELECTRONICO
CREATE TABLE public.sri_estado_comprobante ( 
	ide_sresc	smallint NOT NULL, 
	nombre_sresc	varchar(80) NULL, 
        CONSTRAINT "PK_SRI_ESTADO_COMPROBANTE" PRIMARY KEY (ide_sresc)
)
WITH (
  OIDS = FALSE
)
;
INSERT INTO public.sri_estado_comprobante VALUES(1, 'RECIBIDA');
INSERT INTO public.sri_estado_comprobante VALUES(2, 'DEVUELTA');
INSERT INTO public.sri_estado_comprobante VALUES(3, 'AUTORIZADO');
INSERT INTO public.sri_estado_comprobante VALUES(4, 'RECHAZADO');
INSERT INTO public.sri_estado_comprobante VALUES(5, 'CONTINGENCIA');
INSERT INTO public.sri_estado_comprobante VALUES(6, 'NO AUTORIZADO');


-----TABLA DE FIRMAS ELECRONICAS
CREATE TABLE public.sri_firma_digital ( 
	ide_srfid                       smallint NOT NULL, 
	ruta_srfid                      text ,
        archivo_srfid                     bytea ,
	password_srfid                  character varying(80) NOT NULL,
	fecha_ingreso_srfid       	date NOT NULL,
	fecha_caduca_srfid              date ,
	nombre_representante_srfid	character varying(120),
	correo_representante_srfid	character varying(100),
	disponible_srfid                boolean,
        ide_empr                        bigint,
        CONSTRAINT "PK_SRI_FIRMA_DIGITAL" PRIMARY KEY (ide_srfid)
)
WITH (
  OIDS = FALSE
)
;
INSERT INTO public.sri_firma_digital VALUES(1, 'D:\edgar_mesias_tapia_coral.p12', null,'1Guada92',  '2015-01-01', '2016-10-31', 'EDGAR TAPIA', 'edgartapia@bnf.fin.ec', 'true',0);


------TABLA COMPROBANTES ELECTRONICOS
create table public.sri_comprobante ( 
	ide_srcom                       bigint NOT NULL,		
	ide_srclc                       bigint,  --clave contingencia
	ide_srfid                       smallint,--firma digital 
	ide_sresc                       smallint ,--estado comprobante
	ide_cntdo                       bigint , --tipo documento  con_tipo_document
        ide_cccfa                       bigint , -- factura de venta
        ide_cccnc                       bigint , -- Nota Credito
        ide_cccnd                       bigint , -- Nota Debito
        ide_cncre                       bigint , -- Retencion 
        ide_empr                        bigint,
        ide_sucu                        bigint,   
        ide_usua                        bigint, 
        identificacion_srcom            character varying(30) not null,
	tipoemision_srcom               smallint,      --1=normal; 2=contogencia   
	claveacceso_srcom               character varying(50) not null,
	coddoc_srcom              	character varying(2) not null,
	estab_srcom                     character varying(3) not null,
	ptoemi_srcom                    character varying(3) not null,
	secuencial_srcom             	character varying(9) not null,
	fechaemision_srcom           	date not null,
        autorizacion_srcom              character varying(100),
        fechaautoriza_srcom           	timestamp ,	
        CONSTRAINT "PK_SRI_COMPROBANTE" PRIMARY KEY (ide_srcom),
        CONSTRAINT fk_con_retencion_comprobante FOREIGN KEY (ide_cncre)
        REFERENCES con_cabece_retenc (ide_cncre) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_cxc_factura_comprobante FOREIGN KEY (ide_cccfa)
        REFERENCES cxc_cabece_factura (ide_cccfa) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_sri_estado_comprobante FOREIGN KEY (ide_sresc)
        REFERENCES sri_estado_comprobante (ide_sresc) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_sri_firma_comprobante FOREIGN KEY (ide_srfid)
        REFERENCES sri_firma_digital (ide_srfid) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_sri_clave_comprobante FOREIGN KEY (ide_srclc)
        REFERENCES sri_claves_contingencia (ide_srclc) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_con_documento_comprobante FOREIGN KEY (ide_cntdo)
        REFERENCES con_tipo_document (ide_cntdo) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_sri_comprobante_sis_sucu FOREIGN KEY (ide_sucu)
        REFERENCES sis_sucursal (ide_sucu) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_sri_comprobante_sis_usua FOREIGN KEY (ide_usua)
        REFERENCES sis_usuario (ide_usua) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_sri_comprobante_sis_empr FOREIGN KEY (ide_empr)
        REFERENCES sis_empresa (ide_empr) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT
)
WITH (
  OIDS = FALSE
)
;

-----TABLA ENVIOS XML SRI
create table public.sri_xml_comprobante ( 
        ide_srxmc               BIGSERIAL NOT NULL,
        ide_srcom               bigint ,
        ide_sresc               smallint ,--estado comprobante
        fecha_hora_srxmc        timestamp,        
        xml_srxmc               text,
        msg_recepcion_srxmc     text,
        msg_autoriza_srxmc      text,
        CONSTRAINT "PK_SRI_XML_COMPROBANTE" PRIMARY KEY (ide_srxmc),
        CONSTRAINT fk_sri_estado_comprobante FOREIGN KEY (ide_sresc)
        REFERENCES sri_estado_comprobante (ide_sresc) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT,
        CONSTRAINT fk_sri_xml_comprobante FOREIGN KEY (ide_srcom)
        REFERENCES sri_comprobante (ide_srcom) MATCH SIMPLE
        ON UPDATE RESTRICT ON DELETE RESTRICT
)
WITH (
  OIDS = FALSE
)
;

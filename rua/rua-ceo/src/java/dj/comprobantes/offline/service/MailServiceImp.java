/*
 *********************************************************************
 Objetivo: Servicio que implementa interface MailService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.ParametrosSistemaEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author diego.jacome
 */
//@Stateless
@Singleton
public class MailServiceImp implements MailService {

    private List<MimeMessage> listaMensajes;
    private Session session = null;
    @EJB
    private ArchivoService archivoService;

    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    Properties properties;

    @Override
    public String agregarCorreo(Comprobante comprobanteMail, String correo) {

        configurarMail();

        if (comprobanteMail == null) {
            return null;
        }
        if (correo == null) {
            if (comprobanteMail.getCliente().getCorreo() == null) {
                return null;
            }
            correo = comprobanteMail.getCliente().getCorreo();
        }

        if (utilitario.isCorreoValido(correo) == false) {
            return null;
        }

        String str = "";
        if (listaMensajes == null) {
            listaMensajes = new ArrayList<>();
        }
        try {
            BodyPart texto = new MimeBodyPart();
            String stb_mensaje = "<html><head><title></title></head><body style='font-family: sans-serif'><div align=\"justify\">"
                    //+ "<span style='color:#939598;font-size:44px;'> <img src='http://www.produquimic.com.ec/images/logo_mail.gif'/> </span>"
                    + "<div align='right' style='background-color: rgb(196, 13, 28) !important; background-position: 0% 0%; background-repeat: repeat; background-attachment: scroll; background-image: none; background-size: auto; background-origin: padding-box; background-clip: border-box; width: 98%; color: white !important; padding-right: 35px; height: 33px; font-size: 20px;'>  COMPROBANTES ELECTRONICOS</div>"
                    + "<div style='padding-left:15px;font-size:14px;'>"
                    + "<p>Estimado(a) " + comprobanteMail.getCliente().getNombreCliente().toUpperCase() + "</p>"
                    + "<p>" + "Usted a recibido un Comprobante Electr&oacute;nico :</p>"
                    + "<p> TIPO: " + TipoComprobanteEnum.getDescripcion(comprobanteMail.getCoddoc()) + " </p>"
                    + "<p>NÚMERO : " + comprobanteMail.getEstab() + "-" + comprobanteMail.getPtoemi() + "-" + comprobanteMail.getSecuencial() + "</p>"
                    + "<p>CLAVE DE ACCESO : " + comprobanteMail.getClaveacceso() + "</p>"
                    + "<br/>"
                    + " <p>Gracias por preferirnos.</p> \n"
                    + " <br/>\n"
                    + " </div>\n"
                    + " <div align=\"left\" style=\"display: block; background-color: rgb(196, 13, 28) !important; background-position: 0% 0%; background-repeat: repeat; background-attachment: scroll; background-image: none; background-size: auto; background-origin: padding-box; background-clip: border-box; width: 100%; color: white !important; padding: 7px 15px; font-size: 13px;\">\n"
                    + " NOTA. Esta es una notificación automática, por favor no responder este correo electrónico. Cualquier duda o inconveniente favor contactarnos al correo <strong><a style=\"color: white\" href=\"mailto:inspectorial@salesianos.org.ec?Subject=Ayuda\" target=\"_top\">inspectorial@salesianos.org.ec</a></strong>    \n"
                    + " </div>\n"
                    + "</div>\n"
                    + "</div>\n"
                    + "</div></body></html>";
            texto.setContent(stb_mensaje, "text/html");
            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();

            multiParte.addBodyPart(texto);

            byte[] pdf = archivoService.getPdf(comprobanteMail);
            if (pdf != null) {
                File archivo = File.createTempFile(comprobanteMail.getClaveacceso(), ".pdf", null);
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(pdf);
                fos.close();
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo)));
                adjunto.setFileName(comprobanteMail.getClaveacceso() + ".pdf");
                multiParte.addBodyPart(adjunto);
            }
            byte[] xml = archivoService.getXml(comprobanteMail);
            if (xml != null) {
                File archivo = File.createTempFile(comprobanteMail.getClaveacceso(), ".xml", null);
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(xml);
                fos.close();
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo)));
                adjunto.setFileName(comprobanteMail.getClaveacceso() + ".xml");
                multiParte.addBodyPart(adjunto);
            }

            // Se compone el correo, dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ParametrosSistemaEnum.MAIL_GENERIC.getCodigo()));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(correo));
            message.setSubject("COMPROBANTE ELECTRONICO");
            message.setContent(multiParte);
            listaMensajes.add(message);
        } catch (MessagingException | GenericException | IOException e) {
            e.printStackTrace();
        }

        return str;
    }

    private void configurarMail() {
        if (session == null) {
            ///SSH
            try {
                // Realiza la conexion y valida credenciales smtp
////                properties = new Properties();
////                properties.put("mail.smtp.host", HOSTNAME);
////                // Setting STARTTLS_PORT
////                properties.put("mail.smtp.port", STARTTLS_PORT);
////                // AUTH enabled
////                properties.put("mail.smtp.auth", AUTH);
////                // STARTTLS enabled
////                properties.put("mail.smtp.starttls.enable", STARTTLS);
////                properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

                properties = new Properties();
                properties.put("mail.smtp.host", ParametrosSistemaEnum.MAIL_SMTP_HOST.getCodigo());
                properties.put("mail.smtp.port", ParametrosSistemaEnum.MAIL_SMTP_PORT.getCodigo());
                properties.put("mail.smtp.auth", "false");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.ssl.enable", "false");
                properties.put("mail.smtp.user", "comprobantessalesianos");
                properties.put("mail.smtp.password", ParametrosSistemaEnum.MAIL_PASSWORD.getCodigo());

                Authenticator auth = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ParametrosSistemaEnum.MAIL_GENERIC.getCodigo(), ParametrosSistemaEnum.MAIL_PASSWORD.getCodigo());
                    }
                };
                //session.setDebug(true);
                session = Session.getInstance(properties, auth);
            } catch (Exception e) {
                System.out.println("Error al Conectarse al Servidor de Correo Electrónico SMTP : " + e.getMessage());
            }

        }
    }

    @Override
    public String enviarTodos() {
        String str = "";
        if (listaMensajes != null) {
            try {
//                configurarMail();
//                Transport t = session.getTransport("smtp");
//                t.connect(ParametrosSistemaEnum.MAIL_GENERIC.getCodigo(), ParametrosSistemaEnum.MAIL_PASSWORD.getCodigo());
                for (Message actual : listaMensajes) {
//                    if (t.isConnected() == false) {
//                        t.connect(ParametrosSistemaEnum.MAIL_GENERIC.getCodigo(), ParametrosSistemaEnum.MAIL_PASSWORD.getCodigo());
//                    } else {
//                        t.sendMessage(actual, actual.getRecipients(Message.RecipientType.TO));
//                    }
                    Transport.send(actual);
                }
//                t.close();
            } catch (Exception e) {
                e.printStackTrace();
                str += "No se puede enviar el correo: " + e.getMessage();
            }
            listaMensajes.clear();
        }
        session = null;
        return str;
    }

    @Override
    public String enviar(Comprobante comprobante, String correo) {
        configurarMail();
        String msj = agregarCorreo(comprobante, correo);
        if (msj.isEmpty()) {
            msj = enviarTodos();
        }
        return msj;
    }

}

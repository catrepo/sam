/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.aplicacion.TablaGenerica;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import paq_utilitario.ejb.InfoCorreo;

/**
 *
 * @author KEJA
 */
public class SendMail {

    private InfoCorreo correo = new InfoCorreo();
    Session session;
    private String correoEnvia;
    private String claveCorreo;
    private String cuerpoCorreo;

    public void envio(String tabla, String documento, String usuario, String fecha, String tipo, String envio, String cod, String observacion,
            String cedula, String fono, String celular, String dir, String mail) {
        if (cod.equals("0")) {
            InfCorreo(tabla);
            envioReclamo(documento, usuario, fecha, tipo, envio);
        } else {
            InfCorreo(tabla);
            envioDenuncia(documento, usuario, fecha, tipo, envio, cedula, fono, celular, dir, mail, observacion);
        }
    }

    public void InfCorreo(String aplicacion) {
        TablaGenerica tabDato = correo.getVerificaSolicitud(aplicacion);
        if (!tabDato.isEmpty()) {
            // El correo de envío
            correoEnvia = tabDato.getValor("CORREO_CORR");
            claveCorreo = tabDato.getValor("PASS_CORR");
            cuerpoCorreo = tabDato.getValor("NOTA_CORR");

            // La configuración para enviar correo
            Properties properties = new Properties();
            properties.put("mail.smtp.host", tabDato.getValor("SMTP_HOST"));
            properties.put("mail.smtp.starttls.enable", tabDato.getValor("SMTP_STARTTLS"));
            properties.put("mail.smtp.port", tabDato.getValor("SMTP_PORT"));
            properties.put("mail.smtp.auth", tabDato.getValor("SMTP_AUTH"));
            properties.put("mail.user", correoEnvia);
            properties.put("mail.password", claveCorreo);

            // Obtener la sesion
            session = Session.getInstance(properties, null);
        } else {
            System.err.println("<<->> Credenciales de Correo Invalidas <<->>");
        }
    }

    public void envioReclamo(String documento, String usuario, String fecha, String tipo, String envio) {
        try {
            // Crear el cuerpo del mensaje
            MimeMessage mimeMessage = new MimeMessage(session);
            // Agregar quien envía el correo
            mimeMessage.setFrom(new InternetAddress(correoEnvia, correoEnvia));
            // Los destinatarios
            InternetAddress[] internetAddresses = {
                new InternetAddress(envio)
            };
            // Agregar los destinatarios al mensaje
            mimeMessage.setRecipients(Message.RecipientType.TO,
                    internetAddresses);
            // Agregar el asunto al correo
            mimeMessage.setSubject("Atención: " + tipo + ": " + documento.toString() + " - Asignado a su Unidad");
            // Creo la parte del mensaje
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            String msj = ("Estimado usuario (a),"
                    + "\n"
                    + "Se le informa que el " + tipo + ": " + documento.toString() + ", Solicitado por: " + usuario.toString() + ", recibido el " + fecha.toString() + ", ha sido asignado a su unidad"
                    + "\n" + cuerpoCorreo);
            mimeBodyPart.setText(msj);
            // Crear el multipart para agregar la parte del mensaje anterior
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            // Agregar el multipart al cuerpo del mensaje
            mimeMessage.setContent(multipart);
            // Enviar el mensaje
            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia, claveCorreo);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            System.out.println("Reclamo enviado; Por correo " + documento.toString());
        } catch (Exception e) {
        }
    }

    public void envioDenuncia(String documento, String usuario, String fecha, String tipo, String envio, String cedula, String fono, String celular, String dir, String mail, String observacion) {
        try {
            // Crear el cuerpo del mensaje
            MimeMessage mimeMessage = new MimeMessage(session);
            // Agregar quien envía el correo
            mimeMessage.setFrom(new InternetAddress(correoEnvia, correoEnvia));
            // Los destinatarios
            InternetAddress[] internetAddresses = {
                new InternetAddress(envio)
            };
            // Agregar los destinatarios al mensaje
            mimeMessage.setRecipients(Message.RecipientType.TO,
                    internetAddresses);
            // Agregar el asunto al correo
            mimeMessage.setSubject("Atención: " + tipo + ": " + documento.toString() + " - Asignado a su Unidad");
            // Creo la parte del mensaje
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            String msj = ("Estimado usuario (a),"
                    + "\n"
                    + "\n"
                    + "Se le informa que el " + tipo + ": " + documento.toString() + ", Solicitado por: " + usuario.toString() + ", recibido el " + fecha.toString() + ", ha sido asignado a su unidad"
                    + "\n"
                    + "\n"
                    + "Datos de Ciudadano"
                    + "\n"
                    + "\n"
                    + "Cedula : " + cedula.toString() + "\n"
                    + "Nombre : " + usuario.toString() + "\n"
                    + "Dirección : " + dir.toString() + "\n"
                    + "Teléfono : " + fono.toString() + "\n"
                    + "Celular : " + celular.toString() + "\n"
                    + "Correo : " + mail.toString() + "\n"
                    + "\n"
                    + "Reclamo\n"
                    + "" + observacion.toString() + "\n"
                    + "\n" + cuerpoCorreo
                    + "\n"
                    + "por favor responder A, Dennise Estrella, correo: dennise.estrella@ruminahui.gob.ec");
            mimeBodyPart.setText(msj);
            // Crear el multipart para agregar la parte del mensaje anterior
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            // Agregar el multipart al cuerpo del mensaje
            mimeMessage.setContent(multipart);
            // Enviar el mensaje
            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia, claveCorreo);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            System.out.println("Correo enviado");
        } catch (Exception e) {
        }
    }
}

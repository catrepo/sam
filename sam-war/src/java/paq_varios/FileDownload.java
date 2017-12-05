/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.aplicacion.Framework;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;

/**
 *
 * @author p-chumana
 */
public class FileDownload extends Framework {

    public void downloadFile(File file) {
        String fileName = file.getName();
        String contentType = "application/pdf";

        //Con esto obtenemos el contexto
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        //Con esto obtenemos la response
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        try {
            FileInputStream filedownload = new FileInputStream(file);
            ServletOutputStream out = response.getOutputStream();
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            response.setContentLength((int) filedownload.available());
            byte[] buffer = new byte[4096];
            int i = 0;
            while ((i = filedownload.read(buffer)) != -1) {
                out.write(i);
            }
            out.flush();
            out.close();
            filedownload.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
        }
    }

//    public void muestraArchivo() {
//        File file = new File("", "");
//
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
//        BufferedInputStream input = null;
//        BufferedOutputStream output = null;
//        
//        try {
//            input = new BufferedInputStream(new FileInputStream(file),null);
//            
//        } catch (Exception e) {
//        }
//
//    }

    public void descArchivo() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String fileName = "Doc1.docx";
            String filePath = "D:\\PROYECTOS";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            FileInputStream filedownload = new FileInputStream(filePath + "\\" + fileName);
            int i = 0;
            while ((i = filedownload.read()) != -1) {
                out.write(i);
            }
            out.flush();
            filedownload.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearArchivo(String path) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        StreamedContent content;
        InputStream stream = null;
        try {
            if (path.startsWith("/")) {
                stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path);
            } else {
                stream = new FileInputStream(path);
            }
        } catch (Exception e) {
            crearError("No se puede generar el archivo path: " + path, "crearArchivo()", e);
        }
        if (stream == null) {
            return;
        }
        content = new DefaultStreamedContent(stream);
        if (content == null) {
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        String contentDispositionValue = "attachment";
        try {
            externalContext.setResponseContentType(content.getContentType());
            externalContext.setResponseHeader("Content-Disposition", contentDispositionValue + ";filename=\"" + path.substring(path.lastIndexOf("/") + 1) + "\"");
            externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
            byte[] buffer = new byte[2048];
            int length;
            InputStream inputStream = content.getStream();
            OutputStream outputStream = externalContext.getResponseOutputStream();
            while ((length = (inputStream.read(buffer))) != -1) {
                outputStream.write(buffer, 0, length);
            }
            externalContext.setResponseStatus(200);
            externalContext.responseFlushBuffer();
            content.getStream().close();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (Exception e) {
            crearError("No se puede descargar :  path: " + path, "crearArchivo()", e);
        }
    }
}

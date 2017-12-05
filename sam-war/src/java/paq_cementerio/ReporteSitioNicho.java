/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author l-suntaxi
 */
package paq_cementerio;

import paq_bienes.*;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import paq_beans.Archivo;
import sistema.aplicacion.Pantalla;
import paq_varios.FileDownload;
import persistencia.Conexion;

public class ReporteSitioNicho extends Pantalla {

    //Variable conexion
    //Declaracion de combos
    private Combo cmbTipoLugar = new Combo();
    private Combo cmbDisOc = new Combo();
    //Creacion Calendarios
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Panel panOpcion = new Panel();
    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    private Dialogo diaDialogo = new Dialogo();
    private Grid gridD = new Grid();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    Archivo file = new Archivo();

    public ReporteSitioNicho() {
//        Imagen quinde = new Imagen();
//        quinde.setValue("imagenes/logoactual.png");
//        agregarComponente(quinde);

        bar_botones.quitarBotonsNavegacion();
//        bar_botones.quitarBotonGuardar();
//        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        /*
         * Cadena de conexión base de datos
         */
//        CAYMAN.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcayman"));
//        CAYMAN.NOMBRE_MARCA_BASE = "sqlserver";

        /*
         * Captura de usuario que se logea
         */
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        /*
         * opción para impresión
         */
        /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("REPORTE CATASTRO CEMENTERIO");
        panOpcion.setId("panOpcion");
        panOpcion.setStyle("font-size:12px;color:black;text-align:left;");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SELECCIONE PARAMETROS DE REPORTE");
        Imagen quinde = new Imagen();
        quinde.setStyle("text-align:center;position:absolute;top:190px;left:500px;");
        quinde.setValue("imagenes/logo.png");
        panOpcion.getChildren().add(quinde);

        /*
         * COMBO QUE MUESTRAS LOS AÑOS DE 
         */
        Grid griCuerpo = new Grid();
        griCuerpo.setColumns(2);
        griCuerpo.getChildren().add(new Etiqueta("TIPO DE LUGAR: "));
        cmbTipoLugar.setId("cmbTipoLugar");
        cmbTipoLugar.setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR ORDER BY DETALLE_LUGAR");
        cmbTipoLugar.eliminarVacio();
        griCuerpo.getChildren().add(cmbTipoLugar);

        griCuerpo.getChildren().add(new Etiqueta("ESTADO: "));
        cmbDisOc.setId("cmbDisOc");

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Disponible"
        };
        Object fila2[] = {
            "1", "Ocupado"
        };
        list.add(fila1);;
        list.add(fila2);;
        cmbDisOc.setCombo(list);
        cmbDisOc.eliminarVacio();
        griCuerpo.getChildren().add(cmbDisOc);

        griCuerpo.getChildren().add(new Etiqueta("FECHA INICIAL: "));
        fechaInicio.setId("fechaInicio");
        fechaInicio.setFechaActual();
        fechaInicio.setTipoBoton(true);
        griCuerpo.getChildren().add(fechaInicio);

        griCuerpo.getChildren().add(new Etiqueta("FECHA FINAL: "));
        fechaFin.setId("fechaFin");
        fechaFin.setFechaActual();
        fechaFin.setTipoBoton(true);
        griCuerpo.getChildren().add(fechaFin);
        panOpcion.getChildren().add(griCuerpo);

        Boton botPrint = new Boton();
        botPrint.setId("botPrint");
        botPrint.setValue("IMPRIMIR");
        botPrint.setIcon("ui-icon-print");
        botPrint.setMetodo("abrirListaReportes");

        tabp2.getChildren().add(panOpcion);
        tabp2.getChildren().add(botPrint);
        agregarComponente(tabp2);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("PARA PAGO POR LIQUIDACIÓN"); //titulo
        diaDialogo.setWidth("50%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("40%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        gridD.setColumns(4);
        agregarComponente(diaDialogo);

        Boton botPrin = new Boton();
        botPrin.setId("botPrin");
        botPrin.setValue("IMPRIMIR");
        botPrin.setIcon("ui-icon-print");
        botPrin.setMetodo("desArchivo");
        bar_botones.agregarBoton(botPrin);

    }

    public void desArchivo() {
        FileDownload file = new FileDownload();
        file.downloadFile(new File("D:\\PROYECTOS\\simur\\web\\formulario\\prueba.pdf"));
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "ACTIVOS POR CUENTAS":
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "ACTIVOS POR CUENTAS":
//                String mes,
//                 mes1;
//                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
//                    mes = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
//                } else {
//                    mes = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
//                }
//                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
//                    mes1 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
//                } else {
//                    mes1 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
//                }
//                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(String.valueOf(cmbTipoLugar.getValue() + "").substring(2, 4))) + "01"));
//                p_parametros.put("inicio", Integer.parseInt("1" + Integer.parseInt(String.valueOf(cmbTipoLugar.getValue() + "").substring(2, 4)) + "" + mes));
//                p_parametros.put("fin", Integer.parseInt("1" + Integer.parseInt(String.valueOf(cmbTipoLugar.getValue() + "").substring(2, 4)) + "" + mes1));
                p_parametros.put("cuenta", Integer.parseInt(cmbTipoLugar.getValue() + ""));
//                p_parametros.put("periodo", meses(utilitario.getMes(fechaFin.getFecha() + "")));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");

                System.out.println(p_parametros);
                System.out.println(rep_reporte.getPath());

                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }
}

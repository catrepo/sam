/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import paq_beans.Archivo;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ReporteContabilidad extends Pantalla {

    //Variable conexion
    private Conexion conOracle = new Conexion();
    //Declaracion de combos
    private Combo cmbAno = new Combo();
    private Combo cmbNiveli = new Combo();
    private Combo cmbNivelf = new Combo();
    //Creacion Calendarios
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Panel panOpcion = new Panel();
    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    String link = new String();
    private Boolean dentro;
    Archivo file = new Archivo();

    public ReporteContabilidad() {

        /*
         * Cadena de conexión base de datos
         */
        conOracle.setUnidad_persistencia(utilitario.getPropiedad("oraclejdbc"));
        conOracle.NOMBRE_MARCA_BASE = "oracle";

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
        sef_formato.setConexion(conOracle);
        agregarComponente(sef_formato);

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("REPORTES CONTABILIDAD");
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
        griCuerpo.getChildren().add(new Etiqueta("AÑO: "));
        cmbAno.setId("cmbAno");
        cmbAno.setCombo("select ano_curso as anio, ano_curso from conc_ano order by ano_curso desc ");
        cmbAno.eliminarVacio();
        griCuerpo.getChildren().add(cmbAno);

        /*
         * COMBOS QUE PERMITE SELECCIONAR EL NIVEL DE BUSQUEDA PARA LAS CEDULAS DE GASTOS E INGRESOS
         */

        griCuerpo.getChildren().add(new Etiqueta("NIVEL INICIAL: "));
        cmbNiveli.setId("cmbNiveli");
        List lista = new ArrayList();
        Object filaa[] = {
            "1", "1"
        };
        Object filab[] = {
            "2", "2"
        };
        Object filac[] = {
            "3", "3"
        };
        Object filad[] = {
            "4", "4"
        };
        Object filae[] = {
            "5", "5"
        };
        Object filaf[] = {
            "6", "6"
        };
        Object filag[] = {
            "7", "7"
        };
        lista.add(filaa);;
        lista.add(filab);;
        lista.add(filac);;
        lista.add(filad);;
        lista.add(filae);;
        lista.add(filaf);;
        lista.add(filag);;
        cmbNiveli.setCombo(lista);
        cmbNiveli.eliminarVacio();
        griCuerpo.getChildren().add(cmbNiveli);

        griCuerpo.getChildren().add(new Etiqueta("NIVEL FINAL: "));
        cmbNivelf.setId("cmbNivelf");
        List lista1 = new ArrayList();
        Object fila0[] = {
            "7", "7"
        };
        Object fila1[] = {
            "6", "6"
        };
        Object fila2[] = {
            "5", "5"
        };
        Object fila3[] = {
            "4", "4"
        };
        Object fila4[] = {
            "3", "3"
        };
        Object fila5[] = {
            "2", "2"
        };
        Object fila6[] = {
            "1", "1"
        };
        lista1.add(fila0);;
        lista1.add(fila1);;
        lista1.add(fila2);;
        lista1.add(fila3);;
        lista1.add(fila4);;
        lista1.add(fila5);;
        lista1.add(fila6);;
        cmbNivelf.setCombo(lista1);
        cmbNivelf.eliminarVacio();
        griCuerpo.getChildren().add(cmbNivelf);

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
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "BALANCE DE COMPROBACIÓN 8COLUMNAS":
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "BALANCE DE COMPROBACIÓN 8COLUMNAS":
                String mes,
                 mes1;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes1 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes1 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
//                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(String.valueOf(cmbAno.getValue() + "").substring(2, 4))) + "01"));
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("inicio", Integer.parseInt("1" + Integer.parseInt(String.valueOf(cmbAno.getValue() + "").substring(2, 4)) + "" + mes));
                p_parametros.put("fin", Integer.parseInt("1" + Integer.parseInt(String.valueOf(cmbAno.getValue() + "").substring(2, 4)) + "" + mes1));
                p_parametros.put("anio", Integer.parseInt(cmbAno.getValue() + ""));
                p_parametros.put("periodo", meses(utilitario.getMes(fechaFin.getFecha() + "")));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    //BUSQUEDA PARA IDENTIFICACIÓN DE PERIODO
    public String meses(Integer numero) {
        switch (numero) {
            case 12:
                selec_mes = "DICIEMBRE";
                break;
            case 11:
                selec_mes = "NOVIEMBRE";
                break;
            case 10:
                selec_mes = "OCTUBRE";
                break;
            case 9:
                selec_mes = "SEPTIEMBRE";
                break;
            case 8:
                selec_mes = "AGOSTO";
                break;
            case 7:
                selec_mes = "JULIO";
                break;
            case 6:
                selec_mes = "JUNIO";
                break;
            case 5:
                selec_mes = "MAYO";
                break;
            case 4:
                selec_mes = "ABRIL";
                break;
            case 3:
                selec_mes = "MARZO";
                break;
            case 2:
                selec_mes = "FEBRERO";
                break;
            case 1:
                selec_mes = "ENERO";
                break;
        }
        return selec_mes;
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

    public Conexion getConOracle() {
        return conOracle;
    }

    public void setConOracle(Conexion conOracle) {
        this.conOracle = conOracle;
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

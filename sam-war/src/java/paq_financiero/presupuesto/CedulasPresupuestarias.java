/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.presupuesto;

import framework.aplicacion.TablaGenerica;
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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import paq_financiero.presupuesto.ejb.ReporteCedulas;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class CedulasPresupuestarias extends Pantalla {

    //Variable conexion
    private Conexion conOracle = new Conexion();
    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setIngresos = new SeleccionTabla();
    private SeleccionTabla setPrograma = new SeleccionTabla();
    private SeleccionTabla setPartidas = new SeleccionTabla();
    private SeleccionTabla setProyecto = new SeleccionTabla();
    //Declaracion de combos
    private Combo cmbNivelInicial = new Combo();
    private Combo cmbNivelFinal = new Combo();
    private Combo cmbTipo = new Combo();
    private Combo cmbMes = new Combo();
    private Combo cmbAnio = new Combo();
    private Combo cmbAnioi = new Combo();
    private Combo cmbAnioi1 = new Combo();
    private Combo cmbProgramas = new Combo();
    //Creacion Calendarios
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Panel panelOpcion = new Panel();
    private Panel panelPresupuesto = new Panel();
    private Panel panelArchivo = new Panel();
    //Etiquetas
    private Etiqueta txtTipo = new Etiqueta("TIPO : ");
    private Etiqueta txtMes = new Etiqueta("MES : ");
    private Etiqueta txtAno = new Etiqueta("AÑO : ");
    private Etiqueta txtAnio = new Etiqueta("AÑO : ");
    private Etiqueta txtAnio1 = new Etiqueta("AÑO : ");
    private Etiqueta txtNivelInicio = new Etiqueta("NIVEL INICIAL : ");
    private Etiqueta txtNivelFin = new Etiqueta("NIVEL FINAL : ");
    private Etiqueta txtFechaInicio = new Etiqueta("FECHA INICIAL : ");
    private Etiqueta txtFechaFin = new Etiqueta("FECHA FINAL : ");
    private Dialogo diaArchivo = new Dialogo();
    private Grid gridA = new Grid();
    private Grid grida = new Grid();
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private String nomAlcalde, nomDirector, nomJefe, puesto, puestoDirector, puestoJefe, fecha;
    private HtmlOutputText valor = new HtmlOutputText();
    private HtmlOutputLink botPrin = new HtmlOutputLink();
    private Imagen quinde = new Imagen();
    //Cuadros para texto, busqueda reportes
    private Texto texProyecto = new Texto();
    private Texto texTramite = new Texto();
    private Texto texNombre = new Texto();
    @EJB
    private ReporteCedulas admin = (ReporteCedulas) utilitario.instanciarEJB(ReporteCedulas.class);
    private ClaseGenerica clase = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);

    public CedulasPresupuestarias() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        /*
         * Cadena de conexión base de datos
         */
        conOracle.setUnidad_persistencia(utilitario.getPropiedad("oraclejdbc"));
        conOracle.NOMBRE_MARCA_BASE = "oracle";

        /*
         * Captura de usuario que se logea
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conOracle);
        agregarComponente(sef_formato);

        /*
         * Inicialización de datos
         */
        Imagen quinde = new Imagen();
        quinde.setStyle("text-align:center;position:absolute;top:190px;left:500px;");
        quinde.setValue("imagenes/logo.png");

        Boton botPrint = new Boton();
        botPrint.setId("botPrint");
        botPrint.setValue("IMPRIMIR");
        botPrint.setIcon("ui-icon-print");
        botPrint.setMetodo("abrirListaReportes");

        cmbNivelInicial.setId("cmbNivelInicial");
        List listInicial = new ArrayList();
        Object filai1[] = {
            "1", "1"
        };
        Object filai2[] = {
            "2", "2"
        };
        Object filai3[] = {
            "3", "3"
        };
        Object filai4[] = {
            "4", "4"
        };
        Object filai5[] = {
            "5", "5"
        };
        Object filai6[] = {
            "6", "6"
        };
        listInicial.add(filai1);;
        listInicial.add(filai2);;
        listInicial.add(filai3);;
        listInicial.add(filai4);;
        listInicial.add(filai5);;
        //listInicial.add(filai6);;
        cmbNivelInicial.setCombo(listInicial);
        cmbNivelInicial.eliminarVacio();

        cmbNivelFinal.setId("cmbNivelFinal");
        List listFinal = new ArrayList();
        Object filaf1[] = {
            "6", "6"
        };
        Object filaf2[] = {
            "5", "5"
        };
        Object filaf3[] = {
            "4", "4"
        };
        Object filaf4[] = {
            "3", "3"
        };
        Object filaf5[] = {
            "2", "2"
        };
        Object filaf6[] = {
            "1", "1"
        };
        //listFinal.add(filaf1);;
        listFinal.add(filaf2);;
        listFinal.add(filaf3);;
        listFinal.add(filaf4);;
        listFinal.add(filaf5);;
        listFinal.add(filaf6);;
        cmbNivelFinal.setCombo(listFinal);
        cmbNivelFinal.eliminarVacio();

        fechaInicio.setId("fechaInicio");
        fechaInicio.setFechaActual();
        fechaInicio.setTipoBoton(true);

        fechaFin.setId("fechaFin");
        fechaFin.setFechaActual();
        fechaFin.setTipoBoton(true);

        cmbTipo.setId("cmbTipo");
        List listTipo = new ArrayList();
        Object filat1[] = {
            "1", "Cedula Inicial"
        };
        Object filat2[] = {
            "2", "Cedula Presupuestaria"
        };
        listTipo.add(filat1);;
        listTipo.add(filat2);;
        cmbTipo.setCombo(listTipo);

        cmbMes.setId("cmbMes");
        List meses = new ArrayList();
        Object enero[] = {
            "01", "Enero"
        };
        Object febrero[] = {
            "02", "Febrero"
        };
        Object marzo[] = {
            "03", "Marzo"
        };
        Object abril[] = {
            "04", "Abril"
        };
        Object mayo[] = {
            "05", "Mayo"
        };
        Object junio[] = {
            "06", "Junio"
        };
        Object julio[] = {
            "07", "Julio"
        };
        Object agosto[] = {
            "08", "Agosto"
        };
        Object septiembre[] = {
            "09", "Septiembre"
        };
        Object octubre[] = {
            "10", "Octubre"
        };
        Object noviembre[] = {
            "11", "Noviembre"
        };
        Object diciembre[] = {
            "12", "Diciembre"
        };
        meses.add(enero);;
        meses.add(febrero);;
        meses.add(marzo);;
        meses.add(abril);;
        meses.add(mayo);;
        meses.add(junio);;
        meses.add(julio);;
        meses.add(agosto);;
        meses.add(septiembre);;
        meses.add(octubre);;
        meses.add(noviembre);;
        meses.add(diciembre);;
        cmbMes.setCombo(meses);

        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("select ano_curso as anio, ano_curso from conc_ano where ano_curso>=2017 order by ano_curso desc ");

        /*
         * Dibujar Pantalla a vizualizar
         */

        Panel tabContorno = new Panel();
        tabContorno.setStyle("font-size:19px;color:black;text-align:center;");
        tabContorno.setHeader("REPORTE CEDULAS PRESUPUESTARIAS INGRESOS,GASTOS Y ARCHIVO PLANO");
        panelOpcion.setId("panelOpcion");
        panelOpcion.setStyle("font-size:12px;color:black;text-align:center;");
        panelOpcion.setTransient(true);

        panelPresupuesto.setId("panelPresupuesto");
        panelPresupuesto.setStyle("font-size:12px;color:black;text-align:left;");
        panelPresupuesto.setHeader("GASTOS E INGRESOS");
        panelPresupuesto.setTransient(true);
        Grid grdCedulas = new Grid();
        grdCedulas.setColumns(2);
        grdCedulas.getChildren().add(txtFechaInicio);
        grdCedulas.getChildren().add(fechaInicio);
        grdCedulas.getChildren().add(txtFechaFin);
        grdCedulas.getChildren().add(fechaFin);
        grdCedulas.getChildren().add(txtNivelInicio);
        grdCedulas.getChildren().add(cmbNivelInicial);
        grdCedulas.getChildren().add(txtNivelFin);
        grdCedulas.getChildren().add(cmbNivelFinal);
        panelPresupuesto.getChildren().add(grdCedulas);

        panelArchivo.setId("panelArchivo");
        panelArchivo.setStyle("font-size:12px;color:black;text-align:left;");
        panelArchivo.setHeader("ARCHIVO PLANO");
        panelArchivo.setTransient(true);
        Grid grdArchivo = new Grid();
        grdArchivo.setColumns(2);
        grdArchivo.getChildren().add(txtAnio);
        grdArchivo.getChildren().add(cmbAnio);
        grdArchivo.getChildren().add(txtTipo);
        grdArchivo.getChildren().add(cmbTipo);
        grdArchivo.getChildren().add(txtMes);
        grdArchivo.getChildren().add(cmbMes);
        panelArchivo.getChildren().add(grdArchivo);

        Grid grdCuerpo = new Grid();
        grdCuerpo.setColumns(1);
        grdCuerpo.getChildren().add(panelPresupuesto);
        grdCuerpo.getChildren().add(panelArchivo);
        panelOpcion.getChildren().add(grdCuerpo);

        tabContorno.getChildren().add(panelOpcion);
        tabContorno.getChildren().add(botPrint);
        agregarComponente(tabContorno);
        panelOpcion.getChildren().add(quinde);

        /*
         * opciones para reportes
         */
        Grid grdIngreso = new Grid();
        grdIngreso.setColumns(5);
        grdIngreso.getChildren().add(txtAno);
        cmbAnioi1.setId("cmbAnioi1");
        cmbAnioi1.setCombo("select ano_curso as anio, ano_curso from conc_ano where ano_curso >= 2017 order by ano_curso desc");
        grdIngreso.getChildren().add(cmbAnioi1);

        Boton botIgraux = new Boton();
        botIgraux.setId("botIgraux");
        botIgraux.setValue("Buscar");
        botIgraux.setIcon("ui-icon-search");
        botIgraux.setMetodo("buscAuxIngreso");
        grdIngreso.getChildren().add(botIgraux);

        setIngresos.setId("setIngresos");
        setIngresos.getTab_seleccion().setConexion(conOracle);
        setIngresos.setSeleccionTabla("select CUENMC,CUENMC as cuenta,CEDTMC,NOLAAD\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC=6 and statmc = 'D' \n"
                + "order by CUENMC", "CUENMC");
        setIngresos.getTab_seleccion().getColumna("cuenta").setFiltro(true);
        setIngresos.getTab_seleccion().getColumna("CEDTMC").setFiltro(true);
        setIngresos.getTab_seleccion().getColumna("NOLAAD").setLongitud(70);
        setIngresos.setRadio();
        setIngresos.getGri_cuerpo().setHeader(grdIngreso);
        setIngresos.getBot_aceptar().setMetodo("dibujarReporte");
        setIngresos.setHeader("PROGRAMAS DE INGRESOS");
        agregarComponente(setIngresos);

        setPrograma.setId("setPrograma");
        setPrograma.getTab_seleccion().setConexion(conOracle);
        setPrograma.setSeleccionTabla("SELECT CAUXMA,CAUXMA as CAUXMA1,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "union\n"
                + "select ZONAZ1,ZONAZ1 as CAUXMA, NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '002'", "CAUXMA");
        setPrograma.getTab_seleccion().getColumna("NOMBMA").setLongitud(70);
        setPrograma.setRadio();
        setPrograma.getBot_aceptar().setMetodo("dibujarReporte");
        setPrograma.setHeader("PROGRAMAS DE GASTOS");
        agregarComponente(setPrograma);


        Grid griPartida = new Grid();
        griPartida.setColumns(5);
        griPartida.getChildren().add(txtAno);
        cmbAnioi.setId("cmbAnioi");
        cmbAnioi.setCombo("select ano_curso as anio, ano_curso from conc_ano where ano_curso >= 2017 order by ano_curso desc");
        griPartida.getChildren().add(cmbAnioi);
        griPartida.getChildren().add(new Etiqueta("Programas: "));
        cmbProgramas.setId("cmbProgramas");
        cmbProgramas.setConexion(conOracle);
        cmbProgramas.setCombo("SELECT CAUXMA,CAUXMA as CAUXMA1,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "union\n"
                + "select ZONAZ1,ZONAZ1 as CAUXMA, NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '002'");
        griPartida.getChildren().add(cmbProgramas);

        Boton botPartida = new Boton();
        botPartida.setId("botPartida");
        botPartida.setValue("Buscar");
        botPartida.setIcon("ui-icon-search");
        botPartida.setMetodo("partidas");
        griPartida.getChildren().add(botPartida);
        setPartidas.setId("setPartidas");
        setPartidas.getTab_seleccion().setConexion(conOracle);
        setPartidas.setSeleccionTabla("select *  \n"
                + "from (select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD   \n"
                + "from USFIMRU.TIGSA_GLM03   \n"
                + "where TIPLMC= 'R' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9)   \n"
                + "and LENGTH( substr(CUENMC,1,10))=10) \n"
                + "left join \n"
                + "(SELECT CUENDT,AUAD02   \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "14  \n"
                + "AND SAPRDT<=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "15  \n"
                + "and AUAD02 is not null) \n"
                + "on CUENMC = CUENDT \n"
                + "where AUAD02 = '22'", "CUENMC");
        setPartidas.getTab_seleccion().getColumna("CUENMC").setFiltro(true);
        setPartidas.getTab_seleccion().getColumna("CUENDT").setVisible(false);
        setPartidas.getTab_seleccion().getColumna("AUAD02").setVisible(false);
        setPartidas.getTab_seleccion().getColumna("NOLAAD").setLongitud(70);
        setPartidas.setRadio();
        setPartidas.getTab_seleccion().setRows(10);
        setPartidas.getGri_cuerpo().setHeader(griPartida);
        setPartidas.getBot_aceptar().setMetodo("dibujarReporte");
        setPartidas.setHeader("SELECCIONAR PARTIDA");
        agregarComponente(setPartidas);

        diaArchivo.setId("diaArchivo");
        diaArchivo.setTitle("IMPRIMIR ARCHIVO PLANO"); //titulo
        diaArchivo.setWidth("20%"); //siempre en porcentajes  ancho
        diaArchivo.setHeight("20%");//siempre porcentaje   alto
        diaArchivo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaArchivo.getBot_aceptar().setDisabled(true);
        gridA.setColumns(4);
        agregarComponente(diaArchivo);

        Grid griProyecto = new Grid();
        griProyecto.setColumns(6);
        griProyecto.getChildren().add(new Etiqueta("# Proyecto: "));
        griProyecto.getChildren().add(texProyecto);
        griProyecto.getChildren().add(new Etiqueta("# Tramite: "));
        griProyecto.getChildren().add(texTramite);
        griProyecto.getChildren().add(new Etiqueta("Nom. Proyecto: "));
        texNombre.setSize(30);
        griProyecto.getChildren().add(texNombre);

        Boton botProyecto = new Boton();
        botProyecto.setId("botProyecto");
        botProyecto.setValue("Buscar");
        botProyecto.setIcon("ui-icon-search");
        botProyecto.setMetodo("proyecto");
        griProyecto.getChildren().add(botProyecto);

        setProyecto.setId("setProyecto");
        setProyecto.getTab_seleccion().setConexion(conOracle);
        setProyecto.setSeleccionTabla("select AUAD01,AUAD01 as num_proyec,AUAD02,NDOCDC,proyecto,CUENDT\n"
                + "from (select ROWNUM as numero,CUENDT,AUAD01,AUAD02,NDOCDC,proyecto\n"
                + "from (select CUENDT,AUAD01,AUAD02,NDOCDC,proyecto\n"
                + "from (select CUENDT,AUAD01,AUAD02\n"
                + ",(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD01 and POSTMA=AUAD02 ) as proyecto\n"
                + "from USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR'\n"
                + "AND SAPRDT>=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "14\n"
                + "AND SAPRDT<=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "15\n"
                + "AND TAAD01 = 'PY'\n"
                + "and AUAD02 is not null\n"
                + "and substr(FDOCDT,1,5) <= 1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()))).substring(2, 4) + "03)\n"
                + "inner join \n"
                + "(select NDOCDC,CUENDC,MONTDC,AUA2DC from USFIMRU.PRCO01) \n"
                + "on CUENDT = CUENDC and AUAD02 = AUA2DC \n"
                + "order by auad01))\n"
                + "where numero = 0", "AUAD01");
        setProyecto.setRadio();
        //setProyecto.getTab_seleccion().getColumna("proyecto").setLongitud(50);
        setProyecto.getTab_seleccion().setRows(10);
        setProyecto.getGri_cuerpo().setHeader(griProyecto);
        setProyecto.setWidth("70");
        setProyecto.getBot_aceptar().setMetodo("dibujarReporte");
        setProyecto.setHeader("AUXILIAR POR PROYECTO");
        agregarComponente(setProyecto);

    }

    public void buscAuxIngreso() {
        if (cmbAnioi1.getValue() != null) {
            setIngresos.getTab_seleccion().setSql("select CUENMC,CUENMC as cuenta,CEDTMC,NOLAAD\n"
                    + "from USFIMRU.TIGSA_GLM03 \n"
                    + "where substr(FCRTMC,1,5) >=" + (Integer.parseInt(String.valueOf(cmbAnioi1.getValue()).substring(2, 4)) - 2) + " and NIVEMC=6 and substr(CEDTMC,1,1) in (1,2,3) and TIPLMC= 'R'\n"
                    + "order by CUENMC");
            setIngresos.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar parametros", "");
        }
    }

    public void datosFirmas() {
        TablaGenerica tabDatos = admin.getDatosFirmas("Cedula Presupuestaria");
        if (!tabDatos.isEmpty()) {
            for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                if (tabDatos.getValor(i, "REPT_TIPO").equals("D")) {
                    if (clase.verificaNull(tabDatos.getValor(i, "REPT_FECHA")).equals("0")) {
                        fecha = utilitario.getFechaHoraActual();
                    } else {
                        fecha = tabDatos.getValor(i, "REPT_FECHA");
                    }
                }
                String[] alcaldeArray = tabDatos.getValor(i, "REPT_RESPON").split(",");
                for (int j = 0; j < alcaldeArray.length; j++) {
                    if (i == 0) {
                        nomAlcalde = alcaldeArray[0];
                        puesto = alcaldeArray[1];
                    } else if (i == 1) {
                        nomDirector = alcaldeArray[0];
                        puestoDirector = alcaldeArray[1];
                    } else {
                        nomJefe = alcaldeArray[0];
                        puestoJefe = alcaldeArray[1];
                    }
                }
            }
        }
    }

    public void partidas() {
        if (cmbProgramas.getValue() != null && cmbAnioi.getValue() != null) {
            setPartidas.getTab_seleccion().setSql("select * \n"
                    + "from (select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD  \n"
                    + "from USFIMRU.TIGSA_GLM03  \n"
                    + "where TIPLMC= 'R' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9)  \n"
                    + "and LENGTH( substr(CUENMC,1,10))=10)\n"
                    + "left join\n"
                    + "(SELECT CUENDT,AUAD02  \n"
                    + "FROM USFIMRU.TIGSA_GLB01 \n"
                    + "where STATDT='E' \n"
                    + "AND CCIADT <> 'CM' and CCIADT <> 'MR'\n"
                    + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(cmbAnioi.getValue()).substring(2, 4)) - 1) + "14 \n"
                    + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(cmbAnioi.getValue()).substring(2, 4)) - 1) + "15 \n"
                    + "and AUAD02 is not null)\n"
                    + "on CUENMC = CUENDT\n"
                    + "where AUAD02 ='" + cmbProgramas.getValue()+"'");
            setPartidas.getTab_seleccion().ejecutarSql();
//            setPartidas.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar parametros", "");
        }
    }

    public void proyecto() {
        String valort = "", valorp = "", texto = "", fechas = "";
        if (!texTramite.getValue().toString().isEmpty()) {
            System.out.println("1");
            valort = texTramite.getValue().toString();
            valorp = "null";
            texto = "null";
        } else if (!texProyecto.getValue().toString().isEmpty()) {
            System.out.println("2");
            valorp = texProyecto.getValue().toString();
            valort = "null";
            texto = "null";
        } else if (!texNombre.getValue().toString().isEmpty()) {
            System.out.println("3");
            valort = "null";
            valorp = "null";
            texto = texNombre.getValue().toString();
        } else {
            utilitario.agregarMensaje("Solo un parametro de busqueda", null);
        }
            if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                fechas = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
            } else {
                fechas = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
            }

        setProyecto.getTab_seleccion().setSql("select AUAD01,AUAD01 as num_proyec,AUAD02,NDOCDC,proyecto,CUENDT\n"
                + "from (select CUENDT,AUAD01,AUAD02,proyecto\n"
                + " ,LISTAGG(NDOCDC, ',') WITHIN GROUP (ORDER BY AUAD02) AS NDOCDC \n"
                + "from (select CUENDT,AUAD01,AUAD02,NDOCDC,proyecto\n"
                + "from (select CUENDT,AUAD01,AUAD02\n"
                + ",(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD01 and POSTMA=AUAD02 ) as proyecto\n"
                + "from USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR'\n"
                + "AND SAPRDT>=1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15\n"
                + "AND TAAD01 = 'PY'\n"
                + "and AUAD02 is not null\n"
                + "and substr(FDOCDT,1,5) <= 1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + ""+fechas+")\n"
                + "inner join \n"
                + "(select NDOCDC,CUENDC,MONTDC,AUA2DC from USFIMRU.PRCO01) \n"
                + "on CUENDT = CUENDC and AUAD02 = AUA2DC \n"
                + "order by auad01)\n"
                + "group by CUENDT,AUAD01,AUAD02,proyecto)\n"
                + "where (AUAD01 = "+ valorp +" or NDOCDC like '%" + valort + "%' or proyecto like '%" + texto + "%')");
        setProyecto.getTab_seleccion().ejecutarSql();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "ANTICIPOS DE PROYECTOS":
                dibujarReporte();
                break;
            case "ARCHIVO PLANO":
                archivoPlano();
                break;
            case "AUXILIAR GASTOS PARTIDAS":
                setPartidas.dibujar();
                break;
            case "AUXILIAR GASTOS POR PROYECTO":
                setProyecto.dibujar();
                break;
            case "AUXILIAR INGRESOS PARTIDAS":
                datosFirmas();
                setIngresos.dibujar();
                break;
            case "AUXILIAR INGRESOS PARTIDAS TRANFS":
                break;
            case "GASTOS CONSOLIDADOS":
                datosFirmas();
                dibujarReporte();
                break;
            case "GASTOS NEGATIVOS POR PROGRAMAS":
                setPrograma.dibujar();
                break;
            case "GASTOS NEGATIVO GENERAL":
                dibujarReporte();
                break;
            case "GASTOS PROGRAMAS":
                datosFirmas();
                setPrograma.dibujar();
                break;
            case "INGRESOS CONSOLIDADOS":
                datosFirmas();
                dibujarReporte();
                break;
            case "INGRESOS NEGATIVOS":
                datosFirmas();
                dibujarReporte();
                break;
            case "SALDOS DE PARTIDAS POR COMPROMISOS":
                break;
            case "TOTAL PARTIDAS POR PROYECTOS FINANCIADOS":
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "ANTICIPOS DE PROYECTOS":
                String mes2s,
                 mes3s;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes2s = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes2s = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes3s = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes3s = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes2s));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes3s));
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Ant. Proyectos -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "ARCHIVO PLANO":
                break;
            case "AUXILIAR GASTOS PARTIDAS":
                String fechas;
                if (cmbAnioi.getValue().toString() == String.valueOf(utilitario.getAnio(utilitario.getFechaActual()))) {
                    if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                        fechas = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    } else {
                        fechas = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    }
                } else {
                    fechas = "12";
                }
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("programa", Integer.parseInt(cmbProgramas.getValue() + ""));
                p_parametros.put("anio", Integer.parseInt(cmbAnioi.getValue().toString() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "01"));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + fechas));
                TablaGenerica tabLis = admin.getDatosPartida(setPartidas.getValorSeleccionado() + "", Integer.parseInt("1" + (Integer.parseInt(cmbAnioi.getValue().toString().substring(2, 4)) - 1) + "14"),
                        Integer.parseInt("1" + (Integer.parseInt(cmbAnioi.getValue().toString().substring(2, 4)) - 1) + "15"), cmbProgramas.getValue() + "");
                if (!tabLis.isEmpty()) {
                    p_parametros.put("descripcion", cmbProgramas.getValue() + "." + tabLis.getValor("CEDTMC") + "  " + tabLis.getValor("NOLAAD"));
                } else {
                    System.err.println("2365");
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("cuenta", setPartidas.getValorSeleccionado() + "");
                System.err.println("Aux Gastos Partida -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "AUXILIAR GASTOS POR PROYECTO":
                String fecha1;
                    if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                        fecha1 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    } else {
                        fecha1 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    }
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("ani1", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "01"));
                p_parametros.put("ani2", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + fecha1));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                TablaGenerica tabPro = admin.getDatosProyecto(Integer.parseInt(setProyecto.getValorSeleccionado()), (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1));
                if (!tabPro.isEmpty()) {
                    p_parametros.put("proyecto", tabPro.getValor("proyecto"));
                }
                p_parametros.put("parameter", setProyecto.getValorSeleccionado() + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "AUXILIAR INGRESOS PARTIDAS":
                String fecha2;
                if (cmbAnioi1.getValue().toString() == String.valueOf(utilitario.getAnio(utilitario.getFechaActual()))) {
                    if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                        fecha2 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    } else {
                        fecha2 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    }
                } else {
                    fecha2 = "12";
                }
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("anio", Integer.parseInt(cmbAnioi1.getValue().toString() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "01"));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + fecha2));
                TablaGenerica tabLiss = admin.getDatosIngreso(setIngresos.getValorSeleccionado() + "");
                if (!tabLiss.isEmpty()) {
                    p_parametros.put("descripcion", tabLiss.getValor("CEDTMC") + "  " + tabLiss.getValor("NOLAAD"));
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("fecha", fecha.toString());
                p_parametros.put("cuenta", setIngresos.getValorSeleccionado() + "");
                System.err.println("Aux Ingresos Partida -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "AUXILIAR INGRESOS PARTIDAS TRANFS":
                break;
            case "GASTOS CONSOLIDADOS":
                String mes2,
                 mes3;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes2 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes2 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes3 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes3 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes2));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes3));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("fecha", fecha.toString());
                p_parametros.put("alcalde", nomAlcalde.toUpperCase());
                p_parametros.put("cargo_alcalde", puesto.toUpperCase());
                p_parametros.put("director", nomDirector.toUpperCase());
                p_parametros.put("cargo_director", puestoDirector.toUpperCase());
                p_parametros.put("jefe", nomJefe.toUpperCase());
                p_parametros.put("cargo_jefe", puestoJefe.toUpperCase());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Gastos Consolidado -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "GASTOS NEGATIVOS POR PROGRAMAS":
                String mes8,
                 mes9;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes8 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes8 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes9 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes9 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes8));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes9));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("programa", Integer.parseInt(setPrograma.getValorSeleccionado()));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Negativos Programas -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "GASTOS NEGATIVO GENERAL":
                String mes14,
                 mes15;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes14 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes14 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes15 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes15 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes14));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes15));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Gastos Negativo -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "GASTOS PROGRAMAS":
                String mes = "",
                 mes1 = "";
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
                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes1));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("programa", Integer.parseInt(setPrograma.getValorSeleccionado()));
                TablaGenerica tabLista = admin.getDatosPrograma(Integer.parseInt(setPrograma.getValorSeleccionado()));
                if (!tabLista.isEmpty()) {
                    p_parametros.put("descripcion", tabLista.getValor("NOMBMA"));
                }
                p_parametros.put("alcalde", nomAlcalde.toUpperCase());
                p_parametros.put("cargo_alcalde", puesto.toUpperCase());
                p_parametros.put("director", nomDirector.toUpperCase());
                p_parametros.put("cargo_director", puestoDirector.toUpperCase());
                p_parametros.put("jefe", nomJefe.toUpperCase());
                p_parametros.put("cargo_jefe", puestoJefe.toUpperCase());
                p_parametros.put("fecha", fecha.toString());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Gastos Programas -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "INGRESOS CONSOLIDADOS":
                String mes5,
                 mes6;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes5 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes5 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes6 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes6 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes5));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes6));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("fecha", fecha.toString());
                p_parametros.put("alcalde", nomAlcalde.toUpperCase());
                p_parametros.put("cargo_alcalde", puesto.toUpperCase());
                p_parametros.put("director", nomDirector.toUpperCase());
                p_parametros.put("cargo_director", puestoDirector.toUpperCase());
                p_parametros.put("jefe", nomJefe.toUpperCase());
                p_parametros.put("cargo_jefe", puestoJefe.toUpperCase());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Ingreso Consolidado -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "INGRESOS NEGATIVOS":
                String mes12,
                 mes13;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes12 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes12 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes13 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes13 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes12));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes13));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("fecha", fecha.toString());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Ingreso Negativo -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "SALDOS DE PARTIDAS POR COMPROMISOS":
                break;
            case "TOTAL PARTIDAS POR PROYECTOS FINANCIADOS":
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "01"));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "12"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Proyecto financiamiento -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public void archivoPlano() {
        admin.setGastoClasificacion();
        TablaGenerica tabIngreso = admin.getCedulaGastos(cmbMes.getValue() + "",
                Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""), cmbAnio.getValue() + "");
        if (!tabIngreso.isEmpty()) {
            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                admin.setDatosCedulaGastos(Integer.parseInt(tabIngreso.getValor(i, "numero")), tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "taad01"), tabIngreso.getValor(i, "auad01"), tabIngreso.getValor(i, "CCIADT"),
                        tabIngreso.getValor(i, "auad02"),
                        Double.parseDouble(tabIngreso.getValor(i, "inicial")), Double.parseDouble(tabIngreso.getValor(i, "reforma")), Double.parseDouble(tabIngreso.getValor(i, "codificado")),
                        Double.parseDouble(tabIngreso.getValor(i, "compromiso")), Double.parseDouble(tabIngreso.getValor(i, "devengado")), Double.parseDouble(tabIngreso.getValor(i, "ejecutado")));
            }
        }
        carpeta();
    }

    public void carpeta() {
        File carpeta = new File(FacesContext.
                getCurrentInstance().
                getExternalContext().
                getRealPath("/upload/archivo/"));
        if (!carpeta.exists()) {
            convertidor();
            carpeta.mkdirs();
            archivo(carpeta.toString());
        } else {
            convertidor();
            archivo(carpeta.toString());
        }
    }

    public void archivo(String carpeta) {
        String valor = null, reforma = null, codificado = null, compromiso = null, devengado = null, ejecutado = null, saldoco = null, saldodev = null, cadena = null;
        File archivo;//para manipular el archivo
        FileWriter escribir;//para escribir en el archivo
        PrintWriter linea;//para información del archivo
        if (cmbTipo.getValue() != null && !cmbTipo.getValue().toString().isEmpty()) {
            if (cmbTipo.getValue().equals("1")) {
                cadena = ("Inicial" + cmbMes.getValue() + "_" + convertidor() + ".txt");
            } else if (cmbTipo.getValue().equals("2")) {
                cadena = ("Presupuesto" + cmbMes.getValue() + "_" + convertidor() + ".txt");
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar tipo de archivo", null);
        }
        archivo = new File(carpeta + "\\" + cadena);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                escribir = new FileWriter(archivo, false);
                linea = new PrintWriter(escribir);
                if (cmbTipo.getValue().equals("1")) {
                    TablaGenerica tabIngreso = admin.getCedulaIngresos(cmbMes.getValue() + "", Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""), cmbAnio.getValue() + "");
                    if (!tabIngreso.isEmpty()) {
                        for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                            //escribo en el archivo
                            if (tabIngreso.getValor(i, "inicial").equals(".00")) {
                                valor = "0";
                            } else {
                                valor = tabIngreso.getValor(i, "inicial");
                            }
                            linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabIngreso.getValor(i, "tipo") + "|"
                                    + tabIngreso.getValor(i, "gr") + "|" + tabIngreso.getValor(i, "sub") + "|" + tabIngreso.getValor(i, "item") + "|" + valor + "");
                        }
                    }
                    TablaGenerica tabGasto = admin.getGastoClasificacion(Integer.parseInt(cmbAnio.getValue() + ""));
                    if (!tabGasto.isEmpty()) {
                        for (int i = 0; i < tabGasto.getTotalFilas(); i++) {
                            if (tabGasto.getValor(i, "inicial").equals(".00")) {
                                valor = "0";
                            } else {
                                valor = tabGasto.getValor(i, "inicial");
                            }
                            linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo1") + "|"
                                    + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|" + tabGasto.getValor(i, "funcion") + "|"
                                    + tabGasto.getValor(i, "orientacion") + "|" + valor + "");
                        }
                    }
                    utilitario.agregarMensaje("Archivo Generado", null);
                }
                if (cmbTipo.getValue().equals("2")) {
                    //System.out.println(Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""));
                    TablaGenerica tabIngreso = admin.getCedulaIngresos(cmbMes.getValue() + "", Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""), cmbAnio.getValue() + "");
                    if (!tabIngreso.isEmpty()) {
                        for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                            //escribo en el archivo
                            if (tabIngreso.getValor(i, "inicial").equals(".00")) {
                                valor = "0";
                            } else {
                                valor = tabIngreso.getValor(i, "inicial");
                            }
                            if (tabIngreso.getValor(i, "codificado").equals(".00")) {
                                codificado = "0";
                            } else {
                                codificado = tabIngreso.getValor(i, "codificado");
                            }
                            if (tabIngreso.getValor(i, "devengado").equals(".00")) {
                                devengado = "0";
                            } else {
                                devengado = tabIngreso.getValor(i, "devengado");
                            }
                            if (tabIngreso.getValor(i, "ejecutado").equals(".00")) {
                                ejecutado = "0";
                            } else {
                                ejecutado = tabIngreso.getValor(i, "ejecutado");
                            }
                            if (tabIngreso.getValor(i, "saldo").equals(".00")) {
                                saldoco = "0";
                            } else {
                                saldoco = tabIngreso.getValor(i, "saldo");
                            }
                            linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabIngreso.getValor(i, "tipo") + "|"
                                    + tabIngreso.getValor(i, "gr") + "|" + tabIngreso.getValor(i, "sub") + "|" + tabIngreso.getValor(i, "item") + "|" + valor
                                    + "|" + tabIngreso.getValor(i, "reforma") + "|" + codificado + "|" + devengado
                                    + "|" + ejecutado + "|" + saldoco + "");
                        }
                    }
                    TablaGenerica tabGasto = admin.getGastoClasificacion(Integer.parseInt(cmbAnio.getValue() + ""));
                    if (!tabGasto.isEmpty()) {
                        for (int i = 0; i < tabGasto.getTotalFilas(); i++) {
                            //escribo en el archivo
                            if (tabGasto.getValor(i, "inicial").equals(".00")) {
                                valor = "0";
                            } else {
                                valor = tabGasto.getValor(i, "inicial");
                            }
                            if (tabGasto.getValor(i, "codificado").equals(".00")) {
                                codificado = "0";
                            } else {
                                codificado = tabGasto.getValor(i, "codificado");
                            }
                            if (tabGasto.getValor(i, "compromiso").equals(".00")) {
                                compromiso = "0";
                            } else {
                                compromiso = tabGasto.getValor(i, "compromiso");
                            }
                            if (tabGasto.getValor(i, "devengado").equals(".00")) {
                                devengado = "0";
                            } else {
                                devengado = tabGasto.getValor(i, "devengado");
                            }
                            if (tabGasto.getValor(i, "ejecutado").equals(".00")) {
                                ejecutado = "0";
                            } else {
                                ejecutado = tabGasto.getValor(i, "ejecutado");
                            }
                            if (tabGasto.getValor(i, "saldoCo").equals(".00")) {
                                saldoco = "0";
                            } else {
                                saldoco = tabGasto.getValor(i, "saldoCo");
                            }
                            if (tabGasto.getValor(i, "saldoDe").equals(".00")) {
                                saldodev = "0";
                            } else {
                                saldodev = tabGasto.getValor(i, "saldoDe");
                            }

                            if (tabGasto.getValor(i, "reforma").equals(".00")) {
                                reforma = "0";
                            } else {
                                reforma = tabGasto.getValor(i, "reforma");
                            }

                            linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo1") + "|"
                                    + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|" + tabGasto.getValor(i, "funcion") + "|"
                                    + tabGasto.getValor(i, "orientacion") + "|" + valor
                                    + "|" + reforma + "|" + codificado + "|" + compromiso
                                    + "|" + devengado + "|" + ejecutado + "|" + saldoco + "|" + saldodev + "");
                        }
                    }
                    utilitario.agregarMensaje("Archivo Generado", null);
                }
                linea.close();
                escribir.close();
            } catch (Exception e) {
                utilitario.agregarMensajeError(null, "ha sucedio un error" + e);
            }
            descargarArchivo(archivo.toString());
        }
    }

    public void descargarArchivo(String archivo) {
        ServletContext serCon = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = serCon.getRealPath("") + "/" + "upload/auxiliar" + "/archivo_plano.txt";
        File file = new File(path);
        try (FileOutputStream out = new FileOutputStream(file.getAbsolutePath())) {
            File archivoCarga = new File(archivo);
            FileInputStream fis = new FileInputStream(archivoCarga);
            byte[] bytesIn = new byte[(int) archivoCarga.length()];
            fis.read(bytesIn);
            fis.close();
            out.write(bytesIn);
            out.flush();
            abrirMetodo("archivo_plano.txt");
        } catch (Exception e) {
        }
    }

    public void abrirMetodo(String archivo) {
        valor.clearInitialState();;
        botPrin.clearInitialState();
        quinde.clearInitialState();;

        valor.setId("valor");
        valor.setValue("Descargar Archivo");
        quinde.setId("quinde");
        quinde.setStyle("text-align:center;");
        quinde.setValue("imagenes/download-icone.png");
        botPrin.setId("botPrin");
        botPrin.setValue("./upload/auxiliar/" + archivo);
        botPrin.getChildren().add(valor);

        diaArchivo.Limpiar();
        diaArchivo.setDialogo(grida);
        gridA.getChildren().add(quinde);
        gridA.getChildren().add(botPrin);
        diaArchivo.setDialogo(gridA);
        diaArchivo.dibujar();
        rep_reporte.cerrar();
    }

    public String convertidor() {
        String caracter = "";
        Calendar fecha = Calendar.getInstance();
        int ano = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);
        caracter = ano + "_" + "_" + mes + "_" + dia + "_" + hora + "_" + minuto + "_" + segundo;
        return caracter;
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

    public SeleccionTabla getSetIngresos() {
        return setIngresos;
    }

    public void setSetIngresos(SeleccionTabla setIngresos) {
        this.setIngresos = setIngresos;
    }

    public SeleccionTabla getSetPrograma() {
        return setPrograma;
    }

    public void setSetPrograma(SeleccionTabla setPrograma) {
        this.setPrograma = setPrograma;
    }

    public SeleccionTabla getSetPartidas() {
        return setPartidas;
    }

    public void setSetPartidas(SeleccionTabla setPartidas) {
        this.setPartidas = setPartidas;
    }

    public SeleccionTabla getSetProyecto() {
        return setProyecto;
    }

    public void setSetProyecto(SeleccionTabla setProyecto) {
        this.setProyecto = setProyecto;
    }
}

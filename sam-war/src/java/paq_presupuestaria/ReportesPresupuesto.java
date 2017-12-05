/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ReportesPresupuesto extends Pantalla {

    //Variable conexion
    private Conexion conOracle = new Conexion();
    //Declaracion de combos
    private Combo cmbAno = new Combo();
    private Combo cmbAnop = new Combo();
    private Combo cmbAnod = new Combo();
    private Combo cmbNiveli = new Combo();
    private Combo cmbNivelf = new Combo();
    private Combo cmbProgramas = new Combo();
    private Combo cmbDesglose = new Combo();
    //Creacion Calendarios
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Panel panOpcion = new Panel();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setPrograma = new SeleccionTabla();
    private SeleccionTabla setPrograma1 = new SeleccionTabla();
    private SeleccionTabla setDesglose = new SeleccionTabla();
    private SeleccionTabla setPartidas = new SeleccionTabla();
    private SeleccionTabla setPartida = new SeleccionTabla();
    private SeleccionTabla setProyecto = new SeleccionTabla();
    //Cuadros para texto, busqueda reportes
    private Texto texProyecto = new Texto();
    private Texto texTramite = new Texto();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public ReportesPresupuesto() {
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
        tabp2.setHeader("CEDULAS PRESUPUESTARIAS INGRESOS Y GASTOS");
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
        lista.add(filaa);;
        lista.add(filab);;
        lista.add(filac);;
        lista.add(filad);;
        lista.add(filae);;
        lista.add(filaf);;
        cmbNiveli.setCombo(lista);
        cmbNiveli.eliminarVacio();
        griCuerpo.getChildren().add(cmbNiveli);

        griCuerpo.getChildren().add(new Etiqueta("NIVEL FINAL: "));
        cmbNivelf.setId("cmbNivelf");
        List lista1 = new ArrayList();
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

        setPrograma.setId("setPrograma");
        setPrograma.getTab_seleccion().setConexion(conOracle);
        setPrograma.setSeleccionTabla("SELECT CAUXMA,CAUXMA as CAUXMA1,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "union\n"
                + "select ZONAZ1,ZONAZ1 as CAUXMA, NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '002'", "CAUXMA");
        setPrograma.getTab_seleccion().getColumna("NOMBMA").setLongitud(70);
        setPrograma.setRadio();
        setPrograma.getBot_aceptar().setMetodo("dibujarReporte");
        setPrograma.setHeader("SELECCIONAR PROGRAMA");
        agregarComponente(setPrograma);

        setPrograma1.setId("setPrograma1");
        setPrograma1.getTab_seleccion().setConexion(conOracle);
        setPrograma1.setSeleccionTabla("SELECT CAUXMA,CAUXMA as CAUXMA1,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "union\n"
                + "select ZONAZ1,ZONAZ1 as CAUXMA, NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '002'", "CAUXMA");
        setPrograma1.getTab_seleccion().getColumna("NOMBMA").setLongitud(70);
        setPrograma1.setRadio();
        setPrograma1.getBot_aceptar().setMetodo("dibujarReporte");
        setPrograma1.setHeader("SELECCIONAR PROGRAMA");
        agregarComponente(setPrograma1);

        setDesglose.setId("setDesglose");
        setDesglose.getTab_seleccion().setConexion(conOracle);
        setDesglose.setSeleccionTabla("SELECT CAUXMA,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'", "CAUXMA");
        setDesglose.getTab_seleccion().getColumna("NOMBMA").setLongitud(70);
        setDesglose.setRadio();
        setDesglose.getBot_aceptar().setMetodo("dibujarReporte");
        setDesglose.setHeader("SELECCIONAR PROGRAMA");
        agregarComponente(setDesglose);

        Grid griPartida = new Grid();
        griPartida.setColumns(5);
        griPartida.getChildren().add(new Etiqueta("AÑO: "));
        cmbAnop.setId("cmbAnop");
        cmbAnop.setCombo("select ano_curso as anio, ano_curso from conc_ano order by ano_curso desc");
        griPartida.getChildren().add(cmbAnop);
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
        setPartidas.setSeleccionTabla("select CUENMC,CUENMC as partida,CEDTMC,NOLAAD, POSTMA from( \n"
                + "select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD \n"
                + "from USFIMRU.TIGSA_GLM03 \n"
                + "where TIPLMC= 'P' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9) \n"
                + "and LENGTH( substr(CUENMC,1,10))=10) \n"
                + "inner join \n"
                + "(select DISTINCT CUENDT as CUENDTi1,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "14 \n"
                + "AND SAPRDT<=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "15\n"
                + "and AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA) \n"
                + "on CUENMC = CUENDTi1 \n"
                + "where postma = -1\n"
                + "order by CUENMC", "CUENMC");
        setPartidas.getTab_seleccion().getColumna("partida").setFiltro(true);
        setPartidas.getTab_seleccion().getColumna("NOLAAD").setLongitud(70);
        setPartidas.setRadio();
        setPartidas.getTab_seleccion().setRows(10);
        setPartidas.getGri_cuerpo().setHeader(griPartida);
        setPartidas.getBot_aceptar().setMetodo("dibujarReporte");
        setPartidas.setHeader("SELECCIONAR PARTIDA");
        agregarComponente(setPartidas);

        Grid griDesglose = new Grid();
        griDesglose.setColumns(5);
        griDesglose.getChildren().add(new Etiqueta("AÑO: "));
        cmbAnod.setId("cmbAnod");
        cmbAnod.setCombo("select ano_curso as anio, ano_curso from conc_ano order by ano_curso desc");
        griDesglose.getChildren().add(cmbAnod);
        griDesglose.getChildren().add(new Etiqueta("Programas: "));
        cmbDesglose.setId("cmbDesglose");
        cmbDesglose.setConexion(conOracle);
        cmbDesglose.setCombo("SELECT CAUXMA,CAUXMA as des,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'");
        griDesglose.getChildren().add(cmbDesglose);

        Boton botDesglose = new Boton();
        botDesglose.setId("botDesglose");
        botDesglose.setValue("Buscar");
        botDesglose.setIcon("ui-icon-search");
        botDesglose.setMetodo("desglose");
        griDesglose.getChildren().add(botDesglose);

        setPartida.setId("setPartida");
        setPartida.getTab_seleccion().setConexion(conOracle);
        setPartida.setSeleccionTabla("select CUENMC,CUENMC as partida,CEDTMC,NOLAAD from(\n"
                + "select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9)\n"
                + "and LENGTH( substr(CUENMC,1,10))=10)\n"
                + "inner join\n"
                + "(select CUENDTi1 from (\n"
                + "SELECT substr(CUENDT,1,10) as CUENDTi1 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND TMOVDT='D'\n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "AND TAAD02 BETWEEN 'CC' and 'PY'\n"
                + "AND CLDODT BETWEEN 'AI' and 'CO'\n"
                + "AND AUAD02 in (SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "UNION\n"
                + "SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'and ZONAMA like '__22%'\n"
                + "UNION\n"
                + "SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'and ZONAMA like '___22%')\n"
                + "GROUP BY CUENDT\n"
                + ")\n"
                + "group by CUENDTi1)\n"
                + "on CUENMC = CUENDTi1\n"
                + "order by CUENMC", "CUENMC");
        setPartida.getTab_seleccion().getColumna("partida").setFiltro(true);
        setPartida.getTab_seleccion().getColumna("NOLAAD").setLongitud(70);
        setPartida.setRadio();
        setPartida.getTab_seleccion().setRows(10);
        setPartida.getGri_cuerpo().setHeader(griDesglose);
        setPartida.getBot_aceptar().setMetodo("dibujarReporte");
        setPartida.setHeader("SELECCIONAR PARTIDA");
        agregarComponente(setPartida);

        Grid griProyecto = new Grid();
        griProyecto.setColumns(5);
        griProyecto.getChildren().add(new Etiqueta("Proyecto: "));
        griProyecto.getChildren().add(texProyecto);
        griProyecto.getChildren().add(new Etiqueta("Tramite: "));
        griProyecto.getChildren().add(texTramite);

        Boton botProyecto = new Boton();
        botProyecto.setId("botProyecto");
        botProyecto.setValue("Buscar");
        botProyecto.setIcon("ui-icon-search");
        botProyecto.setMetodo("proyecto");
        griProyecto.getChildren().add(botProyecto);

        setProyecto.setId("setProyecto");
        setProyecto.getTab_seleccion().setConexion(conOracle);
        setProyecto.setSeleccionTabla("select * from (\n"
                + "select ROWNUM as numero,proyecto,tramite,programa,cuenta,nombre from (\n"
                + "select DISTINCT AUAD02 as proyecto,NDOCDC as tramite,POSTMA as programa,CUENMC as cuenta,NOLAAD as nombre\n"
                + "from (select CUENMC,CEDTMC,NOLAAD\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9))\n"
                + "inner join(\n"
                + "select CUENDT,codificado,AUAD02,NDOCDC,POSTMA  from(\n"
                + "SELECT DISTINCT CUENDT, MONTDT as codificado,AUAD02\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND TMOVDT='D'\n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "AND TAAD02 = 'PY'\n"
                + "AND AUAD02 in (SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'))\n"
                + "inner join\n"
                + "(select NDOCDC,CUENDC,MONTDC,AUA2DC from USFIMRU.PRCO01)\n"
                + "on CUENDT = CUENDC and AUAD02 = AUA2DC\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA)\n"
                + "on CUENMC = CUENDT))\n"
                + "where numero =0", "numero");
        setProyecto.setRadio();
        setProyecto.getTab_seleccion().getColumna("nombre").setLongitud(50);
        setProyecto.getTab_seleccion().setRows(10);
        setProyecto.getGri_cuerpo().setHeader(griProyecto);
        setProyecto.getBot_aceptar().setMetodo("dibujarReporte");
        setProyecto.setHeader("AUXILIAR POR PROYECTO");
        agregarComponente(setProyecto);

    }

    public void partidas() {
//        if (cmbProgramas.getValue() != null && cmbProgramas.getValue().toString().isEmpty()) {
        setPartidas.getTab_seleccion().setSql("select CUENMC,CUENMC as partida,CEDTMC,NOLAAD, POSTMA from( \n"
                + "select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD \n"
                + "from USFIMRU.TIGSA_GLM03 \n"
                + "where TIPLMC= 'P' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9) \n"
                + "and LENGTH( substr(CUENMC,1,10))=10) \n"
                + "inner join \n"
                + "(select DISTINCT CUENDT as CUENDTi1,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "14 \n"
                + "AND SAPRDT<=1" + String.valueOf((utilitario.getAnio(utilitario.getFechaActual()) - 1)).substring(2, 4) + "15\n"
                + "and AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA) \n"
                + "on CUENMC = CUENDTi1 \n"
                + "where postma =" + cmbProgramas.getValue() + "\n"
                + "order by CUENMC");
        setPartidas.getTab_seleccion().ejecutarSql();
//            setPartidas.dibujar();
//        } else {
//            utilitario.agregarMensajeInfo("Debe seleccionar parametros", "");
//        }
    }

    public void desglose() {
        setPartida.getTab_seleccion().setSql("select CUENMC,CUENMC as partida,CEDTMC,NOLAAD from( \n"
                + "select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD \n"
                + "from USFIMRU.TIGSA_GLM03 \n"
                + "where TIPLMC= 'P' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9) \n"
                + "and LENGTH( substr(CUENMC,1,10))=10) \n"
                + "inner join \n"
                + "(select CUENDTi1 from ( \n"
                + "SELECT substr(CUENDT,1,10) as CUENDTi1 FROM USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND TMOVDT='D' \n"
                + "AND SAPRDT>=1" + (Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) - 1) + "15\n"
                + "AND TAAD02 BETWEEN 'CC' and 'PY' \n"
                + "AND CLDODT BETWEEN 'AI' and 'CO' \n"
                + "AND AUAD02 in (SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "UNION\n"
                + "SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'and ZONAMA like '__22%'\n"
                + "UNION\n"
                + "SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'and ZONAMA like '___22%')\n"
                + "and AUAD02 = " + (Integer.parseInt(cmbDesglose.getValue() + "")) + " \n"
                + "GROUP BY CUENDT ) \n"
                + "group by CUENDTi1) \n"
                + "on CUENMC = CUENDTi1 \n"
                + "order by CUENMC");
        setPartida.getTab_seleccion().ejecutarSql();
    }

    public void proyecto() {
        String valort = "null", valorp = "null";
        if (texTramite.getValue() != null && texProyecto.getValue() == null || texProyecto.getValue().toString().isEmpty()) {
            valort = texTramite.getValue().toString();
            valorp = "null";
        } else if (texTramite.getValue() == null && texProyecto.getValue() != null || texTramite.getValue().toString().isEmpty()) {
            valorp = texProyecto.getValue().toString();
            valort = "null";
        } else if (texTramite.getValue() != null && texProyecto.getValue() != null) {
            valort = texTramite.getValue().toString();
            valorp = texProyecto.getValue().toString();
        }
        setProyecto.getTab_seleccion().setSql("select * from (\n"
                + "select ROWNUM as numero,proyecto,tramite,programa,cuenta,nombre from (\n"
                + "select DISTINCT AUAD02 as proyecto,NDOCDC as tramite,POSTMA as programa,CUENMC as cuenta,NOLAAD as nombre\n"
                + "from (select CUENMC,CEDTMC,NOLAAD\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9))\n"
                + "inner join(\n"
                + "select CUENDT,codificado,AUAD02,NDOCDC,POSTMA  from(\n"
                + "SELECT DISTINCT CUENDT, MONTDT as codificado,AUAD02\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND TMOVDT='D'\n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "AND TAAD02 = 'PY'\n"
                + "AND AUAD02 in (SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'))\n"
                + "inner join\n"
                + "(select NDOCDC,CUENDC,MONTDC,AUA2DC from USFIMRU.PRCO01)\n"
                + "on CUENDT = CUENDC and AUAD02 = AUA2DC\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA)\n"
                + "on CUENMC = CUENDT))\n"
                + "where (proyecto = " + valorp + " or tramite = " + valort + ")");
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
            case "AUXILIAR GASTOS PARTIDAS":
                setPartidas.dibujar();
                break;
            case "AUXILIAR GASTOS PARTIDAS 22":
                setPartida.dibujar();
                break;
            case "AUXILIAR GASTOS POR PROYECTO":
                setProyecto.dibujar();
                break;
            case "GASTOS CONSOLIDADOS":
                dibujarReporte();
                break;
            case "GASTOS PROGRAMAS":
                setPrograma1.dibujar();
                break;
            case "GASTOS PROGRAMAS 22":
                setDesglose.dibujar();
                break;
            case "GASTOS NEGATIVOS POR PROGRAMAS":
                setPrograma.dibujar();
                break;
            case "GASTOS NEGATIVOS POR PROGRAMAS22":
                setDesglose.dibujar();
                break;
            case "GASTOS NEGATIVO TOTAL":
                dibujarReporte();
                break;
            case "INGRESOS CONSOLIDADOS":
                dibujarReporte();
                break;
            case "INGRESOS NEGATIVOS":
                dibujarReporte();
                break;
            case "TOTAL PARTIDAS POR PROYECTOS FINANCIADOS":
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "INGRESOS CONSOLIDADOS":
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
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes1));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
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
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes2));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes3));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "GASTOS PROGRAMAS":
                String mes4,
                 mes5;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes4 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes4 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes5 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes5 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes4));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes5));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("programa", Integer.parseInt(setPrograma1.getValorSeleccionado()));
                TablaGenerica tabLista = programas.getDatosPrograma1(Integer.parseInt(setPrograma1.getValorSeleccionado()));
                if (!tabLista.isEmpty()) {
                    p_parametros.put("descripcion", tabLista.getValor("NOMBMA"));
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "GASTOS PROGRAMAS 22":
                String mes6,
                 mes7;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes6 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes6 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes7 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes7 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes6));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes7));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("programa", Integer.parseInt(setDesglose.getValorSeleccionado().substring(0, 2)));
                p_parametros.put("desgolse", Integer.parseInt(setDesglose.getValorSeleccionado()));
                TablaGenerica tabList = programas.getDatosDesglose(Integer.parseInt(setDesglose.getValorSeleccionado()));
                if (!tabList.isEmpty()) {
                    p_parametros.put("descripcion", tabList.getValor("CAUXMA") + " " + tabList.getValor("NOMBMA"));
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "AUXILIAR GASTOS PARTIDAS":
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(cmbAnop.getValue().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(cmbAnop.getValue().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("programa", Integer.parseInt(cmbProgramas.getValue() + ""));
                p_parametros.put("anio", Integer.parseInt(cmbAnop.getValue() + ""));
                p_parametros.put("ani1", Integer.parseInt("1" + Integer.parseInt(cmbAnop.getValue().toString().substring(2, 4)) + "01"));
                p_parametros.put("ani2", Integer.parseInt("1" + Integer.parseInt(cmbAnop.getValue().toString().substring(2, 4)) + "12"));
                TablaGenerica tabLis = programas.getDatosPartida(setPartidas.getValorSeleccionado() + "", Integer.parseInt("1" + (Integer.parseInt(cmbAnop.getValue().toString().substring(2, 4)) - 1) + "14"),
                        Integer.parseInt("1" + (Integer.parseInt(cmbAnop.getValue().toString().substring(2, 4)) - 1) + "15"), cmbProgramas.getValue() + "");
                if (!tabLis.isEmpty()) {
                    p_parametros.put("descripcion", cmbProgramas.getValue() + "." + tabLis.getValor("CEDTMC") + "  " + tabLis.getValor("NOLAAD"));
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("cuenta", setPartidas.getValorSeleccionado() + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "AUXILIAR GASTOS PARTIDAS 22":
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("programa", Integer.parseInt("22"));
                p_parametros.put("anio", Integer.parseInt(cmbAnod.getValue() + ""));
                p_parametros.put("ani1", Integer.parseInt("1" + Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) + "01"));
                p_parametros.put("ani2", Integer.parseInt("1" + Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) + "12"));
                TablaGenerica tabLi = programas.getDatosPartidas(setPartida.getValorSeleccionado() + "", Integer.parseInt("1" + (Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) - 1) + "14"),
                        Integer.parseInt("1" + (Integer.parseInt(cmbAnod.getValue().toString().substring(2, 4)) - 1) + "15"), cmbDesglose.getValue() + "");
                if (!tabLi.isEmpty()) {
                    p_parametros.put("descripcion", cmbDesglose.getValue() + "." + tabLi.getValor("CEDTMC") + "  " + tabLi.getValor("NOLAAD"));
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("cuenta", setPartida.getValorSeleccionado() + "");
                p_parametros.put("desglose", Integer.parseInt(cmbDesglose.getValue() + ""));
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "AUXILIAR GASTOS POR PROYECTO":
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15"));
                p_parametros.put("ani1", Integer.parseInt("1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01"));
                p_parametros.put("ani2", Integer.parseInt("1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "12"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                TablaGenerica tabPro = programas.getDatosProyecto(Integer.parseInt(setProyecto.getValorSeleccionado()),(Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1));
                if (!tabPro.isEmpty()) {
                    p_parametros.put("proyecto", tabPro.getValor("proyecto"));
                }
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
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes8));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes9));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("programa", Integer.parseInt(setPrograma.getValorSeleccionado()));
                TablaGenerica tabProg = programas.getDatosPrograma(Integer.parseInt(setPrograma.getValorSeleccionado()));
                if (!tabProg.isEmpty()) {
                    p_parametros.put("descripcion", tabProg.getValor("NOMBZ1"));
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "GASTOS NEGATIVOS POR PROGRAMAS22":
                String mes11,
                 mes10;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes11 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes11 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes10 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes10 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes11));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes10));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("programa", Integer.parseInt(setDesglose.getValorSeleccionado().substring(0, 2)));
                p_parametros.put("desgolse", Integer.parseInt(setDesglose.getValorSeleccionado()));
                TablaGenerica tabProgs = programas.getDatosDesglose(Integer.parseInt(setDesglose.getValorSeleccionado()));
                if (!tabProgs.isEmpty()) {
                    p_parametros.put("descripcion", tabProgs.getValor("CAUXMA") + " " + tabProgs.getValor("NOMBMA"));
                }
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "TOTAL PARTIDAS POR PROYECTOS FINANCIADOS":
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15"));
                p_parametros.put("ani1", Integer.parseInt("1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01"));
                p_parametros.put("ani2", Integer.parseInt("1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "12"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
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
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes12));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes13));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "GASTOS NEGATIVO TOTAL":
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
                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes14));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes15));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha())) - 1).substring(2, 4) + "15"));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
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

    public SeleccionTabla getSetPrograma() {
        return setPrograma;
    }

    public void setSetPrograma(SeleccionTabla setPrograma) {
        this.setPrograma = setPrograma;
    }

    public SeleccionTabla getSetDesglose() {
        return setDesglose;
    }

    public void setSetDesglose(SeleccionTabla setDesglose) {
        this.setDesglose = setDesglose;
    }

    public SeleccionTabla getSetPartidas() {
        return setPartidas;
    }

    public void setSetPartidas(SeleccionTabla setPartidas) {
        this.setPartidas = setPartidas;
    }

    public SeleccionTabla getSetPartida() {
        return setPartida;
    }

    public SeleccionTabla getSetProyecto() {
        return setProyecto;
    }

    public void setSetProyecto(SeleccionTabla setProyecto) {
        this.setProyecto = setProyecto;
    }

    public void setSetPartida(SeleccionTabla setPartida) {
        this.setPartida = setPartida;
    }

    public SeleccionTabla getSetPrograma1() {
        return setPrograma1;
    }

    public void setSetPrograma1(SeleccionTabla setPrograma1) {
        this.setPrograma1 = setPrograma1;
    }
}

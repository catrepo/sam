/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_auditoria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.ListaSeleccion;
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
import paq_financiero.presupuesto.ejb.ReporteCedulas;
import paq_utilitario.ejb.ClaseGenerica;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ReporteContabilidadPresupuesto extends Pantalla {

    //Variable conexion
    private Conexion conOracle = new Conexion();
    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setIngresos = new SeleccionTabla();
    private SeleccionTabla setPrograma = new SeleccionTabla();
    private SeleccionTabla setPrograma1 = new SeleccionTabla();
    private SeleccionTabla setPartidas = new SeleccionTabla();
    private SeleccionTabla setProyecto = new SeleccionTabla();
    private SeleccionTabla setComprobantes = new SeleccionTabla();
    //Declaracion de combos
    private Combo cmbNivelInicial = new Combo();
    private Combo cmbNivelFinal = new Combo();
    private Combo cmbAnioi = new Combo();
    private Combo cmbAnioi1 = new Combo();
    private Combo cmbProgramas = new Combo();
    private Combo cmbAgrupar = new Combo();
    //Creacion Calendarios
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Panel panelOpcion = new Panel();
    private Panel panelPresupuesto = new Panel();
    private Panel panelArchivo = new Panel();
    private ListaSeleccion listReporte = new ListaSeleccion();
    //Etiquetas
    private Etiqueta txtAno = new Etiqueta("AÑO : ");
    private Etiqueta txtAnio1 = new Etiqueta("AÑO : ");
    private Etiqueta txtNivelInicio = new Etiqueta("NIVEL INICIAL : ");
    private Etiqueta txtNivelFin = new Etiqueta("NIVEL FINAL : ");
    private Etiqueta txtFechaInicio = new Etiqueta("FECHA INICIAL : ");
    private Etiqueta txtFechaFin = new Etiqueta("FECHA FINAL : ");
    private Etiqueta etiEstado = new Etiqueta("Agrupar por : ");
    private Etiqueta etiCompania = new Etiqueta("Compañía : ");
    private Etiqueta etiTipo = new Etiqueta("Tipo Comprobante : ");
    private Etiqueta etiPeriodo = new Etiqueta("Periodo Comprobante : ");
    private Etiqueta etiNumero = new Etiqueta("Número Comprobante : ");
    private Texto txtCompania = new Texto();
    private Texto txtTipo = new Texto();
    private Texto txtPeriodo = new Texto();
    private Texto txtMess = new Texto();
    private Texto txtNumero = new Texto();
    private Dialogo diaArchivo = new Dialogo();
    private Dialogo diaEstado = new Dialogo();
    private Grid gridA = new Grid();
    private Grid gridE = new Grid();
    private Grid grida = new Grid();
    private Grid gride = new Grid();
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private String nomAlcalde, nomDirector, nomJefe, puesto, puestoDirector, puestoJefe, fecha;
    private Imagen quinde = new Imagen();
    private Calendario fechaDesde = new Calendario();
    private Calendario fechaHasta = new Calendario();

    //declaracion de tablas
    private SeleccionTabla setMovimientos = new SeleccionTabla();
    private Etiqueta etiCompania1 = new Etiqueta("Compañía o Grupo : ");
    private Etiqueta etiNomInstitucion = new Etiqueta("MUNICIPIO DE RUMIÑAHUI");
    private Etiqueta etiDesde = new Etiqueta("Periodo Desde : ");
    private Etiqueta etiHasta = new Etiqueta("Periodo Hasta : ");
    private Etiqueta etiCuenta = new Etiqueta("Cuenta de Mayor : ");
    private Etiqueta etiDato = new Etiqueta("Tipo de Datos : ");

    private Texto txtCompania1 = new Texto();
    private Texto txtTipo1 = new Texto();
    private Texto txtDesde = new Texto();
    private Texto txtHasta = new Texto();
    private Texto txtCuenta = new Texto();
    @EJB
    private ReporteCedulas admin = (ReporteCedulas) utilitario.instanciarEJB(ReporteCedulas.class);
    private ClaseGenerica clase = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);
//    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public ReporteContabilidadPresupuesto() {
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

        /*
         * Dibujar Pantalla a vizualizar
         */
        Panel tabContorno = new Panel();
        tabContorno.setStyle("font-size:19px;color:black;text-align:center;");
        tabContorno.setHeader("REPORTES FINANCIEROS, CONTABILIDAD, PRESUPUESTO");
        panelOpcion.setId("panelOpcion");
        panelOpcion.setStyle("font-size:12px;color:black;text-align:center;");
        panelOpcion.setTransient(true);

        panelPresupuesto.setId("panelPresupuesto");
        panelPresupuesto.setStyle("font-size:12px;color:black;text-align:left;");
        panelPresupuesto.setHeader("RANGO DE FECHAS");
        panelPresupuesto.setTransient(true);
        Grid grdCedulas = new Grid();
        grdCedulas.setColumns(2);
        grdCedulas.getChildren().add(txtFechaInicio);
        grdCedulas.getChildren().add(fechaInicio);
        grdCedulas.getChildren().add(txtFechaFin);
        grdCedulas.getChildren().add(fechaFin);
//        grdCedulas.getChildren().add(txtNivelInicio);
//        grdCedulas.getChildren().add(cmbNivelInicial);
//        grdCedulas.getChildren().add(txtNivelFin);
//        grdCedulas.getChildren().add(cmbNivelFinal);
        panelPresupuesto.getChildren().add(grdCedulas);

        Grid grdCuerpo = new Grid();
        grdCuerpo.setColumns(1);
        grdCuerpo.getChildren().add(panelPresupuesto);
        panelOpcion.getChildren().add(grdCuerpo);

        tabContorno.getChildren().add(panelOpcion);
        tabContorno.getChildren().add(botPrint);
        agregarComponente(tabContorno);
        panelOpcion.getChildren().add(quinde);

        /*
        impresion de comprobantes de pago
         */
        Grid griComprobante = new Grid();
        griComprobante.setColumns(3);
        griComprobante.getChildren().add(etiCompania);
        griComprobante.getChildren().add(txtCompania);
        txtCompania.setSize(5);
        txtCompania.setValue("MR");
        Grid griAux = new Grid();
        griAux.setColumns(3);
        griAux.getChildren().add(etiPeriodo);
        griAux.getChildren().add(txtPeriodo);
        griAux.getChildren().add(txtMess);
        txtPeriodo.setSize(5);
        txtMess.setSize(2);
        txtPeriodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        txtMess.setValue(mes(utilitario.getMes(utilitario.getFechaActual())));
        griComprobante.getChildren().add(griAux);
        griComprobante.getChildren().add(etiTipo);
        griComprobante.getChildren().add(txtTipo);
        txtTipo.setSize(5);
        Grid griAux1 = new Grid();
        griAux1.setColumns(2);
        griAux1.getChildren().add(etiNumero);
        griAux1.getChildren().add(txtNumero);
        txtNumero.setSize(10);
        griComprobante.getChildren().add(griAux1);

        Boton botComprobante = new Boton();
        botComprobante.setId("botComprobante");
        botComprobante.setValue("Buscar");
        botComprobante.setIcon("ui-icon-search");
        botComprobante.setMetodo("buscaComprobantes");
        griComprobante.getChildren().add(botComprobante);

        setComprobantes.setId("setComprobantes");
        setComprobantes.getTab_seleccion().setConexion(conOracle);
        setComprobantes.setSeleccionTabla("select compromiso as id,compania,aniofecha as Año,mesfecha as Periodo,fecha,dttcom as Tipo_Cta,dtncom as Número_Cta,compromiso \n"
                + "from VT_CABECERA_COMPROBANTE_PAGO\n"
                + "where dtccia='Y'", "id");
        setComprobantes.setRadio();
        setComprobantes.getTab_seleccion().setRows(7);
        setComprobantes.getTab_seleccion().getColumna("Número_Cta").setFiltro(true);
        setComprobantes.getTab_seleccion().getColumna("compromiso").setFiltro(true);
        setComprobantes.getGri_cuerpo().setHeader(griComprobante);
        setComprobantes.setWidth("45");
        setComprobantes.getBot_aceptar().setMetodo("dibujarReporte");
        setComprobantes.setHeader("COMPROBANTE DE PAGO");
        agregarComponente(setComprobantes);

        /*
        saldos y movimiento de mayor
         */
        fechaDesde.setId("fechaDesde");
        fechaDesde.setTipoBoton(true);

        fechaHasta.setId("fechaHasta");
        fechaHasta.setFechaActual();
        fechaHasta.setTipoBoton(true);
        Grid griMovimiento = new Grid();
        griMovimiento.setColumns(1);
        Grid griMovAux = new Grid();
        griMovAux.setColumns(3);
        griMovAux.getChildren().add(etiCompania1);
        griMovAux.getChildren().add(txtCompania1);
        txtCompania1.setValue("MR");
        txtCompania1.setReadonly(true);
        txtCompania1.setSize(5);
        griMovAux.getChildren().add(etiNomInstitucion);
        Grid griMovAux1 = new Grid();
        griMovAux1.setColumns(4);
        griMovAux1.getChildren().add(etiDesde);
        griMovAux1.getChildren().add(fechaDesde);
        griMovAux1.getChildren().add(etiHasta);
        griMovAux1.getChildren().add(fechaHasta);
        Grid griMovAux2 = new Grid();
        griMovAux2.setColumns(2);
        griMovAux2.getChildren().add(etiCuenta);
        griMovAux2.getChildren().add(txtCuenta);
        griMovAux2.getChildren().add(etiDato);
        griMovAux2.getChildren().add(txtTipo1);
        txtTipo1.setValue("R");
        txtTipo1.setReadonly(true);
        txtTipo1.setSize(5);
        txtCuenta.setSize(10);
        griMovimiento.getChildren().add(griMovAux);
        griMovimiento.getChildren().add(griMovAux1);
        griMovimiento.getChildren().add(griMovAux2);

        Boton botMovimiento = new Boton();
        botMovimiento.setId("botMovimiento");
        botMovimiento.setValue("Buscar");
        botMovimiento.setIcon("ui-icon-search");
        botMovimiento.setMetodo("buscaMovimientos");
        griMovimiento.getChildren().add(botMovimiento);

        setMovimientos.setId("setMovimientos");
        setMovimientos.getTab_seleccion().setConexion(conOracle);
        setMovimientos.setSeleccionTabla("select id,CUENMC,NOABMC,SALDO,debe, haber,(debe -(haber*-1)) neto,(debe -(haber*-1)) acumulado\n"
                + "from (select CUENMC as id,CUENMC,NOABMC,0 SALDO ,(case when debe is null then 0 else debe end )debe, (case when haber is null then 0 else haber end)haber\n"
                + "from (select TIPLMC,NOABMC,CUENMC\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'C'\n"
                + "AND nivemc = 4)\n"
                + "left join\n"
                + "(select substr(CUENDT,1,5)CUENDT,(case when sum(MONTDT) is null then 0 else  sum(MONTDT) end ) as debe\n"
                + "from USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND SAPRDT>=11801\n"
                + "AND SAPRDT<=11812\n"
                + "AND TMOVDT='D'\n"
                + "AND CCIADT = 'MR'\n"
                + "group by substr(CUENDT,1,5))\n"
                + "on CUENMC = CUENDT\n"
                + "left join\n"
                + "(select substr(CUENDT,1,5) as auxh,(case when sum(MONTDT) is null then 0 else  sum(MONTDT) end ) as haber\n"
                + "from USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND SAPRDT>=11801\n"
                + "AND SAPRDT<=11812\n"
                + "AND TMOVDT='H'\n"
                + "AND CCIADT = 'MR'\n"
                + "group by substr(CUENDT,1,5))\n"
                + "on CUENMC = auxh\n"
                + "where CUENMC like '98621%')\n"
                + "order by CUENMC", "CUENMC");
        setMovimientos.setRadio();
        setMovimientos.getTab_seleccion().setRows(15);
//        setMovimientos.getTab_seleccion().getColumna("Número_Cta").setFiltro(true);
//        setMovimientos.getTab_seleccion().getColumna("compromiso").setFiltro(true);
        setMovimientos.getGri_cuerpo().setHeader(griMovimiento);
        setMovimientos.setHeight("85");
        setMovimientos.getBot_aceptar().setMetodo("dibujarReporte");
        setMovimientos.setHeader("SALDOS Y MOVIMIENTOS DE MAYOR");
        agregarComponente(setMovimientos);

        /*        Cedulas por programas        */
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

    public void buscaComprobantes() {
        if (txtCompania.getValue() != null && !txtCompania.getValue().toString().isEmpty()) {
            if (txtPeriodo.getValue() != null && !txtPeriodo.getValue().toString().isEmpty() && txtMess.getValue() != null && !txtMess.getValue().toString().isEmpty()) {
                if (txtTipo.getValue() != null && !txtTipo.getValue().toString().isEmpty()) {
                    if (txtNumero.getValue() != null && !txtNumero.getValue().toString().isEmpty()) {
                        setComprobantes.getTab_seleccion().setSql("select compromiso as id,compania,aniofecha,mesfecha,fecha,dttcom,dtncom ,compromiso\n"
                                + "from VT_CABECERA_COMPROBANTE_PAGO\n"
                                + "where dtccia='" + txtCompania.getValue().toString().toUpperCase() + "'\n"
                                + "and dtperi=" + "1" + txtPeriodo.getValue().toString().substring(2, 4) + "" + txtMess.getValue() + "" + "\n"
                                + "and dttcom=" + txtTipo.getValue() + "\n"
                                + "and dtclct = 'P'\n"
                                + "and dtncom >=" + txtNumero.getValue() + "\n"
                                + "order by dtncom,fecha");
                        setComprobantes.getTab_seleccion().ejecutarSql();
                    } else {
                        utilitario.agregarMensajeInfo("Ingresar Número de comprobante", "");
                    }
                } else {
                    utilitario.agregarMensajeInfo("Ingresar Tipo de comprobante", "");
                }
            } else {
                utilitario.agregarMensajeInfo("Ingresar Periodo y Mes", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Ingresar Compañia", "");
        }
    }
    
        public void buscaMovimientos() {
        if (txtCuenta.getValue() != null && !txtCuenta.getValue().toString().isEmpty()) {
            String cadena = txtCuenta.getValue().toString();
            int variable = (cadena.length())+1;
            int codigo = variable + 1;

            String mes,
                    mes1;
            if (String.valueOf((utilitario.getMes(fechaDesde.getFecha()))).length() > 1) {
                mes = String.valueOf((utilitario.getMes(fechaDesde.getFecha())));
            } else {
                mes = "0" + String.valueOf((utilitario.getMes(fechaDesde.getFecha())));
            }
            if (String.valueOf((utilitario.getMes(fechaHasta.getFecha()))).length() > 1) {
                mes1 = String.valueOf((utilitario.getMes(fechaHasta.getFecha())));
            } else {
                mes1 = "0" + String.valueOf((utilitario.getMes(fechaHasta.getFecha())));
            }

            setMovimientos.getTab_seleccion().setSql("select id,CUENMC,NOABMC,SALDO,debe, haber,(debe -(haber*-1)) neto,(debe -(haber*-1)) acumulado\n"
                    + "from (select CUENMC as id,CUENMC,NOABMC,0 SALDO ,(case when debe is null then 0 else debe end )debe, (case when haber is null then 0 else haber end)haber\n"
                    + "from (select TIPLMC,NOABMC,CUENMC \n"
                    + "from USFIMRU.TIGSA_GLM03 \n"
                    + "where TIPLMC= 'C'\n"
                    + "AND nivemc = " + variable + ")\n"
                    + "left join \n"
                    + "(select substr(CUENDT,1," + codigo + ")CUENDT,(case when sum(MONTDT) is null then 0 else  sum(MONTDT) end ) as debe\n"
                    + "from USFIMRU.TIGSA_GLB01\n"
                    + "where STATDT='E'\n"
                    + "AND SAPRDT>=" + Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaDesde.getFecha()))).substring(2, 4) + "" + mes) + "\n"
                    + "AND SAPRDT<=" + Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaHasta.getFecha()))).substring(2, 4) + "" + mes1) + "\n"
                    + "AND TMOVDT='D'\n"
                    + "AND CCIADT = 'MR'\n"
                    + "group by substr(CUENDT,1," + codigo + "))\n"
                    + "on CUENMC = CUENDT\n"
                    + "left join \n"
                    + "(select substr(CUENDT,1," + codigo + ") as auxh,(case when sum(MONTDT) is null then 0 else  sum(MONTDT) end ) as haber\n"
                    + "from USFIMRU.TIGSA_GLB01\n"
                    + "where STATDT='E'\n"
                    + "AND SAPRDT>=" + Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaDesde.getFecha()))).substring(2, 4) + "" + mes) + "\n"
                    + "AND SAPRDT<=" + Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaHasta.getFecha()))).substring(2, 4) + "" + mes1) + "\n"
                    + "AND TMOVDT='H'\n"
                    + "AND CCIADT = 'MR'\n"
                    + "group by substr(CUENDT,1," + codigo + "))\n"
                    + "on CUENMC = auxh\n"
                    + "where CUENMC like '" + txtCuenta.getValue() + "%' )\n"
                    + "order by CUENMC");
            setMovimientos.getTab_seleccion().ejecutarSql();
            setMovimientos.getTab_seleccion().imprimirSql();
        } else {
            utilitario.agregarMensajeInfo("Ingresar Cuenta", "");
        }
    }
    
    public String mes(Integer numero) {
        String mes = "";
        if (String.valueOf(numero).length() > 1) {
            mes = String.valueOf(numero);
        } else {
            mes = "0" + String.valueOf(numero);
        }
        return mes;
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "COMPROBANTE DE PAGO":
                setComprobantes.dibujar();
                break;
            case "SALDOS Y MOVIMIENTOS DE MAYOR":
                setMovimientos.dibujar();
                break;
            case "CEDULA GASTOS PROGRAMAS":
                datosFirmas();
                setPrograma.dibujar();
                break;
            case "CEDULA GASTOS PROGRAMAS (AÑO 2016)":
                setPrograma1.dibujar();
                break;
                case "CEDULA INGRESOS CONSOLIDADOS":
                datosFirmas();
                dibujarReporte();
                break;
                case "CEDULA INGRESOS CONSOLIDADOS (AÑO 2016)":
                datosFirmas();
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "COMPROBANTE DE PAGO":
                p_parametros.put("compania", txtCompania.getValue() + "");
                p_parametros.put("tipo", txtTipo.getValue() + "");
                p_parametros.put("numero", txtNumero.getValue() + "");
                p_parametros.put("periodo", txtPeriodo.getValue() + "");
                p_parametros.put("mes", txtMess.getValue() + "");
                p_parametros.put("comprobante", setComprobantes.getValorSeleccionado() + "");
                p_parametros.put("fecha", "1" + txtPeriodo.getValue().toString().substring(2, 4) + "" + txtMess.getValue() + "");
                System.err.println("->>>Comp " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "SALDOS Y MOVIMIENTOS DE MAYOR":
                String mes,
                 mes1;
                if (String.valueOf((utilitario.getMes(fechaDesde.getFecha()))).length() > 1) {
                    mes = String.valueOf((utilitario.getMes(fechaDesde.getFecha())));
                } else {
                    mes = "0" + String.valueOf((utilitario.getMes(fechaDesde.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaHasta.getFecha()))).length() > 1) {
                    mes1 = String.valueOf((utilitario.getMes(fechaHasta.getFecha())));
                } else {
                    mes1 = "0" + String.valueOf((utilitario.getMes(fechaHasta.getFecha())));
                }

                p_parametros.put("p_desde", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaDesde.getFecha()))).substring(2, 4) + "" + mes) + "");
                p_parametros.put("p_hasta", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaHasta.getFecha()))).substring(2, 4) + "" + mes1) + "");
                p_parametros.put("cuenta", setMovimientos.getValorSeleccionado() + "%");

                TablaGenerica tabLis = admin.getNombPartida(txtCuenta.getValue() + "");
                if (!tabLis.isEmpty()) {
                    p_parametros.put("n_cuenta", tabLis.getValor("NOABMC") + "");
                } else {
                    System.err.println("2365");
                }
                System.err.println("->>>Mayr " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "CEDULA GASTOS PROGRAMAS":
                String mes2 = "",
                 mes3 = "";
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
//                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
//                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                p_parametros.put("niveli", 1);
                p_parametros.put("nivelf", 5);
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes2));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes3));
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
            case "CEDULA GASTOS PROGRAMAS (AÑO 2016)":
                if (utilitario.getAnio(fechaInicio.getFecha()) == 2016 && utilitario.getAnio(fechaFin.getFecha()) == 2016) {
                    String mes4 = "",
                            mes5 = "";
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
//                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
//                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                    p_parametros.put("niveli", 1);
                    p_parametros.put("nivelf", 6);
                    p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes4));
                    p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes5));
                    p_parametros.put("fechain", fechaInicio.getFecha() + "");
                    p_parametros.put("fechafi", fechaFin.getFecha() + "");
                    p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                    p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                    String fecha6;
                    if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                        fecha6 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    } else {
                        fecha6 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                    }
                    p_parametros.put("fechaRe", Integer.parseInt("1" + Integer.parseInt(fechaFin.getFecha().toString().substring(2, 4)) + "" + fecha6));
                    p_parametros.put("programa", Integer.parseInt(setPrograma1.getValorSeleccionado()));
                    TablaGenerica tabListas = admin.getDatosPrograma(Integer.parseInt(setPrograma1.getValorSeleccionado()));
                if (!tabListas.isEmpty()) {
                    p_parametros.put("descripcion", tabListas.getValor("NOMBMA"));
                }
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    System.err.println("Gastos Programas 2016 -> " + p_parametros);
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("Fecha seleccionada no corresponde a año del reporte", "");
                }
                break;
                 case "CEDULA INGRESOS CONSOLIDADOS":
                     
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
//                p_parametros.put("niveli", Integer.parseInt(cmbNivelInicial.getValue() + ""));
//                p_parametros.put("nivelf", Integer.parseInt(cmbNivelFinal.getValue() + ""));
                 p_parametros.put("niveli", 1);
                    p_parametros.put("nivelf", 6);
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
                case "CEDULA INGRESOS CONSOLIDADOS (AÑO 2016)":
                    if (utilitario.getAnio(fechaInicio.getFecha()) == 2016 && utilitario.getAnio(fechaFin.getFecha()) == 2016) {
                        String mes7,
                 mes8;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes7 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes7 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes8 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes8 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
//                p_parametros.put("niveli", Integer.parseInt(cmbNiveli.getValue() + ""));
//                p_parametros.put("nivelf", Integer.parseInt(cmbNivelf.getValue() + ""));
                 p_parametros.put("niveli", 1);
                    p_parametros.put("nivelf", 6);
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes7));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes8));
                p_parametros.put("fechain", fechaInicio.getFecha() + "");
                p_parametros.put("fechafi", fechaFin.getFecha() + "");
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                String fecha7;
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    fecha7 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    fecha7 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("fechaRe", Integer.parseInt("1" + Integer.parseInt(fechaFin.getFecha().toString().substring(2, 4)) + "" + fecha7));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                System.err.println("Ingreso Consolidado -> " + p_parametros);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("Fecha seleccionada no corresponde a año del reporte", "");
                }
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

    public SeleccionTabla getSetPrograma() {
        return setPrograma;
    }

    public void setSetPrograma(SeleccionTabla setPrograma) {
        this.setPrograma = setPrograma;
    }

    public Conexion getConOracle() {
        return conOracle;
    }

    public void setConOracle(Conexion conOracle) {
        this.conOracle = conOracle;
    }

    public ListaSeleccion getListReporte() {
        return listReporte;
    }

    public void setListReporte(ListaSeleccion listReporte) {
        this.listReporte = listReporte;
    }

    public SeleccionTabla getSetComprobantes() {
        return setComprobantes;
    }

    public void setSetComprobantes(SeleccionTabla setComprobantes) {
        this.setComprobantes = setComprobantes;
    }

    public SeleccionTabla getSetMovimientos() {
        return setMovimientos;
    }

    public void setSetMovimientos(SeleccionTabla setMovimientos) {
        this.setMovimientos = setMovimientos;
    }

    public SeleccionTabla getSetPrograma1() {
        return setPrograma1;
    }

    public void setSetPrograma1(SeleccionTabla setPrograma1) {
        this.setPrograma1 = setPrograma1;
    }
}

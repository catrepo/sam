/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

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
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_beans.Archivo;
import paq_financiero.presupuesto.ejb.ReporteCedulas;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ReporteContabilidad extends Pantalla {

    //Variable conexion
    private Conexion conOracle = new Conexion();
    private Conexion conBodega = new Conexion();
    //Declaracion de combos
    private Combo cmbAno = new Combo();
    private Combo cmbNiveli = new Combo();
    private Combo cmbNivelf = new Combo();
    //Creacion Calendarios
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Calendario fechaDesde = new Calendario();
    private Calendario fechaHasta = new Calendario();
    private Panel panOpcion = new Panel();
    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setComprobantes = new SeleccionTabla();
    private SeleccionTabla setMovimientos = new SeleccionTabla();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private VisualizarPDF vzpdfMovimiento = new VisualizarPDF();

    private Etiqueta etiCompania = new Etiqueta("Compañía : ");
    private Etiqueta etiTipo = new Etiqueta("Tipo Comprobante : ");
    private Etiqueta etiPeriodo = new Etiqueta("Periodo Comprobante : ");
    private Etiqueta etiNumero = new Etiqueta("Número Comprobante : ");

    private Etiqueta etiCompania1 = new Etiqueta("Compañía o Grupo : ");
    private Etiqueta etiNomInstitucion = new Etiqueta("MUNICIPIO DE RUMIÑAHUI");
    private Etiqueta etiDesde = new Etiqueta("Periodo Desde : ");
    private Etiqueta etiHasta = new Etiqueta("Periodo Hasta : ");
    private Etiqueta etiCuenta = new Etiqueta("Cuenta de Mayor : ");
    private Etiqueta etiDato = new Etiqueta("Tipo de Datos : ");

    private Texto txtCompania = new Texto();
    private Texto txtTipo = new Texto();
    private Texto txtPeriodo = new Texto();
    private Texto txtMess = new Texto();
    private Texto txtNumero = new Texto();

    private Texto txtCompania1 = new Texto();
    private Texto txtTipo1 = new Texto();
    private Texto txtDesde = new Texto();
    private Texto txtHasta = new Texto();
    private Texto txtCuenta = new Texto();

    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    String link = new String();
    private Boolean dentro;
    Archivo file = new Archivo();

    private Combo cmbTipo = new Combo();
    private Calendario fechDesde = new Calendario();
    private Calendario fechHasta = new Calendario();
    private Etiqueta etiTip = new Etiqueta("Tipo Comprobante : ");
    private Etiqueta etiInicio = new Etiqueta("Periodo Desde : ");
    private Etiqueta etiFinal = new Etiqueta("Periodo Hasta : ");
    private Etiqueta etiCia = new Etiqueta("Cia. o Grupo : ");
    private Etiqueta etiCia1 = new Etiqueta(" RB");
    private Dialogo diaParametro = new Dialogo();
    private Grid gridPa = new Grid();
    private Grid gridpa = new Grid();
    
    
    @EJB
    private ReporteCedulas admin = (ReporteCedulas) utilitario.instanciarEJB(ReporteCedulas.class);
    
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
        fechaDesde.setValue("2016/01/01");
        fechaHasta.setFechaActual();
        
        Grid griMovimiento = new Grid();
        griMovimiento.setColumns(1);
        Grid griMovAux = new Grid();
        griMovAux.setColumns(3);
        griMovAux.getChildren().add(etiCompania1);
        griMovAux.getChildren().add(txtCompania1);
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
        griMovimiento.getChildren().add(griMovAux);
        griMovimiento.getChildren().add(griMovAux1);
        griMovimiento.getChildren().add(griMovAux2);

        Boton botMovimiento = new Boton();
        botMovimiento.setId("botMovimiento");
        botMovimiento.setValue("Buscar");
        botMovimiento.setIcon("ui-icon-search");
        botMovimiento.setMetodo("buscaMovimiento");
        griMovimiento.getChildren().add(botMovimiento);

        setMovimientos.setId("setMovimientos");
        setMovimientos.getTab_seleccion().setConexion(conOracle);
        setMovimientos.setSeleccionTabla("select compromiso as id,compania,aniofecha as Año,mesfecha as Periodo,fecha,dttcom as Tipo_Cta,dtncom as Número_Cta,compromiso \n"
                + "from VT_CABECERA_COMPROBANTE_PAGO\n"
                + "where dtccia='Y'", "id");
        setMovimientos.setRadio();
        setMovimientos.getTab_seleccion().setRows(15);
//        setMovimientos.getTab_seleccion().getColumna("Número_Cta").setFiltro(true);
//        setMovimientos.getTab_seleccion().getColumna("compromiso").setFiltro(true);
        setMovimientos.getGri_cuerpo().setHeader(griMovimiento);
//        setMovimientos.setWidth("45");
        setMovimientos.getBot_aceptar().setMetodo("generarPDF");
        setMovimientos.setHeader("SALDOS Y MOVIMIENTOS DE MAYOR");
        agregarComponente(setMovimientos);

        vzpdfMovimiento.setId("vzpdfMovimiento");
        vzpdfMovimiento.setTitle("DETALLE DE MOVIMIENTOS");
        agregarComponente(vzpdfMovimiento);
        
        
        diaParametro.setId("diaParametro");
        diaParametro.setTitle("Consulta de Datos"); //titulo
        diaParametro.setWidth("30%"); //siempre en porcentajes  ancho
        diaParametro.setHeight("30%");//siempre porcentaje   alto
        diaParametro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaParametro.getBot_aceptar().setMetodo("dibujarReporte");
        gridpa.setColumns(4);
        agregarComponente(diaParametro);
        
         List listTipo = new ArrayList();
        Object fi1[] = {
            "D", "Ingresos"
        };
        Object fi2[] = {
            "H", "Egresos"
        };
        listTipo.add(fi1);;
        listTipo.add(fi2);;
        cmbTipo.setId("cmbTipo"); 
        cmbTipo.setCombo(listTipo);
        
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

    public void buscaMovimiento() {
        setMovimientos.getTab_seleccion().setSql("select compromiso as id,compania,aniofecha,mesfecha,fecha,dttcom,dtncom ,compromiso\n"
                + "from VT_CABECERA_COMPROBANTE_PAGO\n"
                + "where dtccia='" + txtCompania.getValue() + "'\n"
                + "and dtperi=" + "1" + txtPeriodo.getValue().toString().substring(2, 4) + "" +mes(Integer.parseInt(txtMess.getValue().toString())) + "" + "\n"
                + "and dttcom=" + txtTipo.getValue() + "\n"
                + "and dtclct = 'P'\n"
                + "and dtncom >=" + txtNumero.getValue() + "\n"
                + "order by dtncom,fecha");
        setMovimientos.getTab_seleccion().ejecutarSql();
//        setIngresos.getTab_seleccion().imprimirSql();
    }

    public void generarPDF() {
        if (setMovimientos.getValorSeleccionado() != null) {
            ///////////AQUI ABRE EL REPORTE
            Map parametros = new HashMap();
            parametros.put("p_desde", Integer.parseInt(setMovimientos.getValorSeleccionado()));
            parametros.put("p_hasta", Integer.parseInt(setMovimientos.getValorSeleccionado()));
            parametros.put("cuenta", Integer.parseInt(setMovimientos.getValorSeleccionado()));
            parametros.put("n_cuenta", Integer.parseInt(setMovimientos.getValorSeleccionado()));
            
//            p_parametros.put("p_usuario", tabConsulta.getValor("NICK_USUA") + "");
//
//            //System.out.println(" " + str_titulos);
            vzpdfMovimiento.setVisualizarPDF("rep_financiero/rep_saldos_movimientos_mayor.jasper", parametros);
            vzpdfMovimiento.dibujar();
            utilitario.addUpdate("vzpdfMovimiento");
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Solititud de compra", "");
        }
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
            case "COMPROBANTE DE PAGO":
                setComprobantes.dibujar();
                break;
            case "SALDOS Y MOVIMIENTOS DE MAYOR":
                setMovimientos.dibujar();
                break;
                case "BODEGA ARTICULOS CONTABILIDAD":
                diaParametro.setDialogo(gridpa);
                Grid gri = new Grid();
                gri.setColumns(2);
                gri.getChildren().add(etiCia);
                gri.getChildren().add(etiCia1);
                Grid griBusc = new Grid();
                griBusc.setColumns(2);
                griBusc.getChildren().add(etiTip);
                griBusc.getChildren().add(cmbTipo);
                
                griBusc.getChildren().add(etiInicio );
                griBusc.getChildren().add(fechDesde );
                
                griBusc.getChildren().add(etiFinal );
                griBusc.getChildren().add(fechHasta );
                
                gridPa.getChildren().add(griBusc);
                diaParametro.setDialogo(gridPa);
                diaParametro.dibujar();
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
                System.out.println("+"+p_parametros);
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
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
                break;
                case "BODEGA ARTICULOS CONTABILIDAD":
                 String mes2,
                 mes3;
                if (String.valueOf((utilitario.getMes(fechDesde.getFecha()))).length() > 1) {
                    mes2 = String.valueOf((utilitario.getMes(fechDesde.getFecha())));
                } else {
                    mes2 = "0" + String.valueOf((utilitario.getMes(fechDesde.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechHasta.getFecha()))).length() > 1) {
                    mes3 = String.valueOf((utilitario.getMes(fechHasta.getFecha())));
                } else {
                    mes3 = "0" + String.valueOf((utilitario.getMes(fechHasta.getFecha())));
                }
                p_parametros.put("fecIni", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechDesde.getFecha()))).substring(2, 4) + "" + mes2));
                p_parametros.put("fecFin", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechHasta.getFecha()))).substring(2, 4) + "" + mes3));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("tipo", cmbTipo.getValue() + "");
                if (cmbTipo.getValue().equals("H")){
                    p_parametros.put("descripcion", "EGRESOS");
                }else{
                    p_parametros.put("descripcion", "INGRESOS");
                }
                rep_reporte.cerrar();
                    System.err.println("parametro ->"+p_parametros);
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

    public VisualizarPDF getVzpdfMovimiento() {
        return vzpdfMovimiento;
    }

    public void setVzpdfMovimiento(VisualizarPDF vzpdfMovimiento) {
        this.vzpdfMovimiento = vzpdfMovimiento;
    }

    public Conexion getConBodega() {
        return conBodega;
    }

    public void setConBodega(Conexion conBodega) {
        this.conBodega = conBodega;
    }
    
}

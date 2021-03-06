/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.mergeDescuento;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class MesualizacionDecimos extends Pantalla {

    private Tabla tabDecimos = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Conexion conPostgres = new Conexion();
    private Panel panOpcion = new Panel();
    private Combo comboDistributivo = new Combo();
    private Combo comboDecim4to = new Combo();
    private Combo comboDecim4to1 = new Combo();
    private Combo comboDecim3ro = new Combo();
    private Combo comboDecim3ro1 = new Combo();
    private Combo comboDecim3ro2 = new Combo();
    private Combo comboDecim3ro3 = new Combo();
    private Combo comboAcciones = new Combo();
    private Combo comboEmpleados = new Combo();
    private Combo comboEmpleados1 = new Combo();
    private Combo comboEmpleados2 = new Combo();
    private Combo comboEmpleados3 = new Combo();
    private Combo comboEmpleados4 = new Combo();
    private Combo comboEmpleados5 = new Combo();
    private Combo comboEmpleados6 = new Combo();
    private Combo comboEmpleados7 = new Combo();
    private Combo comboAnio = new Combo();
    private Combo comboPeriodo = new Combo();
    private Combo comboAccion = new Combo();
    private Combo comboDecimo = new Combo();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Dialogo diaDialogosr = new Dialogo();
    private Dialogo diaDialogosr1 = new Dialogo();
    private Dialogo diaDialogosdr = new Dialogo();
    private Dialogo diaDialogosdr1 = new Dialogo();
    private Dialogo diaDialogosdr2 = new Dialogo();
    private Dialogo diaDialogosdr3 = new Dialogo();
    private Grid gridr = new Grid();
    private Grid gridr1 = new Grid();
    private Grid griddr = new Grid();
    private Grid griddr1 = new Grid();
    private Grid griddr2 = new Grid();
    private Grid griddr3 = new Grid();
    @EJB
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);

    public MesualizacionDecimos() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        comboDecim4to.setId("comboDecim4to");
        comboDecim4to.setConexion(conPostgres);
        comboDecim4to.setCombo("SELECT periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S' and periodo_columna = 'D4'");
        comboDecim4to.eliminarVacio();

        comboDecim3ro.setId("comboDecim3ro");
        comboDecim3ro.setConexion(conPostgres);
        comboDecim3ro.setCombo("SELECT periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S' and periodo_columna = 'D3'");
        comboDecim3ro.eliminarVacio();

        comboDecim4to1.setId("comboDecim4to1");
        comboDecim4to1.setConexion(conPostgres);
        comboDecim4to1.setCombo("SELECT periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S' and periodo_columna = 'D4'");
        comboDecim4to1.eliminarVacio();

        comboDecim3ro1.setId("comboDecim3ro1");
        comboDecim3ro1.setConexion(conPostgres);
        comboDecim3ro1.setCombo("SELECT periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S' and periodo_columna = 'D3'");
        comboDecim3ro1.eliminarVacio();

        comboDecim3ro2.setId("comboDecim3ro2");
        comboDecim3ro2.setConexion(conPostgres);
        comboDecim3ro2.setCombo("SELECT periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S' and periodo_columna = 'D3'");
        comboDecim3ro2.eliminarVacio();

        comboDecim3ro3.setId("comboDecim3ro3");
        comboDecim3ro3.setConexion(conPostgres);
        comboDecim3ro3.setCombo("SELECT periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S' and periodo_columna = 'D3'");
        comboDecim3ro3.eliminarVacio();

        comboEmpleados2.setId("comboEmpleados2");
        comboEmpleados2.setConexion(conPostgres);
        comboEmpleados2.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        comboEmpleados3.setId("comboEmpleados3");
        comboEmpleados3.setConexion(conPostgres);
        comboEmpleados3.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        comboEmpleados4.setId("comboEmpleados4");
        comboEmpleados4.setConexion(conPostgres);
        comboEmpleados4.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        comboEmpleados5.setId("comboEmpleados5");
        comboEmpleados5.setConexion(conPostgres);
        comboEmpleados5.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        comboEmpleados6.setId("comboEmpleados6");
        comboEmpleados6.setConexion(conPostgres);
        comboEmpleados6.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        comboEmpleados7.setId("comboEmpleados7");
        comboEmpleados7.setConexion(conPostgres);
        comboEmpleados7.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo where id_distributivo=2");

        //barra de tareas para busqueda y carga
        Grid gri_busca = new Grid();
        gri_busca.setColumns(8);
        gri_busca.getChildren().add(new Etiqueta("Acciones :"));
        comboAcciones.setId("comboAcciones");
        List list = new ArrayList();
        Object filas1[] = {
            "1", "INFORMACIÓN"
        };
        Object filas2[] = {
            "2", "MIGRAR A ROL"
        };
        Object filas3[] = {
            "3", "CONSULTAR"
        };
        list.add(filas1);;
        list.add(filas2);;
        list.add(filas3);;
        comboAcciones.setCombo(list);
        gri_busca.getChildren().add(comboAcciones);

        gri_busca.getChildren().add(new Etiqueta("Tipo Servidor :"));
        comboEmpleados1.setId("comboEmpleados1");
        comboEmpleados1.setConexion(conPostgres);
        comboEmpleados1.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
        gri_busca.getChildren().add(comboEmpleados1);

        gri_busca.getChildren().add(new Etiqueta("Tipo Columna :"));
        comboDistributivo.setId("comboDistributivo");
        comboDistributivo.setConexion(conPostgres);
        comboDistributivo.setCombo("select periodo_columna as columna,periodo_columna from srh_periodo_sueldo where periodo_estado = 'S'");
        gri_busca.getChildren().add(comboDistributivo);

        Boton bot = new Boton();
        bot.setValue("Ejecutar");
        bot.setIcon("ui-icon-extlink");
        bot.setMetodo("cargaInfo");
        gri_busca.getChildren().add(bot);
        bar_botones.agregarComponente(gri_busca);

        //tabla de registro, formulario que se llena
        tabDecimos.setId("tabDecimos");
        tabDecimos.setConexion(conPostgres);
        tabDecimos.setTabla("srh_decimo_cuarto_tercero", "decimo_id", 1);
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "SI"
        };
        Object fila2[] = {
            "0", "NO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabDecimos.getColumna("decimo_estado").setRadio(lista, "1");
        tabDecimos.getColumna("decimo_cod_empleado").setLectura(true);
        tabDecimos.getColumna("decimo_id_distributivo").setVisible(false);
        tabDecimos.getColumna("decimo_fecha").setVisible(false);
        tabDecimos.getColumna("decimo_id").setVisible(false);
        tabDecimos.getColumna("decimo_empleado").setFiltro(true);
        tabDecimos.setRows(18);
        tabDecimos.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDecimos);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setTitle("LISTADO DE CALCULO DE DECIMOS ACUMULADOS/PAGO MENSUAL");
        panOpcion.getChildren().add(pnt);
        agregarComponente(panOpcion);

        //para reportes
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        diaDialogosr.setId("diaDialogosr");
        diaDialogosr.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosr.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogosr.setHeight("20%");//siempre porcentaje   alto
        diaDialogosr.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosr.getBot_aceptar().setMetodo("dibujarReporte");
        gridr.setColumns(4);
        agregarComponente(diaDialogosr);

        diaDialogosr1.setId("diaDialogosr1");
        diaDialogosr1.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosr1.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogosr1.setHeight("20%");//siempre porcentaje   alto
        diaDialogosr1.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosr1.getBot_aceptar().setMetodo("dibujarReporte");
        gridr1.setColumns(4);
        agregarComponente(diaDialogosr1);

        diaDialogosdr.setId("diaDialogosdr");
        diaDialogosdr.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosdr.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogosdr.setHeight("20%");//siempre porcentaje   alto
        diaDialogosdr.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosdr.getBot_aceptar().setMetodo("dibujarReporte");
        griddr.setColumns(4);
        agregarComponente(diaDialogosdr);

        diaDialogosdr1.setId("diaDialogosdr1");
        diaDialogosdr1.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosdr1.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogosdr1.setHeight("20%");//siempre porcentaje   alto
        diaDialogosdr1.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosdr1.getBot_aceptar().setMetodo("dibujarReporte");
        griddr1.setColumns(4);
        agregarComponente(diaDialogosdr1);

        diaDialogosdr2.setId("diaDialogosdr2");
        diaDialogosdr2.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosdr2.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogosdr2.setHeight("20%");//siempre porcentaje   alto
        diaDialogosdr2.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosdr2.getBot_aceptar().setMetodo("dibujarReporte");
        griddr2.setColumns(4);
        agregarComponente(diaDialogosdr2);

        diaDialogosdr3.setId("diaDialogosdr3");
        diaDialogosdr3.setTitle("PARAMETROS DE REPORTE"); //titulo
        diaDialogosdr3.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogosdr3.setHeight("20%");//siempre porcentaje   alto
        diaDialogosdr3.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogosdr3.getBot_aceptar().setMetodo("dibujarReporte");
        griddr3.setColumns(4);
        agregarComponente(diaDialogosdr3);

        comboEmpleados.setId("comboEmpleados");
        comboEmpleados.setConexion(conPostgres);
        comboEmpleados.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        comboAnio.setId("comboAnio");
        comboAnio.setConexion(conPostgres);
        comboAnio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");

        comboPeriodo.setId("comboPeriodo");
        comboPeriodo.setConexion(conPostgres);
        comboPeriodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");

        comboAccion.setId("comboAccion");
        List lis = new ArrayList();
        Object fi1[] = {
            "1", "Acumula"
        };
        Object fi2[] = {
            "0", "Pago Mes"
        };
        lis.add(fi1);;
        lis.add(fi2);;
        comboAccion.setCombo(lis);

        comboDecimo.setId("comboDecimo");
        List liss = new ArrayList();
        Object fis1[] = {
            "1", "Decimo Tercero"
        };
        Object fis2[] = {
            "2", "Decimo Cuarto"
        };
        liss.add(fis1);;
        liss.add(fis2);;
        comboDecimo.setCombo(liss);

        actuliLista();
    }

    public void cargaInfo() {
        if (comboAcciones.getValue().equals("1")) {//Llenado de formulario
            if (comboDistributivo.getValue().equals("D3")) {
                decimo3ro();
            } else if (comboDistributivo.getValue().equals("D4")) {
                decimo4to();
            }
        } else if (comboAcciones.getValue()
                .equals("2")) {//Subida a Roles
            setMigraRoles();
        } else if (comboAcciones.getValue()
                .equals("3")) {
            filtarLista();
        } else {
            utilitario.agregarMensaje("Debe escoger una Acción a realizar", "");
        }
    }

    public void decimo3ro() {
        Integer fecha = 0;
        fecha = utilitario.getMes(utilitario.getFechaActual());
        Integer valorDias = 0;
        String columna = "", autoriza = "";
        double rmu = 0.0, valac = 0.0, hxe = 0.0, sbr = 0.0, total = 0.0, valacp = 0.0, valato = 0.0,
                d3rtab, d3rrol, hxetab = 0.0, hxerol = 0.0, sbrtab = 0.0, sbrol = 0.0, totalacu = 0.0, totaltab = 0.0;
        BigDecimal bd;
        TablaGenerica tabDato = mDescuento.getInfoAcumulacion(comboEmpleados1.getValue() + "");
        if (!tabDato.isEmpty()) {
            TablaGenerica tabFecha = mDescuento.getPeriodos(comboDistributivo.getValue() + "");
            if (!tabFecha.isEmpty()) {
                if (comboEmpleados1.getValue().equals("1")) {
                    columna = "15";
                } else if (comboEmpleados1.getValue().equals("2")) {
                    columna = "42";
                }
                for (int i = 0; i < tabDato.getTotalFilas(); i++) {
                    TablaGenerica tabDatos = mDescuento.getInfoListaPago(tabDato.getValor(i, "autoriza_cod_empleado"), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),
                            String.valueOf(utilitario.getMes(utilitario.getFechaActual())), columna);
                    if (!tabDatos.isEmpty()) {
                    } else {
                        if (tabDato.getValor(i, "autoriza_decimo_cuarto").equals("1")) {
                            TablaGenerica tabD3roDife = mDescuento.getDeci3ro(tabDato.getValor(i, "autoriza_cod_empleado"));
                            if (!tabD3roDife.isEmpty()) {
                                rmu = Double.parseDouble(tabD3roDife.getValor("rmu"));
                                hxe = Double.parseDouble(tabD3roDife.getValor("hxe"));
                                sbr = Double.parseDouble(tabD3roDife.getValor("sbr"));
                                total = rmu + hxe + sbr;
                                if (fecha.compareTo(11) == 0) {
                                    autoriza = "0";
                                    if (calcularMeses(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 12) {
                                        TablaGenerica tabD3rAcuAn = mDescuento.getDeci3roAcum(columna, utilitario.getAnio(utilitario.getFechaActual()),
                                                (utilitario.getAnio(utilitario.getFechaActual()) - 1), "1,2,3,4,5,6,7,8,9,10,11", "12");
                                        if (!tabD3rAcuAn.isEmpty()) {
                                            d3rrol = Double.parseDouble(tabD3rAcuAn.getValor("rmu_acum"));
                                            hxerol = Double.parseDouble(tabD3rAcuAn.getValor("hxe_acum"));
                                            sbrol = Double.parseDouble(tabD3rAcuAn.getValor("sbr_acum"));
                                            totaltab = d3rrol + hxerol + sbrol;
                                        }
                                        bd = new BigDecimal(totaltab / 12);
                                        valacp = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        TablaGenerica tabD3rAcuAn = mDescuento.getDeci3roAcum(columna, utilitario.getAnio(utilitario.getFechaActual()),
                                                (utilitario.getAnio(utilitario.getFechaActual()) - 1), "1,2,3,4,5,6,7,8,9,10,11", "0");
                                        if (!tabD3rAcuAn.isEmpty()) {
                                            d3rrol = Double.parseDouble(tabD3rAcuAn.getValor("rmu_acum"));
                                            hxerol = Double.parseDouble(tabD3rAcuAn.getValor("hxe_acum"));
                                            sbrol = Double.parseDouble(tabD3rAcuAn.getValor("sbr_acum"));
                                            totaltab = d3rrol + hxerol + sbrol;
                                        }
                                        bd = new BigDecimal(totaltab / 12);
                                        valacp = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                } else {
                                    autoriza = tabDato.getValor(i, "autoriza_decimo_tercero");
                                    if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                        bd = new BigDecimal(total / 12);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        valorDias = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                        bd = new BigDecimal((total / 360) * valorDias);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                }
                                valato = valacp + valac;
                                mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, valato, autoriza, rmu, hxe, sbr, tabDato.getValor(i, "autoriza_id_distributivo"), valacp, valac);
                            }
                        } else {
                            TablaGenerica tabD3roDife = mDescuento.getDeci3ro(tabDato.getValor(i, "autoriza_cod_empleado"));
                            if (!tabD3roDife.isEmpty()) {
                                rmu = Double.parseDouble(tabD3roDife.getValor("rmu"));
                                hxe = Double.parseDouble(tabD3roDife.getValor("hxe"));
                                sbr = Double.parseDouble(tabD3roDife.getValor("sbr"));
                                total = rmu + hxe + sbr;
                                if (fecha.compareTo(11) == 0) {
                                    if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                        bd = new BigDecimal(total / 12);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        valorDias = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                        bd = new BigDecimal((total / 360) * valorDias);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                    if (calcularMeses(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 12) {
                                        TablaGenerica tabD3rAcu = mDescuento.getDeciAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_decimo_cuarto"), columna, tabFecha.getValor("periodo_fecha_inicial"), tabFecha.getValor("periodo_fecha_final"));
                                        if (!tabD3rAcu.isEmpty()) {
                                            d3rtab = Double.parseDouble(tabD3rAcu.getValor("acumulado_rmu"));
                                            hxetab = Double.parseDouble(tabD3rAcu.getValor("acumulado_hxe"));
                                            sbrtab = Double.parseDouble(tabD3rAcu.getValor("acumulado_sbr"));
                                            totalacu = d3rtab + hxetab + sbrtab;
                                        }
                                        TablaGenerica tabD3rAcuAn = mDescuento.getDeci3roAcum(columna, utilitario.getAnio(utilitario.getFechaActual()),
                                                (utilitario.getAnio(utilitario.getFechaActual()) - 1), "1,2,3,4,5,6,7,8,9,10,11", "12");
                                        if (!tabD3rAcuAn.isEmpty()) {
                                            d3rrol = Double.parseDouble(tabD3rAcuAn.getValor("rmu_acum"));
                                            hxerol = Double.parseDouble(tabD3rAcuAn.getValor("hxe_acum"));
                                            sbrol = Double.parseDouble(tabD3rAcuAn.getValor("sbr_acum"));
                                            totaltab = d3rrol + hxerol + sbrol;
                                        }
                                        bd = new BigDecimal(totaltab - totalacu);
                                        valacp = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();

                                    } else {
                                        TablaGenerica tabD3rAcu = mDescuento.getDeciAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_decimo_cuarto"), columna, tabFecha.getValor("periodo_fecha_inicial"), tabFecha.getValor("periodo_fecha_final"));
                                        if (!tabD3rAcu.isEmpty()) {
                                            d3rtab = Double.parseDouble(tabD3rAcu.getValor("acumulado_rmu"));
                                            hxetab = Double.parseDouble(tabD3rAcu.getValor("acumulado_hxe"));
                                            sbrtab = Double.parseDouble(tabD3rAcu.getValor("acumulado_sbr"));
                                            totalacu = d3rtab + hxetab + sbrtab;
                                        }
                                        TablaGenerica tabD3rAcuAn = mDescuento.getDeci3roAcum(columna, utilitario.getAnio(utilitario.getFechaActual()),
                                                (utilitario.getAnio(utilitario.getFechaActual()) - 1), "1,2,3,4,5,6,7,8,9,10,11", "0");
                                        if (!tabD3rAcuAn.isEmpty()) {
                                            d3rrol = Double.parseDouble(tabD3rAcuAn.getValor("rmu_acum"));
                                            hxerol = Double.parseDouble(tabD3rAcuAn.getValor("hxe_acum"));
                                            sbrol = Double.parseDouble(tabD3rAcuAn.getValor("sbr_acum"));
                                            totaltab = d3rrol + hxerol + sbrol;
                                        }
                                        bd = new BigDecimal((totaltab - totalacu) + valac);
                                        valacp = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                } else {
                                    if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                        bd = new BigDecimal(total / 12);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        valorDias = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                        bd = new BigDecimal((total / 360) * valorDias);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                }
                                valato = valacp + valac;
                                mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, valato, tabDato.getValor(i, "autoriza_decimo_tercero"), rmu, hxe, sbr, tabDato.getValor(i, "autoriza_id_distributivo"), valacp, valac);
                            }
                        }
                    }
                }
            } else {
                utilitario.agregarMensajeInfo("No existe Periodo Activo", null);
            }
            tabDecimos.actualizar();
            filtarLista();
        }

    }

    public void decimo4to() {
        Integer fecha = 0;
        fecha = utilitario.getMes(utilitario.getFechaActual());
        Integer valorDias = 0;
        String columna = "", autoriza = "";
        double d4t = 0.0, rmu = 0.0, valac = 0.0, valac1 = 0.0, acumulado = 0.0;
        BigDecimal bd, bd1;
        TablaGenerica tabDato = mDescuento.getInfoAcumulacion(comboEmpleados1.getValue() + "");
        if (!tabDato.isEmpty()) {
            TablaGenerica tabFecha = mDescuento.getPeriodos(comboDistributivo.getValue() + "");
            if (!tabFecha.isEmpty()) {
                if (comboEmpleados1.getValue().equals("1")) {
                    columna = "16";
                } else if (comboEmpleados1.getValue().equals("2")) {
                    columna = "43";
                }
                for (int i = 0; i < tabDato.getTotalFilas(); i++) {
                    rmu = Double.parseDouble(tabDato.getValor(i, "remuneracion"));
                    TablaGenerica tabDatos = mDescuento.getInfoListaPago(tabDato.getValor(i, "autoriza_cod_empleado"), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),
                            String.valueOf(utilitario.getMes(utilitario.getFechaActual())), columna);
                    if (!tabDatos.isEmpty()) {
                    } else {
                        if (tabDato.getValor(i, "autoriza_decimo_cuarto").equals("1")) {
                            TablaGenerica tabD4T = mDescuento.getCalculoD4T(tabDato.getValor(i, "autoriza_cod_empleado"));
                            if (!tabD4T.isEmpty()) {
                                d4t = Double.parseDouble(tabD4T.getValor("sbu"));
                                if (fecha.compareTo(7) == 0) {// se paga en agosto
                                    autoriza = "0";
                                    if (calcularMeses(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 12) {
                                        bd = new BigDecimal(d4t);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        valorDias = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                        bd = new BigDecimal((d4t / 360) * valorDias);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                } else {
                                    autoriza = tabDato.getValor(i, "autoriza_decimo_cuarto");
                                    if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                            new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                        bd = new BigDecimal(d4t / 12);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    } else {
                                        valorDias = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                        bd = new BigDecimal((d4t / 360) * valorDias);
                                        valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                }
                            }
                            mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, valac, autoriza, rmu, 0.0, 0.0, tabDato.getValor(i, "autoriza_id_distributivo"), 0.0, valac);
                        } else {
                            TablaGenerica tabD4T = mDescuento.getCalculoD4T(tabDato.getValor(i, "autoriza_cod_empleado"));
                            if (!tabD4T.isEmpty()) {
                                TablaGenerica tabD4tDife = mDescuento.getDeciAcumulado(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_decimo_cuarto"), columna, tabFecha.getValor("periodo_fecha_inicial"), tabFecha.getValor("periodo_fecha_final"));
                                if (!tabD4tDife.isEmpty()) {
                                    d4t = Double.parseDouble(tabD4T.getValor("sbu"));
                                    if (fecha.compareTo(7) == 0) {//se paga en julio
                                        acumulado = Double.parseDouble(tabD4tDife.getValor("acumulado_decimo"));
                                        if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                            bd = new BigDecimal(d4t / 12);
                                            valac1 = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                            bd1 = new BigDecimal((d4t - Double.parseDouble(tabD4tDife.getValor("acumulado_decimo"))));
                                            valac = bd1.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        } else {
                                            valorDias = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                    new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                            bd = new BigDecimal((d4t / 360) * valorDias);
                                            valac1 = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                            bd1 = new BigDecimal((d4t - Double.parseDouble(tabD4tDife.getValor("acumulado_decimo"))));
                                            valac = bd1.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        }
                                    } else {
                                        valac1 = 0.0;
                                        acumulado = Double.parseDouble(tabD4tDife.getValor("acumulado_decimo"));
                                        if (calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) >= 30) {
                                            bd = new BigDecimal(d4t / 12);
                                            valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        } else {
                                            valorDias = calcularDias(new GregorianCalendar(utilitario.getAnio(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getMes(tabDato.getValor(i, "autoriza_fecha_ingreso")), utilitario.getDia(tabDato.getValor(i, "autoriza_fecha_ingreso"))),
                                                    new GregorianCalendar(utilitario.getAnio(utilitario.getFechaActual()), utilitario.getMes(utilitario.getFechaActual()), 30)) + 1;
                                            bd = new BigDecimal((d4t / 360) * valorDias);
                                            valac = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();

                                        }
                                    }
                                }
                            }
                            mDescuento.setDatosCalculo(tabDato.getValor(i, "autoriza_cod_empleado"), tabDato.getValor(i, "autoriza_empleado"), columna, valac, tabDato.getValor(i, "autoriza_decimo_cuarto"), rmu, 0.0, 0.0, tabDato.getValor(i, "autoriza_id_distributivo"), acumulado, valac1);
                        }
                    }
                }
            } else {
                utilitario.agregarMensajeInfo("No existe Periodo Activo", null);
            }
            tabDecimos.actualizar();
            filtarLista();
        }
    }

    public void setMigraRoles() {
        TablaGenerica tabDato = mDescuento.VerificarRol();
        if (!tabDato.isEmpty()) {
            for (int i = 0; i < tabDecimos.getTotalFilas(); i++) {
                if (tabDecimos.getValor(i, "decimo_estado").equals("0")) {
                    TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabDecimos.getValor(i, "decimo_anio"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")),
                            tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")));
                    if (!tabDatos.isEmpty()) {
                        mDescuento.setmigrarDescuento(tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_columna")), tabConsulta.getValor("NICK_USUA"), "valor_ingreso", Integer.parseInt(tabDecimos.getValor(i, "decimo_anio")), Double.valueOf(tabDecimos.getValor(i, "decimo_valor")));
                        utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
                    } else {
                        utilitario.agregarMensaje("Datos No Concuerdan en el Rol", tabDecimos.getValor(i, "decimo_empleado"));
                    }
                } else {
                    TablaGenerica tabDatos = mDescuento.getConfirmaDatos(tabDecimos.getValor(i, "decimo_anio"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")),
                            tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")));
                    if (!tabDatos.isEmpty()) {
                        mDescuento.setmigrarDescuento(tabDecimos.getValor(i, "decimo_cod_empleado"), Integer.parseInt(tabDecimos.getValor(i, "decimo_periodo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_id_distributivo")), Integer.parseInt(tabDecimos.getValor(i, "decimo_columna")), tabConsulta.getValor("NICK_USUA"), "valor_ingreso", Integer.parseInt(tabDecimos.getValor(i, "decimo_anio")), Double.valueOf(0.0));
                        utilitario.agregarMensaje("REGISTRO SUBIDO CON EXITO A ROLES", " ");
                    } else {
                        utilitario.agregarMensaje("Datos No Concuerdan en el Rol", tabDecimos.getValor(i, "decimo_empleado"));
                    }
                }
            }
        } else {
            utilitario.agregarNotificacionInfo("Descuento No Puede Ser Subido a Rol", "Periodo de Rol Aun No Esta Creado");
        }
    }

    public static int calcularDias(Calendar cal1, Calendar cal2) {
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();//fecha actual
        long milis2 = cal2.getTimeInMillis();//fecha futura
        long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
        long diffSeconds = diff / 1000; // calcular la diferencia en segundos
        long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
        long diffHours = diffMinutes / 60; // calcular la diferencia en horas a
        long diffDays = diffHours / 24; // calcular la diferencia en dias
        return Integer.parseInt(String.valueOf(diffDays));
    }

    public static int calcularMeses(Calendar cal1, Calendar cal2) {
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();//fecha actual
        long milis2 = cal2.getTimeInMillis();//fecha futura
        long diff = milis2 - milis1;	 // calcular la diferencia en milisengundos
        long diffSeconds = diff / 1000; // calcular la diferencia en segundos
        long diffMinutes = diffSeconds / 60; // calcular la diferencia en minutos
        long diffHours = diffMinutes / 60; // calcular la diferencia en horas a
        long diffDays = diffHours / 24; // calcular la diferencia en dias
        long diffWeek = diffDays / 7; // calcular la diferencia en semanas
        long diffMounth = diffWeek / 4; // calcular la diferencia en meses
        return Integer.parseInt(String.valueOf(diffMounth));
    }

    public void actuliLista() {
        if (!getFiltro().isEmpty()) {
            tabDecimos.setCondicion(getFiltro());
            tabDecimos.ejecutarSql();
            utilitario.addUpdate("tabDecimos");
        }
    }

    private String getFiltro() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tabDecimos.getValorSeleccionado() != null) {
            str_filtros = "decimo_anio ='" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual())) + "'"
                    + "and decimo_periodo = '" + String.valueOf(utilitario.getMes(utilitario.getFechaActual())) + "'";

        } else {
            utilitario.agregarMensajeInfo("No ahi informacion disponible",
                    "");
        }
        return str_filtros;
    }

    public void filtarLista() {
        if (!getFiltroLista().isEmpty()) {
            tabDecimos.setCondicion(getFiltroLista());
            tabDecimos.ejecutarSql();
            utilitario.addUpdate("tabDecimos");
        }
    }

    private String getFiltroLista() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "", valor = "";
        if (tabDecimos.getValorSeleccionado() != null) {
            if (comboEmpleados1.getValue().equals("1")) {
                if (comboDistributivo.getValue().equals("D3")) {
                    valor = "15";
                } else if (comboDistributivo.getValue().equals("D4")) {
                    valor = "16";
                }
            } else if (comboEmpleados1.getValue().equals("2")) {
                if (comboDistributivo.getValue().equals("D3")) {
                    valor = "42";
                } else if (comboDistributivo.getValue().equals("D4")) {
                    valor = "43";
                }
            }
            str_filtros = "decimo_anio ='" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual())) + "'"
                    + "and decimo_periodo = '" + String.valueOf(utilitario.getMes(utilitario.getFechaActual())) + "' and "
                    + "decimo_id_distributivo = '" + comboEmpleados1.getValue() + "' "
                    + "and decimo_columna ='" + valor + "'";

        } else {
            utilitario.agregarMensajeInfo("No ahi informacion disponible",
                    "");
        }
        return str_filtros;
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

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "DECIMO 4TO":
                diaDialogosr.Limpiar();
                Grid griBusca = new Grid();
                griBusca.setColumns(2);
                griBusca.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                griBusca.getChildren().add(comboEmpleados2);
                griBusca.getChildren().add(new Etiqueta("TIPO :"));
                griBusca.getChildren().add(comboDecim4to);
                gridr.getChildren().add(griBusca);
                diaDialogosr.setDialogo(gridr);
                diaDialogosr.dibujar();
                break;
            case "DECIMO 4TO PARTIDA":
                diaDialogosr1.Limpiar();
                Grid griBuscap = new Grid();
                griBuscap.setColumns(2);
                griBuscap.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                griBuscap.getChildren().add(comboEmpleados4);
                griBuscap.getChildren().add(new Etiqueta("TIPO :"));
                griBuscap.getChildren().add(comboDecim4to1);
                gridr1.getChildren().add(griBuscap);
                diaDialogosr1.setDialogo(gridr1);
                diaDialogosr1.dibujar();
                break;
            case "DECIMO 3RO":
                diaDialogosdr.Limpiar();
                Grid griBusc = new Grid();
                griBusc.setColumns(2);
                griBusc.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                griBusc.getChildren().add(comboEmpleados3);
                griBusc.getChildren().add(new Etiqueta("TIPO :"));
                griBusc.getChildren().add(comboDecim3ro);
                griddr.getChildren().add(griBusc);
                diaDialogosdr.setDialogo(griddr);
                diaDialogosdr.dibujar();
                break;
            case "DECIMO 3RO PARTIDA":
                diaDialogosdr1.Limpiar();
                Grid griBuscp = new Grid();
                griBuscp.setColumns(2);
                griBuscp.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                griBuscp.getChildren().add(comboEmpleados5);
                griBuscp.getChildren().add(new Etiqueta("TIPO :"));
                griBuscp.getChildren().add(comboDecim3ro1);
                griddr1.getChildren().add(griBuscp);
                diaDialogosdr1.setDialogo(griddr1);
                diaDialogosdr1.dibujar();
                break;
            case "DECIMO 3RO PARTIDA DETALLE":
                diaDialogosdr2.Limpiar();
                Grid griBu = new Grid();
                griBu.setColumns(2);
                griBu.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                griBu.getChildren().add(comboEmpleados6);
                griBu.getChildren().add(new Etiqueta("TIPO :"));
                griBu.getChildren().add(comboDecim3ro2);
                griddr2.getChildren().add(griBu);
                diaDialogosdr2.setDialogo(griddr2);
                diaDialogosdr2.dibujar();
                break;
            case "DECIMO 3RO PARTIDA HRX TRABAJADOR":
                diaDialogosdr3.Limpiar();
                Grid griBu1 = new Grid();
                griBu1.setColumns(2);
                griBu1.getChildren().add(new Etiqueta("DISTRIBUTIVO :"));
                griBu1.getChildren().add(comboEmpleados7);
                griBu1.getChildren().add(new Etiqueta("TIPO :"));
                griBu1.getChildren().add(comboDecim3ro3);
                griddr3.getChildren().add(griBu1);
                diaDialogosdr3.setDialogo(griddr3);
                diaDialogosdr3.dibujar();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "DECIMO 4TO":
                TablaGenerica tabDatos = mDescuento.getPeriodos(comboDecim4to.getValue() + "");
                if (!tabDatos.isEmpty()) {
                    String columna = "";
                    if (comboEmpleados2.getValue().equals("1")) {
                        if (comboDecim4to.getValue().equals("D3")) {
                            columna = "15";
                        } else if (comboDecim4to.getValue().equals("D4")) {
                            columna = "16";
                        }
                    } else if (comboEmpleados2.getValue().equals("2")) {
                        if (comboDecim4to.getValue().equals("D3")) {
                            columna = "42";
                        } else if (comboDecim4to.getValue().equals("D4")) {
                            columna = "43";
                        }
                    }
                    p_parametros.put("id_distributivo", comboEmpleados2.getValue() + "");
                    p_parametros.put("ide_distributivo", Integer.parseInt(comboEmpleados2.getValue() + ""));
                    p_parametros.put("columna", columna + "");
                    p_parametros.put("fecha_inicio", tabDatos.getValor("periodo_fecha_inicial") + "");
                    p_parametros.put("fecha_fin", tabDatos.getValor("periodo_fecha_final") + "");
                    p_parametros.put("descripcion", tabDatos.getValor("periodo_columna") + "");
                    p_parametros.put("anio", utilitario.getAnio(tabDatos.getValor("periodo_fecha_final") + ""));
                    p_parametros.put("fecha_decimo", utilitario.getDia(tabDatos.getValor("periodo_fecha_final")) + "-" + utilitario.getMes(tabDatos.getValor("periodo_fecha_final")) + "-" + utilitario.getAnio(tabDatos.getValor("periodo_fecha_final")));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;
            case "DECIMO 4TO PARTIDA":
                TablaGenerica tabDatos1 = mDescuento.getPeriodos(comboDecim4to1.getValue() + "");
                if (!tabDatos1.isEmpty()) {
                    String columna = "";
                    if (comboEmpleados4.getValue().equals("1")) {
                        if (comboDecim4to1.getValue().equals("D3")) {
                            columna = "15";
                        } else if (comboDecim4to1.getValue().equals("D4")) {
                            columna = "16";
                        }
                    } else if (comboEmpleados4.getValue().equals("2")) {
                        if (comboDecim4to1.getValue().equals("D3")) {
                            columna = "42";
                        } else if (comboDecim4to1.getValue().equals("D4")) {
                            columna = "43";
                        }
                    }
                    p_parametros.put("id_distributivo", comboEmpleados4.getValue() + "");
                    p_parametros.put("ide_distributivo", Integer.parseInt(comboEmpleados4.getValue() + ""));
                    p_parametros.put("columna", columna + "");
                    p_parametros.put("fecha_inicio", tabDatos1.getValor("periodo_fecha_inicial") + "");
                    p_parametros.put("fecha_fin", tabDatos1.getValor("periodo_fecha_final") + "");
                    p_parametros.put("descripcion", tabDatos1.getValor("periodo_columna") + "");
                    p_parametros.put("anio", utilitario.getAnio(tabDatos1.getValor("periodo_fecha_final") + ""));
                    p_parametros.put("fecha_decimo", utilitario.getDia(tabDatos1.getValor("periodo_fecha_final")) + "-" + utilitario.getMes(tabDatos1.getValor("periodo_fecha_final")) + "-" + utilitario.getAnio(tabDatos1.getValor("periodo_fecha_final")));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;

            case "DECIMO 3RO":
                mDescuento.borrarDecimo();
                TablaGenerica tabDato = mDescuento.getPeriodos(comboDecim3ro.getValue() + "");
                if (!tabDato.isEmpty()) {
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados3.getValue() + ""), tabDato.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + "")) - 1), 14, 40);

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados3.getValue() + ""), tabDato.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""), 14, 40);

                    if (comboEmpleados3.getValue().equals("2")) {
                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados3.getValue() + ""), tabDato.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDato.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + "")) - 1), 14, 40);
                    }
                    Integer columna = 0,
                            column = 0;
                    if (comboEmpleados3.getValue().equals("1")) {
                        columna = 18;
                        column = 0;
                    } else if (comboEmpleados3.getValue().equals("2")) {
                        columna = 75;
                        column = 76;
                    }
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados3.getValue() + ""), tabDato.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + "")) - 1), columna, column);

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados3.getValue() + ""), tabDato.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""), columna, column);
                    if (comboEmpleados3.getValue().equals("2")) {

                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados3.getValue() + ""), tabDato.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDato.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + "")) - 1), columna, column);
                    }
//                    String columna = "";
//                    if (comboEmpleados3.getValue().equals("1")) {
//                        if (comboDecim3ro.getValue().equals("D3")) {
//                            columna = "15";
//                        } else if (comboDecim3ro.getValue().equals("D4")) {
//                            columna = "16";
//                        }
//                    } else if (comboEmpleados3.getValue().equals("2")) {
//                        if (comboDecim3ro.getValue().equals("D3")) {
//                            columna = "42";
//                        } else if (comboDecim3ro.getValue().equals("D4")) {
//                            columna = "43";
//                        }
//                    }
//                    p_parametros.put("id_distributivo", comboEmpleados3.getValue() + "");
                    p_parametros.put("ide_distributivo", Integer.parseInt(comboEmpleados3.getValue() + ""));
//                    p_parametros.put("columna", columna + "");
                    p_parametros.put("fecha_inicio", tabDato.getValor("periodo_fecha_inicial") + "");
                    p_parametros.put("fecha_fin", tabDato.getValor("periodo_fecha_final") + "");
                    p_parametros.put("descripcion", tabDato.getValor("periodo_columna") + "");
//                    p_parametros.put("anio", utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""));
                    p_parametros.put("anioa", utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + ""));
//                    p_parametros.put("anioan", (utilitario.getAnio(tabDato.getValor("periodo_fecha_final") + "")) - 1);
//                    p_parametros.put("fecha_decimo", utilitario.getDia(tabDato.getValor("periodo_fecha_final")) + "-" + utilitario.getMes(tabDato.getValor("periodo_fecha_final")) + "-" + utilitario.getAnio(tabDato.getValor("periodo_fecha_final")));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;


            case "DECIMO 3RO PARTIDA":
                mDescuento.borrarDecimo();
                TablaGenerica tabDato2 = mDescuento.getPeriodos(comboDecim3ro1.getValue() + "");
                if (!tabDato2.isEmpty()) {
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados5.getValue() + ""), tabDato2.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato2.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + "")) - 1), 14, 40);

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados5.getValue() + ""), tabDato2.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato2.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""), 14, 40);
                    if (comboEmpleados5.getValue().equals("2")) {
                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados5.getValue() + ""), tabDato2.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDato2.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + "")) - 1), 14, 40);
                    }
                    Integer columna = 0,
                            column = 0;
                    if (comboEmpleados5.getValue().equals("1")) {
                        columna = 18;
                        column = 0;
                    } else if (comboEmpleados5.getValue().equals("2")) {
                        columna = 75;
                        column = 76;
                    }
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados5.getValue() + ""), tabDato2.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato2.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + "")) - 1), columna, column);

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados5.getValue() + ""), tabDato2.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato2.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""), columna, column);

                    if (comboEmpleados5.getValue().equals("2")) {
                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados5.getValue() + ""), tabDato2.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDato2.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + "")) - 1), columna, column);
                    }

//                    String columna = "";
//                    if (comboEmpleados5.getValue().equals("1")) {
//                        if (comboDecim3ro1.getValue().equals("D3")) {
//                            columna = "15";
//                        } else if (comboDecim3ro1.getValue().equals("D4")) {
//                            columna = "16";
//                        }
//                    } else if (comboEmpleados5.getValue().equals("2")) {
//                        if (comboDecim3ro1.getValue().equals("D3")) {
//                            columna = "42";
//                        } else if (comboDecim3ro1.getValue().equals("D4")) {
//                            columna = "43";
//                        }
//                    }
//                    p_parametros.put("id_distributivo", comboEmpleados5.getValue() + "");
                    p_parametros.put("ide_distributivo", Integer.parseInt(comboEmpleados5.getValue() + ""));
//                    p_parametros.put("columna", columna + "");
                    p_parametros.put("fecha_inicio", tabDato2.getValor("periodo_fecha_inicial") + "");
                    p_parametros.put("fecha_fin", tabDato2.getValor("periodo_fecha_final") + "");
                    p_parametros.put("descripcion", tabDato2.getValor("periodo_columna") + "");
//                    p_parametros.put("anio", utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""));
                    p_parametros.put("anioa", utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + ""));
//                    p_parametros.put("anioan", (utilitario.getAnio(tabDato2.getValor("periodo_fecha_final") + "")) - 1);
//                    p_parametros.put("fecha_decimo", utilitario.getDia(tabDato2.getValor("periodo_fecha_final")) + "-" + utilitario.getMes(tabDato2.getValor("periodo_fecha_final")) + "-" + utilitario.getAnio(tabDato2.getValor("periodo_fecha_final")));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;
            case "DECIMO 3RO PARTIDA DETALLE":
                mDescuento.borrarDecimo();
                TablaGenerica tabDato3 = mDescuento.getPeriodos(comboDecim3ro2.getValue() + "");
                if (!tabDato3.isEmpty()) {
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados6.getValue() + ""), tabDato3.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato3.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + "")) - 1), 14, 40);

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados6.getValue() + ""), tabDato3.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato3.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + ""), 14, 40);

                    if (comboEmpleados6.getValue().equals("2")) {
                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados6.getValue() + ""), tabDato3.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDato3.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + "")) - 1), 14, 40);
                    }
                    Integer columna = 0,
                            column = 0;
                    if (comboEmpleados6.getValue().equals("1")) {
                        columna = 18;
                        column = 0;
                    } else if (comboEmpleados6.getValue().equals("2")) {
                        columna = 75;
                        column = 76;
                    }
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados6.getValue() + ""), tabDato3.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato3.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + "")) - 1), columna, column);

                    if (comboEmpleados6.getValue().equals("2")) {
                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados6.getValue() + ""), tabDato3.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDato3.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + "")) - 1), columna, column);
                    }

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados6.getValue() + ""), tabDato3.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDato3.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + ""), columna, column);

                    p_parametros.put("distributivo", Integer.parseInt(comboEmpleados6.getValue() + ""));
                    p_parametros.put("fecha_inicio", tabDato3.getValor("periodo_fecha_inicial") + "");
                    p_parametros.put("fecha_fin", tabDato3.getValor("periodo_fecha_final") + "");
                    p_parametros.put("ano", utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + ""));
                    p_parametros.put("anio", (utilitario.getAnio(tabDato3.getValor("periodo_fecha_final") + "") - 1));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;
            case "DECIMO 3RO PARTIDA HRX TRABAJADOR":
                mDescuento.borrarDecimo();
                TablaGenerica tabDatoh = mDescuento.getPeriodos(comboDecim3ro3.getValue() + "");
                if (!tabDatoh.isEmpty()) {
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados7.getValue() + ""), tabDatoh.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + "")) - 1), 14, 40);

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados7.getValue() + ""), tabDatoh.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + ""), 14, 40);
                    if (comboEmpleados7.getValue().equals("2")) {
                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados7.getValue() + ""), tabDatoh.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + "")) - 1), 14, 40);
                    }
                    Integer columna = 0,
                            column = 0;
                    if (comboEmpleados7.getValue().equals("1")) {
                        columna = 18;
                        column = 0;
                    } else if (comboEmpleados7.getValue().equals("2")) {
                        columna = 75;
                        column = 76;
                    }
                    mDescuento.setRegistro360(Integer.parseInt(comboEmpleados7.getValue() + ""), tabDatoh.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + ""),
                            ((utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + "")) - 1), columna, column);

                    mDescuento.setRegistro36(Integer.parseInt(comboEmpleados7.getValue() + ""), tabDatoh.getValor("periodo_fecha_final"),
                            String.valueOf(utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final"))),
                            utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + ""), columna, column);

                    if (comboEmpleados7.getValue().equals("2")) {
                        mDescuento.setRegistro30(Integer.parseInt(comboEmpleados7.getValue() + ""), tabDatoh.getValor("periodo_fecha_final"),
                                String.valueOf(utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final"))),
                                utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + ""),
                                ((utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + "")) - 1), columna, column);
                    }
                    p_parametros.put("ide_distributivo", Integer.parseInt(comboEmpleados7.getValue() + ""));
                    p_parametros.put("fecha_inicio", tabDatoh.getValor("periodo_fecha_inicial") + "");
                    p_parametros.put("fecha_fin", tabDatoh.getValor("periodo_fecha_final") + "");
                    p_parametros.put("descripcion", tabDatoh.getValor("periodo_columna") + "");
                    p_parametros.put("anioa", utilitario.getAnio(tabDatoh.getValor("periodo_fecha_final") + ""));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                }
                break;
        }
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabDecimos() {
        return tabDecimos;
    }

    public void setTabDecimos(Tabla tabDecimos) {
        this.tabDecimos = tabDecimos;
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

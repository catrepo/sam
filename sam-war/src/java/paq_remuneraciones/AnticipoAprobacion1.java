/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
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
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;

/**
 *
 * @author p-chumana
 */
public class AnticipoAprobacion1 extends Pantalla {

    private Tabla tabAnticipo = new Tabla();
    private Tabla tabListado = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private Panel panOpcion1 = new Panel();
    private Texto txtNumLista = new Texto();
    private Calendario calFecha = new Calendario();
    private Calendario calFechaIni = new Calendario();
    private Calendario calFechaFin = new Calendario();
    private SeleccionTabla setLista = new SeleccionTabla();
    private SeleccionTabla setAnticipo = new SeleccionTabla();
    private Calendario fecRegistro = new Calendario();
    private Texto txtId = new Texto();
    private Texto txtCedula = new Texto();
    private Texto txtNombre = new Texto();
    private Texto txtValor = new Texto();
    private Combo cmbAnio = new Combo();
    private Combo cmbPeriodo = new Combo();
    private Combo cmbDistributivo = new Combo();
    private AreaTexto txaObservacion = new AreaTexto();
    private Etiqueta txeTitulo = new Etiqueta("# Listado");
    private Etiqueta txeCedula = new Etiqueta("CEDULA :");
    private Etiqueta txeNombre = new Etiqueta("NOMBRES :");
    private Etiqueta txeValor = new Etiqueta("SALDO $ :");
    private Etiqueta txeFecha = new Etiqueta("FECHA DE REGISTRO :");
    private Etiqueta txeObservacion = new Etiqueta("OBSERVACIÓN :");
    private Etiqueta txeInfo = new Etiqueta("DESCARGO DE CUOTAS DEL ROL A MODULO DE ANTICIPOS");
    private Etiqueta txeAdvertencia = new Etiqueta("Seguro de ejecutar esta operación");
    private Etiqueta txeAnio = new Etiqueta("AÑO :");
    private Etiqueta txePeriodo = new Etiqueta(" PERIODO : ");
    private Etiqueta txeDistributivo = new Etiqueta("TIPO : ");
    private Etiqueta txeFechaIni = new Etiqueta("FECHA INICIO : ");
    private Etiqueta txeFechaFin = new Etiqueta("FECHA FIN : ");
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogoc = new Dialogo();
    private Dialogo diaDialogor = new Dialogo();
    private Dialogo diaDialogof = new Dialogo();
    private Dialogo diaDialogodm = new Dialogo();
    private Grid gridD = new Grid();
    private Grid gridC = new Grid();
    private Grid gridDM = new Grid();
    private Grid gridR = new Grid();
    private Grid gridF = new Grid();
    private Grid grid = new Grid();
    private Grid gric = new Grid();
    private Grid gridm = new Grid();
    private Grid grir = new Grid();
    private Grid grif = new Grid();
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private BeanRemuneracion adminRemuneracion = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);
    private ClaseGenerica clase = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);

    public AnticipoAprobacion1() {

        //Mostrar el usuario 
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton botDescargo = new Boton();
        botDescargo.setValue("Descargo Cuota Rol");
        botDescargo.setExcluirLectura(true);
        botDescargo.setIcon("ui-icon-check");
        botDescargo.setMetodo("diaDescargo");
        //bar_botones.agregarBoton(botDescargo);


        Boton botAnticipo = new Boton();
        botAnticipo.setValue("Descargo Rol Mes/Anio");
        botAnticipo.setExcluirLectura(true);
        botAnticipo.setIcon("ui-icon-check");
        botAnticipo.setMetodo("descargoMes");
        bar_botones.agregarBoton(botAnticipo);

        Boton botPago = new Boton();
        botPago.setValue("Registro de Pago");
        botPago.setExcluirLectura(true);
        botPago.setIcon("ui-icon-contact");
        botPago.setMetodo("pagoAnticipo");
//        bar_botones.agregarBoton(botPago);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS INGRESADOS PARA APROBAR");
        agregarComponente(panOpcion);

        tabAnticipo.setId("tabAnticipo");
        tabAnticipo.setSql("SELECT\n"
                + "s.id_solicitud,\n"
                + "s.fecha_ant,\n"
                + "s.id_tipo,\n"
                + "s.ced_empleado,\n"
                + "s.nom_empleado,\n"
                + "a.valor,\n"
                + "a.cuotas,\n"
                + "a.valor_mes,\n"
                + "a.cuota_adicional,\n"
                + "a.mes_ini,\n"
                + "a.anio_ini,\n"
                + "a.mes_fin,\n"
                + "a.anio_fin,\n"
                + "s.id_distributivo,\n"
                + "s.estado_solicitud\n"
                + "FROM dbo.nom_solicitud s\n"
                + "INNER JOIN dbo.nom_anticipo a ON a.id_solicitud = s.id_solicitud\n"
                + "where s.estado_solicitud is null");
        tabAnticipo.setCampoPrimaria("id_solicitud");
        tabAnticipo.setCampoOrden("nom_empleado");
        List lista = new ArrayList();
        Object fila1[] = {
            "4", "Aprobar"
        };
        Object fila2[] = {
            "5", "Negar"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabAnticipo.getColumna("estado_solicitud").setRadio(lista, "");
        tabAnticipo.getColumna("nom_empleado").setLongitud(50);
        tabAnticipo.getColumna("id_distributivo").setVisible(false);
        tabAnticipo.getColumna("mes_fin").setVisible(false);
        tabAnticipo.getColumna("anio_fin").setVisible(false);
        tabAnticipo.getGrid().setColumns(4);
        tabAnticipo.setRows(8);
        tabAnticipo.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tabAnticipo);

        tabListado.setId("tabListado");
        tabListado.setSql("SELECT\n"
                + "s.id_solicitud,\n"
                + "s.fecha_ant,\n"
                + "s.id_tipo,\n"
                + "s.ced_empleado,\n"
                + "s.nom_empleado,\n"
                + "a.valor,\n"
                + "a.cuotas,\n"
                + "a.valor_mes,\n"
                + "a.cuota_adicional,\n"
                + "a.mes_ini,\n"
                + "a.anio_ini,\n"
                + "s.id_distributivo,\n"
                + "s.estado_solicitud,\n"
                + "s.lista_aprobacion\n"
                + "FROM dbo.nom_solicitud s \n"
                + "INNER JOIN dbo.nom_anticipo a ON a.id_solicitud = s.id_solicitud\n"
                + "where s.estado_solicitud =(SELECT id_tipo FROM nom_tipo where desc_tipo = 'Aprobado') and s.lista_aprobacion is null");
        tabListado.setCampoPrimaria("id_solicitud");
        tabListado.setCampoOrden("nom_empleado");
        tabListado.getColumna("lista_aprobacion").setVisible(false);
        tabListado.getColumna("mes_ini").setVisible(false);
        tabListado.getColumna("anio_ini").setVisible(false);
        tabAnticipo.getColumna("id_distributivo").setVisible(false);
        List list = new ArrayList();
        Object fila[] = {
            "1", "Devolver"
        };
        list.add(fila);;
        tabListado.getColumna("estado_solicitud").setRadio(list, "");
        tabListado.getColumna("estado_solicitud").setLongitud(1);
        tabListado.getGrid().setColumns(4);
        tabListado.setRows(7);
        tabListado.dibujar();

        PanelTabla pat_lista = new PanelTabla();
        pat_lista.setPanelTabla(tabListado);
        panOpcion1.getChildren().add(pat_lista);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        panOpcion.getChildren().add(gru);
        txtNumLista.setId("txtNumLista");
        Grid griBusca = new Grid();
        griBusca.setColumns(6);
        griBusca.getChildren().add(txeTitulo);
        griBusca.getChildren().add(txtNumLista);

        Boton botSave = new Boton();
        botSave.setValue("Guardar Listado");
        botSave.setExcluirLectura(true);
        botSave.setIcon("ui-icon-disk");
        botSave.setMetodo("saveLista");

        Boton botDelete = new Boton();
        botDelete.setValue("Quitar de Listado");
        botDelete.setExcluirLectura(true);
        botDelete.setIcon("ui-icon-extlink");
        botDelete.setMetodo("devolver");

        griBusca.getChildren().add(botSave);
        griBusca.getChildren().add(botDelete);
        agregarComponente(griBusca);

        panOpcion1.setId("pan_opcion2");
        panOpcion1.setTransient(true);
        panOpcion1.setHeader("LISTADO DE SOLICITUD DE ANTICIPOS APROBADOS");
        agregarComponente(panOpcion1);

        Division divDivision = new Division();
        divDivision.setId("divDivision");
        divDivision.dividir3(panOpcion, griBusca, panOpcion1, "44%", "50%", "H");
        agregarComponente(divDivision);

        Grupo gru_lis = new Grupo();
        gru_lis.getChildren().add(new Etiqueta("FECHA: "));
        gru_lis.getChildren().add(calFecha);
        Boton bot_lista = new Boton();
        bot_lista.setValue("Buscar");
        bot_lista.setIcon("ui-icon-search");
        bot_lista.setMetodo("buscarColumna");
        bar_botones.agregarBoton(bot_lista);
        gru_lis.getChildren().add(bot_lista);

        /*
         * muestra un listado de , listas generadas
         */
        setLista.setId("setLista");
        setLista.setSeleccionTabla("SELECT DISTINCT fecha_aprobacion, lista_aprobacion  FROM nom_solicitud  where id_solicitud =-1", "fecha_aprobacion");
        setLista.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setLista.getTab_seleccion().getColumna("lista_aprobacion").setLongitud(30);
        setLista.getTab_seleccion().setRows(10);
        setLista.setRadio();
        setLista.setWidth("20%");
        setLista.setHeight("40%");
        setLista.getGri_cuerpo().setHeader(gru_lis);
        setLista.getBot_aceptar().setMetodo("aceptoAprobacion");
        setLista.setHeader("SELECCIONE LISTADO");
        agregarComponente(setLista);

        /*
         * Muestra un listado de todos los anticipos pendiente
         */
        setAnticipo.setId("setAnticipo");
        setAnticipo.setSeleccionTabla("SELECT\n"
                + "s.id_solicitud,\n"
                + "a.fecha,\n"
                + "s.ced_empleado,\n"
                + "s.nom_empleado,\n"
                + "a.valor,\n"
                + "a.cuotas,\n"
                + "a.valor_saldo,\n"
                + "(select desc_tipo from nom_tipo where id_tipo = a.id_tipo) as tipo\n"
                + "FROM dbo.nom_solicitud s\n"
                + "INNER JOIN dbo.nom_anticipo a ON a.id_solicitud = s.id_solicitud\n"
                + "where a.estado_anticipo in (select id_tipo from nom_tipo where desc_tipo in('Aprobado','Cobrando'))\n"
                + "order by nom_empleado", "id_solicitud");
        setAnticipo.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setAnticipo.getTab_seleccion().getColumna("ced_empleado").setFiltro(true);
        setAnticipo.getTab_seleccion().getColumna("nom_empleado").setFiltro(true);
        setAnticipo.getTab_seleccion().getColumna("nom_empleado").setLongitud(50);
        setAnticipo.getTab_seleccion().setRows(14);
        setAnticipo.setRadio();
        setAnticipo.setWidth("50%");
        setAnticipo.setHeight("65%");
        setAnticipo.getBot_aceptar().setMetodo("aceptoPago");
        setAnticipo.setHeader("SELECCIONE LISTADO");
        agregarComponente(setAnticipo);

        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("SELECT ano_curso,ano_curso as anio FROM conc_ano order by ano_curso desc");

        cmbPeriodo.setId("cmbPeriodo");
        cmbPeriodo.setCombo("SELECT periodo,periodo as mes FROM dbo.conc_periodo");

        List listas = new ArrayList();
        Object filas1[] = {
            "NL", "EMPLEADO"
        };

        Object filas2[] = {
            "CT", "TRABAJADOR"
        };
        listas.add(filas1);;
        listas.add(filas2);;
        cmbDistributivo.setId("cmbDistributivo");
        cmbDistributivo.setCombo(listas);

        //muetra información del anticipo
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("PARA PAGO POR LIQUIDACIÓN"); //titulo
        diaDialogo.setWidth("50%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("40%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("aceptoLiquidacion");
        gridD.setColumns(4);
        agregarComponente(diaDialogo);

        diaDialogoc.setId("diaDialogoc");
        diaDialogoc.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogoc.setHeight("15%");//siempre porcentaje   alto
        diaDialogoc.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogoc.getBot_aceptar().setMetodo("descargoCuota");
        gridC.setColumns(4);
        agregarComponente(diaDialogoc);

        diaDialogodm.setId("diaDialogodm");
        diaDialogodm.setWidth("28%"); //siempre en porcentajes  ancho
        diaDialogodm.setHeight("22%");//siempre porcentaje   alto
        diaDialogodm.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogodm.getBot_aceptar().setMetodo("igualaCuota");
        gridDM.setColumns(4);
        agregarComponente(diaDialogodm);

        diaDialogor.setId("diaDialogor");
        diaDialogor.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogor.setHeight("25%");//siempre porcentaje   alto
        diaDialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogor.getBot_aceptar().setMetodo("aceptoAprobacion");
        gridR.setColumns(4);
        agregarComponente(diaDialogor);

        diaDialogof.setId("diaDialogof");
        diaDialogof.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogof.setHeight("25%");//siempre porcentaje   alto
        diaDialogof.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogof.getBot_aceptar().setMetodo("aceptoAprobacion");
        gridF.setColumns(4);
        agregarComponente(diaDialogof);


        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        requisito();
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /*
     * Proceso para generar detalle de anticipo
     */

    public void requisito() {
        for (int i = 0; i < tabAnticipo.getTotalFilas(); i++) {
            if (tabAnticipo.getValor(i, "estado_solicitud") != null) {
                if (tabAnticipo.getValor(i, "estado_solicitud").equals("4")) {
                    if (tabAnticipo.getValor(i, "id_distributivo").equals("NL")) {//detalle solicitud de empleados
                        if (tabAnticipo.getValor(i, "cuota_adicional") != null && !tabAnticipo.getValor(i, "cuota_adicional").isEmpty()) {
                            for (int j = 1; j < (Integer.parseInt(tabAnticipo.getValor(i, "cuotas"))); j++) {
                                Integer anio = utilitario.getAnio(utilitario.getFechaActual());
                                Integer id;
//                                if (utilitario.getDia(utilitario.getFechaActual()) <= 15) {
//                                    id = utilitario.getMes(utilitario.getFechaActual());
//                                } else {
//                                    id = utilitario.getMes(utilitario.getFechaActual()) + 1;
//                                }//calculo para cuota de diciembre
                                id = Integer.parseInt(tabAnticipo.getValor(i, "mes_ini"));
                                Integer mess = 0, mesf = 0, aniof = 0;
                                Double cuota = 0.0;
                                if (((j + id) - 1) == 12) {
                                    cuota = Double.parseDouble(tabAnticipo.getValor(i, "cuota_adicional"));
                                } else {
                                    cuota = Double.parseDouble(tabAnticipo.getValor(i, "valor_mes"));
                                }
                                mess = (id + j) - 1;
                                if (mess <= 12) {
                                    mesf = mess;
                                    aniof = anio;
                                } else if (mess > 12) {
                                    mesf = mess - 12;
                                    aniof = anio + 1;
                                }
//                                System.out.println("Valor mensual : " + Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")) + " " + cuota + " " + j + " " + mesf + " " + aniof);
                                adminRemuneracion.llenarSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), cuota, j, mesf, aniof);
                            }
                            Double valor1 = 0.0, total = 0.0, adicional = 0.0, fina = 0.0;
                            valor1 = (Integer.parseInt(tabAnticipo.getValor(i, "cuotas")) - 2) * Double.parseDouble(tabAnticipo.getValor(i, "valor_mes"));
                            if (tabAnticipo.getValor(i, "cuota_adicional") != null && !tabAnticipo.getValor(i, "cuota_adicional").isEmpty()) {
                                adicional = Double.parseDouble(tabAnticipo.getValor(i, "cuota_adicional"));
                            } else {
                                adicional = 0.0;
                            }
                            total = Double.parseDouble(tabAnticipo.getValor(i, "valor")) - adicional - valor1;
                            fina = Math.rint(total * 100) / 100;
//                            System.out.println("Valor mensual" + Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")) + " " + fina + " " + Integer.parseInt(tabAnticipo.getValor(i, "cuotas")) + " " + Integer.parseInt(tabAnticipo.getValor(i, "mes_fin")) + " " + Integer.parseInt(tabAnticipo.getValor(i, "anio_fin")));
                            adminRemuneracion.llenarSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), fina, Integer.parseInt(tabAnticipo.getValor(i, "cuotas")),
                                    Integer.parseInt(tabAnticipo.getValor(i, "mes_fin")), Integer.parseInt(tabAnticipo.getValor(i, "anio_fin")));
                        } else {
                            for (int j = 1; j < (Integer.parseInt(tabAnticipo.getValor(i, "cuotas"))); j++) {
                                Integer anio = utilitario.getAnio(utilitario.getFechaActual());
                                Integer id;
//                                if (utilitario.getDia(utilitario.getFechaActual()) <= 15) {
//                                    id = utilitario.getMes(utilitario.getFechaActual());
//                                } else {
//                                    id = utilitario.getMes(utilitario.getFechaActual()) + 1;
//                                }
                                 id = Integer.parseInt(tabAnticipo.getValor(i, "mes_ini"));
                                Integer mess = 0, mesf = 0, aniof = 0;
                                mess = (id + j) - 1;
                                if (mess <= 12) {
                                    mesf = mess;
                                    aniof = anio;
                                } else if (mess > 12) {
                                    mesf = mess - 12;
                                    aniof = anio + 1;
                                }
//                                System.err.println("Valor mensual" + Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")) + " " + Double.parseDouble(tabAnticipo.getValor(i, "valor_mes")) + " " + j + " " + mesf + " " + aniof);
                                adminRemuneracion.llenarSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), Double.parseDouble(tabAnticipo.getValor(i, "valor_mes")), j, mesf, aniof);
                            }
                            Double valor1 = 0.0, total = 0.0, fina = 0.0;
                            valor1 = (Integer.parseInt(tabAnticipo.getValor(i, "cuotas")) - 1) * Double.parseDouble(tabAnticipo.getValor(i, "valor_mes"));
                            total = Double.parseDouble(tabAnticipo.getValor(i, "valor")) - valor1;
                            fina = Math.rint(total * 100) / 100;
//                            System.err.println("Valor mensual" + Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")) + " " + fina + " " + Integer.parseInt(tabAnticipo.getValor(i, "cuotas")) + " " + Integer.parseInt(tabAnticipo.getValor(i, "mes_fin")) + " " + Integer.parseInt(tabAnticipo.getValor(i, "anio_fin")));
                            adminRemuneracion.llenarSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), fina, Integer.parseInt(tabAnticipo.getValor(i, "cuotas")),
                                    Integer.parseInt(tabAnticipo.getValor(i, "mes_fin")), Integer.parseInt(tabAnticipo.getValor(i, "anio_fin")));
                        }
                    } else {//detalle para solicitud de trabajadores
                        for (int j = 1; j < (Integer.parseInt(tabAnticipo.getValor(i, "cuotas"))); j++) {
                            Integer id;
//                            if (utilitario.getDia(utilitario.getFechaActual()) <= 15) {
//                                id = utilitario.getMes(utilitario.getFechaActual());
//                            } else {
//                                id = utilitario.getMes(utilitario.getFechaActual()) + 1;
//                            }
                            id = Integer.parseInt(tabAnticipo.getValor(i, "mes_ini"));
                            Integer anio = utilitario.getAnio(utilitario.getFechaActual());
                            Integer mess = 0, mesf = 0, aniof = 0;
                            mess = (id + j) - 1;
                            if (mess <= 12) {
                                mesf = mess;
                                aniof = anio;
                            } else if (mess > 12 && mess <= 24) {
                                mesf = mess - 12;
                                aniof = anio + 1;
                            } else if (mess > 24 && mess <= 36) {
                                mesf = mess - 24;
                                aniof = anio + 2;
                            } else if (mess > 36) {
                                mesf = mess - 36;
                                aniof = anio + 3;
                            }
//                            System.err.println("Valor mensual->" + Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")) + " " + Double.parseDouble(tabAnticipo.getValor(i, "valor_mes")) + " " + j + " " + mesf + " " + aniof);
                            adminRemuneracion.llenarSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), Double.parseDouble(tabAnticipo.getValor(i, "valor_mes")), j, mesf, aniof);
                        }
                        Double valor1 = 0.0, total = 0.0, fina = 0.0;
                        valor1 = (Integer.parseInt(tabAnticipo.getValor(i, "cuotas")) - 1) * Double.parseDouble(tabAnticipo.getValor(i, "valor_mes"));
                        total = Double.parseDouble(tabAnticipo.getValor(i, "valor")) - valor1;
                        fina = Math.rint(total * 100) / 100;
//                        System.err.println("Valor mensual->" + Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")) + " " + fina + " " + Integer.parseInt(tabAnticipo.getValor(i, "cuotas")) + " " + Integer.parseInt(tabAnticipo.getValor(i, "mes_fin")) + " " + Integer.parseInt(tabAnticipo.getValor(i, "anio_fin")));
                        adminRemuneracion.llenarSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), fina, Integer.parseInt(tabAnticipo.getValor(i, "cuotas")),
                                Integer.parseInt(tabAnticipo.getValor(i, "mes_fin")), Integer.parseInt(tabAnticipo.getValor(i, "anio_fin")));
                    }
                    adminRemuneracion.actuaSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), tabAnticipo.getValor(i, "ced_empleado"), 4, utilitario.getVariable("NICK"));
                    adminRemuneracion.actualizSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), "Aprobado");//actualiza cuota de cobro
                } else if (tabAnticipo.getValor(i, "estado_solicitud").equals("5")) {//Solicitud Denegada
                    adminRemuneracion.negarSolicitud(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), "'Anulado'");
                    adminRemuneracion.actuaDevolucion(Integer.parseInt(tabAnticipo.getValor(i, "id_solicitud")), 5);
                }
            }
        }
        tabAnticipo.actualizar();
        utilitario.agregarMensaje("Formularios Aprobados", "");
        txtNumLista.limpiar();
        utilitario.addUpdate("txtNumLista");
        tabListado.actualizar();
    }
    /*
     * Generación de listado, para envio a financiero
     */

    public void saveLista() {
        txtNumLista.setDisabled(true); //Desactiva el cuadro de texto
        String numero = adminRemuneracion.listaMax();
        String valor, anio, num;
        Integer cantidad = 0;
        anio = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
        valor = numero.substring(10, 15);
        cantidad = Integer.parseInt(valor) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "0000" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txtNumLista.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "000" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txtNumLista.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = "00" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txtNumLista.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 1000 && cantidad <= 9999) {
                num = "0" + String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txtNumLista.setValue(cadena + "");
                utilitario.addUpdate("txt_num_listado");
            } else if (cantidad >= 10000 && cantidad <= 99999) {
                num = String.valueOf(cantidad);
                String cadena = "LIST" + "-" + anio + "-" + num;
                txtNumLista.setValue(cadena + "");
                utilitario.addUpdate("txtNumLista");
            }
        }
        saveListado();
    }

    /*
     * Guarda en lista generada y actulizacion de estado
     */
    public void saveListado() {
        for (int i = 0; i < tabListado.getTotalFilas(); i++) {
            tabListado.getValor(i, "id_solicitud");
            tabListado.getValor(i, "ced_empleado");
            adminRemuneracion.llenarListado(Integer.parseInt(tabListado.getValor(i, "id_solicitud")), tabListado.getValor(i, "ced_empleado"), txtNumLista.getValue() + "", utilitario.getVariable("NICK"));
            adminRemuneracion.actuaDevolucion(Integer.parseInt(tabListado.getValor(i, "id_solicitud")), 4);
        }
        utilitario.agregarMensaje("Listado Guardado", "");
        utilitario.addUpdate("txtNumLista");
    }

    /*
     * Proceso de devolucion en caso de no desear registro en el listado que se genera
     */
    public void devolver() {
        for (int i = 0; i < tabListado.getTotalFilas(); i++) {
            tabListado.getValor(i, "estado_solicitud");
            tabListado.getValor(i, "id_solicitud");
            if (tabListado.getValor(i, "estado_solicitud") != null) {
                adminRemuneracion.deleteDevolver(Integer.parseInt(tabListado.getValor(i, "id_solicitud")));
                adminRemuneracion.actuaDevolucion(Integer.parseInt(tabListado.getValor(i, "id_solicitud")), 3);
                adminRemuneracion.actuaSoliDevolucion(Integer.parseInt(tabListado.getValor(i, "id_solicitud")));
            }
        }
        tabAnticipo.actualizar();
        utilitario.agregarMensaje("Formularios Devueltos", "");
        tabListado.actualizar();
    }

    /*
     * Permite ver los listados generados por fecha
     */
    public void buscarColumna() {
        if (calFecha.getValue() != null && calFecha.getValue().toString().isEmpty() == false) {
            setLista.getTab_seleccion().setSql("select ROW_NUMBER() OVER(ORDER BY fecha_aprobacion ) AS Row, lista_aprobacion from(SELECT DISTINCT fecha_aprobacion, lista_aprobacion FROM nom_solicitud  where fecha_aprobacion = '" + calFecha.getFecha() + "') as a\n"
                    + "order by lista_aprobacion");
            setLista.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una fecha", "");
        }
    }

    /*
     * Permite descargo de las cuotas del sistema de roles a aplicación de anticipos
     */
    public void diaDescargo() {
        diaDialogoc.setDialogo(gric);
        Grid griMostrar = new Grid();
        griMostrar.setColumns(1);
        griMostrar.getChildren().add(txeInfo);
        griMostrar.getChildren().add(txeAdvertencia);
        gridC.getChildren().add(griMostrar);
        diaDialogoc.setDialogo(gridC);
        diaDialogoc.dibujar();
    }

    public void descargoMes() {
        diaDialogodm.setDialogo(gridm);
        Grid griMostrar = new Grid();
        griMostrar.setColumns(1);
        griMostrar.getChildren().add(txeInfo);
        Grid griMostra = new Grid();
        griMostra.setColumns(2);
        griMostra.getChildren().add(txeAnio);
        griMostra.getChildren().add(cmbAnio);
        griMostra.getChildren().add(txePeriodo);
        griMostra.getChildren().add(cmbPeriodo);
        griMostrar.getChildren().add(griMostra);
        griMostrar.getChildren().add(txeAdvertencia);
        gridDM.getChildren().add(griMostrar);
        diaDialogodm.setDialogo(gridDM);
        diaDialogodm.dibujar();
    }

    public void igualaCuota() {
        if (String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).equals(cmbAnio.getValue())) {
            TablaGenerica tabDatos = adminRemuneracion.getAnticipoAnt(clase.meses(Integer.parseInt(cmbPeriodo.getValue().toString())), Integer.parseInt(cmbAnio.getValue().toString()));
            if (!tabDatos.isEmpty()) {
                anterior(clase.meses(Integer.parseInt(cmbPeriodo.getValue().toString())), Integer.parseInt(cmbPeriodo.getValue().toString()), Integer.parseInt(cmbAnio.getValue().toString()));
            } else {
                actual(clase.meses(Integer.parseInt(cmbPeriodo.getValue().toString())), Integer.parseInt(cmbPeriodo.getValue().toString()), Integer.parseInt(cmbAnio.getValue().toString()));
            }
        } else {
            anterior(clase.meses(Integer.parseInt(cmbPeriodo.getValue().toString())), Integer.parseInt(cmbPeriodo.getValue().toString()), Integer.parseInt(cmbAnio.getValue().toString()));
        }
    }

    public void descargoCuota() {
        Integer mes = 0, anio = 0;
        if (utilitario.getDia(utilitario.getFechaActual()) >= 1 && utilitario.getDia(utilitario.getFechaActual()) <= 7) {
            mes = utilitario.getMes(utilitario.getFechaActual()) - 1;
            if (utilitario.getMes(utilitario.getFechaActual()) != 1) {
                anio = Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
            } else {
                anio = Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual()))) - 1;
            }
            actual(clase.meses(mes), mes, anio);
        } else if (utilitario.getDia(utilitario.getFechaActual()) >= 25 && utilitario.getDia(utilitario.getFechaActual()) <= 30) {
            mes = utilitario.getMes(utilitario.getFechaActual());
            anio = Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
            actual(clase.meses(mes), mes, anio);
        } else {
            mes = utilitario.getMes(utilitario.getFechaActual()) - 1;
            if (utilitario.getMes(utilitario.getFechaActual()) != 1) {
                anio = Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
            } else {
                anio = Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual()))) - 1;
            }
            anterior(clase.meses(mes), mes, anio);
        }
    }

    public void anterior(String mes, Integer perido, Integer anio) {
        TablaGenerica tabDatos = adminRemuneracion.getAnticipoAnt(mes, anio);
        if (!tabDatos.isEmpty()) {
            for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
//                if(tabDatos.getValor(i, "cedciu").equals("1709339913")){
                //adminRemuneracion.descontarDetalle(tabDatos.getValor(i, "cedciu"), Integer.parseInt(mes),anio , utilitario.getFechaHoraActual(), Double.valueOf(tabDatos.getValor(i, "valtrs")));
                adminRemuneracion.descargoCuotas(tabDatos.getValor(i, "cedciu"), anio, Integer.parseInt(mes), utilitario.getFechaHoraActual(), Double.valueOf(tabDatos.getValor(i, "valtrs")));
                actulizaAnticipo(tabDatos.getValor(i, "cedciu"));
                actualizaSolicitud(tabDatos.getValor(i, "cedciu"));
//                }
            }
             diaDialogodm.cerrar();
        } else {
            utilitario.agregarMensajeInfo("No encuentra datos", null);
        }
    }

    public void actual(String mes, Integer perido, Integer anio) {
        TablaGenerica tabDatos = adminRemuneracion.getAnticipoAct(mes, anio);
        if (!tabDatos.isEmpty()) {
            for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                adminRemuneracion.descargoCuotas(tabDatos.getValor(i, "cedciu"), anio, Integer.parseInt(mes), utilitario.getFechaHoraActual(), Double.valueOf(tabDatos.getValor(i, "valtrs")));
//                adminRemuneracion.descontarDetalle(tabDatos.getValor(i, "cedciu"), perido, anio, utilitario.getFechaHoraActual(), Double.valueOf(tabDatos.getValor(i, "valtrs")));
                actulizaAnticipo(tabDatos.getValor(i, "cedciu"));
                actulizaEstado(Integer.parseInt(tabDatos.getValor(i, "cedciu")));
                actualizaSolicitud(tabDatos.getValor(i, "cedciu"));
            }
        } else {
            utilitario.agregarMensaje("No encuentra datos1", null);
        }
    }

    public void actulizaAnticipo(String codigo) {
        adminRemuneracion.descontarAnticipo(codigo);
    }

    public void actulizaEstado(Integer codigo) {
        TablaGenerica tabDatos = adminRemuneracion.getAnticipoAprobado(codigo);
        if (!tabDatos.isEmpty()) {
            adminRemuneracion.estadoAnticipo(Integer.parseInt(tabDatos.getValor("id_anticipo")), "Cobrando");
            adminRemuneracion.estadoSolicitud(Integer.parseInt(tabDatos.getValor("id_solicitud")), "Cobrando");
        }
    }

    public void actualizaSolicitud(String codigo) {
        TablaGenerica tabDatos = adminRemuneracion.getValorAnticipo(codigo);
        if (!tabDatos.isEmpty()) {
            adminRemuneracion.estadoAnticipo(Integer.parseInt(tabDatos.getValor("id_anticipo")), "Cancelado");
            adminRemuneracion.estadoSolicitud(Integer.parseInt(tabDatos.getValor("id_solicitud")), "Cancelado");
        }
    }

    public void aceptoRegistro() {
        setAnticipo.cerrar();
    }

    /*
     * Proceso para pago anticipadamente o liquidación
     */
    public void pagoAnticipo() {
        setAnticipo.getTab_seleccion().limpiar();
        setAnticipo.dibujar();

    }

    public void aceptoPago() {
        TablaGenerica tabDatos = adminRemuneracion.getSolicitud(Integer.parseInt(setAnticipo.getValorSeleccionado()));
        if (!tabDatos.isEmpty()) {
            diaDialogo.Limpiar();
            diaDialogo.setDialogo(gridD);
            Grid griMostrar = new Grid();
            griMostrar.setColumns(2);
            griMostrar.getChildren().add(txeCedula);
            txtCedula.setSize(10);
            griMostrar.getChildren().add(txtCedula);
            griMostrar.getChildren().add(txeNombre);
            txtNombre.setSize(80);
            griMostrar.getChildren().add(txtNombre);
            griMostrar.getChildren().add(txeValor);
            txtValor.setSize(5);
            griMostrar.getChildren().add(txtValor);
            griMostrar.getChildren().add(txeFecha);
            griMostrar.getChildren().add(fecRegistro);
            griMostrar.getChildren().add(txeObservacion);
            txaObservacion.setCols(70);
            griMostrar.getChildren().add(txaObservacion);
            gridD.getChildren().add(griMostrar);
            diaDialogo.setDialogo(grid);
            diaDialogo.dibujar();
            txtId.setValue(tabDatos.getValor("id_solicitud") + "");
            txtCedula.setValue(tabDatos.getValor("ced_empleado") + "");
            txtNombre.setValue(tabDatos.getValor("nom_empleado") + "");
            txtValor.setValue(tabDatos.getValor("valor_saldo") + "");
            utilitario.addUpdate("txtCedula");
            utilitario.addUpdate("txtNombre");
            utilitario.addUpdate("txtValor");
        } else {
            utilitario.agregarMensaje("Datos no encontrados", null);
        }
    }
    /*
     * registro de pago anticipado
     */

    public void aceptoLiquidacion() {
        TablaGenerica tabDatos = adminRemuneracion.getIdDetalle(Integer.parseInt(txtId.getValue() + ""));
        if (!tabDatos.isEmpty()) {
            try {
                adminRemuneracion.negarSolicitud(Integer.parseInt(txtId.getValue() + ""), "'Cancelado'");
                adminRemuneracion.actualizSolicitud(Integer.parseInt(txtId.getValue() + ""), "'Cancelado'");
                for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                    adminRemuneracion.actualizDetalle(Integer.parseInt(tabDatos.getValor(i, "id_detalle")), "'Cancelado'", fecRegistro.getFecha() + "");
                }
                adminRemuneracion.llenarLiquidacion(Integer.parseInt(txtId.getValue() + ""), 6, fecRegistro.getFecha() + "", Double.valueOf(txtValor.getValue() + ""), txaObservacion.getValue() + "", tabConsulta.getValor("NICK_USUA"), utilitario.getFechaHoraActual());
                utilitario.agregarMensaje("Registro Actualizado", null);
            } catch (Exception e) {
            }
        }
    }

    /*CREACION DE REPORTES */
    //llamada a reporte
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        calFecha.setFechaActual();
        switch (rep_reporte.getNombre()) {
            case "LISTA DE ACREDITACION":
                setLista.dibujar();
                setLista.getTab_seleccion().limpiar();
                break;
            case "LISTA DE ANTICIPO MES":
                diaDialogor.setDialogo(grir);
                Grid griBusca = new Grid();
                griBusca.setColumns(2);
                griBusca.getChildren().add(txeAnio);
                griBusca.getChildren().add(cmbAnio);
                griBusca.getChildren().add(txePeriodo);
                griBusca.getChildren().add(cmbPeriodo);
                griBusca.getChildren().add(txeDistributivo);
                griBusca.getChildren().add(cmbDistributivo);
                gridR.getChildren().add(griBusca);
                diaDialogor.setDialogo(gridR);
                diaDialogor.dibujar();
                break;
            case "TOTAL ANTICIPO INGRESADO":
                diaDialogof.setDialogo(grif);
                Grid griFechas = new Grid();
                griFechas.setColumns(2);
                griFechas.getChildren().add(txeFechaIni);
                griFechas.getChildren().add(calFechaIni);
                griFechas.getChildren().add(txeFechaFin);
                griFechas.getChildren().add(calFechaFin);
                gridF.getChildren().add(griFechas);
                diaDialogof.setDialogo(gridF);
                diaDialogof.dibujar();
                break;
            case "LISTA ANTICIPO APROBADO":
                diaDialogof.setDialogo(grif);
                Grid griFecha = new Grid();
                griFecha.setColumns(2);
                griFecha.getChildren().add(txeFechaIni);
                griFecha.getChildren().add(calFechaIni);
                griFecha.getChildren().add(txeFechaFin);
                griFecha.getChildren().add(calFechaFin);
                gridF.getChildren().add(griFecha);
                diaDialogof.setDialogo(gridF);
                diaDialogof.dibujar();
                break;
        }
    }

    public void aceptoAprobacion() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "LISTA DE ACREDITACION":
                TablaGenerica tabDato = adminRemuneracion.getNumListado(calFecha.getFecha(), Integer.parseInt(setLista.getValorSeleccionado() + ""));
                if (!tabDato.isEmpty()) {
                    p_parametros.put("fecha_listado", tabDato.getValor("fecha_aprobacion") + "");
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    p_parametros.put("listado", tabDato.getValor("lista_aprobacion") + "");
                    p_parametros.put("ide_listado", tabDato.getValor("lista_aprobacion") + "");
                    TablaGenerica tabDatos = adminRemuneracion.getDatosDirector();
                    if (!tabDatos.isEmpty()) {
                        p_parametros.put("director", tabDatos.getValor("RESPONSABLE") + "");
                        p_parametros.put("cargo", tabDatos.getValor("DESCRIPCION") + "");
                    }
                    
                    TablaGenerica tabElabora = adminRemuneracion.getUsuario(tabDato.getValor("ide_responsable"));
                    if (!tabElabora.isEmpty()) {
                        p_parametros.put("quien_elabora", tabElabora.getValor("NOM_USUA") + "");
                    }
                    
                    TablaGenerica tabAprueba = adminRemuneracion.getUsuario(tabDato.getValor("ide_aprobacion"));
                    if (!tabAprueba.isEmpty()) {
                        p_parametros.put("quien_aprueba", tabAprueba.getValor("NOM_USUA") + "");
                    }
                    p_parametros.put("cargoi", "REMUNERACIONES");
                    p_parametros.put("cargoa", "REMUNERACIONES");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    System.err.println(p_parametros);
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("No ahi exite lista en esta fecha", null);
                }
                break;
            case "LISTA DE ANTICIPO MES":
                p_parametros.put("anio", cmbAnio.getValue() + "");
                p_parametros.put("periodo", cmbPeriodo.getValue() + "");
                p_parametros.put("tipo", cmbDistributivo.getValue() + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "TOTAL ANTICIPO INGRESADO":
                p_parametros.put("fecha_ini", calFechaIni.getFecha() + "");
                p_parametros.put("fecha_fin", calFechaFin.getFecha() + "");
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "LISTA ANTICIPO APROBADO":
                p_parametros.put("fecha_ini", calFechaIni.getFecha() + "");
                p_parametros.put("fecha_fin", calFechaFin.getFecha() + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTabAnticipo() {
        return tabAnticipo;
    }

    public void setTabAnticipo(Tabla tabAnticipo) {
        this.tabAnticipo = tabAnticipo;
    }

    public Tabla getTabListado() {
        return tabListado;
    }

    public void setTabListado(Tabla tabListado) {
        this.tabListado = tabListado;
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

    public SeleccionTabla getSetLista() {
        return setLista;
    }

    public void setSetLista(SeleccionTabla setLista) {
        this.setLista = setLista;
    }

    public SeleccionTabla getSetAnticipo() {
        return setAnticipo;
    }

    public void setSetAnticipo(SeleccionTabla setAnticipo) {
        this.setAnticipo = setAnticipo;
    }
}

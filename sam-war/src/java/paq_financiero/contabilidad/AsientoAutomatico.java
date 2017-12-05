/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
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
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_nomina.ejb.mergeDescuento;
import paq_presupuestaria.ejb.Programas;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class AsientoAutomatico extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private Tabla tabTabla = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Tabla setCuenta = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private Texto txtDebe = new Texto();
    private Texto txtHaber = new Texto();
    private Texto txtDevengado = new Texto();
    private Combo cmbCuentas = new Combo();
    private Combo cmbDistributivo = new Combo();
    private Combo cmbEmpleado = new Combo();
    private Combo cmbEmpleado1 = new Combo();
    private Combo cmbEmpleado2 = new Combo();
    private Combo cmbAnio = new Combo();
    private Combo cmbAnio1 = new Combo();
    private Combo cmbMes = new Combo();
    private Combo cmbMes1 = new Combo();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaReporte = new Dialogo();
    private Dialogo diaIESS = new Dialogo();
    private Dialogo diaRetencion = new Dialogo();
    private Grid gridD = new Grid();
    private Grid grid = new Grid();
    private Grid grir = new Grid();
    private Grid gri = new Grid();
    private Grid grire = new Grid();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    String selec_mes, servidor;
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);

    public AsientoAutomatico() {
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        cmbCuentas.setId("cmbCuentas");
        cmbCuentas.setConexion(conPostgres);
        cmbCuentas.setCombo("SELECT ide_cuenta,cue_codigo,cue_descripcion from conc_catalogo_cuentas where nivel in (4,5,6)");

        cmbDistributivo.setId("cmbDistributivo");
        cmbDistributivo.setConexion(conPostgres);
        cmbDistributivo.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        cmbEmpleado.setId("cmbEmpleado");
        cmbEmpleado.setConexion(conPostgres);
        cmbEmpleado.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        cmbEmpleado1.setId("cmbEmpleado1");
        cmbEmpleado1.setConexion(conPostgres);
        cmbEmpleado1.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        cmbEmpleado2.setId("cmbEmpleado2");
        cmbEmpleado2.setConexion(conPostgres);
        cmbEmpleado2.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");

        cmbAnio.setId("cmbAnio");
        cmbAnio.setConexion(conPostgres);
        cmbAnio.setCombo("SELECT ano_curso,ano_curso as año FROM conc_ano order by ano_curso desc");

        cmbAnio1.setId("cmbAnio1");
        cmbAnio1.setConexion(conPostgres);
        cmbAnio1.setCombo("SELECT ano_curso,ano_curso as año FROM conc_ano order by ano_curso desc");

        cmbMes.setId("cmbMes");
        cmbMes.setConexion(conPostgres);
        cmbMes.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual order by ide_periodo");

        cmbMes1.setId("cmbMes1");
        cmbMes1.setConexion(conPostgres);
        cmbMes1.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual order by ide_periodo");

        Boton botAcceso = new Boton();
        botAcceso.setValue("Cargar Asiento");
        botAcceso.setIcon("ui-icon-search");
        botAcceso.setMetodo("cargaRegistro");
        bar_botones.agregarBoton(botAcceso);

        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("select ide_movimiento,nro_comprobante,detalle_aciento from cont_movimiento");
        autBusca.setSize(100);
        autBusca.setMetodoChange("filtrarRegistro");
        bar_botones.agregarComponente(new Etiqueta("Registros Encontrado"));
        bar_botones.agregarComponente(autBusca);

        Boton botLimpiar = new Boton();
        botLimpiar.setIcon("ui-icon-cancel");
        botLimpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(botLimpiar);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("ASIENTO AUTOMATICO DE ROLES");
        agregarComponente(panOpcion);

        Grid griTipo = new Grid();
        griTipo.setColumns(6);
        griTipo.getChildren().add(new Etiqueta("Seleccione: "));
        griTipo.getChildren().add(cmbDistributivo);
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Cuentas de Asientos Automáticos"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("40%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getGri_cuerpo().setHeader(griTipo);
        diaDialogo.getBot_aceptar().setMetodo("aceptoCuenta");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        Grid griTip = new Grid();
        griTip.getChildren().clear();
        griTip.setColumns(6);
        griTip.getChildren().add(new Etiqueta("Seleccione: "));
        griTip.getChildren().add(cmbEmpleado);
        diaReporte.setId("diaReporte");
        diaReporte.setTitle("Anticipos de Sueldo"); //titulo
        diaReporte.setWidth("30%"); //siempre en porcentajes  ancho
        diaReporte.setHeight("20%");//siempre porcentaje   alto
        diaReporte.setResizable(false); //para que no se pueda cambiar el tamaño
        diaReporte.getGri_cuerpo().setHeader(griTip);
        diaReporte.getBot_aceptar().setMetodo("aceptoDescuentos");
        grir.setColumns(2);
        agregarComponente(diaReporte);

        Grid griTi = new Grid();
        griTi.getChildren().clear();
        griTi.setColumns(2);
        griTi.getChildren().add(new Etiqueta("Seleccione: "));
        griTi.getChildren().add(cmbEmpleado1);
        griTi.getChildren().add(new Etiqueta("Seleccione Año: "));
        griTi.getChildren().add(cmbAnio);
        griTi.getChildren().add(new Etiqueta("Seleccione Periodo: "));
        griTi.getChildren().add(cmbMes);
        diaIESS.setId("diaIESS");
        diaIESS.setTitle("IESS"); //titulo
        diaIESS.setWidth("30%"); //siempre en porcentajes  ancho
        diaIESS.setHeight("25%");//siempre porcentaje   alto
        diaIESS.setResizable(false); //para que no se pueda cambiar el tamaño
        diaIESS.getGri_cuerpo().setHeader(griTi);
        diaIESS.getBot_aceptar().setMetodo("aceptoDescuentos");
        gri.setColumns(2);
        agregarComponente(diaIESS);

        Grid griRe = new Grid();
        griRe.getChildren().clear();
        griRe.setColumns(2);
        griRe.getChildren().add(new Etiqueta("Seleccione: "));
        griRe.getChildren().add(cmbEmpleado2);
        griRe.getChildren().add(new Etiqueta("Seleccione Año: "));
        griRe.getChildren().add(cmbAnio1);
        griRe.getChildren().add(new Etiqueta("Seleccione Periodo: "));
        griRe.getChildren().add(cmbMes1);
        diaRetencion.setId("diaRetencion");
        diaRetencion.setTitle("RETENCIONES"); //titulo
        diaRetencion.setWidth("30%"); //siempre en porcentajes  ancho
        diaRetencion.setHeight("25%");//siempre porcentaje   alto
        diaRetencion.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRetencion.getGri_cuerpo().setHeader(griRe);
        diaRetencion.getBot_aceptar().setMetodo("aceptoDescuentos");
        griRe.setColumns(2);
        agregarComponente(diaRetencion);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        dibujarPantalla();
    }

    public void cargaRegistro() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        gridD.getChildren().add(setCuenta);
        setCuenta.setId("setCuenta");
        setCuenta.setConexion(conPostgres);
        setCuenta.setSql("SELECT id_codigo,descripcion,cuentas FROM conc_cuentas_asientos where anio=2015 and estado='A' and id_codigo_asiento is null");
        setCuenta.getColumna("descripcion").setFiltro(true);
        setCuenta.getColumna("cuentas").setLongitud(50);
        setCuenta.setRows(10);
        setCuenta.setTipoSeleccion(false);
        diaDialogo.setDialogo(gridD);
        setCuenta.dibujar();
        diaDialogo.dibujar();
    }

    public void cargaReporte() {
        diaReporte.Limpiar();
        diaReporte.setDialogo(grir);
        diaReporte.dibujar();
    }

    public void cargaReport() {
        diaIESS.Limpiar();
        diaIESS.setDialogo(gri);
        diaIESS.dibujar();
    }

    public void cargaRepor() {
        diaRetencion.Limpiar();
        diaRetencion.setDialogo(grire);
        diaRetencion.dibujar();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("cont_movimiento", "ide_movimiento", 1);
        if (autBusca.getValue() == null) {
            tabTabla.setCondicion("ide_movimiento=-1");
        } else {
            tabTabla.setCondicion("ide_movimiento=" + autBusca.getValor());
        }
        tabTabla.getColumna("ide_movimiento").setVisible(false);
        tabTabla.getColumna("IDE_CENTRO_COSTO").setVisible(false);
        tabTabla.getColumna("MOV_FECHA").setVisible(false);
        tabTabla.getColumna("MOV_USUARIO").setVisible(false);
        tabTabla.getColumna("BANDERA").setVisible(false);
        tabTabla.getColumna("CODIGO_AUX").setVisible(false);
        tabTabla.getColumna("TIPO_AUX").setVisible(false);
        tabTabla.getColumna("IDDOCUMENTO_SAI").setVisible(false);
        tabTabla.getColumna("TIPO").setVisible(false);
        tabTabla.getColumna("IDE_DOCUMENTO").setVisible(false);
        tabTabla.getColumna("IDE_COMPROBANTE").setVisible(false);
        tabTabla.getColumna("IDE_CONCEPTO").setVisible(false);
        tabTabla.getColumna("IDE_TITULO").setVisible(false);
        tabTabla.getColumna("IDE_ENTREGA_DETALLE").setVisible(false);
        tabTabla.getColumna("IP_RESPONSABLE").setVisible(false);
        tabTabla.getColumna("IDE_IMPUESTO").setVisible(false);
        tabTabla.getColumna("TIPO_ASIENTO").setVisible(false);
        tabTabla.getColumna("IDE_TIPO_MOVIMIENTO").setVisible(false);
        tabTabla.getColumna("ANO").setVisible(false);
        tabTabla.getColumna("FECHA_MOVIMIENTO").setVisible(false);
        tabTabla.getColumna("ASIENTO").setVisible(false);
        tabTabla.getColumna("IDE_COMPROBANTE_PAGADO").setVisible(false);
        tabTabla.getColumna("BANDERA_SIGEF").setVisible(false);
        tabTabla.getColumna("IP_INGRE").setVisible(false);
        tabTabla.getColumna("IP_ACTUA").setVisible(false);

        tabTabla.agregarRelacion(tabDetalle);
        tabTabla.setTipoFormulario(true);
        tabTabla.setLectura(true);
        tabTabla.getGrid().setColumns(2);
        tabTabla.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);

        txtDebe.setId("txtDebe");
        txtDebe.setSize(8);
        txtDebe.setValue("0.0");
        txtHaber.setId("txtHaber");
        txtHaber.setSize(8);
        txtHaber.setValue("0.0");
        txtDevengado.setId("txtDevengado");
        txtDevengado.setSize(8);
        txtDevengado.setValue("0.0");
        Grid gri_busca = new Grid();
        gri_busca.setColumns(8);
        gri_busca.getChildren().add(new Etiqueta("Cuenta: "));
        gri_busca.getChildren().add(cmbCuentas);
        gri_busca.getChildren().add(new Etiqueta("Debe $: "));
        gri_busca.getChildren().add(txtDebe);
        gri_busca.getChildren().add(new Etiqueta("Haber $: "));
        gri_busca.getChildren().add(txtHaber);
        gri_busca.getChildren().add(new Etiqueta("Devengado $: "));
        gri_busca.getChildren().add(txtDevengado);
        agregarComponente(gri_busca);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setConexion(conPostgres);
        tabDetalle.setSql("SELECT c.ide_detalle_mov,\n"
                + "c.ide_movimiento,\n"
                + "o.cue_codigo,\n"
                + "c.ide_cuenta,\n"
                + "o.cue_descripcion,\n"
                + "c.mov_debe,\n"
                + "c.mov_haber,\n"
                + "o.cedula,\n"
                + "c.mov_devengado\n"
                + "FROM cont_detalle_movimiento c\n"
                + "INNER JOIN public.conc_catalogo_cuentas o ON c.ide_cuenta = o.ide_cuenta\n"
                + "where c.ide_movimiento = " + tabTabla.getValor("ide_movimiento") + "\n"
                + "order by c.ide_detalle_mov desc");
        tabDetalle.setCampoPrimaria("ide_detalle_mov");
        tabDetalle.setCampoOrden("ide_movimiento");
        tabDetalle.getColumna("ide_cuenta").setVisible(false);
        tabDetalle.getColumna("cedula").setVisible(false);
        tabDetalle.dibujar();
        PanelTabla pnd = new PanelTabla();
        pnd.setPanelTabla(tabDetalle);

        Division divTablas = new Division();
        divTablas.setId("divTablas");
        divTablas.dividir3(pnt, gri_busca, pnd, "33%", "61%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(divTablas);
        panOpcion.getChildren().add(gru);
    }

    public void filtrarRegistro(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpia();
        autBusca.onSelect(evt);
        dibujarPantalla();
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
    }

    public void limpia() {
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void aceptoCuenta() {
        TablaGenerica tab = mDescuento.VerificarRol();
        if (!tab.isEmpty()) {
            TablaGenerica tabDato = programas.getAsientos(Integer.parseInt(setCuenta.getValorSeleccionado()));
            if (!tabDato.isEmpty()) {
                if (tabDato.getValor("abreviatura").equals("PP")) {//Anticipo de sueldo                    
                    TablaGenerica tabDatos = programas.getAsientosCuentas(tabDato.getValor("cuenta") + "", tabDato.getValor("cuenta1") + "", Integer.parseInt(tabTabla.getValor("ide_movimiento")), Integer.parseInt(tabTabla.getValor("ano")),
                            Integer.parseInt(tabTabla.getValor("ide_periodo")), Integer.parseInt(cmbDistributivo.getValue() + ""), tabDato.getValor("abreviatura"));
                    if (!tabDatos.isEmpty()) {
                        for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                            try {
                                TablaGenerica tabDat = programas.getDetalleMovimientos(Integer.parseInt(tabDatos.getValor(i, "cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")));
                                if (!tabDat.isEmpty()) {
                                } else {
                                    programas.setCuentaContable(Integer.parseInt(tabDatos.getValor(i, "cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble(tabDatos.getValor(i, "debe")),
                                            Double.parseDouble(tabDatos.getValor(i, "valor")), Double.parseDouble(tabDatos.getValor(i, "devengado")), tabDatos.getValor(i, "descripcion"), tabDatos.getValor(i, "tipo_movimiento"), tabDatos.getValor(i, "doc_deposito"));
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                } else if (tabDato.getValor("abreviatura").equals("IS")) {//IESS
                    Double valor;
                    TablaGenerica tabDatos = programas.getIESSCuenta(Integer.parseInt(cmbDistributivo.getValue() + ""));
                    if (!tabDatos.isEmpty()) {
                        for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                            if (tabDatos.getValor(i, "partida").equals("51")) {
                                valor = Double.valueOf(tabDatos.getValor(i, "valor_total"));
                                TablaGenerica tabCuenta = programas.getIdCuentas(tabDato.getValor("cuenta"));
                                if (!tabCuenta.isEmpty()) {
                                    programas.setCuentaContable(Integer.parseInt(tabCuenta.getValor("ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble("0.0"),
                                            valor, Double.parseDouble("0.0"), "S/D", "F", "S/D");
                                } else {
                                    utilitario.agregarMensaje("Falta", null);
                                }
                            } else if (tabDatos.getValor(i, "partida").equals("71")) {
                                valor = Double.valueOf(tabDatos.getValor(i, "valor_total"));
                                TablaGenerica tabCuenta = programas.getIdCuentas(tabDato.getValor("cuenta1"));
                                if (!tabCuenta.isEmpty()) {
                                    if (!tabCuenta.isEmpty()) {
                                        programas.setCuentaContable(Integer.parseInt(tabCuenta.getValor("ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble("0.0"),
                                                valor, Double.parseDouble("0.0"), "S/D", "F", "S/D");
                                    } else {
                                        utilitario.agregarMensaje("Fatal", null);
                                    }
                                }
                            }
                        }
                    } else {
                        utilitario.agregarMensaje("Ausente", null);
                    }
                } else if (tabDato.getValor("abreviatura").equals("RJ")) { //Retencion Judicial
                    TablaGenerica tabDatos = programas.getCuentaRent(Integer.parseInt(cmbDistributivo.getValue() + ""));
                    if (!tabDatos.isEmpty()) {
                        for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                            if (Double.valueOf(tabDatos.getValor(i, "valor")) == 4.00 || Double.valueOf(tabDatos.getValor(i, "valor")) == 1.56 || Double.valueOf(tabDatos.getValor(i, "valor")) == 2.48) {
                                programas.setCuentaContable(Integer.parseInt(tabDatos.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble("0.0"),
                                        Double.parseDouble(tabDatos.getValor(i, "valor")), Double.parseDouble("0.0"), "S/D", "F", "S/D");
                            } else {
                                programas.setCuentaContable(Integer.parseInt(tabDatos.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble("0.0"),
                                        Double.parseDouble(tabDatos.getValor(i, "valor")), Double.parseDouble("0.0"), "S/D", "F", "S/D");

                                programas.setCuentaContable(Integer.parseInt(tabDatos.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble(tabDatos.getValor(i, "valor")),
                                        Double.parseDouble("0.0"), Double.parseDouble("0.0"), "S/D", "F", "S/D");
                            }
                        }
                    }

//                    TablaGenerica tabDatos = programas.getCuentaCont(tabDato.getValor("cuentas").substring(0, 6), tabDato.getValor("cuentas").substring(7, 13));
//                    if (!tabDatos.isEmpty()) {
//                        for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
//                            if (tabDatos.getValor(i, "cedula") != null) {
//                                TablaGenerica tabBeneficiario = programas.getAsientoBeneficiarios(tabDatos.getValor(i, "cedula"), Integer.parseInt(cmbDistributivo.getValue() + ""));
//                                if (!tabBeneficiario.isEmpty()) {
//                                    if (Double.valueOf(tabBeneficiario.getValor("mensual")) == 4.00 || Double.valueOf(tabBeneficiario.getValor("mensual")) == 1.56 || Double.valueOf(tabBeneficiario.getValor("mensual")) == 2.48) {
//                                        programas.setCuentaContable(Integer.parseInt(tabDatos.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble("0.0"),
//                                                Double.parseDouble(tabBeneficiario.getValor("mensual")), Double.parseDouble("0.0"), "S/D", "F", "S/D");
//                                    } else {
//                                        programas.setCuentaContable(Integer.parseInt(tabDatos.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble("0.0"),
//                                                Double.parseDouble(tabBeneficiario.getValor("mensual")), Double.parseDouble("0.0"), "S/D", "F", "S/D");
//
//                                        programas.setCuentaContable(Integer.parseInt(tabDatos.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble(tabBeneficiario.getValor("mensual")),
//                                                Double.parseDouble("0.0"), Double.parseDouble("0.0"), "S/D", "F", "S/D");
//                                    }
//                                }
//                            }
//
//                        }
//                    }
//                    TablaGenerica tabAdicional = programas.getCuentaRent(Integer.parseInt(cmbDistributivo.getValue() + ""), Integer.parseInt(setCuenta.getValorSeleccionado()));
//                    if (!tabAdicional.isEmpty()) {
//                        for (int i = 0; i < tabAdicional.getTotalFilas(); i++) {
//                            programas.setCuentaContable(Integer.parseInt(tabAdicional.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble("0.0"),
//                                    Double.parseDouble(tabAdicional.getValor(i, "valor")), Double.parseDouble("0.0"), "S/D", "F", "S/D");
//
//                            programas.setCuentaContable(Integer.parseInt(tabAdicional.getValor(i, "ide_cuenta")), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble(tabAdicional.getValor(i, "valor")),
//                                    Double.parseDouble("0.0"), Double.parseDouble("0.0"), "S/D", "F", "S/D");
//                        }
//                    }

                }
            }
            diaDialogo.cerrar();
            tabTabla.actualizar();
        } else {
            utilitario.agregarMensaje("Información No Disponible", "Rol Aun No Esta Registrado");
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        programas.setCuentaConta(Integer.parseInt(cmbCuentas.getValue() + ""), Integer.parseInt(tabTabla.getValor("ide_movimiento")), Double.parseDouble(txtDebe.getValue() + ""), Double.parseDouble(txtHaber.getValue() + ""), Double.parseDouble(txtDevengado.getValue() + ""));
        tabDetalle.actualizar();
        utilitario.agregarMensaje("Registro Guardado", null);
    }

    @Override
    public void eliminar() {
        programas.setEiminarMovimiento(Integer.parseInt(tabDetalle.getValorSeleccionado()));
        utilitario.agregarMensajeInfo("Registro Eliminado", null);
        tabDetalle.actualizar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    //llamado para seleccionar el reporte
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Anticipos Sueldo":
                cargaReporte();
                break;
            case "IESS":
                cargaReport();
                break;
            case "Retenciones":
                cargaRepor();
                break;
        }
    }

    // dibujo de reporte y envio de parametros
    public void aceptoDescuentos() {
        switch (rep_reporte.getNombre()) {
            case "Anticipos Sueldo":
                TablaGenerica tabCuenta = programas.getCodCuentas("PP");
                TablaGenerica tabTabla = programas.getReporte(Integer.parseInt(tabCuenta.getValor("id_codigo")), Integer.parseInt(cmbEmpleado.getValue() + ""));
                if (!tabTabla.isEmpty()) {
                    p_parametros.put("distributivo", Integer.parseInt(cmbEmpleado.getValue() + ""));
                    p_parametros.put("descripcion", tabTabla.getValor("distributivo"));
                    p_parametros.put("descrip", tabTabla.getValor("descripcion"));
                    p_parametros.put("pide_ano", Integer.parseInt(tabTabla.getValor("anio")));
                    p_parametros.put("columnas", Integer.parseInt(tabTabla.getValor("columna")));
                    p_parametros.put("partida", tabTabla.getValor("cuenta"));
                    p_parametros.put("partida1", tabTabla.getValor("cuenta1"));
                    p_parametros.put("periodo", utilitario.getMes(utilitario.getFechaActual()));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("Seleccione Valor", null);
                }
                break;
            case "IESS":
                TablaGenerica tabCuent = programas.getCodCuentas("IS");
                if (!tabCuent.isEmpty()) {
                    p_parametros.put("distributivo", Integer.parseInt(cmbEmpleado1.getValue() + ""));
                    p_parametros.put("descripcion", servidors(Integer.parseInt(cmbEmpleado1.getValue() + "")));
                    p_parametros.put("anio", Integer.parseInt(cmbAnio.getValue() + ""));
                    p_parametros.put("periodo", Integer.parseInt(cmbMes.getValue() + ""));
                    p_parametros.put("mes", meses(Integer.parseInt(cmbMes.getValue() + "")));
                    p_parametros.put("codigo", Integer.parseInt(tabCuent.getValor("id_codigo")));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("Seleccione Valor", null);
                }
                break;
            case "Retenciones":
                TablaGenerica tabCuents = programas.getCodCuentas("RJ");
                if (!tabCuents.isEmpty()) {
                    p_parametros.put("distributivo", Integer.parseInt(cmbEmpleado2.getValue() + ""));
                    p_parametros.put("descripcion", servidors(Integer.parseInt(cmbEmpleado2.getValue() + "")));
                    p_parametros.put("anio", Integer.parseInt(cmbAnio1.getValue() + ""));
                    p_parametros.put("periodo", Integer.parseInt(cmbMes1.getValue() + ""));
                    p_parametros.put("mes", meses(Integer.parseInt(cmbMes1.getValue() + "")));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("Seleccione Valor", null);
                }
                break;
        }
    }

    public String servidors(Integer numero) {
        switch (numero) {
            case 2:
                servidor = "Trabajador";
                break;
            case 1:
                servidor = "Empleado";
                break;
        }
        return servidor;
    }

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

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }

    public Tabla getSetCuenta() {
        return setCuenta;
    }

    public void setSetCuenta(Tabla setCuenta) {
        this.setCuenta = setCuenta;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
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

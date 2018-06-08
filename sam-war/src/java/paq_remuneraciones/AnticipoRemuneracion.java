/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import static framework.aplicacion.Framework.FORMATO_FECHA;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;

/**
 *
 * @author p-chumana
 */
public class AnticipoRemuneracion extends Pantalla {

    //tablas
    private Tabla tabAnticipo = new Tabla();
    private Tabla tabGarante = new Tabla();
    private Tabla tabSolicitud = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setGarante = new Tabla();
    private Tabla setAnticipo = new Tabla();
    private AreaTexto texComentario = new AreaTexto();
    private Etiqueta etiComentario = new Etiqueta("Ingrese Comentario : ");
    //Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    //autocompletar
    private AutoCompletar autBusca = new AutoCompletar();
    private List lisServidor = new ArrayList();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogoc = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridc = new Grid();
    private Grid gridD = new Grid();
    private Grid gridC = new Grid();
    @EJB
    private BeanRemuneracion adminRemuneracion = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);
    private ClaseGenerica clase = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);
    String PasAuAnt = "0", PasRmu = "0", PasFecha = "0", PasAutoFec = "0", PasGarante = "0", PasCuota = "0", PasIde = "0", PasId = "0", PasAnFis = "0";

    public AnticipoRemuneracion() {

        //Para capturar el usuario que se encuntra utilizando la opción
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();
//
        Boton botGarante = new Boton();
        botGarante.setValue("GARANTES");
        botGarante.setExcluirLectura(true);
        botGarante.setIcon("ui-icon-contact");
        botGarante.setMetodo("verGarante");
        bar_botones.agregarBoton(botGarante);

        Boton botAnular = new Boton();
        botAnular.setValue("ANULAR");
        botAnular.setExcluirLectura(true);
        botAnular.setIcon("ui-icon-contact");
        botAnular.setMetodo("verAnticipo");
        bar_botones.agregarBoton(botAnular);

        // Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_talento.png");
//        agregarComponente(quinde);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS");
        agregarComponente(panOpcion);

        //Auto busqueda para, verificar solicitud
        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("select id_solicitud,fecha_ant,ced_empleado,nom_empleado,estado_solicitud from nom_solicitud where estado_solicitud in (3,4)");
        autBusca.setMetodoChange("filtrarSolicitud");
        autBusca.setSize(80);
//        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud:"));
//        bar_botones.agregarComponente(autBusca);

        setAnticipo.setId("setAnticipo");
        setAnticipo.setSql("SELECT n.id_solicitud,n.fecha_ant,n.ced_empleado,n.nom_empleado,a.valor\n"
                + "FROM nom_solicitud n\n"
                + "INNER JOIN nom_anticipo a ON a.id_solicitud = n.id_solicitud\n"
                + "WHERE n.estado_solicitud IN (3, 4)");
        setAnticipo.getColumna("ced_empleado").setFiltro(true);
        setAnticipo.getColumna("nom_empleado").setFiltro(true);
        setAnticipo.setLectura(true);
        setAnticipo.setRows(10);
        setAnticipo.dibujar();

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Vizualizar Garante Disponible"); //titulo
        diaDialogo.setWidth("55%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("55%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        grid.setColumns(4);
        agregarComponente(diaDialogo);

        diaDialogoc.setId("diaDialogoc");
        diaDialogoc.setTitle("Vizualizar Anticipo"); //titulo
        diaDialogoc.setWidth("45%"); //siempre en porcentajes  ancho
        diaDialogoc.setHeight("55%");//siempre porcentaje   alto
        diaDialogoc.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogoc.getBot_aceptar().setMetodo("aceptoAnulacion");
        gridc.setColumns(4);
        agregarComponente(diaDialogoc);

        dibujarPantalla();
    }

    /*
     * Formulario de ingreso
     */
    public void dibujarPantalla() {
        limpiarPanel();
        tabSolicitud.setId("tabSolicitud");
        tabSolicitud.setTabla("nom_solicitud", "id_solicitud", 1);
        if (autBusca.getValue() == null) {
            tabSolicitud.setCondicion("id_solicitud=-1");
        } else {
            tabSolicitud.setCondicion("id_solicitud=" + autBusca.getValor());
        }
        tabSolicitud.getColumna("ced_empleado").setMetodoChange("buscarDatos");
//        tabSolicitud.getColumna("id_tipo").setCombo("SELECT id_tipo,desc_tipo FROM dbo.nom_tipo where obs_tipo = 'TIP'");

        tabSolicitud.getColumna("ide_responsable").setValorDefecto(utilitario.getVariable("NICK"));
        tabSolicitud.getColumna("ip_responsable").setValorDefecto(utilitario.getIp());
        tabSolicitud.getColumna("fecha_ant").setValorDefecto(utilitario.getFechaActual());
        tabSolicitud.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tabSolicitud.getColumna("ide_periodo").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));

        List lista = new ArrayList();
        Object fila1[] = {"NL", "EMPLEADO"};
        Object fila2[] = {"CT", "TRABAJADOR"};
        lista.add(fila1);
        lista.add(fila2);
        tabSolicitud.getColumna("id_distributivo").setCombo(lista);
//        tabSolicitud.getColumna("fecha_ant").setLectura(true);
        tabSolicitud.getColumna("suel_empleado").setLectura(true);
        tabSolicitud.getColumna("rmu_empleado").setLectura(true);
        tabSolicitud.getColumna("id_distributivo").setLectura(true);
        tabSolicitud.getColumna("contr_empleado").setLectura(true);
        tabSolicitud.getColumna("TIPCONT_EMPLEADO").setLectura(true);
        tabSolicitud.getColumna("FECHA_SALIDA").setLectura(true);
        tabSolicitud.getColumna("estado_solicitud").setValorDefecto("3");
        tabSolicitud.getColumna("anio").setVisible(false);
        tabSolicitud.getColumna("id_tipo").setVisible(false);
        tabSolicitud.getColumna("ide_periodo").setVisible(false);
        tabSolicitud.getColumna("tip_contrato").setVisible(false);
        tabSolicitud.getColumna("ip_responsable").setVisible(false);
        tabSolicitud.getColumna("ide_responsable").setVisible(false);
        tabSolicitud.getColumna("fecha_aprobacion").setVisible(false);
        tabSolicitud.getColumna("lista_aprobacion").setVisible(false);
        tabSolicitud.getColumna("tipo_solicitud").setVisible(false);
        tabSolicitud.getColumna("ide_aprobacion").setVisible(false);
        tabSolicitud.getColumna("estado_solicitud").setVisible(false);
        tabSolicitud.getColumna("ide_empleado").setVisible(false);
        tabSolicitud.getColumna("dir_empleado").setVisible(false);
        tabSolicitud.getColumna("CONTR_EMPLEADO").setVisible(false);
        tabSolicitud.getColumna("ide_autoriza").setVisible(false);
        tabSolicitud.getColumna("obs_login").setVisible(false);
        tabSolicitud.getColumna("obs_comentario").setVisible(false);
        tabSolicitud.getColumna("obs_fecha").setVisible(false);
        tabSolicitud.getColumna("ide_autoriza").setVisible(false);
        tabSolicitud.getColumna("id_autoriza").setVisible(false);

        tabSolicitud.setTipoFormulario(true);
        tabSolicitud.agregarRelacion(tabGarante);
        tabSolicitud.agregarRelacion(tabAnticipo);
        tabSolicitud.getGrid().setColumns(4);
        tabSolicitud.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setMensajeWarn("INFORMACIÓN DE SOLICITANTE");
        tpa.setPanelTabla(tabSolicitud);

        tabGarante.setId("tabGarante");
        tabGarante.setTabla("nom_garante", "id_garante", 2);
        tabGarante.getColumna("ced_garante").setMetodoChange("datosGarante");
        tabGarante.getColumna("ide_garante").setCombo(datoGarante());

        tabGarante.getColumna("tipo_garante").setLectura(true);
        tabGarante.getColumna("cargo_garante").setLectura(true);

        tabGarante.getColumna("id_garante").setVisible(false);
        tabGarante.getColumna("id_solicitud").setVisible(false);
        tabGarante.getColumna("id_distributivo").setVisible(false);
        tabGarante.getColumna("nom_garante").setVisible(false);
        tabGarante.setTipoFormulario(true);
        tabGarante.getGrid().setColumns(6);
        tabGarante.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setMensajeWarn("INFORMACIÓN DE GARANTE");
        tpd.setPanelTabla(tabGarante);

        tabAnticipo.setId("tabAnticipo");
        tabAnticipo.setTabla("nom_anticipo", "id_anticipo", 3);
        tabAnticipo.getColumna("valor").setMetodoChange("validaAnticipo");
        tabAnticipo.getColumna("cuotas").setMetodoChange("validarCuota");
        tabAnticipo.getColumna("id_tipo").setCombo("SELECT id_tipo,desc_tipo FROM dbo.nom_tipo where obs_tipo = 'TIP'");
//        tabAnticipo.getColumna("fecha").setValorDefecto(utilitario.getFechaActual());
        tabAnticipo.getColumna("id_tipo").setValorDefecto("3");
        tabAnticipo.getColumna("id_tipo").setMetodoChange("nuevo");

        List list = new ArrayList();
        Object fil1[] = {"SI", "SI"};
        list.add(fil1);
        tabAnticipo.getColumna("mes_sig").setCombo(list);

        tabAnticipo.getColumna("porcentaje").setLongitud(1);
        tabAnticipo.getColumna("cuota_adicional").setLongitud(1);
        tabAnticipo.getColumna("valor_mes").setLectura(true);
        tabAnticipo.getColumna("cuota_adicional").setLectura(true);
        tabAnticipo.getColumna("mes_ini").setLectura(true);
        tabAnticipo.getColumna("mes_fin").setLectura(true);
        tabAnticipo.getColumna("anio_ini").setLectura(true);
        tabAnticipo.getColumna("anio_fin").setLectura(true);
        tabAnticipo.getColumna("porcentaje").setLectura(true);

        tabAnticipo.getColumna("id_anticipo").setVisible(false);
        tabAnticipo.getColumna("estado_anticipo").setVisible(false);
        tabAnticipo.getColumna("valor_pagado").setVisible(false);
        tabAnticipo.getColumna("cuota_pagada").setVisible(false);

        tabAnticipo.setTipoFormulario(true);
        tabAnticipo.getGrid().setColumns(4);
        tabAnticipo.dibujar();

        PanelTabla tpp = new PanelTabla();
        tpp.setMensajeWarn("INFORMACIÓN DE ANTICIPO A SOLICITAR");
        tpp.setPanelTabla(tabAnticipo);

        PanelTabla t = new PanelTabla();
        Division div = new Division();
        div.setId("div");
        div.dividir2(tpp, t, "85%", "H");

        Division divFormulario = new Division();
        divFormulario.setId("divFormulario");
        divFormulario.dividir3(tpa, tpd, div, "36%", "43%", "H");
        agregarComponente(divFormulario);

        Grupo gru = new Grupo();
        gru.getChildren().add(divFormulario);
        panOpcion.getChildren().add(gru);
    }

    /*
     * Permite limpiar el formulario
     */
    //borra el contenido de la división central central
    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void nuevo() {
        tabAnticipo.setValor("porcentaje", null);
        tabAnticipo.setValor("cuota_adicional", null);
        tabAnticipo.setValor("valor_mes", null);
        tabAnticipo.setValor("mes_ini", null);
        tabAnticipo.setValor("anio_ini", null);
        tabAnticipo.setValor("mes_fin", null);
        tabAnticipo.setValor("anio_fin", null);
        tabAnticipo.setValor("CUOTAS", null);
        utilitario.addUpdate("tabAnticipo");
    }

    //Filtra el cliente seleccionado en el autocompletar
    public void filtrarSolicitud(SelectEvent evt) {
        limpiar();
        autBusca.onSelect(evt);
        dibujarPantalla();
    }

    //permite limpiar el formulario sin guardar o mantener algun dato
    public void limpia_pa() {
        if (tabSolicitud.getValor("ide_solicitud") != null) {
            utilitario.agregarMensaje("Limpia Formulario", "No Guardado");
        } else {
            eliminar();
            insertar();
        }
    }

    /*
     * Muestra una lista de todos los servidores del GADMUR
     */
    public List datoGarante() {
        lisServidor.clear();
        TablaGenerica tabValidar = adminRemuneracion.getActivaCondicion("GR", "NL,CT");
        if (!tabValidar.isEmpty()) {
            TablaGenerica tabDatos = adminRemuneracion.getInfoGarante(tabValidar.getValor("parametro2"));
            if (!tabDatos.isEmpty()) {
                for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                    Object[] obj = new Object[2];
                    obj[0] = tabDatos.getValor(i, "CODTRA");
                    obj[1] = tabDatos.getValor(i, "NOMTRA");
                    lisServidor.add(obj);
                }
            }
        }
        return lisServidor;
    }

    /*
     * Verifica autorización por diversas restricciones
     */
    public void buscarDatos() {
        tabSolicitud.setValor("ide_empleado", "");
        tabSolicitud.setValor("nom_empleado", "");
        tabSolicitud.setValor("rmu_empleado", "");
        tabSolicitud.setValor("id_distributivo", "");
        tabSolicitud.setValor("cargo_empleado", "");
        tabSolicitud.setValor("tipcont_empleado", "");
        tabSolicitud.setValor("area_empleado", "");
        tabSolicitud.setValor("dir_empleado", "");
        tabSolicitud.setValor("contr_empleado", "");
        tabSolicitud.setValor("tip_contrato", "");
        tabSolicitud.setValor("fecha_ingreso", "");
        tabSolicitud.setValor("fecha_salida", "");
        tabSolicitud.setValor("ide_autoriza", "");
        tabSolicitud.setValor("id_autoriza", "");
        utilitario.addUpdate("tabSolicitud");

        TablaGenerica tabAutor = adminRemuneracion.getAutorizaAnticipo(tabSolicitud.getValor("ced_empleado"), utilitario.getAnio(utilitario.getFechaActual()));
        if (!tabAutor.isEmpty()) {
            PasAuAnt = clase.verificaNull(tabAutor.getValor("autoriza_anticipo"));
            PasRmu = clase.verificaNull(tabAutor.getValor("pasar_rmu"));
            PasFecha = clase.verificaNull(tabAutor.getValor("fecha_recomendada"));
            PasAutoFec = clase.verificaNull(tabAutor.getValor("acepta_fecha"));
            PasGarante = clase.verificaNull(tabAutor.getValor("pasar_garante"));
            PasCuota = clase.verificaNull(tabAutor.getValor("pasar_cuota"));
            PasIde = clase.verificaNull(tabAutor.getValor("ide_autoriza"));
            PasId = clase.verificaNull(tabAutor.getValor("id_autoriza"));
            PasAnFis = clase.verificaNull(tabAutor.getValor("pasar_fiscal"));
            tabAnticipo.setValor("fecha",tabAnticipo.getValor("fecha_ant"));
        }
        validaDatos();
    }

    /*
     * validar solicitud
     */
    public void validaDatos() {
        String ban = "";
        TablaGenerica tabActual = adminRemuneracion.getVerificaDatos(tabSolicitud.getValor("ced_empleado"));
        if (!tabActual.isEmpty()) {
            TablaGenerica tabDato = adminRemuneracion.getVerificaSolicitud(tabSolicitud.getValor("ced_empleado"));
            if (!tabDato.isEmpty()) {// verifica ai ahi anticipo pendiente
                if (tabDato.getValor("id_tipo").equals("1")) {
                    utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo Ordinario Pendiente");
                } else {
                    utilitario.agregarMensajeInfo("Solicitante Posee", "Anticipo ExtraOrdinario Pendiente");
                }
                //System.exit(0);
            } else {
                TablaGenerica tabCondicionVer = adminRemuneracion.getActivaCondicion("VAPR", tabActual.getValor("tipctt"));
                if (!tabCondicionVer.isEmpty()) {
                    if (tabCondicionVer.getValor("parametro").equals("1")) {
                        TablaGenerica tabPreCancelado = adminRemuneracion.getFechaPreCancelacion(tabSolicitud.getValor("ced_empleado"));
                        if (!tabPreCancelado.isEmpty()) {//Verifica si se precancelo ultimo anticipo
                            String fecha = clase.verificaNull(tabPreCancelado.getValor("fecha_pago"));
                            if (fecha != "0") {
                                int anio = 0;
                                int mes = 0;
                                anio = Integer.parseInt(fecha.substring(0, 4));
                                mes = Integer.parseInt(fecha.substring(5, 7));
                                if (utilitario.getAnio(utilitario.getFechaActual()) == anio) {
                                    if (utilitario.getMes(utilitario.getFechaActual()) == mes) {
//                                        if (PasAuAnt.equals("SI")) {
//                                            tabSolicitud.setValor("ide_autoriza", PasIde);
//                                            tabSolicitud.setValor("id_autoriza", PasId);
//                                            utilitario.addUpdate("tabSolicitud");
//                                            //
                                        anticipoCond(tabActual.getValor("tipctt"), tabActual.getValor("tipafi"), tabActual.getValor("fecing"));
//                                        } else {
//                                            utilitario.agregarMensajeInfo("No puede Realizar Anticipo en este mes", "");
//                                        }
                                    } else {
                                        //
                                        anticipoCond(tabActual.getValor("tipctt"), tabActual.getValor("tipafi"), tabActual.getValor("fecing"));
                                    }
                                } else {
                                    //
                                    anticipoCond(tabActual.getValor("tipctt"), tabActual.getValor("tipafi"), tabActual.getValor("fecing"));
                                }
                            } else {
                                anticipoCond(tabActual.getValor("tipctt"), tabActual.getValor("tipafi"), tabActual.getValor("fecing"));
                            }
                        } else {
                            anticipoCond(tabActual.getValor("tipctt"), tabActual.getValor("tipafi"), tabActual.getValor("fecing"));
                        }
                    }
                } else {
                    //
                    anticipoCond(tabActual.getValor("tipctt"), tabActual.getValor("tipafi"), tabActual.getValor("fecing"));
                }
            }
        } else {
            utilitario.agregarMensaje("Datos no encontrados", "Verificar con nomina");
        }
    }

    public void anticipoCond(String distributivo, String contrato, String fecha) {
//            TablaGenerica tabVeri = adminRemuneracion.getVerificaSolicitudAn(tabSolicitud.getValor("ced_empleado"));
        TablaGenerica tabMes = adminRemuneracion.getVerificaCondicion("FCM", distributivo, clase.meses(utilitario.getMes(utilitario.getFechaActual())));
        if (!tabMes.isEmpty()) {
            utilitario.agregarMensajeInfo("El " + clase.meses(utilitario.getMes(utilitario.getFechaActual())) + " No se permite anticipo", null);
        } else {
            TablaGenerica tabValidar = adminRemuneracion.getVerificaCondicion1("AUT", distributivo, contrato);
            if (!tabValidar.isEmpty()) {
                datosSolicitud(distributivo);
            } else if (PasFecha.equals("SI")) {
                datosSolicitud(distributivo);
            } else {
                TablaGenerica tabVerifica = adminRemuneracion.getActivaCondicion("PR", distributivo);
                if (!tabVerifica.isEmpty()) {
                    final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día 
                    int año = utilitario.getAnio(fecha);
                    int mes = utilitario.getMes(fecha);
                    int dia = utilitario.getDia(fecha); //Fecha anterior 
                    Calendar calendar = new GregorianCalendar(año, mes - 1, dia);
                    Date fecha1 = new Date(calendar.getTimeInMillis());
                    long diferencia = (Date.valueOf(utilitario.getFechaActual()).getTime() - fecha1.getTime()) / MILLSECS_PER_DAY;
                    //System.out.println(diferencia);

                    //int dias = 0;
                    //dias = utilitario.getDiferenciasDeFechas(Date.valueOf(fecha), Date.valueOf(utilitario.getFechaActual()));
                    if (diferencia >= Integer.parseInt(tabVerifica.getValor("parametro"))) {
                        datosSolicitud(distributivo);
                    } else {
                        utilitario.agregarMensajeError("No cumple con periodo minimo, de trabajo", "");
                    }
                }
            }
        }
    }

    /*
     * Llenar datos de empleado
     */
    public void datosSolicitud(String distributivo) {
        TablaGenerica tabDia = adminRemuneracion.getActivaCondicion("FCD", distributivo);
        if (!tabDia.isEmpty()) {
            String cadena = tabDia.getValor("parametro2");
            String subcadena = String.valueOf(utilitario.getDia(utilitario.getFechaActual()));
            if (cadena.indexOf(subcadena) != -1) {
                TablaGenerica tabActual = adminRemuneracion.getVerificaDatos(tabSolicitud.getValor("ced_empleado"));
                if (!tabActual.isEmpty()) {
                    tabSolicitud.setValor("ide_empleado", tabActual.getValor("CODTRA"));
                    tabSolicitud.setValor("nom_empleado", tabActual.getValor("nomtra"));
                    tabSolicitud.setValor("rmu_empleado", tabActual.getValor("SUEBAS"));
                    tabSolicitud.setValor("id_distributivo", tabActual.getValor("tipctt"));
                    tabSolicitud.setValor("cargo_empleado", tabActual.getValor("cargo"));
                    tabSolicitud.setValor("tipcont_empleado", tabActual.getValor("contrato"));
                    tabSolicitud.setValor("area_empleado", tabActual.getValor("area"));
                    tabSolicitud.setValor("dir_empleado", tabActual.getValor("coddep"));
                    tabSolicitud.setValor("contr_empleado", tabActual.getValor("tipctt"));
                    tabSolicitud.setValor("tip_contrato", tabActual.getValor("tipafi"));
                    tabSolicitud.setValor("fecha_ingreso", tabActual.getValor("fecing"));
                    tabSolicitud.setValor("fecha_salida", tabActual.getValor("fecsal"));
                    utilitario.addUpdate("tabSolicitud");
                    liquidoAnterior();
                } else {
                    utilitario.agregarMensaje("No se encontraron datos", null);
                }
            } else {
                utilitario.agregarMensaje("Ingreso de anticipo son los dias: " + tabDia.getValor("parametro2") + " de cada mes", null);
            }

//                    if (utilitario.getDia(utilitario.getFechaActual()) >= 1 && utilitario.getDia(utilitario.getFechaActual()) <= Integer.parseInt(tabDia.getValor("parametro"))) {
//                        TablaGenerica tabActual = adminRemuneracion.getVerificaDatos(tabSolicitud.getValor("ced_empleado"));
//                        if (!tabActual.isEmpty()) {
//                            tabSolicitud.setValor("ide_empleado", tabActual.getValor("CODTRA"));
//                            tabSolicitud.setValor("nom_empleado", tabActual.getValor("nomtra"));
//                            tabSolicitud.setValor("rmu_empleado", tabActual.getValor("SUEBAS"));
//                            tabSolicitud.setValor("id_distributivo", tabActual.getValor("tipctt"));
//                            tabSolicitud.setValor("cargo_empleado", tabActual.getValor("cargo"));
//                            tabSolicitud.setValor("tipcont_empleado", tabActual.getValor("contrato"));
//                            tabSolicitud.setValor("area_empleado", tabActual.getValor("area"));
//                            tabSolicitud.setValor("dir_empleado", tabActual.getValor("coddep"));
//                            tabSolicitud.setValor("contr_empleado", tabActual.getValor("tipctt"));
//                            tabSolicitud.setValor("tip_contrato", tabActual.getValor("tipafi"));
//                            tabSolicitud.setValor("fecha_ingreso", tabActual.getValor("fecing"));
//                            tabSolicitud.setValor("fecha_salida", tabActual.getValor("fecsal"));
//                            utilitario.addUpdate("tabSolicitud");
//                            liquidoAnterior();
//                        } else {
//                            utilitario.agregarMensaje("No se encontraron datos", null);
//                        }
//                    } else {
//                        utilitario.agregarMensaje("Ingreso maximo de anticipo es: " + tabDia.getValor("parametro") + " de cada mes", null);
//                    }
        }
    }

    /*
     * Saco remuneracion liquida anterior
     */
    public void liquidoAnterior() {
        TablaGenerica tabActual = adminRemuneracion.getMesActual(tabSolicitud.getValor("ide_empleado"), utilitario.getAnio(utilitario.getFechaActual()), clase.meses(utilitario.getMes(utilitario.getFechaActual()) - 1));
        if (!tabActual.isEmpty()) {
            tabSolicitud.setValor("suel_empleado", tabActual.getValor("liquido"));
            utilitario.addUpdate("tabSolicitud");
        } else {
            String an, mes;
            if (utilitario.getMes(utilitario.getFechaActual()) != 1) {
                an = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
                mes = clase.meses(utilitario.getMes(utilitario.getFechaActual()) - 1);
            } else {
                an = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()) - 1);
                mes = clase.meses(12);
            }
            TablaGenerica tabAnAnterior = adminRemuneracion.getMesActual(tabSolicitud.getValor("ide_empleado"), Integer.parseInt(an), mes);
            if (!tabAnAnterior.isEmpty()) {
                tabSolicitud.setValor("suel_empleado", tabAnAnterior.getValor("liquido"));
                utilitario.addUpdate("tabSolicitud");
            } else {
                TablaGenerica tabAnterior1 = adminRemuneracion.getMesAnterior1(tabSolicitud.getValor("ide_empleado"), Integer.parseInt(an), mes);
                if (!tabAnterior1.isEmpty()) {
                    tabSolicitud.setValor("suel_empleado", tabAnterior1.getValor("liquido"));
                    utilitario.addUpdate("tabSolicitud");
                } else {
                    utilitario.agregarMensaje("No puede mostra sueldo anterior", null);
                    tabSolicitud.setValor("nom_empleado", null);
                    tabSolicitud.setValor("rmu_empleado", null);
                    tabSolicitud.setValor("id_distributivo", null);
                    tabSolicitud.setValor("cargo_empleado", null);
                    tabSolicitud.setValor("tipcont_empleado", null);
                    tabSolicitud.setValor("area_empleado", null);
                    tabSolicitud.setValor("dir_empleado", null);
                    tabSolicitud.setValor("contr_empleado", null);
                    tabSolicitud.setValor("tip_contrato", null);
                    tabSolicitud.setValor("fecha_ingreso", null);
                    tabSolicitud.setValor("fecha_salida", null);
                    utilitario.addUpdate("tabSolicitud");
                }
            }
        }
    }

    /*
     * Datos de garante
     */
    public void datosGarante() {
//        String cadena = "";
        if (tabGarante.getValor("ced_garante").equals(tabSolicitud.getValor("ced_empleado"))) {
            utilitario.agregarMensaje("Solicitante no puede ser su mismo Garante", "");
        } else {
            TablaGenerica tabDato = adminRemuneracion.getVerficaGarante(tabGarante.getValor("ced_garante"));
            if (!tabDato.isEmpty()) {
                utilitario.agregarMensaje("Garante no se encuentra disponible", null);
            } else {
                TablaGenerica tabDatos = adminRemuneracion.getVerificaDatos(tabGarante.getValor("ced_garante"));
                if (!tabDatos.isEmpty()) {
                    TablaGenerica tabValidar = adminRemuneracion.getVerificaCondicion1("GR", tabDatos.getValor("tipctt"), tabDatos.getValor("tipafi"));
                    if (!tabValidar.isEmpty()) {
                        tabGarante.setValor("ide_garante", tabDatos.getValor("codtra"));
                        tabGarante.setValor("ced_garante", tabDatos.getValor("cedciu"));
                        tabGarante.setValor("nom_garante", tabDatos.getValor("NOMTRA"));
                        tabGarante.setValor("tipo_garante", tabDatos.getValor("tipafi"));
                        tabGarante.setValor("cargo_garante", tabDatos.getValor("cargo"));
                        tabGarante.setValor("id_distributivo", tabDatos.getValor("tipctt"));
                        tabGarante.setValor("rmu", tabDatos.getValor("suebas"));
                        utilitario.addUpdate("tabGarante");
                        liquidoGarante();
                    } else if (PasGarante.equals("SI")) {
                        tabGarante.setValor("ide_garante", tabDatos.getValor("codtra"));
                        tabGarante.setValor("ced_garante", tabDatos.getValor("cedciu"));
                        tabGarante.setValor("nom_garante", tabDatos.getValor("NOMTRA"));
                        tabGarante.setValor("tipo_garante", tabDatos.getValor("tipafi"));
                        tabGarante.setValor("cargo_garante", tabDatos.getValor("cargo"));
                        tabGarante.setValor("id_distributivo", tabDatos.getValor("tipctt"));
                        tabGarante.setValor("rmu", tabDatos.getValor("suebas"));
                        utilitario.addUpdate("tabGarante");
                        liquidoGarante();
                    } else {
                        utilitario.agregarMensaje("No apto para ser Garante", "Tipo de Contrato: " + tabDatos.getValor("contrato") + " no cumple requisito");
                    }
                }
            }
        }

//        TablaGenerica tabValidar = adminRemuneracion.getActivaCondicion("GR", "NL,CT");
//        if (!tabValidar.isEmpty()) {
//            TablaGenerica tabDato = adminRemuneracion.getVerficaGarante(tabGarante.getValor("ced_garante"));
//            if (!tabDato.isEmpty()) {
//                TablaGenerica tabActual = adminRemuneracion.getVerificaDatos(tabGarante.getValor("ced_garante"));
//                if (!tabActual.isEmpty()) {
//                    utilitario.agregarMensajeInfo("Garante " + tabActual.getValor("nomtra"), "Tipo de Contrato: " + tabActual.getValor("contrato") + " no cumple requisito, o no se encuentra disponible");
//                }
//            } else {
//                TablaGenerica tabAutor = adminRemuneracion.getAutorizaAnticipo(tabSolicitud.getValor("ced_empleado"), utilitario.getAnio(utilitario.getFechaActual()));
//                if (!tabAutor.isEmpty()) {
//                    if (!tabAutor.getValor("pasar_garante").isEmpty()) {
//                        cadena = "0";
//                    } else {
//                        cadena = "1";
//                    }
//                }
//                TablaGenerica tabDatos = adminRemuneracion.getDatosGarante(tabGarante.getValor("ced_garante"), tabValidar.getValor("parametro2"));
//                if (!tabDatos.isEmpty()) {
//                    if (cadena.equals("0")) {
//                        tabGarante.setValor("ide_garante", tabDatos.getValor("codtra"));
//                        tabGarante.setValor("ced_garante", tabDatos.getValor("cedciu"));
//                        tabGarante.setValor("nom_garante", tabDatos.getValor("NOMTRA"));
//                        tabGarante.setValor("tipo_garante", tabDatos.getValor("coddat"));
//                        tabGarante.setValor("cargo_garante", tabDatos.getValor("cargo"));
//                        tabGarante.setValor("id_distributivo", tabDatos.getValor("tipctt"));
//                        tabGarante.setValor("rmu", tabDatos.getValor("suebas"));
//                        utilitario.addUpdate("tabGarante");
//                        liquidoGarante();
//                    } else {
//                        if (Double.valueOf(tabDatos.getValor("SUEBAS")) >= Double.valueOf(tabSolicitud.getValor("rmu_empleado"))) {
//                            tabGarante.setValor("ide_garante", tabDatos.getValor("codtra"));
//                            tabGarante.setValor("ced_garante", tabDatos.getValor("cedciu"));
//                            tabGarante.setValor("nom_garante", tabDatos.getValor("NOMTRA"));
//                            tabGarante.setValor("tipo_garante", tabDatos.getValor("coddat"));
//                            tabGarante.setValor("cargo_garante", tabDatos.getValor("cargo"));
//                            tabGarante.setValor("id_distributivo", tabDatos.getValor("tipctt"));
//                            tabGarante.setValor("rmu", tabDatos.getValor("suebas"));
//                            utilitario.addUpdate("tabGarante");
//                            liquidoGarante();
//                        } else {
//                            utilitario.agregarMensaje("No apto para ser Garante", "Falta de Liquidez");
//                        }
//                    }
//                } else {
//                    utilitario.agregarMensaje("Garante no se encuentra disponible", null);
//                }
//            }
//        }
    }

    /*
     * Remuneracion anterior de garante
     */
    public void liquidoGarante() {
        TablaGenerica tabActual = adminRemuneracion.getMesActual(tabGarante.getValor("ide_garante"), utilitario.getAnio(utilitario.getFechaActual()), clase.meses(utilitario.getMes(utilitario.getFechaActual()) - 1));
        if (!tabActual.isEmpty()) {
            tabGarante.setValor("rmu_anterior", tabActual.getValor("liquido"));
            utilitario.addUpdate("tabGarante");
        } else {
            String an, mes;
            if (utilitario.getMes(utilitario.getFechaActual()) != 1) {
                an = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
                mes = clase.meses(utilitario.getMes(utilitario.getFechaActual()) - 1);
            } else {
                an = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()) - 1);
                mes = clase.meses(12);
            }
            TablaGenerica tabAntGarante = adminRemuneracion.getMesActual(tabGarante.getValor("ide_garante"), Integer.parseInt(an), mes);
            if (!tabAntGarante.isEmpty()) {
                tabGarante.setValor("rmu_anterior", tabAntGarante.getValor("liquido"));
                utilitario.addUpdate("tabGarante");
            } else {
                TablaGenerica tabAnterior1 = adminRemuneracion.getMesAnterior1(tabGarante.getValor("ide_garante"), Integer.parseInt(an), mes);
                if (!tabAnterior1.isEmpty()) {
                    tabGarante.setValor("rmu_anterior", tabAnterior1.getValor("liquido"));
                    utilitario.addUpdate("tabGarante");
                } else {
                    TablaGenerica tabAutor = adminRemuneracion.getAutorizaAnticipo(tabSolicitud.getValor("ced_empleado"), utilitario.getAnio(utilitario.getFechaActual()));
                    if (!tabAutor.isEmpty()) {
                        if (!tabAutor.getValor("pasar_garante").isEmpty()) {
                            String cadena;
                            if (tabAnterior1.getValor("liquido") != null) {
                                cadena = "0";
                            } else {
                                cadena = "1";
                            }
                            if (cadena.equals("0")) {
                                tabGarante.setValor("rmu_anterior", tabAnterior1.getValor("liquido"));
                                utilitario.addUpdate("tabGarante");
                            } else {
                                tabGarante.setValor("rmu_anterior", "0.0");
                                utilitario.addUpdate("tabGarante");
                            }
                        } else {
                            utilitario.agregarMensaje("No puede mostra sueldo anterior", null);
                            tabGarante.setValor("ide_garante", null);
                            tabGarante.setValor("ced_garante", null);
                            tabGarante.setValor("tipo_garante", null);
                            tabGarante.setValor("cargo_garante", null);
                            tabGarante.setValor("id_distributivo", null);
                            tabGarante.setValor("rmu", null);
                            utilitario.addUpdate("tabGarante");
                        }
                    } else {
                        utilitario.agregarMensaje("No puede mostra sueldo anterior", null);
                        tabGarante.setValor("ide_garante", null);
                        tabGarante.setValor("ced_garante", null);
                        tabGarante.setValor("tipo_garante", null);
                        tabGarante.setValor("cargo_garante", null);
                        tabGarante.setValor("id_distributivo", null);
                        tabGarante.setValor("rmu", null);
                        utilitario.addUpdate("tabGarante");
                    }
                }
            }
        }
    }

    /*
     * Valida monto de anticipo
     */
    public void validaAnticipo() {
        double dato1 = 0, dato2 = 0;
        dato2 = Double.parseDouble(tabSolicitud.getValor("rmu_empleado"));
        dato1 = Double.parseDouble(tabAnticipo.getValor("valor"));
        TablaGenerica tabDato = adminRemuneracion.getBuscarTipo(Integer.parseInt(tabAnticipo.getValor("id_tipo")));
        if (!tabDato.isEmpty()) {
            TablaGenerica tabVerifica = adminRemuneracion.getActivaCondicion(tabDato.getValor("descripcion"), tabSolicitud.getValor("id_distributivo"));
            if (!tabVerifica.isEmpty()) {
                Integer parametro = null;
                parametro = Integer.parseInt(tabVerifica.getValor("parametro"));
                /*
                 * Validación de tipo de anticipo
                 */
                if (tabDato.getValor("descripcion").equals("ORD")) {//Anticipo Ordinario
                    if ((dato1 / dato2) <= parametro) {
                        tabAnticipo.setValor("cuotas", tabVerifica.getValor("parametro1"));
                        tabAnticipo.getColumna("cuotas").setLectura(true);
                        tabAnticipo.getColumna("porcentaje").setLectura(true);
                        utilitario.addUpdate("tabAnticipo");
                        validarCuota();
                    } else {
                        utilitario.agregarMensajeInfo("Anticipo, Hasta una Remuneracion", "Plazo Maximo de Cobro, " + tabVerifica.getValor("parametro1") + " Meses");
                    }
                } else if (tabDato.getValor("descripcion").equals("EXT")) {
                    if ((dato1 / dato2) > 1 && (dato1 / dato2) <= parametro) {//Anticipo Extraordinario
                        validaCuota();
                        nuevo();
                        tabAnticipo.getColumna("cuotas").setLectura(false);
                        utilitario.addUpdate("tabAnticipo");
                        utilitario.agregarMensajeInfo("Ingrese Plazo, para descuento", "");
                    } else if ((dato1 / dato2) <= 1) {
                        utilitario.agregarMensajeInfo("Anticipo Ordinario,Cambio de Tipo", "");
                        nuevo();
                        tabAnticipo.getColumna("cuotas").setLectura(true);
                        utilitario.addUpdate("tabAnticipo");
                    } else {
                        nuevo();
                        tabAnticipo.getColumna("cuotas").setLectura(true);
                        utilitario.addUpdate("tabAnticipo");
                        utilitario.agregarMensajeInfo("Monto Excede Anticipo Permitido", parametro + " Remuneraciones Maximo");
                    }
                } else {
                    utilitario.agregarMensajeInfo("Se debe escoger tipo de anticipo", null);
                }
            } else {
                utilitario.agregarMensajeInfo("Error de validacion monto", null);
            }
        } else {
            utilitario.agregarMensaje("Seleccionar tipo de anticipo", null);
        }
    }

    /*
     * validar tiempo que puede acceder al anticipo
     */
    public void validaCuota() {
        TablaGenerica tabValidar = adminRemuneracion.getVerificaCondicion1("AUT", tabSolicitud.getValor("id_distributivo"), tabSolicitud.getValor("tip_contrato"));
        if (!tabValidar.isEmpty()) {
            TablaGenerica tabDato = adminRemuneracion.getBuscarTipo(Integer.parseInt(tabAnticipo.getValor("id_tipo")));
            if (!tabDato.isEmpty()) {
                TablaGenerica tabTiempo = adminRemuneracion.getActivaCondicion(tabDato.getValor("obs_tipo"), tabSolicitud.getValor("id_distributivo"));
                if (!tabTiempo.isEmpty()) {
//                    TablaGenerica tabParametro = adminRemuneracion.getActivaCondicion(tabDato.getValor("obs_tipo"), tabSolicitud.getValor("id_distributivo"));
//                    if (!tabParametro.isEmpty()) {
//                        int parametro = Integer.parseInt(tabTiempo.getValor("parametro"));
//                        if (utilitario.getDia(utilitario.getFechaActual()) <= parametro) {
//                            if (utilitario.getMes(utilitario.getFechaActual()) == 1) {
//                                utilitario.agregarNotificacion("Plazo maximo de anticipo : ", tabTiempo.getValor("parametro1"), null);
//                            } else {
//                                utilitario.agregarNotificacion("Plazo maximo de anticipo : ", String.valueOf(Integer.parseInt(tabTiempo.getValor("parametro1")) - utilitario.getMes(utilitario.getFechaActual())), null);
//                            }
//                        } else {
//                            if (utilitario.getMes(utilitario.getFechaActual()) == 1) {
//                                utilitario.agregarNotificacion("Plazo maximo de anticipo : ", String.valueOf(Integer.parseInt(tabTiempo.getValor("parametro1")) - utilitario.getMes(utilitario.getFechaActual())), null);
//                            } else {
//                                utilitario.agregarNotificacion("Plazo maximo de anticipo : ", String.valueOf(Integer.parseInt(tabTiempo.getValor("parametro1")) - utilitario.getMes(utilitario.getFechaActual())), null);
//                            }
//                        }
//                    }
                    utilitario.agregarNotificacion("Plazo maximo de anticipo : ", tabTiempo.getValor("parametro1"), null);
                    nuevo();
                    utilitario.addUpdate("tabAnticipo");
                }
            }
        } else {
            Integer numero = utilitario.getDiferenciasDeFechas(Date.valueOf(utilitario.getFechaActual()), Date.valueOf(tabSolicitud.getValor("fecha_salida")));
            Integer meses;
            meses = (numero / 30);
            utilitario.agregarNotificacion("Plazo maximo de anticipo : ", meses.toString() + " Meses", null);
            nuevo();
            utilitario.addUpdate("tabAnticipo");
        }
    }

    /*
     * Validación y Calculo de cuota mensual
     */
    public void validarCuota() {
        TablaGenerica tabValidar = adminRemuneracion.getVerificaCondicion1("AUT", tabSolicitud.getValor("id_distributivo"), tabSolicitud.getValor("tip_contrato"));
        if (!tabValidar.isEmpty()) {
            TablaGenerica tabDato = adminRemuneracion.getBuscarTipo(Integer.parseInt(tabAnticipo.getValor("id_tipo")));
            if (!tabDato.isEmpty()) {
                TablaGenerica tabTiempo = adminRemuneracion.getActivaCondicion(tabDato.getValor("descripcion"), tabSolicitud.getValor("id_distributivo"));
                if (!tabTiempo.isEmpty()) {
                    validarCuota1();
                } else {
                    utilitario.agregarMensaje("Error de validacion de condición", null);
                }
            } else {
                utilitario.agregarMensaje("Error de tipo de anticipo", null);
            }
        } else {
            Integer numero = utilitario.getDiferenciasDeFechas(Date.valueOf(utilitario.getFechaActual()), Date.valueOf(tabSolicitud.getValor("fecha_salida")));
            if (((numero / 30)) >= Integer.parseInt(tabAnticipo.getValor("CUOTAS"))) {
                validarCuota1();
            } else {
                Integer meses;
                meses = (numero / 30);
                utilitario.agregarNotificacion("Plazo maximo de anticipo : ", meses.toString() + " Meses", null);
                nuevo();
                utilitario.addUpdate("tabAnticipo");
            }
        }
    }

    /*
     * Valida porcentaje de monto de descuento en realción al anticipo pedido
     */
    public void validarCuota1() {
        TablaGenerica tabDato = adminRemuneracion.getBuscarTipo(Integer.parseInt(tabAnticipo.getValor("id_tipo")));
        if (!tabDato.isEmpty()) {
            TablaGenerica tabVerifica = adminRemuneracion.getActivaCondicion(tabDato.getValor("descripcion"), tabSolicitud.getValor("id_distributivo"));
            if (!tabVerifica.isEmpty()) {

                Double salario = 0.0;
                Double cuota = (Double.valueOf(tabAnticipo.getValor("valor")) / Double.valueOf(tabAnticipo.getValor("cuotas")));
                if (tabSolicitud.getValor("id_distributivo").equals("CT")) {
                    salario = Double.valueOf(tabSolicitud.getValor("rmu_empleado"));
                } else {
                    salario = Double.valueOf(tabSolicitud.getValor("suel_empleado"));
                }
                Double porcentaje = (salario * Double.valueOf(tabVerifica.getValor("parametro2"))) / 100;/////////

                TablaGenerica tabExcepcion = adminRemuneracion.getActivaCondicion("PMD", tabSolicitud.getValor("id_distributivo"));
                if (!tabExcepcion.isEmpty()) {
                    if(tabExcepcion.getValor("parametro2").equals("1")){
                        calculoCuota();
                    }
                }else{

                if (cuota > porcentaje) {
                    if (clase.verificaNull(PasRmu).equals("SI")) {
//                        if (tabAnticipo.getValor("mes_sig").equals("SI")) {
//                            tabAnticipo.setValor("porcentaje", "70");
//                            utilitario.addUpdate("tabAnticipo");
//                            calculoPorcentaje();
//                        } else {
                        calculoCuota();
                        //verificaPorsenta();
//                        }
                    } else {
                        utilitario.agregarMensajeInfo("Cuota excede capacidad de liquidez", null);
                    }
                } else {
//                    if (clase.verificaNull(tabAnticipo.getValor("mes_sig")).equals("SI")) {
//                        tabAnticipo.setValor("porcentaje", "70");
//                        utilitario.addUpdate("tabAnticipo");
//                        calculoPorcentaje();
//                    } else {
                    calculoCuota();
                    //verificaPorsenta();
//                    }
                }
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede validar monto", null);
            }
        }
    }

    /*
     * Calculo de cuota y verificación de meses
     */
    public void verificaPorcentaje() {
        TablaGenerica tabAutor = adminRemuneracion.getAutorizaAnticipo(tabSolicitud.getValor("ced_empleado"), utilitario.getAnio(utilitario.getFechaActual()));
        if (!tabAutor.isEmpty()) {
            String cadena;
            if (tabAutor.getValor("porcentaje") != null) {
                cadena = "0";
            } else {
                cadena = "1";
            }

            if (cadena.equals("0")) {
                Double cuota = Double.valueOf(tabAnticipo.getValor("valor")) / Double.valueOf(tabAnticipo.getValor("cuotas"));
                Double valor = Math.rint(cuota * 100) / 100;
                tabAnticipo.setValor("valor_mes", valor.toString());
                tabAnticipo.setValor("porcentaje", null);
                tabAnticipo.setValor("cuota_adicional", null);
                utilitario.addUpdate("tabAnticipo");
                periodoAnticipo();
            } else {
                calculoCuota();
            }

        } else {
            calculoCuota();
        }

    }

    public void calculoCuota() {
        int ban = 0, parametro = 0, maximo = 0;
        String param;
        TablaGenerica tabVerifica = adminRemuneracion.getActivaCondicion("FCC", tabSolicitud.getValor("id_distributivo"));
        if (!tabVerifica.isEmpty()) {
            if (utilitario.getDia(utilitario.getFechaActual()) <= Integer.parseInt(tabVerifica.getValor("parametro"))) {
                ban = 1;
            } else {
                ban = 0;
            }
        }
        if (tabAnticipo.getValor("id_tipo").equals("2")) {
            param = "EXT";
        } else {
            param = "ORD";
        }

        TablaGenerica tabPlazo = adminRemuneracion.getActivaCondicion(param, tabSolicitud.getValor("id_distributivo"));
        if (!tabPlazo.isEmpty()) {
            maximo = Integer.parseInt(tabPlazo.getValor("parametro1"));
        }

        if (tabSolicitud.getValor("id_distributivo").equals("NL")) {
            if (clase.verificaNull(tabAnticipo.getValor("mes_sig")).equals("SI")) {
                parametro = 1;
            } else {
                parametro = 0;
            }
        }
        Integer mes = utilitario.getMes(utilitario.getFechaActual());
        Integer cantidad = (mes - ban + parametro) + Integer.parseInt(tabAnticipo.getValor("cuotas"));
        if (cantidad == 12) {
            Double cuota = Double.valueOf(tabAnticipo.getValor("valor")) / Double.valueOf(tabAnticipo.getValor("cuotas"));
            Double valor = Math.rint(cuota * 100) / 100;
            tabAnticipo.setValor("valor_mes", valor.toString());
            tabAnticipo.setValor("porcentaje", null);
            tabAnticipo.setValor("cuota_adicional", null);
            utilitario.addUpdate("tabAnticipo");
            periodoAnticipo();
        } else if (cantidad < 12) {
            Double cuota = Double.valueOf(tabAnticipo.getValor("valor")) / Double.valueOf(tabAnticipo.getValor("cuotas"));
            Double valor = Math.rint(cuota * 100) / 100;
            tabAnticipo.setValor("valor_mes", valor.toString());
            tabAnticipo.setValor("porcentaje", null);
            tabAnticipo.setValor("cuota_adicional", null);
            utilitario.addUpdate("tabAnticipo");
            periodoAnticipo();
        } else if (cantidad > 12) {
            if (tabSolicitud.getValor("id_distributivo").equals("CT")) {
                if (Integer.parseInt(tabAnticipo.getValor("cuotas")) <= maximo) {
                    Double cuota = Double.valueOf(tabAnticipo.getValor("valor")) / Double.valueOf(tabAnticipo.getValor("cuotas"));
                    Double valor = Math.rint(cuota * 100) / 100;
                    tabAnticipo.setValor("valor_mes", valor.toString());
                    tabAnticipo.setValor("porcentaje", null);
                    tabAnticipo.setValor("cuota_adicional", null);
                    utilitario.addUpdate("tabAnticipo");
                    periodoAnticipo();
                } else {
                    utilitario.agregarMensaje("Plazo de renumeración ", String.valueOf(maximo) + " Meses");
                    nuevo();
                    utilitario.addUpdate("tabAnticipo");
                }

            } else if (PasAnFis.equals("SI")) {
                tabAnticipo.setValor("porcentaje", "70");
                utilitario.addUpdate("tabAnticipo");
                calculoPorcentaje();
            } else {
                nuevo();
                utilitario.addUpdate("tabAnticipo");
                utilitario.agregarMensaje("Plazo de renumeración excede año fiscal", String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
            }
        }
    }

    /*
     * Porcentaje de descuento, para diciembre LOSEP
     */
    public void calculoPorcentaje() {
        int ban = 0;
        TablaGenerica tabVerificas = adminRemuneracion.getActivaCondicion("FCC", tabSolicitud.getValor("id_distributivo"));
        if (!tabVerificas.isEmpty()) {
            if (utilitario.getDia(utilitario.getFechaActual()) <= Integer.parseInt(tabVerificas.getValor("parametro"))) {
                ban = 1;
            } else {
                ban = 0;
            }
        }
        Integer mess = utilitario.getMes(utilitario.getFechaActual());
        Integer cantidad = (mess - ban) + Integer.parseInt(tabAnticipo.getValor("cuotas"));
        if (cantidad == 12) {
            Double cuota = (Double.valueOf(tabSolicitud.getValor("RMU_EMPLEADO")) * Double.valueOf(tabAnticipo.getValor("porcentaje"))) / 100;
            Double mes = Double.valueOf(tabAnticipo.getValor("valor")) - cuota;
            Double valor = mes / (Double.valueOf(tabAnticipo.getValor("cuotas")) - 1);
            Double mensual = Math.rint(valor * 100) / 100;
            tabAnticipo.setValor("valor_mes", mensual.toString());
            tabAnticipo.setValor("cuota_adicional", cuota.toString());
            utilitario.addUpdate("tabAnticipo");
            periodoAnticipo();
        } else {
            utilitario.agregarMensaje("Solo Disponible para el mes de diciembre", "Disponible solo para Empleados");
        }
    }

    /*
     * Fechas de inicio y fin de anticipo hasta el 15
     */
    public void periodoAnticipo() {
        String mes_ini = "", anio_ini = "", anio_fin = "";
        Date fecha;
        Integer dia = 0, mesa = 0, parametro = 0;
        if (PasAutoFec.equals("SI")) {
        } else {
            mes_ini = String.valueOf(utilitario.getMes(utilitario.getFechaActual()));
            anio_ini = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
            dia = utilitario.getDia(utilitario.getFechaActual());
            anio_fin = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
            mesa = utilitario.getMes(utilitario.getFechaActual());

        }

//        TablaGenerica tabFecha = adminRemuneracion.getAutorizaAnticipo(tabSolicitud.getValor("ced_empleado"), utilitario.getAnio(utilitario.getFechaActual()));
//        if (!tabFecha.isEmpty()) {
//            tabSolicitud.setValor("fecha_ant", tabFecha.getValor("fecha_autoriza"));
//            tabSolicitud.setValor("anio", tabFecha.getValor("anio"));
//            tabSolicitud.setValor("ide_periodo", tabFecha.getValor("mes"));
//            utilitario.addUpdate("tabSolicitud");
//            tabAnticipo.setValor("fecha", tabFecha.getValor("fecha_autoriza"));
//            utilitario.addUpdate("tabAnticipo");
//
//            dia = Integer.parseInt(tabFecha.getValor("dia"));
//            mes_ini = tabFecha.getValor("mes");
//            anio_ini = tabFecha.getValor("anio");
//            anio_fin = tabFecha.getValor("anio");
//            mesa = Integer.parseInt(tabFecha.getValor("mes"));
//        } else {
//            mes_ini = String.valueOf(utilitario.getMes(utilitario.getFechaActual()));
//            anio_ini = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
//            dia = utilitario.getDia(utilitario.getFechaActual());
//            anio_fin = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
//            mesa = utilitario.getMes(utilitario.getFechaActual());
//        }
        TablaGenerica tabDia = adminRemuneracion.getActivaCondicion("FCC", "NL,CT");
        if (!tabDia.isEmpty()) {
            if (clase.verificaNull(tabAnticipo.getValor("mes_sig")).equals("SI")) {
                parametro = utilitario.getDia(utilitario.getFechaActual());
            } else {
                parametro = Integer.parseInt(tabDia.getValor("parametro"));
            }
            if (dia >= 1 && dia < parametro) {
                tabAnticipo.setValor("mes_ini", mes_ini);
                tabAnticipo.setValor("anio_ini", anio_ini);
                utilitario.addUpdate("tabAnticipo");
                Integer total, fin;
                Integer cuota = Integer.parseInt(tabAnticipo.getValor("cuotas"));
                Integer mes = mesa;
                Integer plazo = mes + cuota;
                if (plazo <= 25) {
                    total = plazo - 1;
                    if (total <= 12) {
                        tabAnticipo.setValor("mes_fin", total.toString());
                        tabAnticipo.setValor("anio_fin", anio_fin);
                        utilitario.addUpdate("tabAnticipo");
                    } else {
                        fin = total - 12;
                        tabAnticipo.setValor("mes_fin", fin.toString());
                        tabAnticipo.setValor("anio_fin", String.valueOf(Integer.parseInt(anio_fin) + 1));
                        utilitario.addUpdate("tabAnticipo");
                    }
                } else {
                    total = plazo - 1;
                    fin = total - 24;
                    tabAnticipo.setValor("mes_fin", fin.toString());
                    tabAnticipo.setValor("anio_fin", String.valueOf(Integer.parseInt(anio_fin) + 2));
                    utilitario.addUpdate("tabAnticipo");
                }
            } else {
                periodoAnticipo1();
            }
        }
    }

    /*
     * Fechas de inicio y fin de anticipo pasdo el 15
     */
    public void periodoAnticipo1() {
        if (utilitario.getMes(utilitario.getFechaActual()) == 12) {
            tabAnticipo.setValor("mes_ini", String.valueOf(1));
            tabAnticipo.setValor("anio_ini", String.valueOf(utilitario.getAnio(utilitario.getFechaActual()) + 1));

        } else {
            tabAnticipo.setValor("mes_ini", String.valueOf(utilitario.getMes(utilitario.getFechaActual()) + 1));
            tabAnticipo.setValor("anio_ini", String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        }
        utilitario.addUpdate("tabAnticipo");
        Integer total, fin;
        Integer cuota = Integer.parseInt(tabAnticipo.getValor("cuotas"));
        Integer mes = utilitario.getMes(utilitario.getFechaActual()) + 1;
        Integer plazo = mes + cuota;
        if (plazo <= 25) {
            total = plazo - 1;
            if (total <= 12) {
                tabAnticipo.setValor("mes_fin", total.toString());
                tabAnticipo.setValor("anio_fin", String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
                utilitario.addUpdate("tabAnticipo");
            } else {
                fin = total - 12;
                tabAnticipo.setValor("mes_fin", fin.toString());
                tabAnticipo.setValor("anio_fin", String.valueOf(utilitario.getAnio(utilitario.getFechaActual()) + 1));
                utilitario.addUpdate("tabAnticipo");
            }
        } else {
            total = plazo - 1;
            fin = total - 24;
            tabAnticipo.setValor("mes_fin", fin.toString());
            tabAnticipo.setValor("anio_fin", String.valueOf(utilitario.getAnio(utilitario.getFechaActual()) + 2));
            utilitario.addUpdate("tabAnticipo");
        }
    }

    /*
     * Permite identificar garante
     */

    public void verGarante() {
        diaDialogo.Limpiar();
//        lisGarante.clear();
//        TablaGenerica tabDatos = adminRemuneracion.getInfoGarante1();
//        if (!tabDatos.isEmpty()) {
//            for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
//                Object[] obj = new Object[2];
//                obj[0] = tabDatos.getValor(i, "CODTRA");
//                obj[1] = tabDatos.getValor(i, "NOMTRA");
//                lisGarante.add(obj);
//            }
//        }
        diaDialogo.setDialogo(grid);
        setGarante.setId("setGarante");
        setGarante.setSql("SELECT\n"
                + "g.ced_garante,\n"
                + "g.nom_garante,\n"
                + "s.ced_empleado,\n"
                + "s.nom_empleado,\n"
                + "s.fecha_ant\n"
                + "FROM nom_solicitud s\n"
                + "INNER JOIN nom_garante g ON g.id_solicitud = s.id_solicitud\n"
                + "where s.estado_solicitud in (select id_tipo from nom_tipo where desc_tipo = 'Ingreso' or desc_tipo = 'Aprobado' or desc_tipo = 'Cobrando')");
        setGarante.getColumna("ced_garante").setFiltro(true);
        setGarante.getColumna("nom_garante").setFiltro(true);
        setGarante.getColumna("nom_empleado").setFiltro(true);
        setGarante.setLectura(true);
        setGarante.setRows(15);
        gridD.getChildren().add(setGarante);
        diaDialogo.setDialogo(gridD);
        diaDialogo.setWidth("50%"); //siempre en porcentajes  ancho
        setGarante.dibujar();
        diaDialogo.dibujar();
    }

    public void verAnticipo() {
        diaDialogoc.setDialogo(gridc);
        Grid griMarcas = new Grid();
        griMarcas.setColumns(2);
        griMarcas.getChildren().add(etiComentario);
        texComentario.setCols(45);
        griMarcas.getChildren().add(texComentario);
        gridC.getChildren().add(setAnticipo);
        gridC.getChildren().add(griMarcas);
        diaDialogoc.setDialogo(gridC);
        setAnticipo.dibujar();
        diaDialogoc.dibujar();
    }

    public void aceptoAnulacion() {
        if (setAnticipo.getValorSeleccionado() != null && texComentario.getValue() != null) {
            adminRemuneracion.setAnulaSolicitud(Integer.parseInt(setAnticipo.getValorSeleccionado() + ""), texComentario.getValue() + "", utilitario.getVariable("NICK"), "Anulado");
            adminRemuneracion.actualizSolicitud(Integer.parseInt(setAnticipo.getValorSeleccionado() + ""), "Anulado");
            adminRemuneracion.setActualizaDetalle(Integer.parseInt(setAnticipo.getValorSeleccionado() + ""), "Anulado");
            utilitario.agregarMensaje("Solicitud anulada", null);
        } else {
            utilitario.agregarMensaje("Debe Seleccionar al menos un registro", "Debe ingresar un comentario");
        }
    }

    @Override
    public void insertar() {
        if (tabSolicitud.isFocus()) {
            autBusca.limpiar();
            utilitario.addUpdate("autBusca");
            tabSolicitud.limpiar();
            tabSolicitud.insertar();
            tabGarante.limpiar();
            tabGarante.insertar();
            tabAnticipo.limpiar();
            tabAnticipo.insertar();
        }
    }

    @Override
    public void guardar() {
        double dato3 = 0;
        if (tabSolicitud.getValor("id_solicitud") != null) {
            utilitario.agregarMensaje("Registro No Puede Ser Guardado", "");
        } else {
            tabSolicitud.setValor("ide_autoriza", PasIde);
            tabSolicitud.setValor("id_autoriza", PasId);
            tabSolicitud.setValor("id_tipo", tabAnticipo.getValor("id_tipo"));
            utilitario.addUpdate("tabSolicitud");
            double dato1 = 0, dato2 = 0;
            dato2 = Double.parseDouble(tabSolicitud.getValor("rmu_empleado"));
            dato1 = Double.parseDouble(tabAnticipo.getValor("valor"));
            TablaGenerica tabDato = adminRemuneracion.getBuscarTipo(Integer.parseInt(tabAnticipo.getValor("id_tipo")));
            if (!tabDato.isEmpty()) {
                Integer parametro = null;
                TablaGenerica tabVerifica = adminRemuneracion.getActivaCondicion(tabDato.getValor("descripcion"), tabSolicitud.getValor("id_distributivo"));
                if (!tabVerifica.isEmpty()) {
                    parametro = Integer.parseInt(tabVerifica.getValor("parametro"));
                    if (tabDato.getValor("descripcion").equals("ORD")) {//Anticipo Ordinario
                        if ((dato1 / dato2) <= parametro) {
                            if (tabSolicitud.guardar()) {
                                if (Integer.parseInt(tabSolicitud.getValor("id_autoriza")) > 0) {
                                    adminRemuneracion.actuaAutori(Integer.parseInt(tabSolicitud.getValor("id_autoriza")));
                                }
                                if (tabGarante.guardar()) {
                                    if (tabAnticipo.guardar()) {
                                        guardarPantalla();
                                    }
                                }
                            }
                        } else {
                            utilitario.agregarMensajeInfo("Registro No Puede Ser Guardado", "No Cumple Condición de Anticipo Seleccionado");
                        }
                    } else if (tabDato.getValor("descripcion").equals("EXT")) {
                        if ((dato1 / dato2) > 1 && (dato1 / dato2) <= parametro) {//Anticipo Extraordinario
                            if (tabSolicitud.guardar()) {
                                if (!tabSolicitud.getValor("id_autoriza").isEmpty()) {
                                    adminRemuneracion.actuaAutori(Integer.parseInt(tabSolicitud.getValor("id_autoriza")));
                                }
                                if (tabGarante.guardar()) {
                                    if (tabAnticipo.guardar()) {
                                        guardarPantalla();
                                    }
                                }
                            }
                        } else {
                            utilitario.agregarMensajeInfo("Registro No Puede Ser Guardado", "No Cumple Condición de Anticipo Seleccionado");

                        }
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tabSolicitud.isFocus()) {
            if (tabSolicitud.eliminar()) {
                if (tabGarante.eliminar()) {
                    tabAnticipo.eliminar();
                }
            }
        }
    }

    public int getDiferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
        String fechaInicioString = formatoFecha.format(fechaInicial);
        try {
            fechaInicial = (Date) formatoFecha.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = formatoFecha.format(fechaFinal);
        try {
            fechaFinal = (Date) formatoFecha.parse(fechaFinalString);
        } catch (ParseException ex) {
        }
        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    public Tabla getTabAnticipo() {
        return tabAnticipo;
    }

    public void setTabAnticipo(Tabla tabAnticipo) {
        this.tabAnticipo = tabAnticipo;
    }

    public Tabla getTabGarante() {
        return tabGarante;
    }

    public void setTabGarante(Tabla tabGarante) {
        this.tabGarante = tabGarante;
    }

    public Tabla getTabSolicitud() {
        return tabSolicitud;
    }

    public void setTabSolicitud(Tabla tabSolicitud) {
        this.tabSolicitud = tabSolicitud;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Tabla getSetGarante() {
        return setGarante;
    }

    public void setSetGarante(Tabla setGarante) {
        this.setGarante = setGarante;
    }

    public Tabla getSetAnticipo() {
        return setAnticipo;
    }

    public void setSetAnticipo(Tabla setAnticipo) {
        this.setAnticipo = setAnticipo;
    }
}

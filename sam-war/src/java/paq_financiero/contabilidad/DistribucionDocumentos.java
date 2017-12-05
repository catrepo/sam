/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.panel.Panel;
import paq_financiero.contabilidad.ejb.DocumentosContabilidad;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class DistribucionDocumentos extends Pantalla {
    /*
     * Variable que permite conectar a base de datos diferente
     */

    private Conexion conPostgres = new Conexion();
    /*
     * Declaración de Tablas, para el formulario a utilizar
     */
    private Tabla tabTabla = new Tabla();
    private Tabla tabMostra = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Combo cmbASignado = new Combo();
    private Combo cmbFecha = new Combo();
    private Combo cmbDependencia = new Combo();
    private Combo cmbCombop = new Combo();
    private Combo cmbCombou = new Combo();
    private Calendario fechaInicial = new Calendario();
    private Calendario fechaFinal = new Calendario();
    private Texto texOrden = new Texto();
    private Texto texConcepto = new Texto();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogou = new Dialogo();
    private Dialogo diaDialogou1 = new Dialogo();
    private Dialogo diaDialogop = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridu = new Grid();
    private Grid gridu1 = new Grid();
    private Grid gridp = new Grid();
    @EJB
    private DocumentosContabilidad documento = (DocumentosContabilidad) utilitario.instanciarEJB(DocumentosContabilidad.class);

    /*
     * Para Reportes
     */
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public DistribucionDocumentos() {
        /*
         * Permite tener acceso a información, de los datos de registro
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        /*
         * Cadena de conexión para otra base de datos
         */
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        /*
         * formulario con ordenes de pago
         */
        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("tes_documentos", "id_documento", 1);
        tabTabla.getColumna("doc_responsabe").setLongitud(45);
        tabTabla.getColumna("doc_valor").setLongitud(4);
        tabTabla.getColumna("doc_concepto").setLongitud(70);
        tabTabla.getColumna("id_documento").setVisible(false);
        tabTabla.getColumna("doc_loginrev").setVisible(false);
        tabTabla.getColumna("doc_loginasi").setVisible(false);
        tabTabla.getColumna("doc_usuasignacion").setVisible(false);
        tabTabla.getColumna("doc_comprobante").setVisible(false);
        tabTabla.getColumna("doc_dependencia").setVisible(false);
//        tabTabla.getColumna("doc_revisioncon").setVisible(false);
        tabTabla.getColumna("doc_fechacon").setVisible(false);
//        tabTabla.getColumna("doc_revisiondev").setVisible(false);
        tabTabla.getColumna("doc_ejecutado").setVisible(false);
        tabTabla.getColumna("id_tipo").setVisible(false);
        tabTabla.getColumna("doc_fecha").setLectura(true);
        tabTabla.getColumna("doc_numero").setLectura(true);
        tabTabla.getColumna("doc_responsabe").setLectura(true);
        tabTabla.getColumna("doc_concepto").setLectura(true);
        tabTabla.getColumna("doc_revision").setLectura(true);
        tabTabla.getColumna("doc_fecharev").setLectura(true);
        tabTabla.getColumna("doc_valor").setLectura(true);
        tabTabla.getColumna("doc_valor").setLectura(true);
        tabTabla.getColumna("doc_dependencia").setLectura(true);
//        tabTabla.getColumna("doc_observacion").setVisible(false);
        tabTabla.setRows(10);
        tabTabla.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabTabla);

        cmbFecha.setId("cmbFecha");
        cmbFecha.setConexion(conPostgres);
        cmbFecha.setCombo("SELECT DISTINCT cast(doc_fechacon as date) as codigo, cast(doc_fechacon as date) as fecha FROM tes_documentos\n"
                + "where cast(doc_fechacon as date) is not null");

        cmbDependencia.setId("cmbDependencia");
        cmbDependencia.setConexion(conPostgres);
        cmbDependencia.setCombo("SELECT  DISTINCT doc_dependencia as codigo,doc_dependencia FROM tes_documentos\n"
                + "where doc_dependencia is not null");

        cmbASignado.setId("cmbASignado");
        cmbASignado.setCombo("SELECT u.NICK_USUA,u.NOM_USUA,u.IDE_USUA\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and u.ACTIVO_USUA=1 and p.NOM_PERF like '%Contabilidad%'");

        cmbCombou.setId("cmbCombou");
        cmbCombou.setCombo("SELECT u.NICK_USUA,u.NOM_USUA,u.IDE_USUA\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and u.ACTIVO_USUA=1 and p.NOM_PERF like '%Contabilidad%'");

        Panel panPanel = new Panel();
        panPanel.setId("panPanel");
        panPanel.setStyle("width: 1250px;");

        Grid griBusca = new Grid();
        griBusca.setColumns(10);
        griBusca.getChildren().add(new Etiqueta("Colaborador: "));
        griBusca.getChildren().add(cmbASignado);
        griBusca.getChildren().add(new Etiqueta("Fecha: "));
        griBusca.getChildren().add(fechaInicial);
        griBusca.getChildren().add(fechaFinal);
        griBusca.getChildren().add(new Etiqueta("# Documento : "));
        griBusca.getChildren().add(texOrden);
        griBusca.getChildren().add(new Etiqueta("Beneficiario : "));
        griBusca.getChildren().add(texConcepto);

        Grid griMostrar = new Grid();
        griMostrar.setColumns(5);
        Boton botGuardar = new Boton();
        botGuardar.setValue("Guardar");
        botGuardar.setExcluirLectura(true);
        botGuardar.setIcon("ui-icon-disk");
        botGuardar.setMetodo("guardar");
        griMostrar.getChildren().add(botGuardar);

        Boton botAsignar = new Boton();
        botAsignar.setValue("Asignar");
        botAsignar.setExcluirLectura(true);
        botAsignar.setIcon("ui-icon-person");
        botAsignar.setMetodo("asignaDocumento");
        griMostrar.getChildren().add(botAsignar);

        Boton botMostrar = new Boton();
        botMostrar.setValue("Buscar");
        botMostrar.setExcluirLectura(true);
        botMostrar.setIcon("ui-icon-extlink");
        botMostrar.setMetodo("buscarDocumento");
        griMostrar.getChildren().add(botMostrar);

        Boton botReAsignar = new Boton();
        botReAsignar.setValue("Re-Asignar");
        botReAsignar.setExcluirLectura(true);
        botReAsignar.setIcon("ui-icon-refresh");
        botReAsignar.setMetodo("reAsignar");
        griMostrar.getChildren().add(botReAsignar);

        Boton botFinalizar = new Boton();
        botFinalizar.setValue("Procesar");
        botFinalizar.setExcluirLectura(true);
        botFinalizar.setIcon("ui-icon-lightbulb");
        botFinalizar.setMetodo("finalizar");
        griMostrar.getChildren().add(botFinalizar);

        panPanel.getChildren().add(griBusca);
        panPanel.getChildren().add(griMostrar);

        PanelTabla ptt = new PanelTabla();
        ptt.getChildren().add(panPanel);

        tabMostra.setId("tabMostra");
        tabMostra.setConexion(conPostgres);
        tabMostra.setTabla("tes_documentos", "id_documento", 2);
        tabMostra.getColumna("doc_ejecutado").setMetodoChange("cargarDocumento");
        tabMostra.getColumna("id_documento").setVisible(false);
        tabMostra.getColumna("doc_loginrev").setVisible(false);
        tabMostra.getColumna("doc_revision").setVisible(false);
        tabMostra.getColumna("doc_fecharev").setVisible(false);
        tabMostra.getColumna("doc_asignacion").setVisible(false);
//        tabMostra.getColumna("doc_dependencia").setVisible(false);
//        tabMostra.getColumna("doc_comprobante").setVisible(false);
        tabMostra.getColumna("doc_loginasi").setVisible(false);
        tabMostra.getColumna("doc_fechacon").setVisible(false);
        tabMostra.getColumna("id_tipo").setVisible(false);
        tabMostra.getColumna("doc_fecha").setLectura(true);
        tabMostra.getColumna("doc_numero").setLectura(true);
        tabMostra.getColumna("doc_responsabe").setLectura(true);
        tabMostra.getColumna("doc_concepto").setLectura(true);
        tabMostra.getColumna("doc_valor").setLectura(true);
        tabMostra.getColumna("doc_usuasignacion").setLectura(true);
        tabMostra.getColumna("doc_revisiondev").setLectura(true);
        tabMostra.getColumna("doc_revisioncon").setLectura(true);
        tabMostra.getColumna("id_tipo").setLectura(true);
        tabMostra.getColumna("doc_observacion").setLectura(true);
        tabMostra.getColumna("doc_responsabe").setLongitud(45);
        tabMostra.getColumna("doc_concepto").setLongitud(70);
        tabMostra.getColumna("doc_numero").setFiltro(true);
        tabMostra.getColumna("doc_concepto").setFiltro(true);
        tabMostra.getColumna("doc_observacion").setFiltro(true);
        tabMostra.setRows(10);
        tabMostra.dibujar();
        PanelTabla ptm = new PanelTabla();
        ptm.setPanelTabla(tabMostra);

        Division division = new Division();
        division.setId("division");
        division.dividir3(ptt, pto, ptm, "13%", "45%", "H");
        agregarComponente(division);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Seleccione fechas"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("30%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("aceptoDocumento");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        diaDialogou.setId("diaDialogou");
        diaDialogou.setTitle("Escoja Opción "); //titulo
        diaDialogou.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogou.setHeight("20%");//siempre porcentaje   alto
        diaDialogou.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogou.getBot_aceptar().setMetodo("aceptoDocumento");
        gridu.setColumns(2);
        agregarComponente(diaDialogou);

        diaDialogop.setId("diaDialogop");
        diaDialogop.setTitle("Seleccione tipo"); //titulo
        diaDialogop.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogop.setHeight("20%");//siempre porcentaje   alto
        diaDialogop.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogop.getBot_aceptar().setMetodo("aceptoDocumento");
        gridp.setColumns(2);
        agregarComponente(diaDialogop);

        diaDialogou1.setId("diaDialogou1");
        diaDialogou1.setTitle("Seleccione usuario"); //titulo
        diaDialogou1.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogou1.setHeight("20%");//siempre porcentaje   alto
        diaDialogou1.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogou1.getBot_aceptar().setMetodo("aceptoDocumento");
        gridu1.setColumns(2);
        agregarComponente(diaDialogou1);

        cmbCombop.setId("cmbCombop");
        List lista = new ArrayList();
        Object fil1[] = {
            "1", "Ejecutados"
        };
        Object fil2[] = {
            "0", "Pendientes"
        };
        lista.add(fil1);
        lista.add(fil2);
        cmbCombop.setCombo(lista);


        /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        actualizaLista();
        muestraLista();
    }

    public void finalizar() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            boolean x = Boolean.valueOf(tabTabla.getValor(i, "doc_revisioncon"));
            boolean y = Boolean.valueOf(tabTabla.getValor(i, "doc_revisiondev"));
            boolean z = true;
            boolean w = false;
            if ((x == z) && (y == w)) {
                String cadena = "", cadena1 = "";
                if (tabTabla.getValor(i, "doc_revisioncon") == null || tabTabla.getValor(i, "doc_revisioncon") == "" || tabTabla.getValor(i, "doc_revisioncon").isEmpty()) {
                    cadena = null;
                    cadena1 = null;
                } else {
                    cadena = "'" + tabTabla.getValor(i, "doc_revisioncon") + "'";
                    cadena1 = null;
                }
                documento.setActulizaAdministrador(Integer.parseInt(tabTabla.getValor(i, "id_documento")), tabConsulta.getValor("NICK_USUA"), tabConsulta.getValor("NICK_USUA"), cadena, cadena1, tabTabla.getValor(i, "doc_observacion"));
            } else if ((y == z) && (x == w)) {
                String cadena = "", cadena1 = "";
                if (tabTabla.getValor(i, "doc_revisiondev") == null || tabTabla.getValor(i, "doc_revisiondev") == "" || tabTabla.getValor(i, "doc_revisiondev").isEmpty()) {
                    cadena = null;
                    cadena1 = null;
                } else {
                    cadena = "'" + tabTabla.getValor(i, "doc_revisiondev") + "'";
                    cadena1 = null;
                }
                documento.setActulizaAdministrador(Integer.parseInt(tabTabla.getValor(i, "id_documento")), tabConsulta.getValor("NICK_USUA"), tabConsulta.getValor("NICK_USUA"), cadena1, cadena, tabTabla.getValor(i, "doc_observacion"));
            }
        }
        tabTabla.actualizar();
        utilitario.agregarMensaje("Documentos Procesados", null);
        muestraLista();
    }

    public void asignaDocumento() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            boolean x = Boolean.valueOf(tabTabla.getValor(i, "doc_asignacion"));
            boolean y = true;
            if (x && y) {
                documento.setActulizaDocumento(Integer.parseInt(tabTabla.getValor(i, "id_documento")), "doc_asignacion", "doc_usuasignacion",
                        "'" + tabTabla.getValor(i, "doc_asignacion") + "'", "'" + cmbASignado.getValue() + "'", "doc_loginasi",
                        "'" + tabConsulta.getValor("NICK_USUA") + "'");
            }
        }
        tabTabla.actualizar();
        utilitario.agregarMensaje("Documentos Asignados", null);
    }

    public void reAsignar() {
        for (int i = 0; i < tabMostra.getTotalFilas(); i++) {
            boolean x = Boolean.valueOf(tabMostra.getValor(i, "doc_ejecutado"));
            boolean y = true;
            if (x && y) {
                documento.setActulizaDocum1(Integer.parseInt(tabMostra.getValor(i, "id_documento")), "doc_asignacion", "doc_usuasignacion", "doc_revisiondev", "doc_loginasi",
                        "'" + tabMostra.getValor(i, "doc_ejecutado") + "'", "'" + cmbASignado.getValue() + "'", null, "'" + tabConsulta.getValor("NICK_USUA") + "'");
            }
        }
        tabMostra.actualizar();
        utilitario.agregarMensaje("Documentos Re Asignado", null);
    }

    public void buscarDocumento() {
        if (cmbASignado.getValue() != null && fechaInicial.getFecha() == null && fechaFinal.getFecha() == null) {
            buscarColaborador();
        } else if (cmbASignado.getValue() != null && fechaInicial.getFecha() != null && fechaFinal.getFecha() != null) {
            buscaFecha();
        } else if (texOrden.getValue() != null && cmbASignado.getValue() == null && fechaInicial.getFecha() == null && fechaFinal.getFecha() == null && texConcepto.getValue() == null) {
            buscaDocumento();
        } else if (texConcepto.getValue() != null && texOrden.getValue() == null && cmbASignado.getValue() == null && fechaInicial.getFecha() == null && fechaFinal.getFecha() == null) {
            buscaBeneficiario();
        } else {
            utilitario.agregarMensajeInfo("Parametros de busqueda mal definidos", null);
        }
    }

    /*
     *identificar para que area, va el comprobante 
     */
    public void cargarDocumento() {
        tabMostra.setValor("doc_fechacon", utilitario.getFechaHoraActual());
        utilitario.addUpdate("tabMostra");//actualiza solo componente
        TablaGenerica tabDato = documento.getDocumentoValidar(Integer.parseInt(tabMostra.getValorSeleccionado()));
        if (!tabDato.isEmpty()) {
            boolean x = Boolean.valueOf(tabDato.getValor("doc_revisioncon"));
            boolean y = Boolean.valueOf(tabDato.getValor("doc_revisiondev"));
            boolean z = true;
            boolean w = false;
            if ((x == z) && (y == w)) {
                if (tabDato.getValor("doc_numero").length() <= 7) {
                    TablaGenerica tabDatos = documento.getDocumentoAfectacion(tabDato.getValor("doc_numero"));
                    if (!tabDatos.isEmpty()) {
                        tabMostra.setValor("doc_dependencia", tabDatos.getValor("descripcion"));
                        tabMostra.setValor("doc_comprobante", tabDatos.getValor("comprobante"));
                        tabMostra.setValor("doc_fechacon", utilitario.getFechaHoraActual());
                        utilitario.addUpdate("tabMostra");//actualiza solo componente
                    } else {
                        utilitario.agregarMensaje("Comprobante aún no creado", null);
                    }
                }
            } else if ((y == z) && (x == w)) {
                tabMostra.setValor("doc_fechacon", utilitario.getFechaHoraActual());
                utilitario.addUpdate("tabMostra");//actualiza solo componente
            }
        }
    }

    /*
     * consulta por fecha
     */
    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabTabla.setCondicion(getFiltrosAcceso());
            tabTabla.ejecutarSql();
            utilitario.addUpdate("tabTabla");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_revision = 't' and doc_usuasignacion is null";
        return str_filtros;
    }

    /*
     * consulta por acciones realziadas
     */
    public void muestraLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabMostra.setCondicion(getFiltroAcc());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getFiltroAcc() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "(doc_revisioncon is not null or doc_revisiondev is not null) and doc_fechacon is null and doc_ejecutado is null";
        return str_filtros;
    }

    /*
     * consulta de registro por usuario que atendio
     */
    public void buscarColaborador() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabMostra.setCondicion(getFiltroAcceso());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getFiltroAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_usuasignacion = '" + cmbASignado.getValue() + "' and doc_ejecutado is null";
        return str_filtros;
    }

    /*
     * busqueda por rango de fecha y colaborador que atendio
     */
    public void buscaFecha() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabMostra.setCondicion(getFiltroAcces());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getFiltroAcces() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_usuasignacion = '" + cmbASignado.getValue() + "' and doc_fecha between '" + fechaInicial.getFecha() + "' and '" + fechaFinal.getFecha() + "'";
        return str_filtros;
    }

    /*
     * busqueda por numero de documento
     */
    public void buscaDocumento() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabMostra.setCondicion(getFiltroAcce());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getFiltroAcce() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_numero = '" + texOrden.getValue() + "'";
        return str_filtros;
    }

    /*
     * Busqueda por beneficiario
     */
    public void buscaBeneficiario() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabMostra.setCondicion(getFiltroAcceb());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getFiltroAcceb() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_responsabe like %'" + texConcepto.getValue() + "'%";
        return str_filtros;
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tabMostra.getTotalFilas(); i++) {
            boolean x = Boolean.valueOf(tabMostra.getValor(i, "doc_ejecutado"));
            boolean y = true;
            if (x && y) {
                Integer cadena;
                String cadena1;
                if (tabMostra.getValor(i, "doc_comprobante") == null || tabMostra.getValor(i, "doc_comprobante") == "" || tabMostra.getValor(i, "doc_comprobante").isEmpty()) {
                    cadena = null;
                } else {
//                    System.err.println(tabMostra.getValor(i, "doc_comprobante").toString());
                    cadena = Integer.parseInt(tabMostra.getValor(i, "doc_comprobante"));
                }
//                System.err.println(tabMostra.getValor(i, "doc_dependencia"));
                if (tabMostra.getValor(i, "doc_dependencia") == null || tabMostra.getValor(i, "doc_dependencia") == "" || tabMostra.getValor(i, "doc_dependencia").isEmpty()) {
                    cadena1 = null;
                } else {
                    cadena1 = "'" + tabMostra.getValor(i, "doc_dependencia") + "'";
                }
                documento.setActulizaDocum(Integer.parseInt(tabMostra.getValor(i, "id_documento")), "doc_comprobante", "doc_dependencia", "doc_fechacon", "doc_ejecutado",
                        cadena, cadena1, "'" + tabMostra.getValor(i, "doc_fechacon") + "'", "'" + tabMostra.getValor(i, "doc_ejecutado") + "'");
            }
        }
        utilitario.agregarMensaje("Registro Guardado", null);
        muestraLista();
    }

    @Override
    public void eliminar() {
    }

    /*
     * Llamada a reporte
     */
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    //llamado para seleccionar el reporte
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Documentos de Envio":
                diaDialogou.Limpiar();
                gridu.getChildren().add(new Etiqueta("Fecha :"));
                gridu.getChildren().add(cmbFecha);
                gridu.getChildren().add(new Etiqueta("Dependencia :"));
                gridu.getChildren().add(cmbDependencia);
                diaDialogou.setDialogo(gridu);
                diaDialogou.dibujar();
                break;
            case "Documentos Historial":
                diaDialogop.Limpiar();
                gridp.getChildren().add(new Etiqueta("Seleccione Opción :"));
                gridp.getChildren().add(cmbCombop);
                diaDialogop.setDialogo(gridp);
                diaDialogop.dibujar();
                break;
            case "Documentos Usuario":
                diaDialogou1.Limpiar();
                gridu1.getChildren().add(new Etiqueta("Seleccione Usuario :"));
                gridu1.getChildren().add(cmbCombou);
                diaDialogou1.setDialogo(gridu1);
                diaDialogou1.dibujar();
                break;
            case "Documentos Recibidos Fecha":
                diaDialogo.Limpiar();
                grid.getChildren().add(new Etiqueta("Fecha Inicio:"));
                grid.getChildren().add(fechaInicial);
                grid.getChildren().add(new Etiqueta("Fecha Fin:"));
                grid.getChildren().add(fechaFinal);
                diaDialogo.setDialogo(grid);
                diaDialogo.dibujar();
                break;
        }
    }

    // dibujo de reporte y envio de parametros
    public void aceptoDocumento() {
        switch (rep_reporte.getNombre()) {
            case "Documentos de Envio":
                p_parametros.put("fecha", cmbFecha.getValue());
                p_parametros.put("dependencia", cmbDependencia.getValue());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "Documentos Historial":
                String cadena,
                 cadena1;
                if (cmbCombop.getValue().equals("1")) {
                    cadena = "Enviado";
                    cadena1 = "Devuelto";
                } else {
                    cadena = "Pendiente";
                    cadena1 = "Pendiente";
                }
                p_parametros.put("parametro", cadena);
                p_parametros.put("parametro1", cadena1);
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "Documentos Usuario":
                p_parametros.put("usuario", cmbCombou.getValue());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "Documentos Recibidos Fecha":
                p_parametros.put("fechai", fechaInicial.getFecha());
                p_parametros.put("fechaf", fechaFinal.getFecha());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
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

    public Tabla getTabMostra() {
        return tabMostra;
    }

    public void setTabMostra(Tabla tabMostra) {
        this.tabMostra = tabMostra;
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

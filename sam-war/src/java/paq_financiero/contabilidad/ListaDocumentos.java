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
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_financiero.contabilidad.ejb.DocumentosContabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ListaDocumentos extends Pantalla {

    /*
     * Variable que permite conectar a base de datos diferente
     */
//    private Conexion conPostgres = new Conexion();
    /*
     * Declaración de Tablas, para el formulario a utilizar
     */
    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setTabla = new Tabla();
    private Tabla setReingreso = new Tabla();
    private Calendario fechaRegistro = new Calendario();
    private Calendario fechaInicial = new Calendario();
    private Calendario fechaFinal = new Calendario();
    private Combo cmbCombo = new Combo();
    private Combo cmbCombou = new Combo();
    private Combo cmbCombop = new Combo();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogou = new Dialogo();
    private Dialogo diaDialogod = new Dialogo();
    private Dialogo diaDialogop = new Dialogo();
    private Dialogo diaDialogor = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridu = new Grid();
    private Grid gridd = new Grid();
    private Grid gridp = new Grid();
    private Grid gridr = new Grid();
    private Grid gridD = new Grid();
    private Grid gridR = new Grid();
    /*
     * Acceso a manipulación de datos, CRUD
     */
    @EJB
    private DocumentosContabilidad documento = (DocumentosContabilidad) utilitario.instanciarEJB(DocumentosContabilidad.class);

    /*
     * Para Reportes
     */
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public ListaDocumentos() {
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
         * Componentes para barra de heramientas
         */
        bar_botones.agregarComponente(new Etiqueta("Seleccionar : "));
        fechaRegistro.setId("fechaRegistro");
        fechaRegistro.setFechaActual();
        bar_botones.agregarComponente(fechaRegistro);

        Boton botBusca = new Boton();
        botBusca.setValue("Buscar");
        botBusca.setExcluirLectura(true);
        botBusca.setIcon("ui-icon-search");
        botBusca.setMetodo("cargarRegistro");
        bar_botones.agregarBoton(botBusca);

        Boton botLimpiar = new Boton();
        botLimpiar.setValue("Limpiar");
        botLimpiar.setExcluirLectura(true);
        botLimpiar.setIcon("ui-icon-document");
        botLimpiar.setMetodo("cargarRegistro");
        bar_botones.agregarBoton(botLimpiar);

        Boton botRevisar = new Boton();
        botRevisar.setValue("Revisar");
        botRevisar.setExcluirLectura(true);
        botRevisar.setIcon("ui-icon-clipboard");
        botRevisar.setMetodo("lista");
        bar_botones.agregarBoton(botRevisar);

        Boton botReingreso = new Boton();
        botReingreso.setValue("Reingreso");
        botReingreso.setExcluirLectura(true);
        botReingreso.setIcon("ui-icon-new-win");
        botReingreso.setMetodo("reingreso");
        bar_botones.agregarBoton(botReingreso);

        setTabla.setId("setTabla");
        setTabla.setHeader("Seleccione Tramite");
        setTabla.setSql("select d.id_documento,\n"
                + "t.tipo_nombre,\n"
                + "d.doc_fecha,\n"
                + "d.doc_numero,\n"
                + "d.doc_responsable,\n"
                + "d.doc_concepto,\n"
                + "d.doc_valor,\n"
                + "d.doc_revisioncon,\n"
                + "d.doc_revisiondev\n"
                + "FROM tes_documentos d\n"
                + "INNER JOIN tes_tipo_documento t ON d.id_tipo = t.id_tipo\n"
                + "WHERE d.doc_usuasignacion = '" + tabConsulta.getValor("NICK_USUA") + "' and d.doc_revisioncon is null and d.doc_revisiondev is null");
        setTabla.getColumna("doc_concepto").setLongitud(55);
        setTabla.getColumna("tipo_nombre").setLongitud(5);
        setTabla.getColumna("doc_fecha").setVisible(false);
        setTabla.getColumna("tipo_nombre").setLectura(true);
        setTabla.getColumna("doc_numero").setLectura(true);
        setTabla.getColumna("doc_responsable").setLectura(true);
        setTabla.getColumna("doc_concepto").setLectura(true);
        setTabla.getColumna("doc_valor").setLectura(true);
        setTabla.setRows(10);
        setTabla.dibujar();

        setReingreso.setId("setReingreso");
        setReingreso.setHeader("Seleccione Tramite");
        setReingreso.setSql("SELECT id_documento, \n"
                + "doc_fecha, \n"
                + "doc_numero, \n"
                + "doc_responsable, \n"
                + "doc_concepto, \n"
                + "doc_valor ,\n"
                + "(SELECT DISTINCT top 1\n"
                + "doc_fecharev\n"
                + "FROM tes_documentos \n"
                + "where doc_revisiondev is not null\n"
                + "and doc_numero = a.doc_numero\n"
                + "order by doc_fecharev desc ) as ultima\n"
                + "FROM tes_documentos a\n"
                + "where doc_revisiondev is not null order by doc_fecha");
        setReingreso.setLectura(true);
        setReingreso.getColumna("doc_numero").setFiltro(true);
        setReingreso.setRows(10);
        setReingreso.dibujar();

        cmbCombo.setId("cmbCombo");
        cmbCombo.setCombo("SELECT DISTINCT doc_fecharev as fecha, doc_fecharev FROM tes_documentos\n"
                + "where doc_fecharev is not null  and doc_revisioncon is not null order by doc_fecharev desc");

        cmbCombou.setId("cmbCombou");
        cmbCombou.setCombo("SELECT u.NICK_USUA,u.NOM_USUA,u.IDE_USUA\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and p.PERM_UTIL_PERF =0  and u.ACTIVO_USUA=1 and p.NOM_PERF like '%Contabilidad%'");

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
         * formulario con ordenes de pago
         */
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("tes_documentos", "id_documento", 1);
        tabTabla.getColumna("doc_loginrev").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabTabla.getColumna("id_tipo").setCombo("SELECT id_tipo,tipo_nombre FROM tes_tipo_documento where tipo_estado='1'");
        tabTabla.getColumna("doc_revision").setMetodoChange("horaFecha");
        tabTabla.getColumna("doc_revision").setCheck();
        tabTabla.getColumna("id_tipo").setLongitud(4);
        tabTabla.getColumna("doc_valor").setValorDefecto("0.0");
        tabTabla.getColumna("doc_responsable").setLongitud(45);
        tabTabla.getColumna("doc_revision").setLongitud(2);
        tabTabla.getColumna("doc_valor").setLongitud(4);
        tabTabla.getColumna("doc_concepto").setLongitud(70);
        tabTabla.getColumna("id_documento").setVisible(false);
        tabTabla.getColumna("doc_loginrev").setVisible(false);
        tabTabla.getColumna("doc_usuasignacion").setVisible(false);
        tabTabla.getColumna("doc_asignacion").setVisible(false);
        tabTabla.getColumna("doc_loginasi").setVisible(false);
        tabTabla.getColumna("doc_dependencia").setVisible(false);
        tabTabla.getColumna("doc_comprobante").setVisible(false);
        tabTabla.getColumna("doc_revisioncon").setVisible(false);
        tabTabla.getColumna("doc_fechacon").setVisible(false);
        tabTabla.getColumna("doc_revisiondev").setVisible(false);
        tabTabla.getColumna("doc_ejecutado").setVisible(false);
        tabTabla.getColumna("doc_observacion").setVisible(false);
        tabTabla.getColumna("tes_ide_orden_pago").setVisible(false);
        tabTabla.setRows(15);
        tabTabla.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabTabla);
        agregarComponente(pto);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Seleccione fecha a vizualizar"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("aceptoDocumento");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        diaDialogou.setId("diaDialogou");
        diaDialogou.setTitle("Seleccione usuario"); //titulo
        diaDialogou.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogou.setHeight("20%");//siempre porcentaje   alto
        diaDialogou.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogou.getBot_aceptar().setMetodo("aceptoDocumento");
        gridu.setColumns(2);
        agregarComponente(diaDialogou);

        diaDialogod.setId("diaDialogod");
        diaDialogod.setTitle("Seleccione acción de docmuentos recibidos"); //titulo
        diaDialogod.setWidth("80%"); //siempre en porcentajes  ancho
        diaDialogod.setHeight("60%");//siempre porcentaje   alto
        diaDialogod.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogod.getBot_aceptar().setMetodo("aceptoDocum");
        gridd.setColumns(2);
        agregarComponente(diaDialogod);

        diaDialogop.setId("diaDialogop");
        diaDialogop.setTitle("Seleccione tipo"); //titulo
        diaDialogop.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogop.setHeight("20%");//siempre porcentaje   alto
        diaDialogop.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogop.getBot_aceptar().setMetodo("aceptoDocumento");
        gridp.setColumns(2);
        agregarComponente(diaDialogop);

        diaDialogor.setId("diaDialogor");
        diaDialogor.setWidth("65%"); //siempre en porcentajes  ancho
        diaDialogor.setHeight("70%");//siempre porcentaje   alto
        diaDialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogor.getBot_aceptar().setMetodo("aceptoReingreso");
        gridr.setColumns(2);
        agregarComponente(diaDialogor);

        /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        actualizaLista();
    }

    public void lista() {
        diaDialogod.Limpiar();
        diaDialogod.setDialogo(gridd);
        gridD.getChildren().add(setTabla);
        diaDialogod.setDialogo(gridD);
        setTabla.dibujar();
        diaDialogod.dibujar();
    }

    /*
     *Cargar registros de orden de pagos 
     */
    public void cargarRegistro() {
        TablaGenerica tabDato = documento.getOrdenPago(String.valueOf(fechaRegistro.getFecha()));
        if (!tabDato.isEmpty()) {
            for (int i = 0; i < tabDato.getTotalFilas(); i++) {
                TablaGenerica tabInformacion = documento.getDocumentos(tabDato.getValor(i, "tes_fecha_ingreso"), tabDato.getValor(i, "tes_numero_orden"));
                if (!tabInformacion.isEmpty()) {
                } else {
                    documento.setOrdenPago(tabDato.getValor(i, "tes_numero_orden"));
                }
            }
            actualizaLista();
        }
    }

    public void aceptoDocum() {
        for (int i = 0; i < setTabla.getTotalFilas(); i++) {
            boolean x = Boolean.valueOf(setTabla.getValor(i, "doc_revisioncon"));
            boolean y = Boolean.valueOf(setTabla.getValor(i, "doc_revisiondev"));
            boolean z = true;
            boolean w = false;
            if ((x == z) && (y == w)) {
                String cadena = "", cadena1 = "";
                if (setTabla.getValor(i, "doc_revisioncon") == null || setTabla.getValor(i, "doc_revisioncon") == "" || setTabla.getValor(i, "doc_revisioncon").isEmpty()) {
                    cadena = null;
                    cadena1 = null;
                } else {
                    cadena = "'" + setTabla.getValor(i, "doc_revisioncon") + "'";
                    cadena1 = null;
                }
                documento.setActulizaDocumentos(Integer.parseInt(setTabla.getValor(i, "id_documento")), "doc_revisioncon", "doc_revisiondev", cadena, cadena1);
            } else if ((y == z) && (x == w)) {
                String cadena = "", cadena1 = "";
                if (setTabla.getValor(i, "doc_revisiondev") == null || setTabla.getValor(i, "doc_revisiondev") == "" || setTabla.getValor(i, "doc_revisiondev").isEmpty()) {
                    cadena = null;
                    cadena1 = null;
                } else {
                    cadena = "'" + setTabla.getValor(i, "doc_revisiondev") + "'";
                    cadena1 = null;
                }
                documento.setActulizaDocumentos(Integer.parseInt(setTabla.getValor(i, "id_documento")), "doc_revisiondev", "doc_revisioncon", cadena, cadena1);
            }
        }
        diaDialogod.cerrar();
    }

    public void reingreso() {
        diaDialogor.Limpiar();
        diaDialogor.setDialogo(gridr);
        gridR.getChildren().add(setReingreso);
        diaDialogor.setDialogo(gridR);
        setReingreso.dibujar();
        diaDialogor.dibujar();
    }

    public void aceptoReingreso() {
        documento.setReingreso(Integer.parseInt(setReingreso.getValorSeleccionado()));
        utilitario.agregarMensaje("Reingreso Listo", "");
        diaDialogor.cerrar();
    }

    /*
     * inserta la hora y fecha, para el registro revisado
     */
    public void horaFecha() {
        tabTabla.setValor("doc_fecharev", utilitario.getFechaHoraActual());
        utilitario.addUpdate("tabTabla");//actualiza solo componente
    }

    /*
     * consulta por fecha
     */ public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabTabla.setCondicion(getFiltrosAcceso());
            tabTabla.ejecutarSql();
            utilitario.addUpdate("tabTabla");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_fecha = '"
                + String.valueOf(fechaRegistro.getFecha()) + "' and doc_revision is null";
        return str_filtros;
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            if (Integer.parseInt(tabTabla.getValor(i, "id_tipo")) == 1) {
                if (tabTabla.getValor(i, "doc_fecharev") != null) {
                    documento.setActulizaDocumento(Integer.parseInt(tabTabla.getValor(i, "id_documento")), "doc_revision", "doc_fecharev",
                            "'" + tabTabla.getValor(i, "doc_revision") + "'", "'" + tabTabla.getValor(i, "doc_fecharev") + "'", "doc_loginrev",
                            "'" + tabConsulta.getValor("NICK_USUA") + "'");
                }
            } else {
                TablaGenerica tabInformacion = documento.getDocumentos(tabTabla.getValor(i, "doc_fecha"), tabTabla.getValor(i, "doc_numero"));
                if (!tabInformacion.isEmpty()) {
                } else {
                    documento.setOrdenDocumentos(Integer.parseInt(tabTabla.getValor(i, "id_tipo")), fechaRegistro.getFecha(), tabTabla.getValor(i, "doc_numero"), tabTabla.getValor(i, "doc_responsable"),
                            Double.valueOf(tabTabla.getValor(i, "doc_valor")), tabTabla.getValor(i, "doc_concepto"), tabTabla.getValor(i, "doc_revision"), tabTabla.getValor(i, "doc_fecharev"), tabConsulta.getValor("NICK_USUA"));
                }
            }
        }
        utilitario.agregarMensaje("Registro Guardado", null);
//        actualizaLista();
    }

    @Override
    public void eliminar() {
        if (Integer.parseInt(tabTabla.getValor("id_tipo")) != 1) {
            documento.setDeleteDocumento(Integer.parseInt(tabTabla.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro Eliminado", null);
            actualizaLista();
        }
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
            case "Documentos General":
                aceptoDocumento();
                break;
            case "Documentos Historial":
                diaDialogop.Limpiar();
                gridp.getChildren().add(new Etiqueta("Seleccione Opción :"));
                gridp.getChildren().add(cmbCombop);
                diaDialogop.setDialogo(gridp);
                diaDialogop.dibujar();
                break;
            case "Documentos Usuario":
                diaDialogou.Limpiar();
                gridu.getChildren().add(new Etiqueta("Seleccione Usuario :"));
                gridu.getChildren().add(cmbCombou);
                diaDialogou.setDialogo(gridu);
                diaDialogou.dibujar();
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
            case "Documentos General":
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

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
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

    public Tabla getSetTabla() {
        return setTabla;
    }

    public void setSetTabla(Tabla setTabla) {
        this.setTabla = setTabla;
    }

    public Tabla getSetReingreso() {
        return setReingreso;
    }

    public void setSetReingreso(Tabla setReingreso) {
        this.setReingreso = setReingreso;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_financiero.contabilidad.ejb.DocumentosContabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class AsignarDocumentos extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setReingreso = new Tabla();
    private Calendario fechaRegistro = new Calendario();

    private Dialogo diaDialogor = new Dialogo();
    private Grid gridr = new Grid();
    private Grid gridR = new Grid();

    /*
     * Para Reportes
     */
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    /*
     * Acceso a manipulación de datos, CRUD
     */
    @EJB
    private DocumentosContabilidad documento = (DocumentosContabilidad) utilitario.instanciarEJB(DocumentosContabilidad.class);

    public AsignarDocumentos() {
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
        botBusca.setMetodo("cargarOrden");
        bar_botones.agregarBoton(botBusca);

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
        botReingreso.setMetodo("reIngreso");
        bar_botones.agregarBoton(botReingreso);

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("tes_documentos", "id_documento", 1);
        tabTabla.getColumna("doc_loginrev").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabTabla.getColumna("id_tipo").setCombo("SELECT id_tipo,tipo_nombre FROM tes_tipo_documento where tipo_estado='1'");
        tabTabla.getColumna("doc_revision").setMetodoChange("horaFecha");
        tabTabla.getColumna("doc_revision").setCheck();
        tabTabla.getColumna("doc_valor").setValorDefecto("0.0");
        tabTabla.getColumna("doc_responsable").setLongitud(45);
        tabTabla.getColumna("doc_revision").setLongitud(1);
        tabTabla.getColumna("doc_numero").setLongitud(3);
        tabTabla.getColumna("doc_valor").setLongitud(4);
        tabTabla.getColumna("doc_concepto").setLongitud(70);
        tabTabla.getColumna("id_documento").setVisible(false);
        tabTabla.getColumna("doc_loginrev").setVisible(false);
        tabTabla.getColumna("doc_usuasignacion").setVisible(false);
        tabTabla.getColumna("doc_estado").setVisible(false);
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

        /*
        Regingreso de tramites o ordenes de pago
         */
        diaDialogor.setId("diaDialogor");
        diaDialogor.setWidth("65%"); //siempre en porcentajes  ancho
        diaDialogor.setHeight("70%");//siempre porcentaje   alto
        diaDialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogor.getBot_aceptar().setMetodo("aceptoReingreso");
        gridr.setColumns(2);
        agregarComponente(diaDialogor);

        setReingreso.setId("setReingreso");
        setReingreso.setHeader("Seleccione Tramite");
        setReingreso.setSql("SELECT id_documento, \n"
                + "doc_fecha, \n"
                + "doc_numero, \n"
                + "doc_responsable, \n"
                + "doc_concepto, \n"
                + "doc_valor ,\n"
                + "(SELECT top 1 \n"
                + "doc_fecharev\n"
                + "FROM tes_documentos \n"
                + "where doc_estado= 6\n"
                + "and doc_numero = a.doc_numero\n"
                + "order by doc_fecharev desc ) as ultima\n"
                + "FROM tes_documentos a\n"
                + "where doc_estado= 6 order by doc_fecha");
        setReingreso.setLectura(true);
        setReingreso.getColumna("doc_numero").setFiltro(true);
        setReingreso.setRows(10);
        setReingreso.dibujar();

        actualizaLista();
    }

    /*
     *Cargar registros de orden de pagos 
     */
    public void cargarOrden() {
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
                + String.valueOf(fechaRegistro.getFecha()) + "' and doc_estado =2";
        return str_filtros;
    }

    public void reIngreso() {
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

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            if (tabTabla.getValor(i, "doc_fecharev") != null) {
                if (Integer.parseInt(tabTabla.getValor(i, "id_tipo")) == 1) {
                    documento.setTramitesIngreso(tabTabla.getValor(i, "TES_IDE_ORDEN_PAGO"), "'" + tabTabla.getValor(i, "DOC_FECHAREV") + "'", tabConsulta.getValor("NICK_USUA"), Integer.parseInt(tabTabla.getValor(i, "id_tipo")), null, null, null, null,3,1,0,null);
                } else {
                    TablaGenerica tabInformacion = documento.getDocumentos(tabTabla.getValor(i, "doc_fecha"), tabTabla.getValor(i, "doc_numero"));
                    if (!tabInformacion.isEmpty()) {
                    } else {
                        documento.setTramitesIngreso("0", "'" + tabTabla.getValor(i, "DOC_FECHAREV") + "'", tabConsulta.getValor("NICK_USUA"), Integer.parseInt(tabTabla.getValor(i, "id_tipo")), tabTabla.getValor(i, "DOC_NUMERO"), tabTabla.getValor(i, "DOC_RESPONSABLE"), tabTabla.getValor(i, "DOC_CONCEPTO"), null,3,1,0,Double.valueOf(tabTabla.getValor(i, "DOC_VALOR")));
                    }
                }
            }
        }
        utilitario.agregarMensaje("Registro Guardado", null);
    }

    @Override
    public void eliminar() {
        if (Integer.parseInt(tabTabla.getValor("id_tipo")) != 1) {
            documento.setDeleteDocumento(Integer.parseInt(tabTabla.getValorSeleccionado()));
            utilitario.agregarMensaje("Registro Eliminado", null);
            actualizaLista();
        }
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

}

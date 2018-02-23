/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

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
public class DocumentosProcesados extends Pantalla {

    /*
     * Declaración de Tablas, para el formulario a utilizar
     */
    private Tabla tabMostra = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Dialogo diaDialogop = new Dialogo();
    private Grid gridp = new Grid();
    private Etiqueta etiAnio = new Etiqueta("Año : ");
    private Etiqueta etiEstado = new Etiqueta("Estado : ");
    private Combo cmbAnio = new Combo();
    private Combo cmbCombop = new Combo();
    @EJB
    private DocumentosContabilidad documento = (DocumentosContabilidad) utilitario.instanciarEJB(DocumentosContabilidad.class);

    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public DocumentosProcesados() {
        /*
         * Permite tener acceso a información, de los datos de registro
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        tabMostra.setId("tabMostra");
        tabMostra.setTabla("tes_documentos", "id_documento", 1);
        tabMostra.getColumna("doc_revisioncon").setCheck();
        tabMostra.getColumna("doc_revisiondev").setCheck();
        tabMostra.getColumna("id_documento").setVisible(false);
        tabMostra.getColumna("doc_loginrev").setVisible(false);
        tabMostra.getColumna("doc_revision").setVisible(false);
        tabMostra.getColumna("doc_fecharev").setVisible(false);
        tabMostra.getColumna("doc_asignacion").setVisible(false);
        tabMostra.getColumna("doc_loginasi").setVisible(false);
        tabMostra.getColumna("doc_usuasignacion").setVisible(false);
        tabMostra.getColumna("doc_fechacon").setVisible(false);
        tabMostra.getColumna("id_tipo").setVisible(false);
        tabMostra.getColumna("doc_dependencia").setVisible(false);
        tabMostra.getColumna("doc_comprobante").setVisible(false);
        tabMostra.getColumna("doc_ejecutado").setVisible(false);
        tabMostra.getColumna("tes_ide_orden_pago").setVisible(false);
        tabMostra.getColumna("doc_fecha").setLectura(true);
        tabMostra.getColumna("doc_numero").setLectura(true);
        tabMostra.getColumna("doc_responsable").setLectura(true);
        tabMostra.getColumna("doc_concepto").setLectura(true);
        tabMostra.getColumna("doc_valor").setLectura(true);
        tabMostra.getColumna("doc_responsable").setLongitud(40);
        tabMostra.getColumna("doc_concepto").setLongitud(75);
        tabMostra.setRows(10);
        tabMostra.dibujar();
        PanelTabla ptm = new PanelTabla();
        ptm.setPanelTabla(tabMostra);
        agregarComponente(ptm);
        actualizaLista();

        /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        diaDialogop.setId("diaDialogop");
        diaDialogop.setTitle("Seleccione tipo"); //titulo
        diaDialogop.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogop.setHeight("25%");//siempre porcentaje   alto
        diaDialogop.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogop.getBot_aceptar().setMetodo("aceptoDocumento");
        gridp.setColumns(2);
        agregarComponente(diaDialogop);

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

        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("SELECT DISTINCT SUBSTRING(doc_fechacon,0,5) as id,SUBSTRING(doc_fechacon,0,5) as anio\n"
                + "from tes_documentos\n"
                + "inner join tes_tipo_documento on tes_documentos.id_tipo=tes_tipo_documento.id_tipo\n"
                + "where doc_fechacon is not null");
    }

    /*
     * consulta por fecha
     */
    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabMostra.setCondicion(getFiltrosAcceso());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_usuasignacion ='" + tabConsulta.getValor("NICK_USUA") + "' and doc_revisioncon is null and doc_revisiondev is null";
        return str_filtros;
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tabMostra.getTotalFilas(); i++) {
            boolean x = Boolean.valueOf(tabMostra.getValor(i, "doc_revisioncon"));
            boolean y = Boolean.valueOf(tabMostra.getValor(i, "doc_revisiondev"));
            boolean z = true;
            boolean w = false;
            if ((x == z) && (y == w)) {
                String cadena = "", cadena1 = "";
                if (tabMostra.getValor(i, "doc_revisioncon") == null || tabMostra.getValor(i, "doc_revisioncon") == "" || tabMostra.getValor(i, "doc_revisioncon").isEmpty()) {
                    cadena = null;
                    cadena1 = null;
                } else {
                    cadena = "'" + tabMostra.getValor(i, "doc_revisioncon") + "'";
                    cadena1 = null;
                }
                documento.setActulizaDocumento(Integer.parseInt(tabMostra.getValor(i, "id_documento")), "doc_revisioncon", "doc_revisiondev", cadena, cadena1, "doc_observacion", "'" + tabMostra.getValor(i, "doc_observacion") + "'");
            } else if ((y == z) && (x == w)) {
                String cadena = "", cadena1 = "";
                if (tabMostra.getValor(i, "doc_revisiondev") == null || tabMostra.getValor(i, "doc_revisiondev") == "" || tabMostra.getValor(i, "doc_revisiondev").isEmpty()) {
                    cadena = null;
                    cadena1 = null;
                } else {
                    cadena = "'" + tabMostra.getValor(i, "doc_revisiondev") + "'";
                    cadena1 = null;
                }
                documento.setActulizaDocumento(Integer.parseInt(tabMostra.getValor(i, "id_documento")), "doc_revisiondev", "doc_revisioncon", cadena, cadena1, "doc_observacion", "'" + tabMostra.getValor(i, "doc_observacion") + "'");
            }
        }
        utilitario.agregarMensaje("Registro Guardado", null);
        actualizaLista();
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
            case "Documentos Historial":
                diaDialogop.Limpiar();
                gridp.getChildren().add(etiEstado);
                gridp.getChildren().add(cmbCombop);
                gridp.getChildren().add(etiAnio);
                gridp.getChildren().add(cmbAnio);
                diaDialogop.setDialogo(gridp);
                diaDialogop.dibujar();
                break;
        }
    }

    // dibujo de reporte y envio de parametros
    public void aceptoDocumento() {
        switch (rep_reporte.getNombre()) {
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
                p_parametros.put("anio", Integer.parseInt(cmbAnio.getValue() + ""));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                System.err.println(".>>" + p_parametros);
                sef_formato.dibujar();
                break;
        }
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

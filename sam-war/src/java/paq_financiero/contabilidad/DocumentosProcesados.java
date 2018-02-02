/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
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
    @EJB
    private DocumentosContabilidad documento = (DocumentosContabilidad) utilitario.instanciarEJB(DocumentosContabilidad.class);

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
                documento.setActulizaDocumento(Integer.parseInt(tabMostra.getValor(i, "id_documento")), "doc_revisioncon", "doc_revisiondev", cadena, cadena1,"doc_observacion", "'" + tabMostra.getValor(i, "doc_observacion") + "'");
            } else if ((y == z) && (x == w)) {
                String cadena = "", cadena1 = "";
                if (tabMostra.getValor(i, "doc_revisiondev") == null || tabMostra.getValor(i, "doc_revisiondev") == "" || tabMostra.getValor(i, "doc_revisiondev").isEmpty()) {
                    cadena = null;
                    cadena1 = null;
                } else {
                    cadena = "'" + tabMostra.getValor(i, "doc_revisiondev") + "'";
                    cadena1 = null;
                }
                documento.setActulizaDocumento(Integer.parseInt(tabMostra.getValor(i, "id_documento")), "doc_revisiondev", "doc_revisioncon", cadena, cadena1,"doc_observacion", "'" + tabMostra.getValor(i, "doc_observacion") + "'");
            }
        }
        utilitario.agregarMensaje("Registro Guardado", null);
        actualizaLista();
    }

    @Override
    public void eliminar() {
    }
    public Tabla getTabMostra() {
        return tabMostra;
    }

    public void setTabMostra(Tabla tabMostra) {
        this.tabMostra = tabMostra;
    }
}

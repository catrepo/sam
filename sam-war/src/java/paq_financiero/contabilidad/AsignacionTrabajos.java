/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_financiero.contabilidad.ejb.DocumentosContabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class AsignacionTrabajos extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Tabla tabMostra = new Tabla();
    private Tabla tabConsulta = new Tabla();

    private Combo cmbASignado = new Combo();
    private Panel panOpcion = new Panel();

        /*
     * Acceso a manipulación de datos, CRUD
     */
    @EJB
    private DocumentosContabilidad documento = (DocumentosContabilidad) utilitario.instanciarEJB(DocumentosContabilidad.class);
    
    public AsignacionTrabajos() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        /*
         * Permite tener acceso a información, de los datos de registro
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        cmbASignado.setId("cmbASignado");
        cmbASignado.setCombo("SELECT u.NICK_USUA,u.NOM_USUA,u.IDE_USUA\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and u.ACTIVO_USUA=1 and p.NOM_PERF like '%Contabilidad%'");

        bar_botones.agregarComponente(new Etiqueta("Colaborador : "));
//        bar_botones.agregarComponente(cmbASignado);

        Boton botAsignar = new Boton();
        botAsignar.setValue("Asignar");
        botAsignar.setExcluirLectura(true);
        botAsignar.setIcon("ui-icon-person");
        botAsignar.setMetodo("asignaDocumento");

        Boton botMostrar = new Boton();
        botMostrar.setValue("Buscar");
        botMostrar.setExcluirLectura(true);
        botMostrar.setIcon("ui-icon-search");
        botMostrar.setMetodo("buscarDocumento");

        Boton botFinalizar = new Boton();
        botFinalizar.setValue("Procesar");
        botFinalizar.setExcluirLectura(true);
        botFinalizar.setIcon("ui-icon-lightbulb");
        botFinalizar.setMetodo("finalizar");

        bar_botones.agregarBoton(botAsignar);
        bar_botones.agregarBoton(botMostrar);
        bar_botones.agregarBoton(botFinalizar);
        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        agregarComponente(panOpcion);

        dibujarPantalla();
        cargaRegistros();
    }

    public void dibujarPantalla() {
        limpiarPanel();

        /*
         * formulario con ordenes de pago
         */
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("tes_documentos", "id_documento", 1);
        tabTabla.getColumna("doc_loginasi").setCombo("SELECT u.NICK_USUA,u.NOM_USUA,u.IDE_USUA\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and u.ACTIVO_USUA=1 and p.NOM_PERF like '%Contabilidad%'");
        tabTabla.getColumna("doc_revision").setCheck();
        tabTabla.getColumna("doc_usuasignacion").setCheck();
        tabTabla.getColumna("doc_revisioncon").setCheck();
        tabTabla.getColumna("id_tipo").setVisible(false);
        tabTabla.getColumna("id_documento").setVisible(false);
        tabTabla.getColumna("tes_ide_orden_pago").setVisible(false);
        tabTabla.getColumna("doc_estado").setVisible(false);
        tabTabla.getColumna("doc_fecharev").setVisible(false);
        tabTabla.getColumna("doc_dependencia").setVisible(false);
        tabTabla.getColumna("doc_comprobante").setVisible(false);
        tabTabla.getColumna("doc_fechacon").setVisible(false);
        tabTabla.getColumna("doc_revisiondev").setVisible(false);
        tabTabla.getColumna("doc_ejecutado").setVisible(false);
        tabTabla.getColumna("doc_loginrev").setVisible(false);
        tabTabla.setRows(10);
        tabTabla.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabTabla);

        tabMostra.setId("tabMostra");
        tabMostra.setTabla("tes_documentos", "id_documento", 2);
        tabMostra.setRows(10);
        tabMostra.dibujar();
        PanelTabla ptm = new PanelTabla();
        ptm.setPanelTabla(tabMostra);

        Division division = new Division();
        division.setId("division");
        division.dividir2(pto, ptm, "45%", "H");

        Grupo gru = new Grupo();
        gru.getChildren().add(division);
        panOpcion.getChildren().add(gru);

    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    private void cargaRegistros() {
        verDocumentos();
        registrosAtendidos();
    }

    /*
     * Consulta de registro por asignar
     */
    public void verDocumentos() {
        if (!getDocumentosPedientes().isEmpty()) {
            tabMostra.setCondicion(getDocumentosPedientes());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getDocumentosPedientes() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_estado = 3";
        return str_filtros;
    }

    /*
     * Consulta de registro atendidos
     */
    public void registrosAtendidos() {
        if (!getRegistroAtendido().isEmpty()) {
            tabMostra.setCondicion(getRegistroAtendido());
            tabMostra.ejecutarSql();
            utilitario.addUpdate("tabMostra");
        }
    }

    private String getRegistroAtendido() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "doc_estado = 5";
        return str_filtros;
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        int conteo=0;
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            boolean w = Boolean.valueOf(tabTabla.getValor(i, "doc_revision"));
            boolean x = Boolean.valueOf(tabTabla.getValor(i, "doc_loginasi"));
            boolean z = Boolean.valueOf(tabTabla.getValor(i, "doc_usuasignacion"));
            boolean y = true;
            if (w && y) { //proceso
                if (tabTabla.getValor(i, "doc_observación") == null || tabTabla.getValor(i, "doc_observación").isEmpty()) {
                    documento.setTramitesIngreso(tabTabla.getValor(i, "TES_IDE_ORDEN_PAGO"), "'" + tabTabla.getValor(i, "DOC_FECHAREV") + "'", tabTabla.getValor(i, "DOC_FECHAREV"), Integer.parseInt(tabTabla.getValor(i, "id_tipo")), null, null, null, null,7,2,Integer.parseInt(tabTabla.getValor(i, "ID_DOCUMENTO")),null);
                }else{
                  conteo = conteo+1;  
                }
            } else if (x && y) { //devuelto
                 if (tabTabla.getValor(i, "doc_observación") == null || tabTabla.getValor(i, "doc_observación").isEmpty()) {
                     documento.setTramitesIngreso(tabTabla.getValor(i, "TES_IDE_ORDEN_PAGO"), "'" + tabTabla.getValor(i, "DOC_FECHAREV") + "'", tabTabla.getValor(i, "DOC_FECHAREV"), Integer.parseInt(tabTabla.getValor(i, "id_tipo")), null, null, null, null,6,2,Integer.parseInt(tabTabla.getValor(i, "ID_DOCUMENTO")),null);
                }else{
                  conteo = conteo+1;  
                }
            } else if (z && y) { //asignar
                documento.setTramitesIngreso(tabTabla.getValor(i, "TES_IDE_ORDEN_PAGO"), "'" + tabTabla.getValor(i, "DOC_FECHAREV") + "'", tabTabla.getValor(i, "DOC_FECHAREV"), Integer.parseInt(tabTabla.getValor(i, "id_tipo")), null, null, null, null,4,2,Integer.parseInt(tabTabla.getValor(i, "ID_DOCUMENTO")),null);
            }
        }
        
        tabTabla.actualizar();
        utilitario.agregarMensaje("Registro Guardado", null);
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}

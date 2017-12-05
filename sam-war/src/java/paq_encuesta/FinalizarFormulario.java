/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_encuesta.ejb.BeanFormularios;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class FinalizarFormulario extends Pantalla {

    private Tabla tabDenuncia = new Tabla();
    private Tabla tabRespuesta = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setDenuncia = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Dialogo diaRegistro = new Dialogo();
    private Dialogo diaComentario = new Dialogo();
    private Grid gridrg = new Grid();
    private Grid gridrc = new Grid();
    private Grid gridRg = new Grid();
    private Grid gridRc = new Grid();
    private Texto txtComentario = new Texto();
    private Etiqueta etiComentario = new Etiqueta("COMENTARIO:");
    private Panel panOpcion = new Panel();
    @EJB
    private BeanFormularios admin = (BeanFormularios) utilitario.instanciarEJB(BeanFormularios.class);

    public FinalizarFormulario() {
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF,u.IDE_DIRECCION\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton botVer = new Boton();
        botVer.setValue("VIZUALIZAR");
        botVer.setExcluirLectura(true);
        botVer.setIcon("ui-icon-check");
        botVer.setMetodo("verFormulario");
        bar_botones.agregarBoton(botVer);

        Boton botBuscar = new Boton();
        botBuscar.setValue("FINALIZAR");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("finalizarTramite");
        bar_botones.agregarBoton(botBuscar);

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT\n"
                + "f.ID_FORMULARIO,\n"
                + "f.FECHA,\n"
                + "f.CEDULA,\n"
                + "f.NOMBRE,\n"
                + "f.OBSERVACION,\n"
                + "a.FECHA_ACTIVIDAD,\n"
                + "a.DESCRIPCION,\n"
                + "a.USU_ADJUNTO\n"
                + "FROM dbo.RESUG_FORMULARIO f\n"
                + "INNER JOIN dbo.RESUG_ACTIVIDADES a ON f.ID_FORMULARIO = a.ID_FORMULARIO\n"
                + "where a.TIPO_ACTIVIDAD = (select ID_ESTADO from RESUG_ESTADO where estado = 'Respuesta')");
        autBusca.setSize(70);

        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("Consulta de Información"); //titulo
        diaRegistro.setWidth("70%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("60%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setMetodo("mostrarFormulario");
//        diaRegistro.getBot_cancelar().setMetodo("regresaForma");
        gridrg.setColumns(4);
        agregarComponente(diaRegistro);

        setDenuncia.setId("setDenuncia");
        setDenuncia.setSql("SELECT\n"
                + "f.ID_FORMULARIO,\n"
                + "f.ID_FORMULARIO as Número,\n"
                + "a.FECHA_ACTIVIDAD as Fecha_Resp,\n"
                + "f.FECHA1,\n"
                + "f.CEDULA,\n"
                + "f.NOMBRE,\n"
                + "f.OBSERVACION,\n"
                + "a.DESCRIPCION,\n"
                + "(SELECT DESCRIPCION FROM RESUG_AREA_RESPON WHERE ID_CODIGO = a.UNI_RESPONSABLE) as DIRECCION\n"
                + "FROM dbo.RESUG_FORMULARIO f\n"
                + "INNER JOIN dbo.RESUG_ACTIVIDADES a ON f.ID_FORMULARIO = a.ID_FORMULARIO\n"
                + "where a.TIPO_ACTIVIDAD = (select ID_ESTADO from RESUG_ESTADO where estado = 'Respuesta')");
        setDenuncia.getColumna("NOMBRE").setFiltro(true);
        setDenuncia.getColumna("DIRECCION").setFiltro(true);
        setDenuncia.getColumna("FECHA1").setFiltro(true);
        setDenuncia.getColumna("Número").setLongitud(4);
        setDenuncia.getColumna("OBSERVACION").setVisible(false);
        setDenuncia.getColumna("FECHA1").setVisible(false);
        setDenuncia.getColumna("CEDULA").setVisible(false);
        setDenuncia.getColumna("DIRECCION").setLongitud(40);
        setDenuncia.getColumna("ID_FORMULARIO").setVisible(false);
        setDenuncia.setLectura(true);
        setDenuncia.setRows(10);
        setDenuncia.dibujar();

        diaComentario.setId("diaComentario");
        diaComentario.setTitle("Comentario para finalizar"); //titulo
        diaComentario.setWidth("55%"); //siempre en porcentajes  ancho
        diaComentario.setHeight("30%");//siempre porcentaje   alto
        diaComentario.setResizable(false); //para que no se pueda cambiar el tamaño
        diaComentario.getBot_aceptar().setMetodo("finTramite");
        gridrc.setColumns(4);
        agregarComponente(diaComentario);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("FORMULARIOS LISTOS Y PROCESADOS");
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabDenuncia.setId("tabDenuncia");
        tabDenuncia.setTabla("RESUG_FORMULARIO", "ID_FORMULARIO", 1);
        if (autBusca.getValue() == null) {
            tabDenuncia.setCondicion("ID_FORMULARIO=-1");
        } else {
            tabDenuncia.setCondicion("ID_FORMULARIO=" + autBusca.getValor());
        }
        List list = new ArrayList();
        Object fil1[] = {
            "DNU", "DENUNCIA "
        };
        Object fil2[] = {
            "RSU", "RECLAMO "
        };
        list.add(fil1);;
        list.add(fil2);;
        tabDenuncia.getColumna("tipo").setCombo(list);
//        tabDenuncia.getColumna("ID_FORMULARIO").setVisible(false);
        tabDenuncia.getColumna("ver").setVisible(false);
        tabDenuncia.getColumna("login").setVisible(false);
        tabDenuncia.getColumna("estado").setVisible(false);
        tabDenuncia.getColumna("adjunto").setVisible(false);
        tabDenuncia.setTipoFormulario(true);
        tabDenuncia.agregarRelacion(tabRespuesta);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        tabRespuesta.setId("tabRespuesta");
        tabRespuesta.setTabla("resug_actividades", "id_actividad", 2);
        tabRespuesta.getColumna("usu_adjunto").setUpload("formulario");
        tabRespuesta.getColumna("usu_responsable").setValorDefecto(utilitario.getVariable("NICK"));
        tabRespuesta.getColumna("fecha_actividad").setValorDefecto(utilitario.getFechaActual());
        tabRespuesta.getColumna("uni_responsable").setCombo("SELECT ID_CODIGO,DESCRIPCION FROM dbo.RESUG_AREA_RESPON ");
        tabRespuesta.getColumna("uni_responsable").setAutoCompletar();
        tabRespuesta.getColumna("tipo_actividad").setVisible(false);
//        tabRespuesta.getColumna("id_actividad").setVisible(false);
        tabRespuesta.getColumna("usu_asignacion").setVisible(false);
        tabRespuesta.getColumna("usu_responsable").setVisible(false);
        tabRespuesta.setTipoFormulario(true);
        tabRespuesta.setCondicion("id_formulario =" + autBusca.getValor() + " and TIPO_ACTIVIDAD = (select ID_ESTADO from RESUG_ESTADO where estado = 'Respuesta')");
        tabRespuesta.dibujar();
        PanelTabla pnr = new PanelTabla();
        pnr.setPanelTabla(tabRespuesta);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, pnr, "63%", "h");

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void verFormulario() {
        diaRegistro.Limpiar();
        diaRegistro.setDialogo(gridrg);
        gridRg.getChildren().add(setDenuncia);
        diaRegistro.setDialogo(gridRg);
        setDenuncia.dibujar();
        diaRegistro.dibujar();
    }

    public void mostrarFormulario() {
        if (setDenuncia.getValorSeleccionado() != null) {
            autBusca.setValor(setDenuncia.getValorSeleccionado());
            dibujarPantalla();
            diaRegistro.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un Registro", "");
        }
    }

    public void finalizarTramite() {
        diaComentario.Limpiar();
        diaComentario.setDialogo(gridrc);
        Grid griMostrar = new Grid();
        griMostrar.getChildren().clear();
        griMostrar.setColumns(2);
        griMostrar.getChildren().add(etiComentario);
        txtComentario.setSize(90);
        griMostrar.getChildren().add(txtComentario);
        gridrc.getChildren().add(griMostrar);
        diaComentario.setDialogo(gridRc);
        diaComentario.dibujar();
    }

    public void finTramite() {
        TablaGenerica tabCodigo = admin.getCodigoPrimario();
        if (!tabCodigo.isEmpty()) {
            TablaGenerica tabResponsable = admin.getIdFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")));
            if (!tabResponsable.isEmpty()) {
                TablaGenerica tabEstado = admin.getEstadoDenun("Concluido");
                if (!tabEstado.isEmpty()) {
                    admin.setAccionFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")), tabEstado.getValor("id_estado"),Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                    admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, Integer.parseInt(tabEstado.getValor("id_estado")), Integer.parseInt(tabDenuncia.getValor("id_formulario")), Integer.parseInt(tabResponsable.getValor("UNI_RESPONSABLE")), null, txtComentario.getValue() + "", utilitario.getVariable("NICK"),utilitario.getIp(),Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                    admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                    utilitario.agregarMensaje("Respuesta enviada con exito", null);
                    diaComentario.cerrar();
                    mostrarFormulario1();
                } else {
                    utilitario.agregarMensaje("Error de asignacion", null);
                }
            } else {
                utilitario.agregarMensaje("Error de responsable", null);
            }
        } else {
            utilitario.agregarMensaje("Error de clave primaria", null);
        }
    }

    public void mostrarFormulario1() {
        autBusca.setValor("-1");
        dibujarPantalla();
        diaRegistro.cerrar();
        utilitario.addUpdate("autBusca,panOpcion");

    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getTabDenuncia() {
        return tabDenuncia;
    }

    public void setTabDenuncia(Tabla tabDenuncia) {
        this.tabDenuncia = tabDenuncia;
    }

    public Tabla getTabRespuesta() {
        return tabRespuesta;
    }

    public void setTabRespuesta(Tabla tabRespuesta) {
        this.tabRespuesta = tabRespuesta;
    }

    public Tabla getSetDenuncia() {
        return setDenuncia;
    }

    public void setSetDenuncia(Tabla setDenuncia) {
        this.setDenuncia = setDenuncia;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}

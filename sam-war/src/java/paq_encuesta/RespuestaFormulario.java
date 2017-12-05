/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

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
public class RespuestaFormulario extends Pantalla {

    private Tabla tabDenuncia = new Tabla();
    private Tabla tabRespuesta = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Tabla setDenuncia = new Tabla();
    private Panel panOpcion = new Panel();
    private Dialogo diaRegistro = new Dialogo();
    private Dialogo diaRespuesta = new Dialogo();
    private Dialogo diaComentario = new Dialogo();
    private Grid gridrg = new Grid();
    private Grid gridrs = new Grid();
    private Grid gridrc = new Grid();
    private Grid gridRg = new Grid();
    private Grid gridRs = new Grid();
    private Grid gridRc = new Grid();
    private Texto txtComentario = new Texto();
    private Etiqueta etiComentario = new Etiqueta("COMENTARIO:");
    @EJB
    private BeanFormularios admin = (BeanFormularios) utilitario.instanciarEJB(BeanFormularios.class);

    public RespuestaFormulario() {
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

        Boton botRegresa = new Boton();
        botRegresa.setValue("DEVOLVER");
        botRegresa.setExcluirLectura(true);
        botRegresa.setIcon("ui-icon-close");
        botRegresa.setMetodo("devTramite");
        bar_botones.agregarBoton(botRegresa);

        Boton botRespuesta = new Boton();
        botRespuesta.setValue("RESPONDER");
        botRespuesta.setExcluirLectura(true);
        botRespuesta.setIcon("ui-icon-close");
        botRespuesta.setMetodo("respuestaTramite");
        bar_botones.agregarBoton(botRespuesta);

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT DISTINCT \n"
                + "f.ID_FORMULARIO, \n"
                + "f.FECHA1, \n"
                + "(case when f.tipo = 'DNU' then 'DENUNCIA' else 'RECLAMO' end) as TIPO, \n"
                + "f.CEDULA, \n"
                + "f.CLAVE,  \n"
                + "f.NOMBRE\n"
                + "FROM dbo.RESUG_FORMULARIO f  \n"
                + "INNER JOIN dbo.RESUG_ACTIVIDADES a ON f.ID_FORMULARIO = a.ID_FORMULARIO\n"
                + "order by f.ID_FORMULARIO");
        autBusca.setSize(70);

        setDenuncia.setId("setDenuncia");
        setDenuncia.setSql("SELECT top 1 f.ID_FORMULARIO, \n"
                + "(case when f.tipo = 'DNU' then 'DENUNCIA' else 'RECLAMO' end) as TIPO, \n"
                + "f.FECHA1,  \n"
                + "f.CEDULA, \n"
                + "f.CLAVE,  \n"
                + "f.NOMBRE,  \n"
                + "f.DIRECCION,  \n"
                + "f.OBSERVACION,  \n"
                + "a.DESCRIPCION,  \n"
                + "f.adjunto \n"
                + "FROM dbo.RESUG_FORMULARIO f  \n"
                + "INNER JOIN dbo.RESUG_ACTIVIDADES a ON f.ID_FORMULARIO = a.ID_FORMULARIO \n"
                + "where a.uni_RESPONSABLE = " + utilitario.getVariable("IDE_DIRECCION")+ " and f.ESTADO= (select ID_ESTADO from RESUG_ESTADO where estado = 'Asignado') \n"
                + "order by FECHA");
        setDenuncia.getColumna("TIPO").setFiltro(true);
        setDenuncia.getColumna("NOMBRE").setFiltro(true);
        setDenuncia.getColumna("ID_FORMULARIO").setVisible(false);
        setDenuncia.getColumna("CLAVE").setVisible(false);
        setDenuncia.getColumna("CEDULA").setVisible(false);
        setDenuncia.getColumna("DIRECCION").setVisible(false);
        setDenuncia.getColumna("adjunto").setVisible(false);
        setDenuncia.setLectura(true);
        setDenuncia.setRows(10);
        setDenuncia.dibujar();

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("ATENDER DENUNCIAS Y RECLAMOS ASIGNADOS");
        agregarComponente(panOpcion);

        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("Vizualiza Datos"); //titulo
        diaRegistro.setWidth("70%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("60%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setMetodo("mostrarFormulario");
        diaRegistro.getBot_cancelar().setMetodo("cerrarRespuesta");
        gridrg.setColumns(4);
        agregarComponente(diaRegistro);


        diaRespuesta.setId("diaRespuesta");
        diaRespuesta.setTitle("Enviar Respuesta"); //titulo
        diaRespuesta.setWidth("60%"); //siempre en porcentajes  ancho
        diaRespuesta.setHeight("50%");//siempre porcentaje   alto
        diaRespuesta.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRespuesta.getBot_aceptar().setMetodo("enviarRespuesta");
        diaRespuesta.getBot_cancelar().setMetodo("cerrarRespuesta");
        gridrs.setColumns(4);
        agregarComponente(diaRespuesta);

        diaComentario.setId("diaComentario");
        diaComentario.setTitle("Devolver Formulario"); //titulo
        diaComentario.setWidth("55%"); //siempre en porcentajes  ancho
        diaComentario.setHeight("30%");//siempre porcentaje   alto
        diaComentario.setResizable(false); //para que no se pueda cambiar el tamaño
        diaComentario.getBot_aceptar().setMetodo("devolverTramite");
        diaComentario.getBot_cancelar().setMetodo("cerrarRespuesta");
        gridrc.setColumns(4);
        agregarComponente(diaComentario);

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
        tabDenuncia.getColumna("adjunto").setUpload("formulario");
        tabDenuncia.getColumna("adjunto").setImagen("", "");
        tabDenuncia.getColumna("adjunto").setLongitud(40);
        tabDenuncia.setTipoFormulario(true);
        tabDenuncia.agregarRelacion(tabRespuesta);
        tabDenuncia.setLectura(true);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        tabRespuesta.setId("tabRespuesta");
        tabRespuesta.setTabla("resug_actividades", "id_actividad", 2);
        tabRespuesta.getColumna("usu_adjunto").setUpload("formulario");
        tabRespuesta.getColumna("usu_responsable").setValorDefecto(utilitario.getVariable("NICK"));
        tabRespuesta.getColumna("fecha_actividad").setValorDefecto(utilitario.getFechaActual());
        tabRespuesta.getColumna("uni_responsable").setValorDefecto(utilitario.getVariable("IDE_DIRECCION"));
        tabRespuesta.getColumna("uni_responsable").setVisible(false);
        tabRespuesta.getColumna("tipo_actividad").setVisible(false);
        tabRespuesta.getColumna("id_actividad").setVisible(false);
        tabRespuesta.getColumna("usu_asignacion").setVisible(false);
        tabRespuesta.getColumna("usu_responsable").setVisible(false);
        tabRespuesta.getColumna("fecha_actividad").setVisible(false);
        tabRespuesta.setTipoFormulario(true);
        tabRespuesta.dibujar();

        PanelTabla pnr = new PanelTabla();
        pnr.setPanelTabla(tabRespuesta);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, pnr, "65%", "h");

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
//        cargarDevuelto();
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

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Tabla getSetDenuncia() {
        return setDenuncia;
    }

    public void setSetDenuncia(Tabla setDenuncia) {
        this.setDenuncia = setDenuncia;
    }
}

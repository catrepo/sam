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
public class ResponderTramite extends Pantalla {

    private Tabla tabDenuncia = new Tabla();
    private Tabla tabRespuesta = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Tabla setDenuncia = new Tabla();
    private Tabla setHistorico = new Tabla();
    private Panel panOpcion = new Panel();
    private Dialogo diaRegistro = new Dialogo();
    private Dialogo diaRespuesta = new Dialogo();
    private Dialogo diaComentario = new Dialogo();
    private Dialogo diaHistorico = new Dialogo();
    private Grid gridrg = new Grid();
    private Grid gridrs = new Grid();
    private Grid gridrc = new Grid();
    private Grid gridhi = new Grid();
    private Grid gridRg = new Grid();
    private Grid gridRs = new Grid();
    private Grid gridRc = new Grid();
    private Grid gridHi = new Grid();
    private Texto txtComentario = new Texto();
    private Etiqueta etiComentario = new Etiqueta("COMENTARIO:");
    @EJB
    private BeanFormularios admin = (BeanFormularios) utilitario.instanciarEJB(BeanFormularios.class);

    public ResponderTramite() {
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
                + "f.ID_FORMULARIO as Numero, \n"
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
        setDenuncia.setSql("SELECT ID_FORMULARIO,  \n"
                + "ID_FORMULARIO as Numero, \n"
                + "(case when tipo = 'DNU' then 'DENUNCIA' else 'RECLAMO' end) as TIPO,  \n"
                + "FECHA1,   \n"
                + "CEDULA,  \n"
                + "CLAVE,   \n"
                + "NOMBRE,   \n"
                + "DIRECCION,   \n"
                + "OBSERVACION \n"
                + "FROM dbo.RESUG_FORMULARIO \n"
                + "where IDE_RESPONSABLE = " + tabConsulta.getValor("IDE_DIRECCION") + " and ESTADO = (SELECT ID_ESTADO FROM RESUG_ESTADO where ESTADO = 'Asignado')\n"
                + "order by FECHA1");
        setDenuncia.getColumna("TIPO").setFiltro(true);
        setDenuncia.getColumna("NOMBRE").setFiltro(true);
        setDenuncia.getColumna("ID_FORMULARIO").setVisible(false);
        setDenuncia.getColumna("CLAVE").setVisible(false);
        setDenuncia.getColumna("CEDULA").setVisible(false);
        setDenuncia.getColumna("DIRECCION").setVisible(false);
        setDenuncia.setLectura(true);
        setDenuncia.setRows(10);
        setDenuncia.dibujar();

        setHistorico.setId("setHistorico");
        setHistorico.setSql("SELECT f.ID_FORMULARIO,  \n"
                + "f.ID_FORMULARIO as Numero, \n"
                + "(case when f.tipo = 'DNU' then 'DENUNCIA' else 'RECLAMO' end) as TIPO,  \n"
                + "a.FECHA_ACTIVIDAD as Fecha_Respuesta,   \n"
                + "f.CEDULA,  \n"
                + "f.NOMBRE,     \n"
                + "a.DESCRIPCION as Respuesta\n"
                + "FROM dbo.RESUG_FORMULARIO f   \n"
                + "INNER JOIN dbo.RESUG_ACTIVIDADES a ON f.ID_FORMULARIO = a.ID_FORMULARIO  \n"
                + "where f.ESTADO= (select ID_ESTADO from RESUG_ESTADO where estado = 'Respuesta')\n"
                + "and a.FECHA_ACTIVIDAD BETWEEN '2016-05-29' and '2016-05-31'\n"
                + "and a.TIPO_ACTIVIDAD = (select ID_ESTADO from RESUG_ESTADO where estado = 'Respuesta')\n"
                + "and a.UNI_RESPONSABLE =" + tabConsulta.getValor("IDE_DIRECCION") + " \n"
                + "order by fecha");
        setHistorico.getColumna("TIPO").setFiltro(true);
        setHistorico.getColumna("NOMBRE").setFiltro(true);
        setHistorico.getColumna("ID_FORMULARIO").setVisible(false);
        setHistorico.getColumna("CEDULA").setVisible(false);
        setHistorico.setLectura(true);
        setHistorico.setRows(10);
        setHistorico.dibujar();

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

        diaHistorico.setId("diaHistorico");
        diaHistorico.setTitle("Devolver Formulario"); //titulo
        diaHistorico.setWidth("55%"); //siempre en porcentajes  ancho
        diaHistorico.setHeight("30%");//siempre porcentaje   alto
        diaHistorico.setResizable(false); //para que no se pueda cambiar el tamaño
        diaHistorico.getBot_aceptar().setMetodo("verHistorico");
        diaHistorico.getBot_cancelar().setMetodo("cerrarRespuesta");
        gridhi.setColumns(4);
        agregarComponente(diaHistorico);

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
        tabDenuncia.getColumna("login_actu").setVisible(false);
        tabDenuncia.getColumna("ide_responsable").setVisible(false);
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
        tabRespuesta.getColumna("uni_responsable").setValorDefecto(tabConsulta.getValor("IDE_DIRECCION"));
        tabRespuesta.getColumna("uni_responsable").setVisible(false);
        tabRespuesta.getColumna("tipo_actividad").setVisible(false);
        tabRespuesta.getColumna("id_actividad").setVisible(false);
        tabRespuesta.getColumna("usu_asignacion").setVisible(false);
        tabRespuesta.getColumna("usu_responsable").setVisible(false);
        tabRespuesta.getColumna("fecha_actividad").setVisible(false);
        tabRespuesta.getColumna("usu_ip").setVisible(false);
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
        cargarDevuelto();
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

    public void mostrarFormulario1() {
        autBusca.setValor("-1");
        dibujarPantalla();
        diaRegistro.cerrar();
        utilitario.addUpdate("autBusca,panOpcion");

    }

    public void devTramite() {
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

    public void devolverTramite() {
        TablaGenerica tabCodigo = admin.getCodigoPrimario();
        if (!tabCodigo.isEmpty()) {
            TablaGenerica tabResponsable = admin.getIdFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")));
            if (!tabResponsable.isEmpty()) {
                TablaGenerica tabEstado = admin.getEstadoDenun("Devuelto");
                if (!tabEstado.isEmpty()) {
                    admin.setAccionFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")), tabEstado.getValor("id_estado"), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                    admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, Integer.parseInt(tabEstado.getValor("id_estado")), Integer.parseInt(tabDenuncia.getValor("id_formulario")), Integer.parseInt("0"), null, txtComentario.getValue() + "", utilitario.getVariable("NICK"), utilitario.getIp(), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                    admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                    cerrarRespuesta();
                    utilitario.agregarMensaje("Respuesta enviada con exito", null);
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

    public void respuestaTramite() {
        if (tabRespuesta.getValor("descripcion") != null) {
            TablaGenerica tabEstado = admin.getEstadoDenun("Respuesta");
            if (!tabEstado.isEmpty()) {
                tabRespuesta.setValor("tipo_actividad", tabEstado.getValor("id_estado"));
                if (tabRespuesta.guardar()) {
                    guardarPantalla();
                }
                actuFormulario();
            }
        } else {
            utilitario.agregarMensajeInfo("Debe escribir al menos una descripcion", null);
        }
    }

    public void actuFormulario() {
        TablaGenerica tabEstado = admin.getEstadoDenun("Respuesta");
        if (!tabEstado.isEmpty()) {
            admin.setAccionFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")), tabEstado.getValor("id_estado"), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
            mostrarFormulario1();
        } else {
            utilitario.agregarMensaje("Error de asignacion", null);
        }
    }

    public void cerrarRespuesta() {
        diaRegistro.cerrar();
        diaRespuesta.cerrar();
        diaComentario.cerrar();
        diaHistorico.cerrar();
    }

    public void cargarDevuelto() {
        if (!getFiltraDevuelto().isEmpty()) {
            tabRespuesta.setCondicion(getFiltraDevuelto());
            tabRespuesta.ejecutarSql();
            utilitario.addUpdate("tabRespuesta");
        }
        tabRespuesta.limpiar();
        tabRespuesta.insertar();
    }

    private String getFiltraDevuelto() {
        String str_filtros = "";
        str_filtros = "tipo_actividad is null";
        return str_filtros;
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

    public Tabla getTabRespuesta() {
        return tabRespuesta;
    }

    public void setTabRespuesta(Tabla tabRespuesta) {
        this.tabRespuesta = tabRespuesta;
    }
}

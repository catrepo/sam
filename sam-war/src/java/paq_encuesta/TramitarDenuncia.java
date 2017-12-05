/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_encuesta.ejb.BeanFormularios;
import sistema.aplicacion.Pantalla;
import paq_varios.SendMail;

/**
 *
 * @author Administrador
 */
public class TramitarDenuncia extends Pantalla {

    private Tabla tabDenuncia = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setDenuncia = new SeleccionTabla();
    private SeleccionTabla setTramite = new SeleccionTabla();
    private Combo cmbDireccion = new Combo();
    private Combo cmbArea = new Combo();
    private Combo cmbEstado = new Combo();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    private Calendario fecha1 = new Calendario();
    private Calendario fecha2 = new Calendario();
    private Dialogo diaRegistro = new Dialogo();
    private Grid gridre = new Grid();
    private Grid gridRe = new Grid();
    private Texto txtId = new Texto();
    private Texto txtFecha = new Texto();
    private Texto txtCedula = new Texto();
    private Texto txtNombre = new Texto();
    private Texto txtDireccion = new Texto();
    private Texto txtDireccion2 = new Texto();
    private Texto txtTelefono = new Texto();
    private Texto txtCelular = new Texto();
    private Texto txtMail = new Texto();
    private Texto txtObservacion = new Texto();
    private Texto txtComentario = new Texto();
    private Etiqueta etiFecha = new Etiqueta("FECHA : ");
    private Etiqueta etiCedula = new Etiqueta("CÉDULA : ");
    private Etiqueta etiNombre = new Etiqueta("NOMBRE : ");
    private Etiqueta etiDireccion = new Etiqueta("DIRECCIÓN:");
    private Etiqueta etiDireccion2 = new Etiqueta("DIRECCIÓN2:");
    private Etiqueta etiTelefono = new Etiqueta("TELÉFONO : ");
    private Etiqueta etiCelular = new Etiqueta("CELULAR : ");
    private Etiqueta etiMail = new Etiqueta("E-MAIL : ");
    private Etiqueta etiObservacion = new Etiqueta("DENUNCIA:");
    private Etiqueta etiComentario = new Etiqueta("COMENTARIO:");
    private Boton botAprobar = new Boton();
    private Boton botNegar = new Boton();
    private SendMail correo = new SendMail();
    @EJB
    private BeanFormularios admin = (BeanFormularios) utilitario.instanciarEJB(BeanFormularios.class);

    public TramitarDenuncia() {
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton botBuscar = new Boton();
        botBuscar.setValue("Vizualizar");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("cargarLista");
        bar_botones.agregarBoton(botBuscar);

        Boton botDireccion = new Boton();
        botDireccion.setValue("Busc. Direccion");
        botDireccion.setExcluirLectura(true);
        botDireccion.setIcon("ui-icon-document-b");
        botDireccion.setMetodo("buscaTramite");
        bar_botones.agregarBoton(botDireccion);

        Boton botConcepto = new Boton();
        botConcepto.setValue("Busca Fecha");
        botConcepto.setExcluirLectura(true);
        botConcepto.setIcon("ui-icon-document-b");
        botConcepto.setMetodo("buscaFecha");
        bar_botones.agregarBoton(botConcepto);

        Grid griPanel = new Grid();
        griPanel.setColumns(2);
        Grid griBusca = new Grid();
        griBusca.setColumns(4);
        griBusca.getChildren().add(new Etiqueta("Dirección : "));
        cmbArea.setId("cmbArea");
        cmbArea.setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON where ide_codigo is null order by descripcion");
        griBusca.getChildren().add(cmbArea);
        griBusca.getChildren().add(new Etiqueta("Estado : "));
        cmbEstado.setId("cmbEstado");
        cmbEstado.setCombo("SELECT ID_ESTADO,ESTADO FROM RESUG_ESTADO where ID_ESTADO <> 11 and ID_ESTADO <>6 and ID_ESTADO <>10");
        griBusca.getChildren().add(cmbEstado);

        Boton botBusqueda = new Boton();
        botBusqueda.setValue("Buscar");
        botBusqueda.setIcon("ui-icon-search");
        botBusqueda.setMetodo("aceptoBusqueda");
        griPanel.getChildren().add(griBusca);
        griPanel.getChildren().add(botBusqueda);

        setDenuncia.setId("setDenuncia");
        setDenuncia.setSeleccionTabla("SELECT\n"
                + "f.ID_FORMULARIO,\n"
                + "f.FECHA,\n"
                + "f.CEDULA,\n"
                + "f.NOMBRE,\n"
                + "f.OBSERVACION,\n"
                + "f.TIPO,\n"
                + "(SELECT ESTADO FROM RESUG_ESTADO where ID_ESTADO = f.ESTADO ) as ESTADO,\n"
                + "(SELECT DESCRIPCION FROM RESUG_AREA_RESPON where ID_CODIGO = a.UNI_RESPONSABLE) as DIRECCION\n"
                + "FROM dbo.RESUG_FORMULARIO f\n"
                + "inner join dbo.RESUG_ACTIVIDADES a on f.ID_FORMULARIO = a.ID_FORMULARIO WHERE f.id_formulario=-1", "id_formulario");
        setDenuncia.getTab_seleccion().getColumna("NOMBRE").setLongitud(50);
        setDenuncia.getTab_seleccion().getColumna("DIRECCION").setLongitud(50);
        setDenuncia.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setDenuncia.getTab_seleccion().setRows(9);
        setDenuncia.setWidth("90%");
        setDenuncia.setRadio();
        setDenuncia.getGri_cuerpo().setHeader(griPanel);
        setDenuncia.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(setDenuncia);

        Grid griPan = new Grid();
        griPan.setColumns(2);
        Grid griBusc = new Grid();
        griBusc.setColumns(6);
        griBusc.getChildren().add(new Etiqueta("Dirección : "));
        cmbDireccion.setId("cmbDireccion");
        cmbDireccion.setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON where ide_codigo is null order by descripcion");
        griBusc.getChildren().add(cmbDireccion);
        griBusc.getChildren().add(new Etiqueta("Fecha Inicio : "));
        griBusc.getChildren().add(fecha1);
        griBusc.getChildren().add(new Etiqueta("Fecha Final : "));
        griBusc.getChildren().add(fecha2);

        Boton botBusq = new Boton();
        botBusq.setValue("Buscar");
        botBusq.setIcon("ui-icon-search");
        botBusq.setMetodo("aceptoFecha");
        griPan.getChildren().add(griBusc);
        griPan.getChildren().add(botBusq);

        setTramite.setId("setTramite");
        setTramite.setSeleccionTabla("SELECT\n"
                + "f.ID_FORMULARIO,\n"
                + "f.FECHA,\n"
                + "f.CEDULA,\n"
                + "f.NOMBRE,\n"
                + "f.OBSERVACION,\n"
                + "f.TIPO,\n"
                + "(SELECT ESTADO FROM RESUG_ESTADO where ID_ESTADO = f.ESTADO ) as ESTADO,\n"
                + "(SELECT DESCRIPCION FROM RESUG_AREA_RESPON where ID_CODIGO = a.UNI_RESPONSABLE) as DIRECCION\n"
                + "FROM dbo.RESUG_FORMULARIO f\n"
                + "inner join dbo.RESUG_ACTIVIDADES a on f.ID_FORMULARIO = a.ID_FORMULARIO WHERE f.id_formulario=-1", "id_formulario");
        setTramite.getTab_seleccion().getColumna("NOMBRE").setLongitud(50);
        setTramite.getTab_seleccion().getColumna("DIRECCION").setLongitud(50);
        setTramite.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setTramite.getTab_seleccion().setRows(9);
        setTramite.setWidth("90%");
        setTramite.setRadio();
        setTramite.getGri_cuerpo().setHeader(griPan);
        setTramite.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
        agregarComponente(setTramite);

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT id_formulario,cedula,nombre FROM resug_formulario order by fecha");
        autBusca.setSize(80);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("LISTADO DE DENUNCIAS INGRESADAS");
        agregarComponente(panOpcion);

        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("Consulta de Información"); //titulo
        diaRegistro.setWidth("60%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("60%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setDisabled(true);
        diaRegistro.getBot_cancelar().setMetodo("regresaForma");
        gridre.setColumns(4);
        agregarComponente(diaRegistro);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabDenuncia.setId("tabDenuncia");
        tabDenuncia.setTabla("resug_formulario", "id_formulario", 1);
        if (autBusca.getValue() == null) {
            tabDenuncia.setCondicion("id_formulario=-1");
        } else {
            tabDenuncia.setCondicion("id_formulario=" + autBusca.getValor());
        }
        List list = new ArrayList();
        Object fil1[] = {
            "WEB", "WEB"
        };
        Object fil2[] = {
            "dennis.estrella", "USUARIO"
        };
        list.add(fil1);;
        list.add(fil2);;
        tabDenuncia.getColumna("login").setCombo(list);
        tabDenuncia.getColumna("login").setAutoCompletar();
        tabDenuncia.getColumna("ver").setCheck();
        tabDenuncia.getColumna("ver").setMetodoChange("mostrarRegistro");
        tabDenuncia.getColumna("adjunto").setUpload("formulario");
//        tabDenuncia.getColumna("id_formulario").setVisible(false);
        tabDenuncia.getColumna("estado").setVisible(false);
        tabDenuncia.getColumna("direccion1").setVisible(false);
        tabDenuncia.getColumna("celular").setVisible(false);
        tabDenuncia.getColumna("telefono").setVisible(false);
        tabDenuncia.getColumna("mail").setVisible(false);
        tabDenuncia.getColumna("tipo").setVisible(false);
        tabDenuncia.getColumna("cedula").setLectura(true);
        tabDenuncia.getColumna("adjunto").setLectura(true);
        tabDenuncia.getColumna("login").setLectura(true);
        tabDenuncia.getColumna("nombre").setLectura(true);
        tabDenuncia.getColumna("direccion").setLectura(true);
        tabDenuncia.getColumna("observacion").setLectura(true);
        tabDenuncia.getColumna("nombre").setLongitud(40);
        tabDenuncia.getColumna("direccion").setLongitud(50);
        tabDenuncia.getColumna("adjunto").setVisible(false);
        tabDenuncia.getColumna("login").setLongitud(15);
        tabDenuncia.getColumna("clave").setVisible(false);
        tabDenuncia.getColumna("cedula").setVisible(false);
        tabDenuncia.getColumna("direccion").setVisible(false);
        tabDenuncia.getColumna("fecha1").setVisible(false);
        tabDenuncia.agregarRelacion(tabMovimiento);
        tabDenuncia.setRows(10);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("resug_actividades", "id_actividad", 2);
        tabMovimiento.setHeader("Comentario en Caso de Devolución");
        tabMovimiento.getColumna("uni_responsable").setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON where ide_codigo is null order by descripcion");
//        tabMovimiento.getColumna("id_actividad").setVisible(false);
        tabMovimiento.getColumna("tipo_actividad").setVisible(false);
        tabMovimiento.getColumna("usu_asignacion").setVisible(false);
        tabMovimiento.getColumna("usu_responsable").setVisible(false);
        tabMovimiento.getColumna("descripcion").setLongitud(100);
        tabMovimiento.getColumna("uni_responsable").setLongitud(70);
        tabMovimiento.getColumna("fecha_actividad").setLongitud(30);
        tabMovimiento.setLectura(true);
        tabMovimiento.dibujar();
        PanelTabla pnm = new PanelTabla();
        pnm.setPanelTabla(tabMovimiento);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, pnm, "70%", "h");

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    public void mostrarRegistro() {
        if (tabDenuncia.getValor("ver") != null && tabDenuncia.getValor("ver").toString().isEmpty() == false) {
            TablaGenerica tabDato = admin.getFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")));
            if (!tabDato.isEmpty()) {
                diaRegistro.Limpiar();
                diaRegistro.setDialogo(gridre);
                Grid griMostrar = new Grid();
                griMostrar.getChildren().clear();
                griMostrar.setColumns(2);
                txtFecha.setSize(10);
                griMostrar.getChildren().add(etiFecha);
                griMostrar.getChildren().add(txtFecha);
                txtCedula.setSize(10);
                griMostrar.getChildren().add(etiCedula);
                griMostrar.getChildren().add(txtCedula);
                txtNombre.setSize(38);
                griMostrar.getChildren().add(etiNombre);
                griMostrar.getChildren().add(txtNombre);
                txtDireccion.setSize(60);
                griMostrar.getChildren().add(etiDireccion);
                griMostrar.getChildren().add(txtDireccion);
                txtDireccion2.setSize(60);
                griMostrar.getChildren().add(etiDireccion2);
                griMostrar.getChildren().add(txtDireccion2);
                txtTelefono.setSize(10);
                griMostrar.getChildren().add(etiTelefono);
                griMostrar.getChildren().add(txtTelefono);
                txtCelular.setSize(10);
                griMostrar.getChildren().add(etiCelular);
                griMostrar.getChildren().add(txtCelular);
                txtMail.setSize(30);
                griMostrar.getChildren().add(etiMail);
                griMostrar.getChildren().add(txtMail);
                griMostrar.getChildren().add(etiObservacion);
                txtObservacion.setSize(100);
                griMostrar.getChildren().add(txtObservacion);
                griMostrar.getChildren().add(etiComentario);
                txtComentario.setSize(100);
                griMostrar.getChildren().add(txtComentario);
                gridre.getChildren().add(griMostrar);
                Grid griBotones = new Grid();
                griBotones.getChildren().clear();
                griBotones.setColumns(2);
                botAprobar.setValue("Asignar");
                botAprobar.setIcon("ui-icon-check");
                botAprobar.setMetodo("aprobarDenuncia");
                botNegar.setValue("Negar");
                botNegar.setIcon("ui-icon-close");
                botNegar.setMetodo("negarDenuncia");
                griBotones.getChildren().add(botAprobar);
                griBotones.getChildren().add(botNegar);
                gridRe.getChildren().add(griBotones);
                diaRegistro.setDialogo(gridRe);
                diaRegistro.dibujar();
                txtId.setValue(tabDato.getValor("id_formulario") + "");
                txtFecha.setValue(tabDato.getValor("fecha1") + "");
                txtCedula.setValue(tabDato.getValor("cedula") + "");
                txtNombre.setValue(tabDato.getValor("nombre") + "");
                txtDireccion.setValue(tabDato.getValor("direccion") + "");
                txtDireccion2.setValue(tabDato.getValor("direccion1") + "");
                txtTelefono.setValue(tabDato.getValor("telefono") + "");
                txtCelular.setValue(tabDato.getValor("celular") + "");
                txtMail.setValue(tabDato.getValor("mail") + "");
                txtObservacion.setValue(tabDato.getValor("observacion") + "");
                utilitario.addUpdate("txtFecha");
                utilitario.addUpdate("txtCedula");
                utilitario.addUpdate("txtNombre");
                utilitario.addUpdate("txtDireccion");
                utilitario.addUpdate("txtDireccion2");
                utilitario.addUpdate("txtTelefono");
                utilitario.addUpdate("txtCelular");
                utilitario.addUpdate("txtMail");
                utilitario.addUpdate("txtObservacion");
            } else {
                utilitario.agregarMensajeInfo("No se encuentra registro", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una registro", "");
        }
    }

    public void regresaForma() {
        tabDenuncia.setValor("ver", null);
        utilitario.addUpdate("tabDenuncia");
        diaRegistro.cerrar();
    }

    public void aprobarDenuncia() {
        TablaGenerica tabCodigo = admin.getCodigoPrimario();
        if (!tabCodigo.isEmpty()) {
            TablaGenerica tabResponsable = admin.getResponsableDenun();
            if (!tabResponsable.isEmpty()) {
                TablaGenerica tabArea = admin.getTipoArea(Integer.parseInt(tabResponsable.getValor("IDE_CODIGO")));
                if (!tabArea.isEmpty()) {
                    TablaGenerica tabEstado = admin.getEstadoDenun("Asignado");
                    if (!tabEstado.isEmpty()) {
                        admin.setAccionFormulario(Integer.parseInt(txtId.getValue() + ""), tabEstado.getValor("id_estado"), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                        admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, Integer.parseInt(tabEstado.getValor("id_estado")), Integer.parseInt(txtId.getValue() + ""), Integer.parseInt(tabResponsable.getValor("IDE_CODIGO")), utilitario.getVariable("NICK"), txtComentario.getValue() + "", null, utilitario.getIp(), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                        admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                        for (int i = 0; i < tabResponsable.getTotalFilas(); i++) {
//                                    correo.mandarCorreo(txtId.getValue() + "", txtNombre.getValue() + "", txtFecha.getValue() + "", "RECLAMO", tabRespon.getValor(i, "correo"), "0", null, null, null, null, null, null);
                            correo.envio("resug_formulario", txtId.getValue() + "", txtNombre.getValue() + "", txtFecha.getValue() + "", "DENUNCIA", tabResponsable.getValor(i, "correo"), "0", null, null, null, null, null, null);
                        }
                        admin.setDirAsignacion(Integer.parseInt(txtId.getValue() + ""), Integer.parseInt(tabResponsable.getValor("IDE_CODIGO")));
                        System.out.println("Formulario asignado:" + txtId.getValue() + " a: " + tabResponsable.getValor("IDE_CODIGO") + " con fecha " + utilitario.getFechaActual());
                        utilitario.agregarMensaje("Denuncia Aprobada", "Enviada : " + tabArea.getValor("DESCRIPCION"));
                        diaRegistro.cerrar();
                        cargarLista();
                    } else {
                        utilitario.agregarMensaje("Error de asignacion", null);
                    }
                }
            } else {
                utilitario.agregarMensaje("Error de responsable", null);
            }
        } else {
            utilitario.agregarMensaje("Error de clave primaria", null);
        }
    }

    public void negarDenuncia() {
        TablaGenerica tabCodigo = admin.getCodigoPrimario();
        if (!tabCodigo.isEmpty()) {
            TablaGenerica tabEstado = admin.getEstadoDenun("Negado");
            if (!tabEstado.isEmpty()) {
                admin.setAccionFormulario(Integer.parseInt(txtId.getValue() + ""), tabEstado.getValor("id_estado"), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")), Integer.parseInt(tabEstado.getValor("id_estado")), Integer.parseInt(txtId.getValue() + ""), null, utilitario.getVariable("NICK"), txtComentario.getValue() + "", null, utilitario.getIp(), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                cargarLista();
                admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                utilitario.agregarMensaje("Denuncia Negada", null);
                diaRegistro.cerrar();
            }
        }
    }

    public void buscaTramite() {
        setDenuncia.dibujar();
        setDenuncia.getTab_seleccion().limpiar();
    }

    public void aceptoBusqueda() {
        if (cmbArea.getValue() != null && cmbArea.getValue().toString().isEmpty() == false) {
            setDenuncia.getTab_seleccion().setSql("SELECT\n"
                    + "f.ID_FORMULARIO,\n"
                    + "f.FECHA,\n"
                    + "f.CEDULA,\n"
                    + "f.NOMBRE,\n"
                    + "f.OBSERVACION,\n"
                    + "f.TIPO,\n"
                    + "(SELECT ESTADO FROM RESUG_ESTADO where ID_ESTADO = f.ESTADO ) as estado,\n"
                    + "(SELECT DESCRIPCION FROM RESUG_AREA_RESPON where ID_CODIGO = a.UNI_RESPONSABLE) as DIRECCION\n"
                    + "FROM dbo.RESUG_FORMULARIO f\n"
                    + "inner join dbo.RESUG_ACTIVIDADES a on f.ID_FORMULARIO = a.ID_FORMULARIO\n"
                    + "where tipo= 'DNU' and f.ESTADO = " + cmbEstado.getValue() + " and a.UNI_RESPONSABLE =" + cmbArea.getValue());
            setDenuncia.getTab_seleccion().ejecutarSql();
            setDenuncia.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una elemento", "");
        }
    }

    public void buscaFecha() {
        setTramite.dibujar();
        setTramite.getTab_seleccion().limpiar();
    }

    public void aceptoFecha() {
        if (cmbDireccion.getValue() != null && cmbDireccion.getValue().toString().isEmpty() == false) {
            setTramite.getTab_seleccion().setSql("SELECT\n"
                    + "f.ID_FORMULARIO,\n"
                    + "f.FECHA,\n"
                    + "f.CEDULA,\n"
                    + "f.NOMBRE,\n"
                    + "f.OBSERVACION,\n"
                    + "f.TIPO,\n"
                    + "(SELECT ESTADO FROM RESUG_ESTADO where ID_ESTADO = f.ESTADO ) as estado,\n"
                    + "(SELECT DESCRIPCION FROM RESUG_AREA_RESPON where ID_CODIGO = a.UNI_RESPONSABLE) as DIRECCION\n"
                    + "FROM dbo.RESUG_FORMULARIO f\n"
                    + "inner join dbo.RESUG_ACTIVIDADES a on f.ID_FORMULARIO = a.ID_FORMULARIO\n"
                    + "where tipo= 'DNU' and a.UNI_RESPONSABLE = " + cmbDireccion.getValue() + " and f.FECHA BETWEEN '" + fecha1.getFecha() + "' and '" + fecha2.getFecha() + "'");
            setTramite.getTab_seleccion().ejecutarSql();
            setTramite.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una elemento", "");
        }
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

    public void cargarLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabDenuncia.setCondicion(getFiltrosAcceso());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
        cargarDevuelto();
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "tipo = 'DNU' and  estado in (select ID_ESTADO from RESUG_ESTADO where estado BETWEEN 'Devuelto' and 'Ingresado')";
        return str_filtros;
    }

    public void cargarDevuelto() {
        if (!getFiltraDevuelto().isEmpty()) {
            tabMovimiento.setCondicion(getFiltraDevuelto());
            tabMovimiento.ejecutarSql();
            utilitario.addUpdate("tabMovimiento");
        }
    }

    private String getFiltraDevuelto() {
        String str_filtros = "";
        str_filtros = "tipo_actividad = (select ID_ESTADO from RESUG_ESTADO where estado = 'Devuelto')";
        return str_filtros;
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTabDenuncia() {
        return tabDenuncia;
    }

    public void setTabDenuncia(Tabla tabDenuncia) {
        this.tabDenuncia = tabDenuncia;
    }

    public Tabla getTabMovimiento() {
        return tabMovimiento;
    }

    public void setTabMovimiento(Tabla tabMovimiento) {
        this.tabMovimiento = tabMovimiento;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public SeleccionTabla getSetDenuncia() {
        return setDenuncia;
    }

    public void setSetDenuncia(SeleccionTabla setDenuncia) {
        this.setDenuncia = setDenuncia;
    }

    public SeleccionTabla getSetTramite() {
        return setTramite;
    }

    public void setSetTramite(SeleccionTabla setTramite) {
        this.setTramite = setTramite;
    }
}

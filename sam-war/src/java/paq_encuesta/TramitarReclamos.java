/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
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
import paq_varios.SendMail;

/**
 *
 * @author KEJA
 */
public class TramitarReclamos extends Pantalla {

    private Tabla tabDenuncia = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Combo cmbAsignacion = new Combo();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
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
    private AreaTexto txaComentario = new AreaTexto();
    private Texto txtComentario = new Texto();
    private Etiqueta etiFecha = new Etiqueta("FECHA : ");
    private Etiqueta etiCedula = new Etiqueta("CÉDULA : ");
    private Etiqueta etiNombre = new Etiqueta("NOMBRE : ");
    private Etiqueta etiDireccion = new Etiqueta("DIRECCIÓN:");
    private Etiqueta etiTelefono = new Etiqueta("TELÉFONO : ");
    private Etiqueta etiCelular = new Etiqueta("CELULAR : ");
    private Etiqueta etiMail = new Etiqueta("E-MAIL : ");
    private Etiqueta etiObservacion = new Etiqueta("DENUNCIA:");
    private Etiqueta etiComentario = new Etiqueta("COMENTARIO:");
    private Etiqueta etiAsignacion = new Etiqueta("DIRECCIÓN:");
    private Etiqueta etiDireccion2 = new Etiqueta("DIRECCIÓN2:");
    private Boton botAprobar = new Boton();
    private Boton botNegar = new Boton();
    private Boton botProcesar = new Boton();
    private SendMail correo = new SendMail();
    @EJB
    private BeanFormularios admin = (BeanFormularios) utilitario.instanciarEJB(BeanFormularios.class);

    public TramitarReclamos() {
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

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT id_formulario,cedula,nombre FROM resug_formulario order by fecha");
        autBusca.setSize(80);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("LISTA DE RECLAMOS INGRESADOS");
        agregarComponente(panOpcion);

        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("Consulta de Información"); //titulo
        diaRegistro.setWidth("60%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("75%");//siempre porcentaje   alto
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
        Object fil3[] = {
            "d-estrella", "USUARIO"
        };
        list.add(fil1);;
        list.add(fil2);;
        list.add(fil3);;
        tabDenuncia.getColumna("login").setCombo(list);
        tabDenuncia.getColumna("login").setAutoCompletar();
        tabDenuncia.getColumna("ver").setCheck();
        tabDenuncia.getColumna("adjunto").setUpload("formulario");
        tabDenuncia.getColumna("ver").setMetodoChange("mostrarRegistro");
        tabDenuncia.getColumna("estado").setVisible(false);
        tabDenuncia.getColumna("direccion1").setVisible(false);
        tabDenuncia.getColumna("celular").setVisible(false);
        tabDenuncia.getColumna("telefono").setVisible(false);
        tabDenuncia.getColumna("mail").setVisible(false);
        tabDenuncia.getColumna("tipo").setVisible(false);
        tabDenuncia.getColumna("cedula").setLectura(true);
        tabDenuncia.getColumna("adjunto").setLectura(true);
        tabDenuncia.getColumna("nombre").setLectura(true);
        tabDenuncia.getColumna("login").setLectura(true);
        tabDenuncia.getColumna("direccion").setLectura(true);
        tabDenuncia.getColumna("observacion").setLectura(true);
        tabDenuncia.getColumna("nombre").setLongitud(50);
        tabDenuncia.getColumna("direccion").setLongitud(50);
        tabDenuncia.getColumna("adjunto").setVisible(false);
        tabDenuncia.getColumna("login").setLongitud(15);
        tabDenuncia.getColumna("clave").setVisible(false);
        tabDenuncia.getColumna("cedula").setVisible(false);
        tabDenuncia.getColumna("direccion").setVisible(false);
        tabDenuncia.getColumna("fecha1").setVisible(false);
        tabDenuncia.getColumna("login_actu").setVisible(false);
        tabDenuncia.getColumna("ide_responsable").setVisible(false);
        tabDenuncia.agregarRelacion(tabMovimiento);
        tabDenuncia.setRows(10);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("resug_actividades", "id_actividad", 2);
        tabMovimiento.setHeader("Comentario en Caso de Devolución");
        tabMovimiento.getColumna("uni_responsable").setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON where ide_codigo is null order by descripcion");
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
                txaComentario.setCols(70);
                griMostrar.getChildren().add(txaComentario);
                griMostrar.getChildren().add(etiAsignacion);
                cmbAsignacion.setId("cmbAsignacion");
                cmbAsignacion.setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON where ide_codigo is null order by descripcion");
                griMostrar.getChildren().add(cmbAsignacion);
                griMostrar.getChildren().add(etiComentario);
                txtComentario.setSize(100);
                griMostrar.getChildren().add(txtComentario);
                gridre.getChildren().add(griMostrar);
                Grid griBotones = new Grid();
                griBotones.getChildren().clear();
                griBotones.setColumns(3);
                botAprobar.setValue("Aprobar");
                botAprobar.setIcon("ui-icon-check");
                botAprobar.setMetodo("aprobarDenuncia");
                botNegar.setValue("Negar");
                botNegar.setIcon("ui-icon-close");
                botNegar.setMetodo("negarDenuncia");
                botProcesar.setValue("Procesar");
                botProcesar.setIcon("ui-icon-check");
                botProcesar.setMetodo("procesarDenuncia");
                griBotones.getChildren().add(botAprobar);
                griBotones.getChildren().add(botNegar);
                griBotones.getChildren().add(botProcesar);
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
                txaComentario.setValue(tabDato.getValor("observacion") + "");
                utilitario.addUpdate("txtFecha");
                utilitario.addUpdate("txtCedula");
                utilitario.addUpdate("txtNombre");
                utilitario.addUpdate("txtDireccion");
                utilitario.addUpdate("txtDireccion2");
                utilitario.addUpdate("txtTelefono");
                utilitario.addUpdate("txtCelular");
                utilitario.addUpdate("txtMail");
                utilitario.addUpdate("txaComentario");
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
        TablaGenerica tabArea = admin.getTipoArea(Integer.parseInt(cmbAsignacion.getValue() + ""));
        if (!tabArea.isEmpty()) {
            if (tabArea.getValor("TIPO_EMPRESA").equals("E")) {
                registroPropiedad(tabArea.getValor("DESCRIPCION"));
            } else {
                TablaGenerica tabCodigo = admin.getCodigoPrimario();
                if (!tabCodigo.isEmpty()) {
                    if (cmbAsignacion.getValue() != null && cmbAsignacion.getValue().toString().isEmpty() == false) {
                        TablaGenerica tabRespon = admin.getResponsableReclamo(Integer.parseInt(cmbAsignacion.getValue() + ""));
                        if (!tabRespon.isEmpty()) {
                            TablaGenerica tabEstado = admin.getEstadoDenun("Asignado");
                            if (!tabEstado.isEmpty()) {
                                admin.setAccionFormulario(Integer.parseInt(txtId.getValue() + ""), tabEstado.getValor("id_estado"), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                                admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, Integer.parseInt(tabEstado.getValor("id_estado")), Integer.parseInt(txtId.getValue() + ""), Integer.parseInt(cmbAsignacion.getValue() + ""), utilitario.getVariable("NICK"), txtComentario.getValue() + "", null, utilitario.getIp(), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                                admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                                for (int i = 0; i < tabRespon.getTotalFilas(); i++) {
//                                    correo.mandarCorreo(txtId.getValue() + "", txtNombre.getValue() + "", txtFecha.getValue() + "", "RECLAMO", tabRespon.getValor(i, "correo"), "0", null, null, null, null, null, null);
                                    correo.envio("resug_formulario", txtId.getValue() + "", txtNombre.getValue() + "", txtFecha.getValue() + "", "RECLAMO", tabRespon.getValor(i, "correo"), "0", null, null, null, null, null, null);
                                }
                                admin.setDirAsignacion(Integer.parseInt(txtId.getValue() + ""), Integer.parseInt(cmbAsignacion.getValue() + ""));
                                System.out.println("Formulario asignado:" + txtId.getValue() + " a: " + cmbAsignacion.getValue() + " con fecha " + utilitario.getFechaActual());
                                utilitario.agregarMensaje("Reclamo Aprobada", "Enviada : " + tabArea.getValor("DESCRIPCION"));
                                diaRegistro.cerrar();
                                cargarLista();
                            } else {
                                utilitario.agregarMensaje("Error de asignacion", null);
                            }
                        } else {
                            utilitario.agregarMensaje("Correo no encontrado", "Para asignacion");
                        }
                    } else {
                        utilitario.agregarMensaje("Error de asignación", null);
                    }
                } else {
                    utilitario.agregarMensaje("Error de clave primaria", null);
                }
            }
        }
    }

    public void registroPropiedad(String area) {
        TablaGenerica tabCodigo = admin.getCodigoPrimario();
        if (!tabCodigo.isEmpty()) {
            if (cmbAsignacion.getValue() != null && cmbAsignacion.getValue().toString().isEmpty() == false) {
                TablaGenerica tabRespon = admin.getResponsableReclamo(Integer.parseInt(cmbAsignacion.getValue() + ""));
                if (!tabRespon.isEmpty()) {
                    TablaGenerica tabEstado = admin.getEstadoDenun("Asignado");
                    if (!tabEstado.isEmpty()) {
                        admin.setAccionFormulario(Integer.parseInt(txtId.getValue() + ""), tabEstado.getValor("id_estado"), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                        admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, Integer.parseInt(tabEstado.getValor("id_estado")), Integer.parseInt(txtId.getValue() + ""), Integer.parseInt(cmbAsignacion.getValue() + ""), utilitario.getVariable("NICK"), txtComentario.getValue() + "", null, utilitario.getIp(), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                        cargarLista();
                        admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                        for (int i = 0; i < tabRespon.getTotalFilas(); i++) {
//                           correo.mandarCorreo(txtId.getValue() + "", txtNombre.getValue() + "", txtFecha.getValue() + "", "RECLAMO", tabRespon.getValor(i, "correo"), "1", txaComentario.getValue() + "", txtCedula.getValue() + "", txtTelefono.getValue() + "", txtCelular.getValue() + "", txtDireccion.getValue() + "", txtMail.getValue() + "");
                            correo.envio("resug_formulario", txtId.getValue() + "", txtNombre.getValue() + "", txtFecha.getValue() + "", "RECLAMO", tabRespon.getValor(i, "correo"), "1", txaComentario.getValue() + "", txtCedula.getValue() + "", txtTelefono.getValue() + "", txtCelular.getValue() + "", txtDireccion.getValue() + "", txtMail.getValue() + "");
                        }
                        admin.setDirAsignacion(Integer.parseInt(txtId.getValue() + ""), Integer.parseInt(cmbAsignacion.getValue() + ""));
                        utilitario.agregarMensaje("Denuncia Aprobada", "Enviada : " + area);
                        diaRegistro.cerrar();
                    } else {
                        utilitario.agregarMensaje("Error de asignacion", null);
                    }
                } else {
                    utilitario.agregarMensaje("Correo no encontrado", "Para asignacion");
                }
            } else {
                utilitario.agregarMensaje("Error de asignación", null);
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
                utilitario.agregarMensaje("Reclamo Negada", null);
                diaRegistro.cerrar();
            }
        }
    }

    public void procesarDenuncia() {
        TablaGenerica tabCodigo = admin.getCodigoPrimario();
        if (!tabCodigo.isEmpty()) {
            TablaGenerica tabEstado = admin.getEstadoDenun("Concluido");
            if (!tabEstado.isEmpty()) {
                admin.setAccionFormulario(Integer.parseInt(txtId.getValue() + ""), tabEstado.getValor("id_estado"), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")), Integer.parseInt(tabEstado.getValor("id_estado")), Integer.parseInt(txtId.getValue() + ""), null, utilitario.getVariable("NICK"), txtComentario.getValue() + "",utilitario.getVariable("NICK"), utilitario.getIp(), Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                cargarLista();
                admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                utilitario.agregarMensaje("Reclamo Concluido", null);
                diaRegistro.cerrar();
            }
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
        str_filtros = "tipo = 'RSU' and  estado in (select ID_ESTADO from RESUG_ESTADO where estado BETWEEN 'Devuelto' and 'Ingresado')";
        return str_filtros;
    }

    public void cargarDevuelto() {
        if (!getFiltrosAcceso().isEmpty()) {
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
}

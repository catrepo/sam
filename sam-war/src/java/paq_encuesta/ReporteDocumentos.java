/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BuscarTabla;
import framework.componentes.Calendario;
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

/**
 *
 * @author p-chumana
 */
public class ReporteDocumentos extends Pantalla {

    private Texto txtCedula = new Texto();
    private Combo cmbDireccion = new Combo();
    private Combo cmbOpcion = new Combo();
    private Combo cmbEstado = new Combo();
    private Calendario fecha1 = new Calendario();
    private Calendario fecha2 = new Calendario();
    private Tabla tabDenuncia = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    private AreaTexto txaComentario = new AreaTexto();
    private AreaTexto txaReclamo = new AreaTexto();
    private Texto txtMail = new Texto();
    private Texto txtId = new Texto();
    private Etiqueta etiComentario = new Etiqueta("COMENTARIO:");
    private Etiqueta etiReclamo = new Etiqueta("RECLAMO:");
    private Etiqueta etiMail = new Etiqueta("E-MAIL:");
    private Dialogo diaRegistro = new Dialogo();
    private Dialogo diaEditar = new Dialogo();
    private Grid gridre = new Grid();
    private Grid grided = new Grid();
    private Grid gridRe = new Grid();
    private Grid gridEd = new Grid();
    @EJB
    private BeanFormularios admin = (BeanFormularios) utilitario.instanciarEJB(BeanFormularios.class);

    public ReporteDocumentos() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        cmbDireccion.setId("cmbDireccion");
        cmbDireccion.setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON where ide_codigo is null order by descripcion");

        cmbEstado.setId("cmbEstado");
        cmbEstado.setCombo("SELECT ID_ESTADO,ESTADO FROM RESUG_ESTADO where ID_ESTADO in (7,8)");

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Número"
        };
        Object fila2[] = {
            "1", "Cédula"
        };
        Object fila3[] = {
            "2", "Dirección"
        };
        Object fila4[] = {
            "3", "Ran. Fechas"
        };
        list.add(fila1);;
        list.add(fila2);;
        list.add(fila3);;
        list.add(fila4);;
        cmbOpcion.setCombo(list);

        Grid griPri = new Grid();
        griPri.setColumns(3);

        Grid griPan = new Grid();
        griPan.setColumns(2);
        griPan.getChildren().add(new Etiqueta("Buscar por :"));
        griPan.getChildren().add(cmbOpcion);
        griPri.getChildren().add(griPan);

        Grid griSear = new Grid();
        griSear.setColumns(3);
        griSear.getChildren().add(new Etiqueta("Ingrese: "));
        txtCedula.setSize(11);
        griSear.getChildren().add(txtCedula);

        Grid griDate = new Grid();
        griDate.setColumns(4);
        griDate.getChildren().add(new Etiqueta("Fecha Inicial: "));
        fecha1.setSize(11);
        griDate.getChildren().add(fecha1);
        griDate.getChildren().add(new Etiqueta("Fecha Final: "));
        fecha1.setSize(11);
        griDate.getChildren().add(fecha2);
        griDate.getChildren().add(fecha2);
        griSear.getChildren().add(griDate);

        griSear.getChildren().add(new Etiqueta("Dirección: "));
        griSear.getChildren().add(cmbDireccion);

        Grid griEsta = new Grid();
        griEsta.setColumns(2);
        griEsta.getChildren().add(new Etiqueta("Estado: "));
        griEsta.getChildren().add(cmbEstado);
        griSear.getChildren().add(griEsta);

        griPri.getChildren().add(griSear);

        Grid griBot = new Grid();
        griBot.setColumns(2);
        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar ");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        griBot.getChildren().add(botBuscar);
        Boton botSearch = new Boton();
        botSearch.setValue("Buscar General");
        botSearch.setExcluirLectura(true);
        botSearch.setIcon("ui-icon-wrench");
        botSearch.setMetodo("buscaGeneral");
        griBot.getChildren().add(botSearch);
        Boton botEnd = new Boton();
        botEnd.setValue("Finalizar");
        botEnd.setExcluirLectura(true);
        botEnd.setIcon("ui-icon-close");
        botEnd.setMetodo("cierreDoc");
        griBot.getChildren().add(botEnd);
        Boton botVer = new Boton();
        botVer.setValue("Ver");
        botVer.setExcluirLectura(true);
        botVer.setIcon("ui-icon-open");
        botVer.setMetodo("crearArchivo");
        griBot.getChildren().add(botVer);

        Boton botEditar = new Boton();
        botEditar.setValue("Editar");
        botEditar.setExcluirLectura(true);
        botEditar.setIcon("ui-icon-open");
        botEditar.setMetodo("abrirEdicion");
        griBot.getChildren().add(botEditar);

        Boton botArchivo = new Boton();
        botArchivo.setValue("Re-asignar");
        botArchivo.setExcluirLectura(true);
        botArchivo.setIcon("ui-icon-open");
        botArchivo.setMetodo("asignaDocumento");
        griBot.getChildren().add(botArchivo);

        griPri.getChildren().add(griBot);
        bar_botones.agregarComponente(griPri);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("REPORTE GENERAL DE RECLAMO/S");
        agregarComponente(panOpcion);

        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("Comentario de Cierre"); //titulo
        diaRegistro.setWidth("50%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("40%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setMetodo("finDoc");
        gridre.setColumns(4);
        agregarComponente(diaRegistro);

        diaEditar.setId("diaEditar");
        diaEditar.setTitle("EDITAR DOCUMENTO"); //titulo
        diaEditar.setWidth("50%"); //siempre en porcentajes  ancho
        diaEditar.setHeight("40%");//siempre porcentaje   alto
        diaEditar.setResizable(false); //para que no se pueda cambiar el tamaño
        diaEditar.getBot_aceptar().setMetodo("editaDoc");
        grided.setColumns(4);
        agregarComponente(diaEditar);

        dibujarPantalla();

    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabDenuncia.setId("tabDenuncia");
        tabDenuncia.setTabla("RESUG_FORMULARIO", "ID_FORMULARIO", 1);
        if (autBusca.getValue() == null) {
            tabDenuncia.setCondicion("ID_FORMULARIO=-1");
        } else {
            tabDenuncia.setCondicion("ID_FORMULARIO in (" + autBusca.getValor() + ")");
        }
        tabDenuncia.getColumna("estado").setCombo("SELECT ID_ESTADO,ESTADO FROM RESUG_ESTADO");
        tabDenuncia.getColumna("ide_responsable").setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON");
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
        tabDenuncia.getColumna("login").setFiltro(true);
        tabDenuncia.getColumna("fecha1").setFiltro(true);
        tabDenuncia.getColumna("cedula").setFiltro(true);
        tabDenuncia.getColumna("nombre").setFiltro(true);
        tabDenuncia.getColumna("celular").setFiltro(true);
        tabDenuncia.getColumna("telefono").setFiltro(true);
        tabDenuncia.getColumna("observacion").setFiltro(true);
        tabDenuncia.getColumna("ide_responsable").setFiltro(true);
        tabDenuncia.getColumna("id_formulario").setFiltro(true);
        tabDenuncia.getColumna("ver").setVisible(false);
        tabDenuncia.getColumna("clave").setVisible(false);
        tabDenuncia.getColumna("adjunto").setVisible(false);
        tabDenuncia.getColumna("direccion").setVisible(false);
        tabDenuncia.getColumna("direccion1").setVisible(false);
        tabDenuncia.getColumna("mail").setLongitud(30);
        tabDenuncia.getColumna("nombre").setLongitud(50);
        tabDenuncia.getColumna("observacion").setLongitud(70);
        tabDenuncia.setRows(10);
        tabDenuncia.agregarRelacion(tabMovimiento);
        tabDenuncia.setLectura(true);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("resug_actividades", "id_actividad", 2);
        tabMovimiento.getColumna("tipo_actividad").setCombo("SELECT ID_ESTADO,ESTADO FROM RESUG_ESTADO");
        tabMovimiento.getColumna("uni_responsable").setCombo("SELECT ID_CODIGO,DESCRIPCION FROM RESUG_AREA_RESPON");
        tabMovimiento.getColumna("usu_adjunto").setUpload("formulario");
        tabMovimiento.getColumna("usu_adjunto").setImagen("", "");
        tabMovimiento.getColumna("usu_asignacion").setVisible(false);
        tabMovimiento.getColumna("usu_responsable").setVisible(false);
        tabMovimiento.setRows(10);
        tabMovimiento.setLectura(true);
        tabMovimiento.dibujar();

        PanelTabla pnr = new PanelTabla();
        pnr.setPanelTabla(tabMovimiento);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, pnr, "60%", "h");

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

    /*
     * Metodo para ingresar un comentario de cierre
     */
    public void cierreDoc() {
        if (tabDenuncia.getSelection() != null) {
            diaRegistro.Limpiar();
            diaRegistro.setDialogo(gridre);
            Grid griMostrar = new Grid();
            griMostrar.getChildren().clear();
            griMostrar.setColumns(2);
            griMostrar.getChildren().add(etiComentario);
            txaComentario.setCols(70);
            griMostrar.getChildren().add(txaComentario);
            gridRe.getChildren().add(griMostrar);
            diaRegistro.setDialogo(gridRe);
            diaRegistro.dibujar();
        } else {
            utilitario.agregarMensaje("Seleccione un registro", null);
        }
    }

    public void abrirEdicion() {
        if (tabDenuncia.getSelection() != null) {
            TablaGenerica tabDato = admin.getFormulario1(Integer.parseInt(tabDenuncia.getValor("id_formulario")));
            if (!tabDato.isEmpty()) {
                diaEditar.Limpiar();
                diaEditar.setDialogo(grided);
                Grid griMostrar = new Grid();
                griMostrar.getChildren().clear();
                griMostrar.setColumns(2);
                griMostrar.getChildren().add(etiMail);
                griMostrar.getChildren().add(txtMail);
                griMostrar.getChildren().add(etiReclamo);
                txaReclamo.setCols(70);
                griMostrar.getChildren().add(txaReclamo);
                gridEd.getChildren().add(griMostrar);
                diaEditar.setDialogo(gridEd);
                diaEditar.dibujar();
                txtId.setValue(tabDato.getValor("id_formulario") + "");
                txtMail.setValue(tabDato.getValor("mail") + "");
                txaReclamo.setValue(tabDato.getValor("observacion") + "");
                utilitario.addUpdate("txaReclamo");
                utilitario.addUpdate("txaReclamo");
            } else {
                utilitario.agregarMensaje("No encuentra datos", null);
            }
        } else {
            utilitario.agregarMensaje("Seleccione un registro", null);
        }
    }

    public void finDoc() {
        TablaGenerica tabDato = admin.getFormulario1(Integer.parseInt(tabDenuncia.getValor("id_formulario")));
        if (!tabDato.isEmpty()) {
            if (tabDato.getValor("TIPO_EMPRESA").equals("E")) {
                TablaGenerica tabCodigo = admin.getCodigoPrimario();
                if (!tabCodigo.isEmpty()) {
                    admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, 9, Integer.parseInt(tabDato.getValor("id_formulario")), Integer.parseInt(tabDato.getValor("ide_responsable")), null, txaComentario.getValue() + "", utilitario.getVariable("NICK"),utilitario.getIp(),Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                    admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                    utilitario.agregarMensaje("Documento Concluido", null);
                    admin.setAccionFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")), "9",Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                    diaRegistro.cerrar();
                }
            } else {
                if (Integer.parseInt(tabDato.getValor("ESTADO")) != 7) {
                    TablaGenerica tabCodigo = admin.getCodigoPrimario();
                    if (!tabCodigo.isEmpty()) {
                        admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, 9, Integer.parseInt(tabDato.getValor("id_formulario")), Integer.parseInt(tabDato.getValor("ide_responsable")), null, txaComentario.getValue() + "", utilitario.getVariable("NICK"),utilitario.getIp(),Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                        admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                        utilitario.agregarMensaje("Documento Concluido", null);
                        admin.setAccionFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")), "9",Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                        diaRegistro.cerrar();
                    }
                } else {
                    utilitario.agregarMensaje("Documento aún no tiene respuesta", null);
                }
            }
        }
    }

    public void editaDoc() {
        admin.setMailReclamo(Integer.parseInt(txtId.getValue() + ""), txtMail.getValue() + "", txaReclamo.getValue() + "", utilitario.getVariable("NICK"));
        utilitario.agregarMensaje("Registro Actualizado", null);
        diaEditar.cerrar();
    }
    /*
     * Metodo para busqueda
     */

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscaNumero();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscaCedula();
        } else if (cmbOpcion.getValue().equals("2")) {
            buscaDireccion();
        } else if (cmbOpcion.getValue().equals("3")) {
            buscaFecha();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    /*
     * Re - asignar Documento
     */
    public void asignaDocumento() {
        TablaGenerica tabDato = admin.getFormulario1(Integer.parseInt(tabDenuncia.getValor("id_formulario")));
        if (!tabDato.isEmpty()) {
            TablaGenerica tabCodigo = admin.getCodigoPrimario();
            if (!tabCodigo.isEmpty()) {;
                admin.setActividad(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1, 7, Integer.parseInt(tabDato.getValor("id_formulario")), Integer.parseInt(tabDato.getValor("ide_responsable")), null, null, utilitario.getVariable("NICK"),utilitario.getIp(),Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                admin.setTablaBloqueo(Integer.parseInt(tabCodigo.getValor("MAXIMO_BLOQ")) + 1);
                utilitario.agregarMensaje("Documento Re - Asiganado", null);
                admin.setAccionFormulario(Integer.parseInt(tabDenuncia.getValor("id_formulario")), "7",Integer.parseInt(utilitario.getVariable("IDE_PERF")));
                diaRegistro.cerrar();
            }
        }
    }

    /*
     * Opciones de busqueda
     */
    public void buscaNumero() {
        if (!getBuscaNumero().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaNumero());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaNumero() {
        String str_filtros = "";
        str_filtros = "id_formulario = " + txtCedula.getValue() + "";
        return str_filtros;
    }

    public void buscaCedula() {
        if (!getBuscaCedula().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaCedula());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaCedula() {
        String str_filtros = "";
        str_filtros = "cedula = '" + txtCedula.getValue() + "'";
        return str_filtros;
    }

    public void buscaDireccion() {
        if (!getBuscaDireccion().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaDireccion());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaDireccion() {
        String str_filtros = "";
        str_filtros = "where estado = " + Integer.parseInt(cmbEstado.getValue() + "") + " "
                + "and id_formulario in (SELECT ID_FORMULARIO FROM RESUG_ACTIVIDADES WHERE UNI_RESPONSABLE = " + Integer.parseInt(cmbDireccion.getValue() + "") + ") ";
        return str_filtros;
    }

    public void buscaFecha() {
        if (!getBuscaFecha().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaFecha());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaFecha() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "fecha between '" + fecha1.getFecha() + "' and '" + fecha2.getFecha() + "'";
        return str_filtros;
    }

    public void buscaGeneral() {
        if (!getBuscaGeneral().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaGeneral());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaGeneral() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "",
                anio = String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),
                mes = String.valueOf(utilitario.getMes(utilitario.getFechaActual()) - 1),
                dia = "01";

        str_filtros = "estado <> (SELECT ID_ESTADO FROM RESUG_ESTADO where estado = 'Negado') and estado <> (SELECT ID_ESTADO FROM RESUG_ESTADO where estado = 'Concluido') and fecha1 between '" + dia + "/" + mes + "/" + anio + "' and '" + utilitario.getFechaActual() + "'";
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

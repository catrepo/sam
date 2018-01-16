/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.direccion;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_financiero.contabilidad.ejb.DocumentosContabilidad;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class OrdenPago extends Pantalla {

    private Conexion conOracle = new Conexion();
    //Tabla Normal
    private Tabla tabOrden = new Tabla();
    private SeleccionTabla setRegistro = new SeleccionTabla();
    private Tabla tabConsulta = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private Etiqueta etiAnio = new Etiqueta("Año :");
    private Etiqueta etiTipo = new Etiqueta("Tipo :");
    private Etiqueta txtAno = new Etiqueta("AÑO : ");
    private Combo cmbAnio = new Combo();
    private Combo cmbAnio1 = new Combo();
    private Combo cmbTipo = new Combo();
    //Texto de Ingreso
    Texto txtMotivo = new Texto();
    //Dialogo Busca 
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogor = new Dialogo();
    private Dialogo diaDialogot = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridm = new Grid();
    private Grid gridt = new Grid();
    private Grid gridr = new Grid();
    private Grid gridD = new Grid();
    private Grid gridT = new Grid();
    private Grid gridR = new Grid();
    
        private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private DocumentosContabilidad admin = (DocumentosContabilidad) utilitario.instanciarEJB(DocumentosContabilidad.class);

    public OrdenPago() {
        /*
         * Cadena de conexión base de datos
         */
        conOracle.setUnidad_persistencia(utilitario.getPropiedad("oraclejdbc"));
        conOracle.NOMBRE_MARCA_BASE = "oracle";

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT  TES_IDE_ORDEN_PAGO, TES_NUMERO_ORDEN, TES_PROVEEDOR, TES_VALOR, TES_ACUERDO FROM dbo.TES_ORDEN_PAGO "
                + "where tes_estado_doc = '1' order by tes_numero_orden desc");
        autBusca.setSize(70);
//        bar_botones.agregarComponente(new Etiqueta("Registros Encontrado"));
//        bar_botones.agregarComponente(autBusca);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar Registro");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botBuscar);

        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("SELECT ano_curso,ano_curso as anio FROM conc_ano order by ano_curso desc");

        List listas = new ArrayList();
        Object filas1[] = {
            "Anulada", "Anulada"
        };

        Object filas2[] = {
            "Pendiente", "Pendiente"
        };

        Object filas3[] = {
            "Pagado", "Pagado"
        };
        listas.add(filas1);;
        listas.add(filas2);;
        listas.add(filas3);;
        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo(listas);
        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SOLICITUD DE ORDENES DE PAGO");
        agregarComponente(panOpcion);

        Boton botAnular = new Boton();
        botAnular.setValue("Anular");
        botAnular.setExcluirLectura(true);
        botAnular.setIcon("ui-icon-cancel");
        botAnular.setMetodo("quitar");
        bar_botones.agregarBoton(botAnular);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("¿ MOTIVO DE ANULACIÓN ?"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("anular");
        grid.setColumns(2);
        agregarComponente(diaDialogo);

        diaDialogot.setId("diaDialogot");
        diaDialogot.setTitle("LISTADO DE ORDENES DE PAGO REALIZADAS"); //titulo
        diaDialogot.setWidth("95%"); //siempre en porcentajes  ancho
        diaDialogot.setHeight("85%");//siempre porcentaje   alto
        diaDialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogot.getBot_aceptar().setMetodo("cargarRegistro");
        gridt.setColumns(4);
        agregarComponente(diaDialogot);

        diaDialogor.setId("diaDialogor");
        diaDialogor.setTitle("ORDENES DE PAGO POR TIPO"); //titulo
        diaDialogor.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogor.setHeight("25%");//siempre porcentaje   alto
        diaDialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogor.getBot_aceptar().setMetodo("aceptoOrden");
        gridr.setColumns(4);
        agregarComponente(diaDialogor);

        Grid griPartida = new Grid();
        griPartida.setColumns(5);
        griPartida.getChildren().add(txtAno);
        cmbAnio1.setId("cmbAnio1");
        cmbAnio1.setCombo("select DISTINCT tes_anio, tes_anio as año from tes_orden_pago");
        griPartida.getChildren().add(cmbAnio1);

        Boton botPartida = new Boton();
        botPartida.setId("botPartida");
        botPartida.setValue("Buscar");
        botPartida.setIcon("ui-icon-search");
        botPartida.setMetodo("buscarOrden");
        griPartida.getChildren().add(botPartida);

        setRegistro.setId("setRegistro");
        setRegistro.setSeleccionTabla("SELECT  TES_IDE_ORDEN_PAGO,\n"
                + "TES_NUMERO_ORDEN,\n"
                + "TES_PROVEEDOR,\n"
                + "TES_ASUNTO,\n"
                + "TES_VALOR,\n"
                + "TES_CONCEPTO,\n"
                + "TES_ACUERDO\n"
                + "FROM dbo.TES_ORDEN_PAGO\n"
                + "WHERE tes_estado_doc = -1 \n"
                + "ORDER BY TES_NUMERO_ORDEN DESC, tes_anio ASC", "tes_ide_orden_pago");
        setRegistro.getTab_seleccion().getColumna("tes_ide_orden_pago").setVisible(false);
        setRegistro.getTab_seleccion().getColumna("TES_NUMERO_ORDEN").setLongitud(4);
        setRegistro.getTab_seleccion().getColumna("TES_NUMERO_ORDEN").setFiltroContenido();
        setRegistro.getTab_seleccion().getColumna("TES_PROVEEDOR").setFiltroContenido();
        setRegistro.getTab_seleccion().getColumna("TES_ACUERDO").setFiltroContenido();;
        setRegistro.getTab_seleccion().getColumna("TES_ACUERDO").setLongitud(50);
        setRegistro.getTab_seleccion().getColumna("TES_PROVEEDOR").setLongitud(50);
        setRegistro.getTab_seleccion().getColumna("TES_ASUNTO").setLongitud(20);
        setRegistro.setRadio();
        setRegistro.getTab_seleccion().setRows(10);
        setRegistro.getGri_cuerpo().setHeader(griPartida);
        setRegistro.getBot_aceptar().setMetodo("cargarRegistro");
        setRegistro.setHeader("SELECCIONAR PARTIDA");
        agregarComponente(setRegistro);

                // CONFIGURACIÓN DE OBJETO REPORTE 
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
        
        dibujarOrden();
    }

    public void dibujarOrden() {
        limpiarPanel();
        tabOrden.setId("tabOrden");
        tabOrden.setTabla("tes_orden_pago", "tes_ide_orden_pago", 1);
        if (autBusca.getValue() == null) {
            tabOrden.setCondicion("tes_ide_orden_pago=-1");
        } else {
            tabOrden.setCondicion("tes_ide_orden_pago=" + autBusca.getValor());
        }
        tabOrden.getColumna("tes_id_proveedor").setMetodoChange("buscaProveedor");
        tabOrden.getColumna("tes_estado_doc").setValorDefecto("1");
        tabOrden.getColumna("tes_anio").setVisible(false);
        tabOrden.getColumna("tes_mes").setVisible(false);
        tabOrden.getColumna("tes_ide_orden_pago").setVisible(false);
        tabOrden.getColumna("tes_ide_orden_pago").setVisible(false);
        tabOrden.getColumna("tes_cod_empleado").setVisible(false);
        tabOrden.getColumna("tes_estado_doc").setVisible(false);
        tabOrden.getColumna("tes_empleado").setVisible(false);
        tabOrden.getColumna("tipo_solicitantep").setVisible(false);
        tabOrden.getColumna("tes_login_ing").setVisible(false);
        tabOrden.getColumna("tes_login_anu").setVisible(false);
        tabOrden.getColumna("tes_fecha_anu").setVisible(false);
        tabOrden.getColumna("tes_login_actu").setVisible(false);
        tabOrden.getColumna("tes_fecha_actu").setVisible(false);
        tabOrden.getColumna("tes_login_envio").setVisible(false);
        tabOrden.getColumna("tes_comentario_anula").setVisible(false);
        tabOrden.getColumna("tes_letrero").setVisible(false);
        tabOrden.getColumna("tes_login_mod").setVisible(false);
        tabOrden.getColumna("tes_fecha_mod").setVisible(false);
        tabOrden.getColumna("tes_anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tabOrden.getColumna("tes_mes").setValorDefecto(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        tabOrden.getColumna("TES_FECHA_INGRESO").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("tes_login_ing").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabOrden.getColumna("tes_valor").setMetodoChange("numLetras");
        tabOrden.getColumna("tes_comprobante_egreso").setMetodoChange("comprobante");
        tabOrden.setTipoFormulario(true);
        tabOrden.getGrid().setColumns(2);
        tabOrden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabOrden);
        Grupo gru = new Grupo();
        gru.getChildren().add(pto);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void buscaProveedor() {
        if (tabOrden.getValor("tes_id_proveedor") != null) {
            TablaGenerica tab_dato = admin.getBuscar_Datos_Proveedores(tabOrden.getValor("tes_id_proveedor"));
            if (!tab_dato.isEmpty()) {
                tabOrden.setValor("tes_proveedor", tab_dato.getValor("nombre"));
                 tabOrden.setValor("TES_COD_EMPLEADO", tab_dato.getValor("CODTRA"));
                tabOrden.setValor("tes_estado", "Pendiente");
                utilitario.addUpdate("tabOrden");//actualiza solo componente
            }
        } else {
            utilitario.agregarMensajeInfo("ingresar Cedula o RUC", "");
        }
    }

    public void numLetras() {
        Object objeto = (Object) tabOrden.getValor("tes_valor");
        tabOrden.setValor("tes_valor_letras", utilitario.getLetrasDolarNumero((objeto)));
        utilitario.addUpdate("tabOrden");//actualiza solo componentes
    }

    public void comprobante() {
        if (tabOrden.getValor("tes_estado") != "Anulada") {
            tabOrden.setValor("tes_estado", "Pagado");
            utilitario.addUpdate("tabOrden");
        } else {
            utilitario.agregarMensajeInfo("Comprobante No Puede Ser Pagado", "Se Encuentra Anulado");
        }
    }

    public void buscaRegistro() {
        setRegistro.dibujar();
    }

    public void buscarOrden() {
        if (cmbAnio1.getValue() != null && cmbAnio1.getValue() != null) {
            setRegistro.getTab_seleccion().setSql("SELECT  TES_IDE_ORDEN_PAGO,\n"
                    + "TES_NUMERO_ORDEN,\n"
                    + "TES_PROVEEDOR,\n"
                    + "TES_ASUNTO,\n"
                    + "TES_VALOR,\n"
                    + "TES_CONCEPTO,\n"
                    + "TES_ACUERDO\n"
                    + "FROM dbo.TES_ORDEN_PAGO\n"
                    + "WHERE tes_estado_doc = '1' and tes_anio =" + cmbAnio1.getValue() + " \n"
                    + "ORDER BY TES_NUMERO_ORDEN DESC, tes_anio ASC");
            setRegistro.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar parametros", "");
        }
    }

    public void cargarRegistro() {
        if (setRegistro.getValorSeleccionado() != null) {
            System.err.println(setRegistro.getValorSeleccionado());
            autBusca.setValor(setRegistro.getValorSeleccionado());
            dibujarOrden();
            setRegistro.cerrar();
            utilitario.addUpdate("autBusca,panOpcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Solicitud", "");
        }
    }

    public void quitar() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        txtMotivo.setSize(50);
        Grid griPartida = new Grid();
        griPartida.getChildren().add(new Etiqueta("INGRESE MOTIVO DE ANULACIÓN DE ORDEN"));
        griPartida.getChildren().add(txtMotivo);
        grid.getChildren().add(griPartida);
        diaDialogo.setDialogo(gridD);
        diaDialogo.dibujar();
    }

    public void anular() {
        admin.actOrden(tabOrden.getValor("tes_numero_orden"), Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago")), tabConsulta.getValor("NICK_USUA"), txtMotivo.getValue() + "");
        utilitario.addUpdate("tabOrden");
        utilitario.agregarMensaje("ORDEN ANULADA", "");
        diaDialogo.cerrar();
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        numero();
    }

    @Override
    public void guardar() {
        if (tabOrden.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "IMPRIMIR ORDEN":
                aceptoOrden();
                break;
            case "ORDEN ESTADO":
                diaDialogor.Limpiar();
                diaDialogor.setDialogo(gridr);
                gridR.getChildren().add(etiAnio);
                gridR.getChildren().add(cmbAnio);
                gridR.getChildren().add(etiTipo);
                gridR.getChildren().add(cmbTipo);
                diaDialogor.setDialogo(gridR);
                diaDialogor.dibujar();
                break;
        }
    }

    public void aceptoOrden() {
        switch (rep_reporte.getNombre()) {
            case "IMPRIMIR ORDEN":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("id_orden", tabOrden.getValor("tes_numero_orden") + "");
                p_parametros.put("id_documento", Integer.parseInt(tabOrden.getValor("tes_ide_orden_pago") + ""));
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "ORDEN ESTADO":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("anio", cmbAnio.getValue() + "");
                p_parametros.put("tipo", cmbTipo.getValue() + "");
                p_parametros.put("fecha", utilitario.getFechaActual()+ "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }
    
    public void numero() {
        String numero = admin.maximOrden();
        String num;
        Integer cantidad = 0;
        cantidad = Integer.parseInt(numero) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "000000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "00000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = "0000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 1000 && cantidad <= 9999) {
                num = "000" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 10000 && cantidad <= 99999) {
                num = "00" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 100000 && cantidad <= 999999) {
                num = "0" + String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            } else if (cantidad >= 1000000 && cantidad <= 9999999) {
                num = String.valueOf(cantidad);
                tabOrden.setValor("tes_numero_orden", num);
            }
        }
        utilitario.addUpdate("tabOrden");
    }

    public Conexion getConOracle() {
        return conOracle;
    }

    public void setConOracle(Conexion conOracle) {
        this.conOracle = conOracle;
    }

    public Tabla getTabOrden() {
        return tabOrden;
    }

    public void setTabOrden(Tabla tabOrden) {
        this.tabOrden = tabOrden;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public SeleccionTabla getSetRegistro() {
        return setRegistro;
    }

    public void setSetRegistro(SeleccionTabla setRegistro) {
        this.setRegistro = setRegistro;
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

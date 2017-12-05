/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
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
import paq_nomina.ejb.AntiSueldos;
import paq_nomina.ejb.mergeDescuento;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author l-suntaxi
 */
public class pre_detalle_movimiento_fallecido extends Pantalla {

    //Conexion a base
//    private Conexion conSqler = new Conexion();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_consulta = new Tabla();
    private SeleccionTabla set_rol = new SeleccionTabla();
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //Combos de Selección
    private Combo cmb_anos = new Combo();
    private Combo cmb_mesin = new Combo();
    private Combo cmb_mesfin = new Combo();
    private Combo cmb_anios = new Combo();
    private Calendario fecha = new Calendario();
    private List lisCatastro = new ArrayList();
    //Dialogos
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_reporte = new Dialogo();
    private Dialogo dia_reportes = new Dialogo();
    private Grid grid_g = new Grid();
    private Grid grid_r = new Grid();
    private Grid gridR = new Grid();
    private Grid grid_rs = new Grid();
    private Grid gridRs = new Grid();
    @EJB
    private AntiSueldos iAnticipos = (AntiSueldos) utilitario.instanciarEJB(AntiSueldos.class);
    private mergeDescuento mDescuento = (mergeDescuento) utilitario.instanciarEJB(mergeDescuento.class);
    //COMBOS DE SELECICON
    private Combo cmb_anio = new Combo();
    private Combo cmb_periodo = new Combo();
    private Combo cmb_descripcion = new Combo();
    private Texto txt_partida = new Texto();

    public pre_detalle_movimiento_fallecido() {
        Boton bot_busca = new Boton();
        bot_busca.setValue("BUSCAR");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("actualizaLista");
        bar_botones.agregarBoton(bot_busca);

        //Para capturar el usuario que se encuntra utilizando la opción
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        //cadena de conexión para otra base de datos
//        conSqler.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcer"));
//        conSqler.NOMBRE_MARCA_BASE = "sqlserver";

        tab_tabla1.setId("tab_tabla1");
//        tab_tabla1.setConexion(conSqler);
        tab_tabla1.setSql("select IDE_FALLECIDO,cedula_fallecido,nombres,FECHA_NACIMIENTO,FECHA_DEFUNCION,CERTIFICADO_DEFUN,FECHA_INGRE from CMT_FALLECIDO ORDER BY nombres ASC");
        tab_tabla1.getColumna("cedula_fallecido").setFiltro(true);
        tab_tabla1.getColumna("nombres").setFiltro(true);
        tab_tabla1.setRows(26);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
//        tab_tabla2.setConexion(con_postgres);
        tab_tabla2.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 1);


        tab_tabla2.getColumna("IDE_CATASTRO").setVisible(false);
        tab_tabla2.getColumna("IDE_CMREP").setVisible(false);
        tab_tabla2.getColumna("IDE_FALLECIDO").setVisible(false);
        tab_tabla2.getColumna("NUMERO_FORMULARIO").setVisible(false);
        tab_tabla2.getColumna("CODIGO_LIQUIDACION").setVisible(false);
        tab_tabla2.getColumna("PERIODO_LIQUIDACION").setVisible(false);
        tab_tabla2.getColumna("PERIODO_TITULO").setVisible(false);
        tab_tabla2.getColumna("IDE_CATASTRO2").setVisible(false);

        tab_tabla2.getColumna("NUM_LIQUIDACION").setNombreVisual("LIQUIDACION");
        tab_tabla2.getColumna("IDE_TIPO_MOVIMIENTO").setNombreVisual("TIPO MOVIMIENTO");
//      tab_tabla2.getColumna("NUM_LIQUIDACION").setNombreVisual("LIQUIDACION");
//      tab_tabla2.getColumna("NUM_LIQUIDACION").setNombreVisual("LIQUIDACION");
//      tab_tabla2.getColumna("NUM_LIQUIDACION").setNombreVisual("LIQUIDACION");

        tab_tabla2.setLectura(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "28%", "H");
        agregarComponente(div_division);

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
//        sef_formato.setConexion(conSqler);
        agregarComponente(sef_formato);


        Grid gri_search = new Grid();
        gri_search.setColumns(2);

        /* gri_search.getChildren().add(new Etiqueta("AÑO: "));
         cmb_anos.setId("cmb_anos");
         //        cmb_anos.setConexion(con_postgres);
         cmb_anos.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
         cmb_anos.setMetodo("comp_anio");
         gri_search.getChildren().add(cmb_anos);

         gri_search.getChildren().add(new Etiqueta("Periodo Inicial: "));
         cmb_mesin.setId("cmb_mesin");
         //        cmb_mesin.setConexion(con_postgres);
         cmb_mesin.setCombo("SELECT per_descripcion,per_descripcion as mes FROM cont_periodo_actual ORDER BY ide_periodo");
         gri_search.getChildren().add(cmb_mesin);

         gri_search.getChildren().add(new Etiqueta("Periodo final: "));
         cmb_mesfin.setId("cmb_mesfin");
         //        cmb_mesfin.setConexion(con_postgres);
         cmb_mesfin.setCombo("SELECT per_descripcion,per_descripcion as mes FROM cont_periodo_actual ORDER BY ide_periodo");
         gri_search.getChildren().add(cmb_mesfin);

         //para poder busca por apelllido el garante
         dia_dialogo.setId("dia_dialogo");
         dia_dialogo.setTitle("SELECCIONAR PARAMETROS PARA REPORTE"); //titulo
         dia_dialogo.setWidth("30%"); //siempre en porcentajes  ancho
         dia_dialogo.setHeight("25%");//siempre porcentaje   alto
         dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
         dia_dialogo.getGri_cuerpo().setHeader(gri_search);
         dia_dialogo.getBot_aceptar().setMetodo("aceptoAnticipo");
         grid_g.setColumns(4);
         agregarComponente(dia_dialogo);

         dia_reporte.setId("dia_reporte");
         dia_reporte.setTitle("PARAMETROS PARA REPORTE"); //titulo
         dia_reporte.setWidth("30%"); //siempre en porcentajes  ancho
         dia_reporte.setHeight("25%");//siempre porcentaje   alto
         dia_reporte.setResizable(false); //para que no se pueda cambiar el tamaño
         dia_reporte.getBot_aceptar().setMetodo("aceptoAnticipo");
         grid_r.setColumns(4);
         agregarComponente(dia_reporte);

         dia_reportes.setId("dia_reportes");
         dia_reportes.setTitle("PARAMETROS PARA REPORTE"); //titulo
         dia_reportes.setWidth("30%"); //siempre en porcentajes  ancho
         dia_reportes.setHeight("25%");//siempre porcentaje   alto
         dia_reportes.setResizable(false); //para que no se pueda cambiar el tamaño
         dia_reportes.getBot_aceptar().setMetodo("aceptoAnticipo");
         grid_rs.setColumns(4);
         agregarComponente(dia_reportes);

         Grid gri_busca = new Grid();
         gri_busca.setColumns(2);

         gri_busca.getChildren().add(new Etiqueta("AÑO:"));
         cmb_anio.setId("cmb_anio");
         //        cmb_anio.setConexion(con_postgres);
         cmb_anio.setCombo("select ano_curso, ano_curso from conc_ano order by ano_curso");
         gri_busca.getChildren().add(cmb_anio);

         gri_busca.getChildren().add(new Etiqueta("PERIODO:"));
         cmb_periodo.setId("cmb_periodo");
         //        cmb_periodo.setConexion(con_postgres);
         cmb_periodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
         gri_busca.getChildren().add(cmb_periodo);

         gri_busca.getChildren().add(new Etiqueta("DESCRIPCIÓN:"));
         cmb_descripcion.setId("cmb_descripcion");
         //        cmb_descripcion.setConexion(con_postgres);
         cmb_descripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
         cmb_descripcion.setMetodo("buscarColumna");
         gri_busca.getChildren().add(cmb_descripcion);

         gri_busca.getChildren().add(new Etiqueta("PARTIDA:"));
         txt_partida.setId("txt_partida");
         gri_busca.getChildren().add(txt_partida);

         /*
         * CREACION DE TABLA SELECCION PARA COLUMNAS
         */

        /* set_rol.setId("set_rol");
         //        set_rol.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
         set_rol.setSeleccionTabla("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE ide_col=-1", "ide_col");
         set_rol.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
         set_rol.getTab_seleccion().setRows(12);
         set_rol.setRadio();
         set_rol.getGri_cuerpo().setHeader(gri_busca);
         set_rol.getBot_aceptar().setMetodo("aceptoAnticipo");
         set_rol.setHeader("REPORTES DE DESCUENTOS - SELECCIONE PARAMETROS");
         agregarComponente(set_rol);*/
        actualizaLista();

    }

    public List listado() {
        lisCatastro.clear();
        TablaGenerica tabCatastro = utilitario.consultar("SELECT ano_curso as id,ano_curso FROM conc_ano where actual = 'A'");
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            Object[] obj = new Object[2];
            obj[0] = tabCatastro.getValor(i, "id");
            obj[1] = tabCatastro.getValor(i, "ano_curso");
            lisCatastro.add(obj);
        }
        return lisCatastro;
    }

    public void comp_anio() {
        if (Integer.parseInt(cmb_anos.getValue() + "") <= 2014) {
            utilitario.agregarMensaje("Detalle No Disponible Para Año Seleccionado", "Escoga otro ???");
        }
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tab_tabla2.setCondicion(getFiltrosAcceso());
            tab_tabla2.ejecutarSql();
            utilitario.addUpdate("tab_tabla2");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tab_tabla1.getValorSeleccionado() != null) {

            str_filtros = "IDE_FALLECIDO = "
                    + String.valueOf(tab_tabla1.getValorSeleccionado());

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe seleccionar valor");
        }
        return str_filtros;
    }

    public void buscarColumna() {
        if (cmb_descripcion.getValue() != null && cmb_descripcion.getValue().toString().isEmpty() == false) {
            set_rol.getTab_seleccion().setSql("SELECT ide_col,descripcion_col FROM SRH_COLUMNAS WHERE DISTRIBUTIVO=" + cmb_descripcion.getValue());
            set_rol.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo", "");
        }
    }

    public void migrarCuentas() {
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

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "VER ANTICIPO":
                aceptoAnticipo();
                break;
            case "CONSOLIDADO DE  ANTICIPO":
                dia_dialogo.Limpiar();
                dia_dialogo.dibujar();
                break;
            case "REPORTE ROLES TOTAL DESCUENTOS":
                set_rol.dibujar();
                set_rol.getTab_seleccion().limpiar();
                break;
            case "TOTAL ANTICIPOS PENDIENTES":
                aceptoAnticipo();
                break;
            case "TOTAL ANTICIPOS GENERAL":
                aceptoAnticipo();
                break;
            case "ANTICIPOS POR MES":
                dia_reporte.Limpiar();
                Grid gri_search = new Grid();
                gri_search.setColumns(2);
                dia_reporte.setDialogo(grid_r);
                gri_search.getChildren().add(new Etiqueta("Fecha Final"));
                gri_search.getChildren().add(fecha);
                gri_search.getChildren().add(new Etiqueta("Distributivo"));
                gri_search.getChildren().add(cmb_descripcion);
                grid_r.getChildren().add(gri_search);
                dia_reporte.setDialogo(gridR);
                dia_reporte.dibujar();
                break;
            case "ANTICIPOS POR AÑO":
                cmb_anios.setId("cmb_anios");
                cmb_anios.setCombo(listado());
                dia_reporte.Limpiar();
                Grid grisearch = new Grid();
                grisearch.setColumns(2);
                dia_reportes.setDialogo(grid_rs);
                grisearch.getChildren().add(new Etiqueta("Año : "));
                grisearch.getChildren().add(cmb_anios);
                grid_rs.getChildren().add(grisearch);
                dia_reportes.setDialogo(gridR);
                dia_reportes.dibujar();
                break;
        }
    }

    public void aceptoAnticipo() {
        switch (rep_reporte.getNombre()) {
            case "VER ANTICIPO":
                TablaGenerica tab_dato = iAnticipos.getCedula(Integer.parseInt(tab_tabla2.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                    p_parametros.put("identificacion", tab_dato.getValor("ci_solicitante") + "");
                    p_parametros.put("codigo", Integer.parseInt(tab_dato.getValor("ide_solicitud_anticipo") + ""));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("No puede Mostrar Reporte", "Datos no encontrados");
                }
                break;
            case "DETALLE ANTICIPO POR MESES":
                TablaGenerica tab_dato1 = iAnticipos.getTotalAnt(String.valueOf(Integer.parseInt(cmb_anos.getValue() + "") - 1));
                if (!tab_dato1.isEmpty()) {
                    p_parametros.put("anio_anterior", String.valueOf(Integer.parseInt(cmb_anos.getValue() + "") - 1));
                    p_parametros.put("mes", cmb_mesin.getValue() + "");
                    p_parametros.put("mes1", cmb_mesfin.getValue() + "");
                    p_parametros.put("anio_actual", cmb_anos.getValue() + "");
                    p_parametros.put("anticipo", Double.valueOf(tab_dato1.getValor("anticipo") + ""));
                    p_parametros.put("descuento", Double.valueOf(tab_dato1.getValor("descontar") + ""));
                    p_parametros.put("saldo", Double.valueOf(tab_dato1.getValor("saldo") + ""));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("No puede Mostrar Reporte", "Detalle No encontrado");
                }
                break;
            case "REPORTE ROLES TOTAL DESCUENTOS":
                if (set_rol.getValorSeleccionado() != null) {
                    TablaGenerica tab_dato3 = mDescuento.periodo(Integer.parseInt(cmb_periodo.getValue() + ""));
                    if (!tab_dato3.isEmpty()) {
                        TablaGenerica tab_dato4 = mDescuento.distibutivo(Integer.parseInt(cmb_descripcion.getValue() + ""));
                        if (!tab_dato4.isEmpty()) {
                            TablaGenerica tab_dato5 = mDescuento.columnas(Integer.parseInt(set_rol.getValorSeleccionado() + ""));
                            if (!tab_dato5.isEmpty()) {
                                p_parametros = new HashMap();
                                p_parametros.put("pide_ano", Integer.parseInt(cmb_anio.getValue() + ""));
                                p_parametros.put("periodo", Integer.parseInt(cmb_periodo.getValue() + ""));
                                p_parametros.put("p_nombre", tab_dato3.getValor("per_descripcion") + "");
                                p_parametros.put("distributivo", Integer.parseInt(cmb_descripcion.getValue() + ""));
                                p_parametros.put("descripcion", tab_dato4.getValor("descripcion") + "");
                                p_parametros.put("columnas", Integer.parseInt(set_rol.getValorSeleccionado() + ""));
                                p_parametros.put("descrip", tab_dato5.getValor("descripcion_col") + "");
                                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                                p_parametros.put("partida", txt_partida.getValue() + "");
                                rep_reporte.cerrar();
                                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                                sef_formato.dibujar();
                                set_rol.cerrar();
                            } else {
                                utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                            }
                        } else {
                            utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("no existe en la base de datos", "");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
                }
                break;
            case "TOTAL ANTICIPOS PENDIENTES":
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "ANTICIPOS POR MES":
                TablaGenerica tab_dato4 = mDescuento.distibutivo(Integer.parseInt(cmb_descripcion.getValue() + ""));
                if (!tab_dato4.isEmpty()) {
                    p_parametros = new HashMap();
                    p_parametros.put("fecha", fecha.getFecha());
                    p_parametros.put("mes", String.valueOf(utilitario.getMes(fecha.getFecha())));
                    p_parametros.put("anio", String.valueOf(utilitario.getAnio(fecha.getFecha())));
                    p_parametros.put("nombre", tab_dato4.getValor("descripcion") + "");
                    p_parametros.put("distributivo", Integer.parseInt(cmb_descripcion.getValue() + ""));
                    p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                    set_rol.cerrar();
                }
                break;
            case "TOTAL ANTICIPOS GENERAL":
                p_parametros = new HashMap();
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                set_rol.cerrar();
                break;
            case "ANTICIPOS POR AÑO":
                p_parametros = new HashMap();
                p_parametros.put("anio", Integer.parseInt(cmb_anios.getValue() + ""));
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                set_rol.cerrar();
                break;
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
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

    public SeleccionTabla getSet_rol() {
        return set_rol;
    }

    public void setSet_rol(SeleccionTabla set_rol) {
        this.set_rol = set_rol;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bienes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_bienes.ejb.acfbienes;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class InmueblesGADMUR extends Pantalla{

    private Conexion conCAYMAM = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setDatos = new SeleccionTabla();
    private Panel panOpcion = new Panel();
    private Panel panBotones = new Panel();
    private Combo cmbAnio = new Combo();
    private Tabla tabBienes = new Tabla();
    private List lisCatastro = new ArrayList();
    private AutoCompletar autBusca = new AutoCompletar();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private acfbienes activos = (acfbienes) utilitario.instanciarEJB(acfbienes.class);
    
    public InmueblesGADMUR() {
                tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conCAYMAM.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcayman"));
        conCAYMAM.NOMBRE_MARCA_BASE = "sqlserver";

        autBusca.setId("autBusca");
        autBusca.setConexion(conCAYMAM);
        autBusca.setAutoCompletar("SELECT id_inmueble,numero_acta,fechad,clave FROM ACF_INMUEBLES where acta_baja is null");
        autBusca.setSize(70);
        autBusca.setMetodoChange("buscarActa");

        Boton botBusAnte = new Boton();
        botBusAnte.setId("botBusAnte");
        botBusAnte.setValue("Busca Acta");
        botBusAnte.setIcon("ui-icon-open");
        botBusAnte.setMetodo("abrirActas");
        bar_botones.agregarBoton(botBusAnte);
        
        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("ACTA DE CONSTATACIÓN FISICA INMUEBLES");
        agregarComponente(tabp2);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setStyle("font-size:12px;color:black;text-align:left;");

        Grid griBotones = new Grid();
        griBotones.setColumns(4);
        Boton botNew = new Boton();
        botNew.setId("botNew");
        botNew.setValue("Nuevo");
        botNew.setIcon("ui-icon-document");
        botNew.setMetodo("insertar");

        Boton botSave = new Boton();
        botSave.setId("botSave");
        botSave.setValue("Guardar");
        botSave.setIcon("ui-icon-disk");
        botSave.setMetodo("guardar");

        Boton botPrint = new Boton();
        botPrint.setId("botPrint");
        botPrint.setValue("Imprimir");
        botPrint.setIcon("ui-icon-print");
        botPrint.setMetodo("abrirListaReportes");
        griBotones.getChildren().add(botNew);
        griBotones.getChildren().add(botSave);
        griBotones.getChildren().add(botPrint);

        Boton botCancel = new Boton();
        botCancel.setId("botCancel");
        botCancel.setValue("Anular");
        botCancel.setIcon("ui-icon-print");
        botCancel.setMetodo("eliminar");
        griBotones.getChildren().add(botNew);
        griBotones.getChildren().add(botSave);
        griBotones.getChildren().add(botPrint);
        griBotones.getChildren().add(botCancel);

        panBotones.setId("panBotones");
        panBotones.setTransient(true);
        panBotones.setStyle("font-size:12px;color:black;text-align:center;");
        panBotones.getChildren().add(griBotones);

        agregarComponente(panOpcion);
        agregarComponente(panBotones);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conCAYMAM);
        agregarComponente(sef_formato);

        Grid griBusca = new Grid();
        griBusca.setColumns(2);

        cmbAnio.setId("cmbAnio");
        cmbAnio.setConexion(conCAYMAM);
        cmbAnio.setCombo("select DISTINCT anio, anio as ano from ACF_INMUEBLES order by anio desc");
        griBusca.getChildren().add(new Etiqueta("Año :"));
        griBusca.getChildren().add(cmbAnio);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar");
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        griBusca.getChildren().add(botBuscar);


        setDatos.setId("setDatos");
        setDatos.getTab_seleccion().setConexion(conCAYMAM);
        setDatos.setSeleccionTabla("select id_inmueble,clave,numero_acta,fechad from ACF_INMUEBLES where id_inmueble = -1", "id_inmueble");
        setDatos.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setDatos.getTab_seleccion().getColumna("clave").setLongitud(30);
        setDatos.getTab_seleccion().getColumna("numero_acta").setLongitud(30);
        setDatos.getTab_seleccion().setRows(10);
        setDatos.setRadio();
        setDatos.getGri_cuerpo().setHeader(griBusca);
        setDatos.getBot_aceptar().setMetodo("consultaActa");
        setDatos.setWidth("63%");
        setDatos.setHeader("LISTA DE ACTAS POR AÑO");
        agregarComponente(setDatos);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();

        tabBienes.setId("tabBienes");
        tabBienes.setConexion(conCAYMAM);
        tabBienes.setTabla("ACF_INMUEBLES", "id_inmueble", 1);
        if (autBusca.getValue() == null) {
            tabBienes.setCondicion("id_inmueble=-1");
        } else {
            tabBienes.setCondicion("id_inmueble=" + autBusca.getValor());
        }
        tabBienes.getColumna("clave").setCombo("select distinct (case when MAE_CLVACT is null then clave else MAE_CLVACT end ) as MAE_CLVACT,clave  from vw_ActivosInmuebles left join  vw_CatastroUrbanoRural  on MAE_CLVACT = clave order by clave");
//        tabBienes.getColumna("delegado_avaluos").setCombo("SELECT CUS_ID,CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'') as custodios  from custodio where SUBSTRING(CUS_APELLIDOS, 0, 2)<>'0' order by CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'')   ");
//        tabBienes.getColumna("delegado_financiero").setCombo("SELECT CUS_ID,CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'') as custodios  from custodio where SUBSTRING(CUS_APELLIDOS, 0, 2)<>'0' order by CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'')   ");
        tabBienes.getColumna("clave").setMetodoChange("datos");
        tabBienes.getColumna("id_inmueble").setVisible(false);
        tabBienes.getColumna("valor_avaluo").setVisible(false);
        tabBienes.getColumna("construccion_avaluo").setVisible(false);
        tabBienes.getColumna("area_avaluo").setVisible(false);
        tabBienes.getColumna("codigoend").setVisible(false);
        tabBienes.getColumna("anio").setVisible(false);
        tabBienes.getColumna("cuenta").setVisible(false);
        tabBienes.getColumna("catastro_baja").setVisible(false);
        tabBienes.getColumna("canton_notario").setVisible(false);
        tabBienes.getColumna("fecha_notaria").setVisible(false);
        tabBienes.getColumna("responsable_bienes").setVisible(false);
        tabBienes.getColumna("login_registro").setVisible(false);
        tabBienes.getColumna("ip_registro").setVisible(false);
        tabBienes.getColumna("delegado_avaluos").setVisible(false);
        tabBienes.getColumna("delegado_financiero").setVisible(false);
        tabBienes.getColumna("acta_baja").setVisible(false);
        tabBienes.getColumna("responsable_administrativo").setVisible(false);
        tabBienes.getColumna("ip_registro").setValorDefecto(utilitario.getIp());
        tabBienes.getColumna("login_registro").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabBienes.getColumna("fechad").setValorDefecto(utilitario.getFechaActual());
        tabBienes.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tabBienes.setTipoFormulario(true);
        tabBienes.getGrid().setColumns(2);
        tabBienes.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabBienes);
        Grupo gru = new Grupo();
        gru.getChildren().add(pto);
        panOpcion.getChildren().add(gru);

    }

    public List listado() {
        lisCatastro.clear();
        TablaGenerica tabCatastro = utilitario.consultar("select clave,clave as catastro,barrio,propietario \n"
                + "from prediosv \n"
                + "where propietario like 'municipio%' \n"
                + "or propietario like 'GOBIERNO%' ");
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            Object[] obj = new Object[2];
            obj[0] = tabCatastro.getValor(i, "clave");
            obj[1] = tabCatastro.getValor(i, "catastro");
            lisCatastro.add(obj);
        }
        return lisCatastro;
    }

    public void datos() {
        TablaGenerica tabDato = activos.buscaDatosAvaluos(tabBienes.getValor("clave"));
        if (!tabDato.isEmpty()) {
            tabBienes.setValor("propietario", tabDato.getValor("MAE_PROPIE"));
            tabBienes.setValor("parroquia", tabDato.getValor("parroquia"));
            tabBienes.setValor("barrio", tabDato.getValor("MAE_BARRIO"));
            tabBienes.setValor("calles", tabDato.getValor("MAE_CALLE"));
            tabBienes.setValor("predio", tabDato.getValor("uor_nombre2"));
            tabBienes.setValor("area_terreno", tabDato.getValor("area"));
            tabBienes.setValor("area_cons", tabDato.getValor("VAL_ARCON"));            
            tabBienes.setValor("canton_notario", tabDato.getValor("mae_canton"));
            tabBienes.setValor("fecha_notaria", tabDato.getValor("fec_registro"));            
            tabBienes.setValor("val_catastro", tabDato.getValor("valor_total"));
            tabBienes.setValor("codigo", tabDato.getValor("act_codigo1"));
            tabBienes.setValor("val_bienes", tabDato.getValor("act_valorcompra"));
            tabBienes.setValor("estado", tabDato.getValor("est_nombre"));
            tabBienes.setValor("ubicacion", tabDato.getValor("cus_nombre"));
            tabBienes.setValor("cuenta", tabDato.getValor("gru_nombre1"));
            
            utilitario.addUpdate("tabBienes");
            datosFirmas();
        } else {
            utilitario.agregarMensajeError("No encuentra datos en Avaluos", null);
        }
    }

     public void datosFirmas() {
         TablaGenerica tabDatos = activos.buscaFirmaAvaluos("Constatacion Inmuebles");
        if (!tabDatos.isEmpty()) {
            for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                if (tabDatos.getValor(i, "REPT_TIPO").equals("J")) {
                    System.err.println("");
                    tabBienes.setValor("responsable_bienes", tabDatos.getValor(i,"REPT_delegado"));
                }
                if (tabDatos.getValor(i, "REPT_TIPO").equals("D")) {
                    tabBienes.setValor("responsable_administrativo", tabDatos.getValor(i,"REPT_delegado"));
                }
                if (tabDatos.getValor(i, "REPT_TIPO").equals("DF")) {
                    tabBienes.setValor("delegado_financiero", tabDatos.getValor(i,"REPT_delegado"));
                }
                if (tabDatos.getValor(i, "REPT_TIPO").equals("DAC")) {
                    tabBienes.setValor("delegado_avaluos", tabDatos.getValor(i,"REPT_delegado"));
                }
                 utilitario.addUpdate("tabBienes");
            }
        }else{
             utilitario.agregarMensajeError("No encuentra firmas en Avaluos", null);
        }
     }
    
    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void abrirActas(){
        setDatos.dibujar();
    }
    
    public void busquedaInfo() {
        if (cmbAnio.getValue() != null && cmbAnio.getValue().toString().isEmpty() == false) {
            setDatos.getTab_seleccion().setSql("select id_inmueble,clave,numero_acta,fechad \n"
                    + "from ACF_INMUEBLES\n"
                    + "where anio  = " + cmbAnio.getValue() + " and acta_baja is null order by numero_acta desc");
            setDatos.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
            setDatos.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void consultaActa() {
        limpiar();
        autBusca.setValor(setDatos.getValorSeleccionado());
        dibujarPantalla();
        setDatos.cerrar();
    }

    public void numero() {
        if (Integer.parseInt(activos.listaMax("ACFINM")) > 0) {
            tabBienes.setValor("numero_acta", activos.listaMax("ACFINM"));
            utilitario.addUpdate("tabBienes");
        } else {
            utilitario.agregarMensaje("No se encuentra número para acta", "");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        numero();
    }

    @Override
    public void guardar() {
        if (tabBienes.getValor("id_inmueble") != null) {
            conCAYMAM.guardarPantalla();
        } else {
            if (tabBienes.guardar()) {
                conCAYMAM.guardarPantalla();
            }
            activos.setUpdateFallecido(Integer.parseInt(tabBienes.getValor("numero_acta")), "ACFINM");
        }
    }

    @Override
    public void eliminar() {
        activos.setUpdateActa(Integer.parseInt(tabBienes.getValor("id_inmueble")));
        utilitario.agregarMensaje("Acta Anulada", "");
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Acta Inmuebles":
                mostrarReporte();
                break;
        }
    }

    public void mostrarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Acta Inmuebles":
                    p_parametros.put("acta", Integer.parseInt(tabBienes.getValor("id_inmueble")));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();

                break;
        }
    }
    
    public Conexion getConCAYMAM() {
        return conCAYMAM;
    }

    public void setConCAYMAM(Conexion conCAYMAM) {
        this.conCAYMAM = conCAYMAM;
    }

    public Tabla getTabBienes() {
        return tabBienes;
    }

    public void setTabBienes(Tabla tabBienes) {
        this.tabBienes = tabBienes;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
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

    public SeleccionTabla getSetDatos() {
        return setDatos;
    }

    public void setSetDatos(SeleccionTabla setDatos) {
        this.setDatos = setDatos;
    }
    
}

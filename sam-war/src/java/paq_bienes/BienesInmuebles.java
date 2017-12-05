/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bienes;

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
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.BeanConstatacion;
import paq_utilitario.ejb.ClaseGenerica;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class BienesInmuebles extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private Panel panBotones = new Panel();
    private Combo cmbActa = new Combo();
    private Tabla tabOrden = new Tabla();
    private List lisCatastro = new ArrayList();
    private List lisBienes = new ArrayList();
    private List lisFinal = new ArrayList();
    private AutoCompletar autBusca = new AutoCompletar();
    private Dialogo diaDialogo = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridD = new Grid();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private ClaseGenerica generico = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);
    private BeanConstatacion lista = (BeanConstatacion) utilitario.instanciarEJB(BeanConstatacion.class);

    public BienesInmuebles() {
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("SELECT id_inmueble,numero_acta,fechad,clave FROM cust_bien_inmuebles where anio =" + utilitario.getAnio(utilitario.getFechaActual()));
        autBusca.setSize(70);
        autBusca.setMetodoChange("buscarActa");
        bar_botones.agregarComponente(new Etiqueta("Acta : "));
        bar_botones.agregarComponente(autBusca);

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("ACTA DE CONSTATACIÓN FISICA INMUEBLES");
        agregarComponente(tabp2);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setStyle("font-size:12px;color:black;text-align:left;");

        Grid griBotones = new Grid();
        griBotones.setColumns(3);
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

        panBotones.setId("panBotones");
        panBotones.setTransient(true);
        panBotones.setStyle("font-size:12px;color:black;text-align:center;");
        panBotones.getChildren().add(griBotones);

        agregarComponente(panOpcion);
        agregarComponente(panBotones);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Seleccione Acta"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("mostrarReporte");
        grid.setColumns(4);
        agregarComponente(diaDialogo);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabOrden.setId("tabOrden");
        tabOrden.setConexion(conPostgres);
        tabOrden.setTabla("cust_bien_inmuebles", "id_inmueble", 1);
        if (autBusca.getValue() == null) {
            tabOrden.setCondicion("id_inmueble=-1");
        } else {
            tabOrden.setCondicion("id_inmueble=" + autBusca.getValor());
        }
        tabOrden.getColumna("clave").setCombo(listado());
        tabOrden.getColumna("delegado_avaluos").setCombo("select cod_empleado,nombres from srh_empleado where estado=1 order by nombres");
        tabOrden.getColumna("delegado_financiero").setCombo("select cod_empleado,nombres from srh_empleado where estado=1 order by nombres");
        tabOrden.getColumna("clave").setMetodoChange("datos");
        tabOrden.getColumna("id_inmueble").setVisible(false);
        tabOrden.getColumna("valor_avaluo").setVisible(false);
        tabOrden.getColumna("construccion_avaluo").setVisible(false);
        tabOrden.getColumna("area_avaluo").setVisible(false);
        tabOrden.getColumna("codigoend").setVisible(false);
        tabOrden.getColumna("anio").setVisible(false);
        tabOrden.getColumna("cuenta").setVisible(false);
        tabOrden.getColumna("catastro_baja").setVisible(false);
        tabOrden.getColumna("canton_notario").setVisible(false);
        tabOrden.getColumna("fecha_notaria").setVisible(false);
        tabOrden.getColumna("responsable_bienes").setVisible(false);
        tabOrden.getColumna("login_registro").setVisible(false);
        tabOrden.getColumna("ip_registro").setVisible(false);
        tabOrden.getColumna("responsable_administrativo").setVisible(false);
        tabOrden.getColumna("ip_registro").setValorDefecto(utilitario.getIp());
        tabOrden.getColumna("login_registro").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabOrden.getColumna("fechad").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("anio").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
        tabOrden.setTipoFormulario(true);
        tabOrden.getGrid().setColumns(2);
        tabOrden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabOrden);
        Grupo gru = new Grupo();
        gru.getChildren().add(pto);
        panOpcion.getChildren().add(gru);
    }

    public List listado() {
        lisCatastro.clear();
//        TablaGenerica tabCatastro = utilitario.consultar("select clave,clave as catastro,barrio,propietario \n"
//                + "from prediosv \n"
//                + "where propietario like 'IGLESIA CRISTIANA COMUNIDAD DE FE%' or propietario like 'VIVAS MORALES PEDRO ANTONIO%' \n"
//                + "or propietario like 'BANO GAVILANES JEANNETH DEL ROCIO%' or propietario like 'municipio%' or propietario like 'MINISTERIO%'\n"
//                + "or propietario like 'jijon%' or propietario like 'GOBIERNO%' or propietario like 'SANGOL%'");
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

    public void datos() {
        TablaGenerica tabDato = lista.getCatastro(tabOrden.getValor("clave"));
        if (!tabDato.isEmpty()) {
            TablaGenerica tabDatos = lista.getBienes(tabOrden.getValor("clave"));
            if (!tabDatos.isEmpty()) {
                tabOrden.setValor("propietario", tabDato.getValor("propietario"));
                tabOrden.setValor("parroquia", tabDato.getValor("parroquia"));
                tabOrden.setValor("barrio", tabDato.getValor("barrio"));
                tabOrden.setValor("calles", tabDato.getValor("calles"));
                tabOrden.setValor("predio", tabDato.getValor("predio"));
                tabOrden.setValor("area_terreno", tabDato.getValor("metros"));
                tabOrden.setValor("area_cons", tabDato.getValor("construccion"));
                tabOrden.setValor("canton_notario", tabDato.getValor("canton"));
                tabOrden.setValor("fecha_notaria", tabDato.getValor("registro"));
                tabOrden.setValor("val_catastro", tabDato.getValor("valor"));

                tabOrden.setValor("codigo", tabDatos.getValor("codigo"));
                tabOrden.setValor("val_bienes", tabDatos.getValor("val_compra"));
                tabOrden.setValor("estado", tabDatos.getValor("propiedad"));
                tabOrden.setValor("ubicacion", tabDatos.getValor("ubicacion"));
                tabOrden.setValor("cuenta", tabDatos.getValor("cuenta"));

                tabOrden.setValor("responsable_administrativo", lista.getAdministrativo());
                tabOrden.setValor("responsable_bienes", lista.getBienes());
                utilitario.addUpdate("tabOrden");
            } else {
                tabOrden.setValor("propietario", tabDato.getValor("propietario"));
                tabOrden.setValor("parroquia", tabDato.getValor("parroquia"));
                tabOrden.setValor("barrio", tabDato.getValor("barrio"));
                tabOrden.setValor("calles", tabDato.getValor("calles"));
                tabOrden.setValor("predio", tabDato.getValor("predio"));
                tabOrden.setValor("area_terreno", tabDato.getValor("metros"));
                tabOrden.setValor("area_cons", tabDato.getValor("construccion"));
                tabOrden.setValor("canton_notario", tabDato.getValor("canton"));
                tabOrden.setValor("fecha_notaria", tabDato.getValor("registro"));
                tabOrden.setValor("val_catastro", tabDato.getValor("valor"));
                tabOrden.setValor("responsable_administrativo", lista.getAdministrativo());
                tabOrden.setValor("responsable_bienes", lista.getBienes());
                utilitario.addUpdate("tabOrden");
            }
        } else {
            utilitario.agregarMensajeError("No encuentra datos en Avaluo", null);
        }
    }

    public void buscarActa(SelectEvent evt) {
        limpiar();
        autBusca.onSelect(evt);
        dibujarPantalla();
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
        numero();
    }

    @Override
    public void guardar() {
        if (tabOrden.getValor("id_inmueble") != null) {
            TablaGenerica tabInfo = generico.getCatalogoTabla("*", tabOrden.getTabla(), "id_inmueble = " + tabOrden.getValor("id_inmueble") + "");
            if (!tabInfo.isEmpty()) {
                TablaGenerica tabDato = generico.getNumeroCampos(tabOrden.getTabla());
                if (!tabDato.isEmpty()) {
                    for (int i = 1; i < Integer.parseInt(tabDato.getValor("NumeroCampos")); i++) {
                        if (i != 1) {
                            TablaGenerica tabInfoColum1 = generico.getEstrucTabla(tabOrden.getTabla(), i);
                            if (!tabInfoColum1.isEmpty()) {
                                try {
                                    if (tabOrden.getValor(tabInfoColum1.getValor("Column_Name")).equals(tabInfo.getValor(tabInfoColum1.getValor("Column_Name")))) {
                                    } else {
                                        generico.setActuaRegistro(Integer.parseInt(tabOrden.getValor("id_inmueble")), tabOrden.getTabla(), tabInfoColum1.getValor("Column_Name"), tabOrden.getValor(tabInfoColum1.getValor("Column_Name")), "id_inmueble");
                                    }
                                } catch (NullPointerException e) {
                                }
                            }
                        }
                    }
                }
            }
            utilitario.agregarMensaje("Registro Actalizado", null);
        } else {
            if (tabOrden.guardar()) {
                conPostgres.guardarPantalla();
            }
        }
    }

    public void numero() {
        String numero = lista.maxComprobantes(utilitario.getAnio(utilitario.getFechaActual()));
        String num;
        Integer cantidad = 0;
        cantidad = Integer.parseInt(numero) + 1;
        if (numero != null) {
            if (cantidad >= 0 && cantidad <= 9) {
                num = "00" + String.valueOf(cantidad);
                tabOrden.setValor("numero_acta", num);
            } else if (cantidad >= 10 && cantidad <= 99) {
                num = "0" + String.valueOf(cantidad);
                tabOrden.setValor("numero_acta", num);
            } else if (cantidad >= 100 && cantidad <= 999) {
                num = String.valueOf(cantidad);
                tabOrden.setValor("numero_acta", num);
            }
        }
        utilitario.addUpdate("tabOrden");
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
            case "Acta Inmuebles":
                diaDialogo.Limpiar();
                Grid griActa = new Grid();
                griActa.setColumns(2);
                griActa.getChildren().add(new Etiqueta("ACTAS: "));
                cmbActa.setId("cmbActa");
                cmbActa.setConexion(conPostgres);
                cmbActa.setCombo("SELECT id_inmueble,numero_acta,clave FROM cust_bien_inmuebles where anio=" + utilitario.getAnio(utilitario.getFechaActual()) + " ORDER BY id_inmueble DESC");
                griActa.getChildren().add(cmbActa);
                diaDialogo.setDialogo(grid);
                gridD.getChildren().add(griActa);
                diaDialogo.setDialogo(gridD);
                diaDialogo.dibujar();
                break;
        }
    }

    public void mostrarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Acta Inmuebles":
                TablaGenerica tabDato = lista.getActas(Integer.parseInt(cmbActa.getValue() + ""));
                if (!tabDato.isEmpty()) {
                    p_parametros.put("clave", tabDato.getValor("clave"));
                    p_parametros.put("acta", tabDato.getValor("numero_acta"));
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensajeError(null, "No Disponible");
                }
                break;
        }
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabOrden() {
        return tabOrden;
    }

    public void setTabOrden(Tabla tabOrden) {
        this.tabOrden = tabOrden;
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

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}

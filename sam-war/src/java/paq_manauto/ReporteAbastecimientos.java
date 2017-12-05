/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_manauto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_manauto.ejb.manauto;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ReporteAbastecimientos extends Pantalla {
//Conexion a base

    private Conexion conPostgres = new Conexion();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //Combos de Selección
    private Combo cmbAno = new Combo();
    private Combo cmbano = new Combo();
    private Combo cmbPeriodo = new Combo();
    private Combo cmbPeri = new Combo();
    private Combo cmbPlaca = new Combo();
    private Combo cmbGeneral = new Combo();
    //Dialogos
    private Dialogo diaGeneral = new Dialogo();
    private Dialogo diaIndividual = new Dialogo();
    private Grid gridg = new Grid();
    private Grid gridin = new Grid();
    //Etiquetas
    private Etiqueta etiAno = new Etiqueta("Año : ");
    private Etiqueta etiano = new Etiqueta("Año : ");
    private Etiqueta etiPeriodo = new Etiqueta("Periodo : ");
    private Etiqueta etiPeri = new Etiqueta("Periodo : ");
    private Etiqueta etiTipo = new Etiqueta("Tipo : ");
    private Etiqueta etiPlaca = new Etiqueta("Placa : ");
    @EJB
    private manauto admin = (manauto) utilitario.instanciarEJB(manauto.class);

    public ReporteAbastecimientos() {

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();
        //cadena de conexión para base de datos en postgres/produccion2014
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        panOpcion.setId("pan_opcion");
        panOpcion.setTransient(true);

        Grid grid_pant = new Grid();
        grid_pant.setColumns(1);
        grid_pant.setStyle("text-align:center;position:absolute;top:270px;left:400px;");
        Etiqueta eti_encab = new Etiqueta();
        eti_encab.setStyle("font-size:22px;color:blue;text-align:center;");
        eti_encab.setValue("REPORTES COMSUMO DE AUTOMOTORES,MAQUINARIAS Y OTROS");
        grid_pant.getChildren().add(eti_encab);
        Boton bot_lista = new Boton();
        bot_lista.setValue("LISTA DE REPORTES");
        bot_lista.setMetodo("abrirListaReportes");
        grid_pant.getChildren().add(bot_lista);
        agregarComponente(grid_pant);
        panOpcion.getChildren().add(grid_pant);
        agregarComponente(panOpcion);

        //Configuración de Objeto Reporte
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);

        Grid gri_search = new Grid();
        gri_search.setColumns(2);

        gri_search.getChildren().add(etiAno);
        cmbAno.setId("cmbAno");
        cmbAno.setConexion(conPostgres);
        cmbAno.setCombo("select ano_curso, ano_curso as anio from conc_ano \n"
                + "where ano_curso >= (cast(to_char(now(), 'YYYY') as INTEGER)-2)\n"
                + "order by ano_curso desc");
        gri_search.getChildren().add(cmbAno);

//        gri_search.getChildren().add(etiPeri);
        cmbPeri.setId("cmbPeri");
        cmbPeri.setConexion(conPostgres);
        cmbPeri.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
//        gri_search.getChildren().add(cmbPeri);

        gri_search.getChildren().add(etiTipo);
        List lista = new ArrayList();
        Object filase1[] = {
            "Maquinaria", "Maquinaria"
        };
        Object filase2[] = {
            "Otros", "Otros"
        };
        Object filase3[] = {
            "Vehiculo", "Vehiculo"
        };
        lista.add(filase1);;
        lista.add(filase2);;
        lista.add(filase3);;
        cmbGeneral.setCombo(lista);
        gri_search.getChildren().add(cmbGeneral);

        //para poder busca por apelllido el garante
        diaGeneral.setId("diaGeneral");
        diaGeneral.setTitle("PARAMETROS PARA REPORTE GENERAL"); //titulo
        diaGeneral.setWidth("30%"); //siempre en porcentajes  ancho
        diaGeneral.setHeight("25%");//siempre porcentaje   alto
        diaGeneral.setResizable(false); //para que no se pueda cambiar el tamaño
        diaGeneral.getGri_cuerpo().setHeader(gri_search);
        diaGeneral.getBot_aceptar().setMetodo("aceptarDialogo");
        gridg.setColumns(4);
        agregarComponente(diaGeneral);


        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);

        gri_busca.getChildren().add(etiano);
        cmbano.setId("cmbano");
        cmbano.setConexion(conPostgres);
        cmbano.setCombo("select ano_curso, ano_curso as anio from conc_ano \n"
                + "where ano_curso >= (cast(to_char(now(), 'YYYY') as INTEGER)-2)\n"
                + "order by ano_curso desc");
        gri_busca.getChildren().add(cmbano);

//        gri_busca.getChildren().add(etiPeriodo);
//        cmbPeriodo.setId("cmbPeriodo");
//        cmbPeriodo.setConexion(conPostgres);
//        cmbPeriodo.setCombo("SELECT ide_periodo,per_descripcion FROM cont_periodo_actual ORDER BY ide_periodo");
//        gri_busca.getChildren().add(cmbPeriodo);

        gri_busca.getChildren().add(etiPlaca);
        cmbPlaca.setId("cmbPlaca");
        cmbPlaca.setConexion(conPostgres);
        cmbPlaca.setCombo("select mve_secuencial, placa from vehiculo_vt order by placa");
        gri_busca.getChildren().add(cmbPlaca);

        //para poder busca por apelllido el garante
        diaIndividual.setId("diaIndividual");
        diaIndividual.setTitle("PARAMETROS PARA REPORTE INDIVIDUAL"); //titulo
        diaIndividual.setWidth("30%"); //siempre en porcentajes  ancho
        diaIndividual.setHeight("25%");//siempre porcentaje   alto
        diaIndividual.setResizable(false); //para que no se pueda cambiar el tamaño
        diaIndividual.getGri_cuerpo().setHeader(gri_busca);
        diaIndividual.getBot_aceptar().setMetodo("aceptarDialogo");
        gridin.setColumns(4);
        agregarComponente(diaIndividual);
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "REPORTE GENERAL ABASTECIMIENTO":
                diaGeneral.Limpiar();
                diaGeneral.dibujar();
                break;
            case "REPORTE INDIVIDUAL ABASTECIMIENTO":
                diaIndividual.Limpiar();
                diaIndividual.dibujar();
                break;
        }
    }

    public void aceptarDialogo() {
        switch (rep_reporte.getNombre()) {
            case "REPORTE GENERAL ABASTECIMIENTO":
                TablaGenerica tabData = admin.getAbastecimien(Integer.parseInt(cmbAno.getValue() + ""),cmbGeneral.getValue()+"");
                if (!tabData.isEmpty()) {
                    admin.setDeleteTemp();
                    for (int i = 0; i < tabData.getTotalFilas(); i++) {
                        admin.setInsertTemp(i, Integer.parseInt(tabData.getValor(i, "mve_secuencial")), tabData.getValor(i, "abastecimiento_numero_vale")
                                , tabData.getValor(i, "abastecimiento_fecha"), tabData.getValor(i, "conductor"), tabData.getValor(i, " abastecimiento_kilometraje")
                                , tabData.getValor(i, "abastecimiento_galones").toString(), Double.parseDouble(tabData.getValor(i, "abastecimiento_total")), Integer.parseInt(tabData.getValor(i, "anio")), Integer.parseInt(tabData.getValor(i, "mes"))
                                , tabData.getValor(i, "combustible"), tabData.getValor(i, "abastecimiento_tipo_ingreso"), tabData.getValor(i, "abastecimiento_horasmes"), tabData.getValor(i, "abastecimiento_comentario")
                                , tabData.getValor(i, "dependencia"), tabData.getValor(i, "abastecimiento_valorhora"));
                    }
                    p_parametros.put("parameter1", cmbGeneral.getValue() + "");
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    System.err.println("parametros"+p_parametros);
                    System.err.println("reporte"+rep_reporte.getPath());
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensajeError("Informacion", "No Disponible");
                }
                break;
            case "REPORTE INDIVIDUAL ABASTECIMIENTO":
                TablaGenerica tabDato = admin.getAbastecimientos(Integer.parseInt(cmbano.getValue() + ""), Integer.parseInt(cmbPlaca.getValue() + ""));
                if (!tabDato.isEmpty()) {
                    admin.setDeleteTemp();
                    for (int i = 0; i < tabDato.getTotalFilas(); i++) {
                        admin.setInsertTemp(i, Integer.parseInt(tabDato.getValor(i, "mve_secuencial")), tabDato.getValor(i, "abastecimiento_numero_vale"), tabDato.getValor(i, "abastecimiento_fecha"), tabDato.getValor(i, "conductor"), tabDato.getValor(i, " abastecimiento_kilometraje"), tabDato.getValor(i, "abastecimiento_galones"), Double.parseDouble(tabDato.getValor(i, "abastecimiento_total")), Integer.parseInt(tabDato.getValor(i, "anio")), Integer.parseInt(tabDato.getValor(i, "mes")), tabDato.getValor(i, "combustible"), tabDato.getValor(i, "abastecimiento_tipo_ingreso"), tabDato.getValor(i, "abastecimiento_horasmes"), tabDato.getValor(i, "abastecimiento_comentario"), tabDato.getValor(i, "dependencia"), tabDato.getValor(i, "abastecimiento_valorhora"));
                    }
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensajeError("Informacion", "No Disponible");
                }
                break;
        }
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

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_asig_placas;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class pre_placas_disponibles extends Pantalla {

    //declaracion de tablas
    private Tabla tab_placas = new Tabla();
    private Tabla tab_consulta = new Tabla();
    //combos de seleccion
    private Combo cmb_vehiculo = new Combo();
    private Combo cmb_servicio = new Combo();
    private Combo cmb_tipo = new Combo();
    private Combo cmb_anio = new Combo();
    private Combo cmb_ani = new Combo();
    private Dialogo dia_dialogoA = new Dialogo();
    private Dialogo dia_dialogoU = new Dialogo();
    private Grid grida = new Grid();
    private Grid gridu = new Grid();
    private Grid grid_u = new Grid();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public pre_placas_disponibles() {

        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        bar_botones.agregarComponente(new Etiqueta("Vehiculo :"));
        bar_botones.agregarComponente(cmb_vehiculo);
        bar_botones.agregarSeparador();

        bar_botones.agregarComponente(new Etiqueta("Servicio :"));
        bar_botones.agregarComponente(cmb_servicio);
        bar_botones.agregarSeparador();

        bar_botones.agregarComponente(new Etiqueta("Tipo Placa :"));
        bar_botones.agregarComponente(cmb_tipo);
        bar_botones.agregarSeparador();

        cmb_vehiculo.setId("cmb_vehiculo");
        cmb_vehiculo.setCombo("SELECT IDE_TIPO_VEHICULO, DESCRIPCION_VEHICULO FROM TRANS_VEHICULO_TIPO");
        cmb_vehiculo.setMetodo("servicio");

        cmb_servicio.setId("cmb_servicio");
        cmb_servicio.setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO");

        cmb_tipo.setId("cmb_tipo");
        cmb_tipo.setCombo("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA");

        Boton bot_busca = new Boton();
        bot_busca.setValue("MOSTRAR");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("Actualizarlista");
        bar_botones.agregarBoton(bot_busca);

        tab_placas.setId("tab_placas");
        tab_placas.setTabla("TRANS_PLACAS", "IDE_PLACA", 1);
        tab_placas.getColumna("IDE_TIPO_PLACA").setCombo("SELECT IDE_TIPO_PLACA,DESCRIPCION_PLACA FROM TRANS_TIPO_PLACA");
        tab_placas.getColumna("IDE_TIPO_VEHICULO").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_SERVICIO").setVisible(false);
        tab_placas.getColumna("IDE_INGRESO_PLACAS").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_ESTADO").setVisible(false);
        tab_placas.getColumna("CEDULA_RUC_PROPIETARIO").setVisible(false);
        tab_placas.getColumna("FECHA_REGISTRO_PLACA").setVisible(false);
        tab_placas.getColumna("NOMBRE_PROPIETARIO").setVisible(false);
        tab_placas.getColumna("FECHA_ENTREGA_PLACA").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_PLACA2").setVisible(false);
        tab_placas.getColumna("IDE_TIPO_ESTADO2").setVisible(false);
        tab_placas.getColumna("FECHA_DEFINITIVA_PLACA").setVisible(false);
        tab_placas.getColumna("USU_ENTREGA").setVisible(false);
        tab_placas.getColumna("FECHA_ENTREGA_FINAL").setVisible(false);
        tab_placas.setHeader("Inventario de Placas Disponibles");
        tab_placas.setLectura(true);
        tab_placas.setRows(30);
        tab_placas.dibujar();
        PanelTabla tbp_d = new PanelTabla();
        tbp_d.setPanelTabla(tab_placas);
        Grupo gru = new Grupo();
        gru.getChildren().add(tbp_d);
        agregarComponente(gru);

        cmb_anio.setId("cmb_anio");
        cmb_anio.setCombo("SELECT ano_curso as id,ano_curso FROM conc_ano order by ano_curso desc");

        //CONFIGURACION DE DIALOGO SELECCION DE GESTOR
        dia_dialogoA.setId("dia_dialogoA");
        dia_dialogoA.setTitle("Reporte por año"); //titulo
        dia_dialogoA.setWidth("20%"); //siempre en porcentajes  ancho
        dia_dialogoA.setHeight("20%");//siempre porcentaje   alto
        dia_dialogoA.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoA.getBot_aceptar().setMetodo("aceptoDialogo");
        grida.setColumns(4);
        agregarComponente(dia_dialogoA);
        //CONFIGURACIÓN DE OBJETO REPORTE
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }

    public void servicio() {
        cmb_servicio.setCombo("SELECT IDE_TIPO_SERVICIO,DESCRIPCION_SERVICIO FROM TRANS_TIPO_SERVICIO where IDE_TIPO_VEHICULO= " + cmb_vehiculo.getValue());
        utilitario.addUpdate("cmb_servicio");
    }

    public void Actualizarlista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tab_placas.setCondicion(getFiltrosAcceso());
            tab_placas.ejecutarSql();
            utilitario.addUpdate("tab_placas");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (cmb_vehiculo.getValue() != null
                && cmb_servicio.getValue() != null) {

            str_filtros = " IDE_TIPO_VEHICULO = "
                    + cmb_vehiculo.getValue();
            str_filtros += " AND IDE_TIPO_SERVICIO = "
                    + cmb_servicio.getValue();
            str_filtros += " AND IDE_TIPO_ESTADO = 2";
            str_filtros += " AND IDE_TIPO_PLACA = "
                    + cmb_tipo.getValue();

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe ingresar los fitros de vehiculo y servicio");
        }
        return str_filtros;
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Total Entregadas":
                dia_dialogoA.Limpiar();
                grida.getChildren().add(new Etiqueta("Año :"));
                grida.getChildren().add(cmb_anio);
                dia_dialogoA.setDialogo(grida);
                dia_dialogoA.dibujar();
                break;
            case "Total Ingresadas":
                aceptoDialogo();
                break;
        }
    }

    public void aceptoDialogo() {
        switch (rep_reporte.getNombre()) {
            case "Total Entregadas":
                p_parametros = new HashMap();
                p_parametros.put("anio", Integer.parseInt(cmb_anio.getValue()+""));
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "Total Ingresadas":
                p_parametros = new HashMap();
                p_parametros.put("nom_resp", tab_consulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
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

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_placas() {
        return tab_placas;
    }

    public void setTab_placas(Tabla tab_placas) {
        this.tab_placas = tab_placas;
    }
}

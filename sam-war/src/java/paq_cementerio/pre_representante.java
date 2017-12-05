package paq_cementerio;

/**
 *
 * @author l-suntaxi
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_registros.ejb.ServicioRegistros;

/**
 *
 * @author Diego
 */
public class pre_representante extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private AutoCompletar aut_busca = new AutoCompletar();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private ServicioRegistros servicioregistros = (ServicioRegistros) utilitario.instanciarEJB(ServicioRegistros.class);

    public pre_representante() {

        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_FALLECIDO,CEDULA_FALLECIDO,NOMBRES from CMT_FALLECIDO");
        aut_busca.setMetodoChange("buscarMovimiento");
        aut_busca.setSize(80);
        bar_botones.agregarComponente(new Etiqueta("Buscar:"));
        bar_botones.agregarComponente(aut_busca);


        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 1);
        tab_tabla1.setHeader("DATOS DEL FALLECIDO");
        tab_tabla1.getColumna("CEDULA_FALLECIDO").setMetodoChange("buscaPersona");

        tab_tabla1.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_tabla1);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(tabp);
        agregarComponente(div_division);

        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }

    public void buscarMovimiento(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_tabla1.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_tabla1");
        }
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

//    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO FALLECIDO":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO FALLECIDO":
//              p_parametros.put("nom_resp", tab_tabla2.getValor("NICK_USUA") + "");
//                p_parametros.put("IDE_MOV", tab_tabla2.getValor("IDE_MOVIMIENTO") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    //BUSQUEDA DE PERSONAS EN BASE DE DATOS DEL MUNICIPIO
    public void buscaPersona() {
        if (utilitario.validarCedula(tab_tabla1.getValor("CEDULA_FALLECIDO"))) {
            TablaGenerica tab_dato = servicioregistros.getPersona(tab_tabla1.getValor("CEDULA_FALLECIDO"));
            if (!tab_dato.isEmpty()) {
                // Cargo la información de la base de datos maestra   
                tab_tabla1.setValor("NOMBRES", tab_dato.getValor("nombre"));
                tab_tabla1.setValor("IDE_CMGEN", tab_dato.getValor("sexo"));
                tab_tabla1.setValor("FECHA_NACIMIENTO", tab_dato.getValor("fecha"));
                utilitario.addUpdate("tab_tabla1");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeInfo("El Número de Cédula ingresado no es correcto", "");
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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
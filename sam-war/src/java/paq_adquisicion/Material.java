package paq_adquisicion;

/**
 *
 * @author Andres
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import static paq_adquisicion.AdquisicionesBodega.par_anulado;
import static paq_adquisicion.AdquisicionesBodega.par_aprueba_gasto;
import static paq_adquisicion.AdquisicionesBodega.par_aprueba_solicitud;
import static paq_adquisicion.AdquisicionesBodega.par_tipo_bodeguero;
import static paq_adquisicion.AdquisicionesBodega.par_tipo_secretaria;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class Material extends Pantalla {

    private Tabla tab_material = new Tabla();
    private Tabla tab_partida_material = new Tabla();
    private Dialogo dia_ingreso_material = new Dialogo();
    private Grid grid_ing_mat = new Grid();
    private Grid gridim = new Grid();
    private AreaTexto txt_material = new AreaTexto();
    private Combo cmb_tipo = new Combo();
    private Etiqueta eti_material = new Etiqueta("DESCRIPCIÓN DE MATERIAL : ");
    private Etiqueta eti_tipo = new Etiqueta("TIPO BIEN : ");

    public static String par_tipo_secretaria="";
    public static String par_tipo_bodeguero="";
    public static String par_aprueba_gasto="";
    public static String par_aprueba_solicitud="";
    public static String par_anulado="";
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);

    public Material() {
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonGuardar();

         par_tipo_secretaria=utilitario.getVariable("p_tipo_secretaria");
         par_tipo_bodeguero=utilitario.getVariable("p_tipo_bodeguero");
         par_aprueba_gasto=utilitario.getVariable("p_tipo_generador_gasto");
         par_aprueba_solicitud=utilitario.getVariable("p_tipo_aprueba_solicitud");
         par_anulado=utilitario.getVariable("p_tipo_anulado");
        
         if (tienePerfilSecretaria()) {     
        Boton bot_aprobar = new Boton();
        bot_aprobar.setIcon("ui-icon-document");
        bot_aprobar.setValue("INGRESO DE NUEVOS");
        bot_aprobar.setMetodo("ingresoMateriales");
        bar_botones.agregarBoton(bot_aprobar);

        List lista = new ArrayList();
        Object fila1[] = {"2", "ACTIVO FIJO"};
        Object fila2[] = {"1", "STOCK EN BODEGA"};
        lista.add(fila1);
        lista.add(fila2);

        cmb_tipo.setId("cmb_tipo");
        cmb_tipo.setCombo(lista);
        cmb_tipo.setMetodo("buscaCodigo");
        
        dia_ingreso_material.setId("dia_ingreso_material");
        dia_ingreso_material.setTitle("INGRESO DE MATERIALES"); //titulo
        dia_ingreso_material.setWidth("40%"); //siempre en porcentajes  ancho
        dia_ingreso_material.setHeight("50%");//siempre porcentaje   alto
        dia_ingreso_material.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_ingreso_material.getBot_aceptar().setMetodo("guardarRegistro");
        grid_ing_mat.setColumns(4);
        agregarComponente(dia_ingreso_material);

        tab_material.setId("tab_material");   //identificador
        tab_material.setTabla("adq_material", "ide_admate", 1);
        tab_material.getColumna("IDE_ADGRMA").setCombo(ser_adquisiciones.getGrupoMaterial());
        tab_material.agregarRelacion(tab_partida_material);
        tab_material.getColumna("IDE_ADMATE").setNombreVisual("CODIGO");
        tab_material.getColumna("IDE_ADGRMA").setNombreVisual("CODIGO GRUPO MATERIAL");
        tab_material.getColumna("DETALLE_ADMATE").setNombreVisual("DETALLE");
        tab_material.getColumna("DETALLE_ADMATE").setFiltro(true);
        tab_material.getColumna("CODIGO_ADMATE").setNombreVisual("CODIGO MATERIAL");
        tab_material.getColumna("APLICA_COD_PRESU_ADMATE").setNombreVisual("APLICA CODIGO PRESUPUESTARIO");

        tab_material.getColumna("tipo_bien_admate").setCombo(lista);
        tab_material.dibujar();

        PanelTabla pat_material = new PanelTabla();
        pat_material.setId("pat_material");
        pat_material.setPanelTabla(tab_material);

        tab_partida_material.setId("tab_partida_material");   //identificador
        tab_partida_material.setTabla("adq_partida_material", "ide_adpama", 2);
        tab_partida_material.getColumna("IDE_ADARPA").setCombo(ser_adquisiciones.getAreaPartida());
        tab_partida_material.getColumna("IDE_ADPAMA").setNombreVisual("CODIGO");
        tab_partida_material.getColumna("IDE_ADARPA").setNombreVisual("AREA PARTIDA");
        tab_partida_material.getColumna("CODIGO_ADPAMA").setNombreVisual("CODIGO PARTIDA MATERIAL");

        tab_partida_material.dibujar();

        PanelTabla pat_partida_material = new PanelTabla();
        pat_partida_material.setId("pat_partida_material");
        pat_partida_material.setPanelTabla(tab_partida_material);

        Division div_material = new Division();
        div_material.setId("div_material");
        div_material.dividir2(pat_material, pat_partida_material, "60%", "H");
        agregarComponente(div_material);
        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el registro de Solicitudes de Compra. Consulte con el Administrador");
        }    
    }

    String empleado="";
     String cedula="";
     String ide_ademple="";
private boolean tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioSistema(utilitario.getVariable("IDE_USUA"), "1",par_tipo_bodeguero));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);
           
                empleado = fila[1].toString() ;
                cedula = fila[2].toString();
                ide_ademple = fila[0].toString();
                return true;
           
        } else {
            return false;
        }
    }
    public void ingresoMateriales() {
        dia_ingreso_material.Limpiar();
        Grid gri_modelos = new Grid();
        gri_modelos.setColumns(2);
        gri_modelos.getChildren().add(eti_tipo);
        gri_modelos.getChildren().add(cmb_tipo);
        gri_modelos.getChildren().add(eti_material);
        txt_material.setCols(50);
        gri_modelos.getChildren().add(txt_material);
        dia_ingreso_material.setDialogo(gridim);
        grid_ing_mat.getChildren().add(gri_modelos);
        dia_ingreso_material.setDialogo(grid_ing_mat);
        dia_ingreso_material.dibujar();
    }
    
    public void guardarRegistro(){
         ser_adquisiciones.setUpdateInsertMaterial("adq_material",txt_material.getValue()+"",Integer.parseInt(cmb_tipo.getValue()+""),utilitario.getVariable("NICK"));
        utilitario.agregarMensajeInfo("Resgistro Guardado", "");
        dia_ingreso_material.cerrar();
        utilitario.addUpdate("tab_material");
    }
    
    @Override
    public void insertar() {
        if (tab_material.isFocus()) {
            tab_material.insertar();
        } else if (tab_partida_material.isFocus()) {
            tab_partida_material.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_material.isFocus()) {
            tab_material.guardar();
        } else if (tab_partida_material.isFocus()) {
            tab_partida_material.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_material.isFocus()) {
            tab_material.eliminar();
        } else if (tab_partida_material.isFocus()) {
            tab_partida_material.eliminar();
        }
    }

    public Tabla getTab_material() {
        return tab_material;
    }

    public void setTab_material(Tabla tab_material) {
        this.tab_material = tab_material;
    }

    public Tabla getTab_partida_material() {
        return tab_partida_material;
    }

    public void setTab_partida_material(Tabla tab_partida_material) {
        this.tab_partida_material = tab_partida_material;
    }

}

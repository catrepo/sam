/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class LiquidacionMultiple extends Pantalla {
    
    private Tabla tabConsulta = new Tabla();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabCatastro = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabRepresentante = new Tabla();
    private Combo cmbOpcion = new Combo();
    private SeleccionTabla setFallecido = new SeleccionTabla();
    private AutoCompletar autBusca = new AutoCompletar();
    private Texto texBusqueda = new Texto();
    private Panel panOpcion = new Panel();
    private Texto txtCedula = new Texto();
    private Texto txtNombre = new Texto();
    private Texto txtDireccion = new Texto();
    private Texto txtTelefono = new Texto();
    private Texto txtCorreo = new Texto();
    private Texto txtIdRep = new Texto();
    private Etiqueta etiCedula = new Etiqueta("<br>Cédula : </br>");
    private Etiqueta etiNombre = new Etiqueta("<br>Nombres : </br>");
    private Etiqueta etiDireccion = new Etiqueta("<br>Dirección : </br>");
    private Etiqueta etiTelefono = new Etiqueta("<br>Teléfono : </br>");
    private Etiqueta etiCorreo = new Etiqueta("<br>Correo : </br>");
    @EJB
    private cementerio admin = (cementerio) utilitario.instanciarEJB(cementerio.class);
    
    public LiquidacionMultiple() {
        
        Boton botBusca = new Boton();
        botBusca.setValue("Buscar Representante");
        botBusca.setExcluirLectura(true);
        botBusca.setIcon("ui-icon-search");
        botBusca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botBusca);
        
        Boton botSeleccion = new Boton();
        botSeleccion.setValue("Check");
        botSeleccion.setIcon("ui-icon-check");
        botSeleccion.setMetodo("checkAll1");
        bar_botones.agregarBoton(botSeleccion);
        
        Boton botQuitar = new Boton();
        botQuitar.setValue("Un-Check");
        botQuitar.setIcon("ui-icon-cancel");
        botQuitar.setMetodo("checkAll2");
        bar_botones.agregarBoton(botQuitar);
        
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();
        
        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT IDE_FALLECIDO,FECHA_INGRE,liquidacion,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,CATASTRO,FECHA_DESDE,FECHA_HASTA\n"
                + "FROM CMT_FALLECIDO_RENOVACION");
        autBusca.setMetodoChange("buscarPersona");
        autBusca.setSize(70);
        
        Grid griBuscar = new Grid();
        griBuscar.setColumns(1);
        
        List list = new ArrayList();
        Object fila1[] = {
            "0", "Nombres Representante"
        };
        Object fila2[] = {
            "1", "Cédula Representante"
        };
        list.add(fila1);;
        list.add(fila2);;
        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();
        
        Boton botRegistro = new Boton();
        botRegistro.setValue("Buscar");
        botRegistro.setIcon("ui-icon-search");
        botRegistro.setMetodo("busquedaInfo");
        
        Grid griMast = new Grid();
        griMast.setColumns(2);
        
        Grid griPri = new Grid();
        griPri.setColumns(3);
        
        griMast.getChildren().add(new Etiqueta("Buscar por :"));
        griMast.getChildren().add(cmbOpcion);
        texBusqueda.setSize(50);
        griPri.getChildren().add(new Etiqueta("Buscar Representante :"));
        griPri.getChildren().add(texBusqueda);
        griPri.getChildren().add(botRegistro);
        griBuscar.getChildren().add(griMast);
        griBuscar.getChildren().add(griPri);
        
        setFallecido.setId("setFallecido");
        setFallecido.setSeleccionTabla("select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,DOCUMENTO_IDENTIDAD_CMREP as Cédula,NOMBRES_APELLIDOS_CMREP \n"
                + "from CMT_REPRESENTANTE\n"
                + "where DOCUMENTO_IDENTIDAD_CMREP <>'0000000000   ' and  DOCUMENTO_IDENTIDAD_CMREP <>'01000        '\n"
                + "and  DOCUMENTO_IDENTIDAD_CMREP <>'00000000000' and  DOCUMENTO_IDENTIDAD_CMREP <>'010          '\n"
                + "and ESTADO='ACTIVO' and DOCUMENTO_IDENTIDAD_CMREP ='-1'", "DOCUMENTO_IDENTIDAD_CMREP");
        setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setFallecido.getTab_seleccion().getColumna("NOMBRES_APELLIDOS_CMREP").setFiltro(true);
        setFallecido.getTab_seleccion().setRows(10);
        setFallecido.setRadio();
        setFallecido.getGri_cuerpo().setHeader(griBuscar);//consultaFallecido
        setFallecido.getBot_aceptar().setMetodo("actualizaLista");
        setFallecido.setWidth("50%");
        setFallecido.setHeader("BUSCAR POR PARAMETROS");
        agregarComponente(setFallecido);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        agregarComponente(panOpcion);
        
        dibujarPantalla();
        
    }
    
    public void buscaRegistro() {
        setFallecido.dibujar();
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
    
    public void dibujarPantalla() {
        limpiaCampos();
        txtCedula.setId("txtCedula");
        txtCedula.setSize(15);
        
        txtNombre.setId("txtNombre");
        txtNombre.setSize(50);
        
        txtDireccion.setId("txtDireccion");
        txtDireccion.setSize(70);
        
        txtTelefono.setId("txtTelefono");
        txtTelefono.setSize(15);
        
        txtCorreo.setId("txtCorreo");
        txtCorreo.setSize(50);
        
        Grid griBusca = new Grid();
        griBusca.setColumns(6);
        
        griBusca.getChildren().add(etiCedula);
        griBusca.getChildren().add(txtCedula);
        griBusca.getChildren().add(etiNombre);
        griBusca.getChildren().add(txtNombre);
        griBusca.getChildren().add(etiDireccion);
        griBusca.getChildren().add(txtDireccion);
        griBusca.getChildren().add(etiTelefono);
        griBusca.getChildren().add(txtTelefono);
        griBusca.getChildren().add(etiCorreo);
        griBusca.getChildren().add(txtCorreo);
        PanelTabla pntRepresentante = new PanelTabla();
        pntRepresentante.setMensajeWarn("INFORMACIÓN DE REPRESENTANTE");
        pntRepresentante.getChildren().add(griBusca);
        
        
        limpiarPanel();
        
        tabCatastro.setId("tabCatastro");
        tabCatastro.setTabla("CMT_CATASTRO", "IDE_CATASTRO", 1);
        tabCatastro.getColumna("ide_lugar").setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR");
        tabCatastro.getColumna("habilita").setCheck();
        tabCatastro.getColumna("habilita").setMetodoChange("armaComentario");
        tabCatastro.setCondicion("IDE_CATASTRO=-1");
        tabCatastro.agregarRelacion(tabFallecido);
        tabCatastro.setRows(4);
        tabCatastro.dibujar();
        PanelTabla pntCatastro = new PanelTabla();
        pntCatastro.setMensajeWarn("DATOS DE CATASTRO");
        pntCatastro.setPanelTabla(tabCatastro);
        
        Division divDivision = new Division();
        divDivision.setId("divDivision");
        divDivision.dividir2(pntRepresentante, pntCatastro, "43%", "H");
        
        tabFallecido.setId("tabFallecido");
        tabFallecido.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 2);
        tabFallecido.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tabFallecido.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,MODULO)+'-'+convert(varchar,NUMERO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tabFallecido.agregarRelacion(tabDetalle);
        tabFallecido.agregarRelacion(tabMovimiento);
        tabFallecido.setRows(4);
        tabFallecido.dibujar();
        PanelTabla pntFallecido = new PanelTabla();
        pntFallecido.setMensajeWarn("DATOS DE FALLECIDOS");
        pntFallecido.setPanelTabla(tabFallecido);
        
        
        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 3);
        tabMovimiento.getColumna("ide_tipo_movimiento").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (3)");
        tabMovimiento.getColumna("usuario_ingre").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabMovimiento.getColumna("fecha_ingre").setValorDefecto(utilitario.getFechaActual());
        tabMovimiento.getColumna("fecha_ingresa").setValorDefecto(utilitario.getFechaActual());
        tabMovimiento.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabMovimiento.setTipoFormulario(true);
        tabMovimiento.getGrid().setColumns(6);
        tabMovimiento.dibujar();
        PanelTabla pntMovimiento = new PanelTabla();
        pntMovimiento.setMensajeWarn("DETALLE MOVIMIENTOS PARA LIQUIDACIÓN");
        pntMovimiento.setPanelTabla(tabMovimiento);
        
        
        tabDetalle.setId("tabDetalle");
        tabDetalle.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 4);
        tabDetalle.getColumna("ide_tipo_movimiento").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (3)");
        tabDetalle.getColumna("usuario_ingre").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabDetalle.getColumna("fecha_ingre").setValorDefecto(utilitario.getFechaActual());
        tabDetalle.getColumna("fecha_ingresa").setValorDefecto(utilitario.getFechaActual());
        tabDetalle.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabDetalle.setTipoFormulario(true);
        tabDetalle.getGrid().setColumns(6);
        tabDetalle.dibujar();
        PanelTabla pntDetalle = new PanelTabla();
        pntDetalle.setMensajeWarn("DETALLE MOVIMIENTOS PARA LIQUIDACIÓN");
        pntDetalle.setPanelTabla(tabDetalle);
        
        Division div = new Division();
        div.setId("div");
        div.dividir3(divDivision, pntFallecido, pntDetalle, "35%", "40%", "H");
        
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }
    
    public void limpiaCampos() {
        txtCedula.setValue(null);
        txtNombre.setValue(null);
        txtDireccion.setValue(null);
        txtTelefono.setValue(null);
        txtCorreo.setValue(null);
        utilitario.addUpdate("txtCedula,txtNombre,txtDireccion,txtTelefono,txtCorreo");
    }
    
    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscarSolicitud();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscarSolicitud1();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }
    
    public void buscarSolicitud() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,DOCUMENTO_IDENTIDAD_CMREP as Cédula,NOMBRES_APELLIDOS_CMREP \n"
                    + "from (select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP \n"
                    + "from CMT_REPRESENTANTE\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP <>'0000000000   ' and  DOCUMENTO_IDENTIDAD_CMREP <>'01000        '\n"
                    + "and  DOCUMENTO_IDENTIDAD_CMREP <>'00000000000' and  DOCUMENTO_IDENTIDAD_CMREP <>'010          '\n"
                    + "and ESTADO='ACTIVO') as a\n"
                    + "where NOMBRES_APELLIDOS_CMREP like '%" + texBusqueda.getValue() + "%' order by NOMBRES_APELLIDOS_CMREP");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }
    
    public void buscarSolicitud1() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,DOCUMENTO_IDENTIDAD_CMREP as Cédula,NOMBRES_APELLIDOS_CMREP \n"
                    + "from (select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP \n"
                    + "from CMT_REPRESENTANTE\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP <>'0000000000   ' and  DOCUMENTO_IDENTIDAD_CMREP <>'01000        '\n"
                    + "and  DOCUMENTO_IDENTIDAD_CMREP <>'00000000000' and  DOCUMENTO_IDENTIDAD_CMREP <>'010          '\n"
                    + "and ESTADO='ACTIVO') as a\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP =  '" + texBusqueda.getValue() + "' order by NOMBRES_APELLIDOS_CMREP");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }
    
    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabCatastro.setCondicion(getFiltrosAcceso());
            tabCatastro.ejecutarSql();
            utilitario.addUpdate("tabCatastro");
            actualizaRepresentante();
        }
    }
    
    private String getFiltrosAcceso() {
        String str_filtros = "";
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "IDE_CATASTRO IN (select IDE_CATASTRO from CMT_FALLECIDO where IDE_FALLECIDO in(select DISTINCT IDE_FALLECIDO \n"
                    + "from CMT_REPRESENTANTE\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP = '" + setFallecido.getValorSeleccionado() + "'))";
        }
        return str_filtros;
    }
    
    
    public void actualizaRepresentante() {
        if (!getFiltrosRepresentante().isEmpty()) {
            tabRepresentante.setCondicion(getFiltrosRepresentante());
            tabRepresentante.ejecutarSql();
            utilitario.addUpdate("tabRepresentante");
        }
    }
    
    private String getFiltrosRepresentante() {
        String str_filtros = "";
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "IDE_CMREP = (SELECT top 1 IDE_CMREP from CMT_REPRESENTANTE where DOCUMENTO_IDENTIDAD_CMREP ='" + setFallecido.getValorSeleccionado() + "') \n"
                + "ORDER BY IDE_CMREP";
        }
        return str_filtros;
    }
       
    public void checkAll1() {
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            tabCatastro.setValor(i, "habilita", "true");
        }
        armaComentario();
        utilitario.addUpdate("tabCatastro");
    }
    
    public void checkAll2() {
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            tabCatastro.setValor(i, "habilita", "false");
        }
        utilitario.addUpdate("tabCatastro");
        armaComentario();
    }
    
    public void armaComentario() {
        String rows = "";
        Integer numero = 0;
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            if (tabCatastro.getValor(i, "habilita").equals("true")) {
                rows += tabCatastro.getValor(i, "IDE_CATASTRO") + ",";
                numero = i + 1;
            }
        }
        System.err.println("" + rows);
        String cadenaNueva = rows.substring(0, rows.length() - 1);
        System.err.println("" + cadenaNueva + ", numero " + numero);
        crearConcepto(cadenaNueva, numero);
    }
    
    public void crearConcepto(String cadena, Integer numero) {
        tabDetalle.insertar();
//        TablaGenerica tabLugar = admin.getLugarAnio(cadena);
//        if (!tabLugar.isEmpty()) {
//            TablaGenerica tabDato = admin.getConceptoLiquidacion(cadena, cadena, cadena, cadena);
//            if (!tabDato.isEmpty()) {
//                tabDetalle.setValor("ancedentes", tabDato.getValor("descripcion"));
//            } else {
//                utilitario.agregarMensaje("Antecednetes no se pueden formar", null);
//            }
//        } else {
//            utilitario.agregarMensaje("no se puede mostrar informacion", "sitio ");
//        }
    }
    
    @Override
    public void insertar() {
//        tabDetalle.insertar();
    }
    
    @Override
    public void guardar() {
        if (tabDetalle.guardar()) {
            guardarPantalla();
        }
    }
    
    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public SeleccionTabla getSetFallecido() {
        return setFallecido;
    }
    
    public void setSetFallecido(SeleccionTabla setFallecido) {
        this.setFallecido = setFallecido;
    }
    
    public AutoCompletar getAutBusca() {
        return autBusca;
    }
    
    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
    
    public Tabla getTabFallecido() {
        return tabFallecido;
    }
    
    public void setTabFallecido(Tabla tabFallecido) {
        this.tabFallecido = tabFallecido;
    }
    
    public Tabla getTabCatastro() {
        return tabCatastro;
    }
    
    public void setTabCatastro(Tabla tabCatastro) {
        this.tabCatastro = tabCatastro;
    }
    
    public Tabla getTabDetalle() {
        return tabDetalle;
    }
    
    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }
    
    public Tabla getTabRepresentante() {
        return tabRepresentante;
    }
    
    public void setTabRepresentante(Tabla tabRepresentante) {
        this.tabRepresentante = tabRepresentante;
    }

    public Tabla getTabMovimiento() {
        return tabMovimiento;
    }

    public void setTabMovimiento(Tabla tabMovimiento) {
        this.tabMovimiento = tabMovimiento;
    }
    
}

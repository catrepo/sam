/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
@Stateless
public class BeanFormularios {

    private Utilitario utilitario = new Utilitario();
    private Conexion conSql;

    /**
     * Creates a new instance of BeanFormularios
     */
    public TablaGenerica getResponsableUnidad(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT IDE_RESPONSABLE,DESCRIPCION,RESPONSABLE FROM RESUG_AREA_RESPON where rol ='D' and ESTADO ='A' and IDE_CODIGO = " + codigo);
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getFormulario(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT  ID_FORMULARIO,FECHA1,CEDULA,CLAVE,NOMBRE,DIRECCION,DIRECCION1,ESTADO,IDE_RESPONSABLE,\n"
                + "TELEFONO,CELULAR,MAIL,OBSERVACION,ADJUNTO, (case when tipo = 'DNU' then 'DENUNCIA' else 'RECLAMO' end) as TIPO\n"
                + "FROM RESUG_FORMULARIO\n"
                + "where ID_FORMULARIO = " + codigo);
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getFormulario1(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT f.ID_FORMULARIO,f.FECHA1,f.CEDULA,f.CLAVE,f.NOMBRE,f.DIRECCION,f.DIRECCION1,f.ESTADO,\n"
                + "f.IDE_RESPONSABLE, TELEFONO,f.CELULAR,f.MAIL,f.OBSERVACION,f.ADJUNTO,\n"
                + "(case when tipo = 'DNU' then 'DENUNCIA' else 'RECLAMO' end) AS TIPO,a.TIPO_EMPRESA\n"
                + "FROM dbo.RESUG_FORMULARIO f\n"
                + "left join dbo.RESUG_AREA_RESPON a on f.IDE_RESPONSABLE = a.IDE_CODIGO\n"
                + "where f.ID_FORMULARIO = " + codigo);
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getTipoArea(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT ID_CODIGO ,DESCRIPCION,TIPO_EMPRESA\n"
                + "FROM RESUG_AREA_RESPON\n"
                + "where ID_CODIGO =" + codigo);
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getCodigoPrimario() {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT IDE_BLOQ,TABLA_BLOQ,MAXIMO_BLOQ FROM SIS_BLOQUEO\n"
                + "where TABLA_BLOQ = 'RESUG_ACTIVIDADES'");
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getResponsableDenun() {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "IDE_CODIGO,  \n"
                + "IDE_RESPONSABLE,  \n"
                + "CORREO,  \n"
                + "ROL \n"
                + "FROM RESUG_AREA_RESPON  \n"
                + "where IDE_CODIGO = (SELECT ID_CODIGO FROM RESUG_AREA_RESPON where DESCRIPCION = 'PROCURADURÍA SINDICA')");
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getResponsableReclamo(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select  * from (\n"
                + "SELECT IDE_CODIGO,RESPONSABLE,DESCRIPCION,CORREO,ROL \n"
                + "FROM RESUG_AREA_RESPON  \n"
                + "where IDE_CODIGO is not null and estado = 'A') as a\n"
                + "where a.IDE_CODIGO =" + codigo);
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getIdFormulario(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT DISTINCT\n"
                + "TIPO_ACTIVIDAD,\n"
                + "ID_FORMULARIO,\n"
                + "UNI_RESPONSABLE\n"
                + "FROM RESUG_ACTIVIDADES\n"
                + "where ID_FORMULARIO = " + codigo + " and TIPO_ACTIVIDAD = (select ID_ESTADO from RESUG_ESTADO where estado = 'Asignado')");
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getArchivo(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "ID_ACTIVIDAD,\n"
                + "FECHA_ACTIVIDAD,\n"
                + "TIPO_ACTIVIDAD,\n"
                + "ID_FORMULARIO,\n"
                + "UNI_RESPONSABLE,\n"
                + "USU_ASIGNACION,\n"
                + "DESCRIPCION,\n"
                + "USU_RESPONSABLE,\n"
                + "USU_ADJUNTO\n"
                + "FROM RESUG_ACTIVIDADES\n"
                + "where ID_ACTIVIDAD =" + codigo);
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public TablaGenerica getEstadoDenun(String tipo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT ID_ESTADO,estado FROM RESUG_ESTADO where Estado = '" + tipo + "'");
        tabPersona.ejecutarSql();
        desSQL();
        return tabPersona;
    }

    public void setActividad(Integer codigo, Integer tipo, Integer formulario, Integer unidad, String responsable, String comentario, String auxiliar, String ip, Integer opc) {
        String parametro = "insert into  RESUG_ACTIVIDADES (ID_ACTIVIDAD,TIPO_ACTIVIDAD,ID_FORMULARIO,UNI_RESPONSABLE,USU_ASIGNACION,DESCRIPCION,FECHA_ACTIVIDAD,USU_RESPONSABLE,USU_IP)\n"
                + "values (" + codigo + "," + tipo + "," + formulario + "," + unidad + ",'" + responsable + "','" + comentario + "','" + utilitario.getFechaActual() + "','" + auxiliar + "','" + ip + "')";
        conSql();
        conSql.ejecutarSql(parametro);
        SQLAuditoria(opc, parametro.replace("'", ""), "resug_actividades", "INSERTAR");
        desSQL();
    }
    
    public void SQLAuditoria(Integer opc, String detalle, String tabla, String accion) {
        String strSql = "INSERT INTO sis_auditoria(ide_empr,ide_opci,ide_sucu,ide_usua,fecha_audi,hora_audi,sql_audi,tabla_audi,ip_audi,accion_audi) "
                + "VALUES(0," + opc + ",0," + utilitario.getVariable("IDE_USUA") + ","
                + utilitario.getFormatoFechaSQL(utilitario.getFechaActual()) + "," + utilitario.getFormatoHoraSQL(utilitario.getHoraActual())
                + ",'" + detalle + "','" + tabla + "','" + utilitario.getIp()
                + "','" + accion + "')";
        conSql();
        conSql.ejecutarSql(strSql);
        desSQL();
    }

    //ASIGNACION DE ESTADO A PLACAS NUEVAS
    public void setAccionFormulario(Integer codigo, String valor, Integer opc) {
        String nueva = "update RESUG_FORMULARIO\n"
                + "set estado =" + valor + " \n"
                + "where ID_FORMULARIO = " + codigo;
        conSql();
        conSql.ejecutarSql(nueva);
        SQLAuditoria(opc, nueva, "resug_formulario", "ACTUALIZAR");
        desSQL();
    }

    public void setDevolucionActividad(Integer codigo, Integer id, String fecha) {
        String nueva = "update RESUG_ACTIVIDADES\n"
                + "set estado = 0\n"
                + "where ID_ACTIVIDAD = " + codigo + " and ID_FORMULARIO=" + id + " and FECHA_ACTIVIDAD = '" + fecha + "'";
        conSql();
        conSql.ejecutarSql(nueva);
        desSQL();
    }

    public void setDevolucionFormulario(Integer codigo, String estado) {
        String nueva = "update RESUG_FORMULARIO\n"
                + "set ESTADO = (select ID_ESTADO from RESUG_ESTADO where ESTADO = '" + estado + "')\n"
                + "where ID_FORMULARIO=" + codigo;
        conSql();
        conSql.ejecutarSql(nueva);
        desSQL();
    }

    public void setTablaBloqueo(Integer codigo) {
        String nueva = "update SIS_BLOQUEO\n"
                + "set MAXIMO_BLOQ = " + codigo + "\n"
                + "where TABLA_BLOQ = 'RESUG_ACTIVIDADES'";
        conSql();
        conSql.ejecutarSql(nueva);
        desSQL();
    }

    public void setDirAsignacion(Integer codigo, Integer unidad) {
        String nueva = "update RESUG_FORMULARIO\n"
                + "set IDE_RESPONSABLE = " + unidad + "\n"
                + "where ID_FORMULARIO=" + codigo;
        conSql();
        conSql.ejecutarSql(nueva);
        desSQL();
    }

    public void setMailReclamo(Integer codigo, String unidad, String reclamo, String login) {
        String nueva = "update RESUG_FORMULARIO\n"
                + "set mail = '" + unidad + "',"
                + "observacion = '" + reclamo + "',"
                + "fecha_actu = '" + utilitario.getFechaActual() + "',"
                + "login_actu = '" + login + "'\n"
                + "where ID_FORMULARIO=" + codigo;
        conSql();
        conSql.ejecutarSql(nueva);
        desSQL();
    }

    private void conSql() {
        if (conSql == null) {
            conSql = new Conexion();
            conSql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void desSQL() {
        if (conSql != null) {
            conSql.desconectar(true);
            conSql = null;
        }
    }
}

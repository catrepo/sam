/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_adquisicion.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
/**
/**
 *
 * @author Andres
 */
@Stateless
public class ServiciosAdquisiones {
           /**
     * Retorna los tipos de aprobaciones que existen
     *
     * @return sql de aprobado
     */
    public String getAprobado() {
        String sql="";
        sql="SELECT IDE_ADAPRO, DETALLE_ADAPRO FROM ADQ_APROBADO";
        return sql;
    }
    public String getTipoArea() {
        String sql="";
        sql="select IDE_ADTIAR, DETALLE_ADTIAR from ADQ_TIPO_AREA";
        return sql;
    }
    public String getGrupoMaterial() {
        String sql="";
        sql="SELECT IDE_ADGRMA, DETALLE_ADGRMA, CODIGO_ADGRMA FROM ADQ_GRUPO_MATERIAL";
        return sql;
    }
    public String getPartidaPresupuestaria() {
        String sql="";
        sql="SELECT IDE_ADPAPR, DETALLE_ADPAPR, CODIGO_ADPAPR FROM ADQ_PARTIDA_PRESUPUESTARIA";
        return sql;
    }
    public String getAreaPartida() {
        String sql="";
        sql="select ide_adarpa,codigo_adarpa,detalle_adpapr from ADQ_AREA_PARTIDA a,ADQ_PARTIDA_PRESUPUESTARIA b where a.IDE_ADPAPR = b.IDE_ADPAPR order by codigo_adpapr";
        return sql;
    }
    public String getPartidaMaterial() {
        String sql="";
        sql="select ide_adpama,CODIGO_ADPAMA,DETALLE_ADMATE\n" +
            "from ADQ_PARTIDA_MATERIAL a, ADQ_MATERIAL b\n" +
            "where a.IDE_ADMATE=b.IDE_ADMATE\n" +
            "order by CODIGO_ADPAMA";
        return sql;
    }
    public String getAreaAdministrativa() {
        String sql="";
        sql="SELECT IDE_ADARAD, CODIGO_ADARAD, DETALLE_ADARAD FROM ADQ_AREA_ADMINISTRATIVA";
        return sql;
    }
    public String getEmpleado() {
        String sql="";
        sql="SELECT IDE_ADEMPLE, CEDULA_ADEMPLE, NOMBRES_ADEMPLE FROM ADQ_EMPLEADO";
        return sql;
    }
    public String getCargo() {
        String sql="";
        sql="SELECT IDE_ADCARG, DETALLE_ADCARG FROM ADQ_CARGO";
        return sql;
    }
    public String getTipoAprobador() {
        String sql="";
        sql="SELECT IDE_ADTIAP, DETALLE_ADTIAP FROM ADQ_TIPO_APROBADOR";
        return sql;
    }
}

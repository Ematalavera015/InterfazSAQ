/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import modelo.entidad.Elemento;
import vista.ventana.InterfaceRegistrarDevolucion;
import vista.ventana.InterfaceRegistrarMatyEqu;

/**
 *
 * @author Ema
 */
public class Metodos_db {

    public String[] to_array(ArrayList<String> list) {

        String array[] = new String[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);

        }

        return array;
    }

    public void consultar() {

        Pool metodospool = new Pool();
        Connection cn;

        try {

            cn = metodospool.dataSource.getConnection();
            ResultSet rs;

            String sql;
            PreparedStatement stm;

            ArrayList<String> list_elemento = new ArrayList<>();
            ArrayList<String> list_tipoelemento = new ArrayList<>();
            int idelemento = 0;
            boolean condicional = false;

            if (cn != null) {

                String busqueda_elemento;

                if (InterfaceRegistrarMatyEqu.cbTipoElemento.getItemCount() > 0) {

                    busqueda_elemento = InterfaceRegistrarMatyEqu.cbTipoElemento.getSelectedItem().toString();

                    sql = "SELECT idtipo FROM tipo_elemento where nombre=?";

                    stm = cn.prepareStatement(sql);
                    stm.setString(1, busqueda_elemento);

                    rs = stm.executeQuery();

                    while (rs.next()) {
                        idelemento = (rs.getInt("idtipo"));
                    }

                } else {

                    sql = "SELECT idtipo,nombre FROM tipo_elemento";

                    stm = cn.prepareStatement(sql);

                    rs = stm.executeQuery();

                    while (rs.next()) {

                        if (condicional == false) {

                            idelemento = (rs.getInt("idtipo"));
                            condicional = true;
                        }
                        list_tipoelemento.add(rs.getString("nombre"));

                    }

                }

                sql = "SELECT nombreelemento FROM elemento INNER JOIN tipo_elemento on(tipo_elemento.idtipo=elemento.idtipo) WHERE elemento.idtipo=? ORDER BY nombreelemento ASC";

                stm = cn.prepareStatement(sql);

                stm.setInt(1, idelemento);

                rs = stm.executeQuery();

                while (rs.next()) {

                    list_elemento.add(rs.getString("nombreelemento"));
                }

                InterfaceRegistrarMatyEqu.modeloComboElemento = new DefaultComboBoxModel(to_array(list_elemento));
                InterfaceRegistrarMatyEqu.cbElementos.setModel(InterfaceRegistrarMatyEqu.modeloComboElemento);

                if (condicional == true) {
                    InterfaceRegistrarMatyEqu.modeloComboTipoElemento = new DefaultComboBoxModel(to_array(list_tipoelemento));
                    InterfaceRegistrarMatyEqu.cbTipoElemento.setModel(InterfaceRegistrarMatyEqu.modeloComboTipoElemento);
                }

                cn.close();
            }

        } catch (SQLException e) {

            System.out.println(e);
            System.out.println("Error");

        }
     
    }
    
    public void consultarElementos(){
        
        Pool metodospool = new Pool();
        Connection cn;

        try {

            cn = metodospool.dataSource.getConnection();
            ResultSet rs;

            String sql,busqueda_elem;
            PreparedStatement stm;

            ArrayList<String> list_elemento = new ArrayList<>();

            if (cn != null) {

                sql = "SELECT nombreelemento FROM elemento ORDER BY nombreelemento ASC";

                stm = cn.prepareStatement(sql);

                rs = stm.executeQuery();

                while (rs.next()) {

                    list_elemento.add(rs.getString("nombreelemento"));
                }

                InterfaceRegistrarDevolucion.modeloComboElemento = new DefaultComboBoxModel(to_array(list_elemento));
                //InterfaceRegistrarDevolucion..setModel(InterfaceRegistrarMatyEqu.modeloComboElemento);

                cn.close();
            }

        } catch (SQLException e) {

            System.out.println(e);
            System.out.println("Error");

        }
     
    }
    
    public String[][] obtenerMatriz(){
        
        Elemento elemento = new Elemento();
        ArrayList<Elemento> miLista = new ArrayList<>();
        return null;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.ventana;

import controlador.Pool;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import static vista.ventana.InterfaceRegistrarMatyEqu.integerFormat;

/**
 *
 * @author Ema
 */
public class InterfaceRegistrarDevolucion extends javax.swing.JFrame {

    public static DefaultComboBoxModel modeloComboElemento, modeloComboQuirofano;
    private static DefaultTableModel modelo1;
    private static String[] datos = {"", ""};
    int IdRegistrarDevolucion = 0;

    String fechaActual, horaActual;
    
    ArrayList<String> datosCantidadPrestamo, datosCantidadDevolucion;

    /**
     * Creates new form InterfaceRegistrarDevolucion
     */
    public InterfaceRegistrarDevolucion() {
        initComponents();
        //Elemento de la ventana

        fechaActual = ObtenerFecha();
        horaActual = ObtenerHora();

        lblFecha.setText(fechaActual);
        lblHora.setText(horaActual);

        IdRegistrarDevolucion = obtenerIdRegistro();

        txtRegistro.setText(integerFormat1(IdRegistrarDevolucion));

        this.setTitle("SAQ - Registrar Devolución");
        //setIconImage(new ImageIcon(getClass().getResource("../imagen/logo_SAQ.png")).getImage());
        this.setLocationRelativeTo(null);

        cargarQuirofano();
        
        //ComboBox y Tabla
        Pool metodospool = new Pool();
        Connection cn;

        try {

            cn = metodospool.dataSource.getConnection();
            ResultSet rs;

            String sql;

            PreparedStatement stm;

            ArrayList<String> list_elemento = new ArrayList<>();

            if (cn != null) {

                sql = "SELECT nombreelemento FROM elemento ORDER BY nombreelemento ASC";

                stm = cn.prepareStatement(sql);

                rs = stm.executeQuery();

                //rs.next();
                System.out.println("Elementos " + rs.getRow());
                while (rs.next()) {

                    list_elemento.add(rs.getString("nombreelemento"));
                }

                InterfaceRegistrarDevolucion.modeloComboElemento = new DefaultComboBoxModel(to_array(list_elemento));
                //InterfaceRegistrarDevolucion..setModel(InterfaceRegistrarMatyEqu.modeloComboElemento);
                System.err.println("Seteo el modelo");
                JComboBox cbElemento = new JComboBox(modeloComboElemento);

                modelo1 = new DefaultTableModel();
                modelo1.addColumn("Elemento");
                modelo1.addColumn("Cantidad");
                jTable1.setModel(modelo1);

                TableColumn tc = jTable1.getColumnModel().getColumn(0);
                TableCellEditor tce = new DefaultCellEditor(cbElemento);
                tc.setCellEditor(tce);
                cn.close();

                //TableColumn tc = jTable1.getColumnModel().getColumn(0);
                //tc.setCellEditor(new DefaultCellEditor(cbElemento));
            }

        } catch (SQLException e) {

            System.out.println(e);
            System.out.println("Error");

        }
        ///

    }
    
    public void cargarQuirofano() {

        Pool metodospool = new Pool();
        Connection cn;

        ArrayList<Integer> list_numQuirofano = new ArrayList<>();

        int idqeuirofano;

        boolean condicional = false;

        try {
            cn = metodospool.dataSource.getConnection();
            ResultSet rs;

            String sql;
            PreparedStatement stm;
            if (cn != null) {

                String busqueda_numQuirofano;

                if (cbQuirofano.getItemCount() > 0) {
                    System.out.println("Entro al if");

                    busqueda_numQuirofano = cbQuirofano.getSelectedItem().toString();

                    sql = "SELECT * FROM quirofano where numero=?";

                    stm = cn.prepareStatement(sql);
                    stm.setString(2, busqueda_numQuirofano);

                    rs = stm.executeQuery();

                    while (rs.next()) {
                        idqeuirofano = (rs.getInt("idquirofano"));
                    }

                } else {

                    System.out.println("Entro al else");
                    sql = "SELECT * FROM quirofano";

                    stm = cn.prepareStatement(sql);

                    rs = stm.executeQuery();

                    while (rs.next()) {

                        //if (condicional == false) {

                            //idqeuirofano = (rs.getInt("idquirofano"));
                            //condicional = true;
                        //}
                        if(rs.getBoolean("ocupado")){
                            list_numQuirofano.add(rs.getInt("numero"));
                        }
                        

                    }

                }

                //if (condicional == true) {
                    InterfaceRegistrarDevolucion.modeloComboQuirofano = new DefaultComboBoxModel(to_array2(list_numQuirofano));
                    InterfaceRegistrarDevolucion.cbQuirofano.setModel(InterfaceRegistrarDevolucion.modeloComboQuirofano);
                //}

                cn.close();
            }
        } catch (SQLException e) {
        }

    }
    
     public Integer[] to_array2(ArrayList<Integer> list) {

        Integer array[] = new Integer[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);

        }

        return array;
    }

    public static String[] to_array(ArrayList<String> list) {

        String array[] = new String[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);

        }

        return array;
    }

    public static String integerFormat(int i) {

        java.text.DecimalFormat nft = new java.text.DecimalFormat("#00.###");
        nft.setDecimalSeparatorAlwaysShown(false);
        System.out.println(nft.format(i));

        //DecimalFormat df = new DecimalFormat("##");
        String s = nft.format(i);

        return s;

    }

    public static String integerFormat1(int i) {

        java.text.DecimalFormat nft = new java.text.DecimalFormat("#0000000000000.###");
        nft.setDecimalSeparatorAlwaysShown(false);
        System.out.println(nft.format(i));

        //DecimalFormat df = new DecimalFormat("##");
        String s = nft.format(i);

        return s;

    }

    //
    public static int obtenerIdRegistro1() {

        int idRegistro = 0;
        String fecha = "";
        Pool cc = new Pool();
        ResultSet rs;

        try {
            System.out.println("Antes de la conecion");
            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM devolucion");// WHERE nombres= '" + InterfaceRegistrarMatyEqu.cbElementos.getSelectedItem() + "'");
            System.out.println("Despues de la consulta");
            rs = pst.executeQuery();
            System.out.println("Cantidad de filasa: " + rs.getRow());
            rs.next();
            System.out.println("Cantidad de filasb: " + rs.getRow());
            if (rs.getRow() == 0) {
                System.out.println("No tiene datos");
                idRegistro = 0;
            } else {
                System.out.println("Tiene datos");
                while (rs.next()) {
                    System.out.println("Entro al while");
                    fecha = rs.getString("fecha");
                    System.out.println("Fecha: " + rs.getString("fecha"));
                }

                /*if(ObtenerFecha().equals()){
                    
                }*/
            }

            if (ObtenerFecha().equals(fecha)) {
                PreparedStatement pst1 = cn.prepareStatement("SELECT iddevolucion FROM devolucion WHERE fecha = '" + fecha + "'");
                rs = pst1.executeQuery();
                rs.next();
                while (rs.next()) {
                    idRegistro = rs.getInt("iddevolucion");
                }

            } else {
                idRegistro = 0;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        System.out.println("ID ultimo: " + idRegistro);

        return idRegistro + 1;
    }

    public static int emitirAlerta() {

        int idRegistro = 0, idDevolucion = 0;
        Pool cc = new Pool();
        ResultSet rs1, rs2;

        try {
            System.out.println("Antes de la conecion");
            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM devolucion_elemento");// WHERE nombres= '" + InterfaceRegistrarMatyEqu.cbElementos.getSelectedItem() + "'");
            System.out.println("Despues de la consulta");
            rs1 = pst.executeQuery();
            //System.out.println("Antes del if1 " + rs1.next());
            //System.out.println("Antes del if2 " + rs1.next());
            while (rs1.next()) {
                idRegistro = idRegistro + 1;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        System.out.println("ID ultimo: " + idRegistro);

        return 0;
    }

    public static int obtenerIdRegistro() {

        int idRegistro = 0;
        String fecha = "";
        //String fechaMayor = "";
        Pool cc = new Pool();
        ResultSet rs, rs1;

        fecha = ObtenerFecha();

        try {
            System.out.println("Antes de la conecion");
            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM devolucion");// WHERE nombres= '" + InterfaceRegistrarMatyEqu.cbElementos.getSelectedItem() + "'");
            System.out.println("Despues de la consulta");
            rs = pst.executeQuery();
            rs.next();
            if (rs.getRow() == 0) {
                System.out.println("No tiene datos");
                idRegistro = 0;
            } else {
                System.out.println("Tiene datos");
                fecha = rs.getString("fecha");
                rs.previous();

                while (rs.next()) {
                    System.out.println("Entro al while");

                    if (fecha.compareTo(rs.getString("fecha")) < 0) {
                        fecha = rs.getString("fecha");
                        System.out.println("Fechaaaaa: " + rs.getString("fecha"));
                    }
                    //fechaMayor = rs.getString("fecha");

                }
            }
            System.out.println("Antes del if");
            if (ObtenerFecha().equals(fecha)) {
                PreparedStatement pst1 = cn.prepareStatement("SELECT iddevolucion FROM devolucion WHERE fecha = '" + fecha + "' ORDER BY idprestamo ASC");
                rs1 = pst1.executeQuery();
                //rs.next();
                while (rs1.next()) {
                    System.out.println("Regitros: " + rs1.getInt("iddevolucion"));
                    idRegistro = rs1.getInt("iddevolucion");
                }

            } else {
                idRegistro = 0;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        System.out.println("ID ultimo: " + idRegistro);

        return idRegistro + 1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIdEnfermero = new javax.swing.JTextField();
        cbQuirofano = new javax.swing.JComboBox<>();
        txtRegistro = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        btnCancelar = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        lblApellido = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblEspecialidad = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnControlar = new javax.swing.JButton();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(820, 600));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 255, 204));
        jLabel2.setText("Fecha:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 80, 30));

        jLabel3.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 255, 204));
        jLabel3.setText("Hora:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 60, 30));

        jLabel4.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 255, 204));
        jLabel4.setText("N° Registro:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 110, 30));

        lblFecha.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(153, 255, 204));
        getContentPane().add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 110, 30));

        lblHora.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblHora.setForeground(new java.awt.Color(153, 255, 204));
        getContentPane().add(lblHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 110, 30));

        jLabel1.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 204));
        jLabel1.setText("N° Quirófano:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 255, 204));
        jLabel5.setText("ID Enfermero:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, -1, -1));

        txtIdEnfermero.setEditable(false);
        txtIdEnfermero.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtIdEnfermero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdEnfermeroKeyTyped(evt);
            }
        });
        getContentPane().add(txtIdEnfermero, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 230, -1));

        cbQuirofano.setFont(new java.awt.Font("Arial Narrow", 1, 20)); // NOI18N
        getContentPane().add(cbQuirofano, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 60, -1));

        txtRegistro.setEditable(false);
        txtRegistro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 23, 110, 30));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 820, 11));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 255, 204));
        jLabel6.setText("Apellido:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 255, 204));
        jLabel7.setText("Nombre:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 255, 204));
        jLabel8.setText("Especialidad:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 820, 10));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 255, 204));
        jLabel9.setText("Datos del Enfermero");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 820, 10));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 255, 204));
        jLabel10.setText("Materiales y Equipos");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, -1, -1));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 820, 10));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, -1, 120));
        getContentPane().add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 670, 10));
        getContentPane().add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 670, -1));
        getContentPane().add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 278, 640, 10));

        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setMaximumSize(new java.awt.Dimension(95, 23));
        btnCancelar.setMinimumSize(new java.awt.Dimension(95, 23));
        btnCancelar.setPreferredSize(new java.awt.Dimension(95, 23));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 510, 95, 30));

        btnRegistrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, -1, 30));

        lblApellido.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblApellido.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 640, 30));

        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 640, 20));

        lblEspecialidad.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblEspecialidad.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblEspecialidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 600, 30));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, -1, -1));

        btnControlar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnControlar.setText("Controlar");
        btnControlar.setMaximumSize(new java.awt.Dimension(95, 25));
        btnControlar.setMinimumSize(new java.awt.Dimension(95, 25));
        btnControlar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnControlarActionPerformed(evt);
            }
        });
        getContentPane().add(btnControlar, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 420, -1, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/FONDO SAQ.jpg"))); // NOI18N
        lblFondo.setMinimumSize(new java.awt.Dimension(820, 600));
        lblFondo.setPreferredSize(new java.awt.Dimension(820, 600));
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        InterfaceMenu menu = new InterfaceMenu();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        modelo1.setRowCount(0);
        jTable1.setModel(modelo1);

        
        datosCantidadPrestamo = new ArrayList<>();
        datosCantidadDevolucion = new ArrayList<>();

        Pool metodospool = new Pool();
        Connection cn;

        try {

            cn = metodospool.dataSource.getConnection();
            ResultSet rs;

            String sql;

            PreparedStatement stm;

            //NUEVA CARGA DE TABLA
            int idquirofano = Integer.parseInt(cbQuirofano.getSelectedItem().toString());
            System.out.println("IDQUIROFANO: " + idquirofano);

            sql = "SELECT * FROM prestamo WHERE idquirofano = '" + idquirofano + "' ORDER BY fecha DESC";

            stm = cn.prepareStatement(sql);

            rs = stm.executeQuery();

            if (rs.next()) {
                sql = "SELECT * FROM prestamo WHERE fecha = '" + rs.getString("fecha") + "' ORDER BY hora DESC";

                stm = cn.prepareStatement(sql);

                rs = stm.executeQuery();

                rs.next();

                txtIdEnfermero.setText(rs.getString("idpersonal"));
                cargarPersonal(rs.getInt("idpersonal"));

                int idprestamo = rs.getInt("idprestamo");

                System.out.println("IDPRESTAMO : " + idprestamo);
                String fecha = rs.getString("fecha");
                System.out.println("FECHA : " + fecha);

                sql = "SELECT * FROM prestamo_elemento WHERE idprestamo = '" + idprestamo + " ' AND fecha = '" + fecha + "' ";

                stm = cn.prepareStatement(sql);

                rs = stm.executeQuery();

                while (rs.next()) {
                    String nombre = traerNombre(rs.getInt("idelemento"));

                    datos[0] = nombre;
                    datosCantidadPrestamo.add(Integer.toString(rs.getInt("cantidad")));
                    //datos[1] ="";
                    datos[1] = Integer.toString(rs.getInt("cantidad"));//Cambiar a vacio
                    modelo1.addRow(datos);
                }

                jTable1.setModel(modelo1);

                modelo1.addTableModelListener(new TableModelListener() {
                    @Override
                    public void tableChanged(TableModelEvent e) {
                        if (e.getType() == TableModelEvent.UPDATE) {
                            int columnaSeleccionada = e.getColumn();
                            int filaSeleccionada = e.getFirstRow();
                            System.out.println("VALOR: "+ jTable1.getValueAt(filaSeleccionada, columnaSeleccionada).toString());
                            datosCantidadDevolucion.add(jTable1.getValueAt(filaSeleccionada, columnaSeleccionada).toString());
                        }
                    }
                });
                
                System.out.println("CANTIDAD de DATOS DEVUELTOS: " + datosCantidadDevolucion.size());
                for (int i = 0; i < datosCantidadPrestamo.size(); i++) {
                    System.out.println("Cantidad Prestamo: " + datosCantidadPrestamo.get(i));
                }

                for (int i = 0; i < datosCantidadDevolucion.size(); i++) {
                    System.out.println("Cantidad Devolucion: " + datosCantidadDevolucion.get(i));
                }

            } else {
                JOptionPane.showMessageDialog(null, "No hay préstamos cargados con el N° Quirofano " + idquirofano, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {

            System.out.println(e);
            System.out.println("Error");

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    //Trae el nombre del elemento que se presto de la tabla prestamo_elemento
    public String traerNombre(int idelemento) {
        String nombre = "";

        Pool metodospool = new Pool();
        Connection cn;

        try {

            cn = metodospool.dataSource.getConnection();
            ResultSet rs;

            String sql;
            PreparedStatement stm;

            sql = "SELECT * FROM elemento WHERE idelemento = '" + idelemento + "'";

            stm = cn.prepareStatement(sql);

            rs = stm.executeQuery();

            rs.next();

            nombre = rs.getString("nombreelemento");

        } catch (SQLException e) {

            System.out.println(e);
            System.out.println("Error");

        }

        return nombre;
    }

    //Se cargan los datos del personal para mostrarlos en a pantalla
    public void cargarPersonal(int idpersonal) {

        Pool cc = new Pool();
        ResultSet rs;

        try {
            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM personal WHERE idpersonal= '" + idpersonal + "'");

            rs = pst.executeQuery();

            rs.next();

            lblApellido.setText(rs.getString("apellido"));
            lblNombre.setText(rs.getString("nombre"));

            int idespecialidad = rs.getInt("idespecialidad");

            //System.out.println("Rs Especialidad: "+ rs.getInt("idespecialidad"));
            //System.out.println("Especialidad " + idespecialidad);
            PreparedStatement pst1 = cn.prepareStatement("SELECT * FROM especialidad WHERE idespecialidad= '" + idespecialidad + "'");

            rs = pst1.executeQuery();
            System.out.println("Paso ");
            rs.next();

            lblEspecialidad.setText(rs.getString("nombreespecialidad"));

        } catch (SQLException e) {
            System.out.println("Error" + e);
        }

    }


    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        Pool cc = new Pool();
        ResultSet rs;

        //int cantidadmaterial = 0, cantidadequipo = 0;
        String nombreElmento = "";
        //int cantElemento = 0;

        //Prestamo elemento = new Elemento();
        try {

            Connection cn = cc.dataSource.getConnection();
            //PreparedStatement pst = cn.prepareStatement("INSERT INTO prestamo(idprestamo,fecha,hora,idenfermero,idquirofano) VALUES(?,?,?,?,?,?,?)");

            String idpersonal = txtIdEnfermero.getText();
            if (idpersonal.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos", "Mensaje", JOptionPane.WARNING_MESSAGE);
            } else {

//                jTable1.setValueAt(jTable1.getCellEditor(), 0, 1);
//                if (jTable1.getValueAt(0, 1).equals("")) {
//                    JOptionPane.showMessageDialog(null, "Debe cargar cantidad de elementos a devolver!!!","Mensaje",JOptionPane.WARNING_MESSAGE);
//                } else {
                PreparedStatement pst = cn.prepareStatement("INSERT INTO devolucion(iddevolucion,fecha,hora,idpersonal,idquirofano,idprestamo) VALUES(?,?,?,?,?,?)");

                pst.setInt(1, IdRegistrarDevolucion);
                pst.setString(2, fechaActual);
                pst.setString(3, horaActual);
                pst.setInt(4, Integer.parseInt(idpersonal));
                pst.setInt(5, Integer.parseInt(cbQuirofano.getSelectedItem().toString()));

                PreparedStatement pst1 = cn.prepareStatement("SELECT * FROM prestamo WHERE idquirofano = '" + cbQuirofano.getSelectedItem().toString() + "'");

                rs = pst1.executeQuery();
                rs.next();
                pst.setInt(6, rs.getInt("idprestamo"));

                int a = pst.executeUpdate();

                if (a > 0) {
                    Icon m = new ImageIcon(getClass().getResource("../imagen/tilde.png"));
                    JOptionPane.showMessageDialog(null, "Registro exitoso", "Mensaje", JOptionPane.WARNING_MESSAGE, m);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar");
                }

                //cargando la tabla prestamo elemento
                System.err.println("jTable1.getRowCount() " + jTable1.getRowCount());
                for (int i = 0; i < jTable1.getRowCount(); i++) {

                    nombreElmento = jTable1.getValueAt(i, 0).toString();
                    //jTable1.getValueAt(i, 1).toString();
                    System.out.println(jTable1.getValueAt(i, 0).toString() + "-");
                    PreparedStatement pst2 = cn.prepareStatement("SELECT idelemento FROM elemento WHERE nombreelemento = '" + nombreElmento + "'");

                    rs = pst2.executeQuery();
                    rs.next();

                    PreparedStatement pst3 = cn.prepareStatement("INSERT INTO devolucion_elemento(iddevolucion,fecha,idelemento,cantidad) VALUES(?,?,?,?)");
                    pst3.setInt(1, IdRegistrarDevolucion);
                    pst3.setString(2, fechaActual);
                    pst3.setInt(3, rs.getInt("idelemento"));
                    pst3.setInt(4, Integer.parseInt(jTable1.getValueAt(i, 1).toString()));

                    pst3.executeUpdate();

                    

                }
                PreparedStatement pst4 = cn.prepareStatement("UPDATE quirofano SET ocupado = false WHERE idquirofano = " + cbQuirofano.getSelectedItem().toString());

                pst4.executeUpdate();

                InterfaceMenu menu = new InterfaceMenu();
                menu.setVisible(true);
                this.setVisible(false);
                //}

            }

        } catch (SQLException e) {
            System.out.println("Error Registarr: " + e);
        }

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtIdEnfermeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdEnfermeroKeyTyped
        char tecla;

        tecla = evt.getKeyChar();

        if (!Character.isDigit(tecla) && tecla != KeyEvent.VK_BACK_SPACE) {

            evt.consume();
            getToolkit().beep();

        }
    }//GEN-LAST:event_txtIdEnfermeroKeyTyped

    private void btnControlarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnControlarActionPerformed
        //error
    }//GEN-LAST:event_btnControlarActionPerformed

    public static String ObtenerFecha() {
        //Instanciamos el objeto Calendar
        //en fecha obtenemos la fecha y hora del sistema
        Calendar fecha = new GregorianCalendar();
        //Obtenemos el valor del año, mes, día,
        //hora, minuto y segundo del sistema
        //usando el método get y el parámetro correspondiente
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);

        String fechaActual = dia + "/" + (mes + 1) + "/" + año;

        //System.out.println("Fecha Actual: "
        //       + dia + "/" + (mes + 1) + "/" + año);
        //System.out.printf("Hora Actual: %02d:%02d:%02d %n",
        //      hora, minuto, segundo);
        return fechaActual;
    }

    public static String ObtenerHora() {
        //Instanciamos el objeto Calendar
        //en fecha obtenemos la fecha y hora del sistema
        Calendar fecha = new GregorianCalendar();
        //Obtenemos el valor del año, mes, día,
        //hora, minuto y segundo del sistema
        //usando el método get y el parámetro correspondiente
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);

        String horaActual = integerFormat(hora) + ":" + integerFormat(minuto);// + ":" + segundo;

        //System.out.println("Fecha Actual: "
        //     + dia + "/" + (mes + 1) + "/" + año);
        //System.out.printf("Hora Actual: %02d:%02d:%02d %n",
        //   hora, minuto, segundo);
        //horaActual =integerFormat(Integer.parseInt(horaActual));
        return horaActual;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfaceRegistrarDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceRegistrarDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceRegistrarDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceRegistrarDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceRegistrarDevolucion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnControlar;
    private javax.swing.JButton btnRegistrar;
    public static javax.swing.JComboBox<String> cbQuirofano;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    public static javax.swing.JTable jTable1;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblEspecialidad;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTextField txtIdEnfermero;
    private javax.swing.JTextField txtRegistro;
    // End of variables declaration//GEN-END:variables
}

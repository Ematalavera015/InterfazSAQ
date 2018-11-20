/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.ventana;

import controlador.Metodos_db;
import controlador.Pool;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.entidad.Elemento;

/**
 *
 * @author Ema
 */
public class InterfaceRegistrarMatyEqu extends javax.swing.JFrame {

    int IdRegistrarPrestamo = 0;

    Metodos_db metodos = new Metodos_db();

    String fechaActual, horaActual;

    private DefaultTableModel modelo1;
    public static DefaultComboBoxModel modeloComboQuirofano, modeloComboTipoElemento, modeloComboElemento;

    private String[] datos = new String[3];
    private String[] encabezado = {"Elemento", "Descripcion", "Cantidad"};

    public InterfaceRegistrarMatyEqu() {
        initComponents();
        IdRegistrarPrestamo = obtenerIdRegistro();
        txtPrestamo.setText(integerFormat1(IdRegistrarPrestamo));
        //Obtener Fecha
        fechaActual = ObtenerFecha();
        horaActual = ObtenerHora();

        lblFecha.setText(fechaActual);
        lblHora.setText(horaActual);
        /////////////////////////////////77

        this.setTitle("SAQ - Materiales y Equipos");
        //this.setLocationRelativeTo(InterfaceRegistrarMatyEqu.this);
        this.setLocationRelativeTo(null);

        ControlQuirofano();
        metodos.consultar();

        //this.setResizable(false);
        //setIconImage(new ImageIcon(getClass().getResource("../imagen/logo_SAQ.png")).getImage());

        //Carga de tabla
        modelo1 = new DefaultTableModel();
        modelo1.addColumn("Elemento");
        modelo1.addColumn("Descripcion");
        modelo1.addColumn("Cantidad");

        jTable1.setModel(modelo1);

        ///////////////////////////////////////////////////////////
        //Carga del combobox de elementos de quirofano
        cbTipoElemento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                metodos.consultar();
            }
        });

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
                        if(!rs.getBoolean("ocupado")){
                            list_numQuirofano.add(rs.getInt("numero"));
                        }
                        

                    }

                }

                //if (condicional == true) {
                    InterfaceRegistrarMatyEqu.modeloComboQuirofano = new DefaultComboBoxModel(to_array(list_numQuirofano));
                    InterfaceRegistrarMatyEqu.cbQuirofano.setModel(InterfaceRegistrarMatyEqu.modeloComboQuirofano);
                //}

                cn.close();
            }
        } catch (SQLException e) {
        }

    }

    public Integer[] to_array(ArrayList<Integer> list) {

        Integer array[] = new Integer[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);

        }

        return array;
    }

    //  control de prestamos y quirofanos
    //se controla que hasta que un quirofano no se devuelva el prestamo, éste no aparezca en el ComboBox y no pueda ser seleccionado
    public void ControlQuirofano() {

        String fecha = "";
        Pool cc = new Pool();
        ResultSet rs, rs1;

        try {

            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM prestamo");

            rs = pst.executeQuery();
            rs.next();

            if (rs.getRow() > 0) {
                rs.previous();
                while (rs.next()) {
                    fecha = rs.getString("fecha");
                }
            }
            System.out.println("FECHA "+fecha);
            System.out.println("COMPARACION: "+fecha.compareTo(ObtenerFecha()));
            if (fecha.compareTo(ObtenerFecha()) < 0) {
                PreparedStatement pst1 = cn.prepareStatement("UPDATE quirofano SET ocupado = false");
                pst1.executeUpdate();
            } 

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        cargarQuirofano();
    }

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
        //      + dia + "/" + (mes + 1) + "/" + año);
        //System.out.printf("Hora Actual: %02d:%02d:%02d %n",
        //     hora, minuto, segundo);
        //horaActual =integerFormat(Integer.parseInt(horaActual));
        return horaActual;
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

    public static int obtenerIdRegistro1() {

        int idRegistro = 0;
        String fecha = "";
        Pool cc = new Pool();
        ResultSet rs1, rs2;

        Elemento elemento = new Elemento();

        try {
            System.out.println("Antes de la conecion");
            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM prestamo");// WHERE nombres= '" + InterfaceRegistrarMatyEqu.cbElementos.getSelectedItem() + "'");
            System.out.println("Despues de la consulta");
            rs1 = pst.executeQuery();
            //System.out.println("Antes del if1 " + rs1.next());
            //System.out.println("Antes del if2 " + rs1.next());
            rs1.next();
            System.out.println("Cantidad de filas: " + rs1.getRow());
            if (rs1.getRow() == 0) {
                System.out.println("No tiene datos");
                idRegistro = 0;
            } else {

                //System.out.println("Tiene datos y " + rs1.next());
                //rs.beforeFirst();
                //rs1.next();
                //boolean booli = rs1.next();
                //fecha = rs1.getString("fecha");
                //System.out.println("ccc " + booli);
                /*PreparedStatement ps = cn.prepareStatement("SELECT * FROM prestamo");// WHERE nombres= '" + InterfaceRegistrarMatyEqu.cbElementos.getSelectedItem() + "'");
                    System.out.println("Despues de la consulta");
                       rs1 = pst.executeQuery();*/
                while (rs1.next()) {
                    System.out.println("Entro al while");
                    fecha = rs1.getString("fecha");
                    System.out.println("Fecha: " + rs1.getString("fecha"));
                }
                rs1.close();
            }

            if (ObtenerFecha().equals(fecha)) {
                System.out.println("Entro al if de comparacion de fecha");
                PreparedStatement pst1 = cn.prepareStatement("SELECT idprestamo FROM prestamo WHERE fecha = '" + fecha + "'");
                System.out.println("Paso la sql");

                rs2 = pst1.executeQuery();
                System.out.println("ID prestamo antes del next: " + rs2.getInt("idprestamo"));
                rs2.next();
                System.out.println("ID prestamo despues del next: " + rs2.getInt("idprestamo"));
                while (rs2.next()) {
                    idRegistro = rs2.getInt("idprestamo");
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

    public static int obtenerIdRegistro() {

        int idRegistro = 0;
        String fecha = "";
        Pool cc = new Pool();
        ResultSet rs, rs1;

        try {
            System.out.println("Antes de la conecion");
            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM prestamo");// WHERE nombres= '" + InterfaceRegistrarMatyEqu.cbElementos.getSelectedItem() + "'");
            System.out.println("Despues de la consulta");
            rs = pst.executeQuery();
            rs.next();
            System.out.println("Cantidad de filas: " + rs.getRow());
            if (rs.getRow() == 0) {
                System.out.println("No tiene datos");
                idRegistro = 0;
            } else {
                System.out.println("Tiene datos");
                rs.previous();
                while (rs.next()) {
                    System.out.println("Entro al while");
                    fecha = rs.getString("fecha");
                    System.out.println("Fechaaaaa: " + rs.getString("fecha"));
                }

                /*if(ObtenerFecha().equals()){
                    
                }*/
            }
            System.out.println("Fecha Antes del if eqquals: " + fecha);
            if (ObtenerFecha().equals(fecha)) {
                PreparedStatement pst1 = cn.prepareStatement("SELECT idprestamo FROM prestamo WHERE fecha = '" + fecha + "' ORDER BY idprestamo ASC");
                rs1 = pst1.executeQuery();
                //rs.next();
                while (rs1.next()) {
                    System.out.println("Regitros: " + rs1.getInt("idprestamo"));
                    idRegistro = rs1.getInt("idprestamo");
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
        txtPrestamo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        cbQuirofano = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        TablaMatYEquipo = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        cbElementos = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cbTipoElemento = new javax.swing.JComboBox<>();
        btnAgregar = new javax.swing.JButton();
        lblApellido = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblEspecialidad = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        btnQuitar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 660));
        setPreferredSize(new java.awt.Dimension(800, 660));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 255, 204));
        jLabel2.setText("N° Registro:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 90, 30));

        jLabel3.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 255, 204));
        jLabel3.setText("Fecha :");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 60, 30));

        jLabel4.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 255, 204));
        jLabel4.setText("Hora :");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 50, 30));

        txtPrestamo.setEditable(false);
        txtPrestamo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrestamoActionPerformed(evt);
            }
        });
        getContentPane().add(txtPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 120, 30));

        jLabel5.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 255, 204));
        jLabel5.setText("ID Enfermero:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 230, 40));

        jLabel6.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 255, 204));
        jLabel6.setText("N° Quirófano:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 140, 40));

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 300, 30));

        cbQuirofano.setFont(new java.awt.Font("Arial Narrow", 1, 20)); // NOI18N
        cbQuirofano.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbQuirofanoItemStateChanged(evt);
            }
        });
        cbQuirofano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbQuirofanoActionPerformed(evt);
            }
        });
        getContentPane().add(cbQuirofano, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 60, 30));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 580, 100, 30));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 300, 580, 20));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setPreferredSize(new java.awt.Dimension(50, 10));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 800, 20));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 255, 204));
        jLabel7.setText("Materiales y Equipos");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 350, 30));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 255, 204));
        jLabel8.setText("Apellido:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 80, 30));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 255, 204));
        jLabel9.setText("Nombre:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 80, 30));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 255, 204));
        jLabel10.setText("Especialidad:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, 100, 30));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 800, 20));

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 620, 20));

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 620, 20));

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 800, 20));

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 800, 20));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 255, 204));
        jLabel11.setText("Datos del Enfermero");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 250, 30));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 100, 30));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setText("Registrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 580, 100, 30));

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
        TablaMatYEquipo.setViewportView(jTable1);

        getContentPane().add(TablaMatYEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 480, 640, 90));

        jSeparator8.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 800, 20));

        jSeparator9.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 800, 20));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 255, 204));
        jLabel12.setText("Detalle de Materiales y Equipos");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, 350, 30));

        getContentPane().add(cbElementos, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 360, 220, 30));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 255, 204));
        jLabel13.setText("Elemento :");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 360, 120, 30));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 255, 204));
        jLabel14.setText("Cantidad :");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 360, 100, 30));

        jTextField3.setToolTipText("");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 360, 90, 30));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(204, 255, 204));
        jLabel15.setText("Tipo :");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 80, 30));

        cbTipoElemento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoElementoActionPerformed(evt);
            }
        });
        getContentPane().add(cbTipoElemento, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 360, 140, 30));

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.setName(""); // NOI18N
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 400, 100, 30));

        lblApellido.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblApellido.setForeground(new java.awt.Color(255, 255, 255));
        lblApellido.setName(""); // NOI18N
        getContentPane().add(lblApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 550, 30));

        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setName(""); // NOI18N
        getContentPane().add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 550, 30));

        lblEspecialidad.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblEspecialidad.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(lblEspecialidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 520, 30));

        lblFecha.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(153, 255, 204));
        getContentPane().add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 210, 30));

        lblHora.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblHora.setForeground(new java.awt.Color(153, 255, 204));
        getContentPane().add(lblHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 110, 30));

        btnQuitar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnQuitar.setText("Quitar");
        btnQuitar.setName(""); // NOI18N
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 400, 100, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/FONDO SAQ.jpg"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(800, 620));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void cbQuirofanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbQuirofanoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbQuirofanoActionPerformed

    private void cbQuirofanoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbQuirofanoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbQuirofanoItemStateChanged

    private void txtPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrestamoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrestamoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //Registrar el prestamo
        Pool cc = new Pool();
        ResultSet rs;

        int cantidadmaterial = 0, cantidadequipo = 0;

        String nombreElmento = "";
        int cantElemento = 0;

        //Prestamo elemento = new Elemento();
        try {

            Connection cn = cc.dataSource.getConnection();
            //PreparedStatement pst = cn.prepareStatement("INSERT INTO prestamo(idprestamo,fecha,hora,idenfermero,idquirofano) VALUES(?,?,?,?,?,?,?)");

            String idpersonal = jTextField2.getText();
            if (idpersonal.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos", "Mensaje", JOptionPane.WARNING_MESSAGE);
            } else {
                if (jTable1.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Debe cargar materiales y/o equipos", "Mensaje", JOptionPane.WARNING_MESSAGE);
                } else {
                    PreparedStatement pst = cn.prepareStatement("INSERT INTO prestamo(idprestamo,fecha,hora,idpersonal,idquirofano) VALUES(?,?,?,?,?)");

                    pst.setInt(1, IdRegistrarPrestamo);
                    pst.setString(2, fechaActual);
                    pst.setString(3, horaActual);
                    pst.setInt(4, Integer.parseInt(idpersonal));
                    pst.setInt(5, Integer.parseInt(cbQuirofano.getSelectedItem().toString()));

                    int a = pst.executeUpdate();
                    if (a > 0) {
                        Icon m = new ImageIcon(getClass().getResource("../imagen/tilde.png"));
                        //JOptionPane.showMessageDialog(null, "Registro exitoso", "Mensaje", JOptionPane.WARNING_MESSAGE, m);
                    } else {
                        //JOptionPane.showMessageDialog(null, "Error al agregar");
                    }

                    //cargando la tabla prestamo elemento
                    //boolean c = false;
                    int b = 0;
                    for (int i = 0; i < jTable1.getRowCount(); i++) {

                        nombreElmento = jTable1.getValueAt(i, 0).toString();
                        //jTable1.getValueAt(i, 1).toString();
                        System.out.println(jTable1.getValueAt(i, 0).toString() + "-");
                        PreparedStatement pst2 = cn.prepareStatement("SELECT idelemento FROM elemento WHERE nombreelemento = '" + nombreElmento + "'");

                        rs = pst2.executeQuery();
                        rs.next();

                        PreparedStatement pst1 = cn.prepareStatement("INSERT INTO prestamo_elemento(idprestamo,fecha,idelemento,cantidad) VALUES(?,?,?,?)");
                        pst1.setInt(1, IdRegistrarPrestamo);
                        pst1.setString(2, fechaActual);
                        pst1.setInt(3, rs.getInt("idelemento"));
                        System.out.println(jTable1.getValueAt(i, 1).toString());
                        pst1.setInt(4, Integer.parseInt(jTable1.getValueAt(i, 2).toString()));

                        b = pst1.executeUpdate();

                    }
                    if (b > 0) {

                        Icon m = new ImageIcon(getClass().getResource("../imagen/tilde.png"));
                        JOptionPane.showMessageDialog(null, "Registro exitoso", "Mensaje", JOptionPane.WARNING_MESSAGE, m);

                    } else {

                        JOptionPane.showMessageDialog(null, "Error al agregar", "Mensaje", JOptionPane.ERROR_MESSAGE);

                    }
                    
                    PreparedStatement pst2 = cn.prepareStatement("UPDATE quirofano SET ocupado = true WHERE idquirofano = "+ cbQuirofano.getSelectedItem().toString());
                       
                    pst2.executeUpdate();
                    

                    InterfaceMenu menu = new InterfaceMenu();
                    menu.setVisible(true);
                    this.setVisible(false);

                }

            }

        } catch (SQLException e) {
            System.out.println("Error Registarr: " + e);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    public int obtenerCantidad(int i) {
        int cant = 0, col, filas;

        Pool cc = new Pool();
        ResultSet rs;

        try {
            Connection cn = cc.dataSource.getConnection();
            if (i == 1) {

                if (jTable1.getRowCount() == 0 && jTable1.getSelectedRow() == -1) {

                    JOptionPane.showMessageDialog(null, "Debe cargar materiales y/o equipos", "Mensaje", JOptionPane.WARNING_MESSAGE);

                } else {

                    filas = jTable1.getRowCount();
                    col = jTable1.getColumnCount() - 1;
                    for (int j = 0; j < filas; j++) {

                        String nombreElemento = jTable1.getValueAt(j, 0).toString();

                        PreparedStatement pst = cn.prepareStatement("SELECT * FROM elemento WHERE nombreelemento= '" + nombreElemento + "'");

                        rs = pst.executeQuery();
                        rs.next();

                        if (1 == rs.getInt("idtipo")) {

                            //System.out.println("id_tipo " + rs.getInt("id_tipo"));
                            cant = cant + Integer.parseInt(jTable1.getValueAt(j, col).toString());
                            //System.out.println("cantidad material: " + jTable1.getValueAt(j, col));

                        }
                    }

                }
            } else {

                if (jTable1.getRowCount() == 0 && jTable1.getSelectedRow() == -1) {

                    JOptionPane.showMessageDialog(null, "Debe cargar materiales y/o equipos", "Mensaje", JOptionPane.WARNING_MESSAGE);

                } else {

                    filas = jTable1.getRowCount();
                    col = jTable1.getColumnCount() - 1;
                    for (int j = 0; j < filas; j++) {

                        String nombreElemento = jTable1.getValueAt(j, 0).toString();

                        PreparedStatement pst = cn.prepareStatement("SELECT * FROM elemento WHERE nombreelemento= '" + nombreElemento + "'");

                        rs = pst.executeQuery();
                        rs.next();

                        if (2 == rs.getInt("idtipo")) {

                            System.out.println("idtipo" + rs.getInt("idtipo"));
                            cant = cant + Integer.parseInt(jTable1.getValueAt(j, col).toString());
                            System.out.println("cantidad equipo: " + jTable1.getValueAt(j, col));
                        }
                    }

                }

            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return cant;

    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        InterfaceMenu menu = new InterfaceMenu();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        Pool cc = new Pool();
        ResultSet rs;

        Elemento elemento = new Elemento();

        try {
            Connection cn = cc.dataSource.getConnection();
            PreparedStatement pst = cn.prepareStatement("SELECT * FROM elemento WHERE nombreelemento= '" + InterfaceRegistrarMatyEqu.cbElementos.getSelectedItem() + "'");

            rs = pst.executeQuery();

            rs.next();

            elemento.setIdelemento(rs.getInt("idelemento"));
            elemento.setNombre(rs.getString("nombreelemento"));
            elemento.setDescripcion(rs.getString("descripcionelemento"));
            //elemento.setEstado(rs.getString("estado"));

            datos[0] = elemento.getNombre();
            datos[1] = elemento.getDescripcion();

            if (jTextField3.getText().length() != 0) {
                if (Integer.parseInt(jTextField3.getText()) <= 0) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar numero válido", "Mensaje", JOptionPane.ERROR_MESSAGE);
                } else {
                    datos[2] = jTextField3.getText();
                    System.out.println("Se guarda: " + datos[2]);
                    modelo1.addRow(datos);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar cantidad", "Mensaje", JOptionPane.ERROR_MESSAGE);
            }

            jTable1.setModel(modelo1);

        } catch (SQLException e) {
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (jTextField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe llenar campo ID Enfermero", "Mensaje", JOptionPane.ERROR_MESSAGE);
        } else {
            int idEnfermero = Integer.parseInt(jTextField2.getText());
            Pool cc = new Pool();
            ResultSet rs;

            try {
                Connection cn = cc.dataSource.getConnection();
                PreparedStatement pst = cn.prepareStatement("SELECT * FROM personal WHERE idpersonal= '" + Integer.parseInt(jTextField2.getText()) + "'");

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
            }

        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            modelo1.removeRow(jTable1.getSelectedRow());
        } else {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún registro!!!", "Mensaje", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        char tecla;

        tecla = evt.getKeyChar();

        if (!Character.isDigit(tecla) && tecla != KeyEvent.VK_BACK_SPACE) {

            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Debe ingresar dato numérico!!!", "Mensaje", JOptionPane.WARNING_MESSAGE);

        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        char tecla;

        tecla = evt.getKeyChar();

        if (!Character.isDigit(tecla) && tecla != KeyEvent.VK_BACK_SPACE) {

            evt.consume();
            getToolkit().beep();

            JOptionPane.showMessageDialog(null, "Debe ingresar dato numérico!!!", "Mensaje", JOptionPane.WARNING_MESSAGE);

        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void cbTipoElementoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoElementoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoElementoActionPerformed

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
            java.util.logging.Logger.getLogger(InterfaceRegistrarMatyEqu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceRegistrarMatyEqu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceRegistrarMatyEqu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceRegistrarMatyEqu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceRegistrarMatyEqu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane TablaMatYEquipo;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnQuitar;
    public static javax.swing.JComboBox<String> cbElementos;
    public static javax.swing.JComboBox<String> cbQuirofano;
    public static javax.swing.JComboBox<String> cbTipoElemento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblEspecialidad;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTextField txtPrestamo;
    // End of variables declaration//GEN-END:variables
}

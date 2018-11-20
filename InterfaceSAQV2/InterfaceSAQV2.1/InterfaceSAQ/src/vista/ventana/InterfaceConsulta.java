package vista.ventana;

import javax.swing.ImageIcon;

public class InterfaceConsulta extends javax.swing.JFrame {

    
    public InterfaceConsulta() {
        initComponents();
        
        this.setTitle("SAQ - Consulta Materiales y Equipos");
        //setIconImage(new ImageIcon(getClass().getResource("../imagen/logo_SAQ.png")).getImage());
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        btnPrestamos = new javax.swing.JButton();
        btnDevolucion = new javax.swing.JButton();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Arial Narrow", 1, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 255, 204));
        jLabel8.setText("Consultar Devolución de M y E");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, 280, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/logo_SAQ.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 200, 210));

        btnVolver.setFont(new java.awt.Font("Arial Narrow", 1, 22)); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolver.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        getContentPane().add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 390, -1, 37));

        jLabel9.setFont(new java.awt.Font("Arial Narrow", 1, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 255, 204));
        jLabel9.setText("Consultar Prestamos de M y E");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 280, 30));

        btnPrestamos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/CMat.png"))); // NOI18N
        btnPrestamos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrestamos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrestamos.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/CMat2.png"))); // NOI18N
        btnPrestamos.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/CMat.png"))); // NOI18N
        btnPrestamos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestamosActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrestamos, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 280, 110));

        btnDevolucion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/dMat.jpg"))); // NOI18N
        btnDevolucion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDevolucion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDevolucion.setMaximumSize(new java.awt.Dimension(377, 109));
        btnDevolucion.setMinimumSize(new java.awt.Dimension(377, 109));
        btnDevolucion.setPreferredSize(new java.awt.Dimension(377, 109));
        btnDevolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolucionActionPerformed(evt);
            }
        });
        getContentPane().add(btnDevolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 230, 280, 110));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imagen/FONDO SAQ.jpg"))); // NOI18N
        lblFondo.setMinimumSize(new java.awt.Dimension(820, 600));
        lblFondo.setPreferredSize(new java.awt.Dimension(800, 500));
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 610));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrestamosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrestamosActionPerformed
        // TODO add your handling code here:
        InterfaceConsultarPrestamo o = new InterfaceConsultarPrestamo();
        o.setVisible(true);
        this.setVisible(false);
        //o.show();
    }//GEN-LAST:event_btnPrestamosActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        InterfaceMenu menu = new InterfaceMenu();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnDevolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolucionActionPerformed
        // TODO add your handling code here:
        InterfaceConsultarDevolucion o = new InterfaceConsultarDevolucion();
        o.setVisible(true);
        this.setVisible(false);
        //o.show();
    }//GEN-LAST:event_btnDevolucionActionPerformed

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
            java.util.logging.Logger.getLogger(InterfaceConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceConsulta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDevolucion;
    private javax.swing.JButton btnPrestamos;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblFondo;
    // End of variables declaration//GEN-END:variables
}

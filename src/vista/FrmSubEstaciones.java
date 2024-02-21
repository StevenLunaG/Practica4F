package vista;

import controlador.SubEstacionControl;
import controlador.SubEstacionDao;
import controlador.utiles.Utiles;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.Coordenada;
import vista.tabla.ModeloTablaSub;

public class FrmSubEstaciones extends javax.swing.JFrame {

    private SubEstacionDao fileSub = new SubEstacionDao();
    private SubEstacionControl subControl = new SubEstacionControl();
    private DefaultListModel modeloLista = new DefaultListModel();
    private ModeloTablaSub st = new ModeloTablaSub();
    private FrmGrafos fg = new FrmGrafos();

    public FrmSubEstaciones() {
        initComponents();
        limpiar();
    }

    private File CargarFoto() throws Exception {
        File obj = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter
                = new FileNameExtensionFilter("Imagenes", "jpg", "png", "jpeg");
        chooser.addChoosableFileFilter(filter);
        Integer resp = chooser.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            obj = chooser.getSelectedFile();
            System.out.println("\nCargado: " + obj.getName());
        } else {
            System.out.println("\nNo Cargado");
        }
        return obj;
    }

    private Boolean verificar() {
        return (!txtLongitud.getText().trim().isEmpty()
                && !txtLatitud.getText().trim().isEmpty());
    }

    private String nombreArchivo(String file) {
        String aux = UUID.randomUUID().toString();
        aux += "." + Utiles.extension(file);
        return aux;
    }

    private void guardarFotos(String ruta) throws Exception {
        String[] fotos = new String[modeloLista.getSize()];
        Path dir = Paths.get(ruta);
        Files.createDirectory(dir);
        for (int i = 0; i < modeloLista.size(); i++) {
            File f = (File) modeloLista.getElementAt(i);
            String pic = nombreArchivo(f.getName());
            fotos[i] = pic;
            Utiles.copiarArchivo(f, new File(ruta + "/" + pic));
        }
        subControl.getSubEstacion().setFotos(fotos);
    }

    private void guardar() throws Exception {
        if (verificar()) {

            subControl.getSubEstacion().setGeoPosicion(new Coordenada(Double.valueOf(txtLongitud.getText()), Double.valueOf(txtLatitud.getText())));
            String ruta = "Foto/" + subControl.getSubEstacion().getGeoPosicion().toString();
            guardarFotos(ruta);

            if (subControl.guardar()) {
                fileSub.setSubEstacion(subControl.getSubEstacion());
                fileSub.persist();
                JOptionPane.showMessageDialog(null, "Datos guardados");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar, hubo un error");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Falta llenar campos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        st.setSubEstaciones(fileSub.all());
        tbSubs.setModel(st);
        tbSubs.updateUI();
    }

    private void limpiar() {
        pnlImagen.setVisible(false);
        txtLongitud.setText("");
        txtLatitud.setText("");
        
        cargarTabla();

        txtLongitud.setEnabled(true);
        txtLatitud.setEnabled(true);
        modeloLista.removeAllElements();
        btSubir.setEnabled(true);
        btRemover.setEnabled(true);
        btRegistrar.setEnabled(true);
        btGuardarC.setEnabled(false);
        btModificar.setEnabled(false);
        subControl.setSubEstacion(null);
    }

    private void cargar(Integer dato) {
        try {
            subControl.setSubEstacion(fileSub.getLista().getInfo(dato));

            txtLongitud.setText(subControl.getSubEstacion().getGeoPosicion().getLongitud().toString());
            txtLatitud.setText(subControl.getSubEstacion().getGeoPosicion().getLatitud().toString());
            String ruta = "Foto/" + subControl.getSubEstacion().getGeoPosicion().toString() + "/";
            String fotos[] = subControl.getSubEstacion().getFotos();
            modeloLista.removeAllElements();

            for (int i = 1; i <= subControl.getSubEstacion().getFotos().length; i++) {
                File foto = new File(ruta + fotos[i - 1]);
                modeloLista.addElement(foto);
            }
            lstFotos.setModel(modeloLista);

            txtLongitud.setEnabled(false);
            txtLatitud.setEnabled(false);
            btSubir.setEnabled(false);
            btRemover.setEnabled(false);
            btRegistrar.setEnabled(false);
            btModificar.setEnabled(true);
        } catch (Exception ex) {
            Logger.getLogger(FrmSubEstaciones.class.getName()).log(Level.SEVERE, "Error al cargar", ex);
        }
    }

    private void modificar() throws Exception {
        if (verificar()) {
            String prevGeoP = subControl.getSubEstacion().getGeoPosicion().toString();
            subControl.getSubEstacion().setGeoPosicion(new Coordenada(Double.valueOf(txtLongitud.getText()), Double.valueOf(txtLatitud.getText())));
            String newGeoP = subControl.getSubEstacion().getGeoPosicion().toString();
            if (!prevGeoP.equals(newGeoP)) {
                int i = JOptionPane.showConfirmDialog(null, "Al cambiar las coordenadas de GeoPosicion se cambiara el directorio de almacenamiento de las fotos. ¿Deseas continuar?", "ADVERTENCIA", JOptionPane.OK_CANCEL_OPTION);
                if (i == JOptionPane.OK_OPTION) {
                    String ruta = "Foto/" + newGeoP;
                    guardarFotos(ruta);
                    String dir = "Foto/" + prevGeoP;
                    eliminarCarpeta(new File(dir));
                }
            } else {
                //Crear carpeta temporal para guardar las fotos
                String ruta = "Foto/temp";
                guardarFotos(ruta);
                //Eliminar carpeta anterior
                String dir = "Foto/" + newGeoP;
                eliminarCarpeta(new File(dir));
                //Renombrar carpeta temporal
                File temp = new File(ruta);
                temp.renameTo(new File(dir));
            }
            if (fileSub.merge(subControl.getSubEstacion(), subControl.getSubEstacion().getId())) {
                JOptionPane.showMessageDialog(null, "Cambios guardados");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar los cambios, hubo un error");
            }
        }
    }

    private static void eliminarCarpeta(File carpeta) {
        if (carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isDirectory()) {
                        eliminarCarpeta(archivo);
                    } else {
                        archivo.delete();
                    }
                }
            }
        }
        carpeta.delete();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbSubs = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtLongitud = new javax.swing.JTextField();
        btModificar = new javax.swing.JButton();
        pnlBgImagen = new javax.swing.JPanel();
        pnlImagen = new org.edisoncor.gui.panel.PanelImage();
        btRegistrar = new javax.swing.JButton();
        txtLatitud = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstFotos = new javax.swing.JList<>();
        btGuardarC = new javax.swing.JButton();
        btSubir = new javax.swing.JButton();
        btRemover = new javax.swing.JButton();
        btLimpiar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbSubs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "LONGITUD", "LATITUD"
            }
        ));
        tbSubs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSubsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbSubs);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 640, 170));

        jLabel1.setFont(new java.awt.Font("Roboto Black", 1, 14)); // NOI18N
        jLabel1.setText("Registrar Sub-Estacion");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jLabel2.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("(min. 2 - max. 3)");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, 20));

        jLabel3.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Longitud:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, -1, 30));

        txtLongitud.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jPanel1.add(txtLongitud, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 120, 30));

        btModificar.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btModificar.setText("Modificar");
        btModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btModificar.setEnabled(false);
        btModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 570, -1, -1));

        pnlBgImagen.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlImagenLayout = new javax.swing.GroupLayout(pnlImagen);
        pnlImagen.setLayout(pnlImagenLayout);
        pnlImagenLayout.setHorizontalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        pnlImagenLayout.setVerticalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlBgImagenLayout = new javax.swing.GroupLayout(pnlBgImagen);
        pnlBgImagen.setLayout(pnlBgImagenLayout);
        pnlBgImagenLayout.setHorizontalGroup(
            pnlBgImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBgImagenLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        pnlBgImagenLayout.setVerticalGroup(
            pnlBgImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBgImagenLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel1.add(pnlBgImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 60, 250, 220));

        btRegistrar.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btRegistrar.setText("Registrar");
        btRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegistrarActionPerformed(evt);
            }
        });
        jPanel1.add(btRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 310, -1, -1));

        txtLatitud.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jPanel1.add(txtLatitud, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 120, 30));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Latitud:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, -1, 30));

        lstFotos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstFotosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstFotos);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 210, 80));

        btGuardarC.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btGuardarC.setText("Guardar Cambios");
        btGuardarC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btGuardarC.setEnabled(false);
        btGuardarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuardarCActionPerformed(evt);
            }
        });
        jPanel1.add(btGuardarC, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, -1, -1));

        btSubir.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btSubir.setText("Subir Foto");
        btSubir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btSubir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSubirActionPerformed(evt);
            }
        });
        jPanel1.add(btSubir, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 220, 120, 20));

        btRemover.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btRemover.setText("Remover Foto");
        btRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoverActionPerformed(evt);
            }
        });
        jPanel1.add(btRemover, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, 120, 20));

        btLimpiar.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btLimpiar.setText("Limpiar");
        btLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(btLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 570, -1, -1));

        jLabel5.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Fotos:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, 20));

        jMenu1.setText("Calcular Grafos");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu1.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSubirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSubirActionPerformed
        if (modeloLista.getSize() < 3) {
            try {
                File foto = CargarFoto();
                if (foto != null) {
                    modeloLista.addElement(foto);
                }
                lstFotos.setModel(modeloLista);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", HEIGHT);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se excedio el numero maximo de fotos", "Error", HEIGHT);
        }
    }//GEN-LAST:event_btSubirActionPerformed

    private void lstFotosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstFotosMouseClicked
        Object f = lstFotos.getSelectedValue();
        File foto = (File) f;
        if (foto != null) {
            pnlImagen.setIcon(new ImageIcon(foto.getAbsolutePath()));
            pnlBgImagen.repaint();
            pnlImagen.setVisible(true);
        }
    }//GEN-LAST:event_lstFotosMouseClicked

    private void btRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoverActionPerformed
        Object f = lstFotos.getSelectedValue();
        File foto = (File) f;
        if (foto != null) {
            modeloLista.removeElement(foto);
            pnlImagen.setVisible(false);
        }
    }//GEN-LAST:event_btRemoverActionPerformed

    private void btRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegistrarActionPerformed
        if (modeloLista.getSize() >= 2) {
            try {
                guardar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar los datos", "Error", HEIGHT);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se requiere de minimo 2 fotos para registrar los datos", "Error", HEIGHT);
        }
    }//GEN-LAST:event_btRegistrarActionPerformed

    private void tbSubsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSubsMouseClicked
        int fila = tbSubs.getSelectedRow();
        cargar(fila);
    }//GEN-LAST:event_tbSubsMouseClicked

    private void btLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btLimpiarActionPerformed

    private void btModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btModificarActionPerformed
        txtLongitud.setEnabled(true);
        txtLatitud.setEnabled(true);
        btSubir.setEnabled(true);
        btRemover.setEnabled(true);
        btGuardarC.setEnabled(true);
    }//GEN-LAST:event_btModificarActionPerformed

    private void btGuardarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarCActionPerformed
        if (modeloLista.getSize() >= 2) {
            try {
                modificar();
            } catch (Exception ex) {
                Logger.getLogger(FrmSubEstaciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se requiere de minimo 2 fotos para registrar los datos", "Error", HEIGHT);
        }
    }//GEN-LAST:event_btGuardarCActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        fg.setVisible(true);
    }//GEN-LAST:event_jMenu1MouseClicked

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
            java.util.logging.Logger.getLogger(FrmSubEstaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmSubEstaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmSubEstaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmSubEstaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmSubEstaciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btGuardarC;
    private javax.swing.JButton btLimpiar;
    private javax.swing.JButton btModificar;
    private javax.swing.JButton btRegistrar;
    private javax.swing.JButton btRemover;
    private javax.swing.JButton btSubir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lstFotos;
    private javax.swing.JPanel pnlBgImagen;
    private org.edisoncor.gui.panel.PanelImage pnlImagen;
    private javax.swing.JTable tbSubs;
    private javax.swing.JTextField txtLatitud;
    private javax.swing.JTextField txtLongitud;
    // End of variables declaration//GEN-END:variables
}

package vista;

import controlador.SubEstacionDao;
import controlador.TDA.grafos.PaintGraph;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import vista.tabla.ModeloTablaGraph;
import vista.utiles.UtilesVistaSub;

public class FrmGrafos extends javax.swing.JFrame {

    private SubEstacionDao fileSub = new SubEstacionDao();
    private ModeloTablaGraph tg = new ModeloTablaGraph();

    public FrmGrafos() {
        initComponents();
        limpiar();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void cargarTabla() throws Exception {
        tg.setGrafo(fileSub.getGrafo());
        tg.fireTableDataChanged();
        tbAdyacencias.setModel(tg);
        tbAdyacencias.updateUI();
    }

    private void adyacencia(Integer var) {
        try {
            switch (var) {
                //Normal
                case 1:
                    Integer o = cbxOrigen.getSelectedIndex();
                    Integer d = cbxDestino.getSelectedIndex();
                    if (o.intValue() == d.intValue()) {
                        JOptionPane.showMessageDialog(null, "Escoja subestaciones diferentes");
                    } else {
                        Double dist = UtilesVistaSub.calcularDistanciaSub(fileSub.getLista().getInfo(o), fileSub.getLista().getInfo(d));
                        fileSub.getGrafo().insertEdgeE(fileSub.getLista().getInfo(o), fileSub.getLista().getInfo(d), dist);
                        JOptionPane.showMessageDialog(null, "Adyacencia Generada");
                    }
                    break;

                //Aleatoria
                case 2:
                            try {
                    tg.getGrafo().coneccionAleatoria();
                    // Verificar si el grafo está conectado utilizando DFS
                    boolean conectadoDFS = tg.getGrafo().dfs(1); // Se inicia el recorrido DFS desde el nodo 1
                    if (conectadoDFS) {
                        System.out.println("\n//Estado grafo conectado (DFS): CONECTADO//");
                    } else {
                        System.out.println("\n//Estado grafo conectado (DFS): DESCONECTADO//");
                    }

                    // Verificar si el grafo está conectado utilizando BFS
                    boolean conectadoBFS = tg.getGrafo().bfs(1); // Se inicia el recorrido BFS desde el nodo 1
                    if (conectadoBFS) {
                        System.out.println("\n//Estado grafo conectado (BFS): CONECTADO//");
                    } else {
                        System.out.println("\n//Estado grafo conectado (BFS): DESCONECTADO//");
                    }
                    cargarTabla();
                } catch (Exception ex) {
                    Logger.getLogger(FrmGrafos.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                default:
                    throw new AssertionError();
            }
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se puede generar la adyacencia", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarGrafo() throws Exception {
        PaintGraph p = new PaintGraph();
        p.update(fileSub.getGrafo(), fileSub.getGrafo());

        File nav = new File("d3/grafo.html");
        java.awt.Desktop.getDesktop().open(nav);
    }

    private void cargar() throws Exception {
        int i = JOptionPane
                .showConfirmDialog(null, "¿Estas seguro de cargar?", "ADVERTENCIA", JOptionPane.OK_CANCEL_OPTION);
        if (i == JOptionPane.OK_OPTION) {
            fileSub.loadGraph();
            limpiar();
        }
    }

    public void guardarGrafo() {
        try {
            int i = JOptionPane
                    .showConfirmDialog(null, "¿Estas seguro de guardar?", "ADVERTENCIA", JOptionPane.OK_CANCEL_OPTION);
            if (i == JOptionPane.OK_OPTION) {
                if (fileSub.getGrafo() != null) {
                    fileSub.guardarGrafo();
                    JOptionPane.showMessageDialog(null, "Grafo guardado", "GUARDADO", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se puede guardar un grafo vacio", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        try {
            UtilesVistaSub.cargarComboSub(cbxOrigen);
            UtilesVistaSub.cargarComboSub(cbxDestino);
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        txtRecorrido.setText("");
    }

    private void calcularTiempoAlgoritmo(int algoritmo) throws Exception {
        if (algoritmo == 0) {
            long inicioFloyd = System.nanoTime();
            System.out.println(tg.getGrafo().aplicarAlgoritmoFloydConEtiquetas());
            long finFloyd = System.nanoTime();
            long tiempoFloyd = finFloyd - inicioFloyd;
            System.out.println("Tiempo de ejecucion del algoritmo de Floyd: " + tiempoFloyd + " ns.\n");
            txtRecorrido.setText(tg.getGrafo().encontrarCaminoMasCorto(cbxOrigen.getSelectedIndex() + 1, cbxDestino.getSelectedIndex() + 1));
            inicioFloyd = 0;
            finFloyd = 0;
            tiempoFloyd = 0;
        } else {
            long inicioFloyd = System.nanoTime();
            System.out.println(tg.getGrafo().aplicarAlgoritmoBellmanFord(0));
            long finFloyd = System.nanoTime();
            long tiempoFloyd = finFloyd - inicioFloyd;
            System.out.println("Tiempo de ejecucin del algoritmo de Bellman: " + tiempoFloyd + " ns.\n");
            txtRecorrido.setText(tg.getGrafo().encontrarCaminoMasCorto(cbxOrigen.getSelectedIndex() + 1, cbxDestino.getSelectedIndex() + 1));
            inicioFloyd = 0;
            finFloyd = 0;
            tiempoFloyd = 0;
        }
    }

    private void recorrido() throws Exception {
        if (cbxRecorrido.getSelectedIndex() == 1) {
            calcularTiempoAlgoritmo(0);
        } else {
            calcularTiempoAlgoritmo(1);
        }
    }

    private void compAdy() throws Exception {
        if (cbxRecorrido.getSelectedIndex() == 0) {
            if (tg.getGrafo().dfs(1)) {
                JOptionPane.showMessageDialog(null, "El grafo esta compleatamente conectado");
            } else {
                JOptionPane.showMessageDialog(null, "El grafo no esta conectado");
            }
        } else {
            if (tg.getGrafo().bfs(1)) {
                JOptionPane.showMessageDialog(null, "El grafo esta compleatamente conectado");
            } else {
                JOptionPane.showMessageDialog(null, "El grafo no esta conectado");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        btCargar = new javax.swing.JButton();
        pnlsTabla = new javax.swing.JScrollPane();
        tbAdyacencias = new javax.swing.JTable();
        btGuardar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btAdyacencia = new javax.swing.JButton();
        cbxDestino = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbxOrigen = new javax.swing.JComboBox<>();
        btComprobar = new javax.swing.JButton();
        btRecorrido = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRecorrido = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        cbxAlgoritmo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbxRecorrido = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btAdyacencia1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btVerG = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btCargar.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btCargar.setText("Cargar");
        btCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCargarActionPerformed(evt);
            }
        });
        bg.add(btCargar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 500, -1, -1));

        tbAdyacencias.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        tbAdyacencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        pnlsTabla.setViewportView(tbAdyacencias);

        bg.add(pnlsTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 1030, 200));

        btGuardar.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btGuardar.setText("Guardar");
        btGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuardarActionPerformed(evt);
            }
        });
        bg.add(btGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 500, -1, -1));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btAdyacencia.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btAdyacencia.setText("Adyacencia");
        btAdyacencia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btAdyacencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAdyacenciaActionPerformed(evt);
            }
        });

        cbxDestino.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Roboto Medium", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Destino:");

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Origen:");

        cbxOrigen.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        btComprobar.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btComprobar.setText("Comprobar Adyacencia");
        btComprobar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btComprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btComprobarActionPerformed(evt);
            }
        });

        btRecorrido.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btRecorrido.setText("Calcular Recorrido");
        btRecorrido.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btRecorrido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRecorridoActionPerformed(evt);
            }
        });

        txtRecorrido.setColumns(20);
        txtRecorrido.setFont(new java.awt.Font("Roboto Black", 3, 14)); // NOI18N
        txtRecorrido.setRows(5);
        txtRecorrido.setEnabled(false);
        jScrollPane1.setViewportView(txtRecorrido);

        jLabel3.setFont(new java.awt.Font("Roboto Medium", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Algoritmo:");

        cbxAlgoritmo.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbxAlgoritmo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bellman-Ford", "Floyd" }));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Recorrido:");

        cbxRecorrido.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbxRecorrido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Profundidad", "Anchura" }));

        jLabel5.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Adyacencias");

        btAdyacencia1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btAdyacencia1.setText("Adyacencia Aleatoria");
        btAdyacencia1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btAdyacencia1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAdyacencia1ActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btAdyacencia1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(btAdyacencia, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(2, 2, 2)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(cbxDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(cbxOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(24, 24, 24)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbxRecorrido, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btComprobar)
                                        .addComponent(btRecorrido, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbxAlgoritmo, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jSeparator1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxAlgoritmo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbxRecorrido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btAdyacencia)
                                    .addComponent(btRecorrido))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btAdyacencia1)
                                    .addComponent(btComprobar))))
                        .addGap(16, 16, 16))))
        );

        bg.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1030, 240));

        btVerG.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btVerG.setText("Ver Grafo");
        btVerG.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btVerG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btVerGActionPerformed(evt);
            }
        });
        bg.add(btVerG, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 500, 96, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCargarActionPerformed
        try {
            cargar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btCargarActionPerformed

    private void btGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarActionPerformed
        guardarGrafo();
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btAdyacenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAdyacenciaActionPerformed
        adyacencia(1);
    }//GEN-LAST:event_btAdyacenciaActionPerformed

    private void btVerGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btVerGActionPerformed
        try {
            mostrarGrafo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar el grafo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btVerGActionPerformed

    private void btComprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btComprobarActionPerformed
        try {
            compAdy();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede comprobar la adyacencia", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btComprobarActionPerformed

    private void btRecorridoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRecorridoActionPerformed
        try {
            recorrido();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se calcular el recorrido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btRecorridoActionPerformed

    private void btAdyacencia1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAdyacencia1ActionPerformed
        limpiar();
        adyacencia(2);
    }//GEN-LAST:event_btAdyacencia1ActionPerformed

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
            java.util.logging.Logger.getLogger(FrmGrafos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmGrafos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmGrafos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmGrafos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmGrafos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton btAdyacencia;
    private javax.swing.JButton btAdyacencia1;
    private javax.swing.JButton btCargar;
    private javax.swing.JButton btComprobar;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btRecorrido;
    private javax.swing.JButton btVerG;
    private javax.swing.JComboBox<String> cbxAlgoritmo;
    private javax.swing.JComboBox<String> cbxDestino;
    private javax.swing.JComboBox<String> cbxOrigen;
    private javax.swing.JComboBox<String> cbxRecorrido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JScrollPane pnlsTabla;
    private javax.swing.JTable tbAdyacencias;
    private javax.swing.JTextArea txtRecorrido;
    // End of variables declaration//GEN-END:variables
}

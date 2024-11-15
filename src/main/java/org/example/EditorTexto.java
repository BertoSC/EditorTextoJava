package org.example;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EditorTexto {
    JFrame ventana;
    JTextArea ventanaText;
    JFileChooser fc;
    boolean modificado;
    String archivoActual;

    public EditorTexto() {
        this.ventana = new JFrame("Editor de texto");
        this.ventanaText = new JTextArea();
        this.fc= new JFileChooser();
        this.modificado = false;
        this.archivoActual=null;
        ejecutarEditor();
    }

    private void ejecutarEditor() {

        // Configuración de ventana general

        ventana.setSize(600,600);
        ventana.setMinimumSize(new Dimension(200, 200));
        ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        Font latoFont= importarFuente();

        // Configuración del textarea

        ventanaText.setLineWrap(true);
        ventanaText.setWrapStyleWord(true);
        ventanaText.setFont(latoFont);
        ventanaText.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Creación del scroll

        JScrollPane scrollPane = new JScrollPane(ventanaText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Creación del menú de opciones

        JMenuBar menu = new JMenuBar();
        menu.setBackground(new Color(240, 240,240));

        JMenu archivo = new JMenu("ARCHIVO");
        archivo.setBorder(new EmptyBorder(14, 10, 14, 8));
        archivo.setFont(latoFont);

        JMenuItem nuevo = new JMenuItem("Nuevo");
        nuevo.setBorder(new EmptyBorder(14, 10, 14, 0));
        nuevo.setBackground(new Color(240, 240,240));
        nuevo.setFont(latoFont);
        nuevo.setPreferredSize(new Dimension(200, 50));

        JMenuItem abrir = new JMenuItem("Abrir");
        abrir.setBorder(new EmptyBorder(14, 10, 14, 0));
        abrir.setBackground(new Color(240, 240,240));
        abrir.setFont(latoFont);
        abrir.setPreferredSize(new Dimension(200, 50));

        JMenuItem guardar = new JMenuItem("Guardar");
        guardar.setBorder(new EmptyBorder(14, 10, 14, 0));
        guardar.setBackground(new Color(240, 240,240));
        guardar.setHorizontalTextPosition(SwingConstants.CENTER);
        guardar.setFont(latoFont);
        guardar.setPreferredSize(new Dimension(200, 50));

        JMenuItem guardarComo = new JMenuItem("Guardar como");
        guardarComo.setBorder(new EmptyBorder(14, 10, 14, 0));
        guardarComo.setBackground(new Color(240, 240,240));
        guardarComo.setHorizontalTextPosition(SwingConstants.CENTER);
        guardarComo.setPreferredSize(new Dimension(200, 50));
        guardarComo.setFont(latoFont);

        JMenuItem cerrar = new JMenuItem("Cerrar");
        cerrar.setBorder(new EmptyBorder(14, 10, 14, 0));
        cerrar.setBackground(new Color(240, 240,240));
        cerrar.setFont(latoFont);
        cerrar.setPreferredSize(new Dimension(200, 50));

        nuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoDocumento();
            }
        });

        abrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirArchivo();
            }
        });

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarArchivo();
            }
        });

        guardarComo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComo();
            }
        });

        cerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarAplicacion();
            }
        });

        archivo.add(nuevo);
        archivo.add(abrir);
        archivo.add(guardar);
        archivo.add(guardarComo);
        archivo.addSeparator();
        archivo.add(cerrar);
        menu.add(archivo);

        ventana.add(scrollPane,BorderLayout.CENTER);
        ventana.setJMenuBar(menu);
        ventana.setVisible(true);

        // Atajos de teclado

        nuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        abrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        guardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        guardarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        cerrar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));


        // Detectar modificaciones no guardadas

        ventanaText.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                modificado = true;
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                modificado = true;
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                modificado = true;
            }
        });

        // Configuración del cierre de la APP asociado al método de cierre

        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               cerrarAplicacion();
            }
        });

    }

    // Método para importar una fuente

    private Font importarFuente(){
        Font latoFont = null;
        try {
            latoFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Lato-Regular.ttf")).deriveFont(14f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            latoFont = new Font("SansSerif", Font.PLAIN, 18);
        }
        return latoFont;

    }

    // Método para resetear a nuevo documento

    private void nuevoDocumento(){
        ventanaText.setText("");
        archivoActual=null;
        modificado=false;
    }


    // Método para abrir un archivo

    private void abrirArchivo() {
        int opcion= fc.showOpenDialog(null);
        File f = null;
        if (opcion==JFileChooser.APPROVE_OPTION){
            f = fc.getSelectedFile();
            if (f.exists()) {
                try {
                    ventanaText.setText("");
                    String contenido = Files.readString(f.toPath());
                    ventanaText.setText(contenido);
                    modificado=false;
                    archivoActual=f.getAbsolutePath();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El archivo no existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    // Método para guardar archivo que te detecta, después de guardarlo una vez, si estás trabajando en el archivo ya guardado o cargado

    private void guardarArchivo() {
        if (archivoActual==null){
        int opcion = fc.showSaveDialog(null);
        File f = null;
        if (opcion == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".txt")) {
                f = new File(f.getAbsolutePath() + ".txt");
            }
            try (var bw = Files.newBufferedWriter(f.toPath())) {
                bw.write(ventanaText.getText());
                modificado=false;
                archivoActual=f.getAbsolutePath();
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error en la operación", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        } else {
            File f= new File(archivoActual);
            try (var bw = Files.newBufferedWriter(f.toPath())) {
                bw.write(ventanaText.getText());
                modificado=false;
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error en la operación", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    // Método para guardar y elegir sobreescribir o crear nuevo archivo de forma manual

    private void guardarComo(){
        int opcion = fc.showSaveDialog(null);
        File f = null;
        if (opcion == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".txt")) {
                f = new File(f.getAbsolutePath() + ".txt");
            }
            try (var bw = Files.newBufferedWriter(f.toPath())) {
                bw.write(ventanaText.getText());
                modificado=false;
                archivoActual=f.getAbsolutePath();
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error en la operación", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    // Método para ejecutar el cierre de la aplicación

    private void cerrarAplicacion() {
       if (modificado) {
          int opcion = JOptionPane.showConfirmDialog(null, "Hay cambios sin guardar. ¿Salir de todos modos?", "SALIR", JOptionPane.YES_NO_OPTION);
          if (opcion == JOptionPane.NO_OPTION) {
              return;
          }
       }
       ventana.dispose();
    }

    public static void main(String[] args) {
        new EditorTexto();
    }
}
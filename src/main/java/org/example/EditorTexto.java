package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EditorTexto {
    JFrame ventana;
    JTextArea ventanaText;
    JFileChooser fc;

    public EditorTexto() {
        this.ventana = new JFrame("Editor de texto");
        this.ventanaText = new JTextArea();
        this.fc= new JFileChooser();
        ejecutarEditor();
    }

    private void ejecutarEditor() {

        ventana.setSize(600,600);
        ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        Font latoFont= importarFuente();

        ventanaText.setLineWrap(true);
        ventanaText.setWrapStyleWord(true);
        ventanaText.setFont(latoFont);
        ventanaText.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(ventanaText);

        JMenuBar menu = new JMenuBar();
        menu.setBackground(new Color(240, 240,240));

        JMenu archivo = new JMenu("ARCHIVO");
        archivo.setBorder(new EmptyBorder(14, 14, 14, 14));
        archivo.setFont(latoFont);

        JMenuItem abrir = new JMenuItem("Abrir");
        abrir.setBorder(new EmptyBorder(14, 15, 14, 15));
        abrir.setBackground(new Color(240, 240,240));
        abrir.setFont(latoFont);

        JMenuItem guardar = new JMenuItem("Guardar");
        guardar.setBorder(new EmptyBorder(14, 15, 14, 15));
        guardar.setBackground(new Color(240, 240,240));
        guardar.setFont(latoFont);

        JMenuItem cerrar = new JMenuItem("Cerrar");
        cerrar.setBorder(new EmptyBorder(14, 15, 14, 15));
        cerrar.setBackground(new Color(240, 240,240));
        cerrar.setFont(latoFont);

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

        cerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarAplicacion();
            }
        });

        archivo.add(abrir);
        archivo.add(guardar);
        archivo.addSeparator();
        archivo.add(cerrar);
        menu.add(archivo);

        ventana.add(scrollPane,BorderLayout.CENTER);
        ventana.setJMenuBar(menu);
        ventana.setVisible(true);

        // PRUEBAS PARA QUE SE VERIFIQUEN LAS MODIFICACIONES

    }

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
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El archivo no existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void guardarArchivo() {
        int opcion = fc.showSaveDialog(null);
        File f = null;
        if (opcion == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".txt")) {
                f = new File(f.getAbsolutePath() + ".txt");
            }
            try (var bw = Files.newBufferedWriter(f.toPath())) {
                bw.write(ventanaText.getText());
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error en la operación", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cerrarAplicacion() {
        ventana.dispose();

    }

    public static void main(String[] args) {
        new EditorTexto();
    }
}
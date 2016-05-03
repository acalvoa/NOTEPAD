import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 *
 * @author ANGELO CALVO A.
 */
public class Notepad {
	public static void main(String[] args) throws BadLocationException {
	    Ventana window = new Ventana();
            if(args.length > 0){
                window.loadContent(window.readFile(args[0]));
            }
        }
}
// DEFINIMOS LA CLASE VENTANA
final class Ventana{
    //DEFINIMOS EL CONTENEDOR
    Ventana self = this;
    JFrame f;
    JMenuBar menu;
    JMenuItem openItem,closeItem, cleanItem;
    JTextArea textPane;
    DefaultStyledDocument doc;
    //DEFINIMOS EL CONSTRUCTOR
    public Ventana() throws BadLocationException{
        this.f = new JFrame();
        this.f.setBounds(10,10,500,500);
        this.f.setVisible(true);
        this.f.setTitle("Notepad --");
        this.f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
               if(self.textPane != null){
                   /* CAD AVEZ QUE MODIFICAMOS EL TAMAÑO DE LA VENTANA EL TEXTAREA DEBE TOMAR EL NUEVO TAMAÑO DE LA VENTANA.*/
                   textPane.setBounds( 0, 0, self.f.getSize().width, self.f.getSize().height);
               }      
            }
        });
        // SI CERRAMOS LA VENTANA CERRAMOS EL PROGRAMA
        this.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMenu();
        this.loadTextContent();
    }
    public void setMenu(){
        this.menu = new JMenuBar();
        //Build the first menu.
        JMenu menuo = new JMenu("File");
        menuo.setMnemonic(KeyEvent.VK_A);
        menu.add(menuo);
        this.f.setJMenuBar(this.menu);
        //CREAMOS EL PRIMER ELEMENTO DEL MENU
        openItem = new JMenuItem("Abrir",
            KeyEvent.VK_T);
        // ASIGNAMOS LA COMBINACIÓN DE TECLAS ALT + 1
        openItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        // LO CERRAMOS SI SE DESATA ,LA ACCIÓN
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    /*EN CASO QUE SE ACTIVE SE BUSCA EL ARCHIVO A TRAVES DE UNA VENTANA PARA BSUQUEDA DE ARCHIVOS */
                    File archivo = self.abrirArchivo();
                    String contenido = self.readFile(archivo);
                    self.loadContent(contenido);
                } catch (BadLocationException ex) {
                    JOptionPane.showMessageDialog(self.f, "Error al abrir el archivo");
                }
            }
        });
        menuo.add(openItem);
        // BOTON PARA LIMPIAR
        cleanItem = new JMenuItem("Cerrar Archivo",
            KeyEvent.VK_T);
        // ASIGNAMOS LA COMBINACIÓN DE TECLAS CTRL + 1
        cleanItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        // LO CERRAMOS SI SE DESATA LA ACCIÓN
        cleanItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                self.textPane.setText(null);
            }
        });
        menuo.add(cleanItem);
        //CREAMOS EL BOTON PARA SALIR
        closeItem = new JMenuItem("Salir",
            KeyEvent.VK_T);
        // ASIGNAMOS LA COMBINACIÓN DE TECLAS CTRL + 1
        closeItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        // LO CERRAMOS SI SE DESATA LA ACCIÓN
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                self.f.dispose();
            }
        });
        menuo.add(closeItem);
    }
    private File abrirArchivo() throws BadLocationException {
        // CARGAMOS LA VENTANA QUE SELECCIONA EL ARCHIVO
        JFileChooser file=new JFileChooser();
        file.showOpenDialog(f);
        // ABRIMOS EL ARCHIVOS SELECCIONADO
        return file.getSelectedFile();
    }
    public String readFile(String archivo){
        /*CON ESTE METODO CARGAREMOS UN ARCHIVO DESDE SU NOMBRE O RUTA CON EL OBJETO DE PRECARGAR ARCHIVOS DESDE EL INICIO
        EN CASO DE ERROR GATILLAMOS LLAMADAS A DIALOGOS DE MENSAJE*/
        String retorno = "";
        String buffer;
        FileReader file;
        try {
            file = new FileReader(archivo);
            BufferedReader b = new BufferedReader(file);
            while((buffer = b.readLine())!=null) {
                retorno = retorno.concat(buffer)+"\n";
            }
            b.close();
        } catch (FileNotFoundException ex) {
           JOptionPane.showMessageDialog(self.f, "Archivo no encontrado.");
        } catch (IOException ex) {
           JOptionPane.showMessageDialog(self.f, "Error de lectura y escritura.");
        }
        
        return retorno;
    }
    private String readFile(File archivo){
        /*CON ESTE METODO CARGAREMOS UN ARCHIVO DESDE SU OBJETO, EL CUAL FUE EXTRAIDO DESDE EL SELECTOR DE ARCHIVOS.
        ESTE METODO SOLO ES LLAMADO DESDE EL MENU CON EL OBJETO DE CARGAR UN ARCHIVO NUEVO
        */
        String retorno = "";
        String buffer;
        FileReader file;
        try {
            file = new FileReader(archivo);
            BufferedReader b = new BufferedReader(file);
            while((buffer = b.readLine())!=null) {
                retorno = retorno.concat(buffer)+"\n";
            }
            b.close();
        } catch (FileNotFoundException ex) {
           JOptionPane.showMessageDialog(self.f, "Archivo no encontrado.");
        } catch (IOException ex) {
           JOptionPane.showMessageDialog(self.f, "Error de lectura y escritura.");
        }
        
        return retorno;
    }
    public void loadContent(String text){
        /*LIMPIAMOS EL TEXTAREA Y CARGAMOS EL NUEVO TEXTO*/
        textPane.setText(null);
        textPane.setText(text);
    }
    private void loadTextContent() throws BadLocationException{
        /*CREAMOS EL CONTENEDOR TEXTAREA Y LO SETEAMOS PARA QUE NO PUEDA SER MODIFICADO
        ADEMAS DEFINIMOS LOS COLORES Y EL TAMAÑO INICIAL
        */
        textPane = new JTextArea();
        textPane.setEnabled(false);
        textPane.setDisabledTextColor(Color.BLACK);
        this.f.add(textPane,BorderLayout.LINE_START);
        textPane.setBounds( 0, 0, this.f.getSize().width, this.f.getSize().height);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, this.f.getSize().width, this.f.getSize().height-47);
        this.f.getContentPane().add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(textPane);
    }
}

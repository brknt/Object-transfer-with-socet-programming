
package soket_projesi;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class GUI extends JFrame implements ActionListener {
    private Container container;//butonları oluşturduk
    private JLabel resim_label;
    private ImageIcon resim;
    private JLabel dosya;
    private JLabel ıp;
    private JButton gözat;
    private JTextField ıp_text;
    private JButton gönder;
    private JFileChooser  jfc;//gözat butonunu oluşturur.
    private JButton server_aç;
    
    
    
 public  static String dosya_yolu;
 public  static String ıp_adresi;
      
    public GUI(){
      setTitle("Dosya Transferi-FIRAT ÜNİVERSİTESİ");
        
        container = getContentPane();
        container.setLayout(null);
        
        resim=new ImageIcon("nesneTransfer.png");//görsel ekleme
        resim_label = new JLabel(resim);
        resim_label.setBounds(0, 0, 500, 500);
        
        dosya=new JLabel("Dosya Yolunu Seç:");//dosya labeli
        dosya.setBounds(95,240,200, 30);
        
       gözat=new JButton("Gözat");//dosya seç
        gözat.setBounds(200, 240, 100, 25);
       
        ıp=new JLabel("Bağlanılacak Aygıtın IP Adresi:");//ıp labeli
        ıp.setBounds(20, 290, 200, 30);
         
        ıp_text=new JTextField();//ıp textfieldi
        ıp_text.setBounds(200, 290, 100, 25);
        
        jfc = new JFileChooser();//gözat açmak için 
        
      
        server_aç=new JButton("Bağlantıyı Aç");
        server_aç.setBounds(360,380,120,30);
        
     
        
        gönder=new JButton("Gönder");//gönder butonu
        gönder.setBounds(40,370,100,30);
        gönder.setSize(300, 50);
        
        container.add(resim_label);
        resim_label.add(dosya);
        resim_label.add(ıp);
       resim_label.add(gözat);
        resim_label.add(ıp_text);
        resim_label.add(gönder);
        resim_label.add(server_aç);
        
        server_aç.addActionListener(this);
        gönder.addActionListener(this);
          gözat.addActionListener(this);
    } @Override

    public void actionPerformed(ActionEvent e){
    if(e.getSource()==gözat){
         try{
             jfc.showOpenDialog(null);
             dosya_yolu=jfc.getSelectedFile().getAbsolutePath().toString();
             System.out.println(dosya_yolu);
         }catch(Exception ex){
             ex.getMessage();
         }
     }if(e.getSource()==gönder){
         try{
             if(!dosya_yolu.equals("")|| !ıp_text.equals("")){
              ıp_adresi=ıp_text.getText();
              //CLİENT KISMI                              //Program esnasında kullanılan veriler program sona erdikten sonra 
                                                          // yok olur. Bu verileri kaybetmemenin tek yolu bu verilerin dosyalarda saklanmasıdır.
              File dosya = new File(dosya_yolu);
        Socket soket = new Socket(ıp_adresi, 3332);
        
        ObjectInputStream ois = new ObjectInputStream(soket.getInputStream());//dış kaynaktan veri okumyı sağlar --objeler üzerinden işlmler yapılıyor
        ObjectOutputStream oos = new ObjectOutputStream(soket.getOutputStream());//dış kaynağa veri gönderirken 

        oos.writeObject(dosya.getName());

        FileInputStream fis = new FileInputStream(dosya);
        byte[] buffer = new byte[Server.BUFFER_SIZE];//bufferin yararı kullaıcının beklemesini önlemek.
        Integer bytesRead = 0;

        while ((bytesRead = fis.read(buffer)) > 0) {
            oos.writeObject(bytesRead);
            oos.writeObject(Arrays.copyOf(buffer, buffer.length));
            
        }

        oos.close();
        ois.close();
        System.exit(0);
       
              
             }else{
                 JOptionPane.showMessageDialog(container, "Dosya yolu veya Ip adresi yanlış");
             }
             }
         catch(Exception ex){
             ex.getMessage();
         }
     }
     if(e.getSource()==server_aç){
         new Server().start();
        JOptionPane.showMessageDialog(container, "Dinleme İşlemi Başladı...");
     
     }
        

        }
 
  public static void main(String[] args) throws Exception {
         GUI a=new GUI();
         a.setBounds(0, 0, 500, 500);
         a.setVisible(true);
        a.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
  }
} 
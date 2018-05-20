
package soket_projesi;
import java.io.*;
import java.net.*;

public class Server extends Thread {

    public static final int PORT = 3332;
    public static final int BUFFER_SIZE =100;
   
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket s = serverSocket.accept();
                saveFile(s);
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(Socket socket) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        FileOutputStream fos = null;//çıkış işlemi boş
        byte[] buffer = new byte[BUFFER_SIZE];

        //  dosyayı okuyor.
        Object o = ois.readObject();

        if (o instanceof String) {
            fos = new FileOutputStream(o.toString());
        } else {
            throwException("Hata oluştu1.");
        }

        // dosya okuması bitiyor
        Integer bytesRead = 0;
                                         //10.93.36.84     10.93.37.183
        do {
            o = ois.readObject();

            if (!(o instanceof Integer)) {
                throwException("Hata oluştu2.");
            }

            bytesRead = (Integer) o;

            o = ois.readObject();

            if (!(o instanceof byte[])) { //instanceof nesnenin tipini sorgular.
                throwException("Hata oluştu4.");
            }

            buffer = (byte[]) o;

            // 3. Write data to output file.
            fos.write(buffer, 0, bytesRead);

        } while (bytesRead == BUFFER_SIZE);

        System.out.println("İşlem Tamamlandı");

        fos.close();
        ois.close();
        oos.close();
    }

    public static void throwException(String message) throws Exception {
        throw new Exception(message);
    }

    public static void main(String[] args) {
        new Server().start();
        System.out.println("Dinleme İşlemi Başladı.");
    }
}

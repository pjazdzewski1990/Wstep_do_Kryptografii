/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import javax.imageio.ImageIO;

/**
 * Zastosoowanie szyfru blokowego DES dla map bitowych w dwóch
 *  trybach szyfrowania blokowego
 * @author Patryk
 */
public class ImageDES extends DES {
    
    //wysokość i szerokość boxów na jakie będziemy dzielić obraz 
    protected int box_width = 4;
    protected int box_height = 3;
    
    protected BlockCipherMode mode = BlockCipherMode.ECB; 
    
    //wektor inicjalizujący 
    // nie powinien ulegać zmianie w czasie dzialania algorytmu
    protected BitSet iv;
    //wektor szyfrowania
    // jego początkowa wartość jest taka sama co "iv"
    // po zakodowaniu każdego bloku ulega zmianie
    protected BitSet cipherVector;
    
    public ImageDES(String _key, String _analyzeFile, String _DEStimes, BlockCipherMode _mode){
        super(_key, _analyzeFile, _DEStimes);
        mode = _mode;
        iv = cipherVector = H.randomBitSet(12);
    }
    
    @Override
    public void run() {
        //kasuj historię
        history.clear();
        
        if(isEncoding){
        ///kodujemy tak czy siak ...
        }else{
            H.pl("ImageDES nie wspiera odkodowywania. Rozpoczynam proces kodowania");
        } 

        encode();
        
        H.pl("ImageDES: End");
    }

     /**
      * Zakoduj box z pliku "image" znajdujący się na pozycji ("x","y") 
      *  do pliku "new_image"
      *  stosując tryb ECB 
      * Umownie czarny to zero(czyli false)
      * @param image
      * @param new_image
      * @param x
      * @param y 
      */
    private void encodeECBBox(BufferedImage image, BufferedImage new_image, int x, int y) {
        BitSet data = getBitSet(image,x,y);  
        //zakoduj ten wektor
        data = encode(data, 1, 0);
        //zapisz wynik operacji
        writeBitSet(data, new_image, x, y);
    }
    
         /**
      * Zakoduj box z pliku "image" znajdujący się na pozycji ("x","y") 
      *  do pliku "new_image"
      *  stosując tryb CBC 
      * Umownie czarny to zero(czyli false)
      * @param image
      * @param new_image
      * @param x
      * @param y 
      */
    private void encodeCBCBox(BufferedImage image, BufferedImage new_image, int x, int y) {
        BitSet data = getBitSet(image,x,y);  
        //xor'uj dane z pliku z wektorem 
        data = H.xor(data, cipherVector, 12);
        //zakoduj ten wektor
        data = encode(data, 1, 0);
        //ustaw nowy wektor szyfrujący
        cipherVector = data;
        //zapisz wynik operacji
        writeBitSet(data, new_image, x, y);
    }

    /**
     * Metoda odpowiedzialna za kodowanie
     */
    private void encode() {
        try{
            //otwórz obraz do odczytu
            BufferedImage image = ImageIO.read( new File( src ) );
            //szerokość w pikselach
            int width = image.getWidth();
            //wysokość w pikselach
            int height = image.getHeight();
            
            //utwórz obraz do zapisu
            BufferedImage new_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            for(int i=0; i<height/box_height; i++){
                for(int j=0; j<width/box_width; j++){
                    //zapisz historię dla danego boxa
                    history.add(new ArrayList<String>());

                    //zakoduj prostokąt który "zaczyna się" w podanym punkcie
                    // w podanym trybie
                    switch(mode){
                        case CBC:
                            encodeCBCBox(image, new_image, j*box_width, i*box_height);
                        break;
                        case ECB:
                        default:
                            encodeECBBox(image, new_image, j*box_width, i*box_height);
                    }
                }
            }
            image.flush();
            
            //zapisz zakodowany do pliku
            File outputfile = new File(dest);
            ImageIO.write(new_image, "jpg", outputfile);
            
            //zapisz zebrane informacje "historyczne" do pliku analizy
            if(analyzeFile != null){
                writeHistory();
            }
        }
        catch(Exception ex){
            H.pl("Błąd przy kodowaniu.");
            H.pl(ex.getMessage());
        }
    }

    /**
     * Pobierz box z obrazka "image" o początku w "x,y" i zamień go na BitSet 
     * @param image
     * @param x
     * @param y
     * @return 
     */
    private BitSet getBitSet(BufferedImage image, int x, int y) {
        BitSet data = new BitSet();
        for(int w=0; w<box_width; w++){
            for(int h=0; h<box_height; h++){
                Color color = new Color(image.getRGB(x+w, y+h));
                //czy jest to kolor czarny
                boolean not_black = true;
                if(color.getBlue()>128 && color.getGreen()>128 && color.getRed()>128){
                    not_black = false;
                }
                data.set(w*box_height+h, not_black);
                //System.out.println("Odczytałem: "+(x+w)+":"+(y+h)+" "+color+ "->"+not_black);
            }
        }
        return data;
    }

    /**
     * Zapisz bitSet "data" do "image" jako box o początku w "x,y"
     * @param data
     * @param new_image
     * @param x
     * @param y 
     */
    private void writeBitSet(BitSet data, BufferedImage new_image, int x, int y) {
        for(int w=0; w<box_width; w++){
            for(int h=0; h<box_height; h++){
                //ustaw kolor
                Color color; 
                if(data.get(w+h) == false){
                    color = Color.BLACK;
                }else{
                    color = Color.WHITE;
                }
                new_image.setRGB(x+w, y+h, color.getRGB());
            }
        }
        new_image.flush();
    }
}

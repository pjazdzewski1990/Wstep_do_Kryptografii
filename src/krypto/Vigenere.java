/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.io.*;

/**
 * Szyfr Vinegara
 * uruchmiany Krypto.jar v d/e (-keyFile=plik.txt)
 * @author Patryk
 */
public class Vigenere extends BasicCodeEngine{
    //długość klucza
    int keyLength;
    
    //nazwa pliku tymczasowego
    private String temp = "temp.txt"; 
    
    public Vigenere(String _key){
       key = _key;
       keyLength = _key.length();
    }
    
    @Override
    public String encode(String text) {
        sb = new StringBuilder();
        char c;
        
        for(int i=0; i < text.length(); i++){
            Character charFromText = text.charAt(i);
            Character charFromKey = key.charAt(i % keyLength);
            c = (char)((charFromText + charFromKey) % alp);
            sb.append( c );
        }
        
        return sb.toString();
    }
    
    @Override
    public String decode(String text) {
        sb = new StringBuilder();
        //char c;
        int num;
        
        for(int i=0; i < text.length(); i++){
            Character charFromText = text.charAt(i);
            Character charFromKey = key.charAt(i % keyLength);
            num = charFromText.charValue() - charFromKey.charValue();
            if(num < alp){
                num = alp +num;
            }
            //c = (char)((charFromText - charFromKey) % alp);
            sb.append( (char)num );
        }
        
        return sb.toString();
    }
    
    @Override
    public void run() {
        if(isEncoding){
            prepareTmp(src);
            encode(temp, dest);
            destroyTmp();
        }else{
            decode(src,dest);
        }
        H.pl("Vigenere: End");
    }

    /**
     * Przygotuj plik tymczasowy.
     * Szyfrować bedziemy plik tymczasowy.
     * Plik tmp ma taką samą treść tylko wszystkie jego litery są 
     * zamienione na małe i cały tekst jest w jednej linii.
     * @param src 
     */
    private void prepareTmp(String src) {
        try{

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                        new DataInputStream(
                            new FileInputStream(src)
                        )
                    )
            );
           
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(
                        new File(temp),
                    true)
            );
            
            String strLine ;
            String smallLetters ;
            
            while ((strLine = br.readLine()) != null)   {
                smallLetters = strLine.toLowerCase();
                bw.write(smallLetters);
                bw.write(" ");
            }
            
            br.close();            
            bw.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            H.pl("Error: " + e.getMessage());
        }
    }

    /**
     * Metoda usuwa plik tymczasowy utworzony na potrzeby szyfrowania
     */
    private void destroyTmp() {
        File file = new File(temp);
         if (file.exists()){
             if(!file.delete()){
                 H.pl("Nie powodzenie przy usuwaniu pliku TEMP.txt");
             }
         }
    }

}

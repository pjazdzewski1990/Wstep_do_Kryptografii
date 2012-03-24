/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.io.*;

/**
 * Bazowa klasa dla Silników Kryptograficznych
 * @author Patryk
 */
abstract public class BasicCodeEngine implements Runnable {
    //nazwy plików docelowego, źródłowego i z kluczem
    protected String src;
    protected String dest;
    protected String keyFile;
    
    protected int alp = 127;
    
    //czy szyfrujemy?
    // jeśli false to odszyfrowujemy
    protected boolean isEncoding = true;
    
    //klucz danego szyfrowania
    protected String key;
    
    //bufor stosowany przy szyfrowaniu
    protected StringBuilder sb = new StringBuilder();
    
    public void encode(String src, String dest) {
        try{
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(src);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(
                        new File(dest),
                    true)
            );
            
            String encoded;
            while ((strLine = br.readLine()) != null)   {
                encoded = encode(strLine);
                bw.write(encoded);
                bw.write("\n");
            }
            //Close the input stream
            br.close();
            in.close();
            fstream.close();
            
            bw.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public String encode(String text) {
        sb = new StringBuilder();
        Character encoded;
        for(int i=0; i<text.length(); i++){
            encoded = encode(text.charAt(i));
            sb.append(encoded);
        }
        return sb.toString();
    }
    
    public Character encode(Character ch) {
        return ch;
    }

    public void decode(String src, String dest) {
        try{
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(src);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            
            String[] words;
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(
                        new File(dest),
                    true)
            );
            
            String decoded;
            while ((strLine = br.readLine()) != null)   {
                /*words = strLine.split(" ");
                for(String w : words){
                    decoded = decode(w);
                    bw.write(decoded);
                    bw.write(" ");
                }*/
                decoded = decode(strLine);
                bw.write(decoded);
                bw.write("\n");
            }
            //Close the input stream
            br.close();
            in.close();
            fstream.close();
            
            bw.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public String decode(String text) {
        sb = new StringBuilder();
        Character decoded;
        for(int i=0; i<text.length(); i++){
            decoded = decode(text.charAt(i));
            sb.append(decoded);
        }
        return sb.toString();
    }
    
    public Character decode(Character ch) {
        return ch;
    }

    public void encodingMode(boolean _enc) {
        isEncoding = _enc;
    }
    
    public void direction(String _src, String _dest){
        src = _src;
        dest = _dest;
    }

    @Override
    public void run() {
        //uruchamianie instancji tej klasy jest błędem logicznym
        throw new UnsupportedOperationException("BasicCodeEngine jest wzrocem klas i nie nadaje się do uruchomienia");
    }

}

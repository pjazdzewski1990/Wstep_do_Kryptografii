/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.io.*;

/**
 * Silnik łamiący szyfr afiniczny
 * @author Patryk
 */
public class AfinicB extends BasicAnalysisEngine{
    
    public AfinicB(String _src, String _dest, String _extra){
        src = _src;
        dest = _dest;
        extra = _extra;
    }
    
    @Override
    public void run() {
        if(hasHint){
            //łam z pomocą pliku "extraFile"
            breakWithHint();
        }else{
            //łam bez pomocy
            breakWithoutHint();
        }
        H.pl("Afinic Breaker-END");
    }
    
    public void breakWithoutHint(){
        
        try{
            BufferedReader br = null;
            
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(
                        new File(dest),
                    true)
            );
            
            String strLine;
        
            //spróbuj wszystkie możliwe przesunięcia
            // ax+b mod alp
            for(int a = 1; a<alp; a++){
                for(int b = 0; b<alp; b++){
                    br = new BufferedReader(new InputStreamReader(
                        new DataInputStream(
                                new FileInputStream(src)
                            )
                        )
                    );
                    bw.write("### a="+a+" b="+b+"\n");
                
                    //String[] words;
            
                    String decoded;
                    while ((strLine = br.readLine()) != null)   {
                        int inverse = H.moduloInverse(a, alp);
                        /*words = strLine.split(" ");
                        for(String w : words){
                            decoded = breakWord(w, a, b, inverse);
                            bw.write(decoded);
                            bw.write(" ");
                        }*/
                        decoded = breakWord(strLine, a, b, inverse);
                        bw.write(decoded);
                        bw.write("\n");
                    }
                }
            }
            
            //Close the input stream
            br.close();
            
            bw.close();
        }
        catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    private String breakWord(String word, int a, int b, int inverse) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<word.length(); i++){
            char ch = word.charAt(i);
            //w javie nie możemy liczyć na ew. "przekręcenie" się obiektu char
            if(ch < b){
                ch = (char)(alp - (b - ch));
            }else{
                ch = (char)(ch - b);
            }
            ch = (char)Math.floor(ch * inverse); 
            ch = (char)(ch % alp);
            //char ch = (char)((word.charAt(i) - shift) % alp);
            sb.append(ch);
        }
        return sb.toString();
    }
    
    /*
     * Łam kod w oparciu o kilak pierwszych liter z oryginalnej wiadomości 
     * znajdujących się w pliku "extra"
     */
    public void breakWithHint(){
        try{
            //plik do czytania
            BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    new DataInputStream(
                        new FileInputStream(src)
                    )
                )
            );
            
            BufferedReader brExtra = new BufferedReader(
                new InputStreamReader(
                    new DataInputStream(
                        new FileInputStream(extra)
                    )
                )
            );
            
            int a=0;
            int b=0;
            
            //pierwsza litera w pliku extra odpowiada pierwszej literze
            // pliku przed zakodowaniem
            char firstFromExtra = (char)brExtra.read();
            char secondFromExtra = (char)brExtra.read();
            brExtra.close();
            
            //wczytaj pierwsza litere z plku zakodowanego
            char firstFromCrypto = (char)br.read();
            char secondFromCrypto = (char)br.read();
            br.close();
            
            if(secondFromExtra - firstFromExtra == 0){
                if(secondFromCrypto - firstFromCrypto == 0){
                    a = 1;
                    b = 0;
                }else{
                    throw new RuntimeException(
                        "Nie można obliczyć wartości a i b. Czy pliki podane są poprawne?");
                }
            }
            else{
                a = (secondFromCrypto - firstFromCrypto)/(secondFromExtra - firstFromExtra);
                b = firstFromCrypto - a*firstFromExtra;
            }
            //oblicz przesuniecie
            if(a < 0){
                //czasami będzie trzeba "przekręcić" licznik
                a = alp + a;
            }
            
            if(b < 0){
                //czasami będzie trzeba "przekręcić" licznik
                b = alp + b;
            }
            
            //kiedy znamy już przesunięcie tworzymy szyfr cezara 
            // z naszym przesunięciem i odszyfrowujemy
            Afinic af = new Afinic(a, b);
            af.decode(src, dest);
        }
        catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    
}
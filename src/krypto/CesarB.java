/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.io.*;

/**
 * Silnik łamiący szyfr cezara
 * @author Patryk
 */
public class CesarB extends BasicAnalysisEngine{
    
    public CesarB(String _src, String _dest, String _extra){
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
        H.pl("Cesar Breaker-END");
    }
    
    public void breakWithoutHint(){
  
        try{
            //plik do czytania
            BufferedReader br = null;
            
            //plik do pisania
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(
                        new File(dest),
                    true)
            );
            
            String strLine;
        
            //spróbuj wszystkie możliwe przesunięcia
            for(int i = 0; i<alp; i++){   
                br = new BufferedReader(new InputStreamReader(
                    new DataInputStream(
                            new FileInputStream(src)
                        )
                    )
                );
                bw.write("### b="+i+"\n");
                
                String[] words;
            
                String decoded;
                while ((strLine = br.readLine()) != null)   {
                    /*words = strLine.split(" ");
                    for(String w : words){
                        decoded = breakWord(w, i);
                        bw.write(decoded);
                        bw.write(" ");
                    }*/
                    decoded = breakWord(strLine, i);
                    bw.write(decoded);
                    bw.write("\n");
                }
            }
            //Close the input stream
            br.close();
            //in.close();
            //fstream.close();
            
            bw.close();
        }
        catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    /*
     * Próbuj łamać słowo z przesunięciem równym "shift"
     * tzn. przesuń każdą literę slowa "word" o "shift"
     */
    private String breakWord(String word, int shift) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<word.length(); i++){
            char ch = word.charAt(i);
            //w javie nie możemy liczyć na ew. "przekręcenie" się obiektu char
            if(ch < shift){
                ch = (char)(alp - (shift - ch));
            }else{
                ch = (char)(ch - shift);
            }
            ch = (char)(ch % alp);
            //char ch = (char)((word.charAt(i) - shift) % alp);
            sb.append(ch);
        }
        return sb.toString();
    }
    
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
            
            //pierwsza litera w pliku extra odpowiada pierwszej literze
            // pliku przed zakodowaniem
            char letterFromExtra = (char)brExtra.read();
            brExtra.close();
            
            //wczytaj pierwsza litere z plku zakodowanego
            char letterFromCrypto = (char)br.read();
            br.close();
            
            //oblicz przesuniecie
            int shift = letterFromCrypto - letterFromExtra;
            if(shift < 0){
                //czasami będzie trzeba "przekręcić" licznik
                shift = alp + shift;
            }
            
            //kiedy znamy już przesunięcie tworzymy szyfr cezara 
            // z naszym przesunięciem i odszyfrowujemy
            Cesar c = new Cesar((char)shift);
            c.decode(src, dest);
        }
        catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    
}

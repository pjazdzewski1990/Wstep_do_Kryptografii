/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.util.*;

/**
 * Klasa służąca do łamania kodu Vinegere'a
 * @author Patryk
 */
public class VigenereB extends BasicAnalysisEngine {
    
    //potencjalne długości klucza, min 1.
    private List<Integer> keyLengths;
    //każda z map-pozycji na lisćie określa częstotliwość występowania
    // poszczególnych liter dla danej pozycji w kluczu
    private List<Map<Character,Integer>> freq;

    VigenereB(String _src, String _dest, String _extra) {
        src = _src;
        dest = _dest;
        extra = _extra;
    }
    
    @Override
    public void run() {
        if(hasHint){
            breakWithHint();
        }else{
            //łam bez pomocy
            breakWithoutHint();
        }
        H.pl("vigenere Breaker-END");
    }

    private void breakWithHint() {
        //pomoc to oszacowanie dlugości klucza
        
        //najpoluraniejszy znak w języku naturalnym
        Character popLang = 'a';
        
        //wszystkie potencjalne dlugości kluczy
        keyLengths = findLen();
        
        for(int len : keyLengths){
            //nasz klucz
            StringBuilder mykey = new StringBuilder();
            
            //liczymy ilośc wystąpień każdej litery
            freq = findFreq(len);
            
            //dla każdej pozycji w kluczu
            // i to index mapy z częstotliwościami znaków dla danego przesunięcia
            for(int i=0; i<freq.size(); i++){
                //najpopularniejszy w tekście
                Character popText = null;
                for (Character key : freq.get(i).keySet()) {
                    if(popText == null || freq.get(i).get(popText) < freq.get(i).get(key)){
                        popText = key;
                    }
                }
                
                //przesunięcie dla pozucji "i" w kluczu
                int shift = popText - popLang;
                while(shift < 0){
                    shift += alp;
                }
                while(shift >= alp){
                    shift -=alp;
                }
                
                mykey.append((char)shift);
            }
            
            //odkoduj z naszym kluczem o dlugosci "len"
            H.writeLineToFile(dest, "###Kod "+mykey+" ");
            BasicCodeEngine vin = new Vigenere(mykey.toString());
            vin.encodingMode(false);
            vin.decode(src, dest);
        }
    }

    private void breakWithoutHint() {
        H.pl("Łamanie szyfru Vigenere'a bez podpowiedzi nie jest obsługiwane");
        breakWithHint();
    }

    /**
     * Funkcja znajduje długość klucza w plku "this.src". 
     * Zakładając, że jest on zaszyfrowany kodem vinegere'a
     * @return Oszacowana długość klucza
     */
    private List<Integer> findLen() {
        //zbiór "spotkań" takich samych znaków  
        HashMap<Integer,Integer> match = new HashMap<>();
        String crypto = H.readFileLine(src);
        
        List<Integer> best_shift = new ArrayList<>();
        int max_shift = 0;
        
        int index = 0;
        int hits = 0;
        //i to nasze przesunięcie
        // przesunięcie równe 0 nie ma sensu
        for(int i=1; i<crypto.length(); i++){
            //System.out.println("FindLen dla "+i);
            hits = 0;
            for(int j=0; j<crypto.length(); j++){
                index = (i+j) % crypto.length();
                //System.out.println(crypto.charAt(j) + "==" + crypto.charAt(index));
                if(crypto.charAt(j) == crypto.charAt(index)){
                   hits++; 
                }
            }
            match.put(i, hits);
            if(hits >= max_shift){
                // dodaj kolejnego kandydata
                if(hits == max_shift){
                    //kandydat "i" jest równie dobry co inne
                    best_shift.add(i);  
                }else{
                    //kandydat "i" jest lepszy niż poprzednie
                    max_shift = hits;
                    best_shift.clear();
                    best_shift.add(i);
                }
            }
        }
        
        return best_shift;
    }

    private List<Map<Character, Integer>> findFreq(int length) {
        List<Map<Character, Integer>> ret = new ArrayList<Map<Character,Integer>>();
        String data = H.readFileLine(src);
        
        //przygotuj listę
        for(int i=0; i<length; i++){
            Map<Character,Integer> map = new TreeMap<>();
            ret.add(map);
        }
        
        //analizujemy każdy znak w pliku źródłowym 
        for(int i=0; i<data.length(); i++){
            char ch = data.charAt(i);
            Integer how_many = ret.get(i % length).get(ch);
            if(how_many == null){
                how_many = 1;
            }else{
                how_many++;
            }
            ret.get(i % length).put(ch, how_many);
        }
        
        return ret;
    }
}

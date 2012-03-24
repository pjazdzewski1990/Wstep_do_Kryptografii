/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Implementacja szyfrowania algorytmem DES
 * @author Patryk
 */
public class DES extends BasicCodeEngine {
    
    //ile ma być "obrotów" algorytmu DES
    Integer DEStimes = -1;
    
    //plik do którego zapiszemy kroki algorytmu
    // jeśli jest nullem to nie pisz
    String analyzeFile;
    
    //tablica przejść na potrzeby funkcji rozszerzającej
    // pomniejszone o 1 w odróżnieniu od treści
    Integer[] transitions = new Integer[]{0,1,3,2,3,2,4,5};
    
    //S-boxy na potrzeby "mieszania" danych 
    String[] Sbox1 = new String[]{
                                    "101", "010", "001", "110", "011", "100", "111", "000",
                                    "001", "100", "110", "010", "000", "111", "101", "011"
                                    };
    String[] Sbox2 = new String[]{
                                    "100", "000", "110", "101", "111", "001", "011", "010",
                                    "101", "011", "000", "111", "110", "010", "001", "100"
                                    };
    String[][] box = new String[][] {Sbox1, Sbox2};
    
    //historia przekształceń, znajdzie się ona w pliku "analyzeFile"
    ArrayList<ArrayList<String>> history = new ArrayList<ArrayList<String>>();
    
    public DES(String _key, String _analyzeFile, String _DEStimes){
        DEStimes = Integer.parseInt(_DEStimes);
        analyzeFile = _analyzeFile;
        key = _key;
    }
    
    /**
     * Właściwa część szyfrująca algorytmu DES
     * @param text Wektor bitowy z danymi do zakodowania
     * @param multi Określa "skok" w kluczu przy jego przesuwaniu, 
     *  jeśli jest ujemny to przesuwamy w prawo
     * @param initial_shift Okresla o ile i w jakim kierunku
     *  trzeba przesunąć kod
     * @return Zwracamy wektor zawierający już zakodowane znaki
     */
    protected BitSet encode(BitSet text, int multi, int initial_shift){
        //dwie czesci wyniku
        BitSet left = H.part(text, 0, 6);
        BitSet left_old;
        BitSet right = H.part(text, 6, 12);
        BitSet right_old;
        
        //zapisz pierwotny podział w historii
        if(analyzeFile != null){
            String stringRepresentation = H.bitSetToString(H.merge(left, right, 6), 12);
            history.get(history.size()-1).add( stringRepresentation );
        }
        
        //klucz
        BitSet DESkey = H.StringToBitset(key);
        DESkey = shiftKey(DESkey, 9, initial_shift);
        
        //mamy zrobić "DEStimes" rund
        for(int i=0; i<DEStimes; i++){            
            //zapamiętaj stare wartości
            left_old = left;
            right_old = right;
            
            //zastosuj rozszerzajaca funkcje pseudolosowa
            BitSet set = expand(right);
            
            //xoruj klucz i set
            //System.out.println("Multi " + multi+" klucz " 
            //    + H.bitSetToString(DESkey, 9) );
            set = H.xor(set, DESkey, 8); 
            
            //pomniejsz zbiory długości 3, korzystając z dwóch S-box'ów
            set = H.merge(contract(H.part(set, 0, 4),0), contract(H.part(set, 4, 8),1), 3);
            
            //ostatni xor
            right = H.xor(left, set, 8);
            
            //ustaw lewy
            left = right_old;
            
            //przesuń klucz o i za każdym obrotem, 
            // w jedna lub drugą stronę w zależności od parametru multi:
            //  multi>1 dla przesuwania w lewo(szyfrowanie), 
            //  multi<1 dla przesuwania w prawo(deszyfrowanie)
            //  multi=0 dla stalego klucza
            DESkey = shiftKey(DESkey, 9, multi);
            
            //zapisz informacje o historii przekształceń
            if(analyzeFile != null){
                String stringRepresentation = H.bitSetToString(H.merge(left, right, 6), 12);
                history.get(history.size()-1).add( stringRepresentation );
            }
        }
        
        //złącz 2 zbiory dlugości 6, odwrotnie
        return H.merge(right, left, 6);
    }

    /**
     * Rozwiń bitSet "toExpand" zgodnie z tablicą przejść(funkcją mieszającą) 
     * @param toExpand
     * @return 
     */
    private BitSet expand(BitSet toExpand) {
        BitSet bs = new BitSet();     
        //przypisuj kolejne wartości zgodnie z tablicą przejść
        for(int i=0; i<transitions.length; i++){
            //System.out.println(i+" "+toExpand.get(transitions[i]));
            bs.set(i, toExpand.get(transitions[i]));
        }
        return bs;
    }

    /**
     * Przesuń "bs" o "shift" miejsc w lewo, 
     *  elementy usuwane z lewej pojawiają się po prawej.
     *  Jeśli "shift" jest ujemne to analogicznie przesuwamy 
     *  w prawo
     * @param bs
     * @param len Realna długośc "bs"
     * @param shift
     * @return Zbiór bs po przesunięciu o "shift"  
     */
    private BitSet shiftKey(BitSet bs, int len, int shift) {
        //dla dodatniego "shift"
        if(shift >=0){
            BitSet result = new BitSet();
            int index=0;
            //przepisz elementy znajdujące się za bitem o indeksie "shift" maksymalnie na lewo 
            for(int i=shift; i<len; i++, index++){
                result.set(index, bs.get(i));
            }
            //resztę dodaj na koniec wynikowego zbioru
            for(int i=0; i<shift; i++, index++){
                result.set(index, bs.get(i));
            }
            return result;
        }else{
            //dla ujemnych "shift" przesuwamy w prawo
            BitSet result = new BitSet();
            int index=0;
            //przepisz elementy z konca wyrazu na początek
            for(int i=len+shift; i<len; i++, index++){
                result.set(index, bs.get(i));
            }
            //resztę dodaj na koniec wynikowego zbioru
            for(int i=0; i<len+shift; i++, index++){
                result.set(index, bs.get(i));
            }
            return result;
        }
    }

    /**
     * Stwórz wektor bitowy na podstawie "part" i Sboxa numer "box_num"
     * @param part Wektor bitowy
     * @param box_num Numer boxa który musimy użyć
     */
    private BitSet contract(BitSet part, int box_num) {    
        Integer num = H.bitSetToInt(part, 4);
        BitSet result = H.StringToBitset(box[box_num][num]);
        return result;
    }

    @Override
    public void run() {
        //kasuj historię
        history.clear();
        
        if(isEncoding){
            String fileText = H.readFileLine(src);
            //dla każdego znalezionego słowa
            for(String text : fileText.split(" ")){
                //lista która przechowa przeksztalcenia słowa "text"
                history.add(new ArrayList<String>());
                
                BitSet bs = H.StringToBitset(text);
                BitSet encoded = encode(bs, 1, 0);
                H.writeLineToFile(dest, H.bitSetToString(encoded, 12));
            }
        }else{
            /*String text = H.readFileLine(src);
            BitSet bs = H.StringToBitset(text);
            BitSet encoded = encode(bs, -1, 1);
            H.writeLineToFile(dest, H.bitSetToString(encoded, 12));*/       
            String fileText = H.readFileLine(src);
            //dla każdego znalezionego słowa
            for(String text : fileText.split(" ")){
                //lista która przechowa przeksztalcenia słowa "text"
                history.add(new ArrayList<String>());
                
                BitSet bs = H.StringToBitset(text);
                BitSet encoded = encode(bs, -1, -2);
                H.writeLineToFile(dest, H.bitSetToString(encoded, 12));
                H.writeLineToFile(dest, " ");
            } 
        }
        //zapisz zebrane informacje "historyczne" do pliku analizy
        if(analyzeFile != null){
            writeHistory();
        }
        H.pl("DES: End");
    }

    /**
     * Przepisuje dane historyczne do pliku analizy w formie
     * zdanie1_po_pierwszym_obrocie zdanie2_po_pierwszym_obrocie .... xor_ze_wszystkich_zdań
     * i tak dla każdego kroku
     */
    protected void writeHistory() {
        //ile mamy faz
        int num = history.get(0).size();
        //tu przechowamy tekst do zapisu
        StringBuilder builder = new StringBuilder();
        H.writeLineToFile(analyzeFile, "MiniDES: analiza");
        
        //"i" to krok czyli jakby 'wiersz"
        for(int i=0; i<num; i++){
            //xor ze wszystkich wartości
            String xor = null;
            //trezba przeiterować wszystkie kolumny
            // bo potrzebne nam wszystkie wartości w "wierszach"
            for(int j=0; j<history.size(); j++){
                //uzupelnij xor
                if(xor!=null){
                    xor = xor(xor, history.get(j).get(i));
                }else{
                    xor = history.get(j).get(i);
                }
                //dodaj wartość do zapisu
                builder.append(history.get(j).get(i));
                builder.append(" ");
            }
            H.writeLineToFile(analyzeFile, builder.toString()+xor);
            builder = new StringBuilder();
        }
    }

    /**
     * Wykonaj operację xor na dwóch obiektach string
     *  jeśli znaki na pozycji "i" w argumentach nie są identyczne 
     *  to do wyniku na pozycji "i" wstaw 1, w przeciwnym wypadku 0
     *  wynik ma długość krotszego ciągu
     * @param arg0
     * @param arg1
     * @return Ciąg zer i jedynek odpowiadający wynikowi operacji 
     *  xor na argumentach (zgodnie z definicją)
     */
    private String xor(String arg0, String arg1) {
        int max = Math.max(arg0.length(), arg1.length());
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<max; i++){
            if(arg0.charAt(i)!=arg1.charAt(i)){
                builder.append("1");
            }else{
                builder.append("0");
            }
        }
        return builder.toString();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.io.*;
import java.util.BitSet;
import java.util.Random;

/**
 * Klasa pomocnicza
 *
 * @author Patryk
 */
public class H {

    public static void p(String _str) {
        System.out.print(_str);
    }

    public static void pl(String _str) {
        System.out.println(_str);
    }

    /*
     * Metoda zwraca liczbę odwrotną do "number" \ w arytmetyce modulo "modulo"
     */
    public static int moduloInverse(int number, int modulo) {
        int u = 1;
        int w = number;
        int x = 0;
        int z = modulo;

        while (w != 0) {
            if (w < z) {
                int temp;
                //zamień u i x
                temp = u;
                u = x;
                x = temp;

                //zamień w i z
                temp = w;
                w = z;
                z = temp;
            }
            //oblicz iloczyn całkowity
            int q = w / z;
            //zmień wartość u
            u = u - q * x;
            //zmień wartość w
            w = w - q * z;
        }

        if (z != 1) {
            H.pl("ERROR!Nie ma odwrotności dla " + number + " w arytmetyce modulo " + modulo);
            H.pl("Kontynuuję, ale wyniki nie będą poprawne");
            return number;
        }

        if (x < 0) {
            x = x + modulo;
        }
        return x;
    }

    public static int NWD(int a, int b) {
        int c;
        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    /**
     * Czyta jedną(pierwszą) linię pliku "file".
     *
     * @return Pierwsza linia pliku jako String
     */
    public static String readFileLine(String file) {
        try {
            String strLine;
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                    new DataInputStream(
                    new FileInputStream(file))));
            strLine = br.readLine();
            br.close();
            return strLine;
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            H.pl("Error: " + e.getMessage());
            return "";
        }
    }

    static void writeLineToFile(String src, String string) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(
                        new File(src),
                    true));
            bw.write(string);
            bw.newLine();
            bw.close();
        }
        catch(Exception ex){
            System.err.println(ex);
            H.pl("Błąd przy dopisywaniu."+string);
        }
    }

    /**
     * Zamienia obiekt typu String na BitSet
     *  jeśli wciągu znajdzie się znak inny niż 0 lub 1 to jest on pomijany
     * @param str Ciąg zer i jedynek(String) do zamiany
     * @return Zwraca BitSet odpowiadający ciągowi znakowemu
     */
    static BitSet StringToBitset(String str) {
        BitSet bs = new BitSet(); 
        for(int i=0; i<str.length(); i++){
            if(str.charAt(i) == '0'){
                bs.set(i, false);
            }else{
                if(str.charAt(i) == '1'){
                    bs.set(i, true);
                }
            }
        }
        return bs;
    }

    /**
     * Wytnij z bitSet "bs" część zaczynającą się w "start"(włącznie) i "stop"(bez) 
     * @param bs
     * @param start
     * @param stop
     * @return Wycinek "bs", tzn. bz[start..<stop] 
     */
    static BitSet part(BitSet bs, int start, int stop) {
        BitSet ret = new BitSet();
        int index = 0;
        
        //wytnij odpowiednią część
        for(int i=start; i<stop; i++,index++){
            //System.out.println(i+"->"+bs.get(i));
            ret.set(index, bs.get(i));
        }
        
        return ret;
    }

    /**
     * Zwraca zbiór bitów będący xor'em wartości w arg0 i arg1
     *  jeśli jeden z wektorów jest dłuższy to jego reszta zostanie zignorowana
     * @param arg0
     * @param arg1
     * @param len  Dlugość ciągu wynikowego
     * @return Wektor w taki, że w[i] = (arg0[i] XOR arg1[i]) || (arg0[i] || arg1[i])
     */
    static BitSet xor(BitSet arg0, BitSet arg1, int len) {
        BitSet result = new BitSet();
        //int max = Math.max(arg0.length(), arg1.length());
        for(int i=0; i<len; i++){
            //operacja xor
            result.set(i, arg0.get(i)!=arg1.get(i) );
        }
        return result;
    }

    /**
     * Zamień wektor bitowy na liczbę całkowitą
     * @param bs wektor do zamiany
     * @param len spodziewana długość "bs"
     * @return liczba w systemie dziesiętnym odpowiadająca wektorwi "bs"
     *  zamienionemu z postaci binarnej na dziesiętną
     */
    static Integer bitSetToInt(BitSet bs, int len) {
        int result =0;
        for(int i=0; i<len; i++){
            if(bs.get(i)){
                result += Math.pow(2, len -1 - i);
            }
        }
        return result;
    }

    /**
     * Zsepól dwa bitSety. Wartości z "left" znajdą się po lewej, 
     *  te z "right" po prawej
     * @param left
     * @param right
     * @param len Bit sety nie zawsze mają takie długości jakie powinny mieć
     * @return BitSet będący konkatenacją dwóch BitSetów
     */
    static BitSet merge(BitSet left, BitSet right, int len) {
        int index =0;
        BitSet result = new BitSet();
        for(int i=0; i<len; i++, index++){
            result.set(index, left.get(i));
        }
        for(int i=0; i<len; i++, index++){
            result.set(index, right.get(i));
        }
        return result;
    }

    /**
     * Zamień BitSet bs na reprezentację zero-jedynkową
     * @param bs Zbiór do zapisania
     * @param len Długość właściwa "bs"
     * @return 
     */
    static String bitSetToString(BitSet bs, int len) {
        StringBuilder string = new StringBuilder();
        for(int i=0; i<len; i++){
            if(bs.get(i)){
                string.append('1');
            }else{
                string.append('0');
            }
        }
        return string.toString();
    }

    /**
     * Tworzy losowy BitSet długości i
     * @param len
     * @return 
     */
    static BitSet randomBitSet(int len) {
         Random generator = new Random();
         BitSet bs = new BitSet();
         for(int i=0; i<len; i++){
             bs.set(i,generator.nextBoolean());
         }
         return bs;
    }
}

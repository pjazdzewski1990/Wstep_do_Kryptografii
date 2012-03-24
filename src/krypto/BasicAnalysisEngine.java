/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.util.TreeMap;

/**
 *
 * @author Patryk
 */
class BasicAnalysisEngine implements Runnable {

    //ilość rozwarzanych liter w alfabecie
    protected int alp = 127;
    //określa czy algorytm ma korzystać z dodatkowego
    // pliku z podpowiedzią
    protected boolean hasHint;
    //nazwa + ścieżka do pliku pomocniczego
    protected String extra;
    //nazwa + ścieżka pliku źródłowego
    // to on przechowuje zaszyforwany plik(do żłamania) 
    protected String src;
    //nazwa + ścieżka pliku docelowego
    // tu umieszczamy wynik
    protected String dest;
    //mapa powiązań: znak -> prawdopodobieństwo wystąpienia w tekście 
    TreeMap<Character, Double> frequency = initFrequencyMap();

    /*
     * Tryb pracy. Wykorzystywać plik ekstra czy nie?
     */
    public void hintMode(boolean _hasHint) {
        hasHint = _hasHint;
    }

    @Override
    public void run() {
        //uruchamianie instancji tej klasy jest błędem logicznym
        throw new UnsupportedOperationException("BasicAnalysisEngine jest wzrocem klas i nie nadaje się do uruchomienia");
    }

    private TreeMap<Character, Double> initFrequencyMap() {
        TreeMap<Character, Double> f = new TreeMap<>();
        
        //ustaw wartości - częstotliwości liter
        f.put('a', 7.396);
        f.put('b', 1.4); 
        f.put('c', 3.281); 
        f.put('d', 2.805); 
        f.put('e', 6.5370); 
        f.put('f', 0.176); 
        f.put('g', 1.297); 
        f.put('h', 0.925); 
        f.put('i', 7.217);
        f.put('j', 1.788); 
        f.put('k', 2.66); 
        f.put('l', 1.86); 
        f.put('m', 2.701); 
        f.put('n', 4.443); 
        f.put('o', 6.111); 
        f.put('p', 2.488); 
        f.put('q', 0.004); 
        f.put('r', 3.513);
        f.put('s', 3.475); 
        f.put('t', 3.007); 
        f.put('u', 1.797); 
        f.put('v', 0.014); 
        f.put('w', 3.53); 
        f.put('x', 0.006); 
        f.put('y', 3.38); 
        f.put('z', 4.884);
        /*f.put('ą', 0.926);
        f.put('ć', 0.424); 
        f.put('ę', 1.196); 
        f.put('ł' , 2.273); 
        f.put('ń' , 0.131); 
        f.put('ó', 0.708); 
        f.put('ś', 0.067); 
        f.put('ź', 0.067); 
        f.put('ż', 0.789);
        f.put(' ', 16.13);*/
        /* z http://students.mimuw.edu.pl/~aw214573/krypto/vigenere.php
         * A	7,396 
         * B	1,4 
         * C	3,281 
         * D	2,805 
         * E	6,537 
         * F	0,176 
         * G	1,297 
         * H	0,925 
         * I	7,217
         * J	1,788 
         * K	2,66 
         * L	1,86 
         * M	2,701 
         * N	4,443 
         * O	6,111 
         * P	2,488 
         * Q	0,004 
         * R	3,513
         * S	3,475 
         * T	3,007 
         * U	1,797 
         * V	0,014 
         * W	3,53 
         * X	0,006 
         * Y	3,38 
         * Z	4,884
         * Ą	0,926
         * Ć	0,424 
         * Ę	1,196 
         * Ł	2,273 
         * Ń	0,131 
         * Ó	0,708 
         * Ś	0,067 
         * Ź	0,067 
         * Ż	0,789
         * spacja   16,13
         */

        return f;
    }
}

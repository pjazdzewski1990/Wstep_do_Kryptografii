/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

import java.util.HashMap;

/**
 * Klasa wykonująca zadania kryptograficzne
 * @author Patryk
 */
public class KryptoMaster {
    
    //polecenie: 
    // e-szyfrujemy, 
    // d-deszyfrujemy
    char command = 'e';
    
    //typ szyfru: 
    // c-kod Cezara, 
    // a-afiniczny
    char code = 'c';
    
    //klucz wg. ktorego szyfrujemy
    String key;
    
    //silnik obslugujący szyfrowanie/deszyfrowanie
    BasicCodeEngine kce;
    
    //silnik obsługujący analizę/łamanie kodu
    BasicAnalysisEngine kae;
    
    //na dodatkowe parametry
    // plainFile - plik NIE zaszyfrowany 
    // cryptoFile - plik po zaszyfrowaniu
    // decryptFile - plik po od szyfrowaniu
    // keyFile - plik z kluczem szyfrowania
    // extraFile - plik zawierający pomocniczy tekst jawny w przypadku kryptoanalizy z tekstem jawnym i zaszyfrowanym
    HashMap<String,String> params = new HashMap<>();
    
    /**
     * Wybiera z tablicy args argumenty określające parametry szyfrowania
     * @args parametry przekazane z zewnątrz    
     * @return jesli coś poszło nie tak można zwrócić false by to sygnalizować
     */
    private boolean getCommands(String[] args) {
        String[] param;
        for(String str : args){
            param = str.split("=");
            if(param.length == 2){
                params.put(param[0], param[1]);
            }else{
                if(param[0].equals("c")){
                    code = 'c';
                }
                if(param[0].equals("a")){
                    code = 'a';
                }
                if(param[0].equals("v")){
                    code = 'v';
                }
                if(param[0].equals("x")){
                    code = 'x';
                }
                if(param[0].equals("i")){
                    code = 'i';
                }
                if(param[0].equals("e")){
                    command = 'e';
                }
                if(param[0].equals("d")){
                    command = 'd';
                }
                if(param[0].equals("j")){
                    command = 'j';
                }
                if(param[0].equals("k")){
                    command = 'k';
                }
            }
        }
        
        setDefaults();
        
        return true;
    }
    
    /**
     * Utwórz i zainicjuj silniki szukające
     */
    private void prepareEnv() {
        String a;
        String b;
        String key;
        String analyze = null;
        String times = null;
        
        switch(code){
            case 'c': //kodowanie Cezara
                b = get("b", "1");
                kce = new Cesar(Integer.parseInt(b));
                          
                kae = new CesarB(
                        get("cryptoFile", "crypto.txt"), 
                        get("decryptFile", "decrypt.txt"),
                        get("extraFile", "extra.txt"));
            break;
            case 'a': //kodowanie afiniczne
                a = get("a", "2");
                b = get("b", "1");
                kce = new Afinic(Integer.parseInt(a), Integer.parseInt(b));
                
                kae = new AfinicB(
                        get("cryptoFile", "crypto.txt"), 
                        get("decryptFile", "decrypt.txt"),
                        get("extraFile", "extra.txt"));
            break;
            case 'v': //kodowanie Vigenere'a
                key = get("key", "a");
                kce = new Vigenere(key);
                
                kae = new VigenereB(
                        get("cryptoFile", "crypto.txt"), 
                        get("decryptFile", "decrypt.txt"),
                        get("extraFile", "extra.txt"));
            break;
            case 'x': //szyfr DES
                //jeśli został przekazany odpowiedni parametr to włącz tryb debugu
                if(get("debug", "false").equals("true")){
                    analyze = get("analyzeFile", "analyze.txt");
                }
                //-1 oznacza automatyczne dostosowywanie się do długości klucza
                times = get("timesDES", "8");
                kce = new DES(H.readFileLine(get("keyFile", "key.txt")), analyze, times);
            break;
            case 'i': //szyfr DES dla obrazów(*.bmp)
                //jeśli został przekazany odpowiedni parametr to włącz tryb debugu
                if(get("debug", "false").equals("true")){
                    analyze = get("analyzeFile", "analyze.txt");
                }
                //-1 oznacza automatyczne dostosowywanie się do długości klucza
                times = get("timesDES", "8");
                
                BlockCipherMode mode;
                switch(get("blockMode","ECB")){
                    case "CBC":
                        mode = BlockCipherMode.CBC;
                        break;
                    case "ECB":
                    default:
                        mode = BlockCipherMode.ECB;
                }
                
                kce = new ImageDES(H.readFileLine(get("keyFile", "key.txt")), analyze, times, mode);
            break;                
            default :
                throw new IllegalStateException("KryptoMaster znalazł się w nie poprawnym stanie");
        }
    }
    
    /**
     * Wykonaj polecenie
     */
    private void launchCommand() {
        //przygotuj obiekt na rozpoczecie operacji
        prepareEnv();
        
        switch(command){
            case 'e'://zakoduj
                kce.encodingMode(true);
                kce.src = get("plainFile", "plain.txt");
                kce.dest = get("cryptoFile", "crypto.txt"); 
                new Thread(kce).start();
                break;
            case 'd'://dekoduj
                kce.encodingMode(false);
                kce.dest = get("decryptFile", "decrypt.txt");
                kce.src = get("cryptoFile", "crypto.txt");
                new Thread(kce).start();
                break;
            case 'j'://łam z tekstem jawnym
                kae.hintMode(true);
                new Thread(kae).start();
                break;
            case 'k'://łam bez tekstu jawnego
                kae.hintMode(false);
                new Thread(kae).start();
                break;
            default:
                throw new IllegalStateException("KryptoMaster nie odnalazł polecenia " + command);
        }
    }
    
    /**
     * Stwórz obiekt krypto master argumentami z linii poleceń
     */
    public KryptoMaster(String[] args){
        H.pl("KryptoMaster start");
        
        if(!getCommands(args)){
            H.pl("Nie pooprawne argumenty linii poleceń");
            System.exit(1);
        }
        
        //uruchom operację
        launchCommand();
        
        H.pl("KryptoMaster end");
    }

    /*
     * Metoda pobierająca z mapy haszującej wartość o podanej nazwie "name"
     * w przeciwnym wypadku zwraóci "_default"
     * @param name nazwa pobieranego parametru
     * @param _default wartość zwracan gdy nie ma "nazwa" w mapie
     * @ret wartość params.name lub "_default" gdy zadane nie istnieje
     */
    private String get(String name, String _default) {
        String ret = params.get(name);
        if(ret == null){
            ret = _default;
            if(name.equals("key")){
                //specjalny przypadek dla długich kluczy = odszukaj wartość w pliku "params.keyFile"
                // zwróć zawartość pliku i zapisz do tablicy parametrów
                ret = H.readFileLine(get("keyFile", "key.txt"));
                params.put("key", ret);
            }
        }
        return ret;
    }

    /*
     * Metoda ustawia domyslne wartości niektórych parametrów na wypadek,
     * gdyby użytkownik nie podał ich z linii poleceń 
     */
    private void setDefaults() {
        if(params.get("plainFile") == null){
            params.put("plainFile", "plain.txt");
        }
        if(params.get("cryptoFile") == null){
            params.put("cryptoFile", "crypto.txt");
        }
        if(params.get("decryptFile") == null){
            params.put("decryptFile", "decrypt.txt");
        }
        if(params.get("keyFile") == null){
           params.put("keyFile", "key.txt");
        }
        if(params.get("extraFile") == null){
           params.put("extraFile", "extra.txt");
        }
        if(params.get("analyzeFile") == null){
           params.put("analyzeFile", "analyze.txt");
        }
    }

}

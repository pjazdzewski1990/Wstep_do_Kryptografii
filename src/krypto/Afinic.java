/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;

/**
 * Kod afiniczny
 * @author Patryk
 */
public class Afinic extends BasicCodeEngine{
    int a = 1;
    int b = 0;
    
    public Afinic(int _a, int _b){
       a = _a;
       b = _b;
       if(H.NWD(a,26) > 1){
           H.pl("ERROR! A i 26 nie są względnie pierwsze.");
           H.pl("Kontynuuję, ale wyniki nie będą poprawne");
       }
    }
    
    @Override
    public Character encode(Character ch) {
        //System.out.println((a * ch.charValue() + b) % alp);
        return (char)((a * ch.charValue() + b) % alp);
    }
    
    @Override
    public Character decode(Character ch) {
        //return (char)(((ch.charValue() - b) /a) % alp);
        int znak = ch.charValue();
        
        znak -= b;
        //znak /= a;
        znak = znak * H.moduloInverse(a, alp);
        if(znak >= 0){
            znak %= alp; 
        }else{
            while(znak <= -alp){
                znak += alp;
            }
            znak = alp +znak;
        }
        
        return (char)znak;
    }
    
    @Override
    public void run() {
        if(isEncoding){
            encode(src, dest);
        }else{
            decode(src,dest);
        }
        H.pl("Afinic: End");
    }
}

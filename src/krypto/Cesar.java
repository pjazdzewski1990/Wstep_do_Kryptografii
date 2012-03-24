/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krypto;


/**
 * Klasa reprezentująca szyfr Cezara
 * @author Patryk
 */
public class Cesar extends BasicCodeEngine{

    int rot = 1;
    
    public Cesar(int _rot){
       rot = _rot;
    }
    
    @Override
    public Character encode(Character ch) {
        //System.out.println((ch.charValue() + rot) % alp);
        return (char)((ch.charValue() + rot) % alp);
    }
    
    @Override
    public Character decode(Character ch) {
        int znak = ch.charValue();
        
        znak -= rot;
        if(znak >= 0){
            znak %= alp; 
        }else{
            while(znak <= -alp){
                znak += alp;
            }
            znak = alp +znak;
        }
        
        return (char)znak;
        //return (char)((ch.charValue() - rot) % alp);
    }
    
    @Override
    public void run() {
        if(isEncoding){
            encode(src, dest);
        }else{
            decode(src,dest);
        }
        H.pl("Cesar: End");
    }
    
}

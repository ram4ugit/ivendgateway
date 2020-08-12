package com.limitlessmobil.ivendgateway.util;
 
import java.util.Random;
 
public class RandomString {
 
    private static final String CHAR_LIST = 
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
     
    /**
     * This method generates random string
     * @return
     */
    public String generateRandomString(int RANDOM_STRING_LENGTH){
         
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }
     
    /**
     * This method generates random numbers
     * @return int
     */
    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }
     
    public static void main(String a[]){
        RandomString msr = new RandomString();
        System.out.println(msr.generateRandomString(16));
    }
}
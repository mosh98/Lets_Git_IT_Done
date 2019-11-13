package com.company;

public class Main {

    public static void main(String[] args) {

        StringBuilder stringBuilder = new StringBuilder();


        String p = "Savage ";
        String p2 = "Cabage ";

        stringBuilder.append( p+"\n");

        for(int i =0; i <3; i++){
            stringBuilder.append(" ");
        }
        stringBuilder.append(p2+  "\n");

        System.out.print(stringBuilder.toString());


    }


}

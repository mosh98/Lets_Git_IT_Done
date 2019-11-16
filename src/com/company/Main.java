package com.company;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws ScriptException {

        StringBuilder tempBuilder = new StringBuilder();

        ArrayList<Character> tempArr = new ArrayList<>();

        String tempString = "AssignmentNode\n" +
                "\tIDENT a\n" +
                "\tASSIGN_OP =\n" +
                "\tExpressionNode\n" +
                "\t\tTermNode\n" +
                "\t\t\tFactorNode\n" +
                "\t\t\t\tINT_LIT 1\n" +
                "\t\t\tMULT_OP *\n" +
                "\t\t\tTermNode\n" +
                "\t\t\t\tFactorNode\n" +
                "\t\t\t\t\tINT_LIT 2\n" +
                "\t\tADD_OP +\n" +
                "\t\tExpressionNode\n" +
                "\t\t\tTermNode\n" +
                "\t\t\t\tFactorNode\n" +
                "\t\t\t\t\tLEFT_PAREN (\n" +
                "\t\t\t\t\tExpressionNode\n" +
                "\t\t\t\t\t\tTermNode\n" +
                "\t\t\t\t\t\t\tFactorNode\n" +
                "\t\t\t\t\t\t\t\tINT_LIT 3\n" +
                "\t\t\t\t\t\tSUB_OP -\n" +
                "\t\t\t\t\t\tExpressionNode\n" +
                "\t\t\t\t\t\t\tTermNode\n" +
                "\t\t\t\t\t\t\t\tFactorNode\n" +
                "\t\t\t\t\t\t\t\t\tINT_LIT 4\n" +
                "\t\t\t\t\tRIGHT_PAREN )\n" +
                "\t\t\t\tDIV_OP /\n" +
                "\t\t\t\tTermNode\n" +
                "\t\t\t\t\tFactorNode\n" +
                "\t\t\t\t\t\tINT_LIT 5";

        /**
         * @forLoop this goes through the string and find the numbers and operators,
         * add it to an arraylist
         * @tempArr */
        for (int i = 0; i < tempString.length(); i++) {

            char thisChar = tempString.charAt(i);


            if (Character.isDigit(thisChar)) {
                tempArr.add(thisChar);
            } else if (thisChar == '+') {
                tempArr.add(thisChar);
            } else if (thisChar == '-') {
                tempArr.add(thisChar);
            } else if (thisChar == '/') {
                tempArr.add(thisChar);
            } else if (thisChar == '*') {
                tempArr.add(thisChar);
            } else if (thisChar == '(') {
                tempArr.add(thisChar);
            } else if (thisChar == ')') {
                tempArr.add(thisChar);
            } else if (thisChar == '.') {
                tempArr.add('.');

            }


        }

        /**
         * @forLoop goes theough the arraylist and adds*/
        for (Character c : tempArr) {
            tempBuilder.append(c);
        }

        System.out.println(tempBuilder.toString());

        String s = tempBuilder.toString();



    }
}



package com.mygdx.game;

import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Test{


    public static void main(String args[])
    {
        try {
            Class className=Class.forName("com.mygdx.game.Starfish");
            System.out.println("HERE!!!!!!!!!");
            Method[] methods=className.getMethods();
            System.out.println("First method is" + methods[0] +  methods[1] + methods[2]);
            for(Method oneMethod: methods){
                System.out.println(oneMethod);
            }
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("ERROR OCCURED!!!!!!!!!!!!!!!!!!!!");
        }
    }
}
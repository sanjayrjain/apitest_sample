package com.assurity.testrunner;

import org.testng.TestNG;
import java.util.ArrayList;
import java.util.List;


public class testRunner {


    public static void main(String[] args)  {

        // Create object of TestNG Class
        TestNG runner = new TestNG();

        // Create a list of String
        List<String> suitefiles=new ArrayList<String>();

        // Add xml file which you have to execute
        suitefiles.add("testng.xml");

        // now set xml file for execution
        runner.setTestSuites(suitefiles);

        try{
            // finally execute the runner using run method
            runner.run();
        }catch(Exception e){

        }
    }

}
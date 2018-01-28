package com.assurity.apitestcases;

import com.assurity.utilities.CommonFunctions;
import com.assurity.utilities.LoadConfigFile;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.utils.ExceptionUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.parser.ParseException;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.assurity.utilities.CommonFunctions.*;

public class base {

    public static ExtentReports extent;
    public static ExtentTest test;
    public static String rootReportFolder;
    public static String reportPath;
    public static String rootFolder;
    public static String dataFolder;
    public static String apiResponseFolder;
    public static Map<String,String> ResponseOrgId = new HashMap<>();
    public static String URL;
    public static String strPath;
    public static ArrayList<String> testcases=new ArrayList<>();


    @BeforeSuite
    public void SetupTest() throws IOException {

        // Create report location.
        rootFolder = System.getProperty("user.dir");
        dataFolder = rootFolder + "\\datafiles\\";
        rootReportFolder = rootFolder + "\\results\\";

        apiResponseFolder = rootReportFolder+"\\api_test\\response_file\\";
        reportPath = rootReportFolder +"\\api_test\\APITest.html";
        extent = CommonFunctions.ExtentManager.getReporter(reportPath);
        test =  extent.startTest("API Test for the Services Exposed, Initial Setup","Objective: To verify all the possible for an API");
        extent.flush();

        strPath = dataFolder+"OrganisationsTestCases.xlsx";
        String responsefile = apiResponseFolder+"OrganizationVerify.json";

        ArrayList<String[]> record = selectDBExcel_MutlipleColumnData(strPath, "Select URL,RequestType,DataToVerify from Sheet1 where TestCase_Name='verifyOrganisationId'"
                , 3);

        String dataToVerify = record.get(0)[0];
        URL = record.get(0)[2];


        try {
            ResponseOrgId = getRequestOrg(dataToVerify,responsefile,URL);
            reporter("PASS","Completed the intial Run","Completed the Initial Setup run.");
        } catch (IOException e) {
            e.printStackTrace();
            reporter("FAIL","Got IO exception , while intial Run", ExceptionUtils.getStackTrace(e));
        } catch (ParseException e) {
            e.printStackTrace();
            reporter("FAIL","Got Parse exception , while intial Run", ExceptionUtils.getStackTrace(e));
        }
        extent.endTest(test);
    }

    @BeforeTest
    public static void beforeTestStart(){

        ArrayList<String[]> record = selectDBExcel_MutlipleColumnData(strPath, "Select TestCase_Name from Sheet1 where TestCase_Name is not null"
                , 1);

        for(int i=0;i<record.size();i++){
            testcases.add(record.get(i)[0]);
        }
        UpdateDBExcel(strPath, "Update Sheet1 set Status='NO RUN' where TestCase_Name is not null");
    }
    @BeforeMethod
    public static void beforeMethod(Method method){
        String testcasename = method.getName();
        test =  extent.startTest(testcasename,"");
        test.assignCategory("Organisations Test");
    }

    @AfterMethod
    public static void afterTest(Method method,ITestResult result){
        extent.flush();
        extent.endTest(test);
        testcases.remove(method.getName());
        if(result.getStatus() == ITestResult.FAILURE){
            UpdateDBExcel(strPath, "Update Sheet1 set Status='FAIL' where TestCase_Name='"+method.getName()+"'");
        }else if(result.getStatus()==ITestResult.SUCCESS){
            UpdateDBExcel(strPath, "Update Sheet1 set Status='PASS' where TestCase_Name='"+method.getName()+"'");
        }else{
            UpdateDBExcel(strPath, "Update Sheet1 set Status='SKIP' where TestCase_Name='"+method.getName()+"'");
        }

    }

    @AfterTest(alwaysRun = true)
    public static void afterTest(){
        if(testcases.size() > 0 ){
            for( int i = 0;i<testcases.size();i++){
                test =  extent.startTest(testcases.get(i),"SKIPPED Test cases");

                test.assignCategory("Organisations Test");
                reporter("SKIP","Test cases SKIPPED","SKIPPED the test case due to Other test case failure");
                extent.endTest(test);
                UpdateDBExcel(strPath, "Update Sheet1 set Status='SKIP' where TestCase_Name='"+testcases.get(i)+"'");
            }

        }
        extent.close();

    }



}

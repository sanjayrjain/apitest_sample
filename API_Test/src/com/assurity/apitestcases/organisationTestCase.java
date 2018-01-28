package com.assurity.apitestcases;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.testng.annotations.Test;
import java.util.ArrayList;
import static com.assurity.utilities.CommonFunctions.*;
import static org.testng.Assert.assertTrue;

public class organisationTestCase extends base{


    @Test(priority = 0)
    public static void verifyOrganisationId() throws Exception {

        try{

            ArrayList<String[]> record = selectDBExcel_MutlipleColumnData(strPath, "Select DataToVerify from Sheet1 where TestCase_Name='verifyOrganisationId'"
                    , 1);

            String dataToVerify = record.get(0)[0];
            String orgid = ResponseOrgId.get("orgid");
            reporter("INFO","Got the Organisation Id from the response, but yet to verify","URL Fired ["+URL+"]");
            boolean bflag = compareStringforEqual(orgid,dataToVerify,"Org Id");

            if (! bflag){
                reporter("FAIL","Organisation id not present matches in the response","Organization Id ["+orgid+"] present in the response");
                assertTrue(false);
            }else{
                reporter("PASS","As expected, Organisation Id matches","Organization Id ["+orgid+"] is present in the response");
            }
        }catch(Exception e){
            reporter("FAIL","Got an unexpected Exception","Got an unexpected Exception "+ ExceptionUtils.getStackTrace(e)+"");
            reporter("INFO","Skipping all the further test cases","Skipped all the test cases.");
            assertTrue(false);
           // e.printStackTrace();
        }
    }


    @Test(dependsOnMethods = "verifyOrganisationId")
    public static void verifyOrganisationName() {

        try
        {
            ArrayList<String[]> record = selectDBExcel_MutlipleColumnData(strPath, "Select DataToVerify from Sheet1 where TestCase_Name='verifyOrganisationName'"
                    , 1);

            String dataToVerify = record.get(0)[0];
            reporter("INFO", "Got the Organisation name from the response, but yet to verify", "URL Fired [" + URL + "]");

            String orgname = ResponseOrgId.get("orgname");
            boolean bflag = compareStringforEqual(orgname, dataToVerify, "Org Name");

            if (!bflag) {
                reporter("FAIL", "Organisation name doesn't matches in the response", "Expected Organization Name is [" +dataToVerify  + "],But ["+orgname+"] present in the response" );
                UpdateDBExcel(strPath, "Update Sheet1 set Status='FAIL' where TestCase_Name='verifyOrganisationName'");
                assertTrue(false);
            } else {
                reporter("PASS", "As expected, Organisation name matches for the Org id["+ResponseOrgId.get("orgid")+"]", "Organization Name [" + orgname + "] present in the response");
                UpdateDBExcel(strPath, "Update Sheet1 set Status='PASS' where TestCase_Name='verifyOrganisationName'");
            }
        }catch(Exception e) {
            e.printStackTrace();
            reporter("FAIL","Got an unexpected Exception","Got an unexpected Exception "+ ExceptionUtils.getStackTrace(e)+"");
            assertTrue(false);
        }
    }

    @Test(dependsOnMethods = "verifyOrganisationId")
    public static void verifyOrgDescriptionContain(){

        try{
            ArrayList<String[]> record = selectDBExcel_MutlipleColumnData(strPath, "Select DataToVerify from Sheet1 where TestCase_Name='verifyOrgDescriptionContain'"
                    , 1);

            String dataToVerify = record.get(0)[0];
            reporter("INFO","Got the Organisation Description from the response, " +
                    "but yet to verify","URL Fired ["+URL+"]");


            String orgdesc = ResponseOrgId.get("orgdescription");
            boolean bflag = TextIsPresent(orgdesc,dataToVerify,"Org Description");
            if (! bflag){
                reporter("FAIL","Organisation Description text not present in the " +
                        "response.","Organization Description ["+dataToVerify+"] is missing in the in the response["+orgdesc+"]");
                assertTrue(false);
            }else{
                reporter("PASS","As expected, Organisation Description" +
                        "contain matches","Organization Description ["+orgdesc+"] present in the response of Org description");
            }
        }catch(Exception e){
            e.printStackTrace();
            reporter("FAIL","Got an unexpected Exception","Got an unexpected Exception "+ ExceptionUtils.getStackTrace(e)+"");
            assertTrue(false);
        }
    }

    @Test(dependsOnMethods = "verifyOrganisationId")
    public static void verifySectorId(){

        try {


            ArrayList<String[]> record = selectDBExcel_MutlipleColumnData(strPath, "Select DataToVerify from Sheet1 where TestCase_Name='verifySectorId'"
                    , 1);

            String dataToVerify = record.get(0)[0];
            reporter("INFO", "Got the Sector ID Description from the response, but yet to verify", "URL Fired [" + URL + "]");

            String secId = ResponseOrgId.get("secid");

            boolean bflag = compareStringforEqual(secId, dataToVerify, "Sector ID");
            if (!bflag) {
                reporter("FAIL", "Sector Id not matching for Org id["+ResponseOrgId.get("orgid")+"]", "Expected Sector ID" +
                        " [" + dataToVerify + "] but actual is ["+secId+"] in the response.");
                assertTrue(false);
            } else {
                reporter("PASS", "As expected, Sector id  matches present in the response Org id["+ResponseOrgId.get("orgid")+"]", "Sector Id [" + secId + "] present in the response");
            }
        }catch(Exception e){
            e.printStackTrace();
            reporter("FAIL","Got an unexpected Exception","Got an unexpected Exception "+ ExceptionUtils.getStackTrace(e)+"");
            assertTrue(false);
        }

    }


    @Test(dependsOnMethods = "verifyOrganisationId")
    public static void verifySectorName(){
        try {
            ArrayList<String[]> record = selectDBExcel_MutlipleColumnData(strPath, "Select DataToVerify from Sheet1 where TestCase_Name='verifySectorName'"
                    , 1);

            String dataToVerify = record.get(0)[0];
            reporter("INFO", "Got the Organisation Description from the response, but yet to verify", "URL Fired [" + URL + "]");
            String sectorName = ResponseOrgId.get("sec_name");
            boolean bflag = compareStringforEqual(sectorName, dataToVerify, "Sector Name");
            if (!bflag) {
                reporter("FAIL", "Sector Name is not present matching with the response content", "Actual Sector Name [" + sectorName + "] present in the response" +
                        ", But Expected is ["+dataToVerify+"]");
                assertTrue(false);
            } else {
                reporter("PASS", "As expected, Sector Name  matches present in the response Org id["+ResponseOrgId.get("orgid")+"]", "Sector Name [" + sectorName + "] present in the response");
            }
        }catch(Exception e){
            e.printStackTrace();
            reporter("FAIL","Got an unexpected Exception","Got an unexpected Exception "+ ExceptionUtils.getStackTrace(e)+"");
            assertTrue(false);
        }
    }







}

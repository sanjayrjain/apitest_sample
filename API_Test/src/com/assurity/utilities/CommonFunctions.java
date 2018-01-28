package com.assurity.utilities;


import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContexts;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.assurity.apitestcases.base.*;


public class CommonFunctions {

	public static Properties SYSPARAM=null;
	static ExtentTest logger ;

	public static void initData() throws IOException {
	
	SYSPARAM= new Properties ();
	String path = new File("\\resources\\globalConfig.properties").getAbsolutePath();
	FileInputStream ist= new FileInputStream(path);
	SYSPARAM.load(ist);
	}

	public static class ExtentManager {
		private static ExtentReports extent;

		public synchronized static ExtentReports getReporter(String filePath) {
			if (extent == null) {
				extent = new ExtentReports(filePath, true);
				try {
					extent
							.addSystemInfo("Run Machine - Host Name", InetAddress.getLocalHost().getHostName())
							.addSystemInfo("Run Machine -  IP Address", InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

			}
			return extent;
		}
	}



// Pro Workstation Login Function

	public static void assertBoolean(ExtentTest log , String testStatus , String description, String faildescription){
		//String fPath = fileName;
		//String img = log.addScreenCapture(fPath);
		//System.out.println(testStatus);

		if (testStatus.equalsIgnoreCase("info")){
			log.log(LogStatus.INFO, description );
			return;
		}
		if (testStatus.equalsIgnoreCase("pass")){
			log.log(LogStatus.PASS, description, faildescription );
			return;
		}
		if (testStatus.equalsIgnoreCase("warning")){
			log.log(LogStatus.WARNING, faildescription );
			return;
		}
		if (testStatus.equalsIgnoreCase("skip")){
			log.log(LogStatus.SKIP, faildescription );
			return;
		}
		if (testStatus.equalsIgnoreCase("fatal")){

			log.log(LogStatus.FATAL, faildescription );
			return;
		}
		else
		{
			log.log(LogStatus.FAIL,description,faildescription );
			return;
		}
	}

	public static void reporter(String Status , String stepname , String description){
		//String fileName = new SimpleDateFormat("yyyyMMddhhmmssmm'.png'").format(new Date());

		switch (Status.toLowerCase())
		{
			case "info":
				if (  LoadConfigFile.getInstance().getValue("logLevel").equalsIgnoreCase("DEBUG")){
					test.log(LogStatus.INFO,stepname,description);
				}
				break;

			case "pass":
			case "passed":
				//fileName = rootReportFolder+"\\PASS_"+fileName;
				assertBoolean(test,"PASS",stepname,description);
				break;

			case "fatal":
				//fileName = rootReportFolder+"\\Fatal_"+ fileName;
				assertBoolean(test,"Fatal",stepname,description);
				break;

			case "warning":
				//fileName = rootReportFolder+"\\Warning_"+ fileName;
				assertBoolean(test,"Warning",stepname,description);
				break;

			case "skip":
				//fileName = rootReportFolder+"\\Warning_"+fileName;
				assertBoolean(test,"Skip",stepname,description);
				break;

			case "fail":
			case "failed":
				//fileName = rootReportFolder+"\\FAIL_"+ fileName;
				assertBoolean(test,"Fail",stepname,description);
				break;

			default:
				assertBoolean(test,"WARNING",stepname,description+"\n Note:[this status is not currently supported");
				break;
		}
		extent.flush();
	}

	public static synchronized void UpdateDBExcel(String path, String query) {

		Fillo fillo = new Fillo();
		Connection connection = null;
		try {
			connection = fillo.getConnection(path);
			connection.executeUpdate(query);
		} catch (Exception e) {

			e.printStackTrace();
		}

		connection.close();
	}

	public static synchronized int selectDBExcel_RowCount(String path, String query) {

		Fillo fillo = new Fillo();
		Connection connection = null;
		int count = 0;
		try {
			connection = fillo.getConnection(path);
			Recordset result = connection.executeQuery(query);

			count = result.getCount();

		} catch (Exception e) {

			// e.printStackTrace();
		}

		connection.close();
		return count;
	}

	public static synchronized List<String> selectDBExcel_ColumnData(String path, String query) {

		Fillo fillo = new Fillo();
		Connection connection = null;
		List<String> list = new ArrayList<String>();

		try {
			connection = fillo.getConnection(path);
			Recordset result = connection.executeQuery(query);

			while (result.next()) {
				list.add(result.getField(result.getField(0).name()).toString());
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		connection.close();
		return list;
	}

	public static synchronized ArrayList<String[]> selectDBExcel_MutlipleColumnData(String path, String query, int noOfColumns) {

		Fillo fillo = new Fillo();
		Connection connection = null;
		ArrayList<String[]> list = new ArrayList<String[]>();

		try {
			connection = fillo.getConnection(path);
			Recordset result = connection.executeQuery(query);

			while (result.next()) {
				String[] st = new String[noOfColumns];
				for (int i = 0; i < noOfColumns; i++) {
					st[i] = result.getField(result.getField(i).name()).toString();
				}
				list.add(st);

			}
		} catch (Exception e) {

			// e.printStackTrace();
		}

		connection.close();
		return list;
	}


	public static Map<String,String> getRequestOrg(String InfoRequestOrg,String resultFile,String url_api) throws IOException, ParseException {

		// Make a call to API and save the response to a file.
		boolean apiBflag = getAPIResponseOrg(url_api,resultFile);

		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Map<String, String> maps = new HashMap<>();
		File jsonFile = new File(resultFile);
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(jsonFile));
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray jsonChildObject = (JSONArray)jsonObject.get("organisations");
		for ( int i = 0;i<jsonChildObject.size();i++){
                    String org_id = ((JSONObject) jsonChildObject.get(i)).get("id").toString();
                    if(org_id.equalsIgnoreCase(InfoRequestOrg)) {
                        maps.put("orgid",org_id);

                        String name = ((JSONObject) jsonChildObject.get(i)).get("name").toString();
                        maps.put("orgname",name);
                        System.out.println(name);

                        String description = ((JSONObject) jsonChildObject.get(i)).get("description").toString();
                        maps.put("orgdescription",description);
                        System.out.println(description);

                        Object sec = ((JSONObject) jsonChildObject.get(i)).get("sector");
                        Object sec_id = ((JSONObject) sec).get("id");
                        System.out.println(sec_id.toString());
                        maps.put("secid",sec_id.toString());

                        Object sec_name = ((JSONObject) sec).get("name");
                        System.out.println(sec_name.toString());
                        maps.put("sec_name",sec_name.toString());

                        Object sec_parent = ((JSONObject) sec).get("parentSector");
                        System.out.println(sec_parent.toString());
                        maps.put("sec_parent",sec_parent.toString());
                        break;
                    }
		}
		return maps;
	}


	private static boolean getAPIResponseOrg(String url,String filepath) throws IOException {

		boolean bflag = false;
		CookieStore cs = new BasicCookieStore();
		SSLContext sslContext = SSLContexts.createDefault();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, new NoopHostnameVerifier());
		HttpClient cl = HttpClientBuilder.create().setSSLSocketFactory(sslsf).setDefaultCookieStore(cs).setRedirectStrategy(new LaxRedirectStrategy()).build();
		HttpGet request = new HttpGet(url);
		request.addHeader("Accept-Language", "en-IN");

		try {
			request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			HttpResponse response = cl.execute(request);
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String responsebody = IOUtils.toString(br);
			System.out.println(responsebody);
			reporter("INFO","Got the response","Response is ["+responsebody+"]");
			// Save the string / reponse into a file.

			try {
				File file = new File(filepath);
				boolean result = Files.deleteIfExists(file.toPath());
				writeToFile(filepath, responsebody);
				reporter("INFO","Written the string into a file","File saved is ["+filepath+"]");
				bflag = true;
			}catch (IOException io){
				reporter("FAIL","Failed to write to a file","Failed to write to a file at the location ["+filepath+"]");
			}
		}catch (Exception e){
			reporter("FAIL","Failed to execute the REST Request or read the response.","Got an exception while executing the response request.");
			// Copy the test.json to Response file.
			File file1 = new File(apiResponseFolder+"test.json");
			File file2 = new File(filepath);
			reporter("INFO","Response was not well formated","Copying the saved file into the Organisation.json file");
			copyFileUsingFileStreams(file1,file2);
			bflag = false;
		}

		return bflag;

	}


	public static boolean compareStringforEqual( String DataToVerifyAganist,String string1 , String parameter ){

		if (DataToVerifyAganist.equalsIgnoreCase("") ||
				DataToVerifyAganist==null	){
			return false;
		}

	    if( DataToVerifyAganist.equalsIgnoreCase(string1)){
	        reporter("PASS","Both the values matches",parameter + "  ["+string1+"] is present in the response.");
	        return true;
        }else{
            reporter("FAIL","Both the values mismatches",parameter+" ["+string1+"] is not " +
                    "matching with the value ["+DataToVerifyAganist+"]");
            return false;
        }
    }

    public static boolean TextIsPresent(String DataToVerifyAganist,String value , String parameter){

        if( DataToVerifyAganist.toLowerCase().contains(value.toLowerCase())){
            reporter("PASS","Text is present in the Response String ",parameter + "  ["+value+"] is present in the response.");
            return true;
        }else{
            reporter("FAIL","Text is not present in the Response String",parameter+" ["+value+"] is not " +
                    "present in the response ["+DataToVerifyAganist+"]");
            return false;
        }
    }


	public static void writeToFile(String fileName, String str) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(fileName);
		byte[] strToBytes = str.getBytes();
		outputStream.write(strToBytes);
		outputStream.close();
	}

	private static void copyFileUsingFileStreams(File source, File dest)
			throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}


}

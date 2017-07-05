package methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;

import exception.DiskPermissionsException;
import exception.HddSerialScriptException;
import exception.KeyAlreadyRegisteredException;
import exception.KeyNotExistException;
import exception.KeyNotRegisteredException;
import exception.LisenceExpiredException;
import exception.ProgramFilesBrokenException;
import exception.ServerConnectionException;

public class HTTPClient {

	private final static String USER_AGENT = "Mozilla/5.0";
	private static String SERVER = "https://komplimed.herokuapp.com";
	// private final static String SERVER = "http://localhost:3000";

	public static String makeRequest(String url) throws ServerConnectionException {
		URL obj = null;
		StringBuffer response = null;
		try {
			obj = new URL(url);

			HttpURLConnection con = null;

			con = (HttpURLConnection) obj.openConnection();

			// optional default is GET

			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			BufferedReader in = null;

			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			con.disconnect();
		}

		catch (IOException e) {
			e.printStackTrace();
			throw new ServerConnectionException(e);
		}

		return response.toString();
	}

	public static boolean loginUser(String name, String pass) throws ServerConnectionException {
		String url = null;
		try {
			url = SERVER + "/api/loginuser.xml?name=" + URLEncoder.encode(name, "UTF8") + "&pass="
					+ URLEncoder.encode(pass, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String response = makeRequest(url);

		if (response.equals("1"))
			return true;
		else
			return false;
	}

	public static void registerKey(String key, String userName, String name, String pass) throws 
				DiskPermissionsException, ProgramFilesBrokenException, HddSerialScriptException, 
				ServerConnectionException, KeyNotExistException, KeyAlreadyRegisteredException {
		String url = null;
		String hddSerial = Utils.getHDDSerialNumber();
		try {
			url = SERVER + "/api/registerkey.xml?key=" + URLEncoder.encode(key, "UTF8") + "&user_name="
					+ URLEncoder.encode(userName, "UTF8") + "&hddserial=" + URLEncoder.encode(hddSerial, "UTF8") + "&name="
					+ URLEncoder.encode(name, "UTF8") + "&pass=" + URLEncoder.encode(pass, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String response = makeRequest(url);
		if (response.equals("1"))
			return;
		else if (response.equals("2"))
			throw new KeyAlreadyRegisteredException();
		else
			throw new KeyNotExistException();
	}

	public static void checkKey(String key, String userName) throws 
				DiskPermissionsException, ProgramFilesBrokenException, HddSerialScriptException, 
				ServerConnectionException, KeyNotRegisteredException, LisenceExpiredException {
		String url = null;
		String hddSerial = Utils.getHDDSerialNumber();
		try {
			url = SERVER + "/api/checkkey.xml?key=" + URLEncoder.encode(key, "UTF8") + "&user_name="
					+ URLEncoder.encode(userName, "UTF8") + "&hdd_serial=" + URLEncoder.encode(hddSerial, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String response = makeRequest(url);

		if (response.equals("1"))
			return;
		else if (response.equals("3"))
			throw new LisenceExpiredException();
		else
			throw new KeyNotRegisteredException();
	}

	public static int daysLeft(String key, String userName) throws DiskPermissionsException, 
					ProgramFilesBrokenException, HddSerialScriptException, ServerConnectionException {
		String url = null;
		String hddSerial = Utils.getHDDSerialNumber();
		try {
			url = SERVER + "/api/getdays.xml?key=" + URLEncoder.encode(key, "UTF8") + "&user_name="
					+ URLEncoder.encode(userName, "UTF8") + "&hdd_serial=" + URLEncoder.encode(hddSerial, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String response = makeRequest(url);
		response = response.substring(0, response.indexOf('/'));

		return Integer.parseInt(response);
	}

	public static String getFrom(String key, String userName)
			throws DiskPermissionsException, ProgramFilesBrokenException, HddSerialScriptException, ServerConnectionException {
		String url = null;
		String hddSerial = Utils.getHDDSerialNumber();
		try {
			url = SERVER + "/api/getfrom.xml?key=" + URLEncoder.encode(key, "UTF8") + "&user_name="
					+ URLEncoder.encode(userName, "UTF8") + "&hdd_serial=" + URLEncoder.encode(hddSerial, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String response = makeRequest(url);

		SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = parserSDF.parse(response);
			parserSDF = new SimpleDateFormat("dd.MM.yyyy");
			response = parserSDF.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return response;
	}

	public static String getVersion(String date) throws ServerConnectionException {
		String url = null;
		try {
			url = SERVER + "/api/getversion.xml?date=" + URLEncoder.encode(date, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String response = makeRequest(url);

		if (response.equals("0"))
			return null;

		Document d = Utils.openXMLFromString(response);
		String l = d.getElementsByTagName("location").item(0).getTextContent();
		return l;
	}

	public static void setSERVER(String sERVER) {
		SERVER = sERVER;
	}
}

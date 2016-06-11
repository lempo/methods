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

public class HTTPClient {

	private final static String USER_AGENT = "Mozilla/5.0";
	private static String SERVER = "https://methods-complimed.herokuapp.com";
	// private final static String SERVER = "http://localhost:3000";

	public static String makeRequest(String url) {
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
			Object[] options = { "OK" };
			JOptionPane.showOptionDialog(null, "Не удалось соединиться с сервером!", "Ошибка",
					JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		}

		return response.toString();
	}

	public static boolean loginUser(String name, String pass) {
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

	public static void newUser(String name, String pass) {
		String url = null;
		try {
			url = SERVER + "/api/newuser.xml?name=" + URLEncoder.encode(name, "UTF8") + "&pass="
					+ URLEncoder.encode(pass, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		makeRequest(url);
	}

	public static void deleteUser(String name, String pass) {
		String url = null;
		try {
			url = SERVER + "/api/deleteuser.xml?name=" + URLEncoder.encode(name, "UTF8") + "&pass="
					+ URLEncoder.encode(pass, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		makeRequest(url);
	}

	public static void editUser(String name, String pass, String nameNew, String passNew) {
		String url = null;
		try {
			url = SERVER + "/api/edituser.xml?name=" + URLEncoder.encode(name, "UTF8") + "&pass="
					+ URLEncoder.encode(pass, "UTF8") + "&name_new=" + URLEncoder.encode(nameNew, "UTF8") + "&pass_new="
					+ URLEncoder.encode(passNew, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		makeRequest(url);
	}

	public static boolean registerKey(String key, String userName, String name, String pass) {
		String url = null;
		String hddSerial = Utils.getHDDSerialNumber();
		try {
			url = SERVER + "/api/registerkey.xml?key=" + URLEncoder.encode(key, "UTF8") + "&user_name="
					+ URLEncoder.encode(userName, "UTF8") + "&hddserial=" + URLEncoder.encode(hddSerial, "UTF8")
					+ URLEncoder.encode(name, "UTF8") + "&pass=" + URLEncoder.encode(pass, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String response = makeRequest(url);
		if (response.equals("1"))
			return true;
		else
			return false;
	}

	public static boolean checkKey(String key, String userName) {
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
			return true;
		else
			return false;
	}

	public static int daysLeft(String key, String userName) {
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

	public static String getFrom(String key, String userName) {
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

	public static String getVersion(String date) {
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

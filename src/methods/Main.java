package methods;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import component.CustomDialog;
import defaults.InterfaceTextDefaults;

public class Main {
		public static void main(String[] args) {
			HTTPClient.setSERVER(Utils.getAppServer());

			File f = new File(Utils.getFilePath() + "/config");
			// already registered
			if (f.exists()) {
				// check key
				String key = Utils.getLicenceKey();
				String username = Utils.getLicenceUserName();
				try {
					if (HTTPClient.checkKey(key, username)) {
						JFrame app = new Methods();
						app.setVisible(true);
						if (Utils.getCheckUpdatesAuto()) {
							DateFormat format = new SimpleDateFormat("dd.MM.yy");
							Utils.setLastCheckUpdates(format.format(new Date()));
							String location = HTTPClient.getVersion(Utils.getVersionDate());
							if (location != null) {
								// update
								// show dialog
								CustomDialog d1 = new CustomDialog(app,
										InterfaceTextDefaults.getInstance().getDefault("do_update"),
										InterfaceTextDefaults.getInstance().getDefault("yes"),
										InterfaceTextDefaults.getInstance().getDefault("no"), false);
								if (d1.getAnswer() == 1) {
									try {
										Utils.setLastDownloadUpdates(format.format(new Date()));
										Process proc = Runtime.getRuntime().exec("java -jar updater.jar " + location);
										System.exit(0);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								} else
									return;
							}
						}
					} else {
						JOptionPane.showConfirmDialog((java.awt.Component) null,
								InterfaceTextDefaults.getInstance().getDefault("no_licence"),
								InterfaceTextDefaults.getInstance().getDefault("licence_failed"),
								javax.swing.JOptionPane.DEFAULT_OPTION);
					}
				} catch (HeadlessException e) {
					Object[] options = { "OK" };
					JOptionPane.showOptionDialog(null, "Файлы приложения повреждены", "Ошибка",
							JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					e.printStackTrace();
				} catch (IOException e) {
					Object[] options = { "OK" };
					JOptionPane.showOptionDialog(null, "Не удалось соединиться с сервером!", "Ошибка",
							JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					e.printStackTrace();
				}
			}
			// not registered yet
			else {
				// licence dialog
				UIManager.put("OptionPane.cancelButtonText", InterfaceTextDefaults.getInstance().getDefault("cancel"));
				UIManager.put("OptionPane.okButtonText", InterfaceTextDefaults.getInstance().getDefault("ok"));
				JTextField name = new JTextField();
				JTextField key = new JTextField();
				JTextField login = new JTextField();
				JTextField pass = new JTextField();
				final JComponent[] inputs = new JComponent[] {
						new JLabel(InterfaceTextDefaults.getInstance().getDefault("name_surname_patronymic") + ":"), name,
						new JLabel(InterfaceTextDefaults.getInstance().getDefault("key") + ":"), key,
						new JLabel(InterfaceTextDefaults.getInstance().getDefault("create_login") + ":"), login,
						new JLabel(InterfaceTextDefaults.getInstance().getDefault("create_pass") + ":"), pass};
				int reply = JOptionPane.showConfirmDialog(null, inputs,
						InterfaceTextDefaults.getInstance().getDefault("enter_licence_data"), JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (reply != JOptionPane.OK_OPTION)
					return;

				// create file
				try {
					DateFormat format = new SimpleDateFormat("dd.MM.yy");
					f.createNewFile();
					OutputStreamWriter bufferedWriter = new OutputStreamWriter(new FileOutputStream(f), "UTF8");
					bufferedWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
					bufferedWriter.append("<config>\n");
					bufferedWriter.append("<username>" + name.getText().trim() + "</username>\n");
					bufferedWriter.append("<key>" + key.getText().trim() + "</key>\n");
					bufferedWriter.append("<checkUpdatesAuto>false</checkUpdatesAuto>");
					bufferedWriter.append("<lastCheck>" + format.format(new Date()) + "</lastCheck>\n");
					bufferedWriter.append("<lastDownload>" + format.format(new Date()) + "</lastDownload>\n");
					bufferedWriter.append("</config>");
					bufferedWriter.flush();
					bufferedWriter.close();

					if (HTTPClient.registerKey(key.getText().trim(), name.getText().trim(), login.getText().trim(), pass.getText().trim())) {
						JFrame app = new Methods();
						app.setVisible(true);
					} else {
						f.delete();
						JOptionPane.showConfirmDialog((java.awt.Component) null,
								InterfaceTextDefaults.getInstance().getDefault("no_licence"),
								InterfaceTextDefaults.getInstance().getDefault("licence_failed"),
								javax.swing.JOptionPane.DEFAULT_OPTION);
					}
				} catch (Exception e) {
					e.printStackTrace();
					f.delete();
					JOptionPane.showConfirmDialog((java.awt.Component) null,
							InterfaceTextDefaults.getInstance().getDefault("no_licence"),
							InterfaceTextDefaults.getInstance().getDefault("licence_failed"),
							javax.swing.JOptionPane.DEFAULT_OPTION);
					e.printStackTrace();
				}

			}
		}
}

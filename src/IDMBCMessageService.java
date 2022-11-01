import java.io.FileInputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class IDMBCMessageService {
	String maxvmusepercent;
	String cleansession;
	boolean res_cleansession = false;
	String keepalive;
	int res_keepalive = 70;
	String reconnect;
	boolean res_reconnect = false;
	String limit_read;
	boolean res_limit_read = false;
	String flag_read_initial;
	boolean res_flag_read_initial = false;
	String read_command_topic_hastag;
	boolean res_read_command_topic_hastag = false;
	MqttClient client_transreport_login;
	MqttClient client_transreport;
	String will_retained;
	boolean res_will_retained = false;
	String ip_mongo_db;
	Global_function gf = new Global_function();
	Global_variable gv = new Global_variable();
	Interface_ga inter_login;
	Connection con;
	SQLConnection sqlcon = new SQLConnection();
	int counter = 1;

	public IDMBCMessageService() {

	}

	public String get_tanggal_curdate() {
		String res = "";
		try {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

			String bulan = "";
			if (month < 10) {
				bulan = "0" + month;
			} else {
				bulan = "" + month;
			}
			int d = Calendar.getInstance().get(Calendar.DATE);
			String tanggal = "";
			if (d < 10) {
				tanggal = "0" + d;
			} else {
				tanggal = "" + d;
			}
			String concat = year + "" + bulan + "" + tanggal;
			res = concat;
		} catch (Exception e) {
			res = "";
		}

		return res;
	}

	public String get_tanggal_curdate_curtime() {
		String res = "";
		try {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			String bulan = "";
			if (month < 10) {
				bulan = "0" + month;
			} else {
				bulan = "" + month;
			}
			int d = Calendar.getInstance().get(Calendar.DATE);
			String tanggal = "";
			if (d < 10) {
				tanggal = "0" + d;
			} else {
				tanggal = "" + d;
			}
			int h = Calendar.getInstance().get(Calendar.HOUR);
			String jam = "";
			if (h < 10) {
				jam = "0" + h;
			} else {
				jam = "" + h;
			}
			int min = Calendar.getInstance().get(Calendar.MINUTE);
			String menit = "";
			if (min < 10) {
				menit = "0" + min;
			} else {
				menit = "" + min;
			}
			int sec = Calendar.getInstance().get(Calendar.SECOND);
			String detik = "";
			if (sec < 10) {
				detik = "0" + sec;
			} else {
				detik = "" + sec;
			}
			String concat = year + "" + bulan + "" + tanggal + "" + jam + "" + menit + "" + detik;
			res = concat;
		} catch (Exception e) {
			res = "";
		}

		return res;
	}

	public String get_tanggal_curdate_curtime_format() {
		String res = "";
		try {
			LocalDateTime myDateObj = LocalDateTime.now();
			// System.out.println("Before formatting: " + myDateObj);

			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			String formattedDate = myDateObj.format(myFormatObj);
			System.out.println("After formatting: " + formattedDate);
			res = formattedDate;
		} catch (Exception e) {
			res = "";
		}

		return res;
	}

	String Parser_TASK, Parser_ID, Parser_SOURCE, Parser_COMMAND, Parser_OTP, Parser_TANGGAL_JAM, Parser_VERSI,
			Parser_HASIL, Parser_FROM, Parser_TO, Parser_SN_HDD, Parser_IP_ADDRESS, Parser_STATION, Parser_CABANG,
			Parser_NAMA_FILE, Parser_CHAT_MESSAGE, Parser_REMOTE_PATH, Parser_LOCAL_PATH, Parser_SUB_ID;

	public void UnpackJSON(String json_message) {

		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(json_message);
		} catch (org.json.simple.parser.ParseException ex) {
			System.out.println("message json : " + json_message);
			System.out.println("message error : " + ex.getMessage());
			// ex.printStackTrace();
			// Logger.getLogger(IDMReport.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			Parser_TASK = obj.get("TASK").toString();
		} catch (Exception ex) {
			Parser_TASK = "";
		}
		try {
			Parser_ID = obj.get("ID").toString();
		} catch (Exception exc) {
			Parser_ID = "";
		}
		try {
			Parser_SOURCE = obj.get("SOURCE").toString();
		} catch (Exception exc) {
			Parser_SOURCE = "";
		}
		try {
			Parser_COMMAND = obj.get("COMMAND").toString();
		} catch (Exception exc) {
			Parser_COMMAND = "";
		}
		try {
			Parser_OTP = obj.get("OTP").toString();
		} catch (Exception exc) {
			Parser_OTP = "";
		}

		try {
			Parser_TANGGAL_JAM = obj.get("TANGGAL_JAM").toString();
		} catch (Exception exc) {
			Parser_TANGGAL_JAM = "";
		}
		try {
			Parser_VERSI = obj.get("RESULT").toString().split("_")[7];
		} catch (Exception exc) {
			try {
				Parser_VERSI = obj.get("VERSI").toString();
			} catch (Exception exc1) {
				Parser_VERSI = "";
			}

		}

		try {
			Parser_HASIL = obj.get("HASIL").toString();
			Parser_FROM = obj.get("FROM").toString();
			Parser_TO = obj.get("TO").toString();

		} catch (Exception exc) {
			Parser_HASIL = "";
			Parser_FROM = "";
			Parser_TO = "";
		}

		try {
			Parser_SN_HDD = obj.get("SN_HDD").toString();
		} catch (Exception exc) {
			try {
				Parser_SN_HDD = obj.get("SN_HDD").toString();
			} catch (Exception exc1) {
				Parser_SN_HDD = "";
			}

		}
		try {
			Parser_IP_ADDRESS = obj.get("IP_ADDRESS").toString();
		} catch (Exception exc) {
			try {
				Parser_IP_ADDRESS = obj.get("IP_ADDRESS").toString();
			} catch (Exception exc1) {
				Parser_IP_ADDRESS = "";
			}

		}

		try {
			Parser_STATION = obj.get("STATION").toString();
		} catch (Exception exc) {
			try {
				Parser_STATION = obj.get("STATION").toString();
			} catch (Exception exc1) {
				Parser_STATION = "";
			}

		}

		try {
			Parser_CABANG = obj.get("CABANG").toString();
		} catch (Exception exc) {
			try {
				Parser_CABANG = obj.get("CABANG").toString();
			} catch (Exception exc1) {
				Parser_CABANG = "";
			}
		}

		try {
			Parser_NAMA_FILE = obj.get("NAMA_FILE").toString();
		} catch (Exception exc) {
			Parser_NAMA_FILE = "";
		}
		try {
			Parser_CHAT_MESSAGE = obj.get("CHAT_MESSAGE").toString();
		} catch (Exception exc) {
			Parser_CHAT_MESSAGE = "";
		}
		try {
			Parser_REMOTE_PATH = obj.get("REMOTE_PATH").toString();
		} catch (Exception exc) {
			Parser_REMOTE_PATH = "";
		}
		try {
			Parser_LOCAL_PATH = obj.get("LOCAL_PATH").toString();
		} catch (Exception exc) {
			Parser_LOCAL_PATH = "";
		}
		try {
			Parser_SUB_ID = obj.get("SUB_ID").toString();
		} catch (Exception exc) {
			Parser_SUB_ID = "";
		}

	}
	
	public void BCMessageService() {
		try {
			
			// ---------------------------- COMMAND -----------------------//
			int qos_message_command = 0;
			String topic_bc = gf.getTopic();
			Boolean show_insert = Boolean.parseBoolean(gf.getTampilkan_query_console());
			System.out.println("SUBS : "+topic_bc);
			client_transreport.subscribe(topic_bc, qos_message_command, new IMqttMessageListener() {
				@Override
				public void messageArrived(final String topic, final MqttMessage message) throws Exception {
					// ----------------------------- FILTER TOPIC NOT CONTAINS
					// -------------------------------//
					SimpleDateFormat sformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					Date HariSekarang = new Date();
					Date HariSekarang_run = new Date();
					String payload = new String(message.getPayload());
					int Qos = message.getQos();
					String msg_type = "";
					String message_ADT_Decompress = "";
					if (topic.equals("BC/REPORT")) {

					} else {
						try {
							message_ADT_Decompress = gf.ADTDecompress(message.getPayload());
							msg_type = "json";
						} catch (Exception exc) {
							message_ADT_Decompress = payload;
							msg_type = "non json";
						}

						counter++;
						try {
							UnpackJSON(message_ADT_Decompress);
						} catch (Exception exc) {
							exc.printStackTrace();
						}

						gf.PrintMessage2("RECV > "+topic_bc, counter, msg_type, topic, Parser_TASK, Parser_IP_ADDRESS,
								Parser_TO, HariSekarang, HariSekarang_run);
						gf.InsTransReport(Parser_TASK, Parser_ID, Parser_SOURCE, Parser_COMMAND, Parser_OTP,
								Parser_TANGGAL_JAM, Parser_VERSI, Parser_HASIL, Parser_TO, Parser_FROM, Parser_SN_HDD,
								Parser_IP_ADDRESS, Parser_STATION, Parser_CABANG.substring(0, 4), Parser_NAMA_FILE,
								Parser_CHAT_MESSAGE, Parser_REMOTE_PATH, Parser_LOCAL_PATH, Parser_SUB_ID, show_insert,
								"REPLACE", "transreport");
						
						//gf.get_MonitoringResources();
					}
				}

			}); 
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void Run() {
		System.out.println("=================================          START         ==================================");
		try {
			client_transreport = gf.get_ConnectionMQtt();
			BCMessageService();
		}catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}


public class Main {
	public static void main(String args[]) {
		try {
			Global_function gf = new Global_function(false);
			String tanggal_jam = gf.get_tanggal_curdate_curtime();
			gf.WriteFile("timemessage.txt", "", tanggal_jam, false);
			
			ThreadMain t1 = new ThreadMain(1);
			t1.start();
			
			CheckThread t2 = new CheckThread();
			t2.start();
		}catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}

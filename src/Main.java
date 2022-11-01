
public class Main {
	public static void main(String args[]) {
		try {
			ThreadMain t1 = new ThreadMain(1);
			t1.start();
		}catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}

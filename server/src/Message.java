import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	String Type;
	String Name;
	String Time;
	String Data;
	String hashStr = "Automagic";
	public String toString() {
		String res = "";
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		res += Type + hashStr.hashCode();
		res += Name + hashStr.hashCode();
		res += df.format(new Date()).toString() + hashStr.hashCode();
		res += Data;
		return res;
	}
}

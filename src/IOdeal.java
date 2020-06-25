
public class IOdeal {
	String hashStr = "Automagic";
	public String DecodeToUI(String msg) {
		String res = "";
		String code = String.valueOf(hashStr.hashCode());
		String[] lis = msg.split(code);
		res += lis[1] + "\t";
		res += lis[2] + "\n";
		res += lis[3] + "\n";
		//System.out.println(res);
		return res;
	}
	public Message DecodeToMessage(String msg) {
		Message res = new Message();
		String code = String.valueOf(hashStr.hashCode());
		String[] lis = msg.split(code);
		res.Type = lis[0];
		res.Name = lis[1];
		res.Time = lis[2];
		res.Data = lis[3];
		return res;
	}
}

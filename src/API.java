
import javax.swing.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

public class API {
	String errorMessage = "";
	Boolean isError = false;
	String hashStr = "Automagic";
	//注册账号
	public Boolean Sign(String userName,String userPassword) {
		//清空报错信息
		CleanErrorMessage();
		//先检查用户名长度：
		if(userName.length() < 4 || userName.length() > 15 ) {
			if( userName.length() < 4 )
				errorMessage = "用户名过短！请输入至少4个字符！";
			else
				errorMessage = "用户名过长！请输入15个字符以下！";
			isError = true;
			return false;
		}
		//再检查密码长短：
		if(userPassword.length() < 6 || userPassword.length() > 20 ) {
			if( userPassword.length() < 6 )
				errorMessage = "密码过短！请输入至少6个字符！";
			else
				errorMessage = "密码过长！请输入20个字符以下！";
			isError = true;
			return false;
		}
		return true;
	}
	//登陆
	public Boolean Login(String userName,String userPassword) {
		CleanErrorMessage();
		Database myDatabase = new Database();
		if(myDatabase.IsTrue(userName, userPassword)){
			return true;
		}
		else {
			errorMessage = "用户密码不匹配！";
			isError = true;
			return false;
		}
	}
	//UI->Client
	public String SendToServer(String userName,String msg) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		String send = "Group" + hashStr.hashCode();
		send += userName + hashStr.hashCode();
		send += df.format(new Date()).toString() + hashStr.hashCode();
		send += msg;
		System.out.println(send);
		return send;
	}
	//Client->UI
	public String SendToUI(String msg) {
		//分隔符使用HashCode
		String res = "";
		String code = String.valueOf(hashStr.hashCode());
		String[] lis = msg.split(code);
		res += lis[1] + "\t";
		res += lis[2] + "\n";
		res += lis[3] + "\n";
		System.out.println(res);
		return res;
	}
	//UI->UI
	public String SendToSelf(String userName,String msg) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		String res = "";
		res += userName + "\t";
		res += df.format(new Date()).toString() + "\n";
		res += msg + "\n";
		return res;
	}
	//清空报错信息
	private void CleanErrorMessage() {
		errorMessage = "";
		isError = true;
	}
}


import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
public class Client {
	//启动服务器
	int port;
	String serverName;
	Socket client = null;
	Boolean login = false;
	int userCount = 0;
	String userList = null;
	String userName = "";
	Boolean isError = false;
	//确保收到了服务器的返回
	Boolean isReceived = false;
	//采用StringBuffer读取，保证线程安全
	StringBuffer readBuffer = new StringBuffer();
	StringBuffer UIBuffer = new StringBuffer();
	Client(String servername, int PORT) {
		serverName = servername;
		port = PORT;
	}
	Code myCode = new Code();
	class Read implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			DataInputStream in = null;
			while( !login ) {
				try {
				in = new DataInputStream(client.getInputStream());
				//处理IO
				IOdeal myIOdeal = new IOdeal();
				//将输入信息转换为对象，方便查询
				byte[] b = new byte[1000];
				int length = in.read(b);
	            Message option = myIOdeal.DecodeToMessage(Code.decode(b,length));
	            if( option.Type.equals("Login")) {
	            	if( option.Data.equals("登陆成功") ) {
	            		login = true;
	            		userName = option.Name;
	            	}
	            	else if( option.Data.equals("用户密码不匹配")){
	            		isError = true;
	            		UIBuffer.append("用户密码不匹配！");
	            	}
            		isReceived = true;
            		System.out.println("Client处理了消息！");
	            }
	            else if( option.Type.equals("Sign") ) {
	            	if( option.Data.equals("用户已存在") ) {
	            		isError = true;
	            		UIBuffer.append("该用户已存在！");
	            		System.out.println("消息！");
	            	}
	            	else if( option.Data.equals("创建成功") ) {
	            		isError = true;
	            		UIBuffer.append("注册成功！请登陆！");
	            		System.out.println("处理了消息！");
	            	}
            		isReceived = true;
            		System.out.println("Client处理了消息！");
	            }
				} catch( IOException e) {
					e.printStackTrace();
				}
			}
			while( login ) {
				try {
					//读取服务器的输入
					in = new DataInputStream(client.getInputStream());
					//处理IO
					IOdeal myIOdeal = new IOdeal();
					//将输入信息转换为对象，方便查询
					byte[]b = new byte[1000];
					int length = in.read(b);
		            Message option = myIOdeal.DecodeToMessage(myCode.decode(b,length));
					switch( option.Type ) {
					case "Count":
						userCount = Integer.parseInt(option.Name);
						userList = option.Data;
						break;
					case "Group":
						readBuffer.append(myIOdeal.DecodeToUI(option.toString()));
						System.out.println(myIOdeal.DecodeToUI(option.toString()));
						break;
					case "Login":
						readBuffer.append(option.Name + "加入了聊天室！\n");
						break;
					case "Logout":
						readBuffer.append(option.Name + "离开了聊天室！\n");
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void Login(String name, String pwd) {
		if( name.length() == 0 || pwd.length() == 0 ) {
			UIBuffer.append("用户和密码不能为空！");
			isError = true;
			isReceived = true;
			return;
		}
		Message myMsg = new Message();
		myMsg.Name = name;
		myMsg.Data = pwd;
		myMsg.Type = "Login";
		try {
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.write(Code.encode(myMsg.toString()));
			System.out.println("发送！"+ out.toString());
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
	public void Sign(String name, String pwd) {
		Message myMsg = new Message();
		myMsg.Name = name;
		myMsg.Data = pwd;
		myMsg.Type = "Sign";
		try {
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.write(Code.encode(myMsg.toString()));
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
	//连接服务器
	public void Connect() {
		try{
			System.out.println("连接到主机：" + serverName + " ，端口号：" + port);
			client = new Socket(serverName, port);
			new Thread(new Read()).start();
		}catch(IOException e){
				e.printStackTrace();
		}
	}
	
	//退出聊天室
	public void DisConnect() {
		try {
			Message myMsg = new Message();
			myMsg.Name = userName;
			myMsg.Data = "Logout";
			myMsg.Type = "Logout";
			try {
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				out.write(Code.encode(myMsg.toString()));
			} catch( IOException e ) {
				e.printStackTrace();
			}
			login = false;
			client.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	//发送消息
	public void sendMessage(String name, String msg) {
		Message myMsg = new Message();
		myMsg.Name = name;
		myMsg.Data = msg;
		myMsg.Type = "Group";
		try {
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.write(Code.encode(myMsg.toString()));
			System.out.println("发送！"+ myMsg.toString());
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
	public void CleanRead() {
		readBuffer = new StringBuffer();
	}
	public void CleanUI() {
		UIBuffer = new StringBuffer();
		isError = false;
		isReceived = false;
	}
}

import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.io.*;
public class Server {
	int PORT = 6236; 
	String errorMessage = "";
	Boolean isError = null;
	Map<String,Socket> clientSockets = new HashMap<String,Socket>();
	//启动服务器

	Thread server = new Thread(new Runnable() {
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			serverSocket.setSoTimeout(10000);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		while(true) {
			try {
				//服务器运行中
				System.out.println("服务器等待"+serverSocket.getLocalPort()+"...");
				//等待Socket
				Socket server = serverSocket.accept();
				//将新连接的Socket加入
				Service myClient = new Service(server);
				//为该客户端开新的线程
				new Thread(myClient).start();
				System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
			}catch(SocketTimeoutException e) {
				errorMessage += "连接超时！";
				isError = true;
				break;
			}catch(IOException e) {
				errorMessage += "无效连接！";
				isError = true;
				break;
			}
		}
	}
	});
	
	class Service implements Runnable {
	        Socket socket = null;
	        String name = "";
	        public Service(Socket socket) {
	            // TODO Auto-generated constructor stub
	            this.socket = socket;
	            //未登陆时，一直重复直到获得正确的名字为止
	        }
	        public Service() {
	            // TODO Auto-generated constructor stub
	        }
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Login();
	            Boolean login = true;
	            System.out.println("here");
				while(login) {
					DataInputStream in;
					byte[] b = new byte[1000];
					try {
						//读入信息
						in = new DataInputStream(socket.getInputStream());
						//处理IO
						IOdeal myIOdeal = new IOdeal();
						//将输入信息转换为对象，方便查询
						int length = in.read(b);
						System.out.println("输入：" + b);
			            Message option = myIOdeal.DecodeToMessage(Code.decode(b,length));
			            switch( option.Type ) {
				            case "Group":
				            	//广播信息
				            	System.out.println("收到广播");
				            	BoardCast(option);
				            	break;
				            case "Logout":
				            	Logout(option);
				            	//关闭连接及输入
				            	in.close();
				            	socket.close();
				            	//退出循环
				            	login = false;
				            	break;
			            }
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			//登陆
			public void Login() {
            while ( name == "" ) {
            	try {
            		DataInputStream in = new DataInputStream(socket.getInputStream());
            		byte[] b = new byte[1000];
            		int length = in.read(b);
	                String msg = Code.decode(b,length);
	                IOdeal myIO = new IOdeal();
	                Message option = myIO.DecodeToMessage(msg);
	                if( option.Type.equals("Login") ) {
	                	System.out.println("收到！"+option.Name);
	                	Database myDatabase = new Database();
	                	//登陆成功：
	                	if(myDatabase.IsTrue(option.Name, option.Data)){
	                		name = option.Name;
	                		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	                		option.Data = "登陆成功";
	                		out.write(Code.encode(option.toString()));
	    	                //服务器记录当前的用户名及socket
	    	                clientSockets.put(name, socket);
	    	                //在线名单更新
	    	                BoardCast(GetCount(option));
	                	}
	                	//登陆失败：
	                	else {
	                		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	                		option.Data = "用户密码不匹配";
	                		out.write(Code.encode(option.toString()));
	                	}
	                }
	                if( option.Type.equals("Sign")) {
	                	Database myDatabase = new Database();
	                	//用户被占用：
	                	if(myDatabase.IsExist(option.Name)) {
	                		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	                		option.Data = "用户已存在";
	                		out.write(Code.encode(option.toString()));
	                	}
	                	//创建成功：
	                	else if(myDatabase.CreateUser(option.Name, option.Data)) {
	                		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	                		option.Data = "创建成功";
	                		out.write(Code.encode(option.toString()));
	                	}
	                }
	            } catch (IOException e) {
	                // TODO: handle exception
	                e.printStackTrace();
	            }
            	}
			}	
			//登出
			public void Logout(Message option) {
				//删除该用户的socket信息
				clientSockets.remove(name, socket);
				//广播用户退出的消息
				BoardCast(option);
            	//在线名单更新
            	BoardCast(GetCount(option));
			}
			//广播消息
			public void BoardCast(Message option) {
				Iterator it = clientSockets.keySet().iterator();
				while( it.hasNext() ) {
					String key = it.next().toString();
					Socket sendSocket = clientSockets.get(key);
					try {
						System.out.println("发送！"+option.toString());
						DataOutputStream out = new DataOutputStream(sendSocket.getOutputStream());
						out.write(Code.encode(option.toString()));
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
			//获取在线人数以及姓名列表
			public Message GetCount(Message option) {
				Message res = new Message();
				String resName = "";
				Iterator it = clientSockets.keySet().iterator();
				while( it.hasNext() ) {
					String key = it.next().toString();
					resName += key + '\n';
				}
				res.Type = "Count";
				res.Time = option.Time;
				res.Name = String.valueOf(clientSockets.size());
				res.Data = resName;
				return res;
			}
	}
	
}
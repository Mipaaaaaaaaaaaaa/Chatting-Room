
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class UI {
	//登陆界面布局：
    JLabel userLabel = new JLabel("用户名");
    JTextField userText = new JTextField(20);
    JLabel passwordLabel = new JLabel("密码");
    JPasswordField passwordText = new JPasswordField(20);
    JButton loginButton = new JButton("登陆");
    JButton signButton = new JButton("注册");
    //聊天界面布局
    JLabel toolBar = new JLabel("@Magicmipamipa",JLabel.CENTER);
    JLabel countUser = new JLabel("0 位魔法师在线");
    JTextArea userContent = new JTextArea(2,2);
    JTextArea chatContent = new JTextArea("～欢迎来到魔法聊天室～\n",15,20);
    JTextArea sendContent = new JTextArea("我来啦！",3,5);
    JButton sendButton = new JButton("发送");
    //API及用户名
    API myAPI = new API();
    Client myClient = new Client("127.0.0.1",6236);
    String Name;
    ImageIcon imageIcon = new ImageIcon("assert/img/1.jpg");
    //登陆窗口
    JFrame jf = new JFrame("魔法聊天室-登陆");
    //聊天窗口
    JFrame cr = new JFrame("魔法聊天室");
	public void LoginUI() {
        jf.setSize(240, 240);                      // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        jf.setIconImage(imageIcon.getImage());
        jf.setResizable(false);
        // 2. 创建中间容器（面板容器）
        JPanel panel = new JPanel(new BorderLayout());
        Box vBox = Box.createVerticalBox();              // 创建面板容器，使用默认的布局管理器
        // 启动客户端
        myClient.Connect();
        toolBar.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        // 3. 创建一个基本组件（按钮），并添加到 面板容器 中
        Box hBox1 = Box.createHorizontalBox();
        hBox1.add(Box.createHorizontalStrut(20));
        hBox1.add(userLabel);
        hBox1.add(Box.createHorizontalStrut(10));
        hBox1.add(userText);
        hBox1.add(Box.createHorizontalStrut(20));
        
        Box hBox2 = Box.createHorizontalBox();
        hBox2.add(Box.createHorizontalStrut(20));
        hBox2.add(passwordLabel);
        hBox2.add(Box.createHorizontalStrut(10));
        hBox2.add(passwordText);
        hBox2.add(Box.createHorizontalStrut(20));
        
        Box hBox3 = Box.createHorizontalBox();
        hBox3.add(loginButton);
        hBox3.add(Box.createHorizontalStrut(20));
        hBox3.add(signButton);
        //纵向容器中压入三个横向容器
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(hBox1);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(hBox2);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(hBox3);
        vBox.add(Box.createVerticalStrut(20));
        
        panel.add(vBox,BorderLayout.NORTH);
        panel.add(toolBar,BorderLayout.SOUTH);
        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(panel);

        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
        ListenerLogin();
	}
	public void ChatRoomUI() {
		cr.setSize(450, 450);                      // 设置窗口大小
        cr.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        cr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        cr.setResizable(false);
        cr.setIconImage(imageIcon.getImage());
        //文本的初始位置
        userContent.setEditable(false);
        chatContent.setEditable(false);
        chatContent.setLineWrap(true);
        sendContent.setCaretPosition(0);//光标显示位置
        sendContent.setLineWrap(true);
        toolBar.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        //更新在线人数和在线列表
        userContent.setText(myClient.userList);
        //Box布局1：在线人数和在线列表
        Box vBox1 = Box.createVerticalBox();
        vBox1.add(Box.createVerticalStrut(20));
        vBox1.add(countUser);
        vBox1.add(Box.createVerticalStrut(20));
        vBox1.add(new JScrollPane(
        		userContent,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
        		);
        //Box布局2：接收、发送窗口和发送按钮
        Box vBox2 = Box.createVerticalBox();
        vBox2.add(Box.createVerticalStrut(20));
        vBox2.add(new JScrollPane(
        		chatContent,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
        		);
        vBox2.add(Box.createVerticalStrut(20));
        vBox2.add(new JScrollPane(
        		sendContent,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
        		);
        vBox2.add(Box.createVerticalStrut(10));
        vBox2.add(sendButton);
        //Box布局3：横向放
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox2);
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox1);
        hBox.add(Box.createHorizontalStrut(20));
        //Border布局：左边放、右边放、下边放
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.add(hBox,BorderLayout.NORTH);
        panelMain.add(toolBar,BorderLayout.SOUTH);
        cr.setContentPane(panelMain);
        cr.setVisible(true);
        new Thread(new Fresh()).start();
        ListenerChat();
	}
	public void ListenerLogin() {
		//登陆按钮
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                String uname = userText.getText();
                String pwd = String.valueOf(passwordText.getPassword());
                System.out.println(uname+pwd);
                myClient.CleanUI();
                myClient.Login(uname, pwd);
                //这里应该用线程锁感觉..
                while( myClient.isReceived == false) {
                	try {
						Thread.sleep(100);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					};
                }
                if( myClient.isError ) {
                	JOptionPane.showMessageDialog(new JFrame(),myClient.UIBuffer);
                }
                else {
                    System.out.println("进来");
                	Name = uname;
                	JOptionPane.showMessageDialog(new JFrame(),"你好，亲爱的"+Name+"，欢迎来到魔法聊天室！");
                	//丢弃登陆界面
                	jf.dispose();
                	//进入聊天室界面
                    countUser.setText(myClient.userCount + " 位魔法师在线");
                    ChatRoomUI();
                }
			}
		});
		//注册按钮
		signButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                String uname = userText.getText();
                String pwd = String.valueOf(passwordText.getPassword());
                //先进行基本检测
                if( !myAPI.Sign(uname, pwd) &&  myAPI.isError ){
                	JOptionPane.showMessageDialog(new JFrame(),myAPI.errorMessage);
                }
                else {
                	System.out.println("在搞");
                	myClient.CleanUI();
                	myClient.Sign(uname, pwd);
                	//这里应该用线程锁感觉..
                	while( myClient.isReceived == false) {
                		try {
						Thread.sleep(100);
                		} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
                		};
                	}	
                	System.out.println(myClient.isReceived);
                	System.out.println(myClient.UIBuffer);
                	JOptionPane.showMessageDialog(new JFrame(),myClient.UIBuffer);
                }
			}
		});
	}
	public void ListenerChat() {
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if( sendContent.getText().length() != 0 ) {
					String sendText = sendContent.getText();
					String uname = Name;
					myClient.sendMessage(uname, sendText);
					sendContent.setText("");
			        sendContent.setCaretPosition(0);//光标显示位置
			        sendContent.setCaret(new DefaultCaret() {
			    	    public boolean isVisible() {
			    			return true;
			    		    }
			    		});
			        sendContent.requestFocus();
				}
			}
		});
		//此处需要线程安全
	}
	//刷新线程，保证服务器发送的信息都能被用户所看到
	class Fresh implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("在这在这");
			while(cr.isShowing()) {
				String chat = myClient.readBuffer.toString();
				if( chat != "" ) {
					chatContent.append(chat);
					myClient.readBuffer.delete(0, chat.length());
					userContent.setText(myClient.userList);
				}
		        countUser.setText(myClient.userCount + " 位魔法师在线");
			}
		}
	}
}

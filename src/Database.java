import java.*;
import java.sql.*;
public class Database {
	//创建账户时检测该名称是否可用
	Connection ct = null;
	Statement st = null;
	ResultSet rs = null;
	String errorMessage = "";
	Boolean isError = null;
	String url = "mysqlurl";
	String name = "yourname";
	String pwd = "yourpwd";
	public void JdbcConnection() {
		try {
			//加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			//得到连接
			ct = DriverManager.getConnection(url,name,pwd);
		}
		catch(Exception e) {
			System.out.println("远程连接数据库失败！");
			errorMessage += "远程连接数据库失败！";
			isError = true;
		}
	}
	//检查该用户是否存在
	public Boolean IsExist( String userName ) {
		//更新错误信息
		CleanErrorMessage();
		//连接数据库
		JdbcConnection();
		Boolean flag = null;
		try {
			st = ct.createStatement();
			String sql = "select * from user where username = '" + userName + "'";
			rs = st.executeQuery(sql);
			if( rs.next() ) {
				flag = true;
			}
			else
				flag = false;
		}catch(SQLException e) {
			System.out.println("查询账户是否存在时失败！");
			errorMessage += "查询账户是否存在时失败！";
			isError = true;
		}
		CloseConnection();
		if( flag )
			return true;
		else 
			return false;
	}
	//检查账户密码是否正确
	public Boolean IsTrue( String userName, String userPassword) {
		//更新错误信息
		CleanErrorMessage();
		//连接数据库
		JdbcConnection();
		Boolean flag = false;
		try {
			st = ct.createStatement();
			String sql = "select * from user where userName = '" + userName + "'";
			rs = st.executeQuery(sql);
			if( rs == null ) {
				errorMessage += "该账号不存在！";
				isError = true;
				flag = false;
			}
			else {
			while( rs.next() ) {
				System.out.println(rs.getString("password"));
				if( userPassword.equals(rs.getString("password"))) {
					flag = true;
				}
			else
				flag = false;
			}
			}
		}catch(SQLException e) {
			System.out.println("验证密码时失败！");
			errorMessage += "验证密码时失败！";
			isError = true;
		}
		CloseConnection();
		if(flag) {
			return true;
		}
		else
			return false;
	}
	//添加用户
	public Boolean CreateUser( String userName, String userPassword) {
		//更新错误信息
		CleanErrorMessage();
		//连接数据库
		JdbcConnection();
		int state = 0;
		try {
			st = ct.createStatement();
			String sql = "insert into user(username,password) values('"+userName+"','"+userPassword+"')";
			System.out.println(sql);
			state = st.executeUpdate(sql);
			System.out.println(state);
			
		}catch(SQLException e) {
			//更新错误信息
			System.out.println("创建账户失败！");
			errorMessage += "创建账户失败！";
			isError = true;
		}
		//关闭连接
		CloseConnection();
		//返回参数
		if( state == 1 ) {
			return true;
		}
		else
			return false;
	}
	//关闭SQL的连接
	public void CloseConnection() {
		try {
			if(rs != null) {
				rs.close();
			}
			if(st != null) {
				st.close();
			}
			if(ct != null) {
				ct.close();
			}
		}catch (Exception e) {
			System.out.println("关闭数据库连接时失败！");
			errorMessage += "关闭数据库连接时失败！";
			isError = true;
		}
	}
	//清空报错信息
	private void CleanErrorMessage() {
		errorMessage = "";
		isError = null;
	}
}

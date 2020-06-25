import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		UI myUI1 = new UI();
		UI myUI2 = new UI();
		Database myDatabase = new Database();
		API myAPI = new API();
		Server myServer = new Server();
		myServer.server.start();
		myUI1.LoginUI();
		myUI2.LoginUI();
	}
}

package cp.Class;

public class UserLogin {
	public String userName;
	public String passwordUser;
	
	public UserLogin(String userName, String passwordUser) {
		this.userName = userName;
		this.passwordUser = passwordUser;
	}

	public UserLogin() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswordUser() {
		return passwordUser;
	}

	public void setPasswordUser(String passwordUser) {
		this.passwordUser = passwordUser;
	}

}

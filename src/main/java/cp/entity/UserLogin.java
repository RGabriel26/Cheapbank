package cp.entity;

public class UserLogin {
	public Long userID;
	public String passwordUser;
	
	public UserLogin(Long userID, String passwordUser) {
		this.userID = userID;
		this.passwordUser = passwordUser;
	}

	public UserLogin() {
	}


	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getPasswordUser() {
		return passwordUser;
	}

	public void setPasswordUser(String passwordUser) {
		this.passwordUser = passwordUser;
	}

}

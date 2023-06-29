package cp.bank;


public class Account {
	public int idCnp;
	public String lastName;
	public String firsName;
	public String nationality;
	public int phoneNumber;
	public String email;
	
	public Account(int idCnp, String email, String lastName, String firsName, String nationality, int phoneNumber) {
		this.idCnp = idCnp;
		this.lastName = lastName;
		this.firsName = firsName;
		this.nationality = nationality;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		int age = 0;
		return age;
	}

	public int getIdCnp() {
		return idCnp;
	}

	public void setIdCnp(int idCnp) {
		this.idCnp = idCnp;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirsName() {
		return firsName;
	}

	public void setFirsName(String firsName) {
		this.firsName = firsName;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	

}

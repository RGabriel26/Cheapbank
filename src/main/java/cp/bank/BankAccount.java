package cp.bank;

public class BankAccount {
	double sold;
	long iD;
	String lastName;
	String firstName;
	
	public BankAccount(double sold, long iD, String lastName, String firstName) {
		this.sold = sold;
		this.iD = iD;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public double getSold() {
		return sold;
	}

	public void setSold(long sold) {
		this.sold = sold;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	

	
}

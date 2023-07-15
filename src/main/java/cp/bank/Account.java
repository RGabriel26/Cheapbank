package cp.bank;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Component
@Entity
public class Account {
	
	@Id
	public Long idCnp;
	public String lastName;
	public String firstName;
	public String phoneNumber;
	
	public String email;
	private String password;

	private Double sold = 0d;

	public Account(String firstName, String lastName, Long idCnp, String email, String phoneNumber) {
		this.idCnp = idCnp;
		this.lastName = lastName;
		this.firstName = firstName;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public Account() {
	}




	public Double getSold() {
		return sold;
	}

	public void setSold(Double sold) {
		this.sold = sold;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		int year;
		int month;
		int day;
		String cnp = String.valueOf(this.idCnp);

		if (cnp.length() == 13) {
			year = Integer.parseInt(cnp.substring(1, 3));
			month = Integer.parseInt(cnp.substring(3, 5));
			day = Integer.parseInt(cnp.substring(5, 7));

			// Adăugați 1900 sau 2000 la anul în funcție de prima cifră a CNP-ului
			char genderDigit = cnp.charAt(0);
			if (genderDigit == '1' || genderDigit == '2') {
				year += 1900;
			} else if (genderDigit == '3' || genderDigit == '4') {
				year += 1800;
			} else if (genderDigit == '5' || genderDigit == '6') {
				year += 2000;
			} else if (genderDigit == '7' || genderDigit == '8') {
				year += 2000;
			}

			// Obțineți data curentă
			LocalDate currentDate = LocalDate.now();

			// Calculează diferența între data curentă și data nașterii
			LocalDate birthDate = LocalDate.of(year, month, day);
			Period period = Period.between(birthDate, currentDate);

			// Returnează numărul de ani din diferență
			return period.getYears();
		} else {
			throw new IllegalArgumentException("CNP invalid!");
		}
	}

	public Long getIdCnp() {
		return idCnp;
	}

	public void setIdCnp(Long idCnp) {
		this.idCnp = idCnp;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Account [idCnp=" + idCnp + ", lastName=" + lastName + ", firstName=" + firstName + ", phoneNumber="
				+ phoneNumber + ", email=" + email + "]";
	}

}

package cp.Class;

import java.time.LocalDate;
import java.time.Period;

public class Account {
	public long idCnp;
	public String lastName;
	public String firsName;
	public String nationality;
	public int phoneNumber;
	public String email;
	
	public Account(long idCnp, String email, String lastName, String firsName, String nationality, int phoneNumber) {
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

	public long getIdCnp() {
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

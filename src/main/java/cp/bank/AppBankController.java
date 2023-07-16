package cp.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AppBankController {

	private Long persoanaLogata;

	@Autowired
	public AccountRepository accountRepo;

	//creez mai intai instantele pentru clienti si conturile din banca si dupa le adaug in map uri
	@GetMapping("")
	public String home() {
		return "loginPages/home";
	}

	@GetMapping("/home")
	public String home1() {
		return "loginPages/home";
	}

	@GetMapping("/loginPage")
	public String loginPage(Model model) {
		UserLogin user = new UserLogin();
		model.addAttribute("userLogin", user);
		return "loginPages/loginPage";
	}

	@PostMapping("/inputUser")
	public String loginUser(@RequestParam(value="userID", defaultValue="0") Long userID, @RequestParam("passwordUser") String passwordInput, Model model) {
		model.addAttribute("userLogin", new UserLogin());
		//restrictii in cazul inputurilor goale
		if(userID == 0) {
			System.out.println("Inputul de ID este gol");
			model.addAttribute("loginPageMessage", "Incorrect ID.");
			return loginPage(model);
		}else if(passwordInput == null) {
			System.out.println("Input de password este gol");
			model.addAttribute("loginPageMessage", "Incorrect password.");
			return loginPage(model);
		}

		//verificarea inputurilor pentru logarea utilizatorului
		if(accountRepo.existsById(userID)) {
			System.out.println("Persoana este in baza de date.");
			if(passwordInput.equals(accountRepo.findById(userID).get().getPassword())) {
				System.err.println("Logare cu succes!");
				persoanaLogata = userID;
				return deshboard(model);
			}else {
				System.out.println("Parola incorecta!");
				model.addAttribute("loginPageMessage", "Incorrect password.");
				model.addAttribute("userLogin", new UserLogin());
				return loginPage(model);
			}
		}else {
			System.out.println("Persoana nu este in baza de date.");
			model.addAttribute("loginPageMessage", "Incorrect ID.");
			return loginPage(model);
		}
	}

	@GetMapping("/registerPage")
	public String registerPage(Model model) {
		Account account = new Account();
		model.addAttribute("warningMessage", " ");
		model.addAttribute("userRegistration", account);
		return "loginPages/registerPage";
	}

	@PostMapping("/registrationUser")
	public String registerUser(Model model, @ModelAttribute Account newAccount) {
		//model pentru crearea paginii de inregistrare in cazul redirectionarii
		model.addAttribute("userRegistration", new Account());
		List<Account> listAccounts = accountRepo.findAll(); 
		//restrictiile in cazul unui input gol
		if(newAccount.firstName.isEmpty()) {
			System.out.println("Invalid firstname");
			model.addAttribute("warningMessage", "Invalid firstname");
			return "loginPages/registerPage";
		}else if(newAccount.lastName.isEmpty()) {
			System.out.println("Invalid lastname");
			model.addAttribute("warningMessage", "Invalid lastname");
			return "loginPages/registerPage";
		}else if(newAccount.getPassword().isEmpty()) {
			System.out.println("Invalid password.");
			model.addAttribute("warningMessage", "Invalid password");
			return "loginPages/registerPage";
		}else if(newAccount.email.isEmpty()) {
			System.out.println("Invalid email");
			model.addAttribute("warningMessage", "Invalid email");
			return "loginPages/registerPage";
		}else if(newAccount.phoneNumber.isEmpty()) {
			System.out.println("Invalid phonenumber");
			model.addAttribute("warningMessage", "Invalid phone number");
			return "loginPages/registerPage";
		}else if(newAccount.idCnp==null) {
			newAccount.setIdCnp(1l);
			System.out.println("Invalid id");
			model.addAttribute("warningMessage", "Invalid CNP");
			return "loginPages/registerPage";
			//restrictii in cazul in care datele introduse sunt deja folosite de alt utilizator pentru crearea contului
		}else if(accountRepo.existsById(newAccount.idCnp)){
			System.out.println("Persoana exista deja in baza de date.");
			model.addAttribute("warningMessage", "CNP already used.");
			return "loginPages/registerPage";
		}else {
			for(Account acc:listAccounts) {
				if(newAccount.email.equals(acc.email)){
					System.out.println("Email deja folosit!");
					model.addAttribute("warningMessage", "Email already used.");
					return "loginPages/registerPage";
				}
				if(newAccount.phoneNumber.equals(acc.phoneNumber)) {
					System.out.println("Numarul de telefon este deja folosut.");
					model.addAttribute("warningMessage", "Phone number already used.");
					return "loginPages/registerPage";
				}
			}
			//salvarea in baza de date dupa verificari
			accountRepo.save(newAccount);
			//crearea paginii de login dupa realizarea cu succes a inregistrarii
			model.addAttribute("succesRegistrationMessage", "Successful registration!");
			model.addAttribute("userLogin", new UserLogin());
			return ("loginPages/loginPage");
		}
	}

	@GetMapping("/deshboard")
	public String deshboard(Model model) {

		if(persoanaLogata == null) {
			model.addAttribute("loginPageMessage", "Please login!");
			model.addAttribute("userLogin", new UserLogin());
			return loginPage(model);
		}
		model.addAttribute("info_name", accountRepo.findById(persoanaLogata).get().firstName + " " + accountRepo.findById(persoanaLogata).get().lastName);
		model.addAttribute("info_sold", accountRepo.findById(persoanaLogata).get().getSold());
		model.addAttribute("transfer", new TransferToAccout());
		System.out.println(persoanaLogata);
		return "loginPages/deshboard.html";
	}

	@GetMapping("/transfer")
	public String desgTransfer(Model model) {
		model.addAttribute("transfer", new TransferToAccout());
		model.addAttribute("info_sold", accountRepo.findById(persoanaLogata).get().getSold());
		model.addAttribute("info_name", accountRepo.findById(persoanaLogata).get().firstName + " " + accountRepo.findById(persoanaLogata).get().lastName);
		return "loginPages/transfer-deshboard.html";
	}

	@GetMapping("/profil")
	public String desgProfil(Model model) {



		return "loginPages/profil-deshboard.html";
	}

	@GetMapping("/logOut")
	public String desgLogout(Model model) {
		persoanaLogata = null;

		model.addAttribute("userLogin", new UserLogin());
		return "loginPages/loginPage";
	}

	@PostMapping("/transfer_to")
	public String transferTo(Model model, @ModelAttribute TransferToAccout newTransferToAccout) {
		Long transferto_ID;
		Double amountTransfer;
		model.addAttribute("transfer", new TransferToAccout());
		//validare campuri de input
		if(newTransferToAccout.transfer_toID==null || !accountRepo.existsById(newTransferToAccout.transfer_toID)) {
			System.out.println("ID invalid");
			model.addAttribute("warning_transfer", "Invalid ID");
			return deshboard(model);
		}
		if(newTransferToAccout.amound_toID == null) {
			System.out.println("Amount invalid");
			model.addAttribute("warning_transfer", "Amount invalid");
			return deshboard(model);
		}

		transferto_ID = newTransferToAccout.transfer_toID;
		amountTransfer = newTransferToAccout.amound_toID;

		if(accountRepo.findById(persoanaLogata).get().getSold() < amountTransfer) {
			System.out.println("Suma de transferat este mai mare decat soldul contului.");
			model.addAttribute("warning_transfer", "Not enough money");
			return deshboard(model);
		}
		//realizare transfer

		//adaugare suma in contul beneficiar
		Account newAccount_beneficiar = new Account();
		newAccount_beneficiar.setEmail(accountRepo.findById(transferto_ID).get().email);
		newAccount_beneficiar.setFirstName(accountRepo.findById(transferto_ID).get().firstName);
		newAccount_beneficiar.setIdCnp(transferto_ID);
		newAccount_beneficiar.setLastName(accountRepo.findById(transferto_ID).get().lastName);
		newAccount_beneficiar.setPassword(accountRepo.findById(transferto_ID).get().getPassword());
		newAccount_beneficiar.setPhoneNumber(accountRepo.findById(transferto_ID).get().phoneNumber);
		newAccount_beneficiar.setSold((accountRepo.findById(transferto_ID).get().getSold()) + amountTransfer);
		accountRepo.save(newAccount_beneficiar);

		//scadere suma din contul expeditor
		Account newAccount_expeditor = new Account();
		newAccount_expeditor.setEmail(accountRepo.findById(persoanaLogata).get().email);
		newAccount_expeditor.setFirstName(accountRepo.findById(persoanaLogata).get().firstName);
		newAccount_expeditor.setIdCnp(persoanaLogata);
		newAccount_expeditor.setLastName(accountRepo.findById(persoanaLogata).get().lastName);
		newAccount_expeditor.setPassword(accountRepo.findById(persoanaLogata).get().getPassword());
		newAccount_expeditor.setPhoneNumber(accountRepo.findById(persoanaLogata).get().phoneNumber);
		newAccount_expeditor.setSold((accountRepo.findById(persoanaLogata).get().getSold()) - amountTransfer);
		accountRepo.save(newAccount_expeditor);


		System.out.println("Transfer reusit.");
		model.addAttribute("warning_transfer", "Successful transfer");
		return deshboard(model);
	}



}


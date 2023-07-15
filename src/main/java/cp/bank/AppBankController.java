package cp.bank;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
public class AppBankController {

	@Autowired
	AccountRepository accountRepo;

	Map<Integer, Account> clientData = new HashMap<>();
	Map<Integer, BankAccount> banckData = new HashMap<>();

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
	public String loginUser(@RequestParam("userID") Long userID, @RequestParam("passwordUser") String passwordInput, Model model) {
		model.addAttribute("userLogin", new UserLogin());
		if(accountRepo.existsById(userID)) {
			System.out.println("Persoana este in baza de date.");
			if(passwordInput.equals(accountRepo.findById(userID).get().getPassword())) {
				System.err.println("Logare cu succes!");
				model.addAttribute("info", "Successful login! " + accountRepo.findById(userID).get().lastName);
				return "loginPages/deshboard.html";
			}else {
				System.out.println("Parola incorecta!");
				model.addAttribute("succesRegistrationMessage", "Incorrect password.");
				return "loginPages/loginPage";
			}
		}else {
			System.out.println("Persoana nu este in baza de date.");
			model.addAttribute("succesRegistrationMessage", "Incorrect ID.");
			return "loginPages/loginPage";
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
		model.addAttribute("userRegistration", new Account());
		List<Account> listAccounts = accountRepo.findAll(); 
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
			accountRepo.save(newAccount);
			model.addAttribute("succesRegistrationMessage", "Successful registration!");
			model.addAttribute("userLogin", new UserLogin());
			return ("loginPages/loginPage");
		}
	}


}

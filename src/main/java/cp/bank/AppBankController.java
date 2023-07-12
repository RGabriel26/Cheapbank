package cp.bank;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cp.Class.Account;
import cp.Class.BankAccount;
import cp.Class.UserLogin;

import java.util.HashMap;

@Controller
public class AppBankController {

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
	public String loginUser(@RequestParam("userName") String userInput, @RequestParam("passwordUser") String passwordInput, Model model) {
		UserLogin dataUser = new UserLogin(userInput, passwordInput);
		model.addAttribute("userLogin", dataUser);
		System.out.println(dataUser.userName);
		System.out.println(dataUser.passwordUser);
		return "loginPages/home";

	}

	@GetMapping("/registerPage")
	public String registerPage(Model model) {
		Account account = new Account();
		model.addAttribute("userRegistration", account);
		return "loginPages/registerPage";
	}

	@PostMapping("/registrationUser")
	public String registerUser(Model model, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("idCnp") long idCnp, @RequestParam("email") String email,@RequestParam("phoneNumber") String phoneNumber) {
		Account account = new Account(firstName, lastName, idCnp, email, phoneNumber);
	
		//model for loginPage
		model.addAttribute("userLogin", new UserLogin());
		System.out.println(account.toString());
		return ("loginPages/loginPage");
	}


}

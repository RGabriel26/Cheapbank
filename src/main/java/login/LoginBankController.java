package login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import cp.Class.Account;
import cp.Class.BankAccount;


@Controller
public class LoginBankController {
	
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
	public String loginPage() {
		return "loginPages/loginPage";
	}
	
	@GetMapping("/registerPage")
	public String registerPage() {
		return "loginPages/registerPage";
	}
	
	
}

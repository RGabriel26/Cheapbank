package cp.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cp.entity.TransferToAccout;
import cp.entity.UserLogin;
import services.AccountService;


@Controller
public class AtmController {

	private Long loggedID;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/atm")
	public String atm(Model model) {
		if(loggedID == null) {
			model.addAttribute("loginPageMessage", "Please login!");
			model.addAttribute("userLogin", new UserLogin());
			return "atm/loginAtm.html";
		}
		model.addAttribute("transfer", new TransferToAccout());
		model.addAttribute("info_sold", accountService.getAccount(loggedID).get().getSold());
		model.addAttribute("info_name", accountService.getAccount(loggedID).get().firstName + " " + accountService.getAccount(loggedID).get().lastName);
		return "atm/atm.html";
	}

	@GetMapping("/loginAtm")
	public String atmLogin(Model model) {
		model.addAttribute("userLogin", new UserLogin());
		return "atm/loginAtm.html";

	}
	@PostMapping("/inputAtm")
	public String loginAtm(@RequestParam(value="userID", defaultValue="0") Long userID, @RequestParam("passwordUser") String passwordInput, Model model) {
		model.addAttribute("userLogin", new UserLogin());
		//restrictii in cazul inputurilor goale
		if(userID == 0) {
			System.out.println("Inputul de ID este gol");
			model.addAttribute("loginPageMessage", "Incorrect ID.");
			return atmLogin(model);
		}else if(passwordInput == null) {
			System.out.println("Input de password este gol");
			model.addAttribute("loginPageMessage", "Incorrect password.");
			return atmLogin(model);
		}

		//verificarea inputurilor pentru logarea utilizatorului
		if(accountService.existID(userID)) {
			System.out.println("Persoana este in baza de date.");
			if(passwordInput.equals(accountService.getAccount(userID).get().getPassword())) {
				System.err.println("Logare cu succes!");
				loggedID = userID;
				return atm(model);
			}else {
				System.out.println("Parola incorecta!");
				model.addAttribute("loginPageMessage", "Incorrect password.");
				model.addAttribute("userLogin", new UserLogin());
				return atmLogin(model);
			}
		}else {
			System.out.println("Persoana nu este in baza de date.");
			model.addAttribute("loginPageMessage", "Incorrect ID.");
			return atmLogin(model);
		}
	}
	
	@GetMapping("/logOutATM")
	public String atmLogOut(Model model) {
		loggedID = null;
		model.addAttribute("userLogin", new UserLogin());
		return "atm/loginAtm.html";
	}

}

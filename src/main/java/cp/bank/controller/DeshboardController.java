package cp.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cp.entity.TransferToAccout;
import cp.entity.UserLogin;
import services.AccountService;
import services.HistoryAccountsService;
import services.HistoryTransfersService;


@Controller
public class DeshboardController {
	
	//is null
	private Long loggedID;
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private HistoryAccountsService historyAccService;
	@Autowired
	private HistoryTransfersService historyTranService;

	@GetMapping("/deshboard")
	public String deshboard(Model model) {
		Long loggedID = application.CheapBankApplication.loggedID;
		
		System.out.println("deshboard id: " + loggedID);
		
		if(loggedID == null) {
			model.addAttribute("loginPageMessage", "Please login!");
			model.addAttribute("userLogin", new UserLogin());
			return "loginPages/loginPage";
		}
		
		model.addAttribute("info_name", accountService.getAccount(loggedID).get().firstName + " " + accountService.getAccount(loggedID).get().lastName);
		model.addAttribute("info_sold", accountService.getAccount(loggedID).get().getSold());
		model.addAttribute("transfer", new TransferToAccout());
		if(historyTranService.existID(loggedID))
			model.addAttribute("listTransaction", historyTranService.getHistoryTransfers(loggedID).get().getTransactions());
		if(historyAccService.existID(loggedID))
			model.addAttribute("listAccounts", historyAccService.getHistoryAccount(loggedID).get().getHistoryAccounts());
		System.out.println("Persoana logata: " + loggedID);
		return "loginPages/deshboard.html";
	}
	
	
	@GetMapping("/profil")
	public String deshProfil(Model model) {

		return "loginPages/profil-deshboard.html";
	}
	

	@GetMapping("/logOut")
	public String deshLogout(Model model) {
		loggedID = null;

		model.addAttribute("userLogin", new UserLogin());
		return "loginPages/loginPage";
	}

}

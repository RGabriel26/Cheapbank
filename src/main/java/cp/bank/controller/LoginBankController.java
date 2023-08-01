package cp.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cp.entity.Account;
import cp.entity.HistoryAccounts;
import cp.entity.HistoryTransfers;
import cp.entity.TransferToAccout;
import cp.entity.UserLogin;
import cp.services.AccountService;
import cp.services.HistoryAccountsService;
import cp.services.HistoryTransfersService;

import java.util.LinkedList;
import java.util.List;

@Controller
public class LoginBankController {

	private Long loggedID = cp.application.CheapBankApplication.loggedID;

	@Autowired
	private AccountService accountService;
	@Autowired
	private HistoryAccountsService historyAccService;
	@Autowired
	private HistoryTransfersService historyTranService;

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
		if(accountService.existID(userID)) {
			System.out.println("Persoana este in baza de date.");
			if(passwordInput.equals(accountService.getUserPassword(userID))) {
				System.err.println("Logare cu succes!");
				this.loggedID = userID;
				cp.application.CheapBankApplication.loggedID = userID;
				System.out.println("valoarea variabilei loggedid din application packege: " + cp.application.CheapBankApplication.loggedID);
				//error
				//				return "loginPages/deshboard.html";
				return deshboard(model);
				//				return new cp.bank.controller.DeshboardController().deshboard(model);
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
		List<Account> listAccounts = accountService.getAllUsers(); 
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
		}else if(accountService.existID(newAccount.idCnp)){
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
			//ssave new user to repository 
			accountService.saveNewAccount(newAccount);
			//create login page after successful registration
			model.addAttribute("loginPageMessage", "Successful registration!");
			model.addAttribute("userLogin", new UserLogin());
			return ("loginPages/loginPage");
		}
	}
	/**********************DESHBOARD***************************/

	@GetMapping("/deshboard")
	public String deshboard(Model model) {
		Long loggedID = cp.application.CheapBankApplication.loggedID;
		if(loggedID == null) {
			model.addAttribute("loginPageMessage", "Please login!");
			model.addAttribute("userLogin", new UserLogin());
			return "loginPages/loginPage";
		}
		model.addAttribute("info_name", accountService.getUserFirstName(loggedID) + " " + accountService.getUserLastName(loggedID));
		model.addAttribute("info_sold", accountService.getUserSold(loggedID));
		model.addAttribute("transfer", new TransferToAccout());
		if(historyTranService.existID(loggedID))
			model.addAttribute("listTransaction", historyTranService.getHistoryTransfers(loggedID).get().getTransactions());
		if(historyAccService.existID(loggedID))
			model.addAttribute("listAccounts", historyAccService.getHistoryAccount(loggedID).get().getHistoryAccounts());
		System.out.println("Persoana logata: " + loggedID);
		return "loginPages/deshboard.html";
	}

	@GetMapping("/profil")
	public String desgProfil(Model model) {
		return "loginPages/profil-deshboard.html";
	}


	@GetMapping("/logOut")
	public String deshLogout(Model model) {
		loggedID = 0l;
		model.addAttribute("userLogin", new UserLogin());
		return "loginPages/loginPage";
	}

	/************************ATM*******************************/
	@GetMapping("/atm")
	public String atm(Model model) {
		if(loggedID == 0l) {
			model.addAttribute("loginPageMessage", "Please login!");
			model.addAttribute("userLogin", new UserLogin());
			return "atm/loginAtm.html";
		}
		model.addAttribute("transfer", new TransferToAccout());
		model.addAttribute("info_sold", accountService.getUserSold(loggedID));
		model.addAttribute("info_name", accountService.getUserFirstName(loggedID) + " " + accountService.getUserLastName(loggedID));
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
			if(passwordInput.equals(accountService.getUserPassword(userID))) {
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

	/************************TRANSFERS********************************/

	@PostMapping("/deposit")
	public String deposit(Model model, @ModelAttribute TransferToAccout newTransferToAccout) {
		if(newTransferToAccout.amound_toID == null) {
			model.addAttribute("atmPageMessage", "Invalid amount!");
			model.addAttribute("transfer", new TransferToAccout());
			model.addAttribute("info_sold", accountService.getUserSold(loggedID));
			model.addAttribute("info_name", accountService.getUserFirstName(loggedID) + " " + accountService.getUserLastName(loggedID));
			return "atm/atm.html";
		}else {
			logicTransfer(loggedID, newTransferToAccout.amound_toID, "Deposit");
			model.addAttribute("atmPageMessage", "Successfully transferred!");
			model.addAttribute("transfer", new TransferToAccout());
			model.addAttribute("info_sold", accountService.getUserSold(loggedID));
			model.addAttribute("info_name", accountService.getUserFirstName(loggedID) + " " + accountService.getUserLastName(loggedID));
			return "atm/atm.html";
		}
	}

	@PostMapping("/transfer_from_deshboard")
	private String transferTo(Model model, @ModelAttribute TransferToAccout newTransferToAccout) {
		Long transferto_ID;
		Double amountTransfer;
		//model pentru deshboard
		model.addAttribute("transfer", new TransferToAccout());		
		//validare campuri de input
		if(newTransferToAccout.transfer_toID==null) {
			System.out.println("CNP invalid");
			model.addAttribute("warning_transfer", "Invalid CNP!");
			return deshboard(model);
		}else if(newTransferToAccout.transfer_toID.equals(loggedID)) {
			System.out.println("Nu poti sa faci un tranfer catre tine!");
			model.addAttribute("warning_transfer", "Cannot make a transfer to you!");
			return deshboard(model);
		}
		if(newTransferToAccout.amound_toID==null) {
			System.out.println("Amount invalid");
			model.addAttribute("warning_transfer", "Amount invalid!");
			return deshboard(model);
		}
		if(!accountService.existID(newTransferToAccout.transfer_toID)) {
			System.out.println("CNP doesn't exist");
			model.addAttribute("warning_transfer", "CNP is not associated with a valid account!");
			return deshboard(model);
		}
		transferto_ID = newTransferToAccout.transfer_toID;
		amountTransfer = newTransferToAccout.amound_toID;

		if(accountService.getUserSold(loggedID) < amountTransfer) {
			System.out.println("Suma de transferat este mai mare decat soldul contului.");
			model.addAttribute("warning_transfer", "Not enough money!");
			return deshboard(model);
		}
		//realizare transfer
		logicTransfer(transferto_ID, amountTransfer, "Transfer");


		System.out.println("Transfer reusit.");
		model.addAttribute("warning_transfer", "Successful transfer!");
		return deshboard(model);
	}

	private void logicTransfer(Long transferto_ID, Double amountTransfer, String origin) {
		//provenienta banilor
		List<String> newTransferRepo;
		List<String> newAccountRepo;
		boolean inDataBase = false;

		if(origin.equals("Deposit")) {
			//adaugare suma in cont
			Account newAccount_expeditor = new Account();
			newAccount_expeditor.setEmail( accountService.getUserEmail(loggedID));
			newAccount_expeditor.setFirstName(accountService.getUserFirstName(loggedID));
			newAccount_expeditor.setIdCnp(loggedID);
			newAccount_expeditor.setLastName(accountService.getUserLastName(loggedID));
			newAccount_expeditor.setPassword(accountService.getUserPassword(loggedID));
			newAccount_expeditor.setPhoneNumber(accountService.getUserPhoneNumber(loggedID));
			newAccount_expeditor.setSold((accountService.getUserSold(loggedID)) + amountTransfer);
			accountService.saveNewAccount(newAccount_expeditor);

			//salvare informatie deposit in istoricul contului
			String infoDeposit = "Deposit " + amountTransfer + "$";
			if(historyTranService.existID(loggedID)) {
				newTransferRepo = new LinkedList<>(historyTranService.getHistoryTransfers(loggedID).get().getTransactions());
			}else {
				newTransferRepo = new LinkedList<>();
			}
			newTransferRepo.add(infoDeposit);
			historyTranService.saveNewTransactionsHistory(new HistoryTransfers(loggedID, newTransferRepo));;
		}else if(origin.equals("Transfer")){
			//adaugare suma in contul beneficiar
			Account newAccount_beneficiar = new Account();			
			newAccount_beneficiar.setEmail( accountService.getUserEmail(transferto_ID));
			newAccount_beneficiar.setFirstName(accountService.getUserFirstName(transferto_ID));
			newAccount_beneficiar.setIdCnp(transferto_ID);
			newAccount_beneficiar.setLastName(accountService.getUserLastName(transferto_ID));
			newAccount_beneficiar.setPassword(accountService.getUserPassword(transferto_ID));
			newAccount_beneficiar.setPhoneNumber(accountService.getUserPhoneNumber(transferto_ID));
			newAccount_beneficiar.setSold((accountService.getUserSold(transferto_ID)) + amountTransfer);
			accountService.saveNewAccount(newAccount_beneficiar);

			//scadere suma din contul expeditor
			Account newAccount_expeditor = new Account();
			newAccount_expeditor.setEmail( accountService.getUserEmail(loggedID));
			newAccount_expeditor.setFirstName(accountService.getUserFirstName(loggedID));
			newAccount_expeditor.setIdCnp(loggedID);
			newAccount_expeditor.setLastName(accountService.getUserLastName(loggedID));
			newAccount_expeditor.setPassword(accountService.getUserPassword(loggedID));
			newAccount_expeditor.setPhoneNumber(accountService.getUserPhoneNumber(loggedID));
			newAccount_expeditor.setSold((accountService.getUserSold(loggedID)) - amountTransfer);
			accountService.saveNewAccount(newAccount_expeditor);

			//salvarea informatiilor despre tranzactie in istoricul contului care a efectuat transferul
			String infoTransaction = "Transferred " + amountTransfer + "$ to: " + newAccount_beneficiar.firstName 
					+ " " + newAccount_beneficiar.lastName + "   ";
			if(historyTranService.existID(loggedID)) {
				newTransferRepo = new LinkedList<>(historyTranService.getHistoryTransfers(loggedID).get().getTransactions());
			}else {
				newTransferRepo = new LinkedList<>();
			}
			newTransferRepo.add(infoTransaction);
			historyTranService.saveNewTransactionsHistory(new HistoryTransfers(loggedID, newTransferRepo));;

			//salvare informatiilor despre tranzactie in istoricul contului caruia i se efectueaza transferul
			String infoReceiveTransaction = "Receive " +amountTransfer +"$ from: "
					+ newAccount_expeditor.firstName + " " + newAccount_expeditor.lastName;
			if(historyTranService.existID(transferto_ID)) {
				newTransferRepo = new LinkedList<>(historyTranService.getHistoryTransfers(transferto_ID).get().getTransactions());
			}else {
				newTransferRepo = new LinkedList<>();
			}
			newTransferRepo.add(infoReceiveTransaction);
			historyTranService.saveNewTransactionsHistory(new HistoryTransfers(transferto_ID, newTransferRepo));

			//salvare persoana in istoricAccountsRepository - conturile cu care s-a interactionat in transferuri
			String saveAccount_for_istoricAccountRepo = "[" + transferto_ID + "] : " + newAccount_beneficiar.firstName 
					+ " " + newAccount_beneficiar.lastName + "  " ;
			if(historyAccService.existID(loggedID)) {
				//in cazul in care persoana are deja un istoric de interactiuni cu alti utilizatori
				//verificam daca persoana nu se afla dela in list ul cu persoane 
				//transferto_ID - persoana nu care se face transferul
				for(String acc:historyAccService.getHistoryAccount(loggedID).get().getHistoryAccounts()) {
					if(acc.contains(Long.toString(transferto_ID))){
						inDataBase = true;
					}
				}
				if(inDataBase == false) {
					newAccountRepo = new LinkedList<>(historyAccService.getHistoryAccount(loggedID).get().getHistoryAccounts());
					newAccountRepo.add(saveAccount_for_istoricAccountRepo);
					historyAccService.saveNewAccountsHistory(new HistoryAccounts(loggedID, newAccountRepo));
					System.out.println("Persoana noua introdusa in InfoAccountsRepo");
				}
			}else{
				//in cazul in care persoana nu are un istoric de interactiuni intre utilizatori
				//pregatire string de adaugat in db
				newAccountRepo = new LinkedList<>();
				newAccountRepo.add(saveAccount_for_istoricAccountRepo);
				historyAccService.saveNewAccountsHistory(new HistoryAccounts(loggedID, newAccountRepo));
			}
		}
	}
}


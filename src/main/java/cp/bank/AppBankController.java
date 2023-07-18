package cp.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Service
@Controller
public class AppBankController {

	private Long persoanaLogata;

	@Autowired
	public AccountRepository accountRepo;
	@Autowired
	public IstoricTransferRepository istoricRepo;
	@Autowired
	public IstoricAccountsRepository istoricAccoutsRepo;

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
			model.addAttribute("loginPageMessage", "Successful registration!");
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
		if(istoricRepo.existsById(persoanaLogata))
			model.addAttribute("listTransaction", istoricRepo.findById(persoanaLogata).get().getTransactions());
		if(istoricAccoutsRepo.existsById(persoanaLogata))
			model.addAttribute("listAccounts", istoricAccoutsRepo.findById(persoanaLogata).get().getIstoricAccounts());
		System.out.println("Persoana logata: " + persoanaLogata);
		return "loginPages/deshboard.html";
	}

	@GetMapping("/atm")
	public String atm(Model model) {
		if(persoanaLogata == null) {
			model.addAttribute("loginPageMessage", "Please login!");
			model.addAttribute("userLogin", new UserLogin());
			return "atm/loginAtm.html";
		}
		model.addAttribute("transfer", new TransferToAccout());
		model.addAttribute("info_sold", accountRepo.findById(persoanaLogata).get().getSold());
		model.addAttribute("info_name", accountRepo.findById(persoanaLogata).get().firstName + " " + accountRepo.findById(persoanaLogata).get().lastName);
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
		if(accountRepo.existsById(userID)) {
			System.out.println("Persoana este in baza de date.");
			if(passwordInput.equals(accountRepo.findById(userID).get().getPassword())) {
				System.err.println("Logare cu succes!");
				persoanaLogata = userID;
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

	@PostMapping("/deposit")
	public String deposit(Model model, @ModelAttribute TransferToAccout newTransferToAccout) {
		logicTransfer(persoanaLogata, newTransferToAccout.amound_toID, "Deposit");
		model.addAttribute("atmPageMessage", "Successfully transferred!");
		model.addAttribute("transfer", new TransferToAccout());
		model.addAttribute("info_sold", accountRepo.findById(persoanaLogata).get().getSold());
		model.addAttribute("info_name", accountRepo.findById(persoanaLogata).get().firstName + " " + accountRepo.findById(persoanaLogata).get().lastName);
		return "atm/atm.html";
	}

	@GetMapping("/profil")
	public String desgProfil(Model model) {



		return "loginPages/profil-deshboard.html";
	}

	@GetMapping("/logOut")
	public String deshLogout(Model model) {
		persoanaLogata = null;

		model.addAttribute("userLogin", new UserLogin());
		return "loginPages/loginPage";
	}
	@GetMapping("/logOutATM")
	public String atmLogOut(Model model) {
		persoanaLogata = null;
		model.addAttribute("userLogin", new UserLogin());
		return "atm/loginAtm.html";
	}

	@PostMapping("/transfer_from_deshboard")
	private String transferTo(Model model, @ModelAttribute TransferToAccout newTransferToAccout) {
		Long transferto_ID;
		Double amountTransfer;
		//model pentru deshboard
		model.addAttribute("transfer", new TransferToAccout());		
		//validare campuri de input
		if(newTransferToAccout.transfer_toID.equals(persoanaLogata)) {
			System.out.println("Nu poti sa faci un tranfer catre tine!");
			model.addAttribute("warning_transfer", "Cannot make a transfer to you!");
			return deshboard(model);
		}else if(newTransferToAccout.transfer_toID==null) {
			System.out.println("CNP invalid");
			model.addAttribute("warning_transfer", "Invalid CNP!");
			return deshboard(model);
		}
		if(newTransferToAccout.amound_toID==null) {
			System.out.println("Amount invalid");
			model.addAttribute("warning_transfer", "Amount invalid!");
			return deshboard(model);
		}
		if(!accountRepo.existsById(newTransferToAccout.transfer_toID)) {
			System.out.println("CNP doesn't exist");
			model.addAttribute("warning_transfer", "CNP is not associated with a valid account!");
			return deshboard(model);
		}
		transferto_ID = newTransferToAccout.transfer_toID;
		amountTransfer = newTransferToAccout.amound_toID;

		if(accountRepo.findById(persoanaLogata).get().getSold() < amountTransfer) {
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
			newAccount_expeditor.setEmail(accountRepo.findById(persoanaLogata).get().email);
			newAccount_expeditor.setFirstName(accountRepo.findById(persoanaLogata).get().firstName);
			newAccount_expeditor.setIdCnp(persoanaLogata);
			newAccount_expeditor.setLastName(accountRepo.findById(persoanaLogata).get().lastName);
			newAccount_expeditor.setPassword(accountRepo.findById(persoanaLogata).get().getPassword());
			newAccount_expeditor.setPhoneNumber(accountRepo.findById(persoanaLogata).get().phoneNumber);
			newAccount_expeditor.setSold((accountRepo.findById(persoanaLogata).get().getSold()) + amountTransfer);
			accountRepo.save(newAccount_expeditor);

			//salvare informatie deposit in istoricul contului
			String infoDeposit = "Deposit " + amountTransfer + "$";
			if(istoricRepo.existsById(persoanaLogata)) {
				newTransferRepo = new LinkedList<>(istoricRepo.findById(persoanaLogata).get().getTransactions());
			}else {
				newTransferRepo = new LinkedList<>();
			}
			newTransferRepo.add(infoDeposit);
			istoricRepo.save(new IstoricTransfers(persoanaLogata, newTransferRepo));
		}else if(origin.equals("Transfer")){
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

			//salvarea informatiilor despre tranzactie in istoricul contului care a efectuat transferul
			String infoTransaction = "Transferred " + amountTransfer + "$ to: [" + transferto_ID +"] " + newAccount_beneficiar.firstName 
					+ " " + newAccount_beneficiar.lastName + "   ";
			if(istoricRepo.existsById(persoanaLogata)) {
				newTransferRepo = new LinkedList<>(istoricRepo.findById(persoanaLogata).get().getTransactions());
			}else {
				newTransferRepo = new LinkedList<>();
			}
			newTransferRepo.add(infoTransaction);
			istoricRepo.save(new IstoricTransfers(persoanaLogata, newTransferRepo));

			//salvare informatiilor despre tranzactie in istoricul contului caruia i se efectueaza transferul
			String infoReceiveTransaction = "Receive " +amountTransfer +"$ from: [" + persoanaLogata +"] "
					+ newAccount_expeditor.firstName + " " + newAccount_expeditor.lastName;
			if(istoricRepo.existsById(transferto_ID)) {
				newTransferRepo = new LinkedList<>(istoricRepo.findById(transferto_ID).get().getTransactions());
			}else {
				newTransferRepo = new LinkedList<>();
			}
			newTransferRepo.add(infoReceiveTransaction);
			istoricRepo.save(new IstoricTransfers(transferto_ID, newTransferRepo));

			//salvare persoana in istoricAccountsRepository - conturile cu care s-a interactionat in transferuri
			String saveAccount_for_istoricAccountRepo = "[" + transferto_ID + "] : " + newAccount_beneficiar.firstName 
					+ " " + newAccount_beneficiar.lastName + "  " ;
			if(istoricAccoutsRepo.existsById(persoanaLogata)) {
				//in cazul in care persoana are deja un istoric de interactiuni cu alti utilizatori
				//verificam daca persoana nu se afla dela in list ul cu persoane 
				//transferto_ID - persoana nu care se face transferul
				for(String acc:istoricAccoutsRepo.findById(persoanaLogata).get().istoricAccounts) {
					if(acc.contains(Long.toString(transferto_ID))){
						inDataBase = true;
					}
				}
				if(inDataBase == false) {
					newAccountRepo = new LinkedList<>(istoricAccoutsRepo.findById(persoanaLogata).get().istoricAccounts);
					newAccountRepo.add(saveAccount_for_istoricAccountRepo);
					istoricAccoutsRepo.save(new IstoricAccounts(persoanaLogata, newAccountRepo));
					System.out.println("Persoana noua introdusa in InfoAccountsRepo");
				}
			}else{
				//in cazul in care persoana nu are un istoric de interactiuni intre utilizatori
				//pregatire string de adaugat in db
				newAccountRepo = new LinkedList<>();
				newAccountRepo.add(saveAccount_for_istoricAccountRepo);
				istoricAccoutsRepo.save(new IstoricAccounts(persoanaLogata, newAccountRepo));
			}
		}
	}
}


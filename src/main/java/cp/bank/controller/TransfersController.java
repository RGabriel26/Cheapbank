package cp.bank.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cp.entity.Account;
import cp.entity.HistoryAccounts;
import cp.entity.HistoryTransfers;
import cp.entity.TransferToAccout;

import services.AccountService;
import services.HistoryAccountsService;
import services.HistoryTransfersService;

@Controller
public class TransfersController {
	
	private Long loggedID;
	
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private HistoryTransfersService historyTranService;
	@Autowired
	private HistoryAccountsService historyAccService;

	@PostMapping("/deposit")
	public String deposit(Model model, @ModelAttribute TransferToAccout newTransferToAccout) {
		if(newTransferToAccout.amound_toID == null) {
			model.addAttribute("atmPageMessage", "Invalid amount!");
			model.addAttribute("transfer", new TransferToAccout());
			model.addAttribute("info_sold", accountService.getAccount(loggedID).get().getSold());
			model.addAttribute("info_name", accountService.getAccount(loggedID).get().firstName + " " + accountService.getAccount(loggedID).get().lastName);
			return "atm/atm.html";
		}else {
			logicTransfer(loggedID, newTransferToAccout.amound_toID, "Deposit");
			model.addAttribute("atmPageMessage", "Successfully transferred!");
			model.addAttribute("transfer", new TransferToAccout());
			model.addAttribute("info_sold", accountService.getAccount(loggedID).get().getSold());
			model.addAttribute("info_name", accountService.getAccount(loggedID).get().firstName + " " + accountService.getAccount(loggedID).get().lastName);
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
			return "loginPages/deshboard.html";
		}else if(newTransferToAccout.transfer_toID.equals(loggedID)) {
			System.out.println("Nu poti sa faci un tranfer catre tine!");
			model.addAttribute("warning_transfer", "Cannot make a transfer to you!");
			return "loginPages/deshboard.html";
		}
		if(newTransferToAccout.amound_toID==null) {
			System.out.println("Amount invalid");
			model.addAttribute("warning_transfer", "Amount invalid!");
			return "loginPages/deshboard.html";
		}
		if(!accountService.existID(newTransferToAccout.transfer_toID)) {
			System.out.println("CNP doesn't exist");
			model.addAttribute("warning_transfer", "CNP is not associated with a valid account!");
			return "loginPages/deshboard.html";
		}
		transferto_ID = newTransferToAccout.transfer_toID;
		amountTransfer = newTransferToAccout.amound_toID;

		if(accountService.getAccount(loggedID).get().getSold() < amountTransfer) {
			System.out.println("Suma de transferat este mai mare decat soldul contului.");
			model.addAttribute("warning_transfer", "Not enough money!");
			return "loginPages/deshboard.html";
		}
		//realizare transfer
		logicTransfer(transferto_ID, amountTransfer, "Transfer");


		System.out.println("Transfer reusit.");
		model.addAttribute("warning_transfer", "Successful transfer!");
		return "loginPages/deshboard.html";
	}

	private void logicTransfer(Long transferto_ID, Double amountTransfer, String origin) {
		//provenienta banilor
		List<String> newTransferRepo;
		List<String> newAccountRepo;
		boolean inDataBase = false;

		if(origin.equals("Deposit")) {
			//adaugare suma in cont
			Account newAccount_expeditor = new Account();
			newAccount_expeditor.setEmail( accountService.getAccount(loggedID).get().email);
			newAccount_expeditor.setFirstName(accountService.getAccount(loggedID).get().firstName);
			newAccount_expeditor.setIdCnp(loggedID);
			newAccount_expeditor.setLastName(accountService.getAccount(loggedID).get().lastName);
			newAccount_expeditor.setPassword(accountService.getAccount(loggedID).get().getPassword());
			newAccount_expeditor.setPhoneNumber(accountService.getAccount(loggedID).get().phoneNumber);
			newAccount_expeditor.setSold((accountService.getAccount(loggedID).get().getSold()) + amountTransfer);
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
			newAccount_beneficiar.setEmail(accountService.getAccount(transferto_ID).get().email);
			newAccount_beneficiar.setFirstName(accountService.getAccount(transferto_ID).get().firstName);
			newAccount_beneficiar.setIdCnp(transferto_ID);
			newAccount_beneficiar.setLastName(accountService.getAccount(transferto_ID).get().lastName);
			newAccount_beneficiar.setPassword(accountService.getAccount(transferto_ID).get().getPassword());
			newAccount_beneficiar.setPhoneNumber(accountService.getAccount(transferto_ID).get().phoneNumber);
			newAccount_beneficiar.setSold((accountService.getAccount(transferto_ID).get().getSold()) + amountTransfer);
			accountService.saveNewAccount(newAccount_beneficiar);

			//scadere suma din contul expeditor
			Account newAccount_expeditor = new Account();
			newAccount_expeditor.setEmail(accountService.getAccount(loggedID).get().email);
			newAccount_expeditor.setFirstName(accountService.getAccount(loggedID).get().firstName);
			newAccount_expeditor.setIdCnp(loggedID);
			newAccount_expeditor.setLastName(accountService.getAccount(loggedID).get().lastName);
			newAccount_expeditor.setPassword(accountService.getAccount(loggedID).get().getPassword());
			newAccount_expeditor.setPhoneNumber(accountService.getAccount(loggedID).get().phoneNumber);
			newAccount_expeditor.setSold((accountService.getAccount(loggedID).get().getSold()) - amountTransfer);
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

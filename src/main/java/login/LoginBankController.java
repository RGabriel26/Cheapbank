package login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cp.Class.Account;
import cp.Class.BankAccount;


@Controller
public class LoginBankController {
	
	Map<Integer, Account> clientData = new HashMap<>();
	Map<Integer, BankAccount> banckData = new HashMap<>();
	
	//creez mai intai instantele pentru clienti si conturile din banca si dupa le adaug in map uri
	

}

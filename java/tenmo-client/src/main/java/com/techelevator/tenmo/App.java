package com.techelevator.tenmo;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, 
			LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, 
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, 
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), 
    			new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), 
    			new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, 
    		AccountService accountService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		Double balance = accountService.getAccountBalance(currentUser);
		System.out.println("Your current account balance is: $" + balance);
	}

	private void viewTransferHistory() {	
		/*
		 *  First, the method lists all transfers the user was a party to
		 */
		Transfer[] allOfUsersTransfers = transferService.getAllTransfers(currentUser);
		
		System.out.println("--------------------------------");
		System.out.println("Transfers                       ");
		System.out.println("ID         From/To        Amount");
		System.out.println("--------------------------------");
		
		for (Transfer transfer : allOfUsersTransfers) {
			
			// Create readable variables
			Integer id = transfer.getTransferId();
			boolean isUserTheSender = determineIfUserIsSender(transfer);
			String fromOrTo = isUserTheSender ? "To:   " : "From: ";
			String nameOfOtherPartyInTransfer = getNameOfOtherPartyInTransfer(isUserTheSender, transfer);
			Double amount = transfer.getAmount();
			
			System.out.println(id + "      " + fromOrTo + 
					nameOfOtherPartyInTransfer + "      $ " + amount);						
		}
		
		System.out.println("--------------------------------");
		
		/*
		 *  Second, it asks the user if they want to see any transfer details
		 */
		Integer usersChoice = null;
		boolean continueLoop = true;
		while ( continueLoop ) {
			String choice = console.getUserInput("Please enter transfer ID to view details (0 to cancel)");
			try {
				usersChoice = Integer.parseInt(choice);
				continueLoop = false;
			} catch (NumberFormatException ex) {
				System.out.println("You must type a number. Please try again.");
			}
		}
		
		if (usersChoice.equals(0)) { // If 0, returns to main menu
			mainMenu();
		}
		
		// Pulls transfer data from list of transfers
		Transfer transferToPrint = null;
		for (Transfer transfer : allOfUsersTransfers) {
			if (transfer.getTransferId().equals(usersChoice)) {
				transferToPrint = transfer;
			}
		}
		
		if (transferToPrint == null) { // Variable will remain null if user didn't type a valid id number
			System.out.println("Sorry, that was not a valid selection. Please try again.");
			viewTransferHistory(); // Return user to list of transfers
		} else {
			
			// Create readable variables
			boolean isUserTheSender = determineIfUserIsSender(transferToPrint);
			String nameOfOther = getNameOfOtherPartyInTransfer(isUserTheSender, transferToPrint);			
			String nameOfUser = accountService.getUsername(currentUser, currentUser.getUser().getId());
			String transferTypeName = transferService.getTransferTypeName
					(currentUser, transferToPrint.getTransferType());
			String transferStatusName = transferService.getTransferStatusName
					(currentUser, transferToPrint.getTransferStatus());
			
			System.out.println("--------------------------------");
			System.out.println("Transfer Details");
			System.out.println("--------------------------------");
			System.out.println("Id:      " + transferToPrint.getTransferId());
			System.out.println("From:    " + ((isUserTheSender) ? nameOfUser : nameOfOther));
			System.out.println("To:      " + ((!isUserTheSender) ? nameOfUser : nameOfOther));
			System.out.println("Type:    " + transferTypeName);
			System.out.println("Status:  " + transferStatusName);
			System.out.println("Amount: $" + transferToPrint.getAmount());
		}

	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		/*
		 *  First, asks user to choose the recipient of transfer
		 */
		User[] users = accountService.getAllUsers(currentUser);
		
		System.out.println("--------------------------------");
		System.out.println("Users                           ");
		System.out.println("ID           Name               ");
		System.out.println("--------------------------------");
		for (User user : users) {
			System.out.println(user.getId() + "    :    " + user.getUsername());
		}
		System.out.println("--------------------------------");
		System.out.println();
		
		Long recipientId = null;
		boolean continueLoop1 = true;
		while ( continueLoop1 ) {
			String choice = console.getUserInput("Enter ID of user you are sending to (0 to cancel)");
			try {
				recipientId = Long.parseLong(choice);
				continueLoop1 = false;
			} catch (NumberFormatException ex) {
				System.out.println("You must type a number. Please try again.");
			}
		}
		
		if (recipientId.equals(0L)) { // If 0, returns to main menu
			mainMenu();
		}
		
		/*
		 *  Second, asks user for amount of transfer
		 */
		Double amount = null;
		boolean continueLoop2 = true;
		while ( continueLoop2 ) {
			String choice = console.getUserInput("Enter amount");
			try {
				amount = Double.parseDouble(choice);
				continueLoop2 = false;
			} catch (NumberFormatException ex) {
				System.out.println("You must type a number. Please try again.");
			}
		}
		
		if (amount.equals(0.0)) { // Prevents user from creating a transfer of $0.00
			System.out.println("You must input a number above 0. Please try again.");
			mainMenu();
		}
		
		// Enact transfer and add it to the database
		transferService.transferTEBucks(currentUser, recipientId, amount);
		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}
	
	/**
	 * Determines if the current user was the sender or the recipient of a given transfer
	 * 
	 * @param transfer The transfer in question
	 * @return a boolean indicating if the user was the sender (and not the recipient)
	 */
	private boolean determineIfUserIsSender(Transfer transfer) {
		Long userId = currentUser.getUser().getId();
		Long userIdOfAcctFrom = (long)(transfer.getAccountFrom().getUserId());
		boolean isUserTheSender = userId.equals(userIdOfAcctFrom);
		return isUserTheSender;
	}
	
	/**
	 * Given a transfer, retrieves the name of the other party involved (i.e., not the user)
	 * 
	 * @param isUserTheSender A boolean indicating if the user was the sender (and not the recipient)
	 * @param transfer The transfer in question
	 * @return the name of the other party
	 */
	private String getNameOfOtherPartyInTransfer(boolean isUserTheSender, Transfer transfer) {
		Long userIdOfOther;
		if (isUserTheSender) {
			userIdOfOther = (long)(transfer.getAccountTo().getUserId());
		} else {
			userIdOfOther = (long)(transfer.getAccountFrom().getUserId());
		}
		String nameOfOtherPartyInTransfer = accountService.getUsername(currentUser, userIdOfOther);
		return nameOfOtherPartyInTransfer;
	}
	
	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}

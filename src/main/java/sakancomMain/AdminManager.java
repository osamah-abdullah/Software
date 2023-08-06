package sakancomMain;

import java.io.IOException;
import java.util.*;
import owner.*;

public class AdminManager{
	private static Skankom skankom = Skankom.getInstance();

	public static void createAdmin(User user) throws IOException {
		System.out.println("Enter Admin Name:");
		String adminName = SakancomApp.getScanner().nextLine();
		System.out.println("Enter Email:");
		String email = SakancomApp.getScanner().nextLine();
		System.out.println("Enter Phone Number:");
		String phoneNumber = SakancomApp.getScanner().nextLine();
		Owner owner = new Owner(adminName, email, phoneNumber);
		Admin admin = new Admin(owner.getId(), adminName);
		user.setUserId(admin.getId());
		skankom.addAdmin(admin);
		skankom.addOwner(owner);
		skankom.addUser(user);
		System.out.println(admin.toString() + " is created.");
		enterAsAdmin(admin);
	}

	public static void enterAsAdmin(Admin admin) throws IOException {
		System.out.println("Choose one of the following options:\n1) Show Announecemnts \n2) Show Reservations \n3) Add Announcement \n4) Show Housings \n5) Add Housing \n6) Exit");
		int choice = SakancomApp.scanInt();
		Owner owner = skankom.getOwner(admin.getOwnerId());
		switch (choice) {
		case 1:
			AnnouncementManager.showAnnouncements(admin);
			break;
		case 2:
			ReservationManager.showReservations();
			enterAsAdmin(admin);
			break;
		case 3:
			if (owner != null) {
				AnnouncementManager.addAnnouncement(owner);
			}
			enterAsAdmin(admin);
			break;
		case 4:
			HousingManager.showHousings(admin);
			enterAsAdmin(admin);
			break;
		case 5:
			if (owner != null) {
				HousingManager.createHousing(owner);
			}
			enterAsAdmin(admin);
			break;
		default:
			SakancomApp.entrance();
			return;
		}
	}
}
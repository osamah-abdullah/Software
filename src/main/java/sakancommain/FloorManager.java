package sakancommain;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import owner.*;

public class FloorManager {
	private FloorManager () {}
	private static final Logger LOGGER = Logger.getLogger(FloorManager.class.getName());
	private static Skankom skankom = Skankom.getInstance();

	public static void manageFloors(Housing housing) throws IOException {
		LOGGER.log(Level.INFO,"Choose one of the following options:\n1) Add Floor \n2) Remove Floor \n3) Show Floors \n4) Finish");
		int choice = SakancomApp.scanInt();
		switch (choice) {
		case 1:
			Floor floor = new Floor(housing.getId(), housing.getFloors().size() + 1);
			floor = createFloor(housing, floor);
			String s=floor.toString() + " is created to Housing with: " + housing.toString();
			LOGGER.log(Level.INFO,s);
			housing.addFloor(floor);
			skankom.addFloor(floor);
			manageFloors(housing);
			break;
		case 2:
			String floorInfo = deleteFloor(housing);
			if (floorInfo != null) {
				String v=floorInfo + " is deleted from Housing with: " + housing.toString();
				LOGGER.log(Level.INFO,v);
			}
			manageFloors(housing);
			break;
		case 3:
			showFloors(housing);
			manageFloors(housing);
			break;
		case 4:
			break;
		default:
			LOGGER.log(Level.INFO,"Wrong choice.\n1) Try Again 2) Finish");
			if (SakancomApp.scanInt() == 1) {
				manageFloors(housing);
			}
		}
	}

	public static void showFloors(Housing housing) {
		if (housing.getFloors().isEmpty()) {
			String m="No floors added yet to " + housing.toString();
			LOGGER.log(Level.INFO,m);
			return;
		}
		String y="Floors in " + housing.toString() + " are: [";
		LOGGER.log(Level.INFO,y);
		for (String floorId: housing.getFloors()) {
			Floor floor = skankom.getFloor(floorId);
			if (floor != null) {
				String q=floor.toString();
				LOGGER.log(Level.INFO,q);
			}
		}
		LOGGER.log(Level.INFO,"]");
	}

	public static String deleteFloor(Housing housing) throws IOException {
		if (housing.getFloors().isEmpty()) {
			String l="No floors added yet to " + housing.toString();
			LOGGER.log(Level.INFO,l);
			return null;
		}
		return removeFloor(housing);
	}

	public static String removeFloor(Housing housing) throws IOException {
		LOGGER.log(Level.INFO,"Enter Floor Number:");
		int floorNumber = SakancomApp.scanInt();
		if (floorNumber > housing.getFloors().size()) {
			LOGGER.log(Level.INFO,"The entered number is not exist.\n1) Try Again \n2) Cancel");
			if (SakancomApp.scanInt() == 1) {
				return removeFloor(housing);
			}
			return null;
		}
		LOGGER.log(Level.INFO,"1) Remove \n2) Cancel");
		Boolean remove = SakancomApp.scanInt() == 1;
		if (Boolean.TRUE.equals(!remove)) { return null; }
		String floorId = housing.getFloors().get(floorNumber - 1);
		Floor floor = skankom.getFloor(floorId);
		String floorInfo = floor.toString();
		housing.removeFloor(floor);
		skankom.removeFloor(floor);
		return floorInfo;
	}

	public static Floor createFloor(Housing housing, Floor floor) throws IOException {
		LOGGER.log(Level.INFO,"Choose one of the following options:\n1) Add Apartment \n2) Remove Apartment \n3) Show Apartments \n4) Finish");
		int choice = SakancomApp.scanInt();
		switch (choice) {
		case 1:
			Apartment apartment = ApartmentManager.createApartment();
			if (apartment != null) {
				floor.addApartment(apartment);
				String r="Apartment with Info: " + apartment.toString() + " is added to " + floor.toString();
				LOGGER.log(Level.INFO,r);
			}
			return createFloor(housing, floor);
		case 2:
			String apartmentInfo = ApartmentManager.deleteApartment(floor);
			if (apartmentInfo != null) {
				String u="Apartment with Info: " + apartmentInfo + " is deleted from " + floor.toString();
				LOGGER.log(Level.INFO,u);
			}
			return createFloor(housing, floor);
		case 3:
			ApartmentManager.showApartments(floor);
			return createFloor(housing, floor);
		case 4:
			return floor;
		default:
			LOGGER.log(Level.INFO,"Wrong choice.\n1) Try Again 2) Finish");
			if (SakancomApp.scanInt() == 1) {
				return createFloor(housing, floor);
			}
		}
		return floor;
	}
}
package owner;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import sakancommain.*;

import java.io.*;

public class Housing implements Serializable {
	private String id;
	private String ownerId;
	private String location;
	private boolean isStudentHousing;
	private ArrayList<String> floors;
	private Map<String, String> reservations;

	public Housing(String ownerId, String location, boolean isStudentHousing) {
		this.id = UUID.randomUUID().toString();
		this.ownerId = ownerId;
		this.location = location;
		this.isStudentHousing = isStudentHousing;
		this.floors = new ArrayList<>();
		this.reservations = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getLocation() {
		return location;
	}

	public Map<String, String> getReservations() {
		return reservations;
	}

	public void addReservation(String tenantId, String apartmentId) throws IOException {
		reservations.put(tenantId, apartmentId);
		save();
	}

	public void removeReservation(String tenantId) throws IOException {
		reservations.remove(tenantId);
		Tenant tenant = Skankom.getInstance().getTenant(tenantId);
		if (tenant != null) {
			tenant.removeReservedHousing(getId());
		}
		save();
	}
	private static final Logger LOGGER = Logger.getLogger(Housing.class.getName());
	public void addTenant(String tenantId, String apartmentId) throws IOException {
		removeReservation(tenantId);
		if (tenantId == null) {
			LOGGER.log(Level.INFO,"Tenant id is null");
			return;
		}
		Apartment apartment = Skankom.getInstance().getApartment(apartmentId);
		Tenant tenant = Skankom.getInstance().getTenant(tenantId);
		if (apartment != null && tenant != null) {
			if (tenant.getBookedHousing() != null) {
				LOGGER.log(Level.INFO,"The Tenant already booking other apartment.");
				return;
			}
			LOGGER.log(Level.INFO,"Entre Time to Pay: ");
			String timeToPay = SakancomApp.getScanner().nextLine();
			tenant.setTimeToPay(timeToPay);
			apartment.addTenant(tenantId);
			String h=apartment.toString() + " is now resreved to Tenant with id: " + tenantId;
			LOGGER.log(Level.INFO,h);
		} else {
			LOGGER.log(Level.INFO,"Apartment or Tenant are no longer exist in system");
		}
		save();
	}

	public void setLocation(String location) throws IOException {
		this.location = location;
		save();
	}

	public void setOwnerId(String ownerId) throws IOException {
		this.ownerId = ownerId;
		save();
	}

	public boolean isStudentHousing() {
		return isStudentHousing;
	}

	public void setStudentHousing(boolean studentHousing) throws IOException {
		isStudentHousing = studentHousing;
		save();
	}

	public List<String> getFloors() {
		return floors;
	}

	public void addFloor(Floor floor) throws IOException {
		floors.add(floor.getId());
		save();
	}

	public void removeFloor(Floor floor) throws IOException {
		floors.remove(floor.getId());
		updateFloorNumbers();
		save();
	}
	
	public void removeTenant(Tenant tenant) throws IOException {
		List<Apartment> apartments = getApartments();
		for (Apartment apartment: apartments) {
			apartment.removeTenant(tenant.getId());
		}
		save();
	}

	public void updateFloorNumbers() throws IOException {
		int floorNumber = 1;
		for (String floorId: floors) {
			Floor floor = Skankom.getInstance().getFloor(floorId);
			if (floor != null) {
				floor.setFloorNumber(floorNumber);
				floorNumber++;
			}
		}
	}

	public List<String> getTenants(){
		ArrayList<String> tenantsIds = new ArrayList<>();
		for (String floorId: floors) {
			Floor floor = Skankom.getInstance().getFloor(floorId);
			if (floor != null) {
				for (String apartmentId: floor.getApartments()) {
					Apartment apartement = Skankom.getInstance().getApartment(apartmentId);  
					if (apartement != null) {
						tenantsIds.addAll(apartement.getTenants());
					}
				}
			}
		}
		return tenantsIds;
	}

	public List<Apartment> getApartments() {
		ArrayList<Apartment> apartments = new ArrayList<>();
		for (String floorId: floors) {
			Floor floor = Skankom.getInstance().getFloor(floorId);
			if (floor != null) {
				for (String apartmentId: floor.getApartments()) {
					Apartment apartement = Skankom.getInstance().getApartment(apartmentId);  
					if (apartement != null) {
						apartments.add(apartement);
					}
				}
			}
		}
		return apartments;
	}

	public void viewHousingStatistics(){
		if (floors.isEmpty()) {
			LOGGER.log(Level.INFO,"* No floors in this housing yet.");
		} else {
			String b="* " + floors.size() + " floors in this housing.";
			LOGGER.log(Level.INFO,b);
		}
		List<String> tenants = getTenants();
		if (tenants.isEmpty()) {
			LOGGER.log(Level.INFO,"* No tenants in this housing yet.");
		} else {
			String v="* " + tenants.size() + " tenants in this housing:";
			LOGGER.log(Level.INFO,v);
		}
	}

	public void viewFloors(){
		String h=toString();
		LOGGER.log(Level.INFO,h);
		if (floors.isEmpty()) {
			LOGGER.log(Level.INFO,"No floors in this housing yet.");
		} else {
			LOGGER.log(Level.INFO,"Floors in this housing:");
			for (String floorId : floors) {
				Floor floor = Skankom.getInstance().getFloor(floorId);
				floor.viewApartments();
			}
		}
	}

	public void viewTenants(){
		if (!isStudentHousing) {
			LOGGER.log(Level.INFO,"Sorry, we cannot show tenants for non student housing.");
			return;
		}
		List<String> tenants = getTenants();
		if (tenants.isEmpty()) {
			LOGGER.log(Level.INFO,"No tenants in this housing yet.");
			return;
		}
		for (String tenantId: tenants) {
			Tenant tenant = Skankom.getInstance().getTenant(tenantId);
			if (tenant != null) {
				String j="* " + tenant.getTenantInfo();
				LOGGER.log(Level.INFO,j);
			}
		}
	}

	@Override
	public String toString() {
		return "Location: " + location;
	}
	
	private void save() throws IOException {
		Skankom.getInstance().writeToFile();
	}
}

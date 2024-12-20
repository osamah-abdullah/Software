package owner;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import sakancommain.Skankom;

import java.io.*;

public class Floor implements Serializable {
	private String id;
	private String housingId;
	private int floorNumber;
	private List<String> apartments;

	public Floor(String housingId, int floorNumber) {
		this.id = UUID.randomUUID().toString();
		this.housingId = housingId;
		this.floorNumber = floorNumber;
		this.apartments = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}

	public String getHousingId() {
		return housingId;
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(int floorNumber) throws IOException {
		this.floorNumber = floorNumber;
		save();
	}
	
	public void setHousingId(String housingId) throws IOException {
		this.housingId = housingId;
		save();
	}
	
	public List<String> getApartments() {
		return apartments;
	}

	public void addApartment(Apartment apartment) throws IOException {
		apartments.add(apartment.getId());
		save();
	}
	
	public void removeApartment(Apartment apartment) throws IOException {
		apartments.remove(apartment.getId());
		save();
	}
	private static final Logger LOGGER = Logger.getLogger(Floor.class.getName());
	public void viewApartments(){
		String a=toString();
		LOGGER.log(Level.INFO,a);
		if (apartments.isEmpty()) {
			String t="You haven't listed any apartments yet.";
			LOGGER.log(Level.INFO,t);
		} else {
			LOGGER.log(Level.INFO,"Your listed apartments:");
			for (String apartmentId : apartments) {
				Apartment apartment = Skankom.getInstance().getApartment(apartmentId);
				if (apartment != null) {
					 apartment.viewApartmentInfo();
				}
			}
		}
	}
	
	@Override
    public String toString() {
        return "Floor Number: " + floorNumber;
    }
	
	private void save() throws IOException {
		Skankom.getInstance().writeToFile();
	}
}

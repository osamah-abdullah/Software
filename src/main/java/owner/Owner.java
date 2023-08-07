package owner;

import java.util.*;
import java.io.*;
import sakancomMain.Skankom;

public class Owner extends Person implements Serializable {
	private String id;
    private ArrayList<String> ownedHousing;

    public Owner(String email, String phoneNumber) throws IOException {
    	this.id = UUID.randomUUID().toString();
        this.ownedHousing = new ArrayList<>();
        super.setEmail(email);
        super.setPhoneNumber(phoneNumber);
    }
    
    public String getId() {
    	return id;
    }
    
    public void addHousing(Housing housing) throws IOException {
        ownedHousing.add(housing.getId());
        save1();
    }
    
    public List<String> getOwnedHosuings() {
    	return ownedHousing;
    }
    
    public void removeHousing(Housing housing) throws IOException {
    	ownedHousing.remove(housing);
    	save1();
    }

    public void viewOwnedHousing() throws IOException {
        if (ownedHousing.isEmpty()) {
            System.out.println("You don't have any owned housing yet.");
        } else {
            System.out.println("Your owned housing:");
            for (String housingId : ownedHousing) {
            	Housing housing = Skankom.getInstance().getHousing(housingId);
                System.out.println(housing.getLocation());
            }
        }
    }

    @Override
    public String toString() {
        return "Owner Name: " + super.getName() + ", " + super.toString();
    }
    
    private void save1() throws IOException {
		Skankom.getInstance().writeToFile();
	}
}

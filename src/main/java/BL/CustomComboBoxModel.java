package BL;
import Data.Profile;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import java.util.LinkedList;

public class CustomComboBoxModel implements ComboBoxModel<Profile>{
    private LinkedList<Profile> profiles;
    private Object selectedItem;

    public CustomComboBoxModel(LinkedList<Profile> profiles) {
        this.profiles = profiles;
        // Set the default selected item
        selectedItem = profiles.getFirst();
    }

    public LinkedList<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(LinkedList<Profile> profiles) {
        this.profiles = profiles;
    }
    public void addProfiles(Profile profile) {
        profiles.add(profile);
    }

    @Override
    public int getSize() {
        return profiles.size();
    }

    @Override
    public Profile getElementAt(int index) {
        return profiles.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener listener) {
        // Not implemented
    }

    @Override
    public void removeListDataListener(ListDataListener listener) {
        // Not implemented
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
}

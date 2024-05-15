package com.ds.darknesschat.client;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.List;

public class ClientNameMathWithOtherNamesFinder {
    private boolean isThisUserFound = false;
    private int mathPerCent;
    private final List<Client> clientList;
    private final String checkingName;

    public ClientNameMathWithOtherNamesFinder(String checkingName, List<Client> clientList) {
        this.checkingName = checkingName;
        this.clientList = clientList;
        
        checkName();
    }

    private void checkName() {
        for (Client currentClient : clientList) {
            int mathPerCent = FuzzySearch.ratio(currentClient.getClientName(), checkingName);

            if(mathPerCent >= 50){
                isThisUserFound = true;
                this.mathPerCent = mathPerCent;

                break;
            }
        }
    }

    public boolean isThisUserFound() {
        return isThisUserFound;
    }

    public int getMathPerCent() {
        return mathPerCent;
    }
}

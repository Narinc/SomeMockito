package com.volkan.somemockito;

/**
 * Created by volkannarinc on 21.03.2018 18:25.
 */

public class MailDeliverer {
    private ExternalMailSystem externalMailSystem;

    public void deliver(String address, String body) {
        Email email = new Email();
        email.setBody(body);
        applyUserAndDomainUsingProvidedAddress(address, email);
        externalMailSystem.send(email);
    }

    private void applyUserAndDomainUsingProvidedAddress(String address, Email email) {
        //TODO - refactor this into the Email class (constructor? smart accessor?)
        String[] addressComponents = address.split("@");
        email.setUser(addressComponents[0]);
        email.setDomain(addressComponents[1]);
    }
}

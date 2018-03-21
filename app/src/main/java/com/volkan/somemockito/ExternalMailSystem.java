package com.volkan.somemockito;

/**
 * Created by volkannarinc on 21.03.2018 18:25.
 */

public interface ExternalMailSystem {
    public void send(String domain, String user, String body);

    public void send(Email email);
}

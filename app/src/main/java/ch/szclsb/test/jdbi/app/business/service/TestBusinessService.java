package ch.szclsb.test.jdbi.app.business.service;

import org.springframework.stereotype.Service;

@Service
public class TestBusinessService {

    public String getTestMessage(final String name) {
        return "Server: hello " + name;
    }
}

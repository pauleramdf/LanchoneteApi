package com.br.lanchonete.lanchoneteapi.factory;

import com.br.lanchonete.lanchoneteapi.model.Client;

import java.util.UUID;

public class ClientFactory {

        public static Client createValidClient() {
            Client client = new Client();
            client.setId(UUID.randomUUID());
            client.setName("Test Client");
            client.setEmail("email@email.com");
            return client;
        }
}

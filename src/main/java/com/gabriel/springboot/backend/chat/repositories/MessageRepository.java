package com.gabriel.springboot.backend.chat.repositories;

import com.gabriel.springboot.backend.chat.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}

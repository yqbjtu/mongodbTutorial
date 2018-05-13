package com.yq.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:application-bean.xml"})
public class MongoConfig {

  /*public @Bean MongoClient mongoClient() {
      //if the mongodb auth is disabled, the username and password is not needed.
      //return new MongoClient("192.168.81.140");
      return new MongoClient(Collections.singletonList(new ServerAddress("192.168.81.140", 27017)),
          Collections.singletonList(MongoCredential.createCredential("user1", "db1", "passwd1".toCharArray())));
  }
  public @Bean MongoTemplate mongoTemplate() {
      return new MongoTemplate(mongoClient(), "db2");
  }
  */
}
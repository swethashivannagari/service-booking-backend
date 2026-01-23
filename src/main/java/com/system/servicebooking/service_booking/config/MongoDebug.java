//package com.system.servicebooking.service_booking.config;
//
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//@Configuration
//public class MongoDebug {
//
//    @Bean
//    CommandLineRunner debugMongo(MongoTemplate mongoTemplate) {
//        return args -> {
//            System.out.println("------------------------------------------------");
//            System.out.println("Mongo DB Name      : " + mongoTemplate.getDb().getName());
//            System.out.println("Mongo Server       : " + mongoTemplate.getMongoDatabaseFactory()
//                    .getMongoDatabase().getName());
////            System.out.println("Mongo Client Class : " + mongoTemplate.getMongoDatabaseFactory()
////                    .getMongoDatabase().getClass().getName());
//            System.out.println("------------------------------------------------");
//        };
//    }
//}

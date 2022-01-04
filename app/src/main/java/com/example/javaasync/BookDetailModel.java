package com.example.javaasync;

public class BookDetailModel {
    Integer id;
    String title;
    Integer amount;

    BookDetailModel(Integer id, String title, Integer amount) {
       this.id = id;
       this.title = title;
       this.amount = amount;
    }
}

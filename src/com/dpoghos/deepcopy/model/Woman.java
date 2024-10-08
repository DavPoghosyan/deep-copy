package com.dpoghos.deepcopy.model;

import java.util.List;

public class Woman {
    private String name;
    private int age;
    private List<String> favoriteBooks;

    public Woman(String name, List<String> favoriteBooks) {
        this.name = name;
        this.favoriteBooks = favoriteBooks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<String> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }
}
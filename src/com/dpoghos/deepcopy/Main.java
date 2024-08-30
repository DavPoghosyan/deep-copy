package com.dpoghos.deepcopy;

import java.util.ArrayList;
import java.util.List;

import com.dpoghos.deepcopy.model.Man;
import com.dpoghos.deepcopy.model.Woman;
import com.dpoghos.deepcopy.utils.DeepCopyUtils;

public class Main {

    public static void main(String[] args) {
        System.out.println("Testing deep copy for Man class:");
        testManClass();

        System.out.println("\nTesting deep copy for Woman class:");
        testWomanClass();
    }

    private static void testManClass() {
        List<String> manBooks = new ArrayList<>();
        manBooks.add("The Little Prince");
        manBooks.add("War and Peace");
        manBooks.add("Samuel");

        Man originalMan = new Man("Davit", 32, manBooks);

        Man copyMan = DeepCopyUtils.deepCopy(originalMan);

        System.out.println("Original Man:");
        displayManDetails(originalMan);

        System.out.println("\nCopy Man:");
        displayManDetails(copyMan);

        originalMan.setName("Petros");
        originalMan.setAge(23);
        originalMan.getFavoriteBooks().add("The Book Thief");

        System.out.println("\nAfter modifying the original Man:");
        System.out.println("Original Man:");
        displayManDetails(originalMan);

        System.out.println("\nCopy Man:");
        displayManDetails(copyMan);
    }

    private static void testWomanClass() {
        List<String> womanBooks = new ArrayList<>();
        womanBooks.add("The Little Prince");
        womanBooks.add("War and Peace");

        Woman originalWoman = new Woman("Anna", womanBooks);
        originalWoman.setAge(29);

        Woman copyWoman = DeepCopyUtils.deepCopy(originalWoman);

        System.out.println("Original Woman:");
        displayWomanDetails(originalWoman);

        System.out.println("\nCopy Woman:");
        displayWomanDetails(copyWoman);

        originalWoman.setName("Elena");
        originalWoman.getFavoriteBooks().add("Pride and Prejudice");

        System.out.println("\nAfter modifying the original Woman:");
        System.out.println("Original Woman:");
        displayWomanDetails(originalWoman);

        System.out.println("\nCopy Woman:");
        displayWomanDetails(copyWoman);
    }

    private static void displayManDetails(Man man) {
        System.out.println("Name: " + man.getName());
        System.out.println("Age: " + man.getAge());
        System.out.println("Favorite Books: " + man.getFavoriteBooks());
    }

    private static void displayWomanDetails(Woman woman) {
        System.out.println("Name: " + woman.getName());
        System.out.println("Age: " + woman.getAge());
        System.out.println("Favorite Books: " + woman.getFavoriteBooks());
    }
}

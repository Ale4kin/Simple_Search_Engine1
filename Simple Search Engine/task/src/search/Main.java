package search;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine ();
        searchEngine.run (args);

    }
}

class SearchEngine {
    Scanner scanner = new Scanner (System.in);
    List<String> people = new ArrayList<> ();
    Map<String, List<Integer>> invertedIndex = new HashMap<> ();
    File file;

    public void run(String[] args) {
        file = new File (args[1]);
        readArgs ();
        fillInvertedIndex ();
        getMenu ();
    }

    public void readArgs() {
        try (Scanner reader = new Scanner (file)) {
            while (reader.hasNextLine ()) {
                people.add (reader.nextLine ());
            }
        } catch (FileNotFoundException e) {
            System.err.println ("File not found.");
        }
    }

    public void fillInvertedIndex() {
        for (String str : people) {
            String[] words = str.split ("\\s+");
            for (String word : words) {
                List<Integer> indexes = new ArrayList<> ();
                for (String person : people) {
                    if (person.contains (word) && !invertedIndex.containsKey (word)) {
                        indexes.add (people.indexOf (person));
                    }
                }
                if (!invertedIndex.containsKey (word)) {
                    invertedIndex.put (word.toLowerCase (), indexes);
                }
            }
        }
    }

    public void getMenu() {
        while (true) {
            System.out.println ("\n=== Menu ===");
            System.out.println ("1. Find a person");
            System.out.println ("2. Print all people");
            System.out.println ("0. Exit");

            try {
                int choice = Integer.parseInt (scanner.nextLine ());

                switch (choice) {
                    case 1:
                        System.out.println ("\n" + "Select a matching strategy: ALL, ANY, NONE");
                        String search = scanner.nextLine ().replace ("> ", "");

                        switch (search) {
                            case "ANY": {
                                System.out.println ("\nEnter a name or email to search all suitable people.");
                                findANY (scanner.nextLine ().toLowerCase ().trim ());
                                break;
                            }

                            case "ALL": {
                                System.out.println ("\nEnter a name or email to search all suitable people.");
                                findALL (scanner.nextLine ().toLowerCase ().trim ());
                                break;
                            }

                            case "NONE": {
                                System.out.println ("\nEnter a name or email to search all suitable people.");
                                findNONE (scanner.nextLine ().toLowerCase ().trim ());
                                break;
                            }
                        }
                        break;
                    case 2:
                        printAll ();
                        break;
                    case 0:
                        System.out.println ("\nBye!");
                        return;
                    default:
                        throw new NumberFormatException ();
                }

            } catch (NumberFormatException e) {
                System.out.println ("\nIncorrect option! Try Again.");
            }
        }
    }

    public void findANY(String searchWord) {
        List<String> result = new ArrayList<> ();
        String a = searchWord + " ";
        String[] arrayWords = a.split (" ");
        for (int i = 0; i < arrayWords.length; i++) {
            if (invertedIndex.containsKey (arrayWords[i])) {
                for (int index : invertedIndex.get (arrayWords[i])) {
                    result.add (people.get (index));
                }
            }
        }

        if (result.isEmpty ()) {
            System.out.println ("No matching people found.");
        } else {
            System.out.println ("\n" + result.size () + "  persons found:");
            result.forEach (System.out::println);
        }
    }

    public void findALL(String searchWord) {
        List<String> result = new ArrayList<> ();

        String[] array = (searchWord + " ").split (" ");

        for (int i = 0; i < array.length; i++) {
            if (invertedIndex.containsKey (array[i])) {
                for (int index : invertedIndex.get (array[i])) {
                    result.add (people.get (index));
                }
            }
        }


        if (result.isEmpty ()) {
            System.out.println ("No matching people found.");
        } else {
            System.out.println ("\n" + result.size () + "  persons found:");
            result.forEach (System.out::println);
        }
    }


    public void findNONE(String searchWord) {
        List<String> result;
        List<String> PeopleClone = new ArrayList<>();
        result = people;
        String[] arrayWord = (searchWord + " ").split (" ");
        for (int j = 0; j < arrayWord.length; j++) {
            if (invertedIndex.containsKey (arrayWord[j])) {
                for (int index : invertedIndex.get (arrayWord[j])) {
                    PeopleClone.add (people.get (index));
                }
            }
        }

        result.removeAll (PeopleClone);

        if (result.isEmpty ()) {
            System.out.println ("No matching people found.");
        } else {
            System.out.println ("\n" + result.size () + "  persons found:");
            result.forEach (System.out::println);
        }
    }

    public void printAll() {
        System.out.println ("\n=== List of people ===");
        people.forEach (System.out::println);
    }
}
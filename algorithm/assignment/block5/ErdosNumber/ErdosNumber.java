package block5.ErdosNumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/*
ErdosNumber 

  The Erdos number is a way of describing the "collaborative distance" between a scientist and Paul Erdos by authorship of scientific publications.

  Paul Erdos is the only person who has an Erdos number equal to zero. To be assigned a finite Erdos number, a scientist must publish a paper in co-authorship with a scientist with a finite Erdos number. The Erdos number of a scientist is the lowest Erdos number of his coauthors + 1. The order of publications and numbers assignment doesn't matter, i.e., after each publication the list of assigned numbers is updated accordingly.

  You will be given a String[] publications, each element of which describes the list of authors of a single publication and is formatted as "AUTHOR_1 AUTHOR_2 ... AUTHOR_N" (quotes for clarity only). Paul Erdos will be given as "ERDOS".

  Return the list of Erdos numbers which will be assigned to the authors of the listed publications. Each element of your return should be formatted as "AUTHOR NUMBER" if AUTHOR can be assigned a finite Erdos number, and just "AUTHOR" otherwise. The authors in your return must be ordered lexicographically.
 */
public class ErdosNumber {

  public String[] calculateNumbers(String[] publications) {
    Map<String, Set<String>> graph = new HashMap<>();
    Set<String> allAuthors = new HashSet<>();

    // Build graph
    for (String publication : publications) {
      String[] authors = publication.split(" ");
      for (String author : authors) {
        graph.putIfAbsent(author, new HashSet<>());
        allAuthors.add(author);
      }
      for (int i = 0; i < authors.length; i++) {
        for (int j = i + 1; j < authors.length; j++) {
          graph.get(authors[i]).add(authors[j]);
          graph.get(authors[j]).add(authors[i]);
        }
      }
    }

    Map<String, Integer> erdosNumber = new HashMap<>();
    erdosNumber.put("ERDOS", 0);

    // BFS
    Queue<String> queue = new LinkedList<>();
    queue.offer("ERDOS");

    while (!queue.isEmpty()) {
      String current = queue.poll();
      int currNum = erdosNumber.get(current);
      // get neighbors
      Set<String> neighbors = graph.getOrDefault(current, new HashSet<>());
      for (String neighbor : neighbors) {
        if (!erdosNumber.containsKey(neighbor)) {
          erdosNumber.put(neighbor, currNum + 1);
          queue.offer(neighbor);
        }
      }
    }

    List<String> sortedAuthors = new ArrayList<>(allAuthors);
    Collections.sort(sortedAuthors);

    List<String> result = new ArrayList<>();
    for (String author : sortedAuthors) {
      if (erdosNumber.containsKey(author)) {
        result.add(author + " " + erdosNumber.get(author));
      } else {
        result.add(author);
      }
    }

    return result.toArray(new String[0]);
  }

  public static void main(String[] args) {
    ErdosNumber erdosNumber = new ErdosNumber();
    String[] publications = { "ERDOS B", "A B C", "B A E", "D F" };
    System.out.println(erdosNumber.calculateNumbers(publications));
  }

}

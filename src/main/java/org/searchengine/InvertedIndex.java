package org.searchengine;

import java.util.*;

public class InvertedIndex {
    private final Map<String, PostingList> index = new HashMap<>();
    private final Set<Integer> allDocs = new HashSet<>();

    public  void addDocumentToIndex(int docId, String text) {
        allDocs.add(docId);
        List<String> terms = Tokenizer.tokenize(text);
        Map<String, List<Integer>> positionMap = new HashMap<>();
        for(int i = 0; i < terms.size(); i++) {
            positionMap.computeIfAbsent(terms.get(i), k -> new ArrayList<>()).add(i);
        }
        for(Map.Entry<String, List<Integer>> e : positionMap.entrySet()) {
            PostingList postingList = index.computeIfAbsent(e.getKey(), k -> new PostingList());
            Posting posting = new Posting(docId, e.getValue());
            postingList.add(posting);
        }
    }

    public void finalizeIndex() {
        for(PostingList postingList : index.values()) {
            postingList.sortAndDedup();
            postingList.buildSkips();
        }
    }

    public PostingList getPostingList(String term) {
        return index.getOrDefault(term, new PostingList());
    }

    public List<Integer> getAllDocsSorted() {
        List<Integer> all = new ArrayList<>(allDocs);
        Collections.sort(all);
        return all;
    }

    // Returns the set of all terms in the inverted index.
    public Set<String> vocabulary() {
        return index.keySet();
    }

}
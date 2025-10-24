package org.searchengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * PostingList wraps an ordered list of Postings and supports skip-pointer bookkeeping.
 * skipIndex[i] = j means that from postings[i] we can skip to postings[j]
 */

public class PostingList {
    private final List<Posting> postings = new ArrayList<>();
    private int[] skipIndex = new int[0];

    public void add(int docId) {
        postings.add(new Posting(docId));
    }

    public void add(Posting p) {
        postings.add(p);
    }

    public List<Posting> getPostings() {
        return postings;
    }

    public int size() {
        return postings.size();
    }

    public Posting get(int i) {
        return postings.get(i);
    }

    public void sortAndDedup() {
        postings.sort(Comparator.comparingInt(Posting::getDocId));
        List<Posting> dedup = new ArrayList<>();
        Integer prev = null;
        for(Posting p : postings) {
            if(prev == null || p.getDocId() != prev) {
                dedup.add(p);
                prev = p.getDocId();
            }
        }
        postings.clear();
        postings.addAll(dedup);
    }

    public void buildSkips() {
        int n = postings.size();
        if(n == 0) {
            skipIndex = new int[0];
            return;
        }
        int skip = (int) Math.floor((Math.sqrt(n)));
        skipIndex = new int[n];
        Arrays.fill(skipIndex, -1);
        if(skip > 1) {
            for(int i = 0; i < n; i+=skip) {
                if(i + skip < n) {
                    skipIndex[i] = i + skip;
                }
            }
        }
    }

    public int getSkip(int idx) {
        if(skipIndex == null || idx < 0 || idx >= skipIndex.length) {
            return -1;
        }
        return skipIndex[idx];
    }

    @Override
    public String toString() {
        return postings.toString();
    }
}

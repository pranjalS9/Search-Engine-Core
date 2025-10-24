package org.searchengine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BooleanRetrievalTest {

    @Test
    void testAndOperation() {
        PostingList p1 = new PostingList();
        p1.add(1); p1.add(2); p1.add(4);
        p1.buildSkips();

        PostingList p2 = new PostingList();
        p2.add(2); p2.add(3); p2.add(4);
        p2.buildSkips();

        PostingList result = BooleanRetrieval.andOperation(p1, p2);
        assertEquals(List.of(2, 4), result.getPostings().stream().map(Posting::getDocId).toList());
    }

    @Test
    void testOrOperation() {
        PostingList p1 = new PostingList();
        p1.add(1); p1.add(3);
        PostingList p2 = new PostingList();
        p2.add(2); p2.add(3);
        PostingList result = BooleanRetrieval.orOperation(p1, p2);
        assertEquals(List.of(1, 2, 3), result.getPostings().stream().map(Posting::getDocId).toList());
    }

    @Test
    void testNotOperation() {
        PostingList all = new PostingList();
        all.add(1); all.add(2); all.add(3); all.add(4);

        PostingList subset = new PostingList();
        subset.add(2); subset.add(4);

        PostingList result = BooleanRetrieval.notOperation(all, subset);
        assertEquals(List.of(1, 3), result.getPostings().stream().map(Posting::getDocId).toList());
    }
}

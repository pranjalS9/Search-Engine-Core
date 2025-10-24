package org.searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvertedIndexTest {

    private InvertedIndex invertedIndex;

    @BeforeEach
    void setup() {
        invertedIndex = new InvertedIndex();
        invertedIndex.addDocumentToIndex(1, "new schizophrenia drug");
        invertedIndex.addDocumentToIndex(2, "breakthrough drug for schizophrenia");
        invertedIndex.addDocumentToIndex(3, "new drug trial starts");
        invertedIndex.finalizeIndex();
    }

    @Test
    void vocabularyShouldContainWords() {
        assertTrue(invertedIndex.vocabulary().contains("schizophrenia"));
        assertTrue(invertedIndex.vocabulary().contains("drug"));
        assertTrue(invertedIndex.vocabulary().contains("trial"));
    }

    @Test
    void shouldGiveCorrectPostingsForTerm() {
        List<Posting> schizophrenia = invertedIndex.getPostingList("schizophrenia").getPostings();
        assertEquals(List.of(1, 2), schizophrenia.stream().map(Posting::getDocId).toList());
    }

    @Test
    void shouldStorePositions() {
        Posting p = invertedIndex.getPostingList("drug").getPostings().get(0);
        assertNotNull(p.getPositions());
        assertFalse(p.getPositions().isEmpty());
    }

    @Test
    void shouldSortAllDocs() {
        assertEquals(List.of(1, 2, 3), invertedIndex.getAllDocsSorted());
    }
}

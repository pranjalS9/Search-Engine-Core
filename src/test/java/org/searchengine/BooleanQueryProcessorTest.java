package org.searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanQueryProcessorTest {
    private InvertedIndex index;
    private BooleanQueryProcessor processor;

    @BeforeEach
    void setup() {
        index = new InvertedIndex();
        index.addDocumentToIndex(1, "new schizophrenia drug");
        index.addDocumentToIndex(2, "breakthrough drug for schizophrenia");
        index.addDocumentToIndex(3, "new cancer drug trial");
        index.addDocumentToIndex(4, "cancer breakthrough treatment");
        index.finalizeIndex();
        processor = new BooleanQueryProcessor(index);
    }

    @Test
    void testAndQuery() {
        List<Integer> result = processor.evaluate("drug AND schizophrenia");
        assertEquals(List.of(1, 2), result);
    }

    @Test
    void testOrQuery() {
        List<Integer> result = processor.evaluate("schizophrenia OR cancer");
        assertEquals(List.of(1, 2, 3, 4), result);
    }

    @Test
    void testNotQuery() {
        List<Integer> result = processor.evaluate("NOT schizophrenia");
        assertEquals(List.of(3, 4), result);
    }

    @Test
    void testAndNotQuery() {
        List<Integer> result = processor.evaluate("drug AND NOT schizophrenia");
        assertEquals(List.of(3), result);
    }

    @Test
    void testChainOfOperations() {
        List<Integer> result = processor.evaluate("drug AND schizophrenia OR cancer");
        // Evaluate left-to-right: (drug AND schizophrenia) OR cancer → [1,2] OR [3,4] → [1,2,3,4]
        assertEquals(List.of(1, 2, 3, 4), result);
    }

    @Test
    void testUnknownTerm() {
        List<Integer> result = processor.evaluate("unknownTerm AND drug");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleWordQuery() {
        List<Integer> result = processor.evaluate("cancer");
        assertEquals(List.of(3, 4), result);
    }
}

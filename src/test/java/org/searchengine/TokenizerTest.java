package org.searchengine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenizerTest {

    @Test
    void shouldRemoveStopWords() {
        String text = "New schizophrenia drug for the patients";
        List<String> tokens = Tokenizer.tokenize(text);
        assertEquals(List.of("new", "schizophrenia", "drug", "patients"), tokens);
    }

    @Test
    void shouldReturnEmptyListIfTextHasOnlyStopwords() {
        String text = "the and or of for to with by that";
        List<String> tokens = Tokenizer.tokenize(text);
        assertTrue(tokens.isEmpty());
    }

    @Test
    void shouldReturnListIfTextIsEmpty() {
        List<String> tokens = Tokenizer.tokenize("");
        assertTrue(tokens.isEmpty());
    }

}

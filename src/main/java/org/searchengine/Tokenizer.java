package org.searchengine;

import java.util.*;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Pattern SPLIT = Pattern.compile("\\W+");
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList("a","an","the","is","are","was","were","in","on","at","and","or","not","of","for","to","with","by","that"));

    public Tokenizer(){}

    public static List<String> tokenize(String text) {
        if(text == null) return Collections.emptyList();
        String[] parts = SPLIT.split(text.toLowerCase(Locale.ROOT));
        List<String> out = new ArrayList<>();
        for(String p : parts) {
            if(p.isEmpty()) continue;
            if(STOPWORDS.contains(p)) continue;
            out.add(p);
        }
        return out;
    }
}

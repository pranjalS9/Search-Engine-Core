package org.searchengine;

public class Main {
    public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();
        index.addDocumentToIndex(1, "new schizophrenia drug");
        index.addDocumentToIndex(2, "breakthrough drug for schizophrenia");
        index.finalizeIndex();

        BooleanQueryProcessor qp = new BooleanQueryProcessor(index);

        System.out.println(qp.evaluate("schizophrenia AND drug")); // should match [1,2]
        System.out.println(qp.evaluate("schizophrenia AND NOT drug")); // []
        System.out.println(qp.evaluate("drug OR new")); // [1,2]
    }
}
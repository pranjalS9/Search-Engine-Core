package org.searchengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Posting {
    private final int docId;
    private final List<Integer> positions;

    public Posting(int docId) {
        this.docId = docId;
        this.positions = null;
    }

    public Posting(int docId, List<Integer> positions) {
        this.docId = docId;
        if (positions == null) this.positions = new ArrayList<>();
        else this.positions = new ArrayList<>(positions);
    }

    public int getDocId() {
        return docId;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void addPosition(int p) {
        assert positions != null;
        positions.add(p);
    }

    /*
    Example documents:
    D1: "new schizophrenia drug"
    D2: "breakthrough drug for schizophrenia"
    Inverted index:
    "schizophrenia" → [ Posting(docId=1, positions=[1]), Posting(docId=2, positions=[3]) ]
    "drug"          → [ Posting(docId=1, positions=[2]), Posting(docId=2, positions=[2]) ]
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Posting posting)) return false;
        return docId == posting.docId; // returns posting as equal if the docIds are same regardless of positions
    }


    @Override
    public int hashCode() { return Objects.hash(docId); }


    @Override
    public String toString() { return Integer.toString(docId); }
}

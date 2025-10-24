package org.searchengine;

import java.util.ArrayList;
import java.util.List;

public class BooleanRetrieval {
    public static PostingList andOperation(PostingList p1, PostingList p2) {
        List<Posting> postings_1 = p1.getPostings();
        List<Posting> postings_2 = p2.getPostings();

        List<Posting> result = new ArrayList<>();
        int i = 0, j = 0;

        while(i < postings_1.size() && j < postings_2.size()) {
            int docId_1 = postings_1.get(i).getDocId();
            int docId_2 = postings_2.get(j).getDocId();
            if(docId_1 == docId_2) {
                result.add(postings_1.get(i));
                i++;
                j++;
            } else if(docId_1 < docId_2) {
                int skip = p1.getSkip(i);
                if(skip != -1 && postings_1.get(skip).getDocId() <= docId_2) {
                    i = skip;
                } else {
                    i++;
                }
            } else {
                int skip = p2.getSkip(j);
                if (skip != -1 && postings_2.get(skip).getDocId() <= docId_1) {
                    j = skip;
                } else {
                    j++;
                }
            }
        }

        PostingList res = new PostingList();
        for (Posting p : result) {
            res.add(p);
        }
        return res;
    }

    public static PostingList notOperation(PostingList allDocs, PostingList p) {
        List<Posting> result = new ArrayList<>();
        List<Posting> listAll = allDocs.getPostings();
        List<Posting> listP = p.getPostings();

        int i = 0, j = 0;
        while (i < listAll.size()) {
            if (j >= listP.size()) {
                result.add(listAll.get(i++));
            } else {
                int doc1 = listAll.get(i).getDocId();
                int doc2 = listP.get(j).getDocId();
                if (doc1 == doc2) {
                    i++; j++;
                } else if (doc1 < doc2) {
                    result.add(listAll.get(i++));
                } else {
                    j++;
                }
            }
        }

        PostingList res = new PostingList();
        result.forEach(res::add);
        return res;
    }

    public static PostingList orOperation(PostingList p1, PostingList p2) {
        List<Posting> postings_1 = p1.getPostings();
        List<Posting> postings_2 = p2.getPostings();

        List<Posting> result = new ArrayList<>();
        int i = 0, j = 0;

        while(i < postings_1.size() || j < postings_2.size()) {
            if(i >= postings_1.size()) {
                result.add(postings_2.get(j++));
            } else if(j >= postings_2.size()) {
                result.add(postings_1.get(i++));
            } else {
                int docId_1 = postings_1.get(i).getDocId();
                int docId_2 = postings_2.get(j).getDocId();
                if (docId_1 == docId_2) {
                    result.add(postings_1.get(i));
                    i++;
                    j++;
                } else if (docId_1 < docId_2) {
                    result.add(postings_1.get(i++));
                } else {
                    result.add(postings_2.get(j++));
                }
            }
        }

        PostingList res = new PostingList();
        for (Posting p : result) {
            res.add(p);
        }
        return res;
    }
}

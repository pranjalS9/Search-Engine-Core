package org.searchengine;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class BooleanQueryProcessor {
    private final InvertedIndex invertedIndex;

    public BooleanQueryProcessor(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public List<Integer> evaluate(String query) {
        String[] tokens = query.toLowerCase().split("\\s+");
        Stack<PostingList> valuesStack = new Stack<>();
        Stack<String> opsStack = new Stack<>();

        for (String token : tokens) {
            switch (token) {
                case "and":
                case "or":
                case "not":
                    opsStack.push(token);
                    break;
                default:
                    PostingList postingList = invertedIndex.getPostingList(token);
                    if (!opsStack.isEmpty() && opsStack.peek().equals("not")) {
                        opsStack.pop();
                        PostingList all = new PostingList();
                        for (Integer docId : invertedIndex.getAllDocsSorted()) {
                            all.add(new Posting(docId));
                        }
                        postingList = BooleanRetrieval.notOperation(all, postingList);
                    }
                    valuesStack.push(postingList);
                    break;
            }

            if (valuesStack.size() >= 2 && !opsStack.isEmpty() && (opsStack.peek().equals("and") || opsStack.peek().equals("or"))) {
                PostingList right = valuesStack.pop();
                PostingList left = valuesStack.pop();
                String op = opsStack.pop();
                PostingList result = op.equals("and") ? BooleanRetrieval.andOperation(left, right) : BooleanRetrieval.orOperation(left, right);
                valuesStack.push(result);
            }
        }

        if (valuesStack.isEmpty()) return Collections.emptyList();
        return valuesStack.pop().getPostings().stream().map(Posting::getDocId).collect(Collectors.toList());
    }
}
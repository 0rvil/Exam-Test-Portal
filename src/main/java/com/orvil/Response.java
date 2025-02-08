package com.orvil;

import java.util.ArrayList;

import com.orvil.JSONResponseObjects.Candidate;

// The classes Response, Candidate, Content, Part, and QuestionAnswerPair are
// helper Classes for converting the minimum necessary JSON from the GEMINI LLM response into managable Java Class Objects
public class Response {
    private ArrayList<Candidate> candidates;

    public Candidate getCandidate(int i) {
        return candidates.get(i);
    }
}

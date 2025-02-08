package com.orvil.JSONResponseObjects;

import java.util.ArrayList;

public class Content {
    private ArrayList<Part> parts;

    public Part getPart(int i) {
        return parts.get(i);
    }
}

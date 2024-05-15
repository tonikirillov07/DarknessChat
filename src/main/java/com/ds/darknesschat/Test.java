package com.ds.darknesschat;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println(FuzzySearch.ratio("1", "2"));
    }

}

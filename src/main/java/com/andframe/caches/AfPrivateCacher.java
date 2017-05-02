package com.andframe.caches;

import com.andframe.application.AfApp;

public class AfPrivateCacher extends AfJsonCache {

    private static final String CACHE_NAME = "private";

    public AfPrivateCacher() {
        super(AfApp.get(), CACHE_NAME);
    }

    public AfPrivateCacher(String name) {
        super(AfApp.get(), name);
    }

}

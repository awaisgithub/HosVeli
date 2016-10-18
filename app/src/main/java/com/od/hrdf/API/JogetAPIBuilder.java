package com.od.hrdf.API;

import android.net.Uri;

/**
 * Created by Awais on 10/12/2016.
 */

public class JogetAPIBuilder {
    private Uri.Builder uriBuilder;

    public JogetAPIBuilder(String scheme, String hostAddress, String plugin) throws IllegalArgumentException {
        if (scheme == null) {
            throw new IllegalArgumentException("Joget plugin scheme missing.");
        }

        if (hostAddress == null) {
            throw new IllegalArgumentException("Joget plugin host address missing.");
        }

        if (plugin == null) {
            throw new IllegalArgumentException("Joget plugin name missing.");
        }

        uriBuilder = new Uri.Builder();
        uriBuilder.scheme(scheme).encodedAuthority(hostAddress);
        uriBuilder.appendPath("jw");
        uriBuilder.appendPath("web");
        uriBuilder.appendPath("json");
        uriBuilder.appendPath("plugin");
        uriBuilder.appendPath(plugin);
        uriBuilder.appendPath("service");
    }

    public JogetAPIBuilder appId(String appId) {
        this.uriBuilder.appendQueryParameter("appId", appId);
        return this;
    }

    public JogetAPIBuilder listId(String listId) {
        this.uriBuilder.appendQueryParameter("listId", listId);
        return this;
    }

    public JogetAPIBuilder action(String action) {
        String listAction = action != null ? action : "list";
        this.uriBuilder.appendQueryParameter("action", listAction);
        return this;
    }

    public JogetAPIBuilder primaryFilter(String name, String value) {
        this.uriBuilder.appendQueryParameter("filter1Column", name);
        this.uriBuilder.appendQueryParameter("filter1Value", value);
        return this;
    }

    public JogetAPIBuilder secondaryFilter(String name, String value) {
        this.uriBuilder.appendQueryParameter("filter2Column", name);
        this.uriBuilder.appendQueryParameter("filter2Value", value);
        return this;
    }

    public JogetAPIBuilder willIncludeImages(boolean includeImages) {
        String shouldIncludeImages = includeImages ? "Yes" : "No";
        this.uriBuilder.appendQueryParameter("imageUrl", shouldIncludeImages);
        return this;
    }

    public JogetAPIBuilder willIncludeFiles(boolean includeFiles) {
        String shouldIncludeFiles = includeFiles ? "Yes" : "No";
        this.uriBuilder.appendQueryParameter("fileUrl", shouldIncludeFiles);
        return this;
    }

    public JogetAPIBuilder appendQueryParameter(String name, String value) {
        this.uriBuilder.appendQueryParameter(name, value);
        return this;
    }

    public Uri build() {
        return uriBuilder.build();
    }
}

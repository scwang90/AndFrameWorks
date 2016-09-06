package com.nostra13.universalimageloader.cache.disc.impl.ext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created by SCWANG on 2016/8/17.
 */
public class ApDiskCache extends LruDiskCache {

    public ApDiskCache(File cacheDir, FileNameGenerator fileNameGenerator, long cacheMaxSize) throws IOException {
        super(cacheDir, fileNameGenerator, cacheMaxSize);
    }

    public Bitmap getBitmap(String imageUri) {
        try {
            DiskLruCache.Snapshot snapshot = cache.get(getKey(imageUri));
            if (snapshot == null) return null;
            try {
                return BitmapFactory.decodeStream(snapshot.getInputStream(0));
            } finally {
                snapshot.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void put(String imageUri, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        try {
            save(imageUri, isBm, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(baos);
            Util.closeQuietly(isBm);
        }
    }

    public long size() {
        return cache.size();
    }

    private String getKey(String imageUri) {
        return fileNameGenerator.generate(imageUri);
    }
}

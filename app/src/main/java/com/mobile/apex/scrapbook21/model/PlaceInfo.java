package com.mobile.apex.scrapbook21.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by malvin on 30/03/18.
 */

public class PlaceInfo {

    private String name;
    private String address;
    private String phoneNumber;
    private String id;
    private Uri websiteUri;
    private LatLng latlng;
    private float rating;
    private String attributions;
    private String photoRef;

    private static final String PHOTOREF_PREFIX = "BMP_";
    private static final String PHOTOREF_SUFFIX = ".bmp";
    private String mRefPhotoPath;

    public PlaceInfo(String name, String address, String phoneNumber, String id, Uri websiteUri,
                     LatLng latlng, float rating, String attributions, String photoRef)
    {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.latlng = latlng;
        this.rating = rating;
        this.attributions = attributions;
        this.photoRef = photoRef;
    }

    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latlng=" + latlng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }

    /**
    public Bitmap getPhotoRef() {
        return photoRef;
    }
     */

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(Bitmap photoRef) throws IOException
    {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/reference_images");
        myDir.mkdirs();

        String imageFileName = PHOTOREF_PREFIX + "_";
        //File image = File.createTempFile(imageFileName,PHOTOREF_SUFFIX,myDir);

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String image = imageFileName + n + PHOTOREF_SUFFIX;

        //Save a file: path for use with ACTION_VIEW intents
        // = image.getAbsolutePath();

        File file = new File (myDir, image);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            photoRef.compress(Bitmap.CompressFormat.JPEG, 90, out);
            //this.photoRef = photoRef;
            out.flush();
            //out.close();
          } catch (Exception e) {
          e.printStackTrace();
          }
        //this.photoRef = photoRef;
    }
}

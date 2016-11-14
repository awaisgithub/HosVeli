package com.od.hrdf.BOs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.abouts.AboutUs;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

import static android.R.attr.path;
import static com.od.hrdf.HRDFApplication.context;
import static com.od.hrdf.HRDFApplication.realm;


/**
 * Created by Awais on 10/10/2016.
 */

public class Article extends RealmObject {
    @PrimaryKey
    private String id;

    private Date startDate;
    private String socialMediaMessage;
    private String socialMediaImage;
    private Date endDate;
    private boolean featured = false;
    private String from;
    private String url;
    private String title;
    private String category;
    private String articleSummaryImage;
    private String articleContentImage;
    private boolean enableSocialMediaSharing = false;
    private String description;
    private String active;
    private String sortingSequence;
    boolean isObsolete;

    public Article() {
    }

    public String getArticleSummaryImage() {
        return articleSummaryImage;
    }

    public void setArticleSummaryImage(String articleSummaryImage) {
        this.articleSummaryImage = articleSummaryImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSocialMediaMessage() {
        return socialMediaMessage;
    }

    public void setSocialMediaMessage(String socialMediaMessage) {
        this.socialMediaMessage = socialMediaMessage;
    }

    public String getSocialMediaImage() {
        return socialMediaImage;
    }

    public void setSocialMediaImage(String socialMediaImage) {
        this.socialMediaImage = socialMediaImage;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArticleContentImage() {
        return articleContentImage;
    }

    public void setArticleContentImage(String articleContentImage) {
        this.articleContentImage = articleContentImage;
    }

    public boolean isEnableSocialMediaSharing() {
        return enableSocialMediaSharing;
    }

    public void setEnableSocialMediaSharing(boolean enableSocialMediaSharing) {
        this.enableSocialMediaSharing = enableSocialMediaSharing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getSortingSequence() {
        return sortingSequence;
    }

    public void setSortingSequence(String sortingSequence) {
        this.sortingSequence = sortingSequence;
    }

    public boolean isObsolete() {
        return isObsolete;
    }

    public void setObsolete(boolean obsolete) {
        isObsolete = obsolete;
    }

    //METHODS

    public static Article getArticle(Realm realm, String id) {
        return realm.where(Article.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Article> getArticlesResultController(Realm realm) {
        Date today = new Date();
        return realm.where(Article.class).lessThanOrEqualTo("startDate", today).greaterThanOrEqualTo("endDate", today)
                .equalTo("active", "Yes").equalTo("isObsolete", false)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static void fetchArticles(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(HRDFConstants.TAG, "fetchArticles = " + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                realm.beginTransaction();
                                try {
                                    Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
                                    Type collectionType = new TypeToken<ArrayList<Article>>() {
                                    }.getType();
                                    ArrayList<Article> articles = gson.fromJson(response.toString(), collectionType);
                                    if (articles.size() > 0)
                                        makeAllArticlesObsolete(realm);

                                    for (Article article : articles) {
                                        Article localArticle = Article.getArticle(realm, article.getId());
                                        if (localArticle != null) {
                                            article.setObsolete(false);
                                        } else {
                                        }
                                        realm.copyToRealmOrUpdate(article);
                                    }
                                    deleteAllObsoleteArticles(realm);
//                                    realm.copyToRealmOrUpdate(articles);//Article.class, response);
                                    RealmResults articlesResultSet = query.findAll();
                                    callBack.fetchDidSucceed(articlesResultSet);
                                } catch (Exception e) {
                                    Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
                                    callBack.fetchDidFail(e);
                                }
                                realm.commitTransaction();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        // Adding request to request queue
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    public static void makeAllArticlesObsolete(Realm realm) {
        RealmResults allArticle = realm.where(Article.class).findAll();
        for (int i = 0; i < allArticle.size(); i++) {
            Article article = (Article) allArticle.get(i);
            article.setObsolete(true);
        }
    }

    public static void deleteAllObsoleteArticles(Realm realm) {
        RealmResults obsoleteArticle = realm.where(Article.class).equalTo("isObsolete", true).findAll();
        for (int i = 0; i < obsoleteArticle.size(); i++) {
            Article article = (Article) obsoleteArticle.get(i);
            article.deleteFromRealm();
        }
    }

}

package com.tec.pierre.laboratorio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private final static String LINK = "http://10.0.2.2:3000/";

  private ListView lista;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    lista = findViewById(R.id.lista);

    Thread thread = new Thread(LINK, this);
    thread.execute();

  }


  private class Thread extends AsyncTask<Void, Void, Void> {

    private String[] post = {"books.json", "authors.json"};

    private Context context;

    private String link;

    private URL urlBook;
    private URL urlAuthor;

    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Author> authors = new ArrayList<>();

    public Thread(String link, Context context) {
      this.link = link;
      this.context = context;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      if(!isNetworkConnected()) {
        cancel(true);
      }
      else {
        try {
          urlBook = new URL(link + post[0]);
          urlAuthor = new URL(link + post[1]);
        }
        catch (MalformedURLException e) {
          cancel(true);
        }
      }
    }

    @Override
    protected Void doInBackground(Void... voids) {
      String stringAuthors = getAuthors();
      String stringBooks = getBooks();
      try {
        JSONArray jsonAuthors = new JSONArray(stringAuthors);
        JSONArray jsonBooks = new JSONArray(stringBooks);
        for(int i = 0; i <  jsonAuthors.length(); i++) {
          JSONObject jsonObject = jsonAuthors.getJSONObject(i);
          Author author = new Author(jsonObject.getString("name"), jsonObject.getString("lastname"), jsonObject.getInt("id"));
          authors.add(author);
        }
        for(int i = 0; i < jsonBooks.length(); i++) {
          JSONObject jsonObject = jsonBooks.getJSONObject(i);
          Author author = searchAuthor(jsonObject.getInt("author_id"));
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
          String stringDate = jsonObject.getString("publish_date");
          Date date = formatter.parse(stringDate);
          Book book = new Book(author, jsonObject.getString("title"), date);
          books.add(book);
        }
      }
      catch (JSONException e) {
        e.printStackTrace();
      }
      catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
     //Añadir libros a la vista
      lista = findViewById(R.id.lista);
      ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(context, android.R.layout.simple_list_item_1, books);
      lista.setAdapter(adapter);
    }

    private boolean isNetworkConnected() {
      ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      return cm.getActiveNetworkInfo() != null;
    }

    private Author searchAuthor(int id) {
      for(int i = 0; i < authors.size(); i++) {
        Author author = authors.get(i);
        if(author.getId() == id) {
          return author;
        }
      }
      return null;
    }

    private String getAuthors() {
      return getContent(urlAuthor);
    }

    private String getBooks() {
      return getContent(urlBook);
    }

    private String getContent(URL url) {
      String content = "";
      HttpURLConnection connection = null;
      try {
        connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
          content += line + "\n";
        }
        return content;
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      return content;
    }

  }

}

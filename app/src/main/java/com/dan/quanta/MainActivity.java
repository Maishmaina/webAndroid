package com.dan.quanta;



import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private WebView webView;
private long backPressMessage;
private Toast backToast;
private ProgressDialog prodag;
String url="https://quantatechagencies.com";
SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=(WebView)findViewById(R.id.mywebview);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        prodag= new ProgressDialog(MainActivity.this);
        prodag.setTitle("QUANTA SHOP CENTER");
        prodag.setMessage("Loading...Please wait");
        webView.setWebViewClient(new QuantaLoad(prodag));
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
        swipeRefreshLayout.setRefreshing(true);
        //swipe to reload web page in case of error
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });
    }
    //used to create preloader
    public class QuantaLoad extends WebViewClient{
        ProgressDialog prodag;
        public QuantaLoad(ProgressDialog prodag) {
            this.prodag=prodag;
            prodag.show();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (prodag.isShowing()){
                prodag.dismiss();
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //super.onReceivedError(view, errorCode, description, failingUrl);
            webView.loadUrl("file:///android_asset/androidError.html");
        }
    }
//used to handle onback press and exit.
 @Override
    public void onBackPressed(){
        if (webView.canGoBack()){
          webView.goBack();
        }else{

            if (backPressMessage + 2000 >System.currentTimeMillis()){
                backToast.cancel();
                super.onBackPressed();
                return;
            }else{
                backToast =Toast.makeText(getBaseContext(),"Please press again to exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressMessage= System.currentTimeMillis();

        }
 }
}


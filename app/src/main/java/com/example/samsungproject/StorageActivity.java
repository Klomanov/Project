package com.example.samsungproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class StorageActivity extends Activity {
    View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = View.inflate(getApplicationContext(), R.layout.storage, null);
        Catalog catalog = mainView.findViewById(R.id.catalog);
        catalog.init(this);
        setContentView(mainView);
        findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void updateListView(){
        Catalog catalog = mainView.findViewById(R.id.catalog);
        catalog.init(this);
    }
}


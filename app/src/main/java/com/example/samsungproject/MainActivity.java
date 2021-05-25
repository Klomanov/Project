package com.example.samsungproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.samsungproject.CircuitElements.CircuitElement;
import com.example.samsungproject.CircuitElements.Nothing;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class MainActivity extends AppCompatActivity {

     ListItem[] listItems = new ListItem[Dictionary.AMOUNT_ELEMENTS];
     boolean buildMode = false;

     MainView mainView;

     Bitmap resistorIcon, wireIcon, rWireIcon, tWireIcon, powerIcon, keyIcon, bulbIcon;

     DBHelper dbHelper;

     AlertDialog createDialog;

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         if (item.getItemId() == R.id.action_save){
             View view = View.inflate(mainView.getContext(), R.layout.save_schema, null);
             AlertDialog alertDialog = createAlertWindow(mainView.getContext(), view);
             view.findViewById(R.id.save_schema_button).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     ContentValues contentValues = new ContentValues();

                     EditText editText = view.findViewById(R.id.schema_name_edit);
                     String name = editText.getText().toString();

                     String field = serializeField();

                     contentValues.put(DBHelper.COLUMN_NAME, name);
                     contentValues.put(DBHelper.COLUMN_FIELD, field);

                     Dictionary.database.insert(DBHelper.TABLE_NAME, null, contentValues);

                     Toast toast = Toast.makeText(view.getContext(), R.string.saved, Toast.LENGTH_SHORT);
                     toast.show();
                     alertDialog.dismiss();
                 }
             });

         }
         if (item.getItemId() == R.id.action_createSchema){
             int sizeY = Dictionary.field.length;
             int sizeX = Dictionary.field[0].length;
             for(int y = 0; y < sizeY; y++){
                 for(int x = 0; x < sizeX; x++){
                     Constructor.createNothing(x, y);
                 }
             }
             mainView.invalidate();
             mainView.removeButtons();
         }
         if (item.getItemId() == R.id.action_catalog){
   //          if (DatabaseUtils.queryNumEntries(Dictionary.database, DBHelper.TABLE_NAME) != 0) {
                 Intent i = new Intent(this, StorageActivity.class);
                 startActivity(i);
  //           }
//             else{
//                 AlertDialog.Builder builder = new AlertDialog.Builder(mainView.getContext());
//                 builder.setTitle("Warning!")
//                         .setMessage("Add Schema to continue!")
//                         .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                             @Override
//                             public void onClick(DialogInterface dialog, int which) {
//                                 dialog.cancel();
//                             }
//                         });
//                 builder.create().show();
//             }
         }

         return true;
     }

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.main);
         Dictionary.initImages(getResources());
         initBuilderList();
//         deleteDatabase(DBHelper.DATABASE_NAME);


//         CircuitElement element = new Nothing(4, 4);
//         String toJson = new Gson().toJson(element);
//         CircuitElement element1 = new Gson().fromJson(toJson, CircuitElement.class);


         dbHelper = new DBHelper(this);

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         Dictionary.database = dbHelper.getWritableDatabase();
//         database.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_NAME +  "=" + "test", null);


         mainView = findViewById(R.id.mainView7);
         FloatingActionButton create = findViewById(R.id.create);

         MainActivity activity = this;
         create.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!buildMode) {

                     Context context = mainView.getContext();
                     ListView listView = new ListView(context);
                     CreateListAdapter listAdapter = new CreateListAdapter(context, listItems, activity);

                     listView.setAdapter(listAdapter);

                     createDialog = createAlertWindow(context, listView);
                 }

                 if (buildMode){
                     create.setImageResource(R.drawable.create);
                     create.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.createColor)));
                     Dictionary.createType = -1;
                     buildMode = false;
                 }
             }
         });
     }


         void initBuilderList () {

             resistorIcon = Bitmap.createScaledBitmap(Dictionary.RESISTOR_IMAGE,
                     2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     false);

             wireIcon = Bitmap.createScaledBitmap(Dictionary.WIRE_IMAGE,
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     false);

             rWireIcon = Bitmap.createScaledBitmap(Dictionary.R_WIRE_IMAGE,
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     false);

             tWireIcon = Bitmap.createScaledBitmap(Dictionary.T_WIRE_IMAGE,
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     false);

             powerIcon = Bitmap.createScaledBitmap(Dictionary.POWER_IMAGE, 2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     3 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     false);

             keyIcon = Bitmap.createScaledBitmap(Dictionary.KEY_OFF_IMAGE,
                     2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     false);

             bulbIcon = Bitmap.createScaledBitmap(Dictionary.BULB_OFF_IMAGE,
                     2 * (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     (Dictionary.SQRSIZE + Dictionary.SQRWIDTH),
                     false);

             String[] names = {getString(R.string.resistor), getString(R.string.wire), getString(R.string.rWire), getString(R.string.tWire), getString(R.string.power), getString(R.string.key), getString(R.string.bulb)};
             Bitmap[] images = {resistorIcon, wireIcon, rWireIcon, tWireIcon, powerIcon, keyIcon, bulbIcon};

             for (int i = 0; i < Dictionary.AMOUNT_ELEMENTS; i++) {
                 listItems[i] = new ListItem(names[i], images[i]);
             }
         }

         public static String serializeField(){
//             ByteArrayOutputStream b = new ByteArrayOutputStream();
//             try {
//                 ObjectOutputStream o = new ObjectOutputStream(b);
//                 o.writeObject(Dictionary.field);
//             }
//             catch (IOException e){
//                 e.printStackTrace();
//             }
//             return b.toByteArray();
             return new Gson().toJson(Dictionary.field);
         }



         public AlertDialog createAlertWindow(Context context, View view){
             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
             alertDialogBuilder.setView(view);
             AlertDialog alertDialog = alertDialogBuilder.create();
             alertDialog.show();
             return alertDialog;
         }

        @Override
        protected void onResume() {
            super.onResume();
            if (Dictionary.jsonField != null){
                mainView.updateField(Dictionary.jsonField);
                Dictionary.jsonField = null;
            }
        }
}

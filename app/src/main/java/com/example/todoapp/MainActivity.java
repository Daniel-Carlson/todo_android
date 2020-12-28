package com.example.todoapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText edtItems;
    RecyclerView revView;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edtItems = findViewById(R.id.edtItems);
        revView = findViewById(R.id.revView);





        loadItems();

        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener(){
            @Override
            //here we have the position when an item is long pressed
            public void onItemLongClicked(int position) {
                // delete the item from the model
                items.remove(position);
                // notify the adapter where to delete
                itemAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Task Removed", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        ItemAdapter.OnClickListener onClickListener = new ItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);
                // create new activity using intents
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass relative data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemAdapter = new ItemAdapter(items, onLongClickListener, onClickListener);
        revView.setAdapter(itemAdapter);
        revView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = edtItems.getText().toString();
                // Add the item to the model
                items.add(todoItem);
                // Notify the adapter that an item is inserted
                itemAdapter.notifyItemInserted(items.size() -1);
                edtItems.setText("");
                Toast.makeText(getApplicationContext(),"Task Inserted", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // retreive updated text
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract original positon of the edited item from the key position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the model at the right position with the new text
            items.set(position,itemText);
            //notify the adapter
            itemAdapter.notifyItemChanged(position);
            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(),"Task Edited", Toast.LENGTH_LONG).show();

        }else {
            Log.w("MainActivity", "error when editing");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //This Function will load items by reading every item of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "error reading items", e);
            items = new ArrayList<>();
        }
    }
        // This function will write into the file
        private void saveItems() {
            try {
                FileUtils.writeLines(getDataFile(), items);
            } catch (IOException e) {
                Log.e("MainActivity", "error writing items", e);
            }
        }
}
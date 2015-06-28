

package com.warmani.myapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class MainActivity extends ActionBarActivity {

    Bitmap bmp=null;


    Thread thread;

    Boolean t=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


        Log.i("MyTag", "onCreate");
    }

    protected void onActivityResult(int resuestcode, int resultcode, Intent data) {
        super.onActivityResult(resuestcode, resultcode, data);
        try {
            if (resuestcode == 1 && resultcode == RESULT_OK && null != data) {
                {thread = new Thread(new Runnable() {
                    public void run() {
                        if (bmp != null) System.out.println("Yes its not null");
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        final byte[] bytearray = os.toByteArray();
                        HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M", true, true);
                        PostParameters postParameters =
                                new PostParameters()
                                        .setImg(bytearray)
                                        .setAttribute("all");
                        try {
                            JSONObject result = httpRequests.detectionDetect(postParameters);
                            JSONArray face = result.getJSONArray("face");
                            String smile_value = null;
                            String age_value = null;
                            String gender_value = null;
                            String age_value_r = null;
                            String smile_value_string=null;
                            final int age_value_r_int;
                            boolean smile_value_int_bool=false;
                            double smile_value_int;
                            final int age_value_int;
                            System.out.println(face);
                            if (face == null) {
                                Toast.makeText(MainActivity.this, "Picture not detected! Please use another one.", Toast.LENGTH_LONG).show();
                            } else {
                                smile_value = face.getJSONObject(0).getJSONObject("attribute").getJSONObject("smiling").getString("value");
                                age_value = face.getJSONObject(0).getJSONObject("attribute").getJSONObject("age").getString("value");
                                age_value_r = face.getJSONObject(0).getJSONObject("attribute").getJSONObject("age").getString("range");
                                gender_value = face.getJSONObject(0).getJSONObject("attribute").getJSONObject("gender").getString("value");
                                //result.getJSONArray("face").getJSONObject(0).getString("smile_value");
                                smile_value_int=Double.parseDouble(smile_value);
                                age_value_int = Integer.parseInt(age_value);
                                age_value_r_int=Integer.parseInt(age_value_r);
                                if(smile_value_int >40){
                                    smile_value_int_bool=true;
                                    smile_value_string="You're smile is good \n";
                                }
                                else{
                                    smile_value_string="You don't seem to laugh as I do";
                                }
                                final String finalSmile_value_string = smile_value_string;
                                if(smile_value_int_bool) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=F3EG4olrFjY&list=PLiHhCX0TvCttUhZ0wslA_iiz37Y1RtxTV"));
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=nOfGAMhA4GI&list=PLuYC9zQ9gzT2TAV83g79AROo1s-SaOa3W&index=3"));
                                    startActivity(intent);
                                }
                            }



                        } catch (FaceppParseException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                }

                Uri selectedimage = data.getData();
                String[] filepathcolumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedimage, filepathcolumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filepathcolumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                bmp= BitmapFactory.decodeFile(picturePath);
                imageView.setImageBitmap(bmp);



                thread.start();



            } else {
                Toast.makeText(this, "You haven't picked image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onResume(){
        super.onResume();
            Log.i("MyTag", "true");
            Log.i("MyTag", "onResume");
            /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM"));
            startActivity(intent);*/


    }
    public void onPause(){
        super.onPause();
        Log.i("MyTag","onPause");
    }
    public void onStop(){
        super.onStop();
        Log.i("MyTag","onStop");
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

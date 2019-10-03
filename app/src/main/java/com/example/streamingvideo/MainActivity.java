package com.example.streamingvideo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.view.View;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="VideoViewer";
    public DataInputStream dis;
    ImageView imageView;
    public int len;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView) findViewById(R.id.video_frame);



        try{
            ServerSocket serverSocket=new ServerSocket(8000);
            Socket video_receiver=serverSocket.accept();
            //Log.d(TAG,"Connection completed");

            while (true){
                DataInputStream dis=new DataInputStream(video_receiver.getInputStream());
                len=Integer.parseInt(""+dis.readInt());
                byte [] buffer=new byte[len];
                dis.readFully(buffer,0,len);
                frame2video f2v=new frame2video();
                f2v.execute(buffer);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

 class frame2video extends AsyncTask<byte[],Void, Bitmap>{


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }
    protected Bitmap doInBackground(byte[]...buffer){
        try{
            Bitmap image= BitmapFactory.decodeByteArray(buffer[0],0, buffer[0].length);
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            outputStream.flush();
            image.compress(Bitmap.CompressFormat.PNG,85,outputStream);
            outputStream.close();
            return image;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }


    public void onPostExecute(Bitmap bitmap,ImageView imageView) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
        imageView.invalidate();
    }
}
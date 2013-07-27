package com.unict.geophoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final int ACTION_TAKE_PHOTO = 1;
	private ImageView imageView;
	private Bitmap imageBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = (ImageView) findViewById(R.id.imageViewPhoto);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// If the request went well (OK)
		switch (requestCode) {
		case ACTION_TAKE_PHOTO: {
			if (resultCode == Activity.RESULT_OK) {
				Bundle extras = intent.getExtras();
				imageBitmap = (Bitmap) extras.get("data");
				imageView.setImageBitmap(imageBitmap);
			}
		}
		}
	}

	/** Called when the user clicks the photo button */
	public void takePhoto(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
	}

	/** Called when the user clicks the location button */
	public void takeLocation(View view) {

	}

	/** Called when the user clicks the send button */
	public void sendData(View view) {

	}

}

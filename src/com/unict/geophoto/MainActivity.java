package com.unict.geophoto;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int ACTION_TAKE_PHOTO = 1;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private File imagePath;
	private String location = "";
	private String timestamp = "";
	private ImageView imageView;
	private TextView textLocation;
	private TextView textDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = (ImageView) findViewById(R.id.imageViewPhoto);
		textLocation = (TextView) findViewById(R.id.textLocation);
		textDate = (TextView) findViewById(R.id.textDate);
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
		switch (requestCode) {
		case ACTION_TAKE_PHOTO: {
			if (resultCode == Activity.RESULT_OK) {
				// updateImage();
				updateImage();
				timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
						Locale.getDefault()).format(new Date());
				updateDate();
			}
		}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d("GeoPhoto", outState.toString());
		outState.putString("location", this.location);
		outState.putString("timestamp", this.timestamp);
		if (this.imagePath != null) {
			outState.putString("path", this.imagePath.getAbsolutePath());
		} else {
			outState.putString("path", null);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateImage();
		updateLocation();
		updateDate();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		this.location = savedInstanceState.getString("location");
		this.timestamp = savedInstanceState.getString("timestamp");
		String path = savedInstanceState.getString("path");
		if (path != null) {
			this.imagePath = new File(path);
		} else {
			this.imagePath = null;
		}
	}

	/** Called when the user clicks the photo button */
	public void takePhoto(View view) {
		try {
			imagePath = createImageFile();
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(imagePath));
			startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
		} catch (Exception e) {
			e.printStackTrace();
			imagePath = null;
		}
	}

	/** Called when the user clicks the location button */
	public void takeLocation(View view) {

	}

	/** Called when the user clicks the send button */
	public void sendData(View view) {

	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				getAlbumDir());
		return image;
	}

	private File getAlbumDir() {
		File albumDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getString(R.string.album_name));
		if (albumDir != null) {
			if (!albumDir.mkdirs()) {
				if (!albumDir.exists()) {
					Log.d("GeoPhoto", "failed to create directory");
					return null;
				}
			}
		}
		return albumDir;
	}

	private void updateImage() {
		// imageView.setVisibility(View.INVISIBLE);
		if (imagePath != null) {
			// retrieve sizes
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagePath.getAbsolutePath(), opts);
			final int height = opts.outHeight;
			final int width = opts.outWidth;
			final int reqHeight = imageView.getHeight();
			final int reqWidth = imageView.getWidth();
			// calculate sample ratios
			int inSampleSize = 1;
			if (height > reqHeight || width > reqWidth) {
				// Calculate ratios of height and width to requested height and
				// width
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width
						/ (float) reqWidth);

				// Choose the smallest ratio as inSampleSize value, this will
				// guarantee a final image with both dimensions larger than or
				// equal to the requested height and width.
				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;
			}
			// load and show photo
			BitmapFactory.Options opts2 = new BitmapFactory.Options();
			opts2.inJustDecodeBounds = false;
			opts2.inSampleSize = inSampleSize;
			imageView.setImageDrawable(null);
			// imageView.setImageURI(Uri.fromFile(imagePath));
			imageView.setImageBitmap(BitmapFactory.decodeFile(
					imagePath.getAbsolutePath(), opts2));
			imageView.invalidate();
		}
		// imageView.setVisibility(View.VISIBLE);

	}

	private void updateLocation() {
		textLocation.setText(getString(R.string.text_location) + location);
	}

	private void updateDate() {
		textDate.setText(getString(R.string.text_date) + timestamp);
	}
}

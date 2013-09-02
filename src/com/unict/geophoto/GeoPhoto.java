package com.unict.geophoto;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "", formUri = "http://beaver.cu.cc/geophoto/report.php")
public class GeoPhoto extends Application {
	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		super.onCreate();
		ACRA.init(this);
	}
}
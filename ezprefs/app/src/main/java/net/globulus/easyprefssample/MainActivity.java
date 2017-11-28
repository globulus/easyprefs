package net.globulus.easyprefssample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import net.globulus.easyprefs.EasyPrefs;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int a = 5;
		EasyPrefs.MorePrefs.putIntField(this, a);
		int b = EasyPrefs.MorePrefs.getIntField(this);
		if (a != b) {
			throw new RuntimeException();
		}

		JsonObject json = new JsonObject();
		json.addProperty("a", 1);
		json.addProperty("b", "aa");
		EasyPrefs.MorePrefs.putJsonTest(this, json);
		JsonObject json2 = EasyPrefs.MorePrefs.getJsonTest(this);
		if (!json2.equals(json)) {
			throw new RuntimeException();
		}

	}
}

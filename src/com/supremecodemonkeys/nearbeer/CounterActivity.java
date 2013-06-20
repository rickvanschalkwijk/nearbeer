package com.supremecodemonkeys.nearbeer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends Activity {

	int numberOfBeer;
	TextView counterTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter);
		numberOfBeer = 0;
		Button countBtn = (Button) findViewById(R.id.countBtn);
		counterTxt = (TextView) findViewById(R.id.count);
		countBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String beerNumber = String.format(Integer.toString(numberOfBeer++));
				counterTxt.setText(beerNumber);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.counter, menu);
		return true;
	}

}

package com.spongesoft.bananarun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.spongesoft.bananarun.LineChart;
import com.spongesoft.bananarun.MainActivity;
import com.spongesoft.bananarun.R;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.Toast;

public class ListGraphsActivity extends Activity {
	// action id
	private static final int ID_DISTANCE = 1;
	private static final int ID_SPEED = 2;
	private static final int ID_ALTITUDE = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgraph);

		ActionItem nextItem = new ActionItem(ID_DISTANCE, "Distance",
				getResources().getDrawable(R.drawable.menu_ok));
		ActionItem prevItem = new ActionItem(ID_SPEED, "Speed", getResources()
				.getDrawable(R.drawable.menu_ok));
		ActionItem searchItem = new ActionItem(ID_ALTITUDE, "Altitude",
				getResources().getDrawable(R.drawable.menu_ok));

		// use setSticky(true) to disable QuickAction dialog being dismissed
		// after an item is clicked
		prevItem.setSticky(true);
		nextItem.setSticky(true);

		// create QuickAction. Use QuickAction.VERTICAL or
		// QuickAction.HORIZONTAL param to define layout
		// orientation
		final QuickAction quickAction = new QuickAction(this,
				QuickAction.VERTICAL);

		// add action items into QuickAction
		quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		quickAction.addActionItem(searchItem);

		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						Log.d("actionId", actionId+"");
						// here we can filter which action item was clicked with
						// pos or actionId parameter
						if (actionId == ID_DISTANCE) {
							Intent next = new Intent(ListGraphsActivity.this,
									LineChart.class);
							next.putExtra("graphID", actionId);
							Intent lineChartIntent = new Intent(
									ListGraphsActivity.this, LineChart.class);
							startActivity(lineChartIntent);

							// Toast.makeText(getApplicationContext(),
							// "Let's do some search action",
							// Toast.LENGTH_SHORT).show();
						} else if (actionId == ID_SPEED) {
							Intent next = new Intent(ListGraphsActivity.this,
									LineChart.class);
							next.putExtra("graphID", actionId);
							Intent lineChartIntent = new Intent(
									ListGraphsActivity.this, LineChart.class);
							startActivity(lineChartIntent);
							// Toast.makeText(getApplicationContext(),
							// "I have no info this time",
							// Toast.LENGTH_SHORT).show();
						} else if (actionId == ID_ALTITUDE) {
							Intent next = new Intent(ListGraphsActivity.this,
									LineChart.class);
							next.putExtra("graphID", actionId);
							Intent lineChartIntent = new Intent(
									ListGraphsActivity.this, LineChart.class);
							startActivity(lineChartIntent);
							// Toast.makeText(getApplicationContext(),
							// actionItem.getTitle() + " selected",
							// Toast.LENGTH_SHORT).show();
						}
					}
				});

		// set listnener for on dismiss event, this listener will be called only
		// if QuickAction dialog was dismissed
		// by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
				/*
				 * Toast.makeText(getApplicationContext(), "Dismissed",
				 * Toast.LENGTH_SHORT).show();
				 */
			}
		});

		// show on btn1
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

	}
}
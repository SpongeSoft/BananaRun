package com.spongesoft.bananarun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.spongesoft.bananarun.LineChart;
import com.spongesoft.bananarun.MainActivity;
import com.spongesoft.bananarun.R;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.Toast;


public class ListGraphsActivity extends Activity {
	//action id
	private static final int ID_UP     = 1;
	private static final int ID_DOWN   = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_INFO   = 4;
	private static final int ID_ERASE  = 5;	
	private static final int ID_OK     = 6;
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgraph);

		ActionItem nextItem 	= new ActionItem(ID_DOWN, "Distance", getResources().getDrawable(R.drawable.menu_ok));
		ActionItem prevItem 	= new ActionItem(ID_UP, "Speed", getResources().getDrawable(R.drawable.menu_ok));
        ActionItem searchItem 	= new ActionItem(ID_SEARCH, "Altitude", getResources().getDrawable(R.drawable.menu_ok));
      
        
        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
        prevItem.setSticky(true);
        nextItem.setSticky(true);
		
		//create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout 
        //orientation
		final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
        quickAction.addActionItem(searchItem);
        
        
        //Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
                 
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_SEARCH) {
					Intent lineChartIntent=new Intent(ListGraphsActivity.this,LineChart.class);
	                startActivity(lineChartIntent);
					
					//Toast.makeText(getApplicationContext(), "Let's do some search action", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_DOWN) {
					Intent lineChartIntent=new Intent(ListGraphsActivity.this,LineChart.class);
	                startActivity(lineChartIntent);
					//Toast.makeText(getApplicationContext(), "I have no info this time", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_UP){
					Intent lineChartIntent=new Intent(ListGraphsActivity.this,LineChart.class);
	                startActivity(lineChartIntent);
					//Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
		//by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
			@Override
			public void onDismiss() {
				/*Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();*/
			}
		});
		
		//show on btn1
		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

		
	}
}
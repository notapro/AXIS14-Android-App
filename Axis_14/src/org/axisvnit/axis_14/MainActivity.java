package org.axisvnit.axis_14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.axisvnit.axis_14.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.davemorrissey.labs.subscaleview.PinView;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.AnimationBuilder;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends SherlockActivity implements OnClickListener {
	public HashMap<String, Marker> data;
	public PinView imageView;
	public SlidingMenu slidingMenu;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	TextView txtSlideInfo;
	int opened= 100 , closed=100;
	int sdOpen = 100;
	int done = 0;
	int backcheck1 = 0;
	AlertDialog alertDialog1;

	static Typeface localTypeface;

	private static MainActivity mainActivity ;

	Handler handle = new Handler();

	public static final PointF MAP_CENTER = new PointF(1500.0F, 900.0F);

	@SuppressWarnings("deprecation")
	SlidingDrawer sd; // 
	Button Btnslide; //

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("main", "main");

		localTypeface = Typeface.createFromAsset(getAssets(), "fonts/hello.otf");

		setMainActivity(this);

		this.data = new Locations().data;


		//setting the hashmap values initially 
		imageView = (PinView)findViewById(id.imageView);
		Log.d("imageView", "imageview");

		imageView.setData(this.data);

		txtSlideInfo = (TextView)findViewById(R.id.txtSlideInfo);
		txtSlideInfo.setTypeface(localTypeface);

		slidingMenu = new SlidingMenu(this);
		Log.d("slidingMenu", "slidingMenu");
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		slidingMenu
		.setBehindOffset(getResources().getDisplayMetrics().widthPixels * 1/10);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.attachToActivity(MainActivity.this,
				SlidingMenu.SLIDING_CONTENT);
		slidingMenu.bringToFront();
		slidingMenu.setMenu(R.layout.slidingmenu);

		init_content();

		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Events");
		getSupportActionBar().setIcon(R.drawable.ic_launcher);

		initialiseImage();
		this.done += 1;

		sd = (SlidingDrawer)findViewById(R.id.slidingDrawer1); 
		Btnslide = (Button)findViewById(R.id.handle); //
		Btnslide.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				MainActivity.this.sdOpen = 0;
				//Log.d("SDOPEN CHEKC", "set");
				//sd.animateOpen();

			}


		});
		sd.setVisibility(View.INVISIBLE);
		sd.setOnDrawerOpenListener(new OnDrawerOpenListener() 
		{

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				MainActivity.this.sdOpen = 0;
				Btnslide.setBackgroundResource(R.drawable.down);
			}
		});

		sd.setOnDrawerCloseListener(new OnDrawerCloseListener() 
		{

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				MainActivity.this.sdOpen = 100;
				Btnslide.setBackgroundResource(R.drawable.up);
			}
		});


	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub

		switch (item.getItemId()) 
		{
		case android.R.id.home:

			if(this.sdOpen==0)
			{	
				sd.close();
			}
			this.slidingMenu.toggle();
			this.backcheck1=1;

			return true;
			
		}


		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// slidingMenu.showMenu();
	}

	private void initialiseImage() 
	{
		final GestureDetector gestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() 
		{

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) 
			{
				if (imageView.isImageReady()) 
				{
					PointF sCoord = imageView.viewToSourceCoord(
							e.getX(), e.getY());

					PointF touchedPoint = new PointF(sCoord.x, sCoord.y);
					Marker m = imageView.getNearestMarker(touchedPoint);
					if (m != null) 
					{
						touchedPoint = m.point;
						float maxScale = imageView.getMaxScale();
						float minScale = imageView.getMinScale();
						float scale = (2 * (maxScale - minScale))
								+ minScale;
						imageView.setPin(m);
						sd.setVisibility(View.VISIBLE);
						Btnslide.setText(m.name);
						txtSlideInfo.setText(Html.fromHtml(m.description));

						AnimationBuilder animationBuilder = imageView
								.animateScaleAndCenter(scale,
										touchedPoint);
						animationBuilder.withDuration(750).start();
					}
				} else 
				{
					Toast.makeText(getApplicationContext(),
							"Single tap: Image not ready",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		imageView.setImageAsset("mapfinale.jpg");	

		imageView.setPin(this.data.get("SAC"));


		if(this.done==0)
		{
			check();
		}

		imageView.setOnTouchListener(new OnTouchListener() 
		{

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) 
			{
				return gestureDetector.onTouchEvent(motionEvent);
			}
		});
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message paramAnonymousMessage)
		{
			switch (paramAnonymousMessage.what)
			{
			default: 
				return;
			case 1: 
				//MapActivity.this.showReslutOnMap((String)paramAnonymousMessage.obj);
				return;
			case 2: 
				MainActivity.this.check();
				return;
			case 3: 
				//MapActivity.this.playAnimSound(paramAnonymousMessage.arg1);
				return;
			}
			//MapActivity.this.displayMap();
		}
	};

	final Runnable run1 = new Runnable() 
	{
		@Override
		public void run() 
		{
			float maxScale1 = imageView.getMaxScale();
			float minScale1 = imageView.getMinScale();
			float scale = (108/100 * (maxScale1 - minScale1))
					+ minScale1;
			PointF init = new PointF(1959.0F,921.0F);
			//AnimationBuilder animationBuilder1 = 
			imageView.animateScaleAndCenter(scale,init).withDuration(750).start();
			//animationBuilder1.withDuration(750).start();
		}
	};

	public void check()
	{	
		if(!imageView.isImageReady())
		{
			Message localMessage = this.mHandler.obtainMessage(2);
			this.mHandler.sendMessageDelayed(localMessage, 250L);
		}
		else
		{
			this.handle.post(run1);
		}


	}

	public static MainActivity getMainActivity()
	{
		return mainActivity;
	}

	public static void setMainActivity(MainActivity paramMapActivity)
	{
		mainActivity = paramMapActivity;
	}


	private void init_content() 
	{
		// TODO Auto-generated method stub
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() 
		{

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) 
			{
				//				 Toast.makeText(getApplicationContext(),
				//				 "Group Clicked " + listDataHeader.get(groupPosition),
				//				 Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() 
		{

			@Override
			public void onGroupExpand(int groupPosition) 
			{
				//				Toast.makeText(getApplicationContext(),
				//					listDataHeader.get(groupPosition) + " Expanded",
				//				Toast.LENGTH_SHORT).show();
				//			
				if(MainActivity.this.opened==100)
				{
					MainActivity.this.opened = groupPosition;
				}
				else
				{
					expListView.collapseGroup(MainActivity.this.opened);
					MainActivity.this.opened = groupPosition;
				}


			}
		});


		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() 
		{

			@Override
			public void onGroupCollapse(int groupPosition) 
			{
				//				Toast.makeText(getApplicationContext(),
				//					listDataHeader.get(groupPosition) + " Collapsed",
				//				Toast.LENGTH_SHORT).show();
				if(groupPosition == MainActivity.this.opened) 
				{
					MainActivity.this.opened = 100;
				}

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
						+ " : "
						+ listDataChild.get(
								listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
										.show();
				slidingMenu.toggle();
				Marker m=data.get(listDataChild.get(
						listDataHeader.get(groupPosition)).get(
								childPosition));
				if (m != null) {
					PointF touchedPoint = m.point;
					float maxScale = imageView.getMaxScale();
					float minScale = imageView.getMinScale();
					float scale = (2 * (maxScale - minScale))
							+ minScale;
					imageView.setPin(m);
					AnimationBuilder animationBuilder = imageView
							.animateScaleAndCenter(scale,
									touchedPoint);
					animationBuilder.withDuration(750).start();
					sd.setVisibility(View.VISIBLE);
					Btnslide.setText(m.name);
					txtSlideInfo.setText(Html.fromHtml(m.description));
					expListView.collapseGroup(MainActivity.this.opened);
				}

				return false;
			}

		});
	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Automation & Robotics");
		listDataHeader.add("Construction, Design \n & Architecture");
		listDataHeader.add("Software & Electronics");
		listDataHeader.add("Management & Other Events");
		listDataHeader.add("School Events");
		//listDataHeader.add("Seminars");
		listDataHeader.add("Exhibitions & Attractions");


		// Adding child data
		List<String> autorobo = new ArrayList<String>();
		//autorobo.add("Aqua Hunt");
		//autorobo.add("Autobot");
		//autorobo.add("Contraption");
		autorobo.add("Mechatryst");
		autorobo.add("Robowars");
		autorobo.add("Robotrekker");
		//autorobo.add("Techno.docx");

		List<String> design = new ArrayList<String>();
		design.add("Crepido");
		design.add("Devise");
		design.add("Paradeigma");
		design.add("Pascal Trouble");
		//design.add("Turbo Flux");
		design.add("Techno.docx");

		List<String> soft = new ArrayList<String>();
		//soft.add("Crypto-Crux");
		//soft.add("Insomnia");
		soft.add("Techverve");
		//soft.add("Techno.docx");

		List<String> manage = new ArrayList<String>();
		//manage.add("221B Baker Street");
		//manage.add("Freak-o-Matrix");
		manage.add("Gamesutra");
		manage.add("Informals");
		manage.add("Wall Street");
		manage.add("Who's the Boss");

		List<String> school = new ArrayList<String>();
		school.add("Junior Scientist-a");
		school.add("Junior Scientist-b");
		school.add("Dexter");

		//List<String> exhibitions = new ArrayList<String>();
		////exhibitions.add("GuestSeminar-a");
		//exhibitions.add("GuestSeminar-b");
		//exhibitions.add("Seminar");

		List<String> attract = new ArrayList<String>();
		attract.add("Laser Tag");
		attract.add("DRDO Exhibition");
		//attract.add("Seminar");

		listDataChild.put(listDataHeader.get(0), autorobo); // Header, Child data
		listDataChild.put(listDataHeader.get(1), design);
		listDataChild.put(listDataHeader.get(2), soft);
		listDataChild.put(listDataHeader.get(3), manage);
		listDataChild.put(listDataHeader.get(4), school);
		//listDataChild.put(listDataHeader.get(5), exhibitions);
		listDataChild.put(listDataHeader.get(5), attract);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if(MainActivity.this.sdOpen == 0)
		{
			sd.close();
			return;

		}
		if(MainActivity.this.backcheck1==1)
		{
			slidingMenu.toggle();
			return;
		}
		//if
//		alertDialog1 = new AlertDialog.Builder(getApplicationContext()).create();
//
//		// Setting Dialog Title
//		alertDialog1.setTitle("EXIT ?");
//
//		// Setting Dialog Message
//		
//		alertDialog1.setMessage(Html.fromHtml("<p>Do you really wish to exit AXIS'14 ?"));
//		// Setting Icon to Dialog
//		//alertDialog1.setIcon(R.drawable.tick);
//
//		// Setting OK Button
//		alertDialog1.setButton("OK", new DialogInterface.OnClickListener() 
//		{
//
//			public void onClick(DialogInterface dialog, int which) 
//			{
//				// Write your code here to execute after dialog
//				// closed
//				alertDialog1.cancel();
//
//			}
//		});
//
//		// Showing Alert Message
//		alertDialog1.show();
//		return;
//		
		super.onBackPressed();

	}





}

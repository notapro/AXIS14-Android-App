

package com.davemorrissey.labs.subscaleview;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.axisvnit.axis_14.MainActivity;
import org.axisvnit.axis_14.Marker;
import org.axisvnit.axis_14.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class PinView extends SubsamplingScaleImageView 
{

	private Collection<Marker> markerList;
	//private HashMap<String, Marker> data;
	
    private Marker sPin;
    private Bitmap pin;
    private float density;

	private boolean isFirstLoad = true;
	private MainActivity mainActivity;
    
	public PinView(Context context) {
        this(context, null);
    }
	
    public PinView(Context context, AttributeSet attr) 
    {
        super(context, attr);
        initialise();
    }
    
//    Handler hand1 = new Handler();

//    protected void onImageReady()
//    {
//    	
//	    final Runnable run1 = new Runnable() 
//		{
//	
//			@Override
//			public void run() 
//			{
//				if (PinView.this.isFirstLoad )
//		        {
//		    		
//		          Runnable local1 = new Runnable()
//		          {
//		            public void run()
//		            {
//		              PinView.this.animateScaleAndCenter(PinView.this.getTargetMinScale(), MainActivity.MAP_CENTER).withDuration(750).start();
//		              PinView.this.isFirstLoad = false;
//		            }
//		          };
//		          PinView.this.mainActivity.runOnUiThread(local1);
//		        }
//	
//			 }
//		 };
//		 hand1.postDelayed(run1,4000);
//		 
//        
//     }
    
    public float getTargetMinScale()
    {
      return Math.max(getWidth() / getSWidth(), getHeight() / getSHeight());
    }
    
    
    public void setPin(Marker sPin) 
    {
        this.sPin = sPin;
        initialise();
        invalidate();
    }

    public Marker getPin() 
    {
        return sPin;
    }

    private void initialise() 
    {
        this.density = getResources().getDisplayMetrics().densityDpi;
        pin = BitmapFactory.decodeResource(this.getResources(), drawable.pina);
        float w = (density/420f) * pin.getWidth();
        float h = (density/420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
        this.mainActivity = MainActivity.getMainActivity();
    }

    @Override
    protected void onDraw(Canvas canvas) 
    {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isImageReady()) 
        {
            return;
        }

        Paint paint = new Paint();
        Paint textPaint = new Paint();
        float d2=getResources().getDisplayMetrics().density;
        textPaint.setAntiAlias(true);
        textPaint.setColor(-1);
        textPaint.setShadowLayer(8.0F * d2, -1.0F * d2, 1.0F * d2, -16777216);
        textPaint.setTextSize(16.0F * d2);
        paint.setAntiAlias(true);

        if (sPin != null && pin != null) {
            PointF vPin = sourceToViewCoord(sPin.point);
            float vX = vPin.x - (pin.getWidth()/2);
            float vY = vPin.y - pin.getHeight();
            canvas.drawBitmap(pin, vX, vY, paint);
            canvas.drawText(sPin.buildingName, vX-50, vY+107, textPaint);
            //paramCanvas.drawText(str, localPointF.x - this.bounds.width() / 2, localPointF.y + this.bounds.height(), this.textPaint);
        }

    }
    
    
 // setting the hashmap values from main_activity into map_view class.!

 	  public void setData(HashMap<String, Marker> paramHashMap)
 	  {
 	    //this.data = paramHashMap;
 	    this.markerList = paramHashMap.values();
 	  
 	  }
 	  
 	// to find the nearest marker !! :D 

 	 public Marker getNearestMarker(PointF paramPointF)
 	  {
 	    Marker localObject = null;
 	    float f1 = 1.0E+02F;  // 1*10^8
 	    Iterator localIterator = this.markerList.iterator();
 	    for (;;)
 	    {
 	      if (!localIterator.hasNext()) {
 	        return localObject;
 	      }
 	      Marker localMarker = (Marker)localIterator.next();
 	      float f2 = (float)calculateDistance(localMarker.point, paramPointF);
 	      if ((f2 < f1))
 	      {
 	        f1 = f2;
 	        localObject = localMarker;
 	      }
 	    }
 	  }


 	// to calculate the distance from the touched point to the values !
 	 
 	  private double calculateDistance(PointF paramPointF1, PointF paramPointF2)
 	  {
 	    float f1 = paramPointF1.x - paramPointF2.x;
 	    float f2 = paramPointF1.y - paramPointF2.y;
 	    return Math.sqrt(f1 * f1 + f2 * f2);
 	  }

	public void callSuperOnTouch(MotionEvent paramAnonymousMotionEvent) 
	{
		// TODO Auto-generated method stub
		super.onTouchEvent(paramAnonymousMotionEvent);
	}
 	  
    

}

package com.example.x30_drawingboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ImageView iv;
    int startX;
    int startY;
	private Canvas canvas;
	private Paint paint;
	private Bitmap bmCopy;
	private Bitmap bmCopy2;
	private Bitmap bmSrc;
	private int eraser = 0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //加载画板背景图
        bmSrc = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        
        //创建背景副本（创建纸）
        bmCopy = Bitmap.createBitmap(bmSrc.getWidth(), bmSrc.getHeight(), bmSrc.getConfig());
        //创建画笔
        paint = new Paint();
        paint.setStrokeWidth(5);
        
        //创建画板
        canvas = new Canvas(bmCopy);
        
        //绘制
        canvas.drawBitmap(bmSrc, new Matrix(), paint);
        
        iv = (ImageView) findViewById(R.id.iv);
        iv.setImageBitmap(bmCopy);
        //设置监听事件
        iv.setOnTouchListener(new OnTouchListener() {
			
        	//触摸事件产生时，此方法调用
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//获得事件
				int action = event.getAction();
				switch (action) {
				
				//手指触摸屏幕
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getX();
					startY = (int) event.getY();
					
					break;
					
				//手指在屏幕上移动
				case MotionEvent.ACTION_MOVE:
					int x = (int) event.getX();
					int y = (int) event.getY();
					
					canvas.drawLine(startX, startY, x, y, paint);
					
//					//触摸置为透明
//					bmCopy.setPixel(x, y, Color.TRANSPARENT);
					
					iv.setImageBitmap(bmCopy);
					
					startX = x;
					startY = y;
					
					break;
					
				//手指离开屏幕
				case MotionEvent.ACTION_UP:
					
					break;
				}
				return true;
			}
		});
        
    }
	

	public void black(View v){
		if(eraser == 1){
			paint.setStrokeWidth(5);
			eraser = 0;
		}
		paint.setColor(Color.BLACK);
	}

	public void red(View v){
		if(eraser == 1){
			paint.setStrokeWidth(5);
			eraser = 0;
		}
		paint.setColor(Color.RED);
	}
	
	public void yellow(View v){
		if(eraser == 1){
			paint.setStrokeWidth(5);
			eraser = 0;
		}
		paint.setColor(Color.YELLOW);
	}
	
	public void blue(View v){
		if(eraser == 1){
			paint.setStrokeWidth(5);
			eraser = 0;
		}
		paint.setColor(Color.BLUE);
	}
	
	public void green(View v){
		if(eraser == 1){
			paint.setStrokeWidth(5);
			eraser = 0;
		}
		paint.setColor(Color.GREEN);
	}
	
	public void l(View v){
		if(eraser == 1){
			paint.setColor(Color.BLACK);
			eraser = 0;
		}
		paint.setStrokeWidth(5);
	}
	
	public void x(View v){
		if(eraser == 1){
			paint.setColor(Color.BLACK);
			eraser = 0;
		}
		paint.setStrokeWidth(10);
	}
	
	public void eraser(View v){
		paint.setStrokeWidth(20);
		paint.setColor(Color.WHITE);
		eraser  = 1;
	}
	
	public void clear(View v){
		canvas.drawBitmap(bmSrc, new Matrix(), paint);
		iv.setImageBitmap(bmCopy);
	}
	
	
	public void save(View v){
		EditText et = (EditText) findViewById(R.id.et);
		String text = et.getText().toString();
		
		if(!"".equals(text)){
			File file = new File(Environment.getExternalStorageDirectory(),text + ".jpg");
			FileOutputStream fos = null;
			
			try {
				fos = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//压缩保存
			bmCopy.compress(CompressFormat.PNG, 100, fos);
			
			//发送SD卡就绪广播
//			Intent intent = new Intent();
//			intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//			intent.setData(Uri.parse("file://" + Environment.getExternalStorageDirectory()));
//			sendBroadcast(intent);
			
			MediaScannerConnection.scanFile(this, new String[] { Environment.getExternalStorageDirectory().toString() }, null, new MediaScannerConnection.OnScanCompletedListener() {
	            /*
	             *   (non-Javadoc)
	             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
	             */
	            public void onScanCompleted(String path, Uri uri) 
	              {
	                  Log.i("ExternalStorage", "Scanned " + path + ":");
	                  Log.i("ExternalStorage", "-> uri=" + uri);
	              }
	            });
			
			
			
			
			Toast.makeText(this, "文件成功保存至sdcard/" + text + ".jpg", Toast.LENGTH_SHORT).show();
			
		}else{
			Toast.makeText(this, "请自定义文件名存储", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

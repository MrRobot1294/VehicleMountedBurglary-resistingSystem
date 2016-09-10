package com.example.wificar;

import java.io.PrintWriter;
import java.net.Socket;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private Socket socket = null;
	private static PrintWriter printWriter = null;
	private SeekBar seekBar;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	ToggleButton toggleButtonleft;
	ToggleButton toggleButtonright;
	private Thread thread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		button1 = (Button) findViewById(R.id.buttonup);
		button2 = (Button) findViewById(R.id.buttondown);
		button3 = (Button) findViewById(R.id.buttonleft);
		button4 = (Button) findViewById(R.id.buttonright);
		toggleButtonleft = (ToggleButton) findViewById(R.id.leftled);
		toggleButtonright = (ToggleButton) findViewById(R.id.rightled);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);

		thread = new Thread(runnable);
		thread.start();
		
		button1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				if (arg1.getAction() == arg1.ACTION_DOWN) {
					printWriter.print('a'); // a表示向前命令
					printWriter.flush();    //刷新缓存
				}
				if (arg1.getAction() == arg1.ACTION_UP) {
					printWriter.print('e'); // e表示停止命令
					printWriter.flush();
				}
				return false;
			}
		});

		button2.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == arg1.ACTION_DOWN) {
					printWriter.print('b'); // b表示向后命令
					printWriter.flush();
				}
				if (arg1.getAction() == arg1.ACTION_UP) {
					printWriter.print('f'); // f表示停止命令
					printWriter.flush();
				}
				return false;

			}
		});

		button3.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == arg1.ACTION_DOWN) {
					printWriter.print("c"); // c表示向左命令
					printWriter.flush();
				}
				if (arg1.getAction() == arg1.ACTION_UP) {
					printWriter.print("g"); // g表示停止命令
					printWriter.flush();
				}
				return false;

			}
		});

		button4.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == arg1.ACTION_DOWN) {
					printWriter.print("d"); // d表示向右命令
					printWriter.flush();
				}
				if (arg1.getAction() == arg1.ACTION_UP) {
					printWriter.print("h"); // h表示停止命令
					printWriter.flush();
				}
				return false;
			}
		});
		
		/*左侧LED的开关*/
		
		toggleButtonleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(toggleButtonleft.isChecked()){
					printWriter.print("i");     //打开左侧led的命令
					printWriter.flush();
				}else{
					printWriter.print("k");     //关闭左侧led的命令
					printWriter.flush();
				}
			}
		});
		
		/*右侧LED的开关*/
		
        toggleButtonright.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(toggleButtonright.isChecked()){
					printWriter.print("j");     //打开右侧LED的命令
					printWriter.flush();
				}else{
					printWriter.print("l");     //关闭右侧LED的命令
					printWriter.flush();
				}
			}
		});

        /*seekbar用于调速*/
		seekBar.setMax(200);                    //设置seekbar的最大值为200
		seekBar.setProgress(100);               //设置seekbar的初值为100
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int start;
			int stop;

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				stop = arg0.getProgress();
				if (stop - start > 0) {
					for (int i = 0; i < (stop - start) / 10; i++) {
						printWriter.print('m');    //加速命令
						printWriter.flush();
					}
				} else {
					for (int i = 0; i < (start - stop) / 10; i++) {
						printWriter.print('n');    //减速命令
						printWriter.flush();
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				start = arg0.getProgress();
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
			}
		});

	}

	private Runnable runnable = new Runnable() {
		public void run() {
			try {
				socket = Data.getSocket();      
				printWriter = new PrintWriter(socket.getOutputStream(), true);// 根据新建的sock建立
			} catch (Exception e) {
				return;
			}
		}
	};
}

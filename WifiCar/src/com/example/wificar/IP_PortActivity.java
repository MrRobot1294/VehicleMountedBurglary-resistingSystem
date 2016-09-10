package com.example.wificar;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IP_PortActivity extends Activity {
	Socket socket = null;
	private Thread thread = null;
	private String ip;
	private String port;
	private EditText editText1;
	private EditText editText2;
	Handler myHandler;
	Message message;

	private Button button1;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip__port);
		editText1 = (EditText) findViewById(R.id.ipaddress);
		editText2 = (EditText) findViewById(R.id.portnum);
		button1 = (Button) findViewById(R.id.connect);
		button2 = (Button) findViewById(R.id.exit);

		/*当无法连接时打印提示并将IP及端口号的输入清空，重新获取输入*/
	    myHandler = new Handler() {        
	          public void handleMessage(Message msg) {   
	               switch (msg.what) {   
	                    case 1:{
							thread.interrupt();
							thread = null;
	                    	Toast.makeText(IP_PortActivity.this, "连接失败", Toast.LENGTH_SHORT).show();  
	                        editText1.setText("");
	 						editText2.setText("");
	                         break;
	                    }
	                    default:
	                    	break;
	                            
	               }   
	               super.handleMessage(msg);   
	          }   
	     };
	     
	     
	    /*连接按钮的监听，用于获取到IP和端口号后启动创建socket的线程*/ 
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ip = editText1.getText().toString().trim();
				port = editText2.getText().toString().trim();
				thread = new Thread(runnable);
				thread.start();
			}
		});
		
		/*监听退出按钮，用于结束整个程序*/
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				  android.os.Process.killProcess(android.os.Process.myPid());   //获取PID 
				  System.exit(0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ip__port, menu);
		return true;
	}

	/*由于socket不宜在UI中创建，所以创建socket在线程中进行，
	 * 当连接出错时产生异常，异常的处理使用handler和mseeage将连接失败的提示显示出来，并关闭线程，重新获取IP和端口号用于开启线程*/
	
	private Runnable runnable = new Runnable() {
		public void run() {
				    try {
						socket = new Socket();
						SocketAddress socketAddress = new InetSocketAddress(ip, Integer.parseInt(port));
						socket.connect(socketAddress, 1000);
						Data.setSocket(socket);   //Data类用于将socket定义为全局变量，便于socket在两个页面的传输
						Intent intent = new Intent(IP_PortActivity.this,
										MainActivity.class);
						IP_PortActivity.this.startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("socket","连接失败");
						message = new Message();
						message.what = 1;
						myHandler.sendMessage(message);						
						return ;
					} 			    
				    
					
		}
	};
}

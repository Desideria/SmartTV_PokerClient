package com.cstzju.poker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONException;

import com.cstzju.poker.net.NetMessage;
import com.cstzju.poker.net.NetPushCard;
import com.cstzju.poker.utils.*;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
 
	//UI
	private static Button buttonPost;
	private Button buttonGiveup;
	private Button buttonReselect;
	
	//游戏逻辑
	private Player player;
	
	//通信
	DatagramSocket socket;

	DatagramPacket packetReceive;
	DatagramPacket packetSend;

	InetAddress broadCastAddress;
	String ipAddress;
	String message;
	String serverIp = null;
	static int tcpServerPort;

	byte[] dataSend;
	byte[] dataReceive = new byte[1024];
	
	NetPushCard mNetPushCard;
	NetMessage mNetMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 锁定横屏   
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        setContentView(R.layout.activity_main);
        
        player = Player.getInstance(this);
        mNetPushCard = new NetPushCard();
        mNetMessage = new NetMessage();
        
        buttonPost = (Button) findViewById(R.id.push_poker);
        buttonGiveup = (Button) findViewById(R.id.give_up);
        buttonReselect= (Button) findViewById(R.id.reselect);
        buttonPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int[] myCardsId = CardUtil.Objects2Id(player.myCards);
				try {
					mNetPushCard.setData(myCardsId);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					// TCP线程
					Socket socket = new Socket(serverIp, tcpServerPort);
					// System.out.println("Send a socket:" + message);
					OutputStream os = socket.getOutputStream();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Send a socket:" + message,
									Toast.LENGTH_LONG).show();
						}
					});
					// 加上toStream之后的代码
					mNetMessage.toStream(os);

					Log.i("test", "数据被编码成字节流发送给" + serverIp + ":"
							+ tcpServerPort);
					os.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.cards.removeAll(player.myCards);
				buttonPost.setBackgroundResource(R.drawable.button_push_no);
				buttonPost.setClickable(false);
				
			}
        	
        });
        
        buttonGiveup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<player.myCards.size();++i) {
					player.myCards.get(i).selected = false;
				}
				player.myCards.clear();
			}
        	
        });
        
        buttonReselect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<player.myCards.size();++i) {
					player.myCards.get(i).selected = false;
				}
				player.myCards.clear();
			}
        	
        });
        
        new Thread(new Runnable() {
			@Override
			public void run() {
				//建立连接，获得serverIp
				
				try {
					Log.i("test", "客户端建立UDP");
					socket = new DatagramSocket(CharacterUtil.udpClientPort);
					broadCastAddress = InetAddress.getByName("255.255.255.255");
					ipAddress = Common.getLocalIP();
					dataSend = ipAddress.getBytes();
					packetSend = new DatagramPacket(dataSend, dataSend.length,
							broadCastAddress, CharacterUtil.udpServerPort);
					socket.send(packetSend);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"send:" + ipAddress, Toast.LENGTH_LONG)
									.show();
						}
					});
					Log.i("test", "客户端已经发送UDP广播");
					packetReceive = new DatagramPacket(dataReceive,
							dataReceive.length);
					Log.i("test", "客户端准备接受UDP回应");
					socket.receive(packetReceive);
					Log.i("test", "客户端收到UDP回应");
					tcpServerPort = Integer.parseInt(new String(packetReceive
							.getData()).substring(0, 5));

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"receive:" + tcpServerPort,
									Toast.LENGTH_LONG).show();
						}
					});
					socket.close();
					serverIp = packetReceive.getAddress().getHostAddress();

				} catch (SocketException e) {
					Log.e("Socket Exception", e.toString());
				} catch (UnknownHostException e) {
					Log.e("UnknownHostException", e.toString());
				} catch (IOException e) {
					Log.e("IO Exception", e.toString());
				}
			}
					
			
		}).start();
    }
	
	

	static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					buttonPost.setBackgroundResource(R.drawable.button_push);
					buttonPost.setClickable(true);
					break;
				case 1:
					buttonPost.setBackgroundResource(R.drawable.button_push_no);
					buttonPost.setClickable(false);
					break;
				case 2:
					break;
			}
		}

	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.cstzju.poker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;

import com.cstzju.poker.game.Card;
import com.cstzju.poker.game.CardUtil;
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
	private static Button buttonGiveup;
	private static Button buttonReselect;
	
	//游戏逻辑
	private Player player;
	boolean firstCards = true;
	
	//通信
	DatagramSocket socket;

	DatagramPacket packetReceive;
	DatagramPacket packetSend;

	InetAddress broadCastAddress;
	String ipAddress;
	String message;
	String serverIp;
	static int tcpServerPort;

	byte[] dataSend;
	byte[] dataReceive = new byte[1024];
	
	NetPushCard mNetPushCard;
	
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
        
        buttonPost = (Button) findViewById(R.id.push_poker);
        buttonGiveup = (Button) findViewById(R.id.give_up);
        buttonReselect= (Button) findViewById(R.id.reselect);
        
        
        
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
					Log.i("test", "客户端已经发送UDP广播");
					packetReceive = new DatagramPacket(dataReceive,
							dataReceive.length);
					Log.i("test", "客户端准备接受UDP回应");
					socket.receive(packetReceive);
					Log.i("test", "客户端收到UDP回应");
					socket.close();
					if(serverIp==null) {
						serverIp = packetReceive.getAddress().getHostAddress();
						Log.i("test", "serverIp " + serverIp);
					}
					
				} catch (SocketException e) {
					Log.e("Socket Exception", e.toString());
				} catch (UnknownHostException e) {
					Log.e("UnknownHostException", e.toString());
				} catch (IOException e) {
					Log.e("IO Exception", e.toString());
				}
			}
			
		}).start();
        
        new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("test", "客户端上的服务器TCP");
				ServerSocket server;
				//NetGamePlaying card = new NetGamePlaying();
				try {
					server = new ServerSocket(CharacterUtil.tcpServerPort);
					while (true) {
						Socket client = server.accept();
						Log.i("test", "准备读入数据");
						InputStream ins = client.getInputStream();
						Log.i("test", "已经读入数据");
						
						// 加上fromStream之后的代码
						mNetPushCard.fromStream(ins);
					    int[] newCards = mNetPushCard.getCardNumberArray();
					    ArrayList<Card> cards = (ArrayList<Card>) CardUtil.Id2Object(newCards);
					    if(firstCards) {
					    	player.cards = cards;
					    	firstCards = false;
					    }else{
					    	player.preCards = cards;
					    }
					    
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("test", e.toString());
					e.printStackTrace();
				}
			}
		}).start();
        
        buttonPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
						int[] myCardsId = CardUtil.Objects2Id(player.myCards);
						try {
							mNetPushCard.setData(myCardsId);
							Log.i("test", "mNetPushCard");
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							// TCP
							Socket socket = new Socket(serverIp, CharacterUtil.tcpServerPort);
							Log.i("test", "Send a socket:");
							OutputStream os = socket.getOutputStream();
							// 加上toStream之后的代码
							mNetPushCard.toStream(os);
							Log.i("test", "mNetPushCard pushed");
		
							Log.i("test", "数据被编码成字节流发送给serverIp " + serverIp);
							os.close();
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
//					}
//				}).start();
				player.cards.removeAll(player.myCards);
				player.myCards.clear();
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
					buttonPost.setVisibility(View.INVISIBLE);
			        buttonGiveup.setVisibility(View.INVISIBLE);
			        buttonReselect.setVisibility(View.INVISIBLE);
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

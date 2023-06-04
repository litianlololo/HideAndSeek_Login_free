package com.example.hideandseek_login_free;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//房间类，用于存储房间信息
class Room {
    List<String> Gamers = new ArrayList<String>();    //玩家昵称
    static int GamerNum=0;         //玩家人数

    List<String> GamerName = new ArrayList<>(); //玩家昵称
    boolean Start=false;                        //游戏是否开始 true为开始
    Room(){}
}
class Gamer{
    String GamerName;
    String Icon;
    Gamer(String GamerName,String Icon){
        this.GamerName=GamerName;
        this.Icon=Icon;
    }

    public Gamer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gamer gamer = (Gamer) o;
        return GamerName.equals(gamer.GamerName) && Icon.equals(gamer.Icon);
    }
};
public class Client extends MainActivity implements Runnable{
    private static final String TAG = "MyTag";
    public int Logon_status=0;   //登录状态  0初始 1登录成功 2密码错误 3无账号
    boolean quitflag=false;

    Gamer thisGamer;
    Room room= new Room();
    static String signsuccess="signsuccess";
    static Socket socket;
    public Client(Gamer g){
        this.thisGamer =g;
    }
    //要求断开连接
    public void needQUIT() throws IOException{
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("QUT");
        out.flush();
    }
    //获取玩家人数
    public void needNUM() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("NUM");
        out.flush();
    }
    public void getNUM(int msg_num) throws IOException {
        room.GamerNum=msg_num;
        Log.d(TAG,"getNum"+Integer.toString(msg_num));
        System.out.println(room.GamerNum);

        //TextView GameNumText = (TextView) findViewById(R.id.GamerNum);
        //GameNumText.setText(msg_num);
    }
    //获取全体玩家姓名
    public void needGNM() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("GNM");
        out.flush();
    }
    public void getGNM(String msg) throws IOException {
        Log.d(TAG,msg+"KKKKKKK");
        String tmpName= "";
        for(int i=0;i<msg.length();i++){
            char ch=msg.charAt(i);
            if(ch=='@' && !tmpName.equals("")){
                room.GamerName.add(tmpName.substring(3,tmpName.length()-2));
                Log.d(TAG,tmpName.substring(3,tmpName.length()-2));
                tmpName = "";
            }
            tmpName=tmpName + ch;
        }
        needQUIT();
    }
    //获取信息
    public void getmsg() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean flag=true;
        while(flag) {
            String msg_str = in.readLine();
            Log.d(TAG,msg_str);
            if (msg_str != null) {
                //根据前三位的特征字串进行分流
                String msg_fsub = msg_str.substring(0, 3);
                Log.d(TAG,msg_fsub);
                switch (msg_fsub) {
                    //玩家人数
                    case "NUM":
                        Log.d(TAG,msg_fsub + "case NUM");
                        int GNum=Integer.parseInt(msg_str.substring(3));
                        getNUM(GNum);
                        break;
                    //玩家昵称
                    case "GNM":
                        String GNM_str=msg_str.substring(3);
                        getGNM(GNM_str);
                        break;
                    //游戏结束
                    case "QUT":
                        flag=false;
                        break;
                }
            }
        }
    }
    public void matching() throws IOException {
        //获取系统标准输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //创建一个线程用于读取服务器的信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Logon_status=0;
                        String read = in.readLine();

                        if (read == null) {
                            continue;
                        } else {
                            Log.d(TAG, read);
                            //登录成功 自动进入房间
                            if (read.equals(signsuccess)) {
                                Logon_status = 1;
                                getmsg();
                                return;
                            }
                            return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //上传昵称和头像
        out.println(thisGamer.GamerName);
        out.flush();
        out.println(thisGamer.Icon);
        out.flush();
    }

    @Override
    public void run(){
        try {
            //创建连接指定Ip和端口的socket
            socket = null;
            socket = new Socket("110.40.206.206", 5200);
            matching();
            while (!quitflag){
            }
            socket.close();
        }catch (IOException e) {throw new RuntimeException(e);}
    }
}
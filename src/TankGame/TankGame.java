package TankGame;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.JOptionPane;

public class TankGame extends Frame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int Fram_width = 850; // 静态全局窗口大小
	public static final int Fram_length = 650;
	public static boolean printable = true;	//定义静态全局变量便于线程停止
	public static boolean music_printable = true;	//定义静态全局变量便于线程停止
	static int enemytank_count=8;//初始化敌人坦克数量
	
	static start cStart=new start();
	Image screenImage = null;
	MenuBar jmb = null;//菜单
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null;
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null,
			jmi6 = null, jmi7 = null, jmi8 = null, jmi9 = null,jmi10=null;
	
	GetBlood blood = new GetBlood(); // 实例化生命
	Home home = new Home(373, 545, this);// 实例化home
	TankGame tc=null;
	Tank homeTank = new Tank(300, 560, true, Direction.STOP, this);// 实例化坦克
	
	List<River> theRiver = new ArrayList<River>();// 实例化对象容器
	List<Tank> tanks = new ArrayList<Tank>();
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	List<Bullets> bullets = new ArrayList<Bullets>();
	List<Tree> trees = new ArrayList<Tree>();
	List<CommonWall> homeWall = new ArrayList<CommonWall>(); 
	List<CommonWall> otherWall = new ArrayList<CommonWall>();
	List<MetalWall> metalWall = new ArrayList<MetalWall>();
	
	public static void main(String[] args) {
		if(TankGame.music_printable)
			cStart.start();
		new TankGame(); // 实例化
	}
	
	public void update(Graphics g) {
		screenImage = this.createImage(Fram_width, Fram_length);
		Graphics gps = screenImage.getGraphics();
		gps.setColor(Color.DARK_GRAY);
		gps.fillRect(0, 0, Fram_width, Fram_length-50);//留有50的区域给底栏
		framPaint(gps);
		g.drawImage(screenImage, 0, 0, null);
	}

	public void framPaint(Graphics g) {//对主框架进行构造

		g.setColor(Color.red); // 设置字体显示属性
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("剩余敌方坦克: ", 120, 635);
		g.drawString("" + tanks.size(), 300,635);
		g.drawString("剩余生命值: ", 480, 635);
		g.drawString("" + homeTank.getLife(), 620, 635);

		if (homeTank.isLive() == false) {
			Font f = g.getFont();
			g.setFont(new Font("微软雅黑", Font.BOLD, 40));
			tanks.clear();
			bullets.clear();
			g.setFont(f);
		}

		home.draw(g); // 画出home
		homeTank.draw(g);// 画出自己家的坦克
		homeTank.eat(blood);// 充满血--生命值
		
		for (int i = 0; i < theRiver.size(); i++) { // 画出河流
			River r = theRiver.get(i);
			r.draw(g);
		}
		for (int i = 0; i < theRiver.size(); i++) {
			River r = theRiver.get(i);
			homeTank.collideRiver(r);
			r.draw(g);
		}
		
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i); // 获得键值对的键
			for (int j = 0; j < homeWall.size(); j++) {
				CommonWall cw = homeWall.get(j);
				t.collideWithWall(cw); // 每一个坦克撞到家里的墙时
				cw.draw(g);
			}
			for (int j = 0; j < otherWall.size(); j++) { // 每一个坦克撞到家以外的墙
				CommonWall cw = otherWall.get(j);
				t.collideWithWall(cw);
				cw.draw(g);
			}
			for (int j = 0; j < metalWall.size(); j++) { // 每一个坦克撞到金属墙
				MetalWall mw = metalWall.get(j);
				t.collideWithWall(mw);
				mw.draw(g);
			}
			for (int j = 0; j < theRiver.size(); j++) {
				River r = theRiver.get(j); // 每一个坦克撞到河流时
				t.collideRiver(r);
				r.draw(g);
			}
			t.collideWithTanks(tanks); // 撞到自己的人
			t.collideHome(home);
			t.draw(g);
		}

		blood.draw(g);//画出血条

		for (int i = 0; i < trees.size(); i++) { // 画出trees
			Tree tr = trees.get(i);
			tr.draw(g);
		}

		for (int i = 0; i < bombTanks.size(); i++) { // 画出爆炸效果
			BombTank bt = bombTanks.get(i);
			bt.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) { // 画出otherWall
			CommonWall cw = otherWall.get(i);
			cw.draw(g);
		}

		for (int i = 0; i < metalWall.size(); i++) { // 画出metalWall
			MetalWall mw = metalWall.get(i);
			mw.draw(g);
		}

		homeTank.collideWithTanks(tanks);
		homeTank.collideHome(home);

		for (int i = 0; i < metalWall.size(); i++) {// 撞到金属墙
			MetalWall w = metalWall.get(i);
			homeTank.collideWithWall(w);
			w.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) {
			CommonWall cw = otherWall.get(i);
			homeTank.collideWithWall(cw);
			cw.draw(g);
		}

		for (int i = 0; i < homeWall.size(); i++) { // 家里的坦克撞到自己家
			CommonWall w = homeWall.get(i);
			homeTank.collideWithWall(w);
			w.draw(g);
		}
		
		for (int i = 0; i < bullets.size(); i++) { // 对每一个子弹
			Bullets m = bullets.get(i);
			m.hitTanks(tanks); // 每一个子弹打到坦克上
			m.hitTank(homeTank); // 每一个子弹打到自己家的坦克上时
			m.hitHome(); // 每一个子弹打到家里是

			for (int j = 0; j < metalWall.size(); j++) { // 每一个子弹打到金属墙上
				MetalWall mw = metalWall.get(j);
				m.hitWall(mw);
				//new Thread(new hit()).start();
			}

			for (int j = 0; j < otherWall.size(); j++) {// 每一个子弹打到其他墙上
				CommonWall w = otherWall.get(j);
				m.hitWall(w);
				//new Thread(new hit()).start();
			}

			for (int j = 0; j < homeWall.size(); j++) {// 每一个子弹打到家的墙上
				CommonWall cw = homeWall.get(j);
				m.hitWall(cw);
				//new Thread(new hit()).start();
			}
			m.draw(g); // 画出效果图
		}
	}

	public TankGame() {
		// 创建菜单及菜单选项
		jmb = new MenuBar();
		jm1 = new Menu("游戏");
		jm2 = new Menu("暂停/继续");
		jm3 = new Menu("帮助");
		jm4 = new Menu("游戏难度");
		setFont(new Font("微软雅黑", Font.BOLD, 15));// 设置菜单显示的字体
		
		jmi1 = new MenuItem("开始新游戏");
		jmi2 = new MenuItem("退出");
		jmi3 = new MenuItem("暂停");
		jmi4 = new MenuItem("继续");
		jmi5 = new MenuItem("游戏方法");
		jmi6 = new MenuItem("简单");
		jmi7 = new MenuItem("中等");
		jmi8 = new MenuItem("困难");
		jmi9 = new MenuItem("关闭音效");
		jmi10 = new MenuItem("开启音效");

		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi9);
		jm1.add(jmi10);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jm3.add(jmi5);
		jm4.add(jmi6);
		jm4.add(jmi7);
		jm4.add(jmi8);
		
		jmb.add(jm1);
		jmb.add(jm2);
		jmb.add(jm3);
		jmb.add(jm4);
		
		jmi1.addActionListener(this);
		jmi1.setActionCommand("NewGame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("help");
		jmi6.addActionListener(this);
		jmi6.setActionCommand("level1");
		jmi7.addActionListener(this);
		jmi7.setActionCommand("level2");
		jmi8.addActionListener(this);
		jmi8.setActionCommand("level3");
		jmi9.addActionListener(this);
		jmi9.setActionCommand("stop_music");
		jmi10.addActionListener(this);
		jmi10.setActionCommand("start_music");

		this.setMenuBar(jmb);// 菜单Bar放到JFrame上
		this.setVisible(true);
		
		//布局地图
		for (int i = 0; i < 10; i++) { // 家的格局
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));
		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(220 + 20 * i, 300, this)); // 普通墙布局
				otherWall.add(new CommonWall(20 + 20 * i, 220, this));
				otherWall.add(new CommonWall(200, 400 + 12 * i, this));
				otherWall.add(new CommonWall(540, 400 + 12 * i, this));
			} else if (i < 32) {
				otherWall.add(new CommonWall(220 + 20 * (i - 16), 320, this));
				otherWall.add(new CommonWall(500 + 20 * (i - 16), 220, this));
				otherWall.add(new CommonWall(220, 400 + 12 * (i - 16), this));
				otherWall.add(new CommonWall(520, 400 + 12 * (i - 16), this));
			}
		}

		for (int i = 0; i < 10; i++) { // 金属墙布局
				metalWall.add(new MetalWall(200+50 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 17, this));
				metalWall.add(new MetalWall(150, 400 + 17, this));
		}

		for (int i = 0; i < 8; i++) { // 树的布局
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, this));
				trees.add(new Tree(220 + 30 * i, 360, this));
				trees.add(new Tree(440 + 30 * i, 360, this));
				trees.add(new Tree(660 + 30 * i, 360, this));
			}
			else
			{
				trees.add(new Tree(0 + 30*(i-4), 250, this));
				trees.add(new Tree(220 + 30*(i-4), 250, this));
				trees.add(new Tree(440 + 30*(i-4), 250, this));
				trees.add(new Tree(660 + 30*(i-4), 250, this));
			}
		}
		for (int i = 0; i < 4; i++)
		{
			trees.add(new Tree(0 + 20, 450+i*30, this));
			trees.add(new Tree(220 + 30, 450+i*30, this));
			trees.add(new Tree(0 + 750, 450+i*30, this));
			trees.add(new Tree(220 + 250, 450+i*30, this));
		}
		
		
		for (int i = 0; i < enemytank_count; i++) { // 初始化坦克
			if (i < 9) // 设置坦克出现的位置
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,this));
			else
 				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D,this));
		}
		
		for(int i=0;i<4;i++)//河流的布局
		{
			theRiver.add(new River(60*i, 90, this));
			theRiver.add(new River(60, 400+46*i, this));
		}

		this.setSize(Fram_width, Fram_length); // 设置界面大小
		this.setLocation(500, 250); // 设置界面出现的位置
		this.setTitle("坦克大战");
		this.addWindowListener(new WindowAdapter() { // 窗口监听关闭
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);//禁止改变窗口大小
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor());// 键盘监听
		new Thread(new PaintThread()).start(); // 线程启动
	}
	
	private class PaintThread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			while (printable) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
				if (tanks.size() == 0 && home.isLive() && homeTank.isLive()) {
					JOptionPane.showMessageDialog(tc,"游戏胜利!");
					break;	
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) { // 监听键盘释放
			homeTank.keyReleased(e);
		}
		public void keyPressed(KeyEvent e) { // 监听键盘按下
			homeTank.keyPressed(e);
		}
	}
	
	public void actionPerformed(ActionEvent e) {//接收操作事件的侦听器接口
		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要开始新游戏！", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				printable = true;
				this.dispose();
				new TankGame();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
		} else if (e.getActionCommand().endsWith("Stop")) {
			printable = false;
		} else if (e.getActionCommand().equals("Continue")) {
			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("退出");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
		} else if (e.getActionCommand().equals("help")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "用→ ← ↑ ↓控制方向，F键或空格键发射子弹，R重新开始！",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // 线程启动	
		} else if (e.getActionCommand().equals("level1")) {
			Tank.speedX = 6;
			Tank.speedY = 6;
			Bullets.speedX = 10;
			Bullets.speedY = 10;
			enemytank_count=8;
			this.dispose();
			new TankGame();
			if(TankGame.music_printable)
				new Thread(new add()).start();
		} else if (e.getActionCommand().equals("level2")) {
			Tank.speedX = 10;
			Tank.speedY = 10;
			Bullets.speedX = 12;
			Bullets.speedY = 12;
			enemytank_count=14;
			this.dispose();
			new TankGame();
			if(TankGame.music_printable)
				new Thread(new add()).start();
		} else if (e.getActionCommand().equals("level3")) {
			Tank.speedX = 14;
			Tank.speedY = 14;
			Bullets.speedX = 18;
			Bullets.speedY = 18;
			enemytank_count=20;
			this.dispose();
			new TankGame();
			if(TankGame.music_printable)
				new Thread(new add()).start();
		} 
		else if(e.getActionCommand().equals("stop_music")) {
			music_printable = false;
		}
		else if(e.getActionCommand().equals("start_music")) {
			music_printable = true;
		}	
		}
}

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
	public static final int Fram_width = 850; // ��̬ȫ�ִ��ڴ�С
	public static final int Fram_length = 650;
	public static boolean printable = true;	//���徲̬ȫ�ֱ��������߳�ֹͣ
	public static boolean music_printable = true;	//���徲̬ȫ�ֱ��������߳�ֹͣ
	static int enemytank_count=8;//��ʼ������̹������
	
	static start cStart=new start();
	Image screenImage = null;
	MenuBar jmb = null;//�˵�
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null;
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null,
			jmi6 = null, jmi7 = null, jmi8 = null, jmi9 = null,jmi10=null;
	
	GetBlood blood = new GetBlood(); // ʵ��������
	Home home = new Home(373, 545, this);// ʵ����home
	TankGame tc=null;
	Tank homeTank = new Tank(300, 560, true, Direction.STOP, this);// ʵ����̹��
	
	List<River> theRiver = new ArrayList<River>();// ʵ������������
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
		new TankGame(); // ʵ����
	}
	
	public void update(Graphics g) {
		screenImage = this.createImage(Fram_width, Fram_length);
		Graphics gps = screenImage.getGraphics();
		gps.setColor(Color.DARK_GRAY);
		gps.fillRect(0, 0, Fram_width, Fram_length-50);//����50�����������
		framPaint(gps);
		g.drawImage(screenImage, 0, 0, null);
	}

	public void framPaint(Graphics g) {//������ܽ��й���

		g.setColor(Color.red); // ����������ʾ����
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("ʣ��з�̹��: ", 120, 635);
		g.drawString("" + tanks.size(), 300,635);
		g.drawString("ʣ������ֵ: ", 480, 635);
		g.drawString("" + homeTank.getLife(), 620, 635);

		if (homeTank.isLive() == false) {
			Font f = g.getFont();
			g.setFont(new Font("΢���ź�", Font.BOLD, 40));
			tanks.clear();
			bullets.clear();
			g.setFont(f);
		}

		home.draw(g); // ����home
		homeTank.draw(g);// �����Լ��ҵ�̹��
		homeTank.eat(blood);// ����Ѫ--����ֵ
		
		for (int i = 0; i < theRiver.size(); i++) { // ��������
			River r = theRiver.get(i);
			r.draw(g);
		}
		for (int i = 0; i < theRiver.size(); i++) {
			River r = theRiver.get(i);
			homeTank.collideRiver(r);
			r.draw(g);
		}
		
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i); // ��ü�ֵ�Եļ�
			for (int j = 0; j < homeWall.size(); j++) {
				CommonWall cw = homeWall.get(j);
				t.collideWithWall(cw); // ÿһ��̹��ײ�������ǽʱ
				cw.draw(g);
			}
			for (int j = 0; j < otherWall.size(); j++) { // ÿһ��̹��ײ���������ǽ
				CommonWall cw = otherWall.get(j);
				t.collideWithWall(cw);
				cw.draw(g);
			}
			for (int j = 0; j < metalWall.size(); j++) { // ÿһ��̹��ײ������ǽ
				MetalWall mw = metalWall.get(j);
				t.collideWithWall(mw);
				mw.draw(g);
			}
			for (int j = 0; j < theRiver.size(); j++) {
				River r = theRiver.get(j); // ÿһ��̹��ײ������ʱ
				t.collideRiver(r);
				r.draw(g);
			}
			t.collideWithTanks(tanks); // ײ���Լ�����
			t.collideHome(home);
			t.draw(g);
		}

		blood.draw(g);//����Ѫ��

		for (int i = 0; i < trees.size(); i++) { // ����trees
			Tree tr = trees.get(i);
			tr.draw(g);
		}

		for (int i = 0; i < bombTanks.size(); i++) { // ������ըЧ��
			BombTank bt = bombTanks.get(i);
			bt.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) { // ����otherWall
			CommonWall cw = otherWall.get(i);
			cw.draw(g);
		}

		for (int i = 0; i < metalWall.size(); i++) { // ����metalWall
			MetalWall mw = metalWall.get(i);
			mw.draw(g);
		}

		homeTank.collideWithTanks(tanks);
		homeTank.collideHome(home);

		for (int i = 0; i < metalWall.size(); i++) {// ײ������ǽ
			MetalWall w = metalWall.get(i);
			homeTank.collideWithWall(w);
			w.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) {
			CommonWall cw = otherWall.get(i);
			homeTank.collideWithWall(cw);
			cw.draw(g);
		}

		for (int i = 0; i < homeWall.size(); i++) { // �����̹��ײ���Լ���
			CommonWall w = homeWall.get(i);
			homeTank.collideWithWall(w);
			w.draw(g);
		}
		
		for (int i = 0; i < bullets.size(); i++) { // ��ÿһ���ӵ�
			Bullets m = bullets.get(i);
			m.hitTanks(tanks); // ÿһ���ӵ���̹����
			m.hitTank(homeTank); // ÿһ���ӵ����Լ��ҵ�̹����ʱ
			m.hitHome(); // ÿһ���ӵ��򵽼�����

			for (int j = 0; j < metalWall.size(); j++) { // ÿһ���ӵ��򵽽���ǽ��
				MetalWall mw = metalWall.get(j);
				m.hitWall(mw);
				//new Thread(new hit()).start();
			}

			for (int j = 0; j < otherWall.size(); j++) {// ÿһ���ӵ�������ǽ��
				CommonWall w = otherWall.get(j);
				m.hitWall(w);
				//new Thread(new hit()).start();
			}

			for (int j = 0; j < homeWall.size(); j++) {// ÿһ���ӵ��򵽼ҵ�ǽ��
				CommonWall cw = homeWall.get(j);
				m.hitWall(cw);
				//new Thread(new hit()).start();
			}
			m.draw(g); // ����Ч��ͼ
		}
	}

	public TankGame() {
		// �����˵����˵�ѡ��
		jmb = new MenuBar();
		jm1 = new Menu("��Ϸ");
		jm2 = new Menu("��ͣ/����");
		jm3 = new Menu("����");
		jm4 = new Menu("��Ϸ�Ѷ�");
		setFont(new Font("΢���ź�", Font.BOLD, 15));// ���ò˵���ʾ������
		
		jmi1 = new MenuItem("��ʼ����Ϸ");
		jmi2 = new MenuItem("�˳�");
		jmi3 = new MenuItem("��ͣ");
		jmi4 = new MenuItem("����");
		jmi5 = new MenuItem("��Ϸ����");
		jmi6 = new MenuItem("��");
		jmi7 = new MenuItem("�е�");
		jmi8 = new MenuItem("����");
		jmi9 = new MenuItem("�ر���Ч");
		jmi10 = new MenuItem("������Ч");

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

		this.setMenuBar(jmb);// �˵�Bar�ŵ�JFrame��
		this.setVisible(true);
		
		//���ֵ�ͼ
		for (int i = 0; i < 10; i++) { // �ҵĸ��
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));
		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(220 + 20 * i, 300, this)); // ��ͨǽ����
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

		for (int i = 0; i < 10; i++) { // ����ǽ����
				metalWall.add(new MetalWall(200+50 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 17, this));
				metalWall.add(new MetalWall(150, 400 + 17, this));
		}

		for (int i = 0; i < 8; i++) { // ���Ĳ���
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
		
		
		for (int i = 0; i < enemytank_count; i++) { // ��ʼ��̹��
			if (i < 9) // ����̹�˳��ֵ�λ��
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,this));
			else
 				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D,this));
		}
		
		for(int i=0;i<4;i++)//�����Ĳ���
		{
			theRiver.add(new River(60*i, 90, this));
			theRiver.add(new River(60, 400+46*i, this));
		}

		this.setSize(Fram_width, Fram_length); // ���ý����С
		this.setLocation(500, 250); // ���ý�����ֵ�λ��
		this.setTitle("̹�˴�ս");
		this.addWindowListener(new WindowAdapter() { // ���ڼ����ر�
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);//��ֹ�ı䴰�ڴ�С
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor());// ���̼���
		new Thread(new PaintThread()).start(); // �߳�����
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
					JOptionPane.showMessageDialog(tc,"��Ϸʤ��!");
					break;	
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) { // ���������ͷ�
			homeTank.keyReleased(e);
		}
		public void keyPressed(KeyEvent e) { // �������̰���
			homeTank.keyPressed(e);
		}
	}
	
	public void actionPerformed(ActionEvent e) {//���ղ����¼����������ӿ�
		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ��ʼ����Ϸ��", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				printable = true;
				this.dispose();
				new TankGame();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}
		} else if (e.getActionCommand().endsWith("Stop")) {
			printable = false;
		} else if (e.getActionCommand().equals("Continue")) {
			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ�˳���", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("�˳�");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}
		} else if (e.getActionCommand().equals("help")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "�á� �� �� �����Ʒ���F����ո�������ӵ���R���¿�ʼ��",
					"��ʾ��", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // �߳�����	
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

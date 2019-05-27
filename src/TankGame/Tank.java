package TankGame;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank {
	
	public static  int speedX = 6, speedY =6; // ��̬ȫ�ֱ����ٶ�
	public static int count = 0;//��ʼ�о�̹������
	public static final int width = 35, length = 35; // ̹�˵�ȫ�ִ�С�����в��ɸı���
	private Direction direction = Direction.STOP; // ��ʼ��״̬Ϊ��ֹ
	private Direction Kdirection = Direction.U; // ��ʼ������Ϊ����
	TankGame tc;
	private boolean good;
	private int x, y;
	private int oldX, oldY;
	private boolean live = true; // ��ʼ��Ϊ����
	private int life = 200; // ��ʼ����ֵ

	private static Random r = new Random();
	private int step = r.nextInt(10)+5 ; // ����һ�������,���ģ��̹�˵��ƶ�·��

	private boolean bL = false, bU = false, bR = false, bD = false;

	private static Toolkit tk = Toolkit.getDefaultToolkit();// �������
	private static Image[] tankImags = null; // �洢ȫ�־�̬
	
	static {
		tankImags = new Image[] {
				tk.getImage(BombTank.class.getResource("/Images/tankD.gif")),
				tk.getImage(BombTank.class.getResource("/Images/tankU.gif")),
				tk.getImage(BombTank.class.getResource("/Images/tankL.gif")),
				tk.getImage(BombTank.class.getResource("/Images/tankR.gif")), };
	}

	public Tank(int x, int y, boolean good) {// Tank�Ĺ��캯��1
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankGame tc) {// Tank�Ĺ��캯��2
		this(x, y, good);
		this.direction = dir;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this); // ɾ����Ч��
			}
			return;
		}

		if (good)
			new DrawBloodbBar().draw(g); // ����һ��Ѫ��

		switch (Kdirection) {
		//���ݷ���ѡ��̹�˵�ͼƬ
		case D:
			g.drawImage(tankImags[0], x, y, null);
			break;
		case U:
			g.drawImage(tankImags[1], x, y, null);
			break;
		case L:
			g.drawImage(tankImags[2], x, y, null);
			break;
		case R:
			g.drawImage(tankImags[3], x, y, null);
			break;
		default:
			break;
		}
		move();   //����move����
	}

	void move() {

		this.oldX = x;
		this.oldY = y;

		switch (direction) {  //ѡ���ƶ�����
		case L:
			x -= speedX;
			break;
		case U:
			y -= speedY;
			break;
		case R:
			x += speedX;
			break;
		case D:
			y += speedY;
			break;
		case STOP:
			break;
		}

		if (this.direction != Direction.STOP) {
			this.Kdirection = this.direction;
		}

		if (x < 0)
			x = 0;
		if (y < 40)      //��ֹ�߳��涨����
			y = 40;
		if (x + Tank.width > TankGame.Fram_width)  //����������ָ����߽�
			x = TankGame.Fram_width - Tank.width;
		if (y + Tank.length > TankGame.Fram_length-50)
			y = TankGame.Fram_length-50 - Tank.length;

		if (!good) {
			Direction[] directons = Direction.values();
			if (step == 0) {                  
				step = r.nextInt(12) + 3;  //�������·��
				int rn = r.nextInt(directons.length);
				direction = directons[rn];      //�����������
			}
			step--;

			if (r.nextInt(40) >36)//��������������Ƶ��˿���
				this.fire();
		}
	}

	private void changToOldDir() {  
		x = oldX;
		y = oldY;
	}
	
	public void keyPressed(KeyEvent e) {  //���ܼ����¼�
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_R:  //������Rʱ�����¿�ʼ��Ϸ 
			new TankGame(); //���´������
			tc.dispose();//�ͷ��ɴ� Window�������������ӵ�е������������ʹ�õ����б�����Ļ��Դ��
			//����Щ Component ����Դ�����ƻ�������ʹ�õ������ڴ涼�����ص�����ϵͳ���������Ǳ��Ϊ������ʾ�� 
			break;
		case KeyEvent.VK_RIGHT: //�������Ҽ�
			bR = true;
			break;

		case KeyEvent.VK_LEFT://���������
			bL = true;
			break;

		case KeyEvent.VK_UP:  //�������ϼ�
			bU = true;
			break;

		case KeyEvent.VK_DOWN://�������¼�
			bD = true;
			break;
		}
		decideDirection();//���ú���ȷ���ƶ�����
	}

	void decideDirection() {
		if (bR)  //�����ƶ�
			direction = Direction.R;
		else if (bL)   //������
			direction = Direction.L;

		else if (bU)  //�����ƶ�
			direction = Direction.U;

		else if (bD) //�����ƶ�
			direction = Direction.D;
		else 
			direction = Direction.STOP;  //û�а������ͱ��ֲ���
	}
	
		public void keyReleased(KeyEvent e) {  //�����ͷż���
			int key = e.getKeyCode();
			switch (key) {
			
			case KeyEvent.VK_F://����
			case KeyEvent.VK_SPACE:
				if(TankGame.music_printable)
					new Thread(new fire()).start();
				fire();
				break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;

		case KeyEvent.VK_LEFT:
			bL = false;
			break;

		case KeyEvent.VK_UP:
			bU = false;
			break;

		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		decideDirection();  //�ͷż��̺�ȷ���ƶ�����
	}

	public Bullets fire() {  //���𷽷�
		if (!live)
			return null;
		int x = this.x + Tank.width / 2 - Bullets.width / 2;  //����λ��
		int y = this.y + Tank.length / 2 - Bullets.length / 2;
		Bullets m = new Bullets(x, y + 2, good, Kdirection, this.tc);  //û�и�������ʱ����ԭ���ķ��򷢻�
		tc.bullets.add(m);                                                
		return m;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public boolean collideWithWall(CommonWall w) {  //��ײ����ͨǽʱ
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir();    //ת����ԭ���ķ�����ȥ
			return true;
		}
		return false;
	}

	public boolean collideWithWall(MetalWall w) {  //ײ������ǽ
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir();     
			return true;
		}
		return false;
	}

	public boolean collideRiver(River r) {    //ײ��������ʱ��
		if (this.live && this.getRect().intersects(r.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideHome(Home h) {   //ײ���ҵ�ʱ��
		if (this.live && this.getRect().intersects(h.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideWithTanks(java.util.List<Tank> tanks) {//ײ��̹��ʱ
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.isLive()
						&& this.getRect().intersects(t.getRect())) {
					this.changToOldDir();
					t.changToOldDir();
					return true;
				}
			}
		}
		return false;
	}


	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private class DrawBloodbBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(375, 585, width, 10);
			int w = width * life / 200;
			g.fillRect(375, 585, w, 10);
			g.setColor(c);
		}
	}

	public boolean eat(GetBlood b) {
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			if(this.life<=100)
				this.life = this.life+50;      //ÿ��һ��������50������
			else
				this.life = 200;

			b.setLive(false);
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
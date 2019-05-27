package TankGame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class BombTank {

	private int x, y;
	private boolean live = true; // ��ʼ״̬Ϊ���ŵ�
	private TankGame tc;
	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] imgs = { // �洢��ըͼƬ ��ըЧ��ͼ
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/1.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/2.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/3.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/4.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/5.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/6.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/7.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/8.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/9.png")),
			tk.getImage(BombTank.class.getClassLoader().getResource(
					"Images/10.png")),
				};
	int step = 0;

	public BombTank(int x, int y, TankGame tc) { // ���캯��
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) { // ������ըͼ��

		if (!live) { // ̹����ʧ��ɾ����ըͼ
			tc.bombTanks.remove(this);
			return;
		}
		if (step == imgs.length) {
			live = false;
			step = 0;
			return;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
		if(TankGame.music_printable)
			new Thread(new blast()).start();
	}
}

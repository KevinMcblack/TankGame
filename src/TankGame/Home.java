package TankGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Home {
	private int x, y;
	private TankGame tc;
	public static final int width = 30, length = 30; // 全局静态变量长宽
	private boolean live = true;

	private static Toolkit tk = Toolkit.getDefaultToolkit(); // 全局静态变量
	private static Image[] homeImags = null;
	static {
		homeImags = new Image[] { tk.getImage(CommonWall.class
				.getResource("/Images/home.jpg")) };
	}

	public Home(int x, int y, TankGame tc) {// 构造函数，传递Home的参数并赋值
		this.x = x;
		this.y = y;
		this.tc = tc; // 获得控制
	}

	public void gameOver(Graphics g) {

		tc.tanks.clear();// 作清理页面工作
		tc.metalWall.clear();
		tc.otherWall.clear();
		tc.bombTanks.clear();
		tc.theRiver.clear();
		tc.trees.clear();
		tc.bullets.clear();
		tc.homeWall.clear();
	
		tc.homeTank.setLive(false);
		Color c = g.getColor(); // 设置参数
		g.setColor(Color.green);
		Font f = g.getFont();
		g.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		g.drawString("你输了！", 220, 250);
		g.drawString("  游戏结束！ ", 220, 300);
		g.setFont(f);
		g.setColor(c);

	}

	public void draw(Graphics g) {

		if (live) { // 如果活着，则画出home
			g.drawImage(homeImags[0], x, y, null);
			for (int i = 0; i < tc.homeWall.size(); i++) {
				CommonWall w = tc.homeWall.get(i);
				w.draw(g);
			}
		} else {
			gameOver(g); // 调用游戏结束
		}
	}

	public boolean isLive() { // 判读是否还活着
		return live;
	}

	public void setLive(boolean live) { // 设置生命
		this.live = live;
	}

	public Rectangle getRect() { // 返回长方形实例
		return new Rectangle(x, y, width, length);
	}

}

package TankGame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;


public class Music {

	public void music() {
		
	}
}
class start extends Thread{
	private AudioClip startAudio;
	URL url = this.getClass().getResource("/music/start.wav");
	@Override
	public void run() {
		synchronized(this) {
		startAudio = Applet.newAudioClip(url);
		startAudio.play();}
	}
}
class hit extends Thread{
	private AudioClip startAudio;
	URL url = this.getClass().getResource("/music/hit.wav");
	@Override
	public synchronized void run() {
		startAudio = Applet.newAudioClip(url);
		startAudio.play();
	}
}
class fire extends Thread{
	private AudioClip startAudio;
	URL url = this.getClass().getResource("/music/fire.wav");
	@Override
	public void run() {
		synchronized(this) {
			startAudio = Applet.newAudioClip(url);
			startAudio.play();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
class blast extends Thread{
	private AudioClip startAudio;
	URL url = this.getClass().getResource("/music/blast.wav");
	@Override
	public void run() {
		startAudio = Applet.newAudioClip(url);
		synchronized(this)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			startAudio.play();}
	}	
}
class add extends Thread{
	private AudioClip startAudio;
	URL url = this.getClass().getResource("/music/add.wav");
	@Override
	public void run() {
		startAudio = Applet.newAudioClip(url);
		synchronized(this)
		{startAudio.play();}
	}	
}

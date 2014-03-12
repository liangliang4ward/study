import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ProgressMonitor;
import javax.swing.Timer;

public class TestProgressMonitor
{
	Timer timer;
	public void init()
	{
		final SimulatedTarget target = new SimulatedTarget(1000);
		//以启动一条线程的方式来执行一个耗时的任务
		final Thread targetThread = new Thread(target);
		targetThread.start();
		//创建进度对话框
		final ProgressMonitor dialog = new ProgressMonitor(null ,
			"等待任务完成" , "已完成：" , 0 , target.getAmount());
		//创建一个计时器
		timer = new Timer(300 , new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//以任务的当前完成量设置进度对话框的完成比例
				dialog.setProgress(target.getCurrent());
				//如果用户单击了进度对话框的”取消“按钮
				if (dialog.isCanceled())
				{
					//停止计时器
					timer.stop();
					//中断任务的执行线程
					targetThread.interrupt();
					//系统退出
					System.exit(0);
				}
			}
		});
		timer.start();
	}
	public static void main(String[] args) 
	{
		new TestProgressMonitor().init();
	}
}

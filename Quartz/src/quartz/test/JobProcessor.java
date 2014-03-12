package quartz.test;

import java.text.SimpleDateFormat;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobProcessor implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String name=(String) context.getJobDetail().getJobDataMap().get("name");
		String age=(String) context.getJobDetail().getJobDataMap().get("age");
		String level=(String) context.getJobDetail().getJobDataMap().get("level");
		String pos=(String) context.getJobDetail().getJobDataMap().get("pos");
		System.out.println("name:"+name+",age:"+age+",level:"+level+",pos:"+pos);

		SimpleDateFormat sdf=(SimpleDateFormat) context.getJobDetail().getJobDataMap().get("format");
		System.out.println("nextTime:"+sdf.format(context.getNextFireTime()));
	}
	
}
